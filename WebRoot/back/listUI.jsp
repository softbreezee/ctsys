<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<style>
.m1 {
	border: solid 2px gray;
	border-right:hidden;
	height:500px;
	width: 19%;
	float: left;
	overflow: scroll;
	overflow-y:auto;
	overflow-x:auto;
		
}

.m2 {
	border: solid 2px gray;
	width: 80%;
	height:500px;
	float: left;
	overflow: scroll;
	overflow-y:auto;
	overflow-x:auto;	
}
.m1 dt {
	padding: 10px 15px;
}

th,td {
	padding: 10px;
	border: 1px lightblue solid;
}
fieldset{
	border-radius: 5px;
	border:1px gray solid;
}
</style>
<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.10.2.min.js"></script>
<script language="javascript" type="text/javascript">
	function jump(id) {
		$("#listDetial").load("example_detailUI.action?eid="+id, function() {
			$("#listDetial").fadeIn(100);
		});
	};
	
</script>
<!-- 引入算例库头部文件 -->
<%@ include file="/common/libraryheader.jsp"%>

<!-- content begin -->

<br />
<div class="content">
	<div class="m1">
		<dl>
		<dt>
			<fieldset>
			<legend>算例规模</legend>
			<input type="radio">20任务以上
			<input type="radio">20任务以下
			</fieldset>
		</dt>
		<dt>
			<fieldset>
			<legend>港口</legend>
			<input type="radio">单港口<input type="radio">多港口
			</fieldset>
		</dt>
		<dt>
			<fieldset>
			<legend>堆场</legend>
			<input type="radio">单堆场<input type="radio">多堆场
			</fieldset>
		</dt>
		<dt>			
			<fieldset>
			<legend>集装箱尺寸</legend>
			<input type="radio">单尺寸<input type="radio">多尺寸
			</fieldset>
		</dt>
		<dt>
			<fieldset>
			<legend>资源约束</legend>
			<input type="checkbox">空箱数量约束
			<input type="checkbox">集卡数量约束
			</fieldset>
		</dt>
		<dt>
			<button>查找</button>
		</dt>
		<!-- 
			<dt>
				<a href="#" onclick="jump(1)">算例1</a>
			</dt>
			<dt onclick="jump('includeDetail')">算例2</dt>
			<dt>
				<a href="#" onclick="jump(1)">小规模算例</a>
			</dt> -->
			<s:if test="#session.exampleList !=null">
				<s:iterator var="e" value="#session.exampleList" status="st">
					<dt>
						<a href="#" onclick="jump(${e.eid})"> <s:property value="#e.ename" />
						</a>
					</dt>
				</s:iterator>
			</s:if>
			<s:else>
				<dt>nothing..</dt>
			</s:else>

		</dl>

	</div>
	<div class="m2" >
		<div class="m21"></div>
		<div class="m22" id="listDetial"></div>
	
	</div>
	<div style="clear: both;"></div>
</div>
<br />
<!-- content over -->
<%@ include file="/common/footer.jsp"%>
