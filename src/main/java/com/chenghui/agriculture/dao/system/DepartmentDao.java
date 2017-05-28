package com.chenghui.agriculture.dao.system;


import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Department;
import com.chenghui.agriculture.model.Users;

/**
 * 角色类型访问接口
 * @author yudq
 *
 */
public interface DepartmentDao extends GenericDao<Department, Long> {
	
	@Override
	List<Department> findAll() throws DataAccessException;
	
	List<Department> findAllWithoutOthers() throws DataAccessException;
	
	List<Area> findAllWithoutOther() throws DataAccessException;
	
	List<Department> findParentDepartments() throws DataAccessException;
	
	List<Department> findParentDepartment(String deptName) throws DataAccessException;
	
	@Override
	List<Department> findByHQL(String hql,Object... values) throws DataAccessException;
	
	@Override
	Department get(Long dept_id) throws DataAccessException;

//	List<Department> findDepartmentByRole(Role role);
//
//	Set<Department> findDepartmentSetByRole(Role role);
	
	//Set<Department> findDepartmentSetByUser(Users user);
	
	Set<Area> findAreaSetByUser(Users user);
	
	List<Department> findDepartmentByDeptLevel(int departmentLevelShequ);

	List<Department> findJuWeiHuiBySheQu(Set<Department> depts);

	List<Department> getAllSheQuByJuWeiHui(Set<Department> listDeprt);

	List<Department> getJuWeiHuiBySheQu(Integer fzid);
	
	Department getDepartmentByDeptName(Department department);
	
	List<Department> getChildren(Department department);
}
