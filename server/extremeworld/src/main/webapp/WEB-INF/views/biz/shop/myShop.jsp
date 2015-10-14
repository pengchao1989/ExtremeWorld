<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>店铺管理</title>
</head>
<body>
	<div class="container">
		<h2 class="header">店铺信息</h2>
		
		<div class="row">
			<div class="col s12 m6">
				<!-- Basic Card -->
				<div class="card blue-grey darken-1">
					<div class="card-content white-text">
						<span class="card-title">${shop.name}</span>
						<div class="row">
							<div class="col s1 m1">
								<p>地址</p>
							</div>
							<div class="col s11 m11">
								<p>${shop.address}</p>
							</div>
						</div>
						<div class="row">
							<div class="col s1 m1">
								<p>简介</p>
							</div>
							<div class="col s11 m11">
								<p>${shop.description}</p>
							</div>
						</div>
						
						<div class="row">
							<div class="col s1 m1">
								<p>权限</p>
							</div>
							<div class="col s11 m11">
								<p>
								<c:forEach items="${shop.hobbys}" var="hobby">
									${hobby.name}
								</c:forEach>
								</p>
								
							</div>
						</div>
						
					</div>
					<div class="card-action">
						<a href="#">编辑信息</a>
					</div>
				</div>
			</div>
			
		</div>
	</div>
	<br><br><br><br><br><br><br><br><br><br><br><br><br><br>
</body>
</html>