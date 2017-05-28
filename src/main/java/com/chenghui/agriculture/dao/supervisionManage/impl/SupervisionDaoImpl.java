package com.chenghui.agriculture.dao.supervisionManage.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.supervisionManage.SupervisionDao;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.Supervision;

/**
 * 监管记录
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class SupervisionDaoImpl extends GenericHibernateDao<Supervision, Long> implements SupervisionDao {
	
	@Override
	public List<Long> add(List<Supervision> models) throws DataAccessException {
		List<Long> ids = new ArrayList<Long>();
		for (Supervision supervision : models) {
			ids.add(add(supervision));
		}
		return ids;
	}

	@Override
	public List<Supervision> getSupervisionsBySId(long sId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Supervision.class);
		detachedCriteria.createAlias("projects", "projects");
		Projects projects = new Projects();
		projects.setId(sId);
		detachedCriteria.add(Restrictions.eq("projects", projects));
		detachedCriteria.add(Restrictions.isNull("checks"));
		detachedCriteria.addOrder(Order.desc("id"));
		return (List<Supervision>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	@Override
	@CacheEvict(value={"projects","subProjects"}, allEntries=true)
	public Long add(Supervision model) throws DataAccessException {
		return super.add(model);
	}

}
