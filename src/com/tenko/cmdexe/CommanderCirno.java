package com.tenko.cmdexe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import com.tenko.FriendlyWall;

public class CommanderCirno implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		if((cs.hasPermission("friendlywall.ban") || cs.isOp()) && cs.equals(Bukkit.getConsoleSender())){
			if(c.getName().equalsIgnoreCase("fwreload")){
				try {
					FriendlyWall.getPlugin().getConfig().load(new File(FriendlyWall.getPlugin().getDataFolder(), "config.yml"));
					cs.sendMessage(ChatColor.BLUE + "[FriendlyWall] Reloaded config!");
				} catch (FileNotFoundException e){
					cs.sendMessage(ChatColor.RED + "[FriendlyWall] The configuration file was not found!");
					e.printStackTrace();
				} catch (IOException e) {
					cs.sendMessage(ChatColor.RED + "[FriendlyWall] There was an error while reading the file!");
					e.printStackTrace();
				} catch (InvalidConfigurationException e) {
					cs.sendMessage(ChatColor.RED + "[FriendlyWall] The configuration has invalid settings!");
					e.printStackTrace();
				}
				
				return true;
			}
			
			if(c.getName().equalsIgnoreCase("banip")){
				if(args.length < 1){
					cs.sendMessage(ChatColor.RED + "[FriendlyWall] You didn't supply an argument!");
					return true;
				}
				
				if(validIP(args[0]) == false && !args[0].contains("*")){
					cs.sendMessage(ChatColor.RED + "[FriendlyWall] Must be in IPv4 format!");
					return true;
				}
				
				FriendlyWall fw = FriendlyWall.getPlugin();
				List<String> bans = fw.getConfig().getStringList("Bans");
				
				if(bans.contains(args[0])){
					cs.sendMessage(ChatColor.RED + "[FriendlyWall] That IP is already banned!");
					return true;
				}
				
				bans.add(args[0]);
				fw.getConfig().set("Bans", bans);
				fw.saveConfig();
				cs.sendMessage(ChatColor.BLUE + "[FriendlyWall] Banned IP " + ChatColor.YELLOW + args[0]);
				//Add another layer of security, just in case this plugin breaks.
				Bukkit.getServer().banIP(args[0]);
				return true;
			}
			
			if(c.getName().equalsIgnoreCase("pardonip")){
				FriendlyWall fw = FriendlyWall.getPlugin();
				List<String> bans = fw.getConfig().getStringList("Bans");
				if(bans.contains(args[0])){
					if(bans.remove(args[0])){
						fw.getConfig().set("Bans", bans);
						fw.saveConfig();
						cs.sendMessage(ChatColor.BLUE + "[FriendlyWall] Pardoned IP " + ChatColor.YELLOW + args[0]);
					} else {
						cs.sendMessage(ChatColor.RED + "[FriendlyWall] We somehow failed to pardon the IP!");
					}
					//Remove the extra layer of security.
					Bukkit.getServer().unbanIP(args[0]);
				} else {
					cs.sendMessage(ChatColor.RED + "[FriendlyWall] IP \"" + args[0] + "\" isn't banned!");
				}
				return true;
			}
			
			if(c.getName().equalsIgnoreCase("iplist")){
				cs.sendMessage(ChatColor.BLUE + "[FriendlyWall] Currently banned IP's:");
				for(String ban : FriendlyWall.getPlugin().getConfig().getStringList("Bans")){
					cs.sendMessage(ChatColor.YELLOW + "- " + ban);
				}
				return true;
			}
		} else {
			cs.sendMessage("Unknown command. Type \"help\" for help.");
			return true;
		}
		
		return false;
	}
	
	//I'm an idiot for making this function.
	public static boolean validIP(String ip) {
		int count = 0;
		for(char c : ip.toCharArray()){
			count += (c == 46 ? 1 : 0);
		}
		
		if(count != 3){
			return false;
		}

		String[] segments = ip.split("\\.");
		if(segments.length != 4){
			return false;
		}
		
		for(String s : segments){
			if(s.length() > 3 || s.length() < 1) return false;
		}

		return true;
	}
}
