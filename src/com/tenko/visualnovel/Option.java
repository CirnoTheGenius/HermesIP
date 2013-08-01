package com.tenko.visualnovel;

public class Option {

	private final String text;
	private OptionRunnable action;
	private int optionNumber;

	public Option(int optionNum, String statement, OptionRunnable selected){
		this.text = statement;

		if(selected != null){
			this.action = selected;
		}
		
		this.optionNumber = optionNum;
	}

	public String getText(){
		return text;
	}

	public void answered(){
		if(action != null){
			action.run();
		}
	}

	public int getOptionNum(){
		return optionNumber;
	}
	
	public OptionRunnable getRunnable(){
		return action;
	}

}