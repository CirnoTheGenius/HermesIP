package com.tenko.NovEngine;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.tenko.FriendlyWall;
import com.tenko.npc.BaseNPC;

public class Conversation {

	private Player refPlyr;
	private BaseNPC npc;

	//Actual options to actually show. Has missing options from allOptions.
	private ArrayList<Option> actualOptions;

	private boolean hasStarted = false;

	public Conversation(Player p, BaseNPC npc){
		this.refPlyr = p;
		this.npc = npc;
		
		actualOptions = Lists.newArrayList();
	}

	private void testOptions(){
		for(Option o : npc.getOptions()){
			if(o.shouldShow(refPlyr)){
				actualOptions.add(o);
			}
		}
	}

	public void endConversation(){
		refPlyr.removeMetadata("VNconversation", FriendlyWall.getPlugin());
		actualOptions.clear();
		hasStarted = false;
	}

	public void startConversation(){
		testOptions();
		
		refPlyr.sendMessage(ChatColor.RED + "You are now chatting with " + npc.getName() + "!");
		refPlyr.sendMessage(ChatColor.RED + "Chat the number that corresponds with what you want to say.");

		hasStarted = true;
		for(int i=0; i < actualOptions.size(); i++){
			refPlyr.sendMessage((ChatColor.BLUE + (i + ": " + actualOptions.get(i).getQuestion())).replace("%plyr%", refPlyr.getName()).replace("%ai%", npc.getName()));
		}
	}
	
	public Player getPlayer(){
		return refPlyr;
	}
	
	public BaseNPC getNPC(){
		return npc;
	}

	public int optionListSize(){
		if(hasStarted){
			return actualOptions.size();
		} else {
			throw new IllegalStateException("The conversation hasn't started yet!");
		}
	}

	public Option getOption(int index){
		if(hasStarted){
			return actualOptions.get(index);
		} else {
			throw new IllegalStateException("The conversation hasn't started yet!");
		}	
	}

	public void clearOptions(){
		actualOptions.clear();
	}

}
