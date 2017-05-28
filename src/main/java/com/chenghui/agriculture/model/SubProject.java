package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import com.fasterxml.jackson.annotation.JsonAutoDetect;


@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@DynamicUpdate
@SelectBeforeUpdate
public class SubProject implements Serializable{
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 子项目文号
	 */
	private String subProjectNumber;            
	
	/**
	 * 子项目名称
	 */
	private String subProjectName;              
	
	/**
	 * 总资金
	 */
	private Double totalCapital;                
	
	/**
	 * 实施单位
	 */
	private String implementationUnit;         
	
	/**
	 * 子项目地区
	 */
	private String subProjectArea;              
	
	/**
	 * 农户名称
	 */
	private String farmerName;                  
	
	/**
	 * 父项目
	 */
	@ManyToOne
	private Project project;	              
	
	/**
	 * 建设规模及内容
	 */
	private String projectScaleAndContent;      
	
	/**
	 * 建设方式;0:先建后款;1：先款后建
	 */
	private Integer constructionMode;  			
	
	/**
	 * 应报账
	 */
	private Double shouldAccount;     			
	
	/**
	 * 结转
	 */
	private Double jz;                			
	
	/**
	 * 结余
	 */
	private Double jy;                			
	
	/**
	 * 报账率
	 */
	private String reimbursementRate; 			
	
	/**
	 * 验收状态;0:未验收;1：验收通过;2:验收不通过
	 */
	private int checkStatus;       			
	
	/**
	 * 财务报账率
	 */
	private String financebiLv;				  
	
	/**
	 * 数据状态;0:表示正常;1：表示被删除
	 */
	private int flag;                      

	/**
	 * 用于页面返回到服务器值
	 */
	@Transient
	private Long projectId;                     
	
	/**
	 * 模糊查询字段        
	 */
	@Transient
	private String xxnr;                        
	@Transient
	private String projectScale;                        
	@Transient
	private String projectContent;
	/**
	 * 项目资金年度
	 */
	@Transient
	private String fundYear;                         //项目资金年度
	/**
	 * 项目专项名称
	 */
	@Transient
	private String subjectName;
	/**
	 * 完成期限(月)
	 */
	@Transient
	private Integer deadline;                        //完成期限(月)
	/**
	 * 项目负责人
	 */
	@Transient
	private String chargePerson;                     //项目负责人
	/**
	 * 审核状态：0:正在备案;1:备案通过;2:不通过
	 */
	@Transient
	private String approveState;                    //审核状态：0:正在备案;1:备案通过;2:不通过
	/**
	 * 覆盖农户数(户)
	 */
	@Transient
	private Integer coveredFarmerNumber;             //覆盖农户数(户)
	/**
	 * 覆盖人数(人)
	 */
	@Transient
	private Integer coveringNumber;                  //覆盖人数(人)
	/**
	 * 扶持贫困农户数(户)
	 */
	@Transient
	private Integer povertyStrickenFarmerNumber;     //扶持贫困农户数(户)
	/**
	 * 扶持贫困人口数(人)
	 */
	@Transient
	private Integer povertyStrickenPeopleNumber;     //扶持贫困人口数(人)
	/**
	 * 项目总资金(万元)
	 */
	private Integer povertyGeneralFarmer;			 //扶持一般农户
	private Integer povertyLowIncomeFarmer;          //扶持低收入困难农户
	private Integer povertyGeneralPeople;			 //扶持一般人口
	public Integer getPovertyGeneralFarmer() {
		return povertyGeneralFarmer;
	}

	public void setPovertyGeneralFarmer(Integer povertyGeneralFarmer) {
		this.povertyGeneralFarmer = povertyGeneralFarmer;
	}

	public Integer getPovertyLowIncomeFarmer() {
		return povertyLowIncomeFarmer;
	}

	public void setPovertyLowIncomeFarmer(Integer povertyLowIncomeFarmer) {
		this.povertyLowIncomeFarmer = povertyLowIncomeFarmer;
	}

	public Integer getPovertyGeneralPeople() {
		return povertyGeneralPeople;
	}

	public void setPovertyGeneralPeople(Integer povertyGeneralPeople) {
		this.povertyGeneralPeople = povertyGeneralPeople;
	}

	public Integer getPovertyLowIncomePeople() {
		return povertyLowIncomePeople;
	}

	public void setPovertyLowIncomePeople(Integer povertyLowIncomePeople) {
		this.povertyLowIncomePeople = povertyLowIncomePeople;
	}

	private Integer povertyLowIncomePeople;          //扶持低收入困难人口
	@Transient
	private Double totalFund;                        //项目总资金(万元)
	/**
	 * 项目财政资金
	 */
	@Transient
	private Double financeFund;                      //项目财政资金
	/**
	 * 项目自筹资金
	 */
	@Transient
	private Double selfFinancing;                    //项目自筹资金
	/**
	 * 项目整合资金
	 */
	@Transient
	private Double integrateFund;                    //项目整合资金
	/**
	 * 到县资金(万元)
	 */
	@Transient
	private Double fundToCountry;                    //到县资金(万元)
	/**
	 * 总报账金额
	 */
	@Transient
	private Double bzTotal;					 //总报账金额
	/**
	 * 总拨款金额
	 */
	@Transient
	private Double bkTotal;					 //总拨款金额
	/**
	 * 结余资金
	 */
	@Transient
	private Double balance;					 //结余资金
	/**
	 * 结转资金
	 */
	@Transient
	private Double balance_forword;			 //结转资金
	/**
	 * 项目进度
	 */
	@Transient
	private String projectProcess;					 //项目进度
	/**
	 * 项目类型
	 */
	@Transient
	private Integer projectType;					 //项目类型
	
	
	/**
	 * 上传文件路径
	 */
	private String path;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubProjectNumber() {
		return subProjectNumber;
	}

	public void setSubProjectNumber(String subProjectNumber) {
		this.subProjectNumber = subProjectNumber;
	}

	public String getSubProjectName() {
		return subProjectName;
	}

	public void setSubProjectName(String subProjectName) {
		this.subProjectName = subProjectName;
	}

	public Double getTotalCapital() {
		return totalCapital;
	}

	public void setTotalCapital(Double totalCapital) {
		this.totalCapital = totalCapital;
	}

	public String getImplementationUnit() {
		return implementationUnit;
	}

	public void setImplementationUnit(String implementationUnit) {
		this.implementationUnit = implementationUnit;
	}

	public String getSubProjectArea() {
		return subProjectArea;
	}

	public void setSubProjectArea(String subProjectArea) {
		this.subProjectArea = subProjectArea;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getProjectScaleAndContent() {
		return projectScaleAndContent;
	}

	public void setProjectScaleAndContent(String projectScaleAndContent) {
		this.projectScaleAndContent = projectScaleAndContent;
	}

	public Integer getConstructionMode() {
		return constructionMode;
	}

	public void setConstructionMode(Integer constructionMode) {
		this.constructionMode = constructionMode;
	}

	public Double getShouldAccount() {
		return shouldAccount;
	}

	public void setShouldAccount(Double shouldAccount) {
		this.shouldAccount = shouldAccount;
	}

	public Double getJz() {
		return jz;
	}

	public void setJz(Double jz) {
		this.jz = jz;
	}

	public Double getJy() {
		return jy;
	}

	public void setJy(Double jy) {
		this.jy = jy;
	}

	public String getReimbursementRate() {
		return reimbursementRate;
	}

	public void setReimbursementRate(String reimbursementRate) {
		this.reimbursementRate = reimbursementRate;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getFinancebiLv() {
		return financebiLv;
	}

	public void setFinancebiLv(String financebiLv) {
		this.financebiLv = financebiLv;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getXxnr() {
		return xxnr;
	}

	public void setXxnr(String xxnr) {
		this.xxnr = xxnr;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getProjectScale() {
		return projectScale;
	}

	public void setProjectScale(String projectScale) {
		this.projectScale = projectScale;
	}

	public String getProjectContent() {
		return projectContent;
	}

	public void setProjectContent(String projectContent) {
		this.projectContent = projectContent;
	}

	public String getFundYear() {
		return fundYear;
	}

	public void setFundYear(String fundYear) {
		this.fundYear = fundYear;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getDeadline() {
		return deadline;
	}

	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}

	public String getChargePerson() {
		return chargePerson;
	}

	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}

	public String getApproveState() {
		return approveState;
	}

	public void setApproveState(String approveState) {
		this.approveState = approveState;
	}

	public Integer getCoveredFarmerNumber() {
		return coveredFarmerNumber;
	}

	public void setCoveredFarmerNumber(Integer coveredFarmerNumber) {
		this.coveredFarmerNumber = coveredFarmerNumber;
	}

	public Integer getCoveringNumber() {
		return coveringNumber;
	}

	public void setCoveringNumber(Integer coveringNumber) {
		this.coveringNumber = coveringNumber;
	}

	public Integer getPovertyStrickenFarmerNumber() {
		return povertyStrickenFarmerNumber;
	}

	public void setPovertyStrickenFarmerNumber(Integer povertyStrickenFarmerNumber) {
		this.povertyStrickenFarmerNumber = povertyStrickenFarmerNumber;
	}

	public Integer getPovertyStrickenPeopleNumber() {
		return povertyStrickenPeopleNumber;
	}

	public void setPovertyStrickenPeopleNumber(Integer povertyStrickenPeopleNumber) {
		this.povertyStrickenPeopleNumber = povertyStrickenPeopleNumber;
	}

	public Double getTotalFund() {
		return totalFund;
	}

	public void setTotalFund(Double totalFund) {
		this.totalFund = totalFund;
	}

	public Double getFinanceFund() {
		return financeFund;
	}

	public void setFinanceFund(Double financeFund) {
		this.financeFund = financeFund;
	}

	public Double getSelfFinancing() {
		return selfFinancing;
	}

	public void setSelfFinancing(Double selfFinancing) {
		this.selfFinancing = selfFinancing;
	}

	public Double getIntegrateFund() {
		return integrateFund;
	}

	public void setIntegrateFund(Double integrateFund) {
		this.integrateFund = integrateFund;
	}

	public Double getFundToCountry() {
		return fundToCountry;
	}

	public void setFundToCountry(Double fundToCountry) {
		this.fundToCountry = fundToCountry;
	}

	public Double getBzTotal() {
		return bzTotal;
	}

	public void setBzTotal(Double bzTotal) {
		this.bzTotal = bzTotal;
	}

	public Double getBkTotal() {
		return bkTotal;
	}

	public void setBkTotal(Double bkTotal) {
		this.bkTotal = bkTotal;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getBalance_forword() {
		return balance_forword;
	}

	public void setBalance_forword(Double balance_forword) {
		this.balance_forword = balance_forword;
	}

	public String getProjectProcess() {
		return projectProcess;
	}

	public void setProjectProcess(String projectProcess) {
		this.projectProcess = projectProcess;
	}

	public Integer getProjectType() {
		return projectType;
	}

	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}

	@Override
	public String toString() {
		return "SubProject [id=" + id + ", subProjectNumber=" + subProjectNumber + ", subProjectName=" + subProjectName
				+ ", totalCapital=" + totalCapital + ", implementationUnit=" + implementationUnit + ", subProjectArea="
				+ subProjectArea + ", farmerName=" + farmerName + ", project=" + project + ", projectScaleAndContent="
				+ projectScaleAndContent + ", constructionMode=" + constructionMode + ", shouldAccount=" + shouldAccount
				+ ", jz=" + jz + ", jy=" + jy + ", reimbursementRate=" + reimbursementRate + ", checkStatus="
				+ checkStatus + ", financebiLv=" + financebiLv + ", flag=" + flag + ", projectId=" + projectId
				+ ", xxnr=" + xxnr + ", path=" + path + "]";
	}                        
	
	
}
