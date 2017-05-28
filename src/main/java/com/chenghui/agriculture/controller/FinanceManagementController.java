package com.chenghui.agriculture.controller;


import java.math.BigDecimal;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.hibernate.AssertionFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.exception.RestException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.RestResult;
import com.chenghui.agriculture.model.Finance;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.service.projectManage.FinanceService;
import com.chenghui.agriculture.service.projectManage.ProjectsService;




/**
 * 财务管理
 * @author zhanggx
 * @version 1.0
 */
@RequestMapping("/financeManagement")
@RestController
public class FinanceManagementController {
	private double updateAccount;	//修改之前的[报账金额];
	private double updateAppropriation;	//修改之前的[拨款金额]
	
	private final static Logger logger = LoggerFactory.getLogger(FinanceManagementController.class); 
	
	@Autowired
	private FinanceService financeService;
	
	@Autowired
	private ProjectsService projectsService;
	
	
	/**
	 * 根据项目ID查询财务记录
	 * @param projects_id	项目ID
	 * @param page			页数
	 * @param pageSize		每页条数
	 * @return		财务数据集合
	 */
	@RequestMapping(value = "/selectById/{projects_id:\\d+}", method = RequestMethod.GET)
	public PaginationSupport<Finance> findByFinance(@PathVariable Long projects_id,
								@RequestParam("page") String page,
								@RequestParam("pageSize") String pageSize){
	
		Finance f = new Finance();
		f.setProjects_id(projects_id);
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		PaginationSupport<Finance> pagination = financeService.findSubProjectListForPage(f, Integer.parseInt(pageSize), Integer.parseInt(page), shiroUser);		
		return pagination;
	}
	
	/**
	 * [添加]财务记录
	 * @param finance	财务数据
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult addFinance(@RequestBody Finance finance){
		
		try {
			int i = JudgementByFinance(finance);
			if(i==1){
				return new RestResult(1, "拨款金额超过了可拨款的金额");
			}else if(i==2){
				return new RestResult(2, "报账金额超过了可报账金额");
			}else{
				financeService.addFinance(finance);
				updateByProjectFinacne(finance);
				return new RestResult(0, "添加财务信息成功!");
			}
		} catch (BusinessServiceException busyEx) {
			return new RestResult(-1, busyEx.getLocalizedMessage());
		} catch (AssertionFailure e) {
			
			return new RestResult(-1, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
		}
	}
	
	/**
	 * [修改]财务数据
	 * @param finance	
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult updateUser(@RequestBody Finance finance) {
		Finance f = financeService.get(finance.getId());
		updateAccount = f.getAccount() == null ? 0 : f.getAccount();
		updateAppropriation = f.getAppropriation() == null ? 0 : f.getAppropriation();
		try {
			int i = JudgementByFinance(finance);
			if(i==1){
				return new RestResult(1, "拨款金额超过了可拨款的金额");
			}else if(i==2){
				return new RestResult(2, "报账金额超过了可报账金额");
			}else{	
				financeService.updateFinance(finance);
				updateByProjectFinacne(finance);
				return new RestResult(0, "更新成功!");
			}
		} catch (BusinessServiceException busyEx) {
			return new RestResult(-1, busyEx.getLocalizedMessage());
		} catch (AssertionFailure e) {
			return new RestResult(-1, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
		}
	}
	
	
	/**
	 * [删除]财务数据
	 * @param financeId		财务ID
	 * @param bzAcount		删除的报账的金额
	 * @param subId			项目ID
	 * @return
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public RestResult removeFinance(@RequestBody Finance finance){	
		try {
				
			int i = financeService.removeFinance(finance.getId());	
			if(i==1){
				updateByTotalBKBZ(finance);	//修改项目中[拨款][报账][报账率]的值		
			}
			return new RestResult(0, "删除财务信息成功!");
		} catch (BusinessServiceException busyEx) {
			return new RestResult(-1, busyEx.getLocalizedMessage());
		} catch (AssertionFailure e) {
			return new RestResult(-1, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
		}
	}
	
	
	/**
	 * 报账[确认]按钮，添加‘报账率’，‘报账金额’到项目数据中
	 * @param subId		项目ID
	 */
	@RequestMapping(value = "/addByFinancesToSubProject", method = RequestMethod.POST)
	public void findBySubProjectId(@RequestBody Finance finance){
		if(finance.getId() == null){ 
			//添加财务记录时，操作项目中的财务数据
			List<Finance> lists = financeService.findBySubProjectId(finance.getProjects_id());
			Double[] its = null;
			double sum = 0;	//报账的值
			if(lists.size() != 0){
				its = new Double[lists.size()];
				for(int i = 0; i<lists.size();i++){
					Finance f = lists.get(i);
					its[i] =(f.getAccount() == null ? 0 : f.getAccount());
				}
				for(int j =0;j<its.length;j++){
					sum = sum + its[j];
				}
			}else{
				sum = 0.0;
			}
			Projects p = new Projects();
			p.setId(finance.getProjects_id());
			p.setFinancebiLv(String.valueOf(sum));
			p.setBzTotal(sum);		//报账金额 = 报账率的金额
			projectsService.addFinancebiLv(p);
		}
	}
	
	/**
	 * 拨款[确认]按钮，添加‘拨款金额’到项目数据中
	 */
	@RequestMapping(value = "/addByProjectsFinance", method = RequestMethod.POST)
	public void addByProjectsFinance(@RequestBody Finance finance){
		if(finance.getId() == null ){
			List<Finance> lists = financeService.findBySubProjectId(finance.getProjects_id());
			Double[] its = null;
			Double sum = 0.0;	//报账的值
			if(lists.size() != 0){
				its = new Double[lists.size()];
				for(int i = 0; i<lists.size();i++){
					Finance f = lists.get(i);
					its[i] =(f.getAppropriation()==null ? 0 : f.getAppropriation());
				}
				for(int j =0;j<its.length;j++){
					sum = sum + (its[j]==null ? 0 : its[j]) ;
				}
			}else{
				sum = 0.0;
			}
			Projects p = new Projects();
			p.setId(finance.getProjects_id());
			p.setBkTotal(sum);		//报账金额 = 报账率的金额
			projectsService.addByBKTotal(p);
		}
		
	}
	
	/**
	 * 删除财务记录时。更新项目的'【总拨款】/【总报账】【报账率】'
	 */
	public void updateByTotalBKBZ(Finance f){
		Projects pro =projectsService.get(f.getProjects_id());	//根据项目ID查询出项目
		double newBK = 0;	//需要更新的[拨款值]
		BigDecimal bkTotal = new BigDecimal(Double.toString(pro.getBkTotal()));
		BigDecimal bzTotal = new BigDecimal(Double.toString(pro.getBzTotal()));
		if(f.getAccount() == null){
			
			BigDecimal appropriation = new BigDecimal(Double.toString(f.getAppropriation()==null ? 0 :f.getAppropriation()));
			newBK = bkTotal.subtract(appropriation).doubleValue();
			pro.setBkTotal(newBK);
		}
		//更新[报账记录]
		double newBZ = 0;	//需要更新的[报账值]
		if(f.getAppropriation()==null){
			BigDecimal account = new BigDecimal(Double.toString(f.getAccount()==null ? 0: f.getAccount()));
			newBZ =bzTotal.subtract(account).doubleValue();
			pro.setBzTotal(newBZ);				//更新[报账]
			pro.setFinancebiLv(String.valueOf(newBZ));	//更新报账率
		}
		projectsService.updateByTotalBKBZ(pro);
	}
	
	/**
	 * 【添加】/【更新】财务记录时，对金额进行判断
	 */
	public int  JudgementByFinance(Finance f){
		Projects pro = projectsService.get(f.getProjects_id());
		//已拨款减法计算
		BigDecimal pTotalFund = new BigDecimal(Double.toString(pro.getTotalFund()));
		BigDecimal bk = new BigDecimal(Double.toString(pro.getBkTotal() == null ? 0 : pro.getBkTotal()));
		//已报账减法计算
		BigDecimal bz = new BigDecimal(Double.toString(pro.getBzTotal() == null ? 0 : pro.getBzTotal()));
		//添加财务记录时，判断
		if(f.getId() == null){
			if(f.getAccount() == null){
				//拨款
				if(f.getAppropriation() > (pTotalFund.subtract(bk).doubleValue())){		//可拨款 = 总资金 - 已拨款
					return 1;		//拨款金额超过了可拨款的金额
				}
			}
			if(f.getAppropriation() == null){
				//报账	
				if(f.getAccount() > (bk.subtract(bz).doubleValue())){			//可报账=已拨款 - 已报账
					return 2;		//报账金额超过了可报账金额
				}
			}
		}else{
		//修改财务记录时做判断
			Finance finance = financeService.get(f.getId());
			if(f.getAccount() == null){
				BigDecimal appropriation = new BigDecimal(Double.toString(finance.getAppropriation()));
				//拨款
				if(f.getAppropriation() > ((pTotalFund.subtract(bk).add(appropriation).doubleValue()))){		//可拨款 = 总资金 - 已拨款 + 修改之前的拨款值
					return 1;		//拨款金额超过了可拨款的金额
				}
			}
			if(f.getAppropriation() == null){
				BigDecimal account = new BigDecimal(Double.toString(finance.getAccount()));
				//报账	
				if(f.getAccount() > (bk.subtract(bz).add(account).doubleValue())){			//可报账=已拨款 - 已报账 + 修改之前的报账值
					return 2;		//报账金额超过了可报账金额
				}
			}
		}
		return 0;
	}
	
	/**
	 * 修改财务记录，同时修改项目中的财务记录总值
	 */
	public void updateByProjectFinacne(Finance finance){
		Projects pro =projectsService.get(finance.getProjects_id());
		//Double bkTotal = pro.getBkTotal();	//之前的总拨款
		//Double bzTotal = pro.getBzTotal();	//之前的总报账
		BigDecimal bkTotal = new BigDecimal(Double.toString(pro.getBkTotal()==null?0:pro.getBkTotal()));
		BigDecimal bzTotal = new BigDecimal(Double.toString(pro.getBzTotal()==null?0:pro.getBzTotal()));
		double nowBK = 0;
		double nowBZ = 0;
		
		if(bkTotal == null || bzTotal == null){
			nowBK = finance.getAppropriation()==null ? 0 : finance.getAppropriation();
			nowBZ = finance.getAccount() == null ? 0 : finance.getAccount();
		}else{
			BigDecimal appropriation = new BigDecimal(Double.toString(updateAppropriation));
			BigDecimal account = new BigDecimal(Double.toString(updateAccount));
			BigDecimal nowAppropriation = new BigDecimal(Double.toString(finance.getAppropriation()==null ? 0 : finance.getAppropriation()));
			BigDecimal nowAccount = new BigDecimal(Double.toString(finance.getAccount() == null ? 0 : finance.getAccount()));
			
			nowBK = bkTotal.subtract(appropriation).add(nowAppropriation).doubleValue();	//总报账 = 之前的总拨款 - 修改之前的[报款] + 现在更改的[拨款]
			nowBZ = bzTotal.subtract(account).add(nowAccount).doubleValue();				//总报账 = 之前的总报账 - 修改之前的[报账] + 现在更改的[报账]
		}
		pro.setBkTotal(nowBK);
		pro.setBzTotal(nowBZ);
		projectsService.updateByTotalBKBZ(pro);
	}
	
	
	
	/**
	 * 财务窗口关闭时。更新项目列表中的报账。拨款的统计数据数据
	 */
	@RequestMapping(value = "/findByAccount/{subId:\\d+}", method = RequestMethod.GET)
	public void findByAccount(@PathVariable Long subId ){
		Projects project = projectsService.get(subId);	//查询出项目
		if(project.getLevel() == 1){
			//最子级的项目
			Projects p = projectsService.get(project.getParentId());	//父项目
			List<Projects> list = projectsService.findSubProjectsByParentId(p.getId());
			double financeAccount = 0 ;	//	子项目报账总金额
			double financeaApropriation = 0;	//子项目拨款总金额
			for(Projects pro : list){
				financeAccount += (pro.getBzTotal()== null ? 0 : pro.getBzTotal());
				financeaApropriation += (pro.getBkTotal() == null ? 0 : pro.getBkTotal());
			}
			p.setBkTotal(financeaApropriation);
			p.setBzTotal(financeAccount);
			projectsService.updateByTotal(p);
		}	
	}
	
	
	
	/**
	 * 调项窗口关闭时。更新项目列表中的报账。拨款的统计数据数据
	 */
	@RequestMapping(value = "/findByTotalFiance/{tiaoXiangID:\\d+}", method = RequestMethod.GET)
	public RestResult findByTotalFiance(@PathVariable Long tiaoXiangID ){
		Projects project = projectsService.get(tiaoXiangID);	//查询出项目
		if(project.getLevel() == 1){
			//最子级的项目
			Projects p = projectsService.get(project.getParentId());	//父项目
			List<Projects> list = projectsService.findSubProjectsByParentId(p.getId());
			double financeAccount = 0 ;	//	子项目报账总金额
			double financeaApropriation = 0;	//子项目拨款总金额
			for(Projects pro : list){
				financeAccount += (pro.getBzTotal()== null ? 0 : pro.getBzTotal());
				financeaApropriation += (pro.getBkTotal() == null ? 0 : pro.getBkTotal());
			}
			p.setBkTotal(financeaApropriation);
			p.setBzTotal(financeAccount);
			projectsService.updateByTotal(p);
		}
		return new RestResult("0","操作成功");	
	}
	/**
	 * 填写[结余]资金
	 */
	@RequestMapping(value ="/addByBalance", method = RequestMethod.POST)
	public RestResult addByBalance(@RequestBody Projects projects){
		try{
			Projects pro =projectsService.get(projects.getId());
			pro.setBalance(projects.getBalance());
			projectsService.updateByBalance(pro);
			return new RestResult("0","操作成功");
		}catch(BusinessServiceException busyEx) {
			return new RestResult(-1, busyEx.getLocalizedMessage());
		} catch (AssertionFailure e) {
			return new RestResult(-1, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
		}
	}
	
	
	/**
	 * 【审核】操作：1.更改审核状态，2.清算结转资金 ，3.通过之后。报账率100%
	 * @return 
	 */
	@RequestMapping(value="/approverFinanceByProjects/{projectsID:\\d+}",method=RequestMethod.GET)
	public RestResult approverFinanceByProjects(@PathVariable Long projectsID){
		try{
			projectsService.updateByapproverStatus(projectsID);
			return new RestResult("0", "操作成功");
		}catch(BusinessServiceException busyEx) {
			return new RestResult(-1, busyEx.getLocalizedMessage());
		} catch (AssertionFailure e) {
			return new RestResult(-1, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
		}
	}
}
