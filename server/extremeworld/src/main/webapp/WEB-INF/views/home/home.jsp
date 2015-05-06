<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="static_url" value="http://7u2nc3.com1.z0.glb.clouddn.com/"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="${ctx}/static/styles//colorbox.css" />

<title>Extreme world</title>
</head>
<body>

	<div class="container">
	
		<div class="row">
			<div class="col s12 m9 l10">
				<h3>新鲜事</h3>
				<tbody>
					<c:forEach items="${topics.content}" var="topic">
						
						<div class="row ">
							<div class="col s12 m6">
								<div class="card white-grey darken-1">
									<div class="card-content black-text">
										
										<div class="row valign-wrapper">
											<div class="col s2">
												<a href="${ctx}/u/${topic.user.id}"> <img class="circle responsive-img" src="${static_url}${topic.user.avatar}!webAvatarSmall" alt="..."></a>
											</div>
											<div class="card-user-head-name s10 ">
												<a href="${ctx}/u/${topic.user.id}">${topic.user.name}</a>
												<p>${topic.createTime}</p>
											</div>
										</div>
										
										
										
										<div class="row valign-wrapper">
											<a href="${ctx}/topic/${topic.id}" target="_blank"><span class="card-title black-text">${topic.title}</span></a>
										</div>
										
										<p >${topic.content}</p>
										
										<!-- media -->
										<div class="row">
										
										<ul class="box">
										<c:forEach items="${topic.mediaWrap.medias}" var="media">
										<li class="card-thumbnails">
										<a href="${static_url}${media.path}" data-rel="colorbox">
											<img alt="" src="${static_url}${media.path}!topicListThum">
										</a>
										</li>
										</c:forEach>
										</ul>
										</div>
									</div>
									<div class="card-action">
										<a href="#">评论  ${topic.replyCount}</a> <a href='#'><i class="mdi-action-thumb-up">  ${topic.agreeCount}</i></a>
									</div>
								</div>
							</div>
						</div>
						
						
						</c:forEach>
				</tbody>
			
				<tags:pagination page="${topics}" paginationSize="5"/>
			</div>
	
				
			
			<div class="col hide-on-small-only m3 l2">
				<div class="toc-wrapper pinned" style="top: 0px;">
					<p>123</p>
				</div>	
			
			</div>
		</div>

		
	
		
		
		
	</div>
	
	<script src="${ctx}/static/jquery/jquery.colorbox.js"></script>

	<script type="text/javascript">
		jQuery(function($) {
			var $overflow = '';
			var colorbox_params = {
				rel : 'colorbox',
				reposition : true,
				scalePhotos : true,
				scrolling : false,
				previous : '<i class="ace-icon fa fa-arrow-left"></i>',
				next : '<i class="ace-icon fa fa-arrow-right"></i>',
				close : '&times;',
				current : '{current} of {total}',
				maxWidth : '100%',
				maxHeight : '100%',
				onOpen : function() {
					$overflow = document.body.style.overflow;
					document.body.style.overflow = 'hidden';
				},
				onClosed : function() {
					document.body.style.overflow = $overflow;
				},
				onComplete : function() {
					$.colorbox.resize();
				}
			};

			$('.box [data-rel="colorbox"]')
					.colorbox(colorbox_params);
			$("#cboxLoadingGraphic").html(
					"<i class='mdi-notification-sync'></i>");//let's add a custom loading icon

			$(document).one('ajaxloadstart.page', function(e) {
				$('#colorbox, #cboxOverlay').remove();
			});
		})
	</script>

</body>
</html>