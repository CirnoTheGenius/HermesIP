package com.tenko.asm;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class ASMClassLoader extends URLClassLoader  {

	private final Map<String, byte[]> classes;

	public ASMClassLoader(URL[] urls, ClassLoader parent){
		super(urls, parent);
		classes = new HashMap<String, byte[]>();
	}

	@Override
	protected Class<?> findClass(final String name) throws ClassNotFoundException {
		byte[] classBytes = this.classes.remove(name);
		if (classBytes != null) {
			return defineClass(name, classBytes, 0, classBytes.length);
		}
		return super.findClass(name);
	}
	
	public void addClass(String name, byte[] b){
		classes.put(name, b);
	}
}
