package cn.ctsys.slk.example.action;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.RequestAware;











import cn.ctsys.slk.example.entity.AddCondition;
import cn.ctsys.slk.example.entity.Example;
import cn.ctsys.slk.example.entity.Result;
import cn.ctsys.slk.example.service.ExampleService;
import cn.ctsys.slk.example.service.ResultService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 将example算例表中的数据放在session中，list页面直接访问jsp，不通过action 在添加新的算例的时候，要更新session！
 * @author Leon
 * 
 */
public class ExampleAction extends ActionSupport implements RequestAware {
	//设置request
	private Map<String,Object> request;
	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}
	//算例id
	private int eid;
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}

	//算例生成页的页面信息
	public  AddCondition addCondition;
	public void setAddCondition(AddCondition addCondition) {
		this.addCondition = addCondition;
	}
	public AddCondition getAddCondition() {
		return addCondition;
	}
	
	//求解结果
	public Result result;
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	
	@Resource
	private ExampleService exampleService;
	
	@Resource
	private ResultService resultService;

	
	

	
	


	/**
	 * 进入list页面，但是session中要有算例的信息
	 * 通过测试
	 * @return
	 */
	public String listUI() {
		Map<String, Object> session = (Map<String, Object>) ActionContext
				.getContext().get("session");
		//查询全部的example
		List<Example> all = exampleService.getAll();
		session.put("exampleList", all);
		return "listUI";
	}

	/**
	 * 在web.xml中配置过滤器，防止懒加载！
	 * 测试通过
	 * @return
	 */
	public String detailUI() {

		System.out.println(eid);
		Example ep = exampleService.findById(eid);
		request.put("example", ep);
		//详情页里放入求解记录！
		List<Result> resultList = resultService.getResultList(eid);
		request.put("resultList", resultList);
		
		
		// 1、根据eid查到example
		// 2、根据example查到对应的List<task>
		// 3、防止懒加载，迭代List<task>，查到每个task对应的堆场、港口、客户信息
		// 4、保存在request中转发

		return "detailUI";
	}

	public String solveInfo(){
		return "solveInfo";
	}
	
	
	/**
	 * 删除动作，删除完后进入列表页面！
	 * 待测试！
	 * @return
	 */
	public String delExample(){
		System.out.println(eid);
		exampleService.delete(eid);
		return "list";	
	}
	
	
	/**
	 * 计算动作
	 * @return
	 */
	public String computeUI() {
		return "computeUI";

	}

	/**
	 * 分析
	 * 
	 * @return
	 */
	public String analyseUI() {
		return null;

	}

	//通过
	public String computeRecordUI(){
		List<Result> resultList = resultService.getResultList(eid);
		request.put("resultList", resultList);
		return "computeRecordUI";
	}

	public String delResult(){
//		System.out.println(eid);
		resultService.delResult(eid);
		return "list";	
	}
	

}
