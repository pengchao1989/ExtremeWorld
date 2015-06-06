<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<ul id='dropdown1' class='dropdown-content'>
	<li><a href="${ctx}/all/">全部</a></li>
	<li class="divider"></li>
	<li><a href="${ctx}/skateboard/">滑板</a></li>
	<li><a href="${ctx}/parkour/">跑酷</a></li>
</ul>
			
<div class="navbar-fixed">
	<nav class="cyan">
	<div class="nav-wrapper">

		<div class="" id="bs-example-navbar-collapse-1">
			<ul id="nav_menu" class="nav navbar-nav">
			
				<li><a class='brand-logo dropdown-button' href='#'data-activates='dropdown1'>极限学院</a></li>
				<li><a href="${ctx}/${hobby}">动态 <span class="sr-only">(current)</span></a></li>
				<li><a href="${ctx}/${hobby}/discuss">讨论</a></li>
				<li><a href="${ctx}/${hobby}/video">视频</a></li>
				<li><a href="${ctx}/${hobby}/course">教程</a></li>
				<li><a href="${ctx}/${hobby}/news">新闻</a></li>
			</ul>


			<ul class="right hide-on-med-and-down">
				<shiro:authenticated>  
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
							
							    <shiro:principal property="name"/>     
							
	
						<span class="caret"></span>
						</a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">Action</a></li>
							<li class="divider"></li>
							<li><a href="${ctx}/logout">logout</a></li>
						</ul>
					</li>
				</shiro:authenticated>
				

				
				<shiro:notAuthenticated>  
					<li>
						<a href="${ctx}/login">请登录</a>
					</li>
							
				</shiro:notAuthenticated>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container-fluid -->
</nav>
</div>
