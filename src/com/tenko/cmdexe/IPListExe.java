package com.tenko.cmdexe;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.tenko.HermesIP;

public class IPListExe {
	
	public static void Execute(CommandSender cs){
		HermesIP pl = HermesIP.getPlugin();
		ArrayList<String> bans = new ArrayList<String>(pl.getConfig().getStringList("RangeBans"));
		cs.sendMessage(ChatColor.BLUE + "[HermesIP] Currently banned IP's:");
		
		for(String ban : bans){
			cs.sendMessage(ChatColor.YELLOW + "- " + ban);
		}
	}
	
}
