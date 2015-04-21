<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="${ctx}/static/abplayer/css/base.css?1" />
<script src="${ctx}/static/abplayer/js/CommentCoreLibrary.min.js"></script>
<script src="${ctx}/static/abplayer/js/ABPlayer.min.js"></script>

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

<title>Insert title here</title>

<script type="text/javascript">
	$(document).ready(function() {

		$("a#newReply").click(function() {
			$("div.class").append("<a>Appended item</a>");
		});
	});
</script>

</head>
<body>
	<div class="container main_content">

		<div class="reply_list_head">
			<h1>${topic.title}</h1>
			<div class="media">
				<div class="media-left">
					<a href="#"> <img class="media-object"
						src="http://7u2nc3.com1.z0.glb.clouddn.com/${topic.userInfo.avatar}" alt="..."></a>
				</div>

				<div class="media-body">
					<a href="#">${topic.userInfo.name}</a>
					<p>${topic.createTime}</p>
					<br />
					<h4>${topic.content}</h4>

				<c:if test="${type == 'video'}">


					
 					<c:if test="${topic.videoDetail.videoSource != null}">
						<div >
<!-- 							<video src="http://7vilxo.com1.z0.glb.clouddn.com/video_test.mp4"
								controls="controls"></video> -->
								
								<video id="video-1" autobuffer="true" data-setup="{}" width="800" height="450">
									<source src="${topic.videoDetail.videoSource}" type="video/mp4">
									<p>Your browser does not support html5 video!</p>
								</video>
								<video id="video-2" style="display:none;" data-setup="{}" width="800" height="450">
									<source src=${topic.videoDetail.videoSource} type="video/mp4">
									<p>Your browser does not support html5 video!</p>
								</video>
								<div id="load-player"></div>
								<a id="click-load" class="pbutton" href="#">Load ABPlayerHTML5 Binding</a><br>
	
						</div>
					</c:if> 
					
					</c:if>
				</div>
			</div>
		</div>



		<div class="row">
			<c:forEach items="${replys.content}" var="reply">
				<div class="reply_list_item">
					<div class="media">
						<div class="media-left">
							<img class="media-object avatar" alt="Alexa's Avatar"
								src="${ctx}/static/ace/assets/avatars/avatar1.png" />
						</div>

						<div class="row media-body">

							<div class="col-md-10">
								<a href="#">${reply.userInfo.name}</a>
								<p>${reply.createTime}</p>
								<h4 >${reply.content}</h4>
							</div>
							<div class="col-md-2">
								<p>1楼</p>
								<a class="sub_reply_form">回复</a>
							</div>

						</div>
					</div>

					<!-- 子回复 -->
					<div class="reply_panl">
						<c:forEach items="${reply.subReplys}" var="subreply">
							<div class="media">
								<div class="media-left">
									<img class="media-object avatar" alt="Alex Doe's avatar"
										src="${ctx}/static/ace/assets/avatars/avatar5.png" />
								</div>

								<div class="media-body">
									<a href="#"> ${subreply.userInfo.name} </a>
									<c:if test="${subreply.preSubReply  != null}">
									回复<a href="#"> ${subreply.preSubReply.userInfo.name}</a>
									</c:if>
									${subreply.content}
									<div class="row">
										<p>${subreply.createTime}</p>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>


				</div>
			</c:forEach>
		</div>
		
		<tags:pagination page="${replys}" paginationSize="5"/>
		
		<div class="reply_panl">
			<form action="">
				<input id="submit_btn" class="" type="text" value="回复"/>
				<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
			</form>
		</div>
		
		<div class="row">
				<form class="form-horizontal" action="${ctx}/topic/${topic.id}"  method="post"  class="form-horizontal">
					<section id="edit">
						<textarea id="txt-content"  name="content"  data-autosave="editor-content"  placeholder="这里输入内容" autofocus></textarea>
						<br/>
						<input id="submit_btn" class="btn btn-primary" type="submit" value="回复"/>&nbsp;	
					</section>
				</form>	
		</div>
		
	</div>
	
	<script type="text/javascript">
		$(document).ready(function()
		{
			$(".sub_reply_form").click(function(){
				$(this).text("huifu");
				
				/* 获取到主回复div */
				$(this).parents().filter(".reply_list_item").append('<div class="reply_panl"><form><input></input><form></div>');
			});
		});
	
	</script>


	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/module.js"></script>
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/hotkeys.js"></script>
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/uploader.js"></script>
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/simditor.js"></script>
	<script type="text/javascript" src="${ctx}/static/scripts/edit.js"></script>

</body>
</html>