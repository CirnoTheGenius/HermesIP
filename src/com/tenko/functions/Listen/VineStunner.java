package com.tenko.functions.listen;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;

import com.tenko.FriendlyWall;

public class VineStunner extends TListener {
	
	private final static String functionName = "VineStunner";
	private final static String yamlName = "stunned";
	private final static String listName = "stunned-worlds";
	
	public VineStunner(){
		super("vinestun", functionName, yamlName, listName);
	}

	@EventHandler
	public void stopGrowth(BlockPhysicsEvent e){
		if(e.getBlock().getType() == Material.VINE && cannotGrow(e.getBlock().getWorld().getName())){
			e.setCancelled(true);
		}
	}
	
	private boolean cannotGrow(String worldName){
		return FriendlyWall.getPlugin().getConfig().getStringList(listName).contains(worldName);
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