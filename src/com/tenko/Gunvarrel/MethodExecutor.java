package com.tenko.Gunvarrel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.tenko.annotations.FunctionCommand;
import com.tenko.functions.Function;

//Fancy.
public class MethodExecutor implements CommandExecutor {
	
	private final Method functionMethod;
	private final Function ref;
	
	public MethodExecutor(Function func, Method fM){
		this.functionMethod = fM;
		this.ref = func;
		
		this.functionMethod.setAccessible(true);
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command c, String label, String[] args){
		try {
			FunctionCommand meta = functionMethod.getAnnotation(FunctionCommand.class);
			
			if((meta.opOnly() && !cs.isOp()) || (!meta.permission().isEmpty() && !cs.hasPermission(meta.permission()))){
				cs.sendMessage("Unknown command. Type \"/help\" for help.");
				return true;
			}
			
			if(!(cs instanceof Player) && meta.playerOnly()){
				cs.sendMessage(ChatColor.RED + "You must be a player to use this command.");
				return true;
			}
			
			if(args.length < meta.requiredArgumentCount()){
				cs.sendMessage(c.getUsage());
				return true;
			}
			
			functionMethod.invoke(ref, cs, args);
			return true;
		} catch (IllegalAccessException e){
			cs.sendMessage(ChatColor.RED + "[Error] Encountered IllegalAccessException. Maybe we didn't have permission to access it?");
		} catch (IllegalArgumentException e){
			cs.sendMessage(ChatColor.RED + "[Error] Encountered IllegalArgumentException. Check if ref and functionMethod were null?");
		} catch (InvocationTargetException e){
			cs.sendMessage(ChatColor.RED + "[Error] Encountered InvocationTargetException. Sounds like you told Remi to do something.");
			e.printStackTrace();
		}
		return false;
	}

}
