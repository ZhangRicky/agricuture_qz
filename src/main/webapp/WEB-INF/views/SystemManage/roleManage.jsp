<!DOCTYPE html>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<html>
<head lang="zh-cn">
	<meta charset="UTF-8">
	<meta http-equiv="pragma" content="no-cache">  
	<meta http-equiv="cache-control" content="no-cache">  
	<meta http-equiv="expires" content="0">
<title></title>
<myCSS></myCSS>
</head>
<body>
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a> 系统管理 
		<i class="glyphicon glyphicon-menu-right"></i>角色管理
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group form-inline">
					<input id="inputSearch" type="text" class="form-control" placeholder="输入角色名称筛选" style="width: 400px;" />
					<button id="searchBtn" class="btn btn-danger">
						<i class="glyphicon glyphicon-search"></i> 查询
					</button>
					<button id="resetBtn" class="btn btn-default">
						<i class="glyphicon glyphicon-repeat"></i> 重置
					</button>
				</div>
			</div>
			<div class="col-md-12">
				<div id='roleList'></div>
				<script type="text/x-kendo-template" id="template">
	                <div class="toolbar" >
	                	<shiro:hasPermission name='addRoleInfo'><a id='addRole' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 添加角色</a></shiro:hasPermission>
	                </div>
	            </script>
	            <script type="text/x-kendo-template" id="systemMenus">
	            	<shiro:hasPermission name='operatorRoleMenus'><a class='bindingSiteMenu aLink' title='绑定系统功能'>系统功能</a></shiro:hasPermission>
	            </script>
	            <script type="text/x-kendo-template" id="roleUpdate_Delete">
					<shiro:hasPermission name='updateRoleInfo'><a class='editRole editIcon aLink' title='修改角色'><i class='fa fa-pencil'></i></a>&nbsp;&nbsp;</shiro:hasPermission>
					<shiro:hasPermission name='deleteRoleInfo'><a class='deleteRole deleteIcon aLink' title='删除角色'><i class='glyphicon glyphicon-trash'></i></a> </shiro:hasPermission>
				</script>
			</div>
		</div>
	</div>
	<!-- 添加角色页面 -->
	<div id='roleInfoWindow' style="display: none;">
		<div class="container-fluid">
			<div id='roleInfoValidate' class="forge-heading">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="roleName">角色名称</label>
						<div class="col-md-5">
							<input type="text" hidden="true" id="roleId" value="">
							<input class="form-control input-sm" type="text" id="roleName" name="roleName" required="required" data-required-msg="必填"  maxlength="255" autofocus="autofocus">
						</div>
						<div class="col-md-3">
							<span class="k-invalid-msg" data-for="roleName"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">角色类型</label>
						<div class="col-md-5">
							<select class="form-control" id='roleType' required="required" data-required-msg="必填">
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="roleDesc">角色描述</label>
						<div class="col-md-5">
							<textarea class="form-control input-sm" id='roleDesc' name='roleDesc' required="required" data-required-msg="必填" maxlength="255"></textarea>
						</div>
						<div class="col-md-3">
							<span class="k-invalid-msg" data-for="roleDesc"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveRole' class="k-button k-button-icontext k-primary k-grid-update"> <span class="k-icon k-update"></span>保存
			</a> <a id='cancelRole' class="k-button k-button-icontext k-grid-cancel"> <span class="k-icon k-cancel"></span>取消
			</a>
		</div>
	</div>

	<!--绑定站点菜单 窗口-->
	<div id='bindingSiteMenuWindow' style="display: none;">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div id='resourcesList'></div>
				</div>
			</div>
		</div>
	</div>
	
	<myScript>
		<script type="text/javascript" src="resources/js/dataSource.js"></script>
		<script type="text/javascript" src="resources/js/SystemManage/roleManage.js"></script>
	</myScript>
</body>
</html>