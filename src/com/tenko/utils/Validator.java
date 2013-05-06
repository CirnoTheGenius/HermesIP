package com.tenko.utils;

public class Validator {

	public static boolean validIP(String ip) {
		int count = 0;
		for(char c : ip.toCharArray()){
			count += (c == 46 ? 1 : 0);
		}
		
		if(count != 3){
			return false;
		}

		String[] segments = ip.split("\\.");
		if(segments.length != 4){
			return false;
		}
		
		
		for(String s : segments){
			if(s.length() > 3 || s.length() < 1) return false;
		}

		return true;
	}

}
