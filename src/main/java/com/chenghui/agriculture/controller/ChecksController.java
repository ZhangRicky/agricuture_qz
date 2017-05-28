package com.chenghui.agriculture.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.model.Checks;
import com.chenghui.agriculture.service.supervisonManage.ChecksService;

@RequestMapping("/checks")
@RestController
public class ChecksController {

	@Autowired
	private ChecksService checksService;
	
	@RequestMapping(value="/get/{sId:\\d+}",method = RequestMethod.GET)
	public List<Checks> getCheckssBySubProjectId(@PathVariable Long sId){
		return checksService.findChecksBySId(sId);
	}
	
    @RequestMapping(value = "/search", method = RequestMethod.GET)
	public PaginationSupport<Checks> getChecksImportList(@RequestParam("page") String page,
										@RequestParam("pageSize") String pageSize,
										@RequestParam("sId") Long sId
										) {
    	Checks checksParam = new Checks();
		checksParam.setSubProjectId(sId);
		
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		
		PaginationSupport<Checks> pagination = checksService.findChecksListForPage(checksParam, Integer.parseInt(pageSize), Integer.parseInt(page), shiroUser);

		return pagination;
	}
	
}
