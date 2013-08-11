package com.tenko.NovEngine;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.tenko.FriendlyWall;
import com.tenko.NovEngine.optiontypes.Option;
import com.tenko.asm.IMika;

public class Conversation {
	
	private Player plyr;
	private IMika listener;
	private ArrayList<Option> options;
	
	public Conversation(Player p, IMika mika, Option... initOpts){
		this.plyr = p;
		this.listener = mika;
		this.options = new ArrayList<Option>(Arrays.asList(initOpts));
	}
	
	public Player getPlayer(){
		return plyr;
	}
	
	public IMika getListener(){
		return listener;
	}
	
	public void startConversation(){
		for(int i=0; i < options.size(); i++){
			plyr.sendMessage(ChatColor.BLUE + (i + ": " + options.get(i).getQuestion()));
		}
	}
	
	public void addOption(Option o){
		options.add(o);
	}
	
	public void addOptions(Option... o){
		options.addAll(Arrays.asList(o));
	}
	
	public void removeOption(Option o){
		options.remove(o);
	}
	
	public void clearOptions(){
		options.clear();
	}
	
	public ArrayList<Option> getOptions(){
		return options;
	}
	
	public void endConversation(){
		plyr.removeMetadata("chattingNpc", FriendlyWall.getPlugin());
		listener.setTalking(false);
		this.plyr = null;
		this.listener = null;
		options.clear();
		options = null;
	}
	
}