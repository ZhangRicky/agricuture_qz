﻿﻿﻿﻿﻿﻿﻿﻿$(function() {
	var projectsManageGridObj = {},areaObj={},projectsManageWindow={},labelStyle={},prefix="",
	showChecksWindow={},
	determineRangeWindow={},
	adjustmentRecordwindow={},
	showSupervisionsWindow={},
	updateProjectsManageWindow={},
	adjustmentProjectsManageWindow={},
	isSupervisionsFirstOpen=true,
	isChecksFirstOpen=true,projectId="",
	flag = false,
	pid="",
	j="",
	updatePid="",
	pTotalFund="",
	updateAllTotalFund="",
	areaId="",
	adjustmentPid="",
	tiaoXiangID = "",
	determinePid="",
	points=[],
	currPoints=[]
	subProject_id = "",
	projectAdjustmentData = "",
	addProjectTotalFund = "",
	updateProjectTotalFund = "",
	lock=false;
	/*财务全局*/
	var financeDetailsWindow = {}, baoZhangFinanceObjWindow = {},fianceInfoShowObjWindow = {};
	var subId = "";
	var financeID = "";		//记录财务记录ID
	var financeSum =0;		//记录添加的报账金额的值
	var proInfo = new Array();
	var projects_id = "";
	var approverStatus = "";	
	areaObj = {
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
							height: 630,
							columns: [
							    { field: "areaName",
							      title: "地区"
							     
							    }
							    ],
							dataBound : function() {
								areaObj.doubleClick();
								areaObj.projectsExportList();
						},
					}).data("kendoTreeList");
			},
			
			//单击地区查询项目列表
			doubleClick:function(){
				$("#areaList tr").on('click', function (e) {
					var tr =  $(e.target).closest("tr");
					var data = areaObj.areaGrid.dataItem(tr);
					areaId = data.id;
					searchProjects(areaId);
				})
			},
			
			/*【导出】按钮-zhanggx*/
			projectsExportList : function(){
				$("#exportList").on("click",function(){
					var total = projectsManageGridObj.projectsManageGrid.dataSource.total();					
					if(!total || total == 0){
						infoTip({
							content : "没有数据可以导出!",
							color : "#FF0000"
						});
						return;
					}
					$.messager.confirm("项目数据导出","确定要导出"+total+"条项目数据?",function(){
							lock = true;
						$.fileDownload("projectsManage/export", {
							successCallback: function (url) {								
								infoTip({
									content : "导出成功",
									color : "#D58512"
								});
								lock = false;
							},
							failCallback: function (url) {
								infoTip({
									content : "导出失败!",
									color : "#FF0000"
								});
								lock = false;
							}
						});	
					})	
				})	
			}
	},
	areaObj.init();
	
	// 列表
	projectsManageGridObj = {
		projectsManageGrid : undefined,
		init : function() {
			this.projectsManageGrid = $("#projectsManageList").kendoTreeList({
					dataSource:new kendo.data.TreeListDataSource({
						transport : {
							read : {
								cache: false,
								type : "get",
								url : "projectsManage/searchs",
								dataType : "json",
								contentType : "application/json;charset=UTF-8"
							},
							parameterMap : function(data, type) {
								return {
									areaId: data.filter.filters[0].value,
									projectName : data.filter.filters[1].value,
									fundYear : data.filter.filters[2].value,
									projectType : data.filter.filters[3].value
								};
							}
						},
						batch: true,
						schema: {
							model: {
								id: "id",
								fields: {
									id: { type: "number", editable: false, nullable: false},
									parentId: { type:"number",field: "parentId", nullable: true },
									treeLevel: { type: "string" }
								},
								//total : "totalCount",
								//data : "items",
								expanded: true
							}
						},
						serverFiltering : true,
						filter : [{
							field : 'areaId',
							value : ''
						},{
							field : 'projectName',
							value : ''
						},{
							field : 'fundYear',
							value :  $("#fundYear_select").val()
						},{
							field : 'projectType',
							value : ''
						}],
						requestStart: function(e) {
							lock = true;
						},
						requestEnd: function(e) {
							lock = false;
						}
					}),
					
					toolbar : kendo.template($("#toolBarTemplate").html()),
					 
					reorderable : true,

					resizable : true,

					sortable : true,   

					columnMenu : true,
					 
					height: 560,

					editable : "popup",
					columns: [
					    { field: "treeLevel",width:220, title: "项目名称",locked:true, 
					    	template : function(dataItem){
					    		return dataItem.treeLevel+" "+dataItem.projectName;
					    	}
					    },
					    { field: "countyLevelCity", title: "项目地区", width: 150,
					    	template : function(dataItem){
					    		var areaDisplay = '';
					    		if (dataItem.countyLevelCityID) {
					    			areaDisplay = dataItem.countyLevelCityID.areaName;
								} 
					    		if (dataItem.townID) {
					    			areaDisplay = dataItem.townID.areaName;
					    		}
					    		if (dataItem.villageID) {
					    			areaDisplay += ' ' + dataItem.villageID.areaName;
					    		}
					    		return areaDisplay;
					    	}
					    },
					    { field: "projectType",width:120, title: "项目类型",
					    	template : function(dataItem){
					    		if(dataItem.projectType==1){
					    			return "种植";
					    		}else if(dataItem.projectType==2){
					    			return "基础论证";
					    		}else if(dataItem.projectType==3){
					    			return "培训";
					    		}else if(dataItem.projectType==4){
					    			return "贴息";
					    		}else if(dataItem.projectType==5){
					    			return "经济组织";
					    		}else if(dataItem.projectType==6){
					    			return "其他";
					    		}else{
					    			return "";
					    		}
					    	}
					    },
					    { field: "referenceNumber", title: "项目文号", width: 120},
					    { field: "carryOutUnit", title: "实施单位", width: 120},
					    { field: "chargePerson", title: "项目负责人/农户", width: 150,
					    	template:function(data){
						    	if(data.chargePerson == null){
						    		return data.farmerName;
						    	}else{
						    		return data.chargePerson;
						    	}
					      }
					    },
					    { field: "totalFund", title: "总资金(万)", width: 120},
					    { field: "scaleAndContent", title: "建设规模及内容", width: 200},
					   
					    { field: "bzTotal", 
					      title:"总报账金额",
					      width: 120,
					      template:function(data){
						    	if(data.bzTotal == null ||data.bzTotal == 0){
						    		return "";
						    	}else{
						    		return data.bzTotal;
						    	}
					      }
					    }, { field: "bkTotal", 
						      title:"总拨款金额",
						      width:120,
						      template:function(data){
							    if(data.bkTotal == null || data.bkTotal == 0){
							    	return "";
							    }else{
							    	return data.bkTotal;
							    }
						   }
						 }, 
						 { title:"项目进度", 
						      width:120,
						      template : function(dataItem){
						    	 if(dataItem.isLevel == 1) {
							    	  	if(dataItem.projectProcess == "" || dataItem.projectProcess == null ||dataItem.projectProcess == 0){
											return "<progress class='xiangmujindu' max='100' value='0' style='width:50px;'></progress>&nbsp;0%";
							    	  	}else{
							    	  		var projectProcess=Number(dataItem.projectProcess);
							    	  		return "<progress class='xiangmujindu' max='100' value='"+projectProcess+"' style='width:50px;'></progress>&nbsp;"+projectProcess+"%";
							    	  	}
						    	 }else{
						    		  return "";
						    	 } 
						      }
						 },
						 { title:"报账率", 
						      width:120,
						      template : function(dataItem){
						    	 if(dataItem.isLevel == 1) {
							    	  	if(dataItem.financebiLv == "" || dataItem.financebiLv == null ||dataItem.financebiLv == 0 || dataItem.totalFund == null){
											return "<progress class='baozhang' max='100' value='0' style='width:50px;'></progress>&nbsp;0%";
							    	  	}else{										
											var progress = (parseFloat(dataItem.financebiLv) / parseFloat(dataItem.totalFund)*100).toFixed(2);
											return "<progress class='baozhang' max='100' value='"+progress+"' style='width:50px;'></progress>&nbsp;"+progress+"%";
							    	  	}
						    	 }else{
						    		  return "";
						    	 }  	
						      }
						    },
					    {
					    	title : "项目调项及调项记录",
							width : 160,
							template : function(dataItem) {
								if(dataItem.isLevel == 1) {
									return $("#subProjectAdjustment").html();
								}else {
									return "";
								}
							}
						},
						{
							title : "确定项目范围",
							width : 110,
							template : function(dataItem) {
								if(dataItem.isLevel == 1) {
									return $("#determineProjectsRange").html();
								}else {
									return "";
								}
							}
						},
						{
							title : "监管记录",
							width : 90,
							template : function(dataItem) {
								if(dataItem.isLevel == 1) {
									return $("#showSupervisionsTemplate").html();
								}else {
									return "";
								}
							}
						},{
							title : "验收记录",
							width : 90,
							template : function(dataItem) {
								if(dataItem.isLevel == 1){
							        return $("#showChecksTemplate").html();
								}else {
									return "";
								}	
								
							}
						},
					    { title: "附件",
					      width: 80,
					      template:kendo.template($("#showFileUpload").html())
					    	  //"<a class='fileUpload editIcon aLink' title='附件'>附件<a/>"
					    },
					    { title: "农户信息导入",
						      width: 120,
						      template:kendo.template($("#showFileImport").html())
						    //"<a class='fileImport editIcon aLink' title='农户信息导入'>农户信息导入<a/>"
						 },
					    { title: "操作",
					      width : 130,
					      template : function(dateItem){
					    	  if(dateItem.level == 1){
					    		  return $("#projectsOperation1").html();
					    	  }else if(dateItem.isLevel != 0){
				    			  return $("#projectsOperation3").html();	  
					    	  }else{
					    		  return $("#projectsOperation2").html();
					    	  }
					      }
						  //template : kendo.template($("#projectsOperation").html())
						}],
					dataBound : function() {
						projectsManageGridObj.addProjectsClick();
						projectsManageGridObj.updateProjectsClick();
						projectsManageGridObj.saveProjectsClick();
						//projectsManageGridObj.saveUpdateProjectsClick();
						projectsManageGridObj.deleteProjectsClick();
						projectsManageGridObj.cancelUserClick();
						projectsManageGridObj.fileUploadClick();
						projectsManageGridObj.saveClick();
						projectsManageGridObj.projectsDetailClick();
						projectsManageGridObj.projectsFinacne();
						projectsManageGridObj.getSupervisionsClick();
						projectsManageGridObj.getChecksClick();
						projectsManageGridObj.setRowColor();
						//projectsManageGridObj.setFontColor();
						projectsManageGridObj.projectsAdjustment();
						projectsManageGridObj.saveProjectsAdjustment();
						projectsManageGridObj.projectsAdjustmentRecord();
						projectsManageGridObj.getDetermineRange();
						//projectsManageGridObj.saveDetermineRange();
						projectsManageGridObj.DetailClick();
						projectsManageGridObj.baoZhangClick();
						projectsManageGridObj.fileImportClick();
						projectsManageGridObj.cancelAdjustmentClick();
					},
				}).data("kendoTreeList");
			},
			
			/*单击报账率显示财务信息*/
			baoZhangClick : function(){
				$(".baozhang").on("click",function(e){
					var tr =  $(e.target).closest("tr");
					tr.find(".projectsFinance").click();		
				});
			},
			
			DetailClick : function(){
				$("#projectsManageList tr").on('dblclick', function (e){
					var tr =  $(e.target).closest("tr");
					tr.find(".projectsDetail").click();
				});
			},
			
			/*财务信息*/
			projectsFinacne : function(){
				$(".projectsFinance").on("click",function(e){
					var tr = $(e.target).closest("tr");
					var pdata = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					subId = pdata.id;	//传递项目id
					financeDetailsWindow.obj.center().open();
					financeDetailsWindow.showCount(pdata);
					financeDetailsWindow.financeChart(pdata);
					financeDetailsWindow.showDetailsList(pdata);
					
				});			
			},
			
			
			/*设置报账率的背景色*/
			setFontColor : function(){
				var data = projectsManageGridObj.projectsManageGrid.dataSource.data();
				$.each(data, function (i, row) {
					if(row.isLevel == 1){
						if (row.financebiLv == row.totalFund){
							$('tr[data-uid="' + row.uid + '"] td:nth-child(9)').css("background-color", "#FFC0CB");
						}else{
							$('tr[data-uid="' + row.uid + '"] td:nth-child(9)').css("background-color", "");
						}
					}
				})
			},
			
			
			//项目监管
			getSupervisionsClick:function(){
				$(".getSupervisions").on("click", function(e) {
					var tr =  $(e.target).closest("tr");
					var data = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					showSupervisionsWindow.obj.title("查看项目监管记录 - " + data.projectName );
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
					};
					showSupervisionsWindow.initSupervisionsMap(data);
				});
				
			},	
			
			//验收记录
			getChecksClick:function(){
				$(".getChecks").on("click", function(e) {
					if(lock==true){
						return;
					}
					lock = true;
					var tr =  $(e.target).closest("tr");
					var data = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					showChecksWindow.obj.title("查看项目验收记录 - " + data.projectName );
					showChecksWindow.obj.center().open();
					adjustmentPid = data.id;
					showChecksWindow.refreshChecksList(data.id);
					if(isChecksFirstOpen){
						isChecksFirstOpen = false;
						$("#checkHorizontal").kendoSplitter({
		                    panes: [
		                        { collapsible: false, size: '500px',min: '120px', max:'720px'},
		                        { collapsible: false}
		                    ]
		                });
					};
				});
			},
			
			//确定项目范围
			getDetermineRange:function(){
				$(".determineRange").on("click", function(e) {				
					var tr =  $(e.target).closest("tr");
					var rangeData = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					determinePid = rangeData.id;
					determineRangeWindow.obj.title(""+rangeData.projectName+" － 确定项目范围");
					determineRangeWindow.obj.center().open();
					determineRangeWindow.showRange(rangeData);
				});
			},
			
			
			fileImportClick : function() {
				//$(".fileImport").off("click");
				$(".fileImport").on("click",function(e){
					if(lock == true){
						return;
					}
					lock = true;
				//	$(".fileImport").off("click");
					var tr =  $(e.target).closest("tr");
					var data = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					Project_id = data.id;
					fileImportWindow.obj.center().open();
					fileImportWindow.showDetail(data);
					$("#fileupload1").fileupload({
						type: 'POST',
				        url: "farmerImport/uploadFarmer/"+Project_id,
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
						   }else{
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
						lock = false;
						if(data.result.result=='error'){
							$(this).removeAttr("disabled");
						}else if(data.result.result=='success'){
							$(this).parent().prepend($("<a href='#' >  删除</a>").attr("onclick","uploadImageAjaxDelete('image_ajax_delete.action?dbFileName="+data.result.dbFileName+"',this)").add("<br/>"))
							.prepend($("<img  width='140' height='90' border='0' />").attr("src",data.result.url))
							.prepend($("<input type='hidden' name="+data.result.hiddenName+" id="+data.result.hiddenName+" value='"+data.result.dbFileName+"' />"));
						}
					}).on('fileuploadalways', function (e, data) {
						//lock = true;
						
						$(this).removeAttr("disabled");
						
						if(data.result.resultCode == 0){
							if(lock == false){
								lock = true;
								document.getElementById("info").style.display = "none";
								infoTip({
									content : "导入成功",
									color : "#D58512"
								});
								//$(".fileImport").off("click");
								fileImportWindow.fileImportGrid.dataSource.read();
							}
							
						}else if(data.result.resultCode == -2){
							if(lock == false){
								lock = true;
								document.getElementById("info").style.display = "none";
								infoTip({
									content : data.result.message,
									color : "#FF0000"
								});
							}
						}else if(data.result.resultCode == -1){
							if(lock == false){
								lock = true;
								document.getElementById("info").style.display = "none";
								infoTip({
									content : data.result.message,
									color : "#FF0000"
								});
							}
						}
						
					});
					
					
				});
			},
			
//			fileFarmerClick : function() {
//				lock = true;
//				$("#fileupload1").fileupload({
//					type: 'POST',
//			        url: "farmerImport/uploadFarmer/"+subProject_id,
//			        dataType: 'json',
//			        autoUpload: true,
//			        acceptFileTypes: /(\.|\/)(xls|xlsx)$/i,
//			        formAcceptCharset: 'utf-8',
//			        maxFileSize: 10485760,// 10MB
//			        messages: {
//		                maxNumberOfFiles: '超出了文件个数限制',
//		                acceptFileTypes: '只能上传Excel文件',
//		                maxFileSize: '文件不能大于10M',
//		                minFileSize: '文件太小'
//		            }
//				}).on('fileuploadadd', function (e, data) {
//					  $(this).attr("disabled",true);
//					  $("#importingMsg").html("<label class='uploadImgLoad'>上传中... 0%</label>");
//				}).on('fileuploadprocessalways', function (e, data) {
//					if(data.files.error){
//						var errorMsg = '';
//						$(this).removeAttr("disabled");
//						if(data.files[0].error=='acceptFileTypes'){
//							errorMsg = "<label class='error'>文件类型错误</label>";
//						}else if(data.files[0].error=='maxFileSize'){
//							errorMsg = "<label class='error'>文件不能大于10M</label>";
//						}else {
//							errorMsg = "<label class='error'>"+data.files[0].error+"</label>";
//						} 
//						$("#importingMsg").html(errorMsg);
//					} 
//				}).on('fileuploadprogressall', function (e, data) {
//				    var $p = $("#importingMsg");
//				    var progress = parseInt(data.loaded / data.total * 100, 10);
//				    if($p.text()==''){
//				    	$("#importingMsg").html("<label class='uploadImgLoad'>上传中... "+progress+"%</label>");
//					}else{
//					   console.info(progress);
//					   $p.html("<label class='uploadImgLoad'>上传中... "+progress+"%</label>");
//					   if(lock){
//						   lock = false;
//						   document.getElementById("info").style.display = "block";
//						   $("#info .text").html("正在导入数据，请稍等。。。");
//					   }
//					   if(progress==100){
//					      $p.fadeOut("slow",function(){
//					    	  $("#importingMsg").text("");
//						  });
//					   }
//					} 
//				}).on('fileuploaddone', function (e, data) {
//					if(data.result.result=='error'){
//						$(this).removeAttr("disabled");
//					}else if(data.result.result=='success'){
//						$(this).parent().prepend($("<a href='#' >  删除</a>").attr("onclick","uploadImageAjaxDelete('image_ajax_delete.action?dbFileName="+data.result.dbFileName+"',this)").add("<br/>"))
//						.prepend($("<img  width='140' height='90' border='0' />").attr("src",data.result.url))
//						.prepend($("<input type='hidden' name="+data.result.hiddenName+" id="+data.result.hiddenName+" value='"+data.result.dbFileName+"' />"));
//					}
//				}).on('fileuploadalways', function (e, data) {
//					lock = true;
//					$(this).removeAttr("disabled");
//				//	projectImportGridObj.projectImportGrid.dataSource.read();
//					if(data.result.resultCode == 0){
//						document.getElementById("info").style.display = "none";
//						infoTip({
//							content : data.result.message,
//							color : "#D58512"
//						});
//					}else if(data.result.resultCode == -1){
//						document.getElementById("info").style.display = "none";
//						infoTip({
//							content : data.result.message,
//							color : "#FF0000"
//						});
//					}
//					
//				});
//			},
			// 公示附件
			fileUploadClick : function() {
				$(".fileUpload").on("click", function(e) {
					$("#picture1").html("");
					$("#fileName").html("");
					$("#pifupicture").html("");
					$("#pifufileName").html("");
					var tr =  $(e.target).closest("tr");
					var data = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					projectId = data.id;
					var shuoming = data.shuoming;
					$("#projectId").val(data.id);
					$("#explain").val(data.shuoming);
					deleteRemarkWindow.obj.title("附件");
					deleteRemarkWindow.obj.center().open();		
					$.ajax({
						cache:false,
						type : "GET",
						url : "attachmentUpload/searchPicture/"+projectId,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
					//	data : JSON.stringify(projectId),
						success : function(data, textStatus, jqXHR) {
							$("#picture1").html("");
							$("#fileName").html("");
							if(data == ""){
								 $("#picture1").html("请上传公示附件或图片");	
							}else{
							 for (var index in data) {
								
								if(index != "remove"){
									var fileName = data[index].fileName;
									prefix = fileName.substring(fileName.lastIndexOf(".")+1);
									var fName ;
									if(fileName.indexOf('\\')!=-1){
										fName = fileName.substring(fileName.lastIndexOf("\\")+1);
									}else{
										fName = fileName.substring(fileName.lastIndexOf("/")+1);
									}
									//var div = document.getElementById("pic");
									if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
										  var parentdiv = document.getElementById("fileName");
										  var div = document.createElement("div");
										  div.setAttribute("id", "fileName1");
										  div.setAttribute("class", "fileName2");
										  div.innerHTML = fName+"&nbsp;&nbsp<a class='uploadfiles'>下载文件</a>&nbsp;&nbsp<a class='deletefiles'>删除文件</a>";
										  div.fileName = data[index].fileName;
										  div.id = data[index].id;
										  parentdiv.appendChild(div);
										  uploadFile(div);
									}else{
											var div = document.getElementById("picture1");
											var img = document.createElement("img");
											img.setAttribute("id", "imgs");
											img.setAttribute("class","imgs1");
											img.width=130;
											img.height=100;
											img.src = data[index].fileName;
											img.pictureId = data[index].id;
											div.appendChild(img);
											mouseoverImg(img);
									}
								}
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
					$.ajax({
						cache:false,
						type : "GET",
						url : "attachmentUpload/searchFile/"+projectId,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
					//	data : JSON.stringify(projectId),
						success : function(data, textStatus, jqXHR) {
							$("#pifupicture").html("");
							$("#pifufileName").html("");
							 for (var index in data) {
								 if(data == ""){
									$("#pifupicture").html("请上传批复文件或图片");
									
								 }else{
								if(index != "remove"){
									var fileName = data[index].fileName;
									prefix = fileName.substring(fileName.lastIndexOf(".")+1);
									var fName ;
									if(fileName.indexOf('\\')!=-1){
										fName = fileName.substring(fileName.lastIndexOf("\\")+1);
									}else{
										fName = fileName.substring(fileName.lastIndexOf("/")+1);
									}
									//var div = document.getElementById("pic");
									if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
										  var parentdiv = document.getElementById("pifufileName");
										  var div = document.createElement("div");
										  div.setAttribute("id", "pifufileName1");
										  div.setAttribute("class", "pifufileName2");
										  div.innerHTML = fName+"&nbsp;&nbsp<a class='pifuuploadfiles'>下载文件</a>&nbsp;&nbsp<a class='deletepifufiles'>删除文件</a>";
										  div.fileName = data[index].fileName;
										  div.id = data[index].id;
										  parentdiv.appendChild(div);
										  uploadFile1(div);
									}else{
											var div = document.getElementById("pifupicture");
											var img = document.createElement("img");
											img.setAttribute("id", "imgs");
											img.setAttribute("class","pifuimgs1");
											img.width=130;
											img.height=100;
											img.src = data[index].fileName;
											img.pictureId = data[index].id;
											div.appendChild(img);
											mouseoverImg1(img);
									}
								}
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
				
				
				function uploadFile(div) {
					div.addEventListener("mouseover", function(e){
						$(".fileName2").off("click");
						$(".deletefiles").off("click");
						$(".uploadfiles").off("click");
						var fileNa = div.fileName;
						var id = div.id;
						$(".uploadfiles").on("click", function() {
							$.fileDownload("projectsManage/downloadTemplate/"+fileNa+"/"+prefix, {
							    successCallback: function () {
							    	infoTip({
										content : "下载成功",
										color : "#D58512"
									});
							    	
							    },
							    failCallback: function () {
							    	infoTip({ 
										content : "下载失败!",
										color : "#FF0000"
									});
									
							    }
							});
						});
						$(".deletefiles").on("click", function() {
							if(confirm("要删除此文件吗？")) {
								$.ajax({
									cache:false,
									type : "delete",
									url : "attachmentUpload/delete/"+id,
									dataType : "json",
									contentType : "application/json;charset=UTF-8",
									success : function(data, textStatus, jqXHR) {
										infoTip({
											content : "删除成功!",
											color : "#D58512"
										});
										$("#picture1").html("");
										$("#fileName").html("");
										$.ajax({
											cache:false,
											type : "get",
											url : "attachmentUpload/searchPicture/"+projectId,
											dataType : "json",
											contentType : "application/json;charset=UTF-8",
											success : function(data) {
												for (var index in data) {
													if(index != "remove"){
														var fileName = data[index].fileName;
														prefix = fileName.substring(fileName.lastIndexOf(".")+1);
														var fName ;
														if(fileName.indexOf('\\')!=-1){
															fName = fileName.substring(fileName.lastIndexOf("\\")+1);
														}else{
															fName = fileName.substring(fileName.lastIndexOf("/")+1);
														}
														if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
															  var parentdiv = document.getElementById("fileName");
															  var div = document.createElement("div");
															  div.setAttribute("id", "fileName1");
															  div.setAttribute("class", "fileName2");
															  div.innerHTML = fName+"&nbsp;&nbsp<a class='uploadfiles'>下载文件</a>&nbsp;&nbsp<a class='deletefiles'>删除文件</a>";
															  div.fileName = data[index].fileName;
															  div.id = data[index].id;
															  parentdiv.appendChild(div);
															  uploadFile(div);
															
														}else{
															var div = document.getElementById("picture1");
															var img = document.createElement("img");
															img.setAttribute("id", "imgs");
															img.getAttribute("id");
															img.setAttribute("class","imgs1");
															img.width=130;
															img.height=100;
															img.src = data[index].fileName;
															img.pictureId = data[index].id;
															div.appendChild(img);
															//div.appendChild(divpicture);
															mouseoverImg(img);
														}
													 }
													
												}
											},
											error : function(data, textStatus, jqXHR) {
												infoTip({
													content : "图片读取失败!",
													color : "#FF0000"
												});
											}
										});
									},
									error : function(data, textStatus, jqXHR) {
										infoTip({
											content : "删除失败!",
											color : "#FF0000"
										});
									}

								});
							
							};			
						});
//						$(".fileName2").on("click", function() {
//							
//							
//							//alert(prefix);
//							$.fileDownload("projectsManage/downloadTemplate/"+fileNa+"/"+prefix, {
//							    successCallback: function () {
//							    	infoTip({
//										content : "下载成功",
//										color : "#D58512"
//									});
//							    	
//							    },
//							    failCallback: function () {
//							    	infoTip({ 
//										content : "下载失败!",
//										color : "#FF0000"
//									});
//									
//							    }
//							});
//						});
					})
					
				};
				 function mouseoverImg(img){
					 img.addEventListener("mouseover", function(e){
						 $(".imgs1").off("click");
						 pictureId = img.pictureId;
						 $(".imgs1").kendoTooltip({
							 content: '<img src="'+img.src+'" style="width:480px;height:500px;"/>',
							// width: 50,
							 position: "left"
						 });
						 $(".imgs1").on("click", function() {
								if(confirm("要删除此图片吗？")) {
									$.ajax({
										cache:false,
										type : "delete",
										url : "attachmentUpload/delete/"+pictureId,
										dataType : "json",
										contentType : "application/json;charset=UTF-8",
										success : function(data, textStatus, jqXHR) {
											infoTip({
												content : "删除成功!",
												color : "#D58512"
											});
											$("#picture1").html("");
											$("#fileName").html("");
											$.ajax({
												cache:false,
												type : "get",
												url : "attachmentUpload/searchPicture/"+projectId,
												dataType : "json",
												contentType : "application/json;charset=UTF-8",
												success : function(data) {
													for (var index in data) {
														if(index != "remove"){
															var fileName = data[index].fileName;
															prefix = fileName.substring(fileName.lastIndexOf(".")+1);
															var fName ;
															if(fileName.indexOf('\\')!=-1){
																fName = fileName.substring(fileName.lastIndexOf("\\")+1);
															}else{
																fName = fileName.substring(fileName.lastIndexOf("/")+1);
															}
															if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
																  var parentdiv = document.getElementById("fileName");
																  var div = document.createElement("div");
																  div.setAttribute("id", "fileName1");
																  div.setAttribute("class", "fileName2");
																  div.innerHTML = fName+"&nbsp;&nbsp<a class='uploadfiles'>下载文件</a>&nbsp;&nbsp<a class='deletefiles'>删除文件</a>";
																  div.id = data[index].id;
																  parentdiv.appendChild(div);
																  uploadFile(div);
																
															}else{
																var div = document.getElementById("picture1");
																var img = document.createElement("img");
																img.setAttribute("id", "imgs");
																img.getAttribute("id");
																img.setAttribute("class","imgs1");
																img.width=130;
																img.height=100;
																img.src = data[index].fileName;
																img.pictureId = data[index].id;
																div.appendChild(img);
																//div.appendChild(divpicture);
																mouseoverImg(img);
															}
														 }
														
													}
												},
												error : function(data, textStatus, jqXHR) {
													infoTip({
														content : "图片读取失败!",
														color : "#FF0000"
													});
												}
											});
										},
										error : function(data, textStatus, jqXHR) {
											infoTip({
												content : "删除失败!",
												color : "#FF0000"
											});
										}

									});
								
								};
						   });
			            })
				};
				
				
				function uploadFile1(div) {
					div.addEventListener("mouseover", function(e){
						$(".pifufileName2").off("click");
						$(".deletepifufiles").off("click");
						$(".pifuuploadfiles").off("click");
						var fileNa = div.fileName;
						var id = div.id;
						$(".pifuuploadfiles").on("click", function() {
							$.fileDownload("projectsManage/downloadTemplate/"+fileNa+"/"+prefix, {
							    successCallback: function () {
							    	infoTip({
										content : "下载成功",
										color : "#D58512"
									});
							    	
							    },
							    failCallback: function () {
							    	infoTip({ 
										content : "下载失败!",
										color : "#FF0000"
									});
									
							    }
							});
						});
						$(".deletepifufiles").on("click", function() {
							if(confirm("要删除此文件吗？")) {
								$.ajax({
									cache:false,
									type : "delete",
									url : "attachmentUpload/deletePiFuFile/"+id,
									dataType : "json",
									contentType : "application/json;charset=UTF-8",
									success : function(data, textStatus, jqXHR) {
										infoTip({
											content : "删除成功!",
											color : "#D58512"
										});
										$("#pifupicture").html("");
										$("#pifufileName").html("");
										$.ajax({
											cache:false,
											type : "get",
											url : "attachmentUpload/searchFile/"+projectId,
											dataType : "json",
											contentType : "application/json;charset=UTF-8",
											success : function(data) {
												for (var index in data) {
													if(index != "remove"){
														var fileName = data[index].fileName;
														prefix = fileName.substring(fileName.lastIndexOf(".")+1);
														var fName ;
														if(fileName.indexOf('\\')!=-1){
															fName = fileName.substring(fileName.lastIndexOf("\\")+1);
														}else{
															fName = fileName.substring(fileName.lastIndexOf("/")+1);
														}
														if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
															  var parentdiv = document.getElementById("pifufileName");
															  var div = document.createElement("div");
															  div.setAttribute("id", "pifufileName1");
															  div.setAttribute("class", "pifufileName2");
															  div.innerHTML = fName+"&nbsp;&nbsp<a class='pifuuploadfiles'>下载文件</a>&nbsp;&nbsp<a class='deletepifufiles'>删除文件</a>";
															  div.fileName = data[index].fileName;
															  div.id = data[index].id;
															  parentdiv.appendChild(div);
															  uploadFile1(div);
															
														}else{
															var div = document.getElementById("pifupicture");
															var img = document.createElement("img");
															img.setAttribute("id", "imgs");
															img.getAttribute("id");
															img.setAttribute("class","pifuimgs1");
															img.width=130;
															img.height=100;
															img.src = data[index].fileName;
															img.pictureId = data[index].id;
															div.appendChild(img);
															//div.appendChild(divpicture);
															mouseoverImg1(img);
														}
													 }
													
												}
											},
											error : function(data, textStatus, jqXHR) {
												infoTip({
													content : "图片读取失败!",
													color : "#FF0000"
												});
											}
										});
									},
									error : function(data, textStatus, jqXHR) {
										infoTip({
											content : "删除失败!",
											color : "#FF0000"
										});
									}

								});
							
							};			
						});
//						$(".pifufileName2").on("click", function() {
//							var fileNa = div.fileName;
//							//alert(prefix);
//							$.fileDownload("projectsManage/downloadTemplate/"+fileNa+"/"+prefix, {
//							    successCallback: function () {
//							    	infoTip({
//										content : "下载成功",
//										color : "#D58512"
//									});
//							    	
//							    },
//							    failCallback: function () {
//							    	infoTip({ 
//										content : "下载失败!",
//										color : "#FF0000"
//									});
//									
//							    }
//							});
//						});
					})
					
				};
				 function mouseoverImg1(img){
					 img.addEventListener("mouseover", function(e){
						 $(".pifuimgs1").off("click");
						 pictureId = img.pictureId;
						 $(".pifuimgs1").kendoTooltip({
							 content: '<img src="'+img.src+'" style="width:480px;height:500px;"/>',
							// width: 50,
							 position: "left"
						 });
						 $(".pifuimgs1").on("click", function() {
								if(confirm("要删除此图片吗？")) {
									$.ajax({
										cache:false,
										type : "delete",
										url : "attachmentUpload/deletePiFuFile/"+pictureId,
										dataType : "json",
										contentType : "application/json;charset=UTF-8",
										success : function(data, textStatus, jqXHR) {
											infoTip({
												content : "删除成功!",
												color : "#D58512"
											});
											$("#pifupicture").html("");
											$("#pifufileName").html("");
											$.ajax({
												cache:false,
												type : "get",
												url : "attachmentUpload/searchFile/"+projectId,
												dataType : "json",
												contentType : "application/json;charset=UTF-8",
												success : function(data) {
													for (var index in data) {
														if(index != "remove"){
															var fileName = data[index].fileName;
															prefix = fileName.substring(fileName.lastIndexOf(".")+1);
															var fName ;
															if(fileName.indexOf('\\')!=-1){
																fName = fileName.substring(fileName.lastIndexOf("\\")+1);
															}else{
																fName = fileName.substring(fileName.lastIndexOf("/")+1);
															}
															if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
																  var parentdiv = document.getElementById("pifufileName");
																  var div = document.createElement("div");
																  div.setAttribute("id", "pifufileName1");
																  div.setAttribute("class", "pifufileName2");
																  div.innerHTML = fName+"&nbsp;&nbsp<a class='pifuuploadfiles'>下载文件</a>&nbsp;&nbsp<a class='deletepifufiles'>删除文件</a>";
																  div.fileName = data[index].fileName;
																  div.id = data[index].id;
																  parentdiv.appendChild(div);
																  uploadFile1(div);
																
															}else{
																var div = document.getElementById("pifupicture");
																var img = document.createElement("img");
																img.setAttribute("id", "imgs");
																img.getAttribute("id");
																img.setAttribute("class","pifuimgs1");
																img.width=130;
																img.height=100;
																img.src = data[index].fileName;
																img.pictureId = data[index].id;
																div.appendChild(img);
																//div.appendChild(divpicture);
																mouseoverImg1(img);
															}
														 }
														
													}
												},
												error : function(data, textStatus, jqXHR) {
													infoTip({
														content : "图片读取失败!",
														color : "#FF0000"
													});
												}
											});
										},
										error : function(data, textStatus, jqXHR) {
											infoTip({
												content : "删除失败!",
												color : "#FF0000"
											});
										}

									});
								
								};
						   });
			            })
				};
			},
			
			//项目查看详情
			projectsDetailClick: function() {
				$(".projectsDetail").on("click", function(e) {
					var tr =  $(e.target).closest("tr");
					var data = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					var areaId = data.area.id;
					var parentId = data.parentId;
					var areaDisplay = '';
		    		if (data.countyLevelCityID) {
		    			areaDisplay = data.countyLevelCityID.areaName;
					} 
		    		if (data.townID) {
		    			areaDisplay = data.townID.areaName;
		    		}
		    		if (data.villageID) {
		    			areaDisplay += ' ' + data.villageID.areaName;
		    		}
			        if(data.parentId == null){
						document.getElementById("zhuxiangmu").style.display = "block";
						document.getElementById("zixiangmu").style.display = "none";
						$("#integrateFund").html(data.integrateFund);
						$("#financeFund").html(data.financeFund);
						$("#selfFinancing").html(data.selfFinancing);
						$("#referenceNumber").html(data.referenceNumber);
						$("#projectNumber").html(data.projectNumber);
						$("#fundYear").html(data.fundYear);
						$("#countyLevelCity").html(areaDisplay);
						$("#farmer").html(data.farmerName);
						$("#subjectName").html(data.subjectName);
						$("#fundType").html(data.fundType);
						$("#projectName").html(data.projectName);
						$("#approvalNumber").html(data.approvalNumber);
						$("#totalFund").html(data.totalFund);
						$("#povertyStrickenFarmerNumber").html(data.povertyStrickenFarmerNumber+"户");
						$("#povertyStrickenPeopleNumber").html(data.povertyStrickenPeopleNumber+"人");
						$("#povertyGeneralFarmer").html(data.povertyGeneralFarmer==null ? "0户" : data.povertyGeneralFarmer+"户");
						$("#povertyLowIncomeFarmer").html(data.povertyLowIncomeFarmer==null?"0户" : data.povertyLowIncomeFarmer+"户");
						$("#povertyGeneralPeople").html(data.povertyGeneralPeople==null?"0人":data.povertyGeneralPeople+"人");
						$("#povertyLowIncomePeople").html(data.povertyLowIncomePeople==null?"0人":data.povertyLowIncomePeople+"人");
						$("#scaleAndContent").html(data.scaleAndContent);
						$("#carryOutUnit").html(data.carryOutUnit);
						$("#chargePerson").html(data.chargePerson);
						$("#standbyNumber").html(data.standbyNumber);
						$("#deadline").html(data.deadline+"月");
						$("#fundToCountry").html(data.fundToCountry);
						$("#coveredFarmerNumber").html(data.coveredFarmerNumber+"户");
						$("#coveringNumber").html(data.coveringNumber+"人");
						$("#approveState").html(data.approveState==0?"正在备案":(data.approveState==1?"备案通过":(data.approveState==2?"备案不通过":"")));
						$("#approvingDepartment").html(data.approvingDepartment);
						$("#constructionMode").html(data.constructionMode==0?"先建后款":(data.constructionMode==1?"先款后建":""));
						$("#projectType").html(data.projectType==1?"种植":(data.projectType==2?"基础论证":(data.projectType==3?"培训":(data.projectType==4?"贴息":(data.projectType==5?"经济组织":(data.projectType==6?"其它":""))))));
					}else{
						document.getElementById("zhuxiangmu").style.display = "none";
						document.getElementById("zixiangmu").style.display = "block";
						$("#ziprojectName").html(data.projectName);
						$("#zireferenceNumber").html(data.referenceNumber);
						$("#zicarryOutUnit").html(data.carryOutUnit);
						$("#zitotalFund").html(data.totalFund);
						$("#zifarmerName").html(data.farmerName);	
						$("#ziscaleAndContent").html(data.scaleAndContent);
						$("#zisubjectName").html(data.subjectName);
						//$("#zipath").html(data.path);
						$("#ziconstructionMode").html(data.constructionMode==0?"先建后款":(data.constructionMode==1?"先款后建":""));
						$("#ziprojectType").html(data.projectType==1?"种植":(data.projectType==2?"基础论证":(data.projectType==3?"培训":(data.projectType==4?"贴息":(data.projectType==5?"经济组织":(data.projectType==6?"其它":""))))));
						$("#zicoveredFarmerNumber").html(data.coveredFarmerNumber+"户");
						$("#zicoveringNumber").html(data.coveringNumber+"人");
						$("#zipovertyStrickenFarmerNumber").html(data.povertyStrickenFarmerNumber+"户");
						$("#zipovertyStrickenPeopleNumber").html(data.povertyStrickenPeopleNumber+"人");
						$("#zipovertyGeneralFarmer").html(data.povertyGeneralFarmer==null ? "0户" : data.povertyGeneralFarmer+"户");
						$("#zipovertyLowIncomeFarmer").html(data.povertyLowIncomeFarmer==null?"0户" : data.povertyLowIncomeFarmer+"户");
						$("#zipovertyGeneralPeople").html(data.povertyGeneralPeople==null?"0人":data.povertyGeneralPeople+"人");
						$("#zipovertyLowIncomePeople").html(data.povertyLowIncomePeople==null?"0人":data.povertyLowIncomePeople+"人");
						$("#zideadline").html(data.deadline+"月");
						$("#remark").html(data.remark);
						$("#ziCity").html(areaDisplay);
						$.ajax({
							cache:false,
							type : "GET",
							url : "projectsManage/sreachParent/"+parentId,
							dataType : "json",
							contentType : "application/json;charset=UTF-8",
							//data : JSON.stringify(projects),
							success : function(data, textStatus, jqXHR) {
								$("#zifundYear").html(data[0].fundYear);
								$("#zifundType").html(data[0].fundType);
								$("#ziapprovalNumber").html(data[0].approvalNumber);
								$("#zichargePerson").html(data[0].chargePerson);
								$("#zifundToCountry").html(data[0].fundToCountry);
							},
							error : function(data, textStatus, jqXHR) {
								infoTip({
									content : "查看失败!",
									color : "#FF0000"
								});
							}
						});
//						$.ajax({
//							cache:false,
//							type : "GET",
//							url : "projectsManage/sreachArea/"+areaId,
//							dataType : "json",
//							contentType : "application/json;charset=UTF-8",
//							//data : JSON.stringify(projects),
//							success : function(data, textStatus, jqXHR) {
//								if(data[1] == null && data[2] == null){
//									$("#ziCity").html(data[0].areaName);
//								}else if(data[2] == null){
//									//$("#zitown").html(data[0].areaName);
//									$("#ziCity").html(data[1].areaName+data[0].areaName);
//								}else {
//									$("#ziCity").html(data[0].areaName+data[2].areaName+data[1].areaName);
//									//$("#zitown").html(data[2].areaName);
//									//$("#zivillage").html(data[1].areaName);
//								}
//								
//							},
//							error : function(data, textStatus, jqXHR) {
//								infoTip({
//									content : "查看失败!",
//									color : "#FF0000"
//								});
//							}
//						});
				}
					projectDetailWindow.obj.title("项目详情-"+data.projectName);
					projectDetailWindow.obj.center().open();
//					projectDetailWindow.showDetail(data);
				});
			},
			
			//公示说明保存
			saveClick : function() {
				$("#saveRemark").off("click");
				$("#saveRemark").on("click", function(e) {
					var projects = {},url = "";
					projects.id=$("#projectId").val();
					projects.shuoming=$("#explain").val();
					
					url = "projectsManage/add";
					$.ajax({
						cache:false,
						type : "POST",
						url : url,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						data : JSON.stringify(projects),
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
							projectsManageGridObj.projectsManageGrid.dataSource.read();	
							
						},
						error : function(data, textStatus, jqXHR) {
							infoTip({
								content : "保存失败!",
								color : "#FF0000"
							});
						}
					});
					deleteRemarkWindow.obj.close();
					$(".k-upload-files").html("");
				});
			},
			
			//添加子项目
			addProjectsClick:function(){
				$(".addProjects").on("click",function(e) {
							var tr =  $(e.target).closest("tr");
							var data = projectsManageGridObj.projectsManageGrid.dataItem(tr);
							var areaId = data.area.id;
							pid = data.id;
							addProjectTotalFund = data.totalFund;
	//						alert(pid);
							$.ajax({
					            type: "post", 
					            contentType : "application/json;charset=UTF-8",
					            url: "projectsManage/findTotalFund/"+pid,
					            dataType : "json",
					          //  data : JSON.stringify(projects),
					            success : function(data, textStatus, jqXHR) {
					            	j = data[0].allTotalFund;
								},
								error : function(data, textStatus, jqXHR) {
									//lock = false;
									infoTip({
										content : "查找失败!",
										color : "#FF0000"
									});
								},
							});	
						
							//alert(projectTotalFund);
							$("#pid,#projectName_add,#referenceNumber_add,#carryOutUnit_add,#totalFund_add,#farmerName_add,#scaleAndContent_add,#subjectName_add,#constructionMode_add,#projectType_add,#coveredFarmerNumber_add,#coveringNumber_add,#povertyStrickenFarmerNumber_add,#povertyStrickenPeopleNumber_add,#povertyGeneralFarmer_add,#povertyLowIncomeFarmer_add,#povertyGeneralPeople_add,#povertyLowIncomePeople_add,#deadline_add,#remark_add").val("");
							$("#fundYearAdd_add").val(data.fundYear).attr("readonly",true);
							$('#scaleAndContent_add').attr("readonly",false);	
							var areaList=$("#area_add").kendoDropDownList({
								dataTextField: "areaName",
						        dataValueField: "id",
						        dataSource: {
						            transport: {
						                read: {
						                	cache:false,
						                	dataType : "json",
											url : "area/getAreaChild/"+areaId,
						                }
						            }
						        },
						    });
							$('#area_add').data("kendoDropDownList").select(-1);
							
							projectsManageWindow.obj.title("添加子项目");
							projectsManageWindow.obj.center().open();
				});
			},
			
			//设置行的背景颜色
			setRowColor : function(){
				var data = projectsManageGridObj.projectsManageGrid.dataSource.data();
				$.each(data, function (i, row) {
				  if (!row.parentId || row.parentId == null){
					 $('tr[data-uid="' + row.uid + '"] ').css("background-color", "rgb(224,224,224)");
				  }
				})
			},
			
			//编辑项目
			updateProjectsClick:function(){
				$(".editProjects").on("click",function(e) {
					var tr =  $(e.target).closest("tr");
					var pdata = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					var areaId = pdata.area.id;
					updatePid = pdata.id;
					updateParentId = pdata.parentId;
					updateProjectTotalFund = pdata.totalFund;
					
					if(updateParentId == null){
						updateProjectTotalFund = pdata.totalFund;
					}else{
						$.ajax({
				            type: "post", 
				            contentType : "application/json;charset=UTF-8",
				            url: "projectsManage/findTotalFund/"+updateParentId,
				            dataType : "json",
				          //  data : JSON.stringify(projects),
				            success : function(data, textStatus, jqXHR) {
				            	updateAllTotalFund = data[0].allTotalFund;
							},
							error : function(data, textStatus, jqXHR) {
								//lock = false;
								infoTip({
									content : "查找失败!",
									color : "#FF0000"
								});
							},
						});	
						$.ajax({
				            type: "get", 
				            contentType : "application/json;charset=UTF-8",
				            url: "projectsManage/findProjectsTotalFund/"+updateParentId,
				            dataType : "json",
				          //  data : JSON.stringify(projects),
				            success : function(data, textStatus, jqXHR) {
				            	pTotalFund = data.totalFund;
							},
							error : function(data, textStatus, jqXHR) {
								//lock = false;
								infoTip({
									content : "查找失败!",
									color : "#FF0000"
								});
							},
						});	
					}
					var updateAreaList=$("#area_add").kendoDropDownList({
						dataTextField: "areaName",
				        dataValueField: "id",
				        dataSource: {
				            transport: {
				                read: {
				                	cache:false,
				                	dataType : "json",
									url : "area/getAreaChild/"+areaId,
				                }
				            }
				        },
				    });
					$('#area_add').data("kendoDropDownList").select(-1);
					
					$('#fundYearAdd_add').attr("readonly",true)
					$('#scaleAndContent_add').attr("readonly",true);				

					$("#pid").val(updatePid);
					$("#projectName_add").val(pdata.projectName);
					$("#referenceNumber_add").val(pdata.referenceNumber);
					$("#carryOutUnit_add").val(pdata.carryOutUnit);
					$("#totalFund_add").val(pdata.totalFund);
					$("#farmerName_add").val(pdata.farmerName);
					$("#scaleAndContent_add").val(pdata.scaleAndContent);
					$("#subjectName_add").val(pdata.subjectName);
					$("#constructionMode_add").val(pdata.constructionMode);
					$("#projectType_add").val(pdata.projectType);
					$("#coveredFarmerNumber_add").val(pdata.coveredFarmerNumber);
					$("#coveringNumber_add").val(pdata.coveringNumber);
					$("#povertyStrickenFarmerNumber_add").val(pdata.povertyStrickenFarmerNumber);
					$("#povertyStrickenPeopleNumber_add").val(pdata.povertyStrickenPeopleNumber);
					$("#povertyGeneralFarmer_add").val(pdata.povertyGeneralFarmer==null?"0":pdata.povertyGeneralFarmer);
					$("#povertyLowIncomeFarmer_add").val(pdata.povertyLowIncomeFarmer==null?"0":pdata.povertyLowIncomeFarmer);
					$("#povertyGeneralPeople_add").val(pdata.povertyGeneralPeople==null?"0":pdata.povertyGeneralPeople);
					$("#povertyLowIncomePeople_add").val(pdata.povertyLowIncomePeople==null?"0":pdata.povertyLowIncomePeople);
					$("#deadline_add").val(pdata.deadline);
					$("#remark_add").val(pdata.remark);
					$("#fundYearAdd_add").val(pdata.fundYear);
					
					projectsManageWindow.obj.title("编辑子项目");
					projectsManageWindow.obj.center().open();
				});
			},
			
			//项目调项
			projectsAdjustment:function(){
				$(".subProjectAdjustment").on("click",function(e) {
					var tr =  $(e.target).closest("tr");
					projectAdjustmentData = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					var subPdata = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					var areaId = subPdata.area.id;			
					adjustmentPid = subPdata.id;
					tiaoXiangID =subPdata.id;
					$("#adjustmentPid").val(adjustmentPid);
					$("#projectName2").val(subPdata.projectName);
					$("#referenceNumber2").val(subPdata.referenceNumber);
					$("#carryOutUnit2").val(subPdata.carryOutUnit);
					$("#totalFund2").val(subPdata.totalFund);
					var adjustmentAreaList=$("#adjustmentArea").kendoDropDownList({
						dataTextField: "areaName",
				        dataValueField: "id",
				        dataSource: {
				            transport: {
				                read: {
				                	cache:false,
				                	dataType : "json",
									url : "area/getAreaChild/"+areaId,
				                }
				            }
				        },
				    });
					$('#adjustmentArea').data("kendoDropDownList").select(-1);
					$("#farmerName2").val(subPdata.farmerName);
					$("#scaleAndContent2").val(subPdata.scaleAndContent);
					$("#subjectName2").val(subPdata.subjectName);
					document.getElementById("adjustmentConstructionMode").disabled = true;
					$("#adjustmentConstructionMode").val(subPdata.constructionMode);
					document.getElementById("adjustmentProjectType").disabled = true;
					$("#adjustmentProjectType").val(subPdata.fundType);
					$("#coveredFarmerNumber2").val(subPdata.coveredFarmerNumber);
					$("#coveringNumber2").val(subPdata.coveringNumber);
					$("#povertyStrickenFarmerNumber2").val(subPdata.povertyStrickenFarmerNumber);
					$("#povertyStrickenPeopleNumber2").val(subPdata.povertyStrickenPeopleNumber);
					$("#deadline2").val(subPdata.deadline);
					$("#remark2").val(subPdata.remark);
					$("#adjustmentReason").val("");
					adjustmentProjectsManageWindow.validator.validate();
					adjustmentProjectsManageWindow.obj.title("项目调项");
					adjustmentProjectsManageWindow.obj.center().open();
				});
			},
			
			//调项记录
			projectsAdjustmentRecord:function(){
				$(".subProjectAdjustmentRecord").on('click', function (e) {
					var tr =  $(e.target).closest("tr");
					var subPdata = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					adjustmentRecordwindow.validator.validate();
					adjustmentRecordwindow.obj.title("项目调项记录");
					adjustmentRecordwindow.obj.center().open();
					adjustmentRecordwindow.showAdjustmentRecord(subPdata);
				})
				
			},
			//保存调项
			saveProjectsAdjustment:function(){
				$("#adjustmentProjects").off("click");
				$("#adjustmentProjects").on("click",function(){
					if(!adjustmentProjectsManageWindow.validator.validate()){
						lock = false;
						return;
					}		
					var p = {};
					if(lock == true){
						return;
					}
					lock = true;
					var scaleAndContent = projectAdjustmentData.scaleAndContent;
					var scaleAndContent2 = $("#scaleAndContent2").val();
					if(scaleAndContent == scaleAndContent2){
						alert("内容没进行修改，保存失败！");
						lock = false;
						return;
					}
					p.pid = adjustmentPid;
					p.projectName=$("#projectName2").val();
					p.referenceNumber=$("#referenceNumber2").val();
					p.carryOutUnit=$("#carryOutUnit2").val();
					p.totalFund=$("#totalFund2").val();
					$("#adjustmentArea").val();
					p.farmerName=$("#farmerName2").val();
					p.scaleAndContent=$("#scaleAndContent2").val();
					p.subjectName=$("#subjectName2").val();
					p.constructionMode=$("#adjustmentConstructionMode").val();
					p.projectType=$("#adjustmentProjectType").val();
					p.coveredFarmerNumber=$("#coveredFarmerNumber2").val();
					p.coveringNumber=$("#coveringNumber2").val();
					p.povertyStrickenFarmerNumber=$("#povertyStrickenFarmerNumber2").val();
					p.povertyStrickenPeopleNumber=$("#povertyStrickenPeopleNumber2").val();
					p.deadline=$("#deadline2").val();
					p.remark=$("#remark2").val();
					p.adjustmentReason=$("#adjustmentReason").val();
					$.ajax({					
						cache:false,
						type : "post",
						url : "projectsManage/addProjectsAdjustment",
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						data : JSON.stringify(p),
						success : function(data) {
							$.ajax({
								 cache:false,
								 type : "get",
								 url : "financeManagement/findByTotalFiance/"+tiaoXiangID,
								 dataType : "json",
								 contentType : "application/json;charset=UTF-8",
								 success : function(data, textStatus, jqXHR) {
									 infoTip({
											content : data.message,
											color : "#D58512"
									 });
									 projectsManageGridObj.projectsManageGrid.dataSource.read();
								 },		 					
							});						
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
					adjustmentProjectsManageWindow.obj.close();
					adjustmentProjectsManageWindow.validator = $("#adjustmentProjectValidate").kendoValidator().data("kendoValidator");
				});
			},

			
			//删除项目信息
			deleteProjectsClick:function(){
				$(".deleteProjects").off("click");
				$(".deleteProjects").on("click",function(e) {
					var tr =  $(e.target).closest("tr");
					var data = projectsManageGridObj.projectsManageGrid.dataItem(tr);
					var deleteID = data.id;
					if(confirm("确定删除项目信息么？")){
						$.ajax({
							cache:false,
							type : "get",	
							url : "projectsManage/findById/"+data.id,
							dataType : "json",
							contentType : "application/json;charset=UTF-8",
							success : function(data, textStatus, jqXHR) {
								if(data.length != 0){
									alert("该项目中还包含有子项目，请先删除子项目数据噢");
									return;
								}else{
									$.ajax({
										cache:false,
										type : "delete",	
										url : "projectsManage/delete/"+deleteID,
										dataType : "json",
										contentType : "application/json;charset=UTF-8",
										success : function(data, textStatus, jqXHR) {
											$.ajax({
												 cache:false,
												 type : "get",
												 url : "financeManagement/findByTotalFiance/"+deleteID,
												 dataType : "json",
												 contentType : "application/json;charset=UTF-8",
												 success : function(data, textStatus, jqXHR) {
													 infoTip({
															content : "删除成功!",
															color : "#D58512"
													 });
													 projectsManageGridObj.projectsManageGrid.dataSource.read();
												 },		 					
											});	
											//projectsManageGridObj.projectsManageGrid.dataSource.read();
										},
			  						    error:function(data, textStatus, jqXHR){
											infoTip({
												content : "删除失败!",
												color : "#FF0000"
											});
										}
									});
								}
							}
						});
					};
				});
			},
			
			//[添加/编辑]的【保存】按钮
			saveProjectsClick : function() {
				//$("#saveProjects").off("click");
				$("#saveProjects").on("click",function(e){
					e.preventDefault();
					if(!projectsManageWindow.validator.validate()){
						return;
					}
					var projects = {},area={};
					if(lock == true){
						return;
					}
					lock = true;
					var totalFund1 = $("#totalFund_add").val()
					
					area.id = $("#area_add").val();
					projects.parentId = pid;
					projects.id = $("#pid").val();
					projects.projectName=$("#projectName_add").val();
					projects.referenceNumber=$("#referenceNumber_add").val();
					projects.carryOutUnit=$("#carryOutUnit_add").val();
					projects.totalFund=$("#totalFund_add").val();
					projects.area =area
					projects.farmerName=$("#farmerName_add").val();
					projects.scaleAndContent=$("#scaleAndContent_add").val();
					projects.subjectName=$("#subjectName_add").val();
					projects.constructionMode=$("#constructionMode_add").val();
					projects.projectType=$("#projectType_add").val();
					projects.coveredFarmerNumber=$("#coveredFarmerNumber_add").val();
					projects.coveringNumber=$("#coveringNumber_add").val();
					projects.povertyStrickenFarmerNumber=$("#povertyStrickenFarmerNumber_add").val();
					projects.povertyStrickenPeopleNumber=$("#povertyStrickenPeopleNumber_add").val();
					projects.povertyGeneralFarmer=$("#povertyGeneralFarmer_add").val();
					projects.povertyLowIncomeFarmer=$("#povertyLowIncomeFarmer_add").val();
					projects.povertyGeneralPeople=$("#povertyGeneralPeople_add").val();
					projects.povertyLowIncomePeople=$("#povertyLowIncomePeople_add").val();
					projects.deadline=$("#deadline_add").val();
					projects.remark=$("#remark_add").val();
					projects.fundYear=$("#fundYearAdd_add").val();
					if(projects.id == null || projects.id == ""){
						var i = totalFund1;
						var count = Number(i)+Number(j);
						if(totalFund1 > addProjectTotalFund){
							alert("子项目总资金大于主项目，保存失败！");
							
							lock = false;
							return;
						}else{
							if(count > addProjectTotalFund){
								alert("子项目总金额已经大于主项目金额，保存失败！");
								lock = false;
								return;
							}
						}
					}else {
						var updateAllTotalFund1 = totalFund1;
						var count1 = Number(updateAllTotalFund1)+Number(updateAllTotalFund)-Number(updateProjectTotalFund);
						if(updateParentId == null){
							if(totalFund1 > updateProjectTotalFund){
								alert("子项目总资金大于主项目，保存失败！");
								lock = false;
								return;
							}
						}else{
							if(count1 > pTotalFund){
								alert("子项目总金额已经大于主项目金额，保存失败！");
								lock = false;
								return;
							}else if(totalFund1 > pTotalFund){
								alert("子项目总资金大于主项目，保存失败！");
								lock = false;
								return;
							}
						}
					}
					var url = (projects.id == null || projects.id == "" ? "projectsManage/addProjects" : "projectsManage/updateProjects");
					$.ajax({
			            type: "post", 
			            contentType : "application/json;charset=UTF-8",
			            url: url,
			            dataType : "json",
			            data : JSON.stringify(projects),
			            success : function(data, textStatus, jqXHR) {
							infoTip({
								content : "保存成功!",
								color : "#D58512"
							});
							
							$("#saveProjects").off("click");
							projectsManageGridObj.projectsManageGrid.dataSource.read();
							lock = false;
						},
						error : function(data, textStatus, jqXHR) {
							lock = false;
							infoTip({
								content : "保存失败!",
								color : "#FF0000"
							});
						},
					});				
					//projectsManageWindow.validator = $("#projectValidate").kendoValidator().data("kendoValidator");
					projectsManageWindow.obj.close();		       
			    });
			},
			
			// 取消 【添加】/
			cancelUserClick : function() {
				$("#cancelProjects").on("click", function() {
					projectsManageWindow.obj.close();
				});
			},
			
			/*调项【取消】按钮*/
			cancelAdjustmentClick:function(){
				$("#cancelAdjustment").on("click",function(){
					adjustmentProjectsManageWindow.obj.close();
				});
			},
	},
	projectsManageGridObj.init();
	
	
	projectsManageWindow = {
			obj : undefined,
			id : $("#projectsManageWindow"),
			validator : undefined,
			init : function() {
				if (!projectsManageWindow.id.data("kendoWindow")) {
					projectsManageWindow.id.kendoWindow({
						width : "650px",
						height:"85%",
						actions : ["Close"],
						modal : true,
						title : "添加项目"
					});
                }
				projectsManageWindow.obj = projectsManageWindow.id.data("kendoWindow");
				projectsManageWindow.validator = $("#projectValidate").kendoValidator().data("kendoValidator");
			}
	};
	
	adjustmentProjectsManageWindow = {
			obj : undefined,
			id : $("#adjustmentProjectsManageWindow"),
			validator : undefined,
			init : function() {
				if (!adjustmentProjectsManageWindow.id.data("kendoWindow")) {
					adjustmentProjectsManageWindow.id.kendoWindow({
						width : "650px",
						height:"85%",
						actions : ["Close"],
						modal : true,
						title : "项目调项"
						
					});
				}
				adjustmentProjectsManageWindow.obj = adjustmentProjectsManageWindow.id.data("kendoWindow");
				adjustmentProjectsManageWindow.validator = $("#adjustmentProjectValidate").kendoValidator().data("kendoValidator");
			}
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
		var map = new BMap.Map(container);
		map.addControl(new BMap.MapTypeControl());
		map.enableScrollWheelZoom();//启用滚轮放大缩小
		map.enableContinuousZoom();//启用连续缩放效果
		map.enableInertialDragging();//启用地图惯性拖拽
		map.centerAndZoom("清镇市",15);

		//显示当前位置
		var geolocation = new BMap.Geolocation();
		geolocation.getCurrentPosition(function(r){
			if(this.getStatus() == BMAP_STATUS_SUCCESS){
				var mk = new BMap.Point(r.point);
				map.centerAndZoom(mk,15);
			}else {
				alert('failed'+this.getStatus());
			}        
		},{enableHighAccuracy: true});
		
		// 添加导航控件
		var navigationControl = new BMap.NavigationControl({
			anchor: BMAP_ANCHOR_TOP_LEFT,
			type: BMAP_NAVIGATION_CONTROL_LARGE,
		});
		map.addControl(navigationControl);
		// 添加定位控件
		var geolocationControl = new BMap.GeolocationControl({enableAutoLocation:true});
		//定位成功
		geolocationControl.addEventListener("locationSuccess", function(e){
		    var address = '';
		    address += e.addressComponent.province;
		    address += e.addressComponent.city;
		    address += e.addressComponent.district;
		    address += e.addressComponent.street;
		    address += e.addressComponent.streetNumber;
		});
		//定位失败
		geolocationControl.addEventListener("locationError",function(e){
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
			
		},
		newInitSupervisionsMap:function(data,adjustmentPid){
			var map = new BMap.Map('showSupervisionsMap');
			this.map = map;
			var poi = new BMap.Point(106.475375,26.566064);
		    map.centerAndZoom(poi, 15);
			map.enableScrollWheelZoom();//启用滚轮放大缩小
			map.enableContinuousZoom();//启用连续缩放效果
			map.enableInertialDragging();//启用地图惯性拖拽
			map.addControl(new BMap.NavigationControl());               // 添加平移缩放控件
			map.addControl(new BMap.ScaleControl());                    // 添加比例尺控件
			map.addControl(new BMap.OverviewMapControl());              //添加缩略地图控件
			map.addControl(new BMap.MapTypeControl());          //添加地图类型控件
			
			//显示行政区域
			var bdary = new BMap.Boundary();
			bdary.get("贵州省清镇市", function(rs){       //获取行政区域
				//map.clearOverlays();        //清除地图覆盖物       
				var count = rs.boundaries.length; //行政区域的点有多少个
				if (count === 0) {
					alert('未能获取当前输入行政区域');
					return ;
				}
	          	var pointArray = [];
				for (var i = 0; i < count; i++) {
					var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2,fillColor:"",strokeStyle: 'dashed'}); //建立多边形覆盖物
					map.addOverlay(ply);  //添加覆盖物
					pointArray = pointArray.concat(ply.getPath());
				}    
				map.setViewport(pointArray);    //调整视野                 
			}); 
			
			//显示当前位置
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r){
				if(this.getStatus() == BMAP_STATUS_SUCCESS){
					map.centerAndZoom(r.point,13);
				}
				else {
					alert('failed'+this.getStatus());
				}        
			},{enableHighAccuracy: true});
		    $.ajax({
				cache:false,
				type : "get",
				url : "point/getRange/"+adjustmentPid,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				success : function(data, textStatus, jqXHR) {
					for (var i = 0; i < data.length; i++) {
//						var pointArr = [];
						var ps = data[i];
						var pt = new BMap.Point(ps.lng, ps.lat);
//						pointArr.push(pt);
						var polygon = new BMap.Polygon(ps, {strokeColor:"#F2CDCA",
				            fillColor:"#FF7744",
				            strokeWeight: 1,
				            strokeOpacity: 1,
				            fillOpacity: 0.2,
				            strokeStyle: 'solid'});  //创建多边形
						map.addOverlay(polygon);
					}
				},
				error : function(data, textStatus, jqXHR) {				
					$("#text_div").slideDown('slow');
					$(".text_info").text("没有范围");		
					setTimeout(function(){
						$("#text_div").slideUp("slow");
					},2000);
				}
		    });
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
						if(dd.farmer == null || dd.farmer == ""){
							var msg = "<div> 农户户主: 无<br/>农户电话号码: 无<br/>存在问题: "+dd.existingProblems+ "<br/>整改建议: "+dd.correctSuggestions + "<br/>整改时间: "+dd.correctTime + "</div>";
						}else{
						    var msg = "<div> 农户户主: "+dd.farmer.farmerName+"<br/>农户电话号码: "+dd.farmer.phoneNumber+"<br/>存在问题: "+dd.existingProblems+ "<br/>整改建议: "+dd.correctSuggestions + "<br/>整改时间: "+dd.correctTime + "</div>";
						}
						var imgs = '';
						if(dd.pictures && dd.pictures.length > 0){
							var pitArr = dd.pictures.split(';');
							for(var j=0;j<pitArr.length, pitArr [j].length > 0;j++){
								imgs += '<img src="'+pitArr[j]+'" data-src="'+pitArr[j]+'" class="zoomSize" style="margin-top:8px;float:bottom;zoom:1;overflow:hidden;width:50px;height:50px;margin-left:3px;"/>';
							}
						}

						
						var infotip = {title:'',content:''};
						infotip.content = '<div style="margin:0;line-height:15px;padding:2px;font-size: 12px; color: rgb(77, 77, 77);margin-top: 5px;">' +
	                    	msg + imgs +
	                    	'</div>';
						infotip.title = '<span style="font-size: 10px; color: rgb(77, 77, 77); font-weight: bold; text-decoration: none;">项目监控－'+dd.name+'</span>';
						var label1 = new BMap.Label(infotip.title,{offset:new BMap.Size(20,-10)});
						label1.setStyle(labelStyle);
						marker1.setLabel(label1);
						map.addOverlay(marker1);
						addClickHandler(infotip, marker1);
					}
				}
			}else {
				$("#text_div").slideDown('slow');
				$(".text_info").text("没有监控点");		
				setTimeout(function(){
					$("#text_div").slideUp("slow");
				},2000);		 
			};
			function addClickHandler(infotip,marker){
				EventWrapper.addListener(marker, 'mouseover',function(e){
					var opts = {
						width : 370,     // 信息窗口宽度
						height: 160,     // 信息窗口高度
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
		},
		initSupervisionsMap:function(e){
			adjustmentPid = e.id;
			return;
			var map = new BMap.Map('showSupervisionsMap');
//			var poi = new BMap.Point(106.475375,26.566064);
//			map.centerAndZoom(poi, 15);
		    map.enableScrollWheelZoom();
//			//显示当前位置
//			var geolocation = new BMap.Geolocation();
//			geolocation.getCurrentPosition(function(r){
//				if(this.getStatus() == BMAP_STATUS_SUCCESS){
//					map.centerAndZoom(r.point,18);
//				}
//				else {
//					alert('failed'+this.getStatus());
//				}        
//			},{enableHighAccuracy: true});
		    $.ajax({
				cache:false,
				type : "get",
				url : "point/getRange/"+adjustmentPid,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				success : function(data, textStatus, jqXHR) {
					for (var i = 0; i < data.length; i++) {
//						var pointArr = [];
						var ps = data[i];
						var pt = new BMap.Point(ps.lng, ps.lat);
//						pointArr.push(pt);
						var polygon = new BMap.Polygon(ps, {strokeColor:"#F2CDCA",
				            fillColor:"#FF7744",
				            strokeWeight: 1,
				            strokeOpacity: 1,
				            fillOpacity: 0.2,
				            strokeStyle: 'solid'});  //创建多边形
						map.addOverlay(polygon);
					}
				},
				error : function(data, textStatus, jqXHR) {
					infoTip({
						content : "没有范围",
						color : "#FF0000"
					});
				}
		    });
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
									field : "correctTime",
									width : 110,
									locked: true,
									title : "整改时间"
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
									field : "farmer.farmerName",
									width : 110,
									title : "项目户主姓名",
									template: function(dataItem){
										if(dataItem.farmer == null || dataItem.farmer == ""){
											return "";
										}else{
											return dataItem.farmer.farmerName;
										}
									}
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
									field : "pictures",
									width : 230,
									title : "监管图片",
									template: function(dataItem){
										if(dataItem.pictures && dataItem.pictures.length>0){
											var picturesStr = "";
											var picturesArr = dataItem.pictures.split(";");
											picturesStr += '<div style="overflow-y:hidden;overflow-x:scroll;">'
											for(var i =0;i<picturesArr.length;i++){
												if(picturesArr[i].length>0){
													//picturesStr += "<div class='showphoto' data-src="+picturesArr[i]+" style='background-image: url("+ picturesArr[i] +");'></div>";
													picturesStr += '<img src="'+picturesArr[i]+'" data-src="'+picturesArr[i]+'" class="showphoto" style="margin-top:3px;float:bottom;zoom:1;overflow:hidden;width:70px;height:50px;margin-left:3px;"/>';
												}
											}
//											picturesStr += '<img src="http://localhost:8080/agriculture_project_supervision/PictureUploadFiles_img/11-20160331151816.jpg" class="showphoto" style="margin-top:3px;float:bottom;zoom:1;overflow:hidden;width:70px;height:50px;margin-left:3px;"/>';
//											picturesStr += '<img src="http://localhost:8080/agriculture_project_supervision/PictureUploadFiles_img/11-20160331151816.jpg" class="showphoto" style="margin-top:3px;float:bottom;zoom:1;overflow:hidden;width:70px;height:50px;margin-left:3px;"/>';
//											picturesStr += '<img src="http://localhost:8080/agriculture_project_supervision/PictureUploadFiles_img/11-20160331151816.jpg" class="showphoto" style="margin-top:3px;float:bottom;zoom:1;overflow:hidden;width:70px;height:50px;margin-left:3px;"/>';
//											picturesStr += '<img src="http://localhost:8080/agriculture_project_supervision/PictureUploadFiles_img/11-20160331151816.jpg" class="showphoto" style="margin-top:3px;float:bottom;zoom:1;overflow:hidden;width:70px;height:50px;margin-left:3px;"/>';
											picturesStr += '</div>';
											return picturesStr;
										}else {
											return "无";
											
									}
								}
							},
							{
								field : "description",
								width : 100,
								title : "其它"
							},
							],
						dataBound : function() {
							showSupervisionsWindow.supervisionClick();
							showSupervisionsWindow.showPictureTooltip();
						}
					}).data("kendoGrid");
		},
		showPictureTooltip: function(){
			//鼠标悬停提示
			$(".showphoto").kendoTooltip({
                content: kendo.template($("#pictureTipTemplate").html()),
				// content: '<img src="http://localhost:8080/agriculture_project_supervision/PictureUploadFiles_img/11-20160331151816.jpg" style="width:480px;height:500px;"/>',
                position: "left"
            });
		},
		supervisionClick: function(){
			$("#showSupervisionsList tr").on('click', function (e) {
				var tr =  $(e.target).closest("tr");
				var supervision = showSupervisionsWindow.supervisionsGrid.dataItem(tr);
				var currentMarker =showSupervisionsWindow.markersObj[supervision.id];
//				showSupervisionsWindow.map.centerAndZoom(currentMarker.getPosition(), 18);
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
//			var map = new BMap.Map('showSupervisionsMap');
			showSupervisionsWindow.newInitSupervisionsMap(data,adjustmentPid);
//			map.clearOverlays();
//			showSupervisionsWindow.markersObj = {};
//			if(data.length > 0){
//				for(var i in data){
//					var dd = data[i];
//					if(dd.point && !isNaN(dd.point.lng) && !isNaN(dd.point.lat)){
//						var point1 = new BMap.Point(dd.point.lng, dd.point.lat);
//						var marker1 = new BMap.Marker(point1);//创建标注
//						showSupervisionsWindow.markersObj[dd.id] = marker1; //保存marker到markersObj对象，单击左侧列表时获取此标注
//						if(i==0){
//							map.centerAndZoom(point1,18);
//						}
//						
//						var msg = "<div> 存在问题: "+dd.existingProblems+ "<br/>整改建议: "+dd.correctSuggestions + "<br/>整改时间: "+dd.correctTime + "</div>";
//						var imgs = '';
//						if(dd.pictures && dd.pictures.length > 0){
//							var pitArr = dd.pictures.split(';');
//							for(var j=0;j<pitArr.length, pitArr [j].length > 0;j++){
//								imgs += '<img src="'+pitArr[j]+'" data-src="'+pitArr[j]+'" class="zoomSize" style="margin-top:8px;float:bottom;zoom:1;overflow:hidden;width:50px;height:50px;margin-left:3px;"/>';
//							}
//						}
//
//						
//						var infotip = {title:'',content:''};
//						infotip.content = '<div style="margin:0;line-height:18px;padding:2px;font-size: 12px; color: rgb(77, 77, 77);margin-top: 5px;">' +
//	                    	msg + imgs +
//	                    	'</div>';
//						infotip.title = '<span style="font-size: 14px; color: rgb(77, 77, 77); font-weight: bold; text-decoration: none;">项目监控－'+dd.name+'</span>';
//						var label1 = new BMap.Label(infotip.title,{offset:new BMap.Size(20,-10)});
//						label1.setStyle(labelStyle);
//						marker1.setLabel(label1);
//						map.addOverlay(marker1);
//						addClickHandler(infotip, marker1);
//					}
//				}
//			}else {
//				infoTip({
//					content : "没有监控点!",
//					color : "#D58512"
//				});
//			};
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
	

	//确定项目范围窗口
	determineRangeWindow={
			obj : undefined,
			id : $("#determineRangeWindow"),
			rangeGrid: {},
			validator : undefined,
			map : undefined,//地图对象
			init : function() {
				if (!determineRangeWindow.id.data("kendoWindow")) {
					determineRangeWindow.id.kendoWindow({
						width : "95%",
						height:"700px",
						actions : ["Maximize", "Close"],
						modal : true,
						title : "确定项目范围"
					});
				}
				determineRangeWindow.obj = determineRangeWindow.id.data("kendoWindow");
			},
			showRange:function(data){
				var pdata = data;
				$("#determineProjects,#determineProjects,#cancelDetermine").off("click");			
				adjustmentPid = data.id;			
				$("#showDetermineRangeMap").empty();
				if(lock){
					return;
				}
				$("#showDetermineRangeMap").off("rightclick");
				var map = new BMap.Map('showDetermineRangeMap',{enableMapClick:false});	
				var poi = new BMap.Point(106.475375,26.566064);
				map.centerAndZoom(poi, 15);
				map.enableScrollWheelZoom();//启用滚轮放大缩小
				map.enableContinuousZoom();//启用连续缩放效果
				map.enableInertialDragging();//启用地图惯性拖拽
				map.enableInertialDragging();//启用地图惯性拖拽
				map.addControl(new BMap.NavigationControl());               // 添加平移缩放控件
				map.addControl(new BMap.ScaleControl());                    // 添加比例尺控件
				map.addControl(new BMap.OverviewMapControl());              // 添加缩略地图控件
				//显示行政区域
				var bdary = new BMap.Boundary();
				bdary.get("贵州省清镇市", function(rs){       //获取行政区域
					//map.clearOverlays();        //清除地图覆盖物       
					var count = rs.boundaries.length; //行政区域的点有多少个
					if (count === 0) {
						alert('未能获取当前输入行政区域');
						return ;
					}
		          	var pointArray = [];
					for (var i = 0; i < count; i++) {
						var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2,fillColor:"",strokeStyle: 'dashed'}); //建立多边形覆盖物
						map.addOverlay(ply);  //添加覆盖物
						pointArray = pointArray.concat(ply.getPath());
					}    
					map.setViewport(pointArray);    //调整视野                 
				}); 
				
				
				//查询项目确定的范围
			    $.ajax({
					cache:false,
					type : "get",
					url : "point/getRange/"+adjustmentPid,
					dataType : "json",
					contentType : "application/json;charset=UTF-8",
					success : function(data, textStatus, jqXHR) {
						if(data.length != 0){
							var opts = {
								width: 200,
	//							anchor: BMAP_ANCHOR_TOP_RIGHT,
								height: 80,
								title : '<span style="font-size:14px;color:#0A8021">提示：</span>',
								offset: new BMap.Size(0, -20)
							};
							var infoWindow =new BMap.InfoWindow("右键点击图形删除项目范围！",opts);
							for (var i = 0; i < data.length; i++) {
								var ps = data[i];
								//var pt = new BMap.Point(ps[0].lng, ps[1].lat);
								var polygon = new BMap.Polygon(ps, {strokeColor:"#FF0000",
						            fillColor:"#FBE596",
						            strokeWeight: 1,
						            strokeOpacity: 1,
						            fillOpacity: 1,
						            fillOpacity:0.4,
						            strokeStyle: 'dashed'});  //创建多边形
								map.addOverlay(polygon);
								
								polygon.addEventListener("mouseover",function(e){
									var point = e.point;
									map.openInfoWindow(infoWindow,point);
								});	
								polygon.addEventListener("mouseout",function(){									
									map.closeInfoWindow();
								});	
							
								//鼠标右键删除事件					
								polygon.addEventListener("rightclick",function(e){
									//if (e.overlay) {
										var p = e.target;
										var newPs = p.ia;
										var rangeNumber = newPs[0].rangeNumber;
										//alert(rangeNumber);
										if (confirm("确定删除项目范围？")) {
											$.ajax({
												cache:false,
												type : "delete",
												url : "point/deleteRange/"+adjustmentPid+"/"+rangeNumber,
												dataType : "json",
												contentType : "application/json;charset=UTF-8",
												success : function(data, textStatus, jqXHR) {
													infoTip({
														content : "删除成功!",
														color : "#D58512"
													});
													lock=false;	
													determineRangeWindow.showRange(pdata);
												},
												error : function(data, textStatus, jqXHR) {
													infoTip({
														content : "删除失败！",
														color : "#FF0000"
													});
													lock=false;
												}
											});
										}
									//}
								});						
							}
						}else{
							infoTip({
								content : "没有设定范围",
								color : "#FF0000"
							});
						}
					},
					async:false,
					error : function(data, textStatus, jqXHR) {
						infoTip({
							content : "没有范围",
							color : "#FF0000"
						});
					}
			    });
				
				//显示当前位置
//				var geolocation = new BMap.Geolocation();
//				geolocation.getCurrentPosition(function(r){
//					if(this.getStatus() == BMAP_STATUS_SUCCESS){
//						map.centerAndZoom(r.point,18);
//					}
//					else {
//						alert('failed'+this.getStatus());
//					}        
//				},{enableHighAccuracy: true});
			    var overlays = [];
			    var count = 0;
			    var overlaycomplete = function(e){		    	
			    	points[count++] = e.overlay.ia;
					overlays.push(e.overlay);
			    };
			    //鼠标绘制样式
			    var styleOptions = {
			            strokeColor:"#F2CDCA",    //边线颜色。
			            fillColor:"#FF7744",      //填充颜色。当参数为空时，圆形将没有填充效果。
			            strokeWeight: 2,       //边线的宽度，以像素为单位。
			            strokeOpacity: 1,	   //边线透明度，取值范围0 - 1。
			            fillOpacity: 0.2,      //填充的透明度，取值范围0 - 1。
			            strokeStyle: 'solid' //边线的样式，solid或dashed。
			    }; 
			    //实例化鼠标绘制工具
			    var drawingManager = new BMapLib.DrawingManager(map, {
			            isOpen: false, //是否开启绘制模式
			            enableDrawingTool: true, //是否显示工具栏
			            drawingToolOptions: {
			                anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
			                offset: new BMap.Size(1, 1), //偏离值
			                drawingModes:[BMAP_DRAWING_POLYGON, BMAP_DRAWING_MARKER] 
			            },
			            //circleOptions: styleOptions, //圆的样式
			            polylineOptions: styleOptions, //线的样式
			            polygonOptions: styleOptions, //多边形的样式
			            //rectangleOptions: styleOptions //矩形的样式
			    });
			  //添加鼠标绘制工具监听事件，用于获取绘制结果
			    drawingManager.addEventListener('overlaycomplete', overlaycomplete);
			    //清空覆盖物
			    $('#cancelDetermine').on('click', function() {
			    	for(var i = 0; i < overlays.length; i++){
			            map.removeOverlay(overlays[i]);
			        }
			    	overlays.length = 0; 
			    	points = [];	//清空获取的点坐标
			    });		   
			    
			  
			    
			  //提交范围坐标保存到数据库
				$("#determineProjects").on("click", function(e) {
					var id = determinePid;
					var pointsArray = points;
					if(lock){		
						return;
					}
					lock = true;
					if(points.length == 0){
						alert("没有数据可以保存");
						lock = false;
						return;
					}
					//坐标数组
					for (var i = 0; i < points.length; i++) {
						currPoints = points[i];
						for(var j in currPoints){
							currPoints[j].projectId=id;
						}
					};
					$.ajax({
						cache:false,
						type : "post",
						url : "point/addRange/"+id,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						data : JSON.stringify(pointsArray),
						success : function(data, textStatus, jqXHR) {
							infoTip({
								content : "保存成功!",
								color : "#D58512"
							});
							lock = false;
							
							determineRangeWindow.obj.close();
							projectsManageGridObj.projectsManageGrid.dataSource.read();
						},
						error : function(data, textStatus, jqXHR) {
							lock = false;
							infoTip({
								content : "保存失败!",
								color : "#FF0000"
							});
						}
					});
				});
			},
			
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
		myIcon : undefined,//自定义图标
		firstCheckId: undefined,//第一行Id，用于初始化时在地图上显示路线
		init : function() {
			if (!showChecksWindow.id.data("kendoWindow")) {
				showChecksWindow.id.kendoWindow({
					width : "95%",
					height:"710px",
					actions : ["Maximize", "Close"],
					modal : true,
					title : "查看项目验收记录",
					close: function(){
						lock = false;
					}
				});
			}
			showChecksWindow.obj = showChecksWindow.id.data("kendoWindow");
			showChecksWindow.map = initMap("showChecksMap");
			showChecksWindow.myIcon = new BMap.Symbol(BMap_Symbol_SHAPE_CIRCLE, {
			    scale: 1,
			    strokeWeight: 0,
			    fillColor: 'blue',
			    fillOpacity: 0.1
			  });
		},
		
		refreshChecksList: function(sId){
			showChecksWindow.map.clearOverlays();//清空地图图标
			//显示行政区域边
			var bdary = new BMap.Boundary();		
			bdary.get("贵州省清镇市", function(rs){       //获取行政区域
				//map.clearOverlays();        //清除地图覆盖物       
				var count = rs.boundaries.length; //行政区域的点有多少个
				if (count === 0) {
					alert('未能获取当前输入行政区域');
					return ;
				}
	          	var pointArray = [];
				for (var i = 0; i < count; i++) {
					var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2,fillColor:"",strokeStyle: 'dashed'}); //建立多边形覆盖物
					showChecksWindow.map.addOverlay(ply);  //添加覆盖物
					pointArray = pointArray.concat(ply.getPath());
				}    
				showChecksWindow.map.setViewport(pointArray);    //调整视野                 
			});
			//显示项目范围
			showChecksWindow.showRanges(sId);
			//初始化项目验收列表
			if(this.checksGrid){
				this.checksGrid.dataSource.filter([{
					field : "sId",
					value : sId
				}]);
			}else {
				this.initChecksList(sId);
			}
		},
		
		//显示项目的范围
		showRanges: function(adjustmentPid){
			$.ajax({
				cache:false,
				type : "get",
				url : "point/getRange/"+adjustmentPid,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				success : function(data, textStatus, jqXHR) {
					for (var i = 0; i < data.length; i++) {
						var ps = data[i];
						var pt = new BMap.Point(ps.lng, ps.lat);
						var polygon = new BMap.Polygon(ps, {strokeColor:"#FF0000",
				            fillColor:"#FF7744",
				            strokeWeight: 1,
				            strokeOpacity: 1,
				            fillOpacity: 0.2,
				            strokeStyle: 'dashed'});  //创建多边形
						showChecksWindow.map.addOverlay(polygon);
					}
				},
				error : function(data, textStatus, jqXHR) {
					infoTip({
						content : "未设定项目范围",
						color : "#FF0000"
					});
				}
		    });
		},
		//显示地图
/*		initChecksMap:function(items){
			alert("2")
			adjustmentPid = items.id;
			var map = new BMap.Map('showChecksMap');
		    map.enableScrollWheelZoom();
		    
			//显示当前位置
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r){
				if(this.getStatus() == BMAP_STATUS_SUCCESS){
					map.centerAndZoom(r.point,15);
				}
				else {
					alert('failed'+this.getStatus());
				}        
			},{enableHighAccuracy: true});
		
			//显示行政区域
			var bdary = new BMap.Boundary();
			bdary.get("贵州省清镇市", function(rs){       //获取行政区域
				//map.clearOverlays();        //清除地图覆盖物       
				var count = rs.boundaries.length; //行政区域的点有多少个
				if (count === 0) {
					alert('未能获取当前输入行政区域');
					return ;
				}
	          	var pointArray = [];
				for (var i = 0; i < count; i++) {
					var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2,fillColor:"",strokeStyle: 'dashed'}); //建立多边形覆盖物
					map.addOverlay(ply);  //添加覆盖物
					pointArray = pointArray.concat(ply.getPath());
				}    
				map.setViewport(pointArray);    //调整视野                 
			}); 
			
		    $.ajax({
				cache:false,
				type : "get",
				url : "point/getRange/"+adjustmentPid,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				success : function(data, textStatus, jqXHR) {
					for (var i = 0; i < data.length; i++) {
//						var pointArr = [];
						var ps = data[i];
						var pt = new BMap.Point(ps.lng, ps.lat);
//						pointArr.push(pt);
						var polygon = new BMap.Polygon(ps, {strokeColor:"#F2CDCA",
				            fillColor:"#FF7744",
				            strokeWeight: 1,
				            strokeOpacity: 1,
				            fillOpacity: 0.2,
				            strokeStyle: 'solid'});  //创建多边形
						map.addOverlay(polygon);
					}
				},
				error : function(data, textStatus, jqXHR) {
					infoTip({
						content : "没有范围",
						color : "#FF0000"
					});
				}
		    });
		},
*/		
		//验收列表
		initChecksList: function(sId){
			if(!sId || sId == ''){
				sId = 0;
			}
			this.sId = sId;
			this.checksGrid = $("#showChecksList").kendoGrid({
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
						lock = false;
						if(e && e.response && e.response.items){
							showChecksWindow.mapDataHandler(e.response.items);
						}
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
				    		field : "projects",
				    		width : 120,
				    		locked: true,
				    		title : "项目名称",
				    		template: function(dataItem) {
								if(dataItem.projects && dataItem.projects != null){
									return dataItem.projects.projectName;
								}else{
									return "";
								}
							}
				    	},
				    	{
				    		field : "createDate",
				    		width : 150,
				    		title : "验收日期"
				    	},
						{
							field : "isSuccess",
							width : 80,
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
					showChecksWindow.firstCheckRowClick();
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
		firstCheckRowClick: function(){
			var row = showChecksWindow.checksGrid.tbody.find("tr:eq(0)");
			if(!row){
				return;
			}
			var check = showChecksWindow.checksGrid.dataItem(row);
			if(!check){
				return;
			}
			var everyCheck =showChecksWindow.checksArr[check.id];
			if(everyCheck && everyCheck.everShown == false){
				showChecksWindow.checkClickFirst(everyCheck);
			}
		},
		checkClick: function(){
			$("#showChecksList tr").on('click', function (e) {
				var map = showChecksWindow.map;
				var tr =  $(e.target).closest("tr");
				var check = showChecksWindow.checksGrid.dataItem(tr);
				var everyCheck =showChecksWindow.checksArr[check.id];
				if(!everyCheck){
					return;
				}
				if(everyCheck.everShown == false){
					showChecksWindow.checkClickFirst(everyCheck);
				}else {
					showChecksWindow.checkClickOthers(everyCheck);
				}
			})
		},
		checkClickFirst: function(everyCheck){
			var map = showChecksWindow.map;
			everyCheck.everShown = true;
			for(var i1 = 0;i1 < everyCheck.pointArr.length; i1 ++){
				var pt = everyCheck.pointArr[i1];
				var marker2 = new BMap.Marker(pt,{icon:showChecksWindow.myIcon});  // 创建标注
				map.addOverlay(marker2);              // 将坐标点标注添加到地图中
				everyCheck.pointMarkerArr.push(marker2);
				if(i1 == 0 ){
					map.centerAndZoom(pt,18);
					var label = new BMap.Label("起点",{offset:new BMap.Size(20,-10)});
					label.setStyle(labelStyle);
					marker2.setLabel(label);
					map.addOverlay(marker2);
					everyCheck.startMarker = marker2;
				}
				if(i1 == everyCheck.pointArr.length-1 ){
					var label = new BMap.Label("终点",{offset:new BMap.Size(20,-10)});
					label.setStyle(labelStyle);
					marker2.setLabel(label);
					map.addOverlay(marker2);
					everyCheck.endMarker = marker2;
				}
			}
			
			everyCheck.polyline = new BMap.Polyline(everyCheck.pointArr, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
			map.addOverlay(everyCheck.polyline);
			
			everyCheck.polygon = new BMap.Polygon(everyCheck.pointArr, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});  //创建多边形
			map.addOverlay(everyCheck.polygon);
			
			//计算面积
			var area = BMapLib.GeoUtils.getPolygonArea(everyCheck.pointArr);
			var area1 = computeArea(everyCheck.coordinateArr);
			if(!$.isNumeric(area) || area <= 0){
				if($.isNumeric(area1) && area1 > 0){
					everyCheck.totalArea = (area1*3/2000).toFixed(2);//计算面积(亩);
					everyCheck.totalAreaM2 = area1.toFixed(2);//计算面积(平方米);
				}else{
					everyCheck.totalArea = 0;
					everyCheck.totalAreaM2 = 0;
				}
			}else{
				everyCheck.totalArea = (area*3/2000).toFixed(2);//计算面积(亩)
				everyCheck.totalAreaM2 = area.toFixed(2);//计算面积(平方米)
			}
			
			var label2 = new BMap.Label("总面积：" + everyCheck.totalAreaM2 + "平方米", {offset:new BMap.Size(0,-20)});
			label2.setStyle(labelStyle);
			everyCheck.m2 = new BMap.Marker(everyCheck.pointArr[0]);  // 创建标注
			everyCheck.m2.setLabel(label2);
			map.addOverlay(everyCheck.m2);
			
			everyCheck.m2.hide();
			
			//计算长度
			for(var i2 =0 ;i2< everyCheck.pointArr.length-1; i2++){
				everyCheck.totalDistances += parseFloat((map.getDistance(everyCheck.pointArr[i2], everyCheck.pointArr[i2+1])).toFixed(2));
			}
			// 创建长度信息窗口对象
			var label = new BMap.Label("总里程：" + everyCheck.totalDistances.toFixed(2) + "米",{offset:new BMap.Size(0,-20)});
			label.setStyle(labelStyle);
			var marker = new BMap.Marker(everyCheck.pointArr[0]);  // 创建标注
			map.addOverlay(marker);              // 将标注添加到地图中
			everyCheck.polylineMarker = marker;
			marker.setLabel(label);
			
			
			var label = new BMap.Label("总面积：" + everyCheck.totalArea + "亩", {offset:new BMap.Size(0,-20)});
			label.setStyle(labelStyle);
			everyCheck.polygonMarker = new BMap.Marker(everyCheck.pointArr[0]);  // 创建标注
			everyCheck.polygonMarker.setLabel(label);
			map.addOverlay(everyCheck.polygonMarker);
			
			everyCheck.polygon.hide();
			everyCheck.polygonMarker.hide();
		},
		checkClickOthers: function(everyCheck){
			showChecksWindow.map.centerAndZoom(everyCheck.startMarker.getPosition(), 18);
			if(everyCheck.polyline && everyCheck.polyline.isVisible()){
				everyCheck.polyline.hide();
				everyCheck.polylineMarker.hide();
				everyCheck.m2.hide();
				everyCheck.startMarker.hide();
				everyCheck.endMarker.hide();
				for(var i=0;i< everyCheck.pointMarkerArr.length;i++){
					everyCheck.pointMarkerArr[i].hide();
				}
			}else if(everyCheck.polygon && everyCheck.polygon.isVisible()){
				everyCheck.polygon.hide();
				everyCheck.polygonMarker.hide();
				everyCheck.m2.hide();
				everyCheck.startMarker.hide();
				everyCheck.endMarker.hide();
				for(var i=0;i< everyCheck.pointMarkerArr.length;i++){
					everyCheck.pointMarkerArr[i].hide();
				}
			}else if(everyCheck.polyline&& !everyCheck.polyline.isVisible()){
				everyCheck.polyline.show();
				everyCheck.polylineMarker.show();
				everyCheck.startMarker.show();
				everyCheck.endMarker.show();
				for(var i=0;i< everyCheck.pointMarkerArr.length;i++){
					everyCheck.pointMarkerArr[i].show();
				}
			}else if(everyCheck.polygon && !everyCheck.polygon.isVisible()){
				everyCheck.polygon.show();
				everyCheck.polygonMarker.show();
				everyCheck.m2.hide();
				everyCheck.startMarker.show();
				everyCheck.endMarker.show();
				for(var i=0;i< everyCheck.pointMarkerArr.length;i++){
					everyCheck.pointMarkerArr[i].show();
				}
			}
		},
		
		//验收记录
		mapDataHandler: function(data){
			if(data.length > 0){
				for(var i in data){
					var points = data[i].pointBaidu;
					if(!points || points.length == 0){
						points = data[i].points;
					}
					if(points && points.length > 0){
						var everyCheck = {
							pointArr : [],//坐标数组
							pointMarkerArr : [],//坐标标注数组
							polyline : undefined, //路线图
							polygon : undefined,//面积图
							totalDistances : 0, //路线长度
							totalArea : 0,//路线围绕面积
							totalAreaM2 : 0,//路线围绕面积(平方米)
							startMarker : undefined, //开始标注
							endMarker : undefined, //结束标注
							polylineMarker : undefined, //周长和面积信息提示marker
							polygonMarker: undefined,//面积marker
							m2:undefined,
							coordinateArr: [], //坐标数组
							everShown: false
						};
						for(var i1 = 0;i1 < points.length; i1 ++){
							var dd = points[i1];
							if(dd && !isNaN(dd.lng) && !isNaN(dd.lat)){
								var pt = new BMap.Point(dd.lng, dd.lat);
								everyCheck.pointArr.push(pt);
								everyCheck.coordinateArr.push([dd.lat, dd.lng]);
							}
						}
						showChecksWindow.checksArr[data[i].id] = everyCheck;
						showChecksWindow.firstCheckId = data[i].id;
					}
				}
			}else {
				infoTip({
					content : "没有验收记录!",
					color : "#D58512"
				});
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
			if(v.polyline){
				v.polyline.show();
				v.polylineMarker.show();
				v.startMarker.show();
				v.endMarker.show();
			}
			if(v.m2){
				v.m2.hide();
			}
			if(v.polygon){
				v.polygon.hide();
				v.polygonMarker.hide();
			}
			
		});
	});
	//计算面积(亩)
	$('#area').on('click', function() {
		if(!showChecksWindow.checksArr || showChecksWindow.checksArr.length <= 0){
			infoTip({
				content : "没有验收记录！",
				color : "#FF0000"
			});
			return;
		}
		$.each(showChecksWindow.checksArr, function(p, v) {
			if(v.polyline){
				v.polyline.hide();
				v.polylineMarker.hide();
			}
			if(v.m2){
				v.m2.hide();
			}
			if(v.polygon){
				v.polygon.show();
				v.polygonMarker.show();
				v.startMarker.show();
				v.endMarker.show();
			}
			
		});
	});
	
	//计算面积(平方米)
	$('#m2').on('click', function() {
		if(!showChecksWindow.checksArr || showChecksWindow.checksArr.length <= 0){
			infoTip({
				content : "没有验收记录！",
				color : "#FF0000"
			});
			return;
		}
		$.each(showChecksWindow.checksArr, function(p, v) {
			if(v.polyline){
				v.polyline.hide();
				v.polylineMarker.hide();
			}
			if(v.m2){
				v.m2.show();
				v.polygon.show();
				v.startMarker.show();
				v.endMarker.show();
			}
			if(v.polygon){
//				v.polygon.hide();
				v.polygonMarker.hide();
			}
		});
	});
	
	var searchProjects = function(areaId){
		if(lock == true){
			return;
		}
		projectsManageGridObj.projectsManageGrid.dataSource.filter([{
			field : 'areaId',
			value : areaId
		},{
			field : 'projectName',
			value : $("#projectName_select").val()
		},{
			field : 'fundYear',
			value : $("#fundYear_select").val()
		},{
			field : 'projectType',
			value : $("#projectType_select").val()
		}]);
	};
	$('#searchBtn').on('click', function(){ 
		searchProjects('');
	});
	
	$('#resetBtn').on('click', function() {
		$("#projectName_select").val('');
		$("#fundYear_select").val(2016); //select 2015
		$("#projectType_select").val('');
		searchProjects('');
	});

	//农户信息导入
	fileImportWindow = {
		fileImportGrid : {},
			
		obj : undefined,
		
		id : $("#fileFarmerImport"),
		
		validator : undefined,
		
		init : function() {
			if (!fileImportWindow.id.data("kendoWindow")) {
				fileImportWindow.id.kendoWindow({
					width : "1000px",
					maxHeight: "1000px",
					actions : ["Maximize", "Close", "Open"],
					modal : true,
					title : "农户信息导入",
					close : function() {
						lock = false;
					},
					open : function() {
						lock = false;
					}
				
				});
			};
			fileImportWindow.obj = fileImportWindow.id.data("kendoWindow");
			//项目导入信息验证
			fileImportWindow.validator = $("#fileFarmerImportValidate").kendoValidator().data("kendoValidator");
		},
		
		showDetail: function(data){
		//	toolbar : kendo.template($("#template").html());
			var projectId = data.id;
			this.fileImportGrid = $("#farmerImportList")
			.kendoGrid(
					{
						dataSource : {
							transport : {
								read : {
									type : "get",
									url : "farmerImport/searchDetail/"+projectId,
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
							 input: true,
				                numeric: false,
				                messages: {
				                    display: "{0} - {1} 共 {2} 条数据",
				                    empty: "没有数据",
				                    page: "页",
				                    of: "/ {0}",
				                    itemsPerPage: "条每页",
				                    first: "第一页",
				                    previous: "前一页",
				                    next: "下一页",
				                    last: "最后一页",
				                    refresh: true
				                }
						//	refresh: true
						},
						toolbar : kendo.template($("#template").html()),
						editable : "popup",

						columns : [
						    	{
						        	field : "id",
						        	hidden : true
						    	},
						    	{
						    		field : "farmerNumber",
						    		title : "农户编号",
						    		width : 90
						    	},
								{
									field : "farmerName",
									title : "户主姓名",
//									locked : true,
						    		width : 90
								},
								{
									field : "phoneNumber",
									title : "手机号码",
									
						    		width : 120
								},
								{
									field : "content",
									title : "项目内容",
						    		width : 120
								},
								{
									
									title : "操作",
								//	template : "#if(validateDescription){#<span data-tooltip='kendo-tooltip' title='#:validateDescription#'>#:validateDescription#</span>#}else{##}#",
									width : 40,
									template : kendo.template($("#deleteFarmer").html())
								}
								],
						dataBound : function() {
							fileImportWindow.deleteFarmerClick();
							fileImportWindow.editFarmersClick();
							fileImportWindow.saveFarmerClick();
							fileImportWindow.cancelFarmerClick();
						},
						editable : "popup"
					}).data("kendoGrid");
		},
		
		//弹出编辑页面
		editFarmersClick : function(){
			$(".editFarmers").on("click",function(e) {
				var tr =  $(e.target).closest("tr");
				var data = fileImportWindow.fileImportGrid.dataItem(tr);
				$("#farmerId").val(data.id);
				$("#farmerNumber").val(data.farmerNumber);
				$("#farmerNameInfo").val(data.farmerName);
				$("#phoneNumber").val(data.phoneNumber);
				$("#content").val(data.content);
				farmerInfoWindow.validator.validate();
				farmerInfoWindow.obj.title("编辑农户信息");
				farmerInfoWindow.obj.center().open();
			});
		},
		
		saveFarmerClick : function(){
			$("#saveFarmer").off("click");
			$("#saveFarmer").on("click",function(e){
				if(!farmerInfoWindow.validator.validate()){
					return;
				}
				var farmer = {};
				farmer.id = $("#farmerId").val();
				farmer.farmerNumber = $("#farmerNumber").val();
				farmer.farmerName = $("#farmerNameInfo").val();
				farmer.phoneNumber = $("#phoneNumber").val();
				farmer.content = $("#content").val();
				$.ajax({
					cache:false,
					type : "post",
					url : "farmerImport/updateFarmer",
					dataType : "json",
					contentType : "application/json;charset=UTF-8",
					data : JSON.stringify(farmer),
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
						fileImportWindow.fileImportGrid.dataSource.read();
					},
					error : function(data, textStatus, jqXHR) {
						infoTip({
							content : "保存失败!",
							color : "#FF0000"
						});
					}
				});
				farmerInfoWindow.obj.close();
			});
		},
		
		cancelFarmerClick : function() {
			$("#cancelFarmer").on("click",function(e){
				farmerInfoWindow.obj.close();
			});
		},
		
		//删除农户信息
		deleteFarmerClick : function(){
			$(".deleteFarmers").off("click");
			$(".deleteFarmers").on("click",function(e){
				var tr =  $(e.target).closest("tr");
				var data = fileImportWindow.fileImportGrid.dataItem(tr);
				if(confirm("确定删除农户信息么？")){
					$.ajax({
						cache:false,
						type : "delete",	
						url : "farmerImport/deleteFarmer/"+data.id,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						success : function(data, textStatus, jqXHR) {
							infoTip({
								content : "删除成功!",
								color : "#D58512"
							});
							fileImportWindow.fileImportGrid.dataSource.read();
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
	};
	
	
	
	//公示附件
	deleteRemarkWindow = {

			obj : undefined,

			id : $("#changeDeleteWindow"),
			deleteId :undefined,
			validator : undefined, 
			
			init : function() {
				if (!deleteRemarkWindow.id.data("kendoWindow")) {
					deleteRemarkWindow.id.kendoWindow({
						width : "1200px",
						height: "580px",
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
	

	pifufileUploadObj = {
		init : function() {
			$("#pifufileupload").kendoUpload({
				async :{
				type: 'POST',
		        saveUrl: "attachmentUpload/piFuUploadFile",
		        dataType: 'json',
		        autoUpload: true
				},
			 upload: function (e) {
				 var files = e.files;
				    $.each(files, function() {
				        if (this.extension != ".png" && this.extension != ".jpg" && this.extension != ".gif"
				        		&& this.extension != ".txt" &&  this.extension != ".xls" && this.extension != ".xlsx"
				        		&& this.extension != ".docx" && this.extension != ".doc" && this.extension != ".pdf"
				        ) {
				            alert("文件类型不正确，上传失败！");
				            e.preventDefault();
				        }
				    });
			        e.data = { projectId: $("#projectId").val()};//传参数
			    },
			   // acceptFileTypes: /(\.|\/)(jpg|gif|png)$/i,
			    multiple: false,
			    success : onSuccess1,
			    formAcceptCharset: 'utf-8',
		        maxFileSize: 10485760
			});
			
		}	
	};
	function onSuccess1(e) {
		$("#pifupicture").html("");
		$("#pifufileName").html("");
	    e.data=$("#projectId").val();
	    var projectId = e.data;
	    $.ajax({
			cache:false,
			type : "GET",
			url : "attachmentUpload/searchFile/"+projectId,
			dataType : "json",
			contentType : "application/json;charset=UTF-8",
			success : function(data, textStatus, jqXHR) {
				 for (var index in data) {
					if(index != "remove"){
						var fileName = data[index].fileName;
						prefix = fileName.substring(fileName.lastIndexOf(".")+1);
						var fName ;
						if(fileName.indexOf('\\')!=-1){
							fName = fileName.substring(fileName.lastIndexOf("\\")+1);
						}else{
							fName = fileName.substring(fileName.lastIndexOf("/")+1);
						}
						if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
							  var parentdiv = document.getElementById("pifufileName");
							  var div = document.createElement("div");
							  div.setAttribute("id", "pifufileName1");
							  div.setAttribute("class", "pifufileName2");
							  div.innerHTML = fName+"&nbsp;&nbsp<a class='pifuuploadfiles1'>下载文件</a>&nbsp;&nbsp<a class='deletepifufiles1'>删除文件</a>";
							  div.fileName = data[index].fileName;
							  div.id = data[index].id;
							  parentdiv.appendChild(div);
							  uploadFile1(div);
							
						}else{
							var div = document.getElementById("pifupicture");
							var img = document.createElement("img");
							img.setAttribute("id", "imgs");
							img.getAttribute("id");
							img.setAttribute("class","pifuimgs1");
							img.width=130;
							img.height=100;
							img.src = data[index].fileName;
							img.pictureId = data[index].id;
							div.appendChild(img);
							//div.appendChild(divpicture);
							mouseoverImg1(img);
						}
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
	
	function uploadFile1(div) {
		div.addEventListener("mouseover", function(e){
			$(".pifufileName2").off("click");
			$(".deletepifufiles1").off("click");
			$(".pifuuploadfiles1").off("click");
			var fileNa = div.fileName;
			var id = div.id;
			$(".pifuuploadfiles1").on("click", function() {
				$.fileDownload("projectsManage/downloadTemplate/"+fileNa+"/"+prefix, {
				    successCallback: function () {
				    	infoTip({
							content : "下载成功",
							color : "#D58512"
						});
				    	
				    },
				    failCallback: function () {
				    	infoTip({ 
							content : "下载失败!",
							color : "#FF0000"
						});
						
				    }
				});
			});
			$(".deletepifufiles1").on("click", function() {
				if(confirm("要删除此文件吗？")) {
					$.ajax({
						cache:false,
						type : "delete",
						url : "attachmentUpload/deletePiFuFile/"+id,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						success : function(data, textStatus, jqXHR) {
							infoTip({
								content : "删除成功!",
								color : "#D58512"
							});
							$("#pifupicture").html("");
							$("#pifufileName").html("");
							$.ajax({
								cache:false,
								type : "get",
								url : "attachmentUpload/searchFile/"+projectId,
								dataType : "json",
								contentType : "application/json;charset=UTF-8",
								success : function(data) {
									for (var index in data) {
										if(index != "remove"){
											var fileName = data[index].fileName;
											prefix = fileName.substring(fileName.lastIndexOf(".")+1);
											var fName ;
											if(fileName.indexOf('\\')!=-1){
												fName = fileName.substring(fileName.lastIndexOf("\\")+1);
											}else{
												fName = fileName.substring(fileName.lastIndexOf("/")+1);
											}
											if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
												  var parentdiv = document.getElementById("pifufileName");
												  var div = document.createElement("div");
												  div.setAttribute("id", "pifufileName1");
												  div.setAttribute("class", "pifufileName2");
												  div.innerHTML = fName+"&nbsp;&nbsp<a class='pifuuploadfiles1'>下载文件</a>&nbsp;&nbsp<a class='deletepifufiles1'>删除文件</a>";
												  div.fileName = data[index].fileName;
												  div.id = data[index].id;
												  parentdiv.appendChild(div);
												  uploadFile1(div);
												
											}else{
												var div = document.getElementById("pifupicture");
												var img = document.createElement("img");
												img.setAttribute("id", "imgs");
												img.getAttribute("id");
												img.setAttribute("class","pifuimgs1");
												img.width=130;
												img.height=100;
												img.src = data[index].fileName;
												img.pictureId = data[index].id;
												div.appendChild(img);
												//div.appendChild(divpicture);
												mouseoverImg1(img);
											}
										 }
										
									}
								},
								error : function(data, textStatus, jqXHR) {
									infoTip({
										content : "图片读取失败!",
										color : "#FF0000"
									});
								}
							});
						},
						error : function(data, textStatus, jqXHR) {
							infoTip({
								content : "删除失败!",
								color : "#FF0000"
							});
						}

					});
				
				};			
			});
//			$(".pifufileName2").on("click", function() {
//				var fileNa = div.fileName;
//				//alert(prefix);
//				$.fileDownload("projectsManage/downloadTemplate/"+fileNa+"/"+prefix, {
//				    successCallback: function () {
//				    	infoTip({
//							content : "下载成功",
//							color : "#D58512"
//						});
//				    	
//				    },
//				    failCallback: function () {
//				    	infoTip({ 
//							content : "下载失败!",
//							color : "#FF0000"
//						});
//						
//				    }
//				});
//			});
		})
		
	};
	 function mouseoverImg1(img){
		 img.addEventListener("mouseover", function(e){
			 $(".pifuimgs1").off("click");
			 pictureId = img.pictureId;
			 $(".pifuimgs1").kendoTooltip({
				 content: '<img src="'+img.src+'" style="width:480px;height:500px;"/>',
				// width: 50,
				 position: "left"
			 });
			 $(".pifuimgs1").on("click", function() {
					if(confirm("确定删除此图片吗？")) {
						$.ajax({
							cache:false,
							type : "delete",
							url : "attachmentUpload/deletePiFuFile/"+pictureId,
							dataType : "json",
							contentType : "application/json;charset=UTF-8",
							success : function(data, textStatus, jqXHR) {
								infoTip({
									content : "删除成功!",
									color : "#D58512"
								});
								$("#pifupicture").html("");
								$("#pifufileName").html("");
								
								$.ajax({
									cache:false,
									type : "get",
									url : "attachmentUpload/searchFile/"+projectId,
									dataType : "json",
									contentType : "application/json;charset=UTF-8",
									success : function(data) {
										for (var index in data) {
											if(index != "remove"){
												var fileName = data[index].fileName;
												prefix = fileName.substring(fileName.lastIndexOf(".")+1);
												var fName ;
												if(fileName.indexOf('\\')!=-1){
													fName = fileName.substring(fileName.lastIndexOf("\\")+1);
												}else{
													fName = fileName.substring(fileName.lastIndexOf("/")+1);
												}
												if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
													  var parentdiv = document.getElementById("pifufileName");
													  var div = document.createElement("div");
													  div.setAttribute("id", "pifufileName1");
													  div.setAttribute("class", "pifufileName2");
													  div.innerHTML = fName+"&nbsp;&nbsp<a class='pifuuploadfiles1'>下载文件</a>&nbsp;&nbsp<a class='deletepifufiles1'>删除文件</a>";
													  div.fileName = data[index].fileName;
													  div.id = data[index].id;
													  parentdiv.appendChild(div);
													  uploadFile1(div);
													
												}else{
													var div = document.getElementById("pifupicture");
													var img = document.createElement("img");
													img.setAttribute("id", "imgs");
													img.getAttribute("id");
													img.setAttribute("class","pifuimgs1");
													img.width=130;
													img.height=100;
													img.src = data[index].fileName;
													img.pictureId = data[index].id;
													div.appendChild(img);
													//div.appendChild(divpicture);
													mouseoverImg1(img);
												}
											 }
											
										}
									},
									error : function(data, textStatus, jqXHR) {
										infoTip({
											content : "图片读取失败!",
											color : "#FF0000"
										});
									}
								});
							},
							error : function(data, textStatus, jqXHR) {
								infoTip({
									content : "删除失败!",
									color : "#FF0000"
								});
							}

						});
					
					};
			 });
			
//			 $(".imgs1").kendoTooltip({
//				 content: '<img src="'+img.src+'" style="width:480px;height:500px;/>',
//				// width: 50,
//				 position: "left"
//			 });
			
            })
	};
	
//	lock = true;
//	farmerUploadObj = {
//		init : function() {
//			
//			$("#fileupload1").fileupload({
//				type: 'POST',
//		        url: "farmerImport/uploadFarmer/"+subProject_id,
//		        dataType: 'json',
//		        autoUpload: true,
//		        acceptFileTypes: /(\.|\/)(xls|xlsx)$/i,
//		        formAcceptCharset: 'utf-8',
//		        maxFileSize: 10485760,// 10MB
//		        messages: {
//	                maxNumberOfFiles: '超出了文件个数限制',
//	                acceptFileTypes: '只能上传Excel文件',
//	                maxFileSize: '文件不能大于10M',
//	                minFileSize: '文件太小'
//	            }
//			}).on('fileuploadadd', function (e, data) {
//				  $(this).attr("disabled",true);
//				  $("#importingMsg").html("<label class='uploadImgLoad'>上传中... 0%</label>");
//			}).on('fileuploadprocessalways', function (e, data) {
//				if(data.files.error){
//					var errorMsg = '';
//					$(this).removeAttr("disabled");
//					if(data.files[0].error=='acceptFileTypes'){
//						errorMsg = "<label class='error'>文件类型错误</label>";
//					}else if(data.files[0].error=='maxFileSize'){
//						errorMsg = "<label class='error'>文件不能大于10M</label>";
//					}else {
//						errorMsg = "<label class='error'>"+data.files[0].error+"</label>";
//					} 
//					$("#importingMsg").html(errorMsg);
//				} 
//			}).on('fileuploadprogressall', function (e, data) {
//			    var $p = $("#importingMsg");
//			    var progress = parseInt(data.loaded / data.total * 100, 10);
//			    if($p.text()==''){
//			    	$("#importingMsg").html("<label class='uploadImgLoad'>上传中... "+progress+"%</label>");
//				}else{
//				   console.info(progress);
//				   $p.html("<label class='uploadImgLoad'>上传中... "+progress+"%</label>");
//				   if(lock){
//					   lock = false;
//					   document.getElementById("info").style.display = "block";
//					   $("#info .text").html("正在导入数据，请稍等。。。");
//				   }
//				   if(progress==100){
//				      $p.fadeOut("slow",function(){
//				    	  $("#importingMsg").text("");
//					  });
//				   }
//				} 
//			}).on('fileuploaddone', function (e, data) {
//				if(data.result.result=='error'){
//					$(this).removeAttr("disabled");
//				}else if(data.result.result=='success'){
//					$(this).parent().prepend($("<a href='#' >  删除</a>").attr("onclick","uploadImageAjaxDelete('image_ajax_delete.action?dbFileName="+data.result.dbFileName+"',this)").add("<br/>"))
//					.prepend($("<img  width='140' height='90' border='0' />").attr("src",data.result.url))
//					.prepend($("<input type='hidden' name="+data.result.hiddenName+" id="+data.result.hiddenName+" value='"+data.result.dbFileName+"' />"));
//				}
//			}).on('fileuploadalways', function (e, data) {
//				lock = true;
//				$(this).removeAttr("disabled");
//			//	projectImportGridObj.projectImportGrid.dataSource.read();
//				if(data.result.resultCode == 0){
//					document.getElementById("info").style.display = "none";
//					infoTip({
//						content : data.result.message,
//						color : "#D58512"
//					});
//				}else if(data.result.resultCode == -1){
//					document.getElementById("info").style.display = "none";
//					infoTip({
//						content : data.result.message,
//						color : "#FF0000"
//					});
//				}
//				
//			});
//		}	
//	};
	
	//公示附件上传
	
	fileUploadObj = {
		init : function() {
			$("#fileupload").kendoUpload({
				async :{
				type: 'POST',
		        saveUrl: "attachmentUpload/uploadFile",
		        dataType: 'json',
		        autoUpload: true
				},
			 upload: function (e) {
				 var files = e.files;
				    $.each(files, function() {
				        if (this.extension != ".png" && this.extension != ".jpg" && this.extension != ".gif"
				        		&& this.extension != ".txt" &&  this.extension != ".xls" && this.extension != ".xlsx"
				        		&& this.extension != ".docx" && this.extension != ".doc" && this.extension != ".pdf"
				        ) {
				            alert("文件类型不正确，上传失败！");
				            e.preventDefault();
				        }
				    });
			        e.data = { projectId: $("#projectId").val()};//传参数
			    },
			   // acceptFileTypes: /(\.|\/)(jpg|gif|png)$/i,
			    multiple: false,
			    success : onSuccess,
			    formAcceptCharset: 'utf-8',
		        maxFileSize: 10485760
			});
			
		}	
	};
	function onSuccess(e) {
		$("#picture1").html("");
		$("#fileName").html("");
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
						var fileName = data[index].fileName;
						prefix = fileName.substring(fileName.lastIndexOf(".")+1);
						var fName ;
						if(fileName.indexOf('\\')!=-1){
							fName = fileName.substring(fileName.lastIndexOf("\\")+1);
						}else{
							fName = fileName.substring(fileName.lastIndexOf("/")+1);
						}
						if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
							  var parentdiv = document.getElementById("fileName");
							  var div = document.createElement("div");
							  div.setAttribute("id", "fileName1");
							  div.setAttribute("class", "fileName2");
							  div.innerHTML = fName+"&nbsp;&nbsp<a class='uploadfiles'>下载文件</a>&nbsp;&nbsp<a class='deletefiles'>删除文件</a>";
							  div.fileName = data[index].fileName;
							  div.id = data[index].id;
							  parentdiv.appendChild(div);
							  uploadFile(div);
							
						}else{
							var div = document.getElementById("picture1");
							var img = document.createElement("img");
							img.setAttribute("id", "imgs");
							img.getAttribute("id");
							img.setAttribute("class","imgs1");
							img.width=130;
							img.height=100;
							img.src = data[index].fileName;
							img.pictureId = data[index].id;
							div.appendChild(img);
							//div.appendChild(divpicture);
							mouseoverImg(img);
						}
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
	function uploadFile(div) {
		div.addEventListener("mouseover", function(e){
			$(".fileName2").off("click");
			$(".deletefiles").off("click");
			$(".uploadfiles").off("click");
			var fileNa = div.fileName;
			var id = div.id;
			$(".uploadfiles").on("click", function() {
				$.fileDownload("projectsManage/downloadTemplate/"+fileNa+"/"+prefix, {
				    successCallback: function () {
				    	infoTip({
							content : "下载成功",
							color : "#D58512"
						});
				    	
				    },
				    failCallback: function () {
				    	infoTip({ 
							content : "下载失败!",
							color : "#FF0000"
						});
						
				    }
				});
			});
			$(".deletefiles").on("click", function() {
				if(confirm("要删除此文件吗？")) {
					$.ajax({
						cache:false,
						type : "delete",
						url : "attachmentUpload/delete/"+id,
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						success : function(data, textStatus, jqXHR) {
							infoTip({
								content : "删除成功!",
								color : "#D58512"
							});
							$("#picture1").html("");
							$("#fileName").html("");
							$.ajax({
								cache:false,
								type : "get",
								url : "attachmentUpload/searchPicture/"+projectId,
								dataType : "json",
								contentType : "application/json;charset=UTF-8",
								success : function(data) {
									for (var index in data) {
										if(index != "remove"){
											var fileName = data[index].fileName;
											prefix = fileName.substring(fileName.lastIndexOf(".")+1);
											var fName ;
											if(fileName.indexOf('\\')!=-1){
												fName = fileName.substring(fileName.lastIndexOf("\\")+1);
											}else{
												fName = fileName.substring(fileName.lastIndexOf("/")+1);
											}
											if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
												  var parentdiv = document.getElementById("fileName");
												  var div = document.createElement("div");
												  div.setAttribute("id", "fileName1");
												  div.setAttribute("class", "fileName2");
												  div.innerHTML = fName+"&nbsp;&nbsp<a class='uploadfiles'>下载文件</a>&nbsp;&nbsp<a class='deletefiles'>删除文件</a>";
												  div.fileName = data[index].fileName;
												  div.id = data[index].id;
												  parentdiv.appendChild(div);
												  uploadFile(div);
												
											}else{
												var div = document.getElementById("picture1");
												var img = document.createElement("img");
												img.setAttribute("id", "imgs");
												img.getAttribute("id");
												img.setAttribute("class","imgs1");
												img.width=130;
												img.height=100;
												img.src = data[index].fileName;
												img.pictureId = data[index].id;
												div.appendChild(img);
												//div.appendChild(divpicture);
												mouseoverImg(img);
											}
										 }
										
									}
								},
								error : function(data, textStatus, jqXHR) {
									infoTip({
										content : "图片读取失败!",
										color : "#FF0000"
									});
								}
							});
						},
						error : function(data, textStatus, jqXHR) {
							infoTip({
								content : "删除失败!",
								color : "#FF0000"
							});
						}

					});
				
				};			
			});
//			$(".fileName2").on("click", function() {
//				var fileNa = div.fileName;
//				//alert(prefix);
//				$.fileDownload("projectsManage/downloadTemplate/"+fileNa+"/"+prefix, {
//				    successCallback: function () {
//				    	infoTip({
//							content : "下载成功",
//							color : "#D58512"
//						});
//				    	
//				    },
//				    failCallback: function () {
//				    	infoTip({ 
//							content : "下载失败!",
//							color : "#FF0000"
//						});
//						
//				    }
//				});
//			});
		})
		
	};
	 function mouseoverImg(img){
		 img.addEventListener("mouseover", function(e){
			 $(".imgs1").off("click");
			 pictureId = img.pictureId;
			 $(".imgs1").kendoTooltip({
				 content: '<img src="'+img.src+'" style="width:480px;height:500px;"/>',
				// width: 50,
				 position: "left"
			 });
			 $(".imgs1").on("click", function() {
					if(confirm("确定删除此图片吗？")) {
						$.ajax({
							cache:false,
							type : "delete",
							url : "attachmentUpload/delete/"+pictureId,
							dataType : "json",
							contentType : "application/json;charset=UTF-8",
							success : function(data, textStatus, jqXHR) {
								infoTip({
									content : "删除成功!",
									color : "#D58512"
								});
								$("#picture1").html("");
								$("#fileName").html("");
								
								$.ajax({
									cache:false,
									type : "get",
									url : "attachmentUpload/searchPicture/"+projectId,
									dataType : "json",
									contentType : "application/json;charset=UTF-8",
									success : function(data) {
										for (var index in data) {
											if(index != "remove"){
												var fileName = data[index].fileName;
												prefix = fileName.substring(fileName.lastIndexOf(".")+1);
												var fName ;
												if(fileName.indexOf('\\')!=-1){
													fName = fileName.substring(fileName.lastIndexOf("\\")+1);
												}else{
													fName = fileName.substring(fileName.lastIndexOf("/")+1);
												}
												if(prefix != "jpg" && prefix != "png" && prefix != "gif"){
													  var parentdiv = document.getElementById("fileName");
													  var div = document.createElement("div");
													  div.setAttribute("id", "fileName1");
													  div.setAttribute("class", "fileName2");
													  div.innerHTML = fName+"&nbsp;&nbsp<a class='uploadfiles'>下载文件</a>&nbsp;&nbsp<a class='deletefiles'>删除文件</a>";
													  div.fileName = data[index].fileName;
													  div.id = data[index].id;
													  parentdiv.appendChild(div);
													  uploadFile(div);
													
												}else{
													var div = document.getElementById("picture1");
													var img = document.createElement("img");
													img.setAttribute("id", "imgs");
													img.getAttribute("id");
													img.setAttribute("class","imgs1");
													img.width=130;
													img.height=100;
													img.src = data[index].fileName;
													img.pictureId = data[index].id;
													div.appendChild(img);
													//div.appendChild(divpicture);
													mouseoverImg(img);
												}
											 }
											
										}
									},
									error : function(data, textStatus, jqXHR) {
										infoTip({
											content : "图片读取失败!",
											color : "#FF0000"
										});
									}
								});
							},
							error : function(data, textStatus, jqXHR) {
								infoTip({
									content : "删除失败!",
									color : "#FF0000"
								});
							}

						});
					
					};
			 });
			
//			 $(".imgs1").kendoTooltip({
//				 content: '<img src="'+img.src+'" style="width:480px;height:500px;/>',
//				// width: 50,
//				 position: "left"
//			 });
			
            })
	};
	
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

	};
	
	// 编辑农户信息
	farmerInfoWindow = {
		farmerGrid : {},
			
		obj : undefined,
		
		id : $("#farmerInfoWindow"),
		
		validator : undefined,
		
		init : function() {
			if (!farmerInfoWindow.id.data("kendoWindow")) {
				farmerInfoWindow.id.kendoWindow({
					width : "500px",
					maxHeight: "570px",
					actions : ["Maximize", "Close"],
					modal : true,
					title : "项目明细查看"
				});
			};
			farmerInfoWindow.obj = farmerInfoWindow.id.data("kendoWindow");
			//项目详情验证
			farmerInfoWindow.validator = $("#farmerInfoValidate").kendoValidator().data("kendoValidator");
		},

	};
	
	// 查看详情返回按钮
	$("#backProjectDetail").on("click", function() {
		projectDetailWindow.obj.close();
	});
	
	
	/* 财务窗口 */
	var lock = false;
	financeDetailsWindow = {
		financeListGrid : {},
		userDetailsListGrid : {},
		obj : undefined,
		id : $("#financeDetailsWindow"),
		init : function() {
			financeDetailsWindow.id.kendoWindow({
				width : "1000px",
				height : "500px",
				actions : [ "Maximize", "Close" ],
				modal : true,
				title : "财务明细查看",
				close : function() { // 窗口关闭事件
					 $.ajax({
						 cache:false,
						 type : "get",
						 url : "financeManagement/findByAccount/"+subId,
						 dataType : "json",
						 contentType : "application/json;charset=UTF-8",
						 success : function(data, textStatus, jqXHR) {
							 projectsManageGridObj.projectsManageGrid.dataSource.read();
						 },
						 error : function(data, textStatus, jqXHR) {
							 projectsManageGridObj.projectsManageGrid.dataSource.read();
						 },		 
					 });
				}
			});
			financeDetailsWindow.obj = financeDetailsWindow.id.data("kendoWindow");
		},

		showCount : function(data) {
			var projectsID = data.id;
			this.financeListGrid = $("#financeCount").kendoGrid({
				dataSource : {
					transport : {
						read : {
							type : "get",
							cache : false,
							url : "projectsManage/findByCount/"+ projectsID,
							dataType : "json",
							contentType : "application/json;charset=UTF-8"
						}
					},
					serverFiltering : true
	
				},

				reorderable : true,

				resizable : true,

				sortable : true,

				columnMenu : true,
				
				editable : "inline",
				columns : [
						{
							field : "id",
							hidden : true
						},
						{						
							title : "总资金(万元)",
							width : 60,
							template : function(dataItem) {
								return dataItem.totalFund;
							}
						},
						{
							title : "已拨款金额(万元)",
							width : 70,
							template : function(dataItem) {
								if (dataItem.bkTotal == null) {
									return "";
								}else if(dataItem.bkTotal < 0){
									return "0";
								}
								return dataItem.bkTotal;
							}
						},
						{
							title : "已报账金额(万元)",
							width : 70,
							template : function(dataItem) {
								if (dataItem.bzTotal == null) {
									return "";
								}
								return dataItem.bzTotal;
							}
						},
						{
							title : "可拨款金额(万元)", // 总金额-已拨款
							width : 70,
							template : function(dataItem) {
								if (dataItem.bkTotal == null || dataItem.bkTotal == "") {
									return dataItem.totalFund;
								} else {
									return (dataItem.totalFund - dataItem.bkTotal).toFixed(2);
								}
							}
						},
						{
							title : "可报账金额(万元)", // 已拨款-已报账
							width : 70,
							template : function(dataItem) {
								if (dataItem.bkTotal == null|| dataItem.bkTotal == "") { // 如果拨款金额为空。那么可报账金额就为0
									return "0";
								} else if (dataItem.bzTotal == null|| dataItem.bzTotal == "") { // 如果报账金额为空。那么可报账就 =拨款金额
									return dataItem.bkTotal;
								}else if(dataItem.bzTotal < 0){
									return "0";
								}else {
									return (dataItem.bkTotal - dataItem.bzTotal).toFixed(2);
								}
							}
						},
						{
							field : "balance",
							title : "项目结余(万元)",
							width : 80,
							template : function(dataItem) {
								if (dataItem.balance == null) {
									return "";
								}
								return dataItem.balance;
							}

						},
						{
							title : "操作",
							width : 120,
							template : function(dataItem) {
								if (dataItem.approver_status != 1) {
									approverStatus = dataItem.approver_status
									return "<a class='editBalance editIcon aLink  ' title='资金结余填写' style='margin-left:2px;'><i class='fa fa-pencil' ></i></a>&nbsp;&nbsp;&nbsp;&nbsp;"
											+ "<a class='approverFinance editIcon aLink' title='财务信息审核' style='margin-left:2px;'><i class='fa fa-arrow-up'></i></a>";
								} else {
									approverStatus = dataItem.approver_status
									return "<span>已审核</span>";
								}
							}
						} ],
				dataBound : function() {
					financeDetailsWindow.showEditBalance();
					financeDetailsWindow.approverFinanceClick();
				}
			}).data("kendoGrid");
		},
		//结余资金
		showEditBalance : function() {
			$(".editBalance").on("click",function(e) {
				var tr = $(e.target).closest("tr");
				var data = financeDetailsWindow.financeListGrid.dataItem(tr);
				var projects = {};
				var exp = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
				var jy = prompt("请输入项目的结余金额", data.balance == null ? 0 : data.balance);
				projects.id = data.id;
				projects.balance = jy;
				if (jy != null) {
					if (exp.test(jy)) {
						$.ajax({
							cache : false,
							url : "financeManagement/addByBalance",
							type : "post",
							dataType : "json",
							contentType : "application/json;charset=UTF-8",
							data : JSON.stringify(projects),
							success : function(data) {
								financeDetailsWindow.financeListGrid.dataSource.read();
								infoTip({
									content : data.message,
									color : "#6D5296"
								});
							},
							error : function(data) {
								financeDetailsWindow.financeListGrid.dataSource.read();
								infoTip({
									content : data.message,
									color : "#FF0000"
								});
							}
						});
					} else {
						alert("输入的格式不正确");
					}
				}
			});
		},

		

		// 项目的报表显示
		financeChart : function(pdata) {
			projects_id = pdata.id;
			$.ajax({
				url : "statisticalReport/projectsReport/" + projects_id,
				type : "GET",
				cache : false,
				contentType : "application/json;charset=UTF-8",
				success : function(data) {
					chart3D(data);
				}
			});
		},

		// 财务详细列表
		showDetailsList : function(data) {
			var projects_id = data.id;
			this.userDetailsListGrid = $("#financeList").kendoGrid({
				dataSource : {
					transport : {
						read : {
							type : "get",
							cache : false,
							url : "financeManagement/selectById/"+ projects_id,
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

				// toolbar :
				// kendo.template($("#template").html()),

				reorderable : true,

				resizable : true,

				sortable : true,

				columnMenu : true,

				pageable : {
					refresh : true
				},

				editable : "popup",

				columns : [
						{
							field : "id",
							hidden : true
						},
						{
							field : "certificateNum",
							title : "财务凭证编号",
							width : 120
						},
						{
							field : "appropriation",
							title : "拨款金额(万元)",
							width : 100
						},
						{
							field : "bk_user",
							title : "拨款人 ",
							width : 100
						},
						{
							field : "bk_date",
							title : "拨款日期",
							width : 100
						},
						{
							field : "account",
							title : "报账金额(万元)",
							width : 120,
						},
						{
							field : "bz_user",
							title : "报账人",
							width : 80
						},
						{
							field : "bz_date",
							title : "报账日期",
							width : 100
						},
						{
							title : "操作",
							template : function() {
								if(approverStatus != 1){
									return "&nbsp;&nbsp;<a class='editFiance editIcon aLink  ' title='财务信息修改' style='margin-left:2px;'><i class='fa fa-pencil' ></i></a>"
											+ "&nbsp;&nbsp;<a class='deleteFiance editIcon aLink   title='财务信息删除' style='margin-left:2px;'><i class='glyphicon glyphicon-trash'></i></a>"
											+ "&nbsp;&nbsp;<a class='detailFiance editIcon aLink   title='财务信息详情' style='margin-left:2px;'><i class='fa fa-eye'></i></a>";
								}else{
									return "<a class='detailFiance editIcon aLink   title='财务信息详情' style='margin-left:2px;'><i class='fa fa-eye'></i></a>";
								}
							},
							width : 120
					} ],
				dataBound : function() {
					financeDetailsWindow.detailFiance();
					financeDetailsWindow.removeFinance();
					financeDetailsWindow.editFiance();
				}
			}).data("kendoGrid");
		},
		
		/* 资金的审核 */
		approverFinanceClick : function() {
			$(".approverFinance").on("click",function(e) {
				var tr = $(e.target).closest("tr");
				var data = financeDetailsWindow.financeListGrid.dataItem(tr);
				var projectsID = data.id;
				if (confirm("确定审核此条财务信息吗？审核之后将不能对结余资金进行修改")) {
					$.ajax({
						url : "financeManagement/approverFinanceByProjects/"+ projectsID,
						type : "GET",
						cache : false,
						contentType : "application/json;charset=UTF-8",
						success : function(data) {						
							infoTip({
								content : data.message,
								color : "#D58512"
							});
							financeDetailsWindow.financeListGrid.dataSource.read();
							financeDetailsWindow.userDetailsListGrid.dataSource.read();
						},
						error : function(data) {
							infoTip({
								content : "操作失败!",
								color : "#FF0000"
							});
						}
					});
				}
			});
		},

		/* 财务信息详情 */
		detailFiance : function() {
			$(".detailFiance").on("click",function(e) {
				var tr = $(e.target).closest("tr");
				var data = financeDetailsWindow.userDetailsListGrid.dataItem(tr);
				$("#fid").html(data.id);
				$("#subprojectIdShow").html(subId);

				$("#certificateNumShow").html(data.certificateNum);
				$("#appropriationShow").html((data.appropriation == null) ? "" : data.appropriation + "万元");
				$("#operateUserShow").html(data.bk_user);
				$("#dateshow").html(data.bk_date);

				$("#accountshow").html((data.account == null ? "" : data.account+ "万元"));
				$("#bzUser").html(data.bz_user);
				$("#bzDate").html(data.bz_date);
				$("#remarkshow").html(data.remark);
				fianceInfoShowObjWindow.obj.title("查看财务信息");
				fianceInfoShowObjWindow.obj.center().open();
			});
		},

		/* 删除财务信息 */
		removeFinance : function() {
			$(".deleteFiance").on("click",function(e) {
				var tr = $(e.target).closest("tr");
				var data = financeDetailsWindow.userDetailsListGrid.dataItem(tr);
				var finance = {};
				finance.id = data.id
				finance.projects_id = subId;
				finance.certificateNum = data.certificateNum;
				finance.appropriation = data.appropriation;
				finance.account = data.account;
				finance.bz_user = data.bz_user;
				finance.bz_date = data.bz_date;
				finance.remark = data.remark;
				if (confirm("确定删除此条财务信息吗？")) {
					$.ajax({
						cache : false,
						type : "post",
						url : "financeManagement/remove",
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						data : JSON.stringify(finance),
						success : function(data,textStatus, jqXHR) {
							infoTip({
								content : data.message,
								color : "#D58512"
							});
							$.ajax({
								url : "statisticalReport/projectsReport/"
										+ projects_id,
								type : "GET",
								cache : false,
								contentType : "application/json;charset=UTF-8",
								success : function(data) {
									chart3D(data);
								}
							});

							financeDetailsWindow.userDetailsListGrid.dataSource.read();
							financeDetailsWindow.financeListGrid.dataSource.read();
						},
						error : function(data,
								textStatus, jqXHR) {
							infoTip({
								content : "删除失败!",
								color : "#FF0000"
							});
						}
					});
				}
			});
		},

		/* 编辑财务信息 */
		editFiance : function() {
			$(".editFiance").on("click",function(e) {
				var tr = $(e.target).closest("tr");
				var pdata = financeDetailsWindow.userDetailsListGrid
						.dataItem(tr);
				if (pdata.appropriation == null
						|| pdata.appropriation == "") { // 报账信息
					$("#bzfid").val(pdata.id);
					$("#bzsubprojectId").val(pdata.projects_id);
					$("#bzCertificateNum").val(pdata.certificateNum);
					$("#bzAccount").val(pdata.account);
					$("#bzuser").val(pdata.bz_user);
					$("#bzdate").val(pdata.bz_date);
					$("#bzremark").val(pdata.remark);
					baoZhangFinanceObjWindow.obj.title("修改报账信息");
					baoZhangFinanceObjWindow.obj.center().open();
					return;
				} else { // 拨款信息
					$("#fid").val(pdata.id);
					$("#subprojectId").val(pdata.projects_id);
					$("#certificateNum").val(pdata.certificateNum);
					$("#appropriation").val(pdata.appropriation);
					$("#operateUser").val(pdata.bk_user);
					$("#date").val(pdata.bk_date);
					$("#mark").val(pdata.remark);
					fianceInfoObjWindow.obj.title("修改拨款信息");
					fianceInfoObjWindow.obj.center().open();
					return;
				}
			});
		}
	}
	financeDetailsWindow.init();

	/* 日期格式 */
	$("#date").kendoDatePicker({
		culture : "zh-CN",
		format : "yyyy-MM-dd",
		max : new Date()
	});
	$("#bzdate").kendoDatePicker({
		culture : "zh-CN",
		format : "yyyy-MM-dd",
		max : new Date()
	});

	/* 【添加拨款信息】按钮 */
	$("#addFinance").on("click",function(e) {
		if(approverStatus == 1){
			alert("该项目的财务记录已经被审核通过。不能再添加拨款信息");
			return;
		}else{
			$("#fid,#certificateNum,#appropriation,#account,#operateUser,#date,#mark").val("");
			fianceInfoObjWindow.obj.title("添加拨款信息");
			fianceInfoObjWindow.obj.center().open();
		}
	});

	/* 【添加报账信息】按钮 */
	$("#addBaoZhangInfo").on("click",function() {
		if(approverStatus == 1){
			alert("该项目的财务记录已经被审核通过。不能再添加报账信息");
			return;
		}else{
			$("#bzfid,#bzCertificateNum,#bzAccount,#bzuser,#bzdate,#bzremark").val("");
			baoZhangFinanceObjWindow.obj.title("添加报账信息");
			baoZhangFinanceObjWindow.obj.center().open();
		}
	});

	/* 报账信息窗口 */
	baoZhangFinanceObjWindow = {

		obj : undefined,

		id : $("#baoZhangfoWindow"),

		validator : undefined,

		init : function() {
			if (!baoZhangFinanceObjWindow.id.data("kendoWindow")) {
				baoZhangFinanceObjWindow.id.kendoWindow({
					width : "700px",
					actions : [ "Close" ],
					modal : true,
					title : "报账信息",
					open : function(){
						lock = false;
					}
				});
			}
			baoZhangFinanceObjWindow.obj = baoZhangFinanceObjWindow.id.data("kendoWindow");
			// 用户信息验证
			baoZhangFinanceObjWindow.validator = $("#baozhangInfoValidate").kendoValidator().data("kendoValidator");
		}
	}
	baoZhangFinanceObjWindow.init();
	
	/* 【报账信息【确认】按钮 */
	
	$("#baoZhangSaveFiance").on("click",function(e) {
		e.preventDefault();
		if(!baoZhangFinanceObjWindow.validator.validate()){
			return;
		}
		if (lock == true) {
			return;
		}
		lock = true;
		var finance = {};
		finance.id = $("#bzfid").val();
		finance.projects_id = subId;
		finance.certificateNum = $("#bzCertificateNum").val();
		finance.account = $("#bzAccount").val();
		finance.bz_user = $("#bzuser").val();
		finance.bz_date = $("#bzdate").val();
		finance.remark = $("#bzremark").val();

		var url = (finance.id == null || finance.id == "") ? "financeManagement/add": "financeManagement/update";
		$.ajax({
			cache : false,
			type : "post",
			url : url,
			dataType : "json",
			contentType : "application/json;charset=UTF-8",
			data : JSON.stringify(finance),
			success : function(data) {
				if (data.resultCode == 0) {			
					//添加/更新成功:添加‘报账率’，‘报账金额’到项目数据中
					$.ajax({
						cache : false,
						type : "post",
						url : "financeManagement/addByFinancesToSubProject", // 添加[报账金额]和[报账率]
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						data : JSON.stringify(finance),
						success : function(data) {}
					});
					infoTip({
						content : data.message,
						color : "#D58512"
					});	
				} else {
					infoTip({
						content : data.message,
						color : "#FF0000"
					});
				}
				//添加/更新成功就刷图表
				$("#financeChart").val("");
				sendAjax(projects_id);
				financeDetailsWindow.financeListGrid.dataSource.read();
				financeDetailsWindow.userDetailsListGrid.dataSource.read();
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

	// 拨款信息窗口
	fianceInfoObjWindow = {

		obj : undefined,

		id : $("#fianceInfoWindow"),

		validator : undefined,

		init : function() {
			if (!fianceInfoObjWindow.id.data("kendoWindow")) {
				fianceInfoObjWindow.id.kendoWindow({
					width : "700px",
					actions : [ "Close" ],
					modal : true,
					title : "财务信息",
					close : function(e) {
						financeDetailsWindow.userDetailsListGrid.dataSource.read();
					},
					open : function(){
						lock = false;
					}
				});
			}
			fianceInfoObjWindow.obj = fianceInfoObjWindow.id.data("kendoWindow");
			// 用户信息验证
			fianceInfoObjWindow.validator = $("#fianceInfoWindow").kendoValidator().data("kendoValidator");
		}
	}
	fianceInfoObjWindow.init();

	/* 【拨款信息确认】按钮 */

	$("#saveFiance").on("click",function(e) {
		e.preventDefault();
		if(!fianceInfoObjWindow.validator.validate()){
			return;
		}
		if (lock == true) {
			return;
		}
		lock = true;
		var finance = {}, url = "";
		finance.id = $("#fid").val();
		finance.projects_id = subId;
		finance.appropriation = $("#appropriation").val();
		finance.certificateNum = $("#certificateNum").val();
		finance.bk_date = $("#date").val();
		finance.bk_user = $("#operateUser").val();
		finance.remark = $("#mark").val();

		url = (finance.id == null || finance.id == "") ? "financeManagement/add": "financeManagement/update";
			$.ajax({
				cache : false,
				type : "post",
				url : url,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				data : JSON.stringify(finance),
				success : function(data) {
					//添加/修改成功：“添加‘拨款金额’到项目数据中”
					if (data.resultCode == 0) {
						$.ajax({
							cache : false,
							type : "post",
							url : "financeManagement/addByProjectsFinance",
							dataType : "json",
							contentType : "application/json;charset=UTF-8",
							data : JSON.stringify(finance),
							success : function(data) {	
							}
						});
						infoTip({
							content : data.message,
							color : "#D58512"
						});
					} else {
						infoTip({
							content : data.message,
							color : "#FF0000"
						});
					}
					$("#financeChart").val("");
					sendAjax(projects_id);
					financeDetailsWindow.userDetailsListGrid.dataSource.read();
					financeDetailsWindow.financeListGrid.dataSource.read();
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

	// 信息详情查看窗口
	fianceInfoShowObjWindow = {

		obj : undefined,

		id : $("#fianceInfoShowWindow"),

		validator : undefined,

		init : function() {
			if (!fianceInfoShowObjWindow.id.data("kendoWindow")) {
				fianceInfoShowObjWindow.id.kendoWindow({
					width : "700px",
					actions : [ "Close" ],
					modal : true,
					title : "财务信息",
					close : function() {
						financeDetailsWindow.userDetailsListGrid.dataSource.read();
					}
				});
			}
			fianceInfoShowObjWindow.obj = fianceInfoShowObjWindow.id.data("kendoWindow");
			// 用户信息验证
			fianceInfoShowObjWindow.validator = $("#baozhangInfoValidate").kendoValidator().data("kendoValidator");
		}
	}
	fianceInfoShowObjWindow.init();

	/* 财务信息详情【返回】按钮 */
	$("#fanhui").on("click", function() {
		fianceInfoShowObjWindow.obj.close();
	});

	/* 拨款信息【取消】按钮 */
	$("#cancelFiance").on("click", function() {
		fianceInfoObjWindow.obj.close();
	});

	/* 【报账信息【取消】按钮 */
	$("#baoZhangCancelFiance").on("click", function() {
		baoZhangFinanceObjWindow.obj.close();
	});
	

	//调项窗口
	adjustmentRecordwindow={
			obj : undefined,
			id : $("#adjustmentRecordwindow"),
			validator : undefined,
			init : function() {
				if (!adjustmentRecordwindow.id.data("kendoWindow")) {
					adjustmentRecordwindow.id.kendoWindow({
						width : "80%",
						height : 400,
						actions : ["Close"],
						modal : true,
						title : "项目调项"
					});
				}
				adjustmentRecordwindow.obj = adjustmentRecordwindow.id.data("kendoWindow");
				adjustmentRecordwindow.validator = $("#adjustmentRecordValidate").kendoValidator().data("kendoValidator");
			},
			showAdjustmentRecord:function(subPdata){
				var projectName = subPdata.projectName;
				var adjustmentAreaId = subPdata.area.id;
				$("#adjustmentRecord").html("");
				this.projectsManageGrid = $("#adjustmentRecord").kendoGrid({
							dataSource : {
								transport : {
									read : {
										cache:false,
										type : "get",
										url : "projectsManage/searchAdjustmentRecord/"+adjustmentAreaId+"/"+projectName,
										dataType : "json",
										contentType : "application/json;charset=UTF-8"
									}
								}
							},

							reorderable : true,

							resizable : true,

							sortable : true,

							columnMenu : true,

							editable : "popup",

							columns: [
									    { field:"treeLevel",width:220, title: "项目名称",
									    	template : function(dataItem){
									    		if (dataItem.treeLevel==null) {
									    			return dataItem.projectName;
												}else{
													return dataItem.treeLevel+" "+dataItem.projectName;
												}
									    	}
									    },
									    { field: "projectType",width:120, title: "项目类型",
									    	template : function(dataItem){
									    		if(dataItem.projectType==1){
									    			return "种植";
									    		}else if(dataItem.projectType==2){
									    			return "基础论证";
									    		}else if(dataItem.projectType==3){
									    			return "培训";
									    		}else if(dataItem.projectType==4){
									    			return "贴息";
									    		}else if(dataItem.projectType==5){
									    			return "经济组织";
									    		}else if(dataItem.projectType==6){
									    			return "其他";
									    		}else{
									    			return "";
									    		}
									    	}
									    },
									    { field: "referenceNumber", title: "项目文号", width: 120},
									    { field: "carryOutUnit", title: "实施单位", width: 120},
									    { field: "totalFund", title: "总资金(万)", width: 120},
									    { field: "scaleAndContent", title: "建设规模及内容", width: 200},
									    { field: "area.areaName", title: "地区", width: 80},
									    { field: "bzTotal", 
									      title:"总报账金额",
									      width: 120,
									      template:function(data){
										    	if(data.bzTotal == null ||data.bzTotal == 0){
										    		return "";
										    	}else{
										    		return data.bzTotal;
										    	}
									      }
									    },{ field: "bkTotal", 
										      title:"总拨款金额",
										      width:120,
										      template:function(data){
											    if(data.bkTotal == null || data.bkTotal == 0){
											    	return "";
											    }else{
											    	return data.bkTotal;
											    }
										   }
										 },
										 { title:"报账率", 
										      width:120,
										      template : function(dataItem){
										    	 if(dataItem.isLevel == 1) {
											    	  	if(dataItem.financebiLv == "" || dataItem.financebiLv == null ||dataItem.financebiLv == 0 || dataItem.totalFund == null){
															return "<progress class='baozhang' max='100' value='0' style='width:50px;'></progress>&nbsp;0%";
											    	  	}else{										
															var progress = (parseFloat(dataItem.financebiLv) / parseFloat(dataItem.totalFund)*100).toFixed(2);
															return "<progress class='baozhang' max='100' value='"+progress+"' style='width:50px;'></progress>&nbsp;"+progress+"%";
											    	  	}
										    	 }else{
										    		  return "";
										    	 }  	
										      }
										    },
										{
											title : "确定项目范围",
											width : 110,
											template : function(dataItem) {
												if(dataItem.isLevel == 1) {
													return $("#determineProjectsRange").html();
												}else {
													return "";
												}
											}
										},
										{
											title : "监管记录",
											width : 90,
											template : function(dataItem) {
												if(dataItem.isLevel == 1) {
													return $("#showSupervisionsTemplate").html();
												}else {
													return "";
												}
											}
										},{
											title : "验收记录",
											width : 90,
											template : function(dataItem) {
												if(dataItem.isLevel == 1){
											        return $("#showChecksTemplate").html();
												}else {
													return "";
												}	
												
											}
										},
										 { field: "createUser", 
										      title:"操作人",
										      width:90
										 },
										 { field: "createTime", 
										      title:"操作时间",
										      width:120,
										      template : function(date){
										    	  var date = new Date(date.createTime);
										    	  return date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate();	  	
											  }
										 },
										 { field: "adjustmentReason", 
										      title:"调项原因",
										      width:500
										 }
									],
									dataBound : function() {
										projectsManageGridObj.getDetermineRange();
										projectsManageGridObj.getSupervisionsClick();
										projectsManageGridObj.getChecksClick();
								},
							editable : "popup"
						}).data("kendoGrid");
			}
	};
	
	function test(){
		var arr = new Array()
		arr.push('51.5001524,-0.1262362');
		arr.push('52.5001524,-1.1262362');
		arr.push('53.5001524,-2.1262362');
		arr.push('54.5001524,-3.1262362');
		computeArea(arr);
	};
	function computeArea(area) {
		var a = new Array();

		for(var i=0; i<area.length; i++)
		{
		    a[i] = new google.maps.LatLng(area[i][0],area[i][1]);
		}
//
//		poligon = new google.maps.Polygon({
//		    paths: a,
//		    strokeColor: "#22B14C",
//		    strokeOpacity: 0.8,
//		    strokeWeight: 2,
//		    fillColor: "#22B14C",   
//		    fillOpacity: 0.35   
//		})  
//
//		poligon.setMap(map);//until here is ok 
		var areaResult = google.maps.geometry.spherical.computeArea(a);
		return areaResult;
	};
	
	deleteRemarkWindow.init();
	fileImportWindow.init();
	pifufileUploadObj.init();
	fileUploadObj.init();
//	areaObj.init();
	projectDetailWindow.init();
	projectsManageWindow.init();
	showChecksWindow.init();
	showSupervisionsWindow.init();
	//updateProjectsManageWindow.init();
	adjustmentRecordwindow.init();
	//projectsManageGridObj.init();
	adjustmentProjectsManageWindow.init();
	determineRangeWindow.init();
	farmerInfoWindow.init();
	//farmerUploadObj.init();
});

function sendAjax(projects_id){
	$.ajax({
		url : "statisticalReport/projectsReport/"+ projects_id,
		type : "GET",
		cache : false,
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
			chart3D(data);												
		}
	});
}
function chart3D(data){
	var chart =  new FusionCharts("resources/plugins/fusioncharts-suite-xt/swf/Pie2D.swf", "myChartPie2D", "500", "300");  
    chart.setDataXML(data);            
    chart.render('financeChart'); 
}

/*屏蔽浏览器的内容被选中*/
function mark_forBiddenSelect(){
	$(document).bind("selectstart",function(){
		return false;
	});
	document.onselectstart = new Function("event.returnValue = false;");
	$("*").css({"-moz-user-select":"none"});
}


