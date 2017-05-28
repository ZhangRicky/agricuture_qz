$(function() {
	
	
	
	var  lock = false;
	// 列表
	var areaObj = {

		areaGrid : undefined,

		init : function() {

			this.areaGrid = $("#orgizationList").kendoTreeList(
					{
						dataSource: new kendo.data.TreeListDataSource({
							transport : {
								read : {
									cache: false,
									type : "get",
									url : "organization/search",
									dataType : "json",
									contentType : "application/json;charset=UTF-8"
								},
								parameterMap : function(data, type) {
									return {
										orgName : data.filter.filters[0].value
									};
								}
							},
							batch: true,
							schema: {
								model: {
									id: "orgId",
									fields: {
										orgId: { type: "number", editable: false, nullable: false },
										parentId: { field: "parentCode", nullable: true },
										orgName: { type: "string" }
									},
									expanded: true
								}
							},
							serverFiltering : true,
							filter : {
								field : 'orgName',
								value : ''
							}
						}),
						height: 540,
						toolbar: kendo.template($("#template").html()),
						columns: [
						    { field: "orgName", title: "组织机构名称"},
						    { 
						    	field: "orgLevel", 
						    	title: "组织机构类型",
						    	template : function(dataItem) {
						    		if(dataItem.orgLevel == 1){
						    			return "居委会";
						    		}else if(dataItem.orgLevel == 2){
						    			return "社区服务中心";
						    		}else if(dataItem.orgLevel == 3){
						    			return "区";
						    		}else if(dataItem.orgLevel == 4){
						    			return "市";
						    		}else if(dataItem.orgLevel == 5){
						    			return "省";
						    		}
						    	}
						    },
						    {
						    	title : "操作",
						    	width : 67,
						    	template: kendo.template($("#org_update_delete").html())
						    }],
						    dataBound : function() {
						    	areaObj.editAreaClick();
						    	areaObj.deleteAreaClick();
							}
					
					}).data("kendoTreeList");
			
			this.addAreaClick();
		},

		// 添加部门按钮，显示弹窗
		addAreaClick : function() {

			$("#addBtn").on("click", function() {
				//lock = false;
				var dataItem = {
					orgId : "",
					orgCode : "",
					orgName : "",
					parentCode : ""
				};

				organizationInfoWindow.obj.setOptions({
					"title" : "添加组织机构"
				});

				organizationInfoWindow.initContent(dataItem);
			});
		},

		//添加、编辑保存按钮
		saveAreaClick : function() {
			$("#saveOrganization").on("click", function() {
				if(!organizationInfoWindow.validator.validate()){
					return;
				}
//				if(lock == true){
//					return;
//				}
//				lock = true;
				var organization = {};
				organization.orgId = $("#orgId").val();
				organization.orgCode = $("#orgCode").val();
				organization.orgName = $("#orgName").val();
				organization.parentCode = $("#parentCode").val();
				if($("#orgCode").val().length > 12){
					alert("组织机构编码不能大于12位，请输入正确的组织机构编码！");
					return;
				}
				if($("#orgName").val().length < 3 || $("#orgName").val().length > 12){
					alert("组织机构名称3位到12位之间，请输入正确的组织机构名称！");
					return;
				}
				url = (organization.orgId == null || organization.orgId == "")?"organization/add":"organization/update";
				$.ajax({
					cache:false,
					type : "post",
					url : url,
					dataType : "json",
					contentType : "application/json;charset=UTF-8",
					data : JSON.stringify(organization),
					success : function(data, textStatus, jqXHR) {
						if(data.resultCode == -1){
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
					//	lock = false;
					}

				});
				organizationInfoWindow.obj.close();
			});
		},


		// 【编辑/添加】 - 取消 按钮
		cancelAreaClick : function() {
			$("#cancelOrganization").on("click", function() {
				organizationInfoWindow.obj.close();
			});
		},

		// 编辑组织机构
		editAreaClick : function() {

			$(".editOrganization").on("click", function() {
				//lock = false;
				var dataItem = areaObj.areaGrid.dataItem($(this).closest("tr"));
				dataItem.parentCode = dataItem.parentId;
				
				organizationInfoWindow.obj.setOptions({
					"title" : "修改组织机构"
				});

				organizationInfoWindow.initContent(dataItem);
			});
		},

		// 删除
		deleteAreaClick : function() {
			$(".deleteDepartment").on("click", function() {
				var dataItem = areaObj.areaGrid.dataItem($(this).closest("tr"));
				if(lock == true){
					return;
				}
				lock = true;
				$.messager.confirm("删除组织机构", "确定删除组织机构 "+dataItem.orgName+" 以及它的全部子机构吗？", function() {
					lock = true;
					$.ajax({
						cache:false,
						type : "delete", 
						url : "organization/delete/"+dataItem.orgId,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						success : function(data, textStatus, jqXHR) {
							if(data.resultCode == -1){
								infoTip({content: data.message, color:"#FF0000"});
							}else{
								infoTip({content: data.message, color:"#D58512"});
								$(".deleteDepartment").off("click");
								areaObj.areaGrid.dataSource.read();
							}
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
	var organizationInfoWindow = {

		obj : undefined,

		template : undefined,

		id : $("#organizationInfoWindow"),

		initContent : function(dataItem) {
			// 填充弹窗内容
			organizationInfoWindow.obj.content(organizationInfoWindow.template(dataItem));
			
			// Area信息验证
			organizationInfoWindow.validator = $("#areaInfoValidate").kendoValidator().data("kendoValidator");
			
			// 弹窗内，保存/取消按钮
			areaObj.saveAreaClick();
			areaObj.cancelAreaClick();
			
			organizationInfoWindow.obj.center().open();
			//部门下拉列表，下来列表可选区（县）和社区
			$("#parentCode").kendoDropDownList({
			    dataSource: {
			    	transport: {
			    		read: {
			    			cache: false,
			    			type : "get",
		                    dataType: "json",
		                    url: "organization/parentOrganizations",
		                    contentType : "application/json;charset=UTF-8"
		                }
			    	}
			    },
			    dataTextField: "orgName",
			    dataValueField: "orgId"
			});
			$("#parentCode").data("kendoDropDownList").value(dataItem.parentId);
		},

		init : function() {

			this.template = kendo.template($("#areaWindowTemplate").html());

			if (!organizationInfoWindow.id.data("kendoWindow")) {
				organizationInfoWindow.id.kendoWindow({
					width : "550px",
					actions : ["Close"],
					modal : true,
					title : "添加"
				});
			}
			organizationInfoWindow.obj = organizationInfoWindow.id.data("kendoWindow");
			
		}
	};

	$('#searchBtn').on('click', function() {
		var orgName = $("#inputSearch").val();
		areaObj.areaGrid.dataSource.filter([{
			field : "orgName",
			value : orgName
		}]);
	});
	
	$('#resetBtn').on('click', function() {
		$("#inputSearch").val('');
		$('#searchBtn').trigger('click');
	});
	$('#inputSearch').on('keyup', function() {
		var orgName = $("#inputSearch").val();
//		areaObj.areaGrid.dataSource.filter([{
//			field : "orgName",
//			value : orgName
//		}]);
	});
	areaObj.init();
	// 添加、修改 弹窗
	organizationInfoWindow.init();
	// 列表
	
});
