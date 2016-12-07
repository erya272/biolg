package com.jiao.steinerTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.biosearch.bean.Edge;
import com.biosearch.bean.Node;
import com.jiao.element.Graph;
import com.jiao.file.FileReadAndWrite;
import com.jiao.nexoSim.ComputeSim;

public class ShortestPathResult {
	// 利用Dijkstra求任意两点之间的最短路径
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static int N = 4987;
	static ComputeSim cs =new ComputeSim();

	public void dijkstra(int n, int v, double dist[], int prev[], double[][] c) {
		int maxint = Integer.MAX_VALUE;
		;
		boolean[] s = new boolean[n]; // 记录该点是否被访问过
		for (int i = 0; i < n; i++) {
			dist[i] = c[v][i]; // 初始化
			s[i] = false;
			if (dist[i] == maxint) {
				prev[i] = 0;
			} else {
				prev[i] = v; // 起始点
			}
		}
		dist[v] = 0;
		s[v] = true; // 被访问过
		for (int i = 0; i < n + 1; i++) {
			double temp = maxint;
			int u = v;
			for (int j = 0; j < n; j++) {
				if ((!s[j]) && (dist[j] < temp)) {
					u = j; // 得到最短路径的终点
					temp = dist[j];
				}
			}
			s[u] = true;
			for (int j = 0; j < n; j++) {
				if ((!s[j]) && (c[u][j] < maxint)) {
					double newdist = dist[u] + c[u][j];
					if (newdist < dist[j]) {
						dist[j] = newdist; // 新的最短路径
						prev[j] = u; // 最短路径的上一个点
					}
				}
			}
		}
	}

	public int[] shortestPath(int n, int v1, int v2, double[][] c) {
		int q = 0;
		int[] way = new int[n];
		double[] dist = new double[n];
		int[] prev = new int[n];
		// 调用dijkstra算法，得到最短路
		dijkstra(n, v1, dist, prev, c);
		// 得到路径
		int w = v2;
		while (w != v1) {
			q++;
			way[q] = prev[w];
			w = prev[w];
		}
		// 得到正确顺序的路径
		int[] ret = new int[q + 1];
		int i, j;
		for (i = 0, j = q; j >= 1; i++, j--) {
			ret[i] = way[j];
		}
		// 路径的终点
		ret[i] = v2;
		return ret;

	}

	public String shortestPathAndCost(int n, int v1, int v2, double[][] c) {
		int q = 0;
		int[] way = new int[n];
		double[] dist = new double[n];
		int[] prev = new int[n];
		// 调用dijkstra算法，得到最短路
		dijkstra(n, v1, dist, prev, c);
		// 得到路径
		int w = v2;
		while (w != v1) {
			q++;
			way[q] = prev[w];
			w = prev[w];
		}
		// 得到正确顺序的路径
		int[] ret = new int[q + 1];
		int i, j;
		for (i = 0, j = q; j >= 1; i++, j--) {
			ret[i] = way[j];
		}
		// 路径的终点
		ret[i] = v2;
		String s = ret[0] + "";
		for (int k = 1; k < ret.length; k++) {
			s = s + "-" + ret[k];
		}
		s = s + "," + dist[v2];
		return s;

	}

	public double[][] ReadAdjMatrix(String adjfr,
			HashMap<Integer, String> jkey, HashMap<String, Integer> keyj) {

		ArrayList<String> res = new ArrayList<String>();
		res = fraw.ReadData(adjfr);
		int n = res.size() - 1;
		double[][] c = new double[n][n];
		int count = 0;
		String [] sss =res.get(0).split("\\s+");
		for (String s : sss) {
			// 空格需要转义
			String ss = s.split("\\.")[0];
			jkey.put(count, ss);
			keyj.put(ss, count);
			count++;
		}
		for (int i = 0; i < n; i++) {
			count = -1;
			String [] ssss =res.get(i + 1).split("\\s+");
			for (String s :ssss ) {
				if (count != -1) {
					if (Double.valueOf(s) != 0)// 对于邻接矩阵来说，自身到自身，以及那些点对没有直接边的值为INF，所以需要判断，把直接写入邻接矩阵中的0都变为INF
						c[i][count] = Double.valueOf(s);
					else
						c[i][count] = Double.MAX_VALUE;
				}
				count++;
			}
		}
		return c;
	}

	public ArrayList<String> SortAllPath(String adjfr,
			HashMap<Integer, String> jkey, HashMap<String, Integer> keyj,
			ArrayList<String> keys) {
		ArrayList<String> keywords = new ArrayList<String>();
		keywords.addAll(keys);
		jkey = new HashMap<Integer, String>();
		keyj = new HashMap<String, Integer>();
		int n = keys.size();
		ArrayList<String> path = new ArrayList<String>();
		LinkedHashMap<ArrayList<String>, Double> pc = new LinkedHashMap<ArrayList<String>, Double>();
		double[][] d = ReadAdjMatrix(adjfr, jkey, keyj);
		for (int k = 0; k < n; k++) {
			for (int j = k + 1; j < n; j++) {
				String s = shortestPathAndCost(20, keyj.get(keys.get(k) + ""),
						keyj.get(keys.get(j) + ""), d);
				double cost = Double.valueOf(s.split(",")[1]);
				String[] ret = s.split(",")[0].split("-");
				ArrayList<String> pathkey = new ArrayList<String>();
				for (int i = 0; i < ret.length; i++) {
					pathkey.add(jkey.get(Integer.valueOf(ret[i])));
				}
				pc.put(pathkey, cost);
			}
		}

		List<Map.Entry<ArrayList<String>, Double>> spc = new ArrayList<Map.Entry<ArrayList<String>, Double>>(
				pc.entrySet());
		Collections.sort(spc,
				new Comparator<Map.Entry<ArrayList<String>, Double>>() {
					@Override
					public int compare(Entry<ArrayList<String>, Double> o1,
							Entry<ArrayList<String>, Double> o2) {
						// TODO Auto-generated method stub
						if (o2.getValue() != null && o1.getValue() != null
								&& o2.getValue().compareTo(o1.getValue()) < 0) {
							return 1;
						} else {
							return -1;
						}
					}
				});
		System.out.println("Path Sort：");
		for (int i = 0; i < spc.size(); i++) {
			Entry<ArrayList<String>, Double> ent = spc.get(i);
			System.out.println(ent.getKey() + "=" + ent.getValue());

		}
		System.out.println("---------------");
		for (int i = 0; i < spc.size(); i++) {
			Entry<ArrayList<String>, Double> ent = spc.get(i);
			ArrayList<String> keypath = ent.getKey();
			String ksou = keypath.get(0);
			String ktar = keypath.get(keypath.size() - 1);
			// System.out.println(ksou+","+ktar);
			for (int j = 0; j < keypath.size() - 1; j++) {
				if (!path.contains(keypath.get(j) + "," + keypath.get(j + 1))
						&& !path.contains(keypath.get(j + 1) + ","
								+ keypath.get(j))) {
					path.add(keypath.get(j) + "," + keypath.get(j + 1));
				}
			}
			if (keys.contains(ksou))
				keys.remove(ksou);
			if (keys.contains(ktar))
				keys.remove(ktar);
			if (keys.isEmpty())
				break;
		}
		System.out.println("Path:" + path);
		keys.addAll(keywords);
		return path;

	}

	public ArrayList<String> GetAllPathByKeysPair(String adjfr,
			HashMap<Integer, String> jkey, HashMap<String, Integer> keyj,
			ArrayList<String> keys) {
		ArrayList<String> resu = new ArrayList<String>();
		ArrayList<String> fresu = new ArrayList<String>();
		double cost =0;
		jkey = new HashMap<Integer, String>();
		keyj = new HashMap<String, Integer>();
		double[][] d = ReadAdjMatrix(adjfr, jkey, keyj);
		for (int k = 0; k < keys.size(); k++) {
			for (int j = k + 1; j < keys.size(); j++) {
				if (!resu.contains(keys.get(k) + "," + keys.get(j))) {
					int[] res = shortestPath(N, keyj.get(keys.get(k) + ""),
							keyj.get(keys.get(j) + ""), d);
					for (int i = 0; i < res.length - 1; i++) {
						// System.out.println(res[i] + "," + res[i + 1]);
						if (!resu.contains(res[i] + "," + res[i + 1])
								&& !(resu.contains(res[i + 1] + "," + res[i]))) {
							resu.add(res[i] + "," + res[i + 1]);
							fresu.add(jkey.get(res[i]) + ","
									+ jkey.get(res[i + 1]));
							cost += d[res[i]][res[i + 1]];
						}
					}
				}
			}
		}
		System.out.println(resu);
		System.out.println(fresu);
		System.out.println("Cost------:"+cost);
		return fresu;
	}

	public Graph ConstructeG(String adjfr, HashMap<Integer, String> jkey,
			HashMap<String, Integer> keyj, ArrayList<String> keys) {
		Graph G = new Graph();
		long startTime = System.currentTimeMillis();
		ArrayList<String> res = GetAllPathByKeysPair(adjfr,jkey,keyj,keys);
		long endTime = System.currentTimeMillis();
		System.out.println("running time:"+(endTime-startTime));
//		ArrayList<String> res = SortAllPath(adjfr, jkey, keyj, keys);
		ArrayList<Node> nodes = new ArrayList<Node>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (String s : res) {
			String sou = s.split(",")[0];
			String tar = s.split(",")[1];
			Node node1 = new Node();
			node1.setTitle(sou);
			node1.setId(sou);
			Node node2 = new Node();
			node2.setTitle(tar);
			node2.setId(tar);
			if (!compare(nodes, node1))
				nodes.add(node1);
			if (!compare(nodes, node2))
				nodes.add(node2);
			Edge edge = new Edge();
			edge.setSource(sou);
			edge.setTarget(tar);
			edges.add(edge);
		}
		for(Node node :nodes){
			if(keys.contains(node.getTitle())){
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

	public static boolean compare(List<Node> p, Node in) {
		for (Node t : p) {
			if (t.getTitle() == in.getTitle()
					|| t.getTitle().equals(in.getTitle())) {
				return true;
			}

		}
		return false;
	}

	public static void main(String[] args) {
		ShortestPathResult spr = new ShortestPathResult();

		// double [][] c = {
		//
		// {0,1,1,4,Integer.MAX_VALUE,2,5,Integer.MAX_VALUE},
		// {1,0,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,2,Integer.MAX_VALUE,4},
		// {1,Integer.MAX_VALUE,0,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,3,Integer.MAX_VALUE},
		// {4,Integer.MAX_VALUE,Integer.MAX_VALUE,0,1,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE},
		// {Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,1,0,1,Integer.MAX_VALUE,Integer.MAX_VALUE},
		// {2,2,Integer.MAX_VALUE,Integer.MAX_VALUE,1,0,Integer.MAX_VALUE,Integer.MAX_VALUE},
		// {5,Integer.MAX_VALUE,3,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,0,1},
		// {Integer.MAX_VALUE,4,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,1,0}
		//
		// };
		// spr.shortestPath(8, 0, 7, c);

		HashMap<Integer, String> jkey = new HashMap<Integer, String>();
		HashMap<String, Integer> keyj = new HashMap<String, Integer>();
		String adjfr ="E://ScaleFreeNetwork TestData/experiment/test/12AdjcentMatrix.csv";
		ArrayList<String> keys = new ArrayList<String>();
		keys.add("瞿颖");
		keys.add("窦唯");
		keys.add("李亚鹏");
		keys.add("钟欣桐");
		keys.add("李大齐");
//		double[][] c =spr.ReadAdjMatrix(adjfr, jkey, keyj);
//		int[] res = spr.shortestPath(N, keyj.get("YHL018W"),keyj.get("YLR090W"), c);
		ArrayList<String> allpath = spr.GetAllPathByKeysPair(adjfr, jkey, keyj, keys);
		cs.allpathSim(allpath);
//		System.out.println(c[3][32]);
//		keys.add("LiYapeng");
//		keys.add("DouWei");
//		keys.add("ZhouXun");
//		keys.add("ZhangYadong");

		// keys.add("FangLishen");
		// keys.add("ZhangBozhi");
		// keys.add("ChenWeiting");
		// keys.add("ZhengZhongji");
		// keys.add("LiYapeng");
//		spr.SortAllPath(adjfr, jkey, keyj, keys);
//		 spr.ConstructeG(adjfr, jkey, keyj, keys);

	}
}
