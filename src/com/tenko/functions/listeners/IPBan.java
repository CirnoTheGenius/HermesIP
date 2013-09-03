package com.tenko.functions.listeners;

import java.net.InetAddress;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.google.common.collect.Lists;

public class IPBan extends TListener {

	public IPBan(){
		super("ipb", "HermesIP", "ipBans", "currentBans");
		checkOnlinePlayers();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void StopRightThere(PlayerLoginEvent criminalScum){
		if(isBanned(criminalScum.getAddress())){
			criminalScum.disallow(Result.KICK_OTHER, "Internal exception: java.net.SocketException: Connection reset");
		}
	}
	
	public boolean isBanned(InetAddress address) {
		List<String> bans = config.getStringList("currentBans");

		for(String s : bans){
			if(address.getHostAddress().startsWith(s.contains("*") ? s.substring(0, s.indexOf("*")-1) : s)){
				return true;
			}
		}

		return false;
	}
	
	@Override
	public boolean callMeMaybe(CommandSender cs, String[] args){
		if(!validIP(args[0])){
			cs.sendMessage(ChatColor.RED + "That doesn't appear to be an IPv4 address!");
			return false;
		}
		return true;
	}
	
	private boolean validIP(String ip) {
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
	
	private void checkOnlinePlayers(){
		List<String> kickedPlayers = Lists.newArrayList();
		for(Player p : Bukkit.getOnlinePlayers()){
			if(isBanned(p.getAddress().getAddress())){
				kickedPlayers.add(p.getName());
				p.kickPlayer("Internal exception: java.net.SocketException: Connection reset");
			}
		}
		
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.isOp()){
				for(String s : kickedPlayers){
					p.sendMessage(ChatColor.BLUE + s + " was kicked for having a banned IP.");
				}
			}
		}
	}

}
