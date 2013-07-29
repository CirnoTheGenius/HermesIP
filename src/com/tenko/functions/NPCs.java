package com.tenko.functions;

import com.google.common.io.Files;
import com.tenko.FriendlyWall;
import com.tenko.asm.builders.EntityMikaClassBuilder;
import com.tenko.asm.entity.IMika;
import com.tenko.nms.NMSLib;
import com.tenko.objs.TenkoCmd;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;

//IT'S BEING CLOSED, DAMNIT.
@SuppressWarnings("resource")
public class NPCs extends Function {

	boolean isConfirming;

	private static HashMap<String, IMika> NPCList = new HashMap<String, IMika>();
	private final EntityMikaClassBuilder getter;

	public NPCs(){
		getter = new EntityMikaClassBuilder();
		//		File f = new File(getFunctionDirectory("NPCs"), "npcs.dat");
		//		try {
		//			parseFile(f);
		//		} catch (IOException e){
		//			if(!f.exists()){
		//				try {
		//					f.createNewFile();
		//				} catch (IOException e1){
		//					e1.printStackTrace();
		//				}
		//			}
		//		}
	}

	//Ugliest class I've written so far.
	@Override
	public boolean onCommand(final CommandSender cs, Command c, String thisisnevergoingtobeused, String[] args){
		if(cs.isOp()){
			if(c.getName().equalsIgnoreCase("newnpc")){
				try {
					if(NPCList.containsKey(args[0])){
						cs.sendMessage(ChatColor.RED + "NPC " + args[0] + " already exists!");
						return true;
					}

					if(args[0].length() > 16){
						cs.sendMessage(ChatColor.RED + "That name is way too long!");
						return true;
					}

					IMika t = (IMika)getter.generateInstance(NMSLib.getNMSServer(), NMSLib.getNMSWorld(((Player)cs).getWorld()), args[0]);
					Location l = ((Player)cs).getLocation();
					t.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
					t.spawnIn(NMSLib.getNMSWorld(((Player)cs).getWorld()));
					cs.sendMessage(ChatColor.RED + "Spawned NPC");
					return true;
				} catch (ClassCastException e){
					cs.sendMessage(ChatColor.RED + "You must be a player!");
				} catch (ArrayIndexOutOfBoundsException e){
					cs.sendMessage(ChatColor.RED + "Provide an argument for name!");
				} catch (Exception e){
					cs.sendMessage(ChatColor.RED + "Failed with " + e);
					e.printStackTrace();
				}
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
			}

			if(c.getName().equalsIgnoreCase("diemonster")){
				double bounds = Double.valueOf(args[0]);
				int i = 0;
				for(Entity e : ((Player)cs).getNearbyEntities(bounds, bounds, bounds)){
					try {
						IMika bot = ((IMika)e);
						NPCList.remove(bot.getName());
						i++;
					} catch (Exception e2){}
				}
				cs.sendMessage(ChatColor.BLUE + "Removed " + i + " NPC(s).");
				return true;
			}

			try {
				IMika em = NPCList.get(args[0]);

				if(c.getName().equalsIgnoreCase("removenpc")){
					em.die();
					NPCList.remove(args[0]);
					remove(args[0]);
					cs.sendMessage(ChatColor.BLUE + "Removed NPC " + args[0]);
					return true;
				} else if(c.getName().equalsIgnoreCase("telenpc")){
					em.teleportTo(((Player)cs).getLocation());
					cs.sendMessage(ChatColor.BLUE + "Teleported NPC to you.");
					return true;
				}
			} catch (NullPointerException e){
				cs.sendMessage(ChatColor.RED + "That npc doesn't exist. (CaSe SeNsItIvE!)");
			} catch (ArrayIndexOutOfBoundsException e){
				cs.sendMessage(ChatColor.RED + "Provide an argument for name or number!");
			}
		}

		return false;
	}

	public static HashMap<String, IMika> getNPCList(){
		return NPCList;
	}

	@SuppressWarnings("unused")
	public void parseFile(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String l;
		while ((l = br.readLine()) != null){
			try { 
				String[] stuffs = l.split(";");
				String name = stuffs[0];
				String world = stuffs[1];
				double x = Double.valueOf(stuffs[2]).doubleValue();
				double y = Double.valueOf(stuffs[3]).doubleValue();
				double d1 = Double.valueOf(stuffs[4]).doubleValue();


			}
			catch (ArrayIndexOutOfBoundsException e){
				System.out.println("Failed to parse file!");
			}
		}
		br.close();
	}

	public void writeToFile(String name, String world, double x, double y, double z){
		try {
			File f = new File(getFunctionDirectory("NPCs"), "npcs.dat");
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

	public void die(){
		try {
			new File(getFunctionDirectory("NPCs"), "npcs.dat").delete();
			new File(getFunctionDirectory("NPCs"), "npcs.dat").createNewFile();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void killAllNPCs(){
		for(Entry<String, IMika> e : NPCList.entrySet()){
			e.getValue().die();
			NPCList.remove(e.getKey());
		}
	}

	@EventHandler
	public void iSeeDeadPeople(PlayerMoveEvent e){
		for(Entry<String, IMika> npc : NPCList.entrySet()){
			Player plyr = e.getPlayer();
			IMika mika = npc.getValue();

			if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()){
				if(plyr.getLocation().distanceSquared(npc.getValue().getCraftBukkitLocation()) < 49){
					mika.lookAt(plyr.getEyeLocation());
					if(plyr.hasMetadata("hasTalkedTo" + mika.getName())){
						continue;
					}

					plyr.setMetadata("hasTalkedTo" + mika.getName(), new FixedMetadataValue(FriendlyWall.getPlugin(), true));
					mika.chat(plyr, "Hi! My name is " + mika.getName());
				} else {
					if(plyr.hasMetadata("hasTalkedTo" + mika.getName())){
						plyr.removeMetadata("hasTalkedTo" + mika.getName(), FriendlyWall.getPlugin());
					}
				}
			}
		}
	}
}