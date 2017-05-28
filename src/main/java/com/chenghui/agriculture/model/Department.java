package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@Entity
@JsonIgnoreProperties(value={"users"})
public class Department implements Serializable {

	@Id
	@GeneratedValue
	private Long deptId;

	private String deptCode;

	private String deptName;
	
//	@Column(name="deptLevel", nullable=false, columnDefinition="NUMBER default 1")
	private int deptLevel;//1:居委会 2:社区 3:诚信办
	
	private int sort;
	
	private int num;
	
	private int flag;
	
	private Long parentCode;
	
//	@ManyToMany(mappedBy="department",fetch = FetchType.LAZY)
//	private Set<Role> role = new HashSet<Role>();
	
	@ManyToMany(mappedBy="department",fetch = FetchType.LAZY)
	private Set<Users> users = new HashSet<Users>();
	
	@Transient
	private boolean inUse;

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Long getParentCode() {
		return parentCode;
	}

	public void setParentCode(Long parentCode) {
		this.parentCode = parentCode;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

//	public Set<Role> getRole() {
//		return role;
//	}
//
//	public void setRole(Set<Role> role) {
//		this.role = role;
//	}

	public int getDeptLevel() {
		return deptLevel;
	}

	public void setDeptLevel(int deptLevel) {
		this.deptLevel = deptLevel;
	}
	
	public Set<Users> getUsers() {
		return users;
	}

	public void setUsers(Set<Users> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		if (deptId == null) {
			if (other.deptId != null)
				return false;
		} else if (!deptId.equals(other.deptId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "id:["+this.getDeptId()+"],编码:["+this.getDeptCode()+"],名称:["+this.getDeptName()+"],level:["+this.getDeptLevel()+"]";
	}
}
