package com.tenko.functions;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.event.Listener;

import com.tenko.FriendlyWall;
import com.tenko.objs.TenkoCmd;

public abstract class Function implements Listener {

	protected ArrayList<TenkoCmd> commands = new ArrayList<TenkoCmd>();
	
	public Function(){}

	public static File getFunctionFile(String name, String yaml){
		return new File(getFunctionDirectory(name), yaml + ".yml");
	}

	public static File getFunctionDirectory(String name){		
		File folder = new File(FriendlyWall.getPlugin().getDataFolder(), name);
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder;
	}

	public void stopFunction(){
		if(commands != null && commands.size() > 0){
			for(TenkoCmd cmd : commands){
				FriendlyWall.getCommandRegister().unregisterCommand(cmd);
			}
		}
	}
	
	protected ArrayList<TenkoCmd> getCommands(){
		return commands;
	}

}
