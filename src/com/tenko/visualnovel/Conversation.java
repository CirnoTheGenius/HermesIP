package com.tenko.visualnovel;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.tenko.FriendlyWall;
import com.tenko.asm.IMika;

public class Conversation {
	
	public Player plyr;
	public IMika npc;
	
	public Conversation(Player talking, IMika target){
		this.plyr = talking;
		this.npc = target;
		
		plyr.setMetadata("conversationNpc", new FixedMetadataValue(FriendlyWall.getPlugin(), this));
		npc.setTalking(true);
		startConversation();
	}
	
	public void startConversation(){
		int selNumber = 1;
		for(Option o : npc.getOptions()){
			plyr.sendMessage(selNumber + ": " + o.getText());
			selNumber++;
		}
	}
	
	public void endConversation(){
		npc.setTalking(false);
	}
	
}