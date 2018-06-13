package cn.ctsys.slk.user.service;

import java.io.Serializable;
import java.util.List;

import cn.ctsys.slk.user.entity.User;

public interface UserService {

	// 新增
	public void save(User user);

	// 更新
	public void update(User user);

	// 根据id删除
	public void delete(Serializable id);

	// 根据id查找
	public User findObjectById(Serializable id);

	// 查找列表
	public List<User> findObjects();
	
	//根据账户与密码查找
	public List<User> findByAccountAndPass(String account,String password);
}
