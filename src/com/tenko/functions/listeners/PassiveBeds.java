package com.tenko.functions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PassiveBeds extends TListener {

	public PassiveBeds(){
		super("passive", "passivebeds", "passivebeds", "passivebed-worlds");
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void BedsNoExplode(PlayerInteractEvent iku){
		if(iku.getAction() == Action.RIGHT_CLICK_BLOCK && iku.getClickedBlock().getType() == Material.BED_BLOCK){
			if(isNotExplodable(iku.getClickedBlock().getWorld().getName())){
				iku.setCancelled(true);
			}
		}
	}
	
	private boolean isNotExplodable(String worldName){
		return config.getStringList("passivebed-worlds").contains(worldName);
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
