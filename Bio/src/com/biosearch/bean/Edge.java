package com.biosearch.bean;

public class Edge {

	private String id;
	private String source;
	private String target;
	private Double weight;
	private String description;
	//type = TYPE.tag
	private String type;
	private boolean directed;
	private String evidence;
	
	public boolean isDirected() {
		return directed;
	
	}
	public void setDirected(boolean directed) {
		this.directed = directed;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Edge(){}
	public Edge(String id,String source,String target,String type,Double weight,String description,String evidence,boolean d){
		this.id = id;
		this.source = source;
		this.target = target;
		this.type = type;
		this.weight = weight;
		this.description = description;
		this.evidence = evidence;
		this.directed = d;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Double getWeight() {
		return weight;
	}
	public String getEvidence() {
		return evidence;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
