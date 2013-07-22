package com.tenko.functions;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import com.tenko.FriendlyWall;
import com.tenko.objs.TenkoCmd;

//Lazy man's way of implementing two classes.
public abstract class Function implements Listener, CommandExecutor {
	
	protected ArrayList<TenkoCmd> cmds = new ArrayList<TenkoCmd>();
	
	@Override
	public abstract boolean onCommand(CommandSender cs, Command c, String l, String[] args);
	
	public void stopFunction(){
		for(TenkoCmd cmd : cmds){
			FriendlyWall.unregisterCommand(cmd);
		}
	}
	
	public ArrayList<TenkoCmd> getCommands(){
		return cmds;
	}
}