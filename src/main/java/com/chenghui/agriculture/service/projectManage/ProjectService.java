package com.chenghui.agriculture.service.projectManage;

import java.util.List;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.Project;
import com.chenghui.agriculture.service.GenericService;

/**
 * 项目Service
 * @author yudq
 * @version V1.0
 * @Date 2016-03-04 14:51:00
 */
public interface ProjectService extends GenericService<Project,Long>{

	PaginationSupport<Project> findProjectListForPage(Project project, int limit,int start, ShiroUser shiroUser);
	Project getProgectId(Long id);
	Long add(Project project, ShiroUser shiroUser) throws BusinessServiceException;
	
	Project updateRemark(Project project);

	/**
	 * 查询所有主项目，供添加子项目时选择
	 * @return
	 */
	List<Project> findAllProject();
	/**
	 * 根据参数查询项目
	 * @param project
	 * @return
	 */
	PaginationSupport<Project> findByParams(Project project);

}
