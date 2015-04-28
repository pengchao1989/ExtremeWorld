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
	
		<a class="btn btn-success " href="${ctx}/course/create" role="button">新增教学</a>
		
		<c:forEach items="${courseTaxonomyList}" var="courseTaxonomy">
			<h1>${courseTaxonomy.name}</h1>
			
			<div class="list-group">
			
			
				<c:forEach items="${courseTaxonomy.courses}" var="course">
				<c:if test="${course.t=='1'}">
					<a class="list-group-item" href="${ctx}/course/${course.id}">${course.name}</a>
				</c:if>
					
				</c:forEach>
			
			</div>
			
		</c:forEach>
	</div>
</body>
</html>