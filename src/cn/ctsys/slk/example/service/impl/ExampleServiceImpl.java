package cn.ctsys.slk.example.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ctsys.slk.example.util.Algorithm;
import cn.ctsys.slk.example.util.ArrStrHandler;
import cn.ctsys.slk.example.util.RandomCreate;
import cn.ctsys.slk.example.util.impl.CplexImpl;
import cn.ctsys.slk.example.util.impl.RTSAlgorithmImpl;
import cn.ctsys.slk.example.constant.SlkConstant;
import cn.ctsys.slk.example.dao.ExampleDao;
import cn.ctsys.slk.example.dao.ResultDao;
import cn.ctsys.slk.example.entity.AddCondition;
import cn.ctsys.slk.example.entity.Example;
import cn.ctsys.slk.example.entity.Result;
import cn.ctsys.slk.example.entity.Site;
import cn.ctsys.slk.example.entity.Task;
import cn.ctsys.slk.example.service.ExampleService;


@Service("exampleService")
public class ExampleServiceImpl implements ExampleService {
	
	@Resource
	private ExampleDao exampleDao;
	
	@Resource
	private ResultDao resultDao;
	
	
	private Algorithm alg;

	/**
	 * 任务的排列：IE OE IF OF ie0 ie1 ie2 oe0 oe1 if0 if1 of0 of1 of2 ...
	 * 地理位置的排列：港口、堆场、客户 p0 p1 s0 s1 c0 c1 c2 c3....
	 * 
	 * 重箱任务的数量要等于客户的数量！！
	 * 
	 * 测试通过！！
	 */
	@Override
	public void createExample(String name, int IE, int OE, int IF, int OF,
			int clientNum, int portNum, int stockNum, String carNum) {
		// 1、创建算例
		// 2、根据对应的数据生成map
		// 3、将对应的数据保存在数据库中
		Example ep = new Example();
		ep.setDate(new Date());
		ep.setEname(name);
		//创建集合的过程在创建算例的时候创建！
		List<Task> tasks = ep.getTasks();
		List<Site> sites = ep.getSites();

		Map<String, Object> condition = RandomCreate.getCondition(IE, OE, IF,
				OF, clientNum, portNum, stockNum, 30000, 30000);
		int[][] clientP = (int[][]) condition.get("clientPosition");
		int[][] portP = (int[][]) condition.get("portPosition");
		int[][] stockP = (int[][]) condition.get("stockPosition");
//		double[][] distance = (double[][]) condition.get("distance");
//		int[][] driveTime = (int[][]) condition.get("driveTime");
		int[][] tw1 = (int[][]) condition.get("tw1");
		int[][] tw2 = (int[][]) condition.get("tw2");
		int[] loadTime = (int[]) condition.get("loadTime");
		int[] car = ArrStrHandler.handleCar(carNum);
		// 保存site
		for (int i = 0; i < portNum; i++) {
			Site site = new Site();
			site.setPosition(portP[0][i] + "," + portP[1][i]);
			site.setType("port");
			sites.add(site);
			System.out.println("======1");
		}

		for (int i = 0; i < stockNum; i++) {
			Site site = new Site();
			site.setPosition(stockP[0][i] + "," + stockP[1][i]);
			site.setType("stock");
			site.setCarNum(car[i]);
			sites.add(site);
			System.out.println("======1");
		}

		for (int i = 0; i < clientNum; i++) {
			Site site = new Site();
			site.setPosition(clientP[0][i] + "," + clientP[1][i]);
			site.setType("client");
			sites.add(site);
			System.out.println("======1");
		}

		// 保存task
		// 空箱任务不指定堆场！堆场与客户之间是优化出来的结果！
		for (int i = 0; i < IE; i++) {
			Task task = new Task();
			task.setTaskType("IE");
			task.setTw1(tw1[0][i] + "," + tw1[1][i]);
			tasks.add(task);
		}
		for (int i = 0; i < OE; i++) {
			Task task = new Task();
			task.setTaskType("OE");
			task.setTw1(tw1[0][i + IE] + "," + tw1[1][i + IE]);
			tasks.add(task);
		}
		for (int i = 0; i < IF; i++) {
			Task task = new Task();
			task.setTaskType("IF");
			task.setTw1(tw1[0][i + IE + OE] + "," + tw1[1][i + IE + OE]);
			task.setTw2(tw2[0][i] + "," + tw2[1][i]);
			task.setLoadTime(loadTime[i]);
			tasks.add(task);
		}
		for (int i = 0; i < OF; i++) {
			Task task = new Task();
			task.setTaskType("OF");
			task.setTw1(tw1[0][i + IE + OE + IF] + "," + tw1[1][i + IE + OE + IF]);
			task.setTw2(tw2[0][i + IF] + "," + tw2[1][i + IF]);
			task.setLoadTime(loadTime[i + IF]);
			tasks.add(task);
		}

		// 任务与地理位置的组合！！
		for (int i = 0; i < IE; i++) {
			/**
			 * 进口空箱，从港口到堆场！ 这里只指定港口，堆场是优化的目标！
			 */
			int portIndex = (int) (Math.random() * portNum);
			Site start = sites.get(portIndex);
			tasks.get(i).setStart(start);
		}
		for (int i = 0; i < OE; i++) {
			/**
			 * 出口空箱，从堆场到港口 这里同样只指定出口的港口！！
			 */
			int portIndex = (int) (Math.random() * portNum);
			Site end = sites.get(portIndex);
			tasks.get(i+IE).setEnd(end);
		}
		for (int i = 0; i < IF; i++) {
			/**
			 * 重箱的任务与客户点的顺序一一对应！港口随机分配到
			 * 进口重箱，港口——》客户 ；港口不指定，客户按顺序分配！
			 */
//			int portIndex = (int) (Math.random() * portNum);
//			Site start = sites.get(portIndex);
			Site end = sites.get(i + portNum + stockNum);
//			tasks.get(i + IE + OE).setStart(start);
			tasks.get(i + IE + OE).setEnd(end);
		}
		for (int i = 0; i < OF; i++) {
			/**
			 * 出口重箱，客户——》港口 ；港口随机不指定，客户按顺序分配！
			 */
//			int portIndex = (int) (Math.random() * portNum);
			// int clientIndex = (int) (Math.random()*clientNum);
			// 客户点的数量 == 重箱任务的数量？
			Site start = sites.get(i + portNum + stockNum + IF);
//			Site end = sites.get(portIndex);
			tasks.get(i + IE + OE + IF).setStart(start);
//			tasks.get(i + IE + OE + IF).setEnd(end);
		}

		// 对与满箱任务，港口随机分配，客户随机分配
		exampleDao.save(ep);

	}
	
	
	@Override
	public Example create(String name, int IE, int OE, int IF, int OF,
			int clientNum, int portNum, int stockNum, String carNum) {
		// 1、创建算例
		// 2、根据对应的数据生成map
		// 3、将对应的数据保存在数据库中
		Example ep = new Example();
		ep.setDate(new Date());
		ep.setEname(name);
		//创建集合的过程在创建算例的时候创建！
		List<Task> tasks = ep.getTasks();
		List<Site> sites = ep.getSites();

		Map<String, Object> condition = RandomCreate.getCondition(IE, OE, IF,
				OF, clientNum, portNum, stockNum, 30000, 30000);
		int[][] clientP = (int[][]) condition.get("clientPosition");
		int[][] portP = (int[][]) condition.get("portPosition");
		int[][] stockP = (int[][]) condition.get("stockPosition");
//		double[][] distance = (double[][]) condition.get("distance");
//		int[][] driveTime = (int[][]) condition.get("driveTime");
		int[][] tw1 = (int[][]) condition.get("tw1");
		int[][] tw2 = (int[][]) condition.get("tw2");
		int[] loadTime = (int[]) condition.get("loadTime");
		int[] car = ArrStrHandler.handleCar(carNum);
		// 保存site
		for (int i = 0; i < portNum; i++) {
			Site site = new Site();
			site.setPosition(portP[0][i] + "," + portP[1][i]);
			site.setType("port");
			sites.add(site);
			System.out.println("======1");
		}

		for (int i = 0; i < stockNum; i++) {
			Site site = new Site();
			site.setPosition(stockP[0][i] + "," + stockP[1][i]);
			site.setType("stock");
			site.setCarNum(car[i]);
			sites.add(site);
			System.out.println("======1");
		}

		for (int i = 0; i < clientNum; i++) {
			Site site = new Site();
			site.setPosition(clientP[0][i] + "," + clientP[1][i]);
			site.setType("client");
			sites.add(site);
			System.out.println("======1");
		}

		// 保存task
		// 空箱任务不指定堆场！堆场与客户之间是优化出来的结果！
		for (int i = 0; i < IE; i++) {
			Task task = new Task();
			task.setTaskType("IE");
			task.setTw1(tw1[0][i] + "," + tw1[1][i]);
			tasks.add(task);
		}
		for (int i = 0; i < OE; i++) {
			Task task = new Task();
			task.setTaskType("OE");
			task.setTw1(tw1[0][i + IE] + "," + tw1[1][i + IE]);
			tasks.add(task);
		}
		for (int i = 0; i < IF; i++) {
			Task task = new Task();
			task.setTaskType("IF");
			task.setTw1(tw1[0][i + IE + OE] + "," + tw1[1][i + IE + OE]);
			task.setTw2(tw2[0][i] + "," + tw2[1][i]);
			task.setLoadTime(loadTime[i]);
			tasks.add(task);
		}
		for (int i = 0; i < OF; i++) {
			Task task = new Task();
			task.setTaskType("OF");
			task.setTw1(tw1[0][i + IE + OE + IF] + "," + tw1[1][i + IE + OE + IF]);
			task.setTw2(tw2[0][i + IF] + "," + tw2[1][i + IF]);
			task.setLoadTime(loadTime[i + IF]);
			tasks.add(task);
		}

		// 任务与地理位置的组合！！
		for (int i = 0; i < IE; i++) {
			/**
			 * 进口空箱，从港口到堆场！ 这里只指定港口，堆场是优化的目标！
			 */
			int portIndex = (int) (Math.random() * portNum);
			Site start = sites.get(portIndex);
			tasks.get(i).setStart(start);
		}
		for (int i = 0; i < OE; i++) {
			/**
			 * 出口空箱，从堆场到港口 这里同样只指定出口的港口！！
			 */
			int portIndex = (int) (Math.random() * portNum);
			Site end = sites.get(portIndex);
			tasks.get(i+IE).setEnd(end);
		}
		for (int i = 0; i < IF; i++) {
			/**
			 * 重箱的任务与客户点的顺序一一对应！港口随机分配到
			 * 进口重箱，港口——》客户 ；港口不指定，客户按顺序分配！
			 */
//			int portIndex = (int) (Math.random() * portNum);
//			Site start = sites.get(portIndex);
			Site end = sites.get(i + portNum + stockNum);
//			tasks.get(i + IE + OE).setStart(start);
			tasks.get(i + IE + OE).setEnd(end);
		}
		for (int i = 0; i < OF; i++) {
			/**
			 * 出口重箱，客户——》港口 ；港口随机不指定，客户按顺序分配！
			 */
//			int portIndex = (int) (Math.random() * portNum);
			// int clientIndex = (int) (Math.random()*clientNum);
			// 客户点的数量 == 重箱任务的数量？
			Site start = sites.get(i + portNum + stockNum + IF);
//			Site end = sites.get(portIndex);
			tasks.get(i + IE + OE + IF).setStart(start);
//			tasks.get(i + IE + OE + IF).setEnd(end);
		}

		// 对与满箱任务，港口随机分配，客户随机分配
		//exampleDao.save(ep);

		return ep;
	}


	@Override
	public void createExample(Example example) {
		
		exampleDao.save(example);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}


	@Override
	public void createExample(AddCondition acd) {
		//不同类型是不是不同的保存方式？？否则这个方法写的没有意义
		if(SlkConstant.TYPE_BASIC.equals(acd.getType())){
			
		}
		if(SlkConstant.TYPE_BASIC.equals(acd.getType())){
			
		}
		if(SlkConstant.TYPE_BASIC.equals(acd.getType())){
			
		}
		if(SlkConstant.TYPE_BASIC.equals(acd.getType())){
			
		}
		
	}



	@Override
	public void delete(Example example) {
		exampleDao.delete(example);
	}

	@Override
	public void delete(int id) {
		exampleDao.delete(id);

	}

	@Override
	public List<Example> getAll() {

		return exampleDao.findObjects();
	}

	@Override
	public List<String> getExamplesName() {
		ArrayList<String> al = new ArrayList<String>();
		List<Example> eps = exampleDao.findObjects();
		Iterator<Example> it = eps.iterator();
		while (it.hasNext()) {
			
			Example ep = it.next();
			String name = ep.getEname();
			int eid = ep.getEid();
			
			al.add(name+"_"+eid);
		}
		return al;
	}

	@Override
	public Example findById(int id) {

		return exampleDao.findObjectById(id);
	}

	@Override
	public Map<String, Object> getDetailById(int id) {
		Example ep = exampleDao.findObjectById(id);
		List<Task> tasks = ep.getTasks();
		return null;
	}

	
	/**
	 * 测试通过
	 */
	@Override
	public Map<String, Object> figure(int id, String type) {
		Map<String, Object> cd = computeBasic(id);
		if ("cplex".equals(type)) {
			alg = new CplexImpl();
			Map<String, Object> str = alg.computing(cd);
			System.out.println("service_figure执行！");
			return str;
		}else{
			return new HashMap<String,Object>();
		}
	}

	@Override
	public Map<String, Object> figure(int id, Algorithm alg) {
		
		return null;
	}

	
	@Override
	public Map<String, Object> figure(Map<String, Object> map) {
		int id = (Integer) map.get("empId");
		String type = (String) map.get("sfType");
		Map<String, Object> cd = computeBasic(id);
		if ("cplex".equals(type)) {
			alg = new CplexImpl();
			//计算
			Map<String, Object> str = alg.computing(cd);
			System.out.println("service_figure执行！");
			return str;
		}else if("rts".equals(type)){
			
			alg = new RTSAlgorithmImpl();
			map.put("conditon", cd);
			Map<String, Object> map2 = alg.computing(map);
			return map2;
		}else{
			return new HashMap<String,Object>();
		}
	}


	@Override
	public Map<String, Object> computeBasic(int id) {

		// 1、得到要计算的算例
		Example ep = findById(id);
		// 2、计算
		// 需要的参数有IEOEIFOF、taskNum、portNum、stockNum、
		// loadCar、loadTime、tw1、tw2、driveTime、truckNum
		HashMap<String, Object> cd = new HashMap<String, Object>();
		List<Site> sites = ep.getSites();
		List<Task> tasks = ep.getTasks();

		int taskNum = tasks.size();
		// a、得到IEOEIFOF数量，第一第二时间窗，装卸货时间的数组
		int IE = 0;
		int OE = 0;
		int IF = 0;
		int OF = 0;
		String[] tw1Str = new String[tasks.size()];
		ArrayList<String> tw2List = new ArrayList<String>();
		ArrayList<Integer> loadTimeList = new ArrayList<Integer>();
			
		Iterator<Task> itt = tasks.iterator();
		for (int i =0;itt.hasNext();i++) {
			Task task = itt.next();
			//第一时间窗
			tw1Str[i] = task.getTw1();
			String taskType = task.getTaskType();
			if ("IE".equals(taskType))
				IE++;
			if ("OE".equals(taskType))
				OE++;
			//重箱任务
			if ("IF".equals(taskType) || "OF".equals(taskType)) {
				if ("IF".equals(taskType))
					IF++;
				if ("OF".equals(taskType))
					OF++;
				//第二时间窗
				tw2List.add(task.getTw2());
				//装卸货时间
				loadTimeList.add(task.getLoadTime());
			}
		}
		//第二时间窗、装卸货的对象数组
		Object[] tw2Obj = tw2List.toArray();
		Object[] loadTimeObj = loadTimeList.toArray();
		//转换为String数组、int数组
		String[] tw2Str = new String[tw2Obj.length];
		int[] loadTime = new int[loadTimeObj.length];
		for(int i=0;i<tw2Obj.length;i++){
			tw2Str[i] = (String) tw2Obj[i];			
			loadTime[i] = (Integer) loadTimeObj[i];
		}
		int[][] tw1 = ArrStrHandler.handleTW(tw1Str);
		int[][] tw2 = ArrStrHandler.handleTW(tw2Str);			

		// b、dao 得到port、stock的数量和truckNum的值
		int port = 0;
		int stock = 0;
		String[] posStr = new String[sites.size()];

		ArrayList<Integer> arrList = new ArrayList<Integer>();
		Iterator<Site> its = sites.iterator();
		for (int i = 0; its.hasNext(); i++) {
			Site site = its.next();
			posStr[i] = site.getPosition();
			if ("port".equals(site.getType()))
				port++;
			if ("stock".equals(site.getType())) {
				stock++;
				arrList.add(site.getCarNum());
			}
		}
		Object[] truckNumObj = arrList.toArray();
		int[] truckNum = new int[truckNumObj.length];
		for(int i=0;i<truckNumObj.length;i++){
			truckNum[i] = (Integer) truckNumObj[i];			
		}

		// c、得到site表的postion
		int[][] position = ArrStrHandler.handleTW(posStr);// 二维数组
		// 转换为距离矩阵
		double[][] dis = ArrStrHandler.getDis(position);
		// 转换为行驶时间矩阵
		double[][] driveTime = ArrStrHandler.getDriveTime(dis);
		
		// d、设置loadCar
		int loadCar = 5;
		// e、封装为map
		// 需要的参数有IEOEIFOF、taskNum、portNum、stockNum、
		// loadCar、loadTime、tw1、tw2、driveTime、truckNum
		cd.put("IE", IE);
		cd.put("OE", OE);
		cd.put("IF", IF);
		cd.put("OF", OF);
		cd.put("taskNum", taskNum);
		cd.put("portNum", port);
		cd.put("stockNum", stock);
		cd.put("loadCar", loadCar);
		cd.put("loadTime", loadTime);
		cd.put("tw1", tw1);
		cd.put("tw2", tw2);
		cd.put("driveTime", driveTime);
		cd.put("truckNum", truckNum);
		
		return cd;
	}

	

}
