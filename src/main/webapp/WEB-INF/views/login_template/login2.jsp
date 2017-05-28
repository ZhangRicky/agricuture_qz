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
<div style="text-align: center;">
	<div
		style="box-sizing: border-box; display: inline-block; width: auto; max-width: 480px; background-color: #FFFFFF; border: 2px solid #0361A8; border-radius: 5px; box-shadow: 0px 0px 8px #0361A8; margin: 50px auto auto;">
		<div
			style="background: #0361A8; border-radius: 5px 5px 0px 0px; padding: 15px;">
			<span
				style="font-family: verdana, arial; color: #D4D4D4; font-size: 1.00em; font-weight: bold;">Enter
				your login and password</span>
		</div>
		<div style="background:; padding: 15px" id="ap_style">
			<style type="text/css" scoped>
#ap_style td {
	text-align: left;
	font-family: verdana, arial;
	color: #064073;
	font-size: 1.00em;
}

#ap_style input {
	border: 1px solid #CCCCCC;
	border-radius: 5px;
	color: #666666;
	display: inline-block;
	font-size: 1.00em;
	padding: 5px;
}

#ap_style input[type="text"], input[type="password"] {
	width: 100%;
}

#ap_style input[type="button"], #ap_style input[type="reset"], #ap_style input[type="submit"]
	{
	height: auto;
	width: auto;
	cursor: pointer;
	box-shadow: 0px 0px 5px #0361A8;
	float: right;
	text-align: right;
	margin-top: 10px;
	margin-left: 7px;
}

#ap_style table.center {
	margin-left: auto;
	margin-right: auto;
}

#ap_style .error {
	font-family: verdana, arial;
	color: #D41313;
	font-size: 1.00em;
}
</style>
			<form method="post" action="https://www.authpro.com/auth/deluxe/"
				name="aform" target="_top">
				<input type="hidden" name="action" value="login"> <input
					type="hidden" name="hide" value="">
				<table class='center'>
					<tr>
						<td>Login:</td>
						<td><input type="text" name="login"></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type="password" name="password"></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type='checkbox' name='remember' value='1'>
							Remember me</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Enter"></td>
					</tr>
					<tr>
						<td colspan=2>&nbsp;</td>
					</tr>
					<tr>
						<td colspan=2>Lost your username or password? Find it <a
							href="http://www.authpro.com/auth/liberalnudism12/?action=lost">here</a>!
						</td>
					</tr>
					<tr>
						<td colspan=2>Not member yet? Click <a
							href="http://www.authpro.com/auth/liberalnudism12/?action=reg">here</a>
							to register.
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</div>

</body>
</html>