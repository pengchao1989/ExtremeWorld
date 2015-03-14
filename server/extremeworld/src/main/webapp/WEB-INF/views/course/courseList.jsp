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
	<div class="container">
		<c:forEach items="${courseTaxonomyList}" var="courseTaxonomy">
			<h1>${courseTaxonomy.name}</h1>
			
			<c:forEach items="${courseTaxonomy.courses}" var="course">
				<p>${course.name}</p>
			</c:forEach>
			
		</c:forEach>
	</div>
</body>
</html>