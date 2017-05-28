package com.chenghui.agriculture.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.Digests;
import com.chenghui.agriculture.core.utils.Encodes;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.system.DepartmentDao;
import com.chenghui.agriculture.dao.system.ResourceDao;
import com.chenghui.agriculture.dao.system.RoleDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.Users;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.system.UserService;

@Service("userService")
public class UserServiceImpl extends GenericServiceImpl<Users, Long> implements UserService {
	
	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class); 
//	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private static final int SALT_SIZE = 8;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private DepartmentDao departmentDao;
	
//	@Autowired
//	private ConstantsDao constantsDao;

	@Override
	public Set<Role> findRoleByUserID(Long user_id) {
		Set<Role> roleSet = new HashSet<Role>();
		Users user = userDao.get(user_id);
		if (user != null) {
			Hibernate.initialize(user.getRole());
			roleSet = user.getRole();
			for (Role role : roleSet) {
				Hibernate.initialize(role.getResources());
			}
		}
		return roleSet;
	}

//	@Override
//	public List<Constants> getAllConstants() {
//		String hql="from Constants where type=2";
//		return constantsDao.findByHQL(hql,new Object[]{});
//	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.DelPrivilege, opText="删除用户角色")
	public Set<Role> removeUserRole(Long user_id, Long role_id) {
		Users user = userDao.get(user_id);
		Role role = roleDao.get(role_id);

		if(user==null){
			throw new BusinessServiceException("User未找到");
		}
		if(role==null){
			throw new BusinessServiceException("Role未找到");
		}
		Hibernate.initialize(user.getRole());
		user.getRole().remove(role);
		userDao.save(user);
		return user.getRole();
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.Add, opText="用户角色")
	public Set<Role> addUserRole(Long user_id, Long role_id) {
		Users user = userDao.get(user_id);
		Role role = roleDao.get(role_id);

		if(user==null){
			throw new BusinessServiceException("User未找到");
		}
		if(role==null){
			throw new BusinessServiceException("Role未找到");
		}
		Hibernate.initialize(user.getRole());
		user.getRole().add(role);
		userDao.save(user);
		return user.getRole();
	}

	@Override
	public List<Resources> findResourceByUserID(Long user_id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Resources.class);
		detachedCriteria.createAlias("role", "roleEntity");
		detachedCriteria.createAlias("roleEntity.user", "userEntyty");
		detachedCriteria.add(Restrictions.eq("userEntyty.id", user_id));
		detachedCriteria.addOrder(Order.asc("id"));
		return resourceDao.findByCriteria(detachedCriteria);
	}

	@Override
	public Users findUserByLoginName(String userName) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
		detachedCriteria.add(Restrictions.eq("userName", userName));
		List<Users> userList = new ArrayList<Users>();
		userList = userDao.findByCriteria(detachedCriteria);
		if(userList==null||userList.size()==0){
			return null;
		}else {
			Hibernate.initialize(userList.get(0).getRole());
		//	Hibernate.initialize(userList.get(0).getDepartment());
			Hibernate.initialize(userList.get(0).getArea());
		}
		
		return userList.get(0);
	}
	/**
	 * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	 */
	private void entryptPassword(Users user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));
		byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}

	@Override
	public Long add(Users user,String operatorRoleName,String operatorUserName) {
		boolean bb = userDao.isExistUserByName(user);  
		if (!bb) {
			//注解掉添加guess角色到用户，暂时用途不明
			/*List<Role> rList = roleDao.findByName("guest");
			Role guestRole = null;
			if(rList.size()>0) {
				guestRole = rList.get(0);
			}else {
				
			}
			Set<Role> roleSet = new HashSet<Role>();
			roleSet.add(guestRole);
			user.setRole(roleSet);*/
			
			user.setIsFirstLogin(true);
			user.setCreated(new Date());
			setExpireDate(user, 90);

			if (StringUtils.isBlank(user.getPlainPassword())) {
				setDefaultPassword(user, "_Xz");
			}
			entryptPassword(user);
			
			// 设置创建者
//			user.setCreateRoleName(operatorRoleName);
			user.setCreateUserName(operatorUserName);
			
			userDao.add(user);
			
			return user.getId();
		}else{
		   throw new BusinessServiceException("登录名称 "+user.getUserName()+" 已存在");
		}
	}
	
	private void setExpireDate(Users user, Integer offset) {
//		Date expireDate = DateUtils.addDays(new Date(), offset);
		Date expireDate = DateUtils.addYears(new Date(), offset);
		user.setExpireDate(expireDate);
	}

//	@SuppressWarnings("rawtypes")
	@Override
	public PaginationSupport<Users> findUserListForPage(Users user, int limit,
			int start, ShiroUser shiroUser) {

		PaginationSupport<Users> userPage = userDao.findUserPageByCurrentUserGroup(user, limit, start, shiroUser);
//		List userList = userPage.getItems();
//		List<User> newList = new ArrayList<User>();
//		for (Object user2 : userList) {
//			Object[] userObject = (Object[]) user2;
//			UserInfo userInfo = (UserInfo) (userObject[userObject.length-2]);
//			User userResult = (User) (userObject[userObject.length-1]);
//			userResult.setUserInfo(userInfo);
//			userResult.setUserGroup(getDefaultRoleName(userResult));
//			newList.add(userResult);
//		}
//		userPage.setItems(newList);
		return userPage;
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.UpdatePrivilege, opText="修改个人信息")
	public Users updateUserInfo(Users user) {
		Users persistUser = userDao.get(user.getId());
		persistUser.setRealName(user.getRealName());
		persistUser.setUserName(user.getUserName());
		persistUser.setMobile(user.getMobile());
		persistUser.setEmail(user.getEmail());
		persistUser.setDescription(user.getDescription());
		persistUser.setIsInUse(user.getIsInUse());
//		persistUser.setDeptLevel(user.getDeptLevel());
		try {
			userDao.update(persistUser);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return persistUser;
	}

	@Override
	public List<Object[]> findRoleAllByUserID(Long user_id) {
		List<Object[]> listResult = new ArrayList<Object[]>();
		Users persistUser = userDao.get(user_id);
		Set<Role> userRoleSet = persistUser.getRole();
		List<Role> allRoleList = roleDao.findAll();
		for (Role role : allRoleList) {
			listResult.add(new Object[] {role.getId(),role.getRoleDesc(),userRoleSet.contains(role)?true:false});
		}
		return listResult;
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.UpdatePrivilege, opText="修改用户角色")
	public Set<Role> modifyUserRole(Long user_id, Long role_id) {
		Users persistUser = userDao.get(user_id);
		Role persistRole = roleDao.get(role_id);
		Set<Role> roleSet = new HashSet<Role>();
		roleSet.add(persistRole);
		persistUser.setRole(roleSet);
		userDao.update(persistUser);
		return persistUser.getRole();
		
	}

	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.UpdatePrivilege, opText="重置用户密码")
	@Override
	public void updateResetPassword(Long user_id) {
		Users persistUser = userDao.get(user_id);
		String userName = setDefaultPassword(persistUser,"_Cz");

		setExpireDate(persistUser,90);
		persistUser.setIsFirstLogin(true);
		entryptPassword(persistUser);
		try {
			userDao.update(persistUser);
			logger.info(userName+"重置密码成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessServiceException("重置密码失败");
		}
	}

	@Override
	public void updateUserPassword(Long user_id,String oldPassword,String newPassword) {
		Users persistUser = userDao.get(user_id);
		byte[] salt = Encodes.decodeHex(persistUser.getSalt());
		byte[] hashPassword = Digests.sha1(oldPassword.getBytes(), salt, HASH_INTERATIONS);
		if(!persistUser.getPassword().equals(Encodes.encodeHex(hashPassword))) {
			throw new BusinessServiceException("原密码不正确");
		}
		persistUser.setPlainPassword(newPassword);
		entryptPassword(persistUser);
		persistUser.setIsFirstLogin(false);
		try {
			userDao.update(persistUser);
			logger.info(persistUser.getUserName()+"重置密码成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessServiceException("重置密码失败");
		}
	}

	//默认密码 username+middleFix+Datetime
	private String setDefaultPassword(Users persistUser,String middleFix) {
		String userName = persistUser.getUserName();
//		String dateTime = sdf.format(new Date());
//		String newPlainPassword = userName+middleFix+dateTime;
		String newPlainPassword = userName;
		persistUser.setPlainPassword(newPlainPassword);
		return userName;
	}

	
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.DelPrivilege, opText="删除用户")
	public void removeUserByID(Long id) {
		Users user = userDao.get(id);
		List<Role> roleList = roleDao.findAll();
		for (Role role : roleList) {
			role.getUsers().remove(user);
			roleDao.save(role);
		}
		userDao.remove(user);
	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.UpdatePrivilege, opText="修改用户为不可用")
	public void setInUseFalseById(Long id) {
		Users user = userDao.get(id);
		user.setIsInUse(false);
		userDao.update(user);
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.AddPrivilege, opText="增加用户")
	public Long add(Users user, ShiroUser shiroUser) throws BusinessServiceException {
//		String operatorRoleName = shiroUser.getDefaultRoleName();
		String operatorRoleName = "";
		String operatorUserName = shiroUser.getLoginName();
		Long userId = this.add(user, operatorRoleName, operatorUserName);
		return userId;
	}

//	@Override
//	public List<Department> findDepartmentByUserID(Long user_id) {
//		List<Department> departmentAll = departmentDao.findAllWithoutOthers();
//		
//		Users user = userDao.get(user_id);
//		
//		Set<Department> departmentExists = departmentDao.findDepartmentSetByUser(user);
//		
//		for (Department department : departmentAll) {
//			if (departmentExists.contains(department)) {
//				department.setInUse(true);
//			} else {
//				department.setInUse(false);
//			}
//		}
//		return departmentAll;
//	}
	
	/**
	 * 用户绑定地区权限
	 */
	@Override
	public List<Area> findAreaByUserID(Long user_id) {
		List<Area> areaAll = departmentDao.findAllWithoutOther();
		
		Users user = userDao.get(user_id);
		
		Set<Area> areaExists = departmentDao.findAreaSetByUser(user);
		
		for (Area area : areaAll) {
			if (areaExists.contains(area)) {
				area.setInUse(true);
			} else {
				area.setInUse(false);
			}
		}
		return areaAll;
	}
	
//	@Override
//	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.UpdatePrivilege, opText="修改用户-部门")
//	public void updateUserDepartment(Long user_id,Long department_id) {
//		Users user = userDao.get(user_id);
//		Hibernate.initialize(user.getDepartment());
//		Department department = departmentDao.get(department_id);
//		if(user.getDepartment().contains(department)) {
//			user.getDepartment().remove(department);
//		}else {
//			user.getDepartment().add(department);
//		}
//		userDao.save(user);
//	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.UpdatePrivilege, opText="修改用户-部门(M)")
	public void updateUserAreas(Long user_id,Set<Long> area_ids) {
		Users user = userDao.get(user_id);
//		Hibernate.initialize(user.getArea());
//		user.getArea().clear();
		Set<Area> areas = new HashSet<>();
		for(Long area_id: area_ids){
			Area area = new Area();
			area.setId(area_id);
			areas.add(area);
//			user.getArea().add(area);
		}
		user.setArea(areas);
		userDao.save(user);
	}
	
	@Override
	public List<Role> getRoleByUser(Long user_id,String roleName,ShiroUser currentUser) {
		List<Role> roleAll = new ArrayList<Role>();
		
		Users loginUser = new Users();
		loginUser.setId(currentUser.getId());
		loginUser.setUserName(currentUser.getLoginName());
		//获取登录用户所拥有的角色
		roleAll = roleDao.findRoleByUser(loginUser); 
		
		Users user = userDao.get(user_id);

		List<Role> roleList = roleDao.findRoleByUser(user);
		List<Role> roleListResult = new ArrayList<Role>();
		for (Role role : roleAll) {
			if("admin".equals(role.getRoleName()) || role.getRoleName().indexOf(roleName)==-1) {
				continue;
			}
			if (roleList.contains(role)) {
				role.setCheck("in");
			} else {
				role.setCheck("out");
			}
			Hibernate.initialize(role.getResources());
			roleListResult.add(role);
		}

		return roleListResult;
	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.USER_CN, opType=OperationType.UpdatePrivilege, opText="修改用户-角色")
	public String updateUserRole(Long user_id, Long role_id, String addOrRemove) {
		String result = "";
		Role role = roleDao.get(role_id);
		Users user = userDao.get(user_id);
		
		if("add".equalsIgnoreCase(addOrRemove)){
			user.getRole().add(role);
		}else if("remove".equalsIgnoreCase(addOrRemove)){
			user.getRole().remove(role);
		}
		userDao.save(user);
		return result;
	}
}
