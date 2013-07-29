package com.tenko.Gunvarrel;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import com.tenko.FriendlyWall;
import com.tenko.functions.Function;

public class Gunvarrel {

	private final ArrayList<Function> functions;
	private CommandRegister cr;
	
	public Gunvarrel(){
		this.functions = new ArrayList<Function>();
		cr = new CommandRegister();
	}

	public void loadFunction(Class<? extends Function> c, boolean isListener, String... commands){
		try {
			Function newFunction = c.newInstance();
			functions.add(newFunction);
			
			//Only register the ones we need to register. 
			//It would be pointless to register a listener for a class that doesn't even use it.
			if(isListener){
				Bukkit.getPluginManager().registerEvents(newFunction, FriendlyWall.getPlugin());
			}

			if(commands != null && commands.length > 0){
				for(String cmd : commands){
					newFunction.getCommands().add(cr.registerCommand(cmd, newFunction));
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void loadFunction(Class<? extends Function> c, String... commands){
		loadFunction(c, false, commands);
	}
	
	public void loadFunction(Class<? extends Function> c){
		loadFunction(c, false, (String[])null);
	}
	
	public void loadFunction(Class<? extends Function> c, boolean isListener){
		loadFunction(c, isListener, (String[])null);
	}
	
	public CommandRegister getRegister(){
		return cr;
	}
	
	public void removeAll(){
		for(Function f : functions){
			f.stopFunction();
			try { for(HandlerList l : HandlerList.getHandlerLists()){
				l.unregister(f);
			}} catch (Exception e){}
		}
	}
	
}