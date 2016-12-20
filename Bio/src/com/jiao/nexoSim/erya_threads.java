package com.jiao.nexoSim;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.biosearch.bean.Edge;
import com.biosearch.bean.Node;
import com.jiao.element.DisMAtrixAndIndex;
import com.jiao.element.Graph;
import com.jiao.file.DirMaker;
import com.jiao.file.FileReadAndWrite;
import com.jiao.hierarachy.AbstractMixedGraph;
import com.jiao.hierarachy.HierarchyMapSerializable;
import com.jiao.hierarachy.HierarcyBackForFindResults;
import com.jiao.hierarachy.IndexForHierarchyTree;
import com.jiao.hierarachy.PRSerializable;
import com.jiao.nexoSim.ComputeSim;
import com.jiao.steinerTree.DreyfusWagner;
import com.jiao.steinerTree.ResBackForDrefus;
import com.jiao.steinerTree.ShortestPathResult;

public class erya_threads extends Thread {
	// 为输入的多关键字找到相应的可以运行ST的层次，暂且判断为有三个cluster
	// 谱树的层次是从1到100（1到N）

	String basepath;

	String fr1;
	String path;
	String fr2path;
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static DirMaker mdir = new DirMaker();
	static IndexForHierarchyTree inf = new IndexForHierarchyTree();
	PRSerializable prs;
	public HashMap<String, ArrayList<String>> prmap;
	// HierarchyMapSerializable ms1 = HierarchyMapSerializable
	// .loadMapData(new File(fr1), true, 1);
	HierarchyMapSerializable ms1;

	// static HashMap<String, ArrayList<String>> entleaf = ms1.ent;
	HashMap<String, ArrayList<String>> entleaf;
	// static int N = entleaf.size()+1;// 叶子节点的个数，也是那个cutoff 的层数
	static int N = 4987;
	static String proot = "9973";

	static ShortestPathResult spr = new ShortestPathResult();
	// 把序列化的cutoff全拷贝到内存中
	DreyfusWagner st = new DreyfusWagner();
	
	static ComputeSim cs =new ComputeSim();
	
	
	   private Thread t;
	   private String threadName;
	   
	   public int begin;
	   public int end;
	   public ArrayList<String> mkw;
	   public String root;
	   public HashMap<String, ArrayList<String>> pr;
	   public ArrayList<String> dis;
	   public HashMap<String, String> keyi;
	   public LinkedHashMap<String, ArrayList<String>> rootAndpath;
	   
	   	   
	public erya_threads() {
	}
	
	public erya_threads(String path,String name,int begin, int end,
			ArrayList<String> mkw, String root,
			HashMap<String, ArrayList<String>> pr, ArrayList<String> dis,
			HashMap<String, String> keyi,
			LinkedHashMap<String, ArrayList<String>> rootAndpath) {
		this.basepath = path;
		this.fr1 = path + "Leaf.dat";
		this.path = path + "Clusters/";
		this.fr2path = path + "Cutoff/";
		this.ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
		this.entleaf = this.ms1.ent;
//		this.N = entleaf.size() + 1;
		this.prs = PRSerializable.loadMapData(new File(basepath + "PR.dat"));
		this.prmap = this.prs.ent;
		this.threadName = name;
		
		this.begin =begin;
		this.end=end;
		this.mkw=mkw;
		this.root=root;
		this.pr=pr;
		this.dis=dis;
		this.keyi=keyi;
		this.rootAndpath=rootAndpath;
		
		
	}
	public erya_threads(String path,String name) {
		this.basepath = path;
		this.fr1 = path + "Leaf.dat";
		this.path = path + "Clusters/";
		this.fr2path = path + "Cutoff/";
		this.ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
		this.entleaf = this.ms1.ent;
//		this.N = entleaf.size() + 1;
		this.prs = PRSerializable.loadMapData(new File(basepath + "PR.dat"));
		this.prmap = this.prs.ent;
		this.threadName = name;
	}

	public HashMap<String, ArrayList<String>> FindHeirarcyForMultikeyWords(
			int index, ArrayList<String> mkw, String root,
			HashMap<String, ArrayList<String>> entcutoff) {
		// 首先是先找到这些关键字的cluster（有很多cluster都包含这些cluster），还是应该先找到一个层次往下走，直到满足这些节点所在的cluster满足ST
		// 运行的条件
		// 那个最优划分的层次怎么找到，如果用二分查找，继续进行查找的条件是什么
		// 设置一个hashMap 用来标记那一层包含keywords 的cluster

		// H ms2 = H.loadMapData(
		// new File(fr2path + root + ".dat"), true, 1);
		// HashMap<String, ArrayList<String>> entcutoff = ms2.ent;

		ArrayList<String> cutclus = new ArrayList<String>();
		cutclus = entcutoff.get(index + "");

		// 目前下面的部分已经实现了对于每一层中包含keywords的cluster的定位。
		HashMap<String, ArrayList<String>> mark = new HashMap<String, ArrayList<String>>();
		for (String s : cutclus) {
			ArrayList<String> keys = new ArrayList<String>();// 用来存储每个cluster包含的keywords
			// if (Integer.valueOf(s) > N)
			if (inf.isNumberic(s)) {
				ArrayList<String> leafvalue = new ArrayList<String>();
				leafvalue = entleaf.get(s);
				for (String key : mkw) {
					if (leafvalue.contains(key))
						keys.add(key);
				}
			} else {
				for (String key : mkw) {
					if (s.equals(key) || s == key) {
						keys.add(key);
						break;
					}
				}
			}
			if (!keys.isEmpty())
				mark.put(s, keys);
		}
		return mark;

	}

	public HierarcyBackForFindResults BinarySearch(int begin, int end,
			ArrayList<String> mkw, String root,
			HashMap<String, ArrayList<String>> entcutoff) {
		// 二分查找，确定层次最优划分之后，进行代码调整修改,为了搜索的局部最优
		// 找到使多个关键字在三个clusters中就是二分查找停止的条件
		int low = begin;
		int high = end;

		HierarcyBackForFindResults res = new HierarcyBackForFindResults();
		HashMap<String, ArrayList<String>> mark = new HashMap<String, ArrayList<String>>();
		while (low <= high) {
			int mid = (low + high) / 2;
			int clustersize = Integer.MAX_VALUE;
			if (FindHeirarcyForMultikeyWords(mid, mkw, root, entcutoff).size() >= 3) {
				mark = FindHeirarcyForMultikeyWords(mid, mkw, root, entcutoff);
				res.setIndex(mid + "");
				ArrayList<String> con = new ArrayList<String>();
				for (String s : entcutoff.get(mid + "")) {
					// 这样可以根据root找到相应的层次
					// if ((Integer.valueOf(s) < N + 1) &&
					// entleaf.get(root).contains(s))
					if ((!inf.isNumberic(s)) && entleaf.get(root).contains(s))
						con.add(s);
					// if ((Integer.valueOf(s) > N) &&
					// entleaf.get(root).containsAll(entleaf.get(s)))
					if ((inf.isNumberic(s))
							&& entleaf.get(root).containsAll(entleaf.get(s)))
						con.add(s);
				}
				// res.setIndexContent(entcutoff.get(mid+""));
				res.setIndexContent(con);
				res.setClusterContainKey(mark);
				if (clustersize > res.getIndexContent().size()) {
					clustersize = res.getIndexContent().size();
					high = mid - 1;
				} else
					break;
			}
			// else if(FindHeirarcyForMultikeyWords(mid,mkw).size() >3){
			// high = mid-1;
			// }
			else if (FindHeirarcyForMultikeyWords(mid, mkw, root, entcutoff)
					.size() < 3) {
				low = mid + 1;
			}
		}
		res.setRoot(root);
		return res;
	}

	public void PartitionLeafNodesToCorrespondingCluster(String edgedisfr,
			String index, ArrayList<String> Icon, String root) {
		// 对应的把按层次分割好的节点和cluster，分别写入相对应的位置
		ArrayList<String> edges = new ArrayList<String>();
		edges = fraw.ReadData(edgedisfr);
		String IndependentNodes = path + root + "/" + index + "/";
		mdir.makeDir(new File(IndependentNodes));
		String IN = path + root + "/" + index + "/IN/";
		mdir.makeDir(new File(IN));
		String SuN = path + root + "/" + index + "/";
		mdir.makeDir(new File(SuN));
		String SN = path + root + "/" + index + "/SN/";
		mdir.makeDir(new File(SN));
		String EdgesDis = path + root + "/" + index + "/EdgesDis/";
		mdir.makeDir(new File(EdgesDis));
		if (new File(IndependentNodes + "abstract.csv").exists())
			new File(IndependentNodes + "abstract.csv").delete();

		for (String s : Icon) {
			// if (Integer.valueOf(s) <= N)
			if (!inf.isNumberic(s)) {
				fraw.WriteToFile(IndependentNodes + "IndependentNodes.csv", s);
				for (String edge : edges) {
					String sou = edge.split(",")[0];
					String tar = edge.split(",")[1];
					if (sou.equals(s) || tar.equals(s) || sou == s || tar == s) {
						fraw.WriteToFile(IN + s + ".csv", edge);
					}
				}
			} else {
				ArrayList<String> nodes = entleaf.get(s);
				fraw.WriteToFile(SuN + "SuperNodes.csv", s);
				for (String leaf : nodes) {
					fraw.WriteToFile(SN + s + ".csv", leaf);
				}
				for (String edge : edges) {
					String sou = edge.split(",")[0];
					String tar = edge.split(",")[1];
					if (nodes.contains(tar) && nodes.contains(sou))
						fraw.WriteToFile(EdgesDis + s + ".csv", edge);

					// 只包含其中一个的SN，与其他节点的链接。
					if (nodes.contains(sou) && !nodes.contains(tar)) {
						fraw.WriteToFile(IndependentNodes + "abstract1.csv", s
								+ "," + tar);
					}
					if (nodes.contains(tar) && !nodes.contains(sou)) {
						fraw.WriteToFile(IndependentNodes + "abstract1.csv", s
								+ "," + sou);
					}
				}
			}
		}
	}



	public ArrayList<String> ApplayDreyfus(HierarcyBackForFindResults res) {
		// 通过求出来的层次结果，对该层次进行应用ST,求出该层次的ST树。
		String Disfile = path + res.getRoot() + "/" + res.getIndex() + "/"
				+ "disMatrix.csv";
		String adjfile = path + res.getRoot() + "/" + res.getIndex() + "/"
				+ "AdjcentMatrix.csv";
		ResBackForDrefus dfs = st.getMixedGraphDistanceMatrixFromFile(Disfile);
		double[][] Dis = dfs.getDis();
		HashMap<Integer, String> jkey = new HashMap<Integer, String>();
		jkey = dfs.getJkey();
		HashMap<String, Integer> keyj = new HashMap<String, Integer>();
		keyj = dfs.getKeyj();
		double[][] adj = spr.ReadAdjMatrix(adjfile, jkey, keyj);
		Set<Integer> N = new HashSet<Integer>();
		Set<Integer> Y = new HashSet<Integer>();
		int len = Dis.length;
		for (int i = 0; i < len; i++) {
			N.add(i);
		}
		HashMap<String, ArrayList<String>> map = res.getClusterContainKey();
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			Y.add(keyj.get(key));
		}
		System.out.println("run:\t"+N.size() + " "+Y.size()); ////  +" "+map.size()+map  
		return st.RunDreyfusWagner1(N, Y, Dis, jkey, adj);
		// 这里可以返回ArrayList，包含边的
	}

	public HierarcyBackForFindResults OneOptimalFindHierarchy(int begin,
			int end, ArrayList<String> mkw, String root,
			HashMap<String, ArrayList<String>> entcutoff) {
		// 一次找到全局最优的层次，使每个包含关键字的cluster中只包含一个关键字，使运行一次ST就能满足要求
		int low = begin;
		int high = end;
		HierarcyBackForFindResults res = new HierarcyBackForFindResults();
		HashMap<String, ArrayList<String>> mark = new HashMap<String, ArrayList<String>>();
		while (low <= high) {
			int mid = (low + high) / 2;
			int clustersize = Integer.MAX_VALUE;
			if (FindHeirarcyForMultikeyWords(mid, mkw, root, entcutoff).size() == mkw
					.size()) {
				mark = FindHeirarcyForMultikeyWords(mid, mkw, root, entcutoff);
				res.setIndex(mid + "");
				ArrayList<String> con = new ArrayList<String>();
				for (String s : entcutoff.get(mid + "")) {
					// 这样可以根据root找到相应的层次
					if ((Integer.valueOf(s) < N + 1)
							&& entleaf.get(root).contains(s))
						con.add(s);
					if ((Integer.valueOf(s) > N)
							&& entleaf.get(root).containsAll(entleaf.get(s)))
						con.add(s);
				}
				// res.setIndexContent(entcutoff.get(mid+""));
				res.setIndexContent(con);
				res.setClusterContainKey(mark);
				if (clustersize > res.getIndexContent().size()) {
					clustersize = res.getIndexContent().size();
					high = mid - 1;
				} else
					break;
			} else if (FindHeirarcyForMultikeyWords(mid, mkw, root, entcutoff)
					.size() < mkw.size()) {
				low = mid + 1;
			}
		}
		res.setRoot(root);
		return res;
	}

	public ArrayList<String> getHeirarchyPath(
			HashMap<String, ArrayList<String>> pr,
			HashMap<String, ArrayList<String>> ck, ArrayList<String> STres,
			String root, ArrayList<String> dis, HashMap<String, String> keyi) {
		// 提供该层次的路径，找到该层次的该父节点的最短路径矩阵，然后直接在该层次进行两个节点的最短路径计算。
		// 对于两个节点，或是直接使用PageRank值把它们连接起来展示？
		Set<String> set = ck.keySet();
		Iterator<String> it = set.iterator();
		ArrayList<String> pa = new ArrayList<String>();
		HashMap<String, String> newpr = new HashMap<String, String>();
		while (it.hasNext()) {
			String s = null;
			Object key = it.next();
			if (pr.containsKey(key)) {
				ArrayList<String> ls = ck.get(key);
				ArrayList<String> mp = pr.get(key);
				if (ls.size() == 1) {
					String w = ls.get(0);
					String mid = mp.get(0);
					int i = Integer.valueOf(keyi.get(w));
					double md = Double.MAX_VALUE;
					for (String m : mp) {
						int j = Integer.valueOf(keyi.get(m));
						String d = dis.get(i + 1).split("\t")[j + 1];
						if (!d.equals("Inf") && d != "Inf") {
							if (Double.valueOf(d) < md) {
								md = Double.valueOf(d);
								mid = m;
							}
						}
					}
					newpr.put(key.toString(), mid);
					if (w != mid && !mid.equals(w)) {
						s = w + "," + mid;
					}
				} else if (ls.size() == 2) {
					String w1 = ls.get(0);
					String w2 = ls.get(1);
					String mid = mp.get(0);
					int i = Integer.valueOf(keyi.get(w1));
					int j = Integer.valueOf(keyi.get(w2));
					double md = Double.MAX_VALUE;
					for (String m : mp) {
						int k = Integer.valueOf(keyi.get(m));
						String [] diss = dis.get(k + 1).split("\t");
						String d1 = diss[i + 1];
						String d2 = diss[j + 1];
						if (!d1.equals("Inf") && d1 != "Inf"
								&& !d2.equals("Inf") && d2 != "Inf") {
							if (Double.valueOf(d1) + Double.valueOf(d2) < md) {
								md = Double.valueOf(d1) + Double.valueOf(d2);
								mid = m;
							}
						}
					}
					newpr.put(key.toString(), mid);
					int k = Integer.valueOf(keyi.get(mid));
					String d1 = dis.get(k + 1).split("\t")[i + 1];
					String d2 = dis.get(k + 1).split("\t")[j + 1];
					String d3 = dis.get(i + 1).split("\t")[j + 1];
					if (w1 != mid && !mid.equals(w1) && w2 != mid
							&& !mid.equals(w2)) {
						s = w1 + "," + mid + "," + w2;
						if (md > Double.valueOf(d1) + Double.valueOf(d3)) {
							md = Double.valueOf(d1) + Double.valueOf(d3);
							s = mid + "," + w1 + "," + w2;
						}
						if (md > Double.valueOf(d2) + Double.valueOf(d3)) {
							md = Double.valueOf(d2) + Double.valueOf(d3);
							s = mid + "," + w2 + "," + w1;
						}
					} else
						s = w1 + "," + w2;
				}
				if (s != null)
					if (s.split(",").length == 3) {
						String s1 = s.split(",")[0];
						String s2 = s.split(",")[1];
						String s3 = s.split(",")[2];
						pa.add(s1 + "," + s2);
						pa.add(s2 + "," + s3);
					} else
						pa.add(s);
			}
		}
		for (String s : STres) {
			String sou = s.split(",")[0];
			String tar = s.split(",")[1];
			if (pr.containsKey(sou)) {
				sou = newpr.get(sou);
				if (sou == null)
					sou = s.split(",")[0];
			}

			if (pr.containsKey(tar)) {
				tar = newpr.get(tar);
				if (tar == null)
					tar = s.split(",")[1];
			}
			if (pr.containsKey(sou) && !ck.containsKey(sou)) {
				sou = pr.get(sou).get(0);
			}
			if (pr.containsKey(tar) && !ck.containsKey(tar)) {
				tar = pr.get(tar).get(0);
			}
			pa.add(sou + "," + tar);
		}
		return pa;
	}

	public ArrayList<String> ConstructFinalTree(
			LinkedHashMap<String, ArrayList<String>> rAp,
			ArrayList<String> dis, HashMap<String, String> keyi,
			HashMap<String, ArrayList<String>> pr) {
		
		Set<String> set = rAp.keySet();
		Iterator<String> it = set.iterator();
		ArrayList<String> roots = new ArrayList<String>();
		HashMap<String, String> npr = new HashMap<String, String>();
		LinkedHashMap<String, ArrayList<String>> rAn = new LinkedHashMap<String, ArrayList<String>>();
		HashMap<String, Boolean> flag = new HashMap<String, Boolean>();
		ArrayList<String> allPath = new ArrayList<String>();
		while (it.hasNext()) {
			String key = it.next();
			roots.add(key);
			flag.put(key, true);
			ArrayList<String> path = rAp.get(key);
			ArrayList<String> nodes = new ArrayList<String>();
			for (String p : path) {
				String sou = p.split(",")[0];
				String tar = p.split(",")[1];
				if (!nodes.contains(sou))
					nodes.add(sou);
				if (!nodes.contains(tar))
					nodes.add(tar);
				if (pr.get(sou) != null || pr.get(tar) != null){	
					flag.put(key, false);
				}
					
			}
			rAn.put(key, nodes);
		}
		
		for (int i = roots.size() - 1; i >= 0; i--) {
			String key = roots.get(i);
			if (flag.get(key) == true && !key.equals(proot) && key != proot) {
				ArrayList<String> prs = pr.get(key);
				double min = Double.MAX_VALUE;
				String fp = null;
				String mps = null;
				String minnode = null;
				for (String ps : prs) {
					if (rAn.get(key).contains(ps)) {
						npr.put(key, ps);
						break;
					} else {
						int k = Integer.valueOf(keyi.get(ps));
						double sum = 0;
						double smin = Double.MAX_VALUE;
						for (String node : rAn.get(key)) {
							int kk = Integer.valueOf(keyi.get(node));
							String d = dis.get(k + 1).split("\t")[kk + 1];
							sum += Double.valueOf(d);
							if (smin > Double.valueOf(d)) {
								minnode = node;
								smin = Double.valueOf(d);
							}
						}
						if (min > sum) {
							min = sum;
							mps = ps;
							fp = mps + "," + minnode;
							npr.put(key, mps);
						}
					}
				}
				if (fp != null) {
					allPath.add(fp);
//					System.out.println("fp: "+fp);   ////////
				}
				allPath.addAll(rAp.get(key));
			} else {
				for (String path : rAp.get(key)) {
					String sou = path.split(",")[0];
					String tar = path.split(",")[1];
					boolean fs = false;
					boolean ft = false;
					if (npr.containsKey(sou)) {
						int index = rAn.get(key).indexOf(sou);
						sou = npr.get(sou);
						fs = true;
						if(index!=-1)
							rAn.get(key).set(index, sou);

					}
					if (npr.containsKey(tar)) {
						int index = rAn.get(key).indexOf(tar);
//						System.out.println(index+"..."+key+"..."+rAn.get(key));
						tar = npr.get(tar);
						ft = true;
						if(index!=-1)
							rAn.get(key).set(index, tar);
					}
					if (fs == true || ft == true) {
						int index = rAp.get(key).indexOf(path);
						if(index !=-1)
							rAp.get(key).set(index, path);
					}
					allPath.add(sou + "," + tar);
				}
//				System.out.println("\nheerere\n");
				if (!key.equals(proot) && key != proot) {
					double min = Double.MAX_VALUE;
					String fp = null;
					String mps = null;
					String minnode = null;
					for (String ps : pr.get(key)) {
//						System.out.println("\nheerere\n");
						if (rAn.get(key).contains(ps)) {
							npr.put(key, ps);
							break;
						} else {							
							int k = Integer.valueOf(keyi.get(ps));
							double sum = 0;
							double smin = Double.MAX_VALUE;
							for (String node : rAn.get(key)) {
								int kk = Integer.valueOf(keyi.get(node));
								String d = dis.get(k + 1).split("\t")[kk + 1];
								sum += Double.valueOf(d);
								if (smin > Double.valueOf(d)) {
									minnode = node;
									smin = Double.valueOf(d);
								}
							}
							if (min > sum) {
								min = sum;
								mps = ps;
								fp = mps + "," + minnode;
								npr.put(key, mps);
							}
						}
					}
//					System.out.println(key + "====" + npr.get(key));
					if (fp != null) {
						allPath.add(fp);
//						System.out.println("fp 2: " +fp);
					}
				}

			}

		}

//		double cost = 0;
//		for (String s : allPath) {
//			String sou = s.split(",")[0];
//			String tar = s.split(",")[1];
//			int i = Integer.valueOf(keyi.get(sou));
//			int j = Integer.valueOf(keyi.get(tar));
//			String d = dis.get(i + 1).split("\t")[j + 1];
//			System.out.println(s+"-------"+d);
//			cost += Double.valueOf(d);
//		}
//
//		System.out.println(cost);
		System.out.println("allpath:\t"+allPath);
		return allPath;

	}
	
	public double Cost(ArrayList<String> allPath,ArrayList<String> dis, HashMap<String, String> keyi){
		double cost =0;
		for (String s : allPath) {
			String sou = s.split(",")[0];
			String tar = s.split(",")[1];
			int i = Integer.valueOf(keyi.get(sou));
			int j = Integer.valueOf(keyi.get(tar));
			String d = dis.get(i + 1).split("\t")[j + 1];
//			System.out.println(s+"-------"+d);
			cost += Double.valueOf(d);
		}
		return cost;
	}

	public ArrayList<String> getAllName(String path) {
		ArrayList<String> allName = new ArrayList<String>();
		File f = new File(path);
		File fa[] = f.listFiles();
		for (int i = 0; i < fa.length; i++) {
			File fs = fa[i];
			String s = fs.getName();
			allName.add(s.substring(0, s.length() - 4));
		}
		return allName;
	}

	public Graph ConstructG(ArrayList<String> fallPath,
			ArrayList<String> keywords) {
		Graph G = new Graph();
		ArrayList<Node> nodes = new ArrayList<Node>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (String s : fallPath) {
			String sou = s.split(",")[0];
			String tar = s.split(",")[1];
			Node node1 = new Node();
			node1.setAbstracts(sou);
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
		for (Node node : nodes) {
			if (keywords.contains(node.getTitle())) {
				node.setType("queried");
				System.out.println(node.getTitle() + "," + node.getType());
			} else {
				node.setType("no");
				System.out.println(node.getTitle() + "," + node.getType());
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

	public threads_data CircleFindCluster(int begin, int end,
			ArrayList<String> mkw, String root,
			HashMap<String, ArrayList<String>> pr, ArrayList<String> dis,
			HashMap<String, String> keyi,
			LinkedHashMap<String, ArrayList<String>> rootAndpath) {
		// 循环找到对应的层次，并在对应的层次上运行ST
		
		HierarchyMapSerializable ms2 = HierarchyMapSerializable.loadMapData(
				new File(fr2path + root + ".dat"), true, 1);
		HashMap<String, ArrayList<String>> entcutoff = ms2.ent;
		end = entcutoff.size();
		
		HierarcyBackForFindResults res = BinarySearch(begin, end, mkw, root,entcutoff);		
		HashMap<String, ArrayList<String>> map = res.getClusterContainKey();	
		
		st_data_erya st_data = new st_data_erya(root, pr, dis, keyi, res);
		
		ArrayList<String> STres = ApplayDreyfus(res);
		System.out.println("STres: -------" + STres + "-------");		
		ArrayList<String> path = getHeirarchyPath(pr, map, STres, root, dis,
				keyi);
		rootAndpath.put(root, path);
		
		
//		System.out.println("map:\t"+map);
		
		threads_data dt = new threads_data(begin, end,  pr,dis,  keyi,map,res,st_data);
		
		return dt;	
	}
	
	
/*	int begin, int end,
	ArrayList<String> mkw, String root,
	HashMap<String, ArrayList<String>> pr, ArrayList<String> dis,
	HashMap<String, String> keyi,
	LinkedHashMap<String, ArrayList<String>> rootAndpath*/
	
	public threads_data run(int begin, int end,
			ArrayList<String> mkw, String root,
			HashMap<String, ArrayList<String>> pr, ArrayList<String> dis,
			HashMap<String, String> keyi,
			LinkedHashMap<String, ArrayList<String>> rootAndpath){   				
	      System.out.println("Running " +  threadName );     	      
	      threads_data ress = CircleFindCluster(begin, end, mkw, root, pr, dis ,keyi, rootAndpath);   
	      return ress;	     
	}
	
	
	public void  start(){
//	      System.out.println("Starting " +  threadName );
	      if (t == null) {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
//	      return ress;
	}	
	
	
	
	
	public static void main(String[] args) {
		String path = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";
		erya_threads fch = new erya_threads(path,"erya_thread..");
		ArrayList<String> keys = new ArrayList<String>();
		AbstractMixedGraph amg = new AbstractMixedGraph();
		String disfr = path + "EdgeDisMatrix.csv";
		DisMAtrixAndIndex di = amg.ReadInDisMatrix(disfr);
		LinkedHashMap<String, ArrayList<String>> rootAndpath = new LinkedHashMap<String, ArrayList<String>>();

		 keys.add("YDL141W");
		 keys.add("YHR185C");
		 keys.add("YHR008C");
		 keys.add("YAL014C");
		 keys.add("YDL145C");
	

		long startTime = System.currentTimeMillis();
		fch.CircleFindCluster(1, 100, keys, proot, fch.prmap, di.getDis(),
				di.getKeyi(), rootAndpath);
		ArrayList<String> all = fch.ConstructFinalTree(rootAndpath,
				di.getDis(), di.getKeyi(), fch.prmap);
		System.out.println("===+++"+all);
		cs.allpathSim(all);
		fch.ConstructG(all, keys);
		long endTime = System.currentTimeMillis();
		System.out.println("running time:" + "=======" + (endTime - startTime)
				+ "ms");				
	}
}
