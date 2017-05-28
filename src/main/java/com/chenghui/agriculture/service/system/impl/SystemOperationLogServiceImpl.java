package com.chenghui.agriculture.service.system.impl;

import java.util.Date;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.system.SystemOperationLogDao;
import com.chenghui.agriculture.model.SystemOperationLog;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.system.SystemOperationLogService;

@Service
public class SystemOperationLogServiceImpl extends GenericServiceImpl<SystemOperationLog, Long> implements SystemOperationLogService {
	
	private final static Logger logger = LoggerFactory.getLogger(SystemOperationLogServiceImpl.class); 

	private final static String App = "农业项目监管";
	
	@Autowired
	SystemOperationLogDao systemOperationLogDao;
	
	@Override
	@Async
	public void saveSystemOperationLog(ShiroUser shiroUser,String AppModule,String opType,String opText, String detailMsg,String id) {
//		logger.debug("User:{},AppModule:{},OpType:{},OpText:{}",shiroUser.getLoginName(),AppModule,opType,opText);

		SystemOperationLog systemOperationLog = new SystemOperationLog();
		systemOperationLog.setSubUser(shiroUser == null ? "" :shiroUser.getLoginName());
		systemOperationLog.setApp(App);
		systemOperationLog.setAppModule(AppModule);
		systemOperationLog.setOpText(opText);
		systemOperationLog.setOpType(opType);
		systemOperationLog.setSip(shiroUser == null ? "" :shiroUser.getRemoteIP());
		systemOperationLog.setLogTime(new Date());
		systemOperationLog.setDetailed(detailMsg);
		Long oeprId = (id == null || id.trim().length() == 0) ? 0L : Long.parseLong(id.trim());
		
		systemOperationLog.setOperId(oeprId);
		systemOperationLogDao.add(systemOperationLog);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PaginationSupport<SystemOperationLog> getSystemOperationLogList(SystemOperationLog param,int start, int limit){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SystemOperationLog.class);
		
		if (!StringUtils.isEmpty(param.getSearchEndTime())) {
			criteria.add(Restrictions.lt("LogTime",param.getSearchEndTime()));
		}
		if (!StringUtils.isEmpty(param.getSearchStartTime())) {
			criteria.add(Restrictions.gt("LogTime",param.getSearchStartTime()));
		}
		if (!StringUtils.isEmpty(param.getAppModule())) {
			criteria.add(Restrictions.eq("AppModule", param.getAppModule()));
		}
		if (!StringUtils.isEmpty(param.getOpType())) {
			criteria.add(Restrictions.eq("OpType", param.getOpType()));
		}
		if (!StringUtils.isEmpty(param.getSubUser())) {
			criteria.add(Restrictions.like("SubUser", param.getSubUser(), MatchMode.ANYWHERE));
		}
		if (!StringUtils.isEmpty(param.getSip())) {
			criteria.add(Restrictions.like("Sip", param.getSip(),MatchMode.ANYWHERE));
		}
		
//		if (!StringUtils.isEmpty(param.getSearchName())) {
//			criteria.add(Restrictions.or(new Criterion[]{
//					Restrictions.like("OpText", param.getSearchName(), MatchMode.ANYWHERE),
//					Restrictions.like("AppModule", param.getSearchName(), MatchMode.ANYWHERE),
//					Restrictions.like("Sip", param.getSearchName(), MatchMode.ANYWHERE),
//					Restrictions.like("OpType", param.getSearchName(), MatchMode.ANYWHERE),
//					Restrictions.like("SubUser", param.getSearchName(), MatchMode.ANYWHERE)
//			}));
//		}
		
		criteria.addOrder(Order.desc("id"));
		
		PaginationSupport pagination = systemOperationLogDao.findPageByCriteria(criteria, limit, start);
		return pagination;
	
	}
}
