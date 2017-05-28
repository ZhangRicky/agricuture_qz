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
public class Marker implements Serializable {

	@Id
	@GeneratedValue
	private long id;
	
	/**
	 * 鼠标移到marker上的显示内容。
	 */
	private String offset;
	
	/**
	 * 设置标注所用的图标对象。
	 */
	@ManyToOne
	private Icon icon;
	
	/**
	 * 设置标注的地理坐标。
	 */
	@ManyToOne
	private Point point;
	
	/**
	 * 为标注添加文本标注。
	 */
	@ManyToOne
	private Label label;
	
	/**
	 * 设置标注的标题，当鼠标移至标注上时显示此标题。
	 */
	private String title;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Marker [id=" + id + ", offset=" + offset + ", icon=" + icon + ", point=" + point + ", label=" + label
				+ ", title=" + title + "]";
	}
	
}
