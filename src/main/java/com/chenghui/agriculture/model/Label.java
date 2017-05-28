package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value={})
@DynamicUpdate
@SelectBeforeUpdate
public class Label implements Serializable {

	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * 设置文本标注的内容。支持HTML。
	 */
	private String content;
	
	/**
	 * 设置文本标注坐标。仅当通过Map.addOverlay()方法添加的文本标注有效。(自 1.2 新增)
	 */
	@ManyToOne
	private Point point;
	
	/**
	 * 文本标注的标题
	 */
	private String title;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Label(String content, Point point, String title) {
		super();
		this.content = content;
		this.point = point;
		this.title = title;
	}

	@Override
	public String toString() {
		return "Label [content=" + content + ", point=" + point + ", title=" + title + "]";
	}
	
}
