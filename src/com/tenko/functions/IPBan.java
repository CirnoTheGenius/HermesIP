package com.tenko.functions;

import java.net.InetAddress;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.tenko.FriendlyWall;
import com.tenko.objs.TenkoCmd;
import com.tenko.yaml.YamlWriter;

public class IPBan extends Function {
	private static TenkoCmd[] cmds;

	public IPBan(){
		//Register event.
		Bukkit.getServer().getPluginManager().registerEvents(this, FriendlyWall.getPlugin());

		//Register commands.
		cmds = new TenkoCmd[]{
				FriendlyWall.registerCommand("banip", this),
				FriendlyWall.registerCommand("pardonip", this),
				FriendlyWall.registerCommand("iplist", this),
		};
	}
	
	public static void stopFunction(){
		for(TenkoCmd cmd : cmds){
			FriendlyWall.unregisterCommand(cmd);
		}
	}
	
	public static void startFunction(){
		new IPBan();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void StopRightThere(PlayerLoginEvent criminalScum){
		if(isBanned(criminalScum.getAddress())){
			criminalScum.disallow(Result.KICK_BANNED, "Internal exception: java.net.SocketException: Connection reset");
		}
	}

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args) {
		if(cs.equals(Bukkit.getConsoleSender())){
			if(c.getName().equalsIgnoreCase("banip")){
				//ew. This looks ugly. Will fix.
				if(args.length < 1){
					result = "You didn't supply an argument!";
				} else if(args[0].startsWith("*")){
					result = "Yes, let's ban everyone's IP address. You're a genius.";
				} else if(validIP(args[0]) == false && !args[0].contains("*")){
					result = "Must be in IPv4 format!";
				} else if(!YamlWriter.writeToList(args[0], "Bans")){
					result = "That IP is already banned!";
				} else {
					result = "Banned IP " + ChatColor.YELLOW + args[0];
					successful = true;
				}
				
				cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[FriendlyWall - IP Ban] " + result);
				return true;
			}

			if(c.getName().equalsIgnoreCase("pardonip")){
				if(args.length < 1){
					result = "You didn't supply an argument!";
				} else if(!YamlWriter.removeFromList(args[0], "Bans")){
					result = "IP \"" + args[0] + "\" isn't banned!";
				} else {
					result = "Pardoned IP " + ChatColor.YELLOW + args[0] + ".";
					successful = true;
				}
				
				cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[FriendlyWall - IP Ban] " + result);
				return true;
			}

			if(c.getName().equalsIgnoreCase("iplist")){
				cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - IP Ban] Currently banned IP's:");
				for(String ban : FriendlyWall.getPlugin().getConfig().getStringList("Bans")){
					cs.sendMessage(ChatColor.YELLOW + "- " + ban);
				}
				return true;
			}
			
		} else {
			cs.sendMessage("Unknown command. Type \"help\" for help.");
			return true;
		}
		return false;
	}

	public boolean isBanned(InetAddress address) {
		List<String> bans = FriendlyWall.getPlugin().getConfig().getStringList("Bans");

		for(String s : bans){
			if(address.getHostAddress().startsWith(s.contains("*") ? s.substring(0, s.indexOf("*")-1) : s)){
				return true;
			}
		}

		return false;
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
