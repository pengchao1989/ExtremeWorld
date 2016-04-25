<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>教学</title>
</head>
<body>
	<div class="container main_content">
	
	<shiro:authenticated> 
		<a class="btn btn-success " href="${ctx}/${hobby}/course/create" role="button">新增教学</a>
    </shiro:authenticated>

		<ul class="collapsible" data-collapsible="accordion">

			<c:forEach items="${courseTaxonomyList}" var="courseTaxonomy">
				<li>
					<div class="collapsible-header">
						<h1>${courseTaxonomy.name}/${courseTaxonomy.des}</h1>
					</div>
					<div class="collapsible-body">
						<div class="list-group">

							<h3>${courseCatalogue.name}</h3>
							<div class="list-group">
								<c:forEach items="${courseTaxonomy.courses}" var="course">
									<a class="list-group-item"
										href="${ctx}/${hobby}/course/${course.id}">${course.name}</a>
								</c:forEach>
							</div>

						</div>
					</div>
				</li>

			</c:forEach>
			
		</ul>

	</div>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$('.collapsible').collapsible({
				accordion : false
			});
		});
	</script>
</body>
</html>