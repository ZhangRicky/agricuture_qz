package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value = { "user"})
public class Role implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique = true)
	private String roleName;
	
	@ManyToOne
	private RoleType roleType;
	
	@Transient
	private long roleTypeId;

	private String roleDesc;

	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@OrderBy("id ASC")
	private Set<Resources> resources = new HashSet<Resources>();

	@ManyToMany(mappedBy="role", fetch = FetchType.LAZY)
	private Set<Users> users;

//	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
//	@OrderBy("id ASC")
//	private Set<Area> area;
	
//	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
//	@OrderBy("id ASC")
//	private Set<Department> department;
	
	private String createRoleName;
	
	private String createUserName;
	
	private Date created;
	
	@Transient
	private String check;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<Resources> getResources() {
		return resources;
	}

	public void setResources(Set<Resources> resources) {
		this.resources = resources;
	}

	public Set<Users> getUsers() {
		return users;
	}

	public void setUsers(Set<Users> users) {
		this.users = users;
	}

	public Role addResource(Resources resources) {
		this.resources.add(resources);
		return this;
	}

	public Role removeResource(Resources resources) {
		this.resources.remove(resources);
		return this;
	}

//	public Set<Area> getArea() {
//		return area;
//	}
//
//	public void setArea(Set<Area> area) {
//		this.area = area;
//	}

	public String getCreateRoleName() {
		return createRoleName;
	}

	public void setCreateRoleName(String createRoleName) {
		this.createRoleName = createRoleName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public long getRoleTypeId() {
		return roleTypeId;
	}

	public void setRoleTypeId(long roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

//	public Set<Department> getDepartment() {
//		return department;
//	}
//
//	public void setDepartment(Set<Department> department) {
//		this.department = department;
//	}
	
	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
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
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ID:["+this.getId()+"],角色类型:["+this.getRoleType()+"],角色名称:["+this.getRoleName()+"],描述:["+this.getRoleDesc()+"],创建用户:["+this.getCreateUserName()+"]";
	}
}
