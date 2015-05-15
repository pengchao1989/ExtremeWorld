<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>


</head>
<body>
	<div class="container main_content">
	
	<div class="row">
			<tbody>
			<c:forEach items="${sites.content}" var="site" varStatus="status">
			
						<div class=" col s3">
							<div class="card ">
							
								<div class="card-image">
					              <img src="${site.frontImg}">
					              <span class="card-title">${site.name}</span>
					            </div>
					            
					            <div class="card-content">
					              <p>${site.description}</p>
					            </div>
            
							</div>
						</div>
					
					<c:if test="${status.count%4==0}">
						</br>
					</c:if>
			</c:forEach>
		</tbody>
	</div>

		
<%-- 	<tags:pagination page="${sites}" paginationSize="5" /> --%>
		
	</div>
	<br/>

	<h3 id="json"></h3>

	<div>
		<button type="button" class="btn btn-info btn-lg ladda-button center-block" id="showmore"  onClick="showAlert()" title="显示更多前端代码回放" data-style="slide-down">
				<span class="ladda-label">
					${name}
				</span>
		</button>
	
	</div>


<script type="text/javascript">
    
    function showAlert()
    {
    	alert("loadmore button is click!!");
    }
    	
    $("#showmore").click(function(){
    	$('#json').load('http://localhost:8023/extremeworld/api/v1/topic');
    })

</script>
</body>
</html>