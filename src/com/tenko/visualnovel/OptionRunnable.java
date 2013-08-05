package com.tenko.visualnovel;

import org.bukkit.entity.Player;

import com.tenko.asm.IMika;

public abstract class OptionRunnable implements Runnable {
	
	private Player target;
	private IMika entity;
	private Intention intent;
	private String description;
	
	public enum Intention {
		TALK
	}
	
	public OptionRunnable(Player tar, IMika en, Intention i, String desc){
		this.target = tar;
		this.entity = en;
		this.description = desc;
		this.intent = i;
	}
	
	public Player getPlayer(){
		return target;
	}
	
	public IMika getEntity(){
		return entity;
	}
	
	public Intention getIntention(){
		return intent;
	}
	
	public String getDescription(){
		return description;
	}
	
	@Override
	public abstract void run();

}
