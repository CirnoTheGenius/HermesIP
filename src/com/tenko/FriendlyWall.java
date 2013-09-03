package com.tenko;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import sun.misc.Unsafe;

import com.tenko.Gunvarrel.CommandRegister;
import com.tenko.Gunvarrel.Gunvarrel;
import com.tenko.functions.*;
import com.tenko.functions.listeners.*;
import com.tenko.rootcmds.CommanderCirno;

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
 * 7/31/13:
 * 	I like the regular guns, the 360 noscopes, the FaZe, the silent shots...
 * 	
 * 	Yeah, I really have nothing to do here. Anyways... Been listening to Akiakane
 * 	again. Can't seem to see why I've looped around Rolling Girl about 70ish times.
 * 	wowaka is such an awesome person. Then, I discovered Tightson. Holy crap, this
 * 	guy. He's a rapper; you probably thought "oh jeeze, rap. sex, drugs, killings,
 * 	drivebys." Wrong. Definitly wrong. Look at the lyrics of his cover; it's quite
 * 	depressing, much like Rolling Girl's original lyrics.
 * 
 * 	Now back on the topic of programming, NPC's can talk now. Since the community
 *  wanted it, I decided to give it a visual-novel enviroment with options and all.
 *  There's no cutscenes though, and there's barely anything that it can do when you
 *  select an option besides throw back a statement at you. Damn, this plugin is only
 *  2 months old, and look how much it's expanded.
 *  
 * 8/4/13:
 * 	"K-ON may not be everybody's cup of tea, but for the girls of Sakuragaoka High
 *   School, it is their tea party." - Veronin.
 *  My god that quote.
 *  
 * 8/17/13:
 * 	Asdf, bored. Sick. Runny nose. Added jails and offline teleporting and things.
 * 	Fixed some bugs, idk. I got free games for drawing a picture of Stephen Hawking
 * 	on a VOCALOID box... I'm probably going to get shot at by Hawking fans.
 * 
 * 8/28/13:
 * 	"The Great Refactor".
 * 
 * 9/1/13:
 * 	Still "The Great Refactor"'ing this entire plugin. Now that I really look deep
 * 	into the source, holy crap, this plugin is like... all my work shoved into a
 * 	little box. No, that's what exactly what it is. Anyways, Mou ikkai mou ikkai~
 *  Watashi wa kyou mo korogarimasu! Ehehe. Still listening Akiaken's awesome
 *  cover. I started sophomore year, none of my teachers are particularlly bad.
 *  My Algebra II teacher assiagns a lot of homework, but I'm sure it's for our own
 *  good. YC, amazingly, has only crafted twice due to this plugin. Oopse. I wrote
 *  crafted. I meant crashed. Currently, this plugin has over 2 thousand lines.
 *  Most of it was written by hand. NPC's are becoming more and more diverse.
 *  Currently slowing the entire network to a crawl; my Guilty Crown must be
 *  downloaded. Ha. Take that internet. Make sure you skyrocket our bills. Hahaha.
 *  I should write more of these later; looking through them makes me giggle like
 *  a schoolgirl.
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

	private static final String CRAFTBUKKIT_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
	private static FriendlyWall instance;
	private static Gunvarrel functionLoader;
	
	public static int ExceptionCount;

	//At this point, I'm going insane.
	private static Unsafe theUnsafe;
	
	@Override
	public void onEnable(){
		instance = this;
		Bukkit.getPluginManager().getPermissions().remove(new Permission("fw.bypassminecart"));
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "O" + ChatColor.GOLD + "p" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "r" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "t" + ChatColor.RED + "i" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "n" + " " + ChatColor.GREEN + "F" + ChatColor.BLUE + "r" + ChatColor.LIGHT_PURPLE + "i" + ChatColor.RED + "e" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "d" + ChatColor.GREEN + "l" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "W" + ChatColor.RED + "a" + ChatColor.GOLD + "l" + ChatColor.YELLOW + "l" + ChatColor.GREEN + ":" + " " + ChatColor.BLUE + "C" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "m" + ChatColor.GOLD + "m" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "n" + ChatColor.BLUE + "c" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "!");
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "R" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "i" + ChatColor.GREEN + "n" + ChatColor.BLUE + "b" + ChatColor.LIGHT_PURPLE + "o" + ChatColor.RED + "w" + ChatColor.GOLD + "s" + " " + ChatColor.YELLOW + "m" + ChatColor.GREEN + "e" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.RED + "s" + " " + ChatColor.GOLD + "f" + ChatColor.YELLOW + "r" + ChatColor.GREEN + "i" + ChatColor.BLUE + "e" + ChatColor.LIGHT_PURPLE + "n" + ChatColor.RED + "d" + ChatColor.GOLD + "s" + ChatColor.YELLOW + "h" + ChatColor.GREEN + "i" + ChatColor.BLUE + "p" + " " + ChatColor.LIGHT_PURPLE + "w" + ChatColor.RED + "h" + ChatColor.GOLD + "i" + ChatColor.YELLOW + "c" + ChatColor.GREEN + "h" + " " + ChatColor.BLUE + "m" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.RED + "a" + ChatColor.GOLD + "n" + ChatColor.YELLOW + "s" + " " + ChatColor.GREEN + "u" + ChatColor.BLUE + "n" + ChatColor.LIGHT_PURPLE + "b" + ChatColor.RED + "a" + ChatColor.GOLD + "n" + " " + ChatColor.YELLOW + "k" + ChatColor.GREEN + "a" + ChatColor.BLUE + "d" + ChatColor.LIGHT_PURPLE + "a" + ChatColor.RED + "p" + ChatColor. GOLD + "u" + ChatColor.YELLOW + "n" + ChatColor.GREEN + "n" + ChatColor.BLUE + "y" + ChatColor.LIGHT_PURPLE + "!");

		functionLoader = Gunvarrel.createGunvarrel();

		functionLoader.loadFunction(CommanderCirno.class, false);
		functionLoader.loadFunction(ExampleFunction.class, false);
		
		functionLoader.loadFunction(GCForce.class, false);
		functionLoader.loadFunction(Jail.class, true);
		functionLoader.loadFunction(LightMap.class, false);
		functionLoader.loadFunction(MinecartLogger.class, true);
		functionLoader.loadFunction(NazrinBlocks.class, true);
		functionLoader.loadFunction(NPCs.class, true);
		functionLoader.loadFunction(OfflineTP.class, true);

		functionLoader.loadListener(IPBan.class);
		functionLoader.loadListener(NoMobs.class);
		functionLoader.loadListener(PassiveBeds.class);
		functionLoader.loadListener(VineStunner.class);

		try {
			Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
			unsafeField.setAccessible(true);
			theUnsafe = (Unsafe)unsafeField.get(null);
		} catch (Exception e){
			System.out.println("Couldn't obtain an unsafe instance. Ignore this, this is purely for debugging.");
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			@Override
			public void run(){
				if(ExceptionCount > 20){
					Bukkit.broadcastMessage(ChatColor.RED + "A plugin crashed!");
					unloadPlugin(FriendlyWall.getPlugin());
				}
			}
		}, 20, 20);
	}

	@Override
	public void onDisable(){
		//i r teh progrummer
		((NPCs)functionLoader.getFunction(NPCs.class)).endFunction();
		
		
		//Remove all references.
		instance = null;
		functionLoader = null;
		//Garbage collect.
		System.gc();
	}
	
	public static Unsafe getUnsafe(){
		return theUnsafe;
	}

	public static Gunvarrel getGunvarrel(){
		return functionLoader;
	}

	public static CommandRegister getCommandRegister(){
		return functionLoader.getCommandRegister();
	}

	public static FriendlyWall getPlugin(){
		return instance;
	}

	public static String getCraftBukkitVersion(){
		return CRAFTBUKKIT_VERSION;
	}
	
	public boolean unloadPlugin(Plugin pl){
		try {
			SimplePluginManager man = (SimplePluginManager)Bukkit.getServer().getPluginManager();

			if(man == null){
				Bukkit.broadcastMessage(ChatColor.RED + "Plugin " + ChatColor.BOLD + "majorly" + ChatColor.RESET + ChatColor.RED + " crashed. Technical Info: \"man was null\".");
				return false;
			}

			Field pluginListF = SimplePluginManager.class.getDeclaredField("plugins");
			pluginListF.setAccessible(true);

			Field lookupNamesF = SimplePluginManager.class.getDeclaredField("lookupNames");
			lookupNamesF.setAccessible(true);

			Field commandMapF = SimplePluginManager.class.getDeclaredField("commandMap");
			commandMapF.setAccessible(true);

			List<Plugin> pluginList = (List<Plugin>)pluginListF.get(man);
			Map<String, Plugin> lookupNames = (Map<String, Plugin>)lookupNamesF.get(man);
			SimpleCommandMap cmdMap = (SimpleCommandMap)commandMapF.get(man);

			if(cmdMap == null){
				Bukkit.broadcastMessage(ChatColor.RED + "Plugin " + ChatColor.BOLD + "majorly" + ChatColor.RESET + ChatColor.RED + " crashed. Technical info: cmdMap was null somehow!");
				return false;
			}

			Field knownCommandsF = cmdMap.getClass().getDeclaredField("knownCommands");
			knownCommandsF.setAccessible(true);
			Map<String, Command> knownCommands = (Map<String, Command>)knownCommandsF.get(cmdMap);

			for(Plugin p : man.getPlugins()){
				if(p.getDescription().getName().equalsIgnoreCase(pl.getName())){
					man.disablePlugin(p);

					if(pluginList != null && pluginList.contains(pl)){
						System.out.println("Found pluginList. This is good.");
						pluginList.remove(pl);
					} else {
						System.out.println("Maybe a new plugin?");
					}

					if(lookupNames != null && lookupNames.containsKey(pl.getName())){
						System.out.println("Found lookupNames. This is good.");
						lookupNames.remove(pl.getName());
					}

					Iterator<Entry<String, Command>> it = knownCommands.entrySet().iterator();
					while(it.hasNext()){
						Entry<String, Command> e = it.next();
						if(e.getValue() instanceof PluginCommand){
							PluginCommand cmd = (PluginCommand)e.getValue();

							if(cmd.getPlugin() == p){
								cmd.unregister(cmdMap);
								it.remove();
							}
						}
					}
				}
			}
		} catch (Exception e){
			Bukkit.broadcastMessage(ChatColor.RED + "Plugin " + ChatColor.BOLD + "majorly" + ChatColor.RESET + ChatColor.RED + " crashed. Technical info: " + e);
			return false;
		}

		Bukkit.broadcastMessage(ChatColor.BLUE + "Successfully unloaded plugin!");
		return true;
	}

}