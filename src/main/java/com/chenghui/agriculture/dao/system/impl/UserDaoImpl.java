package com.chenghui.agriculture.dao.system.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Role;
import com.chenghui.agriculture.model.Users;

@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl extends GenericHibernateDao<Users, Long> implements UserDao {

	@Override
	public Set<Users> findUserByRole(Role role) {
		Hibernate.initialize(role.getUsers());
		return role.getUsers();
	}

	@Override
	public PaginationSupport<Users> findPageByExample(Users user, int limit,
			int start) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
		detachedCriteria.createAlias("userInfo","userInfo");
		if(StringUtils.isNotEmpty(user.getUserName())) {
			detachedCriteria.add(Restrictions.or(Restrictions.like("userName", user.getUserName(),MatchMode.ANYWHERE),
					Restrictions.like("userInfo.realName", user.getUserName(),MatchMode.ANYWHERE)));
		}
		detachedCriteria.add(Restrictions.ne("userName", "admin"));
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		return findPageByCriteria(detachedCriteria, limit, start);
	}

	@Override
	public List<Users> findAll()  throws DataAccessException{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
		detachedCriteria.add(Restrictions.ne("userName", "admin"));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		detachedCriteria.addOrder(Order.asc("id"));
		return (List<Users>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	@Override
	public boolean isExistUserByName(Users user) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
		detachedCriteria.add(Restrictions.eq("userName", user.getUserName()));
		List<Users> list = (List<Users>) getHibernateTemplate().findByCriteria(detachedCriteria);
		return (list==null||list.isEmpty())?false:true;
	}

	@Override
	public List<Users> findUserByCreateRoleName(String createRoleName, String userName) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
		if(StringUtils.isNotEmpty(createRoleName)) {
			detachedCriteria.add(Restrictions.eq("createRoleName", createRoleName));
			detachedCriteria.add(Restrictions.ne("userName", "admin"));//过滤admin用户
			if(!"".equals(userName)){
				detachedCriteria.add(Restrictions.ne("userName", userName));
			}
		}
		
		List<Users> list = (List<Users>) getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}
	@Override
	public List<Users> findUserByCreateUserName(String userName) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
			detachedCriteria.add(Restrictions.eq("createUserName", userName));
			detachedCriteria.add(Restrictions.ne("userName", "admin"));//过滤admin用户
			detachedCriteria.add(Restrictions.ne("userName", userName));
		
		List<Users> list = (List<Users>) getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}

	@Override
	public PaginationSupport<Users> findUserPageByCurrentUserGroup(Users user, int limit,int start,ShiroUser currentUser) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
		
		//获取当前用户所属角色下面所有的用户（非admin）
		if(!currentUser.isAdmin){
			detachedCriteria.add(Restrictions.or(
					Restrictions.eq("createUserName", currentUser.getLoginName())
					
				));
			
		}
		
		if(StringUtils.isNotEmpty(user.getUserName())) {
			detachedCriteria.add(Restrictions.or(
					Restrictions.like("userName", user.getUserName(),MatchMode.ANYWHERE),
					Restrictions.like("realName", user.getUserName(),MatchMode.ANYWHERE)
				));
		}
		
		detachedCriteria.add(Restrictions.ne("userName", "admin"));
		detachedCriteria.add(Restrictions.ne("userName", currentUser.getLoginName()));
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		return findPageByCriteria(detachedCriteria, limit, start);
//		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
//		detachedCriteria.createAlias("userInfo","userInfo");
//		if(StringUtils.isNotEmpty(user.getUserName())) {
//			detachedCriteria.add(Restrictions.or(Restrictions.like("userName", user.getUserName(),MatchMode.ANYWHERE),
//					Restrictions.like("userInfo.realName", user.getUserName(),MatchMode.ANYWHERE)));
//		}
//		if(!"admin".equalsIgnoreCase(currentUser.getLoginName())) {
//			detachedCriteria.add(Restrictions.eq("createRoleName", currentUser.getDefaultRoleName()));
//		}
//		detachedCriteria.add(Restrictions.ne("userName", "admin"));
//		detachedCriteria.addOrder(Order.desc("id"));
//		detachedCriteria.setProjection(null);
//		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
//		return findPageByCriteria(detachedCriteria, limit, start);
	}
	
	
	
}
