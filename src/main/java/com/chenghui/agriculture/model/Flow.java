package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
@DynamicUpdate
@SelectBeforeUpdate
@JsonIgnoreProperties(value={"flowNodes"})
public class Flow implements Serializable{
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String flowName;
	
	@ManyToOne
	private FlowType flowType;
	
	@Transient
	private Long flowTypeId;
	
	private String description;
	
	private Boolean isInUse;
	
	@Column(name="flag")
	private int flag; //0:删除 1:正常
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="flow")
	@OrderBy("indexs")
	private List<FlowNode> flowNodes = new ArrayList<FlowNode>();
	
	private String createUser;//创建用户
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date createDate;//创建日期
	
	private String updateUser;//修改用户
	
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date updateDate;//修改日期
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<FlowNode> getFlowNodes() {
		return flowNodes;
	}

	public void setFlowNodes(List<FlowNode> flowNodes) {
		this.flowNodes = flowNodes;
	}

	public Boolean getIsInUse() {
		return isInUse;
	}

	public void setIsInUse(Boolean isInUse) {
		this.isInUse = isInUse;
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

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public FlowType getFlowType() {
		return flowType;
	}

	public void setFlowType(FlowType flowType) {
		this.flowType = flowType;
	}

	public Long getFlowTypeId() {
		return flowTypeId;
	}

	public void setFlowTypeId(Long flowTypeId) {
		this.flowTypeId = flowTypeId;
	}


	public FlowNode ggetFirstNode() {
		if (flowNodes == null || flowNodes.size() == 0) return null;
		return flowNodes.get(0);
	}

	public FlowNode ggetNextNodeByStep(Long stepIndex) {
		if (flowNodes.isEmpty()) return null;
		
		for (FlowNode node : flowNodes){
			if (node.getIndexs() <= stepIndex){
				continue;
			} else if (node.getIndexs() > stepIndex){
				return node;
			}
		}
		return null;
	}
	
	/**
	 * 获取参数节点的前一个节点,如果参数节点是null或者已经是第一个节点，则返回null
	 * @param node
	 * @return FlowNode
	 */
	public FlowNode getPreFlowNode(FlowNode node){
		if (node == null || flowNodes.isEmpty()) {
			return null;
		}
		if (isFirstNode(node)) {
			return null;
		}
		for (int i = 0; i < flowNodes.size(); i++) {
			if (flowNodes.get(i).equals(node)) {
				if (i>0) {
					return flowNodes.get(i-1);
				}
			}
		}
		return null;
	}
	
	/**
	 * 判断是否是第一个节点
	 * @param node
	 * @return boolean
	 */
	public boolean isFirstNode(FlowNode node) {
		if (node == null || flowNodes.isEmpty()) return false;
		return node.equals(flowNodes.get(0));
		
	}
	
	/**
	 * 判断是否是最后一个节点
	 * @param node
	 * @return boolean
	 */
	public boolean isLastNode(FlowNode node) {
		if (node == null || flowNodes.isEmpty()) return false;
		return node.equals(flowNodes.get(flowNodes.size() - 1));
	}
	@Override
	public String toString() {
		return "ID:["+this.getId()+"],流程名称:["+this.getFlowName()+"],类型:["+this.getFlowType().getTypeNameCN()+"],描述:["+this.getDescription()+"],状态:["+(this.getIsInUse()?"可用":"不可用")+"]";
	}
	
}
