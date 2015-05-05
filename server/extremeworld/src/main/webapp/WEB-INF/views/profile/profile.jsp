<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

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
				        <li class="tab col s3"><a href="#topic">动态</a></li>
				        <li class="tab col s3"><a class="active" href="#test2">照片</a></li>
				        <li class="tab col s3"><a href="#test3">视频</a></li>
				        <li class="tab col s3"><a href="#test4">成就</a></li>
				      </ul>
				    </div>
				    <div id="topic" class="col s12">
						<tbody>
							<c:forEach items="${topics}" var="topic">

								<div class="row">
									<div class="col s12 m6">
										<div class="card white-grey darken-1">
											<div class="card-content black-text">
												<div class="row valign-wrapper">
													<a href="${ctx}/u/${topic.user.id}"> <img
														class="circle responsive-img"
														src="${static_url}${topic.user.avatar}!webAvatarSmall"
														alt="..."></a> <a href="${ctx}/topic/${topic.id}"
														target="_blank"><span class="card-title black-text">${topic.title}</span></a>
												</div>

												<p>${topic.content}</p>
											</div>
											<div class="card-action">
												<a href="#">This is a link</a> <a href='#'>This is a
													link</a>
											</div>
										</div>
									</div>
								</div>

							</c:forEach>
						</tbody>
					</div>
				    <div id="test2" class="col s12">Test 2</div>
				    <div id="test3" class="col s12">Test 3</div>
				    <div id="test4" class="col s12">Test 4</div>
				  </div>
        
			</div>
			
		</div>
		
		<div class="row"> 
			
		</div>
	</div>
	
	<script type="text/javascript">
	  $(document).ready(function(){
		    $('ul.tabs').tabs();
		  });
	</script>
</body>
</html>