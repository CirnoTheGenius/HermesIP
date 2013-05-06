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
			e.disallow(Result.KICK_BANNED, "This IP has been blocked this server. If you believe that this is an error, please contact the server administrator or post under Ban Appeals at www.yukkuricraft.net");
		}
	}
	
}
