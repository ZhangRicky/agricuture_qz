package com.chenghui.agriculture.dao.system.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.RoleDao;
import com.chenghui.agriculture.dao.system.RoleTypeDao;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.Users;

@SuppressWarnings("unchecked")
@Repository
public class RoleDaoImpl extends GenericHibernateDao<Role, Long> implements RoleDao {

	@Autowired
	RoleTypeDao roleTypeDao;
	
	@Override
	public Role findDefaultRoleByUser(Users user) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
		if(!"admin".equalsIgnoreCase(user.getUserName())) {
			detachedCriteria.createAlias("users", "user");
			detachedCriteria.add(Restrictions.eq("user.id", user.getId()));
		}
		List<Role> list = (List<Role>)getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list.size()==0) {
			return null;
		}
		for (Role role : list) {
			return role;
		}
		return null;
	}
	
	@Override
	public List<Role> findRoleByUser(Users user) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
		if(!"admin".equalsIgnoreCase(user.getUserName())) {
			detachedCriteria.createAlias("users", "user");
			detachedCriteria.add(Restrictions.eq("user.id", user.getId()));
		}
		List<Role> list = (List<Role>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}

	@Override
	public Role getSystemDefaultRole() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
		detachedCriteria.add(Restrictions.eq("roleName", "guest"));
		List<Role> list = (List<Role>)getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list.size()==0) {
			return null;
		}
		for (Role role : list) {
			return role;
		}
		return null;
	}
	
	@Override
	public Role getRoleByRoleTypeKey(int roleTypeKey) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
		detachedCriteria.createAlias("roleType", "roleType");
		detachedCriteria.add(Restrictions.eq("roleType.typeKey", roleTypeKey));
		List<Role> list = (List<Role>)getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list.size()==0) {
			return null;
		}
		for (Role role : list) {
			return role;
		}
		return null;
	}
	
	@Override
	public Role getRoleByName(Role role) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Role.class);
		detachedCriteria.add(Restrictions.eq("roleName", role.getRoleName()));
		List<Role> list = (List<Role>)getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list.size()==0) {
			return null;
		}
		for (Role r : list) {
			return r;
		}
		return null;
	}

}
