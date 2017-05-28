package com.chenghui.agriculture.service.system;

import java.util.List;
import java.util.Set;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.service.GenericService;



public interface AreaService extends GenericService<Area, Long>{

	List<Area> findAll();
	@Override
	List<Area> findByName(String queryColumn, String columnValue);
	
	List<Area> getParentOrganizations();
	
	List<Area> findArea();
	
	List<Area> findIsNotSuperAdmin(ShiroUser shiroUser,Set<Area> areaSet);
	
	List<Area> getParentOrganization(String areaName);

	Long addOrganization(Area area);
	
	void updateOrganization(Area area);
	
	void removeOrganization(Long areaId);
	
	/**
     * 根据传过来的areaId获得它的所有子节点用于项目添加时地区的选项下来列表
     * @param id
     * @author LLJ
     */
	List<Area> getAreaChlidList(Long id);
	
	void initAreaMap();
	
	/**
	 * 按地区的id排序返回镇级地区
	 */
	List<Area> getAreaSort(Long id);
}
