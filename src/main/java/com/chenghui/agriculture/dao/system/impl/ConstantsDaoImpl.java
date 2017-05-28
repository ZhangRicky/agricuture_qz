package com.chenghui.agriculture.dao.system.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.dao.DataAccessException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.ConstantsDao;
import com.chenghui.agriculture.model.Constants;
import com.chenghui.agriculture.model.Users;
import com.chenghui.agriculture.service.system.impl.UserServiceImpl;
@SuppressWarnings("unchecked")
@Repository
public class ConstantsDaoImpl extends GenericHibernateDao<Constants, Long>implements ConstantsDao {

	@Autowired
	UserServiceImpl userService;
	
	@Override
	public List<Constants> getConstantsByType(int type) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Constants.class);
		
		detachedCriteria.add(Restrictions.and(Restrictions.eq("type", String.valueOf(type)),Restrictions.eq("isDeleted", false)));
		//如果获取信息源单位，根据当前用户deptLevel的值，判断可以显示哪些信息源单位
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		Users user = userService.findUserByLoginName(shiroUser.getLoginName());
		Constants deptLevel = user.getDeptLevel();
		if ((!shiroUser.isAdmin) && (type == Constants.TYPE_XXYDW)){
			detachedCriteria.add(Restrictions.like("code", deptLevel.getCode(),MatchMode.START));
		}
		//END.
		detachedCriteria.addOrder(Order.asc("orderBy")).addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		return findAllByCriteria(detachedCriteria);
	}

	@Override
	public List<Constants> findByHQL(String string, Object... values) throws DataAccessException {
		// TODO Auto-generated method stub
		return super.findByHQL(string, values);
	}
	
	@Override
	public List<Constants> getConstantsByType_NoFilter(int type) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Constants.class);
		
		detachedCriteria.add(Restrictions.and(Restrictions.eq("type", String.valueOf(type)),Restrictions.eq("isDeleted", false)));
		//END.
		detachedCriteria.addOrder(Order.asc("orderBy")).addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		return findAllByCriteria(detachedCriteria);
	}

	
	@Override
	public List<Constants> getByType(int type) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Constants.class);
		
		detachedCriteria.add(Restrictions.and(Restrictions.eq("type", String.valueOf(type))));
		//如果获取信息源单位，根据当前用户deptLevel的值，判断可以显示哪些信息源单位
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		Users user = userService.findUserByLoginName(shiroUser.getLoginName());
		Constants deptLevel = user.getDeptLevel();
		if ((!shiroUser.isAdmin) && (type == Constants.TYPE_XXYDW)){
			detachedCriteria.add(Restrictions.like("code", deptLevel.getCode(),MatchMode.START));
		}
		//END.
		detachedCriteria.addOrder(Order.asc("orderBy")).addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		return findAllByCriteria(detachedCriteria);
	}

	@Override
	public Constants getConstantsByDisplayName(Constants constants) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Constants.class);
		detachedCriteria.add(Restrictions.and(Restrictions.eq("displayName", constants.getDisplayName()),Restrictions.eq("type", constants.getType())));
		List<Constants> list = (List<Constants>) getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list.size()==0) {
			return null;
		}
		for (Constants con : list) {
			return con ;
		}
		return null;
	}

	

	
}
