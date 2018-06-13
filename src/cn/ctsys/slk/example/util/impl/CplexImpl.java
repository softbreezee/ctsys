package cn.ctsys.slk.example.util.impl;

import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.ctsys.slk.example.util.Algorithm;
import cn.ctsys.slk.example.util.ComputeUtil;




/**
 * 计算工具,cplex
 * 
 * 任务的排列：IE OE IF OF
 * ie0 ie1 ie2 oe0 oe1 if0 if1 of0 of1 of2 ...
 * 地理位置的排列：港口、堆场、客户
 * p0 p1 s0 s1 c0 c1 c2 c3....
 * 
 * 重箱任务的数量要等于客户的数量！！
 * 
 * @author Leon
 * 
 */
public class CplexImpl implements Algorithm {

	
	@Test
	public  Map<String,Object> computing(Map<String,Object> cd) {
		//返回的结果
//		Map<String,Object> result =  new HashMap<String,Object>();
		StringBuffer state = new StringBuffer();
		StringBuffer objValue = new StringBuffer();
		StringBuffer route = new StringBuffer();
		StringBuffer solve = new StringBuffer();
		StringBuffer tr = new StringBuffer();
		StringBuffer nodeTime = new StringBuffer();
		
		
		/**
		 * 1、接受已知的参数
		 */
		int IE = (Integer) cd.get("IE");
		int OE = (Integer) cd.get("OE");
		int IF = (Integer) cd.get("IF");
		int OF = (Integer) cd.get("OF");
		int taskNum = (Integer) cd.get("taskNum");
		int portNum = (Integer) cd.get("portNum");
		int stockNum = (Integer) cd.get("stockNum");
		int loadCar = (Integer) cd.get("loadCar");
		int[] loadTime = (int[]) cd.get("loadTime");
		int[][] tw1 = (int[][]) cd.get("tw1");
		int[][] tw2 = (int[][]) cd.get("tw2");
		double[][] driveTime = (double[][]) cd.get("driveTime");
		//注意，不要丢掉
		int[] truckNum = (int[]) cd.get("truckNum");
		
		/**顶点分为任务顶点和出发/返回顶点！***/
		// 任务顶点服务时间
		double[] serviceTime = new double[taskNum];
		// 任务顶点活动时间
		double[][] nodeTW = new double[2][taskNum];
		// 弧转换时间
		double[][] transTime = new double[taskNum + stockNum][taskNum + stockNum];

		/**
		 * 2、条件，用来确定服务时间、顶点活动时间、顶点转换时间的值
		 */
		ComputeUtil.condition(taskNum, IE, OE, IF, portNum, stockNum, loadCar,
				loadTime, tw1, tw2, driveTime, serviceTime, nodeTW, transTime);

		/**
		 * 3、 数学模型
		 */
		List<String> resultRouteList = new ArrayList<String>();
		String resultRouteString = "";
		double time = 0;
		try {
			IloCplex cplex = new IloCplex();
			// 决策变量Xij
			IloNumVar[][] X = new IloNumVar[stockNum + taskNum][];
			for (int i = 0; i < stockNum + taskNum; i++) {
				// 定义决策变量的范围
				// 每一个X[i]都有stockNum + taskNum个数
				X[i] = cplex.boolVarArray(stockNum + taskNum);
			}
			// 决策变量Yij
			IloNumVar[] Y = cplex.numVarArray(taskNum, Double.MIN_VALUE, Double.MAX_VALUE);

			// 目标函数
			IloNumExpr[][] expr0 = new IloNumExpr[stockNum + taskNum][stockNum
					+ taskNum];
			IloNumExpr[] expr1 = new IloNumExpr[stockNum + taskNum];
			for (int i = 0; i < stockNum + taskNum; i++) {
				for (int j = 0; j < stockNum + taskNum; j++) {
					// if (i != j)
					expr0[i][j] = cplex.prod(transTime[i][j], X[i][j]);
				}
			}

			for (int i = 0; i < stockNum + taskNum; i++) {
				expr1[i] = cplex.sum(expr0[i]);
			}

			IloNumExpr exprObj = cplex.sum(expr1);

			// 约束1
			IloNumExpr[][] temp1 = new IloNumExpr[stockNum][taskNum];
			IloNumExpr[] expr3 = new IloNumExpr[stockNum];
			for (int i = 0; i < stockNum; i++) {
				for (int j = 0; j < taskNum; j++) {
					// i属于堆场，j属于任务点
					temp1[i][j] = X[i][j + stockNum];
				}
			}
			for (int i = 0; i < stockNum; i++) {
				expr3[i] = cplex.sum(temp1[i]);
				cplex.addLe(expr3[i], truckNum[i]);
			}

			// 约束2
			IloNumExpr[][] expr4 = new IloNumExpr[taskNum][taskNum + stockNum];
			IloNumExpr[] expr5 = new IloNumExpr[taskNum];
			IloNumExpr[] expr5_2 = new IloNumExpr[taskNum];
			for (int i = 0; i < taskNum; i++) {
				for (int j = 0; j < taskNum + stockNum; j++) {
					expr4[i][j] = X[j][i + stockNum];
				}
			}
			for (int i = 0; i < taskNum; i++) {
				expr5[i] = cplex.sum(expr4[i]);
				expr5_2[i] = cplex.sum(X[i + stockNum]);
			}
			for (int i = 0; i < taskNum; i++) {
				cplex.addEq(expr5[i], expr5_2[i]);
				cplex.addEq(1, expr5[i]);
				cplex.addEq(1, expr5_2[i]);
			}

			// 约束3
			for (int i = 0; i < taskNum; i++) {
				cplex.addLe(nodeTW[0][i], Y[i]);
				cplex.addLe(Y[i], nodeTW[1][i]);
			}

			// 约束4
			int M = Integer.MAX_VALUE;
			for (int i = 0; i < taskNum; i++) {
				for (int j = 0; j < taskNum; j++) {
					// if (i != j)
					// model.add(TB[i]+ET[i]+Tran_time[i][j]-TB[j]<=(1-X[i][j])*M);
					cplex.addLe(
							cplex.diff(
									cplex.sum(Y[i], serviceTime[i] + transTime[i + stockNum][j + stockNum]),//
									Y[j]),
							cplex.prod(
									M, 
									cplex.diff(1.0, X[i + stockNum][ j + stockNum]))
									);
				}
			}

			/**
			 * 模型求解
			 */
			// 最小化目标函数
			cplex.addMaximize(exprObj);
			//获取求解时间
			long startTime=System.currentTimeMillis();
			Boolean solveSuccess = cplex.solve();
			long endTime=System.currentTimeMillis();
			time = endTime-startTime;
			
			if (solveSuccess) {
//				System.out.println("-----------1、求解状态----------");
//				System.out.println("Solution status: " + cplex.getStatus() + "|||" + cplex.getAlgorithm());
				state.append("Solution status: " + cplex.getStatus()+"<br/>");

				//将解从模型中取出
				Double[][] x = new Double[stockNum + taskNum][stockNum + taskNum];
				Double[] y = new Double[taskNum];
				for (int i = 0; i < stockNum + taskNum; i++) {
					for (int j = 0; j < stockNum + taskNum; j++) {
//						if (i != j)
							x[i][j] = (Double) cplex.getValue(X[i][j]);
					}
				}
				for(int i=0;i<taskNum;i++){
					y[i] = cplex.getValue(Y[i]);
					System.out.println(y[i]);
				}

//				System.out.println("-----------2、求解时间----------");
//				System.out.println("The total time is " + cplex.getObjValue());
				objValue.append( cplex.getObjValue());

				//0是堆场。其余是客户。
				System.out.println("-------------3、路径-------------");
				int[][] sf0 = new int[stockNum][taskNum];
				for (int i = 0; i < stockNum; i++) {
					for (int j = 0,index=0; j < stockNum + taskNum; j++) {
						if (x[i][j] == 1) {
								sf0[i][index] = j;
								index++;
						}
					}
				}
				for (int i = 0; i < stockNum; i++) {
					for (int j = 0; j <  taskNum; j++) {
						if (sf0[i][j]!=0) {
							int c = sf0[i][j];
						}
					}
				}
				for (int i = 0; i < stockNum + taskNum; i++) {
					for (int j = 0; j < stockNum + taskNum; j++) {
						if ((i != j) && (x[i][j] == 1)) {
							route.append(i + "--" + j+"<br/>");
						}
					}
				}
				
//				List<Integer> startFrom0 = new ArrayList<Integer>(); 
//				for(int i=0;i<stockNum+taskNum;i++){
//							if(x[0][i]==1 && i<stockNum);
//								startFrom0.add(i);
//				}
//				int m1=0;
//				boolean flag = true;
//			    for(int k=0;k<startFrom0.size();k++){
//			    	m1=startFrom0.get(k);
//			    	route.append("0---"+m1+"("+y[m1-stockNum]+")"+"---");
//					for(int i =0;i<stockNum+taskNum;i++){
//						if(flag){
//							for(int j=0;j<stockNum+taskNum;j++){
//									if(x[m1][j]==1&&j!=0){
//										m1=j;
//										route.append(m1+"("+y[m1-stockNum]+")"+"---");
//										break;
//									}
//									else if(x[m1][j]==1 && j==0){
//										flag = false;
//										route.append("0<br/>");
//										break;
//									}
//							}
//						}
//						else{
//							flag = true;
//							break;
//						}
//					}
//				}
//
//				System.out.println("222222222");
				//师姐

				for (int i = 0; i < stockNum; i++) {
					for (int j = 0; j < stockNum+taskNum; j++) {   
						if((i != j)&&(x[i][j]==1)){
							 	//新路径
								String subResult = "";
//								subResult = i+","+j+"("+transTime[i][j]+")";
								subResult = i+","+j;
//								System.out.println("j = "+j);
								int m = j;
								//然后根据key 找下一个路径
								do {
									for (int k = 0; k < stockNum+taskNum; k++) {
										if (x[m][k] ==1) {
					
//											subResult = subResult+","+k+"("+transTime[m][k]+")";
											subResult = subResult+","+k;
											//在每一行的结尾，显示该节点的时间窗和到达时间
//											if(k>=depotNum){
//												int test = k-depotNum;
//												String invalid = "false";
//												if ((cplex.getValue(TBegin[test])>=Ta[test])&&(cplex.getValue(TBegin[test])<=Tb[test])) {
//													invalid = "true";
//												}else if (cplex.getValue(TBegin[test])<=Ta[test]) {
//													invalid = "early";
//												}else if (cplex.getValue(TBegin[test])>=Tb[test]) {
//													invalid = "later";
//												}
//												subResult +="["+invalid+"]"+"("+Ta[test]+","+Tb[test]+")"+" "+cplex.getValue(TBegin[test]);
//											
//											}
											m=k;
											break;
										}
									}
								} while (m>=stockNum);
					
							resultRouteList.add(subResult);	
							}
					}
				
				}
				//将resultRouteList转换为字符串
				for (int i = 0; i < resultRouteList.size(); i++) {
					resultRouteString = resultRouteString+resultRouteList.get(i)+";";
				}
				
				
				System.out.println(transTime[4][1]);

//				System.out.println("-------------4、解-------------");
				solve.append("<table>");
				for (int i = 0; i < stockNum + taskNum; i++) {
					solve.append("<tr>");
					for (int j = 0; j < stockNum + taskNum; j++) {
//							System.out.println("x[" + i + "]" + "[" + j + "]"+ "=" + x[i][j]);
							solve.append("<td>x[" + i + "]" + "[" + j + "]"+ "=" + x[i][j]+"</td>");
					}
					solve.append("</tr>");
				}
				solve.append("</table>");
				
				
			}
			
//			System.out.println("-------------5、转换时间-------------");
			tr.append("<table>");
			for (int i = 0; i < stockNum + taskNum; i++) {
				tr.append("<tr>");
				for (int j = 0; j < stockNum + taskNum; j++) {
//					System.out.println("transtime[" + i + "][" + j + "]" + "=" + transTime[i][j]);
					tr.append("<td>transtime[" + i + "][" + j + "]" + "=" + transTime[i][j]+"</td>");
				}
				tr.append("</tr>");
			}
			tr.append("</table>");
//			System.out.println("-------------6、节点活动时间-------------");
			nodeTime.append("<table>");
			for (int j = 0; j < taskNum; j++) {
				nodeTime.append("<tr>");
				for (int i = 0; i < 2; i++) {
//					System.out.print("[" + i + "][" + j + "]" + "=" + nodeTW[i][j] + "\t");
					nodeTime.append("<td>[" + i + "][" + j + "]" + "=" + nodeTW[i][j] + "</td>");
				}
				nodeTime.append("</tr>");
			}
			nodeTime.append("</table>");
			
			cplex.end();
		} catch (IloException e) {

			e.printStackTrace();

		}

		System.out.println("cplexUtil执行");
		//返回一个map集合		
		HashMap<String, Object> rs = new HashMap<String,Object>();
		rs.put("state", state.toString());
		rs.put("solveTime", objValue.toString());
//		rs.put("route", route.toString());
		rs.put("route",resultRouteString);
		rs.put("solve", solve.toString());
		rs.put("tr", tr.toString());
		rs.put("nodeTime", nodeTime.toString());
		Double t = time;
		rs.put("computeTime",t.toString() );
		return rs;

	}




}
