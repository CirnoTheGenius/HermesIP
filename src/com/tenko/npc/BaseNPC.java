package com.tenko.npc;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.server.v1_6_R2.Entity;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.CraftServer;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;
import com.tenko.NovEngine.Option;

//Ooo... Strictfp...
public abstract strictfp class BaseNPC extends CraftEntity {
	
	public BaseNPC(CraftServer server, Entity entity){
		super(server, entity);
		//this.goals = new PathfinderGoalSelector(entity.world.methodProfiler);
		//goals.a(new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 0.04F));
	}

	private ArrayList<String> quotes = Lists.newArrayList();
	private ArrayList<Option> options = Lists.newArrayList();
	
	private int chatIndex = 0;
	//private PathfinderGoalSelector goals;
	private Random random = new Random();
	
	public abstract Entity getNMSEntity();
	public abstract void lookAt(Location loc);
	public abstract void die();
	public abstract void teleportTo(Location loc);

	public abstract String getName();
		
	public ArrayList<String> getQuotes(){
		return quotes;
	}
	
	public ArrayList<Option> getOptions(){
		return options;
	}
	
	public void setQuotes(List<String> list){
		quotes = new ArrayList<String>(list);
	}
	
	public void setOptions(List<Option> list){
		options = new ArrayList<Option>(list);
	}
	
	public void chat(Player plyr){
		if(quotes.size() > 0){
			plyr.sendMessage("<" + ChatColor.RED + getName() + ChatColor.RESET + "> " + quotes.get(chatIndex).replace("%plyr%", plyr.getName()).replace("%ai%", getName()));
			chatIndex++;
			if(chatIndex >= quotes.size()){
				chatIndex = 0;
			}
		}
	}
	
	public void chat(Player plyr, String message){
		plyr.sendMessage("<" + ChatColor.RED + getName() + ChatColor.WHITE + "> " + message.replace("%plyr%", plyr.getName()).replace("%ai%", getName()));
	}
	
	public double calculateYaw(Vector motion){
		double dX = motion.getX();
		double dZ = motion.getZ();
		double yawZ = 0;
		
		if(dX != 0){
			if(dX < 0){
				yawZ = 1.5 * 3.141592;
			} else {
				yawZ = 0.5 * 3.141592;
			}
			
			//I really don't want to Math.atan.
			yawZ -= Math.atan(dZ / dX);
		} else if(dZ < 0){
			yawZ = 3.141692;	
		}
		
		return -yawZ * 180 / 3.141592;
	}
	
	//This is probably going to be amazingly inaccurate.
	public double fastAtan(double var){
		//pi/4 * x - x * (|x| - 1) * (0.2447 + 0.0663*|x|)
		return 0.78539 * var - var * (Math.abs(var) - 1) * (0.2447 + 0.0663*Math.abs(var));
	}
	
	public Random getRandom(){
		return random;
	}
	
}