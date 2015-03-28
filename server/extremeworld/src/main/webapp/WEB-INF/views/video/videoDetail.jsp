<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="${ctx}/static/abplayer/css/base.css?1" />
<script src="${ctx}/static/abplayer/js/CommentCoreLibrary.min.js"></script>
<script src="${ctx}/static/abplayer/js/ABPlayer.min.js"></script>

<title>Insert title here</title>

	<script type="text/javascript">
			var $_ = function(e){return document.getElementById(e);};
			window.addEventListener("load",function(){
				$_("click-load").addEventListener("click", function(e){
					if(e && e.preventDefault)
						e.preventDefault();
					var inst = ABP.create(document.getElementById("load-player"), {
						"src":{
							"playlist":[
								{
									"video":document.getElementById("video-1"),
									"comments":"${ctx}/static/abplayer/comment-otsukimi.xml"
								},
								{
									"video":document.getElementById("video-2"),
									"comments":"${ctx}/static/abplayer/comment-science.xml"
								}
							]
						},
						"width":800,
						"height":522
					});
					$_("click-load").style.display= "none";
				});
			});
	</script>
	
	
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
					
						<div >
						<video id="video-1" autobuffer="true" data-setup="{}" width="800" height="450">
							<!-- <source src="//static.cdn.moe/ccltestingvideos/otsukimi_recital.mp4" type="video/mp4"> -->
							<source src=${video.videoSource}>
							<!-- <source src="//static.cdn.moe/ccltestingvideos/otsukimi_recital.webm" type="video/webm"> -->
							<p>Your browser does not support html5 video!</p>
						</video>
						<video id="video-2" style="display:none;" data-setup="{}" width="800" height="450">
							<!-- <source src="//static.cdn.moe/ccltestingvideos/outer_science.webm" type="video/webm"> -->
							<source src=${video.videoSource}>
							<p>Your browser does not support html5 video!</p>
						</video>
						<div id="load-player"></div>
						<a id="click-load" class="pbutton" href="#">Load ABPlayerHTML5 Binding</a><br>
				</div>
			
			
			</div>
		</div>
	
			
	</div>
</body>
</html>