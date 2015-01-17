<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="headerSection">
				<div class="container">
				
					<div class="span3 logo">
						<h1><a href="${ctx}">${name}</a></h1>
					</div>
					

					<div class="navbar">


						<div class="nav-collapse">
							<ul class="nav">
								<li><a href="topic">讨论</a></li>
								<li><a href="#shopAttention">视频</a></li>
								<li><a href="site">场地</a></li>
								<li><a href="#contactSection">Support</a></li>
							</ul>
						</div>

					</div>
				</div>
			</div>
<div id="header">
	<div id="title">
	    
			
			<shiro:user>
			<div class="btn-group pull-right">
				<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
					<i class="icon-user"></i> <shiro:principal property="name"/>
					<span class="caret"></span>
				</a>
			
				<ul class="dropdown-menu">
					<shiro:hasRole name="admin">
						<li><a href="${ctx}/admin/user">Admin Users</a></li>
						<li class="divider"></li>
					</shiro:hasRole>
					<li><a href="${ctx}/api">APIs</a></li>
					<li><a href="${ctx}/profile">Edit Profile</a></li>
					<li><a href="${ctx}/logout">Logout</a></li>
				</ul>
			</div>
		</shiro:user>
		</h1>
	</div>
</div>