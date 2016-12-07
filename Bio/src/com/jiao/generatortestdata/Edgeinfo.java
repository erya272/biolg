package com.jiao.generatortestdata;

public class Edgeinfo {
	/**
	 * special for  ScaleFree Network generator
	 */
	private int source;
	private int target;
	private double weight;
	public Edgeinfo(int source, int target, double weight){
		super();
		this.source = source;
		this.target = target;
		this.weight = weight;
		
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
		this.target = target;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
}
