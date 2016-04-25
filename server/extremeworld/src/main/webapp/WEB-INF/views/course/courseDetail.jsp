<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>${course.name}</title>
</head>
<body>
	<div class="container main_content">

		<div class="jumbotron">
			<div class="row">
				<div class="col-sm-10">
					<h1>${course.name}</h1>
					
					<shiro:authenticated> 
					<div class="row"> 
                        <a class="btn btn-success "
                            href="${ctx}/${hobby}/course/update/${course.id}" role="button">编辑</a>
                        <a class="btn btn-success "
                            href="${ctx}/course/update/${course.id}" role="button">上传教程</a>
					</div>
					</shiro:authenticated>

				</div>
			</div>

			<label>最后由</label><a href="${ctx}/u/${course.user.id}"
				target="_blank">${course.user.name}</a><label>于${course.modifyTime}编辑</label><a
				href="${ctx}/${hobby}/course/revision/${course.id}"> 历史</a>

			<p id="course_content">${course.content}</p>
		</div>



		<h2>讨论与教学</h2>

		<ul class="tabs">
		      <li class="tab col s3"><a class="active" href="#sb">nike sb</a></li>
			<li class="tab col s3"><a id="picture-li" href="#explain">教学</a></li>
		</ul>
		
        <div id="sb" class="row s12">
            <c:forEach items="${sbs.content}" var="video" varStatus="status">
                <div class=" col l3 s12">
                    <div class="card ">
                        <div class="card-image video_image">
                            <a href="${ctx}/${hobby}/video/${video.id}" target="_blank">
                                <img alt="" src="${video.videoDetail.thumbnail}!webVideoListThum">
                            </a>
                        </div>
                        
                        <div class="card-content">
                            <span class=" activator grey-text text-darken-3">${video.title}</span>
                            <p><a href="${ctx}/u/${video.user.id}">${video.user.name}</a></p>
                        </div>
                    </div>
                
                </div>
                <c:if test="${status.count%4==0}">
                    </br>
                </c:if>
                    
                
            </c:forEach>
            
            <div id="more-sb-container"></div>
            
                <a id="load-more-sb" class="waves-effect waves-light btn-large">加载更多</a>

            
        </div>

		<!-- explain fragment -->
		<div id="explain" class="row s12">
			<ul class="collection">
				<c:forEach items="${explains.content}" var="explain">
					<li class="collection-item avatar">
						<a href="${ctx}/u/${explain.user.id}"> <img class="circle" src="${explain.user.avatar}!webAvatarSmall" alt="..."></a>
								
						<a href="${ctx}/${hobby}/topic/${explain.id}" target="_blank"> <span class="title">${explain.title}</span></a>
						<p><a href="${ctx}/u/${explain.user.id}">${question.user.name}</a><br></p>
					</li>
				</c:forEach>
			</ul>
			
			<div id="more-explain-container"></div>
			
			<a id="load-more-explain" class="waves-effect waves-light btn-large">加载更多</a>
		</div>


	</div>
	
	<script type="text/javascript">
	
		function TransferString(content)  
		{  
		    var string = content;  
		    try{  
		        string=string.replace(/\r\n/g,"<br/>")  
		        string=string.replace(/\n/g,"<br/>");  
		    }catch(e) {  
		        alert(e.message);  
		    }  
		    return string;  
		}  
	
	
		$(document).ready(function(){
			$("#course_content").html(TransferString($("#course_content").text()));
			
			/* -------------------- */
			
		    $('ul.tabs').tabs();
		    
			var currentSbPage = 1;
		    var currentCoursePage = 1;
		    
	          $("#load-more-sb").click(function(){
	                $.get("${ctx}/${hobby}/course/loadmore/${sb.id}?magicType=sb&page=" + (++currentQuestionPage),function(data){
	                    $("#more-sb-container").append(data);
	                }) ;
	            });

			
			$("#more-explain-container").click(function(){
				$.get("${ctx}/${hobby}/course/loadCourse/${course.id}?page=" + (currentCoursePage),function(data){
					$("#more-explain-container").append(data);
				}) ;
			});
			
			
			
			
		});
	</script>
</body>
</html>