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
import com.chenghui.agriculture.model.Supervision;
import com.chenghui.agriculture.service.supervisonManage.SupervisionService;

@RequestMapping("/supervision")
@RestController
public class SupervisionController {

	@Autowired
	private SupervisionService supervisionService;
	
	@RequestMapping(value="/get/{sId:\\d+}",method = RequestMethod.GET)
	public List<Supervision> getSupervisionsBySubSupervisionId(@PathVariable Long sId){
		return supervisionService.findSupervisionByPId(sId);
	}
	
    @RequestMapping(value = "/search", method = RequestMethod.GET)
	public PaginationSupport<Supervision> getSupervisionImportList(@RequestParam("page") String page,
										@RequestParam("pageSize") String pageSize,
										@RequestParam("sId") Long sId
										) {
    	Supervision supervisionParam = new Supervision();
		supervisionParam.setSubProjectId(sId);
		
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		
		PaginationSupport<Supervision> pagination = supervisionService.findSupervisionListForPage(supervisionParam, Integer.parseInt(pageSize), Integer.parseInt(page), shiroUser);

		return pagination;
	}
    
	
}
