package com.tenko.asm;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.tenko.NovEngine.optiontypes.Option;

public abstract interface IMika {
	
	public void spawnIn(Object w);

	public void lookAt(Location loc);

	public void chat(Player plyr);
	
	public void chat(Player plyr, String message);
	
	public ArrayList<String> getQuotes();
	
	public float getYaw(Vector motion);
	
	public String getName();

	public Location getCraftBukkitLocation();
	
	public void setPositionRotation(double x, double y, double z, float yaw, float pitch);
	
	public void die();
	
	public void teleportTo(Location l);
	
	public LivingEntity getCraftEntity();
	
	public ArrayList<Option> getOptions();
	
	public boolean isTalking();
	
	public void setTalking(boolean a);
	
}