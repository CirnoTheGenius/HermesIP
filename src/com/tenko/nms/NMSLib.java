package com.tenko.nms;

import com.tenko.FriendlyWall;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMSLib {
	
	private static final String version = FriendlyWall.getVersion();

	public static void spawnCart(Player plyr, Block b){
		try {
			Class<?> nmsWorld = Class.forName("net.minecraft.server." + version + ".World");
			Class<?> craftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
			Class<?> nmsEntityMinecart = Class.forName("net.minecraft.server." + version + ".EntityMinecartAbstract");
			Class<?> nmsMinecartItem = Class.forName("net.minecraft.server." + version + ".ItemMinecart");
			Class<?> craftItemStack = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack");

			Object cbWorld = craftWorld.cast(plyr.getWorld());
			Object nmsWorldObj = cbWorld.getClass().getMethod("getHandle").invoke(cbWorld);
			Object nmsCopy = craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, plyr.getItemInHand());
			Object nmsCopyNo2 = nmsCopy.getClass().getMethod("getItem").invoke(nmsCopy);
			Object itemCart = nmsMinecartItem.cast(nmsCopyNo2);

			Field f = itemCart.getClass().getField("a");
			f.setAccessible(true);
			Object entityMinecart = nmsEntityMinecart.getMethod("a", nmsWorld, Double.TYPE, Double.TYPE, Double.TYPE, Integer.TYPE).invoke(null, nmsWorldObj, Double.valueOf(b.getX() + 0.5D), Double.valueOf(b.getY() + 0.5D), Double.valueOf(b.getZ() + 0.5D), f.get(itemCart));
			f.setAccessible(false);

			nmsWorldObj.getClass().getMethod("addEntity", Class.forName("net.minecraft.server." + FriendlyWall.getVersion() + ".Entity")).invoke(nmsWorldObj, entityMinecart);
			if(plyr.getItemInHand().getAmount() - 1 <= 0){
				plyr.setItemInHand(null);
			} else
				plyr.getItemInHand().setAmount(plyr.getItemInHand().getAmount() - 1);
		}
		catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|NoSuchFieldException|ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	public static Object getNMSWorld(World w){
		try {
			Class<?> craftWorld = Class.forName("org.bukkit.craftbukkit." + version + ".CraftWorld");
			Object cbWorld = craftWorld.cast(w);
			return cbWorld.getClass().getMethod("getHandle").invoke(cbWorld);
		} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|ClassNotFoundException e){
			e.printStackTrace();
		}
		return null;
	}

	public static Object getNMSServer(){
		try {
			Class<?> craftserver = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
			Object cbs = craftserver.cast(Bukkit.getServer());
			return cbs.getClass().getMethod("getServer").invoke(cbs);
		} catch (ClassNotFoundException|IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException e){
			e.printStackTrace();
		}

		return null;
	}

	public static void sendPacket(Player plyr, Object o){
		try {
			Class<?> packet = Class.forName("net.minecraft.server." + version + ".Packet");
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");

			if(!packet.isAssignableFrom(o.getClass())){
				throw new IllegalArgumentException("Object o wasn't a packet!");
			}

			Object cp = craftPlayer.cast(plyr);
			Object handle = craftPlayer.getMethod("getHandle").invoke(cp);
			Object con = handle.getClass().getField("playerConnection").get(handle);
			con.getClass().getMethod("sendPacket", packet).invoke(con, o);
		} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|NoSuchFieldException|ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	public static void sendPackets(Object o){
		try {
			Class<?> packet = Class.forName("net.minecraft.server." + version + ".Packet");
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
			for (Player plyr : Bukkit.getOnlinePlayers()){
				if(!packet.isAssignableFrom(o.getClass())){
					throw new IllegalArgumentException("Object o wasn't a packet!");
				}

				Object cp = craftPlayer.cast(plyr);
				Object handle = craftPlayer.getMethod("getHandle").invoke(cp);
				Object con = handle.getClass().getField("playerConnection").get(handle);
				con.getClass().getMethod("sendPacket", packet).invoke(con, o);
			}
		} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|NoSuchFieldException|ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	public static Object getCraftPlayer(Player p){
		try {
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
			return craftPlayer.cast(p);
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}

		return null;
	}

	public static Object getCraftServer(){
		try {
			Class<?> craftserver = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
			return craftserver.cast(Bukkit.getServer());
		} catch (ClassNotFoundException|IllegalArgumentException|SecurityException e){
			e.printStackTrace();
		}

		return null;
	}
}