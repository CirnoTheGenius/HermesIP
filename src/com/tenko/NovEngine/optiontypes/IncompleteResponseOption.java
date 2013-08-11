package com.tenko.NovEngine.optiontypes;

import com.tenko.asm.IMika;
import com.tenko.functions.NPCsV2.State;

public class IncompleteResponseOption {
	
	private String question;
	private String answer;
	private State currentState;
	private IMika ai;
	
	public IncompleteResponseOption(State s, IMika i){
		this.currentState = s;
		this.ai = i;
	}
	
	public void setQuestion(String s){
		this.question = s;
	}
	
	public void setAnswer(String s){
		this.answer = s;
	}
	
	public IMika getAi(){
		return ai;
	}
	
	public State getState(){
		return currentState;
	}
	
	public void setState(State s){
		this.currentState = s;
	}
	
	public ResponseOption create(){
		return new ResponseOption(question, answer);
	}
	
}
