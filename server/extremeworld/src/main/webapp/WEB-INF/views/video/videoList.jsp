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
	
		<a href="${ctx}/video/create" class="btn btn-primary btn-lg" role="button">上传</a>
	
		<ul class="media-list">
			<c:forEach items="${videos.content}" var="video">
			<div class="row">
			
			</div>
				<a href="${ctx}/video/${video.id}" target="_blank">${video.title}</a>
			</c:forEach>
		</ul>

	</div>
	

</body>
</html>