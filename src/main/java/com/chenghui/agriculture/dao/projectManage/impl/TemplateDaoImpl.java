package com.chenghui.agriculture.dao.projectManage.impl;

import org.springframework.stereotype.Repository;

import com.chenghui.agriculture.dao.GenericHibernateDao;
import com.chenghui.agriculture.dao.projectManage.TemplateDao;
import com.chenghui.agriculture.model.Template;

/**
 * 流程类型访问接口
 * @author yudq
 */
@SuppressWarnings("unchecked")
@Repository
public class TemplateDaoImpl extends GenericHibernateDao<Template, Long> implements TemplateDao {
	
}
