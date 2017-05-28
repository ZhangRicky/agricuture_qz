package com.chenghui.agriculture.dao.projectManage;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Project;

/**
 * 访问接口
 * @author yudq
 * @version V1.0
 */
public interface ProjectDao extends GenericDao<Project, Long> {

	List<Long> add(List<Project> models) throws DataAccessException;
	
	Project findByProjectNumber(Long projectNumber);
	
}
