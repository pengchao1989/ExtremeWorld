<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="${ctx}/static/abplayer/css/base.css?1" />
<link rel="stylesheet" href="${ctx}/static/DanmuPlayer/css/danmuplayer.css">
<link rel="stylesheet" href="${ctx}/static/umeditor/themes/default/css/umeditor.css">


<script src="${ctx}/static/DanmuPlayer/js/danmuplayer.js"></script>

<title>${topic.title}</title>

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

		<div class="reply_list_head ">
		
			<h3>${topic.title}</h3>
			
			
			<c:if test="${type == 'video'}">
			
				<c:if test="${topic.videoDetail.videoSource != null}">
					<div class="row">
						<div class="col s12 m12 l12" id="danmup">
					
						</div>
					</div>

				</c:if> 
		
			</c:if>


			<div class="media">
				<div class="media-left">
					<a href="${ctx}/u/${topic.user.id}"> <img class="media-object"
						src="${topic.user.avatar}!webAvatarSmall" alt="..."></a>
				</div>

				<div class="media-body">
					<a href="${ctx}/u/${topic.user.id}" target="_blank">${topic.user.name}</a>
					<p>${topic.createTime}</p>
					<br />
						
						
						<br />
					
						<p class="flow-text">${topic.content}</p>

				</div>
			</div>
		</div>



		<div class="row">
			<c:forEach items="${replys.content}" var="reply">
				<div class="reply_list_item">
					<div class="media">
						<div class="media-left">
						<a href="${ctx}/u/${reply.user.id}">
							<img class="media-object avatar" alt="Alexa's Avatar"
								src="${reply.user.avatar}!webAvatarSmall" /></a>
						</div>

						<div class="row media-body">

							<div class="col-md-10">
								<a href="${ctx}/u/${reply.user.id}">${reply.user.name}</a>
								<p>${reply.createTime}</p>
								<p class="flow-text">${reply.content}</p>
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
									<img class="media-object avatar" alt=""
										src="${subreply.user.avatar}!webAvatarSmall" />
								</div>

								<div class="media-body">
									<a href="#"> ${subreply.user.name} </a>
									<c:if test="${subreply.preSubReply  != null}">
									回复<a href="#"> ${subreply.preSubReply.user.name}</a>
									</c:if>
									${subreply.content}

									<p>${subreply.createTime}</p>
								</div>
							</div>
						</c:forEach>
					</div>


				</div>
			</c:forEach>
		</div>
		
		<tags:pagination page="${replys}" paginationSize="5"/>
		
<!-- 		<div class="reply_panl">
			<form action="">
				<input id="submit_btn" class="" type="text" value="回复"/>
				<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
			</form>
		</div> -->
		
		<shiro:authenticated> 
			<div class="row">
				<form class="form-horizontal" action="${ctx}/${hobby}/topic/${topic.id}"  method="post"  class="form-horizontal">
					<section id="edit">
						<!-- <textarea id="txt-content"  name="content"  data-autosave="editor-content"  placeholder="这里输入内容" autofocus></textarea> -->
						
						<script id="edit-container" name="content" type="text/plain" style="width:100%;height:360px;"></script>	
						
						<br/>
						<input id="submit_btn" class="btn btn-primary" type="submit" value="回复"/>&nbsp;	
					</section>
				</form>	
			</div>
		</shiro:authenticated>
		
		<shiro:notAuthenticated>  
		</shiro:notAuthenticated>
		

		
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
	
	
	<c:if test="${type == 'video'}">
		 	<script>
		$(document).ready(function(){
		$("#danmup").danmuplayer({
			src:"${topic.videoDetail.videoSource}",
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
		    url_to_get_danmu:"${ctx}/api/v1/danmu/${topic.id}",
		    url_to_post_danmu:"${ctx}/api/v1/danmu/add?videoId=${topic.id}&userId=${userId}"
			});
		
		});
	</script>
	</c:if>



	<!-- 载入UMeditor，以下文件可存放在CDN -->
	<script src="${ctx}/static/umeditor/umeditor.config.js"></script>
	<script src="${ctx}/static/umeditor/umeditor.min.js"></script>
	<script src="${ctx}/static/umeditor/plupload/plupload.full.min.js"></script>
	<script src="${ctx}/static/umeditor/qiniu.min.js"></script>
	
	
	<!-- 初始化七牛插件，必须由服务器动态生成 -->
	<script src="${ctx}/static/umeditor/qiniu.init.js"></script>
	
	<!-- 创建编辑器 -->
	<script>
	    var QINIU_UPTOKEN_URL = '${ctx}/api/v1/uptoken/picture';
 		var QINIU_BUCKET_DOMAIN = 'img.jixianxueyuan.com/';
		var um = UM.getEditor('edit-container');
	</script>

</body>
</html>