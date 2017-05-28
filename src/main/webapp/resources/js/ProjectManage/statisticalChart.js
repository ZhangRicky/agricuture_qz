$(function(){
	var lock= false;
	var d = new Date();
	var selectYear = "";
	var year = d.getFullYear();		//当前年
	var villageList,townList,projectsList;
	//年份的加载
	var industry_year = document.getElementById('industry_year');
	for(var i = year;i>=2010;i--){
		var opt_year = new Option(i,i);	
		industry_year.add(opt_year);	
	}
	var town_year = document.getElementById('town_year');
	for(var i = year;i>=2010;i--){
		var opt_year = new Option(i,i);	
		town_year.add(opt_year);
	}
	var village_year = document.getElementById("village_year");
	for(var i = year;i>=2010;i--){
		var opt_year = new Option(i,i);	
		village_year.add(opt_year);
	}	
	var projectCount_year = document.getElementById("projectCountYear");
	for(var i = year;i>=2010;i--){
		var opt_year = new Option(i,i);	
		projectCount_year.add(opt_year);
	}
	
	
	
	/*加载下拉选的镇*/	
	villageList=$("#town").kendoDropDownList({
		optionLabel: "--请选择镇级--",
		dataTextField: "areaName",
        dataValueField: "id",
        dataSource: {
            transport: {
                read: {
                	cache:false,
                    dataType: "json",
                    url: "statisticalReport/getList_ForTown/2/1"
                }
            }
        }
	});

	
	/*加载下拉选数据*/
	townList=$("#town").kendoDropDownList({
		optionLabel: "--请选择镇级--",
		dataTextField: "areaName",
        dataValueField: "id",
        dataSource: {
            transport: {
                read: {
                	cache:false,
                    dataType: "json",
                    url: "statisticalReport/getList_ForTown/2/1"
                }
            }
        },dataBound: function(e){
	    	var ul = townList.data("kendoDropDownList").ul;
	    	if(ul && ul.length == 1 && ul.children.length>0){
	    		townList.data("kendoDropDownList").select(0);
	    		$("#villageId").empty();
				$fzid=$("#town").val();
				villageList=$("#villageId").kendoDropDownList({
					optionLabel: "--请选择村级--",
					dataTextField: "areaName",
					dataValueField: "id",
					dataSource: {
						transport: {
							read: {
								cache:false,
								dataType: "json",
								//url: "statisticalReport/getList_ForTown/1/"+$fzid
							}
						}
					},
					dataBound: function(e){
						var ul = villageList.data("kendoDropDownList").ul;
						if(ul && ul.length == 1 && ul.children.length>0){
							villageList.data("kendoDropDownList").select(0);						
						}
					}
				});
	    	}
	    }
	});

	/*乡镇变动时加载相应的村*/
	$("#town").on("change",function(){
		$("#villageId").empty();
		$fzid=$("#town").val();
		villageList=$("#villageId").kendoDropDownList({
			optionLabel: "--请选择村级--",
	        dataTextField: "areaName",
	        dataValueField: "id",
	        dataSource: {
	            transport: {
	                read: {
	                	cache:false,
	                    dataType: "json",
	                    url: "statisticalReport/getList_ForTown/1/"+$fzid
	                }
	            }
	        },
	        dataBound: function(e){
	        	var ul = villageList.data("kendoDropDownList").ul;
	        	if(ul && ul.length == 1 && ul.children.length>0){
	        		villageList.data("kendoDropDownList").select(0);
	        	}
	        }
	    });
		villageList.data("kendoDropDownList").select(0);
	});
	
	//$("#town").data("kendoDropDownList").select();
	//$("#villageId").kendoDropDownList();
	//$("#villageId").data("kendoDropDownList").select();
	
	$.ajax({
		url : "statisticalReport/findReportByColumn3D/"+year,
		type : "GET",
		cache:false,
		contentType: "application/json;charset=UTF-8",
		success : function(data) {
			createDoughnut3D(data);
		},
		error : function(data) {
			infoTip({
				content : "统计查询异常错误",
				color : "#FF0000"
			});
		}
	});
	
	
	/*市级产业统计*/
	$("#cityIndustry").on("click",function(){
		$("#city,#financePage").css("display","block");
		$("#townFinanceCount,#projects,#villageContent").css("display","none");
		$("#industry").val("");
		$.ajax({
			url : "statisticalReport/findReportByColumn3D/"+year,
			type : "GET",
			cache:false,
			contentType: "application/json;charset=UTF-8",
			success : function(data) {
				createDoughnut3D(data);
			},
			error : function(data) {
				infoTip({
					content : "统计查询异常错误",
					color : "#FF0000"
				});
			}
		});
	});	
	
	$("#searchBtn_industry").on("click",function(){
		if(lock){
			return;
		}
		lock = true;
		var sele_year = $("#industry_year").val();
		if(!sele_year.match("^(\\d{1,4})$") || sele_year == ""){
			alert("请选择年份");
			lock = false;
			return;
		}
		$("#industry").val("");
		$.ajax({
			url : "statisticalReport/findReportByColumn3D/"+sele_year,
			type : "GET",
			cache:false,
			contentType: "application/json;charset=UTF-8",
			success : function(data) {
				createDoughnut3D(data);
			},
			error : function(data) {
				infoTip({
					content : "统计查询异常错误",
					color : "#FF0000"
				});
			}
		});
		lock = false;
	})
	
	function createDoughnut3D(data){
		var myChart = new FusionCharts("resources/plugins/fusioncharts-suite-xt/swf/Pie2D.swf","MyIndustry","700","400");
		myChart.setDataXML(data);
		myChart.render("industry");
	}
	
	
	
	/*镇级项目年度资金统计*/
	$("#townFinance").on("click",function(){
		$("#financePage,#townFinanceCount").css("display","block");	//资金统计也显示
		$("#city,#villageContent,#projects").css("display","none");	
		$("#townFunds","#villageFinanceCount").val("");	
		$.ajax({
			url : "statisticalReport/findReportByScrollCombiDY2D/"+year,
			type : "GET",
			cache:false,
			contentType: "application/json;charset=UTF-8",
			success : function(data) {
				MSColumn3DLineDY(data)
			},
			error : function(data) {
				infoTip({
					content : "统计查询异常错误",
					color : "#FF0000"
				});
			}
		});
	});	
	
	$("#searchBtn_town").on("click",function(){
		$("#townFunds").val(" ");	
		$("#villageFinanceCount").empty()
		if(lock){
			return;
		}
		lock = true;
		var townYear = $("#town_year").val();
		if(!townYear.match("^(\\d{1,4})$") || townYear == ""){
			alert("请选择年份");
			lock = false;
			return;
		}
		$.ajax({
			url : "statisticalReport/findReportByScrollCombiDY2D/"+ townYear,
			type : "GET",
			cache:false,
			contentType: "application/json;charset=UTF-8",
			success : function(data) {
				$("#townFunds").val("");
				MSColumn3DLineDY(data)
			},
			error : function(data) {
				infoTip({
					content : "统计查询异常错误",
					color : "#FF0000"
				});
			}
		});
		lock= false;
	})
	
	function MSColumn3DLineDY(data){
		var myColumn3D = new FusionCharts("resources/plugins/fusioncharts-suite-xt/swf/Column3D.swf","MyTownFunds","850","370");
		myColumn3D.setDataXML(data);
		myColumn3D.render("townFunds");
	}
	
	
	
	
	/*按村的统计*/
	$("#villageIndustry").on("click",function(){
		$("#financePage,#villageContent").css("display","block");
		$("#projects,#city,#townFinanceCount").css("display","none");
		$("#villageProjectsCount,#villageProjectsDoneCount").val("");
		townList.data("kendoDropDownList").select(-1);
		villageList.data("kendoDropDownList").select(-1);
		
		/*资金统计--按村资金统计【查询按钮】*/
		$("#searchBtn_village").on("click",function(){
			if(lock){
				return;
			}
			lock = true;
			var townId=$("#town").val();
			var villageId=$("#villageId").val();
			var village_year = $("#village_year").val();
			var project_type = $("#project_type").val();
			if(townId == null ||townId == ""){
				alert("请选择需要统计的乡镇");
				lock = false;
				return;
			}
			if(villageId==null || villageId == ""){	
				alert("请选择需要统计的村");
				lock= false;
				return;
			}
			if(!village_year.match("^(\\d{1,4})$") || village_year == ""){
				alert("请选择统计的日期");
				lock= false;
				return;
			}
			if(project_type === '已完成项目'){
				//查询完成项目
				$.ajax({
					url : "statisticalReport/reportForProjectsFinlished/"+townId+"/"+villageId+"/"+village_year,
					type : "GET",
					cache:false,
					contentType: "application/json;charset=UTF-8",
					success : function(data) {
						if(data == "null"){
							infoTip({
								content : "没有相关的统计信息哟~",
								color : "#FF0000"
							});
						}else{
							var MSColumn3D = new FusionCharts("resources/plugins/fusioncharts-suite-xt/swf/ScrollCombi2D.swf","myVillageProjectsDoneCount","850","370");
							MSColumn3D.setDataXML(data);
							MSColumn3D.render("villageProjectsCount");
						}
						lock=false;
					},
					error : function(data) {
						lock=false;
						infoTip({
							content : "完成项目栏统计查询异常",
							color : "#FF0000"
						});
					}
				});
			}
			//查询下达项目
			if(project_type === "已下达项目"){
				$.ajax({
					url : "statisticalReport/reportForVillage/"+townId+"/"+villageId+"/"+village_year,
					type : "GET",
					cache:false,
					contentType: "application/json;charset=UTF-8",
					success : function(data) {
						if(data == "null"){
							infoTip({
								content : "没有相关的统计信息哟~",
								color : "#FF0000"
							});
						}else{						
							var MSColumn3D = new FusionCharts("resources/plugins/fusioncharts-suite-xt/swf/ScrollCombiDY2D.swf","myVillageProjectsCount","850","370");
							MSColumn3D.setDataXML(data);
							MSColumn3D.render("villageProjectsCount");
						}
						lock=false;
					},
					error : function(data) {
						lock=false;
						infoTip({
							content : "完成项目栏统计查询异常",
							color : "#FF0000"
						});
					}
				});
			}
		});
	})

	/**
	 * 项目统计
	 */
	$("#projectsCount").on("click",function(){
		$("#projectsCountIndustry,#projects").css("display","block");
		$("#projectsNameIndustry,#financePage").css("display","none");
		$("#projectsCountReport,#projectCountYear").val("");
		$("#projectsCountReportByVillage").html("");
		sendAjax(year);
	})
	
	$("#searchBtn_count").on("click",function(){
		var pro_year = $("#projectCountYear").val();
		$("#projectsCountReportByVillage").empty()
		if(lock){
			return;
		}
		lock= true;
		selectYear = pro_year;
		$("#projectsCountReport").val("");
		if(pro_year == null || !pro_year.match("^(\\d{1,4})$")){
			alert("请选择查询年份");
			lock = false;
			return;
		}
		sendAjax(pro_year);
	})
	
	/*发送请求*/
	function sendAjax(year){
		$.ajax({
			url : "statisticalReport/reportByProjectsCount/"+year,
			type : "GET",
			cache:false,
			contentType: "application/json;charset=UTF-8",
			success : function(data) {
				lock = false;
				createColumn3D(data);
			},
			error : function(data) {
				lock = false;
				infoTip({
					content : "统计查询异常错误",
					color : "#FF0000"
				});
			}
		});
	}
	
	function createColumn3D(data){
		var Column3D = new FusionCharts("resources/plugins/fusioncharts-suite-xt/swf/Column3D.swf","myColumn3D","850","370");
		Column3D.setDataXML(data);
		Column3D.render("projectsCountReport");
	}
	
	
	/**
	 * 按地区的项目名称统计。加载地区列表
	 */
	$("#projectsIndustry").on("click",function(){	
		$("#projects,#projectsNameIndustry").css("display","block");
		$("#projectsCountIndustry,#financePage").css("display","none");
		$("#projectsByNameCount").html("");
		townList.data("kendoDropDownList").select(-1);
		//projectsList.data("kendoDropDownList").select(-1);
		$("#projectsName").val("");
		townList=$("#projectByTown").kendoDropDownList({
			optionLabel: "--请选择乡镇--",
			dataTextField: "areaName",
	        dataValueField: "id",
	        dataSource: {
	            transport: {
	                read: {
	                	cache:false,
	                    dataType: "json",
	                    url: "statisticalReport/getList_ForTown/2/1"
	                }
	            }
	        }
		});
		townList.data("kendoDropDownList").select(0);
	})
	
	/**
	 *地区变动时。加载该地区下面的项目
	 */	
	$("#projectByTown").on("change",function(){
		var townId=$("#projectByTown").val();
		projectsList=$("#projectsName").kendoDropDownList({
			optionLabel: "--请选择项目--",
	        dataTextField: "projectName",
	        dataValueField: "id",
	        dataSource: {
	            transport: {
	                read: {
	                	cache:false,
	                    dataType: "json",
	                    url: "statisticalReport/projectNameByTown/"+townId
	                }
	            }
	        },
	        dataBound: function(e){
	        	var ul = projectsList.data("kendoDropDownList").ul;
	        	if(ul && ul.length == 1 && ul.children.length>0){
	        		projectsList.data("kendoDropDownList").select(0);
	        	}
	        }
	    });
		projectsList.data("kendoDropDownList").select(0);
	});
	
	//$("#projectsName").data("kendoDropDownList").select();
	
	

	
	$("#searchBtn_projectsName").on("click",function(){
		if(lock){
			return;
		}
		lock = true;
		var townId=$("#projectByTown").val();
		var projectNameId=$("#projectsName").val();
		var projectYear = $("#projectYear").val();
		if(townId == null ||townId == ""){
			alert("请选择需要统计的乡镇");
			lock = false;
			return;
		}
		if(projectNameId==null || projectNameId == ""){	
			alert("请选择项目名称");
			lock= false;
			return;
		}
		$.ajax({
			url : "statisticalReport/reportByProjectsName/"+townId+"/"+projectNameId,
			type : "GET",
			cache:false,
			contentType: "application/json;charset=UTF-8",
			success : function(data) {
				$("#projectsByNameCount").val("");
				if(data == "null"){
					infoTip({
						content : "没有相关的统计信息哟~",
						color : "#FF0000"
					});
				}else{	
					var MSStackedColumn2DLineDY = new FusionCharts("resources/plugins/fusioncharts-suite-xt/swf/StackedColumn3DLineDY.swf","MSStackedColumn2DLineDY","850","370");
					MSStackedColumn2DLineDY.setDataXML(data);
					MSStackedColumn2DLineDY.render("projectsByNameCount");
				}
				lock = false;
			},
			error : function(data) {
				lock = false;
				infoTip({
					content : "统计查询异常错误",
					color : "#FF0000"
				});
			}
		});

	})
})

//点击资金统计的柱状，统计乡镇下各村的资金统计情况
function findByVillageFinance(town){
	$("#villageFinanceCount").html("");
	$.ajax({
		url : "statisticalReport/reportByVillageFinanceCount/"+town,
		type : "GET",
		cache:false,
		contentType: "application/json;charset=UTF-8",
		success : function(data) {
			if(data== "null"){
				infoTip({
					content : "无相关统计数据",
					color : "#FF0000"
				});
			}else{
				var VillageFinanceCount = new FusionCharts("resources/plugins/fusioncharts-suite-xt/swf/ScrollCombi2D.swf","myVillageFinacneCount","850","370");
				VillageFinanceCount.setDataXML(data);
				VillageFinanceCount.render("villageFinanceCount");
			}
		},
		error : function(data) {
			infoTip({
				content : "统计查询异常错误",
				color : "#FF0000"
			});
		}
	});
}


//点击柱状加载相应村的项目数量统计情况
function findByVillage(town){
	//alert(town);
	$("#projectsCountReportByVillage").html("");
	$.ajax({
		url : "statisticalReport/reportByVillageOfProjects/"+town,
		type : "GET",
		cache:false,
		contentType: "application/json;charset=UTF-8",
		success : function(data) {
			if(data== "null"){
				infoTip({
					content : "无相关统计数据哟~",
					color : "#FF0000"
				});
			}else{
				var myCombination2D = new FusionCharts("resources/plugins/fusioncharts-suite-xt/swf/ScrollCombi2D.swf","Combination2D","850","370");
				myCombination2D.setDataXML(data);
				myCombination2D.render("projectsCountReportByVillage");
			}
		},
		error : function(data) {
			infoTip({
				content : "统计查询异常错误哟~",
				color : "#FF0000"
			});
		}
	});
}


