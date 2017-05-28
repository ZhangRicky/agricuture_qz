<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div class="cNavbar">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-4 col-sm-5 col-xs-5">
				<a href="home"> <img src="resources/images/intext.png" class="img-responsive logoImg" />
				</a>
			</div>
			<!-- <div class="col-md-7 col-sm-10 col-xs-10">
				<ul id="menu">
				</ul>
			</div>
			<div class="col-md-1 col-sm-2 col-xs-2">
				<div class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"> Admin <span class="caret"></span>
					</a>
					<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
						<li role="presentation"><a role="menuitem" tabindex="-1" href="#"> <span class="glyphicon glyphicon-edit"></span> 修改密码
						</a></li>
						<li role="presentation"><a role="menuitem" tabindex="-1" href="logout"> <span class="glyphicon glyphicon-off"></span> 退出
						</a></li>
					</ul>
				</div>
			</div> -->
			<!-- <div class="navbar-collapse collapse"> -->
			<div class="col-md-7 col-sm-10 col-xs-10">
			<ul class="nav navbar-nav">
				<li><a href="home">首页</a></li>
				<shiro:hasPermission name="projectCollect or projectImport or attachmentUpload or subProjectManage or projectsManage or statisticalChart">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">项目管理 <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<%-- <shiro:hasPermission name="projectCollect"><li class="divider"></li><li><a class='liLink' href='projectCollect'><i class='glyphicon glyphicon-plus'></i> 项目录入</a></li></shiro:hasPermission>
							<shiro:hasPermission name="attachmentUpload"><li class="divider"></li><li><a class='liLink' href='attachmentUpload'><i class='glyphicon glyphicon-eye-open'></i> 项目管理(old)</a></li></shiro:hasPermission>
							--%>
							<%--
							<shiro:hasPermission name="subProjectManage"><li class="divider"></li><li><a class='liLink' href='subProjectManage'><i class='glyphicon glyphicon-edit'></i> 子项目管理</a></li></shiro:hasPermission>
							<shiro:hasPermission name="financeManagement"><li class="divider"></li><li><a class='liLink' href='financeManagement'><i class='glyphicon glyphicon-usd'></i> 财务管理</a></li></shiro:hasPermission>
							 --%>
							<shiro:hasPermission name="projectImport"><li class="divider"></li><li><a class='liLink' href='projectImport'><i class='glyphicon glyphicon-import'></i> 项目Excel导入</a></li></shiro:hasPermission>
							<shiro:hasPermission name="projectsManage"><li class="divider"></li><li><a class='liLink' href='projectsManage'><i class='glyphicon glyphicon-eye-open'></i> 项目管理</a></li></shiro:hasPermission>
							<shiro:hasPermission name="statisticalChart"><li class="divider"></li><li><a class='liLink' href='statisticalChart'><i class='glyphicon glyphicon-stats'></i> 项目统计分析</a></li></shiro:hasPermission>
							
							<%-- 
							<shiro:hasPermission name="projectSupervision"><li class="divider"></li><li><a class='liLink' href='projectSupervision'><i class='glyphicon glyphicon-usd'></i> 项目监管</a></li></shiro:hasPermission>
							<shiro:hasPermission name="projectChecks"><li class="divider"></li><li><a class='liLink' href='projectChecks'><i class='glyphicon glyphicon-usd'></i> 项目验收</a></li></shiro:hasPermission>
							 --%>
						</ul>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="userManage or roleManage or systemOperationLog or areaManage or flowManage or organizationManage">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">系统管理 <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
						    <shiro:hasPermission name="userManage"><li><a class='liLink' href='userManage'><i class='glyphicon glyphicon-user'></i> 用户管理</a></li></shiro:hasPermission>
							<shiro:hasPermission name="roleManage"><li class="divider"></li><li><a class='liLink' href='roleManage'><i class='glyphicon glyphicon-lock'></i> 角色管理</a></li></shiro:hasPermission>
							<shiro:hasPermission name="systemOperationLog"><li class="divider"></li><li><a class='liLink'  href='systemOperationLog' ><i class='glyphicon glyphicon-duplicate'></i> 系统日志查询</a></li></shiro:hasPermission>
							<shiro:hasPermission name="areaManage"><li class="divider"></li><li><a class='liLink' href='areaManage'><i class='glyphicon glyphicon-globe'></i>地区管理</a></li></shiro:hasPermission>
							<shiro:hasPermission name="flowManage"><li class="divider"></li><li><a class='liLink' href='flowManage'><i class='glyphicon glyphicon-retweet'></i>流程管理</a></li></shiro:hasPermission>
							<shiro:hasPermission name="organizationManage"><li class="divider"></li><li><a class='liLink' href='organizationManage'><i class='glyphicon glyphicon-globe'></i>组织机构管理</a></li></shiro:hasPermission>
						</ul>
					</li>
				</shiro:hasPermission>
				
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><shiro:principal property="name"/>&nbsp;(&nbsp;<shiro:principal/>&nbsp;)&nbsp;&nbsp;<span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="profile">账号设置</a></li>
						<li class="divider"></li>
						<li><a href="logout">&nbsp;&nbsp;&nbsp;&nbsp;退出&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
					</ul>
				</li>
			</ul>
		</div>
		</div>
	</div>
</div>