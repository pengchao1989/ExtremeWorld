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
	
		<a href="${ctx}/video/create" class="btn btn-primary btn-lg active" role="button">发布视频</a>
	
		<ul class="media-list">
			<c:forEach items="${videos.content}" var="video">
				<a href="${ctx}/video/${video.id}" target="_blank">${video.title} }</a>
			</c:forEach>
		</ul>
	
		<button id="getData" class="btn btn-primary">GET_TOKEN</button>
		<p id="result">result</p>
		
		<form id="uploadForm" method="post" action="http://upload.qiniu.com/"
			 enctype="multipart/form-data">
			  <input name="key" value="<resource_key>">
			  <input name="x:<custom_name>" type="hidden" value="<custom_value>">
			  <input id="uptoken" name="token"  value="<upload_token>">
			  <input name="file" type="file" />
			  <input name="accept" type="hidden" />
			  <button type="submit" class="btn btn-default">Submit</button>
		</form>


	</div>
	
	<script>
		$(document).ready(function(){
			  $("#getData").click(function(){

				  
				    $.get("${ctx}/api/v1/uptoken",function(data, status){

				     	alert("Data: " + data + "\nStatus: " + status);
				     	
						  $("#uptoken").val(data.token);
						  $("#result").text("1234");
						  
			     	
			    		});
			  });
			});
	</script>
</body>
</html>