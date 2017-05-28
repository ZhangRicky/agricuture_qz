package com.chenghui.agriculture.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.system.ResourceDao;
import com.chenghui.agriculture.dao.system.RoleDao;
import com.chenghui.agriculture.dao.system.RoleTypeDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.Resources;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.RoleType;
import com.chenghui.agriculture.model.Users;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.system.ResourceService;
import com.chenghui.agriculture.service.system.RoleService;

@Service("roleService")
public class RoleServiceImpl extends GenericServiceImpl<Role, Long> implements RoleService {
//	private final static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private ResourceService resourceService;

//	@Autowired
//	private AreaDao areaDao;
	
	@Autowired
	private RoleTypeDao roleTypeDao;
	
	@Override
	public List<Role> findApproveRoles(){
		List<Role> allRoles = roleDao.findAll();
		List<Role> results = new ArrayList<Role>();
		for(Role role: allRoles){
			if (RoleType.ROLE_LEVEL_ASSESSOR == role.getRoleType().getTypeKey()) {
				results.add(role);
			}
		}
		return results;
	}
	
	@Override
	public List<Resources> getResourceByRole(Long role_id, ShiroUser currentUser) {
		List<Resources> resourceAll = new ArrayList<Resources>();
		if(!currentUser.isAdmin){
//			Role currentUserRole = roleDao.get(currentUser.getDefaultRoleId());
			resourceAll = resourceService.findResourceSetByShiroUser(currentUser);
		}else{
			resourceAll = resourceDao.findAll();
		}
		Role role = roleDao.get(role_id);
		List<Resources> resourceList = resourceDao.findResourceSetByRole(role);
		for (Resources resource : resourceAll) {
			if(resourceList.contains(resource)) {
				resource.setInUse(true);
			}else {
				resource.setInUse(false);
			}
		}
		return resourceAll;
	}

	public void addResource(Resources resource) {
		resourceDao.add(resource);
	}

	@Override
	public List<Users> getUserByRole(Long role_id,String userName,ShiroUser currentUser) {
		List<Users> userAll = new ArrayList<Users>();
		
		if(currentUser!=null) {
//			userAll = userDao.findUserByCreateRoleName(currentUser.getDefaultRoleName(), currentUser.getLoginName());
			userAll = userDao.findUserByCreateUserName(currentUser.getLoginName());
		}else {
			userAll = userDao.findAll();
		}
		Role role = roleDao.get(role_id);


		Set<Users> userSet = userDao.findUserByRole(role);
		/*for (Users userHasRole : userSet) {
			userSet.add(userHasRole);
		}*/
		List<Users> userListResult = new ArrayList<Users>();
		for (Users user : userAll) {
			if(user.getUserName().indexOf(userName)==-1 && user.getRealName().indexOf(userName)==-1) {
				continue;
			}
			if (userSet.contains(user)) {
				user.setCheck("in");
				Set<Role> roleSet = user.getRole();
				if(roleSet.size() == 0){
					user.setUserGroup("");
				}else{
					Iterator<Role> iter = roleSet.iterator();
					for(;iter.hasNext();){
						Role r = iter.next();
						user.setUserGroup(r.getRoleName());
					}
				}
			} else {
				user.setCheck("out");
				Set<Role> roleSet = user.getRole();
				if(roleSet.size() == 0){
					user.setUserGroup("");
				}else{
					Iterator<Role> iter = roleSet.iterator();
					for(;iter.hasNext();){
						Role r = iter.next();
						user.setUserGroup(r.getRoleName());
					}
				}
			}
			userListResult.add(user);
		}

		return userListResult;
	}

//	@Override
//	public List<Area> getAreaByRole(Long role_id) {
//
//		List<Area> areaAll = areaDao.findAll();

//		Role role = roleDao.get(role_id);
//
//		Set<Area> areaExists = areaDao.findAreaSetByRole(role);
//
//		for (Area area : areaAll) {
//			if (areaExists.contains(area)) {
//				area.setInUse(true);
//			} else {
//				area.setInUse(false);
//			}
//		}
//		return areaAll;
//	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ROLE_CN, opType=OperationType.UpdatePrivilege, opText="修改角色-地区")
	public void updateRoleArea(Long role_id,Long area_id) {
//		Role role = roleDao.get(role_id);
//		Hibernate.initialize(role.getArea());
//		Area area = areaDao.get(area_id);
//		if(role.getArea().contains(area)) {
//			role.getArea().remove(area);
//		}else {
//			role.getArea().add(area);
//		}
//		roleDao.save(role);
	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ROLE_CN, opType=OperationType.UpdatePrivilege, opText="修改角色-用户")
	public String updateRoleUser(Long role_id,Long user_id,String addOrRemove) {
		String result = "";
		Role role = roleDao.get(role_id);
		Users user = userDao.get(user_id);
		//清理所有现有的角色
		List<Role> roleList = roleDao.findAll();
		for (Role role2 : roleList) {
			if(role2.getUsers().contains(user)) {
				role2.getUsers().remove(user);
				roleDao.save(role2);
			}
		}
		if("add".equalsIgnoreCase(addOrRemove)){
			role.getUsers().add(user);
		}else if("remove".equalsIgnoreCase(addOrRemove)){
			role.getUsers().remove(user);
		}else{
			
		}
		roleDao.save(role);
		return result;
	}

	@Override
	public PaginationSupport<Role> findRoleListForPage(Role queryParam,
			int limit, int start,ShiroUser shiroUser) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
//		detachedCriteria.createAlias("roleType", "roleType");
		if (StringUtils.isNotEmpty(queryParam.getRoleName())) {
			detachedCriteria.add(
					Restrictions.or(
							Restrictions.like("roleName",queryParam.getRoleName(),MatchMode.ANYWHERE),
							Restrictions.like("roleDesc",queryParam.getRoleName(),MatchMode.ANYWHERE)
							)
					);
		}
		if(!shiroUser.isAdmin) {
//			detachedCriteria.add(Restrictions.eq("createRoleName", shiroUser.getDefaultRoleName()));
			detachedCriteria.add(Restrictions.eq("createUserName", shiroUser.getLoginName()));
		}
//		detachedCriteria.add(Restrictions.ne("roleType.typeKey", RoleType.ROLE_LEVEL_ADMIN));//管理员
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		PaginationSupport<Role> rolePage = roleDao.findPageByCriteria(detachedCriteria, limit, start);
//		List<Role> roleList = rolePage.getItems();
//		List<Role> newList = new ArrayList<Role>();
//		for (Object role : roleList) {
//			Object[] roleObject = (Object[]) role;
//			Role roleResult = (Role) (roleObject[roleObject.length-1]);
//			newList.add(roleResult);
//		}
//		rolePage.setItems(newList);
		
		return rolePage;
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ROLE_CN, opType=OperationType.UpdatePrivilege, opText="修改角色")
	public void updateRole(Role roleVO) {
		Role role = roleDao.get(roleVO.getId());
		if(!role.getRoleName().equals(roleVO.getRoleName())){
			if(roleDao.getRoleByName(roleVO)!=null){
				throw new BusinessServiceException("修改角色失败，角色名称不能重复！");
			}
		}
		
		RoleType roleType = new RoleType();
		roleType.setId(roleVO.getRoleTypeId());
		role.setRoleType(roleType);
		role.setRoleName(roleVO.getRoleName());
		role.setRoleDesc(roleVO.getRoleDesc());
		try{
			roleDao.update(role);
		}catch(Exception e){
			throw new BusinessServiceException("修改角色失败！");
		}
		
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ROLE_CN, opType=OperationType.AddPrivilege, opText="增加角色系统权限")
	public void updateRoleResource(Long role_id, Long resource_id) {
		Role role = roleDao.get(role_id);
		Resources resource = resourceDao.get(resource_id);
		if(role.getResources().contains(resource)) {
			role.getResources().remove(resource);
		}else {
			role.getResources().add(resource);
		}
		roleDao.update(role);
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ROLE_CN, opType=OperationType.DelPrivilege, opText="删除角色")
	public void removeRole(Long role_id) {
	
		Role role = roleDao.get(role_id);
		
		List<Users> userList = userDao.findUserByCreateRoleName(role.getRoleName(), "");
		if(userList.size() > 0) {
			throw new BusinessServiceException("系统中还有该角色创建的用户，无法删除");
		}
		roleDao.remove(role);
		
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ROLE_CN, opType=OperationType.AddPrivilege, opText="增加角色")
	public Long add(Role role, ShiroUser shiroUser) {
		role.setCreated(new Date());
		role.setCreateUserName(shiroUser.getName());
		RoleType roleType = new RoleType();
		roleType.setId(role.getRoleTypeId());
		role.setRoleType(roleType);
		if(roleDao.getRoleByName(role) != null){
			throw new BusinessServiceException("系统中已存在名为"+role.getRoleName()+" 的角色");
		}
		try {
			roleDao.add(role);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessServiceException(e.getLocalizedMessage());
		} 
		return role.getId();
	}

	@Override
	public Role findDefaultRoleByUser(Users user) {
		return 	roleDao.findDefaultRoleByUser(user);

	}

	@Override
	public Role getSystemDefaultRole() {
		return roleDao.getSystemDefaultRole();
	}
	
	@Override
	public List<RoleType> getRoleTypes(){
		return roleTypeDao.findAll();
	}
}
