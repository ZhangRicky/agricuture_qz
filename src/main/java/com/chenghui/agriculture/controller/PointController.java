package com.chenghui.agriculture.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.AssertionFailure;
import org.hibernate.dialect.FirebirdDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.exception.RestException;
import com.chenghui.agriculture.core.utils.RestResult;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Point;
import com.chenghui.agriculture.service.supervisonManage.PointService;
import com.mysql.fabric.xmlrpc.base.Array;

@RequestMapping("/point")
@RestController
public class PointController {
	
	private final static Logger logger = LoggerFactory.getLogger(PointController.class); 
	@Autowired
	private PointService pointService;
	
	/**
     * 根据项目id查询该项目的范围
     * @param areaId
     * @author LLJ
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
     */
	@RequestMapping(value = "/getRange/{determinePid:\\d+}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<List<Point>> getRange(@PathVariable Long determinePid) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		List<Point> list = pointService.getProjectsRange(determinePid);
		List<List<Point>> lines= new ArrayList<List<Point>>();
		Map<Integer, List<Point>> m = new HashMap<>();
		for (Point point : list) {
			if(m.get(point.getRangeNumber())==null){
				List<Point> p = new ArrayList<Point>();
				p.add(point);
				m.put(point.getRangeNumber(), p);
			}else{
				m.get(point.getRangeNumber()).add(point);
			}
		}
		Iterator<List<Point>> it = m.values().iterator();
		while(it.hasNext()){
			lines.add(it.next());
		}
		return lines;
	}
	
	/**
	 * 项目调项
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/addRange/{id:\\d+}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult addRange(@RequestBody List<List<Point>> pointsArray, @PathVariable Long id) {
		try {
			pointService.addRange(pointsArray,id);
    		return new RestResult(0, "确定范围成功!");
		} catch(BusinessServiceException busyEx) {
			return new RestResult(-1, busyEx.getLocalizedMessage());
		} catch(AssertionFailure e){
			return new RestResult(-1, e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
    	}
	}
	
	/**
	 * 删除项目范围
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/deleteRange/{adjustmentPid:\\d+}/{rangeNumber:\\d+}", method = RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult deleteRange(@PathVariable Long adjustmentPid,@PathVariable Integer rangeNumber) {
		try {
			pointService.deleteRange(adjustmentPid,rangeNumber);
			return new RestResult(0, "删除成功!");
		} catch(BusinessServiceException busyEx) {
			return new RestResult(-1, busyEx.getLocalizedMessage());
		} catch(AssertionFailure e){
			return new RestResult(-1, e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
		}
	}
	
}
