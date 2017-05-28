$(function() {

	var flowGridObj = {}, flowInfoWindow = {}, showFlowDetailWindow = {}, editFlowNodesWindow, currentRow = {}, lock = false;
	var width=680;
    var height=450;
    
    var rolesDropDownList = new kendo.data.DataSource({
    	transport : {
			read : {
				cache:false,
				type : "get",
				url : "role/approveRoles",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			}
		}
    });
	
	// 流程列表
	flowGridObj = {
		flowGrid : {},
		init : function() {
			this.flowGrid = $("#flowList")
					.kendoGrid(
							{
								dataSource : {
									transport : {
										read : {
											cache:false,
											type : "get",
											url : "flow/search",
											dataType : "json",
											contentType : "application/json;charset=UTF-8"
										},
										parameterMap : function(data, type) {
											return {
												flowName : data.filter.filters[0].value,
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
										field : 'flowName',
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
								        	title : "流程ID"
								    	},
										{
											field : "flowName",
											title : "流程名称"
										},
										{
											field : "flowType",
											title : "流程类型",
											template: function(dataItem) {
												if(dataItem.flowType){
													return dataItem.flowType.typeNameCN;
												}else{
													return "";
												}
											}
										},
										{
											field : "isInUse",
											title : "状态",
											template: function(dataItem) {
												if(dataItem.isInUse){
													return "有效";
												}else{
													return "无效";
												}
											}
										},
										{
											field : "createUser",
											title : "创建者"
										},
										{
											field : "createDate",
											title : "创建时间"
										},
										{
											field : "updateUser",
											title : "修改者"
										},
										{
											field : "updateDate",
											title : "修改时间"
										},
										{
											field : "description",
											title : "描述"
										},
										{
											title : "流程节点",
											width : 97,
											template : kendo.template($("#modifyFlowNotes").html())
										},
										{
											title : "操作",
											width : 97,
											template : kendo.template($("#flow_update_delete_detail").html())
										}],
								dataBound : function() {
									flowGridObj.editFlowNodesClick();
									flowGridObj.editFlowClick();
									flowGridObj.deleteFlowClick();
									flowGridObj.viewFlowClick();
								},
								editable : "popup"
							}).data("kendoGrid");
			flowGridObj.addFlowClick();
			flowGridObj.saveFlowClick();
			flowGridObj.cancelFlowClick();
		},

		// 添加流程按钮，显示弹窗
		addFlowClick : function() {
			$("#addFlow").on(
							"click",
							function() {
								$("#flowId,#flowName,#description").val("");
								$("#flowType").data("kendoDropDownList").select(0);
								$("input:radio[name=isInUse]")[0].checked = true;
								flowInfoWindow.obj.title("添加流程");
								flowInfoWindow.obj.center().open();
							});
		},
		
		// 保存 【添加】/【编辑】流程
		saveFlowClick : function() {
			$("#saveFlow").on("click", function(e) {
				if(!flowInfoWindow.validator.validate()){
					return;
				}
				var flow = {}, url="";
				flow.id = $("#flowId").val();
				flow.flowName = $("#flowName").val();
				flow.flowTypeId = $("#flowType").val();
				flow.isInUse = ($("input:radio[name=isInUse]:checked").val() == 'true') ? true : false;
				flow.description = $("#description").val();
				
				url = (flow.id == null || flow.id == "")?"flow/add":"flow/update";
				$.ajax({
					cache:false,
					type : "post",
					url : url,
					dataType : "json",
					contentType : "application/json;charset=UTF-8",
					data : JSON.stringify(flow),
					success : function(data, textStatus, jqXHR) {
						if(data && data.resultCode == 0){
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
						flowGridObj.flowGrid.dataSource.read();
					},
					error : function(data, textStatus, jqXHR) {
						infoTip({
							content : "保存失败!",
							color : "#FF0000"
						});
					}
				});
				flowInfoWindow.obj.close();
			});
		},

		// 取消【添加】/【编辑】流程
		cancelFlowClick : function() {
			$("#cancelFlow").on("click", function() {
				flowInfoWindow.obj.close();
			});
		},
		
		// 编辑流程节点按钮，显示弹窗
		editFlowNodesClick : function() {
			$(".editFlowNodes").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = flowGridObj.flowGrid.dataItem(tr);
				currentRow = data;
				editFlowNodesWindow.initTreeList(data);
				editFlowNodesWindow.obj.title("修改流程节点 － " + data.flowName);
				editFlowNodesWindow.obj.center().open();
			});
		},

		// 编辑流程
		editFlowClick : function() {
			$(".editFlow").on("click",function(e) {
				var tr =  $(e.target).closest("tr");
				var data = flowGridObj.flowGrid.dataItem(tr);
				$("#flowId").val(data.id);
				$("#flowName").val(data.flowName);
				$("#flowType").data("kendoDropDownList").value(data.flowType.typeKey);
				$("#description").val(data.description);
				if(data.isInUse==true){
					$("input:radio[name=isInUse]")[0].checked = true;
				}
				else{
					$("input:radio[name=isInUse]")[1].checked = true;
				}
				
				flowInfoWindow.obj.title("编辑流程 - "+data.flowName);
				flowInfoWindow.validator.validate();
				flowInfoWindow.obj.center().open();
			});
		},

		// 删除流程
		deleteFlowClick : function() {
			$(".deleteFlow").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = flowGridObj.flowGrid.dataItem(tr);
				$.messager.confirm("删除流程", "确定删除流程 "+data.flowName+" ？", function() { 
					$.ajax({
						cache:false,
						type : "delete",
//						url : "flow/delete/"+data.id,
						url : "flow/delete/"+data.id, //set inUse to false
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						success : function(data, textStatus, jqXHR) {
							infoTip({
								content : data.message,
								color : "#D58512"
							});
							flowGridObj.flowGrid.dataSource.read();
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
	
		// 查看流程
		viewFlowClick : function() {
			$(".viewFlow").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = flowGridObj.flowGrid.dataItem(tr);
				currentRow = data;
				showFlowDetailWindow.obj.title("查看流程 - " + data.flowName);
				showFlowDetailWindow.obj.center().open();
			});
		}

	};
	// 流程节点窗口
	editFlowNodesWindow = {

		obj : undefined,

		id : $("#editFlowNodesWindow"),
		
		treeListObj: undefined,
		
		//生成绑定节点
		initTreeList: function(data){
			editFlowNodesWindow.obj.title("编辑流程节点  -  " + data.flowName);
			editFlowNodesWindow.treeListObj = $("#flowNodesList")
			.kendoGrid(
					{
						dataSource : {
							transport : {
								read : {
									type : "GET",
									url : "flow/view/" + data.id,
									dataType : "json",
									contentType : "application/json;charset=UTF-8"
								}
							}
						},
						toolbar : kendo.template($("#addFlowNodeTemplate").html()),
//						toolbar : ['create'],
						reorderable : true,
						resizable : true,
						editable : 'inline',
						batch: false,
						columns : [
						    	{
						        	field : "id",
						        	hidden : true
						    	},
								{
									field : "roles",
									title : "角色名称",
									template: function(dataItem){
										if(dataItem.roles && dataItem.roles.roleName){
											return dataItem.roles.roleName;
										}else{
											return "";
										}
									},
									editor: function(container, options){
										$('<input data-bind="value:' + options.field + '"/>')
										.appendTo(container)
										.kendoDropDownList({
											dataTextField: "roleName",
											dataValueField: "id",
											dataSource: rolesDropDownList,
											optionLabel: "选择角色"
										});
									}
								},
								{
									field : "indexs",
									title : "顺序",
									editor: function(container, options){
										$('<input data-bind="value:' + options.field + '"/>')
										.appendTo(container)
										.kendoNumericTextBox({
											min: 0,
											max: 100,
											step: 1,
											format: "0"
											
										});
									}
								},
//								{
//									title : "操作",
//									width : 67,
//									template : function(dataItem){
//										return "<button type='button' class='btn btn-success btn-xs' value='"+dataItem.id+"'>移除</button>"
//									}
//								},
								{ command: ["edit", "destroy"], title: "&nbsp;", width: "250px" }],
						dataBound : function() {
//							$("#flowNodesList button").on("click",function(btnEvent){
//								var flowNode_id = $(this).prop("value");
//								$.ajax({
//									cache:false,
//			  						url : "flow/deleteFlowNode/"+flowNode_id,
//			  						type : "DELETE",
//			  						dataType : "json",
//									success : function(data) {
//										editFlowNodesWindow.treeListObj.dataSource.read();
//										infoTip({content: data.message,color:"#6D5296"});
//									},
//									error : function(data) {
//										infoTip({content: data.message,color:"#FF0000"});
//									}
//								});
//							});
							editFlowNodesWindow.addFlowNodeClick();
						}
						,
						remove:function(e){
							var flowNode_id = e.model.id;
							if(flowNode_id){
								if(lock){
									return;
								}
								lock = true;
								$.ajax({
									cache:false,
			  						url : "flow/deleteFlowNode/"+flowNode_id,
			  						type : "DELETE",
			  						dataType : "json",
									success : function(data) {
										lock = false;
										editFlowNodesWindow.treeListObj.dataSource.read();
										infoTip({content: data.message,color:"#6D5296"});
									},
									error : function(data) {
										lock = false;
										infoTip({content: data.message,color:"#FF0000"});
									}
								});
							}
						}
						,
						save: function(e){
							if(lock){
								return;
							}
							lock = true;
							var param = {};
							param.id = e.model.id;
							param.indexs = e.model.indexs;
							param.roles = e.model.roles;
							$.ajax({
								cache:false,
								url : "flow/saveAndUpdate/flowNode",
								type : "post",
								dataType : "json",
								contentType : "application/json;charset=UTF-8",
								data: JSON.stringify(param),
								success : function(data) {
									lock = false;
									editFlowNodesWindow.treeListObj.dataSource.read();
									infoTip({content: data.message,color:"#6D5296"});
								},
								error : function(data) {
									lock = false;
									infoTip({content: data.message,color:"#FF0000"});
								}
							});
						}
					}).data("kendoGrid");
		},

		init : function() {
			if (!editFlowNodesWindow.id.data("kendoWindow")) {
				editFlowNodesWindow.id.kendoWindow({
					width : "700px",
					minHeight : "200px",
					actions : ["Close"],
					modal : true,
					title : "修改流程节点",
					close: function(e){
						editFlowNodesWindow.treeListObj.destroy();
					}
				});
			}
			editFlowNodesWindow.obj = editFlowNodesWindow.id.data("kendoWindow");
//			editFlowNodesWindow.saveFlowNodesClick();
//			editFlowNodesWindow.cancelFlowNodesClick();
//			editFlowNodesWindow.closeFlowNodesClick();
		},
		// 保存修改【编辑流程节点】
		saveFlowNodesClick : function() {
			$("#saveFlowNodes").on("click", function() {
//				editFlowNodesWindow.treeListObj.saveChanges();
				editFlowNodesWindow.treeListObj.saveRow();
			});
		},
		// 取消修改【编辑流程节点】
		cancelFlowNodesClick : function() {
			$("#cancelFlowNodes").on("click", function() {
				editFlowNodesWindow.treeListObj.cancelChanges();
			});
		},
		// 取消【编辑流程节点】
		closeFlowNodesClick : function() {
			$("#closeFlowNodes").on("click", function() {
				editFlowNodesWindow.obj.close();
			});
		},
		// 添加流程按钮，显示弹窗
		addFlowNodeClick : function() {
			$("#addFlowNode").on("click", function() {
//				editFlowNodesWindow.treeListObj.addRow();
//				reutrn;
				if(lock){
					return;
				}
				lock = true;
				$.ajax({
					cache:false,
						url : "flow/addFlowNode/"+currentRow.id,
						type : "post",
						dataType : "json",
					success : function(data) {
						lock = false;
						editFlowNodesWindow.treeListObj.dataSource.read();
						infoTip({content: data.message,color:"#6D5296"});
					},
					error : function(data) {
						lock = false;
						infoTip({content: data.message,color:"#FF0000"});
					}
				});
			});
		}
		
	};
	
	// 流程信息窗口
	flowInfoWindow = {
			
			obj : undefined,
			
			id : $("#flowInfoWindow"),
			
			validator : undefined,
			
			init : function() {
				if (!flowInfoWindow.id.data("kendoWindow")) {
					flowInfoWindow.id.kendoWindow({
						width : "700px",
						actions : ["Close"],
						modal : true,
						title : "添加流程"
					});
				}
				$("#flowType").kendoDropDownList();
				flowInfoWindow.obj = flowInfoWindow.id.data("kendoWindow");
				// 流程信息验证
				flowInfoWindow.validator = $("#flowInfoValidate").kendoValidator().data("kendoValidator");
			}
	};

	// 查看流程窗口
	showFlowDetailWindow = {
			
		obj : undefined,
		
		id : $("#showFlowDetailWindow"),
		
		validator : undefined,
		
		init : function() {
			if (!showFlowDetailWindow.id.data("kendoWindow")) {
				showFlowDetailWindow.id.kendoWindow({
					width : "700px",
					height: "500px",
					actions : ["Close"],
					modal : true,
					title : "查看流程",
					activate: function() {
						var redraw, g, renderer;
						g = new Graph();
						$.ajax({
							type : "get",
							url : "flow/view/"+currentRow.id,
							dataType : "json",
							contentType : "application/json;charset=UTF-8",
							success : function(data, textStatus, jqXHR) {
								var indexArr = [], nodeArr=[];
								//add start node and end node if data length is great than zero
								if(data.length>0){
									indexArr.push(0);
									g.addNode(0, {label: "开始"});
									indexArr.push(999999);
									g.addNode(999999, {label: "结束"});
								}
								for(var i = 0;i<data.length;i++){
									indexArr.push(data[i].indexs);
									g.addNode(data[i].indexs, {label: data[i].roles.roleName});
								}
								indexArr.sort(function(a,b){
									return a-b
								});
								
								for(var j=0,k=j+1; j<indexArr.length,k<indexArr.length; j++,k++){
									g.addEdge(indexArr[j], indexArr[k], { directed : true, label:"审核", "label-style" : { "font-size": 20 }} );
								}
								var layouter = new Graph.Layout.Ordered(g, g.nodes);
								renderer = new Graph.Renderer.Raphael('canvas', g, width, height);
							},
							error : function(data, textStatus, jqXHR) {
								infoTip({
									content : "查看流程出错",
									color : "#FF0000"
								});
							}

						});

					},
					close: function(){
						$("#canvas").empty();
					}
				});
			}
			showFlowDetailWindow.obj = showFlowDetailWindow.id.data("kendoWindow");
		}
	};
	
	$('#searchBtn').on('click', function() {
		var flowName = $("#inputSearch").val();
		flowGridObj.flowGrid.dataSource.filter([{
			field : "flowName",
			value : flowName
		}]);
	});

	$('#inputSearch').on('keyup', function() {
		var flowName = $("#inputSearch").val();
		flowGridObj.flowGrid.dataSource.filter([{
			field : "flowName",
			value : flowName
		}]);
	});

	flowInfoWindow.init();
	
	editFlowNodesWindow.init();

	flowGridObj.init();
	
	showFlowDetailWindow.init();
	
});

