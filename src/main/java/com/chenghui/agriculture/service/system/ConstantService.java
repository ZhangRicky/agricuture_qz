package com.chenghui.agriculture.service.system;

import java.util.List;

import com.chenghui.agriculture.model.Constant;
import com.chenghui.agriculture.service.GenericService;

public interface ConstantService extends GenericService<Constant, Long> {
	List<Constant> appFindByIntegerColumn(String queryColumn, Integer columnValue);
}
