package com.chenghui.agriculture.dao.projectManage.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.FarmerDao;
import com.chenghui.agriculture.model.Farmer;

/**
 * 流程类型访问接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class FarmerDaoImpl extends GenericHibernateDao<Farmer, Long> implements FarmerDao {
	@Override
	public Farmer findFarmerNumber(String farmerNumber) {
		List<Farmer> farmer = findByNameStrict("farmerNumber", farmerNumber);
		if (farmer != null && !farmer.isEmpty()) {
			return farmer.get(0);
		}
		return null;
	}
	
	
	@Override
	public Farmer getFarmerByFarmerNumber(Farmer farmer) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Farmer.class);
		detachedCriteria.add(Restrictions.eq("farmerNumber", farmer.getFarmerNumber()));
		List<Farmer> list = (List<Farmer>)getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list.size()==0) {
			return null;
		}
		for (Farmer f : list) {
			return f;
		}
		return null;
	}
}
