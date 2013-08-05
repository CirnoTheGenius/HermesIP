package com.tenko.asm;

import java.net.SocketAddress;
import net.minecraft.server.v1_5_R3.*;

//Do not use. Meant for compiling to ASM.
public class ServerNetworkManagerASM implements INetworkManager {
	
	@Override
	public void a(){}

	@Override
	public void a(Connection arg0){}

	@Override
	public void a(String arg0, Object... arg1){}

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
	public void queue(Packet arg0){}

}