package com.biosearch.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.lucene.queryparser.classic.ParseException;

import com.biosearch.bean.*;
import com.jiao.element.DisMAtrixAndIndex;
import com.jiao.element.Graph;
import com.jiao.hierarachy.AbstractMixedGraph;
import com.jiao.hierarachy.FindCorrespondingHierarchy;
import com.jiao.steinerTree.STResult;
import com.jiao.steinerTree.ShortestPathResult;
import com.opensymphony.xwork2.Action;

public class getDataAction implements Action {

	String Disfile = "E://ScaleFreeNetwork TestData/snbt/EdgeDisMatrix.csv";
	String Adjfile = "E://ScaleFreeNetwork TestData/snbt/AdjcentMatrix.csv";
	String path = "E://ScaleFreeNetwork TestData/snbt/";
//	String Disfile = "E://ScaleFreeNetwork TestData/experiment/test/11EdgeDisMatrix.csv";
//	String Adjfile = "E://ScaleFreeNetwork TestData/experiment/test/11AdjcentMatrix.csv";
	public String query2;
	static int count = 0;

	public String getSelectednodes() {
		return selectednodes;
	}

	public void setSelectednodes(String selectednodes) {
		this.selectednodes = selectednodes;
	}

	public String selectednodes;

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getQuery() {
		return query2;
	}

	public void setQuery(String query) {
		this.query2 = query;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public Map<String, Object> map;

	public String getNewNetwork() {

		return SUCCESS;
	}

	public String ZhuJSONData() {
		System.out.println(query2);
		
		FindCorrespondingHierarchy fch = new FindCorrespondingHierarchy(path);
		AbstractMixedGraph amg = new AbstractMixedGraph();
		String disfr = path + "EdgeDisMatrix.csv";
		DisMAtrixAndIndex di = amg.ReadInDisMatrix(disfr);
		LinkedHashMap<String, ArrayList<String>> rootAndpath  = new  LinkedHashMap<String, ArrayList<String>>();
		
//		 STResult st = new STResult();
//		 ShortestPathResult spr = new ShortestPathResult();
		 ArrayList<String> keys = new ArrayList<String>();
		 for(String s:query2.split(",")){
		 keys.add(s);
		 }
		 
//		HashMap<Integer, String> jkey = new HashMap<Integer, String>();
//		HashMap<String, Integer> keyj = new HashMap<String, Integer>();
//		Graph G = spr.ConstructeG(Adjfile, jkey, keyj, keys);
		
//		Graph G = st.ConstructeG(Disfile, Adjfile, keys);
		
		
		fch.CircleFindCluster(1, 100, keys, "9973", fch.prmap, di.getDis(),
				di.getKeyi(),rootAndpath);	
		ArrayList<String> all =fch.ConstructFinalTree(rootAndpath, di.getDis(), di.getKeyi(),
				fch.prmap);
		Graph G = fch.ConstructG(all, keys);
		
		 List<Edge> es = G.getEdges();
		 List<Node> n = G.getNodes();
		


		Type t = getAllType(n, es);

		List<Nodes> l = NodeToNodes(n);

		List<Edges> e = EdgeToEdges(es);

		map = new HashMap<String, Object>();

		map.put("nodes", l);
		map.put("edges", e);
		map.put("types", t);

		System.out.println(map.size());
		return SUCCESS;
	}

	public List<Nodes> NodeToNodes(List<Node> n) {
		List<Nodes> nodes = new ArrayList<Nodes>();
		for (Node t : n) {
			Nodes s = new Nodes();
			s.setData(t);
			nodes.add(s);
		}
		return nodes;
	}

	public List<Edges> EdgeToEdges(List<Edge> es) {
		List<Edges> edges = new ArrayList<Edges>();
		for (Edge e : es) {
			Edges ed = new Edges();
			ed.setData(e);
			edges.add(ed);
		}
		return edges;
	}
	
	public List<Edge> randomEdges(List<Node> node) {
		int num = node.size();
		List<Edge> edge = new ArrayList<Edge>();
		for (int i = 0; i < num; i++) {
			String from = node.get(i).getId();
			Random r2 = new Random();
			int limit = r2.nextInt(2) + 1;
			for (int j = 0; j < limit; j++) {
				Random r1 = new Random();
				int temp = r1.nextInt(10) + 1;
				String to = node.get(temp).getId();

				Edge e = new Edge();
				e.setId(from + "_" + to);
				e.setSource(from);
				e.setTarget(to);

				if (from != to && !edge.contains(e)) {
					e.setWeight((double) temp / 10);
					e.setDirected(true);
					e.setType(node.get(i).getType() + "_"
							+ node.get(temp).getType());
					edge.add(e);

				}
			}
		}
		return edge;
	}
	
	public Type getAllType(List<Node> l, List<Edge> e) {
		// calculate type from nodes and edges
		List<NodeType> nt = new ArrayList<NodeType>();
		List<EdgeType> et = new ArrayList<EdgeType>();
		List<String> s = new ArrayList<String>();
		for (Node t : l) {
			if (!s.contains(t.getType())) {
				s.add(t.getType());
				NodeType n = new NodeType();
				n.setType(t.getType());
				nt.add(n);
			}
		}

		List<String> ss = new ArrayList<String>();
		for (Edge t : e) {
			if (!ss.contains(t.getType())) {
				ss.add(t.getType());
				EdgeType n = new EdgeType();
				n.setType(t.getType());
				et.add(n);
			}
		}

		Type t = new Type();
		t.setEdgetype(et);
		t.setNodetype(nt);
		return t;
	}
	
	public List<Node> geneToNodes() {
		List<String> gene = new ArrayList<String>();
		for(int i=0;i<100;i++){
			gene.add("Atest"+i);
		}
		List<Node> node = new ArrayList<Node>();
		for (String s : gene) {
			Node n = new Node();
			n.setId(s);
			n.setAbstracts("This is a tes tabstracts of this node.");
			n.setTitle(s);
			n.setDatabase("database:DO,GO");
			n.setLink("http://www.baidu.com");
			Random r = new Random();
			int type = r.nextInt(5);

			n.setType("type:"+Integer.toString(type+1));
			n.setNoderank(r.nextInt(20));
			Random r2 = new Random();
			int rr = r2.nextInt(10);
			n.setWeight(rr);
			node.add(n);
		}
		return node;
	}

}
