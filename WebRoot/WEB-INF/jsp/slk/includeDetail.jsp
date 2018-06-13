<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<style>
table {
	border: 1px solid lightblue;
	margin: 0 auto;
	border-collapse: collapse;
	/* width: 800px; */
}

td,th {
	border: 1px solid gray;
	text-align: center;
}

th {
	width: 90px;
}

td {
	width: 90px;
}
</style>

	<br />
<center>
	<button id="update" onclick="doUpdate('<s:property value='#request.example.eid'/>')">修改</button>
	<button id="delete" onclick="doDelete('<s:property value='#request.example.eid'/>')">删除</button>
	<button id="export" onclick="doExport('<s:property value='#request.example.eid'/>')">导出</button>
	<button id="backToListUI" onclick="doBack()">返回</button>
	<hr/>
	
	<table>
		<caption style="font-size: medium;font-weight: bold;">任务信息</caption>
		<tr>
			<th><input type="checkbox"></th>
			<th>编号</th>
			<th>求解方法</th>
			<th>求解时长</th>
			<th>目标值</th>
			<th>路径</th>
			<th>求解时间</th>
			<th>算法参数</th>
			<th>操作</th>
		</tr>
		<s:if test="#request.resultList!=null">
			<s:iterator var="result" value="#request.resultList" status="st">
				<tr>
					<td><input type="checkbox"></td>
					<td><s:property value="#st.count" /></td>
					<td><s:property value="#result.method" /></td>
					<td><s:property value="#result.resultTime" /></td>
					<td><s:property value="#result.objectValue" /></td>
					<td><s:property value="#result.route" /></td>
					<td><s:property value="#result.resultTimestamp" /></td>
					<td><s:property value="#result.paraset" /></td>
					<td><button onclick="ddelete(<s:property value='#result.rid'/>)">删除</button></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			nothing..
		</s:else>
	</table>
	
	
	<!-- 
	<table style="margin-top:10px;border-collapse: collapse;">
		<tr>
			<th>名称</th>
			<th>创建时间</th>
			<th>操作</th>
		</tr>
		<s:if test="#request.example !=null">
			<tr>
				<td><s:property value="#request.example.ename" /></td>
				<td><s:property value="#request.example.date" /></td>
				<td><a href="#">修改</a> <a href="list.html">返回</a></td>
			</tr>
		</s:if>
		<s:else>
			nothing..
		</s:else>
	</table>
 	-->
	<br />
	<br />
	<table>
		<caption style="font-size: medium;font-weight: bold;">任务信息</caption>

		<tr>
			<th>编号</th>
			<th>任务类型</th>
			<th>目标位置</th>
			<th>港口时间窗</th>
			<th>客户时间窗</th>
			<th>装/卸货时间</th>
		</tr>
		<s:if test="#request.example.tasks !=null">
			<s:iterator var="task" value="#request.example.tasks" status="st">
				<tr>
					<td><s:property value="#st.count" /></td>
					<td><s:property value="#task.taskType" /></td>
					<s:if test="#task.taskType=='IF'">
						<td><s:property value="#task.end.position" /></td>
					</s:if>
					<s:elseif test="#task.taskType=='OF'" >
						<td><s:property value="#task.start.position" /></td>
					</s:elseif>
					<s:elseif  test="#task.taskType == 'IE'">
						<td><s:property value="#task.start.position" /></td>
					</s:elseif>
					<s:else>
						<td><s:property value="#task.end.position" /></td>
					</s:else>
					<s:else>
						<td>-</td>
					</s:else>
					<s:if test="#task.tw1 !=null">
						<td><s:property value="#task.tw1" /></td>
					</s:if>
					<s:else>
						<td>-</td>
					</s:else>
					<s:if test="#task.tw2 !=null">
						<td><s:property value="#task.tw2" /></td>
					</s:if>
					<s:else>
						<td>-</td>
					</s:else>
					<td><s:property value="#task.loadTime" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			nothing..
		</s:else>
	</table>
	<br />

	<table>
		<caption style="font-size: medium;font-weight: bold;">地理位置</caption>

		<tr>
			<th>编号</th>
			<th>地点</th>
			<th>卡车数</th>
		</tr>
		<s:if test="#request.example.sites !=null">
			<s:iterator var="site" value="#request.example.sites" status="st">
				<tr>
					<td><s:property value="#st.count" /><s:property value="#site.type"/></td>
					<td><s:property value="#site.position" /></td>
					<s:if test="#site.carNum !=null">
						<td><s:property value="#site.carNum" /></td>
					</s:if>
					<s:else>
						<td>-</td>
					</s:else>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			nothing..
		</s:else>
	</table>
	<br/>

</center>
<s:property value='eid'/>
<script type="text/javascript">
	function doDelete(id){
		var url="${pageContext.request.contextPath}/slk/example_delExample.action";
		$.get(url,{eid:id},function(){
			location.reload();
		});
	};
	function ddelete(id){
		var url="${pageContext.request.contextPath}/slk/example_delResult.action";
		alert(id);
		$.get(url,{eid:id},function(){
			location.reload();
		});
	}
	function doBack(){
		location.reload();
	};


</script>
