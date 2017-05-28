package com.chenghui.agriculture.service.supervisonManage.impl;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.projectManage.ProjectsDao;
import com.chenghui.agriculture.dao.supervisionManage.ChecksDao;
import com.chenghui.agriculture.dao.supervisionManage.PointDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.Checks;
import com.chenghui.agriculture.model.Point;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.SubProject;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.supervisonManage.ChecksService;

/**
 * 子项目验收业务接口实现
 * @author LLJ
 * @version V1.0
 */
@Service("ChecksService")
public class ChecksServiceImpl extends GenericServiceImpl<Checks, Long> implements ChecksService {

	@Autowired
	ChecksDao checksDao;
	
	@Autowired
	PointDao pointDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	ProjectsDao projectsDao;

	@Override
	public PaginationSupport<Checks> findChecksListForPage(Checks checks, int limit, int start, ShiroUser shiroUser) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Checks.class);
		if (checks.getSubProjectId()>0) {
			Projects projects = new Projects();
			projects.setId(checks.getSubProjectId());
			detachedCriteria.add(Restrictions.eq("projects", projects));
		}
//		if (!shiroUser.isAdmin) {
//			detachedCriteria.add(Restrictions.eq("user", userDao.get(shiroUser.getId())));
//		}
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		PaginationSupport<Checks> checksPage = checksDao.findPageByCriteria(detachedCriteria, limit, start);
		for (Checks checks1 : checksPage.getItems()) {
//			Hibernate.initialize(checks1.getSupervisions());
			Hibernate.initialize(checks1.getPointBaidu());
			if (checks1.getPointBaidu().isEmpty()) {
				Hibernate.initialize(checks1.getPoints());
			}
		}
		return checksPage;
	}
		
	/**
	 * 添加项目验收
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECT_CHECKS_CN, opType=OperationType.AppAdd, opText="增加项目验收")
	public Long addChecks(Checks checks){
		Projects projects = projectsDao.get(checks.getSubProjectId());
		if (checks.getChecksId()>0) {
			Checks oldChecks = checksDao.get(checks.getChecksId());
			oldChecks.setDescription(checks.getDescription());
			oldChecks.setIsSuccess(checks.getIsSuccess());
			oldChecks.setPictures(checks.getPictures());
			try {
				oldChecks.getProjects().setCheckStatus(checks.getIsSuccess());
			} catch (Exception e) {
				e.printStackTrace();
			}
			checksDao.update(oldChecks);
			return oldChecks.getId();
		}
		checks.setProjects(projects);
		checks.setCreateDate(new Date());
		checks.setDeleted(false);
		checks.setIsSuccess(2);
		Long id = checksDao.add(checks); //记录
		Point point = new Point(checks.getLng(), checks.getLat());
		point.setCheck(checks);
		pointDao.add(point);
		return id;
	}
	
	/**
	 * 删除项目验收
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECT_CHECKS_CN, opType=OperationType.Del, opText="删除项目验收信息")
	public void removeChecksByID(Long id) {
		Checks checks = checksDao.get(id);
		checksDao.remove(checks);
	}
	
	/**
	 * 修改项目验收
	 * @param checks
	 * @return
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECT_CHECKS_CN, opType=OperationType.Update, opText="修改项目验收信息")
	public Checks updateChecks(Checks checks) {
		Checks newChecks = checksDao.get(checks.getId());
//		newChecks.setProject(checks.getProject());
//		newChecks.setProjectScale(checks.getProjectScale());
//		newChecks.setProjectContent(checks.getProjectContent());
//		newChecks.setApprovalFiles(checks.getApprovalFiles());
//		newChecks.setConstructionMode(checks.getConstructionMode());
//		newChecks.setShouldAccount(checks.getShouldAccount());
//		newChecks.setJz(checks.getJz());
//		newChecks.setJy(checks.getJy());
//		newChecks.setReimbursementRate(checks.getReimbursementRate());
//		newChecks.setCheckStatus(checks.getCheckStatus());
		try {
			checksDao.update(newChecks);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return newChecks;
	}

	/**
	 * 根据id查询项目验收
	 */
	@Override
	public Checks findChecksById(Long id) {
		Checks checks = checksDao.get(id);
		return checks;
	}
	
	@Override
	public List<Checks> findChecksBySId(Long sId){
		return checksDao.getCheckssBySId(sId);
	}
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECT_CHECKS_CN, opType=OperationType.AppQuery, opText="项目验收查询")
	public List<Checks> appFindChecksBySId(Long sId){
		return checksDao.getCheckssBySId(sId);
	}
}
