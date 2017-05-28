package com.chenghui.agriculture.core.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class FuJianSSOAuthenticationToken extends UsernamePasswordToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8290604481716108905L;

    private String username;
    
    private String token;
    
   /* private String clientIp;
    
    private String clientPort;
    
    private String realPath;*/
    
    private String userIp;
    
    private String message;

    public FuJianSSOAuthenticationToken(String username,String token,String userIp,String message) {
		super();
		this.username = username;
		this.token = token;
		/*this.clientIp = clientIp;
		this.clientPort = clientPort;
		this.realPath = realPath;*/
		this.userIp = userIp;
		this.message = message;
	}
    

	@Override
	public String toString() {
		return "FuJianSSOAuthenticationToken [username=" + username
				+ ", token=" + token + ", userIp=" + userIp + ", message="
				+ message + "]";
	}



	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	@Override
	public Object getPrincipal() {
		return getUsername();
	}

	@Override
	public Object getCredentials() {
		return getToken();
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUserIp() {
		return userIp;
	}


	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	
}
