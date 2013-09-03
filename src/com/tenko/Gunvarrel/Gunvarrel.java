package com.tenko.Gunvarrel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.common.collect.Lists;
import com.tenko.FriendlyWall;
import com.tenko.annotations.FunctionCommand;
import com.tenko.functions.Function;
import com.tenko.functions.listeners.TListener;
import com.tenko.objs.TenkoCmd;

public class Gunvarrel {

	private ArrayList<Function> loadedFunctions;
	private ArrayList<TListener> loadedListeners;
	private CommandRegister register;

	private static boolean created = false;

	private Gunvarrel(){
		loadedFunctions = Lists.newArrayList();
		loadedListeners = Lists.newArrayList();
		register = new CommandRegister();
	}

	public static Gunvarrel createGunvarrel(){
		if(Gunvarrel.created){
			throw new UnsupportedOperationException("Gunvarrel is already created!");
		}

		Gunvarrel.created = true;
		return new Gunvarrel();
	}

	public CommandRegister getCommandRegister(){
		return register;
	}

	public Function getFunction(Class<?> function){
		for(Function f : loadedFunctions){
			if(function == f.getClass()){
				return f;
			}
		}
		return null;
	}

	public void loadFunction(Class<? extends Function> c, boolean isListener){
		try {
			Function newFunction = c.newInstance();

			if(isListener){
				Bukkit.getPluginManager().registerEvents(newFunction, FriendlyWall.getPlugin());
			}

			for(Method m : c.getMethods()){
				if(m.isAnnotationPresent(FunctionCommand.class)){
					FunctionCommand anno = m.getAnnotation(FunctionCommand.class);
					TenkoCmd command = new TenkoCmd(m.getName());
					command.setUsage(ChatColor.RED + "Usage: " + anno.usage());
					command.setExecutor(new MethodExecutor(newFunction, m));

					register.registerCommand(command);
				}
			}

			loadedFunctions.add(newFunction);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void loadListener(Class<? extends TListener> c){
		try {
			TListener newListener = c.newInstance();
			Bukkit.getPluginManager().registerEvents(newListener, FriendlyWall.getPlugin());
			newListener.registerCommands();
			loadedListeners.add(newListener);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
