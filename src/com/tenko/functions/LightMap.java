package com.tenko.functions;

import java.io.IOException;

import net.minecraft.server.v1_6_R2.World;
import net.minecraft.server.v1_6_R2.WorldMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R2.map.CraftMapRenderer;
import org.bukkit.craftbukkit.v1_6_R2.map.CraftMapView;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

import com.tenko.annotations.FunctionCommand;
import com.tenko.objs.ImageRenderer;

public class LightMap extends Function {
	
	@FunctionCommand(opOnly = true, playerOnly = true, requiredArgumentCount = 1, usage = "/map <url to image>")
	public void map(CommandSender cs, String[] args) throws IOException {
		Player plyr = (Player)cs;
		if(plyr.getItemInHand().getType() == Material.MAP){
			MapView map = Bukkit.getMap(plyr.getItemInHand().getDurability());
			map.getRenderers().clear();
			map.addRenderer(new ImageRenderer(args[0]));
			cs.sendMessage(ChatColor.BLUE + "Attempting to render " + args[0]);
			for(Player p : Bukkit.getOnlinePlayers()){
				p.sendMap(map);
			}
		}
	}
	
	@SuppressWarnings("unused")
	@FunctionCommand(opOnly = true, playerOnly = true, usage = "/restoremap")
	public void restoreMap(CommandSender cs, String[] args){
		Player plyr = (Player)cs;
		if(plyr.getItemInHand().getType() == Material.MAP){
			MapView map = Bukkit.getMap(plyr.getItemInHand().getDurability());
			map.getRenderers().clear();
			map.addRenderer(new CraftMapRenderer((CraftMapView) map, (WorldMap)((World)((CraftWorld)plyr.getWorld()).getHandle()).a(WorldMap.class, "map_" + map.getId())));
			cs.sendMessage(ChatColor.BLUE + "Cleared map successfull!");
		}
	}
	
}
