<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/css/s.css">
<style>
.header {
	margin-top: 3%;
	margin-left: 36%;
}

.lc {
	margin-top: 50px;
}

.llc {
	text-align: center;
	font-size: medium;
	float: left;
	margin-left: 50px;
	width: 100px;
	border: 1px lightblue solid;
	padding: 20px;
}

.lc1 {
	margin-left: 130px
}
</style>
<script src="js/jquery/jquery-1.10.2.min.js"></script>
</head>
<body>

	<div class="header" >
		<!-- logo -->
		<div class="logo" >
			<img src="${pageContext.request.contextPath }/images/style/logo.jpg"
				height="50" width="50" > 集装箱运输平台 <label
				style="font-size: 70% ;">算例库</label>
		</div>
	</div>
	<!-- content begin -->
	<br />
	<div class="content">
		<div class="lc">

			<div class="lc1 llc">
				<a href="${pageContext.request.contextPath }/slk/add_createUI.action">创建</a>
			</div>
			<div class="lc2 llc">
				<a href="${pageContext.request.contextPath }/slk/example_listUI.action">查看</a>
			</div>
			<div class="lc3 llc">
				<a href="${pageContext.request.contextPath }/slk/example_computeUI.action">计算</a>
			</div>
			<div class="lc4 llc">
				<a href="${pageContext.request.contextPath }/sys/ctsyslib/list.jsp">分析</a>
			</div>
		</div>
	</div>
	<br />
	<!-- content over -->
</body>
</html>



<script type="text/javascript">
	$(".header,.content").fadeOut(500);
	$(".header,.content").fadeIn(2000);
	
	//$(".logo").fadeIn(2000);
	
</script>
