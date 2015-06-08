<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

	
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Todo List</title>
</head>
<body>
	<div class="container">
	
		<div class="row">
			
			<div class="col s12 m6 l6">
				<h3>Web</h3>
				<ul class="collection">
					<li class="collection-item">
						上传图片
					</li>
					<li class="collection-item">
						教程查看任意历史版本
					</li>
					<li class="collection-item">
					    教程模块下发布问题、发布教学内容
					</li>
					<li class="collection-item">
					</li>
				</ul>
			</div>
			
			<div class="col s12 m6 l6">
				<h3>Android</h3>
			</div>
			
		</div>
	
	</div>
</body>
</html>