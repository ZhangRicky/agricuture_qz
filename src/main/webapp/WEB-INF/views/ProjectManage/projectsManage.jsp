<!DOCTYPE html>
	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	﻿<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%> 
<html>
<head lang="zh-cn">
	<meta charset="UTF-8"/>
	<link rel="stylesheet" href="styles/kendo.common.min.css" />
    <link rel="stylesheet" href="styles/kendo.default.min.css" />
   <!--  <link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
    <link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" /> -->
	<myStyle>
		#info{width:250px;
	    		height:55px;
	    		margin:0 auto;
	    		background-color:#FB6;
	    		position:fixed;
				bottom:20px;
				right:15px;
				border-radius: 3px;
				z-index : 99;
	    		}
	    #info .text{font-size:15px;font-family:"微软雅黑";color:#fff;
	    		height:20px;
	    		padding:18px;
	    		margin:0 auto;
	    		text-align:center;
	    		}
	    		
	    #text_div{width:250px;
	    		height:55px;
	    		margin:0 auto;
	    		background-color:#FB6;
	    		position:fixed;
				bottom:20px;
				right:15px;
				border-radius: 3px;
				z-index : 99;
	    		}
	    #text_div .text_info{font-size:15px;font-family:"微软雅黑";color:#fff;
	    		height:20px;
	    		padding:18px;
	    		margin:0 auto;
	    		text-align:center;
	    		}
	</myStyle>
</head>
<myCss>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" />
	<style type="text/css">
	body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}
	#allmap {width: 100%; height:500px; overflow: hidden;}
	#result {width:100%;font-size:12px;}
	dl,dt,dd,ul,li{
		margin:0;
		padding:0;
		list-style:none;
	}
	p{font-size:12px;}
	dt{
		font-size:14px;
		font-family:"微软雅黑";
		font-weight:bold;
		border-bottom:1px dotted #000;
		padding:5px 0 5px 5px;
		margin:5px 0;
	}
	dd{
		padding:5px 0 0 5px;
	}
	li{
		line-height:28px;
	}
	</style>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=您的密钥"></script>
	<!--加载鼠标绘制工具-->
	<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
	<!--加载检索信息窗口-->
	<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
	<style type="text/css">
		#showChecksMap,#showSupervisionsMap,#showDetermineRangeMap{position:"absolute";height:645px;width:98%;overflow: hidden;margin:0;font-family:"微软雅黑";}
		.checks-photo {
             display: inline-block;
             width: 32px;
             height: 32px;
             border-radius: 50%;
             background-size: 32px 35px;
             background-position: center center;
             vertical-align: middle;
             line-height: 32px;
             box-shadow: inset 0 0 1px #999, inset 0 0 10px rgba(0,0,0,.2);
             margin-left: 5px;
         }
          
             
            #left{
				width: 50%;
				height: 300px;
				float: right;
			}
			#right{
				width: 50%;
				height: 300px;
				float: left;
						
				}
        /*  #areaList{
            overflow: visible;
			width: 15%;
			height: 700px;
			border: 1px solid threedshadow;
			margin:0px 0px 20px 10px;
			float: left;
		}
		#projectsManageList{
			overflow: visible;
			width: 83%;
			height: 464px;
			margin-top:5px;
			float: right;
		} */
	</style>
</myCss>
<body>
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a>项目管理
		<i class="glyphicon glyphicon-menu-right"></i>项目管理
	</div>
	<script id="pictureTipTemplate" type="text/x-kendo-template">
		<img class="demo-trigger" src="#=target.data('src')#" style="width:480px"/>
	</script>
	<div class="container-fluid">
		<div class="row">
           	<div class="demo-section k-content">
           		<div class="col-md-2">
               		<div id='areaList'></div>
               	</div>
               	<div class="col-md-10">
					<!-- <div class="form-group form-inline">
						<input id="inputSearchProjects"  type="text" class="form-control" placeholder="输入子项目名称、施工单位、农户名称" style="width: 400px;"/>
						<button id="searchBtnProjects"  class="btn btn-danger"><i class="glyphicon glyphicon-search"></i>查询</button>
					</div> -->
					<div class="form-group form-inline">
						<label>项目名称&nbsp;&nbsp;</label>
						<input id="projectName_select" type="text" class="form-control" style="width: 150px;"  placeholder="输入项目名称查询"/>&nbsp;&nbsp;
						<label>资金年度&nbsp;&nbsp;</label>
						<select class="form-control" id='fundYear_select' style="width:120px">
							<option value="">----全部----</option>
							<option value="2016" selected>2016</option>
							<option value="2015">2015</option>
							<option value="2014">2014</option>
							<option value="2013">2013</option>
							<option value="2012">2012</option>
							<option value="2011">2011</option>
							<option value="2010">2010</option>
						</select>&nbsp;&nbsp;
						<label>项目类型&nbsp;&nbsp;</label>
						<select class="form-control" id='projectType_select' style="width:120px">
							<option value="">----全部----</option>
							<option value="1">种植</option>
							<option value="2">基础论证</option>
							<option value="3">培训</option>
							<option value="4">贴息</option>
							<option value="5">经济组织</option>
							<option value="6">其它</option>
						</select>&nbsp;&nbsp;
						<button id="searchBtn"  class="btn btn-danger"><i class="glyphicon glyphicon-search"></i>查询</button>&nbsp;&nbsp;
						<button id="resetBtn" class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i>重置</button>
					</div>
					<div id='projectsManageList'></div>
					<div class="alert alert-warning alert-dismissible" role="alert">
				 		 <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				  		注：灰色背景表示导入项目，白色背景为子项目
					</div>
					<script type="text/x-kendo-template" id="toolBarTemplate">
	                	<div class="toolbar" >
	                		<a id='exportList' class='btn btn-xs btn-primary'><i class='glyphicon glyphicon-download'></i> 项目数据导出</a>
	                	</div>
	            	</script>
					<script type="text/x-kendo-template" id="projectsOperation1">
						&nbsp;&nbsp;&nbsp;&nbsp;
						<shiro:hasPermission name='editProjects'><a class='editProjects addIcon aLink' title='修改项目'><i class='fa fa-pencil''></i><a/></shiro:hasPermission>
						<shiro:hasPermission name='deleteProjects'><a class='deleteProjects deleteIcon aLink' title='删除'><i class='glyphicon glyphicon-trash'></i></a></shiro:hasPermission>
						<shiro:hasPermission name='projectsDetail'><a class='projectsDetail editIcon aLink' title='查看明细'><i class='fa fa-eye'></i><a/></shiro:hasPermission>
						<shiro:hasPermission name='projectsFinance'><a class='projectsFinance editIcon aLink' title='财务记录'><i class='glyphicon glyphicon-euro'></i><a/></shiro:hasPermission>
					</script>
					<script type="text/x-kendo-template" id="projectsOperation2">
						<shiro:hasPermission name='addProjects'><a class='addProjects addIcon aLink' title='添加项目'><i class='glyphicon glyphicon-plus'></i><a/></shiro:hasPermission>
						<shiro:hasPermission name='editProjects'><a class='editProjects addIcon aLink' title='修改项目'><i class='fa fa-pencil''></i><a/></shiro:hasPermission>
						<shiro:hasPermission name='deleteProjects'><a class='deleteProjects deleteIcon aLink' title='删除'><i class='glyphicon glyphicon-trash'></i></a></shiro:hasPermission>
						<shiro:hasPermission name='projectsDetail'><a class='projectsDetail editIcon aLink' title='查看明细'><i class='fa fa-eye'></i><a/></shiro:hasPermission>
						<!--<shiro:hasPermission name='projectsFinance'><a class='projectsFinance editIcon aLink' title='财务记录'><i class='glyphicon glyphicon-euro'></i><a/></shiro:hasPermission>-->
					</script>
					<script type="text/x-kendo-template" id="projectsOperation3">
						<shiro:hasPermission name='addProjects'><a class='addProjects addIcon aLink' title='添加项目'><i class='glyphicon glyphicon-plus'></i><a/></shiro:hasPermission>
						<shiro:hasPermission name='editProjects'><a class='editProjects addIcon aLink' title='修改项目'><i class='fa fa-pencil''></i><a/></shiro:hasPermission>
						<shiro:hasPermission name='deleteProjects'><a class='deleteProjects deleteIcon aLink' title='删除'><i class='glyphicon glyphicon-trash'></i></a></shiro:hasPermission>
						<shiro:hasPermission name='projectsDetail'><a class='projectsDetail editIcon aLink' title='查看明细'><i class='fa fa-eye'></i><a/></shiro:hasPermission>
						<shiro:hasPermission name='projectsFinance'><a class='projectsFinance editIcon aLink' title='财务记录'><i class='glyphicon glyphicon-euro'></i><a/></shiro:hasPermission>
					</script>
					<script type="text/x-kendo-template" id="subProjectAdjustment">
						<shiro:hasPermission name='subProjectAdjustment'><a class='subProjectAdjustment editIcon aLink' title='项目调项'><i class='glyphicon glyphicon-sort'>项目调项</i><a/></shiro:hasPermission>&nbsp;&nbsp;&nbsp;
						<shiro:hasPermission name='subProjectAdjustmentRecord'><a class='subProjectAdjustmentRecord editIcon aLink' title='项目调项记录'><i class='fa fa-eye'>调项记录</i><a/></shiro:hasPermission>
						</script>
					<script type="text/x-kendo-template" id="determineProjectsRange">
						<shiro:hasPermission name='determineRange'><a class='determineRange editIcon aLink' title='确定项目范围'><i class='fa fa-map-marker'>确定项目范围</i><a/></shiro:hasPermission>
						</script>
					<script type="text/x-kendo-template" id="showSupervisionsTemplate">
						<shiro:hasPermission name='getSupervisions'><a class='getSupervisions editIcon aLink' title='查看项目监管'><i class='fa fa-map-marker'>监管记录</i><a/></shiro:hasPermission>
						</script>
	            	<script type="text/x-kendo-template" id="showChecksTemplate">
						<shiro:hasPermission name='getChecks'><a class='getChecks editIcon aLink' title='查看项目验收'><i class='fa fa-check'>验收记录</i><a/></shiro:hasPermission>
						</script>
						<script type="text/x-kendo-template" id="showFileUpload">
						<shiro:hasPermission name="fileUpload"><a class='fileUpload aLink' title='附件'>附件</a></shiro:hasPermission>
						</script>
						<script type="text/x-kendo-template" id="showFileImport">
						<shiro:hasPermission name="fileImport"><a class='fileImport aLink' title='农户信息导入'>农户信息导入</a></shiro:hasPermission>
						</script>
					<script type="text/x-kendo-template" id="deleteFarmer">
						<shiro:hasPermission name='deleteFarmers'><a class='deleteFarmers deleteIcon aLink' title='删除'><i class='glyphicon glyphicon-trash'></i></a></shiro:hasPermission>
						&nbsp;&nbsp;
						<shiro:hasPermission name='editFarmers'><a class='editFarmers addIcon aLink' title='修改农户信息'><i class='fa fa-pencil''></i><a/></shiro:hasPermission>

                   </script>
				</div>
		 	</div>
    	</div>
    </div>
      <!-- 主项目信息详细  begin -->
	<div id='projectInfoDetailWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='projectInfoDetailValidate' class="forge-heading">
			<!-- 主项目详情 -->
			<p style="font-size:16px;color:block;">基本信息</p>
				<div class="form-horizontal" id="zhuxiangmu" style="display: none;">
				
				   <div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="projectName" style="width: 100px;">项目名称:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="projectName"></label>
						</div>
				   </div>	
				   <div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="referenceNumber" style="width: 100px;">资金文号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="referenceNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="projectNumber" style="width: 100px;">项目编号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="projectNumber"></label>
						</div>
					</div>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<div>
						<label class="col-md-2 control-label" for="fundYear" style="width: 100px;">资金年度:</label>
						</div>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="fundYear"></label>
						</div>
					</div>	
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="countyLevelCity" style="width: 100px;">所属地区:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="countyLevelCity"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="subjectName" style="width: 100px;">专项名称:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="subjectName"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="fundType" style="width: 100px;">资金类明细:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="fundType"></label>
						</div>
					</div>	
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="approvalNumber" style="width: 100px;">批复文号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="approvalNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="scaleAndContent" style="width: 100px;">建设规模及内容:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="scaleAndContent"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="constructionMode" style="width: 100px;">建设方式:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="constructionMode"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="projectType" style="width: 100px;">项目类型:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="projectType"></label>
						</div>
					</div>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="carryOutUnit" style="width: 100px;">实施单位:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="carryOutUnit"></label>
						</div>
					</div>
						<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="chargePerson" style="width: 100px;">项目负责人:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="chargePerson"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="deadline" style="width: 100px;">完成期限(月):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="deadline"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="standbyNumber" style="width: 100px;">后补文号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="standbyNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="approveState" style="width: 100px;">审核状态:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="approveState"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="approvingDepartment" style="width: 100px;">当前审核部门:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="approvingDepartment"></label>
						</div>
					</div>
					
					<!--  
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="countyLevelCity" style="width: 100px;">县名</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="countyLevelCity"></label>
						</div>
					</div>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="town" style="width: 100px;">乡镇名:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="town"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="village" style="width: 100px;">村名:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="village"></label>
						</div>
					</div>			
					-->
					<hr>
					<p style="font-size:16px;color:block;">扶贫信息</p>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="coveredFarmerNumber" style="width: 100px;">扶持总农户数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="coveredFarmerNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="povertyGeneralFarmer" style="width: 150px;">一般农户数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="povertyGeneralFarmer"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="povertyStrickenFarmerNumber" style="width: 150px;">贫困农户数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="povertyStrickenFarmerNumber"></label>
						</div>
					</div>
					
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="povertyLowIncomeFarmer" style="width: 180px;">低收入困难农户数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="povertyLowIncomeFarmer"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="coveringNumber" style="width: 80px;">扶持总人数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="coveringNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="povertyGeneralPeople" style="width: 150px;">一般人口数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="povertyGeneralPeople"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="povertyStrickenPeopleNumber" style="width: 150px;">贫困人口数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="povertyStrickenPeopleNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="povertyLowIncomePeople" style="width: 150px;">低收入人口数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="povertyLowIncomePeople"></label>
						</div>
					</div>
					<hr>
					<p style="font-size:16px;color:block;">资金信息(万元)</p>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="totalFund" style="width: 100px;">总资金:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="totalFund"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="integrateFund" style="width: 100px;">整合资金:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="integrateFund"></label>
						</div>
					</div>		
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="financeFund" style="width: 100px;">财政资金:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="financeFund"></label>
						</div>
					</div>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="selfFinancing" style="width: 100px;">自筹资金:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="selfFinancing"></label>
						</div>
					</div>	
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="fundToCountry" style="width: 100px;">到县资金:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="fundToCountry"></label>
						</div>
					</div>
				
				</div>
				<!-- 主项目详情 -->
				<!-- 子项目详情 -->
				
				<div class="form-horizontal" id="zixiangmu" style="display: none;">
				
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="ziprojectName" style="width: 100px;">子项目名称:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="ziprojectName"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zireferenceNumber" style="width: 100px;">子项目文号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zireferenceNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zifundYear" style="width: 100px;">资金年度:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zifundYear"></label>
						</div>
					</div>		
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<div>
						<label class="col-md-2 control-label" for="ziCity" style="width: 100px;">所属地区:</label>
						</div>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="ziCity"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zisubjectName" style="width: 100px;">专项名称:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zisubjectName"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zifarmerName" style="width: 100px;">农户名称:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zifarmerName"></label>
						</div>
					</div>	
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zifundType" style="width: 100px;">资金类明细:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zifundType"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="ziapprovalNumber" style="width: 100px;">批复文号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="ziapprovalNumber"></label>
						</div>
					</div>	
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="ziscaleAndContent" style="width: 100px;">建设规模及内容:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="ziscaleAndContent"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="ziconstructionMode" style="width: 100px;">建设方式:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="ziconstructionMode"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="ziprojectType" style="width: 100px;">项目类型:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="ziprojectType"></label>
						</div>
					</div>			
					<!-- 
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zitown" style="width: 100px;">乡镇名:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zitown"></label>
						</div>
					</div>
				
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zivillage" style="width: 100px;">村名:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zivillage"></label>
						</div>
					</div>
					 -->
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zicarryOutUnit" style="width: 100px;">实施单位:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zicarryOutUnit"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zichargePerson" style="width: 100px;">项目负责人:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zichargePerson"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zideadline" style="width: 100px;">完成期限(月):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zideadline"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="remark" style="width: 100px;">备注:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="remark"></label>
						</div>
					</div>
					<hr>
					<p style="font-size:16px;color:block;">扶贫信息</p>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zicoveredFarmerNumber" style="width: 100px;">扶持总农户数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zicoveredFarmerNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zipovertyGeneralFarmer" style="width: 150px;">一般农户数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zipovertyGeneralFarmer"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zipovertyStrickenFarmerNumber" style="width: 150px;">贫困农户数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zipovertyStrickenFarmerNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zipovertyLowIncomeFarmer" style="width: 180px;">低收入困难农户数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zipovertyLowIncomeFarmer"></label>
						</div>
					</div>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zicoveringNumber" style="width: 80px;">扶持总人数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zicoveringNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zipovertyGeneralPeople" style="width: 150px;">一般人口数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zipovertyGeneralPeople"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zipovertyStrickenPeopleNumber" style="width: 150px;">贫困人口数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zipovertyStrickenPeopleNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zipovertyLowIncomePeople" style="width: 150px;">低收入人口数:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zipovertyLowIncomePeople"></label>
						</div>
					</div>
					<hr>
					<p style="font-size:16px;color:block;">资金信息(万元)</p>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zitotalFund" style="width: 100px;">总资金:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zitotalFund"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zifundToCountry" style="width: 100px;">到县资金:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zifundToCountry"></label>
						</div>
					</div>
						
					<!--  
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="zipath" style="width: 100px;">批复文件:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="zipath"></label>
						</div>
					</div>	
					-->
				</div>
				<!-- 子项目详情 -->
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter" style="margin-top:10px;">
			<a id='backProjectDetail'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>返回
			</a>
		</div>
	</div>
	<!-- 主项目信息详细  end -->	
	<!-- 项目监管start -->
	<div id="showSupervisionsWindow" style="display:none">
		<div class="container-fluid">
			<div class="row"  id="supervisionHorizontal" style="height:650px;">
				<div class="col-md-4">
					<div id='showSupervisionsList'></div>
					<div class="alert alert-warning alert-dismissible" role="alert">
					  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					  注：点击监管记录查看记录详情
					</div>
				</div>
				<div class="col-md-8">
					<div id='showSupervisionsMap'></div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='clearSupervisions'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>清除所有覆盖物
			</a>
			<a id='initSupervisions'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>显示所有覆盖物
			</a>
		</div>
		
		<div id="text_div" style="display:none">
			<div class="text_info"></div>
		</div>
	
	</div>	
	<!-- 项目监管end -->
	<!-- show checks of sub project -->
	<div id="showChecksWindow" style="display:none">
		<div class="container-fluid">
			<div class="row"  id="checkHorizontal" style="height:650px;">
				<div class="col-md-4">
					<div id='showChecksList'></div>
					<div class="alert alert-warning alert-dismissible" role="alert">
					  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					  注：路线长度与面积均存在一定误差，请以实测数据为准。
					</div>
				</div>
				<div class="col-md-8">
					<div id='showChecksMap'></div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='perimeter'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>计算长度
			</a>
			<a id='area'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>计算面积(亩)
			</a>
			</a>
 			<a id='m2'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>计算面积(平方米)
			</a>
		</div>
	</div>
	
	
	<!-- 确定项目范围 start -->
	<div id="determineRangeWindow" style="display:none">
		<div class="container-fluid">
			<div class="row" id="determineRangeHorizontal" style="height:650px;">
				<div class="col-md-12">
					<div id='showDetermineRangeMap'></div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='determineProjects' class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>确定范围
			</a>
			<a id='cancelDetermine'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>清除覆盖物
			</a>
		</div>
	</div>
	<!-- 确定项目范围 end -->
	
	
     <!-- 公示附件start -->
     
     <div id="changeDeleteWindow" style="display: none;">
		<div class="toolbar">
			<div class="demo-section k-content" id="fileId"> 
				<p class="btn btn-xs text-left" style="margin-top: 0px;">
					<span>公示附件上传</span>	
					 <input name="files" id="fileupload" type="file"/> 		
				</p>
				
				<span style="font-size:12px;color:red">提示:单击图片进行删除</span>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<p class="btn btn-xs text-left" style="margin-top: 0px;">
					<span>批复文件上传</span>
					<input name="file" id="pifufileupload" type="file"/> 		
				</p>
				<span style="font-size:12px;color:red">提示:单击图片进行删除</span>
			</div>
		</div>
		
		<div class="container-fluid" >
			<div id="pic">
		 <div id="right">
			<div style="height:200px;width:100%;overflow-y:scroll;overflow-x:hidden;" id="picture1">
			</div>
			<div style="height:100px;width:100%;overflow-y:scroll;overflow-x:hidden;" id="fileName">	
			</div>
		 </div>
		 <div id="left">
			<div style="height:200px;width:100%;overflow-y:scroll;overflow-x:hidden;" id="pifupicture">
			</div>
			<div style="height:100px;width:100%;overflow-y:scroll;overflow-x:hidden;" id="pifufileName">	
			</div>
		 </div>
		</div>

		 <div id='changeDeleteValidate' class="forge-heading" style="margin-top: 330px">
				<div class="form-horizontal" >
					<div class="form-group">
						<label class="col-md-1 control-label" for="explain">公示说明</label>
						<div class="col-md-7">
							<input type="text" hidden="true" id="projectId" value="">
							<textarea maxlength="183" rows="5" cols="60" id="explain" name="explain" class="form-control" required validationMessage="必填"></textarea>
							
						</div>
						<div class="col-md-2">
						 	<span class="k-invalid-msg" data-for="explain"></span>
						</div>
					</div>
				</div>
			</div>
		
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveRemark'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
		</div>
	</div>
</div>

<!-- 公示附件end -->

	<!-- 农户信息导入start -->
	<div id="fileFarmerImport" style="display: none;">
		<div class="col-md-12">
				
				<script type="text/x-kendo-template" id="template">
	               
						<shiro:hasPermission name='uploadFarmerInfo'>
	                	 <input id="fileupload1" type="file" name="fileFarmer" />
						</shiro:hasPermission>
	                
	            	</script>	
	       
							
			   <span id="importingMsg"></span>
			   
			   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<div id='farmerImportList'></div>
		</div>
		
		<div id='fileFarmerImportValidate'></div>
	</div>
	
	<!-- 农户信息导入end -->
    
	<!-- 添加 begin -->
	<div id='projectsManageWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='projectValidate' class="forge-heading">
				<div class="form-horizontal">
					<p style="font-size:16px;color:block;">基本信息</p>	
					<input type="hidden" id="pid" name="pid"/>
						<div class="form-group">
							<label class="col-md-2 control-label" for="projectName_add">子项目名称</label>
							<div class="col-md-5">
								<input maxlength="30" class="form-control input-sm" type="text" id="projectName_add" name="projectName_add" required validationMessage="必填" value="">
							</div>
						</div>
						 
						<div class="form-group">
							<label class="col-md-2 control-label" for="referenceNumber_add">子项目文号</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="referenceNumber_add" name="referenceNumber_add" required validationMessage="必填" value="">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="carryOutUnit_add">实施单位</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="carryOutUnit_add" name="carryOutUnit_add" required validationMessage="必填" value="">
							</div>
						</div>	
						<div class="form-group">
							<label class="col-md-2 control-label" for="fundYearAdd_add">资金年度</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="fundYearAdd_add" name="fundYearAdd_add" >
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="totalFund_add">总资金(万)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="totalFund_add" name="totalFund_add" required validationMessage="请输入数字(8位长度)" value="" pattern="(\d{1,8}([\.]\d{2})?)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="area_add">子项目所在区域</label>
							<div class="col-md-5">
								<select class="form-control" id="area_add" name="area_add" required="required">
									<option value="">- 选择  -</option>
								</select>
							</div>
						</div>
					
						<div class="form-group">
							<label class="col-md-2 control-label" for="farmerName_add">农户名称</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="farmerName_add" name="farmerName_add"  value="">
							</div>
						</div>
					
						<div class="form-group">
							<label class="col-md-2 control-label" for="scaleAndContent_add">建设规模及内容</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="scaleAndContent_add" name="scaleAndContent_add" required validationMessage="必填" value="">
							</div>
						</div>
					
						<div class="form-group">
							<label class="col-md-2 control-label" for="subjectName_add">项目专项名称</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="subjectName_add" name="subjectName_add" required validationMessage="必填" value="">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="constructionMode_add">建设方式</label>
							<div class="col-md-5">
								<select id="constructionMode_add" name="constructionMode_add" class="form-control" required validationMessage="必填">
									<option value="0" id="constructionMode1">先建后款</option>
									<option value="1" id="constructionMode2">先款后建</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label" for="projectType_add">项目类型</label>
							<div class="col-md-5">
								<select id="projectType_add" name="projectType_add" class="form-control" required validationMessage="必填">
									<option value="1" id="projectType1">种植</option>
									<option value="2" id="projectType2">基础论证</option>
									<option value="3" id="projectType3">培训</option>
									<option value="4" id="projectType4">贴息</option>
									<option value="5" id="projectType5">经济组织</option>
									<option value="6" id="projectType6">其他</option>
								</select>
							</div>
						</div>
						<hr>
						<p style="font-size:16px;color:block;">扶贫信息</p>					
						<div class="form-group">
							<label class="col-md-2 control-label" for="coveredFarmerNumber_add">扶持总农户数(户)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="coveredFarmerNumber_add" name="coveredFarmerNumber_add" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[1-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="povertyGeneralFarmer_add">一般农户(户)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="povertyGeneralFarmer_add" name="povertyGeneralFarmer_add" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[0-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="povertyStrickenFarmerNumber_add">贫困农户(户)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="povertyStrickenFarmerNumber_add" name="povertyStrickenFarmerNumber_add" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[0-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="povertyLowIncomeFarmer_add">低收入困难农户(户)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="povertyLowIncomeFarmer_add" name="povertyLowIncomeFarmer_add" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[0-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="coveringNumber_add">扶持总人数(人)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="coveringNumber_add" name="coveringNumber_add" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[1-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="povertyGeneralPeople_add">一般人口数(人)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="povertyGeneralPeople_add" name="povertyGeneralPeople_add" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[0-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="povertyStrickenPeopleNumber_add">贫困人口数(人)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="povertyStrickenPeopleNumber_add" name="povertyStrickenPeopleNumber_add" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[0-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="povertyLowIncomePeople_add">低收入人口数(人)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="povertyLowIncomePeople_add" name="povertyLowIncomePeople_add" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[0-9]\d{0,9}$)">
							</div>
						</div>
						<hr>
						<p style="font-size:16px;color:block;">其他</p>
						<div class="form-group">
							<label class="col-md-2 control-label" for="deadline_add">完成期限(月)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="deadline_add" name="deadline_add" required validationMessage="请输入数字(6位以下)" value="" pattern="(^[1-9]\d{0,5}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="remark_add">备注</label>
							<div class="col-md-5">
								<input maxlength="50" class="form-control input-sm" type="text" id="remark_add" name="remark_add">
							</div>
						</div>
						<div class="k-edit-buttons k-state-default text-right windowFooter">
							<a id='saveProjects'  class="k-button k-button-icontext k-grid-cancel">
								<span class="k-icon k-cancel"></span>保存
							</a>
							<a id='cancelProjects'  class="k-button k-button-icontext k-grid-cancel">
								<span class="k-icon k-cancel"></span>取消
							</a>
						</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 添加end -->
	
	
	<!-- 调项 begin -->
	<div id='adjustmentProjectsManageWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='adjustmentProjectValidate' class="forge-heading">
				<div class="form-horizontal">
					<input type="hidden" id="adjustmentPid" name="adjustmentPid"/>
						<div class="form-group">
							<label class="col-md-2 control-label" for="projectName">子项目名称</label>
							<div class="col-md-5">
								<input maxlength="30" class="form-control input-sm" readonly="true" type="text" id="projectName2" name="projectName" required validationMessage="必填" value="">
							</div>
						</div>
						 
						<div class="form-group">
							<label class="col-md-2 control-label" for="referenceNumber">子项文号</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" readonly="true" type="text" id="referenceNumber2" name="referenceNumber" required validationMessage="必填" value="">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="carryOutUnit">实施单位</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" readonly="true" type="text" id="carryOutUnit2" name="carryOutUnit" required validationMessage="必填" value="">
							</div>
						</div>	
						<div class="form-group">
							<label class="col-md-2 control-label" for="totalFund">总资金(万)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" readonly="true" type="text" id="totalFund2" name="totalFund" required validationMessage="请输入数字(8位长度)" value="" pattern="(\d{1,8}([\.]\d{2})?)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="area">子项目所在区域</label>
							<div class="col-md-5">
								<select id="adjustmentArea" readonly="true" class="form-control" name="areaId" required="required">
									<option value="">- 选择  -</option>
								</select>
							</div>
						</div>
					
						<div class="form-group">
							<label class="col-md-2 control-label" for="farmerName">农户名称</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" readonly="true"	 type="text" id="farmerName2" name="farmerName" required validationMessage="必填" value="">
							</div>
						</div>
					
						<div class="form-group">
							<label class="col-md-2 control-label" for="scaleAndContent">建设规模及内容</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="scaleAndContent2" name="scaleAndContent2" required validationMessage="必填" value="">
							</div>
						</div>
					
						<div class="form-group">
							<label class="col-md-2 control-label" for="subjectName">项目专项名称</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" readonly="true" type="text" id="subjectName2" name="subjectName" required validationMessage="必填" value="">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label" for="constructionMode">建设方式</label>
							<div class="col-md-5">
								<select id="adjustmentConstructionMode" name="constructionMode" readonly="true" class="form-control" required validationMessage="必填">
									<option value="0" id="constructionMode1">先建后款</option>
									<option value="1" id="constructionMode2">先款后建</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label" for="projectType">项目类型</label>
							<div class="col-md-5">
								<select id="adjustmentProjectType" name="projectType" readonly="true" class="form-control" required validationMessage="必填">
									<option value="1" id="projectType1">种植</option>
									<option value="2" id="projectType2">基础论证</option>
									<option value="3" id="projectType3">培训</option>
									<option value="4" id="projectType4">贴息</option>
									<option value="5" id="projectType5">其他</option>
								</select>
							</div>
						</div>
						
						
						<div class="form-group">
							<label class="col-md-2 control-label" for="coveredFarmerNumber">覆盖农户数(户)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" readonly="true" type="text" id="coveredFarmerNumber2" name="coveredFarmerNumber" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[1-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="coveringNumber">覆盖人数(人)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" readonly="true" type="text" id="coveringNumber2" name="coveringNumber" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[1-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="povertyStrickenFarmerNumber">扶持贫困农户数(户)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" readonly="true" type="text" id="povertyStrickenFarmerNumber2" name="povertyStrickenFarmerNumber" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[1-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="povertyStrickenPeopleNumber">扶持贫困人口数(人)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" readonly="true" type="text" id="povertyStrickenPeopleNumber2" name="povertyStrickenPeopleNumber" required validationMessage="请输入数字(10位以下)" value="" pattern="(^[1-9]\d{0,9}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="deadline">完成期限(月)</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" readonly="true" type="text" id="deadline2" name="deadline" required validationMessage="请输入数字(6位以下)" value="" pattern="(^[1-9]\d{0,5}$)">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="remark">备注</label>
							<div class="col-md-5">
								<input maxlength="50" class="form-control input-sm" readonly="true" type="text" id="remark2" name="remark">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="adjustmentReason">调项原因</label>
							<div class="col-md-5">
								<textarea rows="5" cols="10" class="form-control input-sm" type="text" id="adjustmentReason" name="adjustmentReason" required validationMessage="必填"></textarea>
							</div>
						</div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='adjustmentProjects'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='cancelAdjustment'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>	
		</div>
	</div>
	<!-- 调项：end -->
	<!-- 财务信息列表：start -->
	<div id="adjustmentRecordwindow" style="display:none">
		<div id='adjustmentRecordValidate'></div>
		<div id="adjustmentRecord"></div>
	</div>
	<!-- 财务信息列表：end -->
	
	
	<!-- 财务信息列表：start -->
	<div id="financeDetailsWindow" style="display:none">
			<div>
				<div style="margin-top:0px;margin-bottom:3px;">
					<button id='addFinance' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 新增拨款信息</button>&nbsp;&nbsp;&nbsp;&nbsp;
					<button id='addBaoZhangInfo' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 新增报账信息</button>&nbsp;&nbsp;&nbsp;&nbsp;	
				</div>
		
				<div id="financeCount"></div>	<!-- 财务统计 -->
				<hr>
				<div id="financeChart" align="center"></div>	<!-- 财务报表 -->
				<hr>
				<div id="financeList"></div>	<!-- 财务详细 -->
				
			</div>
		</div>
	<!-- 财务信息列表：end -->
	
	<!-- 添加拨款信息窗口:start -->
	<div id='fianceInfoWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='fianceInfoValidate' class="forge-heading">
				<div class="form-horizontal">
					<input type="hidden" id="fid" name="fid" value =""/>
					<input type="hidden" id="subprojectId" name="subprojectId" value="">
					<div class="form-group">
						<label class="col-md-2 control-label" for="certificateNum">财务凭证号:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="certificateNum" name="certificateNum"  required validationMessage="长度为20位字符内" value=""  pattern="^.{1,20}|.{1,20}$" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="appropriation">拨款金额:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" placeholder="请输入金额数字" type="text" id="appropriation" name="appropriation"   required validationMessage="格式不正确" value="" pattern="^[1-9]\d*$|^0\.[0-9]{1,2}$|^[1-9]\d*\.[0-9]{1,2}$" >
							<font style="color:#CCCCFF;font-size:10px;">单位:万元</font>
						</div>
					</div>
					 
					
					<div class="form-group">
						<label class="col-md-2 control-label" for="operateUser">拨款人:</label>
						<div class="col-md-5">
							<input maxlength="18" class="form-control input-sm" type="text" id="operateUser" name="operateUser"  required validationMessage="必填" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="date">拨款日期:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="datetime" id="date" name="date" pattern ="^(\d{4})-(\d{2})-(\d{2})$" required validationMessage="格式不正确" value="" >
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="date"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="mark">备注:</label>
						<div class="col-md-5">
							<textarea  class="form-control input-sm" id='mark' name='mark' ></textarea>
							<font style="color:#CCCCFF;font-size:10px;">备注在200个字以内</font>
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="mark"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveFiance'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='cancelFiance'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
	</div>
	<!-- 添加拨款信息窗口:end -->
	
	<!-- 添加报账信息窗口:start -->
	<div id='baoZhangfoWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='baozhangInfoValidate' class="forge-heading">
				<div class="form-horizontal">
					<input type="hidden" id="bzfid" name="bzfid" value =""/>
					<input type="hidden" id="bzsubprojectId" name="bzsubprojectId" value="">
					
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzCertificateNum">财务凭证号:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="bzCertificateNum" name="certificateNum"  required validationMessage="长度为20位字符内" value="" pattern="^.{1,20}|.{1,20}$" >
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzAccount">报账金额:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="bzAccount" name="bzAccount" placeholder="请输入金额数字"   required validationMessage="格式不正确" pattern="^[1-9]\d*$|^0\.[0-9]{1,2}$|^[1-9]\d*\.[0-9]{1,2}$" value="">
							<font style="color:#CCCCFF;font-size:10px;">单位:万元</font>
							<span class="k-invalid-msg" data-for="remark"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzuser">报账人:</label>
						<div class="col-md-5">
							<input maxlength="18" class="form-control input-sm" type="text" id="bzuser" name="bzuser" required validationMessage="必填" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzdate">报账日期:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="datetime" id="bzdate" name="bzdate" pattern ="^(\d{4})-(\d{2})-(\d{2})$" required validationMessage="格式不正确" value="" >
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="bzdate"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzremark">备注:</label>
						<div class="col-md-5">
							<textarea  class="form-control input-sm" id='bzremark' name='bzremark' ></textarea>
							<font style="color:#CCCCFF;font-size:10px;">备注在180个字以内</font>
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="remark"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='baoZhangSaveFiance'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='baoZhangCancelFiance'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
	</div>
	<!-- 添加报账信息窗口:end -->
	
	<!-- 信息详情窗口：start -->
	<div id='fianceInfoShowWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='fianceInfoShowValidate' class="forge-heading">
				<div class="form-horizontal">
					<input type="hidden" id="fid" name="fidshow" value =""/>
					<input type="hidden" id="subprojectIdShow" name="subprojectId" value=""/>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="certificateNum">财务凭证号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="certificateNumShow"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="appropriation">拨款金额:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="appropriationShow"></label>
						</div>
					</div>

					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="operateUser">拨款人:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="operateUserShow"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="date">拨款日期:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="dateshow"></label>
						</div>
					</div>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="account">报账金额:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="accountshow"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="bzUser">报账人:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="bzUser"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="date">报账日期:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="bzDate"></label>
						</div>
					</div>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="mark">备注:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="remarkshow"></label>
						</div>
					</div>
					
					<div class="k-edit-buttons k-state-default text-right windowFooter">
						<a id='fanhui'  class="k-button k-button-icontext k-grid-cancel">
							<span class="k-icon k-cancel"></span>返回
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 信息详情列表:end -->
	<!-- 编辑农户信息窗口：start -->
	<div id='farmerInfoWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='farmerInfoValidate' class="forge-heading">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="farmerNumber">农户编号</label>
						<div class="col-md-5">
							<input type="text" hidden="true" id="farmerId" value="">
							<input maxlength="15" class="form-control input-sm" type="text" id="farmerNumber" name="farmerNumber" required validationMessage="必填" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="farmerNumber"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="farmerNameInfo">户主姓名</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="farmerNameInfo" name="farmerNameInfo" required validationMessage="必填" value="" >
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="farmerNameInfo"></span>
						</div>
					</div>
		
					<div class="form-group">
						<label class="col-md-2 control-label" for="phoneNumber">手机号码</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="phoneNumber" name="phoneNumber" required validationMessage="必填" value="" >
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="phoneNumber"></span>
						</div>
					</div>
				<div class="form-group">
						<label class="col-md-2 control-label" for="content">项目内容</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="content" name="content" required validationMessage="必填" value="" >
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="content"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveFarmer'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='cancelFarmer'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
	</div>
	<!-- 编辑农户信息列表:end -->
	<!-- 提示信息 -->
	    <div id="info" style="display:none">
	    
	    	<div class="text"></div>
	    </div>
	
	<myScript>
		<script type="text/javascript" src="resources/js/ProjectManage/projectsManage.js" ></script>
		<script type="text/javascript" src="resources/js/SystemManage/areaManage.js" ></script>
		<script type="text/javascript" src="resources/js/filedownload.js" ></script>		
		<script type="text/javascript" src="resources/plugins/kendoui2015.1.318/js/kendo.all.min.js" ></script>		
		<!-- 上传文件插件 JQuery-File-Upload -->
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/vendor/jquery.ui.widget.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.iframe-transport.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload-process.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload-validate.js"></script>
		
		<script src="resources/plugins/fusioncharts-suite-xt/js/fusioncharts.js"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=LhL7SXvy5bgbjGVS58s25mhZ"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>
		<script src="resources/plugins/jquery/jquery-form.js"></script>
		<script type="text/javascript" src="resources/plugins/baiduMap/GeoUtils.js" ></script>
		<script type="text/javascript" src="resources/plugins/baiduMap/eventWrapper.js" ></script>
		<!--加载鼠标绘制工具-->
		<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
		<!--加载检索信息窗口-->
		<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
		<!-- google calculate area api test -->
		<!-- <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?libraries=geometry&sensor=false"></script> -->	
		<!-- <script src="resources/plugins/GoogleMapsAPIv3_OfflinePack/mapfiles/mapapi.js"></script>	 -->
		<script type="text/javascript" src="resources/js/ProjectManage/googleArea.js" ></script>	
	</myScript>
</body>
</html>