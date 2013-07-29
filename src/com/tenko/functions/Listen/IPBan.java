package com.tenko.functions.listen;

import java.net.InetAddress;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class IPBan extends TListener {
	
	private final static String functionName = "HermesIP";
	private final static String yamlName = "banned";
	private final static String listName = "banned-ips";

	public IPBan(){
		super("ipb", functionName, yamlName, listName);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void StopRightThere(PlayerLoginEvent criminalScum){
		if(isBanned(criminalScum.getAddress())){
			criminalScum.disallow(Result.KICK_BANNED, "Internal exception: java.net.SocketException: Connection reset");
		}
	}

	public boolean isBanned(InetAddress address) {
		List<String> bans = config.getStringList(listName);

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

	//I'm an idiot for making this function.
	public static boolean validIP(String ip) {
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
}
