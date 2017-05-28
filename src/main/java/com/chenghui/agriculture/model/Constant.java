package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@DynamicUpdate
@SelectBeforeUpdate
public class Constant implements Serializable {
	
	public static final int TYPE_PROBLEMS = 1;//存在问题
	public static final int TYPE_SUGGESTIONS = 2;//整改建议
	
	@Id
	@GeneratedValue
	private Long id;

	private int type;
	
	private String displayName;
	
	private String descriptions;
	
	private Boolean isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "Constant [id=" + id + ", type=" + type + ", displayName=" + displayName + ", descriptions="
				+ descriptions + ", isDeleted=" + isDeleted + "]";
	}
}
