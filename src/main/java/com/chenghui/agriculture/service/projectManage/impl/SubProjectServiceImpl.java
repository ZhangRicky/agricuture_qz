package com.chenghui.agriculture.service.projectManage.impl;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.ProjectExcelReader;
import com.chenghui.agriculture.dao.projectManage.FilesDao;
import com.chenghui.agriculture.dao.projectManage.ProjectDao;
import com.chenghui.agriculture.dao.projectManage.SubProjectDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.Project;
import com.chenghui.agriculture.model.SubProject;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.projectManage.SubProjectService;

/**
 * 子项目业务接口实现
 * @author LLJ
 * @version V1.0
 */
@Service("subProjectService")
public class SubProjectServiceImpl extends GenericServiceImpl<SubProject, Long> implements SubProjectService {
	
	@Autowired
	SubProjectDao subProjectDao;
	
	@Autowired
	ProjectDao projectDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	FilesDao filesDao;
	
	public void setSubProjectDao(SubProjectDao subProjectDao) {
		this.subProjectDao = subProjectDao;
	}
	
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


	/**
	 * 添加子项目
	 */
	@Override
//	@CacheEvict(value="subProject",allEntries=true)
	@SystemOperationLogAnnotation(appModule=AppModule.SUBPROJECT_MANAGE_CN, opType=OperationType.Add, opText="添加子项目")
	public Long addSubProject(SubProject subProject) {
		Project newProject = projectDao.get(subProject.getProjectId());
//		newProject.setSubProjectNumber(newProject.getSubProjectNumber()+1);
//		projectDao.update(newProject);
		subProject.setProject(newProject);
		subProject.setFlag(0);
		subProject.setCheckStatus(0);
		if(subProject.getProject() != null){
			subProject.getProject().setSubProjectNumber(subProject.getProject().getSubProjectNumber()+1);
		}
//		Long a= super.add(subProject);
//		Project p = projectDao.get(subProject.getProjectId());
//		p.setSubProjectNumber(subProjectDao.subProjectNumber(p.getId()));
////		subProject.getProject().setSubProjectNumber(subProjectNumber);
//		projectDao.update(p);
		return super.add(subProject);
	}

	/**
	 * 删除子项目
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.SUBPROJECT_MANAGE_CN, opType=OperationType.Del, opText="删除子项目")
	public void removeByID(Long id) {
		SubProject subProject = subProjectDao.get(id);
		subProject.setFlag(1);
		if(subProject.getProject() != null){
			subProject.getProject().setSubProjectNumber(subProject.getProject().getSubProjectNumber()-1);
		}
		subProjectDao.update(subProject);
	}

	/**
	 * 修改子项目
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.SUBPROJECT_MANAGE_CN, opType=OperationType.Update, opText="更新子项目")
	public SubProject updateSubProjectInfo(SubProject subProject,Long subPid) {
		SubProject newSubProject = subProjectDao.get(subPid);
		Project p = projectDao.get(subProject.getProjectId());
		newSubProject.setProject(p);
		newSubProject.setSubProjectName(subProject.getSubProjectName());
		newSubProject.setSubProjectNumber(subProject.getSubProjectNumber());
		newSubProject.setTotalCapital(subProject.getTotalCapital());
		newSubProject.setShouldAccount(subProject.getShouldAccount());
		newSubProject.setImplementationUnit(subProject.getImplementationUnit());
		newSubProject.setSubProjectArea(subProject.getSubProjectArea());
		newSubProject.setFarmerName(subProject.getFarmerName());
		newSubProject.setPath(subProject.getPath());
		newSubProject.setProjectScaleAndContent(subProject.getProjectScaleAndContent());
		newSubProject.setConstructionMode(subProject.getConstructionMode());
		newSubProject.setCheckStatus(subProject.getCheckStatus());
		newSubProject.setFinancebiLv(subProject.getFinancebiLv());
		try {
			subProjectDao.update(newSubProject);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return newSubProject;
	}

	/**
	 * 跟id查询子项目详细信息
	 */
	@Override
	public SubProject findSubProjectById(Long id) {
		SubProject subProject = subProjectDao.get(id);
		return subProject;
	}


	@Override
	public File generateTemplateRules(File templateFile, ShiroUser shiroUser) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * 分页查询子项目
	 */
	@Override
	public PaginationSupport<SubProject> findSubProjectListForPage(String xxnr, int limit, int start,
			ShiroUser shiroUser) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SubProject.class);
		if(StringUtils.isNotEmpty(xxnr)) {
			detachedCriteria.add(
					Restrictions.or(
							Restrictions.like("subProjectName",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("implementationUnit",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("farmerName",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("subProjectNumber",xxnr,MatchMode.ANYWHERE)
					)
				);
		}
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		PaginationSupport<SubProject> subProjectPage = subProjectDao.findPageByCriteria(detachedCriteria, limit, start);
		return subProjectPage;
	}
		
	
	
	@Override
	public List<SubProject> findSubProjectsByPId(Long pId){
		return subProjectDao.getSubProjectsByPId(pId);
	}


	@Override
	public void subProjectFile(MultipartFile file, String rootPath, ShiroUser shiroUser,SubProject subProject) throws BusinessServiceException {
		try {
			//第一步：保存文件到服务器
			File fileOnServer = saveFileToServer(file, rootPath);
			//第三步：保存导入对象到数据库
			addSubProject(subProject);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessServiceException(e.getMessage());
		} 
	}
	/**
	 * 保存上传文件到指定目录下的subProjectFile文件夹下
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
			File dir = new File(rootPath + File.separator + "subProjectFile");			
			if (!dir.exists()) {
				dir.mkdirs();
			}
			//Create the file on server
			String fileName = new String(file.getOriginalFilename().getBytes("ISO8859-1"), "UTF-8");
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
	 * 将财务比率写入子项目记录
	 *  @param sum 报账的总资金
	 */
	@Override
	public void addFinancebiLv(SubProject s) {
		SubProject subproject = subProjectDao.get(s.getId());
		subproject.setFinancebiLv(s.getFinancebiLv());
		subProjectDao.update(subproject);	
	}
	
	@Override
	public void updateSubproject(Long subId,String bzAcount) {
		SubProject sub = subProjectDao.get(subId);
		if(bzAcount == null || bzAcount == ""){
			bzAcount ="0";
		}
		double sum =(Double.parseDouble(sub.getFinancebiLv()) - Double.parseDouble(bzAcount));
		if(sum > 0){
			sub.setFinancebiLv(String.valueOf(sum));	
		}else{
			sub.setFinancebiLv(null);
		}
		subProjectDao.update(sub);
		
	}

	/**
	 * 根据主项目id查询子项目
	 */
	@Override
	public PaginationSupport<SubProject> findSubProjectListByPid(String xxnr, int limit, int start,
			ShiroUser shiroUser,Long projectId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SubProject.class);
		if(StringUtils.isNotEmpty(xxnr)) {
			detachedCriteria.add(
					Restrictions.or(
							Restrictions.like("subProjectName",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("implementationUnit",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("farmerName",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("subProjectNumber",xxnr,MatchMode.ANYWHERE)
					)
				);
		}
		detachedCriteria.add(Restrictions.eq("flag", 0));
		Project project =new Project();
		project.setId(projectId);
		detachedCriteria.add(Restrictions.eq("project", project));
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		PaginationSupport<SubProject> subProjectPage = subProjectDao.findPageByCriteria(detachedCriteria, limit, start);
		return subProjectPage;
	}
	
}
