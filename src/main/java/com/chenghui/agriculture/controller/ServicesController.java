package com.chenghui.agriculture.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.chenghui.agriculture.core.security.ShiroDBRealm.ShiroUser;
import com.chenghui.agriculture.core.security.http.BearerAuthenticationToken;
import com.chenghui.agriculture.core.utils.FileOperateUtil;
import com.chenghui.agriculture.core.utils.PaginationSupport;
import com.chenghui.agriculture.dao.projectManage.FinanceDao;
import com.chenghui.agriculture.model.Checks;
import com.chenghui.agriculture.model.Constant;
import com.chenghui.agriculture.model.Constants;
import com.chenghui.agriculture.model.Farmer;
import com.chenghui.agriculture.model.Finance;
import com.chenghui.agriculture.model.Point;
import com.chenghui.agriculture.model.PointBaidu;
import com.chenghui.agriculture.model.Project;
import com.chenghui.agriculture.model.Projects;
import com.chenghui.agriculture.model.SubProject;
import com.chenghui.agriculture.model.Supervision;
import com.chenghui.agriculture.model.Users;
import com.chenghui.agriculture.service.appLogin.Memory;
import com.chenghui.agriculture.service.appLogin.ThreadTokenHolder;
import com.chenghui.agriculture.service.projectManage.FarmerService;
import com.chenghui.agriculture.service.projectManage.ProjectService;
import com.chenghui.agriculture.service.projectManage.ProjectsService;
import com.chenghui.agriculture.service.projectManage.SubProjectService;
import com.chenghui.agriculture.service.supervisonManage.ChecksService;
import com.chenghui.agriculture.service.supervisonManage.PointService;
import com.chenghui.agriculture.service.supervisonManage.SupervisionService;
import com.chenghui.agriculture.service.system.ConstantService;


/**
 * @author yudq
 * @version V1.0
 * @Date 2016-03-05 16:23:00
 */
@RequestMapping("/services")
@Controller
public class ServicesController {
	
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	SubProjectService subProjectService;
	
	@Autowired
	SupervisionService supervisionService;
	
	@Autowired
	ChecksService checksService;
	
	@Autowired
	PointService pointService;
	
	@Autowired
	ProjectsService projectsService;
	
	@Autowired
	FarmerService farmerService;
	
	@Autowired
	ConstantService constantService;
	
	@Autowired
	FinanceDao financeDao;
	
	@Autowired
    private Memory memory;
	
	/**
	 * 
	 * @param project
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(@ModelAttribute Users users, Model model, HttpServletRequest request){
		String status = "0", errorMsg = "";
		Map<String, Object> map = new HashMap<>();
		String username = users.getUserName();
		String password = users.getPassword();
		if(StringUtils.isEmpty(username) && StringUtils.isEmpty(password)){
			status = "-1";
			errorMsg = "用户名称和登录密码不能为空";
		}else if(StringUtils.isEmpty(username)){
			status = "-1";
			errorMsg = "用户名称不能为空";
		}else if(StringUtils.isEmpty(password)){
			status = "-1";
			errorMsg = "登录密码不能为空";
		}else {
			try {
				String token = request.getHeader(Constants.TOKEN);
		        if (StringUtils.isEmpty(token)) {
		        	// 从请求信息中获取token值
		        	token = request.getParameter(Constants.TOKEN);
		        }
		         
				BearerAuthenticationToken authenticationToken = null;
				if (StringUtils.isNotEmpty(token)) {
					if (!memory.checkLoginInfo(token)) {
						map.put("token", "");
						status = "-2";
						errorMsg = "token无效，请重新登录";
		             }
		             ThreadTokenHolder.setToken(token);
				}else{
					authenticationToken = new BearerAuthenticationToken(username, password);
					SecurityUtils.getSubject().login(authenticationToken);
					token = authenticationToken.getToken();
				}
				map.put("token", token);
			} catch (Exception e) {
				map.put("token", "");
				e.printStackTrace();
				status = "-1";
				errorMsg = e.getMessage();
			}
		}
		
		model.addAttribute("status", status);
		model.addAttribute("data", map);
		model.addAttribute("errorMsg", errorMsg);
		return "jsonTemplate";
	}
	
	/**
	 * 
	 * @param project
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getProjects0", method = RequestMethod.GET)
	public String getProjectByParams(@ModelAttribute Project project, Model model){
		String status = "0", errorMsg = "";
		PaginationSupport<Project> pages = null;
		try {
			if (project.getStartIndex() <0 || project.getPageSize() <=0) {
				project.setStartIndex(0);
				project.setPageSize(10);
			}
			pages = projectService.findByParams(project);
		} catch (Exception e) {
			e.printStackTrace();
			status = "-1";
			errorMsg = e.getMessage();
		}
		
		model.addAttribute("status", status);
		model.addAttribute("data", pages);
		model.addAttribute("errorMsg", errorMsg);
		return "jsonTemplate";
	}
	
	/**
	 * 
	 * @param project
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getProjects", method = RequestMethod.GET)
	public String getProjectByParams1(@ModelAttribute Projects projects, Model model){
		String status = "0", errorMsg = "";
		PaginationSupport<Projects> pages = null;
		try {
			if (projects.getStartIndex() <0 || projects.getLimit() <=0) {
				projects.setStartIndex(0);
				projects.setLimit(10);
			}
			pages = projectsService.appFindByParams(projects, getCurrentUser());
		} catch (Exception e) {
			e.printStackTrace();
			status = "-1";
			errorMsg = e.getMessage();
		}
		
		model.addAttribute("status", status);
		model.addAttribute("data", pages);
		model.addAttribute("errorMsg", errorMsg);
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "getSubProjects", method = RequestMethod.GET)
	public String getSubProjectByParentId(@RequestParam Long pId, Model model){
		if (pId == null || pId <= 0) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", new ArrayList<>());
			model.addAttribute("errorMsg", "父项目不能为空");
		}else{
			List<SubProject> subProjects = new ArrayList<>();
			List<Projects> newProjects = projectsService.appFindLeafProjectsByParentId(pId);
			if (newProjects.isEmpty()) {
				Projects projects = projectsService.get(pId);
				newProjects.add(projects);
			}
			for(Projects projects: newProjects){
				SubProject subProject = new SubProject();
				subProject.setId(projects.getId());
				subProject.setSubProjectNumber(projects.getProjectNumber()+"");
				subProject.setSubProjectName(projects.getProjectName());
				subProject.setTotalCapital(projects.getTotalFund());
				subProject.setImplementationUnit(projects.getCarryOutUnit());
				subProject.setSubProjectArea(projects.getArea() == null ? "" :projects.getArea().getAreaName());
				subProject.setFarmerName(projects.getFarmerName());
				subProject.setProjectScaleAndContent(projects.getScaleAndContent());
				subProject.setConstructionMode(projects.getConstructionMode());
				subProject.setCheckStatus(projects.getCheckStatus() == null ? 1 : projects.getCheckStatus());
				subProject.setFinancebiLv(projects.getFinancebiLv());
				subProject.setJz(1d);
				subProject.setJy(2d);
				subProject.setProjectScale(projects.getScaleAndContent());
				subProject.setProjectContent(projects.getScaleAndContent());
				
				subProject.setFundYear(projects.getFundYear());
				subProject.setSubjectName(projects.getSubjectName());
				subProject.setDeadline(projects.getDeadline());
				subProject.setChargePerson(projects.getChargePerson());
				subProject.setApproveState(projects.getApproveState()==null?"":(projects.getApproveState()==0?"正在备案":(projects.getApproveState()==1?"备案通过":(projects.getApproveState()==2?"备案不通过":""))));
				subProject.setCoveredFarmerNumber(projects.getCoveredFarmerNumber());
				subProject.setCoveringNumber(projects.getCoveringNumber());
				subProject.setPovertyStrickenFarmerNumber(projects.getPovertyStrickenFarmerNumber());
				subProject.setPovertyStrickenPeopleNumber(projects.getPovertyStrickenPeopleNumber());
				subProject.setPovertyGeneralFarmer(projects.getPovertyGeneralFarmer());
				subProject.setPovertyGeneralPeople(projects.getPovertyGeneralPeople());
				subProject.setPovertyLowIncomeFarmer(projects.getPovertyLowIncomeFarmer());
				subProject.setPovertyLowIncomePeople(projects.getPovertyLowIncomePeople());
				subProject.setTotalFund(projects.getTotalFund());
				subProject.setFinanceFund(projects.getFinanceFund());
				subProject.setSelfFinancing(projects.getSelfFinancing());
				subProject.setIntegrateFund(projects.getIntegrateFund());
				subProject.setFundToCountry(projects.getFundToCountry());
				subProject.setBzTotal(projects.getBzTotal());
				subProject.setBkTotal(projects.getBkTotal());
				subProject.setBalance(projects.getBalance());
				subProject.setBalance_forword(projects.getBalance_forword());
				subProject.setProjectProcess(projects.getProjectProcess());
				subProject.setProjectType(projects.getProjectType());
				
				String areaDisplay = "";
	    		if (projects.getCountyLevelCityID() != null) {
	    			areaDisplay = projects.getCountyLevelCityID().getAreaName();
	    		}
	    		if (projects.getTownID() != null) {
	    			areaDisplay = projects.getTownID().getAreaName();
	    		}
	    		if (projects.getVillageID() != null) {
	    			areaDisplay += ' ' + projects.getVillageID().getAreaName();
	    		}
				
				
				Project project = new Project();
				project.setProjectName(projects.getProjectName());
				project.setScaleAndContent(projects.getScaleAndContent());
				project.setVillage(projects.getVillage()== null ? (projects.getVillageID()==null? "" :projects.getVillageID().getAreaName()):projects.getVillage());
				project.setTown(areaDisplay);
				project.setReferenceNumber(projects.getReferenceNumber());
				project.setProjectType(projects.getProjectType());
				subProject.setProject(project);
				subProjects.add(subProject);
			}
			
			model.addAttribute("status", "0");
			model.addAttribute("data", subProjects);
			model.addAttribute("errorMsg", "");
		}
		
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "getSubProjects0", method = RequestMethod.GET)
	public String getSubProjectByParentId0(@RequestParam Long pId, Model model){
		if (pId == null || pId <= 0) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", new ArrayList<>());
			model.addAttribute("errorMsg", "父项目不能为空");
		}else{
			List<SubProject> subProjects = subProjectService.findSubProjectsByPId(pId);
			model.addAttribute("status", "0");
			model.addAttribute("data", subProjects);
			model.addAttribute("errorMsg", "");
		}
		
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "getSubProjects1", method = RequestMethod.GET)
	public String getSubProjectByParentId1(@RequestParam Long pId, Model model){
		if (pId == null || pId <= 0) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", new ArrayList<>());
			model.addAttribute("errorMsg", "父项目不能为空");
		}else{
			List<Projects> subProjects = projectsService.appFindLeafProjectsByParentId(pId);
			if (subProjects.isEmpty()) {
				Projects projects = projectsService.get(pId);
				subProjects.add(projects);
			}
			model.addAttribute("status", "0");
			model.addAttribute("data", subProjects);
			model.addAttribute("errorMsg", "");
		}
		
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "getSupervisions", method = RequestMethod.GET)
	public String getSupervisionsBySubProjectId(@RequestParam Long pId, Model model){
		if (pId == null || pId <= 0) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", new ArrayList<>());
			model.addAttribute("errorMsg", "项目id不能为空");
		}else{
			List<Supervision> subProjects = supervisionService.appFindSupervisionByPId(pId);
			model.addAttribute("status", "0");
			model.addAttribute("data", subProjects);
			model.addAttribute("errorMsg", "");
		}
		
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "addSupervision", method = RequestMethod.POST)
	public String addSupervision(@RequestParam("picture") MultipartFile[] pictures, @RequestParam("video") MultipartFile[] videos, @ModelAttribute Supervision supervision, HttpServletRequest request, Model model){
		try {
			//处理文件
			String fileName = null;
			StringBuffer pictureNames = new StringBuffer();
			StringBuffer videoNames = new StringBuffer();
	    	if (pictures != null && pictures.length >0) {
	    		for(int i =0 ;i< pictures.length && StringUtils.isNotEmpty(pictures[i].getOriginalFilename()); i++){
		            try {
		            	String uploadDir = request.getSession().getServletContext()
		                        .getRealPath("/") + FileOperateUtil.UPLOAD_PICTURE_DIR;
		                File file = new File(uploadDir);
		                if (!file.exists()) {
		                    file.mkdirs();
		                }
		                
		                fileName = uploadDir + pictures[i].getOriginalFilename();
		                pictureNames.append(FileOperateUtil.UPLOAD_PICTURE_DIR + pictures[i].getOriginalFilename() + ";");
		                byte[] bytes = pictures[i].getBytes();
		                BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
		                buffStream.write(bytes);
		                buffStream.close();
		            } catch (Exception e) {
		            	e.printStackTrace();
		            }
	    		}
	        }
	    	if (StringUtils.isNotEmpty(supervision.getPictures())) {
	    		supervision.setPictures(supervision.getPictures() + pictureNames.toString());
			}else {
				supervision.setPictures(pictureNames.toString());
			}
	    	
	    	if (videos != null && videos.length >0) {
	    		for(int i =0 ;i< videos.length && StringUtils.isNotEmpty(videos[i].getOriginalFilename()); i++){
		            try {
		            	String uploadDir = request.getSession().getServletContext()
		                        .getRealPath("/") + FileOperateUtil.UPLOAD_VIDEO_DIR;
		                File file = new File(uploadDir);
		                if (!file.exists()) {
		                    file.mkdirs();
		                }
		                
		                fileName = uploadDir + videos[i].getOriginalFilename();
		                videoNames.append(FileOperateUtil.UPLOAD_VIDEO_DIR + videos[i].getOriginalFilename() + ";");
		                byte[] bytes = videos[i].getBytes();
		                BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
		                buffStream.write(bytes);
		                buffStream.close();
		            } catch (Exception e) {
		            	e.printStackTrace();
		            }
	    		}
	        }
	    	if (StringUtils.isNotEmpty(supervision.getVideos())) {
	    		supervision.setVideos(supervision.getVideos() + videoNames.toString());
			}else {
				supervision.setVideos(videoNames.toString());
			}
	    	//判断项目进度不能为空
	    	if (StringUtils.isEmpty(supervision.getProjectProcess())) {
				throw new Exception("项目进度不能为空，eg：40%");
			}else{
				supervision.setProjectProcess(supervision.getProjectProcess().trim());
			}
	    	
			//处理其它属性
			long id = supervisionService.addSupervision(supervision);
			Map<String, Long> idMap = new HashMap<>();
			idMap.put("id", id);
 			model.addAttribute("status", "0");
			model.addAttribute("data", idMap);
			model.addAttribute("errorMsg", "");
		} catch (Exception e) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", "{}");
			model.addAttribute("errorMsg", e.getMessage());
		}
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "getChecks", method = RequestMethod.GET)
	public String getChecksBySubProjectId(@RequestParam Long subProjectId, Model model){
		if (subProjectId == null || subProjectId <= 0) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", new ArrayList<>());
			model.addAttribute("errorMsg", "子项目id不能为空");
		}else{
			List<Checks> checks = checksService.appFindChecksBySId(subProjectId);
			model.addAttribute("status", "0");
			model.addAttribute("data", checks);
			model.addAttribute("errorMsg", "");
		}
		
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "addCheck", method = RequestMethod.POST)
	public String addCheck(@RequestParam("picture") MultipartFile[] pictures,@ModelAttribute Checks checks, HttpServletRequest request, Model model){
		try {
			//处理文件
			String fileName = null;
			StringBuffer pictureNames = new StringBuffer();
	    	if (pictures != null && pictures.length >0) {
	    		for(int i =0 ;i< pictures.length && StringUtils.isNotEmpty(pictures[i].getOriginalFilename()); i++){
		            try {
		            	String uploadDir = request.getSession().getServletContext()
		                        .getRealPath("/") + FileOperateUtil.UPLOAD_CHECK_PICTURE_DIR;
		                File file = new File(uploadDir);
		                if (!file.exists()) {
		                    file.mkdirs();
		                }
		                
		                fileName = uploadDir + pictures[i].getOriginalFilename();
		                pictureNames.append(FileOperateUtil.UPLOAD_CHECK_PICTURE_DIR + pictures[i].getOriginalFilename() + ";");
		                byte[] bytes = pictures[i].getBytes();
		                BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(fileName)));
		                buffStream.write(bytes);
		                buffStream.close();
		            } catch (Exception e) {
		            	e.printStackTrace();
		            }
	    		}
	        }
	    	if (StringUtils.isNotEmpty(checks.getPictures())) {
	    		checks.setPictures(checks.getPictures() + pictureNames.toString());
			}else {
				checks.setPictures(pictureNames.toString());
			}
			
			
			long id = checksService.addChecks(checks);
			Map<String, Long> idMap = new HashMap<>();
			idMap.put("id", id);
			model.addAttribute("status", "0");
			model.addAttribute("data", idMap);
			model.addAttribute("errorMsg", "");
		} catch (Exception e) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", "{}");
			model.addAttribute("errorMsg", e.getMessage());
		}
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "addCheck2", method = RequestMethod.POST)
	public String addCheck(@ModelAttribute Checks checks, Model model){
		try {
			long id = checksService.addChecks(checks);
			Map<String, Long> idMap = new HashMap<>();
			idMap.put("id", id);
			model.addAttribute("status", "0");
			model.addAttribute("data", idMap);
			model.addAttribute("errorMsg", "");
		} catch (Exception e) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", "{}");
			model.addAttribute("errorMsg", e.getMessage());
		}
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "addPoint", method = RequestMethod.POST)
	public String addPoint(@ModelAttribute Point point, Model model){
		try {
			long id = pointService.addPoint(point);
			Map<String, Long> idMap = new HashMap<>();
			idMap.put("id", id);
			model.addAttribute("status", "0");
			model.addAttribute("data", idMap);
			model.addAttribute("errorMsg", "");
		} catch (Exception e) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", "{}");
			model.addAttribute("errorMsg", e.getMessage());
		}
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "getFarmers", method = RequestMethod.GET)
	public String getFarmers(@RequestParam Long pId, Model model){
		try {
			List<Farmer> farmers = new ArrayList<>();
			farmers = farmerService.appFindFarmerByPId(pId);
			
			model.addAttribute("status", "0");
			model.addAttribute("data", farmers);
			model.addAttribute("errorMsg", "");
		} catch (Exception e) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", "{}");
			model.addAttribute("errorMsg", e.getMessage());
		}
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "getProblems", method = RequestMethod.GET)
	public String getProblems(Model model){
		try {
			List<Constant> problems = new ArrayList<>();
			problems = constantService.appFindByIntegerColumn("type", 1);
			
			model.addAttribute("status", "0");
			model.addAttribute("data", problems);
			model.addAttribute("errorMsg", "");
		} catch (Exception e) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", "{}");
			model.addAttribute("errorMsg", e.getMessage());
		}
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "getSuggestions", method = RequestMethod.GET)
	public String getSuggestions(Model model){
		try {
			List<Constant> suggestions = new ArrayList<>();
			suggestions = constantService.appFindByIntegerColumn("type", 2);
			
			model.addAttribute("status", "0");
			model.addAttribute("data", suggestions);
			model.addAttribute("errorMsg", "");
		} catch (Exception e) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", "{}");
			model.addAttribute("errorMsg", e.getMessage());
		}
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "getFinance", method = RequestMethod.GET)
	public String getFinance(@RequestParam Long pId, Model model){
		try {
			List<Finance> finances = new ArrayList<>();
			finances = financeDao.appFindBySubProjectId(pId);
			
			model.addAttribute("status", "0");
			model.addAttribute("data", finances);
			model.addAttribute("errorMsg", "");
		} catch (Exception e) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", "{}");
			model.addAttribute("errorMsg", e.getMessage());
		}
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "addTrackPoint", method = RequestMethod.POST)
	public String addTrackPoint(@ModelAttribute PointBaidu pointBaidu, HttpServletRequest request, Model model){
		try {
			String errorMsg = "";
			if (pointBaidu != null && pointBaidu.getChecksId()<=0 ) {
				errorMsg = "参数checksId(验收记录Id)不能为空;";
			}
			if (pointBaidu.getLng()==0 || pointBaidu.getLng()<0 ) {
				errorMsg += "参数png(经度)不能为空;";
			}
			if (pointBaidu.getLat()==0 || pointBaidu.getLat()<0 ) {
				errorMsg += "参数lat(纬度)不能为空;";
			}
			if (pointBaidu.getLoc_time()==0 || pointBaidu.getLoc_time()<0 ) {
				errorMsg += "参数loc_time(上传时间)不能为空;";
			}
			if (StringUtils.isNotEmpty(errorMsg)) {
				throw new Exception(errorMsg);
			}
			long id = projectsService.addPointBaidu(pointBaidu);
			Map<String, Long> idMap = new HashMap<>();
			idMap.put("id", id);
			model.addAttribute("status", "0");
			model.addAttribute("data", idMap);
			model.addAttribute("errorMsg", "");
		} catch (Exception e) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", "{}");
			model.addAttribute("errorMsg", e.getMessage());
		}
		return "jsonTemplate";
	}
	
	@RequestMapping(value = "addTrackPoints", method = RequestMethod.POST,consumes="application/json")
	public String addTrackPoints(@RequestBody List<PointBaidu> pointBaidus,@RequestParam Long checksId, HttpServletRequest request, Model model){
		try {
			String errorMsg = "";
			if (checksId == 0 || checksId < 0 ) {
				errorMsg = "参数checksId(验收记录Id)不能为空;";
			}
			if (StringUtils.isNotEmpty(errorMsg)) {
				throw new Exception(errorMsg);
			}
			projectsService.addPointBaiduList(pointBaidus, checksId);
			Map<String, Long> idMap = new HashMap<>();
			model.addAttribute("status", "0");
			model.addAttribute("data", idMap);
			model.addAttribute("errorMsg", "");
		} catch (Exception e) {
			model.addAttribute("status", "-1");
			model.addAttribute("data", "{}");
			model.addAttribute("errorMsg", e.getMessage());
		}
		return "jsonTemplate";
	}
	
	private ShiroUser getCurrentUser(){
		ShiroUser shiroUser = null;
		try{
			if (SecurityUtils.getSubject().getPrincipals() != null) {
				shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
			}else if (StringUtils.isNotEmpty(ThreadTokenHolder.getToken())) {
				shiroUser = (ShiroUser)(memory.getObjectValue(ThreadTokenHolder.getToken()));
			}
		}catch(Exception ex){
			ex.printStackTrace();
			shiroUser = null;
			
		}
		return shiroUser;
	}

}
