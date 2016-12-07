package com.jiao.steinerTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biosearch.bean.Edge;
import com.biosearch.bean.Node;
import com.data.mixed_graph.NetworkInnerPageRank;
import com.jiao.element.Graph;
import com.jiao.nexoSim.ComputeSim;

public class STResult {
	// 计算节点的PageRank值
	static NetworkInnerPageRank npr = new NetworkInnerPageRank();
	DreyfusWagner dr = new DreyfusWagner();
	static ShortestPathResult spr = new ShortestPathResult();
	static ComputeSim cs =new ComputeSim();
	public void ComputePagerank(String nodesr, String edgesr, String ikeyr,
			String pr, double initvalue) {
		// 计算该网络的PageRank值
		/*
		 * nodesr is the nodes file edgesr is the edges file ikeyr is the
		 * corresponding i to node's name pr is the file for writing PageRank
		 */
		npr.GetNodePageRank(nodesr, edgesr, ikeyr, pr, 1);
	}

	public ArrayList<String> RunST(String Disfile, String Adjfile,ArrayList<String> keywords) {
		long tm1 = System.currentTimeMillis();
		ResBackForDrefus dfs = dr.getMixedGraphDistanceMatrixFromFile(Disfile);
		double[][] Dis = dfs.getDis();
		HashMap<Integer, String> jkey = new HashMap<Integer, String>();
		jkey = dfs.getJkey();
		HashMap<String, Integer> keyj = new HashMap<String, Integer>();
		keyj = dfs.getKeyj();		
		double[][] adj = spr.ReadAdjMatrix(Adjfile,jkey,keyj);
		//之前邻接矩阵生成的时候就是在最短路径矩阵的jkey的基础上生成的，所有可以直接使用之前的jkey,keyj
		Set<Integer> N = new HashSet<Integer>();
		Set<Integer> Y = new HashSet<Integer>();	
		int len = Dis.length;
		for (int i = 0; i < len; i++) {
			N.add(i);
		}
		for (String key : keywords) {
			Y.add(keyj.get(key));
//			System.out.println(key+","+keyj.get(key));
		}
		
		System.out.println(Dis.length+" "+keyj.size()+" "+N.size()+" "+Y.size());
		System.out.println( "time for reading files: "+(System.currentTimeMillis()-tm1)/1000 + "s" );
		
//		System.out.println(Disfile.length());
//		System.out.println(Y.size());
		ArrayList<String> res = dr.RunDreyfusWagner1(N, Y, Dis, jkey,adj);
		System.out.println(res);
		return res;
	}

	public Graph ConstructeG(String Disfile, String Adjfile,ArrayList<String> keywords){
		Graph G = new Graph();
		ArrayList<String> res = RunST(Disfile,Adjfile, keywords);
		ArrayList<Node> nodes = new ArrayList<Node>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		cs.allpathSim(res);
//		for(String s:keywords){
//			PNode node =new PNode();
//			node.setNode(s);
//			nodes.add(node);
//		}
		for(String s :res){
			String sou =s.split(",")[0];
			String tar = s.split(",")[1];
			Node node1 =new Node();
			node1.setAbstracts(sou);
			node1.setTitle(sou);
			node1.setId(sou);
			Node node2 =new Node();
			node2.setTitle(tar);
			node2.setId(tar);
			if(!compare(nodes,node1))
				nodes.add(node1);
			if(!compare(nodes,node2))
				nodes.add(node2);
			Edge edge = new Edge();
			edge.setSource(sou);
			edge.setTarget(tar);
			edges.add(edge);
		}
		for(Node node :nodes){
			if(keywords.contains(node.getTitle())){
				node.setType("queried");
				System.out.println(node.getTitle()+","+node.getType());
			}else{
				node.setType("no");
				System.out.println(node.getTitle()+","+node.getType());
			}
			
		}
		G.setEdges(edges);
		G.setNodes(nodes);
		return G;
	}
	
	public static boolean compare(List<Node> p, Node in){
		for(Node t:p){
			if(t.getTitle()==in.getTitle() || t.getTitle().equals(in.getTitle())){
				return true;
			}
			
		}
		return false;
	}
	
	public static void main(String[] args) {
		STResult st = new STResult();
		// st.ComputePagerank("E://ScaleFreeNetwork TestData/experiment/20/nodes.csv",
		// "E://ScaleFreeNetwork TestData/experiment/20/Edges.csv",
		// "E://ScaleFreeNetwork TestData/experiment/20/ikey.csv","E://ScaleFreeNetwork TestData/experiment/20/PageRank.csv",
		// 1);
		String Disfile = "E://ScaleFreeNetwork TestData/experiment/test/13EdgeDisMatrix.csv";
		String Adjfile = "E://ScaleFreeNetwork TestData/experiment/test/12AdjcentMatrix.csv";
		ArrayList<String> keys = new ArrayList<String>();
 
		keys.add("瞿颖");
		keys.add("窦唯");
		keys.add("李亚鹏");
		keys.add("钟欣桐");
		keys.add("李大齐");

		long startTime = System.currentTimeMillis();
		System.out.println(startTime);
//		ArrayList<String> allpath =st.RunST(Disfile,Adjfile, keys);
//		cs.allpathSim(allpath);
		long endTime = System.currentTimeMillis();
		System.out.println("running time:" +"======="+ (endTime - startTime) + "ms");
		st.ConstructeG(Disfile, Adjfile, keys);
	}
}
