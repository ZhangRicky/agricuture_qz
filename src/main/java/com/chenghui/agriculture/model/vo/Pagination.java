package com.chenghui.agriculture.model.vo;

import java.util.List;

/**
 * 前端页面分页数据
 * 临时解决方案，为使服务器查询结果命名与DataTables插件保持一致
 * @author yudq
 * @param <M>
 *
 */
public class Pagination<M> {
	private int iDisplayStart;

	private String iTotalRecords;

	private String iTotalDisplayRecords;

	private List<M> aaData;

	public int getiDisplayStart() {
		return iDisplayStart;
	}

	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public String getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public List<M> getAaData() {
		return aaData;
	}

	public void setAaData(List<M> aaData) {
		this.aaData = aaData;
	}
}
