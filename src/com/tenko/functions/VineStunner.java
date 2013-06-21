package com.tenko.functions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;

import com.tenko.FriendlyWall;
import com.tenko.yaml.YamlWriter;

public class VineStunner extends Function {
	
	@EventHandler
	public void stopGrowth(BlockPhysicsEvent e){
		if(e.getBlock().getType() == Material.VINE && cannotGrow(e.getBlock().getWorld().getName())){
			e.setCancelled(true);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args) {
		if(cs.equals(Bukkit.getConsoleSender())){
			if(c.getName().equalsIgnoreCase("vinestunadd")){
				if(args.length < 1){
					result = "You didn't supply an argument!";
				} else if(!isRealWorld(args[0])){
					result = "That world doesn't exist!";
				} else if(!YamlWriter.writeToList(args[0], "VineStunnerWorlds")){
					result = "That world already denies vine growth!";
				} else {
					result = "Anything squid-like has been stopped! Added " + ChatColor.YELLOW + args[0] + ".";
					successful = true;
				}

				cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[FriendlyWall - VineStunner] " + result);
				return true;
			}

			if(c.getName().equalsIgnoreCase("vinestunrem")){
				if(args.length < 1){
					result = "You didn't supply an argument!";
				} else if(!YamlWriter.removeFromList(args[0], "VineStunnerWorlds")){
					result = "That world isn't set to deny vine growth!";
				} else {
					result = "Removed " + ChatColor.YELLOW + args[0] + ".";
					successful = true;
				}

				cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[FriendlyWall - VineStunner] " + result);
				return true;
			}

			if(c.getName().equalsIgnoreCase("vinestunlist")){
				cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - VineStunner] Current worlds in which vines have stopped growing:");
				for(String warudo : YamlWriter.getList("VineStunnerWorlds")){
					cs.sendMessage(ChatColor.YELLOW + "- " + warudo);
				}
				return true;
			}
		} else {
			cs.sendMessage("Unknown command. Type \"help\" for help.");
			return true;
		}
		return false;
	}
	
	private boolean cannotGrow(String worldName){
		return FriendlyWall.getPlugin().getConfig().getStringList("VineStunnerWorlds").contains(worldName);
	}
	
	private boolean isRealWorld(String worldName){
		return Bukkit.getServer().getWorld(worldName) != null;
	}

}