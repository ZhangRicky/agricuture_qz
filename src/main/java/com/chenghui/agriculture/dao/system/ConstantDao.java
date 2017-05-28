package com.chenghui.agriculture.dao.system;

import java.util.List;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Constant;

public interface ConstantDao extends GenericDao<Constant, Long>{
	
	List<Constant> findByIntegerColumn(String queryColumn, Integer columnValue);
	List<Constant> appFindByIntegerColumn(String queryColumn, Integer columnValue);

}
