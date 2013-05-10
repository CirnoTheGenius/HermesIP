package com.tenko.cmdexe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import com.tenko.YCFriendlyWall;

public class ReloadExe {
	
	public static void Execute(CommandSender cs){
		try {
			YCFriendlyWall.getPlugin().getConfig().load(new File(YCFriendlyWall.getPlugin().getDataFolder(), "config.yml"));
			cs.sendMessage(ChatColor.BLUE + "[YCFriendlyWall] Reloaded config!");
		} catch (FileNotFoundException e){
			cs.sendMessage(ChatColor.RED + "[YCFriendlyWall] The configuration file was not found!");
			e.printStackTrace();
		} catch (IOException e) {
			cs.sendMessage(ChatColor.RED + "[YCFriendlyWall] There was an error while reading the file!");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			cs.sendMessage(ChatColor.RED + "[YCFriendlyWall] The configuration has invalid settings!");
			e.printStackTrace();
		}
	}
	
}