package com.chenghui.agriculture.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.chenghui.agriculture.core.utils.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author yudq
 * @since V1.0
 * @date 2016-03-02 08:19:00
 */
@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(value={"projectImportDetails"})
public class ProjectImport implements Serializable{
	
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 导入时使用的模版
	 */
//	@ManyToOne
	@Transient
	private Template template;
	
	@Transient
	private String templateName;
	
	private String fileName;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="projectImport",fetch=FetchType.LAZY)
	private List<ProjectImportDetail> projectImportDetails = new ArrayList<ProjectImportDetail>();
	
	@Transient
	private List<Projects> projects = new ArrayList<Projects>();
	
	/**
	 * 导入时间
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date importDate;
	
	/**
	 * 操作者
	 */
	@ManyToOne
	private Users operator;
	
	/**
	 * 导入状态 1:成功 2:验证失败 3:没有权限
	 */
	private int importStatus;
	
	/**
	 * 导入数据集中，验证通过的数目
	 */
	private int successCount;
	
	private int failCount;
	
	/**
	 * 新增数目
	 */
	private int addCount;
	
	/**
	 * 修改数目
	 */
	private int updateCount;
	
	/**
	 * 处理时间
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date handleDate;
	
	/**
	 * 处理时间
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date lastUpdateDate;
	
	private boolean isDeleted;
	
	/**
	 * 处理状态
	 */
	private int handleStatus;
	
	@Transient
	private Date searchStartTime;
	
	@Transient
	private Date searchEndTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public int getImportStatus() {
		return importStatus;
	}

	public void setImportStatus(int importStatus) {
		this.importStatus = importStatus;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public Date getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}

	public Users getOperator() {
		return operator;
	}

	public void setOperator(Users operator) {
		this.operator = operator;
	}

	public int getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(int handleStatus) {
		this.handleStatus = handleStatus;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setProjectImportDetails(List<ProjectImportDetail> projectImportDetails) {
		this.projectImportDetails = projectImportDetails;
	}

	public List<ProjectImportDetail> getProjectImportDetails() {
		return projectImportDetails;
	}

	public List<Projects> getProjects() {
		return projects;
	}

	public void setProjects(List<Projects> projects) {
		this.projects = projects;
	}

	public void setProjects() {
		for (ProjectImportDetail projectImportDetail: projectImportDetails) {
			projects.add(projectImportDetail.getProjects());
		}
	}

	public int getAddCount() {
		return addCount;
	}

	public void setAddCount(int addCount) {
		this.addCount = addCount;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	public Date getSearchStartTime() {
		return searchStartTime;
	}

	public void setSearchStartTime(Date searchStartTime) {
		this.searchStartTime = searchStartTime;
	}

	public Date getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(Date searchEndTime) {
		this.searchEndTime = searchEndTime;
	}

	@Override
	public String toString() {
		return "项目导入 [id=" + id + ", 模版类型=" + template==null?"":template.getTemplateName() + ", 导入日期=" + importDate
				+ ", 操作员=" + operator == null ? "": operator.getRealName()
				+ ", 导入状态=" + importStatus + ", 新增数目=" + addCount + ", 修改数目=" + updateCount
				+ ", 处理日期=" + handleDate + ", 处理状态=" + handleStatus + "]";
	}
	
	
}
