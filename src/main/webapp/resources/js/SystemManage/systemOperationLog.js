$(function() {
	//时间范围
	kendo.culture("zh-CN");
	$("#searchStartTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:mm:ss"
	});
	$("#searchEndTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:mm:ss"
	});
	
	//列表
	var logObj = {
		logGrid: undefined,
		init: function(){
			$("#AppModule").kendoDropDownList();
			$("#OpType").kendoDropDownList();
			this.logGrid = $("#logList").kendoGrid({
					dataSource : {
						transport : {
							read : {
								cache:false,
								type : "get",
								url : "systemOperationLog/search",
								dataType : "json",
								contentType : "application/json;charset=UTF-8"
							},
							parameterMap : function(data, type) {
								return {
									AppModule : data.filter.filters[0].value,
									OpType : data.filter.filters[1].value,
									SubUser : data.filter.filters[2].value,
									Sip : data.filter.filters[3].value,
									searchStartTime : data.filter.filters[4].value,
									searchEndTime : data.filter.filters[5].value,
									start : data.skip,
									limit : data.pageSize
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
							field : 'AppModule',
							value : $('#AppModule').val()
						},{
							field : 'OpType',
							value : $('#OpType').val()
						},{
							field : 'SubUser',
							value : $('#SubUser').val()
						},{
							field : 'Sip',
							value : $('#Sip').val()
						},{
							field : 'searchStartTime',
							value : $('#searchStartTime').val()
						},{
							field : 'searchEndTime',
							value : $('#searchEndTime').val()
						}]
					},
					reorderable : true,
					resizable : true,
					sortable : true,
					columnMenu : true,
					pageable : {
						refresh: true
					},
					editable : "popup",
					columns: [{
						field: "LogTime",
						width:170,
						template: "<span class='tdOverFont' title='#:LogTime#'>#:LogTime#</span>",
						encoded: false,
						title: "<span class='tdOverFont' title='操作时间'>操作时间</span>"
					}, {
						field: "appModule",
						width:130,
						template: "<span class='tdOverFont' title='#:appModule#'>#:appModule#</span>",
						encoded: false,
						title: "<span class='tdOverFont' title='模块名称'>模块名称</span>"
					}, {
						field: "subUser",
						width:120,
						template: "<span class='tdOverFont' title='#:subUser#'>#:subUser#</span>",
						encoded: false,
						title: "<span class='tdOverFont' title='操作者'>操作者</span>"
					}, {
						field: "sip",
						width:120,
						template: "<span class='tdOverFont' title='#:sip#'>#:sip#</span>",
						encoded: false,
						title: "<span class='tdOverFont' title='操作IP'>操作IP</span>"
					}, {
						field: "opType",
						width:100,
						template: "<span class='tdOverFont' title='#:opType#'>#:opType#</span>",
						encoded: false,
						title: "<span class='tdOverFont' title='操作类型'>操作类型</span>"
					}, {
						field: "detailed",
						width:470,
						template:  function(dataItem) {
							if(dataItem.detailed == null){
								return "无";
							} else{
								return "<span class='tdOverFont'>"+dataItem.detailed+"</span>";
							}
						},
						encoded: false,
						title: "<span class='tdOverFont' title='详细信息'>详细信息</span>"
					}, {
						field: "opText",
						template: function(data){
							if(data.opType=="Del"&&data.appModule=="基础信息管理"&&(data.isRolBack==null||data.isRolBack=="")){
								return "<span class='tdOverFont' title='"+data.opText+"'>"+data.opText+"</span>" +
										"<span class='rolBackFont' style='margin-left:20px;' title='还原删除'><a href='javascript:' onfocus='this.blur()'>还原删除</a></span>";
							}else if(data.opType=="Del"&&data.appModule=="基础信息管理"&&data.isRolBack==1){
								return "<span class='tdOverFont' title='"+data.opText+"'>"+data.opText+"</span>" +
								"<span class='rolBackFont' style='color:red;'>(已还原)</span>";
							}else{
								return "<span class='tdOverFont' title='"+data.opText+"'>"+data.opText+"</span>";
							}
						},
						encoded: false,
						title: "<span class='tdOverFont' title='操作描述'>操作描述</span>"
					}],
					dataBound : function() {
						logObj.rolBack();
					}
				}).data("kendoGrid");
		},
		rolBack:function(){
			$(".rolBackFont").on("click",function(e){
				var tr =  $(e.target).closest("tr");
				var pdata = logObj.logGrid.dataItem(tr);
				if (confirm("确定还原基础信息么？")) {
					$.ajax({
						type : "delete",
						url : "personalInfo/rolBackPersonal/"+pdata.operId+"/"+pdata.id+"/personal",
						dataType : "json",
						contentType : "application/json;charset=UTF-8",
						success : function(data, textStatus, jqXHR) {
							infoTip({
								content : "还原成功!",
								color : "#D58512"
							});
							logObj.logGrid.dataSource.read();
						},
						error : function(data, textStatus, jqXHR) {
							infoTip({
								content : "还原失败!",
								color : "#FF0000"
							});
						}

					});	
				}
			});
		}
	};
	
	logObj.init();
	var searchSystemOperationLog = function(){
		logObj.logGrid.dataSource.filter([{
			field : 'AppModule',
			value : $('#AppModule').val()
		},{
			field : 'OpType',
			value : $('#OpType').val()
		},{
			field : 'SubUser',
			value : $('#SubUser').val()
		},{
			field : 'Sip',
			value : $('#Sip').val()
		},{
			field : 'searchStartTime',
			value : $('#searchStartTime').val()
		},{
			field : 'searchEndTime',
			value : $('#searchEndTime').val()
		}]);
	};
	$('#searchBtn').on('click', searchSystemOperationLog);
	$('#resetBtn').on('click', function() {
		$("#AppModule").data("kendoDropDownList").select(0);
		$("#OpType").data("kendoDropDownList").select(0);
		$("#SubUser").val('');
		$("#Sip").val('');
		$("#searchStartTime").val('');
		$("#searchEndTime").val('');
		searchSystemOperationLog();
	});

	$('#AppModule').on('change', searchSystemOperationLog);
	$('#OpType').on('change', searchSystemOperationLog);
	$('#SubUser').on('keyup', searchSystemOperationLog);
	$('#Sip').on('keyup', searchSystemOperationLog);
	
	
});