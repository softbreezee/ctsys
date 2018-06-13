package cn.ctsys.slk.example.entity;

import java.util.List;

public class NeighbourResult {

	private List<int[]> neighbourExchange;
	private List<int[]> neighbourRoutes;
	
	public List<int[]> getNeighbourExchange() {
		return neighbourExchange;
	}
	public void setNeighbourExchange(List<int[]> neighbourExchange) {
		this.neighbourExchange = neighbourExchange;
	}
	public List<int[]> getNeighbourRoutes() {
		return neighbourRoutes;
	}
	public void setNeighbourRoutes(List<int[]> neighbourRoutes) {
		this.neighbourRoutes = neighbourRoutes;
	}
}
