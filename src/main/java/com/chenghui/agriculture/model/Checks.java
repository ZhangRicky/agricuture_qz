package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.chenghui.agriculture.core.utils.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @描述：此类代表一次验收记录
 * @author mars
 * @date 2016-03-07 14:08:00
 */
@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value={"subProjectId", "user","lng","lat","isDeleted","checksId"})
@DynamicUpdate
@SelectBeforeUpdate
public class Checks implements Serializable {

	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * 验收子项目
	 */
	@ManyToOne
	private Projects projects;
	
	/**
	 * 验收日期
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date createDate;
	
	/**
	 * 验收人
	 */
	@ManyToOne
	private Users user;
	
	/**
	 * 其它描述信息
	 */
	private String description;
	
	/**
	 * 是否删除
	 */
	private boolean isDeleted;
	
	/**
	 * 多个监管记录点
	 */
	@OneToMany(cascade=CascadeType.ALL, mappedBy="checks")
	private List<Supervision> supervisions = new ArrayList<Supervision>();
	
	/**
	 * 多个坐标点，用来存放行走路线上的坐标点
	 */
	@OneToMany(cascade=CascadeType.ALL, mappedBy="checks")
	@OrderBy("id asc")
	private List<Point> points = new ArrayList<Point>();
	
	/**
	 * 鹰眼坐标集合
	 */
	@OneToMany(cascade=CascadeType.ALL, mappedBy="checks")
	@OrderBy("loc_time asc")
	private List<PointBaidu> pointBaidu = new ArrayList<PointBaidu>();
	
	private Integer isSuccess;//验收结论 1：通过，2：不通过，默认false
	
	private String pictures; //验收图片
	
	@Transient
	private long checksId; //验收记录id
	@Transient
	private long subProjectId; //子项目id
	@Transient
	private float lng; //经度
	@Transient
	private float lat; //纬度

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Projects getProjects() {
		return projects;
	}

	public void setProjects(Projects projects) {
		this.projects = projects;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<Supervision> getSupervisions() {
		return supervisions;
	}

	public void setSupervisions(List<Supervision> supervisions) {
		this.supervisions = supervisions;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
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

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public Integer getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}

	public long getChecksId() {
		return checksId;
	}

	public void setChecksId(long checksId) {
		this.checksId = checksId;
	}

	public List<PointBaidu> getPointBaidu() {
		return pointBaidu;
	}

	public void setPointBaidu(List<PointBaidu> pointBaidu) {
		this.pointBaidu = pointBaidu;
	}

	@Override
	public String toString() {
		return "Checks [id=" + id + ", projects=" + projects + ", createDate=" + createDate + ", user=" + user
				+ ", description=" + description + ", isDeleted=" + isDeleted + "]";
	}
}
