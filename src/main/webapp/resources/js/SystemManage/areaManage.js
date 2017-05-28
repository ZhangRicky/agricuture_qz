$(function() {
	
	var  lock = false;
	// 列表
	var areaObj = {

		areaGrid : undefined,

		init : function() {

			this.areaGrid = $("#areaList").kendoTreeList(
					{
						dataSource: new kendo.data.TreeListDataSource({
							transport : {
								read : {
									cache: false,
									type : "get",
									url : "area/search",
									dataType : "json",
									contentType : "application/json;charset=UTF-8"
								},
								parameterMap : function(data, type) {
									return {
										areaName : data.filter.filters[0].value
									};
								}
							},
							batch: true,
							schema: {
								model: {
									id: "id",
									fields: {
										id: { type: "number", editable: false, nullable: false },
										parentId: { field: "parentCode", nullable: true },
										areaName: { type: "string" }
									},
									expanded: true
								}
							},
							serverFiltering : true,
							filter : {
								field : 'areaName',
								value : ''
							}
						}),
						height: 540,
						toolbar: kendo.template($("#template").html()),
						columns: [
						    { field: "areaName", title: "地区名称"},
						    { 
						    	field: "areaLevel", 
						    	title: "地区类型",
						    	template : function(dataItem) {
						    		if(dataItem.areaLevel == 1){
						    			return "村";
						    		}else if(dataItem.areaLevel == 2){
						    			return "镇";
						    		}else if(dataItem.areaLevel == 3){
						    			return "市";
						    		}
						    	}
						    },
						    {
						    	title : "操作",
						    	width : 67,
						    	template: kendo.template($("#area_update_delete").html())
						    }],
						    dataBound : function() {
						    	areaObj.editAreaClick();
						    	areaObj.deleteAreaClick();
							}
					
					}).data("kendoTreeList");
			
			this.addAreaClick();
		},

		// 添加地区按钮，显示弹窗
		addAreaClick : function() {

			$("#addBtn").on("click", function() {
				lock = false;
				var dataItem = {
					id : "",
					areaLevel : "",
					areaCode : "",
					areaName : "",
					parentCode : ""
				};

				areaInfoWindow.obj.setOptions({
					"title" : "添加地区"
				});

				areaInfoWindow.initContent(dataItem);
				$("#parentCode").attr("disabled", "");
			});
		},

		
		// 【编辑/添加】 - 保存 按钮
		saveAreaClick : function() {
			$("#saveArea").on("click", function() {
			
				if(!areaInfoWindow.validator.validate()){
					return;
				}
//				if(lock == true){
//					return;
//				}
//				lock = true;
				var area = {};
				area.id = $("#id").val();
				area.areaLevel = $("#areaLevel").val();
				area.areaCode = $("#areaCode").val();
				area.areaName = $("#areaName").val();
				area.parentCode = $("#parentCode").val();
				if($("#areaCode").val().length > 12){
					alert("地区编码必须小于12位,请输入正确的编码！"); 	
					return;
				}
				
				if($("#areaName").val().length < 3 || $("#areaName").val().length > 10){
					alert("地区名称在3位到10位之间，请输入正确的地区名称");
					return;
				}
				
				url = (area.id == null || area.id == "")?"area/add":"area/update";
				$.ajax({
					cache:false,
					type : "post",
					url : url,
					dataType : "json",
					contentType : "application/json;charset=UTF-8",
					data : JSON.stringify(area),
					success : function(data, textStatus, jqXHR) {
						if(data.resultCode == -1){
							infoTip({content: data.message, color:"#FF0000"});
						}else if(data.resultCode == -2){
							infoTip({content: data.message, color:"#FF0000"});
						}else{
							infoTip({content: data.message, color:"#D58512"});
							areaObj.areaGrid.dataSource.read();
						}
						//lock = false;
					},
					error : function(data, textStatus, jqXHR) {
						infoTip({
							content : data.message,
							color : "#FF0000"
						});
						//lock = false;
					}

				});
				areaInfoWindow.obj.close();
			});
		},

		// 【编辑/添加】 - 取消 按钮
		cancelAreaClick : function() {
			$("#cancelArea").on("click", function() {
				areaInfoWindow.obj.close();
			});
		},

		// 编辑Area
		editAreaClick : function() {

			$(".editDepartment").on("click", function() {
				lock = false;
				var dataItem = areaObj.areaGrid.dataItem($(this).closest("tr"));
				dataItem.parentCode = dataItem.parentId;
				
				areaInfoWindow.obj.setOptions({
					"title" : "修改地区"
				});

				
				areaInfoWindow.initContent(dataItem);
				//$("#parentCode").data("kendoDropDownList").readonly();//使所属地区不能进行修改
			});
		},

		// 删除Area
		deleteAreaClick : function() {
			$(".deleteDepartment").off("click");
			$(".deleteDepartment").on("click", function(e) {
				var dataItem = areaObj.areaGrid.dataItem($(e.target).closest("tr"));
				if(lock == true){
					return;
				}
				lock = true;
				$.messager.confirm("删除地区", "确定删除地区 "+dataItem.areaName+" 以及它的全部子地区吗？", function() {
					lock = true;
					$.ajax({
						cache:false,
						type : "delete",
						url : "area/delete/"+dataItem.id,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						success : function(data, textStatus, jqXHR) {
								infoTip({content: data.message, color:"#D58512"});
								$(".deleteDepartment").off("click");
								areaObj.areaGrid.dataSource.read();	
								lock = false;
						},
						error : function(data, textStatus, jqXHR) {
							infoTip({
								content : "删除错误",
								color : "#FF0000"
							});
							lock = false;
						}
						
					});
				});
				lock = false;
			});
		}
	};

	// 【修改、添加】窗口
	var areaInfoWindow = {

		obj : undefined,

		template : undefined,
		
		validator : undefined,

		id : $("#areaInfoWindow"),

		initContent : function(dataItem) {
			// 填充弹窗内容
			areaInfoWindow.obj.content(areaInfoWindow.template(dataItem));
			
			// Area信息验证
			areaInfoWindow.validator = $("#areaInfoValidate").kendoValidator().data("kendoValidator");
			
			// 弹窗内，保存/取消按钮
			areaObj.saveAreaClick();
			areaObj.cancelAreaClick();
			
			areaInfoWindow.obj.center().open();
			//地区下拉列表，下来列表可选区（县）和社区
			$("#parentCode").kendoDropDownList({
			    dataSource: {
			    	transport: {
			    		read: {
			    			cache:false,
			    			type : "get",
		                    dataType: "json",
		                    url: "area/parentOrganizations",
		                    contentType : "application/json;charset=UTF-8"
		                }
			    	}
			    },
			    dataTextField: "areaName",
			    dataValueField: "id"
			});
			$("#parentCode").data("kendoDropDownList").value(dataItem.parentId);
		},

		init : function() {

			this.template = kendo.template($("#areaWindowTemplate").html());

			if (!areaInfoWindow.id.data("kendoWindow")) {
				areaInfoWindow.id.kendoWindow({
					width : "550px",
					actions : ["Close"],
					modal : true,
					title : "添加"
				});
			}
			areaInfoWindow.obj = areaInfoWindow.id.data("kendoWindow");
			
		}
	};

	$('#searchBtn').on('click', function() {
		var areaName = $("#inputSearch").val();	
		areaObj.areaGrid.dataSource.filter([{
			field : "areaName",
			value : areaName
		}]);
	});
	
	$('#resetBtn').on('click', function() {
		$("#inputSearch").val('');
		$('#searchBtn').trigger('click');
	});
	$('#inputSearch').on('keyup', function() {
		var areaName = $("#inputSearch").val();
		if(areaName == ''){
			areaObj.areaGrid.dataSource.filter([{
				field : "areaName",
					value : areaName
				}]);
		}

	});
	// 添加、修改 弹窗
	areaInfoWindow.init();
	// 列表
	areaObj.init();

});
