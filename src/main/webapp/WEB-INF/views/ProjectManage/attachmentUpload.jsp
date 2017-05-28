﻿<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
	<meta charset="UTF-8">
	<meta http-equiv="pragma" content="no-cache">  
	<meta http-equiv="cache-control" content="no-cache">  
	<meta http-equiv="expires" content="0">
</head>
<myCss>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" />
	<style type="text/css">
		#showChecksMap,#showSupervisionsMap{position:"absolute";height:645px;width:98%;overflow: hidden;margin:0;font-family:"微软雅黑";}
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
	</style>
</myCss>
<body style="margin:0px;padding:0px;">
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a> 项目管理
		<i class="glyphicon glyphicon-menu-right"></i>项目管理
	</div>
	<script id="pictureTipTemplate" type="text/x-kendo-template">
		<img class="demo-trigger" src="#=target.data('src')#" style="width:480px"/>
	</script>
	<div class="container-fluid">
				
		<div class="row">
			<div class="col-md-12">
				<div class="form-group form-inline">
					<input id="inputSearch" type="text" class="form-control" placeholder="输入项目名、专项名、年度进行查询" style="width: 400px;"/>
					<button id="searchBtn" class="btn btn-danger"><i class="glyphicon glyphicon-search"></i> 查询</button>
				</div>
			</div>
			<div class="col-md-12">
				<div id='projectList'></div>
			</div>
		</div>
	</div>
				
	<div id="subProjectWindow" style="display: none;">
		<div class="container-fluid">
				<div class="row">
					<div class="col-md-12">
						<div id='subProjectList'></div>
					</div>
					<div class="col-md-12">
				<div id='subProjectManageList'></div>
				<script type="text/x-kendo-template" id="template">
	               	<div class="toolbar" >
	                	<a id='addSubProject' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i>添加子项目</a>
						<shiro:hasPermission name="subProjectExport"><a id='personalInfoExport' class='btn btn-xs btn-primary'><i class='glyphicon glyphicon-download'></i>导出</a>&nbsp;&nbsp;&nbsp;&nbsp;</shiro:hasPermission>
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
				<div class="row">
					<div class="col-md-12">
						<div class="alert alert-warning alert-dismissible" role="alert">
						  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						  <!-- <span style="background-color:#FFC0CB"><strong>红色</strong></span>代表导入失败，<span style="background-color:#E3CF57"><strong>黄色</strong></span>代表更新，<strong>白色</strong>代表新增。 -->
						</div>
					</div>
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
						<!-- <div class="form-group">
							<label class="col-md-2 control-label" for="project">主项目</label>
							<div class="col-md-5">
								<select class="form-control" id="project" name="projectId" required="required">
									<option value="">- 选择  -</option>
								</select>
							</div>
						</div> -->
						<div class="form-group">
							<label class="col-md-2 control-label" for="subProjectName">子项目名称</label>
							<div class="col-md-5">
								<input type="hidden" id="pId" name="projectId"/>
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
							<label class="col-md-2 control-label" for="shouldAccount">应报账</label>
								<div class="col-md-5">
								<input maxlength="15" class="form-control input-sm" type="text" id="shouldAccount" name="shouldAccount" required validationMessage="请输入数字" value="" pattern="(^([0-9.]+)$)">
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
			<div class="row"  id="supervisionHorizontal" style="height:650px;">
				<div class="col-md-4">
					<div id='showSupervisionsList'></div>
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
	</div>
	
	<!-- show checks of sub project -->
	<div id="showChecksWindow" style="display:none">
		<div class="container-fluid">
			<div class="row"  id="checkHorizontal" style="height:650px;">
				<div class="col-md-4">
					<div id='showChecksList'></div>
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
		<div id="projectDetailWindow" style="display:none">
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-12">
						<div id='projectDetailList'></div>
					</div>
				</div>
			</div>
		</div>
	<div id="changeDeleteWindow" style="display: none;">
		<div class="toolbar">
			<div class="demo-section k-content" id="fileId"> 
				<p class="btn btn-xs text-left" style="margin-top: 0px;">
					<input name="files" id="fileupload" type="file" /> 
				</p>
				</div>
			</div>
			<div style="height:300px;width:100%;" id="picture1">
			</div>
		 <div class="container-fluid" >
			<div id='changeDeleteValidate' class="forge-heading">
				<div class="form-horizontal">
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
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveRemark'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
		</div>
	</div>
	
	
	<!-- 添加拨款信息窗口:start -->
	<div id='fianceInfoWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='fianceInfoValidate' class="forge-heading">
				<div class="form-horizontal">
					<input type="hidden" id="fid" name="fid" value =""/>
					<input type="hidden" id="subprojectId" name="subprojectId" value="">
					<div class="form-group">
						<label class="col-md-2 control-label" for="certificateNum">凭证号:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="certificateNum" name="certificateNum"  required validationMessage="必填" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="appropriation">拨款金额:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" placeholder="0或者0.00" type="text" id="appropriation" name="appropriation" pattern="^([0-9]+)\d*|\d+(\d|(\.[1-9]{1,2}))$"  required validationMessage="格式不正确" value="">
							<font style="color:#CCCCFF;font-size:10px;">单位:万元</font>
						</div>
					</div>
					<!-- 
					<div class="form-group">
						<label class="col-md-2 control-label" for="account">报账金额:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="account" name="account" pattern="^([0-9]+)$" readonly="readonly"  required validationMessage="必填" value="">
							<font style="color:#CCCCFF;font-size:10px;">单位:万元</font>
							<span class="k-invalid-msg" data-for="remark"></span>
						</div>
					</div>
					 -->
					<div class="form-group">
						<label class="col-md-2 control-label" for="operateUser">拨款人:</label>
						<div class="col-md-5">
							<input maxlength="18" class="form-control input-sm" type="text" id="operateUser" name="operateUser"  required validationMessage="必填" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="date">拨款日期:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="datetime" id="date" name="date" required validationMessage="必填" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="date"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="remark">备注:</label>
						<div class="col-md-5">
							<textarea  class="form-control input-sm" id='remark' name='remark' ></textarea>
							<font style="color:#CCCCFF;font-size:10px;">备注在200个字以内</font>
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="remark"></span>
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
						<label class="col-md-2 control-label" for="bzCertificateNum">凭证号:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="bzCertificateNum" name="certificateNum"  required validationMessage="必填" value="">
						</div>
					</div>
					<!-- 
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzAppropriation">拨款金额:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="bzAppropriation" name="bzAppropriation" pattern="^([0-9]+)$/^([0-9]+)(\.[0-9]{2})$" readonly="readonly" required validationMessage="必填" value="">
							<font style="color:#CCCCFF;font-size:10px;">单位:万元</font>
						</div>
					</div>
					
					 <div class="form-group">
						<label class="col-md-2 control-label" for="bkOperateUser">拨款人:</label>
						<div class="col-md-5">
							<input maxlength="18" class="form-control input-sm" type="text" id="bkOperateUser" name="bkOperateUser" readonly="readonly"  required validationMessage="必填" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="bkDate">拨款日期:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="datetime" id="bkDate" name="bkDate" readonly="readonly" required validationMessage="必填" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="date"></span>
						</div>
					</div>
					 -->
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzAccount">报账金额:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="bzAccount" name="bzAccount" pattern="^([0-9]+)\d*|\d+(\d|(\.[1-9]{1,2}))$"   required validationMessage="格式不正确" value="">
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
							<input class="form-control input-sm" type="datetime" id="bzdate" name="bzdate"  required validationMessage="必填" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="date"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzremark">备注:</label>
						<div class="col-md-5">
							<textarea  class="form-control input-sm" id='bzremark' name='bzremark' ></textarea>
							<font style="color:#CCCCFF;font-size:10px;">备注在200个字以内</font>
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
						<label class="col-md-2 control-label" for="certificateNum">凭证号:</label>
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
					<!--
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="accountJeYu">资金结余:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="accountJeYushow"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="baoZhangLv">报账率:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="baoZhangLvshow"></label>
						</div>
					</div>
					  -->
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
						<label class="col-md-2 control-label" for="remark">备注:</label>
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
							
	<!-- 财务信息列表：start -->
	<div id="userdetailsWindow" style="display:none">
			<div>
				<div>
					<button id='addFinance' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 新增拨款信息</button>&nbsp;&nbsp;&nbsp;&nbsp;
					<button id='addBaoZhangInfo' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 新增报账信息</button>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<p></p>
				<div id="financeList"></div>
			</div>
		</div>
	<!-- 财务信息列表：end -->
	
	
	<!-- 个人信息详细  begin -->
	<div id='projectInfoDetailWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='projectInfoDetailValidate' class="forge-heading">
				<div class="form-horizontal">
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="referenceNumber" style="width: 100px;">文号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="referenceNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="projectNumber" style="width: 100px;">项目唯一编号:</label>
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
						<label class="col-md-2 control-label" for="city" style="width: 100px;">地州名:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="city"></label>
						</div>
					</div>
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
						<label class="col-md-2 control-label" for="projectName" style="width: 100px;">项目名称:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="projectName"></label>
						</div>
					</div>		
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="approvalNumber" style="width: 100px;">批复文号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="approvalNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="totalFund" style="width: 100px;">总资金(万元):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="totalFund"></label>
						</div>
					</div>	
					<!--  	
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="financeFund">财政资金(万元):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="financeFund"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="selfFinancing">自筹资金(万元):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="selfFinancing"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="integrateFund">整合资金(万元):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="integrateFund"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="coveredFarmerNumber">覆盖农户数(户):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="coveredFarmerNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="coveringNumber">覆盖人数(人):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="coveringNumber"></label>
						</div>
					</div>
					-->	
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="povertyStrickenFarmerNumber" style="width: 100px;">扶持贫困农户数(户):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="povertyStrickenFarmerNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="povertyStrickenPeopleNumber" style="width: 100px;">扶持贫困人口数(人):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="povertyStrickenPeopleNumber"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="scaleAndContent" style="width: 100px;">建设规模及内容:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="scaleAndContent"></label>
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
						<label class="col-md-2 control-label" for="fundToCountry" style="width: 100px;">到县资金(万元):</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="fundToCountry"></label>
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
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter" style="margin-top:10px;">
			<a id='backProjectDetail'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>返回
			</a>
		</div>
	</div>
	<!-- 个人信息详细  end -->				

	<myScript>
		<script type="text/javascript" src="resources/js/ProjectManage/attachmentUpload.js" ></script>
		<script type="text/javascript" src="resources/js/filedownload.js" ></script>
			<!-- 上传文件插件 JQuery-File-Upload -->
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/vendor/jquery.ui.widget.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.iframe-transport.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload-process.js"></script>
		<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload-validate.js"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=LhL7SXvy5bgbjGVS58s25mhZ"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>
		<script type="text/javascript" src="resources/plugins/baiduMap/GeoUtils.js" ></script>
		<script type="text/javascript" src="resources/plugins/baiduMap/eventWrapper.js" ></script>
		<script src="resources/plugins/jquery/jquery-form.js"></script>
	</myScript>
</body>
</html>