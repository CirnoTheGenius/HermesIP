package com.tenko.functions;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.permissions.Permission;

import com.tenko.FriendlyWall;
import com.tenko.annotations.FunctionCommand;

public class MinecartLogger extends Function {
	
	public MinecartLogger(){
		Bukkit.getPluginManager().addPermission(new Permission("fw.bypassminecart"));
	}
	
	@FunctionCommand(opOnly = true, usage = "/minecartlogwipe [player]")
	public void minecartLogWipe(final CommandSender cs, String[] args){
		if(args.length < 1){
			cs.sendMessage(ChatColor.RED + "Are you sure!? (Type \"minecartlogyes\", otherwise, this command will timeout in 10 seconds.)");
			FriendlyWall.getCommandRegister().registerTemporaryCommand("minecraftwipeyes", cs.getName(), "op", new Runnable(){
				@Override
				public void run(){
					for(File f : getFunctionDirectory("MinecartLogger").listFiles()){
						f.delete();
					}

					cs.sendMessage(ChatColor.BLUE + "Deleted data.");
				}
			}, 200L);
		} else {
			File f = new File(getFunctionDirectory("MinecartLogger"), args[0] + ".yml");
			if(f.delete()){
				cs.sendMessage(ChatColor.RED + "Deleted player data for " + args[0] + "!");
			} else {
				cs.sendMessage(ChatColor.RED + "Failed to delete data! Does the user even exist?");
			}
		}
	}

	@EventHandler
	public void minecartBirthEvent(PlayerInteractEvent e){
		if(e.hasBlock() && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().getItemInHand().getType().toString().contains("MINECART") && e.getClickedBlock().getType().toString().contains("RAIL") && !e.getPlayer().hasPermission("fw.bypassminecart")){
			if(!logEvent(e.getPlayer(), e.getClickedBlock().getWorld().getName(), 1)){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void minecartDeathEvent(VehicleDestroyEvent e){
		if(e.getAttacker() instanceof Player){
			if(e.getVehicle().getType().toString().contains("MINECART") && e.getAttacker().getType() == EntityType.PLAYER && !((Player)e.getAttacker()).getPlayer().hasPermission("fw.bypassminecart")){
				logEvent(((Player)e.getAttacker()), e.getVehicle().getWorld().getName(), -1);
			}
		}
	}

	public boolean logEvent(Player plyr, String worldName, int countNum){
		File configFile = new File(getFunctionDirectory("MinecartLogger"), plyr.getName() + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		int limit = config.getInt(worldName + ".Limit");
		int current = config.getInt(worldName + ".Placed");
		
		if(!configFile.exists()){
			try {
				configFile.createNewFile();
				config.set(worldName + ".Placed", current + countNum);
				config.set(worldName + ".Limit", 100);
				config.save(configFile);
				config.load(configFile);
				return true;
			} catch (IOException | InvalidConfigurationException e){
				e.printStackTrace();
			}
		}
		
		if(countNum > 1 || countNum < -1){
			throw new IllegalArgumentException("countNum must be 1 or -1.");
		} else {
			current += countNum;
			if(current >= limit){
				plyr.sendMessage(ChatColor.RED + "You cannnot do that!");
				return false;
			} else if(current <= 0){
				current = 0;
			}
			config.set(worldName + ".Placed", current);
			
			try {
				config.save(configFile);
				config.load(configFile);
			} catch (IOException | InvalidConfigurationException e){
				e.printStackTrace();
			}
		}

		return true;

	}

}
