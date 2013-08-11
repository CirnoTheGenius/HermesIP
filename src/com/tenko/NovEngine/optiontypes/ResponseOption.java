package com.tenko.NovEngine.optiontypes;

import org.bukkit.entity.Player;

import com.tenko.asm.IMika;

public class ResponseOption extends Option {
	
	private String answer;
	
	public ResponseOption(String q, String a){
		super(q);
		this.answer = a;
	}
	
	public String getAnswer(){
		return answer;
	}

	@Override
	public void onSelected(IMika em, Player plyr){
		em.chat(plyr, answer);
	}
	
}