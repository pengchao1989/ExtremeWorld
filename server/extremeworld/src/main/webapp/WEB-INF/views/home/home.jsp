<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

	
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="${ctx}/static/styles//colorbox.css" />

<title>新鲜事</title>
</head>
<body>

	<div class="container">
	
		<div class="row">
			<h3>新鲜事</h3>
			<div class="col s12 m9 l10 ">
				
				<tbody>
					<c:forEach items="${topics.content}" var="topic">
						
						<div class="row ">
							<div class="col s12 l10">
								<div class="card white-grey darken-1">
									<div class="card-content black-text">
										
										<div class="row valign-wrapper">
											<div>
												<a href="${ctx}/u/${topic.user.id}"> <img class="circle responsive-img" src="${topic.user.avatar}!webAvatarSmall" alt="..."></a>
											</div>
											<div class="card-user-head-name s10 ">
												<a href="${ctx}/u/${topic.user.id}" target="_blank">${topic.user.name}</a>
												<p>${topic.createTime}</p>
											</div>
										</div>
										
										
										
										<div class="row valign-wrapper">
											<a href="${ctx}/${hobby}/topic/${topic.id}" target="_blank"><span class="card-title black-text">${topic.title}</span></a>
										</div>
										
<%-- 										<c:if test="${topic.videoDetail != null}">
											<video id="myvideo" src="${topic.videoDetail.videoSource}" controls="controls"></video>
										</c:if> --%>
										
										<p >${topic.excerpt}</p>
										
										<!-- media -->
										<div class="row media_container">
										
											<ul class="box">
												<c:forEach items="${topic.mediaWrap.medias}" var="media">
												<c:choose>
													<c:when test="${media.type == 'img'}">
														<li class="card-thumbnails">
															<a href="${media.path}" data-rel="colorbox">
																<img alt="" src="${media.path}!topicListThum">
															</a>
														</li>
													</c:when>
													<c:when test="media.type == 'video' || media.type == 's_video'">
													</c:when>
												</c:choose>
												

												</c:forEach>
											</ul>
										</div>
									</div>
									<div class="card-action">
										<a href="#">评论  ${topic.replyCount}</a> 
										<a class="zan" href='javascript:void(0);'><i class="mdi-action-thumb-up"></i> <span>${topic.agreeCount}</span><lable style="visibility:hidden">${topic.id}</lable></a>
									</div>
								</div>
							</div>
						</div>
						
						
						</c:forEach>
				</tbody>
				
				<div id="more-container">
				
				</div>
				
				<!-- <a id="load-more" class="waves-effect waves-light btn-large">加载更多</a> -->
			
			<tags:loadmore page="${topics}" paginationSize="5"/>
			</div>
	
				
			
			<div class="col hide-on-small-only l2 m3">
				<div class="toc-wrapper pin-top" style="top: 0px;">
			        <div style="height: 1px;">
			          <ul class="section table-of-contents">
			            <li><a href="#grid-container" class="active">Container</a></li>
			            <li><a href="#grid-intro" class="">Introduction</a></li>
			            <li><a href="#grid-offsets" class="">Offsets</a></li>
			            <li><a href="#grid-layouts" class="">Creating Layouts</a></li>
			            <li><a href="#grid-responsive" class="">Responsive Layouts</a></li>
			          </ul>
			        </div>
			      </div>
           
			</div>
		</div>

		
	
		
		  <div class="fixed-action-btn" style="bottom: 45px; right: 24px;">
		    <a class="btn-floating btn-large red">
		      <i class="large mdi-editor-mode-edit"></i>
		    </a>
		    <ul>
		      <li><a class="btn-floating red"><i class="large mdi-editor-insert-chart"></i></a></li>
		      <li><a class="btn-floating yellow darken-1"><i class="large mdi-editor-format-quote"></i></a></li>
		      <li><a class="btn-floating green"><i class="large mdi-editor-publish"></i></a></li>
		      <li><a class="btn-floating blue"><i class="large mdi-editor-attach-file"></i></a></li>
		    </ul>
		  </div>
		
		
	</div>
	


	
	<script src="${ctx}/static/jquery/jquery.colorbox.js"></script>

	<script type="text/javascript">
	
		$(document).ready(function(){   
			
		})
		
		
		var currentPage = 1;
	
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


			$('.box [data-rel="colorbox"]').colorbox(colorbox_params);
			$("#cboxLoadingGraphic").html(
					"<i class='mdi-notification-sync'></i>");//let's add a custom loading icon

			$(document).one('ajaxloadstart.page', function(e) {
				
				$('$(this).box [data-rel="colorbox"]').colorbox(colorbox_params);
				
				$('#colorbox, #cboxOverlay').remove();
			});
					
			$('.media_container').on('mouseenter',function(){
				console.log("mouseenter log 1");
			});
					
			$("#load-more").click(function(){
				$.get("${ctx}/${hobby}/loadmore?page=" + (++currentPage),function(data){
					$("#more-container").append(data);
					
					
				}) ;
			});
			
			
			/* zan */
/* 			$('a.zan').click(function(){
		        var left = parseInt($(this).offset().left)+10, top =  parseInt($(this).offset().top)-10, obj=$(this);

		        $(this).append('<div id="zan"><b>+1<\/b></\div>');
		        $('#zan').css({'position':'absolute','z-index':'1','color':'#C30','left':left+'px','top':top+'px'});
		        $('#zan').animate({top:top-20,opacity: 0},1500,
		        function(){
		            $(this).fadeOut(1500).remove();
		            var Num = parseInt(obj.find('span').text());
		               Num++;
		            obj.find('span').text(Num);
		        });
		    return false;
		    }); */
		    
			$('a.zan').click(function(){
				var topicId = $(this).children("lable").text();
				var obj=$(this)
				console.log(topicId);
				$.ajax({
						type: 'POST',
						contentType:"application/json",
						url:"/api/v1/topic_agree",
						data:
						{
							"topicId":topicId,
							"userId":<shiro:principal property="id"/>
						},
						success:function(data, status){
							var Num = parseInt(obj.find('span').text());
							Num++;
							obj.find('span').text(Num);
						}})
			});
		    
		    $('#myvideo').click(function(){
		    	$(this).play();
		    })
			
		})
		
	</script>

</body>
</html>