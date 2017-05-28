package com.chenghui.agriculture.dao.system;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Constants;

public interface ConstantsDao extends GenericDao<Constants, Long> {

	List<Constants> getConstantsByType(int i);
	
	@Override
	List<Constants> findByHQL(String hql,Object... values) throws DataAccessException;

	List<Constants> getConstantsByType_NoFilter(int typeXxydw);
	
	
	List<Constants> getByType(int i);
	
	Constants getConstantsByDisplayName(Constants constants);
	

}
