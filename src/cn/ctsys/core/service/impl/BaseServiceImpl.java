package cn.ctsys.core.service.impl;

import java.io.Serializable;
import java.util.List;

import cn.ctsys.core.service.BaseService;

public class BaseServiceImpl<T> implements BaseService<T> {
	
	

	@Override
	public void save(T entity) {
	}

	@Override
	public void update(T entity) {
	}

	@Override
	public void delete(Serializable id) {
	}

	@Override
	public T findObjectById(Serializable id) {
		
		return null;
	}

	@Override
	public List<T> findObjects() {
		
		return null;
	}

}
