<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<style>
.m1 {
	border: solid 2px gray;
	border-right: hidden;
	width: 19%;
	height: 500px;
	float: left;
	overflow: scroll;
	overflow-y: auto;
	overflow-x: auto;
}

.m2 {
	border: solid 2px gray;
	width: 80%;
	height: 500px;
	float: left;
	overflow: scroll;
	overflow-y: auto;
	overflow-x: auto;
}

.m1 table {
	padding-left: 5px;
	
}

.m1 td {
	padding: 12px 0;
}

.m1 select {
	margin-top: 2px;
	width: 94px;
}
#figureDetial table{
	border: solid 1px black;
	border-collapse: collapse;
}
#figureDetial td{
	border:solid 1px black;
	width:130px;
	height:25px;
}
#figureDetial span{
	font-size: medium;
	font-weight: bold;
}
</style>
<!-- 引入算例库头部文件 -->
<%@ include file="/common/libraryheader.jsp"%>

<!-- content begin -->

<br />
<div class="content">
	<div class="m1">
		<form id="computeEmp">
			<div>
				请选择算例：
				<select id="eplist">
					<!-- 
					<optgroup label="小规模算例">
						<option>算例1</option>
						</optgroup>
						<optgroup label="大规模算例">
						<option>算例1</option>
						<option>算例2</option>
						</optgroup>
					-->
						<option selected="selected">select</option>
				</select>
			</div>
			<div id="hiddenId"></div>
			<div id="hiddenType"></div>
			<br/>
			<div>
				请选择计算方法： 
				<select id="jsff">
						<option selected="selected">select</option>
					<optgroup label="精确求解">
						<option id="cpqj">cplex求解</option>
					</optgroup>
					<optgroup label="启发式算法">
						<option id="rtsqj">主动禁忌算法</option>
					</optgroup>
				</select>
			</div>
			<br/>
			<div>
				计算时间： <input type="text" style="border:solid 1px;width: 94px;">
			</div>
			<br/>
			<div>
				目标值选择：
				<select name="eMethod" id="">
					<option value="">碳排放</option>
					<option value="">工作时间</option>
				</select>
			</div>
			<br/>

			<div id="rtsParaSet"></div>
			<div id=""></div>
			<div id=""></div>
			<div id=""></div>
			<br/>
			<div>
				<center>
					<input type="button" id="button1" value="计算(json)">
					<input type="button" id="Combut" value="计算(result)">
				</center>
			</div>
		</form>
	</div>
	<div class="m2" >
		<div id="figureDetial">暂无计算时间，计算结果</div>
	</div>

	<div style="clear:both"></div>

</div>
<br />
<!-- content over -->
<%@ include file="/common/footer.jsp"%>
<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.10.2.min.js"></script>
<script type="text/javascript">
	//创建ajax异步对象
	var ajax = null;
	try {
		ajax = new ActiveXObject("microsoft.xmlhttp");

	} catch (e) {
		ajax = new XMLHttpRequest();
		
	}
</script>
<script type="text/javascript">
	window.onload = function() {
		var eplistEle = document.getElementById("eplist");
		ajax.open("post","${pageContext.request.contextPath}/slk/compute_eplist.action");
		ajax.setRequestHeader("content-type","application/x-www-form-urlencoded");
		ajax.send(null);
		ajax.onreadystatechange = function() {
			if (ajax.readyState == 4) {
				if (ajax.status == 200) {
					var jsonJava = ajax.responseText;
					
					
					var jsonJs = eval("(" + jsonJava + ")");
					var eps = jsonJs.eplistName;
					for (var i = 0; i < eps.length; i++) {
						var ep = eps[i];
						var option = document.createElement("option");
						option.innerHTML = ep;
						eplistEle.appendChild(option);
					}
				}
			}
		}
	};
	
	$("#eplist").change(function(){
		var sel = document.getElementById("eplist");
		var optionInner = sel.options[sel.selectedIndex].innerHTML;
		var s =  optionInner.split("_");
		var id = s[1];
		$("#hiddenId").html("<input type='hidden' name='id' value='"+id+"'>");
		//alert(id);
	
	});

</script>
<script type="text/javascript">
	var sfType = "cplex";
		$("#jsff").change(function(){
		//如果选择了cplext求解
		if($("#cpqj").prop("selected") == true){
			sfType = "cplex";		
		}
		//如果选择了rts求解
		if($("#rtsqj").prop("selected") == true){
			sfType = "rts";
			var str;
			str= "<style>input{border:1px solid black;width:130px}";
			str+= "fieldset{border-radius: 5px;border:1px gray solid;margin-left:3px;}</style>";
			str+= "<fieldset>";
			str+= "<legend>主动禁忌算法</legend>";
			str+= "<div id='paraSetDiv'>";
	        str+= "邻域解数";
	       	str+= "<input type='number' name='addCondition.neighbourhoodNum' id='neighbourNum' class='search-input-text'>";
	        str+= "<br>";
	        str+= "禁忌表长度";
	        str+= "<input type='number' name='addCondition.tabuTableLength' id='tabuLengthNum' class='search-input-text'>";
	        str+= "<br>";
	        str+= "循环次数";
	        str+= "<input type='number' name='addCondition.max_gen' id='IterationNum' class='search-input-text'>";
	        str+= "<br>";
	        str+= "禁忌表长度增加系数";
	        str+= "<input type='text' name='addCondition.incLength' id='incLength' class='search-input-text' placeholder='可输入浮点数，如1.05'>";
	        str+= "<br>";
	        str+= "禁忌表长度减小系数";
	        str+= "<input type='text' name='addCondition.decLength' id='decLength' class='search-input-text' placeholder='可输入浮点数，如0.9'>";
	        str+= "<br>";
	        str+= "最大重复次数";
	        str+= "<input type='number' name='addCondition.maxTimesRepeated' id='maxTimesRepeated' class='search-input-text'>";
	        str+= "<br>";
	        str+= "无重复解出现的最大迭代数";
	        str+= "<input type='number' name='addCondition.maxNumWithoutFind' id='maxNumWithoutFind' class='search-input-text'>";
	        str+= "<br>";
	        str+= "<input type='number' value='${exampleId}' id='exampleId' name='exampleId' class='search-input-text' style='display: none'>";
	    	str+= "</div>";
			str+= "</fieldset>";
			$("#rtsParaSet").html(str);
		}
		
	});
	$("#jsff").change(function(){
		$("#hiddenType").html("<input type='hidden' name='sfType' value='"+sfType+"'>");
		//alert(sfType);
	
	});
	

	$("#button1").click(function(){
		//$(".m2bottomAdd").load("${pageContext.request.contextPath}/slk/add_create.action");
		$.ajax({
			type:"POST",
			url:"${pageContext.request.contextPath}/slk/compute_compute.action",
			data:$('#computeEmp').serialize(),
			success:function(data){
				$("#figureDetial").html(data);
			}
		});
	
	});
</script>
<script type="text/javascript">
	function doSave(){
		location.href="${pageContext.request.contextPath}/slk/compute_save.action";
	}
</script>
<script type="text/javascript">
	$("#Combut").click(function(){
		$.ajax({
			type:"POST",
			url:"${pageContext.request.contextPath}/slk/compute_computeUI.action",
			data:$('#computeEmp').serialize(),
			success:function(data){
				$("#figureDetial").html(data);
			}
		});
			
	});
</script>
