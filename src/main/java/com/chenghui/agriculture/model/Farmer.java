package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 农民信息
 * @author yudq
 *
 */

@SuppressWarnings("serial")
@JsonIgnoreProperties(value={"farmerImport"})
@Entity
public class Farmer implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String farmerNumber; //农户编号
	
	private String farmerName;//户主姓名

	private String phoneNumber; //手机号码
	
	private String content; //项目内容
	
	private Long project_id; //子项目Id

	/**
	 * 导入数据主表
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	private FarmerImport farmerImport;

	public FarmerImport getFarmerImport() {
		return farmerImport;
	}

	public void setFarmerImport(FarmerImport farmerImport) {
		this.farmerImport = farmerImport;
	}
	
	public Long getProject_id() {
		return project_id;
	}

	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFarmerNumber() {
		return farmerNumber;
	}

	public void setFarmerNumber(String farmerNumber) {
		this.farmerNumber = farmerNumber;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

//	@Override
//	public String toString() {
//		return "Farmer [id=" + id + ", farmerNumber=" + farmerNumber + ", farmerName=" + farmerName + ", phoneNumber="
//				+ phoneNumber + ", content=" + content + "]";
//	}
	
}
