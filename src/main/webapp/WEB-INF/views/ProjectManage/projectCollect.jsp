<!DOCTYPE html>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<html>
<head lang="zh-cn">
	<meta charset="UTF-8">
</head>
<body>
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a>项目管理
		<i class="glyphicon glyphicon-menu-right"></i>子项目管理
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group form-inline">
					<input id="inputSearch" type="text" class="form-control" placeholder="输入用户名、登陆账号筛选" style="width: 400px;"/>
					<button id="searchBtn" class="btn btn-danger"><i class="glyphicon glyphicon-search"></i> 查询</button>
				</div>
			</div>
			<div class="col-md-12">
				<div id='userList'></div>
				<script type="text/x-kendo-template" id="template">
	                <div class="toolbar" >
	                	<a id='addUser' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 添加项目</a>
	                </div>
	            </script>
	            
	            <script type="text/x-kendo-template" id="resetPasswordTemplate">
	                <shiro:hasPermission name="resetUserPassword"><a class='resetPW aLink' title='重置密码'>重置<a></shiro:hasPermission>
	            </script>
	            <script type="text/x-kendo-template" id="userArea_Role">
	            	<shiro:hasPermission name="operatorUserArea"><a class='bindingArea aLink' title='选择地区'>地区</a>&nbsp;&nbsp;</shiro:hasPermission>
					<shiro:hasPermission name="operatorUserRole"><a class='bindingRole aLink' title='选择角色'>角色</a></shiro:hasPermission>
	            </script>
	             <script type="text/x-kendo-template" id="userUpdate_Delete">
	            	<shiro:hasPermission name="updateUserInfo"><a class='editUser editIcon aLink' title='编辑用户'><i class='fa fa-pencil'></i><a/>&nbsp;&nbsp;</shiro:hasPermission>
					<shiro:hasPermission name="deleteUserInfo"><a class='deleteUser deleteIcon aLink' title='删除用户'><i class='glyphicon glyphicon-trash'></i><a> </shiro:hasPermission>
	            </script>
			</div>
		</div>
	</div>
	
	<div id='userInfoWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='userInfoValidate' class="forge-heading">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="realName">用户姓名</label>
						<div class="col-md-5">
							<input type="text" hidden="true" id="userId" value="">
							<input maxlength="15" class="form-control input-sm" type="text" id="realName" name="realName" required validationMessage="必填" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="realName"></span>
						</div>
					</div>
				<div class="form-group">
						<label class="col-md-2 control-label" for=constant>所属机构</label>
						<div class="col-md-5">
							<select id="constant" name="constant" class="form-control" required validationMessage="必填" style="width: 100%">
							</select>
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="constant"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="userName">登录账号</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="userName" name="userName" required validationMessage="必填" value="" >
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="userName"></span>
						</div>
					</div>
					<div class="form-group" id='passwordWrap'>
						<label class="col-md-2 control-label" for="password">登录密码</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="password" id="password" name="password" required validationMessage="必填" value="" placeholder="请设置六位以上的密码">
							<span id="pasd"></span>
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="password"></span>
						</div>
					</div>
					<div class="form-group" id='confirmpasswordWrap'>
						<label class="col-md-2 control-label" for="confirmpassword">确认密码</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="password" id="confirmpassword" name="confirmpassword" required validationMessage="必填" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="confirmpassword"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="mobile">电话</label>
						<div class="col-md-5">
							<!-- <input class="form-control input-sm" type="text" id="mobile" name="mobile" value=""> -->
							<input class="form-control input-sm" placeholder="15012345678或者0851-12345678" type="text" id="mobile" name="mobile" pattern="((1[3,4,5,7,8]{1}[0-9]{1})+\d{8})|((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?" validationMessage="格式错误" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="mobile"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="email">邮箱</label>
						<div class="col-md-5">
							<input class="form-control input-sm" placeholder="e.g. myname@example.net" type="email" id="email" name="email" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="email"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="available">是否有效</label>
						<div class="col-md-5">
							<label><input type="radio" name='isInUse' checked value="true"/> 是</label>&nbsp;&nbsp;
							<label><input type="radio" name='isInUse' value="false"/> 否</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="description">描述</label>
						<div class="col-md-5">
							<textarea  class="form-control input-sm" id='description' name='description' ></textarea>
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="description"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveUser'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='cancelUser'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
	</div>
	<!--绑定 地区权限  窗口-->
	<div id='bindingDepartmentWindow' style="display: none;">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div id='departmentList'></div>
				</div>
			</div>
			<div class="row">
				<p id="selectedNodes"></p>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveDepartment'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='cancelDepartment'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
	</div>

	<!--绑定角色 窗口-->
	<div id='bindingRoleWindow' style="display: none;">
		<div class="container-fluid">
			<div class="row">
			<p><input id="cond-role" type="text" class="form-control" placeholder="输入角色名称查询"><input type="hidden" id="hiddenRole"></p>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div id='roleList'></div>
				</div>
			</div>
		</div>
	</div>
	<myScript>
		<script type="text/javascript" src="resources/js/ProjectManage/projectCollect.js" ></script>
	</myScript>
	
</body>
</html>