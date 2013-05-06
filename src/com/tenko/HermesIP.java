package com.tenko;

import java.net.InetAddress;
import java.util.List;

import name.richardson.james.bukkit.banhammer.BanHammer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.tenko.cmdexe.CommanderCirno;
import com.tenko.listeners.Hermes;

//No idea why I named it HermesIP. Just sounded "lightweighty".
public class HermesIP extends JavaPlugin {

	private static HermesIP pl;
	private FileConfiguration fc;
	private BanHammer banHammerHandler;
	private Hermes listener;
	private CommanderCirno cc;
	@Override
	public void onEnable(){
		pl = this;
		this.listener = new Hermes();
		this.fc = this.getConfig();
		this.cc = new CommanderCirno();

		getServer().getPluginManager().registerEvents(this.listener, this);
		getCommand("hreload").setExecutor(this.cc);
		getCommand("ipban").setExecutor(this.cc);
		getCommand("pardonip").setExecutor(this.cc);
		getCommand("iplist").setExecutor(this.cc);
		initBanHammer();
	}

	@Override
	public void onDisable(){
		this.saveConfig();
	}

	private void initBanHammer() {
		BanHammer plugin = (BanHammer)this.getServer().getPluginManager().getPlugin("BanHammer");
		if (plugin != null) {
			this.banHammerHandler = plugin;
		}
	}

	public static HermesIP getPlugin(){
		return pl;
	}

	public BanHammer getBanHammer(){
		return this.banHammerHandler;
	}

	public boolean isBanned(InetAddress ip){
		List<String> rbans = this.fc.getStringList("RangeBans");
		List<String> ipbans = this.fc.getStringList("IPBans");
		
		for(String s : rbans){
			if(ip.getHostName().startsWith(s.substring(0, s.indexOf("*")-1))){
				return true;
			}
		}
		
		for(String s : ipbans){
			if(ip.getHostAddress().equals(s)){
				return true;
			}
		}
		
		return false;
	}
}
