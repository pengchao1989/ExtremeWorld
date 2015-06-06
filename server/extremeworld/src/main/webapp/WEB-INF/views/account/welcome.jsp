<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

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
			<div class="col s12 m5 l5">
				<h3>公约</h3>
				<ul>
					<p>用户以各种方式使用极限学院服务和数据（包括但不限于发表、宣传介绍、转载、浏览及利用极限学院或极限学院用户发布内容）的过程中，不得以任何方式利用极限学院直接或间接从事违反中国法律、以及社会公德的行为，且用户应当恪守下述承诺：</p>
					<li>1. 发布、转载或提供的内容符合中国法律、社会公德；</li>
					<li>2. 不得干扰、损害和侵犯极限学院的各种合法权利与利益；</li>
					<li>3. 遵守极限学院以及与之相关的网络服务的协议、指导原则、管理细则等；</li>

					<p>极限学院有权对违反上述承诺的内容予以删除。</p>
				
				</ul>
			</div>
		
			<div class="col s12 m6 l6">
				<h3>个人信息</h3>
			
				<form action="${ctx}/qqlogin/${action}" method="post" class="form-welcome">
			
					<input type="hidden" name="id" value="${user.id}"/>
				
					
					<div class="input-field col s12">
					     <input type="text" class="validate" id="name"  name="name"  value="${user.name}">
					     <label for="name">昵称</label>
					</div>
					
					<div class="input-field col s12">
						<select name="gender">
					      <option value="保密" >您的性别</option>
					      <option value="男">男</option>
					      <option value="女">女</option>
					      <option value="保密">保密</option>
					    </select>
					</div>
					
					<div class="input-field col s12">
					     <input type="text" class="validate" id="birth"  name="birth"  value="1989">
					     <label for="birth">出生年份</label>
					</div>
		
					<div class="form-group">
						<div class="col s12">
							<button type="submit" class="btn waves-effect waves-light">走你 <i class="mdi-content-send right"></i></button>
						</div>
		
					</div>
				</form>
			</div>
			
		</div>
		
		<div style="padding-bottom:360px"/>
		
		
	</div>
	
	<script type="text/javascript">
	  $(document).ready(function() {
		    $('select').material_select();
		  });
	</script>
</body>
	
</html>