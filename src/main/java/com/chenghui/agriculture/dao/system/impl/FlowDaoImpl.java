package com.chenghui.agriculture.dao.system.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.core.exception.FlowNotFoundException;
import com.chenghui.agriculture.core.exception.FlowTypeNotFoundException;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.FlowDao;
import com.chenghui.agriculture.dao.system.FlowTypeDao;
import com.chenghui.agriculture.model.Flow;

@SuppressWarnings("unchecked")
@Repository
public class FlowDaoImpl extends GenericHibernateDao<Flow, Long> implements FlowDao {

	@Autowired
	FlowTypeDao flowTypeDao;
	
	@Override
	public boolean isExistFlowByName(Flow flow) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Flow.class);
		detachedCriteria.add(Restrictions.eq("flowType", flow.getFlowType()));
		detachedCriteria.add(Restrictions.eq("flag", 1)); //0:删除，1:正常
		detachedCriteria.add(Restrictions.eq("isInUse", true)); //是否有效
		if (flow.getId() != null && flow.getId() > 0) {
			detachedCriteria.add(Restrictions.ne("id", flow.getId())); //更新时排除自己
		}
		List<Flow> list = (List<Flow>) getHibernateTemplate().findByCriteria(detachedCriteria);
		return (list==null||list.isEmpty())?false:true;
	}

	@Override
	public PaginationSupport<Flow> findPageByFlow(Flow flow, int limit, int start) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Flow.class);
		
		detachedCriteria.add(Restrictions.eq("flag", 1)); //0:删除，1:正常
		
		if(StringUtils.isNotEmpty(flow.getFlowName())) {
			detachedCriteria.add(Restrictions.like("flowName", flow.getFlowName(),MatchMode.ANYWHERE));
		}
		
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		return findPageByCriteria(detachedCriteria, limit, start);
	}

	@Override
	public Flow getFlowByFlowType(int typeKey) throws FlowTypeNotFoundException, FlowNotFoundException{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Flow.class);
		detachedCriteria.add(Restrictions.eq("flowType", flowTypeDao.geFlowTypeByTypekey(typeKey)));
		detachedCriteria.add(Restrictions.eq("flag", 1));
		detachedCriteria.add(Restrictions.eq("isInUse", true));
		
		List<Flow> flows = findByCriteria(detachedCriteria);
		if (!flows.isEmpty()) {
			Hibernate.initialize(flows.get(0).getFlowNodes());
			return flows.get(0);
		}else{
			throw new FlowNotFoundException("没有流程类型为"+typeKey+"的流程");
		}
	}
}
