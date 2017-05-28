package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SelectBeforeUpdate;

import com.chenghui.agriculture.core.utils.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@SelectBeforeUpdate
public class FlowNode implements Serializable,Comparable<FlowNode> {
	
	@Id
	@GeneratedValue
	private Long id; //主键
	
	@ManyToOne
	private Flow flow; //所属流程
	
	private String nodeName;//节点名称
	
	@ManyToOne
	private Role roles; //审核角色
	
	private int indexs; //审核顺序
	
	private int flag; //0:删除，1：正常

	private String createUser;//创建用户
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date createDate;//创建日期
	
	private String updateUser;//修改用户
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date updateDate;//修改日期
	
	private String description; //描述

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Role getRoles() {
		return roles;
	}

	public void setRoles(Role roles) {
		this.roles = roles;
	}

	public int getIndexs() {
		return indexs;
	}

	public void setIndexs(int indexs) {
		this.indexs = indexs;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
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
		FlowNode other = (FlowNode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ID:["+this.getId()+"],所属流程:["+this.getFlow().getFlowName()+"],审核角色:["+this.getRoles().getRoleName()+"],审核顺序:["+this.getIndexs()+"]";
	}

	@Override
	public int compareTo(FlowNode o) {
		if(this.getIndexs() < o.getIndexs()){
			return -1;
		} else if(this.getIndexs() == o.getIndexs()){
			return 0;
		}
		return 1;
	}
}
