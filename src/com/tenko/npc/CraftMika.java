package com.tenko.npc;

import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.EnumGamemode;
import net.minecraft.server.v1_6_R2.MinecraftServer;
import net.minecraft.server.v1_6_R2.PlayerInteractManager;
import net.minecraft.server.v1_6_R2.World;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.CraftServer;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Vector;

import com.tenko.FriendlyWall;
import com.tenko.npc.connections.NullConnection;
import com.tenko.npc.connections.NullNetworkManager;

/**
 * @author Tsunko
 * @version 2
 */
public class CraftMika extends BaseNPC {
	
	private EntityPlayer entity;
	
	public CraftMika(CraftServer server, EntityPlayer entity) {
		super(server, entity);
		this.entity = entity;
	}
	
	//Factory :3
	public static CraftMika createNPC(String name, Location l){
		final World nmsWorld = ((CraftWorld)l.getWorld()).getHandle();
		final EntityPlayer plyr = new EntityPlayer(MinecraftServer.getServer(), nmsWorld, name, new PlayerInteractManager(nmsWorld));
		plyr.playerConnection = new NullConnection(new NullNetworkManager(), plyr);
		plyr.playerInteractManager.setGameMode(EnumGamemode.CREATIVE);
		plyr.noDamageTicks = 1;
		plyr.fauxSleeping = true;
		plyr.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(FriendlyWall.getPlugin(), new Runnable(){
			@Override
			public void run(){
				nmsWorld.addEntity(plyr, SpawnReason.CUSTOM);
			}
		});
		
		return new CraftMika((CraftServer)Bukkit.getServer(), plyr);
	}
	
	@Override
	public void lookAt(Location loc){
		Vector vLoc = loc.toVector().subtract(this.getLocation().toVector());
		entity.yaw = (float)this.calculateYaw(vLoc);
		entity.aP = entity.yaw;
		entity.pitch = (float) (Math.atan2(Math.sqrt(vLoc.getZ() * vLoc.getZ() + vLoc.getX() * vLoc.getX()), vLoc.getY()) + Math.PI);
	}
	
	@Override
	public void die(){
		entity.die();
	}
	
	@Override
	public void teleportTo(Location loc){
		entity.teleportTo(loc, false);
	}
	
	@Override
	public String getName(){
		return entity.getName();
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}
	
}
