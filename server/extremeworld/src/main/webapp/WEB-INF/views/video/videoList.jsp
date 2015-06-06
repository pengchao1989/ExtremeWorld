<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>视频</title>
</head>
<body>
	<div class="container main_content">
	
	<div class="row">
		<div class="col-xs-10 col-md-10">
			<a href="${ctx}/${hobby}/video/create" class="btn btn-primary" role="button">上传</a>
		</div>
		
		<div class="col-xs-2 col-md-2">
			
		</div>
	</div>
	
		<br>
		<div class="row">

			<c:forEach items="${videos.content}" var="video" varStatus="status">
				<div class=" col l3 s12">
					<div class="card ">
						<div class="card-image video_image">
							<a href="${ctx}/${hobby}/video/${video.id}" target="_blank">
								<img alt="" src="${video.videoDetail.thumbnail}!webVideoListThum">
							</a>
						</div>
						
						<div class="card-content">
							<span class=" activator grey-text text-darken-3">${video.title}</span>
							<p><a href="${ctx}/u/${video.user.id}">${video.user.name}</a></p>
						</div>
					</div>
				
				</div>
				<c:if test="${status.count%4==0}">
					</br>
				</c:if>
					
				
			</c:forEach>
			
		</div>
	</div>
	

</body>
</html>