package cn.ctsys.slk.example.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;





















import cn.ctsys.slk.example.entity.AddCondition;
import cn.ctsys.slk.example.entity.Example;
import cn.ctsys.slk.example.entity.RTSresult;
import cn.ctsys.slk.example.entity.Result;
import cn.ctsys.slk.example.service.ExampleService;
import cn.ctsys.slk.example.service.ResultService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ComputeAction extends ActionSupport {

	@Resource
	private  ExampleService exampleService; 
	
	@Resource
	private ResultService resultService;
	
	
	private int id;
	private String sfType;
	private List<String> eplistName;
	private List<Example> eplist;
	private AddCondition addCondition;
	private Result result;


	//测试通过！！
	public String eplist(){
//		eplist = exampleService.getAll();
		eplistName = exampleService.getExamplesName();
		return "json";
	}
	
	
	
	public String compute() throws IOException{
//		System.out.println(id+"......"+sfType);
//		System.out.println(addCondition.getTabuTableLength());
//		System.out.println(addCondition.getMax_gen());
//		Map<String, Object> figure = exampleService.figure(id, "cplex");
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("empId",id);
		map.put("sfType", sfType);
		if("rts".equals(sfType)){
			map.put("neighbourNum", addCondition.getNeighbourhoodNum());
			map.put("tabuLengthNum", addCondition.getTabuTableLength());
			map.put("IterationNum", addCondition.getMax_gen());
			map.put("incLength", addCondition.getIncLength());
			map.put("decLength", addCondition.getDecLength());
			map.put("maxTimesRepeated", addCondition.getMaxTimesRepeated());
			map.put("maxNumWithoutFind", addCondition.getMaxNumWithoutFind());
		}
		Map<String, Object> figure = exampleService.figure(map);
		System.out.println("执行figure");
		String tip="";
		if("cplex".equals(sfType)){
			tip+="<div id='computeBox'>";
			tip+="<center>";
			tip+="<span>1、求解状态</span><br/>";
			tip+= (String) figure.get("state");
			tip+="<span>2、目标值</span><br/>";
			tip+= (String) figure.get("solveTime")+"<br>";
			tip+="<span>3、路径</span><br/>";
			tip+=(String) figure.get("route");
//			tip+="<span>4、解</span><br/>";
//			tip+=(String) figure.get("solve");
//			tip+="<span>5、转换时间</span><br/>";
//			tip+=(String) figure.get("tr");
//			tip+="<span>4、节点活动时间</span><br/>";
//			tip+= (String) figure.get("nodeTime");
			tip+="</center>";
			tip+="</div>";
		}
		if("rts".equals(sfType)){
			//暂时使用
			RTSresult result = (RTSresult) figure.get("rtsresult");
			tip+="<span>1、路径</span><br/>";
			tip+= result.getResultRoute();
			tip+= "<br/><br/><br/>";
			tip+="<span>2、目标值</span><br/>";
			tip+= result.getObjectValue();
			tip+= "<br/><br/><br/>";
			tip+= "<br/><br/><br/>";
		}
//		System.out.println(tip);
		//将以流的形式发送给ajax异步对象
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(tip);
		out.flush();
		out.close();
		return null;
	}
	
	
	public String computeUI(){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("empId",id);
		map.put("sfType", sfType);
		String tip="";
		if("rts".equals(sfType)){
			map.put("neighbourNum", addCondition.getNeighbourhoodNum());
			map.put("tabuLengthNum", addCondition.getTabuTableLength());
			map.put("IterationNum", addCondition.getMax_gen());
			map.put("incLength", addCondition.getIncLength());
			map.put("decLength", addCondition.getDecLength());
			map.put("maxTimesRepeated", addCondition.getMaxTimesRepeated());
			map.put("maxNumWithoutFind", addCondition.getMaxNumWithoutFind());
			tip += "领域解数:"+ addCondition.getNeighbourhoodNum()+";";
			tip +="禁忌表长度:"+addCondition.getTabuTableLength()+";";
			tip +="循环次数:"+addCondition.getMax_gen()+";";
			tip +="增加系数:"+addCondition.getIncLength()+";";
			tip +="减小系数:"+addCondition.getDecLength()+";";
			tip +="最大重复次数:"+addCondition.getMaxTimesRepeated()+";";
			tip +="最大迭代次数:"+addCondition.getMaxNumWithoutFind()+";";
		}
		System.out.println("computUI");
		Map<String, Object> figure = exampleService.figure(map);
		result = new Result();
		System.out.println("执行figure");
		if("cplex".equals(sfType)){
			String solveTime =  (String) figure.get("solveTime");
			String rout = (String) figure.get("route");
			String resultTime = (String) figure.get("computeTime");
			System.out.println(id);
			Example example = exampleService.findById(id);
			System.out.println(example.getEname());
			result.setExample(example);
			result.setMethod("cplex");
			result.setObjectValue(solveTime);
			result.setRoute(rout);
			result.setResultTime(resultTime);
			result.setResultTimestamp(new Date());
		}
		if("rts".equals(sfType)){
			//暂时使用
			RTSresult rs = (RTSresult) figure.get("rtsresult");
		//	1、路径
			String route = rs.getResultRoute();
		//	2、目标值
			double value = rs.getObjectValue();
			result.setExample(exampleService.findById(id));
			result.setMethod("rts");
			result.setObjectValue(String.valueOf(value));
			result.setRoute(route);
			result.setResultTimestamp(new Date());	
			result.setParaset(tip);
			result.setResultTime((String)figure.get("computeTime"));
		}
		ActionContext.getContext().getSession().put("result", result);
		return "computeUI";
	}
	
	//保存结果至数据库中！
	public String save(){
//		Example emp = exampleService.findById(result.getExample().getEid());
		Result result = (Result) ActionContext.getContext().getSession().remove("result");
//		result.setExample(emp);
		resultService.save(result);
		return "saveResult";
		
	}
	
	

	
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setSfType(String sfType) {
		this.sfType = sfType;
	}
	public String getSfType() {
		return sfType;
	}
	public List<String> getEplistName(){
		return eplistName;
	}
	public List<Example> getEplist() {
		return eplist;
	}
	public AddCondition getAddCondition() {
		return addCondition;
	}
	public void setAddCondition(AddCondition addCondition) {
		this.addCondition = addCondition;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	
	
	
}
