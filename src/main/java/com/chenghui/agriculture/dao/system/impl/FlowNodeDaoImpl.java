package com.chenghui.agriculture.dao.system.impl;

import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.system.FlowNodeDao;
import com.chenghui.agriculture.model.FlowNode;

/**
 * 流程类型访问接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class FlowNodeDaoImpl extends GenericHibernateDao<FlowNode, Long> implements FlowNodeDao {

}
