package com.tenko.functions.listen;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.tenko.FriendlyWall;
import com.tenko.functions.Function;

public abstract class TListener extends Function {

	private static String baseName;
	private final String functionName;
	private final String yaml;
	private final String list;

	public TListener(String naem, String functionNaem, String yamlName, String listName){
		super(functionNaem, yamlName);
		baseName = naem;
		FriendlyWall.getRegister().registerCommand(baseName + "add", this);
		FriendlyWall.getRegister().registerCommand(baseName + "rem", this);
		FriendlyWall.getRegister().registerCommand(baseName + "list", this);
		functionName = functionNaem;
		yaml = yamlName;
		list = listName;
	}

	public abstract boolean callMeMaybe(CommandSender paramCommandSender, String[] paramArrayOfString);

	@Override
	public boolean onCommand(CommandSender cs, org.bukkit.command.Command c, String l, String[] args){
		if(cs.isOp()){
			if(c.getName().equalsIgnoreCase(baseName + "add")){
				try {
					if(!callMeMaybe(cs, args)){
						return false;
					}
					List<String> things = config.getStringList(list);
					if(things.contains(args[0])){
						cs.sendMessage(ChatColor.RED + "That element is already listed!");
						return true;
					}
					things.add(args[0]);
					config.set(list, things);
					config.save(getFunctionFile(functionName, yaml));
					config.load(getFunctionFile(functionName, yaml));
					cs.sendMessage(ChatColor.BLUE + "Added element \"" + args[0] + "\"!");
				} catch (ArrayIndexOutOfBoundsException e){
					cs.sendMessage(ChatColor.RED + "Supply an argument!");
					e.printStackTrace();
				} catch (Exception e){
					cs.sendMessage(ChatColor.RED + "Failed to add element!");
					e.printStackTrace();
				}
			} else if(c.getName().equalsIgnoreCase(baseName + "rem")){
				try {
					List<String> things = config.getStringList(list);
					if(!things.contains(args[0])){
						cs.sendMessage(ChatColor.RED + "That element isn't added!");
						return true;
					}
					things.remove(args[0]);
					config.set(list, things);
					config.save(getFunctionFile(functionName, yaml));
					config.load(getFunctionFile(functionName, yaml));
					cs.sendMessage(ChatColor.BLUE + "Removed element \"" + args[0] + "\"!");
				} catch (ArrayIndexOutOfBoundsException e){
					cs.sendMessage(ChatColor.RED + "Supply an argument!");
					e.printStackTrace();
				} catch (Exception e){
					cs.sendMessage(ChatColor.RED + "Failed to add element!");
					e.printStackTrace();
				}
			} else if(c.getName().equalsIgnoreCase(baseName + "list")){
				try {
					List<String> things = config.getStringList(list);
					cs.sendMessage(ChatColor.GOLD + "[" + functionName + "] Worlds/Players/IP's/Whatever we're looking at:");
					for(String s : things)
						cs.sendMessage(ChatColor.BLUE + "- " + s);
				}
				catch (Exception e){
					cs.sendMessage(ChatColor.RED + "Failed to add element!");
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}