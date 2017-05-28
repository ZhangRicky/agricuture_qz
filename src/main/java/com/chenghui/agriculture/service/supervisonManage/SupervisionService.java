package com.chenghui.agriculture.service.supervisonManage;

import java.util.List;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.Supervision;
import com.chenghui.agriculture.service.GenericService;

/**
 * 监管记录业务接口service
 * @author LLJ
 * @version V1.0
 */
public interface SupervisionService extends GenericService<Supervision, Long>{
	
	/**
	 * 添加监管记录
	 * @param supervision
	 * @param shiroUser
	 * @return
	 */
	Long addSupervision(Supervision supervision);
	
	/**
	 * 修改监管记录
	 * @param supervision
	 * @return Supervision
	 */
	Supervision updateSupervision(Supervision supervision);
	
	/**
	 * 删除监管记录
	 * @param id
	 */
	void removeSupervisionByID(Long id);
	/**
	 * 查询监管记录
	 * @param id
	 * @return
	 */
	Supervision findSupervisionById(Long id);
	/**
	 * 根据子项目pId查询监管记录
	 * @param pId
	 * @return
	 */
	List<Supervision> findSupervisionByPId(Long pId);
	List<Supervision> appFindSupervisionByPId(Long pId);
	/**
	 * 根据子项目sId查询监管记录,翻页
	 * @param sId
	 * @return
	 */
	PaginationSupport<Supervision> findSupervisionListForPage(Supervision supervision, int limit,int start, ShiroUser shiroUser);
}
