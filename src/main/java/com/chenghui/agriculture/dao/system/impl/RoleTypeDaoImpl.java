package com.chenghui.agriculture.dao.system.impl;

import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.RoleTypeDao;
import com.chenghui.agriculture.model.RoleType;

/**
 * 角色类型访问接口
 * @author yudq
 *
 */
@SuppressWarnings("unchecked")
@Repository
public class RoleTypeDaoImpl extends GenericHibernateDao<RoleType, Long> implements RoleTypeDao {

	
}
