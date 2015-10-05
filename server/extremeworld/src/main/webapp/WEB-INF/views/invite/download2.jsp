<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">

<link href="${ctx}/static/bootstrap/3/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="${ctx}/static/styles/magister.css">


	
<title>邀请好友</title>
</head>
<body class="theme-invert">

<!-- Main (Home) section -->
<section class="section" id="head">
	<div class="container">
	
		<h2 class="tagline text-center">扫描二维码或点击下载</h2>
		<div class="row">
			<div class="col-sm-12 text-center">    
				<p><img src="${appVersion.apkBarcodeUrl}" /></p>
				<br>
				<a class="btn btn-default btn-lg" href="${appVersion.apkUrl}" role="button">点击下载</a>
			</div>
			 
		</div>
	</div>
</section>


<script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctx}/static/bootstrap/3/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx}/static/scripts/modernizr.custom.72241.js"></script>
<!-- Custom template scripts -->
<script src="${ctx}/static/scripts/magister.js"></script>
</body>
</html>