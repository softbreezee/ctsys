package cn.ctsys.slk.example.dao;

import java.io.Serializable;
import java.util.List;

import cn.ctsys.core.dao.BaseDao;
import cn.ctsys.slk.example.entity.Result;

public interface ResultDao extends BaseDao<Result> {
	
	int getCalculateTimes(Serializable id);
	List<Result> getCalculateDetails(Serializable id);
}
