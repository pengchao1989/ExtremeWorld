<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>发布视频</title>
<link rel="stylesheet" href="${ctx}/static/qiniu/demo/main.css">
<link rel="stylesheet"
    href="${ctx}/static/qiniu/demo/js/highlight/highlight.css">

</head>
<body>
    <div class="container main_content">
    
    <div class="jumbotron">
        <h3>为${course.name}添加教程</h3>
    </div>

        <input type="hidden" id="domain" value="http://video.jixianxueyuan.com/"> 
        <input type="hidden" id="uptoken_url" value="${ctx}/api/v1/uptoken/video">

        

        <form id="inputForm" action="${ctx}/${hobby}/course/${action}/${course.id}" method="post" class="form-horizontal">

            <input type="hidden" class="form-control" id="video_videosource" name="videoSource"> 
            <input type="hidden" class="form-control" id="front_source" name="frontSource">

            <div class="form-group">
                <label for="inputEmail3" class="col-sm-2 control-label">标题</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="video_title" name="title" placeholder="标题">
                </div>
            </div>

            <div class="form-group">
                <label for="inputEmail3" class="col-sm-2 control-label">描述</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="video_content" name="content" placeholder="描述">
                </div>
            </div>

            <div class="form-group">
                <label for="inputEmail3" class="col-sm-2 control-label">视频</label>

                <div class="col-md-10">
                    <div id="container">
                        <a class="btn btn-default btn-lg " id="pickfiles" href="#"> <i
                            class="glyphicon glyphicon-plus"></i> <sapn>选择文件</sapn>
                        </a>
                    </div>
                </div>

            </div>

            <div class="form-group">
                <div class="col-md-2"></div>
                <div class="col-sm-10">
                    <button id="video_submit" type="submit"
                        class="btn btn-primary btn-lg disabled">发布</button>
                </div>

            </div>

        </form>

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
        src="${ctx}/static/qiniu/demo/js/main.js"></script>
</body>
</html>