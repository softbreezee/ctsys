package cn.ctsys.slk.example.service;

import java.util.List;
import java.util.Map;

import cn.ctsys.slk.example.entity.AddCondition;
import cn.ctsys.slk.example.entity.Example;
import cn.ctsys.slk.example.util.Algorithm;

public interface ExampleService {
	
	
	//生成算例
	void createExample(String name, int IE, int OE, int IF, int OF, int clientNum, int portNum, int stockNum, String carNum);
	void createExample(AddCondition acd);
	void createExample(Example example);
	Example create(String name, int IE, int OE, int IF, int OF, int clientNum, int portNum, int stockNum, String carNum);
	
	//得到全部算例
	List<Example> getAll();
	
	//根据id得到算例
	Example findById(int id);
	
	//删除算例
	void delete(Example	 example);
	void delete(int id);
	
	//得到一个算例的详情
	Map<String,Object> getDetailById(int id);

	
	//计算算例，返回结果！
	Map<String,Object> figure(int id,String type);
	//从数据库中得到并预先处理计算要用到的信息
	Map<String,Object> computeBasic(int id);
	Map<String,Object> figure(int id,Algorithm alg);
	Map<String,Object> figure(Map<String,Object> map);
	
	
	//得到全部算例名称
	 List<String> getExamplesName();
	
	

}
