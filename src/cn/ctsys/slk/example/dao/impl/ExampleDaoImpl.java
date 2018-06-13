package cn.ctsys.slk.example.dao.impl;


import java.util.List;

import cn.ctsys.core.dao.impl.BaseDaoImpl;
import cn.ctsys.slk.example.dao.ExampleDao;
import cn.ctsys.slk.example.entity.Example;
import cn.ctsys.slk.example.entity.Task;

public class ExampleDaoImpl extends BaseDaoImpl<Example> implements ExampleDao {

	
	@Override
	public List<Task> getTasksByEid(int eid) {
		
		return null;
	}

	@Override
	public void delete(Example example) {
		Example ep = findObjectById(example.getEid());
		//清除联系
		ep.getTasks().clear();
		getSession().createQuery("delete from Example where eId=?")
		.setParameter(0, ep.getEid()).executeUpdate();
	}

	@Override
	public Example findById(int id) {
		
		return null;
	}

	


}
