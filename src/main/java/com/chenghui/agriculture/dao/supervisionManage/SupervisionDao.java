package com.chenghui.agriculture.dao.supervisionManage;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Supervision;

/**
 * 监管记录接口
 * @author yudq
 * @version V1.0
 */
public interface SupervisionDao extends GenericDao<Supervision, Long> {

	List<Long> add(List<Supervision> models) throws DataAccessException;
	
	/**
	 * 通过子项目sId查询所有监管记录
	 * @param sId
	 * @return
	 */
	List<Supervision> getSupervisionsBySId(long sId);
	
}
