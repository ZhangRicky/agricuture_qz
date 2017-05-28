package com.chenghui.agriculture.core.security.http;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BearerModularRealmAuthenticator extends ModularRealmAuthenticator{

    private static final Logger logger = LoggerFactory.getLogger(BasicHttpAuthenticationFilter.class);
    
	@Override
	protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
	    assertRealmsConfigured();
	    BearerAuthenticationToken mlat = null;
	    Realm loginRealm = null;

	    if (!(authenticationToken instanceof BearerAuthenticationToken)) {
	        throw new AuthenticationException("Unrecognized token , not a typeof MultiLoginAuthenticationToken ");
	    } else {
	        mlat = (BearerAuthenticationToken) authenticationToken;
	        logger.debug("realm name is : {}", mlat.getRealmName());
	        loginRealm = lookupRealm(mlat.getRealmName());
	    }

	    return doSingleRealmAuthentication(loginRealm, mlat);

	}

	protected Realm lookupRealm(String realmName) throws AuthenticationException {
	    Collection<Realm> realms = getRealms();
	    for (Realm realm : realms) {
	        if (realm.getName().equalsIgnoreCase(realmName)) {
	            logger.debug("look up realm name is : {}", realm.getName());
	            return realm;
	        }
	    }
	    throw new AuthenticationException("No realm configured for Client " + realmName);
	}
	
}
