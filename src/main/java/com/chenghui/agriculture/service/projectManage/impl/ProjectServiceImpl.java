package com.chenghui.agriculture.service.projectManage.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.projectManage.ProjectDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.Project;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.projectManage.ProjectService;

/**
 * @author yudq
 * @version V1.0
 * @Date 2016-03-02 16:12:00
 */
@Service("projectService")
public class ProjectServiceImpl extends GenericServiceImpl<Project, Long> implements ProjectService {
	
	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private UserDao userDao;

	@Override
	public PaginationSupport<Project> findProjectListForPage(Project project, int limit, int start, ShiroUser shiroUser) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Project.class);
		if (StringUtils.isNotEmpty(project.getProjectName())) {
			detachedCriteria.add(Restrictions.like("projectName", project.getProjectName(), MatchMode.ANYWHERE));
		}
		if (!shiroUser.isAdmin) {
			detachedCriteria.add(Restrictions.eq("operator", userDao.get(shiroUser.getId())));
		}
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		PaginationSupport<Project> projectPage = projectDao.findPageByCriteria(detachedCriteria, limit, start);
		return projectPage;
	}

	/**
	 * 查询所有主项目，只取主项目名称
	 */
	@Override
	public List<Project> findAllProject() {
		String hql="from Project order by id ASC";
		return projectDao.findByHQL(hql,new Object[]{});
	}
	
	@Override
	public Project getProgectId(Long id) {
		return projectDao.get(id);
	}
	
	@Override
	public Long add(Project project, ShiroUser shiroUser) throws BusinessServiceException {
		projectDao.add(project);
		return project.getId();
	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ATTACHMENT_UPLOAD_CN, opType=OperationType.Add, opText="添加公示附件说明")
	public Project updateRemark(Project project) {
		Project project1 = projectDao.get(project.getId());		
		try {
			project1.setRemark(project.getRemark());
			projectDao.update(project1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return project1;
		
	}
	@Override
	public PaginationSupport<Project> findByParams(Project project){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Project.class);
		if (StringUtils.isNotEmpty(project.getProjectName())) {
			detachedCriteria.add(Restrictions.like("projectName", project.getProjectName()));
		}
		if (StringUtils.isNotEmpty(project.getFundYear())) {
			detachedCriteria.add(Restrictions.eq("fundYear", project.getFundYear()));
		}
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		return (PaginationSupport<Project>) projectDao.findPageByCriteria(detachedCriteria, project.getPageSize(), project.getStartIndex());
	}
	
}
