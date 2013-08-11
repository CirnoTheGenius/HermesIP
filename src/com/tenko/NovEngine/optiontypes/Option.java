package com.tenko.NovEngine.optiontypes;

import org.bukkit.entity.Player;

import com.tenko.asm.IMika;

public abstract class Option {
	
	private String question;
	
	public Option(String q){
		this.question = q;
	}

	public String getQuestion(){
		return question;
	}
	
	public abstract void onSelected(IMika em, Player p);
	
}