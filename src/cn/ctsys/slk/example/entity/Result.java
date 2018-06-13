package cn.ctsys.slk.example.entity;

import java.util.Date;

public class Result {
	
	private int rid;
	private Example example;
	private String method;
	private Date resultTimestamp;
	private String resultTime;
	private String objectValue;
	private String route;
	private String paraset;

	
	

	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public Example getExample() {
		return example;
	}
	public void setExample(Example example) {
		this.example = example;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Date getResultTimestamp() {
		return resultTimestamp;
	}
	public void setResultTimestamp(Date resultTimestamp) {
		this.resultTimestamp = resultTimestamp;
	}

	public String getResultTime() {
		return resultTime;
	}
	public void setResultTime(String resultTime) {
		this.resultTime = resultTime;
	}
	public String getObjectValue() {
		return objectValue;
	}
	public void setObjectValue(String objectValue) {
		this.objectValue = objectValue;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getParaset() {
		return paraset;
	}
	public void setParaset(String paraset) {
		this.paraset = paraset;
	}
	
	
	

}
