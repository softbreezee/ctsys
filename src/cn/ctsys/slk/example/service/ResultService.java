package cn.ctsys.slk.example.service;

import java.util.List;

import cn.ctsys.slk.example.entity.Example;
import cn.ctsys.slk.example.entity.Result;

public interface ResultService {
	
	//查询求解的次数
	int getCalculateTimes(int id);
	void save(Result result);
	
	List<Result>	getResultList(Example example);
	List<Result> getResultList(int id);
	
	void delResult(int id);
	

}
