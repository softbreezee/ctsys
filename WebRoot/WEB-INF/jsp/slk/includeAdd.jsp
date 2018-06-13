<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<s:property value="example" />
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
	<s:if test="example.tasks !=null">
		<s:iterator var="task" value="example.tasks" status="st">
			<tr>
				<td><s:property value="#st.count" /></td>
				<td><s:property value="#task.taskType" /></td>
				<s:if test="#task.taskType=='IF'">
					<td><s:property value="#task.end.position" /></td>
				</s:if>
				<s:elseif test="#task.taskType=='OF'">
					<td><s:property value="#task.start.position" /></td>
				</s:elseif>
				<s:elseif test="#task.taskType == 'IE'">
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
				<td><s:property value="#st.count" /></td>
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