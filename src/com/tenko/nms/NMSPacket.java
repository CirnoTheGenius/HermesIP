package com.tenko.nms;

import com.tenko.FriendlyWall;
import java.lang.reflect.Field;

public class NMSPacket {
	
	private Object packet;
	private Class<?> nmsPacket;
	private boolean modifiable = true;

	public NMSPacket(String packetName){
		try {
			if(!modifiable){
				throw new IllegalAccessException("Packet is sealed!");
			}
			
			nmsPacket = Class.forName("net.minecraft.server." + FriendlyWall.getVersion() + "." + packetName);
			packet = nmsPacket.newInstance();
		} catch (ClassNotFoundException|InstantiationException|IllegalAccessException e){
			e.printStackTrace();
			packet = null;
			nmsPacket = null;
		}
	}

	public void setField(String fieldName, Object value){
		try {
			if(!modifiable){
				throw new IllegalAccessException("Packet is sealed!");
			}
			
			Field f = packet.getClass().getField(fieldName);
			f.setAccessible(true);
			f.set(packet, value);
			f.setAccessible(false);
		} catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e){
			e.printStackTrace();
		}
	}

	public Object getField(String fieldName){
		try {
			Field f = packet.getClass().getField(fieldName);
			f.setAccessible(true);
			Object s = f.get(packet);
			f.setAccessible(false);
			return s;
		} catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e){
			e.printStackTrace();
		}
		return null;
	}

	public Class<?> getPacketClass(){
		return nmsPacket;
	}

	public Object getPacket(){
		return packet;
	}

	public Object seal(){
		modifiable = false;
		return packet;
	}

	public boolean isUsable(){
		return packet != null;
	}
}
