package com.tenko.cmdexe;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.tenko.YCFriendlyWall;

public class IPListExe {
	
	public static void Execute(CommandSender cs){
		YCFriendlyWall pl = YCFriendlyWall.getPlugin();
		ArrayList<String> bans = new ArrayList<String>(pl.getConfig().getStringList("RangeBans"));
		cs.sendMessage(ChatColor.BLUE + "[YCFriendlyWall] Currently banned IP's:");
		
		for(String ban : bans){
			cs.sendMessage(ChatColor.YELLOW + "- " + ban);
		}
	}
	
}
