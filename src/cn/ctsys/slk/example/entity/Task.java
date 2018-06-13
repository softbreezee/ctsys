package cn.ctsys.slk.example.entity;

import java.util.HashSet;
import java.util.Set;



public class Task {
	private int tid;
	private Example example;
	private Site start;
	private Site end;
	private String tw1;
	private String tw2;
	private String taskType;
	private int loadTime;
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public Example getExample() {
		return example;
	}
	public void setExample(Example example) {
		this.example = example;
	}
	
	public Site getStart() {
		return start;
	}
	public void setStart(Site start) {
		this.start = start;
	}
	public Site getEnd() {
		return end;
	}
	public void setEnd(Site end) {
		this.end = end;
	}
	public String getTw1() {
		return tw1;
	}
	public void setTw1(String tw1) {
		this.tw1 = tw1;
	}
	public String getTw2() {
		return tw2;
	}
	public void setTw2(String tw2) {
		this.tw2 = tw2;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public int getLoadTime() {
		return loadTime;
	}
	public void setLoadTime(int loadTime) {
		this.loadTime = loadTime;
	}

}
