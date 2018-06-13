package cn.ctsys.slk.user.dao;

import java.util.List;

import cn.ctsys.core.dao.BaseDao;
import cn.ctsys.slk.user.entity.User;

public interface UserDao extends BaseDao<User> {
	public List<User> findByAccountAndPass(String account,String password);

}
