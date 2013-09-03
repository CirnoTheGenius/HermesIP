package com.tenko.functions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.tenko.FriendlyWall;
import com.tenko.NovEngine.Conversation;
import com.tenko.NovEngine.options.IncompleteResponseOption;
import com.tenko.annotations.FunctionCommand;
import com.tenko.npc.BaseNPC;
import com.tenko.npc.CraftMika;
import com.tenko.objs.NPCDataParserRunnable;

public class NPCs extends Function {

	/*
	 * TODO:
	 * Finish refactoring NPCsV2.
	 * Rewrite the quote adding system to use AsyncPlayerChatEvent instead of arguments.
	 */

	private final double BOUNDS = Math.PI;
	private final File NPC_DATA_FILE;

	private HashMap<String, BaseNPC> npcMap = Maps.newHashMap();
	private HashMap<String, MutablePair<Stage, BaseNPC>> quoteStages = Maps.newHashMap();
	private HashMap<String, MutablePair<MutablePair<IncompleteResponseOption, Stage>, BaseNPC>> optionStages = Maps.newHashMap();
	private enum Stage {
		INPUTTING_QUESTION_OR_QUOTE, INPUTTING_ANWSER, 
	}

	public NPCs(){
		NPC_DATA_FILE = new File(Function.getFunctionDirectory("NPCs"), "npcs.dat");
		Thread paruingThread = new Thread(new NPCDataParserRunnable(this, NPC_DATA_FILE));
		paruingThread.start();
	}

	@FunctionCommand(opOnly = false, permission = "fw.npcs", playerOnly = true, usage = "/newnpc <name>", requiredArgumentCount = 1)
	public void newNpc(CommandSender cs, String[] args){
		if(npcMap.containsKey(args[0])){
			cs.sendMessage(ChatColor.RED + "A NPC with that name already exists.");
			return;
		} else if(args[0].length() > 16){
			cs.sendMessage(ChatColor.RED + "The specified name is too long!");
			return;
		}
		CraftMika npc = CraftMika.createNPC(args[0], ((Player)cs).getLocation());
		npcMap.put(args[0], npc);
		writeData(args[0], npc.getLocation().getWorld().getName(), npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ());
		cs.sendMessage(ChatColor.BLUE + "Spawned NPC.");
	}

	@FunctionCommand(opOnly = false, permission = "fw.npcs", usage = "/removenpc <name>", requiredArgumentCount = 1)
	public void removeNPC(CommandSender cs, String[] args){
		BaseNPC entity;
		if((entity = npcMap.get(args[0])) != null){
			entity.remove();
			npcMap.remove(args[0]);
			removeData(args[0]);
			cs.sendMessage(ChatColor.BLUE + "Successfully removed NPC.");
		} else {
			cs.sendMessage(ChatColor.RED + "That NPC doesn't appear to exist.");
		}
	}

	@FunctionCommand(opOnly = true, usage = "/diemonster <radius>", requiredArgumentCount = 1)
	public void dieMonster(CommandSender cs, String[] args){
		double bounds = new Double(args[0]);
		int count = 0;

		for(Entity e : ((Player)cs).getNearbyEntities(bounds, bounds, bounds)){
			if(e.getType() == EntityType.PLAYER){
				String name = ((Player)e).getName();
				if(Bukkit.getPlayerExact(name) == null || (npcMap.containsKey(name) && npcMap.get(name).getUniqueId() == e.getUniqueId())){
					e.remove();
					npcMap.remove(name);
					count++;
				}
			}
		}

		cs.sendMessage(ChatColor.BLUE + "Die all " + count + " monsters! You don't belong in this world!");
	}

	@FunctionCommand(opOnly = true, usage = "/npcdebug <list type>", requiredArgumentCount = 1)
	public void npcDebug(CommandSender cs, String[] args) throws IOException{
		if(args[0].equalsIgnoreCase("list")){
			for(String s : npcMap.keySet()){
				cs.sendMessage(ChatColor.BLUE + s);
			}
			cs.sendMessage(ChatColor.GOLD + "Amount of NPCs: " + npcMap.size());
		} else if(args[0].equalsIgnoreCase("rawlist")){
			File f = new File(getFunctionDirectory("NPCs"), "npcs.dat");
			for(String s : Files.readLines(f, Charset.defaultCharset())){
				cs.sendMessage(ChatColor.BLUE + s);
			}
		}
	}

	@FunctionCommand(opOnly = false, permission = "fw.npcs", playerOnly = true, requiredArgumentCount = 1, usage = "/telenpc <name>")
	public void teleNPC(CommandSender cs, String[] args){
		BaseNPC entity;
		if((entity = npcMap.get(args[0])) != null){
			entity.teleportTo(((Player)cs).getLocation());
		} else {
			cs.sendMessage(ChatColor.RED + "That NPC doesn't appear to exist.");
		}
	}

	@FunctionCommand(opOnly = true, playerOnly = true, requiredArgumentCount = 1, usage = "/unsafeentityremove <radius>")
	public void unsafeEntityRemove(CommandSender cs, String[] args){
		double bounds = new Double(args[0]);

		for(Entity e : ((Player)cs).getNearbyEntities(bounds, bounds, bounds)){
			e.remove();
		}
	}

	@FunctionCommand(opOnly = false, permission = "fw.npcs", requiredArgumentCount = 2, usage = "/npcquotes <npc name> <add|rem|list>")
	public void npcQuotes(CommandSender cs, String[] args){
		BaseNPC npc = npcMap.get(args[0]);

		if(npc == null){
			cs.sendMessage(ChatColor.RED + "That NPC doesn't appear to exist.");
			return;
		}

		if(args[1].equalsIgnoreCase("add")){
			quoteStages.put(cs.getName(), MutablePair.of(Stage.INPUTTING_QUESTION_OR_QUOTE, npc));
			cs.sendMessage(ChatColor.BLUE + "Chat the statement you want the NPC to say when near!");
		} else if(args[1].equalsIgnoreCase("rem")){
			if(args.length < 3){
				cs.sendMessage(ChatColor.RED + "This command requires a number after \"rem\".");
				return;
			}

			if(StringUtils.isNumeric(args[2])){
				int pos = new Integer(args[2]);
				npc.getQuotes().remove(pos);
				cs.sendMessage(ChatColor.BLUE + "Removed quote!");
			} else {
				cs.sendMessage(ChatColor.RED + "That last argument needs to be a number.");
				return;
			}
		} else if(args[1].equalsIgnoreCase("list")){
			cs.sendMessage(ChatColor.BLUE + "Current quotes for " + npc.getName() + ":");
			for(int i=0; i < npc.getQuotes().size(); i++){
				cs.sendMessage(ChatColor.BLUE + (i + ": " + npc.getQuotes().get(i)));
			}
		}
	}

	@FunctionCommand(opOnly = false, permission = "fw.npcs", requiredArgumentCount = 2, usage = "/vnstyle <name of npc> <add|rem|list> [index]")
	public void vnStyle(CommandSender cs, String[] args){
		BaseNPC npc = npcMap.get(args[0]);

		if(npc == null){
			cs.sendMessage(ChatColor.RED + "That NPC doesn't appear to exist.");
			return;
		}

		if(args[1].equalsIgnoreCase("add")){
			cs.sendMessage(ChatColor.BLUE + "Type the question that you want the player to be able to select as a choice.");
			cs.sendMessage(ChatColor.BLUE + "You can use %plyr% in place of the players name, and %ai% in place of the NPC's name.");
			optionStages.put(cs.getName(), MutablePair.of(MutablePair.of(new IncompleteResponseOption(), Stage.INPUTTING_QUESTION_OR_QUOTE), npc));
		} else if(args[1].equalsIgnoreCase("rem")){
			if(args.length > 2){
				if(StringUtils.isNumeric(args[2])){
					npc.getOptions().remove(Integer.valueOf(args[2]));
					cs.sendMessage(ChatColor.BLUE + "Removed option!");
				} else {
					cs.sendMessage(ChatColor.RED + "You must provide a number after \"rem\".");					
				}
			} else {
				cs.sendMessage(ChatColor.RED + "You must provide a number after \"rem\".");				
			}
		} else if(args[1].equalsIgnoreCase("list")){
			for(int i=0; i < npc.getOptions().size(); i++){
				cs.sendMessage(ChatColor.AQUA + (i + ": " + npc.getOptions().get(i).getQuestion()));
				cs.sendMessage(ChatColor.BLUE + " - " + npc.getOptions().get(i).getAnswer());
			}
		}
	}

	@EventHandler
	public void playerChat(AsyncPlayerChatEvent e){
		if(quoteStages.containsKey(e.getPlayer().getName())){
			Pair<Stage, BaseNPC> pair = quoteStages.get(e.getPlayer().getName());
			if(pair.getLeft() == Stage.INPUTTING_QUESTION_OR_QUOTE){
				pair.getRight().getQuotes().add(e.getMessage());
				e.getPlayer().sendMessage(ChatColor.BLUE + "Added quote successful!");
				quoteStages.remove(e.getPlayer().getName());
				e.setCancelled(true);
			}
		}
	}	

	@EventHandler
	public void playerMovement(final PlayerMoveEvent e){
		if((Math.abs(e.getFrom().getX() - e.getTo().getX()) > 0.5) && (Math.abs(e.getFrom().getZ() - e.getTo().getZ()) > 0.5)){
			return;
		}

		for(final BaseNPC npc : npcMap.values()){
			Bukkit.getScheduler().scheduleSyncDelayedTask(FriendlyWall.getPlugin(), new Runnable(){
				@Override
				public void run(){
					try {
						Player plyr = e.getPlayer();
						if(plyr.getWorld() != npc.getWorld()){
							return;
						}

						if(plyr.getLocation().distanceSquared(npc.getLocation()) < BOUNDS * BOUNDS){
							npc.lookAt(plyr.getLocation());
							if(plyr.hasMetadata("npcTalkedTo" + npc.getName())){
								return;
							}

							plyr.setMetadata("npcTalkedTo" + npc.getName(), new FixedMetadataValue(FriendlyWall.getPlugin(), new Object()));
							npc.chat(plyr);
						} else {
							if(plyr.hasMetadata("npcTalkedTo" + npc.getName())){
								plyr.removeMetadata("npcTalkedTo" + npc.getName(), FriendlyWall.getPlugin());
							}
						}
					} catch (Exception ex){
						FriendlyWall.ExceptionCount++;
					}
				}
			});
		}

		if(e.getPlayer().hasMetadata("VNconversation")){
			FixedMetadataValue val = (FixedMetadataValue)e.getPlayer().getMetadata("VNconversation").get(0);
			if(((Conversation)val.value()).getNPC().getLocation().distanceSquared(e.getPlayer().getLocation()) > (BOUNDS * BOUNDS)){
				e.getPlayer().removeMetadata("VNconversation", FriendlyWall.getPlugin());
			}
		}
	}

	@EventHandler
	public void playerStartConversation(PlayerInteractEntityEvent e){
		if(e.getRightClicked() instanceof Player && ((Player)e.getRightClicked()).getAddress() == null && !e.getPlayer().hasMetadata("VNconversation")){
			BaseNPC npc = npcMap.get(((Player)e.getRightClicked()).getName());
			if(npc.getOptions().size() > 0){
				npc.lookAt(e.getPlayer().getLocation());
				Conversation con = new Conversation(e.getPlayer(), npc);
				e.getPlayer().setMetadata("VNconversation", new FixedMetadataValue(FriendlyWall.getPlugin(), con));
				con.startConversation();
			}
		}
	}

	@EventHandler
	public void playerSelectOption(AsyncPlayerChatEvent e){
		if(e.getPlayer().hasMetadata("VNconversation")){
			Conversation conv = (Conversation)e.getPlayer().getMetadata("VNconversation").get(0).value();
			int index;
			if(StringUtils.isNumeric(e.getMessage()) && conv.optionListSize() > (index = Math.abs(new Integer(e.getMessage()).intValue()))){
				conv.getOption(index).onSelected(conv.getNPC(), conv.getPlayer(), conv);
				e.setCancelled(true);
			}
		} else if(optionStages.containsKey(e.getPlayer().getName())){
			MutablePair<MutablePair<IncompleteResponseOption, Stage>, BaseNPC> pair = optionStages.get(e.getPlayer().getName());
			IncompleteResponseOption option = pair.getLeft().getLeft();
			if(pair.getLeft().getRight() == Stage.INPUTTING_QUESTION_OR_QUOTE){
				option.setQuestion(e.getMessage());
				pair.getLeft().setValue(Stage.INPUTTING_ANWSER);
				e.getPlayer().sendMessage(ChatColor.BLUE + "Okay, so what's the NPC's response?");
				optionStages.put(e.getPlayer().getName(), pair);
				e.setCancelled(true);
			} else if(pair.getLeft().getRight() == Stage.INPUTTING_ANWSER){
				option.setAnswer(e.getMessage());
				pair.getRight().getOptions().add(option.createResponse());
				e.getPlayer().sendMessage(ChatColor.BLUE + "Added option!");
				optionStages.remove(e.getPlayer().getName());
				e.setCancelled(true);
			}
		}
	}

	public void writeData(String name, String world, Double x, Double y, Double z){
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(NPC_DATA_FILE))){
			bw.write(name);
			bw.write(';');
			bw.write(world);
			bw.write(';');
			bw.write(x.toString());
			bw.write(';');
			bw.write(y.toString());
			bw.write(';');
			bw.write(z.toString());
			bw.newLine();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void removeData(String name){
		name = name + ';';

		try {
			List<String> lines = Files.readLines(NPC_DATA_FILE, Charset.defaultCharset());
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(NPC_DATA_FILE, false))){
				for(String s : lines){
					if(!s.startsWith(name)){
						bw.write(s);
						bw.newLine();
					}
				}
			}
		} catch (IOException e1){
			e1.printStackTrace();
		}
	}

	public void endFunction(){
		for(BaseNPC npc : npcMap.values()){
			npc.remove();
		}

		for(Player p : Bukkit.getOnlinePlayers()){
			p.removeMetadata("VNconversation", FriendlyWall.getPlugin());
		}

		npcMap.clear();
	}

	public void unregisterNPC(String name){
		this.npcMap.remove(name);
	}

	public Collection<BaseNPC> getAllNPCs(){
		return npcMap.values();
	}

	public void registerNPC(String name, CraftMika entity){
		this.npcMap.put(name, entity);
	}
}
