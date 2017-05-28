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
public class PointBaidu implements Serializable {
	
	
	public PointBaidu() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * 经度 百度加密坐标
	 * 范围：double(-180.0 , +180.0)
	 */
	private double lng;
	
	/**
	 * 纬度 百度加密坐标
	 * 范围：double(-90.0 , +90.0)
	 */
	private double lat;
	/**
	 * 该track实时点的上传时间 该时间为用户上传的时间
	 */
	private long loc_time;
	/**
	 * 创建时间 该时间为服务端时间
	 */
	private String create_time;
	/**
	 * 方向 范围为[0,365]，0度为正北方向，顺时针
	 */
	private int direction;
	/**
	 * 速度 单位：km/h
	 */
	private double speed;
	/**
	 * 定位精度 单位：m
	 */
	private double radius;
	
	
	/**
	 * 所属验收结果，为空则表示单个坐标点
	 */
	@ManyToOne
	private Checks checks;
	
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

	public long getLoc_time() {
		return loc_time;
	}

	public void setLoc_time(long loc_time) {
		this.loc_time = loc_time;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Checks getChecks() {
		return checks;
	}

	public void setChecks(Checks checks) {
		this.checks = checks;
	}

	public long getChecksId() {
		return checksId;
	}

	public void setChecksId(long checksId) {
		this.checksId = checksId;
	}

	@Override
	public String toString() {
		return "PointBaidu [id=" + id + ", lng=" + lng + ", lat=" + lat + ", loc_time=" + loc_time + ", create_time="
				+ create_time + ", direction=" + direction + ", speed=" + speed + ", radius=" + radius + ", checks="
				+ checks + ", checksId=" + checksId + "]";
	}

}
