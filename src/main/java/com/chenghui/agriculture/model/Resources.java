package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

@SuppressWarnings("serial")
@JsonIgnoreProperties(value={"role"})
@Entity
public class Resources implements Serializable {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique=true)
	private String resourceName;
	
	@Column(unique=true)
	private String resourceCode;
	
	private String resourceType;
	
	private Long parentId;

	@Transient
	private Boolean inUse;

	@ManyToMany(mappedBy="resources",fetch = FetchType.LAZY)
	private Set<Role> role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public Set<Role> getRole() {
		return role;
	}

	public void setRole(Set<Role> role) {
		this.role = role;
	}

	
	public Boolean getInUse() {
		return inUse;
	}

	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Override
	public String toString() {
		return "ID:["+this.getId()+"],编码:["+this.getResourceCode()+"],名称:["+this.getResourceName()+"],类型:["+this.getResourceType()+"]";
	}
	
	/**
	 * 重载hashCode,只计算resourceCode;
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(resourceCode);
	}

	/**
	 * 重载equals,只计算resourceCode;
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Resources other = (Resources) obj;
		if (resourceCode == null) {
			if (other.resourceCode != null) {
				return false;
			}
		} else if (!resourceCode.equals(other.resourceCode)) {
			return false;
		}
		return true;
	}
}
