package com.chenghui.agriculture.service.projectManage;

import java.util.List;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.Finance;
import com.chenghui.agriculture.service.GenericService;

/**
 * 财务信息Service
 * @author Ricky
 * @version 1.0
 */
public interface FinanceService extends GenericService<Finance,Long> {
	/**
	 * 添加财务信息
	 * @param finance
	 */
	Long addFinance(Finance finance);
	
	
	/**
	 * 删除财务信息
	 */
	int removeFinance(Long financeId);
	
	/**
	 * 分页查询财务信息
	 */
	PaginationSupport<Finance> findSubProjectListForPage(Finance f, int limit,int start, ShiroUser shiroUser);
	
	/**
	 * 更新财务信息
	 */
	void updateFinance(Finance finace);
	
	/**
	 * 根据项目ID查询
	 */
	List<Finance> findBySubProjectId(Long tid);
}

