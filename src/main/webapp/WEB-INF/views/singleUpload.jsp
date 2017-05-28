<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html>
<body>
<h1>Single File Upload</h1>
	<form method="post" enctype="multipart/form-data" action="singleSave">
		Upload File: <input type="file" name="file">
		<br /><br />
		Description: <input type="text" name="desc"/>
		age: <input type="text" name="age"/>
		<br/><br/><input type="submit" value="Upload"> 
	</form>
</body>
</html> 