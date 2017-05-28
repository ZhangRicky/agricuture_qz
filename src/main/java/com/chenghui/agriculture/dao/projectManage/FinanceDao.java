package com.chenghui.agriculture.dao.projectManage;

import java.util.List;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Finance;

/**
 * 财务记录Dao
 * @author Ricky
 * @version 1.0
 */
public interface FinanceDao extends GenericDao<Finance, Long> {

	
	/**
	 * 分页查询财务信息
	 */
	PaginationSupport<Finance> findSubProjectPage(Finance f, int limit,int start, ShiroUser shiroUser);

	/**
	 * 根据凭证号查询财务信息
	 */
	Finance findByCertificateNum(String str);
	
	/**
	 * 根据项目id查询报账金额
	 */
	List<Finance> findBySubProjectId(Long tid);
	List<Finance> appFindBySubProjectId(Long tid);
}
