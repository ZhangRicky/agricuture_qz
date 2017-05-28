﻿﻿﻿﻿$(function() {
	var subProjectManageGridObj = {}, subProjectManageWindow = {}, subProjectManageDetailWindow={}, showChecksWindow = {}, labelStyle={};
	// 用户列表
	subProjectManageGridObj = {
		subProjectManageGrid : {},
		init : function() {
			this.subProjectManageGrid = $("#subProjectManageList")
					.kendoGrid(
							{
								dataSource : {
									transport : {
										read : {
											cache:false,
											type : "get",
											url : "subProjectManage/search/all",
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
								    		title : "子项目名称"
								    	},
								    	{
								    		field : "subProjectNumber",
								    		title : "子项目文号"
								    	},
										{
											field : "project.projectName",
											title : "主项目"
										},
										{
											field : "farmerName",
											title : "农户名称"
										},
										{
											field : "totalCapital",
											title : "总资金(万)"
										},
										{
											field : "implementationUnit",
											title : "实施单位"
										},
										{
											field : "projectScaleAndContent",
											title : "建设规模及内容"
										},
										{
											field : "constructionMode",
											title : "建设方式",
												template:function(data){
													if(data.checkStatus==0){
														return "先建后款";
													}else if (data.checkStatus==1){
														return "先款后建";
													}
												}
										},
//										{	
//											field : "checkStatus",
//											title : "验收状态",
//											template:function(data){
//												if(data.checkStatus==0){
//													return "已验收";
//												}else if (data.checkStatus==1){
//													return "未验收";
//												}
//											}
//										},
										{
											title : "监管记录",
											width : 80,
											template : kendo.template($("#showSupervisionsTemplate").html())
										},
										{
											title : "验收记录",
											width : 80,
											template : kendo.template($("#showChecksTemplate").html())
										},
										{
											title : "操作",
											width : 90,
											template : kendo.template($("#detailScan").html())
										}],
								dataBound : function() {
									subProjectManageGridObj.getSupervisionsClick();
									subProjectManageGridObj.getChecksClick();
									subProjectManageGridObj.addSubProjectClick();
									subProjectManageGridObj.updateSubProjectClick();
									subProjectManageGridObj.deleteSubProjectClick();
									subProjectManageGridObj.detailSubProjectClick();
									subProjectManageGridObj.saveSubProjectClick();
									subProjectManageGridObj.cancelUserClick();
								},
								editable : "popup"
							}).data("kendoGrid");
		},
		getSupervisionsClick:function(){
			$(".getSupervisions").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = subProjectManageGridObj.subProjectManageGrid.dataItem(tr);
				showSupervisionsWindow.obj.title("查看项目监管记录 - " + data.project.projectName );
				showSupervisionsWindow.obj.center().open();
				showSupervisionsWindow.initMap(data.id);
			});
		},
		
		getChecksClick:function(){
			$(".getChecks").on("click", function(e) {
				var tr =  $(e.target).closest("tr");
				var data = subProjectManageGridObj.subProjectManageGrid.dataItem(tr);
				showChecksWindow.obj.title("查看项目验收记录 - " + data.project.projectName );
				showChecksWindow.obj.center().open();
				showChecksWindow.initMap(data.id);
			});
		},
		
		addSubProjectClick:function(){
			$("#addSubProject").on(
					"click",
					function() {
						$("#subPid,#farmerName,#project,#subProjectNumber,#subProjectName,#totalCapital,#implementationUnit,#subProjectArea,#projectScaleAndContent,#constructionMode").val("");
						var size = $("#checkStatus").data("kendoDropDownList");
						var projectList=$("#project").kendoDropDownList({
							dataTextField: "projectName",
					        dataValueField: "id",
					        dataSource: {
					            transport: {
					                read: {
					                	cache:false,
					                	dataType : "json",
										url : "project/getPrjoectList",
					                }
					            }
					        },
					    });
						subProjectManageWindow.obj.title("添加子项目");
						subProjectManageWindow.obj.center().open();
			});
		},
		
		//保存 【添加】/【编辑】的子项目
		saveSubProjectClick : function() {
			$('form').on('submit', function() {
				var subProject = {},url="";
				subProject.id = $("#subPid").val();
				subProject.projectId=$("#project").val();
				subProject.farmerName=$("#farmerName").val();
				subProject.subProjectName=$("#subProjectName").val();
				subProject.subProjectNumber=$("#subProjectNumber").val();
				subProject.totalCapital=$("#totalCapital").val();
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
						subProjectManageGridObj.subProjectManageGrid.dataSource.read();
					},
					error : function(data, textStatus, jqXHR) {
						infoTip({
							content : "保存失败!",
							color : "#FF0000"
						});
					},
				});
		        subProjectManageWindow.obj.close();
		        return false; // 阻止表单自动提交事件
		    });
		},			
		
		// 取消 【添加】/【编辑】的子项目
		cancelUserClick : function() {
			$("#cancelSubProject").on("click", function() {
				subProjectManageWindow.obj.close();
			});
		},
		
		
		
		//子项目信息详情
		detailSubProjectClick:function(){
			$(".subProjectDetail").on("click",function(e) {
				var tr =  $(e.target).closest("tr");
				var pdata = subProjectManageGridObj.subProjectManageGrid.dataItem(tr);
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
				}else if (pdata.checkStatus ==1){
					result += "验收通过";
				}else if (pdata.checkStatus ==2){
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
				var subPdata = subProjectManageGridObj.subProjectManageGrid.dataItem(tr);
				//获取主项目
					var projectList=$("#project").kendoDropDownList({
						dataTextField: "projectName",
				        dataValueField: "id",
				        dataSource: {
				            transport: {
				                read: {
				                	cache:false,
				                	dataType : "json",
									url : "project/getPrjoectList",
				                }
				            }
				        },
				    });
				$("#subPid").val(subPdata.id);
				$("#project").data("kendoDropDownList").select();
				$("#farmerName").val(subPdata.farmerName);
				$("#subProjectName").val(subPdata.subProjectName);
				$("#subProjectNumber").val(subPdata.subProjectNumber);
				$("#totalCapital").val(subPdata.totalCapital);
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
				var data = subProjectManageGridObj.subProjectManageGrid.dataItem(tr);
				if (confirm("确定删除子项目信息么？")) {
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
							subProjectManageGridObj.subProjectManageGrid.dataSource.read();
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
		}		
	};
	
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
	
	subProjectManageWindow = {
			obj : undefined,
			id : $("#subProjectManageWindow"),
			validator : undefined,
			init : function() {
				if (!subProjectManageWindow.id.data("kendoWindow")) {
					subProjectManageWindow.id.kendoWindow({
						width : "650px",
						height:"85%",
						actions : ["Close"],
						modal : true,
						title : "添加子项目"
					});
                }
				subProjectManageWindow.obj = subProjectManageWindow.id.data("kendoWindow");

			subProjectManageWindow.validator = $("#subProjectValidate").kendoValidator().data("kendoValidator");
			}
		};
	
	$('#searchBtn').on('click', function() {
		var xxnr = $("#inputSearch").val();
		$("#subProjectManageList").data("kendoGrid").dataSource.filter([{
			field : "xxnr",
			value : xxnr
		}]);
	});

	$('#inputSearch').on('keyup', function() {
		var xxnr = $("#inputSearch").val();
		subProjectManageGridObj.subProjectManageGrid.dataSource.filter([{
			field : "xxnr",
			value : xxnr
		}]);
	});
	
	// 返回
	$("#backSubProjectManageDetail").on("click", function() {
		subProjectManageDetailWindow.obj.close();
	});
	
	labelStyle = {
		position: "relative",
		color : "red",
		fontSize : "12px",
		width : "80px",
		height : "80px",
		border:"0",
		lineHeight : "20px",
		fontFamily:"微软雅黑"
	}
	//查看项目监管记录window
	showSupervisionsWindow = {
		obj : undefined,
		id : $("#showSupervisionsWindow"),
		validator : undefined,
		map : undefined,
		sId : undefined,
		init : function() {
			if (!showSupervisionsWindow.id.data("kendoWindow")) {
				showSupervisionsWindow.id.kendoWindow({
					width : "1000px",
					height:"700px",
					actions : ["Maximize", "Close"],
					modal : true,
					title : "查看项目监管记录"
				});
			}
			showSupervisionsWindow.obj = showSupervisionsWindow.id.data("kendoWindow");
		},
		initMap : function(sId) {
			this.sId = sId;
			//初始化地图
			var map = new BMap.Map("showSupervisionsMap");
			this.map = map;
//			var point = new BMap.Point(106.657744,26.657144);
			var point = new BMap.Point(106.62677,26.668131);
			map.centerAndZoom(point, 15);
			map.enableScrollWheelZoom(true); 
			
			//显示当前位置
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r){
				if(this.getStatus() == BMAP_STATUS_SUCCESS){
//					var mk = new BMap.Marker(r.point);
//					map.addOverlay(mk);
					map.panTo(r.point);
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
			var geolocationControl = new BMap.GeolocationControl();
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
			
			this.initOverlays(map);
		},
		initOverlays: function(map){
			$.ajax({
				cache:false,
				type : "get",
				url : "supervision/get/"+ this.sId,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				success : function(data, textStatus, jqXHR) {
					if(data.length > 0){
						for(var i in data){
							var dd = data[i];
							if(dd.point && !isNaN(dd.point.lng) && !isNaN(dd.point.lat)){
								var point1 = new BMap.Point(dd.point.lng, dd.point.lat);
								var marker1 = new BMap.Marker(point1);//创建标注
								if(i==0){
									map.centerAndZoom(point1,16);
								}
								
								var msg = "<div> 存在问题: "+dd.existingProblems+ "<br/>整改建议: "+dd.correctSuggestions + "<br/>整改时间: "+dd.correctTime + "</div>";
								var imgs = '';
								if(dd.pictures && dd.pictures.length > 0){
									var pitArr = dd.pictures.split(';');
									for(var j=0;j<pitArr.length, pitArr [j].length > 0;j++){
										imgs += '<img src="'+pitArr[j]+'" class="zoomSize" style="margin-top:8px;float:bottom;zoom:1;overflow:hidden;width:50px;height:50px;margin-left:3px;"/>';
//										imgs += '<img class="demo-trigger" src="'+pitArr[j]+'?w=50" data-zoom="'+pitArr[j]+'?w=200" default_width=50 default_height=50 style="transition:all 0.3s;width:50px;height:50px;"/>';
//										imgs += '<a class="demo-trigger" href="'+pitArr[j]+'?w=150&ch=DPR&dpr=2"><img class="demo-trigger" src="'+pitArr[j]+'?w=50&ch=DPR&dpr=2&border=1,ddd" style="width:50px;height:50px;"/></a>';
										
									}
								}
//								$(".zoomSize").on('click',function(){
//									alert(this.src);
//								});
								
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
					}
					
					
				},
				error : function(data, textStatus, jqXHR) {
					infoTip({
						content : "查询监控记录失败！",
						color : "#FF0000"
					});
				}
			});
			
			var opts = {
					width : 250,     // 信息窗口宽度
					height: 80,     // 信息窗口高度
					title : "项目监管" , // 信息窗口标题
					enableMessage:true//设置允许信息窗发送短息
				   };
			
			// 编写自定义函数,创建标注
			function addMarker(point){
				var marker = new BMap.Marker(point);
				map.addOverlay(marker);
			};
			function addClickHandler(infotip,marker){
				marker.addEventListener("mouseover", function(e){
//					var searchInfoWindow = new BMapLib.SearchInfoWindow(map, infotip.content, {
//						title  : "项目监控",      //标题
//						width  : 290,             //宽度
//						height : 105,              //高度
//						panel  : "panel",         //检索结果面板
//						enableAutoPan : true,      //自动平移
//						enableCloseOnClick : true, //点击关闭
//						searchTypes :[],
//						message: "aaa"
//					});
//				    searchInfoWindow.open(marker);
					var opts = {
					  width : 250,     // 信息窗口宽度
					  height: 145,     // 信息窗口高度
					  title : infotip.title ,  // 信息窗口标题
					  offset:new BMap.Size(0,-20),
					  enableMessage:true,//设置允许信息窗发送短息
					  message:"出来没"
					}
					var infoWindow = new BMap.InfoWindow(infotip.content,opts);
					map.openInfoWindow(infoWindow,marker.getPosition());
					
					//鼠标悬停提示
					$(".zoomSize").kendoTooltip({
		                content: '<img class="demo-trigger" src="'+$(".zoomSize").attr('src')+'" style="width:480px"/>',
		                width: 300,
		                position: "left"
		            });
					
			    })
			};
		}
	};
	
	//查看项目验收记录window
	showChecksWindow = {
		obj : undefined,
		id : $("#showChecksWindow"),
		validator : undefined,
		checksArr : [],
		map : undefined,//地图对象
		sId : undefined,//子项目Id
//		pointArr : undefined,//坐标数组
//		polyline : undefined, 
//		polygon : undefined,
//		totalDistances : 0,
//		totalArea : 0,
//		startPointMarker : undefined, //周长和面积信息提示marker
		init : function() {
			if (!showChecksWindow.id.data("kendoWindow")) {
				showChecksWindow.id.kendoWindow({
					width : "1000px",
					height:"700px",
					actions : ["Maximize", "Close"],
					modal : true,
					title : "查看项目监管记录"
				});
			}
			showChecksWindow.obj = showChecksWindow.id.data("kendoWindow");
		},
		initMap : function(sId) {
			this.sId = sId;
			//初始化地图
			var map = new BMap.Map("showChecksMap");
			showChecksWindow.map = map;
			var point = new BMap.Point(106.62677,26.668131);
			map.centerAndZoom(point, 15);
			map.enableScrollWheelZoom(true); 
			
			//显示当前位置
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r){
				if(this.getStatus() == BMAP_STATUS_SUCCESS){
					map.panTo(r.point);
				}
				else {
					alert('failed'+this.getStatus());
				}        
			},{enableHighAccuracy: true});
//			// 添加带有定位的导航控件
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
			var geolocationControl = new BMap.GeolocationControl();
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
			
			this.initOverlays(map);
		},
		initOverlays: function(map){
			$.ajax({
				cache:false,
				type : "get",
				url : "checks/get/"+ this.sId,
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				success : function(data, textStatus, jqXHR) {
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
									startPointMarker : undefined //周长和面积信息提示marker
								};
//								var pointArr = [], polyline, polygon, totalDistances = 0, totalArea = 0;;
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
										map.addOverlay(marker2);              // 将标注添加到地图中
										if(i1 == 0 ){
											var label = new BMap.Label("起点",{offset:new BMap.Size(20,-10)});
											label.setStyle(labelStyle);
											marker2.setLabel(label);
										}
										if(i1 == points.length-1 ){
											var label = new BMap.Label("终点",{offset:new BMap.Size(20,-10)});
											label.setStyle(labelStyle);
											marker2.setLabel(label);
										}
									}
								}
								everyCheck.polyline = new BMap.Polyline(everyCheck.pointArr, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});
								map.addOverlay(everyCheck.polyline);
								
								everyCheck.polygon = new BMap.Polygon(everyCheck.pointArr, {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});  //创建多边形
								
								for(var i =0 ;i< everyCheck.pointArr.length-1; i++){
									everyCheck.totalDistances += parseFloat((showChecksWindow.map.getDistance(everyCheck.pointArr[i], everyCheck.pointArr[i+1])).toFixed(2));
								}
								
								// 创建信息窗口对象
								var label = new BMap.Label("路线总长度：" + everyCheck.totalDistances.toFixed(2) + "米",{offset:new BMap.Size(0,-20)});
								label.setStyle(labelStyle);
								//计算面积
								everyCheck.totalArea = BMapLib.GeoUtils.getPolygonArea(everyCheck.pointArr);
								everyCheck.totalArea = (everyCheck.totalArea*3/2000).toFixed(2);
								
								var marker = new BMap.Marker(everyCheck.pointArr[0]);  // 创建标注
								showChecksWindow.map.addOverlay(marker);              // 将标注添加到地图中
								everyCheck.startPointMarker = marker;
								marker.setLabel(label);
								
//								var opts = {
//									width : 100,     // 信息窗口宽度
//									height: 60,      // 信息窗口高度
//									title : "路线长度" , // 信息窗口标题
//									enableMessage:false
//								}
//								var infoWindow = new BMap.InfoWindow("路线总长度：" + everyCheck.totalDistances.toFixed(2) + "米", opts);
//								everyCheck.startPointMarker.openInfoWindow(infoWindow, everyCheck.pointArr[0]); //开启信息窗口
//								everyCheck.startPointMarker.addEventListener("mouseover", function(){
//									everyCheck.startPointMarker.openInfoWindow(infoWindow, everyCheck.pointArr[0]); //开启信息窗口
//								});
								
								showChecksWindow.checksArr.push(everyCheck);
							}
						}
					}else {
						infoTip({
							content : "没有验收记录!",
							color : "#D58512"
						});
					}
					
					
				},
				error : function(data, textStatus, jqXHR) {
					infoTip({
						content : "查询验收记录失败！",
						color : "#FF0000"
					});
				}
			});
			
			var opts = {
					width : 250,     // 信息窗口宽度
					height: 80,     // 信息窗口高度
					title : "项目监管" , // 信息窗口标题
					enableMessage:true//设置允许信息窗发送短息
				   };
			
			// 编写自定义函数,创建标注
			function addMarker(point){
				var marker = new BMap.Marker(point);
				map.addOverlay(marker);
			};
			function addClickHandler(infotip,marker){
				marker.addEventListener("mouseover", function(e){
//					var searchInfoWindow = new BMapLib.SearchInfoWindow(map, infotip.content, {
//						title  : "项目监控",      //标题
//						width  : 290,             //宽度
//						height : 105,              //高度
//						panel  : "panel",         //检索结果面板
//						enableAutoPan : true,      //自动平移
//						enableCloseOnClick : true, //点击关闭
//						searchTypes :[],
//						message: "aaa"
//					});
//				    searchInfoWindow.open(marker);
					var opts = {
					  width : 250,     // 信息窗口宽度
					  height: 145,     // 信息窗口高度
					  title : infotip.title ,  // 信息窗口标题
					  offset:new BMap.Size(0,-20),
					  enableMessage:true,//设置允许信息窗发送短息
					  message:"出来没"
					}
					var infoWindow = new BMap.InfoWindow(infotip.content,opts);
					map.openInfoWindow(infoWindow,marker.getPosition());
			    })
			};
		}
	};
	
	$('#clearSupervisions').on('click', function() {
		showSupervisionsWindow.map.clearOverlays();
	});
	
	$('#initSupervisions').on('click', function() {
		showSupervisionsWindow.initOverlays(showSupervisionsWindow.map);
	});
	
	$('#perimeter').on('click', function() {
		if(!showChecksWindow.checksArr || showChecksWindow.checksArr.length <= 0){
			infoTip({
				content : "没有验收记录！",
				color : "#FF0000"
			});
			return;
		}
		
		for(var i =0;i<showChecksWindow.checksArr.length;i++){
			showChecksWindow.map.removeOverlay(showChecksWindow.checksArr[i].polygon);
			showChecksWindow.map.addOverlay(showChecksWindow.checksArr[i].polyline);
			var tipinfo = "路线总长度：" + showChecksWindow.checksArr[i].totalDistances.toFixed(2) + "米";
			if(!showChecksWindow.checksArr[i].startPointMarker.getLabel()){
				var label = new BMap.Label(tipinfo, {offset:new BMap.Size(0,-20)});
				label.setStyle(labelStyle);
				showChecksWindow.checksArr[i].startPointMarker.setLabel(label);
			}else{
				var label = showChecksWindow.checksArr[i].startPointMarker.getLabel();
				label.setStyle(labelStyle);
				label.setContent(tipinfo);
			}
			
//			var infoWindow = showChecksWindow.map.getInfoWindow();
//			infoWindow.setTitle('路线长度');
//			infoWindow.setContent("路线总长度：" + showChecksWindow.checksArr[i].totalDistances.toFixed(2) + "米");
//			showChecksWindow.checksArr[i].startPointMarker.openInfoWindow(infoWindow, showChecksWindow.checksArr[i].pointArr);
		}
		
	});
	
	$('#area').on('click', function() {
		if(!showChecksWindow.checksArr || showChecksWindow.checksArr.length <= 0){
			infoTip({
				content : "没有验收记录！",
				color : "#FF0000"
			});
			return;
		}
		for(var i =0;i<showChecksWindow.checksArr.length;i++){
			showChecksWindow.map.removeOverlay(showChecksWindow.checksArr[i].polyline);
			showChecksWindow.map.addOverlay(showChecksWindow.checksArr[i].polygon);
			var tipinfo = "路线围绕面积：" + showChecksWindow.checksArr[i].totalArea + "亩";
			if(!showChecksWindow.checksArr[i].startPointMarker.getLabel()){
				var label = new BMap.Label(tipinfo, {offset:new BMap.Size(0,-20)});
				label.setStyle(labelStyle);
				showChecksWindow.checksArr[i].startPointMarker.setLabel(label);
			}else{
				var label = showChecksWindow.checksArr[i].startPointMarker.getLabel();
				label.setStyle(labelStyle);
				label.setContent(tipinfo);
			}
			
//			var infoWindow = showChecksWindow.map.getInfoWindow();
//			infoWindow.setTitle('路线围绕面积');
//			infoWindow.setContent("路线围绕面积：" + showChecksWindow.checksArr[i].totalArea.toFixed(2) + "亩");
//			showChecksWindow.checksArr[i].startPointMarker.openInfoWindow(infoWindow, showChecksWindow.checksArr[i].pointArr);
		}
//		showChecksWindow.map.removeOverlay(showChecksWindow.polyline);
//		showChecksWindow.map.addOverlay(showChecksWindow.polygon);
//		if(!showChecksWindow.pointArr || showChecksWindow.pointArr.length <= 0){
//			infoTip({
//				content : "没有验收记录！",
//				color : "#FF0000"
//			});
//			return;
//		}
//		var infoWindow = showChecksWindow.map.getInfoWindow();
//		infoWindow.setTitle('路线总面积');
//		infoWindow.setContent("总面积：" + showChecksWindow.totalArea + "亩");
//		showChecksWindow.map.openInfoWindow(infoWindow, showChecksWindow.pointArr[0]);
	});

	subProjectManageGridObj.init();
	subProjectManageWindow.init();
	subProjectManageDetailWindow.init();
	showSupervisionsWindow.init();
	showChecksWindow.init();
});

