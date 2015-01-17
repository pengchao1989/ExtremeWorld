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
	<br/>
	<br/>
	<div class="list-group bs-callout bs-callout-info">
		<tbody>
		<c:forEach items="${topics.content}" var="topic">
				<a href="${ctx}/topic/${topic.id}" target="_blank" class="list-group-item">
					<span class="badge">14</span>
					<h4 class="list-group-item-heading">${topic.title}</h4>
					<p class="list-group-item-text">...</p>
				</a>
		</c:forEach>
		</tbody>
	</div>
	
	<tags:pagination page="${topics}" paginationSize="5"/>
	
	<form action="${ctx}/topic"  method="post"  class="form-horizontal">
		<section id="edit">
			<input type="text" id="topic_title" name="title"   value="${task.title}" class="form-control"  placeholder="标题" minlength="3"/>
			<br/>
			<textarea id="txt-content"  name="content"  data-autosave="editor-content"  placeholder="这里输入内容" autofocus></textarea>
			<br/>
			<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
		</section>
	</form>	
	

	
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/module.js"></script>
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/hotkeys.js"></script>
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/uploader.js"></script>
	<script type="text/javascript" src="${ctx}/static/simditor-2.0.4/scripts/simditor.js"></script>
	<script type="text/javascript" src="${ctx}/static/scripts/edit.js"></script>
</body>
</html>