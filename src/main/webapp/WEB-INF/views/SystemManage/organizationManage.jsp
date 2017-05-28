<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
	<meta charset="UTF-8">
</head>
<body>
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a> 系统管理
		<i class="glyphicon glyphicon-menu-right"></i>组织机构管理
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" >
				<div class="form-group form-inline">
					<input id="inputSearch" type="text" class="form-control" placeholder="输入部门名称筛选" style="width: 400px;"  />&nbsp;&nbsp;
					<button id="searchBtn" class="btn btn-danger"><i class="glyphicon glyphicon-search"></i> 查询</button>&nbsp;&nbsp;
					<button id="resetBtn" class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
			</div>
			<div class="col-md-12">
				<div id='orgizationList'></div>
				<script type="text/x-kendo-template" id="template">
	                <div class="toolbar" >
	                	<shiro:hasPermission name='addOrganizationInfo'><a id='addBtn' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 添加组织机构</a></shiro:hasPermission>
	                </div>
	            </script>
	            <script type="text/x-kendo-template" id="org_update_delete">
	                <shiro:hasPermission name='updateOrganizationInfo'><a class='editOrganization editIcon aLink' title='修改组织机构'><i class='fa fa-pencil'></i></a>&nbsp;&nbsp;</shiro:hasPermission>
					<shiro:hasPermission name='deleteOrganizationInfo'><a class='deleteDepartment deleteIcon aLink' title='删除组织机构'><i class='glyphicon glyphicon-trash'></i></a> </shiro:hasPermission>
	            </script>
			</div>
		</div>
	</div>
	
	<!--【添加、编辑】弹窗-->
	<div id='organizationInfoWindow' style="display: none;"></div>
	
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
						<label class="col-md-2 control-label" for="orgCode">组织机构编码</label>
						<div class="col-md-5">
							<input type="text" hidden="true" id="orgId" value="#:orgId#">
							<input maxlength="12" class="form-control input-sm" type="text" id="orgCode" name="orgCode"  value='#:orgCode#' required validationMessage="必填" placeholder="组织机构编码最大长度为12位"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for='orgName'>组织机构名称</label>
						<div class="col-md-5">
							<input maxlength="12" class="form-control input-sm" type="text" id="orgName" name="orgName"  value='#:orgName#' required validationMessage="必填"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for='parentCode' style="width:21%">所属上级组织机构</label>
						<div class="col-md-5">
							<select class="form-control" id='parentCode' required="required" data-required-msg="必填" value='#:parentCode#' style="width:100%">
							</select>
						</div>
					</div>
				</div>
			</div>
		</div>
    	<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveOrganization'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='cancelOrganization'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
		</script>
		<script type="text/javascript" src="resources/js/SystemManage/organizationManage.js" ></script>
	</myScript>
</body>
</html>