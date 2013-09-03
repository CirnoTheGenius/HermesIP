package com.tenko.npc.connections;

import java.net.SocketAddress;

import net.minecraft.server.v1_6_R2.Connection;
import net.minecraft.server.v1_6_R2.INetworkManager;
import net.minecraft.server.v1_6_R2.Packet;

public class NullNetworkManager implements INetworkManager {

	@Override
	public void a(){}

	@Override
	public void a(Connection c){}

	@Override
	public void a(String s, Object... o){}

	@Override
	public void b(){}

	@Override
	public void d(){}

	@Override
	public int e(){
		return 0;
	}

	@Override
	public SocketAddress getSocketAddress(){
		return null;
	}

	@Override
	public void queue(Packet p){}

}
