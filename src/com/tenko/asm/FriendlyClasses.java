package com.tenko.asm;

import java.net.URL;

public class FriendlyClasses {
	
	private static ASMClassLoader loader = new ASMClassLoader(new URL[]{}, Thread.currentThread().getContextClassLoader());
	
	public static void loadClass(String s, byte[] b){
		loader.addClass(s, b);
	}
	
	public static Class<?> getClass(String s){
		try {
			return loader.findClass(s);
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
}
