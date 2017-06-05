package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.hadoop.util.hash.Hash;

import scala.Tuple2;

public class super_cluster  implements Serializable {
	
	public int nodes_num, cluster_num; /////// in new network
	int dis[][];
	ArrayList<String> nodes = new ArrayList<String>();  /////// all nodes, original node and cluster node
	ArrayList<String> ori_nodes = new ArrayList<String>();  ////// original nodes
	ArrayList<String> sub_clusters = new ArrayList<String>();
	HashSet<st_edge> links = new HashSet<st_edge>();
	HashMap<Tuple2<String, String>, Integer> tp_links;
	
	public super_cluster(int nodes_num, int cluster_num, int dis[][], ArrayList<String> nodes,ArrayList<String> ori_nodes, ArrayList<String> sub_clusters, HashSet<st_edge> links, HashMap<Tuple2<String, String>, Integer> tp_links){
		this.nodes_num = nodes_num;
		this.cluster_num = cluster_num;
		this.dis = dis;
		this.nodes = nodes;
		this.ori_nodes = ori_nodes;
		this.sub_clusters = sub_clusters;		
		this.links = links;
		this.tp_links = tp_links;
	}
	
	public  int[][] getDis() {
		return dis;
	}
	public ArrayList<String> getnodes(){
		return nodes;
	}
	public  ArrayList<String> getClusters(){
		return sub_clusters;
	}
	public HashSet<st_edge> get_links(){
		return links;
	}
	
	
}
