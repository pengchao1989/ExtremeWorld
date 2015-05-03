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
				<h3>${user.name}</h3>
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
					
				<ul class="nav nav-tabs">
				  <li role="presentation" class="active"><a href="#">动态</a></li>
				  <li role="presentation"><a href="#">照片</a></li>
				  <li role="presentation"><a href="#">视频</a></li>
				  <li role="presentation"><a href="#">技能</a></li>
				</ul>	
			</div>
			
		</div>
		
		<div class="row"> 
			
		</div>
	</div>
	
</body>
</html>