package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 项目实体
 * @author LLJ
 *
 */
@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
//@JsonIgnoreProperties(value={"startIndex", "limit", "countyLevelCityID", "townID", "villageID"})
@JsonIgnoreProperties(value={"startIndex", "limit"})
@DynamicUpdate
@SelectBeforeUpdate
public class Projects implements Serializable{
	
	@Id
	@GeneratedValue
	private Long id;                                 //主项目id
	private int level;                               //项目标记;3:镇;2:村;1:农户
	private String treeLevel;                        //记录树级别
	@ManyToOne
	private Area area;								 //所属地区，村、镇、市
	private Long parentId;                           //父级id
	private String projectName;                      //项目名称
	private String referenceNumber;                  //项目文号
	private String projectNumber;                   //项目唯一编号
	private String carryOutUnit;                     //实施单位
	private String chargePerson;                     //项目负责人
	private String farmerName;                       //农户名称
	private String city;						     //地州名
	private String countyLevelCity;                  //县名
	private String town;                             //乡镇名
	private String village;    						 //村名
	private String fundYear;                         //项目资金年度
	private String subjectName;                      //项目专项名称
	private String fundType;                         //项目资金明细
	private String approvalNumber;                   //项目批复文号
	private String scaleAndContent;                  //财政资金建设规模及内容
	private String standbyNumber;                    //后补文号
	private Double totalFund;                        //项目总资金(万元)
	private Double financeFund;                      //项目财政资金
	private Double selfFinancing;                    //项目自筹资金
	private Double integrateFund;                    //项目整合资金
	private Double fundToCountry;                    //到县资金(万元)
	private Integer coveredFarmerNumber;             //扶持总农户数(户)
	private Integer coveringNumber;                  //扶持总人数(人)
	private Integer povertyStrickenFarmerNumber;     //扶持贫困农户数(户)
	private Integer povertyGeneralFarmer;			 //扶持一般农户
//	private Integer povertyPoorFarmer;               //扶持贫困农户
	private Integer povertyLowIncomeFarmer;          //扶持低收入困难农户
	private Integer povertyStrickenPeopleNumber;     //扶持贫困人口数(人)
	private Integer povertyGeneralPeople;			 //扶持一般人口
//	private Integer povertyPoorPeople;               //扶持贫困人口
	private Integer povertyLowIncomePeople;          //扶持低收入困难人口
	private Integer deadline;                        //完成期限(月)
	private Integer approveState;                    //审核状态：0:正在备案;1:备案通过;2:不通过
	private Integer projectType;                     //项目类型：1：种植;2:基础论证;3:培训;4：贴息;5：经济组织;6:其它
	private Integer constructionMode;                //建设方式;0:先建后款;1：先款后建
	private Integer checkStatus;                     //验收状态;0:未验收;1：验收通过;2：验收不通过
	private String createUser;                       //创建用户ID
	private String createTime;                       //创建时间
	private String inputStatus;                      //录入状态
	private String approvingDepartment;              //当前审核部门
	private String financebiLv;                      //财政报账率
	private String path;                             //上传路径
	private Integer flag;                            //数据状态;0:表示正常;1：表示删除
	private String remark;                           //备注
	private String adjustmentReason;                 //项目调项原因
	private String shuoming;                          //公示说明
	private int adjustmentStatus;                    //调项状态;1:表示正常;2:表示被调项
	private String projectProcess;					 //项目进度
	@ManyToOne
	private Area countyLevelCityID;                  //县ID
	@ManyToOne
	private Area townID;                             //乡镇ID
	@ManyToOne
	private Area villageID;    						 //村ID

	@Transient
	private List<Projects> children = new ArrayList<>();
	
	
	public Integer getPovertyGeneralFarmer() {
		return povertyGeneralFarmer;
	}
	public void setPovertyGeneralFarmer(Integer povertyGeneralFarmer) {
		this.povertyGeneralFarmer = povertyGeneralFarmer;
	}
//	public Integer getPovertyPoorFarmer() {
//		return povertyPoorFarmer;
//	}
//	public void setPovertyPoorFarmer(Integer povertyPoorFarmer) {
//		this.povertyPoorFarmer = povertyPoorFarmer;
//	}
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
//	public Integer getPovertyPoorPeople() {
//		return povertyPoorPeople;
//	}
//	public void setPovertyPoorPeople(Integer povertyPoorPeople) {
//		this.povertyPoorPeople = povertyPoorPeople;
//	}
	public Integer getPovertyLowIncomePeople() {
		return povertyLowIncomePeople;
	}
	public void setPovertyLowIncomePeople(Integer povertyLowIncomePeople) {
		this.povertyLowIncomePeople = povertyLowIncomePeople;
	}
	public String getTreeLevel() {
		return treeLevel;
	}
	public void setTreeLevel(String treeLevel) {
		this.treeLevel = treeLevel;
	}
	public String getShuoming() {
		return shuoming;
	}
	public void setShuoming(String shuoming) {
		this.shuoming = shuoming;
	}
	private int isLevel;                             //是否为子节点;0代表不是叶子节点；1代表是叶子节点

	private int projectsStatus;                     //项目重复标志，0：不重复，1：重复
	
	@Transient
	private int startIndex = 0;						 //开始页

	@Transient
	private int limit = 10;						 	 //页大小
	
	private Double bzTotal;					 //总报账金额
	private Double bkTotal;					 //总拨款金额
	private Double balance;					 //结余资金
	private Double balance_forword;			 //结转资金
	private int approver_status;			 //审核状态	1:表示已审核/0:未审核
	
	
	
	@Transient
	private Long pid;
	
	@Transient
	private Long areaId;                             //用于接收
	
//	@Transient
//	private int allTotalFund;
	
//	public int getAllTotalFund() {
//		return allTotalFund;
//	}
//	public void setAllTotalFund(int allTotalFund) {
//		this.allTotalFund = allTotalFund;
//	}
	/**
	 * 模糊查询字段        
	 */
	@Transient
	private String xxnr;                        
	
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public int getIsLevel() {
		return isLevel;
	}
	public void setIsLevel(int isLevel) {
		this.isLevel = isLevel;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
//	public void setProjectNumber(Integer projectNumber) {
//		this.projectNumber = projectNumber;
//	}
	
	public String getCarryOutUnit() {
		return carryOutUnit;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
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
	public String getFarmerName() {
		return farmerName;
	}
	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
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
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	public String getScaleAndContent() {
		return scaleAndContent;
	}
	public void setScaleAndContent(String scaleAndContent) {
		this.scaleAndContent = scaleAndContent;
	}
	public String getStandbyNumber() {
		return standbyNumber;
	}
	public void setStandbyNumber(String standbyNumber) {
		this.standbyNumber = standbyNumber;
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
	public Integer getDeadline() {
		return deadline;
	}
	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}
	public Integer getApproveState() {
		return approveState;
	}
	public void setApproveState(Integer approveState) {
		this.approveState = approveState;
	}
	public Integer getProjectType() {
		return projectType;
	}
	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}
	public Integer getConstructionMode() {
		return constructionMode;
	}
	public void setConstructionMode(Integer constructionMode) {
		this.constructionMode = constructionMode;
	}
	public Integer getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
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
	public String getFinancebiLv() {
		return financebiLv;
	}
	public void setFinancebiLv(String financebiLv) {
		this.financebiLv = financebiLv;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
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
	
	public Double getBalance_forword() {
		return balance_forword;
	}
	public void setBalance_forword(Double balance_forword) {
		this.balance_forword = balance_forword;
	}
	
	public int getApprover_status() {
		return approver_status;
	}
	public void setApprover_status(int approver_status) {
		this.approver_status = approver_status;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
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
	
	public String getXxnr() {
		return xxnr;
	}
	public void setXxnr(String xxnr) {
		this.xxnr = xxnr;
	}

	public List<Projects> getChildren() {
		return children;
	}
	public void setChildren(List<Projects> children) {
		this.children = children;
	}
	
	
	public String getAdjustmentReason() {
		return adjustmentReason;
	}
	public void setAdjustmentReason(String adjustmentReason) {
		this.adjustmentReason = adjustmentReason;
	}
	
	public int getProjectsStatus() {
		return projectsStatus;
	}
	public void setProjectsStatus(int projectsStatus) {
		this.projectsStatus = projectsStatus;
	}
	
	public int getAdjustmentStatus() {
		return adjustmentStatus;
	}
	public void setAdjustmentStatus(int adjustmentStatus) {
		this.adjustmentStatus = adjustmentStatus;
	}
	
	public String getProjectProcess() {
		return projectProcess;
	}
	public void setProjectProcess(String projectProcess) {
		this.projectProcess = projectProcess;
	}
	
	public Area getCountyLevelCityID() {
		return countyLevelCityID;
	}
	public void setCountyLevelCityID(Area countyLevelCityID) {
		this.countyLevelCityID = countyLevelCityID;
	}
	public Area getTownID() {
		return townID;
	}
	public void setTownID(Area townID) {
		this.townID = townID;
	}
	public Area getVillageID() {
		return villageID;
	}
	public void setVillageID(Area villageID) {
		this.villageID = villageID;
	}
//	@Override
//	public String toString() {
//		return "Projects [id=" + id + ", level=" + level + ", treeLevel=" + treeLevel + ", area=" + area + ", parentId="
//				+ parentId + ", projectName=" + projectName + ", referenceNumber=" + referenceNumber
//				+ ", projectNumber=" + projectNumber + ", carryOutUnit=" + carryOutUnit + ", chargePerson="
//				+ chargePerson + ", farmerName=" + farmerName + ", city=" + city + ", countyLevelCity="
//				+ countyLevelCity + ", town=" + town + ", village=" + village + ", fundYear=" + fundYear
//				+ ", subjectName=" + subjectName + ", fundType=" + fundType + ", approvalNumber=" + approvalNumber
//				+ ", scaleAndContent=" + scaleAndContent + ", standbyNumber=" + standbyNumber + ", totalFund="
//				+ totalFund + ", financeFund=" + financeFund + ", selfFinancing=" + selfFinancing + ", integrateFund="
//				+ integrateFund + ", fundToCountry=" + fundToCountry + ", coveredFarmerNumber=" + coveredFarmerNumber
//				+ ", coveringNumber=" + coveringNumber + ", povertyStrickenFarmerNumber=" + povertyStrickenFarmerNumber
//				+ ", povertyStrickenPeopleNumber=" + povertyStrickenPeopleNumber + ", deadline=" + deadline
//				+ ", approveState=" + approveState + ", projectType=" + projectType + ", constructionMode="
//				+ constructionMode + ", checkStatus=" + checkStatus + ", createUser=" + createUser + ", createTime="
//				+ createTime + ", inputStatus=" + inputStatus + ", approvingDepartment=" + approvingDepartment
//				+ ", financebiLv=" + financebiLv + ", path=" + path + ", flag=" + flag + ", remark=" + remark
//				+ ", adjustmentReason=" + adjustmentReason + ", shuoming=" + shuoming + ", adjustmentStatus="
//				+ adjustmentStatus + ", projectProcess=" + projectProcess + ", children=" + children + ", isLevel="
//				+ isLevel + ", projectsStatus=" + projectsStatus + ", startIndex=" + startIndex + ", limit=" + limit
//				+ ", bzTotal=" + bzTotal + ", bkTotal=" + bkTotal + ", balance=" + balance + ", balance_forword="
//				+ balance_forword + ", approver_status=" + approver_status + ", pid=" + pid + ", areaId=" + areaId
//				+ ", xxnr=" + xxnr + "]";
	
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	
	@Override
	public String toString() {
		return "Projects [id=" + id + ", level=" + level + ", treeLevel=" + treeLevel + ", area=" + area + ", parentId="
				+ parentId + ", projectName=" + projectName + ", referenceNumber=" + referenceNumber
				+ ", projectNumber=" + projectNumber + ", carryOutUnit=" + carryOutUnit + ", chargePerson="
				+ chargePerson + ", farmerName=" + farmerName + ", city=" + city + ", countyLevelCity="
				+ countyLevelCity + ", town=" + town + ", village=" + village + ", fundYear=" + fundYear
				+ ", subjectName=" + subjectName + ", fundType=" + fundType + ", approvalNumber=" + approvalNumber
				+ ", scaleAndContent=" + scaleAndContent + ", standbyNumber=" + standbyNumber + ", totalFund="
				+ totalFund + ", financeFund=" + financeFund + ", selfFinancing=" + selfFinancing + ", integrateFund="
				+ integrateFund + ", fundToCountry=" + fundToCountry + ", coveredFarmerNumber=" + coveredFarmerNumber
				+ ", coveringNumber=" + coveringNumber + ", povertyStrickenFarmerNumber=" + povertyStrickenFarmerNumber
				+ ", povertyGeneralFarmer=" + povertyGeneralFarmer + ", povertyLowIncomeFarmer="
				+ povertyLowIncomeFarmer + ", povertyStrickenPeopleNumber=" + povertyStrickenPeopleNumber
				+ ", povertyGeneralPeople=" + povertyGeneralPeople + ", povertyLowIncomePeople="
				+ povertyLowIncomePeople + ", deadline=" + deadline + ", approveState=" + approveState
				+ ", projectType=" + projectType + ", constructionMode=" + constructionMode + ", checkStatus="
				+ checkStatus + ", createUser=" + createUser + ", createTime=" + createTime + ", inputStatus="
				+ inputStatus + ", approvingDepartment=" + approvingDepartment + ", financebiLv=" + financebiLv
				+ ", path=" + path + ", flag=" + flag + ", remark=" + remark + ", adjustmentReason=" + adjustmentReason
				+ ", shuoming=" + shuoming + ", adjustmentStatus=" + adjustmentStatus + ", projectProcess="
				+ projectProcess + ", countyLevelCityID=" + countyLevelCityID + ", townID=" + townID + ", villageID="
				+ villageID + ", children=" + children + ", isLevel=" + isLevel + ", projectsStatus=" + projectsStatus
				+ ", startIndex=" + startIndex + ", limit=" + limit + ", bzTotal=" + bzTotal + ", bkTotal=" + bkTotal
				+ ", balance=" + balance + ", balance_forword=" + balance_forword + ", approver_status="
				+ approver_status + ", pid=" + pid + ", areaId=" + areaId + ", xxnr=" + xxnr + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Projects other = (Projects) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
