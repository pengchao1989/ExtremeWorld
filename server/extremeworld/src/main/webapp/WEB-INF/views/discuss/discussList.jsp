<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>讨论</title>

<link rel="stylesheet" href="${ctx}/static/umeditor/themes/default/css/umeditor.css">

</head>
<body>

	<div class="container main_content">

		<div class="row">
			<ul>
				<li class="discuss-taxonomy-item">
						<c:if test="${0==currentTaxonomyId}">
							<a class="waves-effect waves-light btn blue" href="${ctx}/${hobby}/discuss?taxonomy=0">全部</a>
						</c:if>
						<c:if test="${0!=currentTaxonomyId}">
							<a class="waves-effect waves-light btn" href="${ctx}/${hobby}/discuss?taxonomy=0">全部</a>
						</c:if>
				</li>
				
				<c:forEach items="${taxonomys}" var="taxonomy">
					<li class="discuss-taxonomy-item">
						<c:if test="${taxonomy.id==currentTaxonomyId}">
							<a class="waves-effect waves-light btn blue" href="${ctx}/${hobby}/discuss?taxonomy=${taxonomy.id}">${taxonomy.name}</a>
						</c:if>
						<c:if test="${taxonomy.id!=currentTaxonomyId}">
							<a class="waves-effect waves-light btn" href="${ctx}/${hobby}/discuss?taxonomy=${taxonomy.id}">${taxonomy.name}</a>
						</c:if>
					</li>
				</c:forEach>
			</ul>
		</div>


		<ul class="collection">
			<c:forEach items="${topics.content}" var="topic">
				<li class="collection-item avatar">
					<a href="${ctx}/u/${topic.user.id}"> <img class="circle" src="${topic.user.avatar}!webAvatarSmall" alt="..."></a>
							
					<a href="${ctx}/${hobby}/discuss/${topic.id}" target="_blank"> <span class="title">${topic.title}</span></a>
					<p><a href="${ctx}/u/${topic.user.id}">${topic.user.name}</a><br></p>
				</li>
			</c:forEach>
		</ul>


		<tags:pagination page="${topics}" paginationSize="5"/>
	
	<shiro:authenticated>  
		<c:if test="${0!=currentTaxonomyId}">
			<div class="row">
				<form class="form-horizontal" action="${ctx}/${hobby}/discuss?taxonomyId=${currentTaxonomyId}"  method="post"  class="form-horizontal">
					<section id="edit">
						<input type="text" id="topic_title" name="title"   value="${task.title}" class="form-control"  placeholder="标题" minlength="3"/>
	<!-- 					<br/>
						<textarea id="txt-content"  name="content"  data-autosave="editor-content"  placeholder="这里输入内容" autofocus></textarea>
						<br/> -->
						
						<script id="edit-container" name="content" type="text/plain" style="width:100%;height:360px;"></script>	
						
						<input id="submit_btn" class="btn btn-primary" type="submit" value="发布"/>&nbsp;	
					</section>
				</form>	
			</div>
		</c:if>
	</shiro:authenticated>


	</div>
	
	<!-- 载入UMeditor，以下文件可存放在CDN -->
	<script src="${ctx}/static/umeditor/umeditor.config.js"></script>
	<script src="${ctx}/static/umeditor/umeditor.min.js"></script>
	<script src="${ctx}/static/umeditor/plupload/plupload.full.min.js"></script>
	<script src="${ctx}/static/umeditor/qiniu.min.js"></script>
	
	
	<!-- 初始化七牛插件，必须由服务器动态生成 -->
	<script src="${ctx}/static/umeditor/qiniu.init.js"></script>
	
	<!-- 创建编辑器 -->
	<script>
	    var QINIU_UPTOKEN_URL = '${ctx}/api/v1/uptoken/picture';
 		var QINIU_BUCKET_DOMAIN = 'img.jixianxueyuan.com';
		var um = UM.getEditor('edit-container');
	</script>
	
</body>
</html>