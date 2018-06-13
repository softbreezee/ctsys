package cn.ctsys.slk.example.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据condition对象创建初始的数据，是否需要保存在数据库中！
 * 方案一
 * 不保存，则不能查看这些数据，或者说算例不能二次创建
 * 
 * 方案二
 * 保存，可以查看修改这些数据，可是创建二次算例
 * 
 * 2017、8、6号，开发停滞在第二时间窗的处理，要求行驶时间。第二时间窗是按照任务的顺序来生成。
 * 但是行驶时间矩阵是按照港口、堆场、客户的顺序生成的。
 * 客户不一定与任务的个数相等！
 * 客户点一定等于重箱任务的的个数！客户点只会在生成的时候出现重合的可能！
 * 但是客户点不一定是按照IE OE IF OF顺序排列的！！
 * 
 * 解决办法：暂时查看别人的生成初始信息的程序改进，完成后台方案一二！
 * @author Leon
 */
public class RandomCreate {
	private static RandomCreate create;
	private static Map<String, Object> map;

	static {
		create = new RandomCreate();
	}

	/**
	 * 静态方法，提供一个map集合 封装信息： 
	 * 客户位置
	 * 港口位置
	 * 堆场位置
	 * 距离矩阵
	 * 时间矩阵
	 * 第一时间窗（taskNum） 
	 * 第二时间窗(IF+OF)
	 * 装卸货时间
	 * 距离矩阵(portNum+stockNum+taskNum)
	 * 
	 * @param ep
	 * @param ep
	 * @return
	 */
	public static Map<String, Object> getCondition(int IE,int OE,int IF,int OF,int clientNum,int portNum,int stockNum,int rangeX,int rangeY) {

		map = new HashMap<String, Object>();

		int[][] clientP = create.getPosition(clientNum,rangeX,rangeY);
		int[][] portP = create.getPosition(portNum,rangeX, rangeY);
		int[][] stockP = create.getPosition(stockNum,rangeX, rangeY);
		map.put("clientPosition", clientP);
		map.put("portPosition", portP);
		map.put("stockPosition", stockP);

		double[][] distance = create.getDis(clientP, stockP, portP);
		map.put("distance", distance);
		
		int[][] driveTime = create.getDriveTime(distance);
		map.put("driveTime", driveTime);
		
		Map<String, Object> tw = create.getTW(IE,OE,IF,OF,portNum,stockNum, distance);
		map.put("tw1", tw.get("tw1"));
		map.put("tw2", tw.get("tw2"));
		
		
		int[] loadTime = create.getLoadTime(IF,OF);
		map.put("loadTime", loadTime);

		return map;
	}


	//行驶时间矩阵
	private int[][] getDriveTime(double[][] distance) {
		int[][] driveTime = new int[distance[0].length][distance[0].length];
		for (int i = 0; i < distance[0].length; i++) {
			for (int j = i; j < distance[0].length; j++) {
				driveTime[i][j] = (int) (distance[i][j]/300);
				driveTime[j][i] = driveTime[i][j];
			}
		}
		
		return driveTime;
	}


	/**
	 * 时间窗问题：1、范围2、行驶的时间
	 * 
	 * @param IE
	 * @param OE
	 * @param IF
	 * @param OF
	 * @param rangeOfClient
	 * @param rangeOfstock
	 */
	private Map<String, Object> getTW(int IENum,int OENum,int IFNum,int OFNum,int portNum,int stockNum,double[][] distance) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 第一时间窗
		int taskNum = IENum+ IFNum + OENum + OFNum;
		int[][] tw1 = new int[2][taskNum];
		// 注意：：随机数应该乘的是范围！！
		for (int i = 0; i < taskNum; i++) {
			//第一时间窗是8：00 - 12：00，宽度是0-3小时
			tw1[0][i] = (int) (Math.random() * 240);
			tw1[1][i] = tw1[0][i] + (int) (Math.random() * 180);
		}
		map.put("tw1", tw1);

		// 第二时间窗,只有重箱任务有第二时间窗
		int num = OFNum + IFNum;
		int[][] tw2 = new int[2][num];
		// 注意：：随机数应该乘的是范围！！
		for (int i = 0; i < num; i++) {
			// 第二时间窗的开始时刻为第一时间窗的开始时刻+客户点与港口之间的行驶距离
			tw2[0][i] = tw1[0][i + taskNum - num] +(int)(distance[0][i+portNum+stockNum]/500) ;
			//宽度是2-5小时
			tw2[1][i] = tw2[0][i] + (int) (Math.random() * 180+120);
		}
		map.put("tw2", tw2);

		return map;
	}
	
	
	/**
	 * 生成初始位置，是否保存？？？是否存储到数据库？？？
	 */
	private int[][] getPosition(int Num, int RangeOfX, int RangeOfY) {
		int[][] arr = new int[2][Num];
		for (int i = 0; i < Num; i++) {
			arr[0][i] = (int) (Math.random() * RangeOfX);
			arr[1][i] = (int) (Math.random() * RangeOfY);
		}
		return arr;
	}
	
	
	/**
	 * 生成距离矩阵
	 */
	private double[][] getDis(int[][] client, int[][] stock, int[][] port) {

		int clientNum = 0;
		if (client != null) {
			clientNum = client[0].length;
		}

		int num = clientNum + stock[0].length + port[0].length;
		double[][] dis = new double[num][num];
		// 港口、堆场、客户点
		// 生成距离矩阵
		for (int i = 0; i < num; i++) {
			for (int j = i; j < num; j++) {
				if (i < port[0].length) {
					if (j < port[0].length) {
						double x = Math.pow(port[0][i] - port[0][j], 2);
						double y = Math.pow(port[1][i] - port[1][j], 2);
						dis[i][j] = Math.pow(x + y, 0.5);
					}
					if (j >= port[0].length
							&& j < stock[0].length + port[0].length) {
						double x = Math.pow(port[0][i] - stock[0][j - port[0].length], 2);
						double y = Math.pow(port[1][i] - stock[1][j - port[0].length], 2);
						dis[i][j] = Math.pow(x + y, 0.5);
					}
					if (j >= stock[0].length + port[0].length) {
						double x = Math.pow(port[0][i] - client[0][j - stock[0].length - port[0].length], 2);
						double y = Math.pow(port[1][i] - client[1][j - stock[0].length - port[0].length], 2);
						dis[i][j] = Math.pow(x + y, 0.5);
					}

				}

				if (i >= port[0].length && i < stock[0].length + port[0].length) {
					if (j >= port[0].length && j < stock[0].length + port[0].length) {
						double x = Math.pow(stock[0][i - port[0].length] - stock[0][j - port[0].length], 2);
						double y = Math.pow(stock[1][i - port[0].length]- stock[1][j - port[0].length], 2);
						dis[i][j] = Math.pow(x + y, 0.5);
					}
					if (j >= stock[0].length + port[0].length) {
						double x = Math.pow(stock[0][i - port[0].length] - client[0][j - stock[0].length - port[0].length], 2);
						double y = Math.pow(stock[1][i - port[0].length] - client[1][j - stock[0].length - port[0].length], 2);
						dis[i][j] = Math.pow(x + y, 0.5);
					}

				}

				if (i >= stock[0].length + port[0].length
						&& j >= stock[0].length + port[0].length) {
					double x = Math.pow(client[0][i - stock[0].length - port[0].length] - client[0][j - stock[0].length - port[0].length],2);
					double y = Math.pow(client[1][i - stock[0].length - port[0].length] - client[1][j - stock[0].length - port[0].length],2);
					dis[i][j] = Math.pow(x + y, 0.5);
				}
				dis[j][i] = dis[i][j];
			}
		}

		return dis;

	}

	/**
	 * 生成装卸时间
	 *  --只有重箱任务有装卸货时间（第一次修改）
	 */
	private int[] getLoadTime(int IFNum,int OFNum) {
		int num =IFNum+ OFNum;
//		int num =ep.getIFNum() +ep.getIENum()+ ep.getOFNum()+ep.getOENum();
		int[] loadTime = new int[num];
		for (int i = 0; i < num; i++) {
			loadTime[i] = (int) (Math.random() * 15 + 5);
		}
		return loadTime;
	}

}
