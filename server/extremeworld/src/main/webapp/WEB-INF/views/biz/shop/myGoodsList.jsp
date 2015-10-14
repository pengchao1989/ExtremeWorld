<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品列表</title>
</head>
<body>
	<div class="container">
		<h2 class="header">商品列表</h2>

		<div class="row">
			<ul class="collection">
			<c:forEach items="${goodsPage.content}" var="goods">
				<li class="collection-item">
					<div class="row">
						<div class="col s2 m2">
						<img src="${goods.cover}!topicListThum"alt="" > 
						</div>
						<div class="col s10 m10">
						
							<p class="flow-text">${goods.name}</p>
							
							售价：${goods.startPrice}
							<br>
							描述：${goods.des}
							<br>
							分类：
							<c:forEach items="${goods.categorys}" var="category">
								${category.hobby.name} ${category.name	}
							</c:forEach>
							
							<a href="#!" class="secondary-content">编辑</a>
						</div>
					</div>
				</li>
			</c:forEach>

			</ul>

		</div>

		<tags:pagination page="${goodsPage}" paginationSize="5" />
	</div>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
</body>
</body>
</html>