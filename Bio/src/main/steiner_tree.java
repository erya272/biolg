package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class steiner_tree implements Serializable{
	
	public int isCluster; //////// -1,0,1;  -1, just 1key; 0  adjs; 1 clusters
	public String cluster_number;
	public ArrayList<String> keys;
	public ArrayList<String> nodes;
	public int weight_all=0;
	public HashSet<st_edge> edges;
	
	public  steiner_tree( String cluster_number,
		ArrayList<String> keys,
		ArrayList<String> nodes,
		int weight_all,
		HashSet<st_edge> edges){
		
//		this.isCluster = iscluster;
		this.cluster_number = cluster_number;
		this.keys = keys;
		this.nodes = nodes;
		this.weight_all = weight_all;
		this.edges = edges;
	}
	
	public void setclunum(String clunum){
		this.cluster_number = clunum;
	}	
	
}