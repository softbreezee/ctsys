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
	<button id="backToListUI" onclick="doBack()">返回</button>
	<hr/>
	<br />
	<br />
	<table>
		<caption style="font-size: medium;font-weight: bold;">任务信息</caption>

		<tr>
			<th>编号</th>
			<th>求解方法</th>
			<th>求解时长</th>
			<th>目标值</th>
			<th>路径</th>
			<th>求解时间</th>
			<th>算法参数</th>
		</tr>
		<s:if test="#request.resultList!=null">
			<s:iterator var="result" value="#request.resultList" status="st">
				<tr>
					<td><s:property value="#st.count" /></td>
					<td><s:property value="#result.method" /></td>
					<td><s:property value="#result.resultTime" /></td>
					<td><s:property value="#result.objectValue" /></td>
					<td><s:property value="#result.route" /></td>
					<td><s:property value="#result.resultTimestamp" /></td>
					<td><s:property value="#result.paraset" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			nothing..
		</s:else>
	</table>
	<br />

</center>
<s:property value='eid'/>
