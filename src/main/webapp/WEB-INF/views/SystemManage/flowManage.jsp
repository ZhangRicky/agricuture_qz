<!DOCTYPE html>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<html>
<head lang="zh-cn">
	<meta charset="UTF-8">
</head>
<body>
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a>系统管理
		<i class="glyphicon glyphicon-menu-right"></i>流程管理
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group form-inline">
					<input id="inputSearch" type="text" class="form-control" placeholder="输入流程名称筛选" style="width: 400px;"/>
					<button id="searchBtn" class="btn btn-danger"><i class="glyphicon glyphicon-search"></i> 查询</button>
				</div>
			</div>
			<div class="col-md-12">
				<div id='flowList'></div>
				<script type="text/x-kendo-template" id="template">
	                <div class="toolbar" >
	                	<shiro:hasPermission name='addFlowInfo'><a id='addFlow' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 添加流程</a></shiro:hasPermission>
	                </div>
	            </script>
	            
	            <script type="text/x-kendo-template" id="modifyFlowNotes">
	                <shiro:hasPermission name="modifyFlowNotes"><a class='editFlowNodes aLink' title='流程节点'>编辑流程节点</a></shiro:hasPermission>
	            </script>
	            <script type="text/x-kendo-template" id="flow_update_delete_detail">
	                <shiro:hasPermission name="updateFlowInfo"><a class='editFlow editIcon aLink' title='编辑流程'><i class='fa fa-pencil'></i></a>&nbsp;&nbsp;</shiro:hasPermission>
					<shiro:hasPermission name="deleteFlowInfo"><a class='deleteFlow deleteIcon aLink' title='删除流程'><i class='glyphicon glyphicon-trash'></i></a>&nbsp;&nbsp;</shiro:hasPermission>
					<shiro:hasPermission name="detailFlowInfo"><a class='viewFlow viewIcon aLink' title='查看流程'><i class='fa fa-eye'></i></a> </shiro:hasPermission>
	            </script>
			</div>
		</div>
	</div>
	
	<div id='flowInfoWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='flowInfoValidate' class="forge-heading">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="flowName">流程名称</label>
						<div class="col-md-5">
							<input type="text" hidden="true" id="flowId" value="">
							<input class="form-control input-sm" placeholder="居民基础信息审核" type="text" id="flowName" name="flowName" required validationMessage="必填" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="flowName"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="flowType">流程类型</label>
						<div class="col-md-5">
							<select class="form-control" id='flowType' required="required" data-required-msg="必填">
								<option value="1">居民基础信息审核</option>
								<option value="2">居民征信信息审核</option>
							</select>
							<!-- <input class="form-control input-sm"  placeholder="e.g. PERSONAL_INFO_APPROVE"  type="text" id="flowType" name="flowType" required pattern="[a-zA-Z_\d]+" data-required-msg="必填" validationMessage="格式不正确,只能输入英文字母、数字、下划线" value=""> -->
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="flowType"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="isInUse">是否有效</label>
						<div class="col-md-5">
							<label><input type="radio" name='isInUse' checked value="true"/> 是</label>&nbsp;&nbsp;
							<label><input type="radio" name='isInUse' value="false"/> 否</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="description">描述</label>
						<div class="col-md-5">
							<textarea  class="form-control input-sm" id='description' name='description' ></textarea>
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="description"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveFlow'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='cancelFlow'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
	</div>
	<!--编辑 流程节点  窗口-->
	<div id='editFlowNodesWindow' style="display: none;">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div id='flowNodesList'></div>
				</div>
			</div>
			<script type="text/x-kendo-template" id="addFlowNodeTemplate">
	            <div class="toolbar" >
	                <a id='addFlowNode' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 添加流程节点</a>
	            </div>
	        </script>
		</div>
		<!-- <div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveFlowNodes'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存修改
			</a>
			<a id='cancelFlowNodes'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-delete"></span>取消修改
			</a>
			<a id='closeFlowNodes'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>关闭
			</a>
		</div> -->
	</div>
	<!--查看流程明细窗口-->
	<div id='showFlowDetailWindow' style="display: none;">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12">
					<div id='canvas'></div>
				</div>
			</div>
		</div>
	</div>
	<myScript>
		<script type="text/javascript" src="resources/plugins/strathausen-dracula-a6a5fa7/js/raphael-min.js"></script>
    	<script type="text/javascript" src="resources/plugins/strathausen-dracula-a6a5fa7/js/dracula_graffle.js"></script>
   	 	<script type="text/javascript" src="resources/plugins/strathausen-dracula-a6a5fa7/js/dracula_graph.js"></script>
    	<script type="text/javascript" src="resources/js/SystemManage/flowManage.js" ></script>
	</myScript>
	
</body>
</html>