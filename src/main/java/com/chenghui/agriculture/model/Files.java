package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * 文件实体
 * @author LLJ
 *
 */
@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@DynamicUpdate
@SelectBeforeUpdate
public class Files implements Serializable {

	/**
	 * id
	 */
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 文件名
	 */
	private String fileName;
	
	/**
	 * 文件路径
	 */
	private String path;

	@ManyToOne
	private SubProject subPorject;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public SubProject getSubPorject() {
		return subPorject;
	}

	public void setSubPorject(SubProject subPorject) {
		this.subPorject = subPorject;
	}

	@Override
	public String toString() {
		return "Files [id=" + id + ", fileName=" + fileName + ", path=" + path + "]";
	}

}
