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

	<div class="container ">

			<tbody>
				<c:forEach items="${topics.content}" var="topic">
					
					<div class="row">
						<div class="col s12 m6">
							<div class="card white-grey darken-1">
								<div class="card-content black-text">
									<div class="row valign-wrapper">
										<a href="${ctx}/u/${topic.user.id}"> <img class="circle responsive-img" src="${static_url}${topic.user.avatar}!webAvatarSmall" alt="..."></a>
										<a href="${ctx}/topic/${topic.id}" target="_blank"><span class="card-title black-text">${topic.title}</span></a>
									</div>
									
									<p >${topic.content}</p>
								</div>
								<div class="card-action">
									<a href="#">This is a link</a> <a href='#'>This is a link</a>
								</div>
							</div>
						</div>
					</div>
					
					
					</c:forEach>
			</tbody>
		
		<tags:pagination page="${topics}" paginationSize="5"/>
		
		<div class="col-md-4">
		
		</div>
	
		
		
		
	</div>
	
</body>
</html>