<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<fieldset>
	<legend>主动禁忌算法</legend>
	<div id="paraSetDiv">
        <button class="static"> 邻域解数 </button>
        <input type="number" name="neighbourNum" id="neighbourNum" class="search-input-text">
        <br>
        <button class="static">禁忌表长度</button>
        <input type="number" name="tabuLengthNum" id="tabuLengthNum" class="search-input-text">
        <br>
        <button class="static">循环次数</button>
        <input type="number" name="IterationNum" id="IterationNum" class="search-input-text">
        <br>
        <button class="static">禁忌表长度增加系数</button>
        <input type="text"name="incLength" id="incLength" class="search-input-text" placeholder="可输入浮点数，如1.05">
        <br>
        <button class="static">禁忌表长度减小系数</button>
        <input type="text" name="decLength" id="decLength" class="search-input-text" placeholder="可输入浮点数，如0.9">
        <br>
        <button class="static">最大重复次数</button>
        <input type="number" name="maxTimesRepeated" id="maxTimesRepeated" class="search-input-text">
        <br>
        <button class="static">无重复解出现的最大迭代数</button>
        <input type="number" name="maxNumWithoutFind" id="maxNumWithoutFind" class="search-input-text">
        <br>
            <input type="number" value="${exampleId}" id="exampleId" name="exampleId" class="search-input-text" style="display: none">
            <button type="submit" class="do-button" onclick="check()" >提交参数并计算</button>
    </div>
</fieldset>

