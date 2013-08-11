package com.tenko.functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.google.common.io.Files;
import com.tenko.FriendlyWall;
import com.tenko.NovEngine.Conversation;
import com.tenko.NovEngine.optiontypes.IncompleteResponseOption;
import com.tenko.NovEngine.optiontypes.Option;
import com.tenko.NovEngine.optiontypes.ResponseOption;
import com.tenko.asm.EntityMikaClassBuilder;
import com.tenko.asm.IMika;
import com.tenko.nms.NMSLib;
import com.tenko.objs.TenkoCmd;

public class NPCsV2 extends Function {

	//1.5.2: aA
	//1.6.2: aP
	public static String HeadRotationField = "aP";
	public static boolean shouldListen = true;
	private final HashMap<String, IMika> npcMap = new HashMap<String, IMika>();
	private final HashMap<String, IncompleteResponseOption> confirmingPlayers = new HashMap<String, IncompleteResponseOption>();
	private final EntityMikaClassBuilder classBuilder;
	private boolean isConfirming = false;

	public enum State {
		INPUTTING_QUESTION, INPUTTING_RESULT
	}

	public NPCsV2(){
		super();

		classBuilder = new EntityMikaClassBuilder();
		parseNPCDataFile(new File(getFunctionDirectory("NPCs"), "npcs.dat"));
	}

	@Override
	public boolean onCommand(final CommandSender cs, Command c, String l, String[] args){
		if(cs.isOp()){
			//Creation of NPC's//
			if(c.getName().equalsIgnoreCase("newnpc")){
				if(args.length > 0 && cs instanceof Player){
					if(npcMap.containsKey(args[0])){
						cs.sendMessage(ChatColor.RED + "NPC " + args[0] + " already exists!");
						return true;
					} else if(args[0].length() > 16){
						cs.sendMessage(ChatColor.RED + "That name is way too long!");
						return true;
					}

					createNewNpc(args[0], ((Player)cs).getLocation(), true);
					cs.sendMessage(ChatColor.BLUE + "Spawned NPC");
					return true;
				} else {
					cs.sendMessage(ChatColor.RED + "Expected a NPC name from a player. Check the command and make sure you're not console?");
					return false;
				}
			} else if(c.getName().equalsIgnoreCase("removenpc")){
				if(args.length > 0){
					npcMap.get(args[0]).die();
					npcMap.remove(args[0]);
					cs.sendMessage(ChatColor.BLUE + "Removed NPC " + args[0]);
				} else {
					cs.sendMessage(ChatColor.RED + "Provide a name for the new NPC!");
				}
				return true;
			} else if(c.getName().equalsIgnoreCase("diemonster")){
				if(args.length > 0){
					double bounds = Double.valueOf(args[0]);
					int i = 0;
					for(Entity e : ((Player)cs).getNearbyEntities(bounds, bounds, bounds)){
						if(e.getType().equals(EntityType.PLAYER)){
							String name = ((Player)e).getName();
							//Why compare UUIDs of a player that's offline?
							if(Bukkit.getPlayerExact(name) == null || (npcMap.containsKey(name) && e.getUniqueId() == npcMap.get(name).getCraftEntity().getUniqueId())){
								e.remove();
								npcMap.remove(name);
								removeDataFromDat(name);
								i++;
							}
						}
					}
					cs.sendMessage(ChatColor.BLUE + "Removed " + i + " NPC(s).");
					return true;
				} else {
					cs.sendMessage(ChatColor.RED + "Provide a number for the search size!");
					return true;
				}
			} else if(c.getName().equalsIgnoreCase("removeallnpcs")){
				cs.sendMessage(ChatColor.RED + "Are you sure!? (Type \"removeallnpcsyes\", otherwise, this command will timeout in 10 seconds.)");
				final TenkoCmd cmd = FriendlyWall.getRegister().registerCommand("removeallnpcsyes", new CommandExecutor(){
					@Override
					public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
						if(isConfirming){
							isConfirming = false;
							resetAllData();
							npcMap.clear();
							cs.sendMessage(ChatColor.RED + "Deleted all data.");
						}
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

				return true;
			}

			//Make NPC do something.
			if(c.getName().equalsIgnoreCase("telenpc")){
				try {
					npcMap.get(args[0]).teleportTo(((Player)cs).getLocation());
				} catch (ClassCastException e){
					cs.sendMessage(ChatColor.RED + "You must be a player!");
				} catch (NullPointerException e){
					cs.sendMessage(ChatColor.RED + "That NPC doesn't appear to exist.");
				}

				return true;
			}

			//Quote manipulation.
			if(c.getName().equalsIgnoreCase("npcquotes")){
				if(args.length > 1){
					IMika em = npcMap.get(args[1]);

					if(em == null){
						cs.sendMessage(ChatColor.RED + "That NPC doesn't exist!");
						return true;
					}

					if(args[0].equalsIgnoreCase("add")){
						String quote = StringUtils.join(args, " ", 2, args.length);
						em.getQuotes().add(quote);
						addQuote(args[1], quote);
						cs.sendMessage(ChatColor.BLUE + "Added quote!");
						return true;
					} else if(args[0].equalsIgnoreCase("rem")){
						int pos = Integer.valueOf(args[2]).intValue();
						em.getQuotes().remove(pos);
						removeQuote(args[1], pos);
						cs.sendMessage(ChatColor.BLUE + "Removed quote!");
						return true;
					} else if(args[0].equalsIgnoreCase("list")){
						cs.sendMessage(ChatColor.BLUE + "Current quotes for " + em.getName());
						for(int i=0; i < em.getQuotes().size(); i++){
							cs.sendMessage(ChatColor.BLUE + (i + ": " + em.getQuotes().get(i)));
						}
						return true;
					}
				} else {
					cs.sendMessage(ChatColor.RED + "Incorrect usage.");
					cs.sendMessage(ChatColor.RED + "/npcquotes <add|rem|list> <npcName> [message or number to remove]");
				}
				return true;
			}

			if(c.getName().equalsIgnoreCase("vnstyle")){
				if(args.length > 1){
					IMika em = npcMap.get(args[1]);

					if(em == null){
						cs.sendMessage(ChatColor.RED + "That NPC doesn't exist!");
						return true;
					}

					if(args[0].equalsIgnoreCase("add")){
						cs.sendMessage(ChatColor.BLUE + "Chat your option statement.");
						confirmingPlayers.put(cs.getName(), new IncompleteResponseOption(State.INPUTTING_QUESTION, em));
						return true;
					} else if(args[0].equalsIgnoreCase("rem")){
						int pos = Integer.valueOf(args[2]).intValue();
						em.getOptions().remove(pos);
						return true;
					} else if(args[0].equalsIgnoreCase("list")){
						cs.sendMessage(ChatColor.BLUE + "Current options for " + em.getName());
						for(int i=0; i < em.getOptions().size(); i++){
							Option o = em.getOptions().get(i);
							cs.sendMessage(ChatColor.BLUE + (i + ": " + o.getQuestion()));
							if(o instanceof ResponseOption){
								cs.sendMessage(ChatColor.BLUE + "-" + ((ResponseOption)o).getAnswer());
							}
						}
						return true;
					}
				} else {
					cs.sendMessage(ChatColor.RED + "Incorrect usage.");
					cs.sendMessage(ChatColor.RED + "/vnstyle <add|rem|list> <npcName> [valueToRemove]");
				}
			}
		}
		return false;
	}

	/*=============================Parse Stored NPC Data=============================*/

	public void parseNPCDataFile(File f){
		String l;
		try(BufferedReader br = new BufferedReader(new FileReader(f))){
			String name, world;
			double x, y, z;
			while((l = br.readLine()) != null){
				try {
					String[] data = l.split(";");
					name = data[0];
					world = data[1];
					x = Double.valueOf(data[2]);
					y = Double.valueOf(data[3]);
					z = Double.valueOf(data[4]);

					createNewNpc(name, new Location(Bukkit.getWorld(world), x, y, z), false);
				} catch (Exception e){
					System.out.println("Bad data; line \"" + l + "\" appears to be invalid.");
					continue;
				}
			}
		} catch (IOException e){
			try {
				f.createNewFile();
			} catch (Exception q){
				q.printStackTrace();
			}
		}
	}

	/*==============================Creation of NPC's=============================*/

	public IMika createNewNpc(String name, Location l, boolean writeToFile){
		IMika t = (IMika)classBuilder.generateInstance(NMSLib.getNMSServer(), NMSLib.getNMSWorld(l.getWorld()), name);
		t.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		t.spawnIn(NMSLib.getNMSWorld(l.getWorld()));
		npcMap.put(name, t);

		if(writeToFile){
			this.storeDataToDat(name, l.getWorld().getName(), l.getX(), l.getY(), l.getZ());
		}

		return t;
	}

	/*=============================Change NPC's Data=============================*/

	public void storeDataToDat(String s, String w, double x, double y, double z){
		File f = new File(getFunctionDirectory("NPCs"), "npcs.dat");
		String lineToWrite = StringUtils.join(new Object[]{s, w, x, y, z}, ";");

		if(f.exists()){
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(f, true))){
				bw.newLine();
				bw.write(lineToWrite);
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	public void removeDataFromDat(String name){
		try {
			List<String> lines = Files.readLines(new File(getFunctionDirectory("NPCs"), "npcs.dat"), Charset.defaultCharset());
			File f = new File(getFunctionDirectory("NPCs"), "npcs.dat");
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(f, false))){
				for (String l : lines){
					if(!l.startsWith(name + ";")){
						bw.write(l);
					}
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/*============================NPC Quote Methods============================*/
	public void addQuote(String npcName, String quote){
		File dir = new File(getFunctionDirectory("NPCs"), "Quotes");
		if(!dir.exists()){
			dir.mkdir();
		}

		try {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(dir, npcName + ".yml"));
			List<String> list = config.getStringList("Quotes");
			list.add(quote);
			config.set("Quotes", list);
			config.save(new File(dir, npcName + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeQuote(String npcName, int quotePos){
		File dir = new File(getFunctionDirectory("NPCs"), "Quotes");
		if(!dir.exists()){
			dir.mkdir();
		}

		try {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(dir, npcName + ".yml"));
			List<String> list = config.getStringList("Quotes");
			list.remove(quotePos);
			config.set("Quotes", list);
			config.save(new File(dir, npcName + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getQuotes(String npcName){
		File dir = new File(getFunctionDirectory("NPCs"), "Quotes");
		if(!dir.exists()){
			dir.mkdir();
		}

		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(dir, npcName + ".yml"));
		return config.getStringList("Quotes");
	}

	/*============================Listen for player movement============================*/
	//ohgodohgodohgod. i'm listening for move events. have mercy on the server.
	@EventHandler
	public void playerMovement(final PlayerMoveEvent e){
		try {
			if(shouldListen){
				if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()){
					return;
				}

				for(final Entry<String, IMika> npc : npcMap.entrySet()){
					Bukkit.getScheduler().scheduleSyncDelayedTask(FriendlyWall.getPlugin(), new Runnable(){
						@Override
						public void run(){
							try {
								Player plyr = e.getPlayer();
								IMika mika = npc.getValue();

								if(plyr.getWorld() == mika.getCraftBukkitLocation().getWorld()){
									if(plyr.getLocation().distanceSquared(npc.getValue().getCraftBukkitLocation()) < 9){
										mika.lookAt(plyr.getEyeLocation());
										if(plyr.hasMetadata("hasTalkedTo" + mika.getName())){
											return;
										}

										plyr.setMetadata("hasTalkedTo" + mika.getName(), new FixedMetadataValue(FriendlyWall.getPlugin(), true));
										mika.chat(plyr);
									} else {
										if(plyr.hasMetadata("hasTalkedTo" + mika.getName())){
											plyr.removeMetadata("hasTalkedTo" + mika.getName(), FriendlyWall.getPlugin());
										}
									}			
								}
							} catch (Exception ez){
								FriendlyWall.exceptionCount++;
							}
						}
					});
				}
			}
		} catch (Exception z){
			FriendlyWall.exceptionCount++;
		}
	}

	/*============================VN Engine; don't know what to name it.============================*/
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent e){
		if(confirmingPlayers.containsKey(e.getPlayer().getName())){
			IncompleteResponseOption iro = confirmingPlayers.get(e.getPlayer().getName());
			switch(iro.getState()){
				case INPUTTING_QUESTION:{
					iro.setQuestion(e.getMessage());
					iro.setState(State.INPUTTING_RESULT);
					e.getPlayer().sendMessage(ChatColor.BLUE + "Okay, we set the question. Now what's the response?");
					e.setCancelled(true);
					break;
				}
	
				case INPUTTING_RESULT:{
					iro.setAnswer(e.getMessage());
					iro.getAi().getOptions().add(iro.create());
					confirmingPlayers.remove(e.getPlayer().getName());
					e.getPlayer().sendMessage(ChatColor.BLUE + "Gotcha! We're done here, try to speak to the NPC by right clicking!");
					e.setCancelled(true);
					break;
				}
			}
		} else if(e.getPlayer().hasMetadata("chattingNpc")){
			if(e.getMessage().length() == 1){
				Conversation c = (Conversation)e.getPlayer().getMetadata("chattingNpc").get(0).value();
				try {
					int pos = Math.abs(new Integer(e.getMessage()));
					if(pos > c.getOptions().size() || c.getOptions().isEmpty()){
						return;
					}
					c.getOptions().get(new Integer(e.getMessage())).onSelected(c.getListener(), e.getPlayer());
					e.setCancelled(true);
					c.endConversation();
				} catch (NumberFormatException ex){
					c.getListener().chat(e.getPlayer(), "I didn't quite catch that; could you repeat what you said?");
				}
			}	
		}
	}

	@EventHandler
	public void playerChat(PlayerInteractEntityEvent e){
		try {
			IMika em = (IMika)((CraftPlayer)e.getRightClicked()).getHandle();
			
			Conversation c = new Conversation(e.getPlayer(), em, em.getOptions().toArray(new Option[em.getOptions().size()]));
			c.startConversation();
			em.setTalking(true);
			em.lookAt(e.getPlayer().getEyeLocation());
			
			e.getPlayer().setMetadata("chattingNpc", new FixedMetadataValue(FriendlyWall.getPlugin(), c));
		} catch (ClassCastException ex){
			System.out.println("failed for " + e.getRightClicked());
		}
	}

	/*==============================Dangerous=================================*/
	public void resetAllData(){
		try {
			new File(getFunctionDirectory("NPCs"), "npcs.dat").delete();
			new File(getFunctionDirectory("NPCs"), "npcs.dat").createNewFile();

			for(File f : new File(getFunctionDirectory("NPCs"), "Quotes").listFiles()){
				f.delete();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void unloadNPCs(){
		for(Entry<String, IMika> npc : npcMap.entrySet()){
			npc.getValue().die();
		}

		npcMap.clear();
	}

}