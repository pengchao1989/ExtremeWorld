<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="${ctx}/static/DanmuPlayer/css/danmuplayer.css">
<script src="${ctx}/static/DanmuPlayer/js/danmuplayer.js"></script>


<title>Insert title here</title>

	
	
</head>
<body>
	<div class="container main_content">
	
		<h1>${topic.title}</h1>
	
		<div class="media">
			<div class="media-left">
					<a href="#"> <img class="media-object"
						src="http://7u2nc3.com1.z0.glb.clouddn.com/${video.user.avatar}" alt="..."></a>
			</div>
			
			<div class="media-body">
			
				<a href="#">${video.user.name}</a>
				<p>${video.createTime}</p>
				<br />
					
				<div id="danmup">
				</div>
			
			
			</div>
		</div>
	
			
	</div>
	
 	<script>
		$(document).ready(function(){
		$("#danmup").danmuplayer({
			src:"${video.videoDetail.videoSource}",
			width:800,
			height:445,
		    speed: 15000,
		    danmuss: {},
		    sumtime: 65535,
		    default_font_color: "#FFFFFF",
		    font_size_small: 16,
		    font_size_big: 24,
		    opacity: "1",
		    top_botton_danmu_time: 5000,
		    url_to_get_danmu:"${ctx}/api/v1/danmu/2",
		    url_to_post_danmu:"${ctx}/api/v1/danmu/add?videoId=${video.id}&userId=${userId}"
			});
		
		});
	</script>


</body>


</html>