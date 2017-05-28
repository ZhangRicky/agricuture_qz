package com.chenghui.agriculture.dao.supervisionManage;



import java.util.List;

import com.chenghui.agriculture.dao.GenericDao;
import com.chenghui.agriculture.model.Point;

/**
 * 地理坐标点接口
 * @author yudq
 * @version V1.0
 */
public interface PointDao extends GenericDao<Point, Long> {
	
	/**
	 * 根据项目id查询项目的范围
	 * @param projectsId
	 * @return
	 */
	List<Point> findProjectsRange(Long determinePid);
	
	List<Point> getByPid(Long id);
	
	List<Point> findPointByProjectIdAndRangeNumberAnd(Long adjustmentPid,Integer rangeNumber);
}
