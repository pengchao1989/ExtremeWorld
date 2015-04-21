<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="container main_content">
	
		<div class="jumbotron">
			<div class = "row">
				<div class="col-sm-10">
					<h1>${course.name}</h1>
				</div>
				
				<div class="col-sm-2">
					<p><a class="btn btn-success " href="${ctx}/course/update/${course.id}" role="button">编辑</a></p>
					<a href="${ctx}/course/revision/${course.id}">历史</a>
				</div>
			</div>
			
			<label>最后由</label><a href="#">${course.userInfo.name}</a><label>于${course.modifyTime}编辑</label>
			
		 	<p>${course.content}</p>
		</div>
		
		<h2>讨论与教学</h2>
		
		<div class="row">
			<tbody>
				<ul class="media-list">
					<c:forEach items="${topics.content}" var="topic">
						<li class="media">
								<div class="media-left">
									<a href="#"> <img class="media-object avatar" src="${ctx}/static/ace/assets/avatars/avatar1.png" alt="..."></a>
									
								</div>
								<div class="media-body">
									<a href="${ctx}/topic/${topic.id}" target="_blank"> 
									<div class="row">
										<div class="col-md-6">
											<h3 class="list-group-item-heading">${topic.title}</h3>
											<a>${topic.userInfo.name}</a> 
										</div>
										
										<div class="col-md-2">
												<p class="views">${topic.replyCount}</p>
												<p>回复</p>
										</div>
										
										<div class="col-md-2">
											<p class="views">99</p>
											<p>查看</p>
										</div>
										
										<div class="col-md-2">
											<p>2015-03-15 17:22</p>
											<p>2015-03-15 17:22</p>
										</div>

									</div>
									

										
									</a>
								</div>
						</li>
					</c:forEach>
				</ul>
			</tbody>

	</div>
	
	</div>
</body>
</html>