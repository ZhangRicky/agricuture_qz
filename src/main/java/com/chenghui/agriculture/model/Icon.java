package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 此类表示标注覆盖物所使用的图标。
 * @author mars
 *
 */
@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value={})
@DynamicUpdate
@SelectBeforeUpdate
public class Icon implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * 地理纬度。
	 */
	private String url;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
