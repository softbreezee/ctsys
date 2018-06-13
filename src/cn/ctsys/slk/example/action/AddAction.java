package cn.ctsys.slk.example.action;




import java.util.Map;

import org.apache.struts2.ServletActionContext;

import cn.ctsys.core.action.BaseAction;
import cn.ctsys.slk.example.entity.AddCondition;
import cn.ctsys.slk.example.entity.Example;
import cn.ctsys.slk.example.service.ExampleService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class AddAction extends BaseAction {
	
	
	private AddCondition addCondition;
	public void setAddCondition(AddCondition addCondition) {
		this.addCondition = addCondition;
	}
	public AddCondition getAddCondition() {
		return addCondition;
	}
	private Example example;
	
	public Example getExample() {
		return example;
	}
	public void setExample(Example example) {
		this.example = example;
	}
	
	
	//-------------------------------
	private ExampleService exampleService;
	public void setExampleService(ExampleService exampleService) {
		this.exampleService = exampleService;
	}
	
	
	
	/**
	 * 进入添加算例视图
	 * 
	 * @return
	 */
	public String createUI() {
		return "createUI";
	}
	
	
	/**
	 * 用于数据回显
	 * @return
	 */
	public String createDetail(){
//		String name, int IE, int OE, int IF, int OF,int clientNum, int portNum, int stockNum,String carNum
		
		System.out.println(addCondition);
		System.out.println(addCondition.getName());
		String name = addCondition.getName();
		int ieNum = addCondition.getIeNum();
		int oeNum = addCondition.getOeNum();
		int ifNum = addCondition.getIfNum();
		int ofNum = addCondition.getOfNum();
		int clientNum = addCondition.getClientNum();
		int portNum = addCondition.getPortNum();
		int stockNum = addCondition.getStockNum();
		String carNum = addCondition.getCarNum();
//		System.out.println(name+ieNum+oeNum+ifNum+ofNum+clientNum+portNum+stockNum+carNum);
		
		//保存在数据库中！
//		exampleService.createExample(name, ieNum, oeNum, ifNum,  ofNum, clientNum, portNum, stockNum, carNum);
		//没保存到数据库
		example = exampleService.create(name, ieNum, oeNum, ifNum, ofNum, clientNum, portNum, stockNum, carNum);
		//保存在session域，传给下一个action
		ActionContext.getContext().getSession().put("emp1", example);
		return "createDetail";
	}
	
	
	/**
	 * 添加算例动作，添加到数据库！
	 * @return
	 */
	public String create() {
		//从session域中获取到
		example = (Example) ActionContext.getContext().getSession().remove("emp1");
		exampleService.createExample(example);
		return "success";
	}
	
	
	/**
	 * 进入类型的条件
	 * 在添加页添加条件！
	 */
	public String zyysCondition(){
		return "zyysCondition";
	}
	public String infoupdateCondition(){
		return "infoupdateCondition";
	}
	public String multsizeCondition(){
		return "multsizeCondition";
	}

	
	
}
