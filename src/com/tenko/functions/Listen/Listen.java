package com.tenko.functions.Listen;

import com.tenko.FriendlyWall;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import com.tenko.functions.Function;
public abstract class Listen extends Function {

	private final String baseName;
	private final String functionName;
	private final String yaml;
	private final String list;

	public Listen(String naem, String functionNaem, String yamlName, String listName){
		baseName = naem;
		functionName = functionNaem;
		yaml = yamlName;
		list = listName;

		cmds.add(FriendlyWall.registerCommand(baseName + "add", this));
		cmds.add(FriendlyWall.registerCommand(baseName + "rem", this));
		cmds.add(FriendlyWall.registerCommand(baseName + "list", this));

		System.out.println(cmds.get(0));
	}

	public abstract boolean callMeMaybe(CommandSender paramCommandSender, String[] paramArrayOfString);

	public boolean onCommand(CommandSender cs, org.bukkit.command.Command c, String l, String[] args){
		if (cs.isOp()){
			if (c.getName().equalsIgnoreCase(baseName + "add")){
				try {
					if (!callMeMaybe(cs, args)){
						return false;
					}
					FileConfiguration fc = FriendlyWall.getFunctionYaml(functionName, yaml);
					List<String> things = fc.getStringList(list);
					if (things.contains(args[0])){
						cs.sendMessage(ChatColor.RED + "That element is already listed!");
						return true;
					}
					things.add(args[0]);
					fc.set(list, things);
					fc.save(FriendlyWall.getFunctionFile(functionName, yaml));
					fc.load(FriendlyWall.getFunctionFile(functionName, yaml));
					cs.sendMessage(ChatColor.BLUE + "Added element \"" + args[0] + "\"!");
				} catch (ArrayIndexOutOfBoundsException e){
					cs.sendMessage(ChatColor.RED + "Supply an argument!");
					e.printStackTrace();
				} catch (Exception e){
					cs.sendMessage(ChatColor.RED + "Failed to add element!");
					e.printStackTrace();
				}
			} else if (c.getName().equalsIgnoreCase(baseName + "rem")){
				try {
					FileConfiguration fc = FriendlyWall.getFunctionYaml(functionName, yaml);
					List<String> things = fc.getStringList(list);
					if (!things.contains(args[0])){
						cs.sendMessage(ChatColor.RED + "That element isn't added!");
						return true;
					}
					things.remove(args[0]);
					fc.set(list, things);
					fc.save(FriendlyWall.getFunctionFile(functionName, yaml));
					fc.load(FriendlyWall.getFunctionFile(functionName, yaml));
					cs.sendMessage(ChatColor.BLUE + "Removed element \"" + args[0] + "\"!");
				} catch (ArrayIndexOutOfBoundsException e){
					cs.sendMessage(ChatColor.RED + "Supply an argument!");
					e.printStackTrace();
				} catch (Exception e){
					cs.sendMessage(ChatColor.RED + "Failed to add element!");
					e.printStackTrace();
				}
			} else if (c.getName().equalsIgnoreCase(baseName + "list")){
				try {
					FileConfiguration fc = FriendlyWall.getFunctionYaml(functionName, yaml);
					System.out.println(FriendlyWall.getFunctionFile(functionName, yaml));
					List<String> things = fc.getStringList(list);
					cs.sendMessage(ChatColor.GOLD + "[" + functionName + "] Worlds/Players/IP's/Whatever we're looking at:");
					for (String s : things)
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