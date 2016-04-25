<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>教学</title>
</head>
<body>
	<div class="container main_content">
	
		<a class="btn btn-success " href="${ctx}/${hobby}/course/create" role="button">新增教学</a>
		
		<c:forEach items="${courseTaxonomyList}" var="courseTaxonomy">
			<h1>${courseTaxonomy.name}/${courseTaxonomy.des}</h1>
			
			<div class="list-group">
			
					<h3>${courseCatalogue.name}</h3>
						<div class="list-group">
						<c:forEach items="${courseTaxonomy.courses}" var="course">
							<a class="list-group-item" href="${ctx}/${hobby}/course/${course.id}">${course.name}</a>
						</c:forEach>
						</div>
			
			</div>
			
		</c:forEach>
	</div>
</body>
</html>