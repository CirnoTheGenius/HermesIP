package com.tenko.cmdexe;

import com.tenko.FriendlyWall;
import com.tenko.objs.TenkoCmd;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

public class CommanderCirno implements CommandExecutor{
	
	private static TenkoCmd[] cmds;

	public CommanderCirno(){
		cmds = new TenkoCmd[]{ 
				FriendlyWall.getRegister().registerCommand("fwreload", this), 
				FriendlyWall.getRegister().registerCommand("fwinfo", this), 
				FriendlyWall.getRegister().registerCommand("fwlistplugindir", this),
		};
	}

	public static void startFunction(){
		new CommanderCirno();
	}

	public static void stopFunction(){
		for (TenkoCmd cmd : cmds){
			FriendlyWall.getRegister().unregisterCommand(cmd);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		if(cs.isOp()){
			if(c.getName().equalsIgnoreCase("fwreload")){
				try {
					FriendlyWall.getPlugin().getConfig().load(new File(FriendlyWall.getPlugin().getDataFolder(), "config.yml"));
					cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - Core] Reloaded config!");
				} catch (FileNotFoundException e){
					cs.sendMessage(ChatColor.RED + "[FriendlyWall - Core] The configuration file was not found!");
					e.printStackTrace();
				} catch (IOException e){
					cs.sendMessage(ChatColor.RED + "[FriendlyWall - Core] There was an error while reading the file!");
					e.printStackTrace();
				} catch (InvalidConfigurationException e){
					cs.sendMessage(ChatColor.RED + "[FriendlyWall - Core] The configuration has invalid settings!");
					e.printStackTrace();
				}
			} else if(c.getName().equalsIgnoreCase("fwinfo")){
				cs.sendMessage(ChatColor.GOLD + "Project FriendlyWall [Version 2.1.0\u03D0]");
				cs.sendMessage(ChatColor.BLUE + "Coding by Tenko/Tsunko");
				cs.sendMessage(ChatColor.BLUE + "Help from Sekibanki");
				cs.sendMessage(ChatColor.BLUE + "Initial Ideas by Remi_Scarlet");
				cs.sendMessage(ChatColor.BLUE + "NPCs idea by Shimitty");
				cs.sendMessage(ChatColor.BLUE + "Chairs idea by community.");
				cs.sendMessage(ChatColor.BLUE + "This plugin is private and to be used only for YukkuriCraft. Any other server, you probably compiled this yourself. Congratulations, you are winrar.");
			} else if(c.getName().equalsIgnoreCase("fwlistplugindir")){
				if(args.length > 0 && ArrayUtils.contains(args, "-d")){
					for(File f : FriendlyWall.getPlugin().getDataFolder().getParentFile().listFiles()){
						if(f.isDirectory()){
							cs.sendMessage(f.getName());
						}
					}
				} else {
					for(File f : FriendlyWall.getPlugin().getDataFolder().getParentFile().listFiles()){
						if(!f.isDirectory()){
							cs.sendMessage(f.getName());
						}
					}
				}
			}
		} else {
			cs.sendMessage("Unknown command. Type \"help\" for help.");
			return true;
		}

		return false;
	}
}