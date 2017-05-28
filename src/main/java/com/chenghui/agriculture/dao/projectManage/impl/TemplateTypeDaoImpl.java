package com.chenghui.agriculture.dao.projectManage.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.TemplateTypeDao;
import com.chenghui.agriculture.model.TemplateType;

/**
 * 流程类型访问接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class TemplateTypeDaoImpl extends GenericHibernateDao<TemplateType, Long> implements TemplateTypeDao {
	
	@Override
	public TemplateType geTemplateTypeByTypekey(int typeKey){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TemplateType.class);		
		detachedCriteria.add(Restrictions.eq("typeKey", typeKey));
		List<TemplateType> templateType = findByCriteria(detachedCriteria);
		
		if (templateType.size() >0){
			return templateType.get(0);
		} 
		return null;
	}

}
