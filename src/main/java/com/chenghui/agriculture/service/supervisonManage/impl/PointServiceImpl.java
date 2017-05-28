package com.chenghui.agriculture.service.supervisonManage.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenghui.agriculture.core.annotation.SystemOperationLogAnnotation;
import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.constant.OperationType;
import com.chenghui.agriculture.dao.projectManage.ProjectsDao;
import com.chenghui.agriculture.dao.projectManage.SubProjectDao;
import com.chenghui.agriculture.dao.supervisionManage.ChecksDao;
import com.chenghui.agriculture.dao.supervisionManage.PointDao;
import com.chenghui.agriculture.model.Checks;
import com.chenghui.agriculture.model.Point;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.SubProject;
import com.chenghui.agriculture.service.GenericServiceImpl;
import com.chenghui.agriculture.service.supervisonManage.PointService;

/**
 * 子坐标点业务接口实现
 * @author LLJ
 * @version V1.0
 */
@Service("PointService")
public class PointServiceImpl extends GenericServiceImpl<Point, Long> implements PointService {

	@Autowired
	PointDao pointDao;
	
	@Autowired
	SubProjectDao subProjectDao;
	
	@Autowired
	ProjectsDao projectsDao;
	
	@Autowired
	ChecksDao checksDao;
	
	/**
	 * 确定项目范围
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.POINT_MANAGE_CN, opType=OperationType.Add, opText="确定子项目范围")
	public List<Long> addRange(List<List<Point>> pointsArray,Long id){
		List<Point> listP = pointDao.getByPid(id);
		Integer rangeNumber = 0;
		if (listP.size()==0) {
			rangeNumber=1;
		}else if (listP.size()>0) {
			rangeNumber = listP.get(listP.size()-1).getRangeNumber()+1;
		}
		Long ids = null;
		List<Long> list = new ArrayList<Long>();
		for (List<Point> pointList : pointsArray) {
			for (Point point : pointList) {
				Point p = new Point();
				Projects projects = projectsDao.get(point.getProjectId());
				p.setLng(point.getLng());
				p.setLat(point.getLat());
				p.setProjects(projects);
				p.setRangeNumber(rangeNumber);
				ids = pointDao.add(p);
				list.add(ids);
			}
			rangeNumber++;
		}
		return list;
	}
	
	/**
	 * 添加坐标点
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.POINT_MANAGE_CN, opType=OperationType.AppAdd, opText="增加坐标点")
	public Long addPoint(Point point){
		if (point.getLng() ==0 || point.getLat() == 0) {
			return -1L;
		}
		if (point.getChecksId()>0) {
			Checks checks = checksDao.get(point.getChecksId());
			point.setCheck(checks);
		}
		Long id = pointDao.add(point); //记录
		return id;
	}
	
	/**
	 * 删除坐标点
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.POINT_MANAGE_CN, opType=OperationType.Del, opText="删除坐标点信息")
	public void removePointByID(Long id) {
		Point point = pointDao.get(id);
		pointDao.remove(point);
	}
	
	/**
	 * 修改坐标点
	 * @param point
	 * @return
	 */
	@Override
	@SystemOperationLogAnnotation(appModule=AppModule.POINT_MANAGE_CN, opType=OperationType.Update, opText="修改坐标点信息")
	public Point updatePoint(Point point) {
		Point newPoint = pointDao.get(point.getId());
//		newPoint.setProject(point.getProject());
//		newPoint.setProjectScale(point.getProjectScale());
//		newPoint.setProjectContent(point.getProjectContent());
//		newPoint.setApprovalFiles(point.getApprovalFiles());
//		newPoint.setConstructionMode(point.getConstructionMode());
//		newPoint.setShouldAccount(point.getShouldAccount());
//		newPoint.setJz(point.getJz());
//		newPoint.setJy(point.getJy());
//		newPoint.setReimbursementRate(point.getReimbursementRate());
//		newPoint.setCheckStatus(point.getCheckStatus());
		try {
			pointDao.update(newPoint);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return newPoint;
	}

	/**
	 * 根据id查询坐标点
	 */
	@Override
	public Point findPointById(Long id) {
		Point point = pointDao.get(id);
		return point;
	}

	@Override
	public List<Point> getProjectsRange(Long determinePid) {
		return pointDao.findProjectsRange(determinePid);
	}

	@Override
	public void deleteRange(Long adjustmentPid, Integer rangeNumber) {
		List<Point> list = pointDao.findPointByProjectIdAndRangeNumberAnd(adjustmentPid,rangeNumber);
		for (Point point : list) {
			pointDao.remove(point);
		}
	}
}
