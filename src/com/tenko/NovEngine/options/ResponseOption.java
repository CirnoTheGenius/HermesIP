package com.tenko.NovEngine.options;

import org.bukkit.entity.Player;

import com.tenko.NovEngine.Conversation;
import com.tenko.NovEngine.Option;
import com.tenko.npc.BaseNPC;

public class ResponseOption extends Option {

	public ResponseOption(String q, String a){
		super(q, a);
	}

	@Override
	public boolean shouldShow(Player p){
		return true;
	}

	@Override
	public void onSelected(BaseNPC npc, Player p, Conversation c){
		npc.chat(p, getAnswer());
		c.endConversation();
	}

}
