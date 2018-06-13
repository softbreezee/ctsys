<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<%@ include file="/common/libraryheader.jsp"%>
<!-- content begin -->
<br />
<div class="tableBox">
	<center>
		<h3>测试详情页</h3>
	</center>
	<table name="stock">
		<caption>港口信息</caption>
		<tr>
			<th>编号</th>
			<th>港口位置</th>
		</tr>
		<tr>
			<td>1</td>
			<td>22,33</td>
		</tr>
	</table>
	<br />


	<table>
		<caption>堆场信息</caption>
		<tr>
			<th>编号</th>
			<th>堆场位置</th>
			<th>车辆数</th>
		</tr>
		<tr>
			<td>1</td>
			<td>23,4</td>
			<td>4</td>
		</tr>
		<tr>
			<td>2</td>
			<td>23,4</td>
			<td>5</td>
		</tr>

	</table>
	<br />
	<table>
		<caption>任务信息</caption>

		<tr>
			<th>编号</th>
			<th>任务类型</th>
			<th>客户时间窗</th>
			<th>港口时间窗</th>
			<th>装/卸货时间</th>
			<th>堆场</th>
			<th>港口</th>
			<th>客户点</th>
		</tr>
		<tr>
			<td>1</td>
			<td>IE</td>
			<td>-</td>
			<td>8:00-9:00</td>
			<td>-</td>
			<td>港口1</td>
			<td>堆场1</td>
			<td>-</td>
		</tr>
		<tr>
			<td>2</td>
			<td>IE</td>
			<td>2:00-3:00</td>
			<td>8:00-9:00</td>
			<td>-</td>
			<td>港口1</td>
			<td>堆场1</td>
			<td>-</td>
		</tr>
		<tr>
			<td>3</td>
			<td>OE</td>
			<td>13:00-14:00</td>
			<td>16:00-17:00</td>
			<td>-</td>
			<td>-</td>
			<td>港口1</td>
			<td>2,3</td>
		</tr>
		<tr>
			<td>4</td>
			<td>OE</td>
			<td>-</td>
			<td>16:00-17:00</td>
			<td>-</td>
			<td>-</td>
			<td>港口1</td>
			<td>34,12</td>
		</tr>
		<tr>
			<td>5</td>
			<td>IF</td>
			<td>10:00-12:00</td>
			<td>8:00-9:00</td>
			<td>5min</td>
			<td>-</td>
			<td>港口1</td>
			<td>5,3</td>
		</tr>
		<tr>
			<td>6</td>
			<td>OF</td>
			<td>8:00-9:00</td>
			<td>10:00-11:00</td>
			<td>6min</td>
			<td>-</td>
			<td>港口1</td>
			<td>11,3</td>
		</tr>

	</table>
	<br />

	<table>
		<caption>求解结果</caption>


		<tr>
			<th>编号</th>

			<th>求解时间</th>

			<th>目标值</th>

			<th>路径</th>

		</tr>
		<tr>
			<td>1</td>

			<td>8</td>

			<td>200</td>

			<td>0-2-3-4-1-5</td>
		</tr>
		<tr>
			<td>2</td>
			<td>5</td>
			<td>200</td>
			<td>0-2-3-4-1-5</td>
		</tr>
	</table>
	<br />
	<center>
		<a href="#">修改</a> <a href="list.html">返回</a>
	</center>
</div>
<br />
<!-- content over -->

<%@ include file="/common/footer.jsp"%>