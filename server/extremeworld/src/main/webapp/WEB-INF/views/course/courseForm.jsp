<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>编辑教学</title>
</head>
<body>

	<div  class="container main_content">
		<form action="${ctx}/${hobby}/course/${action}" method="post" class="form-horizontal">
		
			<input type="hidden" name="id" value="${course.id}"/>
		
			<c:if test="${action == 'create'}">
				 <div class="form-group">
					<label for="inputEmail3" class="col-sm-2 control-label">分类</label>
					<div class="col-sm-10">
						<select class="form-control"  id="taxonomySelect" name="courseTaxonomyId" >
	
						<c:forEach items="${courseTaxonomyList}" var="courseTaxonomy">
							<option value=${courseTaxonomy.id}>${courseTaxonomy.name}</option>
						</c:forEach>
							
						</select>
					</div>
				</div> 
			</c:if>
		

		
			<div class="form-group">
			    <label for="inputEmail3" class="col-sm-2 control-label">名称</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="name"  name="name" placeholder="名称" value="${course.name}">
			    </div>
			  </div>
			  
            <div class="form-group">
                <label for="inputEmail3" class="col-sm-2 control-label">排序</label>
                <div class="col-sm-10">
                  <input type="text" class="form-control" id="sortWeight"  name="sortWeight" placeholder="排序" value="${course.sortWeight}">
                </div>
              </div>
			  
				<div class="form-group">
			    <label for="inputEmail3" class="col-sm-2 control-label">内容</label>
			    <div class="col-sm-10">
			      <textarea   class="form-control" rows="20" id="content" name="content" >${course.content}</textarea>
			    </div>
			  </div>

			<div class="form-group">
				<div class="col-md-2"></div>
				<div class="col-sm-10">
					<button type="submit" class="btn btn-primary btn-lg">发布</button>
				</div>

			</div>

		</form>
	</div>
	
	
	<script type="text/javascript">
		$(document).ready(function()
		{
			$("#courseTaxonomyId").val( $("#taxonomySelect").find("option:selected").text());
			
			$("#taxonomySelect").change(function(){
				$("#courseTaxonomyId").val( $("#taxonomySelect").find("option:selected").text());
			});
			
		});
		
		
	</script>

</body>
</html>