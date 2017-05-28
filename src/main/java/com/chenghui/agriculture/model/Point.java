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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 此类表示一个地理坐标点
 * @author mars
 *
 */
@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value={"checks"})
@DynamicUpdate
@SelectBeforeUpdate
public class Point implements Serializable {
	
	public Point() {
		super();
	}
	
	public Point(float lng, float lat) {
		this.setLng(lng);
		this.setLat(lat);
	}
	
	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * 地理经度
	 */
	private double lng;
	
	/**
	 * 地理纬度。
	 */
	private double lat;
	
	private Integer rangeNumber;
	
	/**
	 * 所属验收结果，为空则表示单个坐标点
	 */
	@ManyToOne
	private Checks checks;
	@ManyToOne
	private Projects projects;
	@Transient
	private long projectId;
	
	@Transient
	private long checksId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public Checks getChecks() {
		return checks;
	}

	public void setCheck(Checks checks) {
		this.checks = checks;
	}
	
	public long getChecksId() {
		return checksId;
	}

	public void setChecksId(long checksId) {
		this.checksId = checksId;
	}

	public void setChecks(Checks checks) {
		this.checks = checks;
	}

	public Projects getProjects() {
		return projects;
	}

	public void setProjects(Projects projects) {
		this.projects = projects;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	
	public Integer getRangeNumber() {
		return rangeNumber;
	}

	public void setRangeNumber(Integer rangeNumber) {
		this.rangeNumber = rangeNumber;
	}

	@Override
	public String toString() {
		return "Point [id=" + id + ", lng=" + lng + ", lat=" + lat + ", checks=" + checks + ", projects=" + projects
				+ ", projectId=" + projectId + ", checksId=" + checksId + "]";
	}

	

}
