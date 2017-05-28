package com.chenghui.agriculture.service.projectManage;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.ProjectImport;
import com.chenghui.agriculture.model.ProjectImportDetail;
import com.chenghui.agriculture.model.Template;
import com.chenghui.agriculture.service.GenericService;

/**
 * 项目导入Service
 * @author yudq
 * @version V1.0
 * @Date 2016-03-02 15:51:00
 */
public interface ProjectImportService extends GenericService<ProjectImport,Long>{

	PaginationSupport<ProjectImport> findProjectImportListForPage(ProjectImport ProjectImport, int limit,int start, ShiroUser shiroUser);
	
	PaginationSupport<ProjectImportDetail> findProjectImportDetailListForPage(ProjectImportDetail ProjectImportDetail, int limit,int start, ShiroUser shiroUser);

	Long add(ProjectImport ProjectImport, ShiroUser shiroUser) throws BusinessServiceException;
	
	List<Template> findTemplateByType(int templateType);
	
	/**
	 * 保存文件中的内容到数据库
	 * @param file
	 * @return Long
	 * @throws 
	 */
	int projectImport(MultipartFile file, String rootPath, ShiroUser shiroUser ) throws BusinessServiceException;
	
	File generateTemplateRules(File templateFile, ShiroUser shiroUser);
	
}
