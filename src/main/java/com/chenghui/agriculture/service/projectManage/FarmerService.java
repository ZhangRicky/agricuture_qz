package com.chenghui.agriculture.service.projectManage;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.Farmer;
import com.chenghui.agriculture.service.GenericService;

/**
 * 农民业务服务Service
 * @version V1.0
 */
public interface FarmerService extends GenericService<Farmer,Long>{
	
	/**
	 * 保存文件中的内容到数据库
	 * @param file
	 * @return Long
	 * @throws 
	 */
	int projectImport(MultipartFile file, String rootPath, Long subProject_id,ShiroUser shiroUser ) throws BusinessServiceException;
	
	PaginationSupport<Farmer> findProjectImportDetailListForPage(Long projectId, int limit,int start, ShiroUser shiroUser);
	
	void removeByID(Long farmerId);
	
	/**
	 * 查询所有农民，用于手机端下拉列表数据填充
	 */
	List<Farmer> findAll();
	
	/**
	 * 根据项目Id查询农民信息，用于手机端下拉列表数据填充
	 */
	List<Farmer> findFarmerByPId(Long pId);
	List<Farmer> appFindFarmerByPId(Long pId);
	
	/**
	 * 添加农民记录
	 * @param farmer
	 * @param shiroUser
	 * @return
	 */
	Long addFarmer(Farmer farmer);
		
	/**
	 * 修改农民记录
	 * @param farmer
	 * @return Farmer
	 */
	Farmer updateFarmer(Farmer farmer);
	
	/**
	 * 修改导入农户信息记录
	 * @param farmer
	 * @return Farmer
	 */
	Farmer updateFarmerInfo(Farmer farmer);
	
	/**
	 * 删除农民记录
	 * @param id
	 */
	void removeFarmerByID(Long id);
	
	
}
