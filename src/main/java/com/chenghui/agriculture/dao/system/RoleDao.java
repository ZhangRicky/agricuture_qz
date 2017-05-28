package com.chenghui.agriculture.dao.system;

import java.util.List;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.Users;

public interface RoleDao extends GenericDao<Role, Long>{
	
	Role findDefaultRoleByUser(Users user);
	
	List<Role> findRoleByUser(Users user);

	Role getSystemDefaultRole();
	
	/**
	 * Get role by role type key
	 * @return
	 * @author yudq
	 * @since 2015-08-27 11:44:00
	 */
	Role getRoleByRoleTypeKey(int roleTypeKey);
	
	Role getRoleByName(Role role) ;

}
