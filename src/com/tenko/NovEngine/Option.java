package com.tenko.NovEngine;

import org.bukkit.entity.Player;

import com.tenko.npc.BaseNPC;

public abstract class Option {
	
	private String question;
	private String answer;
	
	public Option(String q, String a){
		this.question = q;
		this.answer = a;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public String getAnswer(){
		return answer;
	}
	
	public abstract boolean shouldShow(Player p);
	
	public abstract void onSelected(BaseNPC npc, Player p, Conversation c);
	
}
