package com.chenghui.agriculture.service.projectManage;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.SubProject;
import com.chenghui.agriculture.service.GenericService;

/**
 * 子项目业务接口service
 * @author LLJ
 * @version V1.0
 */
public interface SubProjectService extends GenericService<SubProject, Long>{
	
	/**
	 * 分页查询子项目
	 * @param subProject
	 * @param limit
	 * @param start
	 * @param shiroUser
	 * @return
	 */
	PaginationSupport<SubProject> findSubProjectListForPage(String xxnr, int limit,int start, ShiroUser shiroUser);
	
	File generateTemplateRules(File templateFile, ShiroUser shiroUser);
	
	/**
	 * 添加子项目
	 * @param subProject
	 * @return
	 */
	Long addSubProject(SubProject subProject);
	
	/**
	 * 删除子项目
	 * @param id
	 */
	void removeByID(Long id);
	
	/**
	 * 修改子项目
	 * @param subProject
	 * @return
	 */
	SubProject updateSubProjectInfo(SubProject subProject,Long subPid);
	
	/**
	 * 根据id查询子项目详情
	 * @param id
	 * @return
	 */
	SubProject findSubProjectById(Long id);
	
	/**
	 * 根据pId查询子项目
	 * @param pId
	 * @return
	 */
	List<SubProject> findSubProjectsByPId(Long pId);
	
	/**
	 * 保存文件容到数据库
	 * @param file
	 * @return Long
	 * @throws 
	 */
	void subProjectFile(MultipartFile file, String rootPath, ShiroUser shiroUser,SubProject subProject) throws BusinessServiceException;
	
	
	/**
	 * 将财务比率写入子项目记录
	 * @param sum
	 */
	void addFinancebiLv(SubProject s);
	
	/**
	 * 更新项目的比率值
	 */
	void updateSubproject(Long subId,String bzAcount);
	
	/**
	 * 根据主项目id查询所有子项目
	 * @param Project
	 * @return
	 */
	PaginationSupport<SubProject> findSubProjectListByPid(String xxnr, int limit, int start,
			ShiroUser shiroUser,Long projectId);
}
