package com.tenko.functions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

//Lazy man's way of implementing two classes.
public abstract class Function implements Listener, CommandExecutor {
	
	boolean successful = false;
	String result = "No result!";
	
	public abstract boolean onCommand(CommandSender cs, Command c, String l, String[] args);
	
}
