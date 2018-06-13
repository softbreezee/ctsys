package cn.ctsys.core.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.ctsys.core.dao.BaseDao;

public class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T> {

	Class<T> clazz;
	public BaseDaoImpl(){
		//得到BaseDaoImpl<T>
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		//得到T
		clazz = (Class<T>) type.getActualTypeArguments()[0];
		
	}
	
	
	@Override
	public void save(T entity) {
		getHibernateTemplate().save(entity);
	}

	@Override
	public void update(T entity) {
		getHibernateTemplate().update(entity);
	}

	@Override
	public void delete(Serializable id) {
		getHibernateTemplate().delete(findObjectById(id));
	}

	@Override
	public T findObjectById(Serializable id) {
		
		return getHibernateTemplate().get(clazz, id);
	}

	@Override
	public List<T> findObjects() {
		
		Query query = getSession().createQuery("FROM " + clazz.getSimpleName());
		return query.list();
	}


}
