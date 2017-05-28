package com.chenghui.agriculture.core.security.http;

import java.util.Date;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.security.ShiroDBRealm;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.Encodes;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.Users;
import com.chenghui.agriculture.service.appLogin.Memory;
import com.chenghui.agriculture.service.system.SystemOperationLogService;
import com.chenghui.agriculture.service.system.UserService;

public class BearerTokenRealm extends AuthorizingRealm /* or AuthenticatingRealm */ {
	
	private final static Logger logger = LoggerFactory.getLogger(BearerTokenRealm.class); 

	@Autowired
	private UserService userService;
	
	@Autowired
	private Memory memory;
	
	@Autowired
	private SystemOperationLogService systemOperationLogService;
	
	
    public BearerTokenRealm() {
        //this makes the supports(...) method return true only if the token is an instanceof BAT:
        setAuthenticationTokenClass(BearerAuthenticationToken.class);
    }

    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        BearerAuthenticationToken bearerToken = (BearerAuthenticationToken)token;

        String username = bearerToken.getUsername();
		logger.debug("bearerToken.getUsername() "+username);
		
		Users user = userService.findUserByLoginName(username);
		if (user == null) {
			logger.info(username + "登录失败,用户[ "+username+" ]不存在！");
			throw new UnknownAccountException();
		}
		Set<Role> userRoles = user.getRole();
		
		if ( userRoles==null || userRoles.size() == 0) {
			logger.info(username + "登录失败,用户[ "+username+" ]没有绑定角色！");
			throw new AuthenticationException("用户[ "+username+" ]没有绑定角色");
		}

		ServletRequest request = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();  
		String remoteIP = ShiroDBRealm.getRemoteIP((HttpServletRequest)request);
		logger.info("用户[ "+username+" ]登录IP地址：" + remoteIP);
		byte[] salt = Encodes.decodeHex(user.getSalt());
		ShiroUser loginUser = new ShiroUser(
				user.getId(),
				user.getUserName(),
				user.getRealName(),
				user.getIsFirstLogin() == null || user.getIsFirstLogin() ? "false" : "false",
				remoteIP,
				userRoles,
				user.getArea());
		if (!loginUser.isAdmin && !user.getIsInUse()) {
			throw new DisabledAccountException();
		}
		if (!loginUser.isAdmin && (new Date()).after(user.getExpireDate())) {
			throw new ExpiredCredentialsException();
		}
		memory.saveShiroUser(loginUser);
		bearerToken.setToken(loginUser.getToken());
		systemOperationLogService.saveSystemOperationLog(loginUser, AppModule.APP_CN, OperationType.AppLogin, "App登陆","","");
		return new SimpleAuthenticationInfo(loginUser, user.getPassword(),ByteSource.Util.bytes(salt), getName());
    }

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}
	
	@PostConstruct
	public void initCredentialsMatcher() {
		
		// 设定 Password 校验的 Hash 算法与迭代次数.
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserService.HASH_ALGORITHM);
		matcher.setHashIterations(UserService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);

	}
}