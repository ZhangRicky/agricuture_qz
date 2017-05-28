<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="shortcut icon" href="resources/images/favicon.ico" type="image/x-icon" />
   
    <!--bootstrap style-->
    <link rel="stylesheet" href="resources/plugins/bootstrap-3.3.2-dist/css/bootstrap.css">
    
    <!--kendoui style-->
    <link rel="stylesheet" href="resources/plugins/kendoui2015.1.318/styles/kendo.bootstrap.min.css">
    <link rel="stylesheet" href="resources/plugins/kendoui2015.1.318/styles/kendo.common-bootstrap.min.css">
    <link rel="stylesheet" href="resources/plugins/kendoui2015.1.318/styles/kendo.dataviz.bootstrap.min.css">
    <link rel="stylesheet" href="resources/plugins/kendoui2015.1.318/styles/kendo.silver.min.css">
    <link rel="stylesheet" href="resources/plugins/kendoui2015.1.318/styles/kendo.rtl.min.css">
    <!-- <link rel="stylesheet" href="resources/plugins/kendoui2015.1.318/styles/kendo.common-material.min.css">
    <link rel="stylesheet" href="resources/plugins/kendoui2015.1.318/styles/kendo.material.min.css"> -->
    
    <!--button style-->
    <link rel="stylesheet" href="resources/plugins/button/buttons.css" />
    
    <!--easing-->
    <link rel="stylesheet" href="resources/plugins/cplugin/cStyle.css" />
    
    <!--font-awesome-->
    <link rel="stylesheet" href="resources/plugins/font-awesome-4.1.0/css/font-awesome.css">
    
    <!--自定义 style-->
    <link rel="stylesheet" href="resources/css/common.css" />
    
    <sitemesh:write property='myCSS' />
    <style>
		<sitemesh:write property='myStyle' />
	</style>
    <title>清镇市农业（扶贫）项目监管系统</title>
</head>
<body class="mainBody" >
	<%@ include file="header.jsp" %>
	<!-- <div style="min-height:950px;"> -->
		<sitemesh:write property='body'/>
	<!-- </div> -->
	<!-- <div id='subNav' style="margin-top:15px">
		<footer class="text-center">Copyright © 2016 北京诚汇科技有限公司 版权所有</footer>
	</div> -->
	
	<!-- <footer class="text-center mainFooter2 footer">Copyright © 2016 北京诚汇科技有限公司 版权所有</footer> -->
	
    <div class="footer">
        <p>Copyright @ 2016 北京诚汇科技公司 版权所有</p>
    </div>
    <!--jquery js-->
    <script src="resources/plugins/jquery/jquery-1.11.1.min.js"></script>
    <script src="resources/plugins/kendoui2015.1.318/js/jquery.min.js"></script>
    
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
    
    <!--message js-->
    <script type="text/javascript" src="resources/plugins/messager/messager.js"></script>
    
    <!--easing-->
	<script src="resources/plugins/cplugin/jquery.easing.1.3.js"></script>
    <script type="text/javascript" src="resources/plugins/cplugin/cjs.js"></script>
    
    <!--自定义 公共 js-->
    <script type="text/javascript" src="resources/js/common.js"></script>
    
    <sitemesh:write property='myScript'/>
</body>
</html>