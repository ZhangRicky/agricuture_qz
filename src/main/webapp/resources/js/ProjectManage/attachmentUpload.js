﻿﻿$(function() {
	var userGridObj = {},subProjectManageWindow = {},subProjectManageDetailWindow={},showSupervisionsWindow={},subProjectWindow = {}, userListGridObj = {}, userInfoWindow = {},deleteRemarkWindow = {},projectDetailWindow = {}, tooltip={},fileUploadObj={},subProjectManageDetailWindow={}, showChecksWindow = {}, labelStyle={},lock=false,remark="", isSupervisionsFirstOpen=true, isChecksFirstOpen=true;
	
	/*财务信息的变量值*/
	var fianceInfoObjWindow={},showDetailsList={},userdetailsWindow={};
	var fianceInfoShowObjWindow = {},subId = "";
	var baoZhangFinanceObjWindow = {};	//报账窗口
	var financeID = "";		//记录财务记录ID
	var financeSum =0;		//记录添加的报账金额的值
	var subProject_id; 
	var accountNum = ""; 
	var appropriationNum = "";
	
	// 项目列表
	userGridObj = {
		userGrid : {},
		init : function() {
			this.userGrid = $("#projectList")
					.kendoGrid(
							{
								dataSource : {
									transport : {
										read : {
											type : "get",
											cache : false,
											url : "attachmentUpload/search",
											dataType : "json",
											contentType : "application/json;charset=UTF-8"
										},
										parameterMap : function(data, type) {
											return {
												name : data.filter.filters[0].value,
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
										field : 'name',
										value : ''
									}
								},
								
								reorderable : true,

								resizable : true,

								sortable : true,

								columnMenu : true,

								pageable : {
									refresh: true,
									pageSizes: true
								},

								editable : "popup",

								columns : [
								    	{
								        	field : "id",
								        	hidden : true
								    	},
										{
								    		width: 100,
											field : "countyLevelCity",
											title : "县名称",
											template: function(dataItem) {
												if(dataItem.countyLevelCity != null || dataItem.countyLevelCity != ""){
													return dataItem.countyLevelCity;
												}else{
													return "";
												}
											}
										},
										{
											width: 100,
											field : "town",
											title : "乡镇",
											template: function(dataItem) {
												if(dataItem.town != null || dataItem.town != ""){
													return dataItem.town;
												}else{
													return "";
												}
											}
										},
										{
											width: 100,
											field : "village",
											title : "村",
											template: function(dataItem) {
												if(dataItem.village != null || dataItem.village != ""){
													return dataItem.village;
												}else{
													return "";
												}
											}
										},
										{
											width: 100,
											field : "projectNumber",
											title : "项目编号"
										},
										{	
											width : 120,
											field : "projectName",
											title : "项目名"
										},
										{
											width: 90,
											field : "subjectName",
											title : "专项名"
										},
										{
											width: 100,
											field : "fundYear",
											title : "资金年度",
											//template: "#if(personalInfo.censusRegisterProperty == 1){#<span style='color:crimson;'>农业 </span>#}else if(personalInfo.censusRegisterProperty == 2){#<span style='color:crimson;'>非农业 </span>#} else{# #:personalInfo.censusRegisterProperty# #}#"
										},
										{
											width : 90,
											field : "totalFund",
											title : "总资金"
											//template: "#if(personalInfo.domicilePlace){#<span class='tdOverFont' title='#:personalInfo.domicilePlace#'>#:personalInfo.domicilePlace#</span>#}else{#  #}#"
//											template: "<span class='tdOverFont' title='#:personalInfo.domicilePlace#'>#:personalInfo.domicilePlace#</span>"
										},{
											width: 110,
											field : "chargePerson",
											title : "项目负责人",
											template: function(dataItem) {
												if(dataItem.chargePerson != null || dataItem.chargePerson != ""){
													return dataItem.chargePerson;
												}else{
													return "";
												}
											}
										},{
											width: 100,
											field : "approveState",
											title : "审核状态"
										},{
											width: 120,
											field : "subProjectNumber",
											title : "子项目数(个)"
										},{
											title : "操作",
											width : 130,
											template : "<a class='fileUpload editIcon aLink' title='公示附件'>公示附件<a/>"+
											"&nbsp;&nbsp;&nbsp;&nbsp;<a class='editFileUpload editIcon aLink' title='查看详情'>查看详情<a/>"
										}],
								dataBound : function() {
//									userGridObj.approveUserClick();
									userGridObj.projectDetailClick();
									userGridObj.fileUploadClick();
									userGridObj.inputSearch();
									userGridObj.searchBtn();
									userGridObj.saveClick();
									userGridObj.doubleClick();
								},
								editable : "popup"
							}).data("kendoGrid");
		},

		
		//双击主项目行查询所有子项目
		doubleClick : function(){
			$("#projectList tr").on('dblclick', function (e) {
				var tr =  $(e.target).closest("tr");
				var data = userGridObj.userGrid.dataItem(tr);
				var project = data.id;
				subProjectWindow.obj.title("子项目列表"+"&nbsp;&nbsp;>&nbsp;&nbsp;"+data.projectName);
				subProjectWindow.obj.center().open();
				subProjectWindow.showSubProjectList(data);
			})
		},
		
		searchBtn : function() {
			$('#searchBtn').on('click', function() {
				var projectName = $("#inputSearch").val();
				$("#projectList").data("kendoGrid").dataSource.filter([{
					field : "name",
					value : projectName
				}]);
			});
		},
		
		inputSearch : function() {
			$('#inputSearch').on('keyup', function() {
				var projectName = $("#inputSearch").val();
				if(projectName == ''){
					$("#projectList").data("kendoGrid").dataSource.filter([{
						field : "name",
						value : projectName
					}]);
				}
			});
		},
		
		projectDetailClick: function() {
			$(".editFileUpload").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = userGridObj.userGrid.dataItem(tr);
				$("#referenceNumber").html(data.referenceNumber);
				$("#projectNumber").html(data.projectNumber);
				$("#fundYear").html(data.fundYear);
				$("#city").html(data.city);
				$("#countyLevelCity").html(data.countyLevelCity);
				$("#town").html(data.town);
				$("#village").html(data.village);
				$("#subjectName").html(data.subjectName);
				$("#fundType").html(data.fundType);
				$("#projectName").html(data.projectName);
				$("#approvalNumber").html(data.approvalNumber);
				$("#totalFund").html(data.totalFund);
//				$("#financeFund").html(data.financeFund);
//				$("#selfFinancing").html(data.selfFinancing);
//				$("#integrateFund").html(data.integrateFund);
//				$("#coveredFarmerNumber").html(data.coveredFarmerNumber);
//				$("#coveringNumber").html(data.coveringNumber);
				$("#povertyStrickenFarmerNumber").html(data.povertyStrickenFarmerNumber);
				$("#povertyStrickenPeopleNumber").html(data.povertyStrickenPeopleNumber);
				$("#scaleAndContent").html(data.scaleAndContent);
				$("#carryOutUnit").html(data.carryOutUnit);
				$("#chargePerson").html(data.chargePerson);
				$("#fundToCountry").html(data.fundToCountry);
				$("#approveState").html(data.approveState);
				$("#approvingDepartment").html(data.approvingDepartment);
				projectDetailWindow.obj.title("项目详情-"+data.projectName);
				projectDetailWindow.obj.center().open();
//				projectDetailWindow.showDetail(data);
			});
		},
		
		// 公示附件
		fileUploadClick : function() {
			$(".fileUpload").on("click", function(e) {
				$("#picture1").html("");
				var tr =  $(e.target).closest("tr");
				var data = userGridObj.userGrid.dataItem(tr);
				var	projectId = data.id;
				remark = data.remark;
				$("#projectId").val(data.id);
				$("#remark").val(data.remark);
				deleteRemarkWindow.obj.title("公示附件");
				deleteRemarkWindow.obj.center().open();	
				
				$.ajax({
					cache:false,
					type : "GET",
					url : "attachmentUpload/searchPicture/"+projectId,
					dataType : "json",
					contentType : "application/json;charset=UTF-8",
				//	data : JSON.stringify(projectId),
					success : function(data, textStatus, jqXHR) {
						 for (var index in data) {
							if(index != "remove"){
								 var div = document.getElementById("picture1");
									var img = document.createElement("img");
									img.setAttribute("id", "imgs");
									img.setAttribute("class","imgs1");
									img.width=130;
									img.height=100;
									img.src = data[index].fileName;
									img.pictureId = data[index].id;
									div.appendChild(img);
			//						mouseoverImg(img);
							}
				        }
						
					},
					error : function(data, textStatus, jqXHR) {
						infoTip({
							content : "读取图片错误",
							color : "#FF0000"
						});
					}
				});
			});
		},
		
		saveClick : function() {
			$("#saveRemark").off("click");
			$("#saveRemark").on("click", function(e) {
				var project = {},url = "";
				project.id=$("#projectId").val();
				project.remark=$("#remark").val();
				
				url = "project/update";
				$.ajax({
					cache:false,
					type : "POST",
					url : url,
					dataType : "json",
					contentType : "application/json;charset=UTF-8",
					data : JSON.stringify(project),
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
							$("#saveRemark").off("click");
							userGridObj.userGrid.dataSource.read();	
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

	};
	userGridObj.init();
	//子项目列表窗口
	subProjectWindow={
			projectId: undefined,
			subGrid:{},
			obj : undefined,
			id : $("#subProjectWindow"),
			validator : undefined,
			init : function() {
				if (!subProjectWindow.id.data("kendoWindow")) {
					subProjectWindow.id.kendoWindow({
						width : "100%",
						height:"600",
						actions : ["Close"],
						modal : true,
						title : "子项目列表"
					});
                }
				subProjectWindow.obj = subProjectWindow.id.data("kendoWindow");
			},
			//子项目列表
			showSubProjectList:function(data){
				var projectId = data.id;
				subProjectWindow.projectId = projectId;
				$("#pId").val(projectId);
				this.subGrid = $("#subProjectList")
				.kendoGrid(
						{
							dataSource : {
								transport : {
									read : {
										cache:false,
										type : "get",
										url : "subProjectManage/search/all/"+projectId,
										dataType : "json",
										contentType : "application/json;charset=UTF-8"
									},
									parameterMap : function(data, type) {
										return {
											xxnr : data.filter.filters[0].value,
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
									field : 'xxnr',
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
							    		field : "subProjectName",
							    		width : 115,
							    		title : "子项目名称"
							    	},
							    	{
							    		field : "subProjectNumber",
							    		width : 115,
							    		title : "子项目文号"
							    	},
									{
										field : "farmerName",
										width : 100,
										title : "农户名称"
									},
									{
										field : "totalCapital",
										width : 110,
										title : "总资金(万)"
									},
									{
										field : "shouldAccount",
										width : 110,
										title : "应报账(万)"
									},
									{
										field : "implementationUnit",
										width : 100,
										title : "实施单位"
									},
									{
										field : "projectScaleAndContent",
										width : 145,
										title : "建设规模及内容"
									},
									{
										field : "constructionMode",
										width : 100,
										title : "建设方式",
											template:function(data){
												if(data.constructionMode==0){
													return "先建后款";
												}else if (data.constructionMode==1){
													return "先款后建";
												}
											}
									},{	
											title : "报账比率",
											width : 90,
											template : function(data){
												if(data.financebiLv == "" || data.financebiLv == null){
													return "";
												}else if(data.financebiLv == 0){
													return "";
												}else if(data.shouldAccount == null){
													return "";
												}else{
													return (parseFloat(data.financebiLv) / parseFloat(data.shouldAccount)*100).toFixed(2)+"%";
												}
											}
									},
									{	
										field : "checkStatus",
										width : 100,
										title : "验收状态",
										template:function(data){
											if(data.checkStatus==0){
												return "未验收";
											}else if (data.checkStatus==1){
												return "验收通过";
											}else if (data.checkStatus==2){
												return "验收不通过";
											}
										}
									},{
										title : "监管记录",
										width : 90,
										template : kendo.template($("#showSupervisionsTemplate").html())
									},{
										title : "验收记录",
										width : 90,
										template : kendo.template($("#showChecksTemplate").html())
									},{
										title : "报账",
										width : 100,
										template : function(){
											return "<button class='showFiance editIcon aLink btn btn-xs btn-success'  style='margin-left:2px;'><i class='glyphicon glyphicon-plus-sign'></i>&nbsp;财务管理</button>" ;
										}
									},{
										title : "操作",
										width : 80,
										template : kendo.template($("#detailScan").html())
									}],
							dataBound : function() {
								subProjectWindow.getSupervisionsClick();
								subProjectWindow.getChecksClick();
								subProjectWindow.addSubProjectClick();
								subProjectWindow.updateSubProjectClick();
								subProjectWindow.deleteSubProjectClick();
								subProjectWindow.detailSubProjectClick();
								subProjectWindow.saveSubProjectClick();
								subProjectWindow.cancelUserClick();
								subProjectWindow.financeManagerClick();
							},
							editable : "popup"
						}).data("kendoGrid");
			},
			
			/*财务管理【报账】按钮-zhanggx*/
			financeManagerClick : function(){
				$(".showFiance").on("click",function(e){
					var tr = $(e.target).closest("tr");
					var pdata = subProjectWindow.subGrid.dataItem(tr);
					subId = pdata.id;	//传递子项目id
					userdetailsWindow.obj.center().open();
					userdetailsWindow.showDetailsList(pdata);		
				});
			},
			
			getSupervisionsClick:function(){
				$(".getSupervisions").on("click", function(e) {
					var tr =  $(e.target).closest("tr");
					var data = subProjectWindow.subGrid.dataItem(tr);
					showSupervisionsWindow.obj.title("查看项目监管记录 - " + data.project.projectName );
					showSupervisionsWindow.obj.center().open();
					showSupervisionsWindow.refreshSupervisionsList(data.id);
					if(isSupervisionsFirstOpen){
						isSupervisionsFirstOpen = false;
						$("#supervisionHorizontal").kendoSplitter({
		                    panes: [
		                        { collapsible: false, size: '500px',min: '120px', max:'720px'},
		                        { collapsible: false, min: '400px',}
		                    ]
		                });
					}
				});
			},	
	
			getChecksClick:function(){
				$(".getChecks").on("click", function(e) {
					var tr =  $(e.target).closest("tr");
					var data = subProjectWindow.subGrid.dataItem(tr);
					showChecksWindow.obj.title("查看项目验收记录 - " + data.project.projectName );
					showChecksWindow.obj.center().open();
					showChecksWindow.refreshChecksList(data.id);
					if(isChecksFirstOpen){
						isChecksFirstOpen = false;
						$("#checkHorizontal").kendoSplitter({
		                    panes: [
		                        { collapsible: false, size: '500px',min: '120px', max:'720px'},
		                        { collapsible: false}
		                    ]
		                });
					}
				});
			},
			//添加子项目
			addSubProjectClick:function(){
				$("#addSubProject").on(
						"click",
						function() {
							$("#subPid,#farmerName,#project,#subProjectNumber,#subProjectName,#totalCapital,#implementationUnit,#subProjectArea,#projectScaleAndContent,#constructionMode").val("");
							var size = $("#checkStatus").data("kendoDropDownList");
							subProjectManageWindow.obj.title("添加子项目");
							subProjectManageWindow.obj.center().open();
				});
			},
			
			//子项目信息详情
			detailSubProjectClick:function(){
				$(".subProjectDetail").on("click",function(e) {
					var tr =  $(e.target).closest("tr");
					var pdata = subProjectWindow.subGrid.dataItem(tr);
					$("#farmerNameFont").html(pdata.farmerName);
					$("#projectFont").html(pdata.project.projectName);
					$("#subProjectNameFont").html(pdata.subProjectName);
					$("#subProjectNumberFont").html(pdata.subProjectNumber);
					$("#totalCapitalFont").html(pdata.totalCapital);
					$("#fileFont").html(pdata.path);
					$("#implementationUnitFont").html(pdata.implementationUnit);
					$("#subProjectAreaFont").html(pdata.subProjectArea);
					$("#projectScaleAndContentFont").html(pdata.projectScaleAndContent);
					//$("#constructionModeFont").html(pdata.constructionMode==0?"先款后建"(pdata.constructionMode==1?"先建后款":"其他"));
					var newResult="";
					if (pdata.constructionMode ==0){
						newResult += "先建后款";
					}
					if (pdata.constructionMode ==1){
						newResult += "先款后建";
					} 
					$("#constructionModeFont").html(newResult);
					$("#shouldAccountFont").html(pdata.shouldAccount);
					$("#jzFont").html(pdata.jz);
					$("#jyFont").html(pdata.jy);
					$("#reimbursementRate").html(pdata.reimbursementRate);
					//$("#checkStatusFont").html(pdata.checkStatus==0?"已验收":(pdata.checkStatus==1?"未验收":"其他"));
					var result="";
					if (pdata.checkStatus ==0){
						result += "未验收";
					}
					if (pdata.checkStatus ==1){
						result += "验收通过";
					} 
					if (pdata.checkStatus ==2){
						result += "验收不通过";
					} 
					$("#checkStatusFont").html(result);
									
					subProjectManageDetailWindow.obj.title("子项目信息详情");
					subProjectManageDetailWindow.obj.center().open();
				});
			},
			
			//编辑子项目信息
			updateSubProjectClick:function(){
				$(".updateSubProject").on("click",function(e) {
					var tr =  $(e.target).closest("tr");
					var subPdata = subProjectWindow.subGrid.dataItem(tr);
//					//获取主项目
//						var projectList=$("#project").kendoDropDownList({
//							dataTextField: "projectName",
//					        dataValueField: "id",
//					        dataSource: {
//					            transport: {
//					                read: {
//					                	cache:false,
//					                	dataType : "json",
//										url : "project/getPrjoectList",
//					                }
//					            }
//					        },
//					    });
					$("#subPid").val(subPdata.id);
//					$("#project").data("kendoDropDownList").select();
					$("#farmerName").val(subPdata.farmerName);
					$("#subProjectName").val(subPdata.subProjectName);
					$("#subProjectNumber").val(subPdata.subProjectNumber);
					$("#totalCapital").val(subPdata.totalCapital);
					$("#shouldAccount").val(subPdata.shouldAccount);
					$("#implementationUnit").val(subPdata.implementationUnit);
					$("#subProjectArea").val(subPdata.subProjectArea);
					$("#file1").val(subPdata.file1);
					$("#file2").val(subPdata.file2);
					$("#file3").val(subPdata.file3);
					$("#projectScaleAndContent").val(subPdata.projectScaleAndContent);
					$("#constructionMode").val(subPdata.constructionMode);
					$("#checkStatus").val(subPdata.checkStatus);
					
					subProjectManageWindow.validator.validate();
					subProjectManageWindow.obj.title("编辑子项目");
					subProjectManageWindow.obj.center().open();
				});
			},
			
			//删除子项目信息
			deleteSubProjectClick:function(){
				$(".deleteSubProject").on("click",function(e) {
					var tr =  $(e.target).closest("tr");
					var data = subProjectWindow.subGrid.dataItem(tr);
					//data.projectId=subProjectWindow.projectId;
					if(confirm("确定删除子项目信息么？")){
						$.ajax({
							cache:false,
							type : "delete",	
							url : "subProjectManage/delete/"+data.id,
							dataType : "json",
							contentType : "application/json;charset=UTF-8",
							success : function(data, textStatus, jqXHR) {
								infoTip({
									content : "删除成功!",
									color : "#D58512"
								});
								subProjectWindow.subGrid.dataSource.read();
								userGridObj.userGrid.dataSource.read();
							},
							error:function(data, textStatus, jqXHR){
								infoTip({
									content : "删除失败!",
									color : "#FF0000"
								});
							}
						});
					}
				});
			},
			//保存 【添加】/【编辑】的子项目
			saveSubProjectClick : function() {
				$('form').on('submit', function() {
					var subProject = {},url="";
					subProject.id = $("#subPid").val();
					subProject.projectId=subProjectWindow.projectId;
					
					subProject.farmerName=$("#farmerName").val();
					subProject.subProjectName=$("#subProjectName").val();
					subProject.subProjectNumber=$("#subProjectNumber").val();
					subProject.totalCapital=$("#totalCapital").val();
					subProject.shouldAccount=$("#shouldAccount").val();
					subProject.file1=$("#file1").val();
					subProject.file2=$("#file2").val();
					subProject.file3=$("#file3").val();
					subProject.implementationUnit=$("#implementationUnit").val();
					subProject.subProjectArea=$("#subProjectArea").val();
					subProject.projectScaleAndContent = $("#projectScaleAndContent").val();
					subProject.constructionMode = $("#constructionMode").val();
					subProject.checkStatus = $("#status").val();
					$(this).ajaxSubmit({
			            type: "post", 
			            contentType : "application/json;charset=UTF-8",
			            //url: url,
			            dataType : "json",
			            data : JSON.stringify(subProject),
			            success : function(data, textStatus, jqXHR) {
							infoTip({
								content : "保存成功!",
								color : "#D58512"
							});
							subProjectWindow.subGrid.dataSource.read();
						},
						error : function(data, textStatus, jqXHR) {
							infoTip({
								content : "保存失败!",
								color : "#FF0000"
							});
						},
					});
					subProjectWindow.obj.close();
			        return false; // 阻止表单自动提交事件
			    });
			},
			
			// 取消 【添加】/【编辑】的子项目
			cancelUserClick : function() {
				$("#cancelSubProject").on("click", function() {
					subProjectManageWindow.obj.close();
				});
			}
		}
			//子项目信息详情
			subProjectManageDetailWindow = {
				obj : undefined,

				id : $("#subProjectManageDetailWindow"),
				
				validator : undefined,

				init : function() {
					if (!subProjectManageDetailWindow.id.data("kendoWindow")) {
						subProjectManageDetailWindow.id.kendoWindow({
							width : "500px",
							height: "550px",
							actions : ["Close"],
							modal : true,
							title : "子项目信息详细"
						});
					}
					subProjectManageDetailWindow.obj = subProjectManageDetailWindow.id.data("kendoWindow");
				}
			};
			
			// 返回
			$("#backSubProjectManageDetail").on("click", function() {
				subProjectManageDetailWindow.obj.close();
			});

	// 项目详情查看
	projectDetailWindow = {
		projectDetailGrid : {},
			
		obj : undefined,
		
		id : $("#projectInfoDetailWindow"),
		
		validator : undefined,
		
		init : function() {
			if (!projectDetailWindow.id.data("kendoWindow")) {
				projectDetailWindow.id.kendoWindow({
					width : "500px",
					maxHeight: "570px",
					actions : ["Maximize", "Close"],
					modal : true,
					title : "项目明细查看"
				});
			};
			projectDetailWindow.obj = projectDetailWindow.id.data("kendoWindow");
			//项目详情验证
			projectDetailWindow.validator = $("#projectInfoDetailValidate").kendoValidator().data("kendoValidator");
		},

	},

	
	//添加子项目
	subProjectManageWindow = {
			obj : undefined,
			id : $("#subProjectManageWindow"),
			validator : undefined,
			init : function() {
				if (!subProjectManageWindow.id.data("kendoWindow")) {
					subProjectManageWindow.id.kendoWindow({
						width : "650px",
						height:"570px",
						actions : ["Close"],
						modal : true,
						title : "添加子项目"
					});
                }
				subProjectManageWindow.obj = subProjectManageWindow.id.data("kendoWindow");

			subProjectManageWindow.validator = $("#subProjectValidate").kendoValidator().data("kendoValidator");
			}
		};
	
	// 查看详情返回按钮
	$("#backProjectDetail").on("click", function() {
		projectDetailWindow.obj.close();
	});
	
	deleteRemarkWindow = {

				obj : undefined,

				id : $("#changeDeleteWindow"),
				deleteId :undefined,
				validator : undefined, 

				init : function() {
					if (!deleteRemarkWindow.id.data("kendoWindow")) {
						deleteRemarkWindow.id.kendoWindow({
							width : "800px",
							height: "550px",
							actions : ["Close"],
							modal : true,
							title : "公示附件",
							close:function(){
								//location.reload();
								//$(".k-upload-status").html("");
								$(".k-upload-files").html("");
								
							}
						});
					}
					deleteRemarkWindow.obj = deleteRemarkWindow.id.data("kendoWindow");
					deleteRemarkWindow.validator = $("#changeDeleteValidate").kendoValidator().data("kendoValidator");
				}
		};
		
		lock = true;
		fileUploadObj = {
			init : function() {
				$("#fileupload").kendoUpload({
					
					async :{
					type: 'POST',
			        saveUrl: "attachmentUpload/uploadFile",
			        dataType: 'json',
			        autoUpload: true,
			        acceptFileTypes: /(\.|\/)(jpg|gif|png)$/i,
			        formAcceptCharset: 'utf-8',
			        maxFileSize: 10485760
			       
					},
				 upload: function (e) {
				        e.data = { projectId: $("#projectId").val()};
				    },
				    multiple: false,
				    success : onSuccess
				});
				
			}	
		};
		function onSuccess(e) {
			$("#picture1").html("");
		    e.data=$("#projectId").val();
		    var projectId = e.data;
		    $.ajax({
				cache:false,
				type : "GET",
				url : "attachmentUpload/searchPicture/"+projectId,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				success : function(data, textStatus, jqXHR) {
					 for (var index in data) {
						if(index != "remove"){
							var div = document.getElementById("picture1");
							var img = document.createElement("img");
							img.setAttribute("id", "imgs");
							img.getAttribute("id");
							img.width=130;
							img.height=100;
							img.src = data[index].fileName;
							div.appendChild(img);
						 }
			        }
					
				},
				error : function(data, textStatus, jqXHR) {
					infoTip({
						content : "读取图片错误",
						color : "#FF0000"
					});
				}
			});
		};
		labelStyle = {
			position: "relative",
			color : "red",
			fontSize : "12px",
			width : "80px",
			height : "80px",
			border:"0",
			lineHeight : "20px",
			fontFamily:"微软雅黑"
		};
		function initMap(container) {
			
			//初始化地图
//			var map = new BMap.Map(container, { mapType: BMAP_HYBRID_MAP });
			var map = new BMap.Map(container);
			map.addControl(new BMap.MapTypeControl());
			map.enableScrollWheelZoom();//启用滚轮放大缩小
			map.enableContinuousZoom();//启用连续缩放效果
			map.enableInertialDragging();//启用地图惯性拖拽
			
			//显示当前位置
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r){
				if(this.getStatus() == BMAP_STATUS_SUCCESS){
					map.centerAndZoom(r.point,18);
				}
				else {
					alert('failed'+this.getStatus());
				}        
			},{enableHighAccuracy: true});
			
			// 添加带有定位的导航控件
			var navigationControl = new BMap.NavigationControl({
				// 靠左上角位置
				anchor: BMAP_ANCHOR_TOP_LEFT,
				// LARGE类型
				type: BMAP_NAVIGATION_CONTROL_LARGE,
				// 启用显示定位
				enableGeolocation: true
			});
			map.addControl(navigationControl);
			// 添加定位控件
			var geolocationControl = new BMap.GeolocationControl({enableAutoLocation:true});
			geolocationControl.addEventListener("locationSuccess", function(e){
				// 定位成功事件
			    var address = '';
			    address += e.addressComponent.province;
			    address += e.addressComponent.city;
			    address += e.addressComponent.district;
			    address += e.addressComponent.street;
			    address += e.addressComponent.streetNumber;
			});
			geolocationControl.addEventListener("locationError",function(e){
			    // 定位失败事件
				infoTip({
					content : e.message,
					color : "#FF0000"
				});
			});
			map.addControl(geolocationControl);
			return map;
		};
		//查看项目监管记录window
		showSupervisionsWindow = {
			obj : undefined,
			id : $("#showSupervisionsWindow"),
			supervisionsGrid: undefined,
			validator : undefined,
			map : undefined,
			sId : undefined,
			markersObj: undefined,
			init : function() {
				if (!showSupervisionsWindow.id.data("kendoWindow")) {
					showSupervisionsWindow.id.kendoWindow({
						width : "95%",
						height:"710px",
						actions : ["Maximize", "Close"],
						modal : true,
						title : "查看项目监管记录"
					});
				}
				showSupervisionsWindow.obj = showSupervisionsWindow.id.data("kendoWindow");
				showSupervisionsWindow.map = initMap("showSupervisionsMap");
				
			},
			initSupervisionsList: function(sId){
				if(!sId || sId == ''){
					sId = 0;
				}
				this.supervisionsGrid = $("#showSupervisionsList")
				.kendoGrid(
						{
							dataSource : {
								transport : {
									read : {
										cache:false,
										type : "get",
										url : "supervision/search",
										dataType : "json",
										contentType : "application/json;charset=UTF-8"
									},
									parameterMap : function(data, type) {
										return {
											sId : data.filter.filters[0].value,
											page : data.skip,
											pageSize : data.pageSize
										};
									}
								},
								height: 543,
								
								serverPaging: true,
								
	                            serverSorting: true,
	                            
								serverFiltering : true,
								
								pageSize : 50,
								
								schema : {
									total : "totalCount",
									data : "items"
								},
								filter : {
									field : 'sId',
									value : sId
								},
								requestEnd: function(e) {
								    var items = e.response.items;
								    showSupervisionsWindow.mapDataHandler(items);
								}
							},
							scrollable: {
	                            virtual: true
	                        },
							sortable : true,
							
							resizable : true,

							columns : [
							    	{
							        	field : "id",
							        	hidden : true
							    	},
							    	{
							    		field : "name",
							    		width : 115,
							    		locked: true,
							    		title : "监管名称"
							    	},
							    	{
							    		field : "existingProblems",
							    		width : 80,
							    		title : "存在问题"
							    	},
									{
										field : "correctSuggestions",
										width : 100,
										title : "整改建议"
									},
									{
										field : "correctTime",
										width : 110,
										title : "整改时间"
									},
									{
										field : "farmerNumber",
										width : 110,
										title : "农户数"
									},
									{
										field : "fund",
										width : 100,
										title : "金额"
									},
									{
										field : "description",
										width : 100,
										title : "其它"
									}
								],
							dataBound : function() {
								showSupervisionsWindow.supervisionClick();
							}
						}).data("kendoGrid");
			},
			supervisionClick: function(){
				$("#showSupervisionsList tr").on('click', function (e) {
					var tr =  $(e.target).closest("tr");
					var supervision = showSupervisionsWindow.supervisionsGrid.dataItem(tr);
					var currentMarker =showSupervisionsWindow.markersObj[supervision.id];
					showSupervisionsWindow.map.centerAndZoom(currentMarker.getPosition(), 18);
					EventWrapper.trigger(currentMarker, "mouseover");
				})
			},
			refreshSupervisionsList: function(sId){
				if(this.supervisionsGrid){
					this.supervisionsGrid.dataSource.filter([{
						field : "sId",
						value : sId
					}]);
				}else {
					this.initSupervisionsList(sId);
				}
			},
			mapDataHandler: function(data){
				var map = this.map;
				map.clearOverlays();
				showSupervisionsWindow.markersObj = {};
				if(data.length > 0){
					for(var i in data){
						var dd = data[i];
						if(dd.point && !isNaN(dd.point.lng) && !isNaN(dd.point.lat)){
							var point1 = new BMap.Point(dd.point.lng, dd.point.lat);
							var marker1 = new BMap.Marker(point1);//创建标注
							showSupervisionsWindow.markersObj[dd.id] = marker1; //保存marker到markersObj对象，单击左侧列表时获取此标注
							if(i==0){
								map.centerAndZoom(point1,18);
							}
							
							var msg = "<div> 存在问题: "+dd.existingProblems+ "<br/>整改建议: "+dd.correctSuggestions + "<br/>整改时间: "+dd.correctTime + "</div>";
							var imgs = '';
							if(dd.pictures && dd.pictures.length > 0){
								var pitArr = dd.pictures.split(';');
								for(var j=0;j<pitArr.length, pitArr [j].length > 0;j++){
									imgs += '<img src="'+pitArr[j]+'" data-src="'+pitArr[j]+'" class="zoomSize" style="margin-top:8px;float:bottom;zoom:1;overflow:hidden;width:50px;height:50px;margin-left:3px;"/>';
								}
							}

							
							var infotip = {title:'',content:''};
							infotip.content = '<div style="margin:0;line-height:18px;padding:2px;font-size: 12px; color: rgb(77, 77, 77);margin-top: 5px;">' +
		                    	msg + imgs +
		                    	'</div>';
							infotip.title = '<span style="font-size: 14px; color: rgb(77, 77, 77); font-weight: bold; text-decoration: none;">项目监控－'+dd.name+'</span>';
							var label1 = new BMap.Label(infotip.title,{offset:new BMap.Size(20,-10)});
							label1.setStyle(labelStyle);
							marker1.setLabel(label1);
							map.addOverlay(marker1);
							addClickHandler(infotip, marker1);
						}
					}
				}else {
					infoTip({
						content : "没有监控点!",
						color : "#D58512"
					});
					map.centerAndZoom(new BMap.Point(106.475375,26.566064),15);//无数据时显示清镇市地图
				};
				function addClickHandler(infotip,marker){
					EventWrapper.addListener(marker, 'mouseover',function(e){
						var opts = {
							width : 250,     // 信息窗口宽度
							height: 145,     // 信息窗口高度
							title : infotip.title ,  // 信息窗口标题
							offset:new BMap.Size(0,-20)
						};
						var infoWindow = new BMap.InfoWindow(infotip.content,opts);
						infoWindow.addEventListener('open',function(e){
							addPictureTooltip();
						});
						map.openInfoWindow(infoWindow,marker.getPosition());
				    });
				};
				function addPictureTooltip(){
					//鼠标悬停提示
					$(".zoomSize").kendoTooltip({
		                content: kendo.template($("#pictureTipTemplate").html()),
		                position: "left"
		            });
				};
			}
		};
		
		//查看项目验收记录window
		showChecksWindow = {
			obj : undefined,
			id : $("#showChecksWindow"),
			checksGrid: undefined,
			validator : undefined,
			checksArr : {},
			map : undefined,//地图对象
			sId : undefined,//子项目Id
			init : function() {
				if (!showChecksWindow.id.data("kendoWindow")) {
					showChecksWindow.id.kendoWindow({
						width : "95%",
						height:"710px",
						actions : ["Maximize", "Close"],
						modal : true,
						title : "查看项目验收记录"
					});
				}
				showChecksWindow.obj = showChecksWindow.id.data("kendoWindow");
				showChecksWindow.map = initMap("showChecksMap");
			},
			initChecksList: function(sId){
				if(!sId || sId == ''){
					sId = 0;
				}
				this.sId = sId;
				this.checksGrid = $("#showChecksList")
				.kendoGrid(
						{
							dataSource : {
								transport : {
									read : {
										cache:false,
										type : "get",
										url : "checks/search",
										dataType : "json",
										contentType : "application/json;charset=UTF-8"
									},
									parameterMap : function(data, type) {
										return {
											sId : data.filter.filters[0].value,
											page : data.skip,
											pageSize : data.pageSize
										};
									}
								},
								height: 543,
								
								serverPaging: true,
								
	                            serverSorting: true,
	                            
								serverFiltering : true,
								
								pageSize : 50,
								
								schema : {
									total : "totalCount",
									data : "items"
								},
								filter : {
									field : 'sId',
									value : sId
								},
								requestEnd: function(e) {
								    var items = e.response.items;
								    showChecksWindow.mapDataHandler(items);
								}
							},
							scrollable: {
	                            virtual: true
	                        },
							sortable : true,
							
							resizable : true,

							columns : [
							    	{
							        	field : "id",
							        	hidden : true
							    	},
							    	{
							    		field : "subProject",
							    		width : 120,
							    		locked: true,
							    		title : "项目名称",
							    		template: function(dataItem) {
											if(dataItem.subProject && dataItem.subProject != null){
												return dataItem.subProject.subProjectName;
											}else{
												return "";
											}
										}
							    	},
							    	{
							    		field : "createDate",
							    		width : 160,
							    		title : "验收日期"
							    	},
									{
										field : "isSuccess",
										width : 100,
										title : "验收结果",
							    		template: function(dataItem) {
											if(dataItem.isSuccess == 1){
												return "通过";
											}else if(dataItem.isSuccess == 2){
												return "不通过";
											}else{
												return "";
											}
										}
									},
									{
										field : "pictures",
										width : 180,
										title : "验收报告",
										template: function(dataItem){
											if(dataItem.pictures && dataItem.pictures.length>0){
												var picturesStr = "";
												var picturesArr = dataItem.pictures.split(";");
												for(var i =0;i<picturesArr.length;i++){
													if(picturesArr[i].length>0){
														picturesStr += "<div class='checks-photo' data-src="+picturesArr[i]+" style='background-image: url("+ picturesArr[i] +");'></div>";
													}
												}
												return picturesStr;
											}else {
												return "无";
											}
										}
									}
								],
							dataBound : function() {
								showChecksWindow.checkClick();
								showChecksWindow.addPictureTooltip();
							}
						}).data("kendoGrid");
			},
			addPictureTooltip: function(){
				//鼠标悬停提示
				$(".checks-photo").kendoTooltip({
	                content: kendo.template($("#pictureTipTemplate").html()),
	                position: "left"
	            });
			},
			checkClick: function(){
				$("#showChecksList tr").on('click', function (e) {
					var tr =  $(e.target).closest("tr");
					var check = showChecksWindow.checksGrid.dataItem(tr);
					var currentCheck =showChecksWindow.checksArr[check.id];
					showChecksWindow.map.centerAndZoom(currentCheck.startMarker.getPosition(), 18);
					if(currentCheck.polyline && currentCheck.polyline.isVisible()){
						currentCheck.polyline.hide();
						currentCheck.polylineMarker.hide();
						currentCheck.startMarker.hide();
						currentCheck.endMarker.hide();
					}else if(currentCheck.polygon && currentCheck.polygon.isVisible()){
						currentCheck.polygon.hide();
						currentCheck.polygonMarker.hide();
						currentCheck.startMarker.hide();
						currentCheck.endMarker.hide();
					}else if(currentCheck.polygon && !currentCheck.polyline.isVisible()){
						currentCheck.polyline.show();
						currentCheck.polylineMarker.show();
						currentCheck.startMarker.show();
						currentCheck.endMarker.show();
					}else if(currentCheck.polygon && !currentCheck.polygon.isVisible()){
						currentCheck.polygon.show();
						currentCheck.polygonMarker.show();
						currentCheck.startMarker.show();
						currentCheck.endMarker.show();
					}
				})
			},
			refreshChecksList: function(sId){
				if(this.checksGrid){
					this.checksGrid.dataSource.filter([{
						field : "sId",
						value : sId
					}]);
				}else {
					this.initChecksList(sId);
				}
			},
			mapDataHandler: function(data){
				var map = this.map;
				map.clearOverlays();
				if(data.length > 0){
					for(var i in data){
						var points = data[i].points;
						if(points && points.length > 0){
							var everyCheck = {
								pointArr : [],//坐标数组
								polyline : undefined, //路线图
								polygon : undefined,//面积图
								totalDistances : 0, //路线长度
								totalArea : 0,//路线围绕面积
								startMarker : undefined, //开始标注
								endMarker : undefined, //结束标注
								polylineMarker : undefined, //周长和面积信息提示marker
								polygonMarker: undefined//面积marker
							};
							for(var i1 = 0;i1 < points.length; i1 ++){
								var dd = points[i1];
								if(dd && !isNaN(dd.lng) && !isNaN(dd.lat)){
									var pt = new BMap.Point(dd.lng, dd.lat);
									if(i1==0){
										map.centerAndZoom(pt,18);
									}
									everyCheck.pointArr.push(pt);
									var myIcon = new BMap.Symbol(BMap_Symbol_SHAPE_CIRCLE, {
									    scale: 5,
									    strokeWeight: 1,
									    fillColor: 'white',
									    fillOpacity: 0.6
									  });
									var marker2 = new BMap.Marker(pt,{icon:myIcon});  // 创建标注
//									map.addOverlay(marker2);              // 将坐标点标注添加到地图中
									if(i1 == 0 ){
										var label = new BMap.Label("起点",{offset:new BMap.Size(20,-10)});
										label.setStyle(labelStyle);
										marker2.setLabel(label);
										map.addOverlay(marker2);
										everyCheck.startMarker = marker2;
									}
									if(i1 == points.length-1 ){
										var label = new BMap.Label("终点",{offset:new BMap.Size(20,-10)});
										label.setStyle(labelStyle);
										marker2.setLabel(label);
										map.addOverlay(marker2);
										everyCheck.endMarker = marker2;
									}
								}
							}
							everyCheck.polyline = new BMap.Polyline(everyCheck.pointArr, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
							map.addOverlay(everyCheck.polyline);
							
							everyCheck.polygon = new BMap.Polygon(everyCheck.pointArr, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});  //创建多边形
							map.addOverlay(everyCheck.polygon);
							
							for(var i2 =0 ;i2< everyCheck.pointArr.length-1; i2++){
								everyCheck.totalDistances += parseFloat((showChecksWindow.map.getDistance(everyCheck.pointArr[i2], everyCheck.pointArr[i2+1])).toFixed(2));
							}
							
							// 创建信息窗口对象
							var label = new BMap.Label("路线总长度：" + everyCheck.totalDistances.toFixed(2) + "米",{offset:new BMap.Size(0,-20)});
							label.setStyle(labelStyle);
							//计算面积
							everyCheck.totalArea = BMapLib.GeoUtils.getPolygonArea(everyCheck.pointArr);
							everyCheck.totalArea = (everyCheck.totalArea*3/2000).toFixed(2);
							if(!$.isNumeric(everyCheck.totalArea)){
								everyCheck.totalArea = 0;
							}
							
							var marker = new BMap.Marker(everyCheck.pointArr[0]);  // 创建标注
							showChecksWindow.map.addOverlay(marker);              // 将标注添加到地图中
							everyCheck.polylineMarker = marker;
							marker.setLabel(label);
							
							var label = new BMap.Label("路线围绕面积：" + everyCheck.totalArea + "亩", {offset:new BMap.Size(0,-20)});
							label.setStyle(labelStyle);
							everyCheck.polygonMarker = new BMap.Marker(everyCheck.pointArr[0]);  // 创建标注
							everyCheck.polygonMarker.setLabel(label);
							showChecksWindow.map.addOverlay(everyCheck.polygonMarker);
							
							everyCheck.polygon.hide();
							everyCheck.polygonMarker.hide();
							
							showChecksWindow.checksArr[data[i].id] = everyCheck;
						}
					}
				}else {
					infoTip({
						content : "没有验收记录!",
						color : "#D58512"
					});
					map.centerAndZoom(new BMap.Point(106.475375,26.566064),15);//无数据时显示清镇市地图
				}
			}
		};
		
		$('#clearSupervisions').on('click', function() {
			var overlays = showSupervisionsWindow.map.getOverlays();
			for(var i=0;i<overlays.length;i++){
				overlays[i].hide();
			}
		});
		
		$('#initSupervisions').on('click', function() {
			var overlays = showSupervisionsWindow.map.getOverlays();
			for(var i=0;i<overlays.length;i++){
				overlays[i].show();
			}
		});
		//计算路线长度
		$('#perimeter').on('click', function() {
			if(!showChecksWindow.checksArr || showChecksWindow.checksArr.length <= 0){
				infoTip({
					content : "没有验收记录！",
					color : "#FF0000"
				});
				return;
			}
			$.each(showChecksWindow.checksArr, function(p, v) {
				v.polygon.hide();
				v.polygonMarker.hide();
				v.polyline.show();
				v.polylineMarker.show();
				v.startMarker.show();
				v.endMarker.show();
			});
		});
		//计算面积
		$('#area').on('click', function() {
			if(!showChecksWindow.checksArr || showChecksWindow.checksArr.length <= 0){
				infoTip({
					content : "没有验收记录！",
					color : "#FF0000"
				});
				return;
			}
			$.each(showChecksWindow.checksArr, function(p, v) {
				v.polyline.hide();
				v.polylineMarker.hide();
				v.polygon.show();
				v.polygonMarker.show();
				v.startMarker.show();
				v.endMarker.show();
			});
		});
	
		
		/*点击财务按钮展示【财务表】-zhanggx*/
		userdetailsWindow = {
				userDetailsListGrid : {},
				obj : undefined,
				id : $("#userdetailsWindow"),
				init : function() {
					userdetailsWindow.id.kendoWindow({
						width : "1000px",
						height : "300px",
						actions : [ "Maximize", "Close" ],
						modal : true,
						title : "财务明细查看",
						close : function(e){	//窗口关闭事件
							subProjectWindow.subGrid.dataSource.read();
						}
					});
					userdetailsWindow.obj = userdetailsWindow.id.data("kendoWindow");
				},
				showDetailsList : function(data) {
					subProject_id = data.id;
					this.userDetailsListGrid = $("#financeList").kendoGrid({
						dataSource : {
							transport : {
								read : {
									type : "get",
									cache : false,
									url : "financeManagement/selectById/"+subProject_id,
									dataType : "json",
									contentType : "application/json;charset=UTF-8"
								},
								parameterMap : function(data, type) {
									return {
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
								field : 'projectName',
								value : ''
							}
						},

						 //toolbar : kendo.template($("#template").html()),

						reorderable : true,

						resizable : true,

						sortable : true,

						columnMenu : true,

						pageable : {
							refresh : true
						},

						editable : "popup",

						columns : [ {
							field : "id",
							hidden : true
						}, {
							field : "sub_Project_id",
							hidden :true
						},{
							field : "certificateNum",
							title : "财务凭证编号",
							width : 100
						},{
							field : "appropriation",
							title : "拨款金额(单位/万元)",
							width : 100
						}, {
							field : "bk_user",
							title : "拨款人 ",
							width : 100
						},{
							field : "bk_date",
							title : "拨款日期",
							width : 100
						},{
							field : "account",
							title : "报账金额(单位/万元)",
							width : 120,
							
							
						},{
							field : "bz_user",
							title : "报账人",
							width : 100
						},{
							field : "bz_date",
							title : "报账日期",
							width : 100
						},{
							title : "操作",
							template : function(){
								return 	"&nbsp;&nbsp;<a class='editFiance editIcon aLink  ' title='财务信息修改' style='margin-left:2px;'><i class='fa fa-pencil' ></i></a>"+
										"&nbsp;&nbsp;<a class='deleteFiance editIcon aLink   title='财务信息删除' style='margin-left:2px;'><i class='glyphicon glyphicon-trash'></i></a>" +
										"&nbsp;&nbsp;<a class='detailFiance editIcon aLink   title='财务信息详情' style='margin-left:2px;'><i class='fa fa-eye'></i></a>";
							},
							width : 120
						}],
						dataBound : function(){								
							userdetailsWindow.detailFiance();
							userdetailsWindow.removeFinance();
							userdetailsWindow.editFiance();
						}		
					}).data("kendoGrid");
				},
				
				/*财务信息详情-zhanggx*/
				detailFiance : function(){
					$(".detailFiance").on("click",function(e){
						var tr = $(e.target).closest("tr");
						var data = userdetailsWindow.userDetailsListGrid.dataItem(tr);
						$("#fid").html(data.id);
						$("#subprojectIdShow").html(subId);
						
						$("#certificateNumShow").html(data.certificateNum);
						$("#appropriationShow").html((data.appropriation==null) ? "" :data.appropriation+"万元");
						$("#operateUserShow").html(data.bk_user);
						$("#dateshow").html(data.bk_date);
											
						$("#accountshow").html((data.account==null ? "" : data.account+"万元"));
						$("#bzUser").html(data.bz_user);
						$("#bzDate").html(data.bz_date);
						$("#remarkshow").html(data.remark);
						fianceInfoShowObjWindow.obj.title("查看财务信息");
						fianceInfoShowObjWindow.obj.center().open();
					});
				},
				
				/*删除财务信息-zhanggx*/
				removeFinance : function(){
					$(".deleteFiance").on("click",function(e){
						var tr = $(e.target).closest("tr");
						var data = userdetailsWindow.userDetailsListGrid.dataItem(tr);
						var financeId = data.id;	//财务信息id
						var bzAcount = data.account;
						if(bzAcount == null || bzAcount==""){
							bzAcount = 0;
						}
						if (confirm("确定删除此条财务信息吗？")) {
							$.ajax({
								cache:false,
								type : "delete",
								url : "financeManagement/remove/"+financeId+"/"+ bzAcount+"/"+subId,
								dataType : "json",
								contentType : "application/json;charset=UTF-8",
								success : function(data, textStatus, jqXHR) {
									infoTip({
										content : data.message,
										color : "#D58512"
									});
									userdetailsWindow.userDetailsListGrid.dataSource.read();
								},
								error : function(data, textStatus, jqXHR) {
									infoTip({
										content : "删除失败!",
										color : "#FF0000"
									});
								}
							});	
						}
					});
				},
				
				/*编辑财务信息-zhanggx*/
				editFiance : function(){
					$(".editFiance").on("click",function(e){
						var tr = $(e.target).closest("tr");
						var pdata = userdetailsWindow.userDetailsListGrid.dataItem(tr);
						if(pdata.appropriation == null|| pdata.appropriation == ""){	//报账信息
							$("#bzfid").val(pdata.id);
							$("#bzsubprojectId").val(pdata.sub_Project_id);
							$("#bzCertificateNum").val(pdata.certificateNum);
							$("#bzAccount").val(pdata.account);
							$("#bzuser").val(pdata.bz_user);
							$("#bzdate").val(pdata.bz_date);
							$("#bzremark").val(pdata.remark);
							baoZhangFinanceObjWindow.obj.title("修改报账信息");
							baoZhangFinanceObjWindow.obj.center().open();
							return ;
						}else{						//拨款信息
							$("#fid").val(pdata.id);
							$("#subprojectId").val(pdata.sub_Project_id);
							$("#certificateNum").val(pdata.certificateNum);
							$("#appropriation").val(pdata.appropriation);
							$("#operateUser").val(pdata.bk_user);
							$("#date").val(pdata.bk_date);
							$("#remark").val(pdata.remark);
							fianceInfoObjWindow.obj.title("修改拨款信息");
							fianceInfoObjWindow.obj.center().open();
							return ;
						}
					});
				}
		}	
		userdetailsWindow.init();
		

	// 返回
	$("#backProjectDetail").on("click", function() {
		projectDetailWindow.obj.close();
	});
	
	/*拨款信息【取消】按钮-zhanggx*/
	$("#cancelFiance").on("click",function(){
		fianceInfoObjWindow.obj.close();
	});

	/*日期格式*/
	$("#date").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: new Date(),
		max:new Date()
	});
	
	$("#bzdate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: new Date(),
		max:new Date()
	});
	
	/*财务信息详情【返回】按钮-zhanggx*/
	$("#fanhui").on("click",function(){
		fianceInfoShowObjWindow.obj.close();
	});
	
	/*【添加拨款信息】按钮-zhanggx*/
	$("#addFinance").on("click", function(e) {
		$("#fid,#certificateNum,#appropriation,#account,#operateUser,#date,#remark").val("");
		fianceInfoObjWindow.obj.title("添加拨款信息");
		fianceInfoObjWindow.obj.center().open();
		
	});
	
	/*【添加报账信息】按钮-zhanggx*/
	$("#addBaoZhangInfo").on("click",function(){		
		$("#bzfid,#bzCertificateNum,#bzAccount,#bzuser,#bzdate,#bzremark").val("");
		baoZhangFinanceObjWindow.obj.title("添加报账信息");
		baoZhangFinanceObjWindow.obj.center().open();
	});
	
	
	/*【拨款信息确认】按钮-zhanggx*/
	$("#saveFiance").on("click",function(e){
		e.preventDefault();

		var finance = {} , url="";
		finance.id = $("#fid").val();
		finance.sub_Project_id = subId;
		finance.appropriation = $("#appropriation").val();
		finance.certificateNum =$("#certificateNum").val();	
		finance.bk_date = $("#date").val();
		finance.bk_user = $("#operateUser").val();	
		finance.remark = $("#remark").val();
		if(finance.certificateNum.length ==  "" ||finance.certificateNum == null){
			alert("凭证号不能为空");
			lock = false;
			return;
		}
		   
		if(finance.bk_date == ""){
			alert("拨款日期不能为空");
			lock = false;
			return;
		}
		url = (finance.id == null || finance.id == "") ? "financeManagement/add" : "financeManagement/update";		
		$.ajax({					
				cache:false,
				type : "post",
				url : url,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				data : JSON.stringify(finance),
				success : function(data) {
					infoTip({
						content : data.message,
						color : "#D58512"
					});
					userdetailsWindow.userDetailsListGrid.dataSource.read();
					lock = false;
				},
				error : function(data) {
					infoTip({
						content : "保存失败!",
						color : "#FF0000"
					});
					lock = false;
				}
			});	
		fianceInfoObjWindow.obj.close();
		fianceInfoObjWindow.validator = $("#fianceInfoValidate").kendoValidator().data("kendoValidator");
	});
	
	/*拨款信息【取消】按钮-zhanggx*/
	$("#cancelFiance").on("click",function(){
		fianceInfoObjWindow.obj.close();
	});
	
	
	/*拨款信息窗口-zhanggx*/
	fianceInfoObjWindow = {

		obj : undefined,

		id : $("#fianceInfoWindow"),
		
		validator : undefined,

		init : function() {
			if (!fianceInfoObjWindow.id.data("kendoWindow")) {
				fianceInfoObjWindow.id.kendoWindow({
					width : "700px",
					actions : ["Close"],
					modal : true,
					title : "财务信息",
					close : function(e){
						subProjectWindow.subGrid.dataSource.read();
					}
				});
			}
			fianceInfoObjWindow.obj = fianceInfoObjWindow.id.data("kendoWindow");
			// 用户信息验证
			fianceInfoObjWindow.validator = $("#fianceInfoValidate").kendoValidator().data("kendoValidator");
		}
	}
	fianceInfoObjWindow.init();
	
	/*信息详情查看窗口-zhanggx*/
	fianceInfoShowObjWindow = {

		obj : undefined,

		id : $("#fianceInfoShowWindow"),
		
		validator : undefined,

		init : function() {
			if (!fianceInfoShowObjWindow.id.data("kendoWindow")) {
				fianceInfoShowObjWindow.id.kendoWindow({
					width : "400px",
					actions : ["Close"],
					modal : true,
					title : "财务信息"
				});
			}
			fianceInfoShowObjWindow.obj = fianceInfoShowObjWindow.id.data("kendoWindow");
			// 用户信息验证
			//fianceInfoShowObjWindow.validator = $("#baozhangInfoValidate").kendoValidator().data("kendoValidator");
		}
	}
	fianceInfoShowObjWindow.init();
	
	/*报账信息窗口-zhanggx*/	
	baoZhangFinanceObjWindow = {

		obj : undefined,

		id : $("#baoZhangfoWindow"),
		
		validator : undefined,

		init : function() {
			if (!baoZhangFinanceObjWindow.id.data("kendoWindow")) {
				baoZhangFinanceObjWindow.id.kendoWindow({
					width : "700px",
					actions : ["Close"],
					modal : true,
					title : "报账信息"

				});
			}
			baoZhangFinanceObjWindow.obj = baoZhangFinanceObjWindow.id.data("kendoWindow");
			// 用户信息验证
			baoZhangFinanceObjWindow.validator = $("#baozhangInfoValidate").kendoValidator().data("kendoValidator");
		}
	}
	baoZhangFinanceObjWindow.init();
	
	/*【报账信息【取消】按钮-zhanggx*/
	$("#baoZhangCancelFiance").on("click",function(){
		baoZhangFinanceObjWindow.obj.close();
	});
	
	/*【报账信息【确认】按钮-zhanggx*/
	$("#baoZhangSaveFiance").on("click",function(e){
		e.preventDefault();
		var finance = {} ;
		finance.id = $("#bzfid").val();
		finance.sub_Project_id = subId;
		finance.certificateNum =$("#bzCertificateNum").val();		
		finance.account = $("#bzAccount").val();
		finance.bz_user = $("#bzuser").val();
		finance.bz_date = $("#bzdate").val();
		finance.remark = $("#remark").val();
		var url = (finance.id == null || finance.id == "") ? "financeManagement/add" : "financeManagement/update";		

//		var bkTime = $("#bkDate").val();
//		var bzTime = $("#bzdate").val();
//		var bk=new Date(Date.parse(bkTime.replace(/-/g, "/")));//发生时间
//		var bz = new Date(Date.parse(bzTime.replace(/-/g, "/")));//发生时间
//		if(bz < bk){
//			alert("报账时间应该在拨款时间之后，请检查");
//			return;
//		}
		$.ajax({					
				cache:false,
				type : "post",
				url : url,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				data : JSON.stringify(finance),
				success : function(data) {
					$.ajax({
						cache:false,
						type : "get",
						url : "financeManagement/addByFinancesToSubProject/"+subId,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						success : function(data) {
							financeSum = data;		//将返回的数据传到全局变量
							//subProjectWindow.subGrid.dataSource.read();
							subProjectWindow.subGrid.dataSource.read();
						}
					});
					infoTip({
						content : "操作成功",
						color : "#D58512"
					});
					userdetailsWindow.userDetailsListGrid.dataSource.read();
					lock = false;
				},
				error : function(data) {
					infoTip({
						content : "操作失败",
						color : "#FF0000"
					});
					lock = false;
				}
			});	

		baoZhangFinanceObjWindow.obj.close();
		baoZhangFinanceObjWindow.validator = $("#baozhangInfoValidate").kendoValidator().data("kendoValidator");
	});

	deleteRemarkWindow.init();
	
	fileUploadObj.init();

	subProjectManageWindow.init();

	subProjectWindow.init();

	projectDetailWindow.init();

	subProjectManageDetailWindow.init();
	showSupervisionsWindow.init();
	showChecksWindow.init();
	subProjectManageDetailWindow.init();

});

