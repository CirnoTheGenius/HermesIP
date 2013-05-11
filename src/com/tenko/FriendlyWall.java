package com.tenko;

import java.net.InetAddress;
import java.util.List;

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
