package com.tenko.Gunvarrel;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;

import com.tenko.FriendlyWall;
import com.tenko.objs.TenkoCmd;

public class CommandRegister {
	
	//Hacks.
	private static CommandMap map;
	private static HashMap<String, Command> knownCommands;
	
	public CommandRegister(){
		//Reflection. wstfgl. help.
		try {
			final Field cmdMap = Class.forName("org.bukkit.craftbukkit." + FriendlyWall.getCraftVersion() + ".CraftServer").getDeclaredField("commandMap");
			cmdMap.setAccessible(true);
			map = (CommandMap)cmdMap.get(Bukkit.getServer());
			final Field kwnCmd = map.getClass().getDeclaredField("knownCommands");
			kwnCmd.setAccessible(true);
			knownCommands = (HashMap<String, Command>)kwnCmd.get(map);

			kwnCmd.setAccessible(false);
			cmdMap.setAccessible(false);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public TenkoCmd registerCommand(String cmd, CommandExecutor exe){
		if(cmd != null && !cmd.isEmpty() && exe != null){
			TenkoCmd theCmd = new TenkoCmd(cmd);
			map.register("", theCmd);
			knownCommands.put(cmd, theCmd);
			theCmd.setExecutor(exe);
			return theCmd;
		}
		
		System.out.println("Couldn't register command; it apperently was null in either the command name or command executor.");
		return null;
	}
	
	public void unregisterCommand(TenkoCmd cmd){
		if(knownCommands.get(cmd.getName()).toString().startsWith("com.tenko.objs.TenkoCmd")){
			knownCommands.remove(cmd.getName());
		}
	}
	
}