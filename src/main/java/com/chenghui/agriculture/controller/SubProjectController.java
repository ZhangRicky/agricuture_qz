package com.chenghui.agriculture.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.chenghui.agriculture.core.constant.AppModule;
import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.exception.RestException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.FileOperateUtil;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.core.utils.RestResult;
import com.chenghui.agriculture.model.SubProject;
import com.chenghui.agriculture.service.projectManage.SubProjectService;

/**
 * 子项目Controller
 * @author LLJ
 * @version V1.0
 */
@RequestMapping("/subProjectManage")
@RestController
public class SubProjectController {
	private final static Logger logger = LoggerFactory.getLogger(SubProjectController.class); 
	@Autowired
	private SubProjectService subProjectService;
	
	/**
	 * 查询所有子项目
	 * @param page
	 * @param pageSize
	 * @param projectScale
	 * @return
	 */
	@RequestMapping(value = "/search/all/{project_id:\\d+}", method = RequestMethod.GET)
	public PaginationSupport<SubProject> getSubProjectListByPid(@RequestParam("page") String page,
			@RequestParam("pageSize") String pageSize,
			@RequestParam("xxnr") String xxnr,
			@PathVariable Long project_id) {
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		PaginationSupport<SubProject> pagination = subProjectService.findSubProjectListByPid(xxnr,Integer.parseInt(pageSize), Integer.parseInt(page), shiroUser,project_id);
		return pagination;
	}
	
	/**
	 * 查询所有子项目
	 * @param page
	 * @param pageSize
	 * @param projectScale
	 * @return
	 */
	@RequestMapping(value = "/search/all", method = RequestMethod.GET)
	public PaginationSupport<SubProject> getProjectImportList(@RequestParam("page") String page,
			@RequestParam("pageSize") String pageSize,@RequestParam("xxnr") String xxnr) {
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		PaginationSupport<SubProject> pagination = subProjectService.findSubProjectListForPage(xxnr,Integer.parseInt(pageSize), Integer.parseInt(page), shiroUser);
		return pagination;
	}

	
	/**
	 * 删除子项目信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.DELETE)
	public RestResult removeSubProject(@PathVariable Long id) {
    	try {
    		subProjectService.removeByID(id);
        	return new RestResult(0,"删除成功");
    	} catch (DataIntegrityViolationException ve) {
			throw new RestException(HttpStatus.CONFLICT, "操作失败" + ve.getMessage());
	    } catch (Exception e) {
	    	throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败" + e.getMessage());
	    }
	}
	 
	/**
	 * 添加子项目
	 * @param subProject
	 * @return
	 */
	@RequestMapping(value = "/addSubProject",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView addSubProject(@ModelAttribute("subProject") SubProject subProject,@RequestParam("file") MultipartFile[] files,HttpServletRequest request,HttpServletResponse response,@RequestParam("subPid") Long subPid){
		try {
				String path=null;
				String fileName=null;
				StringBuffer sb = new StringBuffer();
				for (MultipartFile multipartFile : files) {
					if (multipartFile == null || multipartFile.isEmpty()) {
						continue;
					}
				//String rootPath = request.getServletContext().getRealPath("/");
				//fileName = new String(multipartFile.getOriginalFilename().getBytes("ISO8859-1"), "UTF-8");
				path = FileOperateUtil.UPLOADDIR_SUPPROJECT;
				fileName = path+multipartFile.getOriginalFilename();
				File targetFile = new File(path);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				byte[] bytes = multipartFile.getBytes();
				BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(fileName));
				buffStream.write(bytes);
				buffStream.close();
				sb.append(fileName + ";");
			}
		    if (subPid!=null) {
				subProject.setPath(sb.toString());
				subProjectService.updateSubProjectInfo(subProject,subPid);
			}else{
				subProject.setPath(sb.toString());
				subProjectService.addSubProject(subProject);
			}
		} catch (BusinessServiceException e) {
			logger.error(e.getMessage());
	    }catch (Exception e) {
	    	logger.error(e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	    }
		
		//使用ModelAndView重定向到查询列表
		//return new ModelAndView("redirect:/"+AppModule.SUBPROJECT_MANAGE_EN);
		return new ModelAndView("redirect:/"+AppModule.ATTACHMENT_UPLOAD_EN);
	}
}
