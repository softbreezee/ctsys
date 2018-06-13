<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags"  prefix="s" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
    <head>
        <meta charset="utf-8">
		<link rel="stylesheet" href="${pageContext.request.contextPath }/css/s.css">
    </head>
    <body>
    <!-- header begin -->
    <div class="header">
    	<!-- logo -->
    	<div class="logo">
    		<img src="${pageContext.request.contextPath }/images/style/logo.jpg" height="50" width="50">
    		<span style="font-size:24px;">集装箱运输平台</span>  <label style="font-size: 70% ;">算例库</label>
    		&nbsp;&nbsp;
    		<span style="font-size:100%;font-family:'宋体';"><s:property value="#session.SYS_USER.name"/>，欢迎您！<span>
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
    		<li><s:a href="add_createUI.action">创建</s:a></li>
    		<li><s:a href="example_listUI.action">查看</s:a></li>
    		<li><s:a href="example_computeUI.action">计算</s:a></li>
    		<li><a href="${pageContext.request.contextPath }/sys/ctsyslib/analyse.jsp">分析(待开发)</a></li> 
    	</ul>
    </div>
    <!-- nav over -->