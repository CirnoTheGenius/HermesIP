package com.tenko.objs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TenkoCmd extends Command {

    private CommandExecutor exe = null;
    
    public TenkoCmd(String name) {
        super(name);
    }

    @Override
	public boolean execute(CommandSender sender, String commandLabel,String[] args) {
        if(exe != null){
            exe.onCommand(sender, this, commandLabel,args);
        }
        return false;
    }
    
    public void setExecutor(CommandExecutor par1){
        this.exe = par1;
    }

}