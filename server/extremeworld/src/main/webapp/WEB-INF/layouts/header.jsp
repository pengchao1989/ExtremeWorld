<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div class="navbar-fixed">
	<nav class="cyan">
	<div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">

			<!-- Dropdown Trigger -->
			<a class='navbar-brand dropdown-button ' href='#'
				data-activates='dropdown1'>极限学院</a>

			<!-- Dropdown Structure -->
			<ul id='dropdown1' class='dropdown-content'>
				<li><a href="${ctx}/all/">全部</a></li>
				<li class="divider"></li>
				<li><a href="${ctx}/skateboard/">滑板</a></li>
				<li><a href="${ctx}/parkour/">跑酷</a></li>
			</ul>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul id="nav_menu" class="nav navbar-nav">
				<li><a href="${ctx}/${hobby}">动态 <span class="sr-only">(current)</span></a></li>
				<li><a href="${ctx}/${hobby}/discuss">讨论</a></li>
				<li><a href="${ctx}/${hobby}/video">视频</a></li>
				<li><a href="${ctx}/${hobby}/course">教程</a></li>
				<li><a href="${ctx}/${hobby}/site">场地</a></li>
				<li><a href="${ctx}/${hobby}/news">新闻</a></li>
			</ul>


			<ul class="nav navbar-nav navbar-right">
				<li><a href="/me">我</a></li>
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-expanded="false">Dropdown
						<span class="caret"></span>
				</a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="#">Action</a></li>
						<li class="divider"></li>
						<li><a href="${ctx}/logout">logout</a></li>
					</ul></li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container-fluid -->
</nav>
</div>
