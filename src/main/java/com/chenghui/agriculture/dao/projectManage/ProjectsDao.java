package com.chenghui.agriculture.dao.projectManage;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.vo.ProjectsSumTotalFund;
import com.chenghui.agriculture.model.vo.ReportProjects;


/**
 * 项目dao接口
 * @author LLJ
 *
 */
public interface ProjectsDao extends GenericDao<Projects, Long>{
	void executeLoad(String loadSql);
	
	List<Projects> getByProjects(Projects projects, ShiroUser shiroUser);
	/**
	 * 初始化
	 * @param xxnr
	 * @return
	 * @author LLJ
	 */
	List<Projects> findAllInit();
	
	/**
	 * 初始化
	 * @param xxnr
	 * @return
	 * @author LLJ
	 */
	List<Projects> findAllInit(String xxnr);
	
	List<ProjectsSumTotalFund> findAllTotalFund(Long pid);
	
	/**
	 * 调项记录
	 * @param xxnr
	 * @param adjustmentPid
	 * @return
	 * @author LLJ
	 */
	List<Projects> findAllAdjustmentRecord(Long adjustmentAreaId,String projectName);
	/**
	 * 查询所有
	 * @param areaId
	 * @return
	 * @author LLJ
	 */
	List<Projects> findAllProjects(Long areaId,int limit, int start);
	
	/**
	 * 
	 * @param parentId
	 * @return
	 * 
	 */
	List<Projects> findProjectParent(Long parentId);
	
	Projects findProject(int projectNumber);
	
	/**
	 * 模糊查询方法
	 * @param projectName
	 * @param areaId
	 * @return
	 * @throws DataAccessException
	 * @author LLJ
	 */
	List<Projects> findParentProjects(String xxnr) throws DataAccessException;

	/**
	 * 获得所有子节点
	 * @param projects
	 * @return
	 * @author LLJ
	 */
	List<Projects> getChild(Projects projects);
	
	List<Projects> exportExcelList(Projects p);
	
	List<Long> add(List<Projects> models) throws DataAccessException;
	
	Projects findByProjectNumber(String projectNumber);
	
	/**
	 * 查询与村相关数据
	 * @param areaId
	 * @return
	 * @author LLJ
	 */
	List<Projects> findImportProjects(Long areaId);
	
	/**
	 * 查询与县相关数据
	 * @param areaId
	 * @return
	 */
	List<Projects> findCountyLevelCityId(Long areaId);
	
	/**
	 * 查询与镇/乡相关数据
	 * @param areaId
	 * @return
	 */
	List<Projects> findTownId(Long areaId);
	
	/*
	 * 查询每个镇的项目资金 
	 */
	List<ReportProjects> findByProjectsOfTown( int fund_year);
	
	/*
	 * 获取乡镇中，每个村，在同一个年度的统计分析
	 */
	List<ReportProjects> getReportByTown(String town,String year);
	
	/**
	 * 查询产业统计数据
	 */
	List<ReportProjects> getReportByIndustry(Integer year);
	/**
	 * 指定村统计
	 */
	List<Projects> getReportByVillage(Long townId,Long villageId,String year);
	
	/**
	 * 统计项目的个数
	 */
	List<ReportProjects> getReportByProjectCount(Integer year);
	/**
	 * 根据乡镇名称查询所有的村
	 * @author zgx
	 * @param town 乡镇名称
	 */
	List<ReportProjects> findByTown(String town,Integer year);
	/**
	 * 查询多个村镇实施的同一个项目
	 * @author zgx
	 */
	List<Projects> findbyOneToMany(Long townId);
	/**
	 * 按项目名称统计
	 * @author zgx
	 * @param area 地区
	 * @param projectNameId 项目ID
	 */
	List<Projects> getBySameProjectName(Area area,Long projectsNameId);
	
	/**
	 * 按村统计已完成项目
	 * @author zgz
	 * @param townId 乡镇ID
	 * @param villageId 乡村Id
	 * @param year 统计年份
	 */
	List<Projects> reportForProjectsFinlished(Long townId, Long villageId, String year);
	
	/**
	 * 
	 * @param town 乡镇名称
	 * @param year 查询年份
	 * @return
	 */
	List<ReportProjects> reportByVillageFinanceCount(String town,Integer year);

	
	List<Projects> findAllNormalProjects();
	
	/**
	 * 项目数据的导出
	 */
	List<Projects> exportProjects(ShiroUser shiroUser);
}
