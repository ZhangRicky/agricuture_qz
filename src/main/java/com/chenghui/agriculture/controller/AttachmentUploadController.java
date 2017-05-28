package com.chenghui.agriculture.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.chenghui.agriculture.model.FileUpload;
import com.chenghui.agriculture.model.PictureUpload;
import com.chenghui.agriculture.model.Project;
import com.chenghui.agriculture.service.projectManage.AttachmentUploadService;



@RequestMapping("/attachmentUpload")
@RestController
public class AttachmentUploadController {
	
	@Autowired
	AttachmentUploadService attachmentUploadService;

	 @RequestMapping(value = "/search", method = RequestMethod.GET)
		public PaginationSupport<Project> getProjectList(@RequestParam("page") int page,
											@RequestParam("pageSize") int pageSize,
											@RequestParam("name") String name) {
		 Project project = new Project();
	    	project.setProjectName(name);

			ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
			
			PaginationSupport<Project> pagination = null;
			try {
				pagination = attachmentUploadService.findProjectListForPage(project, pageSize, page, shiroUser);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return pagination;
		}
	
	 /**
	  * 上传附件
	  * @param file
	  * @param projectId
	  * @param request
	  * @return
	  */
	 @RequestMapping(value="/uploadFile", method=RequestMethod.POST)
	    public RestResult uploadFileHandler(@RequestParam("files") MultipartFile file,@RequestParam("projectId") Long projectId,HttpServletRequest request ){
	    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
	    	
	    	String fileName = "";
			try{
				if (file == null || file.isEmpty()) {
					throw new BusinessServiceException("文件内容为空。");
				}
				fileName = new String(file.getOriginalFilename());
				String rootPath = request.getServletContext().getRealPath("/");
				int uploadFile = attachmentUploadService.projectImport(file,projectId,rootPath,shiroUser);
				if (uploadFile == 1 || uploadFile == 3) {
					return new RestResult(0, "文件《"+fileName+"》导入成功！");
				}else {
					return new RestResult(-1, "文件《"+fileName+"》导入失败，原因：验证失败！");
				}
			}catch(Exception e){
				e.printStackTrace();
				return new RestResult(-1, "文件《"+fileName+"》导入失败, 原因："+e.getMessage());
			}
	    }
	 
	 /**
	  * 上传批复文件
	  * @param file
	  * @param projectId
	  * @param request
	  * @return
	  */
	 @RequestMapping(value="/piFuUploadFile", method=RequestMethod.POST)
	    public RestResult piFuUploadFile(@RequestParam("file") MultipartFile file,@RequestParam("projectId") Long projectId,HttpServletRequest request ){
	    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
	    	
	    	String fileName = "";
			try{
				if (file == null || file.isEmpty()) {
					throw new BusinessServiceException("文件内容为空。");
				}
				fileName = new String(file.getOriginalFilename());
				String rootPath = request.getServletContext().getRealPath("/");
				int uploadFile = attachmentUploadService.fileImport(file,projectId,rootPath,shiroUser);
				if (uploadFile == 1 || uploadFile == 3) {
					return new RestResult(0, "文件《"+fileName+"》导入成功！");
				}else {
					return new RestResult(-1, "文件《"+fileName+"》导入失败，原因：验证失败！");
				}
			}catch(Exception e){
				e.printStackTrace();
				return new RestResult(-1, "文件《"+fileName+"》导入失败, 原因："+e.getMessage());
			}
	    }
	 
	 /**
	  * 查找公示文件
	  * @param projectId
	  * @return
	  */
	 @RequestMapping(value = "/searchPicture/{projectId:\\d+}", method = RequestMethod.GET)
		public List<PictureUpload> getAllPicture(@PathVariable Long projectId) {
	    	System.out.println("======================================"+projectId);
	    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
	    	List<PictureUpload> pictureList = attachmentUploadService.getAllPicture(projectId);
			return pictureList;
		}
	 
	 /**
	  * 查找批复文件
	  * @param projectId
	  * @return
	  */
	 @RequestMapping(value = "/searchFile/{projectId:\\d+}", method = RequestMethod.GET)
		public List<FileUpload> getAllFile(@PathVariable Long projectId) {
	    	System.out.println("======================================"+projectId);
	    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
	    	List<FileUpload> pictureList = attachmentUploadService.getAllFile(projectId);
			return pictureList;
		}
	 
	 /**
	  * 查看项目详情
	  * @param id
	  * @return
	  */
	 @RequestMapping(value = "/searchDetail/{id:\\d+}", method = RequestMethod.GET)
	    public List<Project> getProjectDetailList(@PathVariable Long id) {
	    	
	    	ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
	    	
	    	List<Project> pagination = attachmentUploadService.findProjectDetailListForPage(id, shiroUser);
	    	
	    	return pagination;
	    }
	 
	 /**
	  *删除公示附件图片或文件
	  * @param pictureId
	  * @return
	  */
	 @RequestMapping(value = "/delete/{pictureId:\\d+}", method = RequestMethod.DELETE)
		public RestResult removePicture(@PathVariable Long pictureId) {
	    	try {
	    		attachmentUploadService.removeByID(pictureId);
	        	return new RestResult(0,"删除成功");
	    	} catch (DataIntegrityViolationException ve) {
				throw new RestException(HttpStatus.CONFLICT, "操作失败" + ve.getMessage());
		    } catch (Exception e) {
		    	throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败" + e.getMessage());
		    }
		}
	 
	 
	 /**
	  * 删除批复图片或文件
	  * @param pictureId
	  * @return
	  */
	 @RequestMapping(value = "/deletePiFuFile/{pictureId:\\d+}", method = RequestMethod.DELETE)
		public RestResult removePiFuFile(@PathVariable Long pictureId) {
	    	try {
	    		attachmentUploadService.removePiFuFile(pictureId);
	        	return new RestResult(0,"删除成功");
	    	} catch (DataIntegrityViolationException ve) {
				throw new RestException(HttpStatus.CONFLICT, "操作失败" + ve.getMessage());
		    } catch (Exception e) {
		    	throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败" + e.getMessage());
		    }
		}
	 
	 
}
