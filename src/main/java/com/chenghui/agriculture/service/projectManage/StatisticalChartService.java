package com.chenghui.agriculture.service.projectManage;

import java.util.List;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.vo.ReportProjects;
import com.chenghui.agriculture.service.GenericService;

/**
 * 项目财务统计分析
 * @author ZGX
 *
 */

public interface StatisticalChartService extends GenericService<Projects, Long> {
	/**
	 * 查询所有镇级别的资金使用
	 */
	List<ReportProjects> findByProjectOfTown( int fund_year);
	
	/**
	 *获取乡镇，村
	 */
	List<Area> getDepartListBySort_ForReport(Integer sort,Long fzid, ShiroUser shiroUser);
	
	/**
	 * 获取乡镇中每个村，在同一个年度的统计分析
	 */
	List<ReportProjects> getReportByTown(Long townId,String year);
	
	/**
	 * 按村统计分析
	 */
	List<Projects> getReportByVillage(Long townId,Long villageId,String year);
	
	/**
	 * 统计产业资金占比
	 */
	List<ReportProjects> getReportByIndustry(Integer year);
	
	/**
	 * 统计项目的个数
	 */
	List<ReportProjects> getReportByProjectsCount(Integer year);
	
	/**
	 * 同一个项目多个村实施统计
	 */
	List<Projects> getBySameProjectName(Long townId,Long projectNameId);
	/**
	 * 统计已完成的项目
	 */
	List<Projects> reportForProjectsFinlished(Long townId,Long villageId,String year);
	
	/**
	 * 根据镇统计村中的项目资金
	 */
	List<ReportProjects> reportByVillageFinanceCount(String town,Integer year);
	
}
