package com.chenghui.agriculture.dao.system.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.ConstantDao;
import com.chenghui.agriculture.model.Constant;

@SuppressWarnings("unchecked")
@Repository
@CacheConfig(cacheNames = {"constand"})
public class ConstantDaoImpl extends GenericHibernateDao<Constant, Long> implements ConstantDao {

	@Cacheable(key="#columnValue")
	public List<Constant> findByIntegerColumn(String queryColumn, Integer columnValue){
		return super.findByIntegerColumn(queryColumn, columnValue);
	}
	@Cacheable(key="#columnValue")
	public List<Constant> appFindByIntegerColumn(String queryColumn, Integer columnValue){
		return super.findByIntegerColumn(queryColumn, columnValue);
	}
}
