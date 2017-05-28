package com.chenghui.agriculture.controller;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.hibernate.AssertionFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.exception.RestException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.RestResult;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.service.system.AreaService;


@RequestMapping("/area")
@RestController
public class AreaController {
	
	@Autowired
	AreaService areaService;
	
	/**
     * 根据传过来的areaId获得它的所有子节点用于项目添加时地区的选项下来列表
     * @param areaId
     * @author LLJ
     */
	@RequestMapping(value = "/getAreaChild/{areaId:\\d+}", method = RequestMethod.GET)
	public List<Area> getAreaChild(@PathVariable Long areaId) throws RestException{
		List<Area> list = areaService.getAreaChlidList(areaId);
		return list;
    	
	}
	
	@RequestMapping(value = "/getArea/{areaId:\\d+}", method = RequestMethod.GET)
	public Area getArea(@PathVariable Long areaId) throws RestException{
		Area area = areaService.get(areaId);
		return area;
    	
	}
	
	/**
	 * 查找地区
	 * @param areaName
	 * @return
	 * @author byq
	 */
	@RequestMapping(value="/search", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Area> getOrganization(String areaName){
		Area area = new Area();
		area.setAreaName(areaName);
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		if(StringUtils.isEmpty(areaName)){
			//return areaService.findAll();
			if(!shiroUser.isSuperAdmin){
				Set<Area> areaSet = shiroUser.areas;
				List<Area> list1 = areaService.findIsNotSuperAdmin(shiroUser,areaSet);
				return list1;
			}else{
				List<Area> list = areaService.findArea();
				return list;
			}
		}else{
			return areaService.getParentOrganization(areaName);
		}
	}
	
	
	/**
	 * 添加地区
	 * @param area
	 * @return
	 * @author byq
	 */
	 @RequestMapping(value = "/add", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		public RestResult addOrganization(@RequestBody Area area) {
	    	try {
	    		areaService.addOrganization(area);
	    		return new RestResult(0, "增加地区成功!");
			} catch(AssertionFailure e){
				return new RestResult(-1, e.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
				throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
	    	}
		}
	 
	/**
	 * 修改地区
	 * @param area
	 * @return
	 * @author byq
	 */
	 @RequestMapping(value = "/update", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
		public RestResult updateOrganization(@RequestBody Area area){
			try {
				Area organization2 = areaService.get(area.getId());
				organization2.setAreaName(area.getAreaName());
				organization2.setAreaCode(area.getAreaCode());
				organization2.setParentCode(area.getParentCode());
				organization2.setAreaLevel(area.getAreaLevel());
				areaService.updateOrganization(organization2);
			}catch (Exception e) {
				return new RestResult(-1, e.getMessage());
			}
			return new RestResult("添加地区", "修改地区成功！");
		}
	 
	 /**
	  * 删除地区
	  * @param id
	  * @return
	  * @throws RestException
	  * @author byq
	  */
	 @RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.DELETE)
		public RestResult removeOrganization(@PathVariable Long id) throws RestException{
	    	try {
	    		areaService.removeOrganization(id);
				return new RestResult("地区", "删除成功");
			}catch (BusinessServiceException busex) {
				busex.printStackTrace();
				return new RestResult("地区", -1, busex.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
				throw new RestException(HttpStatus.SERVICE_UNAVAILABLE, "地区-删除 错误");
			}
		}
	 
	@RequestMapping(value = "/parentOrganizations", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Area> getParentOrganizations(){
		List<Area> organizationList = areaService.getParentOrganizations();
		return organizationList;
	}
	
	
}
