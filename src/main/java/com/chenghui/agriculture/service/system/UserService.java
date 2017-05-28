package com.chenghui.agriculture.service.system;

import java.util.List;
import java.util.Set;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Department;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.Users;
import com.chenghui.agriculture.service.GenericService;

public interface UserService extends GenericService<Users,Long>{

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;

	Set<Role> findRoleByUserID(Long user_id);

	Set<Role> removeUserRole(Long user_id, Long role_id);

	Set<Role> addUserRole(Long user_id, Long role_id);

	List<Resources> findResourceByUserID(Long user_id);
	
//	List<Constants> getAllConstants();

	Users findUserByLoginName(String username);
	
	Users updateUserInfo(Users user);

	Long add(Users user);

	List<Object[]> findRoleAllByUserID(Long user_id);

	Set<Role> modifyUserRole(Long user_id, Long role_id);

	void updateResetPassword(Long user_id);

	void removeUserByID(Long id);
	
	void setInUseFalseById(Long id);

	void updateUserPassword(Long user_id, String plainPassword,String newPassword);

	PaginationSupport<Users> findUserListForPage(Users user, int limit,int start, ShiroUser shiroUser);

	Long add(Users user, String operatorRoleName, String operatorUserName) throws BusinessServiceException;
	
	Long add(Users user, ShiroUser shiroUser) throws BusinessServiceException;
	
	//List<Department> findDepartmentByUserID(Long user_id);
	
	List<Area> findAreaByUserID(Long user_id);
	
//	void updateUserDepartment(Long user_id, Long department_id);
	
	void updateUserAreas(Long user_id, Set<Long> area_ids);
	
	List<Role> getRoleByUser(Long user_id,String roleName,ShiroUser currentUser);
	
	String updateUserRole(Long user_id, Long role_id, String addOrRemove);
	
}
