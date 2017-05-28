package com.chenghui.agriculture.dao.supervisionManage;



import java.util.List;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.PointBaidu;

/**
 * 地理坐标点接口
 * @author yudq
 * @version V1.0
 */
public interface PointBaiduDao extends GenericDao<PointBaidu, Long> {
	
	List<PointBaidu> getByPid(Long id);
	
}
