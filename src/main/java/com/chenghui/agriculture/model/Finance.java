package com.chenghui.agriculture.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * 财务
 * @描述：财务id,子项目,拨款金额，报账金额，凭证号，填写时间，经手人
 * @author Ricky
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
@Entity
@JsonAutoDetect
@DynamicUpdate
@SelectBeforeUpdate
public class Finance implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	
	private Long projects_id;	//子项目id

	private Double appropriation;	//拨款金额（万元）

	private String certificateNum;	//凭证号
	
	private String bk_date;				//拨款时间
	
	private String bk_user;		//拨款人
	
	
	private Double account;			//报账金额（万元）
	
	private String bz_user;		//报账
	
	private String bz_date;		//报账日期
	
	private String remark;			//备注

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjects_id() {
		return projects_id;
	}

	public void setProjects_id(Long projects_id) {
		this.projects_id = projects_id;
	}

	public Double getAppropriation() {
		return appropriation;
	}

	public void setAppropriation(Double appropriation) {
		this.appropriation = appropriation;
	}

	public String getCertificateNum() {
		return certificateNum;
	}

	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}

	public String getBk_date() {
		return bk_date;
	}

	public void setBk_date(String bk_date) {
		this.bk_date = bk_date;
	}

	public String getBk_user() {
		return bk_user;
	}

	public void setBk_user(String bk_user) {
		this.bk_user = bk_user;
	}

	public Double getAccount() {
		return account;
	}

	public void setAccount(Double account) {
		this.account = account;
	}

	public String getBz_user() {
		return bz_user;
	}

	public void setBz_user(String bz_user) {
		this.bz_user = bz_user;
	}

	public String getBz_date() {
		return bz_date;
	}

	public void setBz_date(String bz_date) {
		this.bz_date = bz_date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Finance [id=" + id + ", projects_id=" + projects_id + ", appropriation=" + appropriation
				+ ", certificateNum=" + certificateNum + ", bk_date=" + bk_date + ", bk_user=" + bk_user + ", account="
				+ account + ", bz_user=" + bz_user + ", bz_date=" + bz_date + ", remark=" + remark + "]";
	}
}
