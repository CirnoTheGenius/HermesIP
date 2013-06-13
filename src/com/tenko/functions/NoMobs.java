package com.tenko.functions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.tenko.FriendlyWall;
import com.tenko.yaml.YamlWriter;

public class NoMobs extends Function {

	public NoMobs(){
		//Register event.
		Bukkit.getServer().getPluginManager().registerEvents(this, FriendlyWall.getPlugin());

		//Register commands.
		FriendlyWall.registerCommand("moblessadd", this);
		FriendlyWall.registerCommand("moblessrem", this);
		FriendlyWall.registerCommand("moblesslist", this);
	}
	
	public static void startFunction(){
		new NoMobs();
	}
	
	@EventHandler
	public void noSpawn(CreatureSpawnEvent e){
		if(cannotSpawn(e.getLocation().getWorld().getName())){
			e.setCancelled(true);
		}
	}

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		if(cs.equals(Bukkit.getConsoleSender())){
			if(c.getName().equalsIgnoreCase("moblessadd")){
				if(args.length < 1){
					result = "You didn't supply an argument!";
				} else if(!isRealWorld(args[0])){
					result = "That world doesn't exist!";
				} else if(!YamlWriter.writeToList(args[0], "MoblessWorlds")){
					result = "That world already denies mob spawning!";
				} else {
					result = "Removed all tenctacles! Added " + ChatColor.YELLOW + args[0] + ".";
					successful = true;
				}

				cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[FriendlyWall - MoblessWorlds] " + result);
				return true;
			}

			if(c.getName().equalsIgnoreCase("moblessrem")){
				if(args.length < 1){
					result = "You didn't supply an argument!";
				} else if(!YamlWriter.removeFromList(args[0], "MoblessWorlds")){
					result = "That world isn't set to deny mob spawning!";
				} else {
					result = "Removed \"" + ChatColor.YELLOW + args[0] + "\".";
					successful = true;
				}

				cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[FriendlyWall - MoblessWorlds] " + result);
				return true;
			}

			if(c.getName().equalsIgnoreCase("moblesslist")){
				cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - MoblessWorlds] Current mobless worlds:");
				for(String warudo : YamlWriter.getList("MoblessWorlds")){
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

	private boolean cannotSpawn(String worldName){
		return FriendlyWall.getPlugin().getConfig().getStringList("MoblessWorlds").contains(worldName);
	}

	private boolean isRealWorld(String worldName){
		return Bukkit.getServer().getWorld(worldName) != null;
	}
}
