<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="pragma" content="no-cache">  
		<meta http-equiv="cache-control" content="no-cache">  
		<meta http-equiv="expires" content="0">
		<myCSS></myCSS>
	</head>
	<body>
		<div class='container-fluid'>
			<div class='row'>
				<div class='col-md-12'>
					<ol class="breadcrumb">
						<li><a href="home"><span class="glyphicon glyphicon-home"></span></a> 账号设置 </li>
						<li class="active">我的资料</li>
					</ol>
				</div>
			</div>
			<div class='row'>
				<div class='col-md-6 col-md-offset-3'>
					<div class="panel">
						<div class="panel-body">
							<div style='margin-left: 15px;'>
								<form class="form-horizontal" role="form">
									<div class="form-group hidden">
										<label for="userId" class="col-sm-3 control-label">id</label>
										<div class="col-sm-9">
											<input type="text" class="form-control" id='userId' readonly>
										</div>
									</div>
									<div class="form-group">
										<label for="userName" class="col-sm-3 control-label">登录名称</label>
										<div class="col-sm-9">
											<input type="text" class="form-control" id='userName' readonly>
										</div>
									</div>
									<div class="form-group">
										<label for="realName" class="col-sm-3 control-label">用户姓名</label>
										<div class="col-sm-9">
											<input type="text" class="form-control" id='realName' required maxlength="50">
										</div>
									</div>
									<div class="form-group">
										<label for="mobile" class="col-sm-3 control-label">手机号</label>
										<div class="col-sm-9">
											<input type="text" class="form-control" id='mobile' required pattern="^(13[0-9]|15[0|3|6|7|8|9]|18[0-9])\d{8}$" maxlength="20">
										</div>
									</div>
									<div class="form-group">
										<label for="email" class="col-sm-3 control-label">邮箱地址</label>
										<div class="col-sm-9">
											<input type="email" class="form-control" id='email' required  pattern="^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$">
										</div>
									</div>
									
									<div class="form-group">
										<div class="col-sm-offset-3 col-sm-9">
											<div class="row">
												<div class='col-sm-6'>
													<button id="showPasswordModel" type="button" class="btn btn-default btn btn-primary" style='width: 100%;' data-target="#setPasswordModal" data-toggle="modal" >
														<span class="glyphicon glyphicon-edit"></span> 修改密码
													</button>
												</div>
												<div class='col-sm-6'>
													<button id="modifyUserButton" type="button" class="btn btn-default btn btn-danger" style='width: 100%;'>
														<span class="glyphicon glyphicon-edit"></span> 保存
													</button>
												</div>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 修改密码 start -->
		<div class="modal fade" id="setPasswordModal" tabindex="-1" role="dialog" aria-labelledby="webModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title" id="webModalLabel">
							<span class='glyphicon glyphicon-user'></span>
							修改密码<span id='accountLabel'></span>
						</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" role="form">
							
							<div class="form-group">
								<label for="inputEmail" class="col-sm-3 control-label">当前密码</label>
								<div class="col-sm-9">
									<input type="password" class="form-control" id='inputOldPassword' required autocomplete="off">
								</div>
							</div>
							<div class="form-group">
								<label for="inputEmail" class="col-sm-3 control-label">新密码</label>
								<div class="col-sm-9">
									<input type="password" class="form-control" id='inputNewPassword' required>
								</div>
							</div>
							<div class="form-group">
								<label for="inputEmail" class="col-sm-3 control-label">确认新密码</label>
								<div class="col-sm-9">
									<input type="password" class="form-control" id='inputNewPasswordAgain' required>
								</div>
							</div>
							
						  <div class="form-group">
						    <label for="newPassword" class="col-sm-3 control-label">密码规则</label>
						    <div class="col-sm-9">
								<ul>
									<li>密码长度不得少于6个字符</li>
								</ul>
						    </div>
						  </div>
							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-9">
									<div class="row">
										<div class='col-sm-offset-6 col-sm-6'>
											<button id="setPasswordButton" type="button" class="btn btn-default btn btn-danger" style='width: 100%;'>
												<span class="glyphicon glyphicon-edit"></span> 确定
											</button>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- 重置密码 end -->
		<myScript>
			<script src="resources/js/SystemManage/profile.js"></script>
		</myScript>
	</body>
</html>