<!DOCTYPE html>
	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	﻿<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%> 
<html>
<head lang="zh-cn">
	<meta charset="UTF-8"/>
</head>
<myCss>
	<link rel="stylesheet" href="resources/plugins/jQuery-File-Upload-9.11.2/css/jquery.fileupload.css">
	<link rel="stylesheet" href="resources/plugins/jQuery-File-Upload-9.11.2/css/jquery.fileupload-ui.css">
	<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" />
	<style type="text/css">
		/* body, html{margin:0;font-family:"微软雅黑";} */
		/* #showSupervisionsMap{height:100%;width:100%;} */
		#showChecksMap,#showSupervisionsMap{position:"absolute";height:645px;width:100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
		#r-result{width:100%;}
	</style>
</myCss>
<body>
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a>项目管理
		<i class="glyphicon glyphicon-menu-right"></i>子项目管理
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group form-inline">
					<input id="inputSearch" type="text" class="form-control" placeholder="输入子项目名称、施工单位、农户名称" style="width: 400px;"/>
					<button id="searchBtn" class="btn btn-danger"><i class="glyphicon glyphicon-search"></i>查询</button>
				</div>
			</div>
			<div class="col-md-12">
				<div id='subProjectManageList'></div>
				<script type="text/x-kendo-template" id="template">
	               	<div class="toolbar" >
	                	<a id='addSubProject' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i>添加子项目</a>
	                </div>
	            </script>
	            <script type="text/x-kendo-template" id="showSupervisionsTemplate">
					<shiro:hasPermission name='getSupervisions'><a class='getSupervisions editIcon aLink' title='查看项目监管'><i class='fa fa-map-marker'>监管记录</i><a/></shiro:hasPermission>
				</script>
	            <script type="text/x-kendo-template" id="showChecksTemplate">
					<shiro:hasPermission name='getChecks'><a class='getChecks editIcon aLink' title='查看项目验收'><i class='fa fa-check'>验收记录</i><a/></shiro:hasPermission>
				</script>
	            <script type="text/x-kendo-template" id="detailScan">
					<shiro:hasPermission name='updateSubProject'><a class='updateSubProject editIcon aLink' title='修改子项目'><i class='fa fa-pencil'></i><a/></shiro:hasPermission>
					<shiro:hasPermission name='deleteSubProject'><a class='deleteSubProject editIcon aLink' title='删除子项目'><i class='glyphicon glyphicon-trash'></i><a/></shiro:hasPermission>
					<shiro:hasPermission name='subProjectDetail'><a class='subProjectDetail editIcon aLink' title='查看子项目明细'><i class='fa fa-eye'></i><a/></shiro:hasPermission>
				</script>	            
				<script type="text/x-kendo-template" id="subProjectOperation">
					<shiro:hasPermission name='detailSubProject'><a class='detailSubProject editIcon aLink' title='查看明细'><i class='fa fa-eye'></i><a/></shiro:hasPermission>
					<shiro:hasPermission name='deleteSubProject'><a class='deleteSubProject deleteIcon aLink' title='删除'><i class='glyphicon glyphicon-trash'></i></a>&nbsp;&nbsp; </shiro:hasPermission>
					<shiro:hasPermission name='updateSubProject'><a class='editSubProject editIcon aLink' title='编辑'><i class='fa fa-pencil'></i></a>&nbsp;&nbsp;</shiro:hasPermission>
				</script>
			</div>
		</div>
	</div>
	<!-- 添加/编辑窗口 begin -->
	<div id='subProjectManageWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='subProjectValidate' class="forge-heading">
				<div class="form-horizontal">
					<form method="post" enctype="multipart/form-data" action="subProjectManage/addSubProject" accept-charset="UTF-8">
					<input type="hidden" id="subPid" name="subPid"/>
						<div class="form-group">
							<label class="col-md-2 control-label" for="project">主项目</label>
							<div class="col-md-5">
								<select class="form-control" id="project" name="projectId" required="required">
									<option value="">- 选择  -</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="subProjectName">子项目名称</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="subProjectName" name="subProjectName" required validationMessage="必填" value="">
							</div>
						</div>
						 
						<div class="form-group">
							<label class="col-md-2 control-label" for="subProjectNumber">子项文号</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="subProjectNumber" name="subProjectNumber" required validationMessage="必填" value="">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="totalCapital">总资金</label>
							<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="totalCapital" name="totalCapital" required validationMessage="请输入数字" value="" pattern="(^([0-9.]+)$)">
							</div>
						</div>
						
						<div class="form-group">
						<label class="col-md-2 control-label" for="implementationUnit">实施单位</label>
						<div class="col-md-5">
							<input maxlength="15" class="form-control input-sm" type="text" id="implementationUnit" name="implementationUnit" required validationMessage="必填" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="subProjectArea">子项目所在区域</label>
						<div class="col-md-5">
							<input maxlength="15" class="form-control input-sm" type="text" id="subProjectArea" name="subProjectArea" required validationMessage="必填" value="">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label" for="farmerName">农户名称</label>
						<div class="col-md-5">
							<input maxlength="15" class="form-control input-sm" type="text" id="farmerName" name="farmerName" required validationMessage="必填" value="">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label" for="projectScaleAndContent">建设规模及内容</label>
						<div class="col-md-5">
							<input maxlength="15" class="form-control input-sm" type="text" id="projectScaleAndContent" name="projectScaleAndContent" required validationMessage="必填" value="">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label" for="path">批复文件</label>
						<div class="col-md-5" id="path">
							<input class="form-control input-sm" type="file" name="file" id="file1"/>
							<input class="form-control input-sm" type="file" name="file" id="file2"/>
							<input class="form-control input-sm" type="file" name="file" id="file3"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="constructionMode">建设方式</label>
						<div class="col-md-5">
							<select id="constructionMode" name="constructionMode" class="form-control" required validationMessage="必填">
								<option value="0" id="constructionMode1">先建后款</option>
								<option value="1" id="constructionMode2">先款后建</option>
							</select>
						</div>
					</div>
					<div class="k-edit-buttons k-state-default text-right windowFooter">
						<input class="k-button k-button-icontext k-primary k-grid-update" type="submit" value="保存"/>
						<a id='cancelSubProject'  class="k-button k-button-icontext k-grid-cancel">
							<span class="k-icon k-cancel"></span>取消
						</a>
					</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<!-- show supervisions of sub project -->
	<div id="showSupervisionsWindow" style="display:none">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
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
	</div>
	
	<!-- show checks of sub project -->
	<div id="showChecksWindow" style="display:none">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div id='showChecksMap'></div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='perimeter'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>计算长度
			</a>
			<a id='area'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>计算面积
			</a>
		</div>
	</div>
	
	<!-- 子项目详情  begin -->
	<div id='subProjectManageDetailWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='subProjectManageDetailValidate' class="forge-heading">
				<div class="form-horizontal">
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="project">主项目:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="projectFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="farmerName">农户名称：</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="farmerNameFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="subProjectName">子项目名称:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="subProjectNameFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="subProjectNumber">子项目文号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="subProjectNumberFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="totalCapital">总资金:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="totalCapitalFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="path">批复文件:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="fileFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="implementationUnit">实施单位:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="implementationUnitFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="subProjectArea">子项目所在区域:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="subProjectAreaFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="projectScaleAndContent">建设规模及内容：</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="projectScaleAndContentFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="constructionMode">建设方式:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="constructionModeFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="shouldAccount">应报账:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="shouldAccountFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="jz">结转:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="jzFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="jy">结余:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="jyFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="reimbursementRate">报账率:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="reimbursementRateFont"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="checkStatus">验收状态:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="checkStatusFont"></label>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter" style="margin-top:10px;">
			<a id='backSubProjectManageDetail'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>返回
			</a>
		</div>
	</div>
	<!-- 子项目详情  end -->
		
	<myScript>
		<script type="text/javascript" src="resources/js/ProjectManage/subProjectManage.js" ></script>
		<script type="text/javascript" src="resources/js/ProjectManage/GeoUtils.js" ></script>
		<script type="text/javascript" src="resources/js/filedownload.js" ></script>
		<!-- 上传文件插件 JQuery-File-Upload -->
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/vendor/jquery.ui.widget.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.iframe-transport.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload-process.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload-validate.js"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=LhL7SXvy5bgbjGVS58s25mhZ"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>
		<script src="resources/plugins/jquery/jquery-form.js"></script>
	</myScript>
</body>
</html>