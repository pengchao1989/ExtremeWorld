<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="static_url" value="http://7u2nc3.com1.z0.glb.clouddn.com/"/>

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
					<h1>${course.name}</h1><p><a class="btn btn-success " href="${ctx}/${hobby}/course/update/${course.id}" role="button">编辑</a></p>
				</div>
				
				<div class="col-sm-2">
					<p><a class="btn btn-success " href="${ctx}/course/update/${course.id}" role="button">挑战</a></p>
					
				</div>
			</div>
			
			<label>最后由</label><a href="${ctx}/u/${course.user.id}" target="_blank">${course.user.name}</a><label>于${course.modifyTime}编辑</label><a href="${ctx}/${hobby}/course/revision/${course.id}">  历史</a>
			
		 	<p id="course_content">${course.content}</p>
		</div>
		
		<h2>讨论与教学</h2>
		
		<div class="row">
			<ul class="collection">
				<c:forEach items="${topics.content}" var="topic">
					<li class="collection-item avatar">
						<a href="${ctx}/u/${topic.user.id}"> <img class="circle" src="${static_url}${topic.user.avatar}!webAvatarSmall" alt="..."></a>
								
						<a href="${ctx}/${hobby}/topic/${topic.id}" target="_blank"> <span class="title">${topic.title}</span></a>
						<p><a href="${ctx}/u/${topic.user.id}">${topic.user.name}</a><br></p>
					</li>
				</c:forEach>
			</ul>

		</div>
	
	</div>
	
	<script type="text/javascript">
	
		function TransferString(content)  
		{  
		    var string = content;  
		    try{  
		        string=string.replace(/\r\n/g,"<br/>")  
		        string=string.replace(/\n/g,"<br/>");  
		    }catch(e) {  
		        alert(e.message);  
		    }  
		    return string;  
		}  
	
	
		$(document).ready(function(){
			$("#course_content").html(TransferString($("#course_content").text()));
		});
	</script>
</body>
</html>