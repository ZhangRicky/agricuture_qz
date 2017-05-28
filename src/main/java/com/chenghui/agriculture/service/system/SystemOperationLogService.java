package com.chenghui.agriculture.service.system;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.SystemOperationLog;
import com.chenghui.agriculture.service.GenericService;

public interface SystemOperationLogService extends GenericService<SystemOperationLog,Long>{

	void saveSystemOperationLog(ShiroUser shiroUser, String AppModule,String opType, String opText, String detailMsg, String id);
	
	PaginationSupport<SystemOperationLog> getSystemOperationLogList(SystemOperationLog param,int start, int limit);

}
