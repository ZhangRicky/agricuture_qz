package com.chenghui.agriculture.dao.projectManage.impl;



import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.FinanceDao;
import com.chenghui.agriculture.model.Finance;


/**
 * 财务信息dao
 * @author Ricky
 * @version 1.0
 */
@SuppressWarnings("unchecked")
@Repository
public class FinanceDaoImpl extends GenericHibernateDao<Finance, Long> implements FinanceDao  {
	

	/*分页查询财务信息*/
	@Override
	public PaginationSupport<Finance> findSubProjectPage(Finance f, int limit, int start, ShiroUser shiroUser) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Finance.class);
		detachedCriteria.add(Restrictions.eq("projects_id", f.getProjects_id()));
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		PaginationSupport<Finance> result = findPageByCriteria(detachedCriteria, limit, start);
		return result;
	}
	
	/**
	 * 根据凭证号查询财务信息
	 */
	@Override
	public Finance findByCertificateNum(String str) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Finance.class);
		detachedCriteria.add(Restrictions.eq("certificateNum",str));
		List<Finance> list = (List<Finance>)getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list.size()==0) {
			return null;
		}
		for (Finance f : list) {
			return f;
		}
		return null;
		
	}

	@Override
	public List<Finance> findBySubProjectId(Long tid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Finance.class);
		detachedCriteria.add(Restrictions.eq("projects_id",tid));
		List<Finance> list = (List<Finance>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.APP_CN, opType=OperationType.AppQuery, opText="财务信息查询")
	public List<Finance> appFindBySubProjectId(Long tid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Finance.class);
		detachedCriteria.add(Restrictions.eq("projects_id",tid));
		List<Finance> list = (List<Finance>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}

}
