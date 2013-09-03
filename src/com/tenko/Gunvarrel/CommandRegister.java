package com.tenko.Gunvarrel;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.tenko.FriendlyWall;
import com.tenko.objs.TenkoCmd;

public class CommandRegister {
	
	private CommandMap cmdMap;
	private HashMap<String, Command> knownCommands;
	private HashMap<String, String> temporaryCommands = Maps.newHashMap();
	
	public CommandRegister(){
		try {
			Field commandM = Class.forName("org.bukkit.craftbukkit." + FriendlyWall.getCraftBukkitVersion() + ".CraftServer").getDeclaredField("commandMap");
			commandM.setAccessible(true);
			cmdMap = (CommandMap)commandM.get(Bukkit.getServer());
			commandM.setAccessible(false);
			
			final Field kwnCmd = cmdMap.getClass().getDeclaredField("knownCommands");
			kwnCmd.setAccessible(true);
			knownCommands = (HashMap<String, Command>)kwnCmd.get(cmdMap);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public TenkoCmd registerCommand(String command, CommandExecutor executor){
		TenkoCmd cmd = new TenkoCmd(command);
		cmd.setExecutor(executor);
		
		registerCommand(cmd);
		return cmd;
	}
	
	public void registerTemporaryCommand(String command, String commander, final String permission, final Runnable exe, long l){
		final TenkoCmd cmd = new TenkoCmd(command);
		
		cmd.setExecutor(new CommandExecutor(){
			@Override
			public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
				if(temporaryCommands.containsKey(c.getName()) && cs.getName().equals(temporaryCommands.get(c.getName()))){
					exe.run();
					temporaryCommands.remove(c.getName());
				} else {
					if(permission != null && !permission.isEmpty() && ((permission.equalsIgnoreCase("op") && cs.isOp()) || cs.hasPermission(permission))){
						cs.sendMessage(ChatColor.RED + "Someone is already confirming this command.");
					} else {
						cs.sendMessage("Unknown command. Type \"/help\" for help.");
					}
				}
				return true;
			}
		});
		
		temporaryCommands.put(command, commander);
		registerCommand(cmd);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(FriendlyWall.getPlugin(), new Runnable(){
			@Override
			public void run(){
				if(temporaryCommands.containsKey(cmd.getName())){
					Player p = Bukkit.getPlayer(temporaryCommands.get(cmd.getName()));
					
					if(p == null){
						//Assume console.
						Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "Didn't answer in 10 seconds. Assuming \"no\".");
					} else {
						p.sendMessage(ChatColor.BLUE + "Didn't answer in 10 seconds. Assuming \"no\".");						
					}
				}
				
				System.out.println("Unregistering command.");
				unregisterCommand(cmd);
			}
		}, l);
	}
	
	public void registerCommand(TenkoCmd command){
		cmdMap.register("", command);
	}
	
	public void unregisterCommand(TenkoCmd cmd){
		if(knownCommands.get(cmd.getName()) == null){
			System.out.println(cmd.getName() + " returned null!");
			return;
		}
		if(knownCommands.get(cmd.getName()) instanceof TenkoCmd){
			knownCommands.remove(cmd.getName());
		}
	}
	
}
