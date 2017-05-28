package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.chenghui.agriculture.core.utils.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
@JsonIgnoreProperties(value = { "serachName", "searchStartTime", "searchEndTime" })
public class SystemOperationLog implements Serializable {
	
	/**
	 * 登录应用
	 */
	public static String LOGTYPE_LOGIN = "Login";
	/**
	 * 登出应用
	 */
	public static String LOGTYPE_LOGOUT = "Logout";
	/**
	 * 用户添加和权限相关的任何信息，包括添加用户、用户组、权限、角色等
	 */
	public static String LOGTYPE_ADDPRIVILEGE = "AddPrivilege";
	/**
	 * 用户删除和权限相关的任何信息，包括添加用户、用户组、权限、角色等
	 */
	public static String LOGTYPE_DELPRIVILEGE = "DelPrivilege";
	/**
	 * 用户更新和权限相关的任何信息，包括添加用户、用户组、权限、角色等
	 */
	public static String LOGTYPE_UPDATEPRIVILEGE = "UpdatePrivilege";
	/**
	 * 业务数据查阅
	 */
	public static String LOGTYPE_VIEW = "View";
	/**
	 * 业务数据增加
	 */
	public static String LOGTYPE_ADD = "Add";
	/**
	 * 业务数据更新
	 */
	public static String LOGTYPE_UPDATE = "Update";
	/**
	 * 业务数据删除
	 */
	public static String LOGTYPE_DEL = "Del";
	/**
	 * 业务数据审核
	 */
	public static String LOGTYPE_APPROVE = "Approve";
	/**
	 * 其它类别
	 */
	public static String LOGTYPE_OTHER = "Other";
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date LogTime;
	
	private String SubUser;
	
	private String App;
	
	private String Sip;
	
	private String AppModule;
	
	/**
	 * “Login”(登录应用)
	 * “Logout” （登出应用）
	 * “AddPrivilege”（用户添加和权限相关的任何信息，包括添加用户、用户组、权限、角色等）
	 * “DelPrivilege”（用户删除和权限相关的任何信息，包括添加用户、用户组、权限、角色等）
	 * “UpdatePrivilege”（用户更新和权限相关的任何信息，包括添加用户、用户组、权限、角色等）
	 * “View”（业务数据查阅）
	 * “Add”（业务数据增加）
	 * “Update”（业务数据更新）
	 * “Del”（业务数据删除）
	 * “Approve” (业务数据审核)
	 * “Other”（其它类别）
	 */
	private String OpType;
	
	@Transient
	private Date searchStartTime;
	
	@Transient
	private Date searchEndTime;
	
	@Transient
	private String searchName;
	
	private String OpText;
	
	private String detailed;
	
	private Boolean isRolBack;
	
	private Long operId;//操作的数据ID

	public Long getOperId() {
		return operId;
	}

	public void setOperId(Long operId) {
		this.operId = operId;
	}

	public Boolean getIsRolBack() {
		return isRolBack;
	}

	public void setIsRolBack(Boolean isRolBack) {
		this.isRolBack = isRolBack;
	}

	public String getDetailed() {
		return detailed;
	}

	public void setDetailed(String detailed) {
		this.detailed = detailed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLogTime() {
		return LogTime;
	}

	public void setLogTime(Date logTime) {
		LogTime = logTime;
	}

	public String getSubUser() {
		return SubUser;
	}

	public void setSubUser(String subUser) {
		SubUser = subUser;
	}

	public String getApp() {
		return App;
	}

	public void setApp(String app) {
		App = app;
	}

	public String getSip() {
		return Sip;
	}

	public void setSip(String sip) {
		Sip = sip;
	}

	public String getAppModule() {
		return AppModule;
	}

	public void setAppModule(String appModule) {
		AppModule = appModule;
	}

	public String getOpType() {
		return OpType;
	}

	public void setOpType(String opType) {
		OpType = opType;
	}

	public String getOpText() {
		return OpText;
	}

	public void setOpText(String opText) {
		OpText = opText;
	}

	public Date getSearchStartTime() {
		return searchStartTime;
	}

	public void setSearchStartTime(Date searchStartTime) {
		this.searchStartTime = searchStartTime;
	}

	public Date getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(Date searchEndTime) {
		this.searchEndTime = searchEndTime;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	
}
