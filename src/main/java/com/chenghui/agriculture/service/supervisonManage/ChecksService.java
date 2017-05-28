package com.chenghui.agriculture.service.supervisonManage;

import java.util.List;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.Checks;
import com.chenghui.agriculture.service.GenericService;

/**
 * 验收记录业务接口service
 * @author LLJ
 * @version V1.0
 */
public interface ChecksService extends GenericService<Checks, Long>{
	
	/**
	 * 添加验收记录
	 * @param checks
	 * @param shiroUser
	 * @return
	 */
	Long addChecks(Checks checks);
	
	/**
	 * 修改验收记录
	 * @param checks
	 * @return Checks
	 */
	Checks updateChecks(Checks checks);
	
	/**
	 * 删除验收记录
	 * @param id
	 */
	void removeChecksByID(Long id);
	/**
	 * 查询验收记录
	 * @param id
	 * @return
	 */
	Checks findChecksById(Long id);
	/**
	 * 根据子项目sId查询验收记录
	 * @param sId
	 * @return
	 */
	List<Checks> findChecksBySId(Long sId);
	List<Checks> appFindChecksBySId(Long sId);
	/**
	 * 根据子项目sId查询验收记录,翻页
	 * @param sId
	 * @return
	 */
	PaginationSupport<Checks> findChecksListForPage(Checks checks, int limit,int start, ShiroUser shiroUser);
}
