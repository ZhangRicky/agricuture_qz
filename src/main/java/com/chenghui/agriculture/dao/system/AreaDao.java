package com.chenghui.agriculture.dao.system;

import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Area;




public interface AreaDao extends GenericDao<Area, Long>{

	@Override
	List<Area> findAll() throws DataAccessException;
	
	List<Area> findArea() throws DataAccessException;
	
//	List<Area> findIsNotSuperAdmin(ShiroUser shiroUser,Set<Area> areaSet) throws DataAccessException;

	List<Area> findParentOrganizations() throws DataAccessException;
	
	List<Area> findParentOrganization(String areaName) throws DataAccessException;
	List<Area> findAllArea(Long areaId);
	@Override
	List<Area> findByHQL(String hql,Object... values) throws DataAccessException;
	
	@Override
	Area get(Long id) throws DataAccessException;
	
	List<Area> findOrganizationByOrgLevel(int organizationLevelShequ);
	
	Area getOrganizationByOrgName(Area area);
	
	List<Area> getChildren(Area area);
	
	/**
     * 根据传过来的areaId获得它的所有子节点用于项目添加时地区的选项下来列表
     * @param areaId
     * @author LLJ
     */
	List<Area> findAreaChildList(Area area);
	
	List<Area> getArea(Long id);
	
	/**
	 * 查询所有的镇级别的地区
	 */
	List<Area> findByLevel(Integer sort, Long fzid);
	
	/**
	 * 根据id查询所以的子级地区
	 */
	List<Area> getByParentCode(Long townId);
	
	Area findByAreaCode(Area area);
	
	boolean isExistByAreacode(Area area);

}
