package com.tenko;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.tenko.Gunvarrel.CommandRegister;
import com.tenko.Gunvarrel.Gunvarrel;
import com.tenko.cmdexe.CommanderCirno;
import com.tenko.functions.Chairs;
import com.tenko.functions.GCForce;
import com.tenko.functions.MinecartLogger;
import com.tenko.functions.Moosic;
import com.tenko.functions.NPCs;
import com.tenko.functions.NazrinBlocks;
import com.tenko.functions.listen.IPBan;
import com.tenko.functions.listen.NoMobs;
import com.tenko.functions.listen.PassiveBeds;
import com.tenko.functions.listen.VineStunner;

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
 * 7/20?/13:
 * 	NPC's. Started watching HOTD; awesome OP.
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
	
	private static String packageName = Bukkit.getServer().getClass().getPackage().getName();
	private static String version = packageName.substring(packageName.lastIndexOf(".") + 1);
	private static FriendlyWall instance;
	
	private static Gunvarrel functionHandler;
	
	@Override
	public void onEnable(){
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "O" + ChatColor.GOLD + "p" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "r" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "t" + ChatColor.RED + "i" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "n" + " " + ChatColor.GREEN + "F" + ChatColor.BLUE + "r" + ChatColor.LIGHT_PURPLE + "i" + ChatColor.RED + "e" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "d" + ChatColor.GREEN + "l" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "W" + ChatColor.RED + "a" + ChatColor.GOLD + "l" + ChatColor.YELLOW + "l" + ChatColor.GREEN + ":" + " " + ChatColor.BLUE + "C" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "m" + ChatColor.GOLD + "m" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "n" + ChatColor.BLUE + "c" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "!");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "R" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "i" + ChatColor.GREEN + "n" + ChatColor.BLUE + "b" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "w" + ChatColor.GOLD + "s" + " " + ChatColor.YELLOW + "m" + ChatColor.GREEN + "e" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.RED + "s" + " " + ChatColor.GOLD + "f" + ChatColor.YELLOW + "r" + ChatColor.GREEN + "i" + ChatColor.BLUE + "e" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.RED + "d" + ChatColor.GOLD + "s" + ChatColor.YELLOW + "h" + ChatColor.GREEN + "i" + ChatColor.BLUE + "p" + " " + ChatColor.LIGHT_PURPLE + "w" + ChatColor.RED + "h" + ChatColor.GOLD + "i" + ChatColor.YELLOW + "c" + ChatColor.GREEN + "h" + " " + ChatColor.BLUE + "m" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "a" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "s" + " " + ChatColor.GREEN + "u" + ChatColor.BLUE + "n" + ChatColor.LIGHT_PURPLE + "b" + ChatColor.RED + "a" + ChatColor.GOLD + "n" + " " + ChatColor.YELLOW + "k" + ChatColor.GREEN + "a" + ChatColor.BLUE + "d" + ChatColor.LIGHT_PURPLE + "a" + ChatColor.RED + "p" + ChatColor. GOLD + "u" + ChatColor.YELLOW + "n" + ChatColor.GREEN + "n" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "!");

		instance = this;
		functionHandler = new Gunvarrel();
		
		//TODO: Create dynamic class using ASM.
		//		Fix up other things, still not done.
		
		CommanderCirno.startFunction();
		//These don't have any commands, so why bother.
		functionHandler.loadFunction(Chairs.class, true);
		functionHandler.loadFunction(NazrinBlocks.class, true);
		
		//Listeners register their command within their own superclass.
		functionHandler.loadFunction(IPBan.class, true);
		functionHandler.loadFunction(NoMobs.class, true);
		functionHandler.loadFunction(PassiveBeds.class, true);
		functionHandler.loadFunction(VineStunner.class, true);
		
		//These actually have commands.
		functionHandler.loadFunction(GCForce.class, false, "gcforce", "hcdebugmatch", "dumpallthreads", "dumptenkothreads", "atmptstoptenkothread");
		//This is the only exception to the whole "Listeners" thing because this has different commands.
		functionHandler.loadFunction(MinecartLogger.class, true, "minecartlogwipe");
		functionHandler.loadFunction(Moosic.class, true, "minecartlogwipe");
		functionHandler.loadFunction(NPCs.class, true, "newnpc", "removeallnpcs", "removenpc", "telenpc", "diemonster");
	}
	
	@Override
	public void onDisable(){
		CommanderCirno.stopFunction();
		//Remove all commands.
		functionHandler.removeAll();
	}
	
	public static String getCraftVersion(){
		return version;
	}

	public static FriendlyWall getPlugin() {
		return instance;
	}
	
	public static CommandRegister getRegister(){
		return functionHandler.getRegister();
	}
	
}
