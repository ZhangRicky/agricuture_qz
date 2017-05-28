package com.chenghui.agriculture.service.appLogin;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.MD5Util;
import com.chenghui.agriculture.core.utils.TokenProcessor;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Component
public class Memory {
	@Autowired
	private Cache ehcache; // 注意这里引入的Cache是net.sf.ehcache.Cache

	public void setValue(String key, String value) {
		ehcache.put(new Element(key, value));
	}

	public Object getObjectValue(String key) {
		Element element = ehcache.get(key);
		return element != null ? element.getObjectValue() : null;
	}
	
    /**
      * 关闭缓存管理器
      */
     @PreDestroy
     protected void shutdown() {
         if (ehcache != null) {
        	 ehcache.getCacheManager().shutdown();
         }
     }
 
     /**
      * 保存当前登录用户信息
      * 
      * @param shiroUser
      */
     public void saveShiroUser(ShiroUser shiroUser) {
         // 生成seed和token值
         String seed = MD5Util.MD5Encode(shiroUser.getLoginName());
         String token = TokenProcessor.getInstance().generateToken(seed, true);
         // 保存token到登录用户中
         shiroUser.setToken(token);
         // 清空之前的登录信息
         clearLoginInfoBySeed(seed);
         // 保存新的token和登录信息
         boolean external = ehcache.getCacheConfiguration().isEternal();
         int timeToIdleSeconds = Integer.parseInt(ehcache.getCacheConfiguration().getTimeToIdleSeconds()+"");
         int timeToLiveSeconds = Integer.parseInt(ehcache.getCacheConfiguration().getTimeToLiveSeconds()+"");
         ehcache.put(new Element(seed, token, external, timeToIdleSeconds, timeToLiveSeconds));
         ehcache.put(new Element(token, shiroUser, external, timeToIdleSeconds, timeToLiveSeconds));
     }
 
     /**
      * 获取当前线程中的用户信息
      * 
      * @return
      */
     public ShiroUser currentShiroUser() {
         Element element = ehcache.get(ThreadTokenHolder.getToken());
         return element == null ? null : (ShiroUser) element.getObjectValue();
     }
 
     /**
      * 根据token检查用户是否登录
      * 
      * @param token
      * @return
      */
     public boolean checkLoginInfo(String token) {
         Element element = ehcache.get(token);
         return element != null && (ShiroUser) element.getObjectValue() != null;
     }
 
     /**
      * 清空登录信息
      */
     public void clearLoginInfo() {
         ShiroUser shiroUser = currentShiroUser();
         if (shiroUser != null) {
             // 根据登录的用户名生成seed，然后清除登录信息
             String seed = MD5Util.MD5Encode(shiroUser.getLoginName());
             clearLoginInfoBySeed(seed);
         }
     }
 
     /**
      * 根据seed清空登录信息
      * 
      * @param seed
      */
     public void clearLoginInfoBySeed(String seed) {
         // 根据seed找到对应的token
         Element element = ehcache.get(seed);
         if (element != null) {
             // 根据token清空之前的登录信息
             ehcache.remove(seed);
             ehcache.remove(element.getObjectValue());
         }
     }
}