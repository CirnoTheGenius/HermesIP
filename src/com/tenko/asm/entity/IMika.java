package com.tenko.asm.entity;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract interface IMika {
	
	public void spawnIn(Object w);

	public void lookAt(Location loc);

	public void chat(Player plyr, String s);
	
	public ArrayList<String> getQuotes();
	
	public float getYaw(Vector motion);
	
	public String getName();

	public Location getCraftBukkitLocation();
	
	public void setPositionRotation(double x, double y, double z, float yaw, float pitch);
	
	public void die();
	
	public void teleportTo(Location l);
	
}
