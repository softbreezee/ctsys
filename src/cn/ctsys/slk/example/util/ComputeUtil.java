package cn.ctsys.slk.example.util;

import java.util.Arrays;
import java.util.Map;

public class ComputeUtil {
	
	/**
	 * 条件的判断
	 * 
	 * 
	 * @param taskNum
	 * @param IENum
	 * @param OENum
	 * @param IFNum
	 * @param portNum
	 * @param stockNum
	 * @param LOADTIME
	 * @param loadTime2
	 * @param tw1
	 * @param tw2
	 * @param driveTime
	 * @param serviceTime
	 * @param nodeTW
	 * @param transTime
	 * 
	 * double[] serviceTime = new double[taskNum];
		// 任务顶点活动时间
		double[][] nodeTW = new double[2][taskNum];
		// 弧转换时间
		double[][] transTime = new double[taskNum + stockNum][taskNum
				+ stockNum];
	 */
	public static void condition(int taskNum, int IENum, int OENum, int IFNum,
			int portNum, int stockNum, final int LOADTIME, int[] loadTime2,
			int[][] tw1, int[][] tw2, double[][] driveTime, double[] serviceTime,
			double[][] nodeTW, double[][] transTime) {
		/**
		 * 服务时间
		 */
		for (int i = 0; i < taskNum; i++) {
			// i属于IE+OE
			if (i < (IENum + OENum)) {
				serviceTime[i] = LOADTIME;
			}
			// i属于IF
			if (i >= (IENum + OENum) && i < (IFNum + IENum + OENum)) {
				serviceTime[i] = (
						Math.max(
							(tw2[0][i - IENum - OENum] - tw1[1][i]),
							//第一个客户就是if0对应的
							(LOADTIME + driveTime[0][i + stockNum + portNum - IENum - OENum])
						) +
						loadTime2[i - IENum - OENum] +LOADTIME);
			}
			// i属于OF
			if (i >= (IFNum + IENum + OENum) && i < taskNum) {
				serviceTime[i] = (
						Math.max(
							(tw2[0][i - IENum - OENum] - tw1[1][i]),
							(loadTime2[i - IENum - OENum] +LOADTIME + driveTime[i + stockNum + portNum - IENum - OENum][0])
						) +
						LOADTIME);
			}
		}

		/**
		 * 任务顶点的时间窗的开始时刻与结束时刻
		 * 
		 */
		for (int i = 0; i < taskNum; i++) {
			// i属于IE+OE
			if (i < (IENum + OENum)) {
				nodeTW[0][i] = tw1[0][i];
				nodeTW[1][i] = tw1[1][i];
			}
			// i属于IF
			if (i >= (IENum + OENum) && i < (IFNum + IENum + OENum)) {
				nodeTW[0][i] = 
						Math.min(
								Math.max(
										tw1[0][i], 
										tw2[0][i - IENum - OENum] - LOADTIME - driveTime[0][i -IENum-OENum+ portNum + stockNum]
										),
								tw1[1][i]
								);

				nodeTW[1][i] = 
						Math.min(
								tw1[1][i], 
								tw2[1][i - IENum - OENum]- LOADTIME - driveTime[0][i -IENum-OENum+ portNum + stockNum]
								);
			}
			// i属于OF
			if (i >= (IFNum + IENum + OENum) && i < taskNum) {

				nodeTW[0][i] = 
						Math.min(
							Math.max(
									tw1[0][i], 
									tw2[0][i - IENum - OENum] - LOADTIME - driveTime[i + portNum + stockNum - IENum - OENum][0] - loadTime2[i - IENum - OENum]
									), 
							tw1[1][i]
							);

				nodeTW[1][i] = 
						Math.min(
								tw1[1][i], 
								tw2[1][i - IENum - OENum] - LOADTIME - driveTime[i + portNum + stockNum - IENum - OENum][0] - loadTime2[i - IENum - OENum]
								);
			}
		}

		/**
		 * 装换时间Tij 从零开始先堆场，后任务
		 */
		double[] drive0 = Arrays.copyOfRange(driveTime[0], portNum, stockNum
				+ portNum);
		Arrays.sort(drive0);

		for (int i = 0; i < taskNum + stockNum; i++) {
			// i 属于 堆场
			if (i < stockNum) {
				for (int j = 0; j < taskNum + stockNum; j++) {
					// j属于 堆场
					if (j < stockNum) {
						transTime[i][j] = 0;
					}
					// j属于 OE
					else if (j >= stockNum + IENum && j < stockNum + IENum + OENum) {
						transTime[i][j] = LOADTIME + driveTime[i + portNum][0];
					}
					// j属于 OF
					else if (j >= stockNum + IENum + OENum + IFNum && j < taskNum + stockNum) {
						transTime[i][j] = LOADTIME * 2 + driveTime[i + portNum][j + portNum - IENum - OENum];
//						transTime[i][j] = LOADTIME * 2 + driveTime[i + portNum][j + portNum];
					}
					// j属于 IF U IE
					else {
						transTime[i][j] = driveTime[i + portNum][0];
					}
				}
			}
			// i 属于 IE
			else if (i >= stockNum && i < stockNum + IENum) {
				for (int j = 0; j < taskNum + stockNum; j++) {
					// j属于 堆场
					if (j < stockNum) {
						transTime[i][j] = LOADTIME + driveTime[0][j + portNum];
					}
					// j属于 OE
					else if (j >= stockNum + IENum && j < stockNum + IENum + OENum) {
						transTime[i][j] = 0;
					}
					// j属于 OF
					else if (j >= stockNum + IENum + OENum + IFNum && j < taskNum + stockNum) {
						transTime[i][j] = LOADTIME + driveTime[0][j + portNum - IENum - OENum];
//						transTime[i][j] = LOADTIME + driveTime[0][j + portNum ];
					}
					// j属于 IF U IE
					else {
						transTime[i][j] = LOADTIME + drive0[0] * 2;
					}
				}
			}
			// i 属于 IF
			else if (i >= stockNum + IENum + OENum && i < stockNum + IENum + OENum + IFNum) {
				for (int j = 0; j < taskNum + stockNum; j++) {
					// j属于 堆场
					if (j < stockNum) {
						transTime[i][j] = LOADTIME * 2 + driveTime[i + portNum - IENum - OENum][j + portNum];
					}
					// j属于 OE
					else if (j >= stockNum + IENum && j < stockNum + IENum + OENum) {
						transTime[i][j] = LOADTIME + driveTime[i + portNum - IENum - OENum][0];
					}
					// j属于 OF
					else if (j >= stockNum + IENum + OENum + IFNum && j < taskNum + stockNum) { 
						if (driveTime[i + portNum - IENum - OENum  ][j + portNum - IENum - OENum ] > 0) {
							transTime[i][j] = LOADTIME * 2  + driveTime[i + portNum - IENum - OENum  ][j + portNum - IENum - OENum ];
						} else {
							transTime[i][j] = 0;
						}
					}
					// j属于 IF U IE
					else {
						double[] ds = Arrays.copyOfRange(  driveTime[i + portNum - IENum - OENum], portNum, stockNum + portNum);
						Arrays.sort(ds);
						transTime[i][j] = LOADTIME * 2 + ds[0] + drive0[0];
					}
				}

			}
			// i 属于 OF U OE
			else {
				for (int j = 0; j < taskNum + stockNum; j++) {

					// j属于 堆场
					if (j < stockNum) {
						transTime[i][j] = driveTime[0][j + portNum];
					}
					// j属于 OE
					else if (j >= stockNum + IENum && j < stockNum + IENum + OENum) {
						transTime[i][j] = drive0[0] * 2 + LOADTIME;
					}
					// j属于 OF
					else if (j >= stockNum + IENum + OENum + IFNum && j < taskNum + stockNum) {
						double[] ds = Arrays.copyOfRange( driveTime[j + portNum - IENum - OENum], portNum, stockNum + portNum);
						Arrays.sort(ds);
						transTime[i][j] = LOADTIME * 2 + ds[0] + drive0[0]; 
					}
					// j属于 IF U IE
					else {
						transTime[i][j] = 0;
					}
				}

			}
		}
	}


}
