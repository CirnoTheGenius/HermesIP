package com.tenko.functions;

import com.tenko.FriendlyWall;
import com.tenko.objs.TenkoCmd;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
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
import org.bukkit.inventory.ItemStack;

public class MinecartLogger extends Function {
	
	boolean isConfirming = false;
	private String packageName = Bukkit.getServer().getClass().getPackage().getName();
	private String version = this.packageName.substring(this.packageName.lastIndexOf(".") + 1);
	Class<?> nmsWorld;
	Class<?> craftWorld;
	Class<?> nmsEntityMinecart;
	Class<?> nmsMinecartItem;
	Class<?> craftItemStack;

	public MinecartLogger(){
		try {
			this.nmsWorld = Class.forName("net.minecraft.server." + this.version + ".World");
			this.nmsEntityMinecart = Class.forName("net.minecraft.server." + this.version + ".EntityMinecartAbstract");
			this.nmsMinecartItem = Class.forName("net.minecraft.server." + this.version + ".ItemMinecart");

			this.craftWorld = Class.forName("org.bukkit.craftbukkit." + this.version + ".CraftWorld");
			this.craftItemStack = Class.forName("org.bukkit.craftbukkit." + this.version + ".inventory.CraftItemStack");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCommand(final CommandSender cs, Command c, String l, String[] args){	
		if(cs.equals(Bukkit.getConsoleSender()) && c.getName().equalsIgnoreCase("minecartlogwipe")){
			if(args.length < 1){
				this.result = "Are you sure!? (Type \"minecartlogyes\", otherwise, this command will timeout in 10 seconds.)";
				this.isConfirming = true;

				final TenkoCmd cmd = FriendlyWall.registerCommand("minecartlogyes", new confirmationCommand());

				Bukkit.getScheduler().scheduleSyncDelayedTask(FriendlyWall.getPlugin(), new Runnable(){
					@Override
					public void run(){
						if(isConfirming){
							cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - MinecartLogger] Didn't answer in 10 seconds. Assuming \"no\".");
						}
						FriendlyWall.unregisterCommand(cmd);
						isConfirming = false;
					}
				}, 200L);
			} else {
				File f = new File(FriendlyWall.getFunctionDirectory("MinecartLogger"), args[0] + ".yml");
				if(f.delete()){
					this.result = "Deleted player data for " + args[0] + "!";
				} else {
					this.result = "Failed to delete data! Does the user even exist?";
				}
			}

			cs.sendMessage((this.successful ? ChatColor.BLUE : ChatColor.RED) + "[FriendlyWall - MoblessWorlds] " + this.result);
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

			spawnCart(e.getPlayer(), e.getClickedBlock());
		}
	}

	public void spawnCart(Player plyr, Block b){
		try {
			Object cbWorld = this.craftWorld.cast(plyr.getWorld());
			Object nmsWorldObj = cbWorld.getClass().getMethod("getHandle", new Class[0]).invoke(cbWorld, new Object[0]);
			Object nmsCopy = this.craftItemStack.getMethod("asNMSCopy", new Class[] { ItemStack.class }).invoke(null, new Object[] { plyr.getItemInHand() });
			Object nmsCopyNo2 = nmsCopy.getClass().getMethod("getItem", new Class[0]).invoke(nmsCopy, new Object[0]);
			Object itemCart = this.nmsMinecartItem.cast(nmsCopyNo2);
			Field f = itemCart.getClass().getField("a");
			f.setAccessible(true);
			Object entityMinecart = this.nmsEntityMinecart.getMethod("a", new Class[] { Class.forName("net.minecraft.server." + this.version + ".World"), Double.TYPE, Double.TYPE, Double.TYPE, Integer.TYPE }).invoke(null, new Object[] { nmsWorldObj, Double.valueOf(b.getX() + 0.5D), Double.valueOf(b.getY() + 0.5D), Double.valueOf(b.getZ() + 0.5D), f.get(itemCart) });
			f.setAccessible(false);

			nmsWorldObj.getClass().getMethod("addEntity", new Class[] { Class.forName("net.minecraft.server." + this.version + ".Entity") }).invoke(nmsWorldObj, new Object[] { entityMinecart });
			plyr.setItemInHand(null);
		} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|NoSuchFieldException|ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	@EventHandler
	public void minecartDeathEvent(VehicleDestroyEvent e){
		if(e.getVehicle().getType().toString().contains("MINECART") && e.getAttacker().getType() == EntityType.PLAYER && !((Player)e.getAttacker()).getPlayer().hasPermission("friendlywall.minecartlogger.ignore")){
			logEvent(((Player)e.getAttacker()).getName(), e.getVehicle().getWorld().getName(), Result.DESTROY);
		}
	}

	public boolean logEvent(String plyrName, String worldName, Result create){
		File file = new File(FriendlyWall.getFunctionDirectory("MinecartLogger"), plyrName + ".yml");
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

	private final class confirmationCommand implements CommandExecutor {
		
		@Override
		public boolean onCommand(CommandSender cs, Command c, String l, String[] a){
			if(c.getName().equalsIgnoreCase("minecartlogyes")){
				isConfirming = false;
				for (File f : FriendlyWall.getFunctionDirectory("MinecartLogger").listFiles()){
					f.delete();
				}
				cs.sendMessage(ChatColor.BLUE + "[FriendlyWall - MinecartLogger] Cleared entire logging directory!");
			}
			return false;
		}
	}
}