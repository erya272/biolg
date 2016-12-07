package com.jiao.serializable;

public class Logger {
//Logger
	public static void debug(String msg, Class<?> clazz){
		System.out.println(clazz.getName() + ": " + msg);
 	}

}
