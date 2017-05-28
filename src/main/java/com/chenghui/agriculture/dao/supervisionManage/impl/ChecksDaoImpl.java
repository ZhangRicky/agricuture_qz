package com.chenghui.agriculture.dao.supervisionManage.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.supervisionManage.ChecksDao;
import com.chenghui.agriculture.model.Checks;
import com.chenghui.agriculture.model.SubProject;

/**
 * 验收记录接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class ChecksDaoImpl extends GenericHibernateDao<Checks, Long> implements ChecksDao {
	
	@Override
	public List<Long> add(List<Checks> models) throws DataAccessException {
		List<Long> ids = new ArrayList<Long>();
		for (Checks checks : models) {
			ids.add(add(checks));
		}
		return ids;
	}

	@Override
	public List<Checks> getCheckssBySId(long sId){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Checks.class);
		detachedCriteria.createAlias("subProject", "subProject");
		SubProject subProject = new SubProject();
		subProject.setId(sId);
		detachedCriteria.add(Restrictions.eq("subProject", subProject));
		List<Checks> checksList = (List<Checks>) getHibernateTemplate().findByCriteria(detachedCriteria);
		for (Checks checks : checksList) {
			Hibernate.initialize(checks.getSupervisions());
			Hibernate.initialize(checks.getPoints());
		}
		return checksList;
	}
	
}
