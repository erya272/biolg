package com.biosearch.bean;

public class Node {
	private String show_id;
	private String id;
	private String title;
	private String abstracts;
	private String link;
	private Double weight;
	private String type;
	private String database;
	private int noderank;
	private boolean tag;
	//tag 用标记node是source 还是target
	private String subtitle;
	
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public boolean isTag() {
		return tag;
	}
	public void setTag(boolean tag) {
		this.tag = tag;
	}
	public Node(){}
	public Node(String show_id,String primary_id,String title,String abstracts,String link,Double weight,String type,String database,int noderank,String subtitle){
		//原始id
		this.show_id = show_id;	
		//id 的值是primary_id
		this.id = primary_id;
		this.title = title;
		this.abstracts = abstracts;
		this.link = link;
		this.weight = weight;
		this.type = type;
		this.database = database;
		this.noderank = noderank;
		this.subtitle = subtitle;
	}
	public String getShow_id() {
		return show_id;
	}
	public void setShow_id(String show_id) {
		this.show_id = show_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public int getNoderank() {
		return noderank;
	}
	public void setNoderank(int noderank) {
		this.noderank = noderank;
	}
	
}
