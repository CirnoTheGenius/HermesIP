package com.tenko;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_5_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import com.tenko.cmdexe.CommanderCirno;
import com.tenko.objs.TenkoCmd;

import com.tenko.functions.IPBan;
import com.tenko.functions.NoMobs;
import com.tenko.functions.PassiveBeds;
import com.tenko.functions.VineStunner;

/*
 * Achievements:
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
 * 9/9/99:
 * 	I accidentally wipe my entire GitHub repository and run 9 magnets
 * 	through my hard drive, losing all source code. I also accidentally
 * 	launch a mysterious executable in which infected millions of computers
 * 	and wiped all data after 1.65 hours (99 minutes), kicking the entire
 * 	century back into the Stone Age. Good game, guys. We tried.
 */
public class FriendlyWall extends JavaPlugin {
	
	private static FriendlyWall instance;
	
	//Hacks.
	private static CommandMap map;
	
	@Override
	public void onEnable(){
		instance = this;
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "O" + ChatColor.GOLD + "p" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "r" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "t" + ChatColor.RED + "i" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "n" + " " + ChatColor.GREEN + "F" + ChatColor.BLUE + "r" + ChatColor.LIGHT_PURPLE + "i" + ChatColor.RED + "e" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "d" + ChatColor.GREEN + "l" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "W" + ChatColor.RED + "a" + ChatColor.GOLD + "l" + ChatColor.YELLOW + "l" + ChatColor.GREEN + ":" + " " + ChatColor.BLUE + "C" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "m" + ChatColor.GOLD + "m" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "n" + ChatColor.BLUE + "c" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "!");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "R" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "i" + ChatColor.GREEN + "n" + ChatColor.BLUE + "b" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "w" + ChatColor.GOLD + "s" + " " + ChatColor.YELLOW + "m" + ChatColor.GREEN + "e" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.RED + "s" + " " + ChatColor.GOLD + "f" + ChatColor.YELLOW + "r" + ChatColor.GREEN + "i" + ChatColor.BLUE + "e" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.RED + "d" + ChatColor.GOLD + "s" + ChatColor.YELLOW + "h" + ChatColor.GREEN + "i" + ChatColor.BLUE + "p" + " " + ChatColor.LIGHT_PURPLE + "w" + ChatColor.RED + "h" + ChatColor.GOLD + "i" + ChatColor.YELLOW + "c" + ChatColor.GREEN + "h" + " " + ChatColor.BLUE + "m" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "a" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "s" + " " + ChatColor.GREEN + "u" + ChatColor.BLUE + "n" + ChatColor.LIGHT_PURPLE + "b" + ChatColor.RED + "a" + ChatColor.GOLD + "n" + " " + ChatColor.YELLOW + "k" + ChatColor.GREEN + "a" + ChatColor.BLUE + "d" + ChatColor.LIGHT_PURPLE + "a" + ChatColor.RED + "p" + ChatColor. GOLD + "u" + ChatColor.YELLOW + "n" + ChatColor.GREEN + "n" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "!");
	
		//Reflection. wstfgl. help.
		try {
			final Field cmdMap = CraftServer.class.getDeclaredField("commandMap");
			cmdMap.setAccessible(true);
			map = (CommandMap)cmdMap.get(Bukkit.getServer());
		} catch (Exception e){
			e.printStackTrace();
		}
		
		//Find a way to manage this later.
		CommanderCirno.startFunction();
		IPBan.startFunction();
		PassiveBeds.startFunction();
		NoMobs.startFunction();
		VineStunner.startFunction();
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
	public static void registerCommand(String cmd, CommandExecutor exe){
		if(cmd != null && !cmd.isEmpty() && exe != null){
			TenkoCmd theCmd = new TenkoCmd(cmd);
			map.register("", theCmd);
			theCmd.setExecutor(exe);
		}
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
