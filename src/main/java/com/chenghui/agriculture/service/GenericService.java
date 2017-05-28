package com.chenghui.agriculture.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.chenghui.agriculture.core.utils.PaginationSupport;

public interface GenericService<M, PK extends Serializable> {

	PK add(M model);

	void remove(M model);
	
	void removeByID(PK id);
	
	void update(M model);
	
	M get(PK id);
	
	M load(PK id);
	
	List<M> findAll();
	
	M findByName(String name);
	
	PaginationSupport<M> findPageByCriteria(final DetachedCriteria detachedCriteria, final int pageSize,final int startIndex);

	boolean findNameExists(String queryColumn, String columnValue,String flag);
	
	List<M> findByName(String queryColumn, String columnValue);
	
}
