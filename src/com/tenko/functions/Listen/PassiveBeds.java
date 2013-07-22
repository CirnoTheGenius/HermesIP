package com.tenko.functions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.tenko.FriendlyWall;
import com.tenko.yaml.YamlWriter;

public class PassiveBeds extends Function {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void BedsNoExplode(PlayerInteractEvent iku){
		if(iku.getAction() == Action.RIGHT_CLICK_BLOCK && iku.getClickedBlock().getType() == Material.BED_BLOCK){
			if(isNotExplodable(iku.getClickedBlock().getWorld().getName())){
				iku.setCancelled(true);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		if(cs.equals(Bukkit.getConsoleSender())){
			if(c.getName().equalsIgnoreCase("passiveadd")){
				if(args.length < 1){
					result = "You didn't supply an argument!";
				} else if(!isRealWorld(args[0])){
					result = "That world doesn't exist!";
				} else if(!YamlWriter.writeToList(args[0], "PassiveBedWorlds")){
					result = "That world is already passive!";
				} else {
					result = "Pomf! Added \"" + ChatColor.YELLOW + args[0] + "\".";
					successful = true;
				}
				
				cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[FriendlyWall - PassiveBeds] " + result);
				return true;
			}
			
			if(c.getName().equalsIgnoreCase("passiverem")){
				if(args.length < 1){
					result = "You didn't supply an argument!";
				} else if(!YamlWriter.removeFromList(args[0], "PassiveBedWorlds")){
					result = "That world isn't set to have passive beds!";
				} else {
					result = "Removed \"" + ChatColor.YELLOW + args[0] + "\".";
					successful = true;
				}
				
				cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[FriendlyWall - PassiveBeds] " + result);
				return true;
			}
			
			if(c.getName().equalsIgnoreCase("passivelist")){
				cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - PassiveBeds] Current passive worlds:");
				for(String world : FriendlyWall.getPlugin().getConfig().getStringList("PassiveBedWorlds")){
					cs.sendMessage(ChatColor.YELLOW + "- " + world);
				}
				return true;
			}
		} else {
			cs.sendMessage("Unknown command. Type \"help\" for help.");
			return true;
		}
		return false;
	}
	
	private boolean isNotExplodable(String worldName){
		return FriendlyWall.getPlugin().getConfig().getStringList("PassiveBedWorlds").contains(worldName);
	}
	
	private boolean isRealWorld(String worldName){
		return Bukkit.getServer().getWorld(worldName) != null;
	}
}
