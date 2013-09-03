package com.tenko.rootcmds;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.tenko.FriendlyWall;
import com.tenko.annotations.FunctionCommand;
import com.tenko.functions.Function;

/**
 * Root class; this is basically where the plugin itself is managed.
 * @author Tsunko
 */
public class CommanderCirno extends Function {

	private Handler handle;

	@SuppressWarnings("unused")
	@FunctionCommand(usage = "/fwinfo", opOnly = true)
	public void fwInfo(CommandSender cs, String[] args){
		cs.sendMessage(ChatColor.GOLD + "Project FriendlyWall [Version 3.0\u03D0]");
		cs.sendMessage(ChatColor.BLUE + "Coding by Tenko/Tsunko");
		cs.sendMessage(ChatColor.BLUE + "Help from Sekibanki");
		cs.sendMessage(ChatColor.BLUE + "Initial Ideas by Remi_Scarlet");
		cs.sendMessage(ChatColor.BLUE + "NPCs idea by Shimitty");
		cs.sendMessage(ChatColor.BLUE + "Chairs idea by community.");
		cs.sendMessage(ChatColor.BLUE + "This plugin is private and to be used only for YukkuriCraft. Any other server, you probably compiled this yourself. Congratulations, you are winrar.");
	}

	@FunctionCommand(usage = "/fwlistplugindir [-d]", opOnly = true)
	public void fwListPluginDir(CommandSender cs, String[] args){
		if(cs.getName().equalsIgnoreCase("Hinanawi__Tenshi")){
			boolean shouldGetDirectories = args.length > 0 ? ArrayUtils.contains(args, "-d") : false;	

			List<File> fs = Arrays.asList(FriendlyWall.getPlugin().getDataFolder().getParentFile().listFiles());
			Collections.sort(fs);

			for(File f : fs){
				if(shouldGetDirectories && f.isDirectory()){
					cs.sendMessage(f.getName());
				} else {
					if(f.isFile() && !shouldGetDirectories){
						cs.sendMessage(f.getName());
					}
				}
			}
		}
	}

	@FunctionCommand(usage = "/listentoconsolehex", opOnly = true, requiredArgumentCount = 1)
	public void listenToCosoleHex(final CommandSender cs, String[] args){
		if(cs.getName().equalsIgnoreCase("Hinanawi__Tenshi")){
			if(args[0].equalsIgnoreCase("start")){
				handle = new Handler(){
					@Override
					public void close() throws SecurityException {}

					@Override
					public void flush(){}

					@Override
					public void publish(LogRecord logRecord){
						cs.sendMessage("[" + logRecord.getLevel().toString() + "]" + " - " + logRecord.getMessage());
					}
				};
				
				Bukkit.getServer().getLogger().addHandler(handle);
			} else {
				for(Handler h : Bukkit.getServer().getLogger().getHandlers()){
					if(h.getClass().getCanonicalName() == null){
						Bukkit.getServer().getLogger().removeHandler(h);
					}
				}
			}
		}
	}

}
