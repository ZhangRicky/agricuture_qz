package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 地区管理
 * @author baiyq
 *
 */

@SuppressWarnings("serial")
@JsonIgnoreProperties(value = {"parent", "children"})
@Entity
public class Area implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String areaName;

	private Long areaCode;
	
	
	private int areaLevel;//3、市  2、镇 1、村
	
	/**
	 * 父级地区码,省地区码的此值为空或0
	 */
	private Long parentCode;

//	@ManyToMany(mappedBy="area",fetch = FetchType.LAZY)
//	private Set<Role> role = new HashSet<Role>();
	
	//是否使用
	private Boolean inUse;
	
	@Transient
	private Area parent = null;
	
	@Transient
	private List<Area> children = new ArrayList<>();
	
	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}

	public List<Area> getChildren() {
		return children;
	}

	public void setChildren(List<Area> children) {
		this.children = children;
	}

	public Area(String areaName, Long areaCode) {
		super();
		this.areaName = areaName;
		this.areaCode = areaCode;
	}
	
	public Area(String areaName, Long areaCode, Long parentCode) {
		super();
		this.areaName = areaName;
		this.areaCode = areaCode;
		this.parentCode = parentCode;
	}

	public Area() {
		super();
	}

	public Boolean isInUse() {
		return inUse;
	}

	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Long getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(Long areaCode) {
		this.areaCode = areaCode;
	}

	

//	public Set<Role> getRole() {
//		return role;
//	}
//
//	public void setRole(Set<Role> role) {
//		this.role = role;
//	}

	public Long getParentCode() {
		return parentCode;
	}

	public void setParentCode(Long parentCode) {
		this.parentCode = parentCode;
	}

	

	public int getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(int areaLevel) {
		this.areaLevel = areaLevel;
	}

	public Boolean getInUse() {
		return inUse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Area other = (Area) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "Area [id=" + id + ", areaName=" + areaName + ", areaCode=" + areaCode + ", areaLevel=" + areaLevel
				+ ", parentCode=" + parentCode + ", inUse=" + inUse + ", parent=" + parent + ", children=" + children
				+ "]";
	}
}
