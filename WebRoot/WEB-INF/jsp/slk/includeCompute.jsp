<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<s:property value="result" />

<div style="margin-left:50px;">
<form id="incomui">
	<table >
		<tr>
			<td>算例</td>
			<td  style="width:600px;">
				<s:property value="result.example.ename"/>
				<input type="hidden" name="result.example.ename" value="<s:property value='result.example.ename'/>" />
				<input type="hidden" name="result.example.eid" value="<s:property value='result.example.eid'/>" />
			</td>
		</tr>
		<tr>
			<td>求解方法</td>
			<td>
				<s:property value="result.method"/>
				<input type="hidden" name="result.method" value="<s:property value='result.method'/>" />
			</td>
		</tr>
		<tr>
			<td>目标值</td>
			<td>
				<s:property value="result.objectValue"/>
				<input type="hidden" name="result.objectValue" value="<s:property value='result.objectValue'/>" />
			</td>
		</tr>
		<tr>
			<td>路径</td>
			<td>
				<s:property value="result.route"/>
				<input type="hidden" name="result.route" value="<s:property value='result.route'/>" />
			</td>
		</tr>
		<tr>
			<td>求解时长</td>
			<td>
				<s:property value="result.resultTime"/>
				<input type="hidden" name="result.resultTime" value="<s:property value='result.resultTime'/>">
				<input type="hidden" name="result.resultTimestamp" value="<s:property value='result.resultTimestamp'/>">
			</td>
		</tr>
		<tr></tr>
	</table>
			<input type="button"  value="保存" onclick="doSave()" />
</form>
</div>
<script type="text/javascript">
	function doSave(){
		$.ajax({
			type:"POST",
			url:"${pageContext.request.contextPath}/slk/compute_save.action",
			data:$("#incomui").serialize(),
			success:function(){
				alert("保存成功！！");
			}
		});
	};

</script>
