package com.jiao.element;

import java.util.List;

import com.biosearch.bean.Edge;
import com.biosearch.bean.Node;

public class Graph {
	//由多节点多边组成图的定义
	private List<Node> nodes;
	private List<Edge> edges;
	public List<Node> getNodes() {
		return nodes;
	}
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	public List<Node> addnode(String node, double nweight){
		Node nod = new Node();
		nod.setTitle(node);
		nod.setId(node);
		nod.setWeight(nweight);
		this.nodes.add(nod);
		return this.nodes;
		
	}
	public List<Edge> addedge(String sou,String tar,double eweight){
		Edge edge = new Edge();
		edge.setSource(sou);
		edge.setTarget(tar);
		edge.setWeight(eweight);
		this.edges.add(edge);
		return this.edges;
		
	}
}
