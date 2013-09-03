package com.tenko.functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import com.google.common.collect.Maps;
import com.tenko.FriendlyWall;
import com.tenko.annotations.FunctionCommand;

public class Jail extends Function {

	private HashMap<String, Location> jailedUsers = Maps.newHashMap();
	private FileConfiguration config;
	private final File theConfigFile;
	private boolean isFirstTime = true;

	public Jail(){
		theConfigFile = new File(getFunctionDirectory("Jails"), "jails.yml");
		try {
			if(!theConfigFile.exists() || theConfigFile.length() == 0L){
				theConfigFile.createNewFile();
			} else {
				isFirstTime = false;
			}
			
			config = YamlConfiguration.loadConfiguration(theConfigFile);
			loadDataOnStartup();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	@FunctionCommand(usage = "/jail <user>", requiredArgumentCount = 1, opOnly = false, permission = "fw.jails")
	public void jail(CommandSender cs, String[] args){
		Player plyr = Bukkit.getPlayer(args[0]);
		
		if(plyr == null){
			cs.sendMessage(ChatColor.RED + "That player doesn't exist!");
			return;
		} else if(jailedUsers.containsKey(plyr.getName())){
			cs.sendMessage(ChatColor.RED + "That person is already jailed!");
			return;
		}
		
		Location oldLoc = plyr.getLocation();
		List<Object> pos = (List<Object>)config.getList(args.length > 1 ? args[1] : config.getString("Default"));
		plyr.teleport(new Location(Bukkit.getWorld((String)pos.get(0)), (double)pos.get(1), (double)pos.get(2), (double)pos.get(3)));
		plyr.setFlying(false);
		plyr.setSprinting(false);
		jailedUsers.put(plyr.getName(), oldLoc);
		plyr.sendMessage(ChatColor.RED + "Stop! You violated the l- Oops. Wrong game. Anyways, you've been jailed.");
		cs.sendMessage(ChatColor.BLUE + "Jailed " + plyr.getName());
	}
	
	@FunctionCommand(usage = "/setjail <name of jail>", requiredArgumentCount = 1, opOnly = true, playerOnly = true)
	public void setJail(CommandSender cs, final String[] args){
		final Player p = (Player)cs;
		
		final List<Object> pos = new ArrayList<Object>(){{
			add(p.getWorld().getName());
			add(p.getLocation().getX());
			add(p.getLocation().getY());
			add(p.getLocation().getZ());
		}};
		cs.sendMessage(ChatColor.BLUE + "Are you sure? Type \"/setjailyes\" to set the jail.");

		FriendlyWall.getCommandRegister().registerTemporaryCommand("setjailyes", cs.getName(), "op", new Runnable(){

			@Override
			public void run(){
				config.set(args[0], pos);
				if(isFirstTime){
					config.set("Default", args[0]);
				}	
				
				try {
					config.save(theConfigFile);
					config.load(theConfigFile);
					p.sendMessage(ChatColor.BLUE + "Set new jail.");
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			
		}, 200L);
	}
	
	@FunctionCommand(usage = "/unjail <user>", requiredArgumentCount = 1, permission = "fw.jails", opOnly = false)
	public void unjail(CommandSender cs, String[] args){
		Player plyr = Bukkit.getPlayer(args[0]);
		if(plyr == null || !jailedUsers.containsKey(plyr.getName())){
			cs.sendMessage(ChatColor.RED + "That user isn't jailed!");
			return;
		}
		
		Location l = jailedUsers.remove(plyr.getName());
		cs.sendMessage(ChatColor.RED + "Unjailed!");
		plyr.teleport(l);
		plyr.sendMessage(ChatColor.GREEN + "You have been unjailed.");
	}
	
	@FunctionCommand(usage = "/defaultjail", requiredArgumentCount = 1, opOnly = true)
	public void defaultjail(CommandSender cs, String[] args){
		if(config.getList(args[0]) != null){
			config.set("Default", args[0]);
			cs.sendMessage(ChatColor.BLUE + "Set the deafult jail.");
		} else {
			cs.sendMessage(ChatColor.RED + "That jail doesn't appear to be exisiting.");
		}
	}

	public void loadDataOnStartup() throws IOException{
		File datFile = new File(getFunctionDirectory("Jails"), "jailedUsers.dat");
		if(!datFile.exists()){
			datFile.createNewFile();
			return;
		}
		
		try(BufferedReader bw = new BufferedReader(new FileReader(datFile))){
			String l;
			String[] data;
			
			while((l = bw.readLine()) != null){
				data = l.split(":");
				jailedUsers.put(data[0], new Location(Bukkit.getWorld(data[1]), Double.valueOf(data[2]), Double.valueOf(data[3]), Double.valueOf(data[4])));
			}
		}
	}
	
	public void saveDataOnShutdown() throws IOException {
		File datFile = new File(getFunctionDirectory("Jails"), "jailedUsers.dat");
		if(!datFile.exists()){
			datFile.createNewFile();
			return;
		}
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(datFile))){
			for(Entry<String, Location> s : jailedUsers.entrySet()){
				if(datFile.length() != 0){
					bw.newLine();
				}

				bw.write(s.getKey() + ":" + s.getValue().getWorld().getName() + ":" + s.getValue().getX() + ":" + s.getValue().getY() + ":" + s.getValue().getZ());
			}
		}
	}
	
	//I'm too damn lazy to give them proper names.
	@EventHandler
	public void a(AsyncPlayerChatEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(PlayerInteractEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(PlayerTeleportEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
			System.out.println("Denied.");
		}
	}

	@EventHandler
	public void a(PlayerFishEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(PlayerGameModeChangeEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(PlayerItemConsumeEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(PlayerItemHeldEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(PlayerPickupItemEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(PlayerToggleFlightEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(PlayerToggleSprintEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(PlayerVelocityEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(BlockBreakEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void a(BlockPlaceEvent e){
		if(jailedUsers.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}

}
