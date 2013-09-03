package com.tenko.npc.connections;

import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.INetworkManager;
import net.minecraft.server.v1_6_R2.MinecraftServer;
import net.minecraft.server.v1_6_R2.Packet;
import net.minecraft.server.v1_6_R2.PlayerConnection;

public class NullConnection extends PlayerConnection {

	public NullConnection(INetworkManager inm, EntityPlayer p) {
		super(MinecraftServer.getServer(), inm, p);
	}
	
	@Override
	public boolean a(){
		return true;
	}

	@Override
	public void a(double d0, double d1, double d2, float f, float f1){}

	@Override
	public boolean b(){
		return false;
	}

	@Override
	public void onUnhandledPacket(Packet p){}

	@Override
	public void sendPacket(Packet p){}

	@Override
	public int lowPriorityCount(){
		return 0;
	}

}
