<!DOCTYPE html>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<html>
<head lang="zh-cn">
	<meta charset="UTF-8">
    <style>
   	 	html { font-size: 14px; font-family: Arial, Helvetica, sans-serif; }
    </style>
</head>

	
<body>
	
        <div id="example">
            <div class="demo-section k-content">

                <div id="treeview-left">
                </div>
                <div id="right">右</div>
                
            </div>
 			</div>
	            <div id="clear"></div>
	            <div id="footer"></div>
        </div>
            <style>
            	#example{
					height: 800px;
					background-color: whitesmoke;
				
				}
              
                #treeview-left{
                 	overflow: visible;
					width: 331px;
					height: 700px;
					background: whitesmoke;
					border: 1px solid threedshadow;
					margin-left: 10px;
					margin-top:5px;
					float: left;
				}
				#right{
					overflow: visible;
					margin-top:5px;
					width: 1000px;
					height: 700px;
					background: whitesmoke;
					
					float: right;
						
				}
				#clear{
					clear: both;
					width: 0px;
					height: 0px;
				}
				#footer{
					height: 10px;
					background: whitesmoke;
				
				}
            </style>
        </div>

	<myScript>
		<script type="text/javascript" src="resources/js/SystemManage/maintenanceManage.js" ></script>
	</myScript>
</body>

</html>