package com.tenko.cmdexe;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.tenko.HermesIP;
import com.tenko.utils.Validator;

public class IPPardonExe {

	public static void Execute(CommandSender cs, String[] args){
		if(args.length < 1){
			cs.sendMessage(ChatColor.RED + "[HermesIP] You didn's supply enough arguments!");
			return;
		}

		if(Validator.validIP(args[0]) == false){
			cs.sendMessage(ChatColor.RED + "[HermesIP] Must be in IPv4 format!");
			return;
		}

		HermesIP pl = HermesIP.getPlugin();
		ArrayList<String> bans = new ArrayList<String>(pl.getConfig().getStringList("RangeBans"));

		if(bans.contains(args[0])){
			if(bans.remove(args[0])){
				pl.saveConfig();
				pl.getConfig().set("RangeBans", bans);
				cs.sendMessage(ChatColor.BLUE + "[HermesIP] Pardoned IP " + ChatColor.YELLOW + args[0]);
			} else {
				cs.sendMessage(ChatColor.RED + "[HermesIP] We somehow failed to pardon the IP!");
			}
		} else {
			cs.sendMessage(ChatColor.RED + "[HermesIP] IP \"" + args[0] + "\" isn't banned!");
		}

	}

}
