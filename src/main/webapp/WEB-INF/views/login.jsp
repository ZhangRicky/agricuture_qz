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

<link rel="shortcut icon" href="resources/images/favicon.ico" type="image/x-icon" />

<link rel="stylesheet" href="resources/css/login.css"/>

<!--bootstrap style-->
<link rel="stylesheet" href="resources/plugins/bootstrap-3.3.2-dist/css/bootstrap.css" media="screen" />
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
    <script src="resources/plugins/jquery/html5shiv.min.js"></script>
    <script src="resources/plugins/jquery/respond.min.js"></script>
    <![endif]-->
    
<!--jquery style-->
<script src="resources/plugins/jquery/jquery-1.11.1.min.js"></script>
<!--bootstrap js-->
<script src="resources/plugins/bootstrap-3.3.2-dist/js/bootstrap.min.js"></script>


<script>
	<shiro:authenticated>
	$(document).ready(function() {
		window.location.href = "home";
	});
	</shiro:authenticated>
</script>

<title>清镇市农业（扶贫）项目监管系统</title>
</head>
<body>
	<div class="container">
	    <div class="most">
	        <img class="zhima" src="resources/images/login/message_bg.png"/>
	        <img class="login" src="resources/images/login/title.png">
	
	        <form action="login" method="POST" class="user">
	            <div class="form-group">
	                <lable>用户<span style="letter-spacing:14px">名</span></lable>
	                <input class="scui-input-md" name="username" required="required" placeholder="用户名" type="text" style="border: none"/>
	            </div>
	
	            <div class="form-group">
	                <lable><span style="letter-spacing:15px">密码</span></lable>
	                <input class="scui-input-md"  type="password" name="password" required="required"  placeholder="密码" value="" style="border: none"/>
	            </div>
	
	            <div class="form-group-sm">
	                <lable>记住密码</lable>
	                <input type="checkbox" value="" id="Remember"/>
	            </div>
	
	            <div class="form-group">
	                <lable class="mr60"></lable>
	                <input type="submit" class="mememe" value="登录" style="border: none"/>
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
</body>
</html>