package com.chenghui.agriculture.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.chenghui.agriculture.core.utils.PaginationSupport;

public interface GenericDao<M extends Serializable, PK extends Serializable> {

	PK save(M model) throws DataAccessException;

	PK add(M model) throws DataAccessException;

	void remove(M model) throws DataAccessException;

	void removeByID(PK id) throws DataAccessException;

	void update(M model) throws DataAccessException;

	List<M> findByExample(M model) throws DataAccessException;

	List<M> findByCriteria(DetachedCriteria detachedCriteria) throws DataAccessException;

	List<M> findByHQL(String hql,Object... values) throws DataAccessException;

	M get(PK id) throws DataAccessException;

	M load(PK id) throws DataAccessException;

	List<M> findAll() throws DataAccessException;

	List<M> findByName(String name) throws DataAccessException;

	PaginationSupport<M> findPageByCriteria(DetachedCriteria detachedCriteria);

	PaginationSupport<M> findPageByCriteria(DetachedCriteria detachedCriteria,int pageSize, int startIndex);

	List<M> findAllByCriteria(DetachedCriteria detachedCriteria);

	int getCountByCriteria(DetachedCriteria detachedCriteria);
	
	boolean findNameExists(String queryColumn, String columnValue,String flag);
	
	List<M> findByName(String queryColumn, String columnValue);
	
	List<M> findByNameStrict(String queryColumn, String columnValue);
	
	List<M> findByLongColumn(String queryColumn, Long columnValue);
	
	List<M> findByIntegerColumn(String queryColumn,Integer columnValue);
}
