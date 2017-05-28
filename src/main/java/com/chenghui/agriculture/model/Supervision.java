package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.chenghui.agriculture.core.utils.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 此类代表一次监管记录
 * 根据项目类型，填写的监管记录不同
 *    a.种养殖类、基础论证类、培训类均填写名称、上传图片或视频、存在问题、整改建议、整改时间；
 * 	  b.贴息类填写户数及金额；
 *    c.其它类填写文本。
 * @author mars
 * @date 2016-03-07 14:11:00
 */
@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value={"subProject","subProjectId","lng","lat","projectType","checks","checksId"})
@DynamicUpdate
@SelectBeforeUpdate
public class Supervision implements Serializable{

	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 所监管的工程
	 */
	@ManyToOne
	private Projects projects;
	
	/**
	 * 监管名称
	 */
	private String name;
	
	/**
	 * 上传图片
	 */
	private String pictures;
	
	/**
	 * 上传视频
	 */
	private String videos;
	
	/**
	 * 存在问题
	 */
	private String existingProblems;
	
	/**
	 * 整改建议
	 */
	private String correctSuggestions ;
	
	/**
	 * 整改时间
	 */
	private String correctTime;
	
	/**
	 * 农户数
	 */
	private int farmerNumber=0;
	
	/**
	 * 农户名称
	 */
	@ManyToOne
	private Farmer farmer;
	
	/**
	 * 金额
	 */
	private float fund=0;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 监管的坐标地点
	 */
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Point point;
	
	
	/**
	 * 添加监管日期
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date createDate;
	
	/**
	 * 监管人
	 */
	@ManyToOne
	private Users user;
	
	/**
	 * 项目进度
	 */
	private String projectProcess;
	/**
	 * 所属验收结果，为空则表示单个监管记录
	 */
	@ManyToOne
	private Checks checks;
	
	@Transient
	private long subProjectId; //子项目id
	@Transient
	private float lng; //经度
	@Transient
	private float lat; //纬度
	@Transient
	private int projectType; //项目类型
	@Transient
	private long checksId=0; //验收记录id
	@Transient
	private long farmerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public String getVideos() {
		return videos;
	}

	public void setVideos(String videos) {
		this.videos = videos;
	}

	public String getExistingProblems() {
		return existingProblems;
	}

	public void setExistingProblems(String existingProblems) {
		this.existingProblems = existingProblems;
	}

	public String getCorrectSuggestions() {
		return correctSuggestions;
	}

	public void setCorrectSuggestions(String correctSuggestions) {
		this.correctSuggestions = correctSuggestions;
	}

	public String getCorrectTime() {
		return correctTime;
	}

	public void setCorrectTime(String correctTime) {
		this.correctTime = correctTime;
	}

	public int getFarmerNumber() {
		return farmerNumber;
	}

	public void setFarmerNumber(int farmerNumber) {
		this.farmerNumber = farmerNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getFund() {
		return fund;
	}

	public void setFund(float fund) {
		this.fund = fund;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Checks getChecks() {
		return checks;
	}

	public void setChecks(Checks checks) {
		this.checks = checks;
	}

	public long getSubProjectId() {
		return subProjectId;
	}

	public void setSubProjectId(long subProjectId) {
		this.subProjectId = subProjectId;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public int getProjectType() {
		return projectType;
	}

	public void setProjectType(int projectType) {
		this.projectType = projectType;
	}
	
	public long getChecksId() {
		return checksId;
	}

	public void setChecksId(long checksId) {
		this.checksId = checksId;
	}

	public Projects getProjects() {
		return projects;
	}

	public void setProjects(Projects projects) {
		this.projects = projects;
	}

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public long getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(long farmerId) {
		this.farmerId = farmerId;
	}

	public String getProjectProcess() {
		return projectProcess;
	}

	public void setProjectProcess(String projectProcess) {
		this.projectProcess = projectProcess;
	}

	@Override
	public String toString() {
		return "Supervision [id=" + id + ", projects=" + projects + ", name=" + name + ", existingProblems=" + existingProblems + ", correctSuggestions="
				+ correctSuggestions + ", correctTime=" + correctTime + ", farmerNumber=" + farmerNumber + ", fund="
				+ fund + ", description=" + description + ", createDate=" + createDate + ", subProjectId=" + subProjectId + ", lng=" + lng + ", lat=" + lat
				+ ", projectType=" + projectType + "]";
	}

}
