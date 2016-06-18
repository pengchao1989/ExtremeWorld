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

	
<title>邀请好友</title>

<style>
    body{left: 50%;top:50%; background-color: #2E2A31;}
    .whiteText{color: #F3EBEB;}
</style>


</head>
<body class="theme-invert">

<div class="container">

    <br/><br/><br/><br/><br/>

	<h2 class="tagline text-center whiteText">点击下载APK或跳转到iTunes</h2>
	<div class="row">
		<div class="col-sm-12 text-center">    
			<br>
			<a  id="btnDownload" class="btn btn-default btn-lg" href="${appVersion.apkUrl}" role="button">点击下载</a>
		</div>
	</div>
</div>


<script>
	var btn = document.getElementById('btnDownload');
	var isIOS = /(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent);
	if(isIOS) {
	    btn.href = 'https://itunes.apple.com/cn/app/posterlabs-stylish-collage/id1105467596?ls=1&mt=8';
	}else {
	    btn.href = '${appVersion.apkUrl}';
	}
	
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
		btn.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.yumfee.skate';
	});
	</script>   
</body>
</html>