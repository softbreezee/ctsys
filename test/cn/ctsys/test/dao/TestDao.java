package cn.ctsys.test.dao;

import java.io.Serializable;

import cn.ctsys.slk.user.entity.User;
import cn.ctsys.test.entity.Person;

public interface TestDao {

	//保存人员
	public void save(Person person);
	
	//根据id查询人员
	public Person findPerson(Serializable id);
	
	public void save(User user);

}
