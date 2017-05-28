package com.chenghui.agriculture.controller;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.AssertionFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.exception.RestException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.RestResult;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.RoleType;
import com.chenghui.agriculture.model.Users;
import com.chenghui.agriculture.service.system.RoleService;

@RequestMapping("/role")
@RestController
public class RoleController {

	@Autowired
	RoleService roleService;

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public List<Role> getRoleList() throws RestException{
    	List<Role> roleList = roleService.findAll();
		return roleList;
	}
	@RequestMapping(value = "/roleTypes", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<RoleType> getRoleTypeList() throws RestException{
		List<RoleType> roleTypeList = roleService.getRoleTypes();
		return roleTypeList;
	}
//	@RequestMapping(value = "/approveRoles", method = RequestMethod.GET)
//	public List<Role> getAproveRoleList() throws RestException{
//		List<Role> roleList = roleService.findApproveRoles();
//		return roleList;
//	}
    @RequestMapping(value = "/search", method = RequestMethod.GET)
	public PaginationSupport<Role> getRolePage(
			@RequestParam("roleName") String roleName,
			@RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize) {

		Role role = new Role();
		role.setRoleName(roleName);
		
		Subject currentUser = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipals().getPrimaryPrincipal();
		
		PaginationSupport<Role> pagination = roleService.findRoleListForPage(role,pageSize, page, shiroUser);

		return pagination;
	}
    @RequestMapping(value = "/{role_id:\\d+}", method = RequestMethod.GET)
	public Role getRole(@PathVariable Long role_id){
		Role role = roleService.get(role_id);
		if(role==null){
			throw new RestException(HttpStatus.NOT_FOUND, "请求资源不存在");
		}
		return role;
	}
    @RequestMapping(value = "/update", method = RequestMethod.POST)
	public RestResult updateRole(@RequestBody Role roleVO){
		try {
    		roleService.updateRole(roleVO);
		} catch (Exception e) {
			return new RestResult(-1, e.getMessage());
		}
		return new RestResult("添加权限", "保存权限成功！");
	}

    @RequestMapping(value = "/{role_id:\\d+}/{userName}/user", method = RequestMethod.GET)
	public List<Users> getRoleUser(@PathVariable Long role_id,@PathVariable String userName){
    	if("null".equals(userName)){userName = "";}
    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
    	
    	List<Users> userList  = new ArrayList<Users>();
    	
    	if(shiroUser.isAdmin) {
    		userList = roleService.getUserByRole(role_id,userName,null);
    	}else {
    		userList = roleService.getUserByRole(role_id,userName,shiroUser);
    	}
		if(userList==null){
			throw new RestException(HttpStatus.NOT_FOUND, "请求资源不存在");
		}
		return userList;
	}
    
    @RequestMapping(value = "/users/{role_id:\\d+}", method = RequestMethod.GET)
	public List<Map<String, Object>> getRoleUsers(@PathVariable Long role_id,String userName){
    	if("null".equals(userName)){userName = "";}
    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
    	
    	List<Users> userList  = new ArrayList<Users>();
    	
    	userList = roleService.getUserByRole(role_id,userName,shiroUser);
    	
		if(userList==null){
			throw new RestException(HttpStatus.NOT_FOUND, "请求资源不存在");
		}
		
		List<Map<String, Object>> tree = new ArrayList<Map<String, Object>>();
		for(Users u : userList){
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("id", u.getId());
			userMap.put("text", u.getRealName());
			userMap.put("expanded", true);
			tree.add(userMap);
		}
		
		
		return tree;
	}

    @RequestMapping(value = "/{role_id:\\d+}/user/{user_id:\\d+}/{addOrRemove}", method = RequestMethod.POST)
	public RestResult updateRoleUser(@PathVariable Long role_id,@PathVariable Long user_id,@PathVariable String addOrRemove){
    	String result = "";
    	try {
    		if("add".equalsIgnoreCase(addOrRemove)||"remove".equalsIgnoreCase(addOrRemove)){
        		result = roleService.updateRoleUser(role_id, user_id,addOrRemove);
    		}
			return new RestResult(result,"修改成功");
    	} catch (Exception e) {
			throw new RestException(HttpStatus.SERVICE_UNAVAILABLE, "权限-用户 错误");
		}
	}
    
//    @RequestMapping(value = "/{role_id:\\d+}/area", method = RequestMethod.GET)
//	public List<Area> getRoleArea(@PathVariable Long role_id){
//		List<Area> areaList= roleService.getAreaByRole(role_id);
//		if(areaList==null){
//			throw new RestException(HttpStatus.NOT_FOUND, "请求资源不存在");
//		}
//		return areaList;
//	}
    
//    @RequestMapping(value = "/{role_id:\\d+}/department", method = RequestMethod.GET)
//    public List<Department> getRoleDepartment(@PathVariable Long role_id){
//    	List<Department> departmentList= roleService.getDepartmentByRole(role_id);
//    	if(departmentList==null){
//    		throw new RestException(HttpStatus.NOT_FOUND, "请求资源不存在");
//    	}
//    	return departmentList;
//    }

    @RequestMapping(value = "/{role_id:\\d+}/area/{area_id:\\d+}", method = RequestMethod.POST)
	public RestResult updateRoleArea(@PathVariable Long role_id,@PathVariable Long area_id){
    	try {
    		roleService.updateRoleArea(role_id, area_id);
    		return new RestResult("权限-地区","修改成功");
    	} catch (Exception e) {
			throw new RestException(HttpStatus.SERVICE_UNAVAILABLE, "权限-地区 错误");
		}
	}
    
//    @RequestMapping(value = "/{role_id:\\d+}/department/{department_id:\\d+}", method = RequestMethod.POST)
//    public RestResult updateRoleDepartment(@PathVariable Long role_id,@PathVariable Long department_id){
//    	try {
//    		roleService.updateRoleDepartment(role_id, department_id);
//    		return new RestResult("权限-部门","修改成功");
//    	} catch (Exception e) {
//    		throw new RestException(HttpStatus.SERVICE_UNAVAILABLE, "权限-部门 错误");
//    	}
//    }

    @RequestMapping(value = "/delete/{role_id:\\d+}", method = RequestMethod.DELETE)
	public RestResult removeRole(@PathVariable Long role_id) throws RestException{
    	try {
			roleService.removeRole(role_id);
			return new RestResult("权限管理", "删除成功");
		}catch (BusinessServiceException busex) {
//			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, busex.getMessage());
			busex.printStackTrace();
			return new RestResult("权限管理", -1, busex.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.SERVICE_UNAVAILABLE, "权限-删除 错误");
		}
	}
    @RequestMapping(value = "/add", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult addRole(@RequestBody Role role) {
    	Subject currentUser = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipals().getPrimaryPrincipal();
//		String roleName = shiroUser.defaultRoleName;
    	try {
    		roleService.add(role, shiroUser);
    		return new RestResult(0, "增加角色成功!");
		} catch(BusinessServiceException busyEx) {
			return new RestResult(-1, busyEx.getLocalizedMessage());
		} catch(AssertionFailure e){
			return new RestResult(-1, e.getMessage());
		}
    	catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
    	}
	}

    @RequestMapping(value = "/{role_id:\\d+}", method = RequestMethod.PUT,produces=MediaType.APPLICATION_JSON_VALUE)
	public Role updateRole(@RequestBody Role role,@PathVariable Long role_id) {
    	try {
    		roleService.updateRole(role);
        	return role;
    	} catch (Exception e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
    	}
	}


    @RequestMapping(value = "/{role_id:\\d+}/resource", method = RequestMethod.GET)
	public List<Resources> getRoleResource(@PathVariable Long role_id) {
		Subject currentUser = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipals().getPrimaryPrincipal();
		
    	List<Resources> resourceList = roleService.getResourceByRole(role_id, shiroUser);
    	return resourceList;
	}

    @RequestMapping(value = "/{role_id:\\d+}/resource/{resource_id:\\d+}", method = RequestMethod.POST)
	public RestResult updateRoleResource(@PathVariable Long role_id,@PathVariable Long resource_id){
    	try {
    		roleService.updateRoleResource(role_id, resource_id);
    		return new RestResult("权限-系统权限","修改成功");
    	} catch (Exception e) {
			throw new RestException(HttpStatus.SERVICE_UNAVAILABLE, "权限-系统权限 错误");
		}
	}
    
}
