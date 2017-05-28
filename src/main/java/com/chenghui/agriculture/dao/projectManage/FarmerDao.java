package com.chenghui.agriculture.dao.projectManage;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Farmer;

/**
 * 模版访问接口
 * @author yudq
 * @version V1.0
 */
public interface FarmerDao extends GenericDao<Farmer, Long> {

	
	Farmer findFarmerNumber(String farmerNumber);
	
	Farmer getFarmerByFarmerNumber(Farmer farmer);
}
