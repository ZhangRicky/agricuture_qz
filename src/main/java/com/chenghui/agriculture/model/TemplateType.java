package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value = {"" })
public class TemplateType implements Serializable {
	
	public static final int TEMPLATE_TYPE_PROJECT=1; //项目模版
	
	@Id
	@GeneratedValue
	private Long id;
	
	private int typeKey;
	
	private String typeNameCN;
	
	private String typeNameEN;
	
	private String typeDesc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(int typeKey) {
		this.typeKey = typeKey;
	}

	public String getTypeNameCN() {
		return typeNameCN;
	}

	public void setTypeNameCN(String typeNameCN) {
		this.typeNameCN = typeNameCN;
	}

	public String getTypeNameEN() {
		return typeNameEN;
	}

	public void setTypeNameEN(String typeNameEN) {
		this.typeNameEN = typeNameEN;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	@Override
	public String toString() {
		return "ID:["+this.getId()+"],TYPEKEY:["+this.getTypeKey()+"],名称:["+this.getTypeNameCN()+"]";
	}
}
