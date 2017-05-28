<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
<html>
<body>
    <h1> Multiple File Upload </h1>
	<form method="post" enctype="multipart/form-data" action="services/addSupervision">
		Upload File 1: <input type="file" name="picture"> <br/>
		Upload File 2: <input type="file" name="picture"> <br/>
		Upload File 3: <input type="file" name="picture"> <br/>
		Upload File 4: <input type="file" name="picture"> <br/>
		
		
		existingProblems: <input type="text" name="existingProblems"/>
		correctSuggestions: <input type="text" name="correctSuggestions"/>
		checksId: <input type="text" name="checksId"/>
		<br /><br /><input type="submit" value="Upload"> 
	</form>
</body>
</html> 