<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html>
<html>
<head lang="zh-cn">
	<meta charset="UTF-8">
	<meta http-equiv="pragma" content="no-cache">  
	<meta http-equiv="cache-control" content="no-cache">  
	<meta http-equiv="expires" content="0">
</head>
<body>
	<div id='subNav'>
		<a href="home"><i class="glyphicon glyphicon-home"></i></a> 项目管理
		<i class="glyphicon glyphicon-menu-right"></i>财务管理
	</div>
	
	<div class="container-fluid">
		 
		<div class="row">
			<div class="col-md-12" >
				<div class="form-group form-inline">
					<input id="inputSearch" type="text" class="form-control" placeholder="输入项目名称筛选" style="width: 400px;"  />&nbsp;&nbsp;
					<button id="searchBtn" class="btn btn-danger"><i class="glyphicon glyphicon-search"></i> 查询</button>&nbsp;&nbsp;
					<button id="resetBtn" class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
			</div>
			<div class="col-md-12" >
				<div id="projectList"></div>
			</div>
		</div>	
	</div>
	
	
	<!-- 添加拨款信息窗口:start -->
	<div id='fianceInfoWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='fianceInfoValidate' class="forge-heading">
				<div class="form-horizontal">
					<input type="hidden" id="fid" name="fid" value =""/>
					<input type="hidden" id="subprojectId" name="subprojectId" value="">
					<div class="form-group">
						<label class="col-md-2 control-label" for="certificateNum">凭证号:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="certificateNum" name="certificateNum"  required validationMessage="必填" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="appropriation">拨款金额:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" placeholder="0或者0.00" type="text" id="appropriation" name="appropriation" pattern="^([0-9]+)\d*|\d+(\d|(\.[1-9]{1,2}))$"  required validationMessage="格式不正确" value="">
							<font style="color:#CCCCFF;font-size:10px;">单位:万元</font>
						</div>
					</div>
					<!-- 
					<div class="form-group">
						<label class="col-md-2 control-label" for="account">报账金额:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="account" name="account" pattern="^([0-9]+)$" readonly="readonly"  required validationMessage="必填" value="">
							<font style="color:#CCCCFF;font-size:10px;">单位:万元</font>
							<span class="k-invalid-msg" data-for="remark"></span>
						</div>
					</div>
					 -->
					<div class="form-group">
						<label class="col-md-2 control-label" for="operateUser">拨款人:</label>
						<div class="col-md-5">
							<input maxlength="18" class="form-control input-sm" type="text" id="operateUser" name="operateUser"  required validationMessage="必填" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="date">拨款日期:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="datetime" id="date" name="date" required validationMessage="必填" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="date"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="remark">备注:</label>
						<div class="col-md-5">
							<textarea  class="form-control input-sm" id='remark' name='remark' ></textarea>
							<font style="color:#CCCCFF;font-size:10px;">备注在200个字以内</font>
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="remark"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveFiance'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='cancelFiance'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
	</div>
	<!-- 添加拨款信息窗口:end -->
	
	<!-- 添加报账信息窗口:start -->
	<div id='baoZhangfoWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='baozhangInfoValidate' class="forge-heading">
				<div class="form-horizontal">
					<input type="hidden" id="bzfid" name="bzfid" value =""/>
					<input type="hidden" id="bzsubprojectId" name="bzsubprojectId" value="">
					
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzCertificateNum">凭证号:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="bzCertificateNum" name="certificateNum"  required validationMessage="必填" value="">
						</div>
					</div>
					<!-- 
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzAppropriation">拨款金额:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="bzAppropriation" name="bzAppropriation" pattern="^([0-9]+)$/^([0-9]+)(\.[0-9]{2})$" readonly="readonly" required validationMessage="必填" value="">
							<font style="color:#CCCCFF;font-size:10px;">单位:万元</font>
						</div>
					</div>
					
					 <div class="form-group">
						<label class="col-md-2 control-label" for="bkOperateUser">拨款人:</label>
						<div class="col-md-5">
							<input maxlength="18" class="form-control input-sm" type="text" id="bkOperateUser" name="bkOperateUser" readonly="readonly"  required validationMessage="必填" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="bkDate">拨款日期:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="datetime" id="bkDate" name="bkDate" readonly="readonly" required validationMessage="必填" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="date"></span>
						</div>
					</div>
					 -->
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzAccount">报账金额:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="text" id="bzAccount" name="bzAccount" pattern="^([0-9]+)\d*|\d+(\d|(\.[1-9]{1,2}))$"   required validationMessage="格式不正确" value="">
							<font style="color:#CCCCFF;font-size:10px;">单位:万元</font>
							<span class="k-invalid-msg" data-for="remark"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzuser">报账人:</label>
						<div class="col-md-5">
							<input maxlength="18" class="form-control input-sm" type="text" id="bzuser" name="bzuser" required validationMessage="必填" value="">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzdate">报账日期:</label>
						<div class="col-md-5">
							<input class="form-control input-sm" type="datetime" id="bzdate" name="bzdate"  required validationMessage="必填" value="">
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="date"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="bzremark">备注:</label>
						<div class="col-md-5">
							<textarea  class="form-control input-sm" id='bzremark' name='bzremark' ></textarea>
							<font style="color:#CCCCFF;font-size:10px;">备注在200个字以内</font>
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="remark"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='baoZhangSaveFiance'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='baoZhangCancelFiance'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
	</div>
	<!-- 添加报账信息窗口:end -->
	
	<!-- 信息详情窗口：start -->
	<div id='fianceInfoShowWindow' style="display: none;">
		<div class="container-fluid" >
			<div id='fianceInfoShowValidate' class="forge-heading">
				<div class="form-horizontal">
					<input type="hidden" id="fid" name="fidshow" value =""/>
					<input type="hidden" id="subprojectIdShow" name="subprojectId" value=""/>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="certificateNum">凭证号:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="certificateNumShow"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="appropriation">拨款金额:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="appropriationShow"></label>
						</div>
					</div>
					<!--
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="accountJeYu">资金结余:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="accountJeYushow"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="baoZhangLv">报账率:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="baoZhangLvshow"></label>
						</div>
					</div>
					  -->
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="operateUser">拨款人:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="operateUserShow"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="date">拨款日期:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="dateshow"></label>
						</div>
					</div>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="account">报账金额:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="accountshow"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="bzUser">报账人:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="bzUser"></label>
						</div>
					</div>
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="date">报账日期:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="bzDate"></label>
						</div>
					</div>
					
					<div class="form-group" style="margin-top:0px;margin-bottom:0px;">
						<label class="col-md-2 control-label" for="remark">备注:</label>
						<div class="col-md-5" style="line-height: 30px;">
							<label id="remarkshow"></label>
						</div>
					</div>
					
					<div class="k-edit-buttons k-state-default text-right windowFooter">
						<a id='fanhui'  class="k-button k-button-icontext k-grid-cancel">
							<span class="k-icon k-cancel"></span>返回
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 信息详情列表:end -->
		
		
	<!-- 财务信息列表：start -->
	<div id="financeDetailsWindow" style="display:none">
			<div>
				<div>
					<button id='addFinance' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 新增拨款信息</button>&nbsp;&nbsp;&nbsp;&nbsp;
					<button id='addBaoZhangInfo' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 新增报账信息</button>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<p></p>
				<div id="financeList"></div>
			</div>
		</div>
	<!-- 财务信息列表：end -->
		
	<myScript>
		<script type="text/javascript" src="resources/js/ProjectManage/financeManagement.js" ></script>
	</myScript>
</body>
</html>