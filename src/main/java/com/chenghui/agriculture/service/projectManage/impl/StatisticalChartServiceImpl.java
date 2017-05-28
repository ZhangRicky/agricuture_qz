package com.chenghui.agriculture.service.projectManage.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.dao.projectManage.ProjectsDao;
import com.chenghui.agriculture.dao.system.AreaDao;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.vo.ReportProjects;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.projectManage.StatisticalChartService;

/**
 * 
 * @author ZGX
 *
 */
@Service("statisticalChartService")
public class StatisticalChartServiceImpl extends GenericServiceImpl<Projects, Long> implements StatisticalChartService {
	
	@Autowired
	private ProjectsDao projectsDao;
	
	@Autowired
	private AreaDao areaDao;
	/**
	 * 查询每个镇级别的项目资金使用情况
	 */
	@Override
	public List<ReportProjects> findByProjectOfTown( int fund_year) {
		//List<Area> areas = areaDao.findAll();
		List<ReportProjects> list =projectsDao.findByProjectsOfTown(fund_year);
		return list;
	}
	
	/**
	 * 查询所有的镇
	 */
	@Override
	public List<Area> getDepartListBySort_ForReport(Integer sort, Long fzid,ShiroUser shiroUser) {
		List<Area> areas = areaDao.findByLevel(sort,fzid);
		return areas;
	}
	
	/**
	 * 获取乡镇中，每个村，在同一个年度的统计分析
	 */
	@Override
	public List<ReportProjects> getReportByTown(Long townId, String year) {
		Area area = areaDao.get(townId);
		String town = area.getAreaName();
		List<ReportProjects> list =projectsDao.getReportByTown(town,year);
		return list;
	}

	@Override
	public List<ReportProjects> getReportByIndustry(Integer year) {
		List<ReportProjects> list =projectsDao.getReportByIndustry(year);
		return list;
	}

	@Override
	public List<Projects> getReportByVillage(Long townId, Long villageId, String year) {
//		Area area = areaDao.get(townId);
//		String townName = area.getAreaName();
//		Area areaVillage = areaDao.get(villageId);
//		String villageName = areaVillage.getAreaName();
		List<Projects> list =projectsDao.getReportByVillage(townId,villageId,year);
		return list;
	}

	@Override
	public List<ReportProjects> getReportByProjectsCount(Integer year) {
		List<ReportProjects> list = projectsDao.getReportByProjectCount(year);
		return list;
	}

	@Override
	public List<Projects> getBySameProjectName(Long townId, Long projectNameId) {
		Area area = areaDao.get(townId);
		List<Projects> list =  projectsDao.getBySameProjectName(area,projectNameId);
		return list;
	}

	@Override
	public List<Projects> reportForProjectsFinlished(Long townId, Long villageId, String year) {
		
		List<Projects> list =projectsDao.reportForProjectsFinlished(townId,villageId,year);
		return list;
	}

	@Override
	public List<ReportProjects> reportByVillageFinanceCount(String town, Integer year) {
		List<ReportProjects> list = projectsDao.reportByVillageFinanceCount(town,year);
		return list;
	}
	
	

}
