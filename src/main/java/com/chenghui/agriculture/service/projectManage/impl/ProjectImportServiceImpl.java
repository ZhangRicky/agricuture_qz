package com.chenghui.agriculture.service.projectManage.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.DateUtil;
import com.chenghui.agriculture.core.utils.MD5Util;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.ProjectExcelReader;
import com.chenghui.agriculture.dao.projectManage.ProjectImportDao;
import com.chenghui.agriculture.dao.projectManage.ProjectImportDetailDao;
import com.chenghui.agriculture.dao.projectManage.ProjectsDao;
import com.chenghui.agriculture.dao.projectManage.TemplateDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.ProjectImport;
import com.chenghui.agriculture.model.ProjectImportDetail;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.Template;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.projectManage.ProjectImportService;

/**
 * 征信导入Service
 * @author yudq
 * @version V1.0
 * @Date 2016-03-02 16:12:00
 */
@Service("projectImportService")
public class ProjectImportServiceImpl extends GenericServiceImpl<ProjectImport, Long> implements ProjectImportService {
	
	@Autowired
	private ProjectImportDao projectImportDao;
	
	@Autowired
	private ProjectImportDetailDao projectImportDetailDao;
	
	@Autowired
	private TemplateDao templateDao;
	
	@Autowired
	private ProjectsDao projectsDao;
	
	@Autowired
	private UserDao userDao;

	@Override
	public PaginationSupport<ProjectImport> findProjectImportListForPage(ProjectImport projectImport, int limit, int start, ShiroUser shiroUser) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProjectImport.class);
		if (StringUtils.isNotEmpty(projectImport.getFileName())) {
			detachedCriteria.add(Restrictions.like("fileName", projectImport.getFileName(), MatchMode.ANYWHERE));
		}
		if (projectImport.getSearchStartTime() != null) {
			detachedCriteria.add(Restrictions.ge("importDate", projectImport.getSearchStartTime()));
		}
		if (projectImport.getSearchEndTime() != null) {
			detachedCriteria.add(Restrictions.le("importDate", projectImport.getSearchEndTime()));
		}
		if (projectImport.getImportStatus() != 0) {
			detachedCriteria.add(Restrictions.eq("importStatus", projectImport.getImportStatus()));
		}
		if (!shiroUser.isAdmin) {
			detachedCriteria.add(Restrictions.eq("operator", userDao.get(shiroUser.getId())));
		}
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		PaginationSupport<ProjectImport> projectImportPage = projectImportDao.findPageByCriteria(detachedCriteria, limit, start);
		return projectImportPage;
	}
	
	@Override
	public PaginationSupport<ProjectImportDetail> findProjectImportDetailListForPage(ProjectImportDetail projectImportDetail, int limit,int start, ShiroUser shiroUser) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ProjectImportDetail.class);
		if (projectImportDetail.getProjectImport() != null && StringUtils.isNotEmpty(projectImportDetail.getProjectImport().getFileName())) {
			detachedCriteria.add(Restrictions.eq("projectImport", projectImportDetail.getProjectImport()));
		}
//		if (StringUtils.isNotEmpty(projectImportDetail.getName())) {
//			detachedCriteria.add(Restrictions.or(Restrictions.like("name", projectImportDetail.getName(), MatchMode.ANYWHERE), 
//					Restrictions.like("idNumber", projectImportDetail.getName(), MatchMode.ANYWHERE)));
//		}
		detachedCriteria.addOrder(Order.asc("validateStatus"));
		detachedCriteria.addOrder(Order.asc("rowNumber"));
		detachedCriteria.setProjection(null);
		PaginationSupport<ProjectImportDetail> projectImportDetailPage = projectImportDetailDao.findPageByCriteria(detachedCriteria, limit, start);
		return projectImportDetailPage;
	}

	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECT_IMPORT_CN, opType=OperationType.Add, opText="项目批量导入")
	public Long add(ProjectImport projectImport, ShiroUser shrioUser) throws BusinessServiceException {
		Long projectImportId = this.add(projectImport);
		return projectImportId;
	}
	
	@Override
	public List<Template> findTemplateByType(int templateType){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Template.class);
		detachedCriteria.createAlias("templateType", "templateType");
		detachedCriteria.add(Restrictions.eq("templateType.typeKey", templateType));
		detachedCriteria.setProjection(null);
		detachedCriteria.addOrder(Order.desc("id"));
		
		List<Template> templateList = templateDao.findAllByCriteria(detachedCriteria);
		List<Template> newList = new ArrayList<Template>();
		for (Object template : templateList) {
			Object[] templateObject = (Object[]) template;
			Template templateResult = (Template) (templateObject[templateObject.length-1]);
			newList.add(templateResult);
		}
		return newList;
	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECT_IMPORT_CN, opType=OperationType.Import, opText="项目批量导入")
	public int projectImport(MultipartFile file, String rootPath, ShiroUser shiroUser) throws BusinessServiceException{
		try {
			//第一步：保存文件到服务器
			File fileOnServer = saveFileToServer(file, rootPath);
			//第二步：实例化导入结果信息
			ProjectImport projectImport = new ProjectImport();
			projectImport.setOperator(userDao.get(shiroUser.getId()));
			projectImport.setImportDate(new Date());
			projectImport.setFileName(fileOnServer.getName());
			projectImport.setImportStatus(1);
			projectImport.setDeleted(false);
			projectImport.setLastUpdateDate(projectImport.getImportDate());
			
			//第三步：读取文件类容到List对象
			List<Map<String, Object>> projectList = ProjectExcelReader.readExcel(fileOnServer);
			
			//第四步：将map对象转换为实体对象，并且验证每个属性是否正确
			List<ProjectImportDetail> projectImportDetails = convertAndValidate(projectList, projectImport, shiroUser);
			
			projectImport.setProjectImportDetails(projectImportDetails);
			//第五步：保存导入对象到数据库
			projectImportDao.add(projectImport);
			
			if (projectImport.getImportStatus() == 1) {
				projectImport.setProjects();
				projectsDao.add(projectImport.getProjects());
			}
			return projectImport.getImportStatus();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessServiceException(e.getMessage());
		} 
	}
	
	/**
	 * 保存上传文件到指定目录下的ProjectImportFiles文件夹下
	 * @author yudq
	 * @param
	 * @return File
	 * @throws BusinessServiceException
	 */
	private File saveFileToServer(MultipartFile file, String rootPath) throws BusinessServiceException{
		try{
			if (StringUtils.isEmpty(ProjectExcelReader.ROOT_PATH)) {
				ProjectExcelReader.ROOT_PATH = rootPath;
			}
			InputStream inputStream = file.getInputStream();
			File dir = new File(rootPath + File.separator + "ProjectImportFiles");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			//Create the file on server
			String fileName = file.getOriginalFilename();//new String(file.getOriginalFilename().getBytes("ISO8859-1"), "UTF-8");
			int lastDotIndex = fileName.lastIndexOf('.');
			String preFileName = fileName.substring(0, lastDotIndex);
			String extensionName = fileName.substring(lastDotIndex);
			String serverFileName = dir.getAbsolutePath() + File.separator + preFileName + "-"+DateUtil.getCurrentDateTime2()+extensionName;
			File targetFile = new File(serverFileName);
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
			FileCopyUtils.copy(inputStream, outputStream);
			inputStream.close();
			outputStream.close();
			return targetFile;
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessServiceException(e.getMessage());
		}
	}
	/**
	 * 将List<Map>转换为List<ProjectImportDetail>
	 * @param creditList
	 * @return
	 * @author mars
	 */
	private List<ProjectImportDetail> convertAndValidate(List<Map<String, Object>> projectList, ProjectImport projectImport, ShiroUser shiroUser){
		List<ProjectImportDetail> projectImportDetails = new ArrayList<ProjectImportDetail>();
		int successCount = 0;
		int failCount = 0;
		int addCount = 0;
		int updateCount = 0;
		String seed = null;
		for(Map<String, Object> map : projectList){
			StringBuffer stringBuffer = new StringBuffer();
			String projectNumber = null;
			ProjectImportDetail projectImportDetail = new ProjectImportDetail();
			if (StringUtils.isEmpty(map.get("projectNumber") + "")) {
//				stringBuffer.append("项目唯一编号为空");
				//编号为空自动生成编号
				seed = MD5Util.MD5Encode(map.get("referenceNumber") + "" + map.get("fundYear") + ""
						 + map.get("countyLevelCity") + "" + map.get("town") + "" 
						 + (map.get("village") + "")== null ? "" : (map.get("village") + "")
						 + map.get("projectName") + "" + map.get("scaleAndContent") + "");	
//				failCount++;
				projectImportDetail.setImportStatus(1);	//新增
				projectImportDetail.setRepeatStatus(0);
				projectImportDetail.setCoverStatus(0);
				projectImportDetail.setValidateDescription(stringBuffer.toString());
				addCount ++;
				successCount ++;
	//			projectImportDetail.setValidateStatus(-1);
			}else {
				projectNumber = map.get("projectNumber")+"";
				Projects oldProject = projectsDao.findByProjectNumber(projectNumber);
				if(oldProject != null){
					if (oldProject.getProjectNumber().equals(projectNumber)) {
						String referenceNumber = map.get("referenceNumber") + "";
						String fundYear = map.get("fundYear") + "";
						String city = map.get("city") + "";
						String countyLevelCity = map.get("countyLevelCity") + "";
						String town = map.get("town") + "";
						String village = map.get("village") + "";
						String subjectName = map.get("subjectName") + "";
						String fundType = map.get("fundType") + "";
						String projectName = map.get("projectName") + "";
						String approvalNumber = map.get("approvalNumber") + "";
						Double totalFund = Double.parseDouble(map.get("totalFund") + "");
						Double financeFund = Double.parseDouble(map.get("financeFund") + "");
						Double selfFinancing = Double.parseDouble(map.get("selfFinancing") + "");
						Double integrateFund = Double.parseDouble(map.get("integrateFund") + "");
					    Integer coveredFarmerNumber = Integer.parseInt(map.get("coveredFarmerNumber") + "");
					    Integer coveringNumber = Integer.parseInt(map.get("coveringNumber") + "");
					    Integer povertyStrickenFarmerNumber = Integer.parseInt(map.get("povertyStrickenFarmerNumber") + "");
					    Integer povertyStrickenPeopleNumber = Integer.parseInt(map.get("povertyStrickenPeopleNumber") + "");
						String scaleAndContent = map.get("scaleAndContent") + "";
						String carryOutUnit = map.get("carryOutUnit") + "";
						String chargePerson = map.get("chargePerson") + "";
						Integer deadline = Integer.parseInt(map.get("deadline") + "");
						String standbyNumber = map.get("standbyNumber") + "";
						Double fundToCountry = Double.parseDouble(map.get("fundToCountry") + "");
						String createUser = map.get("createUser") + "";
						String createTime = map.get("createTime") + "";
						String inputStatus = map.get("inputStatus") + "";
						String approvingDepartment = map.get("approvingDepartment") + "";
						if(!((oldProject.getReferenceNumber()==null ? "":oldProject.getReferenceNumber() ).equals(referenceNumber)) || !((oldProject.getFundYear() == null ? "" : oldProject.getFundYear()).equals(fundYear))
							|| !((oldProject.getCity()==null?"":oldProject.getCity()).equals(city)) || !((oldProject.getCountyLevelCity()==null?"":oldProject.getCountyLevelCity()).equals(countyLevelCity)) ||
							!((oldProject.getTown()==null?"":oldProject.getTown()).equals(town)) || !((oldProject.getVillage()==null?"":oldProject.getVillage()).equals(village)) || !((oldProject.getSubjectName()==null?"":oldProject.getSubjectName()).equals(subjectName))
							|| !((oldProject.getFundType()==null?"":oldProject.getFundType()).equals(fundType)) || !((oldProject.getProjectName()==null?"":oldProject.getProjectName()).equals(projectName)) || 
							!((oldProject.getApprovalNumber()==null?"":oldProject.getApprovalNumber()).equals(approvalNumber)) || !((oldProject.getTotalFund()==null?"":oldProject.getTotalFund()).equals(totalFund))
							|| !((oldProject.getFinanceFund()==null?"":oldProject.getFinanceFund()).equals(financeFund)) || !((oldProject.getSelfFinancing()==null?"":oldProject.getSelfFinancing()).equals(selfFinancing))
							|| !((oldProject.getIntegrateFund()==null?"":oldProject.getIntegrateFund()).equals(integrateFund)) || !((oldProject.getCoveredFarmerNumber()==null?"":oldProject.getCoveredFarmerNumber()).equals(coveredFarmerNumber))
							|| !((oldProject.getCoveringNumber()==null?"":oldProject.getCoveringNumber()).equals(coveringNumber)) || !((oldProject.getPovertyStrickenFarmerNumber()==null?"":oldProject.getPovertyStrickenFarmerNumber()).equals(povertyStrickenFarmerNumber))
							|| !((oldProject.getPovertyStrickenPeopleNumber()==null?"":oldProject.getPovertyStrickenPeopleNumber()).equals(povertyStrickenPeopleNumber)) || !((oldProject.getScaleAndContent()==null?"":oldProject.getScaleAndContent()).equals(scaleAndContent))
							|| !((oldProject.getCarryOutUnit()==null?"":oldProject.getCarryOutUnit()).equals(carryOutUnit)) || !((oldProject.getChargePerson()==null?"":oldProject.getChargePerson()).equals(chargePerson)) || !((oldProject.getDeadline()==null?"":oldProject.getDeadline()).equals(deadline))
							|| !((oldProject.getStandbyNumber()==null?"":oldProject.getStandbyNumber()).equals(standbyNumber)) || !((oldProject.getFundToCountry()==null?"":oldProject.getFundToCountry()).equals(fundToCountry))
							|| !((oldProject.getCreateUser()==null?"":oldProject.getCreateUser()).equals(createUser)) || !((oldProject.getCreateTime()==null?"":oldProject.getCreateTime()).equals(createTime)) || !((oldProject.getInputStatus()==null?"":oldProject.getInputStatus()).equals(inputStatus))
							|| !((oldProject.getApprovingDepartment()==null?"":oldProject.getApprovingDepartment()).equals(approvingDepartment))
							){
							ProjectImportDetail  compareDescription = compareProjectImportDetailList(fundYear,town,village,subjectName,projectName,scaleAndContent,chargePerson,deadline,oldProject);
							projectImportDetail.setImportStatus(2);	//修改
							projectImportDetail.setRepeatStatus(2);
							projectImportDetail.setCoverStatus(0);
							projectImportDetail.setValidateDescription(compareDescription.getValidateDescription());
//							projectImportDetail.setCompareDescription(compareDescription.getCompareDescription());
						}else {
							stringBuffer.append("项目已存在,项目记录相同,");
							projectImportDetail.setImportStatus(2);	//修改
							projectImportDetail.setRepeatStatus(1);
							projectImportDetail.setCoverStatus(0);
							projectImportDetail.setValidateDescription(stringBuffer.toString());
						}
						//projectImportDetail.setProjects(oldProject);
						updateCount ++;
					} 
				}else {
					projectImportDetail.setImportStatus(1);	//新增
					projectImportDetail.setRepeatStatus(0);
					projectImportDetail.setCoverStatus(0);
					projectImportDetail.setValidateDescription(stringBuffer.toString());
					addCount ++;
				}
				successCount ++;
			}
			projectImportDetail.setProjectImport(projectImport);
			projectImportDetail.setReferenceNumber(map.get("referenceNumber") + "");
	//		projectImportDetail.setProjectNumber((map.get("projectNumber") + "")==null ? seed : map.get("projectNumber")+"");
			if (StringUtils.isEmpty(map.get("projectNumber") + "")){
				projectImportDetail.setProjectNumber(seed);
			}else{
				projectImportDetail.setProjectNumber((map.get("projectNumber") + "")==null ? "": map.get("projectNumber")+"");
			}
			
			projectImportDetail.setFundYear(map.get("fundYear") + "");
			projectImportDetail.setCity(map.get("city") + "");
			projectImportDetail.setCountyLevelCity(map.get("countyLevelCity") + "");
			projectImportDetail.setTown(map.get("town") + "");
			projectImportDetail.setVillage(map.get("village") + "");
			projectImportDetail.setSubjectName(map.get("subjectName") + "");
			projectImportDetail.setFundType(map.get("fundType") + "");
			projectImportDetail.setProjectName(map.get("projectName") + "");
			projectImportDetail.setApprovalNumber(map.get("approvalNumber") + "");
			projectImportDetail.setTotalFund(Double.parseDouble(map.get("totalFund") + ""));
			projectImportDetail.setFinanceFund(Double.parseDouble(map.get("financeFund") + ""));
			projectImportDetail.setSelfFinancing(Double.parseDouble(map.get("selfFinancing") + ""));
			projectImportDetail.setIntegrateFund(Double.parseDouble(map.get("integrateFund") + ""));
			projectImportDetail.setCoveredFarmerNumber(map.get("coveredFarmerNumber") + "");
			projectImportDetail.setCoveringNumber(map.get("coveringNumber") + "");
			projectImportDetail.setPovertyStrickenFarmerNumber(map.get("povertyStrickenFarmerNumber") + "");
			projectImportDetail.setPovertyGeneralPeople(map.get("povertyGeneralPeople") + "");
			projectImportDetail.setPovertyGeneralFarmer(map.get("povertyGeneralFarmer") + "");
			projectImportDetail.setPovertyLowIncomeFarmer(map.get("povertyLowIncomeFarmer") + "");
			projectImportDetail.setPovertyLowIncomePeople(map.get("povertyLowIncomePeople") + "");
//			projectImportDetail.setPovertyPoorFarmer(map.get("povertyPoorFarmer") + "");
//			projectImportDetail.setPovertyPoorPeople(povertyPoorPeople);
			projectImportDetail.setPovertyStrickenPeopleNumber(map.get("povertyStrickenPeopleNumber") + "");
			projectImportDetail.setScaleAndContent(map.get("scaleAndContent") + "");
			projectImportDetail.setCarryOutUnit(map.get("carryOutUnit") + "");
			projectImportDetail.setChargePerson(map.get("chargePerson") + "");
			projectImportDetail.setDeadline(map.get("deadline") + "");
			projectImportDetail.setStandbyNumber(map.get("standbyNumber") + "");
			projectImportDetail.setFundToCountry(Double.parseDouble(map.get("fundToCountry") + ""));
			projectImportDetail.setApproveState(map.get("approveState") + "");
			projectImportDetail.setCreateUser(map.get("createUser") + "");
			projectImportDetail.setCreateTime(map.get("createTime") + "");
			projectImportDetail.setInputStatus(map.get("inputStatus") + "");
			projectImportDetail.setApprovingDepartment(map.get("approvingDepartment") + "");
			
			projectImportDetails.add(projectImportDetail);
		}
		projectImport.setSuccessCount(successCount);
		projectImport.setFailCount(failCount);
		projectImport.setAddCount(addCount);
		projectImport.setUpdateCount(updateCount);
		if (projectImport.getFailCount()>0) {
			projectImport.setImportStatus(2); //验证失败
		}
		if (!shiroUser.isAdmin) {
			if (!shiroUser.isCommon) {
				projectImport.setImportStatus(3);
			}
		}
		return projectImportDetails;
	}
	
	private ProjectImportDetail compareProjectImportDetailList(String fundYear,String town,String village,String subjectName,String projectName,String scaleAndContent,String chargePerson,Integer deadline,Projects oldProject){
		ProjectImportDetail projectImportDetail = new ProjectImportDetail();
		StringBuffer stringBuffer = new StringBuffer();
			if(!((oldProject.getFundYear() == null ? "" : oldProject.getFundYear()).equals(fundYear)) && 
			   !((oldProject.getTown()==null?"":oldProject.getTown()).equals(town))){
				stringBuffer.append("资金年度不同,乡镇名不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getFundYear() == null ? "" : oldProject.getFundYear()).equals(fundYear)) && 
					!((oldProject.getVillage()==null?"":oldProject.getVillage()).equals(village))){
				stringBuffer.append("资金年度不同,村名不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getFundYear() == null ? "" : oldProject.getFundYear()).equals(fundYear)) && 
					!((oldProject.getSubjectName()==null?"":oldProject.getSubjectName()).equals(subjectName))){
				stringBuffer.append("资金年度不同,专项名称不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getFundYear() == null ? "" : oldProject.getFundYear()).equals(fundYear)) && 
					!((oldProject.getProjectName()==null?"":oldProject.getProjectName()).equals(projectName))){
				stringBuffer.append("资金年度不同,项目名称不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getFundYear() == null ? "" : oldProject.getFundYear()).equals(fundYear)) &&
					!((oldProject.getScaleAndContent()==null?"":oldProject.getScaleAndContent()).equals(scaleAndContent))){
				stringBuffer.append("资金年度不同,财政资金建设规模及内容不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getFundYear() == null ? "" : oldProject.getFundYear()).equals(fundYear)) && 
					 !((oldProject.getChargePerson()==null?"":oldProject.getChargePerson()).equals(chargePerson))){
				stringBuffer.append("资金年度不同,项目负责人不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getFundYear() == null ? "" : oldProject.getFundYear()).equals(fundYear)) && 
					!((oldProject.getDeadline()==null?"":oldProject.getDeadline()).equals(deadline))){
				stringBuffer.append("资金年度不同,完成期限不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getTown()==null?"":oldProject.getTown()).equals(town)) &&
					!((oldProject.getVillage()==null?"":oldProject.getVillage()).equals(village))){
				stringBuffer.append("乡镇名不同,村名不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getTown()==null?"":oldProject.getTown()).equals(town)) &&
					!((oldProject.getSubjectName()==null?"":oldProject.getSubjectName()).equals(subjectName))){
				stringBuffer.append("乡镇名不同,专项名称不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getTown()==null?"":oldProject.getTown()).equals(town)) &&
					!((oldProject.getProjectName()==null?"":oldProject.getProjectName()).equals(projectName))){
				stringBuffer.append("乡镇名不同,项目名称不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getTown()==null?"":oldProject.getTown()).equals(town)) &&
					!((oldProject.getScaleAndContent()==null?"":oldProject.getScaleAndContent()).equals(scaleAndContent))){
				stringBuffer.append("乡镇名不同,财政资金建设规模及内容不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getTown()==null?"":oldProject.getTown()).equals(town)) &&
					!((oldProject.getChargePerson()==null?"":oldProject.getChargePerson()).equals(chargePerson))){
				stringBuffer.append("乡镇名不同,项目负责人不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getVillage()==null?"":oldProject.getVillage()).equals(village)) &&
					!((oldProject.getSubjectName()==null?"":oldProject.getSubjectName()).equals(subjectName))){
				stringBuffer.append("村名不同,专项名称不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getVillage()==null?"":oldProject.getVillage()).equals(village)) &&
					!((oldProject.getProjectName()==null?"":oldProject.getProjectName()).equals(projectName))){
				stringBuffer.append("村名不同,项目名称不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getVillage()==null?"":oldProject.getVillage()).equals(village)) &&
					!((oldProject.getScaleAndContent()==null?"":oldProject.getScaleAndContent()).equals(scaleAndContent))){
				stringBuffer.append("村名不同,财政资金建设规模及内容不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getVillage()==null?"":oldProject.getVillage()).equals(village)) &&
					!((oldProject.getChargePerson()==null?"":oldProject.getChargePerson()).equals(chargePerson))){
				stringBuffer.append("村名不同,项目负责人不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getSubjectName()==null?"":oldProject.getSubjectName()).equals(subjectName)) &&
					!((oldProject.getProjectName()==null?"":oldProject.getProjectName()).equals(projectName))){
				stringBuffer.append("专项名称不同,项目名称不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getSubjectName()==null?"":oldProject.getSubjectName()).equals(subjectName)) &&
					!((oldProject.getScaleAndContent()==null?"":oldProject.getScaleAndContent()).equals(scaleAndContent))){
				stringBuffer.append("专项名称不同,财政资金建设规模及内容不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getSubjectName()==null?"":oldProject.getSubjectName()).equals(subjectName)) &&
					!((oldProject.getChargePerson()==null?"":oldProject.getChargePerson()).equals(chargePerson))){
				stringBuffer.append("专项名称不同,项目负责人不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getProjectName()==null?"":oldProject.getProjectName()).equals(projectName)) &&
					!((oldProject.getScaleAndContent()==null?"":oldProject.getScaleAndContent()).equals(scaleAndContent))){
				stringBuffer.append("项目名称不同,财政资金建设规模及内容不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getProjectName()==null?"":oldProject.getProjectName()).equals(projectName)) &&
					!((oldProject.getChargePerson()==null?"":oldProject.getChargePerson()).equals(chargePerson))){
				stringBuffer.append("项目名称不同,项目负责人不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getScaleAndContent()==null?"":oldProject.getScaleAndContent()).equals(scaleAndContent)) &&
					!((oldProject.getChargePerson()==null?"":oldProject.getChargePerson()).equals(chargePerson))){
				stringBuffer.append("财政资金建设规模及内容不同,项目负责人不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getFundYear() == null ? "" : oldProject.getFundYear()).equals(fundYear))){
				stringBuffer.append("资金年度不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getTown()==null?"":oldProject.getTown()).equals(town))){
				stringBuffer.append("乡镇名不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getVillage()==null?"":oldProject.getVillage()).equals(village))){
				stringBuffer.append("村名不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getSubjectName()==null?"":oldProject.getSubjectName()).equals(subjectName))){
				stringBuffer.append("专项名称不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if( !((oldProject.getProjectName()==null?"":oldProject.getProjectName()).equals(projectName))){
				stringBuffer.append("项目名称不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getScaleAndContent()==null?"":oldProject.getScaleAndContent()).equals(scaleAndContent))){
				stringBuffer.append("财政资金建设规模及内容不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if( !((oldProject.getChargePerson()==null?"":oldProject.getChargePerson()).equals(chargePerson))){
				stringBuffer.append("项目负责人不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else if(!((oldProject.getDeadline()==null?"":oldProject.getDeadline()).equals(deadline))){
				stringBuffer.append("完成期限不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}else {
				stringBuffer.append("项目内容不同");
				projectImportDetail.setValidateDescription(stringBuffer.toString());
			}	
		return projectImportDetail;
	}
	
	@Override
	public File generateTemplateRules(File templateFile, ShiroUser shiroUser){
//		List<CreditRules> creditRulesList = creditRuleDao.findAllByUserDeptLevel();
//		Constants deptLevel = shiroUser.constants;
//		return ExcelReader.generateTemplateRules(templateFile, creditRulesList, deptLevel);
		return null;
	}

}
