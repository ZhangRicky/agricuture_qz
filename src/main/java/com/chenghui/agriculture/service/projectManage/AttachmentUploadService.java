package com.chenghui.agriculture.service.projectManage;

import com.chenghui.agriculture.model.FileUpload;
import com.chenghui.agriculture.model.PictureUpload;
import com.chenghui.agriculture.model.Project;
import com.chenghui.agriculture.service.GenericService;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;


public interface AttachmentUploadService extends GenericService<Project,Long>{

	PaginationSupport<Project> findProjectListForPage(Project project, int limit,int start, ShiroUser shiroUser);
	
	int projectImport(MultipartFile file,Long projectId,String rootPath, ShiroUser shiroUser ) throws BusinessServiceException;
	
	int fileImport(MultipartFile file,Long projectId,String rootPath, ShiroUser shiroUser ) throws BusinessServiceException;
	
	List<PictureUpload> getAllPicture(Long projectId);
	
	List<FileUpload> getAllFile(Long projectId);
	
	List<Project> findProjectDetailListForPage(Long id, ShiroUser shiroUser);
	
	void removeByID(Long pictureId);
	
	void removePiFuFile(Long pictureId);
}
