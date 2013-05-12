package com.tenko;

import java.net.InetAddress;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.tenko.cmdexe.CommanderCirno;
import com.tenko.listeners.Guard;

//I've gone insane to rewrite this.
public class FriendlyWall extends JavaPlugin {
	
	private static FriendlyWall instance;
	private CommanderCirno cc;
	
	@Override
	public void onEnable(){
		instance = this;
		this.cc = new CommanderCirno();

		getServer().getPluginManager().registerEvents(new Guard(), this);
		getCommand("fwreload").setExecutor(this.cc);
		getCommand("banip").setExecutor(this.cc);
		getCommand("pardonip").setExecutor(this.cc);
		getCommand("iplist").setExecutor(this.cc);
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "O" + ChatColor.GOLD + "p" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "r" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "t " + ChatColor.RED + "i" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "n" + " " + ChatColor.GREEN + "F" + ChatColor.BLUE + "r" + ChatColor.LIGHT_PURPLE + "i" + ChatColor.RED + "e" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "d" + ChatColor.GREEN + "l" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "W" + ChatColor.RED + "a" + ChatColor.GOLD + "l" + ChatColor.YELLOW + "l" + ChatColor.GREEN + ":" + " " + ChatColor.BLUE + "C" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "m" + ChatColor.GOLD + "m" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "n" + ChatColor.BLUE + "c" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "!");
	}
	
	@Override
	public void onDisable(){
		this.saveConfig();
	}
	
	public static FriendlyWall getPlugin(){
		return instance;
	}

	public boolean isBanned(InetAddress address) {
		List<String> bans = getConfig().getStringList("Bans");
		
		for(String s : bans){
			if(address.getHostName().startsWith(s.contains("*") ? s.substring(0, s.indexOf("*")-1) : s)){
				return true;
			}
		}
		
		return false;
	}
	
}
