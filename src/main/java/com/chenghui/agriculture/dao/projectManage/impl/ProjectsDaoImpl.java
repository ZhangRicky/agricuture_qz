package com.chenghui.agriculture.dao.projectManage.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.ProjectsDao;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.vo.ProjectsSumTotalFund;
import com.chenghui.agriculture.model.vo.ReportProjects;

/**
 * 项目dao接口的实现
 * @author LLJ
 *
 */
@SuppressWarnings("unchecked")
@Repository
public class ProjectsDaoImpl extends GenericHibernateDao<Projects, Long> implements ProjectsDao{
	
	@Override
	public void executeLoad(String loadSql) {
		getSessionFactory().getCurrentSession().createSQLQuery(loadSql).executeUpdate();
	}

	/**
	 * 
	 */
	@Override
	public List<Projects> getByProjects(Projects projects, ShiroUser shiroUser){
		boolean recursionFlag = false;
		Set<Projects> results = new HashSet<>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		if(StringUtils.isNotEmpty(projects.getProjectName())) {
			recursionFlag = true;
			detachedCriteria.add(Restrictions.like("projectName",projects.getProjectName(),MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotEmpty(projects.getFundYear())) {
			recursionFlag = true;
			detachedCriteria.add(Restrictions.eq("fundYear",projects.getFundYear()));
		}
		if(projects.getProjectType() != null && projects.getProjectType() > 0) {
			recursionFlag = true;
			detachedCriteria.add(Restrictions.eq("projectType",projects.getProjectType()));
		}
		if (projects.getArea() != null) {
			if (projects.getArea().getAreaLevel()==3) {
				detachedCriteria.add(Restrictions.eq("countyLevelCityID", projects.getArea()));
			}else if(projects.getArea().getAreaLevel()==2){
				recursionFlag = true;
				detachedCriteria.add(Restrictions.eq("townID", projects.getArea()));
			}else{
				recursionFlag = true;
				detachedCriteria.add(Restrictions.eq("villageID", projects.getArea()));
			}
		}
		if (shiroUser != null && !shiroUser.isSuperAdmin) {
			Set<Area> countyLevelCitys = shiroUser.getCountyLevelCitys();
			Set<Area> towns = shiroUser.getTowns();
			Set<Area> villages = shiroUser.getVillages();
			if (!countyLevelCitys.isEmpty() && !towns.isEmpty() && !villages.isEmpty()) {
				detachedCriteria.add(Restrictions.or(Restrictions.in("countyLevelCityID", countyLevelCitys), Restrictions.in("townID", towns), Restrictions.in("villageID", villages)));
			}else if (countyLevelCitys.isEmpty() && towns.isEmpty()  && !villages.isEmpty()) {
				detachedCriteria.add(Restrictions.in("villageID", villages));
			}else if (countyLevelCitys.isEmpty() && !towns.isEmpty()  && !villages.isEmpty()){
				detachedCriteria.add(Restrictions.or(Restrictions.in("townID", towns), Restrictions.in("villageID", villages)));
			}
		}
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		detachedCriteria.addOrder(Order.desc("id"));
		List<Projects> list=  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
//		if (recursionFlag) {
//			Set<Long> idSet = new HashSet<>();
//			for(Projects projects2: list){
//				idSet.add(projects2.getId());
//			}
//			
//			Iterator<Projects> it = list.iterator();
//			Set<Long> parentIdSet = new HashSet<>();
//	        while (it.hasNext()) {
//	        	Projects p = (Projects) it.next();
//	        	if(p.getParentId() !=null && p.getParentId()>0 && !idSet.contains(p.getParentId())){
//	        		parentIdSet.add(p.getParentId());
//	        	}
//	            results.addAll(getChildList(p));
//	        }
//	        for(Long pId: parentIdSet){
//	        	results.addAll(getParentList(pId));
//	        }
//		}
//		for(Projects projects2 : results){
//			if (!list.contains(projects2)) {
//				list.add(projects2);
//			}
//		}
        return list;
	}
	
	@Override
	public List<Projects> findAllNormalProjects(){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		detachedCriteria.addOrder(Order.desc("id"));
		return  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	/**
	 * 项目名称模糊查询
	 * @author LLJ
	 */
	@Override
	public List<Projects> findParentProjects(String xxnr) throws DataAccessException {
		Set<Projects> results = new HashSet<>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		if(StringUtils.isNotEmpty(xxnr)) {
			detachedCriteria.add(
					Restrictions.or(
							Restrictions.like("projectName",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("carryOutUnit",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("chargePerson",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("farmerName",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("referenceNumber",xxnr,MatchMode.ANYWHERE)
					)
				);
		}
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		detachedCriteria.addOrder(Order.desc("id"));
		List<Projects> list= (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
		results.addAll(list);
		Iterator<Projects> it = list.iterator();
		Set<Long> parentIdSet = new HashSet<>();
        while (it.hasNext()) {
        	Projects p = (Projects) it.next();
        	if(p.getParentId() !=null && p.getParentId()>0){
        		parentIdSet.add(p.getParentId());
        	}
            results.addAll(getChildList(p));
        }
        for(Long pId: parentIdSet){
        	results.addAll(getParentList(pId));
        }
        
        List<Projects> list1 = new ArrayList<>();
        list1.addAll(results);
        return setTreeLevel(list1);
	}
	
	/**
	 * 初始化
	 * @param xxnr
	 * @return
	 * @author LLJ
	 */
	public List<Projects> findAllInit(){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		detachedCriteria.addOrder(Order.desc("id"));
		List<Projects> list=  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return setTreeLevel(list);
	}
	
	/**
	 * 初始化
	 * @param xxnr
	 * @return
	 * @author LLJ
	 */
	public List<Projects> findAllInit(String xxnr){
		Set<Projects> results = new HashSet<>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		if(StringUtils.isNotEmpty(xxnr)) {
			detachedCriteria.add(
					Restrictions.or(
							Restrictions.like("projectName",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("carryOutUnit",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("chargePerson",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("farmerName",xxnr,MatchMode.ANYWHERE),
							Restrictions.like("referenceNumber",xxnr,MatchMode.ANYWHERE)
					)
				);
		}
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		detachedCriteria.addOrder(Order.desc("id"));
		List<Projects> list=  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
		results.addAll(list);
		Iterator<Projects> it = list.iterator();
		Set<Long> parentIdSet = new HashSet<>();
        while (it.hasNext()) {
        	Projects p = (Projects) it.next();
        	if(p.getParentId() !=null && p.getParentId()>0){
        		parentIdSet.add(p.getParentId());
        	}
            results.addAll(getChildList(p));
        }
        for(Long pId: parentIdSet){
        	results.addAll(getParentList(pId));
        }
        
        List<Projects> list1 = new ArrayList<>();
        list1.addAll(results);
        return setTreeLevel(list1);
	}

	@Override
	public List<Projects> exportExcelList(Projects p) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		//detachedCriteria.createAlias("projects","ps");
		detachedCriteria.add(Restrictions.isNotNull("parent_id"));
		detachedCriteria.addOrder(Order.desc("id"));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Projects> list=  findByCriteria(detachedCriteria);
		return list;
	}

	/**
	 * 根据子项目查找子项目信息
	 * @author LLJ
	 */
	@Override
	public List<Projects> findProjectParent(Long parentId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("id", parentId));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Projects> list=  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
		for(Projects projects : list){
			if(projects.getParentId() != null){
				List<Projects> listarea = (List<Projects>)findByLongColumn("id", projects.getParentId());
				listarea.add(projects);
				return listarea;
			}
			
		}
		return setTreeLevel(list);
	}
	
	@Override
	public Projects findProject(int projectNumber) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("projectNumber", projectNumber));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Projects> list=  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
		for(Projects projects : list){
			if(projects.getProjectsStatus() == 0){
				projects.setProjectsStatus(1);
				
			}else if(projects.getProjectsStatus() == 1) {
				projects.setProjectsStatus(0);
				
			}
		
		}
		return null;
	}



	/**
	 * 查询所有
	 * @author LLJ
	 */
	@Override
	public List<Projects> findAllProjects(Long areaId,int limit, int start) {
		Set<Projects> results = new HashSet<>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		Area area = new Area();
		area.setId(areaId);
		detachedCriteria.add(Restrictions.eq("area", area));
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Projects> list = (List<Projects>) getHibernateTemplate().findByCriteria(detachedCriteria);
		results.addAll(list);
		Iterator<Projects> it = list.iterator();
		Set<Long> parentIdSet = new HashSet<>();
        while (it.hasNext()) {
        	Projects p = (Projects) it.next();
        	if(p.getParentId() !=null && p.getParentId()>0){
        		parentIdSet.add(p.getParentId());
        	}
            results.addAll(getChildList(p));
        }
        for(Long pId: parentIdSet){
        	results.addAll(getParentList(pId));
        }
        
        List<Projects> list1 = new ArrayList<>();
        list1.addAll(results);
        return setTreeLevel(list1);
//		return list1;
	}
	
	/**
	 * 顺序输出树
	 * @param list
	 * @return
	 */
	private List<Projects> setTreeLevel(List<Projects> list){
		Map<Long, Projects> map = new HashMap<>();
		for(Projects projects: list){
			map.put(projects.getId(), projects);
		}
		for(Projects projects: list){
			if (projects.getParentId()!=null && projects.getParentId()>0  && map.get(projects.getParentId()) != null) {
				map.get(projects.getParentId()).getChildren().add(projects);
			}
		}
		int topLevel = 1;
		for(int i =0;i<list.size();i++){
			Projects projects = list.get(i);
			if(projects.getParentId() == null || projects.getParentId() == 0){
				projects.setTreeLevel(topLevel+++"");
				setChildrenLevel(map, projects);
			}
		}
		return list;
	}
	
	private void setChildrenLevel(Map<Long, Projects> map, Projects projects){
		List<Projects> children = map.get(projects.getId()).getChildren();
		for(int i =0;i<children.size();i++){
			Projects project = children.get(i);
			project.setTreeLevel(map.get(projects.getId()).getTreeLevel()+"."+(i+1) + "");
			if (!project.getChildren().isEmpty()) {
				setChildrenLevel(map, project);
			}
		}
	}
	
	
	/**
	 *  得到父节点列表
	 * @param list
	 * @param projects
	 * @return
	 * @author LLJ
	 */
	public Set<Projects> getParentList(Long pId) {
		Set<Projects> projectsList = new HashSet<Projects>();
		Projects parentProjects = get(pId);
		if(parentProjects == null){
			return projectsList;
		}
		projectsList.add(parentProjects);
		if(parentProjects.getParentId() !=null && parentProjects.getParentId()>0){
			projectsList.addAll(getParentList(parentProjects.getParentId()));
		}
		return projectsList;
	}
	
	
	/**
	 *  得到子节点列表
	 * @param list
	 * @param projects
	 * @return
	 * @author LLJ
	 */
    public List<Projects> getChildList(Projects projects) {
    	List<Projects> results = new ArrayList<>();
		List<Projects> list = getChild(projects);
		if(list.isEmpty()){
			return list;
		}
		results.addAll(list);
        for(Projects p: list) {
        	results.addAll(getChildList(p));
        }
        return setTreeLevel(results);
    }

    /**查询所有子节点
     * @author LLJ
     */
	@Override
	@Cacheable(value="subProjects",key="#projects.id")
	public List<Projects> getChild(Projects projects) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("parentId", projects.getId()));
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Projects> list = (List<Projects>) getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}
	
    /**
     * add all projects
     */
	@Override
	@CacheEvict(value={"projects","subProjects"}, allEntries=true)
	public List<Long> add(List<Projects> models) throws DataAccessException {
		List<Long> ids = new ArrayList<Long>();
		for (Projects projects : models) {
			ids.add(add(projects));
		}
		return ids;
	}

	@Override
	public Projects findByProjectNumber(String projectNumber) {
//		List<Projects> projects = findByIntegerColumn("projectNumber", projectNumber);
		List<Projects> projects = findByNameStrict("projectNumber", projectNumber);
		if (projects != null && !projects.isEmpty()) {
			return projects.get(0);
		}
		return null;
	}

	/**
	 * 通过areaId查询与之关联的项目
	 */
	@Override
	public List<Projects> findImportProjects(Long areaId) {
		Set<Projects> results = new HashSet<>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		Area area = new Area();
		area.setId(areaId);
		detachedCriteria.add(Restrictions.eq("area", area));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		detachedCriteria.addOrder(Order.desc("id"));
		List<Projects> list = (List<Projects>) getHibernateTemplate().findByCriteria(detachedCriteria);
//		results.addAll(list);
//		long startTime = System.currentTimeMillis();
//		for (Projects projects : list) {
//			if (projects.getParentId()!=null && projects.getParentId()>0) {
//				results.addAll(getParentList(projects.getParentId()));
//			}
//			results.addAll(getChildList(projects));
//		}
//		long endTime = System.currentTimeMillis();
//        System.out.println("运行时间是："+(endTime-startTime)+"ms "+(endTime-startTime)/1000+"s");
	
//		Iterator<Projects> it = list.iterator();
//		Set<Long> parentIdSet = new HashSet<>();
//        while (it.hasNext()) {
//        	Projects p = (Projects) it.next();
//        	if(p.getParentId() !=null && p.getParentId()>0){
//        		parentIdSet.add(p.getParentId());
//        	}
//            results.addAll(getChildList(p));
//        }
//        for(Long pId: parentIdSet){
//        	results.addAll(getParentList(pId));
//        }
        
        
        
//        List<Projects> list1 = new ArrayList<>();
//        list1.addAll(results);
        return setTreeLevel(list);
	}


	/**
	 * 调项记录
	 * @author LLJ
	 */
	@Override
	public List<Projects> findAllAdjustmentRecord(Long adjustmentAreaId,String projectName) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("flag", 0));
		Area area = new Area();
		area.setId(adjustmentAreaId);
		detachedCriteria.add(Restrictions.eq("area", area));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 2));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("projectName", projectName));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Projects> list=  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}


	/**
	 * 查询每个镇的项目资金
	 * author : agx
	 */
	@Override
	public List<ReportProjects> findByProjectsOfTown( int fund_year) {
		//String SQL = "select id,project_name, townID,sum(total_fund),fund_year,project_type from projects where fund_year='{1}' and flag = 0 and adjustment_Status = 1 and projects_Status = 0  and town <>'' group by townid order by area";
		
		String SQL = "select p.id,p.project_name, a.area_name,sum(total_fund),p.fund_year,p.project_type "+ 
					"from projects p ,area a "+
					"where p.townID = a.id "+
					"and p.fund_year='{1}' and p.flag = 0 and p.adjustment_Status = 1 and p.projects_Status = 0  and p.townID <>'' "+
					"group by a.area_name "+
					"order by p.area";
		String SQL_FINAL = SQL.replace("{1}",String.valueOf(fund_year)); 
		System.out.println("-----------------"+SQL_FINAL);
		SQLQuery sqlQuery = getSession().createSQLQuery(SQL_FINAL);
		List list = sqlQuery.list();
		
		List<ReportProjects> result = new ArrayList<ReportProjects>();
		try{
			for (int i = 0; i < list.size(); i++){
				Object[] row = (Object[])list.get(i);
				ReportProjects reports = new ReportProjects();
				reports.setId(getInteger(row[0]));
				reports.setProjectName(getString(row[1]));
				reports.setTown(getString(row[2]));			
				reports.setTotalFund(getDouble(row[3]));
				reports.setFundYear(getString(row[4]));
				reports.setProjectType(getInteger(row[5]));
				result.add(reports);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;

	}


	/*
	 * 获取乡镇中，每个村，在同一个年度的统计分析
	 */
	@Override
	public List<ReportProjects> getReportByTown(String town, String year) {
		String SQL = "select id,project_name, town,village,sum(total_fund),fund_year from projects where fund_year = {1} group by village having town='{2}'";
		String SQL_FINAL = SQL.replace("{1}",year).replace("{2}", town);
		
		System.out.println("-----------------"+SQL_FINAL);
		SQLQuery sqlQuery = getSession().createSQLQuery(SQL_FINAL);
		List list = sqlQuery.list();
		
		List<ReportProjects> result = new ArrayList<ReportProjects>();
		try{
			for (int i = 0; i < list.size(); i++){
				Object[] row = (Object[])list.get(i);
				ReportProjects reports = new ReportProjects();
				reports.setId(getInteger(row[0]));
				reports.setProjectName(getString(row[1]));
				reports.setTown(getString(row[2]));
				reports.setVillage(getString(row[3]));
				reports.setTotalFund(getDouble(row[4]));
				reports.setFundYear(getString(row[5]));
				result.add(reports);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询产业统计数据
	 * author :agx
	 */
	@Override
	public List<ReportProjects> getReportByIndustry(Integer year) {
		//String SQL ="select id,project_name, town,village,sum(total_fund),fund_year,project_type from projects group by town having project_type ={1} order by id ";
		//String SQL_FINAL = SQL.replace("{1}", String.valueOf(type));
		String SQL = "select id,project_name, town,village,sum(total_fund),fund_year,project_type from projects where fund_year = {1} and flag = 0 and adjustment_Status = 1 and projects_Status =0  group by  project_type ";
		String SQL_FINAL = SQL.replace("{1}", String.valueOf(year));
		System.out.println("-----------------"+SQL_FINAL);
		SQLQuery sqlQuery = getSession().createSQLQuery(SQL_FINAL);
		List list = sqlQuery.list();
		
		List<ReportProjects> result = new ArrayList<ReportProjects>();
		try{
			for (int i = 0; i < list.size(); i++){
				Object[] row = (Object[])list.get(i);
				ReportProjects reports = new ReportProjects();
				reports.setId(getInteger(row[0]));
				reports.setProjectName(getString(row[1]));
				reports.setTown(getString(row[2]));
				reports.setVillage(getString(row[3]));
				reports.setTotalFund(getDouble(row[4]));
				reports.setFundYear(getString(row[5]));
				reports.setProjectType(getInteger(row[6]));
				result.add(reports);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	private String getString(Object object) {
		return object == null ? "" : object.toString();
	}

	private Double getDouble(Object object) {
		return object == null ? 0 : Double.parseDouble(object.toString());
	}

	private Integer getInteger(Object object) {
		return object == null ? 0 : Integer.parseInt(object.toString());
	}



	@Override
	public List<Projects> getReportByVillage(Long townId,Long villageId, String year) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		Area town = new Area();
		town.setId(townId);
		Area village = new Area();
		village.setId(villageId);
		detachedCriteria.add(Restrictions.eq("townID",town));
		detachedCriteria.add(Restrictions.eq("villageID",village));
		detachedCriteria.add(Restrictions.eq("fundYear",year));
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.or(Restrictions.lt("projectProcess","100"),Restrictions.or(Restrictions.isNull("projectProcess"))));
		detachedCriteria.addOrder(Order.desc("id"));
		
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Projects> list=  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}



	/**
	 * 
	 * 统计项目的个数
	 * @param year 年份
	 * 年份为空就统计总个数。不为空就统计指定年
	 *
	 */
	@Override
	public List<ReportProjects> getReportByProjectCount(Integer year) {
		String SQL= "select p.id,a.area_name,count(*) from projects p ,area a ";
		String SQL_FINAL = "";
//		if(!year.equals("0")){
		SQL += "where p.townID = a.id "+
				"and p.fund_year={1} and p.flag = 0 and p.adjustment_Status = 1 and p.projects_Status = 0  and p.townID <>'' "+
				"group by a.area_name ";
		SQL_FINAL = SQL.replace("{1}",String.valueOf(year));
		System.out.println("-----------------"+SQL_FINAL);
//		}
//		if(year.equals("0")){
//			SQL_FINAL= SQL+ "group by town";
//			System.out.println("-----------------"+SQL_FINAL);
//		}
		
		SQLQuery sqlQuery = getSession().createSQLQuery(SQL_FINAL);
		List list = sqlQuery.list();
		
		List<ReportProjects> result = new ArrayList<ReportProjects>();
		try{
			for (int i = 0; i < list.size(); i++){
				Object[] row = (Object[])list.get(i);
				ReportProjects reports = new ReportProjects();
				reports.setId(getInteger(row[0]));
				reports.setTown(getString(row[1]));
				reports.setProjectCount(getInteger(row[2]));
				result.add(reports);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}


	/**
	 * 根据乡镇查询所有的村
	 * @author zgx
	 * @param town 乡镇名称
	 */
	@Override
	public List<ReportProjects> findByTown(String town,Integer year) {
		String SQL = "select id ,town,village,count(total_Fund),count(*)  from projects  where town='{1}' and fund_year={2} and flag = 0 and adjustment_Status = 1 and projects_Status =0  group by villageid order by id desc";
		String SQL_FINAL = SQL.replace("{1}",town).replace("{2}", String.valueOf(year));
		System.out.println("=============="+SQL_FINAL);
		SQLQuery sqlQuery = getSession().createSQLQuery(SQL_FINAL);
		List list = sqlQuery.list();
		
		List<ReportProjects> result = new ArrayList<ReportProjects>();
		try{
			for (int i = 0; i < list.size(); i++){
				Object[] row = (Object[])list.get(i);
				ReportProjects reports = new ReportProjects();
				reports.setId(getInteger(row[0]));
				reports.setTown(getString(row[1]));
				reports.setVillage(getString(row[2]));
				reports.setTotalFund(getDouble(row[3]));
				reports.setProjectCount(getInteger(row[4]));
				result.add(reports);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;	
	}



	
	@Override
	public List<Projects> findCountyLevelCityId(Long areaId) {
		Set<Projects> results = new HashSet<>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		Area area = new Area();
		area.setId(areaId);
		detachedCriteria.add(Restrictions.eq("countyLevelCityID", area));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		detachedCriteria.addOrder(Order.desc("id"));
		List<Projects> list = (List<Projects>) getHibernateTemplate().findByCriteria(detachedCriteria);
        return setTreeLevel(list);
	}



	@Override
	public List<Projects> findTownId(Long areaId) {
		Set<Projects> results = new HashSet<>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		Area area = new Area();
		area.setId(areaId);
		detachedCriteria.add(Restrictions.eq("townID", area));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		detachedCriteria.addOrder(Order.desc("id"));
		List<Projects> list = (List<Projects>) getHibernateTemplate().findByCriteria(detachedCriteria);
        return setTreeLevel(list);
	}


	/**
	 * 多个村镇实施同一个项目
	 * 到农户的项目将不再查出来
	 * @author zgx
	 */
	@Override
	public List<Projects> findbyOneToMany(Long townId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		Area town = new Area();
		town.setId(townId);
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.add(Restrictions.eq("townID", town));
		detachedCriteria.add(Restrictions.ne("level", 1));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Projects> list=  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}


	/**
	 * 按项目名称统计
	 * @author zgx
	 * @param Area 地区
	 * @param projectNameId 项目ID
	 */
	@Override
	public List<Projects> getBySameProjectName(Area area, Long projectsNameId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.add(Restrictions.eq("townID", area));
		detachedCriteria.add(Restrictions.eq("parentId", projectsNameId));
		detachedCriteria.add(Restrictions.ne("projectProcess", "100"));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Projects> list=  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}



	@Override
	public List<Projects> reportForProjectsFinlished(Long townId, Long villageId, String year) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		Area town = new Area();
		town.setId(townId);
		Area village = new Area();
		village.setId(villageId);
		detachedCriteria.add(Restrictions.eq("townID",town));
		detachedCriteria.add(Restrictions.eq("villageID",village));
		detachedCriteria.add(Restrictions.eq("fundYear",year));
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("projectProcess","100"));
		detachedCriteria.addOrder(Order.desc("id"));
		
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<Projects> list=  (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
		return list;
	}


	@Override
	public List<ReportProjects> reportByVillageFinanceCount(String town, Integer year) {
		String SQL = "select id,project_name, town,village,sum(total_fund),fund_year,project_type from projects where town ='{1}'  and fund_year= {2} and flag = 0 and adjustment_Status = 1 and projects_Status = 0 group by villageid";
		String SQL_FINAL =  SQL.replace("{1}",town).replace("{2}", String.valueOf(year));
		System.out.println("-----------------"+SQL_FINAL);
		SQLQuery sqlQuery = getSession().createSQLQuery(SQL_FINAL);
		List list = sqlQuery.list();
		
		List<ReportProjects> result = new ArrayList<ReportProjects>();
		try{
			for (int i = 0; i < list.size(); i++){
				Object[] row = (Object[])list.get(i);
				ReportProjects reports = new ReportProjects();
				reports.setId(getInteger(row[0]));
				reports.setProjectName(getString(row[1]));
				reports.setTown(getString(row[2]));
				reports.setVillage(getString(row[3]));
				reports.setTotalFund(getDouble(row[4]));
				reports.setFundYear(getString(row[5]));
				reports.setProjectType(getInteger(row[6]));
				result.add(reports);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}


	@Override
	@CacheEvict(value={"projects","subProjects"}, allEntries=true)
	public void update(Projects model) {
		super.update(model);
	}

	@Override
	@CacheEvict(value={"projects","subProjects"}, allEntries=true)
	public Long save(Projects model) throws DataAccessException {
		return super.save(model);
	}

	@Override
	@CacheEvict(value={"projects","subProjects"}, allEntries=true)
	public Long add(Projects model) throws DataAccessException {
		return super.add(model);
	}

	@Override
	@Cacheable(value="projects",key="#id")
	public Projects get(Long id) throws DataAccessException {
		return super.get(id);
	}

	@Override
	@CacheEvict(value={"projects","subProjects"}, allEntries=true)
	public void remove(Projects model) throws DataAccessException {
		super.remove(model);
	}

	@Override
	@CacheEvict(value={"projects","subProjects"}, allEntries=true)
	public void removeByID(Long id) throws DataAccessException {
		super.removeByID(id);
	}

	@Override
	public List<Projects> exportProjects(ShiroUser shiroUser) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Projects.class);
		if (shiroUser != null && !shiroUser.isSuperAdmin) {
			Set<Area> countyLevelCitys = shiroUser.getCountyLevelCitys();
			Set<Area> towns = shiroUser.getTowns();
			Set<Area> villages = shiroUser.getVillages();
			if (!countyLevelCitys.isEmpty() && !towns.isEmpty() && !villages.isEmpty()) {
				detachedCriteria.add(Restrictions.or(Restrictions.in("countyLevelCityID", countyLevelCitys), Restrictions.in("townID", towns), Restrictions.in("villageID", villages)));
			}else if (countyLevelCitys.isEmpty() && towns.isEmpty()  && !villages.isEmpty()) {
				detachedCriteria.add(Restrictions.in("villageID", villages));
			}else if (countyLevelCitys.isEmpty() && !towns.isEmpty()  && !villages.isEmpty()){
				detachedCriteria.add(Restrictions.or(Restrictions.in("townID", towns), Restrictions.in("villageID", villages)));
			}
		}
		detachedCriteria.add(Restrictions.eq("flag", 0));
		detachedCriteria.add(Restrictions.eq("projectsStatus", 0));
		detachedCriteria.add(Restrictions.eq("adjustmentStatus", 1));
		detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		detachedCriteria.addOrder(Order.desc("id"));
		return (List<Projects>)getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	public List<ProjectsSumTotalFund> findAllTotalFund(Long pid){
			String sql="select id,sum(total_fund) from projects where flag=0 and adjustment_status=1 and parent_id="+pid+"";
			SQLQuery sqlQuery = getSession().createSQLQuery(sql);
			List list = sqlQuery.list();
			List<ProjectsSumTotalFund> result = new ArrayList<ProjectsSumTotalFund>();
			ProjectsSumTotalFund totalFund = null;
		try{
			   Object[] row = (Object[])list.get(0);
			   totalFund = new ProjectsSumTotalFund();
			   totalFund.setId(getInteger(row[0]));
			   totalFund.setAllTotalFund(getDouble(row[1]));	
			   result.add(totalFund);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
