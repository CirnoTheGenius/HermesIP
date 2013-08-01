package com.tenko.functions;

import com.google.common.io.Files;
import com.tenko.FriendlyWall;
import com.tenko.asm.builders.EntityMikaClassBuilder;
import com.tenko.asm.entity.IMika;
import com.tenko.nms.NMSLib;
import com.tenko.objs.TenkoCmd;
import com.tenko.visualnovel.Conversation;
import com.tenko.visualnovel.Option;
import com.tenko.visualnovel.OptionRunnable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class NPCs extends Function {

	boolean isConfirming;

	private static HashMap<String, IMika> NPCList = new HashMap<String, IMika>();
	private final EntityMikaClassBuilder getter;

	//1.5.2: aA
	//1.6.2: aP
	public static String field = "aP";


	public NPCs(){
		getter = new EntityMikaClassBuilder();
		File f = new File(getFunctionDirectory("NPCs"), "npcs.dat");
		try {
			parseNPCSpawnFile(f);
		} catch (IOException e){
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e1){
					e1.printStackTrace();
				}
			}
		}
	}

	//Ugliest class I've written so far.
	@Override
	public boolean onCommand(final CommandSender cs, Command c, String thisisnevergoingtobeused, String[] args){
		if(cs.isOp()){
			if(c.getName().equals("newnpc")){
				try {
					if(args.length > 0){
						if(NPCList.containsKey(args[0])){
							cs.sendMessage(ChatColor.RED + "NPC " + args[0] + " already exists!");
							return true;
						} else if(args[0].length() > 16){
							cs.sendMessage(ChatColor.RED + "That name is way too long!");
							return true;
						}

						createNewNpc(args[0], ((Player)cs).getLocation());
						cs.sendMessage(ChatColor.BLUE + "Spawned NPC");
					} else {
						cs.sendMessage(ChatColor.RED + "Provide a name for the new NPC!");
					}
				} catch (ClassCastException e){
					cs.sendMessage(ChatColor.RED + "You must be a player!");
				} catch (Exception e){
					cs.sendMessage(ChatColor.RED + "Failed with " + e);
					e.printStackTrace();
				}

				return true;
			} else if(c.getName().equalsIgnoreCase("telenpc")){
				try {
					NPCList.get(args[0]).teleportTo(((Player)cs).getLocation());
				} catch (ClassCastException e){
					cs.sendMessage(ChatColor.RED + "You must be a player!");
				} catch (NullPointerException e){
					cs.sendMessage(ChatColor.RED + "That NPC doesn't appear to exist.");
				}
			} else if(c.getName().equalsIgnoreCase("removenpc")){
				if(args.length > 0){
					NPCList.get(args[0]).die();
					NPCList.remove(args[0]);
					cs.sendMessage(ChatColor.BLUE + "Removed NPC " + args[0]);
				} else {
					cs.sendMessage(ChatColor.RED + "Provide a name for the new NPC!");
				}
				return true;
			} else if(c.getName().equalsIgnoreCase("removeallnpcs")){
				cs.sendMessage(ChatColor.RED + "Are you sure!? (Type \"removeallnpcsyes\", otherwise, this command will timeout in 10 seconds.)");
				final TenkoCmd cmd = FriendlyWall.getRegister().registerCommand("removeallnpcsyes", new CommandExecutor(){
					@Override
					public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
						killAllNPCs();
						die();
						return true;
					}
				});

				Bukkit.getScheduler().scheduleSyncDelayedTask(FriendlyWall.getPlugin(), new Runnable(){
					@Override
					public void run(){
						if(isConfirming){
							cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - NPCs] Didn't answer in 10 seconds. Assuming \"no\".");
						}
						FriendlyWall.getRegister().unregisterCommand(cmd);
						isConfirming = false;
					}
				}, 200L);
			} else if(c.getName().equalsIgnoreCase("diemonster")){
				if(args.length > 0){
					double bounds = Double.valueOf(args[0]);
					int i = 0;
					for(Entity e : ((Player)cs).getNearbyEntities(bounds, bounds, bounds)){
						if(e.getType().equals(EntityType.PLAYER)){
							//Why compare UUIDs of a player that's offline?
							if(Bukkit.getPlayerExact(((Player)e).getName()) == null){
								e.remove();
								remove(((Player)e).getName());
								i++;
								continue;
							}
						}
						Iterator<Entry<String, IMika>> it = NPCList.entrySet().iterator();
						while(it.hasNext()){
							Entry<String, IMika> entry = it.next();
							if(entry.getValue().getCraftEntity().getUniqueId() == e.getUniqueId()){
								entry.getValue().die();
								remove(entry.getValue().getName());
								it.remove();
								i++;
							}
						}
					}
					cs.sendMessage(ChatColor.BLUE + "Removed " + i + " NPC(s).");
				} else {
					cs.sendMessage(ChatColor.RED + "Provide a number for the search size!");
				}
				return true;
			} else if(c.getName().equalsIgnoreCase("vnstyle")){
				if(args.length > 1){
					IMika em = NPCList.get(args[0]);

					if(em == null){
						cs.sendMessage(ChatColor.RED + "A NPC was not found by that name.");
						return true;
					}

					if(args[1].equalsIgnoreCase("add")){
						if(args.length <= 2){
							cs.sendMessage(ChatColor.RED + "Provide what the option says please.");
							return true;
						}

						StringBuffer sb = new StringBuffer();
						for(int i=2; i < args.length; i++){
							sb.append(args[i]);
							sb.append(' ');
						}
						em.getOptions().add(new Option(em.getOptions().size(), sb.toString(), null));
					} else if(args[1].equalsIgnoreCase("set")){
						try {
							if(args.length > 3){
								int pos = Integer.parseInt(args[2]);
								Option o = em.getOptions().get(pos);

								if(args[3].equalsIgnoreCase("talk")){
									final StringBuffer sb = new StringBuffer();
									for(int i=4; i < args.length; i++){
										sb.append(args[i]);
										sb.append(' ');
									}

									em.getOptions().set(pos, new Option(o.getOptionNum(), o.getText().trim(), new OptionRunnable(((Player)cs), em, OptionRunnable.Intention.TALK, "chat(" + sb.toString().trim() + ")"){
										@Override
										public void run(){
											getEntity().chat(getPlayer(), sb.toString().trim());
										}
									}));
									cs.sendMessage(ChatColor.BLUE + "Set option!");
								}
							} else {
								cs.sendMessage(ChatColor.RED + "What did you want to do?");
							}
						} catch (NumberFormatException e){
							cs.sendMessage(ChatColor.RED + "Provide a number, not a word or spam.");
						} catch (IndexOutOfBoundsException e){
							cs.sendMessage(ChatColor.RED + "That doesn't appear to be an option.");
						}
					} else if(args[1].equalsIgnoreCase("list")){
						cs.sendMessage(ChatColor.BLUE + "Current actions for " + em.getName() + ":");

						for(Option o : em.getOptions()){
							if(o.getRunnable() == null){
								cs.sendMessage(ChatColor.GOLD + String.valueOf(o.getOptionNum()) + ChatColor.BLUE + ":" + ChatColor.GOLD + o.getText());
								cs.sendMessage(ChatColor.RED + "The option above does not do anything when selected.");
							} else {
								cs.sendMessage(ChatColor.GOLD + String.valueOf(o.getOptionNum()) + ChatColor.BLUE + ":" + ChatColor.GOLD + o.getText() + ":" + o.getRunnable().getIntention().toString() + ChatColor.BLUE + ":" + ChatColor.GOLD + o.getRunnable().getDescription());
							}
						}
					} else if(args[1].equalsIgnoreCase("forcequitall")){
						for(Player p : Bukkit.getOnlinePlayers()){
							try {
								((Conversation)p.getMetadata("conversationNpc").get(0).value()).endConversation();
								p.removeMetadata("conversationNpc", FriendlyWall.getPlugin());
							} catch (Exception e){}
						}
						cs.sendMessage(ChatColor.BLUE + "Attempted to force all metadata to stop.");
					}
				} else {
					cs.sendMessage(ChatColor.RED + "Uh, what are you exactly trying to do? It's used like this:");
					cs.sendMessage(ChatColor.RED + "/vnstyle <npcName> <add|set|list|forcequitall> [message|optionnum] [talk] [message]");
				}
			} else if(c.getName().equalsIgnoreCase("npcquotes")){
				if(args.length > 2){
					if(args[0].equalsIgnoreCase("add")){
						try {
							StringBuffer sb = new StringBuffer();
							for(int i=2; i < args.length; i++){
								sb.append(args[i]);
								sb.append(' ');
							}
							NPCList.get(args[1]).getQuotes().add(sb.toString().trim());
							addToYaml(args[1], "NPCQuoteData", sb.toString().trim());
							cs.sendMessage(ChatColor.BLUE + "Added quote!");
						} catch (NullPointerException e){
							cs.sendMessage(ChatColor.RED + "That NPC doesn't exist!");
						}
					} else if(args[0].equalsIgnoreCase("rem")){
						try {
							NPCList.get(args[1]).getQuotes().remove((int)Integer.valueOf(args[2]));
							removeFromYaml(args[1], "NPCQuoteData", Integer.valueOf(args[2]));
							cs.sendMessage(ChatColor.BLUE + "Removed quote!");
						} catch (NullPointerException e){
							cs.sendMessage(ChatColor.RED + "That NPC doesn't exist!");
						} catch (NumberFormatException e){
							cs.sendMessage(ChatColor.RED + "That needs to be a number!");
						}
					} else if(args[0].equalsIgnoreCase("list")){
						try {
							IMika em = NPCList.get(args[0]);
							cs.sendMessage(ChatColor.BLUE + "Current quotes for " + em.getName());
							int i=0;
							for(String s : em.getQuotes()){
								cs.sendMessage(ChatColor.BLUE + (i + ": " + s));
								i++;
							}
						} catch (NullPointerException e){
							cs.sendMessage(ChatColor.RED + "That NPC doesn't exist!");
						} 
					}
				}
			}
		}
		return false;
	}

	public static HashMap<String, IMika> getNPCList(){
		return NPCList;
	}

	public void parseNPCSpawnFile(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String l;
		while ((l = br.readLine()) != null){
			try { 
				String[] stuffs = l.split(";");
				String name = stuffs[0];
				String world = stuffs[1];
				double x = Double.valueOf(stuffs[2]).doubleValue();
				double y = Double.valueOf(stuffs[3]).doubleValue();
				double z = Double.valueOf(stuffs[4]).doubleValue();

				Location temp = new Location(Bukkit.getWorld(world), x, y, z);
				IMika i = createNewNpc(name, temp, false);
				i.getQuotes().addAll(getList(name, "NPCQuoteData"));
			} catch (ArrayIndexOutOfBoundsException e){
				System.out.println("Failed to parse file!");
			}
		}
		br.close();
	}

	public void createNewNpc(String name, Location l){
		createNewNpc(name, l, true);
	}

	public IMika createNewNpc(String name, Location l, boolean writeToFile){
		IMika t = (IMika)getter.generateInstance(NMSLib.getNMSServer(), NMSLib.getNMSWorld(l.getWorld()), name);
		t.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		t.spawnIn(NMSLib.getNMSWorld(l.getWorld()));

		if(writeToFile){
			this.writeToFile(name, l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
		}

		return t;
	}
	
	public void writeToFile(String name, String world, double x, double y, double z){
		File dir = new File(getFunctionDirectory("NPCs"), "NPCVNData");
		if(!dir.exists()){
			dir.mkdir();
		}
		
		try {
			File f = new File(dir, name + ".dat");
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(name + ";" + world + ";" + x + ";" + y + ";" + z);
			bw.flush();
			bw.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void remove(String name){
		try {
			List<String> lines = Files.readLines(new File(getFunctionDirectory("NPCs"), "npcs.dat"), Charset.defaultCharset());
			File f = new File(getFunctionDirectory("NPCs"), "npcs.dat");
			BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
			for (String l : lines){
				if(!l.startsWith(name + ";")){
					bw.write(l);
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void addToYaml(String npc, String listName, String text){
		File dir = new File(getFunctionDirectory("NPCs"), "NPCData");
		if(!dir.exists()){
			dir.mkdir();
		}

		try {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(dir, npc + ".yml"));
			List<String> list = config.getStringList(listName);
			list.add(text);
			config.set("Quotes", list);
			config.save(new File(dir, npc + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getList(String npc, String listName){
		File dir = new File(getFunctionDirectory("NPCs"), "NPCData");
		if(!dir.exists()){
			dir.mkdir();
		}

		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(dir, npc + ".yml"));
		return config.getStringList(listName);
	}

	public void removeFromYaml(String npc, String listName, int quotePos){
		File dir = new File(getFunctionDirectory("NPCs"), "NPCData");
		if(!dir.exists()){
			dir.mkdir();
		}

		try {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(dir, npc + ".yml"));
			List<String> list = config.getStringList(listName);
			list.remove(quotePos);
			config.set("Quotes", list);
			config.save(new File(dir, npc + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void die(){
		try {
			new File(getFunctionDirectory("NPCs"), "npcs.dat").delete();
			new File(getFunctionDirectory("NPCs"), "npcs.dat").createNewFile();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public static void killAllNPCs(){
		for(Entry<String, IMika> e : NPCList.entrySet()){
			e.getValue().die();
			NPCList.remove(e.getKey());
		}
	}

	@EventHandler
	public void inb4Clannad(PlayerInteractEntityEvent e){
		Entity entity = e.getRightClicked();
		if(entity.getType() == EntityType.PLAYER){
			if(NPCList.containsKey(((Player)entity).getName())){
				IMika mika = NPCList.get(((Player)entity).getName());
				if(mika.getOptions().size() > 0){
					if(mika.isTalking()){
						e.getPlayer().sendMessage("That person is already talking to someone.");
						return;
					}

					new Conversation(e.getPlayer(), mika);
					mika.lookAt(e.getPlayer().getEyeLocation());
				}
			}
		}
	}

	@EventHandler
	public void talkingLikeVN(AsyncPlayerChatEvent e){
		if(e.getMessage().length() == 1){
			if(e.getPlayer().hasMetadata("conversationNpc")){
				Conversation conv = (Conversation)e.getPlayer().getMetadata("conversationNpc").get(0).value();

				for(Option o : conv.npc.getOptions()){
					if(e.getMessage().equals(String.valueOf(o.getOptionNum()))){
						o.answered();
						conv.npc.setTalking(false);
						e.getPlayer().removeMetadata("conversationNpc", FriendlyWall.getPlugin());
						e.setCancelled(true);
						return;
					}
				}

				conv.npc.chat(e.getPlayer(), "I didn't quite get that.");
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void iSeeDeadPeople(PlayerMoveEvent e){
		for(Entry<String, IMika> npc : NPCList.entrySet()){
			Player plyr = e.getPlayer();
			IMika mika = npc.getValue();

			if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()){
				//No, sempai won't notice you.
				if(mika.isTalking()){
					return;
				}

				if(plyr.getLocation().distanceSquared(npc.getValue().getCraftBukkitLocation()) < 16){	
					mika.lookAt(plyr.getEyeLocation());

					if(plyr.hasMetadata("hasTalkedTo" + mika.getName())){
						continue;
					}

					plyr.setMetadata("hasTalkedTo" + mika.getName(), new FixedMetadataValue(FriendlyWall.getPlugin(), true));
					mika.chat(plyr);
				} else {
					if(plyr.hasMetadata("hasTalkedTo" + mika.getName())){
						plyr.removeMetadata("hasTalkedTo" + mika.getName(), FriendlyWall.getPlugin());
					}
				}
			}
		}
	}
}