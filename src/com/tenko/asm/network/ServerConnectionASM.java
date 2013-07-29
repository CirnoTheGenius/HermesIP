package com.tenko.asm.network;

import net.minecraft.server.v1_6_R2.*;

//Do not use. Meant for compiling to ASM.
public class ServerConnectionASM extends PlayerConnection {
	
	public ServerConnectionASM(EntityPlayer entityplayer){
		super(MinecraftServer.getServer(), new ServerNetworkManagerASM(), entityplayer);
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