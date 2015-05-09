<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="static_url" value="http://7u2nc3.com1.z0.glb.clouddn.com/"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Extreme world</title>
</head>
<body>
	<div class="container main_content">
		
		<div class="user-profile row">
			
			<div class="col-xs-12 col-sm-3 center">
				<div class="thumbnail">
					<img data-src="holder.js/150x150" alt="..."src="${static_url}${user.avatar}">
				</div>
				<div class="profile-name">${user.name}<span class="profile-interest">/滑板 小轮车</span></div>
			</div>
			
			<div class="col-xs-12 col-sm-9">
				<div class="profile-user-info profile-user-info-striped">
					<div class="profile-info-row">
						<div class="profile-info-name"> Location </div>
						<div class="profile-info-value"> 成都 </div>
					</div>
					<div class="profile-info-row">
						<div class="profile-info-name"> age </div>
						<div class="profile-info-value"> ${user.birth} </div>
					</div>
					<div class="profile-info-row">
						<div class="profile-info-name"> Joined </div>
						<div class="profile-info-value"> ${user.registerDate} </div>
					</div>
					<div class="profile-info-row">
						<div class="profile-info-name"> Last Online </div>
						<div class="profile-info-value"> 3 hours ago </div>
					</div>				
					
				</div>
				
				<br>
					
				  <div class="row">
				    <div class="col s12">
				      <ul class="tabs">
				        <li class="tab col s3"><a class="active" href="#topic">动态</a></li>
				        <li class="tab col s3"><a id="picture-li" href="#picture">照片</a></li>
				        <li class="tab col s3"><a id="video-li" href="#video">视频</a></li>
				        <li class="tab col s3"><a href="#score">成就</a></li>
				      </ul>
				    </div>
				    <!-- topic Fragment -->
				    <div id="topic" class="col s12">
						<tbody>
							<c:forEach items="${topics.content}" var="topic">

								<div class="row ">
									<div class="col s12 m10">
										<div class="card white-grey darken-1">
											<div class="card-content black-text">
												
												<div class="row valign-wrapper">
													<div>
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
												<div class="row media_container">
												
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
						
						<div id="more-topic-container">
						</div>
						
						<a id="load-more-topic" class="waves-effect waves-light btn-large">加载更多</a>
					</div>
					
					<!-- picture fragment -->
				    <div id="picture" class="col s12">
						<div id="more-picture-container">
						</div>
				    </div>
				    
				    <!-- video fragment -->
				    <div id="video" class="col s12">
						<div id="more-video-container">
						</div>
				    </div>
				    <div id="score" class="col s12">Test 4</div>
				  </div>
        
			</div>
			
		</div>
		
		<div class="row"> 
			
		</div>
	</div>
	
	<script type="text/javascript">
	  $(document).ready(function(){
		  
		    $('ul.tabs').tabs();
		    
		    var currentTopicPage = 1;
		    var currentPicturePage = 0;
		    var currentVideoPage = 0;
		    
		    
			$("#load-more-topic").click(function(){
				$.get("${ctx}/u/loadtopic/${user.id}?page=" + (++currentTopicPage),function(data){
					$("#more-topic-container").append(data);
					
					
				}) ;
			});
			
			$("#picture-li").click(function(){
				$.get("${ctx}/u/loadpicture/${user.id}?page=" + (++currentPicturePage),function(data){
					$("#more-picture-container").append(data);
					
					
				}) ;
			});
			
			$("#video-li").click(function(){
				$.get("${ctx}/u/loadvideo/${user.id}?page=" + (++currentVideoPage),function(data){
					$("#more-video-container").append(data);
					
					
				}) ;
			});
			
		  });
	</script>
</body>
</html>