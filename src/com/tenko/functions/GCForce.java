package com.tenko.functions;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import com.tenko.FriendlyWall;

public class GCForce extends Function {

	public Integer id;

	public GCForce(){
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(FriendlyWall.getPlugin(), new Runnable(){

			@Override
			public void run(){
				if(FriendlyWall.exceptionCount > 20){
					Bukkit.broadcastMessage(ChatColor.RED + "A plugin crashed!");
					Bukkit.getScheduler().cancelTask(id);
					unloadPlugin("FriendlyWall", FriendlyWall.getPlugin());
				}
			}

		}, 20, 120);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
		if(cs.isOp()){
			if(c.getName().equalsIgnoreCase("gcforce")){
				System.gc();
				cs.sendMessage(ChatColor.BLUE + "Invoked garbage collector!");
				return true;
			} else if(c.getName().equalsIgnoreCase("hcdebugmatch")){
				cs.sendMessage(ChatColor.RED + "Hardcore Debugmatch.");
				cs.sendMessage(ChatColor.GOLD + "Avaliable Processors: " + ChatColor.BLUE + Runtime.getRuntime().availableProcessors());
				cs.sendMessage(ChatColor.GOLD + "Max RAM JVM can use: " + ChatColor.BLUE + (Runtime.getRuntime().maxMemory()/1048576) + "MB");
				cs.sendMessage(ChatColor.GOLD + "Free RAM: " + ChatColor.BLUE + (Runtime.getRuntime().freeMemory()/(1048576*1024)) + "GB");
				cs.sendMessage(ChatColor.GOLD + "Total RAM: " + ChatColor.BLUE + (Runtime.getRuntime().totalMemory()/1048576*1024) + "GB");
				cs.sendMessage(ChatColor.GOLD + "Total Threads: " + ChatColor.BLUE + Thread.activeCount());
				cs.sendMessage(ChatColor.GOLD + "Operating System: " + System.getProperty("os.name"));
				cs.sendMessage(ChatColor.GOLD + "Arch: " + System.getProperty("os.arch"));
				cs.sendMessage(ChatColor.GOLD + "OS Version: " + System.getProperty("os.version"));
				return true;
			} else if(c.getName().equalsIgnoreCase("dumpallthreads")){
				Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				for(Thread t : threadSet){
					cs.sendMessage(ChatColor.GOLD + "==" + t.getName() + "==");
					cs.sendMessage(ChatColor.GOLD + "Thread Class: " + t.getClass());
					cs.sendMessage(ChatColor.GOLD + "Thread Package: " + t.getClass().getPackage().toString());
					cs.sendMessage(ChatColor.GOLD + "Thread ID: " + t.getId());
					cs.sendMessage(ChatColor.GOLD + "Thread State: " + t.getState());
					cs.sendMessage(ChatColor.GOLD + "Possibility of one of my threads? " + t.getClass().getPackage().toString().contains("tenko"));
				}
				return true;
			} else if(c.getName().equalsIgnoreCase("dumptenkothreads")){
				Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				int i=0;
				for(Thread t : threadSet){
					if(t.getClass().getPackage().toString().contains("tenko")){
						cs.sendMessage(ChatColor.GOLD + "==" + t.getName() + "==");
						cs.sendMessage(ChatColor.GOLD + "Thread Class: " + t.getClass());
						cs.sendMessage(ChatColor.GOLD + "Thread Package: " + t.getClass().getPackage().toString());
						cs.sendMessage(ChatColor.GOLD + "Thread ID: " + t.getId());
						cs.sendMessage(ChatColor.GOLD + "Thread State: " + t.getState());
						i++;
					}
				}
				cs.sendMessage(ChatColor.RED + "Running threads: " + ChatColor.WHITE + i);
				return true;
			} else if(c.getName().equalsIgnoreCase("atmptstoptenkothread")){
				Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				for(Thread t : threadSet){
					if(t.getClass().getPackage().toString().contains("tenko")){
						try {
							t.stop();
							cs.sendMessage(ChatColor.GOLD + "Stopped thread " + t.getName());
						} catch (SecurityException e){
							cs.sendMessage(ChatColor.RED + "Failed for thread ID " + t.getId());
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean unloadPlugin(String plugin, Plugin pl){
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
				if(p.getDescription().getName().equalsIgnoreCase(plugin)){
					man.disablePlugin(p);

					if(pluginList != null && pluginList.contains(pl)){
						System.out.println("Found pluginList. This is good.");
						pluginList.remove(pl);
					} else {
						System.out.println("Maybe a new plugin?");
					}

					if(lookupNames != null && lookupNames.containsKey(plugin)){
						System.out.println("Found lookupNames. This is good.");
						lookupNames.remove(plugin);
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

		Bukkit.broadcastMessage(ChatColor.BLUE + "Successfully unloaded plugin! Update is beginning soon!");
		return true;
	}

}
