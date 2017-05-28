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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.DateUtil;
import com.chenghui.agriculture.core.utils.FarmerExcelReader;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.ProjectExcelReader;
import com.chenghui.agriculture.dao.projectManage.FarmerDao;
import com.chenghui.agriculture.dao.projectManage.FarmerImportDao;
import com.chenghui.agriculture.dao.projectManage.ProjectsDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.Farmer;
import com.chenghui.agriculture.model.FarmerImport;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.projectManage.FarmerService;


@Service("farmerService")
@CacheConfig(cacheNames = {"farmers"})
public class FarmerServiceImpl extends GenericServiceImpl<Farmer, Long> implements FarmerService {
	
	@Autowired
	FarmerDao farmerDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	FarmerImportDao farmerImportDao;
	
	@Autowired
	ProjectsDao projectsDao;
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.PROJECT_IMPORT_CN, opType=OperationType.Import, opText="项目批量导入")
	public int projectImport(MultipartFile file, String rootPath,Long subProject_id, ShiroUser shiroUser) throws BusinessServiceException{
		try {
			//第一步：保存文件到服务器
			File fileOnServer = saveFileToServer(file, rootPath);
			//第二步：实例化导入结果信息
			FarmerImport farmerImport = new FarmerImport();
			farmerImport.setOperator(userDao.get(shiroUser.getId()));
			farmerImport.setImportDate(new Date());
			farmerImport.setFileName(fileOnServer.getName());
			farmerImport.setImportStatus(1);
			farmerImport.setDeleted(false);
			farmerImport.setLastUpdateDate(farmerImport.getImportDate());
			
			//第三步：读取文件类容到List对象
			List<Map<String, Object>> farmerList = FarmerExcelReader.readExcel(fileOnServer);
			
			//第四步：将map对象转换为实体对象，并且验证每个属性是否正确
			List<Farmer> farmerImportDetails = convertAndValidate(farmerList, farmerImport, subProject_id,shiroUser);
			
			farmerImport.setFarmers(farmerImportDetails);
			//第五步：保存导入对象到数据库
			farmerImportDao.add(farmerImport);
			
			return farmerImport.getImportStatus();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessServiceException(e.getMessage());
		} 
	}
	
	/**
	 * 保存上传文件到指定目录下的ProjectImportFiles文件夹下
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
	private List<Farmer> convertAndValidate(List<Map<String, Object>> farmerList, FarmerImport farmerImport,Long subProject_id, ShiroUser shiroUser){
		List<Farmer> farmers = new ArrayList<Farmer>();
		int successCount = 0;
		int failCount = 0;
		int updateCount = 0;
		for(Map<String, Object> map : farmerList){
			StringBuffer stringBuffer = new StringBuffer();
			String farmerNumber = null;
			if (StringUtils.isEmpty(map.get("farmerNumber") + "")) {
				stringBuffer.append("农户编号为空");
				failCount ++;
				continue;
			}else {
				farmerNumber = map.get("farmerNumber")+"";
			}
			
			Farmer farmer = new Farmer();
			Farmer farmer1 = farmerDao.findFarmerNumber(farmerNumber);
			if(farmer1 != null){
				if(farmerNumber.equals(farmer1.getFarmerNumber())) {
					throw new BusinessServiceException("用户编号已存在，导入失败！");
				}
			}
			farmer.setFarmerImport(farmerImport);
			farmer.setProject_id(subProject_id);
			farmer.setFarmerNumber(map.get("farmerNumber") + "");
			farmer.setFarmerName(map.get("farmerName") + "");
			farmer.setPhoneNumber(map.get("phoneNumber") + "");
			farmer.setContent(map.get("content") + "");
			successCount ++;
			farmers.add(farmer);
		}
		farmerImport.setSuccessCount(successCount);
		farmerImport.setFailCount(failCount);
		farmerImport.setUpdateCount(updateCount);
		if (farmerImport.getFailCount()>0) {
			farmerImport.setImportStatus(2); //验证失败
		}
		if (!shiroUser.isAdmin) {
			if (!shiroUser.isCommon) {
				farmerImport.setImportStatus(3);
			}
		}
		return farmers;
	}
	
	/**
	 * 根据项目查找导入的农户信息
	 * @param farmer
	 * @param limit
	 * @param start
	 * @param shiroUser
	 * @return
	 */
	@Override
	public PaginationSupport<Farmer> findProjectImportDetailListForPage(Long projectId, int limit,int start, ShiroUser shiroUser) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Farmer.class);
		if (projectId != null) {
			detachedCriteria.add(Restrictions.eq("project_id", projectId));
		}
//		if (StringUtils.isNotEmpty(projectImportDetail.getName())) {
//			detachedCriteria.add(Restrictions.or(Restrictions.like("name", projectImportDetail.getName(), MatchMode.ANYWHERE), 
//					Restrictions.like("idNumber", projectImportDetail.getName(), MatchMode.ANYWHERE)));
//		}
		detachedCriteria.addOrder(Order.asc("id"));
		detachedCriteria.setProjection(null);
		PaginationSupport<Farmer> farmerPage = farmerDao.findPageByCriteria(detachedCriteria, limit, start);
		return farmerPage;
	}
	
	/**
	 * 删除农户信息
	 */
	@Override
	@CacheEvict(allEntries=true)
	@SystemOperationLogAnnotation(appModule=AppModule.FARMER_MANAGE_CN, opType=OperationType.Del, opText="删除农户")
	public void removeByID(Long farmerId) {
		Farmer farmer = farmerDao.get(farmerId);
		farmerDao.remove(farmer);
	}
	
	@Override
	@Cacheable
	public List<Farmer> findAll() {
		return super.findAll();
	}
	
	@Override
	public List<Farmer> findFarmerByPId(Long pId){
		
		return farmerDao.findByLongColumn("project_id", pId);
	}
	
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.FARMER_MANAGE_CN, opType=OperationType.AppQuery, opText="查询农户列表")
	public List<Farmer> appFindFarmerByPId(Long pId){
		List<Farmer> farmers = new ArrayList<>();
		farmers.addAll(farmerDao.findByLongColumn("project_id", pId));
		if (farmers.isEmpty()) {
			Projects projects = projectsDao.get(pId);
			if (projects != null && projects.getParentId() != null) {
				farmers.addAll(farmerDao.findByLongColumn("project_id", projects.getParentId()));
				if (farmers.isEmpty()) {
					Projects projects1 = projectsDao.get(projects.getParentId());
					if (projects1 != null && projects1.getParentId() != null) {
						farmers.addAll(farmerDao.findByLongColumn("project_id", projects1.getParentId()));
					}
				}
			}
		}
		return farmers;
	}

	/**
	 * 添加农民
	 */
	@Override
	@CacheEvict(allEntries=true)
	@SystemOperationLogAnnotation(appModule=AppModule.POINT_MANAGE_CN, opType=OperationType.AddPrivilege, opText="增加农民")
	public Long addFarmer(Farmer farmer){
		Long id = farmerDao.add(farmer); //记录
		return id;
	}
	
	/**
	 * 删除农民
	 */
	@Override
	@CacheEvict(allEntries=true)
	@SystemOperationLogAnnotation(appModule=AppModule.POINT_MANAGE_CN, opType=OperationType.DelPrivilege, opText="删除农民信息")
	public void removeFarmerByID(Long id) {
		Farmer farmer = farmerDao.get(id);
		farmerDao.remove(farmer);
	}
	
	/**
	 * 修改农民
	 * @param farmer
	 * @return
	 */
	@Override
	@CacheEvict(allEntries=true)
	@SystemOperationLogAnnotation(appModule=AppModule.POINT_MANAGE_CN, opType=OperationType.UpdatePrivilege, opText="修改农民信息")
	public Farmer updateFarmer(Farmer farmer) {
		Farmer newFarmer = farmerDao.get(farmer.getId());
		newFarmer.setFarmerName(farmer.getFarmerName());
		newFarmer.setPhoneNumber(farmer.getPhoneNumber());
		newFarmer.setFarmerNumber(farmer.getFarmerNumber());
		
		try {
			farmerDao.update(newFarmer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return newFarmer;
	}
	
	/**
	 * 修改农民
	 * @param farmer
	 * @return
	 */
	@Override
//	@CacheEvict(allEntries=true)
	@SystemOperationLogAnnotation(appModule=AppModule.FARMER_MANAGE_CN, opType=OperationType.UpdatePrivilege, opText="修改农户信息")
	public Farmer updateFarmerInfo(Farmer farmer) {
		Farmer newFarmer = farmerDao.get(farmer.getId());
		if(!newFarmer.getFarmerNumber().equals(farmer.getFarmerNumber())){
			if(farmerDao.getFarmerByFarmerNumber(farmer) != null){
				throw new BusinessServiceException("农户编号已存在,保存失败！");
			}
		}
		newFarmer.setFarmerName(farmer.getFarmerName());
		newFarmer.setPhoneNumber(farmer.getPhoneNumber());
		newFarmer.setFarmerNumber(farmer.getFarmerNumber());
		newFarmer.setContent(farmer.getContent());
		try {
			farmerDao.update(newFarmer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return newFarmer;
	}
}
