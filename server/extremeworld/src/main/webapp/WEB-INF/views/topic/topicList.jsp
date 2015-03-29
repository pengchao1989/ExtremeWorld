<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<<c:set var="static_url" value="http://7u2nc3.com1.z0.glb.clouddn.com/"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>


</head>
<body>

	<div class="container main_content">

		<div class="row">
			<tbody>
				<ul class="media-list">
					<c:forEach items="${topics.content}" var="topic">
						<li class="media">
								<div class="media-left">
									<a href="#"> <img class="media-object avatar" src="${static_url}${topic.user.avatar}" alt="..."></a>
									
								</div>
								<div class="media-body">
									<a href="${ctx}/topic/${topic.id}" target="_blank"> 
									
									<div class="row">
										<div class="col-md-6">
											<h3 class="list-group-item-heading">${topic.title}</h3>
											<a>${topic.user.name}</a> 
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


		<tags:pagination page="${topics}" paginationSize="5"/>
	
	<div class="row">
			<form class="form-horizontal" action="${ctx}/topic"  method="post"  class="form-horizontal">
				<section id="edit">
					<input type="text" id="topic_title" name="title"   value="${task.title}" class="form-control"  placeholder="标题" minlength="3"/>
					<br/>
					<textarea id="txt-content"  name="content"  data-autosave="editor-content"  placeholder="这里输入内容" autofocus></textarea>
					<br/>
					<input id="submit_btn" class="btn btn-primary" type="submit" value="发布"/>&nbsp;	
				</section>
			</form>	
	</div>
	

	</div>

	
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/module.js"></script>
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/hotkeys.js"></script>
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/uploader.js"></script>
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/simditor.js"></script>
	<script type="text/javascript" src="${ctx}/static/scripts/edit.js"></script>
</body>
</html>