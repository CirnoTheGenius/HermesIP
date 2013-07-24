package com.tenko.objs;

import com.tenko.FriendlyWall;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class ConfirmationCommand implements CommandExecutor {
	
	private final String confirmingPlayer;
	private final TenkoCmd command;
	private final Runnable doCommand;
	private boolean isConfirming;

	public ConfirmationCommand(String cmdName, final String conPlayer, Runnable r){
		command = FriendlyWall.registerCommand(cmdName, this);
		confirmingPlayer = conPlayer;

		Bukkit.getScheduler().scheduleSyncDelayedTask(FriendlyWall.getPlugin(), new Runnable(){
			public void run(){
				if(isConfirming){
					Bukkit.getServer().getPlayer(conPlayer).sendMessage(ChatColor.BLUE + "Didn't answer in 10 seconds. Assuming \"no\".");
				}
				
				FriendlyWall.unregisterCommand(command);
				isConfirming = false;
			}
		}, 200L);

		doCommand = r;
	}

	public boolean onCommand(CommandSender cs, Command c, String l, String[] a){
		if(cs.getName().equalsIgnoreCase(confirmingPlayer) && cs.isOp() && c.getName().equalsIgnoreCase(command.getName())){
			cs.sendMessage(ChatColor.BLUE + "Executing command!");
			doCommand.run();
		}
		return false;
	}
}