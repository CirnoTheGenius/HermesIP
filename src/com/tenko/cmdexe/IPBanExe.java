package com.tenko.cmdexe;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.tenko.HermesIP;
import com.tenko.utils.Validator;

public class IPBanExe {

	public static void Execute(CommandSender cs, String[] args){
		if(args.length < 1){
			cs.sendMessage(ChatColor.RED + "[HermesIP] You didn's supply enough arguments!");
			return;
		}

		if(args[0].contains("*")){
			HermesIP pl = HermesIP.getPlugin();
			ArrayList<String> bans = new ArrayList<String>(pl.getConfig().getStringList("RangeBans"));
			bans.add(args[0]);
			pl.getConfig().set("RangeBans", bans);
			pl.saveConfig();
			cs.sendMessage(ChatColor.BLUE + "[HermesIP] Banned IP " + ChatColor.YELLOW + args[0]);
		} else {
			if(Validator.validIP(args[0]) == false && !args[0].contains("*")){
				cs.sendMessage(ChatColor.RED + "[HermesIP] Must be in IPv4 format!");
				return;
			}
			
			Bukkit.getServer().banIP(args[0]);
		}
	}

}
