package com.biosearch.dao;

public class Edge {
	//边的定义
	private String source;
	private String target;
	private double eWeight;
	public Edge(){}
	public Edge(String source,String target,double eWeight){
		super();
		this.source = source;
		this.target = target;
		this.eWeight = eWeight;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public double geteWeight() {
		return eWeight;
	}
	public void seteWeight(double eWeight) {
		this.eWeight = eWeight;
	}
}
