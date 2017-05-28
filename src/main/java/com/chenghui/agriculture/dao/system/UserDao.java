package com.chenghui.agriculture.dao.system;

import java.util.List;
import java.util.Set;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.Users;

public interface UserDao extends GenericDao<Users, Long>{

	Set<Users> findUserByRole(Role role);

	PaginationSupport<Users> findPageByExample(Users user, int limit, int start);
	
	boolean isExistUserByName(Users user);

	PaginationSupport<Users> findUserPageByCurrentUserGroup(Users user, int limit,int start, ShiroUser shiroUser);

	List<Users> findUserByCreateRoleName(String operatorRoleName, String userName);
	
	List<Users> findUserByCreateUserName(String userName);
}
