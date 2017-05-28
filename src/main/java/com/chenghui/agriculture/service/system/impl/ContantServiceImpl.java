package com.chenghui.agriculture.service.system.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.dao.system.ConstantDao;
import com.chenghui.agriculture.model.Constant;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.system.ConstantService;
@Service("constantService")
public class ContantServiceImpl extends GenericServiceImpl<Constant, Long> implements ConstantService {
	
	@Autowired
	private ConstantDao constantDao;

	@SystemOperationLogAnnotation(appModule=AppModule.APP_CN, opType=OperationType.AppQuery, opText="常量信息查询")
	public List<Constant> appFindByIntegerColumn(String queryColumn, Integer columnValue){
		return constantDao.findByIntegerColumn(queryColumn, columnValue);
	}

}
