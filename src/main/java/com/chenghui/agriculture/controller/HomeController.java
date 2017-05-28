package com.chenghui.agriculture.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chenghui.agriculture.core.constant.AppModule;


@Controller
public class HomeController {
	
	private boolean checkPermission(String resourceName) {
		Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(resourceName);
	}
	
    @RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
    @RequestMapping(value = "/agricultureHome", method = {RequestMethod.POST,RequestMethod.GET})
    public String personalHome() {
    	return "agricultureHome";
	}
    @RequestMapping(value = "/home", method = {RequestMethod.POST,RequestMethod.GET})
    public String home() {
    	return "home";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
	public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
		return "login";
	}
    
    @RequestMapping(value = "/projectCollect", method = {RequestMethod.POST,RequestMethod.GET})
    public String projectCollect() {
    	if(!checkPermission(AppModule.PROJECT_COLLECT_EN)) {
    		return "redirect:/home";
    	}
		return "ProjectManage/"+AppModule.PROJECT_COLLECT_EN;
	}
    
    @RequestMapping(value = "/projectImport", method = {RequestMethod.POST,RequestMethod.GET})
    public String projectImport() {
    	if(!checkPermission(AppModule.PROJECT_IMPORT_EN)) {
    		return "redirect:/home";
    	}
    	return "ProjectManage/"+AppModule.PROJECT_IMPORT_EN;
    }
    
    @RequestMapping(value = "/attachmentUpload", method = {RequestMethod.POST,RequestMethod.GET})
    public String attachmentUpload() {
    	if(!checkPermission(AppModule.ATTACHMENT_UPLOAD_EN)) {
    		return "redirect:/home";
    	}
    	return "ProjectManage/"+AppModule.ATTACHMENT_UPLOAD_EN;
    }
    
    @RequestMapping(value = "/projectsManage", method = {RequestMethod.POST,RequestMethod.GET})
    public String projectsManage() {
    	if(!checkPermission(AppModule.PROJECTS_MANAGE_EN)) {
    		return "redirect:/home";
    	}
    	return "ProjectManage/"+AppModule.PROJECTS_MANAGE_EN;
    }
    
    @RequestMapping(value = "/subProjectManage", method = {RequestMethod.POST,RequestMethod.GET})
    public String subProjectManage() {
    	if(!checkPermission(AppModule.SUBPROJECT_MANAGE_EN)) {
    		return "redirect:/home";
    	}
    	return "ProjectManage/"+AppModule.SUBPROJECT_MANAGE_EN;
    }
    
    @RequestMapping(value = "/financeManagement", method = {RequestMethod.POST,RequestMethod.GET})
    public String financeManagement() {
    	if(!checkPermission(AppModule.FINANCE_MANAGEMENT_EN)) {
    		return "redirect:/home";
    	}
    	return "ProjectManage/"+AppModule.FINANCE_MANAGEMENT_EN;
    }
    
    @RequestMapping(value = "/statisticalChart",method = {RequestMethod.POST,RequestMethod.GET})
    public String statisticalChart(){
    	if(!checkPermission(AppModule.STATISTICAL_CHART_EN)){
    		return "redirect:/home";
    	}
    	return "ProjectManage/"+AppModule.STATISTICAL_CHART_EN;
    }
    
    @RequestMapping(value = "/userManage", method = {RequestMethod.POST,RequestMethod.GET})
    public String user() {
    	if(!checkPermission(AppModule.USER_EN)) {
    		return "redirect:/home";
    	}
    	return "SystemManage/"+AppModule.USER_EN;
    }
    
    @RequestMapping(value = "/roleManage", method = {RequestMethod.POST,RequestMethod.GET})
    public String role() {
    	if(!checkPermission(AppModule.ROLE_EN)) {
    		return "redirect:/home";
    	}
		return "SystemManage/"+AppModule.ROLE_EN;
	}
    
    @RequestMapping(value = "/organizationManage", method = {RequestMethod.POST,RequestMethod.GET})
    public String organizationManage(){
    	if (!checkPermission(AppModule.ORGANIZATION_EN)){
    		return "redirect:/home";
    	}
    	return "SystemManage/"+AppModule.ORGANIZATION_EN;
    }
    
    
	@RequestMapping(value = "/systemOperationLog", method = { RequestMethod.POST, RequestMethod.GET })
	public String systemOperation() {
		if (!checkPermission(AppModule.SYSTEM_OPREATION_LOG_EN)) {
			return "redirect:/home";
		}
		return "SystemManage/"+AppModule.SYSTEM_OPREATION_LOG_EN;

	}
    @RequestMapping(value = "/areaManage", method = { RequestMethod.POST,RequestMethod.GET })
 	public String areaManage() {
    	if(!checkPermission(AppModule.AREA_EN)) {
    		return "redirect:/home";
    	}
 		return "SystemManage/"+AppModule.AREA_EN;
 		
    }
    @RequestMapping(value = "/profile", method = { RequestMethod.POST,RequestMethod.GET })
    public String myProfile() {
    	return "SystemManage/"+AppModule.PROFILE_EN;
    	
    }
}
