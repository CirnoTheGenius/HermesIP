package com.tenko.cmdexe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import com.tenko.FriendlyWall;
import com.tenko.objs.TenkoCmd;
import com.tenko.updater.Updater;

public class CommanderCirno implements CommandExecutor {
	private static TenkoCmd[] cmds;
	
	//Base for entire plugin.
	public CommanderCirno(){
		cmds = new TenkoCmd[]{
				FriendlyWall.registerCommand("fwreload", this),
				FriendlyWall.registerCommand("fwinfo", this),
				FriendlyWall.registerCommand("fwupdate", this),
		};
	}
	
	public static void startFunction(){
		new CommanderCirno();
	}
	
	public static void stopFunction(){
		for(TenkoCmd cmd : cmds){
			FriendlyWall.unregisterCommand(cmd);
		}
	}

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		if(cs.equals(Bukkit.getConsoleSender()) || cs.isOp()){
			if(c.getName().equalsIgnoreCase("fwreload")){
				try {
					FriendlyWall.getPlugin().getConfig().load(new File(FriendlyWall.getPlugin().getDataFolder(), "config.yml"));
					cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - Core] Reloaded config!");
				} catch (FileNotFoundException e){
					cs.sendMessage(ChatColor.RED + "[FriendlyWall - Core] The configuration file was not found!");
					e.printStackTrace();
				} catch (IOException e) {
					cs.sendMessage(ChatColor.RED + "[FriendlyWall - Core] There was an error while reading the file!");
					e.printStackTrace();
				} catch (InvalidConfigurationException e) {
					cs.sendMessage(ChatColor.RED + "[FriendlyWall - Core] The configuration has invalid settings!");
					e.printStackTrace();
				}
			} else if(c.getName().equalsIgnoreCase("fwinfo")){
				//Major, Minor, Sub-Minor, Bugfix
				cs.sendMessage(ChatColor.GOLD + "Project FriendlyWall [Version 0.1.8b]");
				cs.sendMessage(ChatColor.BLUE + "Coding by Tenko/Tsunko");
				cs.sendMessage(ChatColor.BLUE + "Idea by Remi_Scarlet");
				cs.sendMessage(ChatColor.BLUE + "This plugin is private and to be used only for YukkuriCraft. Any other server, you probably compiled this yourself. Congratulations, you are winrar.");
			} else if(c.getName().equalsIgnoreCase("fwupdate")){
				if(args.length < 1){
					cs.sendMessage("[FriendlyWall - Updater] Needs parameter!");
					return true;
				}
				Updater.update(args[0]);
			}
		} else {
			cs.sendMessage("Unknown command. Type \"help\" for help.");
			return true;
		}

		return false;
	}

}
