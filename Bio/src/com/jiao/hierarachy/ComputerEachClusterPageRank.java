package com.jiao.hierarachy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.jiao.element.AdjmatrixforPR;
import com.jiao.element.EdNd;
import com.jiao.file.DirMaker;
import com.jiao.file.FileReadAndWrite;
import com.jiao.mixedGraph.BasePageRank;

public class ComputerEachClusterPageRank {
	// 计算每一父节点的下面节点的cluster
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static DirMaker mdir = new DirMaker();
	public static double MAX = 0.0000001;// 阈值
	static double d = 0.85;// 阻尼系数
	HierarchyMapSerializable ms1;
	String basepath;
	static String proot = "10101";

	public ComputerEachClusterPageRank() {
	}

	public ComputerEachClusterPageRank(String basepath) {
		this.basepath = basepath;
		this.ms1 = HierarchyMapSerializable.loadMapData(new File(basepath
				+ "Leaf.dat"), true, 1);

	}

	public HashMap<String, EdNd> DataPrepare(HashMap<String, ArrayList<String>> map,
			String edgefr) {
		ArrayList<String> edges = fraw.ReadData(edgefr);
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		HashMap<String, EdNd> EN = new HashMap<String, EdNd>();
		while (it.hasNext()) {
			Object key = it.next();
			EdNd en = new EdNd();
			ArrayList<String> nodes = (ArrayList<String>) map.get(key);
			ArrayList<String> ed = new ArrayList<String>();
			for (String edge : edges) {
				String sou = edge.split(",")[0];
				String tar = edge.split(",")[1];
				if (nodes.contains(sou) && nodes.contains(tar)) {
					ed.add(edge);
				}
			}
		   en.setEdgesmap(ed);
		   en.setNodesmap(nodes);
		   EN.put(key.toString(), en);
		}
		
		System.out.println("===========");
		return EN;	
	}
	public HashMap<String, ArrayList<String>> ComputerPR(HashMap<String, EdNd> ens){
		BasePageRank dpfs = new BasePageRank();
		Set<String> set = ens.keySet();
		Iterator<String> it = set.iterator();
		HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		while (it.hasNext()) {
			Object key = it.next();
			EdNd en= ens.get(key);
			ArrayList<String> edges = en.getEdgesmap();
			ArrayList<String> nodes =en.getNodesmap();
			
			int n = nodes.size();
			double[] q1 = new double[n];
			for (int i = 0; i < n; i++) {
				q1[i] = 1;
			}
			AdjmatrixforPR apr = dpfs.adjacencymatrix(nodes, edges);
			double[][] u = dpfs.getU(n);
			double[] q = dpfs.calPageRank(apr.getM(), u, q1, d);
			ArrayList<String> value =MaxPageRank(q,apr.getIkey());
			map.put(key.toString(), value);
			System.out.println(key+","+value.size());
		}
		
		return map;
	}

	public ArrayList<String> MaxPageRank(double[] q,Map<Integer,String> ikey) {
		int len = q.length;
		double max = 0;
		ArrayList<String> nodes = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			if (q[i] > max)
				max = q[i];
		}
		for (int i = 0; i < len; i++) {
			if (q[i] == max)
				nodes.add(ikey.get(i));
		}
		return nodes;
	}
	
	public HashMap<String, ArrayList<String>> GlobalComputePR(HashMap<String, EdNd> ens){
		BasePageRank dpfs = new BasePageRank();
		Set<String> set = ens.keySet();
		Iterator<String> it = set.iterator();
		HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		
		EdNd all = ens.get(proot);
		ArrayList<String> aedges = all.getEdgesmap();
		ArrayList<String> anodes = all.getNodesmap();
		int an = anodes.size();
		double[] aq1 = new double[an];
		for (int i = 0; i < an; i++) {
			aq1[i] = 1;
		}
		AdjmatrixforPR aapr = dpfs.adjacencymatrix(anodes, aedges);
		double[][] au = dpfs.getU(an);
		double[] aq = dpfs.calPageRank(aapr.getM(), au, aq1, d);		
		System.out.println(aq.length);	
		while (it.hasNext()) {
			Object key = it.next();
			EdNd en= ens.get(key);
			ArrayList<String> edges = en.getEdgesmap();
			ArrayList<String> nodes =en.getNodesmap();	
			int n = nodes.size();
			double[] q = new double[n];
			AdjmatrixforPR apr = dpfs.adjacencymatrix(nodes, edges);
			int j = 0;
			for(int i =0 ;i <an;i++){
				if(nodes.contains(apr.getIkey())){
					q[j] = aq[i];
					j++;
				}		
			}
			ArrayList<String> value =MaxPageRank(q,apr.getIkey());
			map.put(key.toString(), value);
			System.out.println(key+","+value.size());
		}
		return map;	
	}
	public static void main(String[] args) {
		String basepath = "/home/lee/biolg/ScaleFreeNetwork TestData/nnbt/";
		ComputerEachClusterPageRank cep = new ComputerEachClusterPageRank(
				basepath);
		HashMap<String, ArrayList<String>> entleaf = cep.ms1.ent;
		cep.ComputerPR(cep.DataPrepare(entleaf, basepath+"Edges.csv"));
//		cep.GlobalComputePR(cep.DataPrepare(entleaf, basepath+"Edges.csv"));
		

	}

}
