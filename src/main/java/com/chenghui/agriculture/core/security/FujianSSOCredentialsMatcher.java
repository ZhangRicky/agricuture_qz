package com.chenghui.agriculture.core.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.linkage.securer.client.CResult;
import com.linkage.securer.client.Client;

public class FujianSSOCredentialsMatcher extends SimpleCredentialsMatcher {
	private final static Logger logger = LoggerFactory.getLogger(FujianSSOCredentialsMatcher.class);
	
	private String realPath = "";
	private String clientIp = "";
	private String clientPort = "";

	public FujianSSOCredentialsMatcher(String realPath,String clientIp,String clientPort){
		super();
		this.realPath = realPath;
		this.clientIp = clientIp;
		this.clientPort = clientPort;
	}
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {
		FuJianSSOAuthenticationToken ssoToken = (FuJianSSOAuthenticationToken) token;
		
		logger.debug("FuJianSSOAuthenticationToken is {}",ssoToken.toString());
		logger.debug("AuthenticationInfo is {}",info.toString());
		String message = ssoToken.getMessage();
		if(!"".equals(message)){
			throw new AuthenticationException(message);
		}else{
		String loginName = ssoToken.getUsername();
		String ticket = ssoToken.getToken();
		String userIp = ssoToken.getUserIp();
		
		System.out.println("realPath:"+realPath);
		System.out.println("clientIp:"+clientIp);
		System.out.println("clientPort:"+clientPort);
		
		Client client = Client.getInstance(realPath);	
		CResult result = client.doSSO(loginName,ticket, clientIp,Integer.parseInt(clientPort),userIp);
		
		if (result.getResultCode() == 0) {
			System.out.println("认证返回结果为:0,"+loginName+"认证通过.");
			return true;
		}else if (result.getResultCode() == -1) {
			System.out.println("认证返回结果为:-1,原因:"+result.getResultMsg());
			throw new AuthenticationException(result.getResultMsg());
		}else {
			System.out.println("认证不通过！原因:"+result.getResultMsg());
			throw new AuthenticationException("认证不通过! 原因:" + result.getResultMsg());
		}
	   }
	}

}
