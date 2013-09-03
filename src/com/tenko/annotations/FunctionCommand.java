package com.tenko.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionCommand {
	
	String usage();
	String permission() default "";
	
	int requiredArgumentCount() default 0;
	
	boolean opOnly();
	boolean playerOnly() default false;
	
}
