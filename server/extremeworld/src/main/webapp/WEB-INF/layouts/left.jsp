<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="${ctx}/">极限学院</a>
    </div>
    
<ul id="nav-mobile" class="side-nav fixed" style="width: 240px;">
<!--         <li class="logo"><a id="logo-container" href="http://materializecss.com/" class="brand-logo">
            <object id="front-page-logo" type="image/svg+xml" data="res/materialize.svg">Your browser does not support SVG</object></a></li> -->
        
        
        <li class="bold"><a href="${ctx}/" class="waves-effect waves-teal">极限学院</a></li>
        <li class="bold"><a href="${ctx}/?hobby=1" class="waves-effect waves-teal">滑板</a></li>
        <li class="bold"><a href="${ctx}/?hobby=2" class="waves-effect waves-teal">跑酷</a></li>
        <li class="bold"><a href="${ctx}/?hobby=3" class="waves-effect waves-teal">单车</a></li>
        <li class="bold"><a href="${ctx}/?hobby=4" class="waves-effect waves-teal">轮滑</a></li>
      </ul>