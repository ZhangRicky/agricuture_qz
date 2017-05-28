<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page
	import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<!DOCTYPE html>
<html>
<head lang="zh-cn">
<meta charset="UTF-8">
<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="shortcut icon" href="resources/images/favicon.ico"
	type="image/x-icon" />

<!--bootstrap style-->
<link rel="stylesheet"
	href="resources/plugins/bootstrap-3.3.2-dist/css/bootstrap.css"
	media="screen" />

<!--kendoui style-->
<link rel="stylesheet"
	href="resources/plugins/kendoui2015.1.318/styles/kendo.silver.min.css"
	media="screen" />
<link rel="stylesheet"
	href="resources/plugins/kendoui2015.1.318/styles/kendo.common-bootstrap.min.css"
	media="screen" />

<!--button style-->
<link rel="stylesheet" href="resources/plugins/button/buttons.css"
	media="screen" />

<!--自定义 style-->
<link rel="stylesheet" href="resources/css/common.css" media="screen" />

<!--jquery style-->
<script src="resources/plugins/jquery/jquery-1.11.1.min.js"></script>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
    <script src="resources/plugins/jquery/html5shiv.min.js"></script>
    <script src="resources/plugins/jquery/respond.min.js"></script>
    <![endif]-->

<!--bootstrap js-->
<script src="resources/plugins/bootstrap-3.3.2-dist/js/bootstrap.min.js"></script>

<!--kendoui js-->
<script src="resources/plugins/kendoui2015.1.318/js/kendo.all.min.js"></script>
<script
	src="resources/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script
	src="resources/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>

<!--button js-->
<script type="text/javascript" src="resources/plugins/button/buttons.js"></script>
<script>
	<shiro:authenticated>
	$(document).ready(function() {
		window.location.href = "home";
	});
	</shiro:authenticated>
</script>

<title>清镇市农业局项目过程监管系统</title>
</head>
<body class="loginBody">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-offset-1">
				<img class="logo" src="resources/images/logo_img3.png" /> <img
					class="logo" src="resources/images/dltext.png" />
			</div>
			<div class="col-md-12 col-md-offset-0">
				<!-- <div class="loginWrap"> -->
				<div class="row">
					<div class="col-md-offset-1 col-md-8">
						<div class="row">
							<!-- <div class="col-md-6 col-sm-6 tipImg">
									<img src="resources/images/loginImg.png"/>
								</div> -->
							<div class="col-md-6 loginContentWrap">


								<div class="container">
									<div id="loginbox" style="margin-top: 50px;"
										class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
										<div class="panel panel-info">
											<div class="panel-heading">
												<div class="panel-title">欢迎登录</div>
											</div>

											<div style="padding-top: 40px" class="panel-body">

												<div style="display: none" id="login-alert"
													class="alert alert-danger col-sm-12"></div>

												<form id="loginForm"  action="login" method="POST" class="form-horizontal" role="form">
													<div style="margin-bottom: 40px" class="input-group">
														<span class="input-group-addon"><i
															class="glyphicon glyphicon-user"></i></span> <input
															id="login-username" type="text" class="form-control"
															name="username" value="" required="required" placeholder="用户名">
													</div>

													<div style="margin-bottom: 40px" class="input-group">
														<span class="input-group-addon"><i
															class="glyphicon glyphicon-lock"></i></span> 
														<input id="login-password" type="password" class="form-control"
															name="password" required="required"  placeholder="密码">
													</div>

													<div class="input-group">
															<label><input id="login-remember" type="checkbox" name="rememberMe" value="1">
																<font size = "2" color="black">记住密码</font>
															</label>
													</div>


													<div style="margin-top: 20px" class="form-group">
														<div class="col-sm-offset-5 col-sm-12 controls">
															<button class='btn btn-success'>登录</button>
														</div>
													</div>
													
													<%
														String error = (String) request
																.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
														if (error != null) {
													%>
													<div class="alert alert-error controls input-large" style="font-color: black">
														<font size = "2" color="black">
														<button class="close" data-dismiss="alert">×</button>
														<%
															if (error.contains("DisabledAccountException")) {
																out.print("用户不可用，请登录其他用户。");
															} else if (error.contains("ExpiredCredentialsException")) {
																out.print("密码已经过期，请联系<a href=mailto:administrator@cheng-hui.com>管理员</a>重置密码。");
															} else if (error.contains("UnknownAccountException")) {
																out.print("用户不存在。");
															} else if (error.contains("AuthenticationException")) {
																out.print("无权限，请联系<a href=mailto:administrator@cheng-hui.com>管理员</a>分配权限。");
															} else {
																out.print("密码错误，请联系<a href=mailto:administrator@cheng-hui.com>管理员</a>修改密码。");
																out.print("请重试。");//
															}
														%>
														</font>
													</div>
													<%
														}
													%>
												</form>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- </div> -->
			</div>
		</div>
	</div>
	</div>
</body>
</html>