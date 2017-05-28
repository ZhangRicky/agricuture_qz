package com.chenghui.agriculture.dao.supervisionManage.impl;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.supervisionManage.PointDao;
import com.chenghui.agriculture.model.Point;
import com.chenghui.agriculture.model.Projects;

/**
 * 地理坐标接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class PointDaoImpl extends GenericHibernateDao<Point, Long> implements PointDao {

	/**
	 * 根据项目id查询项目范围
	 *
	 */
	@Override
	public List<Point> findProjectsRange(Long determinePid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Point.class);
		Projects projects = new Projects();
		projects.setId(determinePid);
		detachedCriteria.add(Restrictions.eq("projects", projects));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Point> list = (List<Point>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}

	@Override
	public List<Point> getByPid(Long id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Point.class);
		Projects projects = new Projects();
		projects.setId(id);
		detachedCriteria.add(Restrictions.eq("projects", projects));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Point> list = (List<Point>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}

	@Override
	public List<Point> findPointByProjectIdAndRangeNumberAnd(Long adjustmentPid, Integer rangeNumber) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Point.class);
		Projects projects = new Projects();
		projects.setId(adjustmentPid);
		detachedCriteria.add(Restrictions.eq("projects", projects));
		detachedCriteria.add(Restrictions.eq("rangeNumber", rangeNumber));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Point> list = (List<Point>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}


}
