var projectImportDetailWindow = {};
$(function() {
	
	//时间范围
	kendo.culture("zh-CN");
	$("#searchStartTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:mm:ss"
	});
	$("#searchEndTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:mm:ss"
	});

	var projectImportGridObj = {}, templateDownloadWindow = {}, fileUploadObj = {}, lock = false;
	// 项目导入列表
	projectImportGridObj = {
		projectImportGrid : {},
		init : function() {
			$("#importStatus").kendoDropDownList();
			this.projectImportGrid = $("#projectImportList")
					.kendoGrid(
							{
								dataSource : {
									transport : {
										read : {
											type : "get",
											url : "projectImport/search",
											dataType : "json",
											contentType : "application/json;charset=UTF-8"
										},
										parameterMap : function(data, type) {
											return {
												fileName : data.filter.filters[0].value,
												searchStartTime : data.filter.filters[1].value,
												searchEndTime : data.filter.filters[2].value,
												importStatus : data.filter.filters[3].value,
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
									filter : [{
										field : "fileName",
										value : ''
									},{
										field : "searchStartTime",
										value : ''
									},{
										field : "searchEndTime",
										value : ''
									},{
										field : "importStatus",
										value : '0'
									}]
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
											field : "importDate",
											title : "上传时间"
										},
										{
											field : "fileName",
											title : "文件名称",
											width : 280
										},
										{
											field : "importStatus",
											title : "导入状态",
											template: function(dataItem) {
												if(dataItem.importStatus == 1){
													return "导入成功";
												}else if(dataItem.importStatus == 2){
													return "部分数据导入失败,请重新导入！";
												}else if(dataItem.importStatus == 3){
													return "导入失败,没有操作权限！";
												}
											}
										},
										{
											field : "addCount",
											title : "新增数目"
										},
										{
											field : "updateCount",
											title : "更新数目"
										},
										{
											field : "failCount",
											title : "失败数目"
										},
										{
											field : "operator",
											title : "操作者",
											template:function(dataItem){
												if(dataItem.operator){
													return dataItem.operator.realName;
												}else{
													return "";
												}
											}
											
										},
										{
											title : "操作",
											width : 67,
											template : kendo.template($("#detailScan").html())
										}],
								dataBound : function() {
									projectImportGridObj.projectImportDetailClick();
									projectImportGridObj.setRowColor();
								},
								editable : "popup"
							}).data("kendoGrid");
			projectImportGridObj.templateDownloadBtnClick();
			projectImportGridObj.downloadBtnClick();
			projectImportGridObj.cancelDownloadClick();
//			projectImportGridObj.projectImportBtnClick();
//			projectImportGridObj.importBtnClick();
//			projectImportGridObj.cancelImportBtnClick();
		},
		
		//设置行的背景颜色
		setRowColor : function(){
			var data = projectImportGridObj.projectImportGrid.dataSource.data();
			$.each(data, function (i, row) {
				if (row.importStatus == 2){
					$('tr[data-uid="' + row.uid + '"] ').css("background-color", "#FFC0CB");
				}else if (row.importStatus == 3){
					$('tr[data-uid="' + row.uid + '"] ').css("background-color", "#DA70D6");
				}
			})
		},
		
		//查看项目导入明细
		projectImportDetailClick: function() {
			$(".projectImportDetail").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = projectImportGridObj.projectImportGrid.dataItem(tr);
				projectImportDetailWindow.obj.setOptions({"title":"项目导入明细查看 - " + data.fileName});
				projectImportDetailWindow.obj.center().open();
				projectImportDetailWindow.showDetail(data);
			});
		},

		// 点击工具栏［模版下载］按钮，显示弹窗
		templateDownloadBtnClick : function() {
			$("#templateDownloadBtn").on("click",function() {
				lock = false;
				$("#templates").data("kendoDropDownList").select(0);
				templateDownloadWindow.obj.title("模版下载");
				templateDownloadWindow.obj.center().open();
			});
		},

		// 选择模版后，点击［下载模版］按钮进行下载
		downloadBtnClick : function() {
			$("#downloadBtn").on("click", function(e) {
				if(!templateDownloadWindow.validator.validate()){
					return;
				}
				if(lock == true){
					return;
				}
				lock = true;
				var templateName = $("#templates").val();
				var saveAsFileName = $("#saveAsFileName").val() == '' ? templateName : $("#saveAsFileName").val();
				
				$.fileDownload("projectImport/downloadTemplate/"+templateName+"/"+saveAsFileName, {
				    successCallback: function () {
				    	infoTip({
							content : "下载成功",
							color : "#D58512"
						});
				    	lock = false;
				    },
				    failCallback: function () {
				    	infoTip({ 
							content : "下载失败!",
							color : "#FF0000"
						});
						lock = false;
				    }
				});
				templateDownloadWindow.obj.close();
			});
		},
		// 取消下载模版
		cancelDownloadClick : function() {
			$("#cancelDownloadBtn").on("click", function() {
				templateDownloadWindow.obj.close();
			});
		},
		
		// 点击工具栏［项目导入］按钮，显示弹窗
		projectImportBtnClick : function() {
			$("#projectImportBtn").on("click",function() {
				lock = false;
				projectImportWindow.obj.center().open();
			});
		},

		// 选择导入文件后，点击［项目导入］按钮开始导入
		importBtnClick : function() {
			$("#importBtn").on("click", function(e) {
				if(!projectImportWindow.validator.validate()){
					return;
				}
				if(lock == true){
					return;
				}
				lock = true;
//				var templateName = $("#templates").val();
				$("#fileupload").fileupload({
					dataType:'json',
					done: function(e, data){
						$.each(data.result.files, function (index, file) {
			                $('<p/>').text(file.name).appendTo(document.body);
			            });
						projectImportWindow.obj.close();
					}
				});
				
			});
		},
		// 取消［项目导入］
		cancelImportBtnClick : function() {
			$("#cancelImportBtn").on("click", function() {
				projectImportWindow.obj.close();
			});
		}
	};
	// 项目模版下载窗口
	templateDownloadWindow = {
		obj : undefined,

		id : $("#templateDownloadWindow"),
		
		validator : undefined,

		init : function() {
			if (!templateDownloadWindow.id.data("kendoWindow")) {
				templateDownloadWindow.id.kendoWindow({
					width : "500px",
					actions : ["Close"],
					modal : true,
					title : "模版下载",
					animation: {
			        	close: {
			        		effects: "fadeOut zoom:out",
			        		duration: 300
			        	},
			        	open: {
			        		effects: "fadeIn zoom:in",
			        		duration: 300
			        	}
			        }
				});
			};
			templateDownloadWindow.obj = templateDownloadWindow.id.data("kendoWindow");
			
			//初始化模版下拉列表
			$("#templates").kendoDropDownList({
		        dataTextField: "templateName",
		        dataValueField: "templateName",
		        dataSource: {
		            transport: {
		                read: {
		                	type : "get",
		                    dataType: "json",
		                    url: "projectImport/getTemplateList",
		                    contentType : "application/json;charset=UTF-8"
		                }
		            }
		        },
		        select: function(e) {
		        	$("#saveAsFileName").val(this.value());
		        },
		        dataBound: function(e) {
		        	$("#templates").data("kendoDropDownList").trigger("select");
		        }
		    });
			//项目模版下载验证
			templateDownloadWindow.validator = $("#templateDownloadValidate").kendoValidator().data("kendoValidator");
		}
	};
	
	// 项目文件导入窗口
	projectImportWindow = {
		obj : undefined,
		
		id : $("#projectImportWindow"),
		
		validator : undefined,
		
		init : function() {
			if (!projectImportWindow.id.data("kendoWindow")) {
				projectImportWindow.id.kendoWindow({
					width : "500px",
					actions : ["Close"],
					modal : true,
					title : "项目导入"
				});
			};
			projectImportWindow.obj = projectImportWindow.id.data("kendoWindow");
			//项目导入信息验证
			projectImportWindow.validator = $("#projectImportValidate").kendoValidator().data("kendoValidator");
		}
	};
	
	// 项目文件导入窗口
	projectImportDetailWindow = {
		projectImportDetailGrid : {},
			
		obj : undefined,
		
		id : $("#projectImportDetailWindow"),
		
		validator : undefined,
		
		init : function() {
			if (!projectImportDetailWindow.id.data("kendoWindow")) {
				projectImportDetailWindow.id.kendoWindow({
					width : "1000px",
					maxHeight: "800px",
					actions : ["Maximize", "Close"],
					modal : true,
					title : "项目导入明细查看"
				});
			};
			projectImportDetailWindow.projectImportDetailWindowBtnClick();
			projectImportDetailWindow.obj = projectImportDetailWindow.id.data("kendoWindow");
			//项目导入信息验证
			projectImportDetailWindow.validator = $("#projectImportDetailValidate").kendoValidator().data("kendoValidator");
		},
		
		showDetail: function(data){
			var projectImportId = data.id;
			this.projectImportDetailGrid = $("#projectImportDetailList")
			.kendoGrid(
					{
						dataSource : {
							transport : {
								read : {
									type : "get",
									url : "projectImport/searchDetail/"+projectImportId,
									dataType : "json",
									contentType : "application/json;charset=UTF-8"
								},
								parameterMap : function(data, type) {
									return {
										searchName : data.filter.filters[0].value,
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
								field : 'searchName',
								value : ''
							}
						},

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
						    		field : "rowNumber",
						    		title : "序号",
						    		hidden : true
						    	},
								{
									field : "referenceNumber",
									title : "文号",
						    		width : 130
								},
								{
									field : "projectNumber",
									title : "项目唯一编号",
						    		width : 140
								},
								{
									field : "fundYear",
									title : "资金年度",
						    		width : 100
								},
								{
									field : "city",
									title : "地州名",
									width : 90
								},
								{
									field : "countyLevelCity",
									title : "县名",
									width : 90
								},
								{
									field : "town",
									title : "乡镇名",
									width : 100
								},
								{
									field : "village",
									title : "村名",
									width : 100
								},
								{
									field : "subjectName",
									title : "专项名称",
									width : 100
								},
								{
									field : "fundType",
									title : "资金类明细",
									width : 110
								},
								{
									field : "projectName",
									title : "项目名称",
//									locked : true,
									width : 150
								},
								{
									field : "approvalNumber",
									title : "批复文号",
									width : 160
								},
								{
									field : "totalFund",
									title : "总资金(万元)",
									width : 140
								},
								{
									field : "financeFund",
									title : "财政资金(万元)",
									width : 140
								},
								{
									field : "selfFinancing",
									title : "自筹资金(万元)",
									width : 140
								},
								{
									field : "integrateFund",
									title : "整合资金(万元)",
									width : 140
								},
								{
									field : "coveredFarmerNumber",
									title : "扶持总农户数(户)",
									width : 160
								},
								
								{
									field : "povertyGeneralFarmer",
									title : "扶持一般农户数(户)",
									width : 170
								},
								{
									field : "povertyStrickenFarmerNumber",
									title : "扶持贫困农户数(户)",
									width : 170
								},
								{
									field : "povertyLowIncomeFarmer",
									title : "扶持低收入困难农户数(户)",
									width : 200
								},
								
								{
									field : "coveringNumber",
									title : "扶持总人口数(户)",
									width : 150
								},
								{
									field : "povertyGeneralPeople",
									title : "扶持一般人口数(人)",
									width : 170
								},
								{
									field : "povertyStrickenPeopleNumber",
									title : "扶持贫困人口数(人)",
									width : 170
								},
								{
									field : "povertyLowIncomePeople",
									title : "扶持低收入困难人口数(人)",
									width : 200
								},
								{
									field : "scaleAndContent",
									title : "财政资金建设规模及内容",
									width : 200
								},
								{
									field : "carryOutUnit",
									title : "实施单位",
									width : 120
								},
								{
									field : "chargePerson",
									title : "项目负责人",
									width : 120
								},
								{
									field : "deadline",
									title : "完成期限(月)",
									width : 120
								},
								{
									field : "standbyNumber",
									title : "后补文号",
									width : 120
								},
								{
									field : "fundToCountry",
									title : "到县资金(万元)",
									width : 130
								},
								{
									field : "approveState",
									title : "审核状态",
									width : 100
								},
								{
									field : "createUser",
									title : "创建用户",
									width : 100
								},
								{
									field : "createTime",
									title : "创建时间",
									width : 160
								},
								{
									field : "inputStatus",
									title : "录入状态",
									width : 100
								},
								{
									field : "approvingDepartment",
									title : "当前审核部门",
									width : 120
								},
								{
									field : "validateStatus",
									title : "验证状态",
									template: function(dataItem) {
										if(dataItem.validateStatus == 0){
											return "通过";
										}else if(dataItem.validateStatus == -1){
											return "失败";
										}else if(dataItem.validateStatus == -2){
											return "警告";
										}else {
											return "";
										}
									},
									width : 100
								},
								{
									field : "importStatus",
									title : "导入状态",
									template: function(dataItem) {
										if(dataItem.importStatus == 1){
											return "新增";
										}else if(dataItem.importStatus == 2){
											return "修改";
										}else {
											return "";
										}
									},
									width : 100
								},
								{
									field : "validateDescription",
									title : "验证描述",
//									template : "#if(validateDescription){#<span data-tooltip='kendo-tooltip' title='#:validateDescription#'>#:validateDescription#</span>#}else{##}#",
									template : function(dataItem) {
										var content = "";
										if(dataItem.coverStatus == 0 && dataItem.repeatStatus == 1){
											content = "项目已存在,项目记录相同";
											
										}else if(dataItem.repeatStatus == 2 && dataItem.coverStatus == 0){
											content = "项目已存在,"+dataItem.validateDescription+"<a onClick=cover("+dataItem.projectNumber+","+dataItem.id+")>是否覆盖</a>";
										}else if(dataItem.coverStatus == 1 && dataItem.repeatStatus == 3){
											content = "此项目已被覆盖";
										}else {
											content = dataItem.validateDescription;
										}
										return "<span data-tooltip='kendo-tooltip' data-content='"+content+"' title='"+dataItem.validateDescription+"'>"+dataItem.validateDescription+"</span>";
									},
									width : 120
								}
								],
						dataBound : function() {
							projectImportDetailWindow.setRowColor();
							projectImportDetailWindow.initKendoTooltip();
						},
						editable : "popup"
					}).data("kendoGrid");
		},
		//鼠标悬停提示
		initKendoTooltip : function(){
			$("[data-tooltip='kendo-tooltip']").kendoTooltip({
                content: kendo.template($("#projectImportDetailTemplate").html()), 
                width: 300,
                position: "left"
            });		
	},
		
		// 取消下载模版
		projectImportDetailWindowBtnClick : function() {
			$("#closeProjectImportDetailWindowBtn").on("click", function() {
				projectImportDetailWindow.obj.close();
			});
		},
		//设置行的背景颜色
		setRowColor : function(){
			var data = projectImportDetailWindow.projectImportDetailGrid.dataSource.data();
			$.each(data, function (i, row) {
			  if (row.coverStatus == 1 && row.repeatStatus == 3){
				 $('tr[data-uid="' + row.uid + '"] ').css("background-color", "#FFC0CB");
			  }else if (row.repeatStatus == 1){
				 $('tr[data-uid="' + row.uid + '"] ').css("background-color", "#E3CF57");
			  }else if (row.repeatStatus == 2){
				 $('tr[data-uid="' + row.uid + '"] ').css("background-color", "#BBFFFF");
			  }else if (row.repeatStatus == 3){
				 $('tr[data-uid="' + row.uid + '"] ').css("background-color", "#FFCCFF");
			  }
			})
		}
	};
	lock = true;
	fileUploadObj = {
		init : function() {
			$("#fileupload").fileupload({
				type: 'POST',
		        url: 'projectImport/uploadFile',
		        dataType: 'json',
		        autoUpload: true,
		        acceptFileTypes: /(\.|\/)(xls|xlsx)$/i,
		        formAcceptCharset: 'utf-8',
		        maxFileSize: 10485760,// 10MB
		        messages: {
	                maxNumberOfFiles: '超出了文件个数限制',
	                acceptFileTypes: '只能上传Excel文件',
	                maxFileSize: '文件不能大于10M',
	                minFileSize: '文件太小'
	            }
			}).on('fileuploadadd', function (e, data) {
				  $(this).attr("disabled",true);
				  $("#importingMsg").html("<label class='uploadImgLoad'>上传中... 0%</label>");
			}).on('fileuploadprocessalways', function (e, data) {
				if(data.files.error){
					var errorMsg = '';
					$(this).removeAttr("disabled");
					if(data.files[0].error=='acceptFileTypes'){
						errorMsg = "<label class='error'>文件类型错误</label>";
					}else if(data.files[0].error=='maxFileSize'){
						errorMsg = "<label class='error'>文件不能大于10M</label>";
					}else {
						errorMsg = "<label class='error'>"+data.files[0].error+"</label>";
					} 
					$("#importingMsg").html(errorMsg);
				} 
			}).on('fileuploadprogressall', function (e, data) {
			    var $p = $("#importingMsg");
			    var progress = parseInt(data.loaded / data.total * 100, 10);
			    if($p.text()==''){
			    	$("#importingMsg").html("<label class='uploadImgLoad'>上传中... "+progress+"%</label>");
				}else{
				   console.info(progress);
				   $p.html("<label class='uploadImgLoad'>上传中... "+progress+"%</label>");
				   if(lock){
					   lock = false;
					   document.getElementById("info").style.display = "block";
					   $("#info .text").html("正在导入数据，请稍等。。。");
				   }
				   if(progress==100){
				      $p.fadeOut("slow",function(){
				    	  $("#importingMsg").text("");
					  });
				   }
				} 
			}).on('fileuploaddone', function (e, data) {
				if(data.result.result=='error'){
					$(this).removeAttr("disabled");
				}else if(data.result.result=='success'){
					$(this).parent().prepend($("<a href='#' >  删除</a>").attr("onclick","uploadImageAjaxDelete('image_ajax_delete.action?dbFileName="+data.result.dbFileName+"',this)").add("<br/>"))
					.prepend($("<img  width='140' height='90' border='0' />").attr("src",data.result.url))
					.prepend($("<input type='hidden' name="+data.result.hiddenName+" id="+data.result.hiddenName+" value='"+data.result.dbFileName+"' />"));
				}
			}).on('fileuploadalways', function (e, data) {
				lock = true;
				$(this).removeAttr("disabled");
				projectImportGridObj.projectImportGrid.dataSource.read();
				if(data.result.resultCode == 0){
					document.getElementById("info").style.display = "none";
					infoTip({
						content : data.result.message,
						color : "#D58512"
					});
				}else if(data.result.resultCode == -1){
					document.getElementById("info").style.display = "none";
					infoTip({
						content : data.result.message,
						color : "#FF0000"
					});
				}
				
			});
		}	
	};
	
	var searchProjectImport = function() {
		var fileName = $("#inputSearch").val();
		var searchStartTime = $("#searchStartTime").val();
		var searchEndTime = $("#searchEndTime").val();
		var importStatus = $("#importStatus").val();
		projectImportGridObj.projectImportGrid.dataSource.filter([{
			field : "fileName",
			value : fileName
		},{
			field : "searchStartTime",
			value : searchStartTime
		},{
			field : "searchEndTime",
			value : searchEndTime
		},{
			field : "importStatus",
			value : importStatus
		}]);
	};
	
	$('#searchBtn').on('click', searchProjectImport);
	
	$('#resetBtn').on('click', function() {
		$("#inputSearch").val('');
		$("#searchStartTime").val('');
		$("#searchEndTime").val('');
		$("#importStatus").data("kendoDropDownList").select(0);
		searchProjectImport();
	});

//	$('#inputSearch').on('keyup', function() {
//		var fileName = $("#inputSearch").val();
//		projectImportGridObj.projectImportGrid.dataSource.filter([{
//			field : "fileName",
//			value : fileName
//		}]);
//	});

	projectImportGridObj.init();
	templateDownloadWindow.init();
//	projectImportWindow.init();
	projectImportDetailWindow.init();
	fileUploadObj.init();
	
});

function cover(projectNumber,projectDetailId) {
	$.ajax({					
		cache:false,
		type : "GET",
		url : "projectsManage/getProject/"+projectNumber+"/"+projectDetailId,
		dataType : "json",
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
			infoTip({
				content : "覆盖成功",
				color : "#D58512"
			});
			//projectImportDetailWindow.projectImportDetailGrid.dataSource.read();
			projectImportDetailWindow.obj.center().close();
		},
		error : function(data) {
			infoTip({
				content : "覆盖失败!",
				color : "#FF0000"
			});
		}
	});	
}

