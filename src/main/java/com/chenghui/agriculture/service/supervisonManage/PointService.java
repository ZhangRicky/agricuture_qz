package com.chenghui.agriculture.service.supervisonManage;

import java.util.List;
import java.util.Map;

import com.chenghui.agriculture.model.Point;
import com.chenghui.agriculture.service.GenericService;

/**
 * 监管记录业务接口service
 * @author LLJ
 * @version V1.0
 */
public interface PointService extends GenericService<Point, Long>{
	
	/**
	 * 根据坐标获取项目范围
	 * @param projectsId
	 * @return
	 */
	List<Point> getProjectsRange(Long determinePid);
	
	/**
	 * 确定项目范围
	 * @param point
	 * @param shiroUser
	 * @return
	 */
	List<Long> addRange(List<List<Point>> pointsArray,Long id);
	
	/**
	 * 添加监管记录
	 * @param point
	 * @param shiroUser
	 * @return
	 */
	Long addPoint(Point point);
		
	/**
	 * 修改监管记录
	 * @param point
	 * @return Point
	 */
	Point updatePoint(Point point);
	
	/**
	 * 删除监管记录
	 * @param id
	 */
	void removePointByID(Long id);
	/**
	 * 查询监管记录
	 * @param id
	 * @return
	 */
	Point findPointById(Long id);
	
	void deleteRange(Long adjustmentPid,Integer rangeNumber);
}
