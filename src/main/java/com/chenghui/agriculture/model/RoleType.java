package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value = {"" })

public class RoleType implements Serializable {
	
	public static final int ROLE_LEVEL_ADMIN = 1;//"系统管理员";
	public static final int ROLE_LEVEL_COMMON = 2;//"普通角色";
	public static final int ROLE_LEVEL_ASSESSOR = 3;//"审核权限";

	@Id
	@GeneratedValue
	private Long id;
	
	private String typeName;
	private int typeKey;
	
	public int getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(int typeKey) {
		this.typeKey = typeKey;
	}

	private String typeDesc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((typeDesc == null) ? 0 : typeDesc.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
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
		RoleType other = (RoleType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (typeDesc == null) {
			if (other.typeDesc != null)
				return false;
		} else if (!typeDesc.equals(other.typeDesc))
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RoleType [id=" + id + ", typeName=" + typeName + ", typeDesc=" + typeDesc + "]";
	}

	
}
