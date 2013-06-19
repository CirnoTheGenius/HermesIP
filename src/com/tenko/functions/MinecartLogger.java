package com.tenko.functions;

import java.io.File;
import java.io.IOException;
import net.minecraft.server.v1_5_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_5_R3.ItemMinecart;
import net.minecraft.server.v1_5_R3.World;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

import com.tenko.FriendlyWall;
import com.tenko.objs.TenkoCmd;

public class MinecartLogger extends Function {
	/*
	 * TODO:
	 * Per world.
	 * Failsafe to wipe individual data and all data.
	*/
	
	boolean isConfirming = false;
	
	private static TenkoCmd[] cmds;
	
	public MinecartLogger(){
		//Register commands.
		cmds = new TenkoCmd[]{
				FriendlyWall.registerCommand("minecartlogwipe", this),
		};
		//register listener
		Bukkit.getPluginManager().registerEvents(this, FriendlyWall.getPlugin());
	}
	
	public static void startFunction(){
		new MinecartLogger();
	}
	
	public static void stopFunction(){
		for(TenkoCmd cmd : cmds){
			FriendlyWall.unregisterCommand(cmd);
		}
	}
	
	@Override
	public boolean onCommand(final CommandSender cs, Command c, String l, String[] args){
		if(cs.equals(Bukkit.getConsoleSender())){
			if(c.getName().equalsIgnoreCase("minecartlogwipe")){
				if(args.length < 1){
					result = "Are you sure!? (Type \"minecartlogyes\", otherwise, this command will timeout in 10 seconds.)";
					isConfirming = true;
					
					final TenkoCmd cmd = FriendlyWall.registerCommand("minecartlogyes", new confirmationCommand());
					
					//20*10 = 10 seconds, right?
					Bukkit.getScheduler().scheduleSyncDelayedTask(FriendlyWall.getPlugin(), new Runnable(){
						@Override
						public void run() {
							if(isConfirming){
								cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - MinecartLogger] Didn't answer in 10 seconds. Assuming \"no\".");
							}
							FriendlyWall.unregisterCommand(cmd);
							isConfirming = false;
						}
					}, 20*10);
				} else {
					File f = new File(FriendlyWall.getFunctionDirectory("MinecartLogger"), args[0] + ".yml");
					if(f.delete()){
						result = "Deleted player data for " + args[0] + "!";
					} else {
						result = "Failed to delete data! Does the user even exist?";
					}
				}
				
				cs.sendMessage((successful ? ChatColor.BLUE : ChatColor.RED) + "[FriendlyWall - MoblessWorlds] " + result);
			}
		}
		return false;
	}
	
	@EventHandler
	public void minecartBirthEvent(PlayerInteractEvent e){
		if(e.hasBlock() && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().getItemInHand().getType().toString().contains("MINECART") && e.getClickedBlock().getType().toString().contains("RAIL") && !e.getPlayer().hasPermission("friendlywall.minecartlogger.ignore")){
			e.setCancelled(true);
			
			if(!logEvent(e.getPlayer().getName(), e.getPlayer().getWorld().getName(), Result.CREATE)){
				e.getPlayer().sendMessage(ChatColor.RED + "You can't do that!");
				return;
			}
			
			World nmsw = ((CraftWorld)e.getPlayer().getWorld()).getHandle();
			ItemMinecart itemCart = (ItemMinecart)CraftItemStack.asNMSCopy(e.getPlayer().getItemInHand()).getItem();
			EntityMinecartAbstract a = EntityMinecartAbstract.a(nmsw, (double)e.getClickedBlock().getX() + 0.5F, (double)e.getClickedBlock().getY() + 0.5F, (double)e.getClickedBlock().getZ() + 0.5F, itemCart.a);
			nmsw.addEntity(a);
		}
	}
	
	@EventHandler
	public void minecartDeathEvent(VehicleDestroyEvent e){
		if(e.getVehicle().getType().toString().contains("MINECART") && e.getAttacker().getType() == EntityType.PLAYER && !((Player)e.getAttacker()).getPlayer().hasPermission("friendlywall.minecartlogger.ignore")){
			logEvent(((Player)e.getAttacker()).getName(), e.getVehicle().getWorld().getName(), Result.DESTROY);
		}
	}
	
	public boolean logEvent(String plyrName, String worldName, Result create){
	    File file = new File(FriendlyWall.getFunctionDirectory("MinecartLogger"), plyrName+".yml");
	    try {
	        file.createNewFile();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
	    int limit = fc.getInt(worldName + ".Limit");
	    int logged = fc.getInt(worldName + ".Placed") + (create.equals(Result.DESTROY) ? -1 : 1);
	    if(limit == 0){
	    	limit = 100;
	    	fc.set(worldName + ".Limit", 100);
	    }
	    if(logged > limit){
	    	return false;
	    }
	    //For some strange reason, this function is called twice.
	    fc.set(worldName + ".Placed", logged);
	    try {
			fc.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return true;
	}
	
	private final class confirmationCommand implements CommandExecutor {
		public confirmationCommand(){}

		@Override
		public boolean onCommand(CommandSender cs, Command c, String l, String[] a){
			if(c.getName().equalsIgnoreCase("minecartlogyes")){
				isConfirming = false;
				for(File f : FriendlyWall.getFunctionDirectory("MinecartLogger").listFiles()){
					f.delete();
				}
				cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - MinecartLogger] Cleared entire logging directory!");
			}
			return false;
		}
	}
	
	private enum Result {
		CREATE, DESTROY
	}
}