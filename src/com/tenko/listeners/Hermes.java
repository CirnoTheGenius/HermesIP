package com.tenko.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.tenko.YCFriendlyWall;

public class Hermes implements Listener {
	
	@EventHandler
	public void loginListener(PlayerLoginEvent e){		
		if(YCFriendlyWall.getPlugin().isBanned(e.getAddress())){
			e.disallow(Result.KICK_BANNED, "java.net.SocketException: TCP error");
		}
	}
	
}
