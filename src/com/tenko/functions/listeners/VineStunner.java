package com.tenko.functions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;

public class VineStunner extends TListener {

	public VineStunner(){
		super("vinestun", "VineStunner", "stunned", "stunned-worlds");
	}

	@EventHandler
	public void stopGrowth(BlockPhysicsEvent e){
		if(e.getBlock().getType() == Material.VINE && cannotGrow(e.getBlock().getWorld().getName())){
			e.setCancelled(true);
		}
	}
	
	private boolean cannotGrow(String worldName){
		return config.getStringList("stunned-worlds").contains(worldName);
	}
	
	@Override
	public boolean callMeMaybe(CommandSender cs, String[] args) {
		if(Bukkit.getServer().getWorld(args[0]) == null){
			cs.sendMessage(ChatColor.RED + "That world doesn't exist!");
			return false;
		}
		return true;
	}
	
}
