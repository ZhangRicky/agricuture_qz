package com.chenghui.agriculture.dao.projectManage;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.FarmerImport;


public interface FarmerImportDao extends GenericDao<FarmerImport, Long> {
	
	void executeLoad(String loadSql);

}
