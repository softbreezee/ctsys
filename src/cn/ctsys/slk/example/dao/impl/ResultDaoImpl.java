package cn.ctsys.slk.example.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;

import cn.ctsys.core.dao.impl.BaseDaoImpl;
import cn.ctsys.slk.example.dao.ResultDao;
import cn.ctsys.slk.example.entity.Example;
import cn.ctsys.slk.example.entity.Result;

public class ResultDaoImpl extends BaseDaoImpl<Result> implements ResultDao {

	@Override
	public int getCalculateTimes(Serializable id) {
		int uniqueResult = (Integer) getSession().createQuery("select count(*) from Result where e_id= ? ").setParameter(0, id).uniqueResult();
		return uniqueResult;
	}

	@Override
	public List<Result> getCalculateDetails(Serializable id) {
		Query query = getSession().createQuery("FROM Result where e_id=?").setParameter(0,id);
		return query.list();
	}

	
}
