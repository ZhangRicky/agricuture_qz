package com.chenghui.agriculture.dao.projectManage;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.TemplateType;

/**
 * 模版类型访问接口
 * @author yudq
 * @version V1.0
 */
public interface TemplateTypeDao extends GenericDao<TemplateType, Long> {

	TemplateType geTemplateTypeByTypekey(int typeKey);

}
