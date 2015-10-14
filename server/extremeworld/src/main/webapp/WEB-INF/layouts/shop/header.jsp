<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<header id="header" class="page-topbar">
	<!-- start header nav-->
	<div class="navbar-fixed">
		<nav class="cyan">
			<div class="nav-wrapper">

				<a href="${ctx}/shop/" class="brand-logo">商家管理后台</a>

				<ul class="right hide-on-med-and-down">
					<li><a href="javascript:void(0);"
						class="waves-effect waves-block waves-light toggle-fullscreen"><i
							class="mdi-action-settings-overscan"></i></a></li>
					<li><a href="javascript:void(0);"
						class="waves-effect waves-block waves-light"><i
							class="mdi-navigation-apps"></i></a></li>
					<li><a href="javascript:void(0);"
						class="waves-effect waves-block waves-light"><i
							class="mdi-social-notifications"></i></a></li>
					<li><a href="#" data-activates="chat-out"
						class="waves-effect waves-block waves-light chat-collapse"><i
							class="mdi-communication-chat"></i></a></li>
				</ul>
			</div>
		</nav>
	</div>
	<!-- end header nav-->
	<!-- START LEFT SIDEBAR NAV-->
	<aside id="left-sidebar-nav">
		<ul id="slide-out" class="side-nav fixed leftside-navigation">
			<li class="no-padding">
				<ul class="collapsible collapsible-accordion">
					<li class="bold"><a
						class="collapsible-header waves-effect waves-cyan"><i
							class="mdi-action-shop"></i>店铺管理</a>
						<div class="collapsible-body">
							<ul>
								<li><a href="${ctx}/shop/">基本资料</a></li>
							</ul>
						</div></li>
					<li class="bold"><a
						class="collapsible-header  waves-effect waves-cyan"><i
							class="mdi-action-subject"></i>商品管理</a>
						<div class="collapsible-body">
							<ul>
								<li><a href="${ctx}/shop/goods">商品列表</a></li>
								<li><a href="${ctx}/shop/goods/create">发布商品</a></li>
							</ul>
						</div></li>

				</ul>
			</li>
			<li class="li-hover"><div class="divider"></div></li>
			<li class="li-hover"><p class="ultra-small margin more-text">MORE</p></li>
			<li><a href="css-grid.html"><i class="mdi-image-grid-on"></i>
					Grid</a></li>
			<li><a href="css-color.html"><i
					class="mdi-editor-format-color-fill"></i> Color</a></li>
			<li><a href="css-helpers.html"><i
					class="mdi-communication-live-help"></i> Helpers</a></li>
			<li><a href="changelogs.html"><i
					class="mdi-action-swap-vert-circle"></i> Changelogs</a></li>
			<li class="li-hover"><div class="divider"></div></li>
			<li class="li-hover"><p class="ultra-small margin more-text">Daily
					Sales</p></li>
			<li class="li-hover">
				<div class="row">
					<div class="col s12 m12 l12">
						<div class="sample-chart-wrapper">
							<div class="ct-chart ct-golden-section" id="ct2-chart"></div>
						</div>
					</div>
				</div>
			</li>
		</ul>
		<a href="#" data-activates="slide-out"
			class="sidebar-collapse btn-floating btn-medium waves-effect waves-light hide-on-large-only cyan"><i
			class="mdi-navigation-menu"></i></a>
	</aside>
	<!-- END LEFT SIDEBAR NAV-->
</header>