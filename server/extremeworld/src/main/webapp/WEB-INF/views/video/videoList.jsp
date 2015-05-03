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
	
	<div class="row">
		<div class="col-xs-10 col-md-10">
			<div class="btn-group btn-group-lg" role="group" aria-label="...">
			  <button type="button" class="btn btn-default">全部</button>
			  <button type="button" class="btn btn-default">国内</button>
			  <button type="button" class="btn btn-default">国外</button>
			</div>
		</div>
		
		<div class="col-xs-2 col-md-2">
			<a href="${ctx}/video/create" class="btn btn-primary" role="button">上传</a>
		</div>
	</div>
	
		<br>

	
		<ul class="media-list">

			<c:forEach items="${videos.content}" var="video">
			
			<li class="media">
				<div class="media-left">
					<div class="col-xs-6 col-md-3">
						<a href="${ctx}/video/${video.id}">
							<img alt="" src="${video.videoDetail.thumbnail}">
						</a>
					</div>
				</div>
				
				<div class="media-body">
					<h3><a href="${ctx}/video/${video.id}" target="_blank">${video.title}</a></h3>
					<a href="#">${video.user.name}</a> 
					<p>${video.content}</p>
				</div>
			</li>

				
			</c:forEach>
		</ul>
	</div>
	

</body>
</html>