package com.chenghui.agriculture.controller;


import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.exception.RestException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.RestResult;
import com.chenghui.agriculture.model.Farmer;
import com.chenghui.agriculture.service.projectManage.FarmerService;


/**
 * 
 * @date 2016-03-02 17:09:00
 * @version V1.0
 *
 */
@RequestMapping("/farmerImport")
@RestController
public class FarmerController {
	
	@Autowired
	private FarmerService farmerService;
	
	private final static Logger logger = LoggerFactory.getLogger(FarmerController.class); 
	
    /**
     * 导入农户信息
     * @param fileFarmer
     * @param subProject_id
     * @param request
     * @return
     */
    @RequestMapping(value="/uploadFarmer/{subProject_id:\\d+}", method=RequestMethod.POST)
    public RestResult uploadFileHandler(@RequestParam("fileFarmer") MultipartFile fileFarmer,@PathVariable Long subProject_id, HttpServletRequest request ){
    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String fileName = "";
		try{
			if (fileFarmer == null || fileFarmer.isEmpty()) {
				throw new BusinessServiceException("文件内容为空。");
			}
			fileName = fileFarmer.getOriginalFilename();//new String(file.getOriginalFilename().getBytes("ISO8859-1"), "UTF-8");
			String rootPath = request.getServletContext().getRealPath("/");
			int projectImportStatus = farmerService.projectImport(fileFarmer,rootPath,subProject_id,shiroUser);
			if (projectImportStatus == 1 || projectImportStatus == 3) {
				return new RestResult(0, "文件《"+fileName+"》导入成功！");
			}else {
				return new RestResult(-1, "文件《"+fileName+"》导入失败，原因：验证失败！");
			}
		}catch (BusinessServiceException e) {
    		logger.error(e.getMessage());
			return new RestResult(-2,e.getMessage());
    	}catch(Exception e){
			e.printStackTrace();
			return new RestResult(-1, "文件《"+fileName+"》导入失败, 原因："+e.getMessage());
		}
    }
    
    /**
     * 根据项目査找农户信息
     * @param page
     * @param pageSize
     * @param searchName
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/searchDetail/{projectId:\\d+}", method = RequestMethod.GET)
    public PaginationSupport<Farmer> getProjectImportDetailList(@RequestParam("page") String page,
    		@RequestParam("pageSize") String pageSize,
    		@RequestParam("searchName") String searchName,
    		@PathVariable Long projectId
    		) {
    	
    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	
    	PaginationSupport<Farmer> pagination = farmerService.findProjectImportDetailListForPage(projectId, Integer.parseInt(pageSize), Integer.parseInt(page), shiroUser);
    	
    	return pagination;
    }
    
    /**
	  *删除农户信息
	  * @param farmerId
	  * @return
	  */
	 @RequestMapping(value = "/deleteFarmer/{farmerId:\\d+}", method = RequestMethod.DELETE)
		public RestResult removeFarmer(@PathVariable Long farmerId) {
	    	try {
	    		farmerService.removeByID(farmerId);
	        	return new RestResult(0,"删除成功");
	    	} catch (DataIntegrityViolationException ve) {
				throw new RestException(HttpStatus.CONFLICT, "操作失败" + ve.getMessage());
		    } catch (Exception e) {
		    	throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败" + e.getMessage());
		    }
		}
	 
	 
	 /**
	  * 修改农户信息
	  * @param farmer
	  * @return
	  */ 
	 @RequestMapping(value = "/updateFarmer", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
		public RestResult updateFarmer(@RequestBody Farmer farmer) {
			try {
				farmerService.updateFarmerInfo(farmer);
	    		return new RestResult("0", "修改农户信息成功!");
	    	}catch (BusinessServiceException e) {
	    		logger.error(e.getMessage());
				return new RestResult(-1,e.getMessage());
	    	}catch (Exception e) {
				throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
	    	}
		}
}
