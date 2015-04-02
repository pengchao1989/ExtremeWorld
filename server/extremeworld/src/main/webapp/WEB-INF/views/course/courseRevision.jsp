<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<script type="text/javascript" src="${ctx}/static/mergely-3.3.9/lib/codemirror.js"></script>
	<link type="text/css" rel="stylesheet" href="${ctx}/static/mergely-3.3.9/lib/codemirror.css" />
	
		<!-- Requires Mergely -->
	<script type="text/javascript" src="${ctx}/static/mergely-3.3.9/lib/mergely.js"></script>
	<link type="text/css" rel="stylesheet" href="${ctx}/static/mergely-3.3.9/lib/mergely.css" />
	
<title>Insert title here</title>
</head>
<body>
	<div class="container main_content">
		<div id="mergely-resizer">
			<div id="compare">
			</div>
		</div>
		
	</div>
	
	
		<script type="text/javascript">

        $(document).ready(function () {
        	
			$('#compare').mergely({
				width: 'auto',
				height: 200,
				cmsettings: {
					readOnly: true, 
					lineWrapping: true,
				},lhs: function(setValue) {
					setValue("${preversion.content}");
				},
				rhs: function(setValue) {
					setValue("${version.content}");
				}
			});
			
			
		});
	</script>
</body>
</html>