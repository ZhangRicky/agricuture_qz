package com.chenghui.agriculture.dao.system.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.core.exception.FlowTypeNotFoundException;
import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.FlowTypeDao;
import com.chenghui.agriculture.model.FlowType;

/**
 * 流程类型访问接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class FlowTypeDaoImpl extends GenericHibernateDao<FlowType, Long> implements FlowTypeDao {

	@Override
	public FlowType geFlowTypeByTypekey(int typeKey) throws FlowTypeNotFoundException{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FlowType.class);		
		detachedCriteria.add(Restrictions.eq("typeKey", typeKey));
		List<FlowType> flowType = findByCriteria(detachedCriteria);
		
		if (flowType.size() >0){
			return flowType.get(0);
		} else{
			throw new FlowTypeNotFoundException("获取流程类型失败！无该类型。typeKey:"+typeKey);
		}
		
	}

}
