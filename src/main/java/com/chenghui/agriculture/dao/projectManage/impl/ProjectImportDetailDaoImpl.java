package com.chenghui.agriculture.dao.projectManage.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.ProjectImportDetailDao;
import com.chenghui.agriculture.model.ProjectImportDetail;

/**
 * 流程类型访问接口
 * @author yudq
 * @date 2016-03-02 15:50:00
 */
@SuppressWarnings("unchecked")
@Repository
public class ProjectImportDetailDaoImpl extends GenericHibernateDao<ProjectImportDetail, Long> implements ProjectImportDetailDao {
	
	@Override
	public ProjectImportDetail findProjectNumber(Long projectDetailId) {
		List<ProjectImportDetail> projectImportDetail = findByLongColumn("id", projectDetailId);
		if (projectImportDetail != null && !projectImportDetail.isEmpty()) {
			return projectImportDetail.get(0);
		}
		return null;
	}
	
}
