$(function(){
	//角色列表
	var roleGridObj = {
		roleGrid: undefined,
		init: function(){
			this.roleGrid = $("#roleList").kendoGrid({
				dataSource: {
					transport : {
						read : {
							cache:false,
							type : "get",
							url : "role/search",
							dataType : "json",
							contentType : "application/json;charset=UTF-8"
						},
						parameterMap : function(data, type) {
							return {
								roleName : data.filter.filters[0].value,
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
						field : 'roleName',
						value : ''
					}
				},
		        toolbar: kendo.template($("#template").html()),
				reorderable: true,
				resizable: true,
				sortable: true,
				columnMenu: true,
				pageable: {
					refresh: true
				},
				columns: [{
					field: "roleName",
					title: "角色名称"
				}, {
					field: "roleType",
					title: "角色类型",
					template: "#if(roleType){#<span style='color:crimson;'>#:roleType.typeName# </span>#}else{# #:roleType# #}#"
				}, {
					field: "roleDesc",
					title: "角色描述"
				}, {
					title: "权限分配",
					width: 75,
					template: kendo.template($("#systemMenus").html())
				}, {
					title: "操作",
					width: 67,
					template: kendo.template($("#roleUpdate_Delete").html())
				}],
				dataBound: function(){
					roleGridObj.editRoleClick();
					roleGridObj.deleteRoleClick();
					roleGridObj.bindingSiteMenuClick();
					roleGridObj.cancelBindingSiteMenuClick();
				}
			}).data("kendoGrid");
		},
		
		//添加角色按钮，显示弹窗
		addRoleClick: function() {
			$("#addRole").on("click", function() {
				$("#roleId,#roleName,#roleDesc").val("");
				$("#roleType").attr("disabled", "");
				$("#roleType").data("kendoDropDownList").select(0);
//				$("#roleType").data("kendoDropDownList").readonly(false);
				
				roleInfoWindow.obj.setOptions({"title":"添加角色"});
				roleInfoWindow.obj.center().open();
			});
		},
		
		//保存 【添加】/【编辑】的角色
		saveRoleClick: function(){
			$("#saveRole").on("click",function(){
				if(!roleInfoWindow.validator.validate()){
					return;
				}
				var role = {}, url="";
				role.id = $("#roleId").val();
				role.roleName = $("#roleName").val();
				role.roleTypeId = $("#roleType").val();
				role.roleDesc = $("#roleDesc").val();
				
				url = (role.id == null || role.id == "")?"role/add":"role/update";
				$.ajax({
					cache:false,
					type : "post",
					url : url,
					dataType : "json",
					contentType : "application/json;charset=UTF-8",
					data : JSON.stringify(role),
					success : function(data, textStatus, jqXHR) {
						if(data.resultCode == -1){
							infoTip({content: data.message, color:"#FF0000"});
						}else{
							infoTip({content: data.message, color:"#D58512"});
							roleGridObj.roleGrid.dataSource.read();
						}
					},
					error : function(data, textStatus, jqXHR) {
						infoTip({
							content : data.message,
							color : "#FF0000"
						});
					}

				});
				roleInfoWindow.obj.close();
			});
		},
		
		//取消【添加】/【编辑】角色
		cancelRoleClick: function(){
			$("#cancelRole").on("click",function(){
				roleInfoWindow.obj.close();
			});
		},
	
		//编辑角色
		editRoleClick: function() {
			$(".editRole").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = roleGridObj.roleGrid.dataItem(tr);
				$("#roleType").data("kendoDropDownList").select(-1);//unselect
				
				$("#roleId").val(data.id);
				$("#roleName").val(data.roleName);
				$("#roleType").data("kendoDropDownList").value(data.roleType.id);
//				$("#roleType").data("kendoDropDownList").readonly();
//				$("#roleType").data("kendoDropDownList").enable(true);
				
				$("#roleDesc").val(data.roleDesc);
				
				roleInfoWindow.obj.setOptions({"title":"编辑角色 - "+data.roleName});
				roleInfoWindow.obj.center().open();
			});
		},
	
		//删除角色
		deleteRoleClick: function() {
			$(".deleteRole").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = roleGridObj.roleGrid.dataItem(tr);
				$.messager.confirm("删除角色", "确定删除角色"+data.roleName+"？", function() {
					$.ajax({
						cache:false,
						type : "delete",
						url : "role/delete/"+data.id,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						success : function(data, textStatus, jqXHR) {
							if(data.resultCode == -1){
								infoTip({
									content : data.message,
									color : "#FF0000"
								});
							}else{
								infoTip({
									content : "删除成功!",
									color : "#D58512"
								});
								roleGridObj.roleGrid.dataSource.read();
							}
						},
						error : function(data, textStatus, jqXHR) {
							infoTip({
								content : "删除失败，角色正在被其他用户使用和分配用系统权限，处理完这些原因后才能删除!",
								color : "#FF0000"
							});
						}

					});
				});
			});
		},
		
		//绑定系统功能
		bindingSiteMenuClick: function() {
			$(".bindingSiteMenu").on("click", function(e) {
				bindingSiteMenuWindow.obj.open();
				var tr =  $(e.target).closest("tr");
				var data = roleGridObj.roleGrid.dataItem(tr);
				bindingSiteMenuWindow.initTreeList(data);
			});
		},
		
		//确定绑定系统功能
		saveBindingSiteMenuClick: function(){
			$("#saveBindingSiteMenu").on("click",function(){
				bindingSiteMenuWindow.obj.close();
				infoTip({content: "绑定站点菜单成功！",color:"#6D5296"});
			});
		},
		//取消绑定系统功能
		cancelBindingSiteMenuClick: function(){
			$("#cancelBindingSiteMenu").on("click",function(){
				bindingSiteMenuWindow.obj.close();
			});
		}
	};
	
	//权限信息窗口
	var roleInfoWindow = {
		
		obj: undefined,
		
		id: $("#roleInfoWindow"),
		
		validator : undefined,
		
		init: function(){
			//权限信息验证
			this.validator = $("#roleInfoValidate").kendoValidator().data("kendoValidator");
			
			//权限下拉列表
			$("#roleType").kendoDropDownList({
			    dataSource: {
			    	transport: {
			    		read: {
			    			type : "get",
		                    dataType: "json",
		                    url: "role/roleTypes",
		                    contentType : "application/json;charset=UTF-8"
		                }
			    	}
			    },
			    dataTextField: "typeName",
			    dataValueField: "typeKey"
			});
			
			if (!this.id.data("kendoWindow")) {
				this.id.kendoWindow({
					width: "700px",
					actions: ["Close"],
					modal:true,
					title: "添加角色"
				});
			}
			roleInfoWindow.obj = roleInfoWindow.id.data("kendoWindow");
		}
	};
	
	//绑定系统功能窗口
	var bindingSiteMenuWindow = {
		
		obj: undefined,
		
		treeListObj: undefined,
		
		id: $("#bindingSiteMenuWindow"),
		
		//生成绑定系统功能列表
		initTreeList: function(data){
			bindingSiteMenuWindow.obj.title("绑定系统权限  -  " + data.roleName);
			$("#resourcesList").empty();
			bindingSiteMenuWindow.treeListObj = $("#resourcesList")
			.kendoTreeList(
					{
						dataSource : new kendo.data.TreeListDataSource({
							transport : {
								read : {
									cache:false,
									type : "get",
									url : "role/" + data.id+"/resource",
									dataType : "json",
									contentType : "application/json;charset=UTF-8"
								}
							},
							batch: true,
							schema: {
								model: {
									id: "id",
									fields: {
										id: { type: "number", editable: false, nullable: false },
										parentId: { field: "parentId", nullable: true },
										resourceName: { type: "string" }
									},
									expanded: true
								}
							}
						}),
						columns : [
						           {
						        	   field : "resourceName",
						        	   title : "菜单名称"
						           },
						           {
						        	   title : "操作",
						        	   width : 67,
						        	   template : function(dataItem) {
//						        		   if(dataItem.parentId){
							        		   if(dataItem.inUse){
							        			   return "<button type='button' class='btn btn-success btn-xs' value='"+dataItem.id+"'>移除</button>";
							        		   }else{
							        			   return "<button type='button' class='btn btn-default btn-xs' value='"+dataItem.id+"'>加入</button>";
							        		   }
//						        		   }
						        	   }
						           }],
						           dataBound : function() {
						        	   $("#resourcesList button").on("click",function(btnEvent){
						        		   var button = $(this);
						        		   var resource_id = $(this).prop("value");
						        		   $.ajax({
						        			   cache:false,
						        			   url : "role/" + data.id +"/resource/"+resource_id,
						        			   type : "POST",
						        			   success : function(data) {
						        				   infoTip({content: data.message,color:"#6D5296"});
						        				   if(button.hasClass("btn-success")){
						        					   button.removeClass("btn-success").addClass("btn-default").text("加入");
						        				   }else{
						        					   button.removeClass("btn-default").addClass("btn-success").text("移除");
						        				   }
						        			   },
						        			   fail : function(data) {
						        				   infoTip({content: data.message,color:"#FF0000"});
						        			   }
						        		   });
						        	   });
						           }
					}).data("kendoTreeList");
		},
		
		init: function(){
			
			if (!bindingSiteMenuWindow.id.data("kendoWindow")) {
				bindingSiteMenuWindow.id.kendoWindow({
					position:{
						top: 80,
						left: ($(window).width()-400)/2
					},
					width: "400px",
					height: "550px",
					actions: ["Close"],
					modal:true,
					title: "绑定系统功能"
				});
			}
			bindingSiteMenuWindow.obj = bindingSiteMenuWindow.id.data("kendoWindow");
		}
	};
	
	$('#searchBtn').on('click', function() {
		var roleName = $("#inputSearch").val();
		roleGridObj.roleGrid.dataSource.filter([{
			field : "roleName",
			value : roleName
		}]);
	});
	$('#resetBtn').on('click', function() {
		$("#inputSearch").val('');
		$('#searchBtn').trigger('click');
	});

	$('#inputSearch').on('keyup', function() {
		var roleName = $("#inputSearch").val();
		roleGridObj.roleGrid.dataSource.filter([{
			field : "roleName",
			value : roleName
		}]);
	});
	
	bindingSiteMenuWindow.init();
	
	roleInfoWindow.init();
	
	roleGridObj.init();
	
	roleGridObj.addRoleClick();
	
	roleGridObj.cancelRoleClick();
	
	roleGridObj.saveRoleClick();
	
	roleGridObj.saveBindingSiteMenuClick();
	
});
