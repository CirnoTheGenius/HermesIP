package com.tenko.functions;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import com.tenko.FriendlyWall;
import com.tenko.objs.TenkoCmd;

//Lazy man's way of implementing two classes.
public abstract class Function implements Listener, CommandExecutor {

	protected ArrayList<TenkoCmd> cmds = new ArrayList<TenkoCmd>();
	protected FileConfiguration config;

	public Function(String name, String configName){
		config = YamlConfiguration.loadConfiguration(getFunctionFile(name, configName));
	}

	//Initalize nothing.
	public Function(){}

	public static File getFunctionFile(String name, String yaml){
		return new File(getFunctionDirectory(name), yaml + ".yml");
	}

	public static File getFunctionDirectory(String s){
		File folder = new File(FriendlyWall.getPlugin().getDataFolder(), s);
		if(!folder.exists()){
			folder.mkdir();
		}
		return folder;
	}

	@Override
	public abstract boolean onCommand(CommandSender cs, Command c, String l, String[] args);

	public void stopFunction(){
		if(cmds.size() > 0){
			for(TenkoCmd cmd : cmds){
				FriendlyWall.getRegister().unregisterCommand(cmd);
			}
		}
	}

	public ArrayList<TenkoCmd> getCommands(){
		return cmds;
	}

}