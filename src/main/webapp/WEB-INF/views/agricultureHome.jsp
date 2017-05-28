﻿﻿<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
</head>
<body class="mainBody">
	<div id='menuWrap' class="container" style="margin-top: 45px;">
		<div class="row">
			<shiro:hasPermission name="projectCollect or projectImport or attachmentUpload or subProjectManage or financeManagement or projectSupervision or projectChecks or projectsManage">
			<div class="col-md-12">
				<h1 id='menu1' class="heading1" >项目管理</h1>
				<div class="block1">
					<div class="row">
						<!--  
						<shiro:hasPermission name="projectCollect">
							<div class="col-md-2 col-sm-4 text-center">
								<a href='projectCollect' class="button button-primary button-box button-raised button-giant button-longshadow-left button-longshadow-expand"> <i
									class="glyphicon glyphicon-plus"></i>
								</a>
								<div>项目录入</div>
							</div>
						</shiro:hasPermission>
						 -->
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
						
						<shiro:hasPermission name="projectsManage">
							<div class="col-md-2 col-sm-4 text-center">
								<a href='projectsManage' class="button button-jumbo button-box button-raised button-giant button-longshadow-right button-longshadow-expand"> <i
									class="glyphicon glyphicon-edit"></i>
								</a>
								<div>测试项目管理</div>
							</div>
						</shiro:hasPermission>	
						<!-- 
						<shiro:hasPermission name="financeManagement">
							<div class="col-md-2 col-sm-4 text-center">
								<a href='financeManagement' class="button button-highlight button-box button-raised button-giant button-longshadow-right button-longshadow-expand"> <i
									class="glyphicon glyphicon-usd"></i>
								</a>
								<div>财务管理</div>
							</div>
						</shiro:hasPermission>
						 -->
						<%-- <shiro:hasPermission name="projectSupervision">
							<div class="col-md-2 col-sm-4 text-center">
								<a href='projectSupervision' class="button button-highlight button-box button-raised button-giant button-longshadow-right button-longshadow-expand"> <i
									class="glyphicon glyphicon-usd"></i>
								</a>
								<div>项目监管</div>
							</div>
						</shiro:hasPermission>										
						<shiro:hasPermission name="projectChecks">
							<div class="col-md-2 col-sm-4 text-center">
								<a href='projectChecks' class="button button-highlight button-box button-raised button-giant button-longshadow-right button-longshadow-expand"> <i
									class="glyphicon glyphicon-usd"></i>
								</a>
								<div>项目验收</div>
							</div>
						</shiro:hasPermission> --%>										
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
</body>
</html>