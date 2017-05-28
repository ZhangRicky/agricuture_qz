package com.chenghui.agriculture.service.system.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.AreaUtil;
import com.chenghui.agriculture.dao.system.AreaDao;
import com.chenghui.agriculture.model.Area;

import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.system.AreaService;


@Service("areaService")
public class AreaServiceImpl extends GenericServiceImpl<Area, Long> implements AreaService{
	
	@Autowired
	private AreaDao areaDao;
	
	private static final Map<Long, Area> areaMap = new HashMap<>(); 
	
	@Override
	public List<Area> findAll() {
		return areaDao.findAll();
	}
	
	@Override
	public List<Area> getParentOrganizations() {
		return areaDao.findParentOrganizations();
	}
	
	@Override
	@Cacheable(value="allEnabledAreas")
	public List<Area> getParentOrganization(String areaName) {
		return areaDao.findParentOrganization(areaName);
	}
	
	@Override
	@Cacheable(value="allAreas")
	public List<Area> findArea() {
		return areaDao.findArea();
	}
	
	@Override
	@Cacheable(value="allArea")
	public List<Area> findIsNotSuperAdmin(ShiroUser shiroUser,Set<Area> areaSet){
	//	return areaDao.findIsNotSuperAdmin(shiroUser,areaSet);
			findAllArea();
			List<Area> areaList = new ArrayList<>();
			Set<Area> areaSet1 = new HashSet<Area>();
			areaSet1.addAll(areaSet);
			for(Area area : areaSet){
				areaSet1.addAll(getAreaTreeList(area));
			}
			areaList.addAll(areaSet1);
			return areaList;
		
	}
	
	public List<Area> findAllArea(){
		List<Area> areas = areaDao.findAll();
		areaMap.clear();
		for(Area area : areas){
			areaMap.put(area.getId().longValue(), area);
		}
		for(Area area : areas){
			if (area.getParentCode() !=null && area.getParentCode() > 0) {
				area.setParent(areaMap.get(area.getParentCode()));
				areaMap.get(area.getParentCode()).getChildren().add(area);
			}
		}
		return areas;
	}
	
	public Set<Area> getAreaTreeList(Area area){
		Set<Area> areaSet = new HashSet<Area>();
		areaSet.addAll(getParentList(area));
		areaSet.addAll(getChildList(area));
		return areaSet;
	}
	
	public Set<Area> getParentList(Area area) {
		Set<Area> areaSet = new HashSet<Area>();
		Area parentArea = areaMap.get(area.getId()).getParent();
		if(parentArea == null){
			return areaSet;
		}
		areaSet.add(parentArea);
		if(parentArea.getParentCode() !=null && parentArea.getParentCode()>0){
			areaSet.addAll(getParentList(parentArea));
		}
		return areaSet;
	}
	
	public Set<Area> getChildList(Area area) {
		Set<Area> areaSet = new HashSet<Area>();
    	List<Area> list = areaMap.get(area.getId()).getChildren();
		if(list.isEmpty()){
			return areaSet;
		}
		areaSet.addAll(list);
        for(Area p: list) {
        	areaSet.addAll(getChildList(p));
        }
        
        return areaSet;
    }
	
	@Override
	public List<Area> findByName(String queryColumn, String columnValue) {
		return areaDao.findByName(queryColumn, columnValue);
	}
	
	@CacheEvict(value={"areas","allEnabledAreas","allAreas","allArea"},allEntries=true)
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.AREA_CN, opType=OperationType.Add, opText="增加地区")
	public Long addOrganization(Area area) {
		
			try {
				Long parentCode = area.getParentCode();
				if (parentCode == null) {
					area.setAreaLevel(3);//如果父节点是空则子节点是区（县）节点
				}else{
					Area parentDepartment = areaDao.get(parentCode);
					if (parentDepartment.getAreaLevel() == 3) {
						area.setAreaLevel(2);//如果父节点是区（县）则子节点是社区（乡镇级）
					}else if (parentDepartment.getAreaLevel() == 2) {
						area.setAreaLevel(1);//如果父节点是社区则子节点是居委会（村级）
					}
				}
				areaDao.add(area);
			} catch (DataIntegrityViolationException e) {
				throw new BusinessServiceException(e.getLocalizedMessage());
			} 
			return area.getId();
		
	}
	
	@CacheEvict(value={"areas","allEnabledAreas","allAreas","allArea"},allEntries=true, key="#area.getId()")
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.AREA_CN, opType=OperationType.Update, opText="修改地区 ")
	public void updateOrganization(Area area){
//		Area area1 = areaDao.get(area.getId());
//		if(!area1.getAreaCode().equals(area.getAreaCode())){
//			if(areaDao.findByAreaCode(area) != null){
//				throw new BusinessServiceException("修改地区失败，地区已存在！");
//			}
//		}
			try {
				Long parentCode = area.getParentCode();
				Area parentDepartment = areaDao.get(parentCode);
				if (parentDepartment.getAreaLevel() == 3) {
					area.setAreaLevel(2);//如果父节点是区（县）则子节点是社区（乡镇级）
				}else if (parentDepartment.getAreaLevel() == 2) {
					area.setAreaLevel(1);//如果父节点是社区则子节点是居委会（村级）
				}
			areaDao.update(area);
		}catch(Exception e){
			throw new BusinessServiceException("修改地区失败！");
		}
		
	}
	
	@CacheEvict(value={"areas","allEnabledAreas","allAreas","allArea"},allEntries=true)
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.AREA_CN, opType=OperationType.Del, opText="删除地区")
	public void removeOrganization(Long areaId){
	
		Area area = areaDao.get(areaId);
		if (area.getAreaLevel() == 1) {//村
			areaDao.remove(area);
		}else if (area.getAreaLevel() == 2) {//镇
			List<Area> children = areaDao.getChildren(area);
			for(Area child : children){
				areaDao.remove(child);
			}
			areaDao.remove(area);
		}else if (area.getAreaLevel() == 3) { //市
			List<Area> children = areaDao.getChildren(area);
			for(Area child : children){
				List<Area> children1 = areaDao.getChildren(child);
				for(Area child1 : children1){
					areaDao.remove(child1);
				}
				areaDao.remove(child);
			}
			areaDao.remove(area);
		}
	}

//	@CacheEvict(value="areas", key="#id")
	/**
     * 根据传过来的areaId获得它的所有子节点用于项目添加时地区的选项下来列表
     * @param id
     * @author LLJ
     */
	@Override
	public List<Area> getAreaChlidList(Long id) {
		Area a = areaDao.get(id);
		List<Area> list=null;
		if (a.getAreaLevel()!=1) {
			list=areaDao.getChildren(a);
		}else{
			list = areaDao.getArea(id);
		}
		return list;
	}
	
	@Override
	@PostConstruct
	public void initAreaMap(){
		Map<String, Set<Area>> areaNameMap = AreaUtil.areaNameMap;
		Map<Long, Area> areaMap = AreaUtil.areaMap;
		List<Area> areaList = this.findAll();
		if(areaMap.isEmpty()){
			for(Area area: areaList){
				if (areaNameMap.containsKey(area.getAreaName().trim())) {
					areaNameMap.get(area.getAreaName().trim()).add(area);
				}else{
					Set<Area> areas = new HashSet<>();
					areas.add(area);
					areaNameMap.put(area.getAreaName().trim(), areas);
				}
				
				areaMap.put(area.getId(), area);
			}
		}
	}

	@Override
	public List<Area> getAreaSort(Long id) {
		List<Area> areaList = areaDao.getByParentCode(id);
		return areaList;
	}

}