package com.tenko.asm;

import java.util.ArrayList;

import net.minecraft.server.v1_5_R3.DataWatcher;
import net.minecraft.server.v1_5_R3.WatchableObject;

public class ChairWatcherASM extends DataWatcher {

	private byte metadata;

	public ChairWatcherASM(byte i) {
		this.metadata = i;
	}

	@Override
	public ArrayList<Object> b(){
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(new WatchableObject(0, 0, this.metadata));
		return list;
	}

}
