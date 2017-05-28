package com.chenghui.agriculture.service.system;

import java.util.List;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.RoleType;
import com.chenghui.agriculture.model.Users;
import com.chenghui.agriculture.service.GenericService;

public interface RoleService extends GenericService<Role, Long> {

	void addResource(Resources resource);

	List<Users> getUserByRole(Long role,String userName,ShiroUser currentUser);

//	List<Area> getAreaByRole(Long role);
	
	List<Resources> getResourceByRole(Long role_id, ShiroUser currentUser);

	void updateRoleArea(Long role_id, Long area_id);
	
	void updateRole(Role roleVO);

	void updateRoleResource(Long role_id, Long resource_id);

	String updateRoleUser(Long role_id, Long user_id, String addOrRemove);
	
	void removeRole(Long role);

	PaginationSupport<Role> findRoleListForPage(Role queryParam,int iDisplayLength, int iDisplayStart, ShiroUser shiroUser);

	Long add(Role role, ShiroUser shiroUser);
	
	Role findDefaultRoleByUser(Users user);

	Role getSystemDefaultRole();
	
	List<Role> findApproveRoles();
	
	List<RoleType> getRoleTypes();

}
