package cn.ctsys.slk.example.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ctsys.slk.example.dao.ResultDao;
import cn.ctsys.slk.example.entity.Example;
import cn.ctsys.slk.example.entity.Result;
import cn.ctsys.slk.example.service.ResultService;

@Service("resultService")
public class ResultServiceImpl implements ResultService {

	@Resource
	private ResultDao resultDao;
	
	@Override
	public int getCalculateTimes(int id) {
		
		return resultDao.getCalculateTimes(id);
	}

	@Override
	public void save(Result result) {
		resultDao.save(result);
	}

	@Override
	public List<Result> getResultList(Example example) {
		
		return getResultList(example.getEid());
	}

	@Override
	public List<Result> getResultList(int id) {
		
		return resultDao.getCalculateDetails(id);
	}

	@Override
	public void delResult(int id) {
		resultDao.delete(id);
	}
	

	
	

}
