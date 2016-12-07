package com.jiao.generatortestdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biosearch.bean.Edge;
import com.biosearch.bean.Node;
import com.jiao.element.Graph;
import com.jiao.serializable.MapSerializable;
public class GetTestConnectedGraph {
	//测试数剧生成，连通图的生成
	private static MapSerializable ms = MapSerializable.loadMapData(new File(
			"D://map.dat"));
	public ArrayList<String> sourceID = new ArrayList<String>();
	Graph graph = new Graph();

	public Graph GetRandomTestConnectedGraph() throws FileNotFoundException,
			ClassNotFoundException, IOException {
		// 这样做保证图是连通图
		List<Node> nodes = new ArrayList<Node>();
		List<Edge> edges = new ArrayList<Edge>();
		ArrayList<String> testlist = new ArrayList<String>();// 接下来要产生的随机点就从这个list进行选取
		ArrayList<String> visted = new ArrayList<String>();// 用来记录已经访问过的点。
		ArrayList<String> allkey = ms.ReadAllKey();
		int index = (int) (Math.random() * allkey.size());
		String fd = allkey.get(index);
		visted.add(fd);
		Collection<String> target = new ArrayList<String>();
		target = ms.getPair(fd);
		testlist.addAll(Gettestlist(target, visted, testlist));
		nodes.add(SetNode(fd));
		nodes.addAll(SettargetList(target, visted, nodes));
		edges.addAll(SetEdge(fd, target, visted));
		while (nodes.size() <= 10000) {
			//通过控制测试数据集nodes.size()和edges.size()来控制测试数据集的大小。
			int ind = (int) (Math.random() * testlist.size());
			String sfd = testlist.get(ind);
			visted.add(sfd);
			testlist.remove(sfd);
			Collection<String> starget = new ArrayList<String>();
			starget = ms.getPair(sfd);
			testlist.addAll(Gettestlist(starget, visted, testlist));
			nodes.addAll(SettargetList(starget, visted, nodes));
			edges.addAll(SetEdge(sfd, starget, visted));
			if(edges.size() >20000)
				break;
		}
		System.out.println(nodes.size() + "," + edges.size());
		graph.setNodes(nodes);
		graph.setEdges(edges);
		return graph;
	}

	// public void Test1(String sfd,Collection<String> starget,ArrayList<String>
	// test){
	// ArrayList<String> f = GetStringF(starget);
	// for(String s :f){
	// if (test.contains(s)){
	// System.out.println(" 有环"+sfd+","+s);
	// }
	// }
	// }
	public Node SetNode(String id) {
		Node node = new Node();
		node.setTitle(id);
		node.setWeight(0);
		return node;

	}

	public ArrayList<String> GetStringF(Collection<String> target) {
		ArrayList<String> sl = new ArrayList<String>();
		for (String s : target) {
			sl.add(s.split(",")[0]);
		}
		return sl;
	}

	public ArrayList<String> Gettestlist(Collection<String> target,
			ArrayList<String> visted, ArrayList<String> test) {
		ArrayList<String> testlist = new ArrayList<String>();
		for (String s : target) {
			String s1 = s.split(",")[0];
			if (!visted.contains(s1) && !test.contains(s1)) {
				testlist.add(s1);
			}
		}
		return testlist;

	}

	public boolean EdgeDuplicateRemoval(String t, List<Edge> edges) {
		for (Edge ed : edges) {
			if (ed.getTarget().equals(t)) {
				return false;
			}
		}
		return true;
	}

	public boolean NodeDuplicateRemoval(String t, List<Node> nodes) {
		for (Node nd : nodes) {
			if (nd.getTitle().equals(t)) {
				return false;
			}
		}
		return true;
	}

	public List<Edge> SetEdge(String fd, Collection<String> target,
			ArrayList<String> visted) {
		List<Edge> edges = new ArrayList<Edge>();
		for (String s : target) {
			String s1 = s.split(",")[0];
			boolean judge = EdgeDuplicateRemoval(s1, edges);
			if (!visted.contains(s1) && judge) {
				Edge edge = new Edge();
				edge.setSource(fd);
				edge.setTarget(s1);
				edge.setWeight(Double.valueOf(s.split(",")[1]));
				edges.add(edge);
			}
		}
		return edges;

	}

	public List<Node> SettargetList(Collection<String> target,
			ArrayList<String> visted, List<Node> allnodes) {
		List<Node> nodes = new ArrayList<Node>();
		for (String s : target) {
			String s1 = s.split(",")[0];
			boolean jud = NodeDuplicateRemoval(s1, allnodes);
			boolean jud2 = NodeDuplicateRemoval(s1, nodes);
			if (!visted.contains(s1) && jud && jud2) {
				Node node = new Node();
				node.setTitle(s1);
				node.setWeight(0);
				nodes.add(node);
			}
		}
		return nodes;
	}

	public void GraphOutToFile(Graph g ,String filenamenodes,String filenameedges) throws IOException{
		FileWriter fwn = new FileWriter(filenamenodes);
		FileWriter fwe = new FileWriter(filenameedges);
		List<Node> nodes = g.getNodes();
		List<Edge> edges = g.getEdges();
		for(Node no :nodes){
			String  node = no.getTitle();
			String  sline = node;
			fwn.write(sline);
			fwn.write("\r\n");
			fwn.flush();
		}
		for(Edge ed :edges){
			String  source = ed.getSource();
			String  targ = ed.getTarget();
			double  eweight = ed.getWeight();
			String  sline = source+","+targ+","+eweight;
			fwe.write(sline);
			fwe.write("\r\n");
			fwe.flush();
		}
		fwn.close();
		fwe.close();
	}

	public static void main(String[] args) throws FileNotFoundException,
			ClassNotFoundException, IOException {
		GetTestConnectedGraph gt = new GetTestConnectedGraph();
		Graph g = gt.GetRandomTestConnectedGraph();
		gt.GraphOutToFile(g, "D://For Cluster/Testdata9/Testdata9Node.csv","D://For Cluster/Testdata9/Testdata9Edge.csv");
//		List<PNode> nodes = g.getNodes();
//		List<PEdge> edges = g.getEdges();
//		for (PNode p : nodes) {
//			System.out.println(p.getNode());
//		}
//		for (PEdge p : edges) {
//			System.out.println(p.getSource() + "," + p.getTarget() + ","
//					+ p.geteWeight());
//		}
	}

}
