package com.chenghui.agriculture.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghui.agriculture.core.utils.DateUtil;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.SystemOperationLog;
import com.chenghui.agriculture.service.system.SystemOperationLogService;

@RequestMapping("/systemOperationLog")
@RestController
public class SystemOperationLogController {

	@Autowired
	SystemOperationLogService systemOperationLogService;

	@RequestMapping(value = "/search", method = RequestMethod.GET )
	public PaginationSupport<SystemOperationLog> getSystemOperationLogList(@RequestParam("start") int start,
			@RequestParam("limit") int limit,
			@RequestParam("searchStartTime") String searchStartTime,
			@RequestParam("searchEndTime") String searchEndTime,
			@RequestParam("OpType") String OpType,
			@RequestParam("SubUser") String SubUser,
			@RequestParam("Sip") String Sip,
			@RequestParam("AppModule") String AppModule) {
	 
		SystemOperationLog systemOperationLog = new SystemOperationLog();
		systemOperationLog.setSearchStartTime(DateUtil.formatString2Date(searchStartTime));
		systemOperationLog.setSearchEndTime(DateUtil.formatString2Date(searchEndTime));
		systemOperationLog.setOpType(OpType);
		systemOperationLog.setSubUser(SubUser);
		systemOperationLog.setSip(Sip);
		systemOperationLog.setAppModule(AppModule);
		return systemOperationLogService.getSystemOperationLogList(systemOperationLog, start, limit);
	}
	 
}
