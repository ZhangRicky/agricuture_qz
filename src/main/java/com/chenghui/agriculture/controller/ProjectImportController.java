package com.chenghui.agriculture.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.DateUtil;
import com.chenghui.agriculture.core.utils.FileOperateUtil;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.RestResult;
import com.chenghui.agriculture.model.ProjectImport;
import com.chenghui.agriculture.model.ProjectImportDetail;
import com.chenghui.agriculture.model.Template;
import com.chenghui.agriculture.model.TemplateType;
import com.chenghui.agriculture.service.projectManage.ProjectImportService;

/**
 * @author yudq
 * @date 2016-03-02 17:09:00
 * @version V1.0
 *
 */
@RequestMapping("/projectImport")
@RestController
public class ProjectImportController {
	
	@Autowired
	private ProjectImportService projectImportService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
	public PaginationSupport<ProjectImport> getProjectImportList(@RequestParam("page") String page,
										@RequestParam("pageSize") String pageSize,
										@RequestParam("fileName") String fileName,
										@RequestParam("searchStartTime") String searchStartTime,
										@RequestParam("searchEndTime") String searchEndTime,
										@RequestParam("importStatus") Integer importStatus
										) {
		ProjectImport projectImportParam = new ProjectImport();
		projectImportParam.setFileName(fileName);
		projectImportParam.setSearchStartTime(DateUtil.formatString2Date(searchStartTime));
		projectImportParam.setSearchEndTime(DateUtil.formatString2Date(searchEndTime));
		projectImportParam.setImportStatus(importStatus);
		
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		
		PaginationSupport<ProjectImport> pagination = projectImportService.findProjectImportListForPage(projectImportParam, Integer.parseInt(pageSize), Integer.parseInt(page), shiroUser);

		return pagination;
	}
    
    @RequestMapping(value="/downloadTemplate/{templateName}/{saveAsFileName}", method = RequestMethod.GET)
    public void doDownload( @PathVariable String templateName, @PathVariable String saveAsFileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	ServletContext context = request.getServletContext();
		String appPath = context.getRealPath("/");
		String templatePath = "/resources/templates/";
		String fullPath = appPath + templatePath+templateName+".xls";
		System.out.println("===================="+fullPath);
        File downloadFile = new File(fullPath);
        if (!downloadFile.exists()) {
        	fullPath += "x";
		}
        
        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        
        try {
        	FileOperateUtil.download(request, response, fullPath, mimeType, saveAsFileName + fullPath.substring(fullPath.lastIndexOf(".")));
		} catch (Exception e) {
			e.printStackTrace();
		}
 
    }
    
    @RequestMapping(value = "/searchDetail/{projectImport_id:\\d+}", method = RequestMethod.GET)
    public PaginationSupport<ProjectImportDetail> getProjectImportDetailList(@RequestParam("page") String page,
    		@RequestParam("pageSize") String pageSize,
    		@RequestParam("searchName") String searchName,
    		@PathVariable Long projectImport_id
    		) {
    	ProjectImportDetail projectImportDetailParam = new ProjectImportDetail();
    	projectImportDetailParam.setProjectImport(projectImportService.get(projectImport_id));
//    	projectImportDetailParam.setName(searchName);
    	
    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	
    	PaginationSupport<ProjectImportDetail> pagination = projectImportService.findProjectImportDetailListForPage(projectImportDetailParam, Integer.parseInt(pageSize), Integer.parseInt(page), shiroUser);
    	
    	return pagination;
    }
    
    
    @RequestMapping(value = "/getTemplateList", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Template> getTemplateList() {
    	return projectImportService.findTemplateByType(TemplateType.TEMPLATE_TYPE_PROJECT);
    }
    
    @RequestMapping(value="/uploadFile", method=RequestMethod.POST)
    public RestResult uploadFileHandler(@RequestParam("file") MultipartFile file, HttpServletRequest request ){
    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String fileName = "";
		try{
			if (file == null || file.isEmpty()) {
				throw new BusinessServiceException("文件内容为空。");
			}
			fileName = file.getOriginalFilename();//new String(file.getOriginalFilename().getBytes("ISO8859-1"), "UTF-8");
			String rootPath = request.getServletContext().getRealPath("/");
			int projectImportStatus = projectImportService.projectImport(file,rootPath, shiroUser);
			if (projectImportStatus == 1 || projectImportStatus == 3) {
				return new RestResult(0, "文件《"+fileName+"》导入成功！");
			}else {
				return new RestResult(-1, "文件《"+fileName+"》导入失败，原因：验证失败！");
			}
		}catch(Exception e){
			e.printStackTrace();
			return new RestResult(-1, "文件《"+fileName+"》导入失败, 原因："+e.getMessage());
		}
    }
}
