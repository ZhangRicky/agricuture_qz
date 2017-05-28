package com.chenghui.agriculture.dao.projectManage.impl;

import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.FarmerImportDao;
import com.chenghui.agriculture.model.FarmerImport;


@SuppressWarnings("unchecked")
@Repository
public class FarmerImportDaoImpl extends GenericHibernateDao<FarmerImport, Long> implements FarmerImportDao {

	@Override
	public void executeLoad(String loadSql) {
		getSessionFactory().getCurrentSession().createSQLQuery(loadSql).executeUpdate();
	}
	
}
