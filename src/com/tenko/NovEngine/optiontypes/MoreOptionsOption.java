package com.tenko.NovEngine.optiontypes;

import org.bukkit.entity.Player;

import com.tenko.NovEngine.Conversation;
import com.tenko.asm.IMika;

public class MoreOptionsOption extends Option {
	
	private Option[] newOptions;
	private Conversation conv;
	
	public MoreOptionsOption(String q, Conversation c, Option... options){
		super(q);
		this.newOptions = options;
		this.conv = c;
	}

	@Override
	public void onSelected(IMika em, Player p){
		conv.clearOptions();
		conv.addOptions(newOptions);
		
		conv.startConversation();
	}

}