package com.tenko.cmdexe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import com.tenko.HermesIP;

public class ReloadExe {
	
	public static void Execute(CommandSender cs){
		try {
			HermesIP.getPlugin().getConfig().load(new File(HermesIP.getPlugin().getDataFolder(), "config.yml"));
			cs.sendMessage(ChatColor.BLUE + "[HermesIP] Reloaded config!");
		} catch (FileNotFoundException e){
			cs.sendMessage(ChatColor.RED + "[HermesIP] The configuration file was not found!");
			e.printStackTrace();
		} catch (IOException e) {
			cs.sendMessage(ChatColor.RED + "[HermesIP] There was an error while reading the file!");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			cs.sendMessage(ChatColor.RED + "[HermesIP] The configuration has invalid settings!");
			e.printStackTrace();
		}
	}
	
}