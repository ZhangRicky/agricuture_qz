package com.chenghui.agriculture.service.projectManage;



import java.util.List;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.PointBaidu;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.vo.ProjectsSumTotalFund;
import com.chenghui.agriculture.model.vo.ReportProjects;
import com.chenghui.agriculture.service.GenericService;

/**
 * 项目业务接口
 * @author LLJ
 * @version V1.0
 */
public interface ProjectsService extends GenericService<Projects, Long>{
	
	/**
	 * 根据条件查询所有项目
	 * @param projects
	 * @return
	 * @author mars
	 */
	List<Projects> getByProjects(Projects projects, ShiroUser shiroUser);
	
	/**
	 * 初始化项目
	 * @param xxnr
	 * @return
	 * @author LLJ
	 */
	List<Projects> getAllInit();
	
	/**
	 * 初始化项目
	 * @param xxnr
	 * @return
	 * @author LLJ
	 */
	List<Projects> getAllInit(String xxnr);
	
	/**
	 * 调项记录
	 * @param xxnr
	 * @param adjustmentPid
	 * @return
	 * @author LLJ
	 */
	List<Projects> getAllAdjustmentRecord(Long adjustmentAreaId,String projectName);
	/**
	 * 查询所有
	 * @param areaId
	 * @return
	 * @author LLJ
	 */
	List<Projects> getAllProjects(Long areaId);
	
	List<Area> findAllArea(Long areaId);
	 
	List<Projects> findProjectParent(Long parentId);
	
	Projects updateProject(String projectNumber,Long projectDetailId);
	
	List<ProjectsSumTotalFund> findTotalFund(Long pid);
	
	Projects getProjectsTotalFund(Long updateParentId);
	
	/**
	 * 模糊查询
	 * @param projectName
	 * @param areaId
	 * @author LLJ
	 * @return
	 */
	List<Projects> getParentProjects(String xxnr);
	
	List<Projects> exportExcelList(Projects p,ShiroUser shiroUser);

	Projects updateRemark(Projects projects);

	/**
	 * 项目调项
	 * @param projects
	 * @return
	 */
	Long addAdjustment(Projects projects);
	/**
	 * 添加子项目
	 * @param projects
	 * @return
	 * @author LLJ
	 */
	Long addProjects(Projects projects);
	
	/**
	 * 修改子项目
	 * @param projects
	 * @param subPid
	 * @return
	 * @author LLJ
	 */
	Projects updateProjectsInfo(Projects projects);

	
	
	
	void updateSubproject(Long Pid,String bzAcount);
	
	/**
	 * 根据参数查询项目
	 * @param project
	 * @return
	 */
	PaginationSupport<Projects> appFindByParams(Projects projects, ShiroUser shiroUser);
	
	/**
	 * 根据parentId查询所有子项目
	 * @param parentId
	 * @return
	 */
	List<Projects> findSubProjectsByParentId(Long parentId);

	/**
	 * 根据parentId查询所有叶子节点项目（叶子节点）
	 * @param parentId
	 * @return
	 */
	List<Projects> appFindLeafProjectsByParentId(Long parentId);
	
	/*
	 * 窗口关闭时。更新项目列表中的报账。拨款的统计数据数据
	 */
	void updateByAccount(Long subId,double financeAccount, double financeaApropriation);
	
	/*
	 * 拨款[确认]按钮，添加‘拨款金额’到项目数据中
	 */
	void addByBKTotal(Projects p);
	 
	/*
	 * 报账[确认]按钮，添加‘报账率’，‘报账金额’到项目数据中
	 */
	void addFinancebiLv(Projects p);
	
	/*
	 * 删除财务记录时。更新项目的'【总拨款】/【总报账】【报账率】'
	 */
	void updateByTotalBKBZ(Projects p);
	/*
	 * 填写资金[结余] 
	 */
	void updateByBalance(Projects pro);
	/*
	 * 根据镇查询所有的村
	 */
	List<ReportProjects> findByTown(String town,Integer year);
	
	/*
	 * 查询一个项目，多个不同地区的农户实施
	 */
	List<Projects> findbyOneToMany(Long townId);
	
	/**
	 * 财务的审核
	 */
	void updateByapproverStatus(Long projectsID);
	
	/**
	 * 单个添加鹰眼接口返回的坐标
	 */
	Long addPointBaidu(PointBaidu model);
	
	/**
	 * 批量添加鹰眼接口返回的坐标
	 */
	void addPointBaiduList(List<PointBaidu> pointBaidus, Long checksId);
	
	/**
	 * 查询所有有效的projects，并且设置parent和children对象
	 * @return List<Projects>
	 */
	List<Projects> findAllNormalProjects();
	
	/**
	 * 更新总的报账/报账记录
	 * @author zgx
	 */
	void updateByTotal(Projects p);
	
	List<Projects> getChildList(Projects p);
	
}
