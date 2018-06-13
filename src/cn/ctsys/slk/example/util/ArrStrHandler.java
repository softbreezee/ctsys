package cn.ctsys.slk.example.util;

import org.junit.Test;

/**
 * 工具类，处理类型转换
 * @author Leon
 * 
 */

public class ArrStrHandler {

	
	/**
	 * 距离矩阵转换行驶时间矩阵
	 * @param distance
	 * @return
	 * 修改：
	 * 第一次，将返回值从int[][]变为double[][]
	 */
	public static double[][] getDriveTime(double[][] distance) {
//		int[][] driveTime = new int[distance[0].length][distance[0].length];
		double[][] driveTime = new double[distance[0].length][distance[0].length];
		for (int i = 0; i < distance[0].length; i++) {
			for (int j = 0; j < distance[0].length; j++) {
//				driveTime[i][j] = (int) (distance[i][j]/300);
				driveTime[i][j] = distance[i][j]/300;
				driveTime[j][i] = driveTime[i][j];
			}
		}
		
		return driveTime;
	}
	
	
	
	/**
	 * 二维位置数组转距离矩阵
	 * @param position
	 * @return
	 */
	public static double[][] getDis(int[][] position) {
		int count = position[0].length;
		double[][] dis = new double[count][count];
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < count; j++) {
				double x = Math.pow(position[0][i] - position[0][j], 2);
				double y = Math.pow(position[1][i] - position[1][j], 2);
				dis[i][j] = Math.pow(x + y, 0.5);
			}
		}
		return dis;
	}

	/**
	 * 处理时间窗
	 * 
	 * @param str
	 * @return
	 */
	public static int[][] handleTW(String[] str) {
		int[][] tw = new int[2][str.length];
		for (int i = 0; i < str.length; i++) {
			String[] split = str[i].split(",");
			tw[0][i] = Integer.parseInt(split[0]);
			tw[1][i] = Integer.parseInt(split[1]);
		}

		return tw;
	}

	/**
	 * 接受集卡数量的字符换，并转换成为int[]
	 * 
	 * @param str
	 * @return
	 */
	public static int[] handleCar(String str) {
		String[] split = str.split(",");
		int[] arr = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			arr[i] = Integer.parseInt(split[i]);
		}
		return arr;
	}

}
