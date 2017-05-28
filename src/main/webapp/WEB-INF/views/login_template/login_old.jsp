<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="shortcut icon" href="resources/images/favicon.ico" type="image/x-icon" />
    
    <!--bootstrap style-->
    <link rel="stylesheet" href="resources/plugins/bootstrap-3.3.2-dist/css/bootstrap.css" media="screen" />
    
    <!--kendoui style-->
    <link rel="stylesheet" href="resources/plugins/kendoui2015.1.318/styles/kendo.silver.min.css" media="screen" />
    <link rel="stylesheet" href="resources/plugins/kendoui2015.1.318/styles/kendo.common-bootstrap.min.css" media="screen" />
    
    <!--button style-->
    <link rel="stylesheet" href="resources/plugins/button/buttons.css" media="screen" />
    
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
    <script src="resources/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
    <script src="resources/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>
    
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
			<div class="col-md-offset-2">
				<img class="logo" src="resources/images/logo_img3.png" />
				<img class="logo" src="resources/images/logo_font.png" />
			</div>
			<div class="col-md-12 col-md-offset-0">
				<div class="loginWrap">
					<div class="row">
						<div class="col-md-offset-2 col-md-8">
							<div class="row">
								<div class="col-md-6 col-sm-6 tipImg">
									<img src="resources/images/loginImg.png"/>
								</div>
								<div class="col-md-6 loginContentWrap">
									<div class="loginContent">
										<div class="row">
											<div class="col-md-12">
												<div class="loginTitle">
													<h4>欢迎登陆</h4>
													<div class="dividerLine"></div>
												</div>
											</div>
										</div>
										<form id="loginForm" action="login" method="POST">
										<div class="row">
											<div class="col-md-2">
												<label class="loginLabel">账&nbsp;&nbsp;&nbsp;号：</label>
											</div>
											<div class="col-md-10">
												<input type="text" class="form-control button-pill" required="required" name="username"/>
											</div>
										</div><br />
										<div class="row">
											<div class="col-md-2">
												<label class="loginLabel">密&nbsp;&nbsp;&nbsp;码：</label>
											</div>
											<div class="col-md-10">
												<input type="password" class="form-control button-pill" required="required" name="password"/>
											</div>
										</div><br />
										<div class="row">
											<div class="col-md-6">
												<div class="checkbox">
													<label><input type="checkbox" name="rememberMe"/>记住密码</label>
												</div>
											</div>
											<div class="col-md-6 text-right">
												<!-- <a href="main.html" style="width: 100%;" class="button button-pill button-glow  button-border button-rounded button-plain" >
													登录
												</a> -->
												<button class='button button-pill button-glow  button-border button-rounded button-plain'>登录</button>
											</div>
										</div>
										<%
											String error = (String) request
													.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
											if (error != null) {
										%>
										<div class="alert alert-error controls input-large">
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
	</div>
</body>
</html>