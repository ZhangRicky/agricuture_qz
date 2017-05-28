package com.chenghui.agriculture.model.vo;
/**
 * 项目统计工具类
 * @author Ricky
 *
 */
public class ReportProjects {
	private Integer id;		//id
	private String projectName;	//项目名称
	private String town;		//乡镇
	private String village;		//村级
	private Double totalFund;	//总资金
	private String fundYear;	//资金年度
	private Integer projectType;	//产业类型
	private Integer projectCount;	//项目个数总计
	
	
	public Integer getProjectType() {
		return projectType;
	}
	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	public Double getTotalFund() {
		return totalFund;
	}
	public void setTotalFund(Double totalFund) {
		this.totalFund = totalFund;
	}
	public String getFundYear() {
		return fundYear;
	}
	public void setFundYear(String fundYear) {
		this.fundYear = fundYear;
	}
	public Integer getProjectCount() {
		return projectCount;
	}
	public void setProjectCount(Integer projectCount) {
		this.projectCount = projectCount;
	}
	
	
	
	
}
