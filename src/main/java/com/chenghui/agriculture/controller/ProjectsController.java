package com.chenghui.agriculture.controller;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.hibernate.AssertionFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chenghui.agriculture.core.exception.BusinessServiceException;
import com.chenghui.agriculture.core.exception.RestException;
import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.utils.DateUtil;
import com.chenghui.agriculture.core.utils.FileExportUtil;
import com.chenghui.agriculture.core.utils.FileOperateUtil;
import com.chenghui.agriculture.core.utils.RestResult;
import com.chenghui.agriculture.model.Area;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.vo.ProjectsSumTotalFund;
import com.chenghui.agriculture.service.projectManage.ProjectsService;



/**
 * 子项目Controller
 * @author LLJ
 * @version V1.0
 * @param <E>
 */
@RequestMapping("/projectsManage")
@RestController
public class ProjectsController<E> {
	
	
	private final static Logger logger = LoggerFactory.getLogger(ProjectsController.class); 
	
	@Autowired
	private ProjectsService projectsService;
	
	
	/**
	 * 项目调项记录
	 * @param areaId
	 * @param projectName
	 * @return
	 * @author LLJ
	 */
	@RequestMapping(value = "/searchAdjustmentRecord/{adjustmentAreaId:\\d+}/{projectName}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Projects> getAdjustmentRecord(@PathVariable Long adjustmentAreaId,@PathVariable String projectName){
			return projectsService.getAllAdjustmentRecord(adjustmentAreaId,projectName);	
	}
	
	/**
	 * 项目调项
	 * @param p
	 * @return
	 */
	@RequestMapping(value = "/addProjectsAdjustment", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult addAdjustmentProjects(@RequestBody Projects p) {   	
		try {
			projectsService.addAdjustment(p);
			return new RestResult("项目调项", "调项成功!");
		} catch (BusinessServiceException e) {
			logger.error(e.getMessage());
			return new RestResult("项目调项", "调项失败!"+e.getMessage());
    	}catch (Exception e) {
    		logger.error(e.getMessage());
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    	}
	}
	
	/**
	 * 初始化数据
	 * @param areaId
	 * @param projectName
	 * @return
	 * @author LLJ
	 */
	@RequestMapping(value = "/searchs", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Projects> searchAll(@RequestParam Long areaId, @RequestParam String projectName, @RequestParam String fundYear, @RequestParam Integer projectType){
		Projects projects = new Projects();
		projects.setProjectName(projectName);
		projects.setFundYear(fundYear);
		projects.setProjectType(projectType);
		projects.setAreaId(areaId);
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		return projectsService.getByProjects(projects, shiroUser);
	}
	
	/**
	 * 初始化数据
	 * @param areaId
	 * @param projectName
	 * @return
	 * @author LLJ
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Projects> getAll(String xxnr){
		Projects projects = new Projects();
		projects.setXxnr(xxnr);
		if (StringUtils.isEmpty(xxnr)) {
			return projectsService.getAllInit();
		}else{
			return projectsService.getAllInit(xxnr);	
		}
	}
	
	/**
	 * 查询树
	 * @param areaId
	 * @param projectName
	 * @return
	 * @author LLJ
	 */
	@RequestMapping(value = "/search/{areaId:\\d+}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Projects> getAllProjects(@PathVariable Long areaId,String xxnr){
			Projects projects = new Projects();
			projects.setXxnr(xxnr);
			if (StringUtils.isEmpty(xxnr)) {
				return projectsService.getAllProjects(areaId);
			}else{
				return projectsService.getParentProjects(xxnr);
			}
	}

	
	/**
	 * 项目数据的导出
	 * @param response
	 */
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void fileExport(HttpServletResponse response, HttpServletRequest request){
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
		Projects p = new Projects();
		List<Projects> projectList = projectsService.exportExcelList(p,shiroUser);
		int record = 2;
		//设置表头数据
		FileExportUtil.setExcelData();
		try {
			String downloadFileName = FileExportUtil.title+"-"+DateUtil.getCurrentDateTime2();
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", FileExportUtil.encodeFilename(request, downloadFileName));
	        response.setHeader(headerKey, headerValue);
	        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
				
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet excelSheet = workbook.createSheet(FileExportUtil.title);
			FileExportUtil.setExcelTitle(excelSheet, workbook);	
			FileExportUtil.setExcelHeader(excelSheet,workbook);	
			setExcelRecords(excelSheet, workbook, projectList,record);

			OutputStream out = response.getOutputStream();
			workbook.write(out);          
			out.flush();     
			out.close();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	/**
	 * 文件导出的样式设置和数据写入
	 * @param excelSheet	Sheet表
	 * @param workbook		工作薄对象
	 * @param projectList	写入的数据
	 * @param record		开始写入的行号
	 */
	public void setExcelRecords(XSSFSheet excelSheet, XSSFWorkbook workbook, List<Projects> projectList,int record ){
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);//设置居中
		cellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex()); //设置背景色
		cellStyle.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex()); //设置背景色
		XSSFFont font = workbook.createFont();
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		cellStyle.setFont(font);
		for (Object obj : projectList) {
			Projects p = (Projects) obj;
			XSSFRow excelRow = excelSheet.createRow(record++);
			XSSFCell headerCell0 = excelRow.createCell(0);
			headerCell0.setCellStyle(cellStyle);	
			if(p.getLevel()==3){headerCell0.setCellValue( "一级");}
			if(p.getLevel()==2){ headerCell0.setCellValue( "二级"); }
			if(p.getLevel()==1){headerCell0.setCellValue("三级");}
			
			XSSFCell headerCell1 = excelRow.createCell(1);
			headerCell1.setCellStyle(cellStyle);
			headerCell1.setCellValue((p.getArea()==null) ? "" : p.getArea().getAreaName());
			
			XSSFCell headerCell2 = excelRow.createCell(2);
			headerCell2.setCellStyle(cellStyle);
			headerCell2.setCellValue(p.getProjectName());
			
			XSSFCell headerCell3 = excelRow.createCell(3);
			headerCell3.setCellStyle(cellStyle);
			headerCell3.setCellValue(p.getReferenceNumber());
			
			XSSFCell headerCell4 = excelRow.createCell(4);
			headerCell4.setCellStyle(cellStyle);
			headerCell4.setCellValue((p.getProjectNumber()==null) ? "" : p.getProjectNumber()+"");
			
			XSSFCell headerCell5 = excelRow.createCell(5);
			headerCell5.setCellStyle(cellStyle);
			headerCell5.setCellValue(p.getCarryOutUnit());
			
			XSSFCell headerCell6 = excelRow.createCell(6);
			headerCell6.setCellStyle(cellStyle);
			headerCell6.setCellValue(p.getChargePerson());
			
			XSSFCell headerCell7 = excelRow.createCell(7);
			headerCell7.setCellStyle(cellStyle);
			headerCell7.setCellValue(p.getFundYear());
			
			XSSFCell headerCell8 = excelRow.createCell(8);
			headerCell8.setCellStyle(cellStyle);
			headerCell8.setCellValue(p.getSubjectName());
			
			XSSFCell headerCell9 = excelRow.createCell(9);
			headerCell9.setCellStyle(cellStyle);
			headerCell9.setCellValue(p.getFundType());
			
			XSSFCell headerCell10 = excelRow.createCell(10);
			headerCell10.setCellStyle(cellStyle);
			headerCell10.setCellValue(p.getApprovalNumber());
			
			XSSFCell headerCell11 = excelRow.createCell(11);
			headerCell11.setCellStyle(cellStyle);
			headerCell11.setCellValue(p.getScaleAndContent());
			
			XSSFCell headerCell12 = excelRow.createCell(12);
			headerCell12.setCellStyle(cellStyle);
			headerCell12.setCellValue(p.getStandbyNumber());
			
			XSSFCell headerCell13 = excelRow.createCell(13);
			headerCell13.setCellStyle(cellStyle);
			headerCell13.setCellValue(p.getTotalFund()==null ? "" : p.getTotalFund()+"");
			
			XSSFCell headerCell14 = excelRow.createCell(14);
			headerCell14.setCellStyle(cellStyle);
			headerCell14.setCellValue((p.getFinanceFund()== null) ? "" : p.getFinanceFund()+"");
			
			//覆盖人数
			XSSFCell headerCell16 = excelRow.createCell(16);
			headerCell16.setCellStyle(cellStyle);
			headerCell16.setCellValue("".equals(p.getCoveringNumber()) ? "" : p.getCoveringNumber()+"");
			
			//扶持贫困人口
			XSSFCell headerCell17 = excelRow.createCell(17);
			headerCell17.setCellStyle(cellStyle);
			headerCell17.setCellValue("".equals(p.getPovertyStrickenPeopleNumber()) ? "" : p.getPovertyStrickenPeopleNumber()+"");
			
			//完成期限
			XSSFCell headerCell18 = excelRow.createCell(18);
			headerCell18.setCellStyle(cellStyle);
			headerCell18.setCellValue("".equals(p.getDeadline()) ? "" : p.getDeadline()+"");
			//审核状态
			XSSFCell headerCell19 = excelRow.createCell(19);
			headerCell19.setCellStyle(cellStyle);
			if("".equals(p.getApproveState())){headerCell19.setCellValue("");}
			if("0".equals(p.getApproveState())){headerCell19.setCellValue("正在备案");}
			if("1".equals(p.getApproveState())){headerCell19.setCellValue("备案通过");}
			if("2".equals(p.getApproveState())){headerCell19.setCellValue("不通过");}
			
			//建设方式
			XSSFCell headerCell20 = excelRow.createCell(20);
			headerCell20.setCellStyle(cellStyle);
			headerCell20.setCellValue("1".equals(p.getConstructionMode()) ? "先款后建" : "0".equals(p.getConstructionMode()) ? "先建后款" : "");

			//验收状态
			XSSFCell headerCell21 = excelRow.createCell(21);
			headerCell21.setCellStyle(cellStyle);
			headerCell20.setCellValue("0".equals(p.getCheckStatus()) ? "已验收" : "1".equals(p.getCheckStatus()) ? "未验收" : "");

			//创建用户
			XSSFCell headerCell22 = excelRow.createCell(22);
			headerCell22.setCellStyle(cellStyle);
			headerCell22.setCellValue(p.getCreateUser());
			//创建时间
			XSSFCell headerCell23 = excelRow.createCell(23);
			headerCell23.setCellStyle(cellStyle);
			headerCell23.setCellValue(p.getCreateTime());
			//录入状态
			XSSFCell headerCell24 = excelRow.createCell(24);
			headerCell24.setCellStyle(cellStyle);
			headerCell24.setCellValue(p.getInputStatus());
			
			//当前审核部门
			XSSFCell headerCell25 = excelRow.createCell(25);
			headerCell25.setCellStyle(cellStyle);
			headerCell25.setCellValue(p.getApprovingDepartment());
			//备注
			XSSFCell headerCell26 = excelRow.createCell(26);
			headerCell26.setCellStyle(cellStyle);
			headerCell26.setCellValue(p.getRemark());
	
		}
	} 
	
	/**
	 * 添加子项目
	 * @param subProject
	 * @return
	 * @author LLJ
	 */
	@RequestMapping(value = "/addProjects",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult addSubProject(@RequestBody Projects projects){
		try {
			projectsService.addProjects(projects);
			return new RestResult(0, "添加项目信息成功!");
		} catch (BusinessServiceException busyEx) {
			return new RestResult(-1, busyEx.getLocalizedMessage());
		} catch (AssertionFailure e) {
			return new RestResult(-1, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
		}
	}
	
	/**
	 * 编辑子项目
	 * @param subProject
	 * @return
	 * @author LLJ
	 */
	@RequestMapping(value = "/updateProjects",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public RestResult updateSubProjects(@RequestBody Projects projects){
		System.out.println("update");
		try {
			projectsService.updateProjectsInfo(projects);
			return new RestResult(0, "修改项目信息成功!");
		} catch (BusinessServiceException busyEx) {
			return new RestResult(-1, busyEx.getLocalizedMessage());
		} catch (AssertionFailure e) {
			return new RestResult(-1, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
		}
	}
	
	
	/**
	 * 删除子项目信息
	 * @param id
	 * @return
	 * @author LLJ
	 */
	@RequestMapping(value = "/delete/{id:\\d+}", method = RequestMethod.DELETE)
	public RestResult removeProjects(@PathVariable Long id) {
    	try {
    		projectsService.removeByID(id);
        	return new RestResult(0,"删除成功");
    	} catch (DataIntegrityViolationException ve) {
			throw new RestException(HttpStatus.CONFLICT, "操作失败" + ve.getMessage());
	    } catch (Exception e) {
	    	throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败" + e.getMessage());
	    }
	}
	
	/**
	 * 添加项目公示说明
	 * @param projects
	 * @return
	 */
	 @RequestMapping(value = "/add", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	   	public RestResult updateUser(@RequestBody Projects projects) {
	   		try {
	   			projectsService.updateRemark(projects);
	       		return new RestResult("0", "修改成功");
	       	}catch (BusinessServiceException e) {
	   			return new RestResult(-1,e.getMessage());
	       	}catch (Exception e) {
	   			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败");
	       	}
	   	}
	 
	 /**
	  * 查找地区
	  * @param areaId
	  * @return
	  */
		@RequestMapping(value = "/sreachArea/{areaId:\\d+}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
		public List<Area> getAllArea(@PathVariable Long areaId){	
			return projectsService.findAllArea(areaId);
		}
		
		/**
		 * 根据子项目查找主项目的信息
		 * @param parentId
		 * @return
		 */
		@RequestMapping(value = "/sreachParent/{parentId:\\d+}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
		public List<Projects> sreachParent(@PathVariable Long parentId){	
			return projectsService.findProjectParent(parentId);
		}
		
		/**
		 * 查询项目的总的报账，拨款记录
		 * 	@author Ricky
		 */
		@RequestMapping(value = "/findByCount/{projectsID:\\d+}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
		public List<Projects> findByCount(@PathVariable Long projectsID){
			Projects project = projectsService.get(projectsID);
			List<Projects> list = new ArrayList<Projects>();
			list.add(project);
			return list;
		}

		
		/**
		 * 覆盖项目
		 * @param projectNumber
		 * @return
		 */
		@RequestMapping(value = "/getProject/{projectNumber:\\d+}/{projectDetailId:\\d+}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
		public RestResult getProject(@PathVariable String projectNumber,@PathVariable Long projectDetailId){	
			try {
				 projectsService.updateProject(projectNumber,projectDetailId);
	        	return new RestResult(0,"覆盖成功");
	    	} catch (DataIntegrityViolationException ve) {
				throw new RestException(HttpStatus.CONFLICT, "操作失败" + ve.getMessage());
		    } catch (Exception e) {
		    	throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败" + e.getMessage());
		    }
			
		}
		
		/**
		 * 下载文件
		 * @param fileNa
		 * @param prefix
		 * @param request
		 * @param response
		 * @throws IOException
		 */
		  @RequestMapping(value="/downloadTemplate/PictureUploadFiles_img/{fileNa}/{prefix}", method = RequestMethod.GET)
		    public void doDownload( @PathVariable String fileNa,@PathVariable String prefix,HttpServletRequest request, HttpServletResponse response) throws IOException {
		    	ServletContext context = request.getServletContext();
				String appPath = context.getRealPath("/");
				String templatePath = "/PictureUploadFiles_img/";
				String fullPath = appPath + templatePath + fileNa;
				System.out.println("===================="+fullPath);
		        File downloadFile = new File(fullPath);
//		        if (fullPath != appPath + templatePath + fileNa + ".txt") {
//		        	fullPath = appPath + templatePath + fileNa + ".xls";
//				}
		        
		        // get MIME type of the file
		        String mimeType = context.getMimeType(fullPath);
		        if (mimeType == null) {
		            // set to binary type if MIME mapping not found
		            mimeType = "application/octet-stream";
		        }
		        
		        try {
		        	FileOperateUtil.download(request, response, fullPath, mimeType, fileNa);
				} catch (Exception e) {
					e.printStackTrace();
				}
		 
		    }
		  
		  /**
		   * 找到主项目下所有子项目的资金总和
		   * @param pid
		   * @return
		   */
			@RequestMapping(value = "/findTotalFund/{pid:\\d+}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
			public List<ProjectsSumTotalFund> getProjectTotalFund(@PathVariable Long pid){	
				List<ProjectsSumTotalFund> result =  projectsService.findTotalFund(pid); 
				return result;
			}
			
			/**
			 * 查找主项目的总资金
			 * @param updateParentId
			 * @return
			 */
			 @RequestMapping(value = "/findProjectsTotalFund/{updateParentId:\\d+}", method = RequestMethod.GET)
				public Projects findProjectsTotalFund(@PathVariable Long updateParentId) {
				 Projects result = projectsService.getProjectsTotalFund(updateParentId);
					return result;
				}
			 
		/**
		 * 查询项目下面是否还有子项目
		 */
		@RequestMapping(value = "/findById/{id:\\d+}", method = RequestMethod.GET)
		public List<Projects> findById(@PathVariable Long id){
			Projects p = new Projects();
			p.setId(id);
			List<Projects> result =projectsService.getChildList(p);
			return result;
			
		}
}
