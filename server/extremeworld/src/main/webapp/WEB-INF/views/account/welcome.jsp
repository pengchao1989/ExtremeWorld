<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="container main_content">
		<h1>welcome</h1>
		
		<form action="${ctx}/qqlogin/${action}" method="post" class="form-horizontal">
			
			<input type="hidden" name="id" value="${user.id}"/>
		
			<div class="form-group">
			    <label for="inputEmail3" class="col-sm-2 control-label">名字</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="name"  name="name" placeholder="名字" value="${user.name}">
			    </div>
			  </div>

			<div class="form-group">
				<div class="col-md-2"></div>
				<div class="col-sm-10">
					<button type="submit" class="btn btn-primary btn-lg">发布</button>
				</div>

			</div>
		</form>
	</div>
</body>
</html>