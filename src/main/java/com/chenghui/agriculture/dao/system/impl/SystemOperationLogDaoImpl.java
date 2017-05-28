package com.chenghui.agriculture.dao.system.impl;

import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.SystemOperationLogDao;
import com.chenghui.agriculture.model.SystemOperationLog;

@Repository
public class SystemOperationLogDaoImpl extends GenericHibernateDao<SystemOperationLog, Long> implements SystemOperationLogDao {

}
