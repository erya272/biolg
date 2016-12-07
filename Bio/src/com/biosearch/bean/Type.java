package com.biosearch.bean;

import java.util.List;

public class Type {

	private List<EdgeType> edgetype;
	private List<NodeType> nodetype;
	public List<EdgeType> getEdgetype() {
		return edgetype;
	}
	public void setEdgetype(List<EdgeType> edgetype) {
		this.edgetype = edgetype;
	}
	public List<NodeType> getNodetype() {
		return nodetype;
	}
	public void setNodetype(List<NodeType> nodetype) {
		this.nodetype = nodetype;
	}
	
}
