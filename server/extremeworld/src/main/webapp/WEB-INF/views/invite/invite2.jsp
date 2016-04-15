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


	
<title>${inviter.name}邀请你加入滑板圈</title>

<style>
    body { -webkit-tap-highlight-color: rgba(0, 0, 0, 0); -webkit-touch-callout: none;background-color: #231134;}
    body,div{padding: 0; margin: 0;}img{display: block;width: 100%;}.content{padding-bottom: 50px;}
    .download{position: fixed;height: 60px; width: 100%; bottom: 0;left: 0;background:rgba(15,8,32,.9);}
    .btn{width: 200px;height: 35px;line-height: 35px; color: #fff; text-decoration: none;text-align: center; font-size: 14px; position: absolute;background-color: #00a8ff; border-radius: 5px;left: 50%;top:50%;margin: -16px 0 0 -100px;}
</style>

</head>


<body class="theme-invert">


<div class="content" id="content"><img src="http://download.jixianxueyuan.com/1.png" alt=""></div>
<div class="download">
    <a id="btnDownload" href="${ctx}/qqlogin?inviteid=${inviter.id}" class="btn">${inviter.name}邀请你加入(戳我)</a>
</div>


</body>
</html>