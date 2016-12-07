package com.jiao.steinerTree;

import java.util.Comparator;

import com.biosearch.bean.Node;



public class NodeComparator implements Comparator<Node>{
//the comparison of node's pagerank value
	public int compare(Node o1, Node o2) {
		// TODO Auto-generated method stub
		return Double.compare(o1.getWeight(), o2.getWeight());
	}

}
