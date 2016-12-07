package com.data.search;

import java.util.ArrayList;
import java.util.HashMap;

import com.biosearch.bean.Edge;
import com.biosearch.bean.Node;
import com.jiao.element.Graph;
import com.jiao.file.FileReadAndWrite;

//对于两个点经过多个社团联系的，可以把该问题分解成多个两个社团进行连接的问题。
//对于查询的q 如何利用 Berger的算法进行Cluster定位。
//利用Steiner Tree 如何在我们Super Node的抽象图进行运行。
public class DataSearch {
	//首先获得mixedGraph
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static Index index = new Index();
	static String snedgepath = "D://For Cluster/Testdata6/FixedR/Edgedis/";
	static String inedgepath = "D://For Cluster/Testdata6/FixedR/IN/";
	static DataPreparingForSearch da = new DataPreparingForSearch();
	static HashMap<String, ArrayList<String>> snedges = da.getAllEdges(snedgepath);
	//每一对应的SN和它对应的内部的图
	static HashMap<String, ArrayList<String>> inedges = da.getAllEdges(inedgepath);
	//每一对应的IN和它对应相关联的边
	public Graph ConstructMixedGraphFromFile(String mixededgesfr, String nodesprfr){
		ArrayList<String> edgesList = new ArrayList<String>();
		ArrayList<String> nodespr = new ArrayList<String>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Node> nodes = new ArrayList<Node>();
		Graph g = new Graph();
		edgesList = fraw.ReadData(mixededgesfr);
		nodespr = fraw.ReadData(nodesprfr);
		for(String s:nodespr){
			Node node = new Node();
			String n = s.split(",")[0];
			double weight = Double.valueOf(s.split(",")[1]);
			node.setTitle(n);
			node.setWeight(weight);
			nodes.add(node);
		}
		for(String s: edgesList){
			Edge edge = new Edge();
			String sou = s.split(",")[0];
			String tar = s.split(",")[1];
			double distance =  Double.valueOf(s.split(",")[2]);
			edge.setSource(sou);
			edge.setTarget(tar);
			edge.setWeight(distance);
		}
		g.setEdges(edges);
		g.setNodes(nodes);
		return g;
	}
	public String getSNForKey(String key) {
		String ns = index.Searcher(key);
		String sn = ns.split(",")[0];
		return sn;
		
	}
	public String getTagForKey(String key){
		String ns = index.Searcher(key);
		String tag = ns.split(",")[1];
		return tag;
	}
	public HashMap<String,String> DealAllKey(ArrayList<String> keys){
		HashMap<String,String> keysn = new HashMap<String,String>();
		for(String key : keys){
			String sn = getSNForKey(key);
			keysn.put(key,sn);
		}
		return keysn;
	}
	
	public static void main(String[] args){
		
	}

}
