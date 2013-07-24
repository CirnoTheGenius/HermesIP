package com.tenko;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.tenko.bot.EntityMika;
import com.tenko.cmdexe.CommanderCirno;
import com.tenko.objs.TenkoCmd;

import com.tenko.functions.Chairs;
import com.tenko.functions.GCForce;
import com.tenko.functions.MinecartLogger;
import com.tenko.functions.Moosic;
import com.tenko.functions.NPCs;
import com.tenko.functions.NazrinBlocks;
import com.tenko.functions.Listen.IPBan;
import com.tenko.functions.Listen.NoMobs;
import com.tenko.functions.Listen.PassiveBeds;
import com.tenko.functions.Listen.VineStunner;

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
 * 6/19/13:
 * 	Made updater; it downloads from my Dropbox account, but by the time this is
 * 	uploaded, I would have removed the line for it. Listening to World's End, one of
 * 	Demetori's arranges of Cosmic Mind.
 * 		http://www.youtube.com/watch?v=JI0jAW7s0YY
 * 	Eh, I don't have anything to put here. 
 * 
 * 7/18/13:
 * 	Made chairs! Took some code from Chairs Reloaded and added my own touch to it.
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
	private static HashMap<String, Location> sitters = new HashMap<String, Location>();
	private static ConcurrentHashMap<String, EntityMika> npcs = new ConcurrentHashMap<String, EntityMika>();
	
	private FunctionManager fm;
	
	private static String packageName = Bukkit.getServer().getClass().getPackage().getName();
	private static String version = packageName.substring(packageName.lastIndexOf(".") + 1);

	@Override
	public void onEnable(){
		try {
			instance = this;

			//This is VERY important. Never remove. Ever. No matter what.
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "O" + ChatColor.GOLD + "p" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "r" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "t" + ChatColor.RED + "i" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "n" + " " + ChatColor.GREEN + "F" + ChatColor.BLUE + "r" + ChatColor.LIGHT_PURPLE + "i" + ChatColor.RED + "e" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "d" + ChatColor.GREEN + "l" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "W" + ChatColor.RED + "a" + ChatColor.GOLD + "l" + ChatColor.YELLOW + "l" + ChatColor.GREEN + ":" + " " + ChatColor.BLUE + "C" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "m" + ChatColor.GOLD + "m" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "n" + ChatColor.BLUE + "c" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "!");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "R" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "i" + ChatColor.GREEN + "n" + ChatColor.BLUE + "b" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "w" + ChatColor.GOLD + "s" + " " + ChatColor.YELLOW + "m" + ChatColor.GREEN + "e" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.RED + "s" + " " + ChatColor.GOLD + "f" + ChatColor.YELLOW + "r" + ChatColor.GREEN + "i" + ChatColor.BLUE + "e" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.RED + "d" + ChatColor.GOLD + "s" + ChatColor.YELLOW + "h" + ChatColor.GREEN + "i" + ChatColor.BLUE + "p" + " " + ChatColor.LIGHT_PURPLE + "w" + ChatColor.RED + "h" + ChatColor.GOLD + "i" + ChatColor.YELLOW + "c" + ChatColor.GREEN + "h" + " " + ChatColor.BLUE + "m" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "a" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "s" + " " + ChatColor.GREEN + "u" + ChatColor.BLUE + "n" + ChatColor.LIGHT_PURPLE + "b" + ChatColor.RED + "a" + ChatColor.GOLD + "n" + " " + ChatColor.YELLOW + "k" + ChatColor.GREEN + "a" + ChatColor.BLUE + "d" + ChatColor.LIGHT_PURPLE + "a" + ChatColor.RED + "p" + ChatColor. GOLD + "u" + ChatColor.YELLOW + "n" + ChatColor.GREEN + "n" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "!");

			//Reflection. wstfgl. help.
			try {
				final Field cmdMap = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer").getDeclaredField("commandMap");
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
			//edit: Found a better way :3
			CommanderCirno.startFunction();

			fm = new FunctionManager();
			fm.add(NoMobs.class);
			fm.add(IPBan.class);
			fm.add(PassiveBeds.class);
			fm.add(VineStunner.class);
			
			fm.add(Chairs.class);
			fm.add(NazrinBlocks.class);
			
			fm.add(MinecartLogger.class, "minecartlogwipe");
			fm.add(Moosic.class, "moosic", "moosicstop", "moosicsound", "moosictrack");
			fm.add(GCForce.class, "gcforce", "hcdebugmatch", "dumpallthreads", "atmptstoptenkothread", "dumptenkothreads");
			fm.add(NPCs.class, "newnpc", "removenpc", "rotatenpc", "removeallnpcs", "walknpc", "telenpc", "lookatme", "npcchat");
		} catch (Exception e){
			Bukkit.getServer().broadcastMessage("Plugin is bork. Tell Tenko: ");
			Bukkit.getServer().broadcastMessage(e.toString());
		}
	}

	@Override
	public void onDisable(){
		this.saveConfig();
		CommanderCirno.stopFunction();
		fm.removeAll();

		for(String s : getSitters().keySet()){
			Chairs.onDisabledStopSitting(Bukkit.getPlayer(s));
		}
		
		for(Entry<String, EntityMika> s : getNpcList().entrySet()){
			s.getValue().die();
		}

	}

	public static void disablePlugin(){
		Bukkit.getPluginManager().disablePlugin(FriendlyWall.getPlugin());
	}

	public static FriendlyWall getPlugin(){
		return instance;
	}

	public static String getVersion(){
		return version;
	}

	public static Map<String, Location> getSitters(){
		return sitters;
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
		if(knownCommands.get(cmd.getName()).toString().startsWith("com.tenko.objs.TenkoCmd")){
			knownCommands.remove(cmd.getName());
		}
	}

	public static File getFunctionDirectory(String s){
		File folder = new File(FriendlyWall.getPlugin().getDataFolder(), s);
		if(!folder.exists()){
			folder.mkdir();
		}
		return folder;
	}
	
	public static FileConfiguration getFunctionYaml(String name, String yaml){
		return YamlConfiguration.loadConfiguration(getFunctionFile(name, yaml));
	}
	
	public static File getFunctionFile(String name, String yaml){
		return new File(getFunctionDirectory(name), yaml + ".yml");
	}
	
	public static ConcurrentHashMap<String, EntityMika> getNpcList(){
		return npcs;
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
