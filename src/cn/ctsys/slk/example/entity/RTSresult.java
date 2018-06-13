package cn.ctsys.slk.example.entity;

public class RTSresult {
	private double objectValue;
	private String resultRoute;
	public double getObjectValue() {
		return objectValue;
	}
	public void setObjectValue(double bestEvaluation) {
		this.objectValue = bestEvaluation;
	}
	public String getResultRoute() {
		return resultRoute;
	}
	public void setResultRoute(String resultRoute) {
		this.resultRoute = resultRoute;
	}
}
