package com.tenko.functions.listeners;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.tenko.FriendlyWall;
import com.tenko.functions.Function;

public abstract class TListener extends Function {

	private String baseName;
	private final String yamlFileName;
	private final String listName;
	protected final FileConfiguration config;
	private final File configFile;

	public TListener(String name, String functionName, String yaml, String listName){
		this.baseName = name;
		this.yamlFileName = yaml;
		this.listName = listName;
		this.config = YamlConfiguration.loadConfiguration(configFile = new File(getFunctionDirectory(functionName), this.yamlFileName));
	}

	public abstract boolean callMeMaybe(CommandSender cs, String[] args);
	
	public void registerCommands(){
		FriendlyWall.getCommandRegister().registerCommand(baseName + "add", new CommandExecutor(){
			@Override
			public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
				if(cs.isOp()){
					if(!callMeMaybe(cs, args)){
						return false;
					}

					if(args.length == 0){
						cs.sendMessage(ChatColor.RED + "You must provide an argument!");
						return true;
					}

					List<String> things = config.getStringList(listName);
					if(things.contains(args[0])){
						cs.sendMessage(ChatColor.RED + "That element is already listed!");
						return true;
					}
					things.add(args[0]);
					config.set(listName, things);
					cs.sendMessage(ChatColor.BLUE + "Added element!");

					try {
						config.save(configFile);
						config.load(configFile);
					} catch (IOException | InvalidConfigurationException e){
						cs.sendMessage(ChatColor.RED + "Something happened. Tell Remi to check server log. Remi, check server log.");
						e.printStackTrace();
					}
				} else {
					cs.sendMessage("Unknown command. Type \"/help\" for help.");
					return true;
				}
				return false;
			}
		});

		FriendlyWall.getCommandRegister().registerCommand(baseName + "rem", new CommandExecutor(){
			@Override
			public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
				if(cs.isOp()){
					if(args.length == 0){
						cs.sendMessage(ChatColor.RED + "You must provide an argument!");
						return true;
					}

					List<String> things = config.getStringList(listName);
					if(!things.contains(args[0])){
						cs.sendMessage(ChatColor.RED + "That element isn't listed!");
						return true;
					}
					things.remove(args[0]);
					config.set(listName, things);
					cs.sendMessage(ChatColor.BLUE + "Removed element!");

					try {
						config.save(configFile);
						config.load(configFile);
					} catch (IOException | InvalidConfigurationException e){
						cs.sendMessage(ChatColor.RED + "Something happened. Tell Remi to check server log. Remi, check server log.");
						e.printStackTrace();
					}
				} else {
					cs.sendMessage("Unknown command. Type \"/help\" for help.");
					return true;
				}
				return false;
			}
		});

		FriendlyWall.getCommandRegister().registerCommand(baseName + "list", new CommandExecutor(){
			@Override
			public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
				if(cs.isOp()){
					List<String> things = config.getStringList(listName);
					cs.sendMessage(ChatColor.GOLD + "Worlds/Players/IP's/Whatever we're looking at:");
					for(String s : things){
						cs.sendMessage(ChatColor.BLUE + "- " + s);
					}
				} else {
					cs.sendMessage("Unknown command. Type \"/help\" for help.");
					return true;
				}
				return false;
			}
		});
	}

}
