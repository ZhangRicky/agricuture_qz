package com.chenghui.agriculture.dao.projectManage.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.ProjectDao;
import com.chenghui.agriculture.model.Project;

/**
 * 项目访问接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class ProjectDaoImpl extends GenericHibernateDao<Project, Long> implements ProjectDao {
	
	@Override
	public List<Long> add(List<Project> models) throws DataAccessException {
		List<Long> ids = new ArrayList<Long>();
		for (Project project : models) {
			ids.add(add(project));
		}
		return ids;
	}

	@Override
	public Project findByProjectNumber(Long projectNumber) {
		List<Project> projects = findByLongColumn("projectNumber", projectNumber);
		if (projects != null && !projects.isEmpty()) {
			return projects.get(0);
		}
		return null;
	}
	
}
