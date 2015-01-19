<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

	<script type="text/javascript">
		$(document).ready(function(){
	
			  $("a#newReply").click(function(){
			    $("div.class").append("<a>Appended item</a>");
			  });
			});
	</script>
	
</head>
<body>
					
	<div class="container">
	
		<div class="row">
			<div class="widget-header widget-header-flat">
				<h4 class="widget-title smaller">
					<h1>${topic.title}</h1>
					<p class="lead">${topic.content}</p>
				</h4>
			</div>
		</div>
		
		<div class="row">
			<tbody>
			<c:forEach items="${replys.content}" var="reply">
				<div class="itemdiv dialogdiv">
					<div class="user">
						<img alt="Alexa's Avatar" src="${ctx}/static/ace/assets/avatars/avatar1.png" />
					</div>
			
					<div class="body">
						<div class="time">
							<i class="ace-icon fa fa-clock-o"></i>
							${reply.createTime}
						</div>
			
						<div class="clearfix">
							<a class="user" href="#">${reply.userInfo.name}</a>
						</div>
						<div class="text">${reply.content}</div>
			
						<div class="tools action-buttons">
							<a id="newReply" class="btn btn-minier btn-info">
								<i class="icon-only ace-icon fa fa-share"></i>
							</a>
						</div>
						
						<!-- 子回复 -->
						<div>
							<c:forEach items="${reply.subReplys}" var="subreply">
								<div class="profile-activity ">
									<div>
										<img class="pull-left" alt="Alex Doe's avatar" src="${ctx}/static/ace/assets/avatars/avatar5.png" />
										<a href="#"> ${subreply.userInfo.name} </a>
											${subreply.content}
										<div class="time">
											<i class="ace-icon fa fa-clock-o bigger-110"></i>
											an hour ago
										</div>
									</div>
	
									<div class="tools action-buttons">
										<a href="#" class="blue">
											<i class="ace-icon fa fa-comment bigger-125"></i>
										</a>
									</div>
								</div>
							</c:forEach>
						</div>
						
						
						
					</div>
				</div>


			</c:forEach>
			</tbody>
		</div>
	</div>
	
		

</body>
</html>