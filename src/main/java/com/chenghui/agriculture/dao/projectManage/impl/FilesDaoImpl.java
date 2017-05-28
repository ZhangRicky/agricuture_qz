package com.chenghui.agriculture.dao.projectManage.impl;

import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.FilesDao;
import com.chenghui.agriculture.model.Files;

/**
 * 子项目DAO实现
 * @author LLJ
 * @version V1.0
 */
@SuppressWarnings("unchecked")
@Repository
public class FilesDaoImpl extends GenericHibernateDao<Files, Long> implements FilesDao {

	@Override
	public void executeLoad(String loadSql) {
		
		getSessionFactory().getCurrentSession().createSQLQuery(loadSql).executeUpdate();
		
	}

}
