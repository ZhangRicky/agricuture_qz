package com.chenghui.agriculture.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.exception.RestException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.RestResult;
import com.chenghui.agriculture.model.Project;
import com.chenghui.agriculture.service.projectManage.ProjectService;

/**
 * @author yudq
 * @date 2016-03-04 14:32:33
 * @version V1.0
 *
 */
@RequestMapping("/project")
@RestController
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
	public PaginationSupport<Project> getProjectImportList(@RequestParam("page") String page,
										@RequestParam("pageSize") String pageSize,
										@RequestParam("name") String name
										) {
		Project projectParam = new Project();
		projectParam.setProjectName(name);
		
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		
		PaginationSupport<Project> pagination = projectService.findProjectListForPage(projectParam, Integer.parseInt(pageSize), Integer.parseInt(page), shiroUser);

		return pagination;
	}
    
    
    @RequestMapping(value = "/update", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
   	public RestResult updateUser(@RequestBody Project project) {
   		try {
   			projectService.updateRemark(project);
       		return new RestResult("0", "修改成功");
       	}catch (BusinessServiceException e) {
   			return new RestResult(-1,e.getMessage());
       	}catch (Exception e) {
   			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
       	}
   	}
    
    /**
     * 查询主项目名称用于添加子项目
     * @return
     */
    @RequestMapping(value = "/getPrjoectList", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
   	public List<Project> findAllProjectList() {
    	List<Project> pagination = projectService.findAllProject();
		return pagination;
   	}
     
}
