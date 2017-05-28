package com.chenghui.agriculture.dao.supervisionManage.impl;


import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.supervisionManage.PointBaiduDao;
import com.chenghui.agriculture.model.PointBaidu;
import com.chenghui.agriculture.model.Projects;

/**
 * 地理坐标接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class PointBaiduDaoImpl extends GenericHibernateDao<PointBaidu, Long> implements PointBaiduDao {

	@Override
	public List<PointBaidu> getByPid(Long id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PointBaidu.class);
		Projects projects = new Projects();
		projects.setId(id);
		detachedCriteria.add(Restrictions.eq("projects", projects));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<PointBaidu> list = (List<PointBaidu>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}

}
