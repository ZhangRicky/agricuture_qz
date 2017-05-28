﻿﻿$(function() {

	var userGridObj = {}, userInfoWindow = {};
	// 用户列表
	userGridObj = {
		userGrid : {},
		init : function() {
			this.userGrid = $("#userList")
					.kendoGrid(
							{
								dataSource : {
									transport : {
										read : {
											cache:false,
											type : "get",
											url : "user/search",
											dataType : "json",
											contentType : "application/json;charset=UTF-8"
										},
										parameterMap : function(data, type) {
											return {
												userName : data.filter.filters[0].value,
												page : data.skip,
												pageSize : data.pageSize
											};
										}
									},
									serverPaging : true,
									serverFiltering : true,
									pageSize : GLOBALPARAMS.length,
									schema : {
										total : "totalCount",
										data : "items"
									},
									filter : {
										field : 'userName',
										value : ''
									}
								},

								toolbar : kendo.template($("#template").html()),

								reorderable : true,

								resizable : true,

								sortable : true,

								columnMenu : true,

								pageable : {
									refresh: true
								},

								editable : "popup",

								columns : [
								    	{
								        	field : "id",
								        	hidden : true
								    	},
										{
											field : "realName",
											title : "用户名称"
										},
//										{
//											field : "deptLevel",
//											title : "所属机构",
//											template: function(dataItem) {
//												if(dataItem.deptLevel == null){
//													return "";
//												}else{
//													return dataItem.deptLevel.displayName;
//												}
//											}
//										},
										{
											field : "userName",
											title : "登陆账号"
										},
										{
											field : "mobile",
											title : "电话"
										},
										{
											field : "email",
											title : "邮箱"
										},
										{
											field : "isInUse",
											title : "是否有效",
											template: function(dataItem) {
												if(dataItem.isInUse == true){
													return "有效";
												}else{
													return "无效";
												}
											}
										},
										{
											field : "created",
											title : "创建时间"
										},
										{
											field : "description",
											title : "描述"
										},
										{
											title : "权限",
											width : 80,
											template : kendo.template($("#userArea_Role").html())
										},
										{
											title : "密码",
											width : 47,
//											template : "<a class='resetPW aLink' title='重置密码'>重置<a>"
											template : kendo.template($("#resetPasswordTemplate").html())
										},
										{
											title : "操作",
											width : 67,
											template : kendo.template($("#userUpdate_Delete").html())
										}],
								dataBound : function() {
									userGridObj.addUserClick();
									userGridObj.cancelUserClick();
									userGridObj.editUserClick();
									userGridObj.deleteUserClick();
									userGridObj.resetPWClick();
									userGridObj.bindingAreaClick();
									userGridObj.cancelBindingAreaClick();
									userGridObj.bindingRoleClick();
								},
								editable : "popup"
							}).data("kendoGrid");
		},

		
		
		// 添加用户按钮，显示弹窗
		addUserClick : function() {
			$("#addUser").on(
							"click",
							function() {
								$("#passwordWrap").show();
								$("#password").attr("required", true);
								$("#confirmpasswordWrap").show();
								$("#confirmpassword").attr("required", true);
								$("#userId,#realName,#userName,#password,#confirmpassword,#mobile,#email,#description").val("");
								//获取所属机构
//								var degree=$("#constant").kendoDropDownList({
//							        dataTextField: "displayName",
//							        dataValueField: "id",
//							        dataSource: {
//							            transport: {
//							                read: {
//							                	cache:false,
//							                    dataType: "json",
//							                    url: "user/getconstantsList"
//							                }
//							            }
//							        } 
//							    });
//								$("#constant").data("kendoDropDownList").select();
								
								userInfoWindow.obj.title("添加用户");
								userInfoWindow.obj.center().open();
							});
		},

		// 保存 【添加】/【编辑】用户
		saveUserClick : function() {
			$("#saveUser").on("click", function(e) {
				if(!userInfoWindow.validator.validate()){
					return;
				}
				
				
				if(!checkPassword()){
					infoTip({
						content : "两次密码不一致!",
						color : "#FF0000"
					});
					return;
				}
				

				var user = {}, url="";
			//	user.deptLevel = {};
				user.id = $("#userId").val();
				user.realName = $("#realName").val();
			//	user.deptLevel.id = $("#constant").val();
				user.userName = $("#userName").val();
				user.plainPassword = $("#password").val();
				user.mobile = $("#mobile").val();
				user.email = $("#email").val();
				user.isInUse = ($("input:radio[name=isInUse]:checked").val() == 'true') ? true : false;
				user.description = $("#description").val();
				//密码验证
				if((user.id == null || user.id == "") && $("#password").val().length<6){  
					alert("密码长度设置在6位以上,请输入正确的密码");
					return;  
					
				}
			   //用户姓名验证
				if($("#realName").val().length<2){
					alert("请输入两个到十五个之间的用户姓名");
					return;
				}
				url = (user.id == null || user.id == "")?"user/add":"user/update";
				$.ajax({
					cache:false,
					type : "post",
					url : url,
					dataType : "json",
					contentType : "application/json;charset=UTF-8",
					data : JSON.stringify(user),
					success : function(data, textStatus, jqXHR) {
						if(data && data.title == '0'){
							infoTip({
								content : data.message,
								color : "#D58512"
							});
						}else{
							infoTip({
								content : data.message,
								color : "#FF0000"
							});
						}
						userGridObj.userGrid.dataSource.read();
					},
					error : function(data, textStatus, jqXHR) {
						infoTip({
							content : "保存失败!",
							color : "#FF0000"
						});
					}
				});
				userInfoWindow.obj.close();
			});
		},

		// 取消【添加】/【编辑】用户
		cancelUserClick : function() {
			$("#cancelUser").on("click", function() {
				userInfoWindow.obj.close();
			});
		},

		// 编辑用户
		editUserClick : function() {
			$(".editUser").on("click",function(e) {
				$("#passwordWrap").hide();
				$("#password").attr("required", false);
				$("#confirmpasswordWrap").hide();
				$("#confirmpassword").attr("required", false);
				var tr =  $(e.target).closest("tr");
				var data = userGridObj.userGrid.dataItem(tr);
				$("#userId").val(data.id);
				$("#realName").val(data.realName);
				$("#userName").val(data.userName);
				$("#mobile").val(data.mobile);
				$("#email").val(data.email);
				$("#description").val(data.description);
				if(data.isInUse==true){
					$("input:radio[name=isInUse]")[0].checked = true;
				}
				else{
					$("input:radio[name=isInUse]")[1].checked = true;
				}
				//获取所属机构
//				var degreeList=$("#constant").kendoDropDownList({
//			        dataTextField: "displayName",
//			        dataValueField: "id",
//			        dataSource: {
//			            transport: {
//			                read: {
//			                	cache:false,
//			                    dataType: "json",
//			                    url: "user/getconstantsList"
//			                }
//			            }
//			        }
//			    });
//				$("#constant").data("kendoDropDownList").select();
//				var deptLevel = $("#constant").data("kendoDropDownList");
//				if (data.deptLevel != null){
//					deptLevel.value(data.deptLevel.id);
//				}
				userInfoWindow.obj.title("编辑用户 - "+data.realName);
				userInfoWindow.validator.validate();
				userInfoWindow.obj.center().open();
			});
		},

		// 删除用户
		deleteUserClick : function() {
			$(".deleteUser").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = userGridObj.userGrid.dataItem(tr);
				$.messager.confirm("删除用户", "确定删除用户 "+data.realName+" ？", function() { 
					$.ajax({
						cache:false,
						type : "delete",
//						url : "user/delete/"+data.id,
						url : "user/setInUseFalse/"+data.id, //set inUse to false
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						success : function(data, textStatus, jqXHR) {
							infoTip({
								content : data.message,
								color : "#D58512"
							});
							userGridObj.userGrid.dataSource.read();
						},
						error : function(data, textStatus, jqXHR) {
							infoTip({
								content : data.message,
								color : "#FF0000"
							});
						}

					});
			      });
			});
		},

		// 重置密码
		resetPWClick : function() {
			$(".resetPW").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = userGridObj.userGrid.dataItem(tr);
				$.messager.confirm("重置密码", "确定重置用户 "+data.realName+" 的密码吗？", function() {
					$.ajax({
						cache:false,
						type : "put",
						url : "user/" + data.id +"/resetpassword",
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
//						data : JSON.stringify(user),
						success : function(data, textStatus, jqXHR) {
							infoTip({
								content : "重置密码成功!",
								color : "#D58512"
							});
							userGridObj.userGrid.dataSource.read();
						},
						error : function(data, textStatus, jqXHR) {
							infoTip({
								content : "重置密码失败!",
								color : "#FF0000"
							});
						}
					});
				});
			});
		},
		//地区权限
		bindingAreaClick: function() {
			$(".bindingArea").on("click", function(e) {
				bindingDepartmentWindow.obj.open();
				var tr =  $(e.target).closest("tr");
				var data = userGridObj.userGrid.dataItem(tr);
				bindingDepartmentWindow.initTreeList(data);
			});
		},
		
		//确定绑定地区
		saveBindingAreaClick: function(){
			$("#saveBindingArea").on("click",function(){
				bindingDepartmentWindow.obj.close();
				infoTip({content: "绑定地区成功！",color:"#6D5296"});
			});
			
		},
		//取消绑定地区
		cancelBindingAreaClick: function(){
			$("#cancelBindingArea").on("click",function(){
				bindingDepartmentWindow.obj.close();
			});
		},
		//绑定角色
		bindingRoleClick: function() {
			
			$(".bindingRole").on("click", function(e) {
				$("#cond-role").val("");
				bindingRoleWindow.obj.open();
				
				var tr =  $(e.target).closest("tr");
				var data = userGridObj.userGrid.dataItem(tr);
				bindingRoleWindow.initTreeList(data);
				
			});
		}
	};
	userGridObj.saveUserClick();
	// 用户信息窗口
	userInfoWindow = {

		obj : undefined,

		id : $("#userInfoWindow"),
		
		validator : undefined,

		init : function() {
			if (!userInfoWindow.id.data("kendoWindow")) {
				userInfoWindow.id.kendoWindow({
					width : "700px",
					actions : ["Close"],
					modal : true,
					title : "添加用户"
				});
			}
			userInfoWindow.obj = userInfoWindow.id.data("kendoWindow");
			// 用户信息验证
			userInfoWindow.validator = $("#userInfoValidate").kendoValidator().data("kendoValidator");
		}
	};
	
	$('#searchBtn').on('click', function() {
		var userName = $("#inputSearch").val();
		userGridObj.userGrid.dataSource.filter([{
			field : "userName",
			value : userName
		}]);
	});

	$('#inputSearch').on('keyup', function() {
		var userName = $("#inputSearch").val();
		userGridObj.userGrid.dataSource.filter([{
			field : "userName",
			value : userName
		}]);
	});

	userInfoWindow.init();

	userGridObj.init();
	var checkPassword = function(){
		var pwd = $("#password");
		var confirmPwd = $("#confirmpassword");
		if(pwd.val() != confirmPwd.val()){
			return false;
		}else{
			return true;
		}

		
	};
	
	
	
	//绑定 地区权限
	var bindingDepartmentWindow = {
		userId: undefined,
		obj: undefined,
		treeListObj: undefined,
		id: $("#bindingDepartmentWindow"),
		//生成绑定用户 树列表
		initTreeList: function(data){
			bindingDepartmentWindow.userId = data.id;
			bindingDepartmentWindow.obj.title("绑定地区权限  -  " + data.realName);
			$.ajax({
				cache:false,
				type : "get",
				url : "user/areaTree/"+data.id,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				success: function(data){
					var treeDataSource = new kendo.data.HierarchicalDataSource({
						data: data
					});
					bindingDepartmentWindow.treeListObj.setDataSource(treeDataSource);
				}
			});
		},
		
		init: function(){
			if (!bindingDepartmentWindow.id.data("kendoWindow")) {
				bindingDepartmentWindow.id.kendoWindow({
					position:{
						top: 10,
						left: ($(window).width()-400)/2
					},
					width: "400px",
					maxHeight: "550px",
					actions: ["Close"],
					modal:true,
					title: "绑定地区权限"
				});
			}
			bindingDepartmentWindow.obj = bindingDepartmentWindow.id.data("kendoWindow");
			bindingDepartmentWindow.treeListObj = $("#departmentList").kendoTreeView({
				checkboxes: {
					checkChildren: true
				},
				dataSource: []
			}).data("kendoTreeView");
			
			bindingDepartmentWindow.saveDepartmentClick();
			bindingDepartmentWindow.cancelDepartmentClick();
		},
		// 保存 【添加】/【编辑】用户
		saveDepartmentClick : function() {
			$("#saveDepartment").on("click", function(e) {
				var checkedNodes = [];
				onCheck(checkedNodes);
				if(checkedNodes.length == 0){
					infoTip({
						content : "保存地区不能为空！",
						color : "#FF0000"
					});
					return;
				}
				var params = {}, url="";
				params.userId = bindingDepartmentWindow.userId;
				params.deptIds = checkedNodes;
				
				$.ajax({
					cache:false,
					type : "post",
					url : "user/"+ params.userId+"/areas/" + params.deptIds,
					dataType : "json",
					contentType : "application/json;charset=UTF-8",
					success : function(data, textStatus, jqXHR) {
						if(data && data.resultCode == '0'){
							infoTip({
								content : data.message,
								color : "#D58512"
							});
						}else{
							infoTip({
								content : data.message,
								color : "#FF0000"
							});
						}
						bindingDepartmentWindow.obj.close();
					},
					error : function(data, textStatus, jqXHR) {
						infoTip({
							content : "保存失败!",
							color : "#FF0000"
						});
					}
				});
			});
		},
		cancelDepartmentClick: function(){
			$("#cancelDepartment").on("click", function(e) {
				bindingDepartmentWindow.obj.close();
			});
		}
		
	};
	
	//绑定角色窗口
	var bindingRoleWindow = {
			
		obj: undefined,
		
		treeListObj: undefined,
		
		id: $("#bindingRoleWindow"),
		
		//生成绑定角色
		initTreeList: function(data){
			bindingRoleWindow.obj.title("选择角色  -  " + data.realName);
			var roleName = $("#cond-role").val();
			if(roleName == '') roleName = null;
			bindingRoleWindow.treeListObj = $("#roleList")
			.kendoGrid(
					{
						dataSource : {
							transport : {
								read : {
									cache:false,
									type : "get",
									url : "user/" + data.id+"/"+roleName+"/role",
									dataType : "json",
									contentType : "application/json;charset=UTF-8"
								}
							}
						},
						reorderable : true,
						resizable : true,
//						height: 540,
						columns : [
						    	{
						        	field : "id",
						        	hidden : true
						    	},
								{
									field : "roleName",
									title : "角色名称"
								},
								{
									field : "roleType",
									title : "角色类型",
									template: "#if(roleType){#<span style='color:crimson;'>#:roleType.typeName# </span>#}else{# #:roleType# #}#"
								},
								{
									field : "resources",
									title : "角色权限",
									template: function(dataItem) {
										var resourcesNames = "";
										if(dataItem.resources){
											for(var i=0; i<dataItem.resources.length; i++){
												resourcesNames += dataItem.resources[i].resourceName + ',';
											}
										}
										if(resourcesNames.length>0){
											resourcesNames = resourcesNames.substring(0,resourcesNames.length-1);
										}
										return resourcesNames;
									}
								},
								{
									title : "操作",
									width : 67,
									template : function(dataItem) {
										if(dataItem.check=="in"){
								    		return "<button type='button' class='btn btn-success btn-xs' value='"+dataItem.id+"'>移除</button>";
										}else{
											return "<button type='button' class='btn btn-default btn-xs' value='"+dataItem.id+"'>加入</button>";
										}
									}
								}],
						dataBound : function() {
							$("#roleList button").on("click",function(btnEvent){
								var role_id = $(this).prop("value");
								var button = $(this);
								var addOrRemove = (button.hasClass("btn-success"))?"remove":"add";
								$.ajax({
									cache:false,
			  						url : "user/" +data.id+"/role/"+role_id+"/"+addOrRemove,
			  						type : "POST",
									success : function(data) {
										infoTip({content: data.message,color:"#6D5296"});
										if(button.hasClass("btn-success")){
											button.text("加入").removeClass("btn-success").addClass("btn-default");
										}else{
											button.text("移除").removeClass("btn-default").addClass("btn-success");
										}
									},
									error : function(data) {
										infoTip({content: data.message,color:"#FF0000"});
									}
								});
							});
						}
					}).data("kendoGrid");
		},
		
		init: function(){
			
			if (!this.id.data("kendoWindow")) {
				this.id.kendoWindow({
					position:{
						top: 80,
						left: ($(window).width()-600)/2
					},
					width: "600px",
					maxHeight: "550px",
					actions: ["Close"],
					modal:true,
					title: "绑定角色"
				});
			}
			bindingRoleWindow.obj = bindingRoleWindow.id.data("kendoWindow");
			
		},
		
		//确定绑定role
		savebindingRoleUsedClick: function(){
			$(".bindingRoleUsed").on("click",function(){
				infoTip({content: "移除用户成功！",color:"#6D5296"});
			});
			
		},
		savebindingRoleNotUsedClick: function(){
			$(".bindingRoleNotUsed").on("click",function(){
				infoTip({content: "绑定用户成功！",color:"#6D5296"});
			});
			
		}
	};
	
	$('#cond-role').on('keyup', function() {
		var userName = $("#cond-role").val();
		if(userName != ''){
			bindingRoleWindow.treeListObj.dataSource.filter([{
				field : "realName",
				value : userName
			}]);
		}else{
			bindingRoleWindow.treeListObj.dataSource.filter([]);
		}
	});
	
	
	// function that gathers IDs of checked nodes
    var checkedNodeIds = function(nodes, checkedNodes) {
        for (var i = 0; i < nodes.length; i++) {
//            if (nodes[i].checked && nodes[i].hasChildren == false) {
            if (nodes[i].checked) {
                checkedNodes.push(nodes[i].id);
            }

            if (nodes[i].hasChildren) {
                checkedNodeIds(nodes[i].children.view(), checkedNodes);
            }
        }
    };

    // show checked node IDs on datasource change
    var onCheck = function(checkedNodes) {
        var checkedNodes = checkedNodes, treeView = $("#departmentList").data("kendoTreeView");

        checkedNodeIds(treeView.dataSource.view(), checkedNodes);
    };
    
	bindingDepartmentWindow.init();
	
	bindingRoleWindow.init();
	
});

