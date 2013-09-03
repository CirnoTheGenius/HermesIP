package com.tenko.functions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class NoMobs extends TListener {

	public NoMobs() {
		super("mobless", "NoMobs", "mobless", "mobless-worlds");
	}

	@Override
	public boolean callMeMaybe(CommandSender cs, String[] args){
		if(Bukkit.getServer().getWorld(args[0]) == null){
			cs.sendMessage(ChatColor.RED + "That world doesn't exist!");
			return false;
		}

		//Die monster! You don't belong in this world! -shot for stupid reference/pun-
		for(Entity e : Bukkit.getServer().getWorld(args[0]).getEntities()){
			if(e.getType() != EntityType.PLAYER){
				e.remove();
			}
		}
		return true;
	}
	
	@EventHandler
	public void noSpawn(CreatureSpawnEvent e){
		if(e.getSpawnReason() == SpawnReason.NATURAL){
			if(cannotSpawn(e.getLocation().getWorld().getName())){
				e.setCancelled(true);
			}
		}
	}

	private boolean cannotSpawn(String worldName){
		return config.getStringList("mobless-worlds").contains(worldName);
	}

}
