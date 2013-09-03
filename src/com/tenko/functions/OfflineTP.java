package com.tenko.functions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import net.minecraft.server.v1_6_R2.NBTTagCompound;
import net.minecraft.server.v1_6_R2.NBTTagDouble;
import net.minecraft.server.v1_6_R2.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_6_R2.CraftOfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OfflineTP extends Function {
	
	@EventHandler
	public void commandPreprocess(PlayerCommandPreprocessEvent e){
		if(e.getPlayer() != null && e.getMessage().startsWith("/tp")){
			String[] args = e.getMessage().replace("/tp", "").trim().split(" ");
			if(args.length == 1){
				OfflinePlayer p;
				if((p = Bukkit.getOfflinePlayer(args[0])) != null && Bukkit.getPlayer(args[0]) == null && !e.getMessage().contains(",") && p.hasPlayedBefore()){
					if(e.getPlayer().hasPermission("fw.teleportoffline")){
						CraftOfflinePlayer plyr = (CraftOfflinePlayer)p;
						try {
							Method m = plyr.getClass().getDeclaredMethod("getData");
							m.setAccessible(true);
							NBTTagCompound tag = (NBTTagCompound)m.invoke(plyr);
							NBTTagList list = tag.getList("Pos");
							NBTTagDouble x = (NBTTagDouble)list.get(0);
							NBTTagDouble y = (NBTTagDouble)list.get(1);
							NBTTagDouble z = (NBTTagDouble)list.get(2);
							Location l = new Location(Bukkit.getWorld(new UUID(tag.getLong("WorldUUIDMost"), tag.getLong("WorldUUIDLeast"))), x.data, y.data, z.data);
							e.getPlayer().teleport(l);
							e.getPlayer().sendMessage(ChatColor.YELLOW + "Teleported.");
						} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
							e1.printStackTrace();
						} catch (NullPointerException e2){
							e.getPlayer().sendMessage(ChatColor.RED + "No players matched query.");
						}
					} else {
						e.getPlayer().sendMessage(ChatColor.RED + "You don't have permission.");
					}
					e.setCancelled(true);
				}
			}
		}
	}
	
}
