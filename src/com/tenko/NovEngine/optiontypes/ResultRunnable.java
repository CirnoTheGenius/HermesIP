package com.tenko.NovEngine.optiontypes;

import com.tenko.NovEngine.Conversation;

public abstract class ResultRunnable implements Runnable {
	
	private Conversation conversation;
	
	public ResultRunnable(Conversation c){
		this.conversation = c;
	}
	
	public Conversation getConversation(){
		return conversation;
	}
	
	@Override
	public abstract void run();

}
