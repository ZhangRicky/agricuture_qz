package com.chenghui.agriculture.dao.projectManage.impl;

import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.ProjectImportDao;
import com.chenghui.agriculture.model.ProjectImport;

/**
 * 流程类型访问接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class ProjectImportDaoImpl extends GenericHibernateDao<ProjectImport, Long> implements ProjectImportDao {

	@Override
	public void executeLoad(String loadSql) {
		getSessionFactory().getCurrentSession().createSQLQuery(loadSql).executeUpdate();
	}
	
}
