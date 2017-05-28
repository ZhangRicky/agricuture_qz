package com.chenghui.agriculture.service.projectManage.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.ProjectsUtil;
import com.chenghui.agriculture.dao.projectManage.ProjectImportDetailDao;
import com.chenghui.agriculture.dao.projectManage.ProjectsDao;
import com.chenghui.agriculture.dao.supervisionManage.ChecksDao;
import com.chenghui.agriculture.dao.supervisionManage.PointBaiduDao;
import com.chenghui.agriculture.dao.system.AreaDao;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Checks;
import com.chenghui.agriculture.model.PointBaidu;
import com.chenghui.agriculture.model.ProjectImportDetail;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.vo.ProjectsSumTotalFund;
import com.chenghui.agriculture.model.vo.ReportProjects;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.projectManage.ProjectsService;

/**
 * 项目业务接口实现
 * @author LLJ
 * @version V1.0
 */
@Service("projectsService")
public class ProjectsServiceImpl extends GenericServiceImpl<Projects, Long> implements ProjectsService{

	@Autowired
	private ProjectsDao projectsDao;
	
	@Autowired
	private ProjectImportDetailDao projectImportDetailDao;

	@Autowired
	private AreaDao areaDao;
	
	@Autowired
	private AreaDao araDao;
	
	@Autowired
	private PointBaiduDao pointBaiduDao;
	
	@Autowired
	private ChecksDao checksDao;
	
	@Override
	public List<Projects> getParentProjects(String xxnr) {
		return projectsDao.findParentProjects(xxnr);
	}
	
	@Override
	public List<Projects> getByProjects(Projects projects, ShiroUser shiroUser){
		boolean recursionFlag = false;
		//设置地区对象
		if (projects.getAreaId() != null && projects.getAreaId()>0) {
			projects.setArea(areaDao.get(projects.getAreaId()));
			if (projects.getArea().getAreaLevel()!=3){
				recursionFlag = true;
			}
		}
		if(StringUtils.isNotEmpty(projects.getProjectName()) || StringUtils.isNotEmpty(projects.getFundYear()) || (projects.getProjectType() != null && projects.getProjectType() > 0)) {
			recursionFlag = true;
		}
		//
		List<Projects> list = projectsDao.getByProjects(projects, shiroUser);
		if (recursionFlag) {
			Set<Projects> results = new HashSet<>();
			Set<Long> idSet = new HashSet<>();
			for(Projects projects2: list){
				idSet.add(projects2.getId());
			}
			
			Iterator<Projects> it = list.iterator();
			Set<Long> parentIdSet = new HashSet<>();
	        while (it.hasNext()) {
	        	Projects p = (Projects) it.next();
	        	if(p.getParentId() !=null && p.getParentId()>0 && !idSet.contains(p.getParentId())){
	        		parentIdSet.add(p.getParentId());
	        	}
	            results.addAll(getChildList(p));
	        }
	        for(Long pId: parentIdSet){
	        	results.addAll(getParentList(pId));
	        }
	        for(Projects projects2 : results){
				if (!list.contains(projects2)) {
					list.add(projects2);
				}
			}
		}
		
		return setTreeLevel(list);
	}
	
	public Set<Projects> getParentList(Long pId) {
		Set<Projects> projectsList = new HashSet<Projects>();
		Projects parentProjects = projectsDao.get(pId);
		if(parentProjects == null){
			return projectsList;
		}
		projectsList.add(parentProjects);
		if(parentProjects.getParentId() !=null && parentProjects.getParentId()>0){
			projectsList.addAll(getParentList(parentProjects.getParentId()));
		}
		return projectsList;
	}
	
    public List<Projects> getChildList(Projects projects) {
    	List<Projects> results = new ArrayList<>();
		List<Projects> list = projectsDao.getChild(projects);
		if(list.isEmpty()){
			return list;
		}
		results.addAll(list);
        for(Projects p: list) {
        	results.addAll(getChildList(p));
        }
        return setTreeLevel(results);
    }

	/**
	 * 查询所有
	 * @author LLJ
	 */
	@Override
	public List<Projects> getAllProjects(Long areaId) {
			List<Projects> list = new ArrayList<Projects>();
			List<Projects> newList = new ArrayList<Projects>();
			Area a = araDao.get(areaId);
			if (a.getAreaLevel()==3) {
				List<Projects> list1 = projectsDao.findCountyLevelCityId(areaId);
				newList=list1;
			}else if(a.getAreaLevel()==2){
				List<Projects> list2 = projectsDao.findTownId(areaId);
				newList=list2;
			}else{
				List<Projects> list3 = projectsDao.findImportProjects(areaId);
				newList=list3;
			}
			list.addAll(newList);
			return list;
	}
	
	/**
	 * 项目调项
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.SUBPROJECT_MANAGE_CN, opType=OperationType.Add, opText="添加子项目")
	public Long addAdjustment(Projects projects) {
		Projects pj = projectsDao.get(projects.getPid());
			ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
			Area a = areaDao.get(pj.getArea().getId());
			projects.setArea(a);
			projects.setProjectsStatus(0);
			projects.setFlag(0);
			projects.setParentId(pj.getParentId());
	//		projects.setProjectNumber(Integer.valueOf(ProjectsUtil.getProjectNumberNo(pj.getId())));
			projects.setProjectNumber(ProjectsUtil.getProjectNumberNo(pj.getId()));
			projects.setLevel(pj.getLevel());
			projects.setApproveState(pj.getApproveState());
			projects.setProjectType(pj.getProjectType());
			projects.setConstructionMode(pj.getConstructionMode());
			projects.setIsLevel(pj.getIsLevel());
			projects.setAdjustmentStatus(1);
			projects.setCheckStatus(pj.getCheckStatus());
			projects.setTownID(pj.getTownID());
			projects.setCountyLevelCityID(pj.getCountyLevelCityID());
			projects.setVillageID(a);
			projects.setApprovalNumber(pj.getApprovalNumber());
			projects.setApprovingDepartment(pj.getApprovingDepartment());
			projects.setChargePerson(pj.getChargePerson());
			projects.setCreateUser(shiroUser.loginName);
			projects.setCreateTime(String.valueOf(new Date()));
			projects.setFarmerName(pj.getFarmerName());
			projects.setFinanceFund(pj.getFinanceFund());
			projects.setFinancebiLv(pj.getFinancebiLv());
			projects.setFundToCountry(pj.getFundToCountry());
			projects.setFundType(pj.getFundType());
			projects.setFundYear(pj.getFundYear());
			projects.setInputStatus(pj.getInputStatus());
			projects.setIntegrateFund(pj.getIntegrateFund());
			projects.setRemark(pj.getRemark());
			projects.setSelfFinancing(pj.getSelfFinancing());
			projects.setStandbyNumber(pj.getStandbyNumber());
			projects.setCity(pj.getCity());
			projects.setTown(pj.getTown());
			projects.setVillage(pj.getVillage());
			Long ps =  projectsDao.add(projects);
			if (ps!=null) {
				pj.setAdjustmentReason(projects.getAdjustmentReason());
				pj.setCreateUser(shiroUser.loginName);
				pj.setCreateTime(String.valueOf(new Date()));
				pj.setAdjustmentStatus(2);
				projectsDao.update(pj);
			}
		return ps;
	}
	
	/**
	 * 添加子项目
	 * @author LLJ
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.SUBPROJECT_MANAGE_CN, opType=OperationType.Add, opText="添加子项目")
	public Long addProjects(Projects projects) {
		Projects p = projectsDao.get(projects.getParentId());
		p.setIsLevel(0);
		projectsDao.update(p);
		projects.setIsLevel(1);
		Area area = areaDao.get(p.getArea().getId());
		if (area.getAreaLevel()==1) {
			projects.setLevel(1);
		}else if (area.getAreaLevel()==2) {
			projects.setLevel(2);
		}else{
			projects.setLevel(3);
			projects.setTreeLevel(String.valueOf(projects.getParentId()));
		}
		projects.setParentId(projects.getParentId());
		projects.setFlag(0);
		projects.setCheckStatus(0);
		projects.setAdjustmentStatus(1);
		Area a = areaDao.get(projects.getArea().getId());
		projects.setArea(a);
	//	projects.setProjectNumber(Integer.valueOf(ProjectsUtil.getProjectNumberNo(p.getId())));
		projects.setProjectNumber(ProjectsUtil.getProjectNumberNo(p.getId()));
		if (p.getTownID()==null) {
			projects.setCountyLevelCityID(p.getCountyLevelCityID());
			projects.setTownID(a);
		}else{
			projects.setTownID(p.getTownID());
			projects.setCountyLevelCityID(p.getCountyLevelCityID());
			projects.setVillageID(a);
		}
		return projectsDao.add(projects);
	}
	
	/**
	 * 删除子项目
	 * @author LLJ
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.SUBPROJECT_MANAGE_CN, opType=OperationType.Del, opText="删除子项目")
	public void removeByID(Long id) {
		Projects projects = projectsDao.get(id);
		if (projects.getParentId()!=null) {
			Projects p = projectsDao.get(projects.getParentId());
			List<Projects> list = projectsDao.getChild(p);
			if (list.size()==1) {
				p.setIsLevel(1);
				projectsDao.update(p);
			}
		}
		projects.setFlag(1);
		projectsDao.update(projects);
	}
	
	/**
	 * 修改子项目
	 * @author LLJ
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.SUBPROJECT_MANAGE_CN, opType=OperationType.Update, opText="更新子项目")
	public Projects updateProjectsInfo(Projects projects) {
		Projects newProjects = projectsDao.get(projects.getId());
		Area area = areaDao.get(newProjects.getArea().getId());
		newProjects.setArea(area);

		newProjects.setProjectName(projects.getProjectName());
		newProjects.setReferenceNumber(projects.getReferenceNumber());
		newProjects.setCarryOutUnit(projects.getCarryOutUnit());
		newProjects.setTotalFund(projects.getTotalFund());
		newProjects.setFarmerName(projects.getFarmerName());
		newProjects.setScaleAndContent(projects.getScaleAndContent());
		newProjects.setSubjectName(projects.getSubjectName());
		newProjects.setConstructionMode(projects.getConstructionMode());
		newProjects.setProjectType(projects.getProjectType());
		newProjects.setCoveredFarmerNumber(projects.getCoveredFarmerNumber());
		newProjects.setCoveringNumber(projects.getCoveringNumber());
		newProjects.setPovertyStrickenFarmerNumber(projects.getPovertyStrickenFarmerNumber());
		newProjects.setPovertyStrickenPeopleNumber(projects.getPovertyStrickenPeopleNumber());
		newProjects.setPovertyGeneralFarmer(projects.getPovertyGeneralFarmer());
		newProjects.setPovertyGeneralPeople(projects.getPovertyGeneralPeople());
		newProjects.setPovertyLowIncomeFarmer(projects.getPovertyLowIncomeFarmer());
		newProjects.setPovertyLowIncomePeople(projects.getPovertyLowIncomePeople());
		newProjects.setRemark(projects.getRemark());
		newProjects.setFundYear(projects.getFundYear());
		newProjects.setDeadline(projects.getDeadline());
		
//		newProjects.setChargePerson(projects.getChargePerson());
//		newProjects.setFundType(projects.getFundType());
//		newProjects.setApprovalNumber(projects.getApprovalNumber());	
//		newProjects.setStandbyNumber(projects.getStandbyNumber());	
//		newProjects.setFinanceFund(projects.getFinanceFund());
//		newProjects.setSelfFinancing(projects.getSelfFinancing());
//		newProjects.setIntegrateFund(projects.getIntegrateFund());
//		newProjects.setFundToCountry(projects.getFundToCountry());
//		newProjects.setApproveState(projects.getApproveState());
//		newProjects.setCheckStatus(projects.getCheckStatus());
//		newProjects.setInputStatus(projects.getInputStatus());
//		newProjects.setApprovingDepartment(projects.getApprovingDepartment());
//		newProjects.setFinancebiLv(projects.getFinancebiLv());
//		newProjects.setPath(projects.getPath());

		try {
			projectsDao.update(newProjects);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return newProjects;
	}


	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECTS_MANAGE_CN, opType=OperationType.Export, opText="项目数据导出")
	public List<Projects> exportExcelList(Projects p,ShiroUser shiroUser) {
		return projectsDao.exportProjects(shiroUser);		
	}
	

	/**
	 * 添加主项目公示说明
	 * @author byq
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECTS_MANAGE_CN, opType=OperationType.Add, opText="添加公示附件说明")
	public Projects updateRemark(Projects projects) {
		Projects projects1 = projectsDao.get(projects.getId());		
		try {
			projects1.setShuoming(projects.getShuoming());;
			projectsDao.update(projects1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return projects1;
		
	}

	/**
	 * 查看详情获取市、镇、村
	 * @author byq
	 */
	@Override
	public List<Area> findAllArea(Long areaId) {
		return araDao.findAllArea(areaId);
	}
	
	/**
	 * 根据子项目査找子项目信息
	 */
	@Override
	public List<Projects> findProjectParent(Long parentId) {
		return projectsDao.findProjectParent(parentId);
	}
	

	
	/**
	 * 根据项目唯一编号查找项目对项目状态进行修改，覆盖以前的项目	
	 * @author byq
	 */
	@Override
	public Projects updateProject(String projectNumber,Long projectDetailId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("projectNumber", projectNumber));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Projects> list=  (List<Projects>)projectsDao.findByCriteria(detachedCriteria);
		ProjectImportDetail projectImportDetail = projectImportDetailDao.findProjectNumber(projectDetailId);
		String referenceNumber = "";
		String fundYear = "";
		String city = "";
		String countyLevelCity = "";
		String town = "";
		String village = "";
		String subjectName = "";
		String fundType = "";
		String projectName = "";
		String approvalNumber = "";
		Double totalFund = 0.0;
		Double financeFund = 0.0;
		Double selfFinancing = 0.0;
		Double integrateFund = 0.0;
	//	Double integrateFund = Double.parseDouble("");
	    Integer coveredFarmerNumber = 0;
	    Integer coveringNumber = 0;
	    Integer povertyStrickenFarmerNumber = 0;
	   // Integer povertyStrickenFarmerNumber = Integer.parseInt("");
	    Integer povertyStrickenPeopleNumber = 0;
	    Integer povertyGeneralFarmer = 0;			 //扶持一般农户
//	    Integer povertyPoorFarmer = 0;               //扶持贫困农户
	    Integer povertyLowIncomeFarmer = 0;          //扶持低收入困难农户
		Integer povertyGeneralPeople = 0;			 //扶持一般人口
//		Integer povertyPoorPeople = 0;               //扶持贫困人口
		Integer povertyLowIncomePeople = 0;          //扶持低收入困难人口
		String scaleAndContent = "";
		String carryOutUnit = "";
		String chargePerson = "";
		Integer deadline = 0;
		String standbyNumber = "";
		Double fundToCountry = 0.0;
		String createUser = "";
		String createTime = "";
		String inputStatus = "";
		String approvingDepartment = "";
		Area townID = null;
		Area villageID = null;
		for(Projects projects1 : list){
			
			if(projects1.getProjectsStatus() == 1){
				 referenceNumber = projects1.getReferenceNumber();
			     fundYear = projects1.getFundYear();
				 city = projects1.getCity();
				 countyLevelCity = projects1.getCountyLevelCity();
				 town = projects1.getTown();
				 village = projects1.getVillage();
				 subjectName = projects1.getSubjectName();
				 fundType = projects1.getFundType();
				 projectName = projects1.getProjectName();
				 approvalNumber = projects1.getApprovalNumber();
				 totalFund = projects1.getTotalFund();
				 financeFund = projects1.getFinanceFund();
				 selfFinancing = projects1.getSelfFinancing();
				 integrateFund = projects1.getIntegrateFund();
			     coveredFarmerNumber = projects1.getCoveredFarmerNumber();
			     coveringNumber = projects1.getCoveringNumber();
			     povertyStrickenFarmerNumber = projects1.getPovertyStrickenFarmerNumber();
			     povertyStrickenPeopleNumber = projects1.getPovertyStrickenPeopleNumber();
				 scaleAndContent = projects1.getScaleAndContent();
				 carryOutUnit = projects1.getCarryOutUnit();
				 chargePerson = projects1.getChargePerson();
				 deadline = projects1.getDeadline();
				 standbyNumber = projects1.getStandbyNumber();
				 fundToCountry = projects1.getFundToCountry();
				 createUser = projects1.getCreateUser();
				 createTime = projects1.getCreateTime();
				 inputStatus = projects1.getInputStatus();
				 approvingDepartment = projects1.getApprovingDepartment();
				 townID = projects1.getTownID();
				 villageID = projects1.getVillageID();
				 povertyGeneralFarmer = projects1.getPovertyGeneralFarmer();			 //扶持一般农户
//				 povertyPoorFarmer  = projects1.getPovertyPoorFarmer();           //扶持贫困农户
				 povertyLowIncomeFarmer =  projects1.getPovertyLowIncomeFarmer();        //扶持低收入困难农户
				 povertyGeneralPeople = projects1.getPovertyGeneralPeople();			 //扶持一般人口
//				 povertyPoorPeople = projects1.getPovertyPoorPeople();              //扶持贫困人口
				 povertyLowIncomePeople = projects1.getPovertyLowIncomePeople();
			}
		}
		if(list.size() == 1){
			return null;
		}else{
			for(Projects projects : list){
				
				if(projects.getProjectsStatus() == 0){
					projects.setReferenceNumber(referenceNumber);
					projects.setFundYear(fundYear);
					projects.setCity(city);
					projects.setCountyLevelCity(countyLevelCity);
					projects.setTown(town);
					projects.setSubjectName(subjectName);
					projects.setVillage(village);
					projects.setFundType(fundType);
					projects.setProjectName(projectName);
					projects.setApprovalNumber(approvalNumber);
					projects.setTotalFund(totalFund);
					projects.setFinanceFund(financeFund);
					projects.setSelfFinancing(selfFinancing);
					projects.setIntegrateFund(integrateFund);
					projects.setCoveredFarmerNumber(coveredFarmerNumber);
					projects.setCoveringNumber(coveringNumber);
					projects.setPovertyStrickenFarmerNumber(povertyStrickenFarmerNumber);
					projects.setPovertyStrickenPeopleNumber(povertyStrickenPeopleNumber);
					projects.setScaleAndContent(scaleAndContent);
					projects.setCarryOutUnit(carryOutUnit);
					projects.setChargePerson(chargePerson);
					projects.setDeadline(deadline);
					projects.setStandbyNumber(standbyNumber);
					projects.setFundToCountry(fundToCountry);
					projects.setCreateUser(createUser);
					projects.setCreateTime(createTime);
					projects.setInputStatus(inputStatus);
					projects.setApprovingDepartment(approvingDepartment);
					projects.setTownID(townID);
					projects.setVillageID(villageID);
					projects.setPovertyGeneralFarmer(povertyGeneralFarmer);
					projects.setPovertyGeneralPeople(povertyGeneralPeople);
					projects.setPovertyLowIncomeFarmer(povertyLowIncomeFarmer);
					projects.setPovertyLowIncomePeople(povertyLowIncomePeople);
//					projects.setPovertyPoorFarmer(povertyPoorFarmer);
//					projects.setPovertyPoorPeople(povertyPoorPeople);
					projects.setProjectsStatus(0);
					projectsDao.update(projects);
					
					
				}else if(projects.getProjectsStatus() == 1) {
					projects.setProjectsStatus(1);
					projectsDao.remove(projects);
				}
			 
			}
		}
		projectImportDetail.setCoverStatus(1);
		projectImportDetail.setRepeatStatus(3);
		projectImportDetail.setValidateDescription("此项目已被覆盖");
		projectImportDetailDao.update(projectImportDetail);
		return null;
	}	

	/**
	 * 删除财务记录时，更新报账值
	 */
	@Override
	public void updateSubproject(Long pid, String bzAcount) {
		Projects sub = projectsDao.get(pid);
		if(bzAcount == null || bzAcount == ""){
			bzAcount ="0";
		}
		double sum =(Double.parseDouble(sub.getFinancebiLv()== null ? "0" : sub.getFinancebiLv()) - Double.parseDouble(bzAcount));
		if(sum > 0){
			sub.setFinancebiLv(String.valueOf(sum));	
		}else{
			sub.setFinancebiLv(null);
		}
		projectsDao.update(sub);
		
	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.APP_CN, opType=OperationType.AppQuery, opText="主项目查询")
	public PaginationSupport<Projects> appFindByParams(Projects projects, ShiroUser shiroUser){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		if (StringUtils.isNotEmpty(projects.getProjectName())) {
			detachedCriteria.add(Restrictions.or(Restrictions.like("projectName", projects.getProjectName(), MatchMode.ANYWHERE), Restrictions.eq("fundYear", projects.getProjectName())));
		}
		if (StringUtils.isNotEmpty(projects.getFundYear())) {
			detachedCriteria.add(Restrictions.eq("fundYear", projects.getFundYear()));
		}
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.add(Restrictions.isNull("parentId"));
		
		//根据地区过滤项目
		if (shiroUser != null && !shiroUser.isSuperAdmin) {
			Set<Area> countyLevelCitys = shiroUser.getCountyLevelCitys();
			Set<Area> towns = shiroUser.getTowns();
			Set<Area> villages = shiroUser.getVillages();
			if (!countyLevelCitys.isEmpty() && !towns.isEmpty() && !villages.isEmpty()) {
				detachedCriteria.add(Restrictions.or(Restrictions.in("countyLevelCityID", countyLevelCitys), Restrictions.in("townID", towns), Restrictions.in("villageID", villages)));
			}else if (countyLevelCitys.isEmpty() && towns.isEmpty()  && !villages.isEmpty()) {
				detachedCriteria.add(Restrictions.in("villageID", villages));
			}else if (countyLevelCitys.isEmpty() && !towns.isEmpty()  && !villages.isEmpty()){
				detachedCriteria.add(Restrictions.or(Restrictions.in("townID", towns), Restrictions.in("villageID", villages)));
			}
		}
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		PaginationSupport<Projects> paginationSupport = (PaginationSupport<Projects>) projectsDao.findPageByCriteria(detachedCriteria, projects.getLimit(), projects.getStartIndex());
		List<Projects> items = paginationSupport.getItems();
		for(Projects item : items){
			String areaDisplay = "";
    		if (item.getCountyLevelCityID() != null) {
    			areaDisplay = item.getCountyLevelCityID().getAreaName();
    		}
    		if (item.getTownID() != null) {
    			areaDisplay = item.getTownID().getAreaName();
    		}
    		if (item.getVillageID() != null) {
    			areaDisplay += ' ' + item.getVillageID().getAreaName();
    		}
    		item.setTown(areaDisplay);
		}
		return paginationSupport;
	}
	
//	@Override
//	public List<Projects> findSubProjectsByParentId(Long parentId){
//		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
//		detachedCriteria.add(Restrictions.eq("parentId", parentId));
//		detachedCriteria.add(Restrictions.eq("flag", 0));
//		detachedCriteria.add(Restrictions.eq("isLevel", 1));
//		detachedCriteria.addOrder(Order.desc("id"));
//		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
//		return (List<Projects>) projectsDao.findByCriteria(detachedCriteria);
//	}
	
	@Override
	public List<ProjectsSumTotalFund> findTotalFund(Long pid){
		List<ProjectsSumTotalFund> projects = null;
		try {
			projects = projectsDao.findAllTotalFund(pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projects;
	}
	
	/**
     * 根据传过来的parentId获得它的所有子节点
     * @param parentId
     * @author mars
     */
	@Override
    public List<Projects> findSubProjectsByParentId(Long parentId){
    	List<Projects> projectsList = new ArrayList<Projects>();
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("parentId", parentId));
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Projects> list = (List<Projects>) projectsDao.findByCriteria(detachedCriteria);
		if(list.isEmpty()){
			return projectsList;
		}
		projectsList.addAll(list);
        for(Projects p: list) {
        	projectsList.addAll(findSubProjectsByParentId(p.getId()));
        }
        return projectsList;
    }
    
	/**
     * 根据传过来的parentId获得它的叶子节点
     * @param parentId
     * @author mars
     */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.APP_CN, opType=OperationType.AppQuery, opText="子项目查询")
	public List<Projects> appFindLeafProjectsByParentId(Long parentId){
		List<Projects> leafProjects = new ArrayList<Projects>();
		List<Projects> subProjects = findSubProjectsByParentId(parentId);
		for(Projects projects: subProjects){
			if(projects.getIsLevel() == 1){
				leafProjects.add(projects);
			}
		}
		return leafProjects;
	}
	
	/*
	 * 窗口关闭时。更新项目列表中的报账。拨款的统计数据数据
	 * @see com.chenghui.agriculture.service.projectManage.ProjectsService#updateByAccount(java.lang.Long, double, double)
	 */
	@Override
	public void updateByAccount(Long subId,double financeAccount,double financeaApropriation) {
		Projects p =projectsDao.get(subId);
		p.setBzTotal(financeAccount);
		p.setBkTotal(financeaApropriation);
		projectsDao.update(p);	
	}

	@Override
	public List<Projects> getAllInit(String xxnr) {
		return projectsDao.findAllInit(xxnr);
	}
	
	@Override
	public List<Projects> getAllInit() {
		return projectsDao.findAllInit();
	}


	
	/**
	 * 报账[确认]按钮，添加‘报账率’，‘报账金额’到项目数据中
	 *  @param sum 报账的总资金
	 */
	@Override
	public void addFinancebiLv(Projects p) {
		Projects subproject = projectsDao.get(p.getId());
		subproject.setFinancebiLv(p.getFinancebiLv());
		subproject.setBzTotal(p.getBzTotal());
		projectsDao.update(subproject);	
	}
	
	/*
	 * 拨款[确认]按钮，添加‘拨款金额’到项目数据中
	 */
	@Override
	public void addByBKTotal(Projects p) {
		Projects subproject = projectsDao.get(p.getId());
		subproject.setBkTotal(p.getBkTotal());
		projectsDao.update(subproject);	
	}
	
	/**
	 *删除/修改财务记录时。更新项目的'【总拨款】/【总报账】【报账率】'
	 */
	@Override
	public void updateByTotalBKBZ(Projects p) {
		projectsDao.update(p);
		
	}
	
	/**
	 * 填写资金的结余
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECTS_MANAGE_CN, opType=OperationType.Add, opText="填写项目资金结余")
	public void updateByBalance(Projects pro) {
		projectsDao.update(pro);
	}



	/**
	 * 初始化项目
	 * @param xxnr
	 * @param adjustmentPid
	 * @return
	 * @author LLJ
	 */
	@Override
	public List<Projects> getAllAdjustmentRecord(Long adjustmentAreaId,String projectName) {
		return projectsDao.findAllAdjustmentRecord(adjustmentAreaId,projectName);
	}
	
	/**
	 * 根据乡镇查询所有的村
	 * @author zgx
	 * @param town 乡镇名称
	 */
	@Override
	public List<ReportProjects> findByTown(String town,Integer year) {
		return projectsDao.findByTown(town,year);		
	}
	
	/**
	 * 多个村镇实施的同一个项目
	 * @author zgx
	 * 
	 */
	@Override
	public List<Projects> findbyOneToMany(Long townId) {
		return projectsDao.findbyOneToMany(townId);
	}
	
	/**
	 * 资金的审核操作
	 * @author zgx
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECTS_MANAGE_CN, opType=OperationType.Add, opText="项目资金的审核")
	public void updateByapproverStatus(Long projectsID) {
		Projects pro = projectsDao.get(projectsID);
		pro.setApprover_status(1);			//设置审核状态
		pro.setFinancebiLv(String.valueOf(pro.getTotalFund()));	//设置报账率
		//清算结转资金
		
		projectsDao.update(pro);
		
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.POINT_BAIDU_CN, opType=OperationType.AppAdd, opText="单个添加鹰眼轨迹坐标")
	public Long addPointBaidu(PointBaidu model) {
		long checksId = model.getChecksId();
		if (checksId>0) {
			Checks checks = checksDao.get(checksId);
			model.setChecks(checks);
		}
		return pointBaiduDao.add(model);
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.POINT_BAIDU_CN, opType=OperationType.AppAdd, opText="批量添加鹰眼轨迹坐标")
	public void addPointBaiduList(List<PointBaidu> pointBaidus, Long checksId) {
		Checks checks = checksDao.get(checksId);
		for(PointBaidu model: pointBaidus){
			if (model.getLng() == 0 || model.getLat() ==0) {
				continue;
			}
			model.setChecks(checks);
			pointBaiduDao.add(model);
		}
	}
	
	@Override
	@Cacheable(value="allProjects")
	public List<Projects> findAllNormalProjects(){
		List<Projects> list = projectsDao.findAllNormalProjects();
		Map<Long, Projects> pMap = new HashMap<>();
		for(Projects projects: list){
			pMap.put(projects.getId(), projects);
		}
		for(Projects projects: list){
			Long pId = projects.getParentId();
			if (pId != null) {
				pMap.get(pId).getChildren().add(projects);
			}
		}
		return list;
	}
	
	/**
	 * 顺序输出树
	 * @param list
	 * @return
	 */
	private List<Projects> setTreeLevel(List<Projects> list){
		Map<Long, Projects> map = new HashMap<>();
		for(Projects projects: list){
			map.put(projects.getId(), projects);
		}
		for(Projects projects: list){
			if (projects.getParentId()!=null && projects.getParentId()>0  && map.get(projects.getParentId()) != null) {
				map.get(projects.getParentId()).getChildren().add(projects);
			}
		}
		int topLevel = 1;
		for(int i =0;i<list.size();i++){
			Projects projects = list.get(i);
			if(projects.getParentId() == null || projects.getParentId() == 0){
				projects.setTreeLevel(topLevel+++"");
				setChildrenLevel(map, projects);
			}
		}
		return list;
	}
	
	private void setChildrenLevel(Map<Long, Projects> map, Projects projects){
		List<Projects> children = map.get(projects.getId()).getChildren();
		for(int i =0;i<children.size();i++){
			Projects project = children.get(i);
			project.setTreeLevel(map.get(projects.getId()).getTreeLevel()+"."+(i+1) + "");
			if (!project.getChildren().isEmpty()) {
				setChildrenLevel(map, project);
			}
		}
	}

	@Override
	public void updateByTotal(Projects p) {
		projectsDao.update(p);
	}
	
	@Override
	public Projects getProjectsTotalFund(Long updateParentId) {
		
		Projects projects = null;
		try {
			String hql="from Projects  where id=?";
			List<Projects> list=projectsDao.findByHQL(hql, new Object[]{updateParentId});
			projects=(list==null||list.size()==0)?null:list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projects;
	}
}
