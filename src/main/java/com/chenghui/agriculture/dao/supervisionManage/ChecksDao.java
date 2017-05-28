package com.chenghui.agriculture.dao.supervisionManage;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Checks;

/**
 * 监管记录接口
 * @author yudq
 * @version V1.0
 */
public interface ChecksDao extends GenericDao<Checks, Long> {

	List<Long> add(List<Checks> models) throws DataAccessException;
	
	/**
	 * @param sId
	 * @return
	 */
	List<Checks> getCheckssBySId(long sId);
	
}
