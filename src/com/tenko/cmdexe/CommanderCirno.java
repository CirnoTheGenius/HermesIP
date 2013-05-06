package com.tenko.cmdexe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommanderCirno implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command c, String l, String[] args) {
		if(c.getName().equalsIgnoreCase("hreload")){
			ReloadExe.Execute(cs);
			return true;
		}
		
		if(c.getName().equalsIgnoreCase("ipban")){
			IPBanExe.Execute(cs, args);
			return true;
		}
		
		if(c.getName().equalsIgnoreCase("pardonip")){
			IPPardonExe.Execute(cs, args);
			return true;
		}
		
		if(c.getName().equalsIgnoreCase("iplist")){
			IPListExe.Execute(cs);
			return true;
		}
		return false;
	}
	
}
