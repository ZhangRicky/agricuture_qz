package com.chenghui.agriculture.dao.system;

import com.chenghui.agriculture.core.exception.FlowNotFoundException;
import com.chenghui.agriculture.core.exception.FlowTypeNotFoundException;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Flow;

public interface FlowDao extends GenericDao<Flow, Long>{

	/**
	 * Used to check whether exist the save flow type data already, note don't check removed and not inUse records;
	 * Especially when id is great than zero, check the records which is not equal flow.The id is not null means making update operation. 
	 * @param flow
	 * @return
	 * @author yudq
	 */
	boolean isExistFlowByName(Flow flow);

	PaginationSupport<Flow> findPageByFlow(Flow flow, int limit, int start);

	Flow getFlowByFlowType(int typeKey) throws FlowTypeNotFoundException, FlowNotFoundException;
	
}
