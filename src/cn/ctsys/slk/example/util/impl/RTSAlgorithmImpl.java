/**
 * @author Leon
 *
 */
package cn.ctsys.slk.example.util.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ctsys.slk.example.entity.NeighbourResult;
import cn.ctsys.slk.example.entity.RTSresult;
import cn.ctsys.slk.example.util.Algorithm;
import cn.ctsys.slk.example.util.ComputeUtil;

public class RTSAlgorithmImpl implements Algorithm{
	
		/** 转换时间矩阵 */
		private double[][] transferMatrix;
		/** 每个顶点的服务时间 */
		private double[] ServeTime;
		/** 任务顶点的时间窗上限 */
		private double[] Ta;
		/** 任务顶点的时间窗下限 */
		private double[] Tb;
		/** depotNum堆场数 */
		private int depotNum;
		/** 定义1个解中 结点的数量 */
		private int nodeNum;
		/** 每次搜索邻居个数 */
		private int neighbourhoodNum;
		/** 禁忌长度 */
		private double tabuTableLength;
		/** 禁忌表 */
		private List<int[]> tabuTable;
		/** 当前路线 */
		private int[] route;
		/** 最好的路径 */
		private int[] bestRoute;
		/** 最佳路径总长度 */
		private double bestEvaluation;
		/** 循环次数 */
		private int MAX_GEN;
		/** 禁忌表增加系数 */
		private double IncLength;
		/** 禁忌表减少系数 */
		private double decLength;
		/** 未发现循环节次数 */
		private int maxNumWithoutFind;
		/** 循环n次，触发逃逸机制 */
		private int maxTimesRepeated;
		private int tasksNum;
		private int[] carNum;
		

	
		
		@Override
		public Map<String, Object> computing(Map<String, Object> cd) {
			/**
			 * 1、数据库信息
			 */
			Map<String,Object> condition= (Map<String, Object>) cd.get("conditon");
			double[][] driveTime = (double[][]) condition.get("driveTime");
			int IE = (Integer) condition.get("IE");
			int OE = (Integer) condition.get("OE");
			int IF = (Integer) condition.get("IF");
			int OF = (Integer) condition.get("OF");
			int taskNum = (Integer) condition.get("taskNum");
			tasksNum = taskNum;
			int portNum = (Integer) condition.get("portNum");
			int stockNum = (Integer) condition.get("stockNum");
			depotNum = stockNum;
			int loadCar = (Integer) condition.get("loadCar");
			int[] loadTime = (int[]) condition.get("loadTime");
			int[][] tw1 = (int[][]) condition.get("tw1");
			int[][] tw2 = (int[][]) condition.get("tw2");
			//注意，不要丢掉
			int[] truckNum = (int[]) condition.get("truckNum");
			carNum = truckNum;
			
			//????nodeNum为何意，是否正确，有待验证！
//			nodeNum = missions.size();
//			for (int j = 0; j < eDepots.size(); j++) {
//				nodeNum += eDepots.get(j).getTruckNum(); 
//			}
			nodeNum = taskNum;
			for (int j = 0; j < stockNum; j++) {
				nodeNum += truckNum[j]; 
			}
			
			
			//邻域解数
			neighbourhoodNum =(Integer) cd.get("neighbourNum");
			//禁忌表长度
			tabuTableLength =  (Double) cd.get("tabuLengthNum");
			//循环次数
			MAX_GEN =  (Integer) cd.get("IterationNum");
			//禁忌表长度增加系数
			IncLength =  (Double) cd.get("incLength");
			//禁忌表长度减小系数
			decLength =  (Double) cd.get("decLength");
			//最大重复次数
			maxTimesRepeated =  (Integer) cd.get("maxTimesRepeated");
			//最大循环未找到重复解次数
			maxNumWithoutFind =  (Integer) cd.get("maxNumWithoutFind");
			//缺少最好的路径、最佳路径总长度、禁忌表、当前路径
			
			
			/**
			 * 2、条件，用来确定服务时间、顶点活动时间、顶点转换时间的值
			 */
			/**顶点分为任务顶点和出发/返回顶点！***/
			// 任务顶点服务时间
			double[] serviceTime = new double[taskNum];
			// 任务顶点活动时间
			double[][] nodeTW = new double[2][taskNum];
			// 弧转换时间
			double[][] transTime = new double[taskNum + stockNum][taskNum + stockNum];
			ComputeUtil.condition(taskNum, IE, OE, IF, portNum, stockNum, loadCar,
					loadTime, tw1, tw2, driveTime, serviceTime, nodeTW, transTime);
			ServeTime = serviceTime;
			Ta = nodeTW[0];
			Tb = nodeTW[1];
			transferMatrix = transTime;
			
			
			
			long startTime=System.currentTimeMillis();
			//生成初始解！
			int[] vertexRoutes = InitialSolution();
			//计算
			RTSresult result = RTSalgorithm(vertexRoutes);
			long endTime = System.currentTimeMillis();
			Double time = (double) (endTime-startTime);
			Map<String,Object> rs = new HashMap<String,Object>();
			rs.put("rtsresult", result);
			rs.put("computeTime",time.toString());
			
			return rs;
		}

		/**用map传入数据
		public RTSalgorithm(double[][] transferMatrix, double[] serveTime,
				double[] ta, double[] tb, List<EMission> missions,
				List<EDepot> eDepots, int depotNum, int nodeNum,
				int neighbourhoodNum, double tabuTableLength, int mAX_GEN,
				double incLength, double decLength, int maxNumWithoutFind,
				int maxTimesRepeated) {
			super();
			this.transferMatrix = transferMatrix;
			ServeTime = serveTime;
			Ta = ta;
			Tb = tb;
			this.missions = missions;
			this.eDepots = eDepots;
			this.depotNum = depotNum;
			this.nodeNum = nodeNum;
			this.neighbourhoodNum = neighbourhoodNum;
			this.tabuTableLength = tabuTableLength;
			MAX_GEN = mAX_GEN;
			IncLength = incLength;
			this.decLength = decLength;
			this.maxNumWithoutFind = maxNumWithoutFind;
			this.maxTimesRepeated = maxTimesRepeated;
		}
		*/

		// 运用Cluster Method 生成初始解
		/*
		 * transferMatrix[depotNum+missionNum] 是各顶点间的转换时间 ServeTime[missionNum]
		 * 是任务定点上的服务时间 Ta[missionNum] 每个任务顶点的时间窗上限 Tb[missionNum] 每个任务顶点的时间窗下限
		 * depotNum 堆场数 missionNum 任务数 List<EMission> missions 客户任务信息存放 List<EDepot>
		 * eDepots 堆场信息存放
		 */
		public int[] InitialSolution() {
			List<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
			boolean iterationValue = true;

			double[][] copyTransferMatrix = new double[depotNum + tasksNum][depotNum
					+ tasksNum];
			double[] copyTa = new double[tasksNum];
			double[] copyTb = new double[tasksNum];
			double[] copyServeTime = new double[tasksNum];
			// transferMatrix复制，名称为copyTransferMatrix
			for (int i = 0; i < transferMatrix.length; i++) {
				for (int j = 0; j < transferMatrix[i].length; j++) {
					copyTransferMatrix[i][j] = transferMatrix[i][j];
				}
			}
			// Ta,Tb,ServeTime复制
			for (int i = 0; i < tasksNum; i++) {
				copyTa[i] = Ta[i];
				copyTb[i] = Tb[i];
				copyServeTime[i] = ServeTime[i];
			}
			for (int i = 0; i < tasksNum; i++) {
				// j定义为终点
				for (int j = 0; j < tasksNum; j++) {
					// 找出不松弛的路径,标记为2
					if (i == j) {
						copyTransferMatrix[i + depotNum][j + depotNum] = Integer.MAX_VALUE; // 不松弛，则转换时间为一个很大的常数
					}
				}
			}

			while (iterationValue) {
				double minTransTime = Integer.MAX_VALUE / 2;
				int minEnd = Integer.MAX_VALUE;
				int minStart = Integer.MAX_VALUE;
				boolean flag = false;
				// i为起点
				for (int i = 0; i < tasksNum; i++) {
					// j定义为终点
					for (int j = 0; j < tasksNum; j++) {
						// 在转换路径中找到最短的转换时间
						if (copyTransferMatrix[i + depotNum][j + depotNum] < minTransTime) {
							// 判断最小值是不是更新了，如果更新了为True
							flag = true;
							minTransTime = copyTransferMatrix[i + depotNum][j
									+ depotNum];
							minStart = i;
							minEnd = j;
						}
					}
				}

				if (flag == true) {
					//
					System.out.println("minStart = " + minStart + "; minEnd = "
							+ minEnd);
					System.out.println("-------flag =" + flag);
					// 设置新的服务时间、时间窗
					double kServeTime = 0;
					double kTa = 0;
					double kTb = 0;
					if (flag) {
						// 服务时间 kServeTime
						kServeTime = copyTa[minEnd] - copyTb[minStart]
								+ copyServeTime[minEnd];
						if (copyTa[minEnd] - copyTb[minStart] < copyServeTime[minStart]
								+ copyTransferMatrix[minStart + depotNum][minEnd
										+ depotNum]) {
							kServeTime = copyServeTime[minStart]
									+ copyTransferMatrix[minStart + depotNum][minEnd
											+ depotNum] + copyServeTime[minEnd];
						}
						// 时间窗上限 kTa
						double kTa_first = copyTa[minStart];
						kTa = copyTb[minStart];
						if (copyTa[minStart] < copyTa[minEnd]
								- copyServeTime[minStart]
								- copyTransferMatrix[minStart + depotNum][minEnd
										+ depotNum]) {
							kTa_first = copyTa[minEnd]
									- copyServeTime[minStart]
									- copyTransferMatrix[minStart + depotNum][minEnd
											+ depotNum];
						}
						if (kTa_first < kTa) {
							kTa = kTa_first;
						}
						// 时间窗下限 kTb
						kTb = copyTb[minStart];
						if (copyTb[minEnd]
								- copyServeTime[minStart]
								- copyTransferMatrix[minStart + depotNum][minEnd
										+ depotNum] < kTb) {
							kTb = copyTb[minEnd]
									- copyServeTime[minStart]
									- copyTransferMatrix[minStart + depotNum][minEnd
											+ depotNum];
						}

					}

					int[] time = { minStart, minEnd };
					// 设置新的转换时间 transferMatrix[i][] transferMatrix[][j]
					for (int i = 0; i < tasksNum; i++) {
						copyTransferMatrix[minStart + depotNum][i + depotNum] = Integer.MAX_VALUE;
						copyTransferMatrix[i + depotNum][minEnd + depotNum] = Integer.MAX_VALUE;
					}
					copyTransferMatrix[minEnd + depotNum][minStart + depotNum] = Integer.MAX_VALUE;

					// 把i j放入数组中
					if (list.isEmpty()) {
						ArrayList<Integer> array = new ArrayList<Integer>();
						array.add(minStart);
						array.add(minEnd);
						list.add(array);
						for (int i = 0; i < time.length; i++) {
							int j = time[i];
							copyServeTime[j] = kServeTime;
							copyTa[j] = kTa;
							copyTb[j] = kTb;
						}
					} else {
						int start = Integer.MAX_VALUE;
						int end = Integer.MAX_VALUE;
						int num = 0;
						for (int i = 0; i < list.size(); i++) {
							// 获取每个数组的长度
							int itemLengthOfList = list.get(i).size();
							if (list.get(i).get(0) == minEnd) {
								num = num + 1;
								start = i;
							}
							if (list.get(i).get(itemLengthOfList - 1) == minStart) {
								num = num + 2;
								end = i;
							}
						}
						if (num == 0) {
							ArrayList<Integer> item = new ArrayList<Integer>();
							item.add(minStart);
							item.add(minEnd);
							list.add(item);
							for (int i = 0; i < time.length; i++) {
								int j = time[i];
								copyServeTime[j] = kServeTime;
								copyTa[j] = kTa;
								copyTb[j] = kTb;
							}
						} else if (num == 1) {
							list.get(start).add(0, minStart);
							for (int i = 0; i < list.get(start).size(); i++) {
								int j = list.get(start).get(i);
								copyServeTime[j] = kServeTime;
								copyTa[j] = kTa;
								copyTb[j] = kTb;
							}
						} else if (num == 2) {
							list.get(end).add(minEnd);
							for (int i = 0; i < list.get(end).size(); i++) {
								int j = list.get(end).get(i);
								copyServeTime[j] = kServeTime;
								copyTa[j] = kTa;
								copyTb[j] = kTb;
							}
						} else if (num == 3) {
							list.get(end).addAll(list.get(start));

							for (int i = 0; i < list.get(end).size(); i++) {
								int j = list.get(end).get(i);
								copyServeTime[j] = kServeTime;
								copyTa[j] = kTa;
								copyTb[j] = kTb;
							}
							list.remove(start);
						}

					}
				}
				// 判断要不要循环，当路径中存在< double.max_value 的时候 继续循环
				// i为起点
				iterationValue = false;

				minTransTime = Integer.MAX_VALUE / 2;
				for (int i = 0; i < tasksNum; i++) {
					// j定义为终点
					for (int j = 0; j < tasksNum; j++) {
						// 找出不松弛的路径,标记为2
						if (copyTb[j] <= copyTa[i] + copyServeTime[i]
								+ copyTransferMatrix[i + depotNum][j + depotNum]) {
							copyTransferMatrix[i + depotNum][j + depotNum] = Integer.MAX_VALUE; // 不松弛，则转换时间为一个很大的常数
						}
						// 在转换路径中找到是不是存在不松弛的路径
						if (copyTransferMatrix[i + depotNum][j + depotNum] < minTransTime) {
							// 判断最小值是不是更新了，如果更新了为True
							iterationValue = true;
						}
					}
				}

				if (iterationValue == false) {
					boolean[] misslist = new boolean[tasksNum];
					for (int j2 = 0; j2 < tasksNum; j2++) {
						for (int k = 0; k < list.size(); k++) {
							for (int k2 = 0; k2 < list.get(k).size(); k2++) {

								if (j2 == list.get(k).get(k2)) {
									misslist[j2] = true;
								}
							}
						}
					}
					for (int i = 0; i < misslist.length; i++) {
						if (misslist[i] == false) {
							ArrayList<Integer> array = new ArrayList<Integer>();
							array.add(i);
							list.add(array);
						}
					}
				}
				System.out.println(iterationValue);
			}

			// 获取每个depot中卡车数目
			int[] numofTruck = new int[depotNum];
			int totalTruckNum = 0;
			for (int i = 0; i < depotNum; i++) {
				numofTruck[i] = carNum[i];
				totalTruckNum += numofTruck[i];
			}

			// 把起点，加入每条路径中。
			for (int i = 0; i < list.size(); i++) {
				ArrayList<Integer> item = list.get(i);
				int minStartDepot = Integer.MAX_VALUE;
				double minTime = Integer.MAX_VALUE;
				// 选择初始堆场
				for (int k = 0; k < depotNum; k++) {
					if (numofTruck[k] >= 1) {
						if (transferMatrix[k][depotNum + item.get(0)] < minTime) {
							minTime = transferMatrix[k][depotNum + item.get(0)];
							minStartDepot = k;
						}
					}
				}
				numofTruck[minStartDepot] = numofTruck[minStartDepot] - 1;
				item.add(0, minStartDepot);
				for (int j = 1; j < item.size(); j++) {
					int index = item.get(j) + depotNum;
					item.set(j, index);
				}
			}
			// 剩余卡车数目
			List<Integer> unsignedList = new ArrayList<Integer>();
			for (int i = 0; i < numofTruck.length; i++) {
				int j = numofTruck[i];
				for (int j2 = 0; j2 < j; j2++) {
					unsignedList.add(i);
				}
			}

			// 把list<Arraylist>转变为一个二维数组
			for (int i = 1; i < list.size(); i++) {
				list.get(0).addAll(list.get(i));
			}
			int[] vertexRoutes = new int[tasksNum + totalTruckNum];
			for (int i = 0; i < vertexRoutes.length; i++) {
				if (i < list.get(0).size()) {
					vertexRoutes[i] = list.get(0).get(i);
				} else {
					vertexRoutes[i] = unsignedList.get(i - list.get(0).size());
				}
			}
			return vertexRoutes;
		}

		/** 复制编码体，复制Gha到Ghb */
		public void copyGh(int[] Gha, int[] Ghb) {
			for (int i = 0; i < nodeNum; i++) {
				Ghb[i] = Gha[i];
			}
		}

		/** 计算路线的总距离 */
		public double evaluate(int[] chr) {
			double transferTime = 0;

			for (int i = 0; i < chr.length - 1; i++) {
				if (chr[i + 1] >= depotNum) {
					transferTime += transferMatrix[chr[i]][chr[i + 1]];
				} else if ((chr[i] >= depotNum) && (chr[i + 1] < depotNum)) {
					// 需要选择距离最短的depot
					double minTimeTodepot = transferMatrix[chr[i]][0];
					if (depotNum >= 2) {
						for (int j = 1; j < depotNum; j++) {
							if (minTimeTodepot > transferMatrix[chr[i]][j]) {
								minTimeTodepot = transferMatrix[chr[i]][j];
							}
						}
					}
					transferTime += minTimeTodepot;
					// System.out.println(i+"------"+minTimeTodepot);
				}
			}
			if (chr[nodeNum - 1] >= depotNum) {
				double minTimeTodepot = transferMatrix[chr[nodeNum - 1]][0];
				if (depotNum >= 2) {
					for (int j = 1; j < depotNum; j++) {
						if (minTimeTodepot > transferMatrix[chr[nodeNum - 1]][j]) {
							minTimeTodepot = transferMatrix[chr[nodeNum - 1]][j];
						}
					}
				}
				transferTime += minTimeTodepot;
			}

			return transferTime;
		}

		/**
		 * 转换为路径String
		 * */
		public String routeToString(int[] chr) {
			String routeString = "";

			// 编码，起始城市,城市1,城市2...城市n
			for (int i = 0; i < nodeNum - 1; i++) {
				if ((chr[i] < depotNum) && (chr[i + 1] >= depotNum)) {
					routeString = routeString + chr[i] + "," + chr[i + 1];
				} else if ((chr[i] >= depotNum) && (chr[i + 1] < depotNum)) {
					// 需要选择距离最短的depot
					int best = 0;
					double minTimeTodepot = transferMatrix[chr[i]][0];
					if (depotNum >= 2) {
						for (int j = 1; j < depotNum; j++) {
							if (minTimeTodepot > transferMatrix[chr[i]][j]) {
								minTimeTodepot = transferMatrix[chr[i]][j];
								best = j;
							}
						}
					}
					routeString = routeString + "," + best + ";";
				} else if ((chr[i] >= depotNum) && (chr[i + 1] >= depotNum)) {
					routeString = routeString + "," + chr[i + 1];
				}
			}
			if (chr[nodeNum - 1] >= depotNum) {
				int best = 0;
				double minTimeTodepot = transferMatrix[chr[nodeNum - 1]][0];
				if (depotNum >= 2) {
					for (int j = 1; j < depotNum; j++) {
						if (minTimeTodepot > transferMatrix[chr[nodeNum - 1]][j]) {
							minTimeTodepot = transferMatrix[chr[nodeNum - 1]][j];
						}
					}
				}
				routeString = routeString + "," + best + ";";
			}
			return routeString;
		}

		/**
		 * 随机获取一定数量的领域路径
		 * */		public NeighbourResult getNeighbourhood(int[] route) {
			// System.out.println("搜索邻域解");
			NeighbourResult result = new NeighbourResult();

			boolean isStop = false;
			List<int[]> NeighbourhoodRoutes = new ArrayList<int[]>();
			List<int[]> tempExchangeNodeList = new ArrayList<int[]>();
			int temp;
			int ran0, ran1;
			int[] tempRoute = new int[route.length];
			// List<int[]> errorList = new ArrayList<>();
			boolean iscontinue;
			for (int i = 0; i < neighbourhoodNum; i++) {

				do {
					copyGh(route, tempRoute);

					iscontinue = false;
					// 随机生成一个邻域;

					ran0 = (int) (1 + Math.floor(Math.random() * (nodeNum - 1)));
					do {
						ran1 = (int) (1 + Math.floor(Math.random() * (nodeNum - 1)));
					} while ((ran0 == ran1) || (tempRoute[ran0] == tempRoute[ran1]));

					temp = tempRoute[ran0];
					tempRoute[ran0] = tempRoute[ran1];
					tempRoute[ran1] = temp;
					// 判断是不是在求解

					// if (!errorList.isEmpty()) {
					// for (int j = 0; j < errorList.size(); j++) {
					// if ((errorList.get(j)[0]== tempRoute[ran0] &&
					// errorList.get(j)[1]==tempRoute[ran1])) {
					// iscontinue=true;
					// }
					// }
					// }

					// 在本次循环中，判断是不是已经存在过该交换（内容）
					boolean test = true;

					// if (iscontinue != true) {
					// if (!NeighbourhoodRoutes.isEmpty()) {
					// for (int j = 0; j <NeighbourhoodRoutes.size(); j++) {
					// test = true;
					// int index = 0;
					// for (int j2 = 0; j2 < nodeNum; j2++) {
					// if (NeighbourhoodRoutes.get(j)[j2] != tempRoute[j2]){
					// test = false;
					// break;
					// }
					// index = j2;
					// }
					//
					// if ((test == true)&&(index == nodeNum-1)) {
					// iscontinue = true;
					// }
					// }
					// }
					// }
					// 判断算法是不是可行

					boolean infeasible = false;
					if (iscontinue == false) {// 交换代码
					// temp = tempRoute[ran0];
					// tempRoute[ran0] = tempRoute[ran1];
					// tempRoute[ran1] = temp;
						// 判断tempRoute是不是可行
						double startTime = 0;

						for (int j = 0; j < tempRoute.length - 1; j++) {
							// 是一条新的路径
							if ((tempRoute[j] < depotNum)
									&& (tempRoute[j + 1] >= depotNum)) {
								startTime = Ta[tempRoute[j + 1] - depotNum];
							} else if ((tempRoute[j] >= depotNum)
									&& (tempRoute[j + 1] >= depotNum)) { // 不是一条新路径
								startTime += ServeTime[tempRoute[j] - depotNum]
										+ transferMatrix[tempRoute[j]][tempRoute[j + 1]];
								if (startTime > Tb[tempRoute[j + 1] - depotNum]) {
									infeasible = true;
									break;
								} else if (startTime < Ta[tempRoute[j + 1]
										- depotNum]) { // 不是一条新路径
									startTime = Ta[tempRoute[j + 1] - depotNum];
								}
							}
						}
						if (infeasible) {
							// int [] errorArray = new
							// int[]{tempRoute[ran0],tempRoute[ran1]};
							// errorList.add(errorArray);
							iscontinue = true;
						} else {
							// 把交换的数组放到nodelist中
							int[] array = new int[] { tempRoute[ran0],
									tempRoute[ran1] };
							int[] storeRoute = new int[nodeNum];
							copyGh(tempRoute, storeRoute);
							// 计算最优值
							double tempValue = evaluate(tempRoute);
							// 如果最优值 <= 结果值，则跳出循环
							if (tempValue <= bestEvaluation) {
								NeighbourhoodRoutes.add(tempRoute);
								tempExchangeNodeList.add(array);
								isStop = true;
								break;
							}
							//
							if (!isInTabuTable(array)) {
								NeighbourhoodRoutes.add(storeRoute);
								tempExchangeNodeList.add(array);
							} else {
								iscontinue = true;
							}
						}
					}

				} while (iscontinue);
				if (isStop == true) {
					break;
				}
				// System.out.println("------搜索邻域解第"+i+"次完成");
			}
			result.setNeighbourExchange(tempExchangeNodeList);
			result.setNeighbourRoutes(NeighbourhoodRoutes);
			// System.out.println("-----搜索邻域解结束-----");
			return result;
		}

		/** 判断路径是否在禁忌表中 */
		public boolean isInTabuTable(int[] exchangeItems) {
			boolean flag = false;
			if ((tabuTable.isEmpty())) { // 小于，说明禁忌表没满
				flag = false;
			} else {
				double tabusize = tabuTableLength;
				if (tabuTable.size() < tabuTableLength) {
					tabusize = tabuTable.size();
				}
				for (int i = 0; i < tabusize; i++) {
					if (Arrays.equals(tabuTable.get(i), exchangeItems)) {
						flag = true;
					}
				}
			}
			return flag;
		}

		/** 解禁忌与加入禁忌，注意禁忌策略的选择 */
		public void flushTabuTable(int[] tempGh) {
			// 先测试tabuTable是不是满了
			if (tabuTable.size() < tabuTableLength) { // 小于，说明禁忌表没满
				tabuTable.add(tempGh);
			} else { // 大于等于，说明需要更新啦
				tabuTable.remove(0);
				tabuTable.add(tempGh);
			}
		}

		/** 逃逸机制,执行多次随机交换 */
		// public void escapeMethod (int[] route){
		// //随机交换5次
		// //不考虑是不是可行
		// }

		public NeighbourResult escapeMethod(int[] route, int tempNeighbourhoodNum) {
			NeighbourResult result = new NeighbourResult();
			int FirIndex = 0;
			int SecIndex = 0;
			int thiIndex = 0;
			int[] MinResult = new int[route.length];
			double Minvalue = Integer.MAX_VALUE;
			NeighbourResult result_First = new NeighbourResult();

			result_First = getNeighbourhood(route);// 根据解找到其邻域解
			if (result_First.getNeighbourRoutes().size() < tempNeighbourhoodNum) {
				int num = result_First.getNeighbourRoutes().size();
				List<int[]> NeighbourhoodRoutes = new ArrayList<int[]>();
				NeighbourhoodRoutes.add(result_First.getNeighbourRoutes().get(
						num - 1));
				List<int[]> tempExchangeNodeList = new ArrayList<int[]>();
				tempExchangeNodeList.add(result_First.getNeighbourExchange().get(
						num - 1));
				result.setNeighbourRoutes(NeighbourhoodRoutes);
				result.setNeighbourExchange(tempExchangeNodeList);
				return result;
			} else {
				List<NeighbourResult> resultList = new ArrayList<NeighbourResult>(); // 找到邻域解的邻域解
				for (int i = 0; i < result_First.getNeighbourRoutes().size(); i++) {
					// 这一步原来交换的元素是
					NeighbourResult result_second = getNeighbourhood(result_First
							.getNeighbourRoutes().get(i));
					resultList.add(result_second);
				}
				// List<NeighbourResult> resultListSec = new
				// ArrayList<NeighbourResult>();
				// for(int j = 0;j<resultList.size();j++) {
				// for (int i = 0; i <
				// resultList.get(j).getNeighbourRoutes().size(); i++) {
				// NeighbourResult result_thi =
				// getNeighbourhood(resultList.get(j).getNeighbourRoutes().get(i));
				// resultListSec.add(result_thi);
				// }
				// }
				// 在结果中找到最优的解
				// for (int i = 0; i < resultListSec.size(); i++) {
				// for (int j = 0; j <
				// resultListSec.get(i).getNeighbourRoutes().size(); j++) {
				// double value =
				// evaluate(resultListSec.get(i).getNeighbourRoutes().get(j));
				// if (value < Minvalue) {
				// Minvalue = value;
				// //System.out.println("------Minvalue = "+Minvalue);
				// MinResult = resultListSec.get(i).getNeighbourRoutes().get(j);
				// FirIndex = i;
				// SecIndex = j;
				// }
				// }
				// }

				for (int i = 0; i < resultList.size(); i++) {
					for (int j = 0; j < resultList.get(i).getNeighbourRoutes()
							.size(); j++) {
						double value = evaluate(resultList.get(i)
								.getNeighbourRoutes().get(j));
						if (value != evaluate(route)) {
							if (value < Minvalue) {
								Minvalue = value;
								// System.out.println("------Minvalue = "+Minvalue);
								MinResult = resultList.get(i).getNeighbourRoutes()
										.get(j);
								FirIndex = i;
								SecIndex = j;
							}
						}
					}
				}
				// 循环找
				// resultList结果
				// int indexOne = FirIndex/tempNeighbourhoodNum;
				// int indexTwo = FirIndex%tempNeighbourhoodNum;
				//
				// int indexThi = indexOne/tempNeighbourhoodNum;
				// int indexFour = indexOne%tempNeighbourhoodNum;

				// 把交换的两个元素，存入到tabulist中
				List<int[]> NeighbourhoodRoutes = new ArrayList<int[]>();
				NeighbourhoodRoutes.add(MinResult);
				List<int[]> tempExchangeNodeList = new ArrayList<int[]>();
				// tempExchangeNodeList.add(resultList.get(indexThi).getNeighbourExchange().get(indexFour));
				// tempExchangeNodeList.add(resultList.get(indexOne).getNeighbourExchange().get(indexTwo));
				tempExchangeNodeList.add(resultList.get(FirIndex)
						.getNeighbourRoutes().get(SecIndex));
				result.setNeighbourRoutes(NeighbourhoodRoutes);
				result.setNeighbourExchange(tempExchangeNodeList);
				return result;
			}
		}

		// 主动禁忌搜索算法
		/*
	 * 
	 * 
	 * 
	 * */
		public RTSresult RTSalgorithm(int[] vertexRoutes) {
			double neighbourhoodEvaluation;
			double currentBestRouteEvaluation;

			NeighbourResult escaperesult = new NeighbourResult();
			/** 最佳路径总长度 */
			double bestEvaluation;
			/** 存放邻域路径 */
			int[] neighbourhoodOfRoute = new int[nodeNum];
			/** 当代最好路径 */
			int[] currentBestRoute = new int[nodeNum];
			/** 当前代数 */
			int currentIterateNum = 0;
			/** 最佳出现代数 */
			int bestIterateNum = 0;
			/** 历史最优解 */
			int[] bestRoute = new int[nodeNum];
			/** 当前路线 */
			int[] route = new int[nodeNum];

			/** 用于存放每一代中被接受的那个解 */
			List<int[]> historyBest = new ArrayList<int[]>();
			/** 用于tabulist中，存放的问题一组数 */
			tabuTable = new ArrayList<int[]>();
			/** 将结果存储在结果表里,rtsResult */
			RTSresult rtsResult = new RTSresult();
			// 将当前路径作为最好路径
			copyGh(vertexRoutes, bestRoute);
			bestEvaluation = evaluate(bestRoute);
			// 把当前路径作为route
			copyGh(vertexRoutes, route);
			System.out.println("2.迭代搜索....");
			while (currentIterateNum < MAX_GEN) {
				System.out.println("---------------------");
				// 测试获取邻域解的结果

				NeighbourResult result = getNeighbourhood(route);
				List<int[]> neighbourhoodOfRoutes = new ArrayList<int[]>();
				neighbourhoodOfRoutes.addAll(result.getNeighbourRoutes());
				List<int[]> neighbourExchange = new ArrayList<int[]>();
				neighbourExchange.addAll(result.getNeighbourExchange());

				boolean escapeflag = false;
				boolean repeatflag = false;
				System.out.println("测试点：currentIterateNum= " + currentIterateNum);
				currentBestRouteEvaluation = Integer.MAX_VALUE;
				int[] exchangeItem = new int[2];
				for (int nn = 0; nn < neighbourhoodOfRoutes.size(); nn++) {

					// 得到当前路径route的一个邻域路径neighbourhoodOfRoute
					copyGh(neighbourhoodOfRoutes.get(nn), neighbourhoodOfRoute);
					neighbourhoodEvaluation = evaluate(neighbourhoodOfRoute);
					// System.out.println("邻域解"+nn+"="+neighbourhoodEvaluation);
					if (neighbourhoodEvaluation <= currentBestRouteEvaluation) {
						copyGh(neighbourhoodOfRoute, currentBestRoute);
						currentBestRouteEvaluation = neighbourhoodEvaluation;
						// 复制交换的元素
						for (int i = 0; i < neighbourExchange.get(nn).length; i++) {
							exchangeItem[i] = neighbourExchange.get(nn)[i];
						}

					}
				}

				// 判断下历史最优解里是不是有该解，有的话重复多少次
				int repeatTimes = 0;

				for (int i = 0; i < historyBest.size(); i++) {
					if (Arrays.equals(currentBestRoute, historyBest.get(i))) { // 相等,则说明存在
						repeatTimes += 1;

					}
				}
				System.out.println("repeatTimes = " + repeatTimes);

				if (repeatTimes >= 20) { // 重复次数大于等于20次
					System.out.println("repeatTimes =" + repeatTimes);
					// 调用逃逸机制方法
					System.out.println("触发逃逸机制");
					escapeflag = true;
					escaperesult = escapeMethod(currentBestRoute, neighbourhoodNum);
					copyGh(escaperesult.getNeighbourRoutes().get(0),
							currentBestRoute);
					currentBestRouteEvaluation = evaluate(currentBestRoute);
				}
				if (repeatTimes != 0) {// 不等
					tabuTableLength = tabuTableLength * IncLength;// 存在于历史最优解中
				}
				// 禁忌表长度缩小
				if (historyBest.size() > maxNumWithoutFind) {
					for (int i = 0; i < maxNumWithoutFind; i++) {
						for (int j = 0; j < historyBest.size() - i; j++) {
							if (Arrays.equals(
									historyBest.get(historyBest.size() - i - 1),
									historyBest.get(j))) {
								repeatflag = true;
								break;
							}
						}
					}
				}

				if (repeatflag != true) {
					tabuTableLength = tabuTableLength * decLength;
				}

				tabuTableLength = (int) Math.rint(tabuTableLength);
				if (escapeflag) {
					for (int i = 0; i < escaperesult.getNeighbourExchange().size(); i++) {
						flushTabuTable(escaperesult.getNeighbourExchange().get(i));
					}
				} else {
					flushTabuTable(exchangeItem);
				}
				for (int i = 0; i < tabuTable.size(); i++) {
					System.out.println("TabuList [" + i + "] ="
							+ tabuTable.get(i)[0] + "," + tabuTable.get(i)[1]);
				}
				int[] currentBestRouteCopy = new int[nodeNum];

				copyGh(currentBestRoute, currentBestRouteCopy);
				// 把每代最优解，加入序列
				historyBest.add(currentBestRouteCopy);

				if (currentBestRouteEvaluation < bestEvaluation) {
					bestIterateNum = currentIterateNum;
					copyGh(currentBestRoute, bestRoute);
					bestEvaluation = currentBestRouteEvaluation;
					System.out.println("测试：currentBestRouteEvaluation="
							+ currentBestRouteEvaluation);
				}
				copyGh(currentBestRoute, route);
				// 解禁忌表，currentBestRoute加入禁忌表
				// System.out.println("测试点：currentBestRoute= "+currentBestRoute);

				currentIterateNum++;

				// 打印状态
				System.out.println("BestEvalution" + "------" + bestEvaluation);
				System.out
						.println("CurrentEvaluation" + currentBestRouteEvaluation);

			}
			System.out.println("历史最优解" + "------" + bestEvaluation);
			for (int j = 1; j < bestRoute.length; j++) {
				System.out.println("------" + j + "------");

				System.out.println(bestRoute[j - 1] + "---------" + bestRoute[j]);
			}

			// 把最优解变成字符
			String resultRouteString = routeToString(bestRoute);
			rtsResult.setObjectValue(bestEvaluation);
			rtsResult.setResultRoute(resultRouteString);
//			System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIII");
			return rtsResult;
		}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}