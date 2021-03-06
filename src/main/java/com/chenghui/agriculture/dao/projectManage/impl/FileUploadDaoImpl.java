package com.chenghui.agriculture.dao.projectManage.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.FileUploadDao;
import com.chenghui.agriculture.dao.projectManage.PictureUploadDao;
import com.chenghui.agriculture.model.FileUpload;
import com.chenghui.agriculture.model.PictureUpload;



/**
 * 流程类型访问接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class FileUploadDaoImpl extends GenericHibernateDao<FileUpload, Long> implements FileUploadDao {

	@Override
	public void executeLoad(String loadSql) {
		
		getSessionFactory().getCurrentSession().createSQLQuery(loadSql).executeUpdate();
		
	}
	
}
