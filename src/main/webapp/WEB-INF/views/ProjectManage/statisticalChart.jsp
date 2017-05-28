<!DOCTYPE html>
	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	﻿<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%> 
<html>
<head lang="zh-cn">
	<meta charset="UTF-8"/>
	<link rel="stylesheet" href="styles/kendo.common.min.css" />
    <link rel="stylesheet" href="styles/kendo.default.min.css" />

</head>
<body>
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a>项目管理
		<i class="glyphicon glyphicon-menu-right"></i>项目统计分析
	</div>
	<div class="container-fluid">
		<!-- 统计选择区start -->
 		<div class="title col-md-12" align="center">
			<span class="button-dropdown button-dropdown-action" data-buttons="dropdown">
		        <a href="#" class="button button-action"> 资金统计 <i class="fa fa-caret-down"></i></a>
			    <ul class="button-dropdown-list is-below">
			      <li id="cityIndustry" style="cursor:pointer"><a>市级产业统计</a></li>
			      <li id="townFinance" style="cursor:pointer"><a>镇级资金统计</a></li>
			      <li id="villageIndustry" style="cursor:pointer"><a>村级项目统计</a></li>
			    </ul>
			</span>
			 
			<span class="button-dropdown button-dropdown-action" data-buttons="dropdown">
		      <a href="#" class="button button-action"> 项目统计 <i class="fa fa-caret-down"></i></a>
		      <ul class="button-dropdown-list is-below">
		        <li id="projectsCount" style="cursor:pointer"><a>区域项目数量统计</a></li>
		        <li id="projectsIndustry" style="cursor:pointer"><a>项目名称统计</a></li>				    
		      </ul>
 			</span>
		</div>
		<!-- 统计选择区end -->
		
		<!-- 资金统计分析start -->
		<div class="demo-section k-content" id="financePage">
			<!-- 市级产业统计start -->
			<div id="city"  style ="margin-top:80px;display:block;margin-top:100px">
			 	<h4 align="center">清镇市产业统计分布占比</h4>
			 	<div style ="width:100%" class="col-md-2_veecohy control-label">			
			    	<label class="col-md-2_veecohy control-label" for="industry_year" style="line-height:35px;float:left;">统计年份</label>
			    	<select class="form-control" id="industry_year"  name="industry_year"  required="required" data-required-msg="必填" style="width:150px;float:left;">
								
					</select>
					<button id="searchBtn_industry" class="btn btn-danger" style="margin-left:20px" onfocus="this.blur()"><i class="glyphicon glyphicon-search"></i> 查询统计</button>
				</div>	
				<div id="industry" value="Pie2D" align="center" style ="width:100%;"></div>  					
		    </div>
		   <!-- 市级产业统计end -->
		   <!-- 镇级资金统计 start -->
		    <div id="townFinanceCount" style ="margin-top:80px;display:none" >
		    	<h4 align="center">乡镇项目资金年度统计分析</h4>
		    	<div style ="width:100%"  class="col-md-2_veecohy control-label" >
			    	<label class="col-md-2_veecohy control-label" for="town_year" style="line-height:35px;float:left;">统计年份</label>
			    	<select class="form-control" id="town_year" name="town_year" required="required" data-required-msg="必填" style="width:150px;float:left;">							
					</select>
					<button id="searchBtn_town" class="btn btn-danger" style="margin-left:20px" onfocus="this.blur()"><i class="glyphicon glyphicon-search"></i> 查询统计</button>
				</div>
				<div id="townFunds" value = "Column3D" style ="width:100%;" align="center"></div>
				<div id="villageFinanceCount" value="ScrollCombiDY2D"  style ="width:100%;" align="center"></div>
				
				<div class="alert alert-warning alert-dismissible" role="alert">
			 		 <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			  		注：默认显示为当前年份的统计信息&nbsp;<span style="background-color:rgb(255,128,128)"><strong>点击柱状图</strong></span>可查看相关的资金分布情况
				</div>
		    </div>
			<!-- 镇级资金统计 end -->
			
			<!-- 村级项目统计:start -->	
	        <div id="villageContent" style ="margin-top:80px;display:none" >
		        <h4 align="center">指定村每年的统计分析</h4>		      
		        <div style ="width:100%"  class="col-md-2_veecohy control-label">
					<label class="col-md-2_veecohy control-label" for="town" style="line-height:35px;float:left;">乡镇、社区</label>
					<div class="col-md-5-veecohy">
						<select class="contryInfo form-control" id="town" name="town" required="required" data-required-msg="必填" style="width:150px;float:left;"></select>
					</div>
					<label class="col-md-2_veecohy control-label" for="villageId" style="line-height:35px;float:left;margin-left:10px">村,居</label>
					<div class="col-md-5-veecohy">
						<select class="contryInfo form-control" id="villageId" name="villageId" required="required" data-required-msg="必填" style="width:150px;float:left;"></select>
					</div>
					<label class="col-md-2_veecohy control-label" for="project_type" style="line-height:35px;float:left;margin-left:10px">统计类型</label>
					<div class="col-md-5-veecohy">
						<select class="contryInfo form-control" id="project_type" name="project_type" required="required" data-required-msg="必填" style="width:150px;float:left;">
							<option>已完成项目</option>
							<option selected="selected">已下达项目</option>							
						</select>
					</div>
					<label class="col-md-2_veecohy control-label" for="village_year" style="line-height:35px;float:left;margin-left:10px">统计年度</label>
					<div class="col-md-5-veecohy">
						<select class="contryInfo form-control" id="village_year" name="village_year" required="required" data-required-msg="必填" style="width:150px;float:left;">
						</select>
					</div>
					<button id="searchBtn_village"  style="margin-left:20px" class="btn btn-danger" onfocus="this.blur()"><i class="glyphicon glyphicon-search"></i> 查询统计</button>
				</div>
				<div id="villageProjectsCount" style ="width:100%; margin-top:70px;" align="center"></div>
		
			</div>	
			<!-- 村级项目统计 :end -->
		</div>
		<!-- 资金统计分析end -->
			
		<!-- 项目统计start -->
		<div class="demo-section k-content" id="projects">
			<!-- 区域项目数量统计start -->
			<div id="projectsCountIndustry" style ="margin-top:80px;display:none" >
				<h4 align="center">乡镇项目数量统计分析</h4>
				<label class="col-md-2_veecohy control-label" for="projectCountYear" style="line-height:35px;margin-left:10px;float:left;">统计年度</label>
				<select class="form-control" id="projectCountYear" name="projectCountYear" required="required" data-required-msg="必填" style="width:150px;float:left;">					
				</select>
				<button id="searchBtn_count" style="margin-left:20px" class="btn btn-danger" onfocus="this.blur()"><i class="glyphicon glyphicon-search"></i> 查询统计</button>
				<div id="projectsCountReport" style ="width:100%;margin-top:10px" align="center"></div>
				<div id="projectsCountReportByVillage" style ="width:100%;margin-top:10px" align="center"></div>
				<div class="alert alert-warning alert-dismissible" role="alert">
			 		 <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			  		注：默认显示为当前年份的统计信息 <span style="background-color:rgb(255,128,128)"><strong>点击柱状图</strong></span>可查看相关的项目分布情况
				</div>
			</div>
			<!-- 区域项目数量统计end -->
			
			<!-- 根据项目名称统计start -->
			<div id="projectsNameIndustry" style ="margin-top:80px;display:none" >
				<h4 align="center">指定项目统计分析</h4>
				<label class="col-md-2_veecohy control-label" for="projectByTown" style="line-height:35px;margin-left:10px;float:left;">乡镇，社区</label>
				<select class="form-control" id="projectByTown" name="projectByTown" required="required" data-required-msg="必填" style="width:150px;float:left;">						
				</select>
				<label class="col-md-2_veecohy control-label" for="projectsName" style="line-height:35px;float:left;">项目名称</label>
		    	<select class="form-control" id="projectsName" name="projectsName" required="required" data-required-msg="必填" style="width:200px;float:left;">
		    			
		    	</select>
		    	<button id="searchBtn_projectsName" style="margin-left:20px" class="btn btn-danger" onfocus="this.blur()"><i class="glyphicon glyphicon-search"></i> 查询统计</button>		    	
		    	<div id="projectsByNameCount" style ="width:100%;margin-top:10px" align="center"></div>
			</div>
			<!-- 根据项目名称统计end -->
		</div>
		<!-- 项目统计end -->
	</div>
	<myScript>
		<script src="resources/plugins/jquery/jquery-form.js"></script>
		<script src="resources/plugins/fusioncharts-suite-xt/js/fusioncharts.js"></script>
		<script type="text/javascript" src="resources/js/ProjectManage/statisticalChart.js"></script>
	</myScript>

</body>
</html>