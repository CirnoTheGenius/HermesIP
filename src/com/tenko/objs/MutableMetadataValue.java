package com.tenko.objs;

import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.plugin.Plugin;

//My only little mutable metadata.
public class MutableMetadataValue extends LazyMetadataValue {
	
	private Object obj;
	
	public MutableMetadataValue(Plugin p, Object o){
		super(p);
		this.obj = o;
	}
	
	public void setValue(Object o){
		this.obj = o;
	}

	@Override
	public synchronized void invalidate(){
		this.obj = null;
	}

	@Override
	public Object value(){
		return obj;
	}

}
