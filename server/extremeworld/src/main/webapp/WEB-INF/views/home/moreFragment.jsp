<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="static_url" value="http://7u2nc3.com1.z0.glb.clouddn.com/"/>

<div>
<c:forEach items="${topics.content}" var="topic">

	<div class="row ">
		<div class="col s12 m10">
			<div class="card white-grey darken-1">
				<div class="card-content black-text">

					<div class="row valign-wrapper">
						<div class="col s2">
							<a href="${ctx}/u/${topic.user.id}"> <img
								class="circle responsive-img"
								src="${static_url}${topic.user.avatar}!webAvatarSmall" alt="..."></a>
						</div>
						<div class="card-user-head-name s10 ">
							<a href="${ctx}/u/${topic.user.id}">${topic.user.name}</a>
							<p>${topic.createTime}</p>
						</div>
					</div>



					<div class="row valign-wrapper">
						<a href="${ctx}/topic/${topic.id}" target="_blank"><span
							class="card-title black-text">${topic.title}</span></a>
					</div>

					<p>${topic.content}</p>

					<!-- media -->
					<div class="row">

						<ul class="box">
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