package com.tenko.nms;

public class NMSMathHelper {
	
	public static int floor(double d){
		int i = (int)d;

		return d < i ? i - 1 : i;
	}
	
}