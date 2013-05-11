package com.tenko.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.tenko.FriendlyWall;

public class Guard implements Listener {
	
	public void StopRightThereCriminalScumPayTheCourtAFineOrServeYourSentenceYourStolenGoodsAreNowForfeit(PlayerLoginEvent e){
		if(FriendlyWall.getPlugin().isBanned(e.getAddress())){
			e.disallow(Result.KICK_BANNED, "java.net.SocketException: TCP error");
		}
	}
	
}
