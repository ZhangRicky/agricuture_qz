﻿<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
<myCss>
<link rel="stylesheet" href="resources/css/home.css"/>
</myCss>
</head>
<body>
<!--     <div class="header">
        <p>清镇市农业局项目监管系统</p>
    </div> -->
    <shiro:hasPermission name="projectImport or projectsManage or statisticalChart">
	    <div class="navi">
	        <p>项目管理</p>
	    </div>
	    <div class="con">
	    	<%-- <shiro:hasPermission name="attachmentUpload">
	    		<a href='attachmentUpload' class="list1">
		           <div class="con-list tu1"></div>
		            <p>项目管理</p>
	    		</a>
		        
	        </shiro:hasPermission> --%>
	        <shiro:hasPermission name="projectImport">
		        <a href='projectImport' class="list1">
		            <div class="con-list tu2"></div>
		            <p>项目Excle导入</p>
		        </a>
	        </shiro:hasPermission>
	        <shiro:hasPermission name="projectsManage">
				<a href='projectsManage' class="list1">
					<div class="con-list tu1"></div>
					<p>项目管理</p>
				</a>
			</shiro:hasPermission>	
			
			<shiro:hasPermission name="statisticalChart">
		        <a href='statisticalChart' class="list1">
		            <div class="con-list tu3"></div>
		            <p>项目统计分析</p>
		        </a>
	        </shiro:hasPermission>
	        <div class="clear"></div>
	
	    </div>
    </shiro:hasPermission>
    <shiro:hasPermission name="userManage or roleManage or systemOperationLog or areaManage">
	    <div class="navi tab">
	        <p>系统管理</p>
	    </div>
	    <div class="con ">
	    	<shiro:hasPermission name="userManage">
		        <a href='userManage' class="list1">
		            <div class="con-list tu1 son1"></div>
		            <p>用户管理</p>
		        </a>
		    </shiro:hasPermission>
		    <shiro:hasPermission name="roleManage">
		        <a href='roleManage' class="list1">
		            <div class="con-list tu2 son2"></div>
		            <p>角色管理</p>
		        </a>
		   </shiro:hasPermission>
		   <shiro:hasPermission name="areaManage">
		        <a href='areaManage' class="list1">
		            <div class="con-list tu2 son4"></div>
		            <p>地区管理</p>
		        </a>
		   </shiro:hasPermission>
		   <shiro:hasPermission name="systemOperationLog">
		       <a href='systemOperationLog' class="list1">
		            <div class="con-list tu2 son3"></div>
		            <p>系统日志管理</p>
		        </a>
		    </shiro:hasPermission>
	        <div class="clear"></div>
	    </div>
    </shiro:hasPermission>

</body>

<%-- <body class="mainBody">
	<div id='menuWrap' class="container" style="margin-top: 45px;">
		<div class="row">
			<shiro:hasPermission name="projectImport or attachmentUpload or subProjectManage or financeManagement or projectSupervision or projectChecks">
			<div class="col-md-12">
				<h1 id='menu1' class="heading1">项目管理</h1>
				<div class="block1">
					<div class="row">
						 <shiro:hasPermission name="attachmentUpload">
							<div class="col-md-2 col-sm-4 text-center">
								<a href='attachmentUpload' class="button button-highlight button-box button-raised button-giant button-longshadow-right button-longshadow-expand"> <i
									class="glyphicon glyphicon-eye-open"></i>
								</a>
								<div>项目管理</div>
							</div>
						</shiro:hasPermission>
						<shiro:hasPermission name="projectImport">
							<div class="col-md-2 col-sm-4 text-center">
								<a href='projectImport' class="button button-royal button-box button-raised button-giant button-longshadow-left button-longshadow-expand"> <i
									class="glyphicon glyphicon-import"></i>
								</a>
								<div>项目Excel导入</div>
							</div>
						</shiro:hasPermission>
						
					</div>
				</div>
			</div>
			</shiro:hasPermission>
			<shiro:hasPermission name="userManage or roleManage or systemOperationLog or areaManage or flowManage or organizationManage">
			<div class="col-md-12">
				<h1 id='menu4' class="heading1">系统管理</h1>
				<div class="block1">
					<div class="row">
						<shiro:hasPermission name="userManage">
							<div class="col-md-2 col-sm-4 text-center">
								<a href="userManage" class="button button-royal button-box button-raised button-giant button-longshadow-right button-longshadow-expand"> <i
									class="glyphicon glyphicon-user"></i>
								</a>
								<div>用户管理</div>
							</div>
						</shiro:hasPermission>
						<shiro:hasPermission name="roleManage">
							<div class="col-md-2 col-sm-4 text-center">
								<a href="roleManage" class="button button-primary button-box button-raised button-giant button-longshadow-left button-longshadow-expand"> <i
									class="glyphicon glyphicon-lock"></i>
								</a>
								<div>角色管理</div>
							</div>
						</shiro:hasPermission>
						<shiro:hasPermission name="systemOperationLog">
							<div class="col-md-2 col-sm-4 text-center">
								<a href='systemOperationLog' class="button button-caution button-raised button-box button-giant button-longshadow-left button-longshadow-expand"> <i
									class="glyphicon glyphicon-duplicate"></i>
								</a>
								<div>系统日志查询</div>
							</div>
						</shiro:hasPermission>
						<shiro:hasPermission name="areaManage">
							<div class="col-md-2 col-sm-4 text-center opacity">
								<a href='areaManage' class="button button-raised button-box button-giant button-longshadow-right button-longshadow-expand"> <i
									class="glyphicon glyphicon-globe"></i>
								</a>
								<div>地区管理</div>
							</div>
						</shiro:hasPermission>
						
						<shiro:hasPermission name="flowManage">
							<div class="col-md-2 col-sm-4 text-center opacity">
								<a href='flowManage' class="button button-royal button-box button-giant button-longshadow-right button-longshadow-expand"> <i
									class="glyphicon glyphicon-retweet"></i>
								</a>
								<div>流程管理</div>
							</div>
						</shiro:hasPermission>
						
						<shiro:hasPermission name="organizationManage">
							<div class="col-md-2 col-sm-4 text-center opacity">
								<a href='organizationManage' class="button button-raised button-box button-giant button-longshadow-right button-longshadow-expand"> <i
									class="glyphicon glyphicon-globe"></i>
								</a>
								<div>组织机构管理</div>
							</div>
						</shiro:hasPermission>
						
						<shiro:hasPermission name="maintenanceManage">
							<div class="col-md-2 col-sm-4 text-center"> 
								<a href="maintenanceManage" 
								class="button button-primary button-box button-raised button-giant button-longshadow-left button-longshadow-expand"> <i
									class="glyphicon glyphicon-wrench"></i>
								</a>
								<div>系统参数管理</div>
							</div>
						</shiro:hasPermission>
					</div>
				</div>
			</div>
			</shiro:hasPermission>
		</div>
	</div>
	
	<myScript> <script type="text/javascript">
		$(function() {

			$("#menu").kendoMenu();

			$(window).on("scroll", function() {
				var scrollTop = $(window).scrollTop() + 10;
				var menu1Top = $("#menu1").offset().top;
				/* var menu2Top = $("#menu2").offset().top;
				var menu3Top = $("#menu3").offset().top; */
				var menu4Top = $("#menu4").offset().top;
				if (scrollTop <= menu1Top) {
					$("li[target = 'menu1']").addClass("k-state-active").siblings().removeClass("k-state-active");
				}/*  else if (scrollTop <= menu2Top) {
					$("li[target = 'menu2']").addClass("k-state-active").siblings().removeClass("k-state-active");
				}else if (scrollTop <= menu3Top) {
					$("li[target = 'menu3']").addClass("k-state-active").siblings().removeClass("k-state-active");
				} */ else if (scrollTop <= menu4Top) {
					$("li[target = 'menu4']").addClass("k-state-active").siblings().removeClass("k-state-active");
				}
			});

			$("#menu li").on("click", function() {
				var target = $(this).attr("target");
				$(this).addClass("k-state-active").siblings().removeClass("k-state-active");
				var top = $("#" + target).offset().top;
				$("html,body").animate({
					"scrollTop" : top - 75
				}, 300);
			});
		});
	</script> </myScript>
</body> --%>
</html>