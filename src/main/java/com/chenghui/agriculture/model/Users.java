package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.chenghui.agriculture.core.utils.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value={"password","role","salt"})
@DynamicUpdate
@SelectBeforeUpdate
public class Users implements Serializable{

	@Id
	@GeneratedValue
	private Long id;
	
	private String realName; //真实姓名
	
	private String userName; //登录账号
	
	private Boolean isInUse;
	
	private String password;
	
	@Transient
	private String plainPassword;
	
	@Transient
	private String userGroup;

	private String createRoleName;
	
	private String createUserName;

	@JsonSerialize(using = JsonDateSerializer.class)
	private Date created;

	private String salt;
	
	@Transient
	private String check;
	
	@Temporal(TemporalType.DATE)
	private Date expireDate;

	private Boolean isFirstLogin;
	
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@OrderBy("id ASC")
	private Set<Area> area = new HashSet<Area>();
	
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@OrderBy("id ASC")
	private Set<Department> department = new HashSet<Department>();
	
	@ManyToOne
	private Constants deptLevel;//所属部门类别，对应征信规则的信息源单位，用于相应职能部门只能看到自己所属范围的征信信息
	
	private String  email;
	
	private String  mobile;
	
	private String description;
	
	@Transient
	private Integer deptLevelType;//所属机构情况

	@ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
	@OrderBy("Id ASC")
	private Set<Role> role = new HashSet<Role>();
	
	@Transient
	private String token;//用户App登录token
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getIsInUse() {
		return isInUse;
	}
	public void setIsInUse(Boolean isInUse) {
		this.isInUse = isInUse;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Role> getRole() {
		return role;
	}
	public void setRole(Set<Role> role) {
		this.role = role;
	}

	public Users addRole(Role role) {
		this.role.add(role);
		return this;
		
	}
	public Users removeRole(Role role) {
		this.role.remove(role);
		return this;	
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getPlainPassword() {
		return plainPassword;
	}
	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getCreateRoleName() {
		return createRoleName;
	}
	public void setCreateRoleName(String createRoleName) {
		this.createRoleName = createRoleName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	
	public Set<Area> getArea() {
		return area;
	}
	public void setArea(Set<Area> area) {
		this.area = area;
	}
    public Set<Department> getDepartment() {
		return department;
	}
	public void setDepartment(Set<Department> department) {
		this.department = department;
	}
		public Constants getDeptLevel() {
		return deptLevel;
	}
	public void setDeptLevel(Constants deptLevel) {
		this.deptLevel = deptLevel;
	}
	public Boolean getIsFirstLogin() {
		return isFirstLogin;
	}
	public void setIsFirstLogin(Boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Integer getDeptLevelType() {
		return deptLevelType;
	}
	public void setDeptLevelType(Integer deptLevelType) {
		this.deptLevelType = deptLevelType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "ID:["+this.getId()+"],姓名:["+this.getRealName()+"],登录名:["+this.getUserName()+"],电话:["+this.getMobile()+"],创建用户:["+this.getCreateUserName()+"]";
	}
}
