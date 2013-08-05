package com.tenko.functions;

import com.tenko.FriendlyWall;
import com.tenko.nms.NMSLib;
import com.tenko.objs.TenkoCmd;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

public class MinecartLogger extends Function {

	boolean isConfirming = false;

	@Override
	public boolean onCommand(final CommandSender cs, Command c, String l, String[] args){	
		if(cs.isOp() && c.getName().equalsIgnoreCase("minecartlogwipe")){
			if(args.length < 1){
				cs.sendMessage(ChatColor.RED + "Are you sure!? (Type \"minecartlogyes\", otherwise, this command will timeout in 10 seconds.)");
				final TenkoCmd cmd = FriendlyWall.getRegister().registerCommand("", new CommandExecutor(){
					@Override
					public boolean onCommand(CommandSender cs, Command c, String l, String[] args){
						for(File f : getFunctionDirectory("MinecartLogger").listFiles()){
							f.delete();
						}
						return false;
					}
				});

				Bukkit.getScheduler().scheduleSyncDelayedTask(FriendlyWall.getPlugin(), new Runnable(){
					@Override
					public void run(){
						if(isConfirming){
							cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - MinecartLogger] Didn't answer in 10 seconds. Assuming \"no\".");
						}
						FriendlyWall.getRegister().unregisterCommand(cmd);
						isConfirming = false;
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

		return false;
	}

	@EventHandler
	public void minecartBirthEvent(PlayerInteractEvent e){
		try {
			if(e.hasBlock() && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().getItemInHand().getType().toString().contains("MINECART") && e.getClickedBlock().getType().toString().contains("RAIL") && !e.getPlayer().hasPermission("friendlywall.minecartlogger.ignore")){
				e.setCancelled(true);

				if(!logEvent(e.getPlayer().getName(), e.getPlayer().getWorld().getName(), Result.CREATE)){
					e.getPlayer().sendMessage(ChatColor.RED + "You can't do that!");
					return;
				}

				NMSLib.spawnCart(e.getPlayer(), e.getClickedBlock());
			}
		} catch (Exception ex){
			FriendlyWall.exceptionCount++;
		}
	}

	@EventHandler
	public void minecartDeathEvent(VehicleDestroyEvent e){
		try {
			if(e.getAttacker() != null){
				if(e.getVehicle().getType().toString().contains("MINECART") && e.getAttacker().getType() == EntityType.PLAYER && !((Player)e.getAttacker()).getPlayer().hasPermission("friendlywall.minecartlogger.ignore")){
					logEvent(((Player)e.getAttacker()).getName(), e.getVehicle().getWorld().getName(), Result.DESTROY);
				}
			}
		} catch (Exception ex){
			FriendlyWall.exceptionCount++;
		}
	}

	public boolean logEvent(String plyrName, String worldName, Result create){
		File file = new File(getFunctionDirectory("MinecartLogger"), plyrName + ".yml");
		try {
			file.createNewFile();
		} catch (IOException e){
			e.printStackTrace();
		}
		FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
		int limit = fc.getInt(worldName + ".Limit");
		int logged = fc.getInt(worldName + ".Placed") + (create.equals(Result.DESTROY) ? -1 : 1);
		if(limit == 0){
			limit = 100;
			fc.set(worldName + ".Limit", Integer.valueOf(100));
		}
		if(logged > limit){
			return false;
		}

		fc.set(worldName + ".Placed", Integer.valueOf(logged));
		try {
			fc.save(file);
		} catch (IOException e){
			e.printStackTrace();
		}
		return true;
	}

	private static enum Result
	{
		CREATE, DESTROY;
	}
}