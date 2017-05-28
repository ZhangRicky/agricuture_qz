package com.chenghui.agriculture.service.appLogin;

public class ThreadTokenHolder {
	
	public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
	
	public static void setToken(String token){
		threadLocal.set(token);
	}
	
	public static String getToken(){
		return threadLocal.get();
	}

}
