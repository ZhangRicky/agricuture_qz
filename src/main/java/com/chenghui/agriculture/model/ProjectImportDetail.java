package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.chenghui.agriculture.core.utils.AreaUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
public class ProjectImportDetail implements Serializable{
	
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 记录所在行号
	 */
	private int rowNumber;
	/**
	 * 导入数据主表
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	private ProjectImport projectImport;
	/**
	 * 文号
	 */
	private String referenceNumber;
	/**
	 * 项目唯一编号
	 */
	private String projectNumber;
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
	private Double totalFund;
	/**
	 * 财政资金(万元)
	 */
	private Double financeFund;
	/**
	 * 自筹资金(万元)
	 */
	private Double selfFinancing;
	/**
	 * 整合资金(万元)
	 */
	private Double integrateFund;
	/**
	 * 扶持总农户数(户)
	 */
	private String coveredFarmerNumber;
	/**
	 * 扶持总人数(人)
	 */
	private String coveringNumber;
	/**
	 * 扶持贫困农户数(户)
	 */
	private String povertyStrickenFarmerNumber;		 //扶持贫困农户
	private String povertyGeneralFarmer;			 //扶持一般农户
//	private String povertyPoorFarmer;               //扶持贫困农户
	private String povertyLowIncomeFarmer;          //扶持低收入困难农户
	/**
	 * 扶持贫困人口数(人)
	 */
	private String povertyStrickenPeopleNumber;		//扶持贫困人口
	private String povertyGeneralPeople;			 //扶持一般人口
//	private String povertyPoorPeople;               //扶持贫困人口
	private String povertyLowIncomePeople;          //扶持低收入困难人口
	
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
	private Double fundToCountry;
	
	public Double getFundToCountry() {
		return fundToCountry;
	}

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
	
	/**
	 * 本条数据验证状态 0:验证通过，-1:验证不通过，代表错误信息，-2：警告信息   在description里给出详细描述
	 */
	private int validateStatus;
	
	/**
	 * 数据描述：验证失败详细描述
	 */
	private String validateDescription;
	
	/**
	 * 导入状态：1：新增，2:修改
	 */
	private int importStatus;
	
	private int repeatStatus;            //重复状态，0：新纪录，1：重复记录并且记录相同，2：重复记录并且记录不同

	private int coverStatus;            //覆盖状态，0：未覆盖，1：已覆盖

	/**
	 * 转换项目明细为项目对象
	 */
	@Transient
	private Projects projects;
	
	public String getPovertyGeneralFarmer() {
		return povertyGeneralFarmer;
	}

	public void setPovertyGeneralFarmer(String povertyGeneralFarmer) {
		this.povertyGeneralFarmer = povertyGeneralFarmer;
	}

//	public String getPovertyPoorFarmer() {
//		return povertyPoorFarmer;
//	}
//
//	public void setPovertyPoorFarmer(String povertyPoorFarmer) {
//		this.povertyPoorFarmer = povertyPoorFarmer;
//	}

	public String getPovertyLowIncomeFarmer() {
		return povertyLowIncomeFarmer;
	}

	public void setPovertyLowIncomeFarmer(String povertyLowIncomeFarmer) {
		this.povertyLowIncomeFarmer = povertyLowIncomeFarmer;
	}

	public String getPovertyGeneralPeople() {
		return povertyGeneralPeople;
	}

	public void setPovertyGeneralPeople(String povertyGeneralPeople) {
		this.povertyGeneralPeople = povertyGeneralPeople;
	}

//	public String getPovertyPoorPeople() {
//		return povertyPoorPeople;
//	}
//
//	public void setPovertyPoorPeople(String povertyPoorPeople) {
//		this.povertyPoorPeople = povertyPoorPeople;
//	}

	public String getPovertyLowIncomePeople() {
		return povertyLowIncomePeople;
	}

	public void setPovertyLowIncomePeople(String povertyLowIncomePeople) {
		this.povertyLowIncomePeople = povertyLowIncomePeople;
	}
	
	public int getCoverStatus() {
		return coverStatus;
	}

	public void setCoverStatus(int coverStatus) {
		this.coverStatus = coverStatus;
	}
	
	public int getRepeatStatus() {
		return repeatStatus;
	}

	public void setRepeatStatus(int repeatStatus) {
		this.repeatStatus = repeatStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public ProjectImport getProjectImport() {
		return projectImport;
	}

	public void setProjectImport(ProjectImport projectImport) {
		this.projectImport = projectImport;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}



//	public Integer getProjectNumber() {
//		return projectNumber;
//	}
//
//	public void setProjectNumber(Integer projectNumber) {
//		this.projectNumber = projectNumber;
//	}

	public String getFundYear() {
		return fundYear;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
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

	public void setFundToCountry(Double fundToCountry) {
		this.fundToCountry = fundToCountry;
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

	public int getValidateStatus() {
		return validateStatus;
	}

	public void setValidateStatus(int validateStatus) {
		this.validateStatus = validateStatus;
	}

	public String getValidateDescription() {
		return validateDescription;
	}

	public void setValidateDescription(String validateDescription) {
		this.validateDescription = validateDescription;
	}

	public int getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(int importStatus) {
		this.importStatus = importStatus;
	}
	
	/**
	 * @return
	 * @since 2016-03-15
	 */
	public Projects getProjects() {
			if (this.importStatus == 1 || projects == null) {
				projects = new Projects();
			} 
			if(this.repeatStatus == 1 || this.repeatStatus == 2){
				projects.setReferenceNumber(this.getReferenceNumber());
				projects.setProjectNumber(this.getProjectNumber()+"");
				projects.setFundYear(this.getFundYear());
				projects.setArea(AreaUtil.getAreaByAreaName(this.getCity(), this.getCountyLevelCity(), this.getTown(), this.getVillage()));
				projects.setCountyLevelCityID(StringUtils.isNotEmpty(this.getCountyLevelCity()) ? AreaUtil.getAreaByAreaName(this.getCity(), this.getCountyLevelCity(), "", "") : null);
				projects.setTownID(StringUtils.isNotEmpty(this.getTown()) ? AreaUtil.getAreaByAreaName(this.getCity(), this.getCountyLevelCity(), this.getTown(), "") : null);
				projects.setVillageID(StringUtils.isNotEmpty(this.getVillage()) ? AreaUtil.getAreaByAreaName(this.getCity(), this.getCountyLevelCity(), this.getTown(), this.getVillage()) : null);
				projects.setCity(this.getCity());
				projects.setCountyLevelCity(this.getCountyLevelCity());
				projects.setTown(this.getTown());
				projects.setVillage(this.getVillage());
				projects.setSubjectName(this.getSubjectName());
				projects.setFundType(this.getFundType());
				projects.setProjectName(this.getProjectName());
				projects.setCarryOutUnit(this.getCarryOutUnit());
				projects.setApprovalNumber(this.getApprovalNumber());
				projects.setTotalFund(this.getTotalFund()!= null ? this.getTotalFund() : 0);
				projects.setFinanceFund(this.getFinanceFund()!=null ? this.getFinanceFund() : 0);
				projects.setSelfFinancing(this.getSelfFinancing()!=null ? this.getSelfFinancing() : 0);
				projects.setIntegrateFund(this.getIntegrateFund()!=null ? this.getIntegrateFund() : 0);
				projects.setCoveredFarmerNumber(StringUtils.isNotEmpty(this.getCoveredFarmerNumber()) ? Integer.parseInt(this.getCoveredFarmerNumber()) : 0);
				projects.setCoveringNumber(StringUtils.isNotEmpty(this.getCoveringNumber()) ? Integer.parseInt(this.getCoveringNumber()) : 0);
				projects.setPovertyStrickenFarmerNumber(StringUtils.isNotEmpty(this.getPovertyStrickenFarmerNumber()) ? Integer.parseInt(this.getPovertyStrickenFarmerNumber()) : 0);
				projects.setPovertyStrickenPeopleNumber(StringUtils.isNotEmpty(this.getPovertyStrickenPeopleNumber()) ? Integer.parseInt(this.getPovertyStrickenPeopleNumber()) : 0);
				projects.setPovertyGeneralFarmer(StringUtils.isNotEmpty(this.getPovertyGeneralFarmer()) ? Integer.parseInt(this.getPovertyGeneralFarmer()) : 0);
				projects.setPovertyGeneralPeople(StringUtils.isNotEmpty(this.getPovertyGeneralPeople()) ? Integer.parseInt(this.getPovertyGeneralPeople()) : 0);
				projects.setPovertyLowIncomeFarmer(StringUtils.isNotEmpty(this.getPovertyLowIncomeFarmer()) ? Integer.parseInt(this.getPovertyLowIncomeFarmer()) : 0);
				projects.setPovertyLowIncomePeople(StringUtils.isNotEmpty(this.getPovertyLowIncomePeople()) ? Integer.parseInt(this.getPovertyLowIncomePeople()) : 0);
//				projects.setPovertyPoorFarmer(StringUtils.isNotEmpty(this.getPovertyPoorFarmer()) ? Integer.parseInt(this.getPovertyPoorFarmer()) : 0);
//				projects.setPovertyPoorPeople(StringUtils.isNotEmpty(this.getPovertyPoorPeople()) ? Integer.parseInt(this.getPovertyPoorPeople()) : 0);
				projects.setScaleAndContent(this.getScaleAndContent());
				projects.setChargePerson(this.getChargePerson());
				projects.setDeadline(StringUtils.isNotEmpty(this.getDeadline()) ? Integer.parseInt(this.getDeadline()) : 0);
				projects.setStandbyNumber(this.getStandbyNumber());
				projects.setFundToCountry(this.getFundToCountry()!=null ? this.getFundToCountry() : 0);
				projects.setApproveState("正在备案".equals(this.getApproveState()) ? 0 :"备案通过".equals(this.getApproveState()) ? 1 : 2);
				projects.setCreateUser(this.getCreateUser());
				projects.setCreateTime(this.getCreateTime());
				projects.setInputStatus(this.getInputStatus());
				projects.setApprovingDepartment(this.getApprovingDepartment());
				
				projects.setFlag(0);
				projects.setCheckStatus(0);
				projects.setProjectType(1);
				projects.setLevel(0);
				projects.setProjectsStatus(1);
				projects.setAdjustmentStatus(1);
				projects.setApprover_status(0);
				return projects;
			}else{
				projects.setReferenceNumber(this.getReferenceNumber());
				projects.setProjectNumber(this.getProjectNumber()+"");
				projects.setFundYear(this.getFundYear());
				projects.setArea(AreaUtil.getAreaByAreaName(this.getCity(), this.getCountyLevelCity(), this.getTown(), this.getVillage()));
				projects.setCountyLevelCityID(StringUtils.isNotEmpty(this.getCountyLevelCity()) ? AreaUtil.getAreaByAreaName(this.getCity(), this.getCountyLevelCity(), "", "") : null);
				projects.setTownID(StringUtils.isNotEmpty(this.getTown()) ? AreaUtil.getAreaByAreaName(this.getCity(), this.getCountyLevelCity(), this.getTown(), "") : null);
				projects.setVillageID(StringUtils.isNotEmpty(this.getVillage()) ? AreaUtil.getAreaByAreaName(this.getCity(), this.getCountyLevelCity(), this.getTown(), this.getVillage()) : null);
				projects.setCity(this.getCity());
				projects.setCountyLevelCity(this.getCountyLevelCity());
				projects.setTown(this.getTown());
				projects.setVillage(this.getVillage());
				projects.setSubjectName(this.getSubjectName());
				projects.setFundType(this.getFundType());
				projects.setProjectName(this.getProjectName());
				projects.setCarryOutUnit(this.getCarryOutUnit());
				projects.setApprovalNumber(this.getApprovalNumber());
				projects.setTotalFund(this.getTotalFund()!=null ? this.getTotalFund() : 0);
				projects.setFinanceFund(this.getFinanceFund()!=null? this.getFinanceFund() : 0);
				projects.setSelfFinancing(this.getSelfFinancing()!=null ? this.getSelfFinancing() : 0);
				projects.setIntegrateFund(this.getIntegrateFund()!=null ? this.getIntegrateFund() : 0);
				projects.setCoveredFarmerNumber(StringUtils.isNotEmpty(this.getCoveredFarmerNumber()) ? Integer.parseInt(this.getCoveredFarmerNumber()) : 0);
				projects.setCoveringNumber(StringUtils.isNotEmpty(this.getCoveringNumber()) ? Integer.parseInt(this.getCoveringNumber()) : 0);
				projects.setPovertyStrickenFarmerNumber(StringUtils.isNotEmpty(this.getPovertyStrickenFarmerNumber()) ? Integer.parseInt(this.getPovertyStrickenFarmerNumber()) : 0);
				projects.setPovertyStrickenPeopleNumber(StringUtils.isNotEmpty(this.getPovertyStrickenPeopleNumber()) ? Integer.parseInt(this.getPovertyStrickenPeopleNumber()) : 0);
				projects.setPovertyGeneralFarmer(StringUtils.isNotEmpty(this.getPovertyGeneralFarmer()) ? Integer.parseInt(this.getPovertyGeneralFarmer()) : 0);
				projects.setPovertyGeneralPeople(StringUtils.isNotEmpty(this.getPovertyGeneralPeople()) ? Integer.parseInt(this.getPovertyGeneralPeople()) : 0);
				projects.setPovertyLowIncomeFarmer(StringUtils.isNotEmpty(this.getPovertyLowIncomeFarmer()) ? Integer.parseInt(this.getPovertyLowIncomeFarmer()) : 0);
				projects.setPovertyLowIncomePeople(StringUtils.isNotEmpty(this.getPovertyLowIncomePeople()) ? Integer.parseInt(this.getPovertyLowIncomePeople()) : 0);
//				projects.setPovertyPoorFarmer(StringUtils.isNotEmpty(this.getPovertyPoorFarmer()) ? Integer.parseInt(this.getPovertyPoorFarmer()) : 0);
//				projects.setPovertyPoorPeople(StringUtils.isNotEmpty(this.getPovertyPoorPeople()) ? Integer.parseInt(this.getPovertyPoorPeople()) : 0);
				projects.setScaleAndContent(this.getScaleAndContent());
				projects.setChargePerson(this.getChargePerson());
				projects.setDeadline(StringUtils.isNotEmpty(this.getDeadline()) ? Integer.parseInt(this.getDeadline()) : 0);
				projects.setStandbyNumber(this.getStandbyNumber());
				projects.setFundToCountry(this.getFundToCountry()!=null ? this.getFundToCountry() : 0);
				projects.setApproveState("正在备案".equals(this.getApproveState()) ? 0 :"备案通过".equals(this.getApproveState()) ? 1 : 2);
				projects.setCreateUser(this.getCreateUser());
				projects.setCreateTime(this.getCreateTime());
				projects.setInputStatus(this.getInputStatus());
				projects.setApprovingDepartment(this.getApprovingDepartment());
				
				projects.setFlag(0);
				projects.setCheckStatus(0);
				projects.setProjectType(1);
				projects.setLevel(0);
				projects.setIsLevel(1);
				projects.setProjectsStatus(0);
				projects.setAdjustmentStatus(1);
				projects.setApprover_status(0);
				return projects;
		}
	}

	public void setProjects(Projects projects) {
		this.projects = projects;
	}
	

	
	
//	@Override
//	public String toString() {
//		return "ProjectImportDetail [id=" + id + ", rowNumber=" + rowNumber + ", projectImport=" + projectImport
//				+ ", referenceNumber=" + referenceNumber + ", projectNumber=" + projectNumber + ", fundYear=" + fundYear
//				+ ", city=" + city + ", countyLevelCity=" + countyLevelCity + ", town=" + town + ", village=" + village
//				+ ", subjectName=" + subjectName + ", fundType=" + fundType + ", projectName=" + projectName
//				+ ", approvalNumber=" + approvalNumber + ", totalFund=" + totalFund + ", financeFund=" + financeFund
//				+ ", selfFinancing=" + selfFinancing + ", integrateFund=" + integrateFund + ", coveredFarmerNumber="
//				+ coveredFarmerNumber + ", coveringNumber=" + coveringNumber + ", povertyStrickenFarmerNumber="
//				+ povertyStrickenFarmerNumber + ", povertyStrickenPeopleNumber=" + povertyStrickenPeopleNumber
//				+ ", scaleAndContent=" + scaleAndContent + ", carryOutUnit=" + carryOutUnit + ", chargePerson="
//				+ chargePerson + ", deadline=" + deadline + ", standbyNumber=" + standbyNumber + ", fundToCountry="
//				+ fundToCountry + ", approveState=" + approveState + ", createUser=" + createUser + ", createTime="
//				+ createTime + ", inputStatus=" + inputStatus + ", approvingDepartment=" + approvingDepartment
//				+ ", validateStatus=" + validateStatus + ", validateDescription=" + validateDescription + "]";
//	}
	
}
