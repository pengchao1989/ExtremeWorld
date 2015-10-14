<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增商品</title>
<link rel="stylesheet" href="${ctx}/static/qiniu/demo/main.css">
<link rel="stylesheet"
	href="${ctx}/static/qiniu/demo/js/highlight/highlight.css">
</head>
<body>
	<div class="container">

		<input type="hidden" id="domain" value="http://img.jixianxueyuan.com/">
		<input type="hidden" id="uptoken_url"
			value="${ctx}/api/v1/uptoken/picture">

		<div class="row">
			<div class="card-panel">
				<h4 class="header2">发布商品</h4>
				<div class="row">
					<form class="col s12" action="${ctx}/shop/goods/${action}" method="post" >
						<input type="hidden" class="form-control" id="picture_url" name="cover">
						<div class="row">
							<div class="input-field col s12">
								<input id="name" type="text" name="name"> <label for="first_name" class="">标题</label>
							</div>
						</div>
						<div class="row">
							<div class="input-field col s12">
								<input id="price" type=text name="startPrice"> <label for="text" class="">售价</label>
							</div>
						</div>

						<div class="row">
							<div class="input-field col s12">
								<input id="price" type=text name="des"> <label for="text" class="">一句话描述</label>
							</div>
						</div>
						
						<div class="row">
							<div class="input-field col s12">
								<input id="price" type=text name="taobaoId"> <label for="text" class="">淘宝id</label>
							</div>
						</div>

						<div class="row">
							<div class="input-field col s12">
								<select class="browser-default" name="categoryId">
									<option value="" selected>选择分类</option>
									<c:forEach items="${categoryList}" var="category">
										<option value="${category.id}">${category.name}</option>
									</c:forEach>
								</select>
							</div>

						</div>


						<div class="row">
							<div class="col-md-10">
								<div id="container">
									<a class="btn btn-default btn-lg " id="pickfiles" href="#">
										<i class="glyphicon glyphicon-plus"></i> <sapn>商品图标</sapn>
									</a>
								</div>
							</div>

						</div>

						<div style="display: none" id="success" class="col-md-12">
							<div class="alert-success">队列全部文件处理完毕</div>
						</div>
						<div class="col-md-12 ">
							<table class="table table-striped table-hover text-left"
								style="margin-top: 40px; display: none">
								<thead>
									<tr>
										<th class="col-md-4">Filename</th>
										<th class="col-md-2">Size</th>
										<th class="col-md-6">Detail</th>
									</tr>
								</thead>
								<tbody id="fsUploadProgress">
								</tbody>
							</table>
						</div>


						<div class="row">
							<div class="input-field col s12">
								<button id="submit" class="btn btn-primary btn-lg disabled"
									type="submit" name="action">发布</button>
							</div>
						</div>

					</form>
				</div>
			</div>
		</div>


	</div>

	<script type="text/javascript"
		src="${ctx}/static/plupload/plupload.full.min.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/qiniu/demo/js/plupload/i18n/zh_CN.js"></script>
	<script type="text/javascript" src="${ctx}/static/qiniu/demo/js/ui.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/qiniu/demo/js/qiniu.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/qiniu/demo/js/highlight/highlight.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/qiniu/demo/js/uploadPicture.js"></script>
</body>
</html>
