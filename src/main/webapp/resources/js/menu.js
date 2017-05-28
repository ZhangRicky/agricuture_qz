$(function(){
	
	//页面导航
	window.menu = $("#menu").kendoMenu({
		dataSource: [{
			text: "个人征信",
			items: [{
				text: "<a class='liLink' id='paraProcessing' ><i class='glyphicon glyphicon-baby-formula'></i> 征信规则管理</a>",
				encoded: false
			}, {
				text: "<a class='liLink' id='hardwareInforManage'><i class='glyphicon glyphicon-user'></i> 基础信息管理</a>",
				encoded: false
			}]
		}, {
			text: "系统管理",
			items: [{
				text: "<a class='liLink' href='userManage'><i class='glyphicon glyphicon-user'></i> 用户管理</a>",
				encoded: false
			}, {
				text: "<a class='liLink' href='roleManage'><i class='glyphicon glyphicon-lock'></i> 角色管理</a>",
				encoded: false
			}, {
				text: "<a class='liLink'  href='systemLog' ><i class='glyphicon glyphicon-user'></i> 系统日志查询</a>",
				encoded: false
			}, {
				text: "<a class='liLink' id='notificationGroupManage'><i class='glyphicon glyphicon-user'></i>地区管理</a>",
				encoded: false,
				cssClass:"k-state-disabled"
			}]
		}]
	}).data("kendoMenu");
	
});

