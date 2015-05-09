<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="static_url" value="http://7u2nc3.com1.z0.glb.clouddn.com/"/>

<div>
<c:forEach items="${topics.content}" var="topic">

	<div class="row ">
		<div class="col s12 m10">
			<div class="white-grey darken-1">
				<div class="card-content black-text">

						
					
					<!-- media -->
					<div class="row">

						<ul class="box">
						
							<div>
								<h4><fmt:formatDate value="${topic.mediaWrap.createTime}" pattern="yyyy年MM月dd日" /></h4>
								<a href="${ctx}/topic/${topic.id}" target="_blank"><span class="card-title black-text">${topic.title}</span></a>
							</div>
							
							<c:forEach items="${topic.mediaWrap.medias}" var="media">
								<li class="card-thumbnails"><a
									href="${static_url}${media.path}" data-rel="colorbox"> <img
										alt="" src="${static_url}${media.path}!topicListThum">
								</a></li>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div class="card-action">
					<a href="#">评论 ${topic.replyCount}</a> <a href='#'><i
						class="mdi-action-thumb-up"> ${topic.agreeCount}</i></a>
				</div>
			</div>
		</div>
	</div>


</c:forEach>
</div>