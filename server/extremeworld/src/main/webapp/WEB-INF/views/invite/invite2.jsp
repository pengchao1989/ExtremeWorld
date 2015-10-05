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

		<div class="row">
			<div class="col-md-10 col-lg-10 col-md-offset-1 col-lg-offset-1 text-center">	

				<!-- Site Title, your name, HELLO msg, etc. -->
				<h1 class="title">滑板圈</h1>
				<h2 class="subtitle">专业的原创视频与教学分享平台</h2>

				<!-- Short introductory (optional) -->
				<h3 class="tagline">
					此次重新开发了滑板圈应用，增加了短视频功能，以及将会开启弹幕视频功能，软件完整性有了很大的改善.<br>
				</h3>
				
				<h3 class="tagline">${inviter.name}邀请你加入</h3>>
				<h3 class="tagline">Cookie=${cookie.inviteid.value}</h3>>
				
				<a class="btn btn-default btn-lg" href="${ctx}/qqlogin?inviteid=${inviter.id}" role="button">立即加入</a>
	
			</div> <!-- /col -->
		</div> <!-- /row -->
	
	</div>
</section>

<!-- Second (About) section -->
<section class="section" id="about">
	<div class="container">
	
		<h2 class="text-center title">About me</h2>
		<div class="row">
			<div class="col-sm-4 col-sm-offset-2">    
				<h5><strong>Where's my lorem ipsum?<br></strong></h5>
				<p>Well, here it is: Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorum, ullam, ducimus, eaque, ex autem est dolore illo similique quasi unde sint rerum magnam quod amet iste dolorem ad laudantium molestias enim quibusdam inventore totam fugit eum iusto ratione alias deleniti suscipit modi quis nostrum veniam fugiat debitis officiis impedit ipsum natus ipsa. Doloremque, id, at, corporis, libero laborum architecto mollitia molestiae maxime aut deserunt sed perspiciatis quibusdam praesentium consectetur in sint impedit voluptates! Deleniti, sequi voluptate recusandae facere nostrum?</p>    
			</div>
			<div class="col-sm-4">
				<h5><strong>More, more lipsum!<br></strong></h5>    
				<p>Tempore, eos, voluptatem minus commodi error aut eaque neque consequuntur optio nesciunt quod quibusdam. Ipsum, voluptatibus, totam, modi perspiciatis repudiandae odio ad possimus molestias culpa optio eaque itaque dicta quod cupiditate reiciendis illo illum aspernatur ducimus praesentium quae porro alias repellat quasi cum fugiat accusamus molestiae exercitationem amet fugit sint eligendi omnis adipisci corrupti. Aspernatur.</p>    
				<h5><strong>Author links<br></strong></h5>    
				<p><a href="http://be.net/pozhilov9409">Behance</a> / <a href="https://twitter.com/serggg">Twitter</a> / <a href="http://linkedin.com/pozhilov">LinkedIn</a> / <a href="https://www.facebook.com/pozhilov">Facebook</a></p>
			</div>
		</div>
	</div>
</section>

<!-- Third (Works) section -->
<section class="section" id="themes">
	<div class="container">
	
		<h2 class="text-center title">More Themes</h2>
		<p class="lead text-center">
			Huge thank you to all people who publish<br>
			their photos at <a href="http://unsplash.com">Unsplash</a>, thank you guys!
		</p>
		<div class="row">
			<div class="col-sm-4 col-sm-offset-2">
				<div class="thumbnail">
					<img src="assets/screenshots/sshot1.jpg" alt="">
					<div class="caption">
						<h3>Thumbnail label</h3>
						<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Neque doloribus enim vitae nam cupiditate eius at explicabo eaque facere iste.</p>
						<p><a href="#" class="btn btn-primary" role="button">Button</a> <a href="#" class="btn btn-default" role="button">Button</a></p>
					</div>
				</div>
			</div>
			<div class="col-sm-4">
				<div class="thumbnail">
					<img src="assets/screenshots/sshot4.jpg" alt="">
					<div class="caption">
						<h3>Thumbnail label</h3>
						<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Neque doloribus enim vitae nam cupiditate eius at explicabo eaque facere iste.</p>
						<p><a href="#" class="btn btn-primary" role="button">Button</a> <a href="#" class="btn btn-default" role="button">Button</a></p>
					</div>
				</div>
			</div>
			<div class="col-sm-4 col-sm-offset-2">
				<div class="thumbnail">
					<img src="assets/screenshots/sshot5.jpg" alt="">
					<div class="caption">
						<h3>Thumbnail label</h3>
						<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Neque doloribus enim vitae nam cupiditate eius at explicabo eaque facere iste.</p>
						<p><a href="#" class="btn btn-primary" role="button">Button</a> <a href="#" class="btn btn-default" role="button">Button</a></p>
					</div>
				</div>
			</div>
			<div class="col-sm-4">
				<div class="thumbnail">
					<img src="assets/screenshots/sshot3.jpg" alt="">
					<div class="caption">
						<h3>Thumbnail label</h3>
						<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Neque doloribus enim vitae nam cupiditate eius at explicabo eaque facere iste.</p>
						<p><a href="#" class="btn btn-primary" role="button">Button</a> <a href="#" class="btn btn-default" role="button">Button</a></p>
					</div>
				</div>
			</div>

		</div>

	</div>
</section>

<!-- Fourth (Contact) section -->
<section class="section" id="contact">
	<div class="container">
	
		<h2 class="text-center title">Get in touch</h2>

		<div class="row">
			<div class="col-sm-8 col-sm-offset-2 text-center">
				<p class="lead">Have a question about this template, or want to suggest a new feature?</p>
				<p>Feel free to email me, or drop me a line in Twitter!</p>
				<p><b>gt@gettemplate.com</b><br><br></p>
				<ul class="list-inline list-social">
					<li><a href="https://twitter.com/serggg" class="btn btn-link"><i class="fa fa-twitter fa-fw"></i> Twitter</a></li>
					<li><a href="https://github.com/pozhilov" class="btn btn-link"><i class="fa fa-github fa-fw"></i> Github</a></li>
					<li><a href="http://linkedin/in/pozhilov" class="btn btn-link"><i class="fa fa-linkedin fa-fw"></i> LinkedIn</a></li>
				</ul>
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