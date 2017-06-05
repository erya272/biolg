package main;

import java.util.ArrayList;
import java.util.HashMap;

import scala.Serializable;

public class  graph_stru implements Serializable {
	public steiner_tree tree;
	public String cluster_num; ///// father clusters
	public ArrayList<String> nodes; ///// super_clusters' nodes(clusters or original nodes)
	public ArrayList<String> low_nodes; //////// cluster in fa_sons (not father cluster's sons)
	public HashMap<String, String> low2high ;
	
	public graph_stru(		steiner_tree tree, String cluster_num, ArrayList<String> nodes, 
			ArrayList<String> low_nodes, HashMap<String, String> low2high ) {
		// TODO Auto-generated constructor stub
		this.tree = tree;
		this.cluster_num = cluster_num;
		this.nodes = nodes;
		this.low_nodes = low_nodes;
		this.low2high = low2high;

	}
}
