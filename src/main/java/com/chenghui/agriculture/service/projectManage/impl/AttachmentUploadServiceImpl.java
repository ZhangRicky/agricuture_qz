package com.chenghui.agriculture.service.projectManage.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.DateUtil;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.ProjectExcelReader;
import com.chenghui.agriculture.dao.projectManage.FileUploadDao;
import com.chenghui.agriculture.dao.projectManage.PictureUploadDao;
import com.chenghui.agriculture.dao.projectManage.ProjectDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.FileUpload;
import com.chenghui.agriculture.model.PictureUpload;
import com.chenghui.agriculture.model.Project;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.projectManage.AttachmentUploadService;






@Service("attachmentUploadService")
public class AttachmentUploadServiceImpl extends GenericServiceImpl<Project, Long> implements AttachmentUploadService {

	final String PATH_UPLOAD_IMG = "PictureUploadFiles_img";
	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private PictureUploadDao pictureUploadDao;
	
	@Autowired
	private FileUploadDao fileUploadDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public PaginationSupport<Project> findProjectListForPage(Project project, int limit, int start, ShiroUser shiroUser) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Project.class);
		
		if(!StringUtils.isEmpty(project.getProjectName())){
			
			detachedCriteria.add(Restrictions.or(Restrictions.like("projectName", project.getProjectName(), MatchMode.ANYWHERE),
								 				 Restrictions.like("fundYear", project.getProjectName(), MatchMode.ANYWHERE),
								 				 Restrictions.like("subjectName", project.getProjectName(), MatchMode.ANYWHERE)
					));
		}
		
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		
		PaginationSupport<Project> projectPage = projectDao.findPageByCriteria(detachedCriteria, limit, start);
		
		return projectPage;
	}
	
	/**
	 * 上传附件
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ATTACHMENT_UPLOAD_CN, opType=OperationType.Import, opText="附件上传")
	public int projectImport(MultipartFile file,Long projectId,String rootPath,ShiroUser shiroUser) throws BusinessServiceException{
		try {
			//第一步：保存文件到服务器
			File fileOnServer = saveFileToServer(file, rootPath);
			
			//第二步：实例化导入信息
			PictureUpload pictureUpload = new PictureUpload();
			pictureUpload.setOperator(userDao.get(shiroUser.getId()));
			pictureUpload.setImportDate(new Date());
			pictureUpload.setFileName(PATH_UPLOAD_IMG  + File.separator + fileOnServer.getName());
			pictureUpload.setImportStatus(1);
			pictureUpload.setDeleted(false);
			pictureUpload.setLastUpdateDate(pictureUpload.getImportDate());
			pictureUpload.setProjectId(projectId);
			
			
			//第三步：保存导入对象到数据库
			pictureUploadDao.add(pictureUpload);
			if (pictureUpload.getImportStatus() == 1) {
				pictureUpload.setProjects();
				projectDao.add(pictureUpload.getProjects());
			}
			return pictureUpload.getImportStatus();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessServiceException(e.getMessage());
		} 
		
	}
	
	
	private File saveFileToServer(MultipartFile file, String rootPath) throws BusinessServiceException{
		try{
			if (StringUtils.isEmpty(ProjectExcelReader.ROOT_PATH)) {
				ProjectExcelReader.ROOT_PATH = rootPath;
			}
			InputStream inputStream = file.getInputStream();
			File dir = new File(rootPath + File.separator + PATH_UPLOAD_IMG);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			//Create the file on server
			String fileName = new String(file.getOriginalFilename());
			int lastDotIndex = fileName.lastIndexOf('.');
			String preFileName = fileName.substring(0, lastDotIndex);
			String extensionName = fileName.substring(lastDotIndex);
			String serverFileName = dir.getAbsolutePath() + File.separator + preFileName + "-"+DateUtil.getCurrentDateTime2()+extensionName;
			//+DateUtil.getCurrentDateTime2()+"-"
			ProjectExcelReader.getExtensionName(serverFileName);
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
	 * 上传批复文件
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ATTACHMENT_UPLOAD_CN, opType=OperationType.Import, opText="附件上传")
	public int fileImport(MultipartFile file,Long projectId,String rootPath,ShiroUser shiroUser) throws BusinessServiceException{
		try {
			//第一步：保存文件到服务器
			File fileOnServer = savePiFuFileToServer(file, rootPath);
			
			//第二步：实例化导入信息
			FileUpload fileUpload = new FileUpload();
			fileUpload.setOperator(userDao.get(shiroUser.getId()));
			fileUpload.setImportDate(new Date());
			fileUpload.setFileName(PATH_UPLOAD_IMG  + File.separator + fileOnServer.getName());
			fileUpload.setImportStatus(1);
			fileUpload.setDeleted(false);
			fileUpload.setLastUpdateDate(fileUpload.getImportDate());
			fileUpload.setProjectId(projectId);
			
			
			//第三步：保存导入对象到数据库
			fileUploadDao.add(fileUpload);
			if (fileUpload.getImportStatus() == 1) {
				fileUpload.setProjects();
				projectDao.add(fileUpload.getProjects());
			}
			return fileUpload.getImportStatus();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessServiceException(e.getMessage());
		} 
		
	}
	
	
	private File savePiFuFileToServer(MultipartFile file, String rootPath) throws BusinessServiceException{
		try{
			if (StringUtils.isEmpty(ProjectExcelReader.ROOT_PATH)) {
				ProjectExcelReader.ROOT_PATH = rootPath;
			}
			InputStream inputStream = file.getInputStream();
			File dir = new File(rootPath + File.separator + PATH_UPLOAD_IMG);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			//Create the file on server
			String fileName = new String(file.getOriginalFilename());
			int lastDotIndex = fileName.lastIndexOf('.');
			String preFileName = fileName.substring(0, lastDotIndex);
			String extensionName = fileName.substring(lastDotIndex);
			String serverFileName = dir.getAbsolutePath() + File.separator + preFileName + "-"+DateUtil.getCurrentDateTime2()+extensionName;
			//+DateUtil.getCurrentDateTime2()+"-"
			ProjectExcelReader.getExtensionName(serverFileName);
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
	 * 查找公示附件
	 */
	public List<PictureUpload> getAllPicture(Long projectId) {
		List<PictureUpload> list = null;
		try {
			String hql="from PictureUpload  where projectId=?";
			list=pictureUploadDao.findByHQL(hql, new Object[]{projectId});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查找批复文件
	 */
	public List<FileUpload> getAllFile(Long projectId) {
		List<FileUpload> list = null;
		try {
			String hql="from FileUpload  where projectId=?";
			list=fileUploadDao.findByHQL(hql, new Object[]{projectId});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查看项目详情
	 */
	@Override
	public List<Project> findProjectDetailListForPage(Long id, ShiroUser shiroUser) {
		List<Project> projectList = null;
		try {
			String hql="from Project  where id=?";
			projectList=projectDao.findByHQL(hql, new Object[]{id});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectList;
	}

	/**
	 * 删除附件图片
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ATTACHMENT_UPLOAD_CN, opType=OperationType.Del, opText="删除图片")
	public void removeByID(Long pictureId) {
		PictureUpload picture = pictureUploadDao.get(pictureId);
		pictureUploadDao.remove(picture);
	}
	
	/**
	 * 删除批复文件图片
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.ATTACHMENT_UPLOAD_CN, opType=OperationType.Del, opText="删除图片")
	public void removePiFuFile(Long pictureId) {
		FileUpload picture = fileUploadDao.get(pictureId);
		fileUploadDao.remove(picture);
	}
}
