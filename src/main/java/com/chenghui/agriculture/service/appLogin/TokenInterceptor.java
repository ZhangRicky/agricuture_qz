package com.chenghui.agriculture.service.appLogin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.chenghui.agriculture.model.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenInterceptor extends HandlerInterceptorAdapter {
     @Autowired
     private Memory memory;
 
     private List<String> allowList; // 放行的URL列表
 
     private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
 
     @Override
     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         // 判断请求的URI是否运行放行，如果不允许则校验请求的token信息
         if (checkAllowAccess(request.getServletPath())) {
             // 检查请求的token值是否为空
             String token = getTokenFromRequest(request);
             response.setContentType(MediaType.APPLICATION_JSON_VALUE);
             response.setCharacterEncoding("UTF-8");
             response.setHeader("Cache-Control", "no-cache, must-revalidate");
             Map<String, String> map = new HashMap<>();
             ObjectMapper jsonConverter = new ObjectMapper();
             if (StringUtils.isEmpty(token)) {
            	 map.put("status", "-3");
            	 map.put("data", "{}");
            	 map.put("errorMsg", "token不能为空");
                 response.getWriter().write(jsonConverter.writeValueAsString(map));
                 response.getWriter().close();
                 return false;
             }
             if (!memory.checkLoginInfo(token)) {
                 map.put("status", "-2");
                 map.put("data", "{}");
            	 map.put("errorMsg", "token无效，请重新登录");
                 response.getWriter().write(jsonConverter.writeValueAsString(map));
                 response.getWriter().close();
                 return false;
             }
             ThreadTokenHolder.setToken(token); // 保存当前token，用于Controller层获取登录用户信息
         }
         return super.preHandle(request, response, handler);
     }
 
     /**
      * 检查URI是否放行
      * 
      * @param URI
      * @return 返回检查结果
      */
     private boolean checkAllowAccess(String URI) {
         if (!URI.startsWith("/")) {
             URI = "/" + URI;
         }
         for (String allow : allowList) {
             if (PATH_MATCHER.match(allow, URI) && !URI.contains("/services/login")) {
                 return true;
             }
         }
         return false;
     }
 
     /**
      * 从请求信息中获取token值
      * 
      * @param request
      * @return token值
      */
     private String getTokenFromRequest(HttpServletRequest request) {
         // 默认从header里获取token值
         String token = request.getHeader(Constants.TOKEN);
         if (StringUtils.isEmpty(token)) {
             // 从请求信息中获取token值
             token = request.getParameter(Constants.TOKEN);
         }
         return token;
     }
 
     public List<String> getAllowList() {
         return allowList;
     }
 
     public void setAllowList(List<String> allowList) {
         this.allowList = allowList;
     }
 }