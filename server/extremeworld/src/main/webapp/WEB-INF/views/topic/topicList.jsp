<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="list-group bs-callout bs-callout-info">
		<tbody>
		<c:forEach items="${topics.content}" var="topic">
				<a href="#" class="list-group-item">
					<span class="badge">14</span>
					<h4 class="list-group-item-heading">${topic.title}</h4>
					<p class="list-group-item-text">...</p>
				</a>
		</c:forEach>
		</tbody>
	</div>
	
	<tags:pagination page="${topics}" paginationSize="5"/>
	
</body>
</html>