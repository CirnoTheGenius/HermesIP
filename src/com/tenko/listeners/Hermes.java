package com.tenko.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.tenko.HermesIP;

public class Hermes implements Listener {
	
	@EventHandler
	public void loginListener(PlayerLoginEvent e){		
		if(HermesIP.getPlugin().isBanned(e.getAddress())){
			e.disallow(Result.KICK_BANNED, "java.net.IOException: Stream to remote host was cut off abruptly.");
		}
	}
	
}
