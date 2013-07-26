package com.tenko.bot;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.tenko.FriendlyWall;
import com.tenko.bot.network.ServerConnection;

import net.minecraft.server.v1_6_R2.*;

public class EntityMika extends EntityPlayer {

	public EntityMika(Object ms, Object w, String name){
		super((MinecraftServer)ms, (World)w, name, new PlayerInteractManager((World)w));
		playerConnection = new ServerConnection(this);
		FriendlyWall.getNpcList().put(name, this);

		this.playerInteractManager.setGameMode(EnumGamemode.SURVIVAL);
	}

	public void spawnIn(Object w){
		((World)w).addEntity(this);
	}

	public void walkToLocation(Location to){
		float speed = 0.69999998807907104F; //Movement speed of the player
		double angle = Math.toRadians(yaw);//Convert angle to radians
		//X and Z offsets
		double xoffs = -1*speed*Math.cos(angle);
		double zoffs = speed*Math.sin(angle);
		move(xoffs, 0, zoffs);
	}

	public void lookAt(Location l){
		Vector v = l.toVector().subtract(this.getBukkitEntity().getEyeLocation().toVector());
		this.yaw = this.aP = getYaw(v);
		this.pitch = (float)(l.getY()-65);
	}

	public void chat(Player plyr, String s){
		plyr.sendMessage("<" + ChatColor.RED + this.name + ChatColor.WHITE + "> " + s);
	}

	private float getYaw(Vector motion) {
		double dx = motion.getX();
		double dz = motion.getZ();
		double yaw = 0;
		// Set yaw
		if (dx != 0) {
			// Set yaw start value based on dx
			if (dx < 0) {
				yaw = 1.5 * Math.PI;
			} else {
				yaw = 0.5 * Math.PI;
			}
			yaw -= Math.atan(dz / dx);
		} else if (dz < 0) {
			yaw = Math.PI;
		}
		return (float)(-yaw * 180 / Math.PI);
	}

}