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
			<input type="button" value="查找">
		</dt>
		<!-- 
			<dt>
				<a href="#" onclick="jump(1)">算例1</a>
			</dt>
			<dt onclick="jump('includeDetail')">算例2</dt>
			<dt>
				<a href="#" onclick="jump(1)">小规模算例</a>
			</dt> -->
		</dl>
	</div>
	<div class="m2" >
		<div class="m2topList" id="listDetial" >
			<table>
				<tr>
					<td>编号</td>
					<td>名称</td>
					<td>规模</td>
					<td>类型</td>
					<td>求解次数</td>
					<td>备注</td>
					<td>操作</td>
				</tr>
				<tr>
					<td>1</td>
					<td>算例1</td>
					<td>10任务</td>
					<td>基本接驳</td>
					<td id="qjcs">3</td>
					<td>单堆场、多港口</td>
					<td>删除<br/>详情</td>
				</tr>
				<s:if test="#session.exampleList !=null">
					<s:iterator var="e" value="#session.exampleList" status="st">
						<tr>
							<td>
								<s:property value="#e.eid" />
							</td>
							<td>
								<s:property value="#e.ename" />
							</td>
							<td>
								<s:property value="#e.tasks.size"/>
							</td>
							<td>
								<s:property value="#e.etype" />
							</td>
							<td>
								<s:property value="#e.etype" />
							</td>
							<td>
								<s:property value="#e.eremark" />
							</td>
							<td>
								<button href="#" onclick="jump(${e.eid})">
								详情
								</button>
								<br/>
								<button onclick="qjjl(${e.eid})">求解记录</button>
								<br/>
								<button onclick="doDel(${e.eid})">删除</button>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>nothing..</tr>
				</s:else>
			</table>		
		</div>
		<div class="m2bottomList"></div>
	</div>
	<div style="clear: both;"></div>
</div>
<br />
<!-- content over -->
<%@ include file="/common/footer.jsp"%>

<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.10.2.min.js"></script>
<script language="javascript" type="text/javascript">
	function jump(id) {
		$("#listDetial").load("example_detailUI.action?eid="+id, function() {
			$("#listDetial").fadeIn(100);
		});
	};
	function doDel(id) {
		var url="${pageContext.request.contextPath}/slk/example_delExample.action";
		$.get(url,{eid:id},function(){
			location.reload();
		});
	};
		
	
	function qjjl(id){
		$("#listDetial").load("example_computeRecordUI.action?eid="+id, function() {
			$("#listDetial").fadeIn(100);
		});
	}
	
	
	$("#qjcs").click(function(){
		
		$(".m2bottomList").load("${pageContext.request.contextPath}/slk/example_solveInfo.action");
	
	});
	
</script>
