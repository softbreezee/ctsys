<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<%@ include file="/common/libraryheader.jsp"%>
<style>
/* add */
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
	width: 97%;
	height:500px;
	float: left;
	overflow: scroll;
	overflow-y:auto;
	overflow-x:auto;	
	padding:10px 10px;
}
.m1 dt {
	padding: 10px 15px;
}
th,td {
	padding: 10px;
	border: 1px lightblue solid;
}
input{
	border:1px black solid;
	width:120px;
}
fieldset{
	border-radius: 5px;
	border:1px gray solid;
}
</style>
<script src="../public/jquery-3.1.1.min.js"></script>
<!-- content begin -->
<div class="content">
	<!-- 
	<div class="m1">
		<ul>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
			<li></li>
		</ul>
	</div>
	-->
	<div class="m2">
		<div class="m2topAdd" style="">
			<form id="addemp">
				<div class="m2ta1">
					<fieldset>
						<legend>算例类型</legend>
						<div style="float:left;width:30%;">
							名称
							<input type="text" name="addCondition.name" style="border:1px solid black;"/>
						</div>
						<div style="float:left;width:30%;">
							条件<br/>
							<input id="czyys" type="checkbox" >考虑资源约束<br/>
							<input id="cinfoupdate！" type="checkbox" >考虑信息更新<br/>
							<input id="cmultsize" type="checkbox" >多尺寸
						</div>
						<div style="float:left;width:30%;">
							运输模式<br/>
							<input type="radio" checked="checked" name="tranType">普通<br/>
							<input type="radio" name="tranType">甩挂
						</div>
						<div>
							<input type="button" id="emptypebutton" value="下一步">
						</div>
					</fieldset>
				</div>
				<div class="m2ta2">
					<fieldset>
						<legend>算例参数</legend>
							堆场个数<input type="number" name="addCondition.stockNum" />
							集卡数量<input id="sn" type="text" name="addCondition.carNum">
							港口个数<input type="number" name="addCondition.portNum" />
							客户个数<input type="number" name="addCondition.clientNum" />
							<br/>
							OF<input type="number" name="addCondition.ofNum" />
						 	IF<input type="number" name="addCondition.ifNum" />
							OE<input type="number" name="addCondition.oeNum" />
							IE<input type="number" name="addCondition.ieNum" />
							<br/>
					</fieldset>
				</div>
				<div id="m2zzys"></div>
				<div id="m2infoupdate"></div>
				<div id="m2multsize"></div>
				<div></div>
				<div></div>
				<div class="m2ta2" ></div>
				<input id="ns" type="button" value="下一步">
				<input id="create" type="button" value="生成">
				<input type="reset" value="重置" onclick="zyys()" > 
				<input type="button" value="导入" onclick="doImport()"> 
			</form>
		</div>
		<hr/>
		<div class="m2bottomAdd" style="text-align: center;"></div>
		<br>

	</div>
	<div style="clear: both;"></div>

</div>
<br />
<!-- content over -->
<%@ include file="/common/footer.jsp"%>
<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
	//下一步，显示生成的算例的信息！
	$("#ns").click(function(){
		//$(".m2bottomAdd").load("${pageContext.request.contextPath}/slk/add_createDetail.action");
		$.ajax({
			type:"POST",
			url:"${pageContext.request.contextPath}/slk/add_createDetail.action",
			//异步提交一个表单
			data:$('#addemp').serialize(),
			success:function(data){
				$(".m2bottomAdd").html(data);
			}
		});
		//document.forms[0].action = "${pageContext.request.contextPath}/slk/add_createDetail.action";
      	//document.forms[0].submit();
	});
	
	//生成算例
	$("#create").click(function(){
		//$(".m2bottomAdd").load("${pageContext.request.contextPath}/slk/add_create.action");
		$.ajax({
			type:"POST",
			url:"${pageContext.request.contextPath}/slk/add_create.action",
			data:$('#addemp').serialize(),
			success:function(){
				alert("创建成功！");
			}
		});
	
	});
	//追加条件
	$("#emptypebutton").click(function(){
		//alert("ss");
		//var bool = $("#czyys").prop("checked");
		if($("#czyys").prop("checked") == true){
			$("#m2zzys").load("${pageContext.request.contextPath}/slk/add_zyysCondition.action");
		}
		if($("#cinfoupdate").prop("checked") == true){
			$("#m2infoupdate").load("${pageContext.request.contextPath}/slk/add_infoupdateCondition.action");
		}
		if($("#cmultsize").prop("checked") == true){
			$("#m2multsize").load("${pageContext.request.contextPath}/slk/add_multsizeCondition.action");
		}
		//alert("dd");		
	});
	function zyys(){
		alert("111111");
		$("#m2zzys").load("${pageContext.request.contextPath}/slk/add_zyysCondition.action");
	};
</script>


