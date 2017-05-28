﻿<!DOCTYPE html>
<html>
<head lang="zh-cn">
	<meta charset="UTF-8">
</head>
<body>
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a> 系统管理
		<i class="glyphicon glyphicon-menu-right"></i>系统日志查询
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" >
				<div class="form-group form-inline">
					<label>模块名称&nbsp;&nbsp;</label>
					<select class="form-control" id='AppModule' style="width:120px">
						<option value="">----全部----</option>
						<option value="项目管理">项目管理</option>
						<option value="项目监管">项目监管</option>
						<option value="项目验收">项目验收</option>
						<option value="项目Excel导入">项目Excel导入</option>
						<option value="附件管理">附件管理</option>
						<option value="坐标管理">坐标管理</option>
						<option value="鹰眼坐标管理">鹰眼坐标管理</option>
						<option value="财务管理">财务管理</option>
						<option value="项目统计分析">项目统计分析</option>
						<option value="用户管理">用户管理</option>
						<option value="角色管理">角色管理</option>
						<option value="手机应用">手机应用</option>
						<option value="登录">登录</option>
					</select>&nbsp;&nbsp;
					<label>操作者&nbsp;&nbsp;</label>
					<input id="SubUser" type="text" class="form-control" style="width: 150px;"  placeholder="输入操作者查询"/>&nbsp;&nbsp;
					<label>时间范围&nbsp;&nbsp;</label>
					<input id='searchStartTime' type="text" class="form-control" /> - 
					<input id='searchEndTime' type="text" class="form-control" />&nbsp;&nbsp;
					
				</div>
			</div>
			<div class="col-md-12" >
				<div class="form-group form-inline">
					<label>操作类型&nbsp;&nbsp;</label>
					<select class="form-control" id='OpType' style="width:120px">
						<option value="">----全部----</option>
						<option value="Add">Add</option>
						<option value="Update">Update</option>
						<option value="Del">Del</option>
						<option value="AddPrivilege">AddPrivilege</option>
						<option value="UpdatePrivilege">UpdatePrivilege</option>
						<option value="DelPrivilege">DelPrivilege</option>
						<option value="Login">Login</option>
						<option value="Approve">Approve</option>
						<option value="View">View</option>
						<option value="Import">Import</option>
						<option value="Export">Export</option>
						<option value="AppLogin">AppLogin</option>
						<option value="AppQuery">AppQuery</option>
						<option value="AppAdd">AppAdd</option>
						<option value="Other">Other</option>
					</select>&nbsp;&nbsp;
					<label>操作IP&nbsp;&nbsp;</label>
					<input id="Sip" type="text" class="form-control" style="width: 150px;"  placeholder="输入操作IP查询"/>&nbsp;&nbsp;
					<button id="searchBtn" class="btn btn-danger"><i class="glyphicon glyphicon-search"></i>确定</button>&nbsp;&nbsp;
					<button id="resetBtn" class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i>重置</button>
				</div>
			</div>
			<div class="col-md-12">
				<div id='logList'></div>
			</div>
		</div>
	</div>
	
	<myScript>
		<script type="text/javascript" src="resources/js/SystemManage/systemOperationLog.js" ></script>
	</myScript>
	
</body>
</html>