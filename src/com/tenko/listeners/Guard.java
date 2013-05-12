package com.tenko.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.tenko.FriendlyWall;

public class Guard implements Listener {
	
	@EventHandler
	public void StopRightThere(PlayerLoginEvent e){
		if(FriendlyWall.getPlugin().isBanned(e.getAddress())){
			e.disallow(Result.KICK_BANNED, "Timed out.");
		}
	}
	
}
