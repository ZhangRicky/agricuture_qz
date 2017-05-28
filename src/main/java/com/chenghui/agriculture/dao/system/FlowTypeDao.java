package com.chenghui.agriculture.dao.system;

import com.chenghui.agriculture.core.exception.FlowTypeNotFoundException;
import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.FlowType;

/**
 * 流程类型访问接口
 * @author yudq
 *
 */
public interface FlowTypeDao extends GenericDao<FlowType, Long> {

	FlowType geFlowTypeByTypekey(int typeKey)throws FlowTypeNotFoundException;

}
