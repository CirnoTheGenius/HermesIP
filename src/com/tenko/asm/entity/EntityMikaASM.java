package com.tenko.asm.entity;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.tenko.functions.NPCs;

import net.minecraft.server.v1_6_R2.*;

//Do not use. Meant for compiling to ASM.
public class EntityMikaASM extends EntityPlayer implements IMika {
	
	private ArrayList<String> quotes = new ArrayList<String>();
	
	public EntityMikaASM(Object ms, Object w, String naem){
		super((MinecraftServer)ms, (World)w, naem, new PlayerInteractManager((World)w));
		playerConnection = new com.tenko.asm.network.ServerConnectionASM(this);
		NPCs.getNPCList().put(naem, this);

		this.playerInteractManager.setGameMode(EnumGamemode.SURVIVAL);
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public Location getCraftBukkitLocation(){
		return getBukkitEntity().getLocation();
	}

	@Override
	public void spawnIn(Object w){
		((World)w).addEntity(this);
	}

	@Override
	public void lookAt(Location loc){
		Vector v = loc.toVector().subtract(this.getBukkitEntity().getEyeLocation().toVector());
		this.yaw = this.aP = getYaw(v);
		this.pitch = (float)(loc.getY()-65);
	}

	@Override
	public void chat(Player plyr, String s){
		plyr.sendMessage("<" + ChatColor.RED + this.name + ChatColor.WHITE + "> " + s);
	}
	
	@Override
	public ArrayList<String> getQuotes(){
		return quotes;
	}
	
	@Override
	public float getYaw(Vector motion){
		double dx = motion.getX();
		double dz = motion.getZ();
		double yawz = 0;
		// Set yaw
		if (dx != 0) {
			// Set yaw start value based on dx
			if (dx < 0) {
				yawz = 1.5 * Math.PI;
			} else {
				yawz = 0.5 * Math.PI;
			}
			yawz -= Math.atan(dz / dx);
		} else if (dz < 0) {
			yawz = Math.PI;
		}
		return (float)(-yawz * 180 / Math.PI);
	}
	
	@Override
	public void setPositionRotation(double x, double y, double z, float yaw, float pitch){
		super.setPositionRotation(x, y, z, yaw, pitch);
	}
	
	@Override
	public void die(){
		super.die();
	}
	
	@Override
	public void teleportTo(Location l){
		super.getBukkitEntity().teleport(l);
	}

}