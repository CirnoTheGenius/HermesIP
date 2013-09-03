package com.tenko.NovEngine.options;

public class IncompleteResponseOption {

	private String question;
	private String answer;

	public void setQuestion(String q){
		question = q;
	}
	
	public void setAnswer(String a){
		answer = a;
	}
	
	public String getQuestion(){
		return question;
	}
	
	public String getAnswer(){
		return answer;
	}
	
	public ResponseOption createResponse(){
		return new ResponseOption(question, answer);
	}

}
