<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">

<link href="${ctx}/static/bootstrap/3/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="${ctx}/static/materialize/css/materialize.min.css"  media="screen,projection"/>

<title>分享</title>
</head>
<body>
<div class="container">

    <blockquote>
       <p class="flow-text">百城千县推广计划</p>
    </blockquote>
	
	<div class="card-panel">
	<p>滑板圈APP定位为一款原创视频及教学平台，为板友及相关滑板从业人员提供一个免费的属于我们自己的内容共享中心!</p>

	<br>
	<h6 class=" red-text">做人如果没有梦想，跟咸鱼有什么分别？</h6>
	</div>

    <blockquote>
       <p class="flow-text">我的邀请记录</p>
    </blockquote>
    
    <c:if test="${inviteUserList== null || fn:length(inviteUserList) == 0}">
     <p>暂时还没有邀请到好友.<br>滑板社区靠大家、推广滑板你我他</p>
    </c:if>
    
    <ul class="collection">
	    <c:forEach items="${inviteUserList}" var="user">
		    <li class="collection-item avatar">
		    <img class="circle" src="${user.avatar}!webAvatarSmall" alt="...">
		    <p>${user.name}</p>
		    <p><fmt:formatDate value="${user.registerDate}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
		    </li>
	    </c:forEach>
    </ul>
    
    <blockquote>
       <p class="flow-text">至板友们</p>
    </blockquote>
    <div class="card-panel">
    <p class="blue-text">滑板圈APP又和大家见面了，就如大家想的一样，来到这里的朋友都是因为滑板，而作为程序dog的我们始终心系滑板，想为滑板圈子做点什么，就像很多人心里有个开滑板店的梦想！然而我们选择了一件更难的事，那就是为滑板做一款APP，幸运的是滑板圈APP在没有任何投资人和品牌商的支持下居然给憋出来了，所以能如此轻装上阵我们可以在混乱的滑板圈子里保持初心，为滑板的推广与传播做上力所能及的贡献！
    </p>
    </div>
    
    <br>
    <br>
    <br>
    <br>
 
</div>
</body>
</html>