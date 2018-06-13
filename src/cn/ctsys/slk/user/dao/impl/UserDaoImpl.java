package cn.ctsys.slk.user.dao.impl;

import java.util.List;

import org.hibernate.Query;

import cn.ctsys.core.dao.impl.BaseDaoImpl;
import cn.ctsys.slk.user.dao.UserDao;
import cn.ctsys.slk.user.entity.User;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@Override
	public List<User> findByAccountAndPass(String account, String password) {
		
		Query query = getSession().createQuery("FROM User WHERE account=? AND password=?");
		query.setParameter(0, account);
		query.setParameter(1, password);
		return query.list();
	}
}
