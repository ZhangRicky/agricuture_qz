package com.chenghui.agriculture.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.exception.RestException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.Digests;
import com.chenghui.agriculture.core.utils.Encodes;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.RestResult;
import com.chenghui.agriculture.core.utils.TreeUtil;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.Users;
import com.chenghui.agriculture.model.tree.TreeNode;
import com.chenghui.agriculture.service.system.ConstantsService;
import com.chenghui.agriculture.service.system.UserService;

@RequestMapping("/user")
@RestController
public class UserController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class); 

	
	@Autowired
	UserService userService;
	
	@Autowired
	ConstantsService constantsService;
	
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
	public Users getUser(@PathVariable Long id) {
		Users user = userService.get(id);
		return user;
	}
	
    @RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<Users> getUserList() {
		List<Users> userList = userService.findAll();
		return userList;
	}
    
    @RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.DELETE)
	public RestResult removeUser(@PathVariable Long id) {
    	try {
//    		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();

        	userService.removeUserByID(id);
        	return new RestResult(0,"删除成功");
    	} catch (DataIntegrityViolationException ve) {
			throw new RestException(HttpStatus.CONFLICT, "操作失败" + ve.getMessage());
	    } catch (Exception e) {
	    	throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败" + e.getMessage());
	    }
	}
    @RequestMapping(value = "/setInUseFalse/{id:\\d+}", method = RequestMethod.DELETE)
    public RestResult setInUseFalseById(@PathVariable Long id) {
    	try {
    		userService.setInUseFalseById(id);
    		return new RestResult(0,"删除成功");
    	} catch (DataIntegrityViolationException ve) {
    		throw new RestException(HttpStatus.CONFLICT, "操作失败" + ve.getMessage());
    	} catch (Exception e) {
    		throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败" + e.getMessage());
    	}
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult updateUser(@RequestBody Users user) {
		try {
			//user.setDeptLevel(constantsService.get(user.getDeptLevel().getId()));
    		userService.updateUserInfo(user);
    		return new RestResult("0", "修改用户"+user.getRealName()+" 成功!");
    	}catch (BusinessServiceException e) {
    		logger.error(e.getMessage());
			return new RestResult(-1,e.getMessage());
    	}catch (Exception e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
    	}
	}
    @RequestMapping(value = "/add", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult addUser(@RequestBody Users user) {
    	

		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
//    	Hibernate.initialize(user.getRole());
		try {
		//	user.setDeptLevel(constantsService.get(user.getDeptLevel().getId()));
			userService.add(user, shiroUser);
			return new RestResult("0", "添加用户 "+user.getRealName()+" 成功!");

		} catch (BusinessServiceException e) {
			logger.error(e.getMessage());
			return new RestResult("-1", "添加用户 "+user.getRealName()+" 失败!"+e.getMessage());
    	}catch (Exception e) {
    		logger.error(e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    	}
	}
    @RequestMapping(value = "/search", method = RequestMethod.GET)
	public PaginationSupport<Users> getUserList(@RequestParam("page") String page,
										@RequestParam("pageSize") String pageSize,
										@RequestParam("userName") String userName
										) {
		Users userParam = new Users();
		userParam.setUserName(userName);
		userParam.setRealName(userName);
		
		
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		
		PaginationSupport<Users> pagination = userService.findUserListForPage(userParam,Integer.parseInt(pageSize), Integer.parseInt(page),shiroUser);

		return pagination;
	}

	@RequestMapping(value = "/{user_id:\\d+}/role/all", method = RequestMethod.GET)
	public List<Object[]> getRoleListAll(@PathVariable Long user_id) throws BusinessServiceException {
		List<Object[]> roleList = userService.findRoleAllByUserID(user_id);
		return roleList;
	}
    
	@RequestMapping(value = "/{user_id:\\d+}/roles", method = RequestMethod.GET)
	public Set<Role> getRoleList(@PathVariable Long user_id) throws BusinessServiceException {
		Set<Role> roleSet = userService.findRoleByUserID(user_id);
		return roleSet;
	}
	@RequestMapping(value = "/{user_id:\\d+}/role/{role_id:\\d+}", method = RequestMethod.DELETE)
	public Set<Role> removeRole(@PathVariable Long user_id,@PathVariable Long role_id) throws BusinessServiceException{
		Set<Role> roleSet = userService.removeUserRole(user_id,role_id);
		return roleSet;
	}
	@RequestMapping(value = "/{user_id:\\d+}/role/{role_id:\\d+}", method = RequestMethod.POST)
	public Set<Role> addRole(@PathVariable Long user_id,@PathVariable Long role_id) throws BusinessServiceException{
		Set<Role> roleSet = userService.addUserRole(user_id,role_id);
		return roleSet;
	}
	@RequestMapping(value = "/{user_id:\\d+}/role/{role_id:\\d+}", method = RequestMethod.PUT)
	public Set<Role> setRole(@PathVariable Long user_id,@PathVariable Long role_id) throws BusinessServiceException{
		Set<Role> roleSet = userService.modifyUserRole(user_id,role_id);
		return roleSet;
	}

	@RequestMapping(value = "/{user_id:\\d+}/resetpassword", method = RequestMethod.PUT)
	public RestResult resetPassword(@PathVariable Long user_id) throws BusinessServiceException{
		try {
        	userService.updateResetPassword(user_id);
        	return new RestResult("重置密码","重置密码成功");
    	} catch (Exception e) {
    		e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
    	}
	}

	@RequestMapping(value = "/{user_id:\\d+}/resources", method = RequestMethod.GET)
	public List<Resources> getResourceList(@PathVariable Long user_id) throws BusinessServiceException {
		List<Resources> resourceList = userService.findResourceByUserID(user_id);
		return resourceList;
	}


	@RequestMapping(value = "/my-profile", method = RequestMethod.GET)
	public Users getMyProfile() throws BusinessServiceException {
		Subject currentUser = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipals().getPrimaryPrincipal();
		Long user_id = shiroUser.id;
		return userService.get(user_id);
	}
    @RequestMapping(value = "/save-my-profile", method = RequestMethod.PUT,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult saveMyProfile(@RequestBody Users user) {
    	Subject currentUser = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipals().getPrimaryPrincipal();
		Long user_id = shiroUser.id;
		if(!user_id.equals(user.getId())) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "只能修改自己的信息");
		}
    	try {
    		Users thisUser = userService.get(user.getId());
    		thisUser.setRealName(user.getRealName());
    		thisUser.setMobile(user.getMobile());
    		thisUser.setEmail(user.getEmail());
    		userService.updateUserInfo(thisUser);
    		return new RestResult("修改个人信息","修改成功");
    	} catch (Exception e) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
    	}
	}

    @RequestMapping(value = "/reset-my-password", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult setMyPassword(@RequestBody ModelMap passwordMap) {
    	Subject currentUser = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipals().getPrimaryPrincipal();
		Long user_id = shiroUser.id;
    	try {
    		userService.updateUserPassword(user_id, 
    				passwordMap.get("oldPassword").toString(), 
    				passwordMap.get("newPassword").toString());
        	return new RestResult(0,"修改成功");

    	} catch (BusinessServiceException e) {
    		logger.error(e.getMessage());
    		return new RestResult(-1, e.getMessage());
	    } catch (Exception e1) {
	    	logger.error(e1.getMessage());
	        	throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR,e1.getMessage());
		}
    }
    
    private static final int SALT_SIZE = 8;
    public static final int HASH_INTERATIONS = 1024;
    @RequestMapping(value = "/modiPswd", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
    public RestResult modiPswd(){
    	List<Users> allList = userService.findAll();
    	for(Users user : allList){
    		if (user.getUserName().equals("admin")) continue;
    		String userName = user.getUserName();
    		String mobile = user.getMobile();
    		String password = user.getMobile();
    		if (mobile==null || mobile.isEmpty()){
    			password = userName.trim();
    		} else {
    			password=user.getMobile().trim();
    		}
    		//计算密码
    		byte[] salt = Digests.generateSalt(SALT_SIZE);
    		user.setSalt(Encodes.encodeHex(salt));
    		byte[] hashPassword = Digests.sha1(password.getBytes(), salt, HASH_INTERATIONS);
    		user.setPassword(Encodes.encodeHex(hashPassword));
    		userService.update(user);

    		System.out.println(user.getRealName()+"," +userName+","+password+"salt:"+user.getSalt());
    	}
    	
    	return new RestResult(0,"修改成功");
    }
    
//    @RequestMapping(value = "/department", method = RequestMethod.GET)
//    public List<Department> getUserDepartment(){
//    	Subject currentUser = SecurityUtils.getSubject();
//		ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipals().getPrimaryPrincipal();
//		Long user_id = shiroUser.id;
//    	List<Department> departmentList= userService.findDepartmentByUserID(user_id);
//    	if(departmentList==null){
//    		throw new RestException(HttpStatus.NOT_FOUND, "请求资源不存在");
//    	}
//    	return departmentList;
//    }
//    @RequestMapping(value = "/departmentTree/{user_id:\\d+}", method = RequestMethod.GET)
//    public List<TreeNode> getUserDepartmentTree(@PathVariable Long user_id){
////    	Subject currentUser = SecurityUtils.getSubject();
////    	Long user_id = shiroUser.id;
//    	List<Department> departmentList= userService.findDepartmentByUserID(user_id);
//    	if(departmentList==null){
//    		throw new RestException(HttpStatus.NOT_FOUND, "请求资源不存在");
//    	}
//    	return TreeUtil.buildTreeNodeList(TreeUtil.convertTreeNodeList(departmentList));
//    }
    
    /**
     * 用户绑定地区权限
     * @param user_id
     * @return
     */
    @RequestMapping(value = "areaTree/{user_id:\\d+}", method = RequestMethod.GET)
    public List<TreeNode> getUserAreaTree(@PathVariable Long user_id){
//    	Subject currentUser = SecurityUtils.getSubject();
//    	Long user_id = shiroUser.id;
    	List<Area> areaList= userService.findAreaByUserID(user_id);
    	if(areaList==null){
    		throw new RestException(HttpStatus.NOT_FOUND, "请求资源不存在");
    	}
    	return TreeUtil.buildTreeNodeList(TreeUtil.convertTreeNodeLists(areaList));
    }
    
//    @RequestMapping(value = "/{user_id:\\d+}/department/{department_id:\\d+}", method = RequestMethod.POST)
//    public RestResult updateUserDepartment(@PathVariable Long user_id,@PathVariable Long department_id){
//    	try {
//    		userService.updateUserDepartment(user_id, department_id);
//    		return new RestResult("权限-部门","修改成功");
//    	} catch (Exception e) {
//    		throw new RestException(HttpStatus.SERVICE_UNAVAILABLE, "权限-部门 错误");
//    	}
//    }
    @RequestMapping(value = "/{user_id:\\d+}/areas/{area_ids}", method = RequestMethod.POST)
    public RestResult updateUserAreas(@PathVariable Long user_id,@PathVariable String[] area_ids){
    	try {
//    		Long[] depts = new Long[department_ids.length];
    		Set<Long> areaIdSet = new HashSet<Long>();
    		for(String area_id: area_ids){
    			areaIdSet.add(Long.parseLong(area_id));
    		}
    		
    		userService.updateUserAreas(user_id, areaIdSet);
    		return new RestResult("权限-部门", 0, "修改成功");
    	} catch (Exception e) {
    		throw new RestException(HttpStatus.SERVICE_UNAVAILABLE, "权限-部门 错误");
    	}
    }
    
    @RequestMapping(value = "/{user_id:\\d+}/{roleName}/role", method = RequestMethod.GET)
	public List<Role> getRoleByUser(@PathVariable Long user_id,@PathVariable String roleName){
    	if("null".equals(roleName)){roleName = "";}
    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
    	
    	List<Role> roleList  = new ArrayList<Role>();
    	
    	roleList = userService.getRoleByUser(user_id,roleName,shiroUser);
		if(roleList==null){
			throw new RestException(HttpStatus.NOT_FOUND, "请求资源不存在");
		}
		return roleList;
	}
    
    @RequestMapping(value = "/{user_id:\\d+}/role/{role_id:\\d+}/{addOrRemove}", method = RequestMethod.POST)
	public RestResult updateUserRole(@PathVariable Long user_id,@PathVariable Long role_id,@PathVariable String addOrRemove){
    	String result = "";
    	try {
    		if("add".equalsIgnoreCase(addOrRemove)||"remove".equalsIgnoreCase(addOrRemove)){
        		result = userService.updateUserRole(user_id, role_id, addOrRemove);
    		}
			return new RestResult(result,"修改成功");
    	} catch (Exception e) {
			throw new RestException(HttpStatus.SERVICE_UNAVAILABLE, "权限-用户 错误");
		}
	}
}
