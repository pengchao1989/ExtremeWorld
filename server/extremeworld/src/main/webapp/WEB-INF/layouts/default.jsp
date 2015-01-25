<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title>ExtremeWorld:<sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta property="qc:admins" content="23561375476173314563757504255577244" />

<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
<link href="${ctx}/static/bootstrap/3/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/styles/default.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/static/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation/1.11.1/messages_bs_zh.js" type="text/javascript"></script>


<link rel="stylesheet" type="text/css" href="${ctx}/static/font-awesome-4.2.0/css/font-awesome.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/simditor-2.0.4/styles/simditor.css" />

<!-- ace -->
<link rel="stylesheet" type="text/css" href="${ctx}/static/ace/assets/css/ace-fonts.css" >
<link rel="stylesheet" href="${ctx}/static/ace/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />


<sitemesh:head/>
</head>

<body>
	<div class="main-container" id="main-container">
		<%@ include file="/WEB-INF/layouts/header.jsp"%>
		<div id="content">
			<sitemesh:body/>
		</div>
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
	<script src="${ctx}/static/bootstrap/3/js/bootstrap.min.js" type="text/javascript"></script>
	
</body>
</html>