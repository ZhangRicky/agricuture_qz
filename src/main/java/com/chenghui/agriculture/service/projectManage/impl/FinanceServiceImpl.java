package com.chenghui.agriculture.service.projectManage.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.projectManage.FinanceDao;
import com.chenghui.agriculture.model.Finance;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.projectManage.FinanceService;

/**
 * 财务信息Service实现
 * @author Ricky
 * @version 1.0
 */
@Service("financeService")
public class FinanceServiceImpl extends GenericServiceImpl<Finance, Long> implements FinanceService {
	@Autowired
	private FinanceDao financeDao;
	
	/**
	 * 添加财务信息
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.FINANCE_MANAGEMENT_CN, opType=OperationType.Add, opText="添加财务记录")
	public Long addFinance(Finance finance) {
		if(financeDao.findByCertificateNum(finance.getCertificateNum()) != null){
			throw new BusinessServiceException("系统中已存在凭证号为"+finance.getCertificateNum()+" 的财务信息");
		}
		try {
			financeDao.add(finance);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessServiceException(e.getLocalizedMessage());
		}
		return finance.getId();
	}
	
	/**
	 * 删出项目的财务信息
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.FINANCE_MANAGEMENT_CN, opType=OperationType.Del, opText="删除财务记录")
	public int removeFinance(Long financeId) {
		Finance f = financeDao.get(financeId);
		financeDao.remove(f);	
		return 1;
	}

	/*分页查询财务信息*/
	@Override
	public PaginationSupport<Finance> findSubProjectListForPage(Finance f, int limit, int start,
			ShiroUser shiroUser) {
		return financeDao.findSubProjectPage(f, limit, start, shiroUser);
		
	}
	
	/**
	 * 修改财务信息
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.FINANCE_MANAGEMENT_CN, opType=OperationType.Update, opText="修改财务记录")
	public void updateFinance(Finance finace) {
		Finance f =  financeDao.get(finace.getId());
//		if(f.getCertificateNum().equals(finace.getCertificateNum())){
//			if(financeDao.findByExample(finace) != null){
//				throw new BusinessServiceException("修改信息失败，凭证号重复！");
//			}
//		}

		if(f.getAccount()== null){
			f.setCertificateNum(finace.getCertificateNum());
			f.setAppropriation(finace.getAppropriation());
			f.setBk_date(finace.getBk_date());
			f.setBk_user(finace.getBk_user());
			f.setRemark(finace.getRemark());
		}
		if(f.getAppropriation() == null){
			f.setCertificateNum(finace.getCertificateNum());		
			f.setAccount(finace.getAccount());
			f.setBz_user(finace.getBz_user());
			f.setBz_date(finace.getBz_date());	
			f.setRemark(finace.getRemark());
		}
		try{
			financeDao.update(f);
		}catch(Exception e){
			throw new BusinessServiceException("修改财务信息失败！");
		}
		
	}

	@Override
	public List<Finance> findBySubProjectId(Long tid) {
		List<Finance> lists = financeDao.findBySubProjectId(tid);
		return lists;
	}

	
	
}
