package com.chenghui.agriculture.dao.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.dao.DataAccessException;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.AreaDao;
import com.chenghui.agriculture.dao.system.UserDao;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.Users;



@SuppressWarnings("unchecked")
@Repository
public class AreaDaoImpl extends GenericHibernateDao<Area, Long> implements AreaDao {

	@Autowired
	UserDao userDao;
	
	private static final Map<Long, Area> areaMap = new HashMap<>(); 
	
	@Override
	public List<Area> getChildren(Area area) throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.eq("parentCode", area.getId()));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Area>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	@Override
	public Area getOrganizationByOrgName(Area area) throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.eq("areaName", area.getAreaName()));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Area> organizations = (List<Area>) getHibernateTemplate().findByCriteria(detachedCriteria);
		if (organizations == null || organizations.isEmpty()) {
			return null;
		}
		return  organizations.get(0);
	}
	
//	@Override
//	public List<Area> findAll() throws DataAccessException {
//		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
//		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
//		Users user = findUserByLoginName(shiroUser.getLoginName());
//		Set<Area> areaSet = user.getArea();
//		
//		if(!shiroUser.isAdmin){
//			detachedCriteria.add(Restrictions.in("areaName", areaSet));
//		}
//		detachedCriteria.add(Restrictions.or(Restrictions.eq("areaLevel", 2),Restrictions.eq("areaLevel", 3)));
//		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		detachedCriteria.addOrder(Order.asc("id"));
//		return  (List<Area>) getHibernateTemplate().findByCriteria(detachedCriteria);
//	}
	
	@Override
	public List<Area> findParentOrganizations() throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.or(Restrictions.eq("areaLevel", 2),Restrictions.eq("areaLevel", 3)));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		detachedCriteria.addOrder(Order.asc("id"));
		return  (List<Area>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	@Override
	@Cacheable(value="allEnabledAreas")
	public List<Area> findParentOrganization(String areaName) throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
//		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
//		Users user = findUserByLoginName(shiroUser.getLoginName());
//		Set<Area> areaSet = user.getArea();
//		detachedCriteria.createAlias("area", "area");
//		if(!shiroUser.isAdmin){
//			detachedCriteria.add(Restrictions.in("area", areaSet));
//		}
		detachedCriteria.add(Restrictions.like("areaName", areaName, MatchMode.ANYWHERE));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Area> departments=  (List<Area>)getHibernateTemplate().findByCriteria(detachedCriteria);
		for(Area department : departments){
			if(department.getParentCode() != null){
				List<Area> dept = (List<Area>)findByLongColumn("id", department.getParentCode());
				
				for(Area department1 : dept){
						List<Area> dept2 = (List<Area>)findByLongColumn("id", department1.getParentCode());
						dept2.add(department);
						dept2.add(department1);
						return dept2;
				}
				dept.add(department);
				return dept;
			}
			
		}
		return departments;
	}
	
//	@Override
//	public List<Area> findArea() throws DataAccessException {
//		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
//		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
//		Users user = findUserByLoginName(shiroUser.getLoginName());
//		Set<Area> areaSet = user.getArea();
//	//	detachedCriteria.createAlias("area", "area");
//		if(!shiroUser.isSuperAdmin){
//			//detachedCriteria.add(Restrictions.eq("areaName","贵阳领翊新村"));
//			detachedCriteria.add(Restrictions.in("area", areaSet));
//			detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//			List<Area> departments1=  (List<Area>)getHibernateTemplate().findByCriteria(detachedCriteria);
//			for(Area department : departments1){
//				if(department.getParentCode() != null){
//					List<Area> dept = (List<Area>)findByLongColumn("id", department.getParentCode());
//					
//					for(Area department1 : dept){
//							List<Area> dept2 = (List<Area>)findByLongColumn("id", department1.getParentCode());
//							dept2.add(department);
//							dept2.add(department1);
//							return dept2;
//					}
//					dept.add(department);
//					return dept;
//				}
//				
//			}
//			return departments1;
//		}else{
//		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		List<Area> departments = (List<Area>)getHibernateTemplate().findByCriteria(detachedCriteria);
//		
//		return departments;
//		}
//	}
	
	@Override
	@Cacheable(value="allAreas")
	public List<Area> findArea() throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Area> departments = (List<Area>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return departments;
	}
	
//	@Override
//	public List<Area> findIsNotSuperAdmin(ShiroUser shiroUser,Set<Area> areaSet) throws DataAccessException {
////		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
//		List<Area> areaList = new ArrayList<>();
//		Set<Area> areaSet1 = new HashSet<Area>();
//		areaSet1.addAll(areaSet);
//		for(Area area : areaSet){
//			areaSet1.addAll(getAreaTreeList(area));
//		}
//		areaList.addAll(areaSet1);
//		return areaList;
////		Users user = findUserByLoginName(shiroUser.getLoginName());
////		Set<Area> areaSet = user.getArea();
////		List<String> list=new ArrayList<String>();
////		Set<Area> list1=new HashSet<Area>();
////		List<Area> list2=new ArrayList<Area>();
////		List<Long> listParentCode = new ArrayList<Long>();
////		for(Area area : areaSet){
////			listParentCode.add(area.getParentCode()) ;
////			list.add(area.getAreaName());
////			
////		}
////			detachedCriteria.add(Restrictions.in("areaName", list));
////			detachedCriteria.add(Restrictions.eq("areaLevel", 1));
////			detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
////			List<Area> departments1=  (List<Area>)getHibernateTemplate().findByCriteria(detachedCriteria);
////			List<Area> dept2 = null;
////			for(Area department : departments1){
////				for(Long parentCode : listParentCode){
////					if(department.getParentCode().equals(parentCode)){
////						List<Area> dept = (List<Area>)findByLongColumn("id", department.getParentCode());
////						list1.add(department);
////						for(Area department1 : dept){
////							    dept2 = (List<Area>)findByLongColumn("id", department1.getParentCode());
////							    list1.add(department1);
////							    for(Area department2 : dept2){				
////									list1.add(department2);	
////								}	
////						}
////					}
////				}
////			}
////			list2.addAll(list1);
////			return  list2;
//	}
	
	
	
	
	/**
	 * 根据登录名查找
	 * @param loginName
	 * @return
	 */
	private Users findUserByLoginName(String loginName) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
		detachedCriteria.add(Restrictions.eq("userName", loginName));
		List<Users> userl = new ArrayList<Users>();
		userl = userDao.findByCriteria(detachedCriteria);
		if(userl==null||userl.size()==0){
			return null;
		}else {
			userl.get(0).getRole();
			Hibernate.initialize(userl.get(0).getArea());
		}
		
		return userl.get(0);
	}
	
	@Override
	@Cacheable(value="areaLevel")
	public List<Area> findOrganizationByOrgLevel(int level) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		
		if(level == 3){
			level = 2;
		}
		
		detachedCriteria.add(Restrictions.eq("areaLevel", level));
		
		return findAllByCriteria(detachedCriteria);
	}

	/**
     * 根据传过来的areaId获得它的所有子节点用于项目添加时地区的选项下来列表
     * @param areaId
     * @author LLJ
     */
    public List<Area> findAreaChildList(Area area){
    	List<Area> areaList = new ArrayList<Area>();
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.eq("parentCode", area.getId()));
		detachedCriteria.add(Restrictions.between("areaLevel", 1, 3));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Area> list = (List<Area>) getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list.isEmpty()){
			return areaList;
		}
		areaList.addAll(list);
        for(Area a: list) {
        	areaList.addAll(findAreaChildList(a));
        }
        return areaList;
    }
    
    
	@Override
	public List<Area> findAllArea(Long areaId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.eq("id", areaId));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Area> list=  (List<Area>)getHibernateTemplate().findByCriteria(detachedCriteria);
		for(Area area : list){
			if(area.getAreaCode() != null){
				List<Area> listarea = (List<Area>)findByLongColumn("id", area.getParentCode());
				for(Area area1 : listarea){
						List<Area> area2 = (List<Area>)findByLongColumn("id", area1.getParentCode());
						area2.add(area);
						area2.add(area1);
						return area2;
				}
				listarea.add(area);
				return listarea;
			}
			
		}
		return list;
	}

	
	@Override
	public List<Area> getArea(Long id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.eq("id", id));
		detachedCriteria.add(Restrictions.eq("areaLevel", 1));
		List<Area> list=  (List<Area>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}

	@Override
	public List<Area> findByLevel(Integer sort, Long fzid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.eq("parentCode", fzid));
		detachedCriteria.add(Restrictions.eq("areaLevel", sort));
		List<Area> list=  (List<Area>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}

	@Override
	public List<Area> getByParentCode(Long townId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.eq("parentCode", townId));
		detachedCriteria.addOrder(Order.asc("id"));
		List<Area> list=  (List<Area>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}
	
	/**
	 * 根据地区编码查找是否存在
	 */
	@Override
	public Area findByAreaCode(Area area) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.eq("areaCode", area.getAreaCode()));
		List<Area> list = (List<Area>)getHibernateTemplate().findByCriteria(detachedCriteria);
		if(list.size()==0) {
			return null;
		}
		for (Area u : list) {
			return u;
		}
		return null;
	}
	
	@Override
	public boolean isExistByAreacode(Area area) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.eq("areaCode", area.getAreaCode()));
		List<Area> list = (List<Area>) getHibernateTemplate().findByCriteria(detachedCriteria);
		return (list==null||list.isEmpty())?false:true;
	}
}
