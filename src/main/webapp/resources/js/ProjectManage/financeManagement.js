$(function(){
	var projectGridObj = {},fianceInfoObjWindow={},lock = false,showDetailsList={},userdetailsWindow={};
	var fianceInfoShowObjWindow = {},subId = "";
	var baoZhangFinanceObjWindow = {};
	var financeID = "";		//记录财务记录ID
	var financeSum =0;		//记录添加的报账金额的值
	//var removeFinance = 0;	//记录删除的报账金额的值
	//项目列表
	projectGridObj={
		projectGrid : undefined,
		init : function(){
			this.projectGrid = $("#projectList").kendoGrid({
				dataSource :{
					transport : {
						read : {
							type : "get",
							cache:false,
							url : "financeManagement/subProjectList",
							dataType : "json",
							contentType : "application/json;charset=UTF-8"
						},
						parameterMap : function(data, type) {
							return {
								projectName : data.filter.filters[0].value,
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
						field : 'subprojectName',
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
				columns : [{
						field : "id",
						hidden : true
					},{
						field : "subProjectNumber",
						title : "项目文号",
						width : 100
					},{
						field : "subProjectName",
						title : "项目名称",
						width : 150
					},{
						field : "totalCapital",
						title : "财政资金(万元)",
						width : 150
					},{
						field : "projectScaleAndContent",
						title : "建设内容及规模",
						width : 180
					},{
						field : "shouldAccount",
						title : "应报账金额(万元)",
						width : 130
					},{
						//field : "financebiLv",
						title : "报账比率",
						width : 100,
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
					},{
						field : "implementationUnit",
						title : "实施单位",
						width : 120
					},{
						field : "checkStatus",
						title : "验收状态",
						width : 100,
						template : function(data){
							if(data.checkStatus == 0){
								return "未验收";
							}else if(data.checkStatus == 1){
								return "验收通过";
							}else if(data.checkStatus == 2){
								return "验收不通过";
							}
						}
					},{
						title : "操作",
						template : function(){		
							return "<a class='showFiance editIcon aLink button button-action button-rounded  button-small'  style='margin-left:2px;'><i class='glyphicon glyphicon-th-list'>&nbsp;查看财务记录</i></a>" ;
						}
					}],
				dataBound : function(){			
					projectGridObj.showFinanceList();
				}
			}).data("kendoGrid");
		},
		
		showFinanceList : function(){
			$(".showFiance").on("click",function(e){
				var tr = $(e.target).closest("tr");
				var pdata = projectGridObj.projectGrid.dataItem(tr);
				subId = pdata.id;	//传递子项目id
				userdetailsWindow.obj.center().open();
				userdetailsWindow.showDetailsList(pdata);		
			});
		}
	}

	
		//财务表
		var subProject_id; var accountNum = ""; var appropriationNum = "";
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
						projectGridObj.projectGrid.dataSource.read();
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
					},
//					{
//						title : "资金结余",
//						width : 100,
//						template :function(data){
//							if((data.appropriation - data.account) >= 0){
//								return data.appropriation - data.account;
//							}else{
//								return "已超支"+(data.account-data.appropriation)+"万元";
//							}
//						}
//					},
					{
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
			
			/*财务信息详情*/
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
			
			/*删除财务信息*/
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
			
			/*编辑财务信息*/
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
		
	projectGridObj.init();
	
	/*【添加拨款信息】按钮*/
	$("#addFinance").on("click", function(e) {
		$("#fid,#certificateNum,#appropriation,#account,#operateUser,#date,#remark").val("");
		fianceInfoObjWindow.obj.title("添加拨款信息");
		fianceInfoObjWindow.obj.center().open();
		
	});
	
	/*【添加报账信息】按钮*/
	$("#addBaoZhangInfo").on("click",function(){		
		$("#bzfid,#bzCertificateNum,#bzAccount,#bzuser,#bzdate,#bzremark").val("");
		baoZhangFinanceObjWindow.obj.title("添加报账信息");
		baoZhangFinanceObjWindow.obj.center().open();
	});
	
	
	/*【拨款信息确认】按钮*/
	$("#saveFiance").on("click",function(e){
		e.preventDefault();
		if(lock == true){
			return;
		}
		lock = true;
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
	
	/*拨款信息【取消】按钮*/
	$("#cancelFiance").on("click",function(){
		fianceInfoObjWindow.obj.close();
	});
	
	
	
	
	/*【搜索】模块*/
	$('#searchBtn').on('click', function() {
		var projectName = $("#inputSearch").val();
		projectGridObj.projectGrid.dataSource.filter([{
			field : "projectName",
			value : projectName
		}]);
	});
	
	$("#inputSearch").on("keyup",function(){
		var projectName = $("#inputSearch").val();
		projectGridObj.projectGrid.dataSource.filter([{
			field : "projectName",
			value : projectName
		}]);
	});
	
	$("#resetBtn").on("click",function(){
		$("#inputSearch").val("");
		$('#searchBtn').trigger('click');
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
	
	/*财务信息详情【返回】按钮*/
	$("#fanhui").on("click",function(){
		fianceInfoShowObjWindow.obj.close();
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
					actions : ["Close"],
					modal : true,
					title : "财务信息",
					close : function(e){
						projectGridObj.projectGrid.dataSource.read();
					}
				});
			}
			fianceInfoObjWindow.obj = fianceInfoObjWindow.id.data("kendoWindow");
			// 用户信息验证
			fianceInfoObjWindow.validator = $("#fianceInfoValidate").kendoValidator().data("kendoValidator");
		}
	}
	fianceInfoObjWindow.init();
	
	//信息详情查看窗口
	fianceInfoShowObjWindow = {

		obj : undefined,

		id : $("#fianceInfoShowWindow"),
		
		validator : undefined,

		init : function() {
			if (!fianceInfoShowObjWindow.id.data("kendoWindow")) {
				fianceInfoShowObjWindow.id.kendoWindow({
					width : "700px",
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
	
	/*报账信息窗口*/	
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
//					close : function(){
//						$.ajax({
//							cache:false,
//							type : "get",
//							url : "financeManagement/findByFinances/"+subId,
//							dataType : "json",
//							contentType : "application/json;charset=UTF-8",
//							success : function(data) {
//								financeSum = data;		//将返回的数据传到全局变量
//								projectGridObj.projectGrid.dataSource.read();
//							}
//						});
//					}
				});
			}
			baoZhangFinanceObjWindow.obj = baoZhangFinanceObjWindow.id.data("kendoWindow");
			// 用户信息验证
			baoZhangFinanceObjWindow.validator = $("#baozhangInfoValidate").kendoValidator().data("kendoValidator");
		}
	}
	baoZhangFinanceObjWindow.init();
	
	/*【报账信息【取消】按钮*/
	$("#baoZhangCancelFiance").on("click",function(){
		baoZhangFinanceObjWindow.obj.close();
	});
	
	/*【报账信息【确认】按钮*/
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
							projectGridObj.projectGrid.dataSource.read();
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
	
});




