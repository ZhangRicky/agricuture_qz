package com.chenghui.agriculture.dao.system.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.dao.DataAccessException;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.DepartmentDao;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Department;
import com.chenghui.agriculture.model.Users;

/**
 * 角色类型访问接口
 * @author yudq
 *
 */
@SuppressWarnings("unchecked")
@Repository
public class DepartmentDaoImpl extends GenericHibernateDao<Department, Long> implements DepartmentDao {
	
	
	@Override
	public List<Department> getChildren(Department department) throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Department.class);
		detachedCriteria.add(Restrictions.eq("parentCode", department.getDeptId()));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return   (List<Department>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	@Override
	public Department getDepartmentByDeptName(Department department) throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Department.class);
		detachedCriteria.add(Restrictions.eq("deptName", department.getDeptName()));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Department> departments = (List<Department>) getHibernateTemplate().findByCriteria(detachedCriteria);
		if (departments == null || departments.isEmpty()) {
			return null;
		}
		return  departments.get(0);
	}
	
	@Override
	public List<Department> findAllWithoutOthers() throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Department.class);
		detachedCriteria.add(Restrictions.ne("deptId", -1L));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		detachedCriteria.addOrder(Order.asc("id"));
		return  (List<Department>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	/**
	 * 绑定用户权限
	 */
	@Override
	public List<Area> findAllWithoutOther() throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class);
		detachedCriteria.add(Restrictions.ne("id", -1L));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		detachedCriteria.addOrder(Order.asc("id"));
		return  (List<Area>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	@Override
	public List<Department> findParentDepartments() throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Department.class);
		detachedCriteria.add(Restrictions.or(Restrictions.eq("deptLevel", 2),Restrictions.eq("deptLevel", 3)));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		detachedCriteria.addOrder(Order.asc("id"));
		return  (List<Department>) getHibernateTemplate().findByCriteria(detachedCriteria);
	}
	
	@Override
	public List<Department> findParentDepartment(String deptName) throws DataAccessException {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Department.class);
		detachedCriteria.add(Restrictions.like("deptName", deptName, MatchMode.ANYWHERE));
		detachedCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Department> departments=  (List<Department>)getHibernateTemplate().findByCriteria(detachedCriteria);
		for(Department department : departments){
			if(department.getParentCode() != null){
				List<Department> dept = (List<Department>)findByLongColumn("id", department.getParentCode());
				
				for(Department department1 : dept){
						List<Department> dept2 = (List<Department>)findByLongColumn("id", department1.getParentCode());
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
//	public Set<Department> findDepartmentSetByUser(Users user) {
//		Hibernate.initialize(user.getDepartment());
//		Set<Department> departmentSet = user.getDepartment();
//		return departmentSet;
//	}
	
	/**
	 * 绑定地区权限
	 */
	@Override
	public Set<Area> findAreaSetByUser(Users user) {
		Hibernate.initialize(user.getArea());
		Set<Area> areaSet = user.getArea();
		return areaSet;
	}

	@Override
	public List<Department> findDepartmentByDeptLevel(int level) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Department.class);
		
		if(level == 3)
			level =2;
		
		detachedCriteria.add(Restrictions.eq("deptLevel", level));
		
		return findAllByCriteria(detachedCriteria);
	}

	@Override
	public List<Department> findJuWeiHuiBySheQu(Set<Department> depts) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Department.class);
		
		List<Long> shequ = new ArrayList<Long>();
		for(Department dept: depts){
			shequ.add(dept.getDeptId());
		}
		detachedCriteria.add(Restrictions.eq("deptLevel", 1));
		detachedCriteria.add(Restrictions.in("parentCode", shequ));
		return findAllByCriteria(detachedCriteria);
	}
	@Override
	public List<Department> getAllSheQuByJuWeiHui(Set<Department> listDeprt) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Department.class);
		List<Long> deptId = new ArrayList<Long>();
		for(Department dept : listDeprt){
			if (dept.getParentCode() != null) {
				deptId.add(Long.parseLong(""+dept.getParentCode()));
			}
		}
		 Criterion criter = null;
		if (deptId.size()>0){
			criter = Restrictions.in("deptId", deptId.toArray());
		}
		if (criter != null){
			criter = Restrictions.and(criter,Restrictions.eq("deptLevel", 2));
			detachedCriteria.add(criter);
		} else
			detachedCriteria.add(Restrictions.eq("deptLevel", 2));
		return findAllByCriteria(detachedCriteria);
	}
	@Override
	public List<Department> getJuWeiHuiBySheQu(Integer fzid) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Department.class);
		 Criterion criter = Restrictions.eq("deptLevel", 1);
		detachedCriteria.add(Restrictions.and(criter,Restrictions.eq("parentCode", new Long(fzid))));
		return findAllByCriteria(detachedCriteria);
	}
}
