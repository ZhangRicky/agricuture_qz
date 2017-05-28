package com.chenghui.agriculture.core.security.http;

import org.apache.shiro.authc.AuthenticationToken;

@SuppressWarnings("serial")
public class BearerAuthenticationToken implements AuthenticationToken {
	
	private String username;
	
	private String password;
	
	private String realmName="BearerTokenRealm";
	
	private String token = "";
	
    public BearerAuthenticationToken(String token) {}
    
    public BearerAuthenticationToken() {
    }
    
    public BearerAuthenticationToken(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return getUsername();
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return getPassword();
	}
	
	public String getRealmName() {
		return realmName;
	}

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}