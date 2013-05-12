package com.tenko.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.tenko.FriendlyWall;

public class Guard implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void StopRightThere(PlayerLoginEvent criminalScum){
		if(FriendlyWall.getPlugin().isBanned(criminalScum.getAddress())){
			criminalScum.disallow(Result.KICK_BANNED, "Timed out.");
		}
	}
	
}
