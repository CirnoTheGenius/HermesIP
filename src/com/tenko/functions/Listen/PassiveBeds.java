package com.tenko.functions.listen;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.tenko.FriendlyWall;

public class PassiveBeds extends TListener {
	
	private final static String functionName = "NoMobs";
	private final static String yamlName = "passivebeds";
	private final static String listName = "passivebed-worlds";
	
	public PassiveBeds(){
		super("passive", functionName, yamlName, listName);
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