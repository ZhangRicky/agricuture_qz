<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
	<meta charset="UTF-8">
	<meta http-equiv="pragma" content="no-cache">  
	<meta http-equiv="cache-control" content="no-cache">  
	<meta http-equiv="expires" content="0">
</head>
<body>
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a> 系统管理
		<i class="glyphicon glyphicon-menu-right"></i>地区管理
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" >
				<div class="form-group form-inline">
					<input id="inputSearch" type="text" class="form-control" placeholder="输入地区名称筛选" style="width: 400px;"  />&nbsp;&nbsp;
					<button id="searchBtn" class="btn btn-danger"><i class="glyphicon glyphicon-search"></i> 查询</button>&nbsp;&nbsp;
					<button id="resetBtn" class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
			</div>
			<div class="col-md-12">
				<div id='areaList'></div>
				<script type="text/x-kendo-template" id="template">
	                <div class="toolbar" >
	                	<shiro:hasPermission name='addAreaInfo'><a id='addBtn' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 添加地区</a></shiro:hasPermission>
	                </div>
	            </script>
	            <script type="text/x-kendo-template" id="area_update_delete">
					<shiro:hasPermission name='updateAreaInfo'><a class='editDepartment editIcon aLink' title='修改地区'><i class='fa fa-pencil'></i></a>&nbsp;&nbsp;</shiro:hasPermission>
					<shiro:hasPermission name='deleteAreaInfo'><a class='deleteDepartment deleteIcon aLink' title='删除地区'><i class='glyphicon glyphicon-trash'></i></a> </shiro:hasPermission>
				</script>
			</div>
		</div>
	</div>
	
	<!--【添加、编辑】弹窗-->
	<div id='areaInfoWindow' style="display: none;"></div>
	
	<!--【绑定号码段】弹窗-->
	<div id='bindNoWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='bindNoGrid' style="height: 350px;"></div>
			<script type="text/x-kendo-template" id="bindTemplate">
	             <div class="toolbar" >
	             	<a class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 全部加入</a>&nbsp;&nbsp;
	             	<a class='btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 全部移除</a>
	       		</div>
	        </script>
			
			<div class="k-edit-buttons k-state-default text-right windowFooter">
				<a id='cancelBindNo'  class="k-button k-button-icontext k-grid-cancel">
					<span class="k-icon k-cancel"></span>取消
				</a>
			</div>
		</div>
	</div>
	<myScript>
		<script type="text/x-kendo-template" id="areaWindowTemplate">
    	<div class="container-fluid" >
			<div id='areaInfoValidate' class="forge-heading">
				<div class="form-horizontal" >
					<div class="form-group">  
						<label class="col-md-2 control-label" for="areaCode">地区编码</label>
						<div class="col-md-5">
							<input type="text" hidden="true" id="id" value="#:id#">
							<input type="text" hidden="true" id="areaLevel" value="#:areaLevel#">
							<input maxlength="12" class="form-control input-sm" type="text" id="areaCode" name="areaCode"  value='#:areaCode#' required validationMessage="必填" placeholder="地区编码最大长度为12位"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for='areaName'>地区名称</label>
						<div class="col-md-5">
							<input maxlength="10" class="form-control input-sm" type="text" id="areaName" name="areaName"  value='#:areaName#' required validationMessage="必填"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for='parentCode'>所属地区</label>
						<div class="col-md-5">
							<select class="form-control" id='parentCode' required="required" data-required-msg="必填" value='#:parentCode#'>
							</select>
						</div>
					</div>
				</div>
			</div>
		</div>
    	<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveArea'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='cancelArea'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
		</script>
		<script type="text/javascript" src="resources/js/SystemManage/areaManage.js" ></script>
	</myScript>
</body>
</html>