<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div>
	<ul class="collection">
		<c:forEach items="${topics.content}" var="topic">
			<li class="collection-item avatar">
				<a href="${ctx}/u/${topic.user.id}"> <img class="circle" src="${topic.user.avatar}!webAvatarSmall" alt="..."></a>
						
				<a href="${ctx}/${hobby}/topic/${topic.id}" target="_blank"> <span class="title">${topic.title}</span></a>
				<p><a href="${ctx}/u/${topic.user.id}">${topic.user.name}</a><br></p>
			</li>
		</c:forEach>
	</ul>
</div>