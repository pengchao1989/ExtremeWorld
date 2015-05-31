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

<meta property="qc:admins" content="23561375476173314563757504255577244" />

<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
<link href="${ctx}/static/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />


<link rel="stylesheet" type="text/css" href="${ctx}/static/font-awesome-4.2.0/css/font-awesome.css" />

<link type="text/css" rel="stylesheet" href="${ctx}/static/materialize/css/materialize.css"  media="screen,projection"/> 

<link href="${ctx}/static/styles/default.css" type="text/css" rel="stylesheet" />

<body>
<div class="container">

	<form id="loginForm" action="${ctx}/login" method="post" class="form-signin">
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
		
		<h2 class="form-signin-heading">欢迎回来</h2>
		<input type="text" id="username" name="username"  value="${username}" class="form-control" placeholder="Email address" required autofocus/>
		<input type="password" id="password" name="password" class="form-control" placeholder="Password" required/>

				
<!--  			<div class="checkbox">
				<label class="checkbox" for="rememberMe"><input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我</label>
			</div> -->

			<p>
				<input type="checkbox" class="filled-in" id="filled-in-box" name="rememberMe" /> 
				<label for="filled-in-box">记住我</label>
			</p>
			
			
			<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>

			<br/>
			 <a class="btn btn-lg btn-primary btn-block" href="${ctx}/register">注册</a>
			 
			 <a href="${ctx}/qqlogin"><img alt="" src="${ctx}/static/images/qq_login_icon.jpg"></a>
			 <a href="${ctx}/qqlogin"><img alt="" src="${ctx}/static/images/icon48_wx_button.png"></a>
			
	</form>
	
	
	
<!--  		<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" data-appid="101189354" data-redirecturi="http://www.17skate.com/extremeworld/qqlogin" charset="utf-8" ></script>

		<span id="qqLoginBtn" ></span>

		<script type="text/javascript">
		 QC.Login({
		  btnId : "qqLoginBtn",//插入按钮的html标签id
		  size : "A_L",//按钮尺寸
		  scope : "get_user_info",//展示授权，全部可用授权可填 all
		  display : "pc"//应用场景，可选
		 });
		</script> -->
</div>


	<script>
		$(document).ready(function() {
			$("#loginForm").validate();
		});
	</script>
	
	<%-- <script src="${ctx}/static/materialize/js/materialize.min.js" type="text/javascript"></script> --%>
	
	
</body>
</html>
