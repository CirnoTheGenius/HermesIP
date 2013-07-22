package com.tenko.objs.nms;

import java.util.ArrayList;

import com.tenko.FriendlyWall;

import net.minecraft.server.v1_6_R2.*;

public class ChairWatcher extends DataWatcher {

    private byte metadata;
    
    public ChairWatcher(byte i) {
        this.metadata = i;
    }

    @Override
    public ArrayList<Object> b(){
        try {
            ArrayList<Object> list = new ArrayList<Object>();
			Class<?> Watchable = Class.forName("net.minecraft.server." + FriendlyWall.getVersion() + ".WatchableObject");
	        list.add(Watchable.getConstructor(int.class, int.class, Object.class).newInstance(0, 0, this.metadata));
	        return list;
		} catch (Exception e) {
			e.printStackTrace();
		}

        return null;
    }
    
}