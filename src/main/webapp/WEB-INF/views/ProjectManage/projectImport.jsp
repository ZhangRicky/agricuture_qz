	﻿<!DOCTYPE html>
	<%@ page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
	﻿<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>   
	<html>
	<head lang="zh-cn">
	<myCss>
		<link rel="stylesheet" href="resources/plugins/jQuery-File-Upload-9.11.2/css/jquery.fileupload.css">
		<link rel="stylesheet" href="resources/plugins/jQuery-File-Upload-9.11.2/css/jquery.fileupload-ui.css">
	</myCss>
	<myStyle>
		#info{width:250px;
	    		height:55px;
	    		margin:0 auto;
	    		background-color:#FB6;
	    		position:fixed;
				bottom:20px;
				right:15px;
				border-radius: 3px;
	    		}
	    #info .text{font-size:15px;font-family:"微软雅黑";color:#fff;
	    		height:20px;
	    		padding:18px;
	    		margin:0 auto;
	    		text-align:center;
	    		}
	</myStyle>
	</head>
	<body>
		<div id='subNav'>
			<a href="home"><i class="glyphicon glyphicon-home"></i></a> 项目管理
			<i class="glyphicon glyphicon-menu-right"></i>项目Excel录入
		</div>
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12" >
					<div class="form-group form-inline">
						<label>文件名称&nbsp;&nbsp;</label>
						<input id="inputSearch" type="text" class="form-control" placeholder="文件名称" title="请输入文件名称筛选" style="width: 200px;"/>&nbsp;&nbsp;
						
						<label>上传时间&nbsp;&nbsp;</label>
						<input id='searchStartTime' type="text" class="form-control" /> - 
						<input id='searchEndTime' type="text" class="form-control" />&nbsp;&nbsp;
						
						<label>导入状态&nbsp;&nbsp;</label>
						<select class="form-control" id='importStatus' style="width:80px">
							<option value="0">--全部--</option>
							<option value="1">成功</option>
							<option value="2">失败</option>
						</select>&nbsp;&nbsp;
						
						<button id="searchBtn" class="btn btn-danger"><i class="glyphicon glyphicon-search"></i>查询</button>&nbsp;&nbsp;
						<button id="resetBtn" class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i>重置</button>
					</div>
				</div>
				<div class="col-md-12">
					<div id='projectImportList'></div>
					<script type="text/x-kendo-template" id="template">
	                <div class="toolbar" >
						<shiro:hasPermission name='downloadProjectImportTemplate'><a id='templateDownloadBtn' class='btn btn-xs btn-primary'><i class='glyphicon glyphicon-download'></i>模版下载</a>&nbsp;&nbsp;&nbsp;&nbsp;</shiro:hasPermission>
	                	<!--<a id='projectImportBtn' class='btn btn-xs btn-success'><i class='glyphicon glyphicon-import'></i>项目导入</a>&nbsp;&nbsp;&nbsp;&nbsp;-->
	                	<shiro:hasPermission name='uploadProjectImportData'>
							<span class="btn btn-xs btn-success fileinput-button">
				        	<i class="glyphicon glyphicon-plus"></i>
				        	<span>项目导入</span>
							<input id="fileupload" type="file" name="file" />
							</span>
							<span id="importingMsg"></span>
						</shiro:hasPermission>
	                </div>
	            	</script>
	            	<script type="text/x-kendo-template" id="detailScan">
						<shiro:hasPermission name='projectImportDetail'><a class='projectImportDetail editIcon aLink' title='查看明细'><i class='fa fa-eye'></i><a/></shiro:hasPermission>
					</script>
				</div>
				<div class="col-md-12">
					<div class="alert alert-warning alert-dismissible" role="alert">
					  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					  <span style="background-color:#FFC0CB"><strong>红色</strong></span>代表导入失败，<span style="background-color:#DA70D6"><strong>淡紫色</strong></span>代表没有导入权限，<strong>白色</strong>代表导入完成。
					</div>
				</div>
			</div>
		</div>
		<!-- template download -->
		<div id="templateDownloadWindow" style="display:none">
			<div class="container-fluid">
				<div id="templateDownloadValidate" class="forge-heading">
					<div class="form-horizontal">
						<div class="form-group">
							<label class="col-md-3 control-label" for="templates">选择模板</label>
							<div class="col-md-5">
								<select class="form-control" id="templates" required="required" data-required-msg="必填"></select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label" for="templates">保存文件名称</label>
							<div class="col-md-5">
								<input class="form-control input-sm" id="saveAsFileName" type="text" placeholder="保存文件名称"/>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="k-edit-buttons k-state-default text-right windowFooter">
				<a id="downloadUrl" href="" ><span id="downloadUrlSpan"></span></a>
				<a id='downloadBtn' class="k-button k-button-icontext k-primary k-grid-update">
					<span class="k-icon k-update"></span>开始下载
				</a>
				<a id='cancelDownloadBtn'  class="k-button k-button-icontext k-grid-cancel">
					<span class="k-icon k-cancel"></span>取消
				</a>
			</div>
		</div>
		
		<script id="projectImportDetailTemplate" type="text/x-kendo-template">
			<div>
                #=target.data('content')#
			</div>
        </script>
		<!-- credit import -->
		<div id="projectImportWindow" style="display:none">
			<!-- <form action="projectImport/uploadFile" method="POST" enctype="multipart/form-data"> -->
				<div class="container-fluid">
					<div id="projectImportValidate" class="forge-heading">
						<div class="form-horizontal">
							<div class="form-group">
								<div class="col-md-13">
									<span class="btn btn-xs btn-success fileinput-button">
					                    <i class="glyphicon glyphicon-plus"></i>
					                    <span>项目导入</span>
					                   
					                    <input id="fileupload1" type="file" name="file" />
					                </span>
								</div>
							</div>
							<div class="form-group">
								<div id="progress" class="col-md-13">
									<div class="bar" style="width: 0%;"></div>
								</div>
							</div>
							
							<div class="form-group">
								<div class="col-md-13">
									注意：只能上传Excel文件，文件上传成功后，只有校验全部通过，数据才会导入到数据库。
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="k-edit-buttons k-state-default text-right windowFooter">
					<button id='importBtn'  class="k-button k-button-icontext k-primary k-grid-update">
						<span class="k-icon k-update"></span>开始导入
					</button>
					<a id='cancelImportBtn'  class="k-button k-button-icontext k-grid-cancel">
						<span class="k-icon k-cancel"></span>取消
					</a>
				</div>
			<!-- </form> -->
		</div>
		
		<!-- credit import detail-->
		<div id="projectImportDetailWindow" style="display:none">
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-12">
						<div id='projectImportDetailList'></div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<div class="alert alert-warning alert-dismissible" role="alert">
						
						<input type="hidden" id="projectId" name="projectId" value =""/>
						  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						  <span style="background-color:#FFC0CB"><strong>粉红色</strong></span>代表项目已被覆盖，<span style="background-color:#E3CF57"><strong>黄色</strong></span>代表项目重复记录相同，<strong>白色</strong>代表新增， <span style="background-color:#BBFFFF"><strong>天蓝色</strong></span>代表项目重复且记录不同。
						</div>
					</div>
				</div>
			</div>
			<div class="k-edit-buttons k-state-default text-right windowFooter">
				<a id='closeProjectImportDetailWindowBtn'  class="k-button k-button-icontext k-grid-cancel">
					<span class="k-icon k-cancel"></span>返回
				</a>
			</div>
		</div>
		
		<!-- The global progress state -->
		<div class="col-lg-5 fileupload-progress fade">
			<!-- The global progress bar -->
			<div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
				<div class="progress-bar progress-bar-success" style="width:0%;"></div>
			</div>
			<!-- The extended global progress state -->
			<div class="progress-extended">&nbsp;</div>
		</div>
		
		<!-- 提示信息 -->
	    <div id="info" style="display:none">
	    
	    	<div class="text"></div>
	    </div>
		
		<myScript>
			<script type="text/javascript" src="resources/js/ProjectManage/projectImport.js" ></script>
			<script type="text/javascript" src="resources/js/filedownload.js" ></script>
			<!-- 上传文件插件 JQuery-File-Upload -->
			<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/vendor/jquery.ui.widget.js"></script>
			<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.iframe-transport.js"></script>
			<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload.js"></script>
			<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload-process.js"></script>
			<script src="resources/plugins/jQuery-File-Upload-9.11.2/js/jquery.fileupload-validate.js"></script>
		</myScript>
		
	</body>
	</html>