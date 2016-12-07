package com.jiao.steinerTree;

import java.util.Comparator;

import com.biosearch.bean.Edge;



public class EdgeComparator implements Comparator<Edge> {
////the comparison of edge's distance
	public int compare(Edge o1, Edge o2) {
		// TODO Auto-generated method stub
		return Double.compare(o1.getWeight(), o2.getWeight());
		/*
		 * o1 > o2 return -1
		 * o1 = o2 return 0
		 * o1 < 02 return 1
		 */
	}
}
