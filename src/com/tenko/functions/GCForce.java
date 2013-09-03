package com.tenko.functions;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.tenko.annotations.FunctionCommand;

/**
 * Debugging class.
 * @author Tsunko
 */
public class GCForce extends Function {
	
	@SuppressWarnings("unused")
	@FunctionCommand(usage = "/gcforce", opOnly = true)
	public void gcForce(CommandSender cs, String[] args){
		System.gc();
		cs.sendMessage(ChatColor.BLUE + "Invoked garbage collector!");
	}

	@SuppressWarnings("unused")
	@FunctionCommand(usage = "/hcdebugmatch", opOnly = true)
	public void hcDebugMatch(CommandSender cs, String[] args){
		cs.sendMessage(ChatColor.RED + "Hardcore Debugmatch.");
		cs.sendMessage(ChatColor.GOLD + "Avaliable Processors: " + ChatColor.BLUE + Runtime.getRuntime().availableProcessors());
		cs.sendMessage(ChatColor.GOLD + "Max RAM JVM can use: " + ChatColor.BLUE + (Runtime.getRuntime().maxMemory()/1048576) + "MB");
		cs.sendMessage(ChatColor.GOLD + "Free RAM: " + ChatColor.BLUE + (Runtime.getRuntime().freeMemory()/(1048576*1024)) + "GB");
		cs.sendMessage(ChatColor.GOLD + "Total RAM: " + ChatColor.BLUE + (Runtime.getRuntime().totalMemory()/1048576*1024) + "GB");
		cs.sendMessage(ChatColor.GOLD + "Total Threads: " + ChatColor.BLUE + Thread.activeCount());
		cs.sendMessage(ChatColor.GOLD + "Operating System: " + System.getProperty("os.name"));
		cs.sendMessage(ChatColor.GOLD + "Arch: " + System.getProperty("os.arch"));
		cs.sendMessage(ChatColor.GOLD + "OS Version: " + System.getProperty("os.version"));
	}
	
	@SuppressWarnings("unused")
	@FunctionCommand(usage = "/dumpallthreads", opOnly = true)
	public void dumpAllThreads(CommandSender cs, String[] args){
		int count = 0;
		for(Thread t : Thread.getAllStackTraces().keySet()){
			if(t.getClass().getPackage().toString().contains("tenko")){
				cs.sendMessage(ChatColor.GOLD + "==" + t.getName() + "==");
				cs.sendMessage(ChatColor.GOLD + "Thread Class: " + t.getClass());
				cs.sendMessage(ChatColor.GOLD + "Thread Package: " + t.getClass().getPackage().toString());
				cs.sendMessage(ChatColor.GOLD + "Thread ID: " + t.getId());
				cs.sendMessage(ChatColor.GOLD + "Thread State: " + t.getState());
				count++;
			}
		}
		cs.sendMessage(ChatColor.GOLD + "Number of threads running: " + count);
	}
	
	@SuppressWarnings({ "unused", "deprecation" })
	@FunctionCommand(usage = "/stoptenkothreads", opOnly = true)
	public void stoptenkothreads(CommandSender cs, String[] args){
		for(Thread t : Thread.getAllStackTraces().keySet()){
			if(t.getClass().getPackage().toString().contains("tenko")){
				try {
					t.stop();
					cs.sendMessage(ChatColor.GOLD + "Stopped thread " + t.getName());
				} catch (Exception e){
					cs.sendMessage(ChatColor.RED + "Failed for thread ID " + t.getId());
				}
			}
		}
	}
	
	@FunctionCommand(usage = "/forceyukkuri <player name>", opOnly = true, requiredArgumentCount = 1)
	public void forceYukkuri(CommandSender cs, String[] args){
		File f = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "players/"+args[0]+".dat");
		f.delete();
		cs.sendMessage(ChatColor.GOLD + "Attempted to make user " + args[0] + " a yukkuri/infant.");
	}

}