<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags"  prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
    <head>
        <meta charset="utf-8">
		<link rel="stylesheet" href="${pageContext.request.contextPath }/css/s.css">
		<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.10.2.min.js"></script>
		<style>
			.nav_2_but {
				float: right;
				height: 29px;
			}
		</style>
    </head>
    <body>
    <!-- header begin -->
    <div class="header">
    	<!-- logo -->
    	<div class="logo" style="font-size:24px;">
    		<img src="${pageContext.request.contextPath }/images/style/logo.jpg" height="50" width="50">
    		集装箱运输平台
    	</div>
    	<!-- sreach -->
    	<div class="sreach">
    		<input type="text" class="sreach_text">
    		<input type="button" class="sreach_but">
    	</div>
    </div>
    <!-- header over -->

    <!-- nav begin -->
    <div class="nav">
    	<ul>
    		<li><a href="${pageContext.request.contextPath }/ctsys.jsp">首页</a></li>
    		<li><a href="${pageContext.request.contextPath }/libctsys.jsp">算例库</a></li>
    		<li><a href="${pageContext.request.contextPath}/slk/compute_save.action">港口预约（未开发）</a></li>
    		<li><a href="${pageContext.request.contextPath}/slk/compute_save.action">在线学习（待定）</a></li>
    		<li><a href="${pageContext.request.contextPath}/slk/compute_save.action">我的空间（未开发）</a></li>
    	</ul>
    </div>
    <!-- nav over -->