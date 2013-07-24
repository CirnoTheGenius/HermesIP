package com.tenko.bot;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.tenko.FriendlyWall;
import com.tenko.bot.network.ServerConnection;

import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.EnumGamemode;
import net.minecraft.server.v1_6_R2.MinecraftServer;
import net.minecraft.server.v1_6_R2.PlayerInteractManager;
import net.minecraft.server.v1_6_R2.World;

public class EntityMika extends EntityPlayer {

	private Iterator<Coordinate> path;

	public EntityMika(MinecraftServer ms, World w, String name, PlayerInteractManager pim){
		super(ms, w, name, pim);
		playerConnection = new ServerConnection(this);
		FriendlyWall.getNpcList().put(name, this);

		this.playerInteractManager.setGameMode(EnumGamemode.SURVIVAL);
	}

	//	public void moveToLocation(final Location l){
	//		final Vector v = l.toVector().subtract(this.getBukkitEntity().getLocation().toVector());
	//
	//		Bukkit.broadcastMessage("x vector = " + v.getX());
	//		Bukkit.broadcastMessage("y vector = " + v.getY());
	//		Bukkit.broadcastMessage("z vector = " + v.getZ());
	//		double distance = this.getBukkitEntity().getLocation().distance(l);
	//		Bukkit.broadcastMessage("dist = " + distance);
	//		
	//		//velocity = r
	//		//direction = atan(y, x)
	//		//x = r * cos(direction)
	//		//y = r * sin(direction)
	//		
	//		//yaw = left/right
	//		//pitch = up/down
	//		new Thread(){
	//			public void run(){
	//				try {
	//					
	//					Thread.sleep(10);
	//				} catch (InterruptedException e){
	//					e.printStackTrace();
	//				}
	//			}
	//		}.start();
	//	}

	public void calculatePath(Location destination){
		Vector force = getForce(destination.toVector().subtract(this.getBukkitEntity().getLocation().toVector()));
		short distance = (short)getBukkitEntity().getLocation().distance(destination);
//		BlockIterator calculator = new BlockIterator(destination.getWorld(), getBukkitEntity().getLocation().toVector(), direction, 0, (int)getBukkitEntity().getLocation().distance(destination));
		ArrayList<Coordinate> pathTmp = new ArrayList<Coordinate>();
		Location currentPos = getBukkitEntity().getLocation().clone();

//		while(calculator.hasNext()){
//			Block b = calculator.next();
//			Block above = b.getLocation().add(0,1,0).getBlock();
//			Block below = b.getLocation().subtract(0, 1, 0).getBlock();
//
//			int yOffset = 0;
//
//			if(!above.isEmpty()){
//				while(!above.isEmpty()){
//					above = above.getLocation().add(0, 1, 0).getBlock();
//					yOffset++;
//				}
//			}
//
//			if(below.isEmpty()){
//				while(below.isEmpty()){
//					below = below.getLocation().subtract(0, 1, 0).getBlock();
//					yOffset--;
//				}
//			}
//
//			pathTmp.add(new Coordinate(b.getLocation().getX(), b.getLocation().getY()+yOffset, b.getLocation().getZ()));
//		}
		
		Coordinate c = new Coordinate(currentPos.getX(), currentPos.getY(), currentPos.getZ());
		
		//for(int i=0; i < distance; i++){
		if(c.getX() <= destination.getBlockX()){
			while(Math.floor(c.getX()) <= destination.getBlockX()){
				System.out.println("working");
				c.x += force.getX();
				pathTmp.add(c);
			}
		}
		
		path = pathTmp.iterator();
	}

	public void walkPath(final Location l){
		new Thread(){
			public void run(){
				while(true){
					float speed = 0.69999998807907104F;
					double ang = aP < 0 ? 360 + aP : aP;
					move(speed*Math.sin(ang), 0, speed*Math.cos(ang));
				}
//				calculatePath(l);
//				
//				while(path.hasNext()){
//					Coordinate c = path.next();
//					setPosition(c.getX(), c.getY(), c.getZ());
//				}
//				Location dest = getBukkitEntity().getLocation().clone().subtract(l);
//				Vector force = getForce(dest.getDirection());
//				
//				System.out.println(force.getX() + "," + force.getY() + "," + force.getZ());
				
				
				
				
//				calculatePath(l);
//				while(path.hasNext()){
//					Coordinate c = path.next();
//					Location la = new Location(l.getWorld(), c.getX(), c.getY(), c.getZ());
//					float speed = 0.69999998807907104F;
//					System.out.println(speed*Math.abs(c.getX()-l.getX()));
//					System.out.println(Math.abs(c.getX()-l.getX()));
//					move((c.getX()-l.getX()), 0, (c.getZ()-l.getZ()));
//				}
//				/*
//						float speed; //Movement speed of the player
//						double yaw; //Angle of rotation
//						double distance; //Distance
//						double ang = Math.toRadians(yaw); //Convert angle to radians
//						//X and Z offsets
//						double xoffs = distance*Math.cos(angle); // cos x = a/h
//						double zoffs = distance*Math.sin(angle); // sin y = o/h (We're treating y as z)
//				 */

			}
		}.start();
	}

	public void lookAt(LivingEntity l){
		Vector v = l.getEyeLocation().toVector().subtract(this.getBukkitEntity().getEyeLocation().toVector());
		this.yaw = getYaw(v);
		this.pitch = (float)(l.getEyeLocation().getY()-65);
		this.aP = (float)this.getYaw(v);
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
	
	public Vector getForce(Vector direction){
		double x = direction.getX(), y = direction.getY(), z = direction.getZ();
		double newX = (x/maxNum(x,y,z)) * 0.69999998807907104F;
		double newY = (y/maxNum(x,y,z)) * 0.69999998807907104F;
		double newZ = (z/maxNum(x,y,z)) * 0.69999998807907104F;
		
		return new Vector(newX, newY, newZ);
	}
	
	private double maxNum(double... nums){
		Double max = null;
		for(double n : nums){
			if(max == null || n > max){
				max = d;
			}
		}
		
		return max.doubleValue();
	}

}