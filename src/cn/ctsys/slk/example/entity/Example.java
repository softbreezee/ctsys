package cn.ctsys.slk.example.entity;
	
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
	
public class Example {
	private int eid;
	private String ename;
	private String eremark;
	private Date date;
	private String etype;
	private List<Task> tasks = new ArrayList<Task>();
	private List<Site> sites = new ArrayList<Site>();
	
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getEremark() {
		return eremark;
	}
	public void setEremark(String eremark) {
		this.eremark = eremark;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<Site> getSites() {
		return sites;
	}
	public void setSites(List<Site> sites) {
		this.sites = sites;
	}
	public String getEtype() {
		return etype;
	}
	public void setEtype(String etype) {
		this.etype = etype;
	}
	
	
	
	
}	
