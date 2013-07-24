package com.tenko.bot.network;

import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.MinecraftServer;
import net.minecraft.server.v1_6_R2.Packet;
import net.minecraft.server.v1_6_R2.PlayerConnection;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;

public class ServerConnection extends PlayerConnection {
	
	public ServerConnection(EntityPlayer entityplayer){
		super(MinecraftServer.getServer(), new ServerNetworkManager(), entityplayer);
	}

	public boolean a(){
		return true;
	}

	public void a(double d0, double d1, double d2, float f, float f1){}

	public boolean b(){
		return false;
	}

	public CraftPlayer getPlayer(){
		return player == null ? null : player.getBukkitEntity();
	}

	public void onUnhandledPacket(Packet p){}

	public void sendPacket(Packet p){}

	public int lowPriorityCount(){
		return 0;
	}
}