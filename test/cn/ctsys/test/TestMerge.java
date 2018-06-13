package cn.ctsys.test;

import java.util.UUID;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;






import cn.ctsys.slk.user.entity.User;
import cn.ctsys.test.dao.TestDao;
import cn.ctsys.test.entity.Person;
import cn.ctsys.test.service.TestService;

public class TestMerge {
	
	ClassPathXmlApplicationContext ctx;

	@Before
	public void loadCtx() {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	@Test
	public void testSpring() {
		TestService ts = (TestService)ctx.getBean("testService");
		ts.say();
	}
	
	@Test
	public void testHibernate() {
		SessionFactory sf = (SessionFactory)ctx.getBean("sessionFactory");
		
		Session session = sf.openSession();
		Transaction transaction = session.beginTransaction();
		//保存人员
		session.save(new Person("人员1"));
		transaction.commit();
		session.close();
	}
	
	@Test
	public void testServiceAndDao() {
		TestService ts = (TestService)ctx.getBean("testService");
		ts.save(new Person("人员2"));
		//System.out.println(ts.findPerson("4028eea54c8cdb1f014c8cdb20ab0000").getName());
	}
	
	@Test
	public void testTransationReadOnly() {//只读事务，如果在只读事务中出现更新操作则回滚
		TestService ts = (TestService)ctx.getBean("testService");
		System.out.println(ts.findPerson("4028eea54c8cdb1f014c8cdb20ab0000").getName());
	}
	
	@Test
	public void testTransationRollback() {//回滚事务，如果操作中出现有任务异常则回滚先前的操作
		TestService ts = (TestService)ctx.getBean("testService");
		ts.save(new Person("人员4"));
	}
	
	@Test
	public void testUser(){
		User user = new User();
//	user.setId(UUID.randomUUID().toString());
		user.setAccount("admin");
		user.setName("admin");
		user.setPassword("admin");
		user.setPro("stu");
		TestDao ts = (TestDao)ctx.getBean("testDao");
		ts.save(user);
		
		
	}
	

	public void test1(){
		
		
		
	}
	

}
