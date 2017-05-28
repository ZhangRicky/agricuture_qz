package com.chenghui.agriculture.service.supervisonManage.impl;
import java.util.Date;
import java.util.List;

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
import com.chenghui.agriculture.dao.projectManage.FarmerDao;
import com.chenghui.agriculture.dao.projectManage.ProjectsDao;
import com.chenghui.agriculture.dao.projectManage.SubProjectDao;
import com.chenghui.agriculture.dao.supervisionManage.ChecksDao;
import com.chenghui.agriculture.dao.supervisionManage.SupervisionDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.Checks;
import com.chenghui.agriculture.model.Farmer;
import com.chenghui.agriculture.model.Point;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.Supervision;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.supervisonManage.SupervisionService;

/**
 * 监管记录业务接口实现
 * @author LLJ
 * @version V1.0
 */
@Service("SupervisionService")
public class SupervisionServiceImpl extends GenericServiceImpl<Supervision, Long> implements SupervisionService {

	@Autowired
	SupervisionDao supervisionDao;
	
	@Autowired
	SubProjectDao subProjectDao;
	
	@Autowired
	ChecksDao checksDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	ProjectsDao projectsDao;
	
	@Autowired
	FarmerDao farmerDao;
	
	public void setSupervisionDao(SupervisionDao supervisionDao) {
		this.supervisionDao = supervisionDao;
	}
	
	
	@Override
	public PaginationSupport<Supervision> findSupervisionListForPage(Supervision supervision, int limit, int start, ShiroUser shiroUser) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Supervision.class);
		if (supervision.getSubProjectId()>0) {
			Projects projects = new Projects();
			projects.setId(supervision.getSubProjectId());
			detachedCriteria.add(Restrictions.eq("projects", projects));
		}
//		if (!shiroUser.isAdmin) {
//			detachedCriteria.add(Restrictions.eq("user", userDao.get(shiroUser.getId())));
//		}
		detachedCriteria.add(Restrictions.isNull("checks"));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		detachedCriteria.addOrder(Order.desc("id"));
		PaginationSupport<Supervision> supervisionsPage = supervisionDao.findPageByCriteria(detachedCriteria, limit, start);
		return supervisionsPage;
	}
		
	/**
	 * 添加监管记录
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECT_SUPERVISION_CN, opType=OperationType.AppAdd, opText="增加项目监管信息")
	public Long addSupervision(Supervision supervision){
		Point point = new Point(supervision.getLng(), supervision.getLat());
		supervision.setPoint(point);
		supervision.setCreateDate(new Date());
		long checksId = supervision.getChecksId();
		
		if (supervision.getFarmerId() >0) {
			Farmer farmer = farmerDao.get(supervision.getFarmerId());
			if (farmer != null) {
				supervision.setFarmer(farmer);
			}
		}
		
		if (checksId != 0) {//项目验收
			Checks checks = checksDao.get(checksId);
			supervision.setChecks(checks);
			//System.out.println");
		}else{//项目监管
			Projects projects = projectsDao.get(supervision.getSubProjectId());
			projects.setProjectProcess(supervision.getProjectProcess());
			supervision.setProjects(projects);
		}
		
		supervisionDao.add(supervision);
		Long id = supervision.getId();
		return id;
	}
	
	/**
	 * 删除监管记录
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECT_SUPERVISION_CN, opType=OperationType.Del, opText="删除项目监管信息")
	public void removeSupervisionByID(Long id) {
		Supervision supervision = supervisionDao.get(id);
		supervisionDao.remove(supervision);
	}
	
	/**
	 * 修改监管记录
	 * @param supervision
	 * @return
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECT_SUPERVISION_CN, opType=OperationType.Update, opText="修改项目监管信息")
	public Supervision updateSupervision(Supervision supervision) {
		Supervision newSupervision = supervisionDao.get(supervision.getId());
//		newSupervision.setProject(supervision.getProject());
//		newSupervision.setProjectScale(supervision.getProjectScale());
//		newSupervision.setProjectContent(supervision.getProjectContent());
//		newSupervision.setApprovalFiles(supervision.getApprovalFiles());
//		newSupervision.setConstructionMode(supervision.getConstructionMode());
//		newSupervision.setShouldAccount(supervision.getShouldAccount());
//		newSupervision.setJz(supervision.getJz());
//		newSupervision.setJy(supervision.getJy());
//		newSupervision.setReimbursementRate(supervision.getReimbursementRate());
//		newSupervision.setCheckStatus(supervision.getCheckStatus());
		try {
			supervisionDao.update(newSupervision);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return newSupervision;
	}

	/**
	 * 根据id查询监管记录
	 */
	@Override
	public Supervision findSupervisionById(Long id) {
		Supervision supervision = supervisionDao.get(id);
		return supervision;
	}
	
	@Override
	public List<Supervision> findSupervisionByPId(Long pId){
		return supervisionDao.getSupervisionsBySId(pId);
	}
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.APP_CN, opType=OperationType.AppQuery, opText="监管记录查询")
	public List<Supervision> appFindSupervisionByPId(Long pId){
		return supervisionDao.getSupervisionsBySId(pId);
	}
}
