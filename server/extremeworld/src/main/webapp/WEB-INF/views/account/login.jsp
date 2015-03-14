<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>登录页</title>
</head>

<body>
<div class="container">

	<form  action="${ctx}/login" method="post" class="form-signin">
		<%
		String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if(error != null){
		%>
			<div class="alert alert-error input-medium controls">
				<button class="close" data-dismiss="alert">×</button>登录失败，请重试.
			</div>
		<%
		}
		%>
		
		<h2 class="form-signin-heading">Please sign in</h2>
		<label for="inputEmail" class="sr-only">Email address</label>
		<input type="text" id="username" name="username"  value="${username}" class="form-control" placeholder="Email address" required autofocus/>
		<label for="inputPassword" class="sr-only">Password</label>
		<input type="password" id="password" name="password" class="form-control" placeholder="Password" required/>

				
			<div class="checkbox">
				<label class="checkbox" for="rememberMe"><input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我</label>
			</div>
			
			 <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
			 <a class="btn" href="${ctx}/register">注册</a>
			
	</form>
</div>


	<script>
		$(document).ready(function() {
			$("#loginForm").validate();
		});
	</script>
</body>
</html>
