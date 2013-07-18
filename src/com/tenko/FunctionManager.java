package com.tenko;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import com.tenko.functions.Function;

//Just an easier way of managing things.
public class FunctionManager {

	private final ArrayList<Function> functions;

	public FunctionManager(){
		functions = new ArrayList<Function>();
	}

	public boolean add(Class<? extends Function> f, String... commands){
		try {
			Function newFunction = f.newInstance();
			functions.add(newFunction);

			Bukkit.getPluginManager().registerEvents(newFunction, FriendlyWall.getPlugin());

			if(commands != null && commands.length > 0){
				for(String cmd : commands){
					newFunction.getCommands().add(FriendlyWall.registerCommand(cmd, newFunction));
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void removeAll(){
		for(Function f : functions){
			f.stopFunction();
			for(HandlerList l : HandlerList.getHandlerLists()){
				l.unregister(f);
			}
		}
	}
}