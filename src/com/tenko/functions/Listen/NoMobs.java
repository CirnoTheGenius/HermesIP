package com.tenko.functions.Listen;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.tenko.FriendlyWall;

public class NoMobs extends Listen {
	
	private final static String functionName = "NoMobs";
	private final static String yamlName = "mobless";
	private final static String listName = "mobless-worlds";
	
	public NoMobs(){
		super("mobless", functionName, yamlName, listName);
	}
	
	@Override
	public boolean callMeMaybe(CommandSender cs, String[] args){
		if(Bukkit.getServer().getWorld(args[0]) == null){
			cs.sendMessage(ChatColor.RED + "That world doesn't exist!");
			return false;
		}
		return true;
	}

	@EventHandler
	public void noSpawn(CreatureSpawnEvent e){
		if(cannotSpawn(e.getLocation().getWorld().getName())){
			e.setCancelled(true);
		}
	}

	private boolean cannotSpawn(String worldName){
		return FriendlyWall.getPlugin().getConfig().getStringList(listName).contains(worldName);
	}
	
}