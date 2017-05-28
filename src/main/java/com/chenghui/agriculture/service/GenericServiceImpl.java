package com.chenghui.agriculture.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.GenericDao;

public abstract class GenericServiceImpl<M extends Serializable, PK extends Serializable>
		implements GenericService<M, PK> {
	private final static Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class); 

	@Autowired
	private GenericDao<M, PK> genericDao;

	@Override
	public PK add(M model) {
		return genericDao.add(model);
	}
	@Override
	public void remove(M model) {
		genericDao.remove(model);
	}

	@Override
	public void removeByID(PK id) {
		genericDao.removeByID(id);
	}
	@Override
	public void update(M model) {
		genericDao.update(model);
	}

	@Override
	public M get(PK id) {
		return genericDao.get(id);
	}
	@Override
	public M load(PK id) {
		return genericDao.load(id);
	}

	@Override
	public List<M> findAll() {
		return genericDao.findAll();
	}

	@Override
	public M findByName(String name) {
		logger.info(name);
		List<M> entityList =  genericDao.findByName(name);
		return entityList!=null&&entityList.size()==1?entityList.get(0):null;
	}
	public PaginationSupport<M> findPageByCriteria(final DetachedCriteria detachedCriteria, final int pageSize,final int startIndex){
		return genericDao.findPageByCriteria(detachedCriteria,pageSize,startIndex);
		
	}

	@Override
	public boolean findNameExists(String queryColumn, String columnValue,String flag){
		return genericDao.findNameExists(queryColumn, columnValue,flag);
	}
	
	@Override
	public List<M> findByName(String queryColumn,String columnValue) {
		return genericDao.findByName(queryColumn, columnValue);
	}
}
