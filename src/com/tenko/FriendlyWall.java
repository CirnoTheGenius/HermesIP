package com.tenko;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_5_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import com.tenko.cmdexe.CommanderCirno;
import com.tenko.objs.TenkoCmd;

import com.tenko.functions.IPBan;
import com.tenko.functions.MinecartLogger;
import com.tenko.functions.NoMobs;
import com.tenko.functions.PassiveBeds;
import com.tenko.functions.VineStunner;

/*
 * Log entry when I feel like it:
 * Unknown: HermesIP was started.
 * 
 * Unknown: HermesIP's name changed to FriendlyWall.
 * 
 * Unknown: FriendlyWall became a swiss army knife.
 * 
 * 6/13/13: 
 * 	Added up all the lines of FW; apperently, it's 666 lines.
 * 	Ironic, considering the fact that I'm making this plugin for
 * 	"Remi_Scarlet", whose character in Touhou is Remilia Scarlet,
 * 	mistress of the Scarlet Devil Mansion. She was nicknamed
 * 	"The Scarlet Devil" in Touhou 8, Perfect Memento in Strict Sense.
 * 	The number for the devil is 666, so therefore, this on this day, this
 * 	plugin had the same length as Remilia's number.
 * 	
 * 	I also realized that the explanation of the whole "666 lines" was
 * 	absolutely unnecessary.
 * 
 * 	Note: This probably isn't accurate, as there's whitespaces and newlines.
 * 
 * 6/16/13:
 *  Damn that took a long time. Anyways, finished making minecart loggers
 *  and everything. Rewrote a crud load so I can dynamically register commands
 *  and unregister them at will. Currently listening to Rolling Girl,
 *  Akiakane's cover if you ask. Specifically, this one:
 *  	https://www.youtube.com/watch?v=Q7vrU2UxUD8
 *  Not responsible for any homework time lost off of randomly clicking
 *  on random videos on the side. Trust me, we've all been there. I question
 *  myself on why I write these entries. I don't know either. Just felt
 *  like putting it down after that whole 666 line fiasco, which by the way
 *  was most likely to be inaccurate due to the white spaces. Did I ever tell
 *  you that whenever I code on this plugin, I am likely to be re-encoding 
 *  some anime in the background? Currently ramming the CPU usage to the
 *  top of the charts. I don't know why, but Hyperthreading isn't showing
 *  8 cores like it's supposed to. Oh well, that's for something to into for
 *  later.
 * 
 * 9/9/99:
 * 	I accidentally wipe my entire GitHub repository and run 9 magnets
 * 	through my hard drive, losing all source code. I also accidentally
 * 	launch a mysterious executable in which spreads it through the Windows
 * 	RPC protocol by buffer overflowing it with the number 9, then performs
 * 	an SQL injection that changes every username to "Baka". Using this method, 
 * 	it infected 9 million computers and wiped all data after 1.65 hours 
 * 	(99 minutes), kicking the entire century back into the Stone Age. 
 * 	Good game, guys. 
 * 	We tried.
 */
public class FriendlyWall extends JavaPlugin {
	
	private static FriendlyWall instance;
	
	//Hacks.
	private static CommandMap map;
	private static HashMap<String, Command> knownCommands;
	
	@Override
	public void onEnable(){
		instance = this;
		
		//This is VERY important. Never remove. Ever. No matter what.
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "O" + ChatColor.GOLD + "p" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "r" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "t" + ChatColor.RED + "i" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "n" + " " + ChatColor.GREEN + "F" + ChatColor.BLUE + "r" + ChatColor.LIGHT_PURPLE + "i" + ChatColor.RED + "e" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "d" + ChatColor.GREEN + "l" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "W" + ChatColor.RED + "a" + ChatColor.GOLD + "l" + ChatColor.YELLOW + "l" + ChatColor.GREEN + ":" + " " + ChatColor.BLUE + "C" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "m" + ChatColor.GOLD + "m" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "n" + ChatColor.BLUE + "c" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "!");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "R" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "i" + ChatColor.GREEN + "n" + ChatColor.BLUE + "b" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "w" + ChatColor.GOLD + "s" + " " + ChatColor.YELLOW + "m" + ChatColor.GREEN + "e" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.RED + "s" + " " + ChatColor.GOLD + "f" + ChatColor.YELLOW + "r" + ChatColor.GREEN + "i" + ChatColor.BLUE + "e" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.RED + "d" + ChatColor.GOLD + "s" + ChatColor.YELLOW + "h" + ChatColor.GREEN + "i" + ChatColor.BLUE + "p" + " " + ChatColor.LIGHT_PURPLE + "w" + ChatColor.RED + "h" + ChatColor.GOLD + "i" + ChatColor.YELLOW + "c" + ChatColor.GREEN + "h" + " " + ChatColor.BLUE + "m" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "a" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "s" + " " + ChatColor.GREEN + "u" + ChatColor.BLUE + "n" + ChatColor.LIGHT_PURPLE + "b" + ChatColor.RED + "a" + ChatColor.GOLD + "n" + " " + ChatColor.YELLOW + "k" + ChatColor.GREEN + "a" + ChatColor.BLUE + "d" + ChatColor.LIGHT_PURPLE + "a" + ChatColor.RED + "p" + ChatColor. GOLD + "u" + ChatColor.YELLOW + "n" + ChatColor.GREEN + "n" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "!");
	
		//Reflection. wstfgl. help.
		try {
			final Field cmdMap = CraftServer.class.getDeclaredField("commandMap");
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
		
		//Find a way to manage this later.
		CommanderCirno.startFunction();
		IPBan.startFunction();
		PassiveBeds.startFunction();
		NoMobs.startFunction();
		VineStunner.startFunction();
		MinecartLogger.startFunction();
	}
	
	@Override
	public void onDisable(){
		this.saveConfig();
	}
	
	public void disablePlugin(){
		onDisable();
		this.setEnabled(false);
	}
	
	public static FriendlyWall getPlugin(){
		return instance;
	}
	
	/**
	 * Dynamic command registering.
	 */
	public static TenkoCmd registerCommand(String cmd, CommandExecutor exe){
		if(cmd != null && !cmd.isEmpty() && exe != null){
			TenkoCmd theCmd = new TenkoCmd(cmd);
			map.register("", theCmd);
			theCmd.setExecutor(exe);
			return theCmd;
		}
		
		return null;
	}
	
	public static void unregisterCommand(TenkoCmd cmd){
		for(String s : cmd.getAliases()){
			if(knownCommands.containsKey(s) && knownCommands.get(s).toString().contains(FriendlyWall.getPlugin().getName())){
				knownCommands.remove(s);
			}
		}
	}
	
	public static File getFunctionDirectory(String s){
		File folder = new File(FriendlyWall.getPlugin().getDataFolder(), s);
		if(!folder.exists()){
			folder.mkdir();
		}
		return folder;
	}
	
	/**
	 * The wonderful color function.
	 * @param s - The message to be rainbow'd.
	 * @return Friendship and unbanning.
	 */
	public String colorz(String s){
		String[] wat = {
				"RED",
				"GOLD",
				"YELLOW",
				"GREEN",
				"BLUE",
				"LIGHT_PURPLE"
		};
		
		int i=0;
		StringBuffer b = new StringBuffer();
		for(char c : s.toCharArray()){
			if(c == ' '){
				b.append("\" \" + ");
				continue;
			}
			if(i >= wat.length){
				i = 0;
			}
			b.append("ChatColor." + wat[i] + " + ");
			b.append("\"" + c + "\"");
			b.append(" + ");
			i++;
		}
		return b.toString();
	}
	
}
