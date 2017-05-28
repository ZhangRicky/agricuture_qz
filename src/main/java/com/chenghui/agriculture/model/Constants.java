package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@DynamicUpdate
@SelectBeforeUpdate
public class Constants implements Serializable {
	public static final String TOKEN = "token";
	public static final int TYPE_XXLX = 1;//信息类型
	public static final int TYPE_XXYDW = 2;//信息源单位
	public static final int TYPE_JMLX = 3;//居民类型
	public static final String CREDIT_XXLX_CODE_HMD="1001";//信息类型黑名单
	public static final String CREDIT_XXLX_CODE_YPFJ="1002";//一票否决
	public static final String CREDIT_XXLX_CODE_SHIXINJF="1003";//失信减分项
	public static final String CREDIT_XXLX_CODE_SHOUXINJF="1004";//守信行为加分项
	@Id
	@GeneratedValue
	private Long id;

	private String type;
	
	private String code;
	
	private String displayName;
	
	private String parentCode;
	
	private String comments;
	
	private Boolean isDeleted;
	
	private Long orderBy;
	
	private String lastUpdateUser;
	
	@Temporal(TemporalType.DATE)
	private Date lastUpdateTime;
	

	public Long getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comment) {
		this.comments = comment;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	@Override
	public String toString() {
		return "ID:["+this.getId()+"],类型:["+this.getType()+"],编码:[" + this.getCode()+"],显示名称:["+this.getDisplayName()+"]";
	}
	
}
