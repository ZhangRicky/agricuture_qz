package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @描述：项目编码、文件号、资金年度、项目名称、批复文号、财政资金建设规模及内容、实施单位、项目负责人、到县资金
 * @author mars
 */
@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value={"startIndex", "pageSize"})
@DynamicUpdate
@SelectBeforeUpdate
public class Project implements Serializable{
	
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 文号
	 */
	private String referenceNumber;
	/**
	 * 项目唯一编号
	 */
	private Long projectNumber;
	/**
	 * 资金年度
	 */
	private String fundYear;
	/**
	 * 地州名
	 */
	private String city;
	/**
	 * 县名
	 */
	private String countyLevelCity;
	/**
	 * 乡镇名
	 */
	private String town;
	/**
	 * 村名
	 */
	private String village;
	/**
	 * 专项名称
	 */
	private String subjectName;
	/**
	 * 资金类明细
	 */
	private String fundType;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 批复文号
	 */
	private String approvalNumber;
	/**
	 * 总资金(万元)
	 */
	private String totalFund;
	/**
	 * 财政资金(万元)
	 */
	private String financeFund;
	/**
	 * 自筹资金(万元)
	 */
	private String selfFinancing;
	/**
	 * 整合资金(万元)
	 */
	private String integrateFund;
	/**
	 * 覆盖农户数(户)
	 */
	private String coveredFarmerNumber;
	/**
	 * 覆盖人数(人)
	 */
	private String coveringNumber;
	/**
	 * 扶持贫困农户数(户)
	 */
	private String povertyStrickenFarmerNumber;
	/**
	 * 扶持贫困人口数(人)
	 */
	private String povertyStrickenPeopleNumber;
	/**
	 * 财政资金建设规模及内容
	 */
	private String scaleAndContent;
	/**
	 * 实施单位
	 */
	private String carryOutUnit;
	/**
	 * 项目负责人
	 */
	private String chargePerson;
	/**
	 * 完成期限(月)
	 */
	private String deadline;
	/**
	 * 后补文号
	 */
	private String standbyNumber;
	/**
	 * 到县资金(万元)
	 */
	private String fundToCountry;
	/**
	 * 审核状态：正在备案、备案通过、不通过
	 */
	private String approveState;
	/**
	 * 创建用户ID
	 */
	private String createUser;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 录入状态
	 */
	private String inputStatus;
	/**
	 * 当前审核部门
	 */
	private String approvingDepartment;
	
	private String remark;
	
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 项目类型：1：种养殖类、基础论证类、培训类；2:贴息类；3:其它
	 */
	private int projectType;
	
	/**
	 * 子项目数量
	 */
	private int subProjectNumber;
	
	/**
	 * 开始页
	 */
	@Transient
	private int startIndex = 0;
	
	/**
	 * 每页显示数目
	 */
	@Transient
	private int pageSize = 10;
	
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public Long getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(Long projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String getFundYear() {
		return fundYear;
	}
	public void setFundYear(String fundYear) {
		this.fundYear = fundYear;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountyLevelCity() {
		return countyLevelCity;
	}
	public void setCountyLevelCity(String countyLevelCity) {
		this.countyLevelCity = countyLevelCity;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	public String getTotalFund() {
		return totalFund;
	}
	public void setTotalFund(String totalFund) {
		this.totalFund = totalFund;
	}
	public String getFinanceFund() {
		return financeFund;
	}
	public void setFinanceFund(String financeFund) {
		this.financeFund = financeFund;
	}
	public String getSelfFinancing() {
		return selfFinancing;
	}
	public void setSelfFinancing(String selfFinancing) {
		this.selfFinancing = selfFinancing;
	}
	public String getIntegrateFund() {
		return integrateFund;
	}
	public void setIntegrateFund(String integrateFund) {
		this.integrateFund = integrateFund;
	}
	public String getCoveredFarmerNumber() {
		return coveredFarmerNumber;
	}
	public void setCoveredFarmerNumber(String coveredFarmerNumber) {
		this.coveredFarmerNumber = coveredFarmerNumber;
	}
	public String getCoveringNumber() {
		return coveringNumber;
	}
	public void setCoveringNumber(String coveringNumber) {
		this.coveringNumber = coveringNumber;
	}
	public String getPovertyStrickenFarmerNumber() {
		return povertyStrickenFarmerNumber;
	}
	public void setPovertyStrickenFarmerNumber(String povertyStrickenFarmerNumber) {
		this.povertyStrickenFarmerNumber = povertyStrickenFarmerNumber;
	}
	public String getPovertyStrickenPeopleNumber() {
		return povertyStrickenPeopleNumber;
	}
	public void setPovertyStrickenPeopleNumber(String povertyStrickenPeopleNumber) {
		this.povertyStrickenPeopleNumber = povertyStrickenPeopleNumber;
	}
	public String getScaleAndContent() {
		return scaleAndContent;
	}
	public void setScaleAndContent(String scaleAndContent) {
		this.scaleAndContent = scaleAndContent;
	}
	public String getCarryOutUnit() {
		return carryOutUnit;
	}
	public void setCarryOutUnit(String carryOutUnit) {
		this.carryOutUnit = carryOutUnit;
	}
	public String getChargePerson() {
		return chargePerson;
	}
	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getStandbyNumber() {
		return standbyNumber;
	}
	public void setStandbyNumber(String standbyNumber) {
		this.standbyNumber = standbyNumber;
	}
	public String getFundToCountry() {
		return fundToCountry;
	}
	public void setFundToCountry(String fundToCountry) {
		this.fundToCountry = fundToCountry;
	}
	public String getApproveState() {
		return approveState;
	}
	public void setApproveState(String approveState) {
		this.approveState = approveState;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getInputStatus() {
		return inputStatus;
	}
	public void setInputStatus(String inputStatus) {
		this.inputStatus = inputStatus;
	}
	public String getApprovingDepartment() {
		return approvingDepartment;
	}
	public void setApprovingDepartment(String approvingDepartment) {
		this.approvingDepartment = approvingDepartment;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getProjectType() {
		return projectType;
	}
	public void setProjectType(int projectType) {
		this.projectType = projectType;
	}
	
	public int getSubProjectNumber() {
		return subProjectNumber;
	}
	public void setSubProjectNumber(int subProjectNumber) {
		this.subProjectNumber = subProjectNumber;
	}
	@Override
	public String toString() {
		return "Project [id=" + id + ", referenceNumber=" + referenceNumber + ", projectNumber=" + projectNumber
				+ ", fundYear=" + fundYear + ", city=" + city + ", countyLevelCity=" + countyLevelCity + ", town="
				+ town + ", village=" + village + ", subjectName=" + subjectName + ", fundType=" + fundType
				+ ", projectName=" + projectName + ", approvalNumber=" + approvalNumber + ", totalFund=" + totalFund
				+ ", financeFund=" + financeFund + ", selfFinancing=" + selfFinancing + ", integrateFund="
				+ integrateFund + ", coveredFarmerNumber=" + coveredFarmerNumber + ", coveringNumber=" + coveringNumber
				+ ", povertyStrickenFarmerNumber=" + povertyStrickenFarmerNumber + ", povertyStrickenPeopleNumber="
				+ povertyStrickenPeopleNumber + ", scaleAndContent=" + scaleAndContent + ", carryOutUnit="
				+ carryOutUnit + ", chargePerson=" + chargePerson + ", deadline=" + deadline + ", standbyNumber="
				+ standbyNumber + ", fundToCountry=" + fundToCountry + ", approveState=" + approveState
				+ ", createUser=" + createUser + ", createTime=" + createTime + ", inputStatus=" + inputStatus
				+ ", approvingDepartment=" + approvingDepartment + ", remark=" + remark + ", projectType=" + projectType
				+ ", subProjectNumber=" + subProjectNumber + ", startIndex=" + startIndex + ", pageSize=" + pageSize
				+ "]";
	}
	
}
