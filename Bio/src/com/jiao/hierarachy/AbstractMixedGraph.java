package com.jiao.hierarachy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.jiao.element.DisMAtrixAndIndex;
import com.jiao.file.FileReadAndWrite;

public class AbstractMixedGraph {
	// 抽象图的边和节点
	// weight = 1.0在这里是只考虑PageRank值最大的点作为边的代表
	// 依照老师的idea的话，那么两个SN之间边的抽象应该是两个社团之间的PageRank值最小的边
	// SN 和IN之间边是IN与SN对于的社团中PageRank值之间的边的大小。
	// 或者用两个社团的平均距离来作为抽象边的大小。
	static FileReadAndWrite fraw = new FileReadAndWrite();
	String basepath;
	String edgedis;
	PRSerializable prs;
	HashMap<String, ArrayList<String>> prmap;
	HierarchyMapSerializable ms1;
	HashMap<String, ArrayList<String>> entleaf;

	public AbstractMixedGraph() {
	}

	public AbstractMixedGraph(String basepath) {
		this.basepath = basepath;
		this.edgedis = basepath + "EdgeDis.csv";
		this.prs = PRSerializable.loadMapData(new File(basepath + "PR.dat"));
		this.prmap = this.prs.ent;
		this.ms1 = HierarchyMapSerializable.loadMapData(new File(basepath
				+ "Leaf.dat"), true, 1);
		this.entleaf = this.ms1.ent;
	}

	public HashMap<String, ArrayList<String>> CenterNormalNodesMap(String path) {
		// 获取所有以center为SuperNode的每一个SuperNode点集合
		File file = new File(path);
		File[] tempList = file.listFiles();
		int n = tempList.length;
		HashMap<String, ArrayList<String>> cn = new HashMap<String, ArrayList<String>>();
		for (int i = 0; i < n; i++) {
			ArrayList<String> nodes = new ArrayList<String>();
			nodes = fraw.ReadData(tempList[i].toString());
			String c = tempList[i].getName().split("\\.")[0];
			cn.put(c, nodes);
		}
		return cn;
	}

	public DisMAtrixAndIndex ReadInDisMatrix(String fr) {
		DisMAtrixAndIndex di = new DisMAtrixAndIndex();
		ArrayList<String> read = fraw.ReadData(fr);
		HashMap<String, String> ikey = new HashMap<String, String>();
		HashMap<String, String> keyi = new HashMap<String, String>();
		String s = read.get(0);
		int len = read.size() - 1;
		System.out.println(len);
		// double[][] dis = new double[len][len];
		String[] arr = s.split("\t");
		for (int i = 0; i < len; i++) {
			ikey.put(i + "", arr[i]);
			keyi.put(arr[i], i + "");
			// String ss = read.get(i + 1);
			// System.out.println(i);
			// for (int j = i; j < len; j++) {
			// String sss = ss.split("\t")[j + 1];
			// if (sss.equals("Inf") || sss == "Inf"){
			// dis[i][j] = Double.MAX_VALUE;
			// dis[j][i] = Double.MAX_VALUE;
			// }
			// else{
			// dis[i][j] = Double.valueOf(sss);
			// dis[j][i] = Double.valueOf(sss);
			// }
			// }
		}
		di.setDis(read);
		di.setIkey(ikey);
		di.setKeyi(keyi);
//		System.out.println("++++++++++++++");
		return di;
	}

	public String getTwoSuperNodeRelationbyMinDis(String sn1, String sn2,
			HashMap<String, ArrayList<String>> leaf, ArrayList<String> dis,
			HashMap<String, String> keyi) {
		ArrayList<String> nn1 = leaf.get(sn1);
		ArrayList<String> nn2 = leaf.get(sn2);
		String s = null;
		outer: for (String n1 : nn1) {
			for (String n2 : nn2) {
				int i = Integer.valueOf(keyi.get(n1));
				int j = Integer.valueOf(keyi.get(n2));
				String d = dis.get(i + 1).split("\t")[j + 1];
				if (!d.equals("Inf") || d != "Inf") {
					if (Double.valueOf(d) == 1) {
						s = n1 + "," + n2 + "," + d;
						break outer;
					}
				}
			}
		}
		return s;

	}

	public String getTwoSuperNodeRelationbyPR(String sn1, String sn2,
			HashMap<String, ArrayList<String>> prmap, ArrayList<String> dis,
			HashMap<String, String> keyi) {
		ArrayList<String> pn1 = prmap.get(sn1);
		ArrayList<String> pn2 = prmap.get(sn2);
		// 获取社团中PageRank值最大的list,从其中选取使两个社团地距离最小的两个PageRank；
		String s = null;
		double min = Double.MAX_VALUE;
		for (String n1 : pn1) {
			for (String n2 : pn2) {
				int i = Integer.valueOf(keyi.get(n1));
				int j = Integer.valueOf(keyi.get(n2));
				String d = dis.get(i + 1).split("\t")[j + 1];
				if (!d.equals("Inf") || d != "Inf") {
					if (Double.valueOf(d) < min) {
						min = Double.valueOf(d);
						s = n1 + "," + n2 + "," + min;
					}
				}
			}
		}
		return s;
	}

	public String getIndependentNodeAndSuperNodeRelationbyMinDis(String in1,
			String sn2, HashMap<String, ArrayList<String>> leaf,
			ArrayList<String> dis, HashMap<String, String> keyi) {
		String s = null;
		ArrayList<String> nn2 = leaf.get(sn2);
		int i = Integer.valueOf(keyi.get(in1));
		for (String n2 : nn2) {
			int j = Integer.valueOf(keyi.get(n2));
			String d = dis.get(i + 1).split("\t")[j + 1];
			if (!d.equals("Inf") || d != "Inf") {
			}
		}
		return s;

	}

	public String getIndependentNodeAndSuperNodeRelation(String in1,
			String sn1, HashMap<String, ArrayList<String>> prmap,
			ArrayList<String> dis, HashMap<String, String> keyi) {
		ArrayList<String> pn1 = prmap.get(sn1);
		// 获取社团中PageRank值最大的list,从其中选取使两个社团地距离最小的两个PageRank；
		String s = null;
		double min = Double.MAX_VALUE;
		int i = Integer.valueOf(keyi.get(in1));
		for (String n1 : pn1) {
			int j = Integer.valueOf(keyi.get(n1));
			String d = dis.get(i + 1).split("\t")[j + 1];
			if (!d.equals("Inf") || d != "Inf") {
				if (Double.valueOf(d) < min) {
					min = Double.valueOf(d);
					s = n1 + "," + in1 + "," + min;
				}
			}
		}
		return s;
	}

	public String getTwoindepedentNodeRelation(String in1, String in2,
			String edgedisfile) {
		ArrayList<String> edgedis = new ArrayList<String>();
		edgedis = fraw.ReadData(edgedisfile);
		String resedge = null;
		for (String s : edgedis) {
			String ss[] = s.split(",");
			String sou = ss[0];
			String tar = ss[1];
			if (in1.equals(sou) && in2.equals(tar) || in1.equals(tar)
					&& in2.equals(sou)) {
				resedge = s;
				return resedge;
			}
		}
		return resedge;
	}

	public String getTwoindepedentNodeRelationbyMinDis(String in1, String in2,
			ArrayList<String> dis, HashMap<String, String> keyi) {
		int i = Integer.valueOf(keyi.get(in1));
		int j = Integer.valueOf(keyi.get(in2));
		String s = null;
		String d = dis.get(i + 1).split("\t")[j + 1];
		if (!d.equals("Inf") || d != "Inf") {
			if (Double.valueOf(d) == 1)
				s = in1 + "," + in2 + "," + Double.valueOf(d);
		}
		return s;
	}

	public HashMap<String, String> getMixedGraph(String snsfile,
			String insfile, String mixedgefile, String edgedisfile,
			String absRealationfile, ArrayList<String> dis,
			HashMap<String, String> keyi,
			HashMap<String, ArrayList<String>> leaf, String absedgesfr) {
		ArrayList<String> sns = new ArrayList<String>();
		ArrayList<String> ins = new ArrayList<String>();
		if (new File(snsfile).exists())
			sns = fraw.ReadData(snsfile);
		if (new File(insfile).exists())
			ins = fraw.ReadData(insfile);
		ArrayList<String> edges = fraw.ReadData(edgedisfile);
		ArrayList<String> absedges =new ArrayList<String>();
		if(new File(absedgesfr).exists())
			absedges =fraw.ReadData(absedgesfr);
		ArrayList<String> remcopy = new ArrayList<String>();
		// System.out.println(ins);
//		int snslen = sns.size();
		// int inslen = ins.size();
		HashMap<String, String> mixg = new HashMap<String, String>();
		// for (int i = 0; i < snslen; i++) {
		// String sn1 = sns.get(i);
		// for (int j = i + 1; j < snslen; j++) {
		// String sn2 = sns.get(j);
		// String res = getTwoSuperNodeRelationbyMinDis(sn1, sn2, leaf,
		// dis, keyi);
		// if (res != null) {
		// mixg.put(sn1 + "," + sn2, res);
		// // System.out.println("+++++"+res);
		// fraw.WriteToFile(mixedgefile,
		// sn1 + "," + sn2 + "," + res.split(",")[2]);
		// fraw.WriteToFile(absRealationfile, sn1 + "," + sn2 + ","
		// + res.split(",")[2] + "," + res.split(",")[0] + ","
		// + res.split(",")[1]);
		// }
		// }
		// }

		if(new File(mixedgefile).exists())
			new File(mixedgefile).delete();
		
		for (String s : edges) {
			String ss[] = s.split(",");
			String sou = ss[0];
			String tar = ss[1];
			if (ins.contains(sou) && ins.contains(tar)) {
				fraw.WriteToFile(mixedgefile, s);
			}
		}
		for (String s : absedges) {
			String ss[] = s.split(",");
			String sou = ss[0];
			String tar = ss[1];
			if (sns.contains(sou) && ins.contains(tar)) {
				fraw.WriteToFile(mixedgefile, s + "," + 1);
			}
			for (String sn : sns) {
				if (sns.contains(sou) && leaf.get(sn).contains(tar)) {
					String sss1 = sou + "," + sn + "," + 1;
					String sss2 = sn + "," + sou + "," + 1;
					if (!remcopy.contains(sss1) && !remcopy.contains(sss2))
						remcopy.add(sss1);
				}
			}
		}
		for (String s : remcopy) {
			fraw.WriteToFile(mixedgefile, s);
		}
		// for (int i = 0; i < snslen; i++) {
		// String sn1 = sns.get(i);
		// for (int j = i + 1; j < snslen; j++) {
		// String sn2 = sns.get(j);
		// if(leaf.get(sn1).contains(sou)&&leaf.get(sn2).contains(tar) ||
		// leaf.get(sn1).contains(tar)&&leaf.get(sn2).contains(sou) ){
		// fraw.WriteToFile(mixedgefile,sn1+","+sn2+","+w);
		// }
		// }
		// }
		// }
		// for (int i = 0; i < inslen; i++) {
		// String in1 = ins.get(i);
		// for (int j = i + 1; j < inslen; j++) {
		// String in2 = ins.get(j);
		// String res = getTwoindepedentNodeRelationbyMinDis(in1, in2,
		// dis,keyi);
		// if (res != null) {
		// mixg.put(in1 + "," + in2, res);
		// // System.out.println("+++++"+res);
		// fraw.WriteToFile(mixedgefile,
		// in1 + "," + in2 + "," + res.split(",")[2]);
		// // fraw.WriteToFile(absRealationfile, in1 + "," + in2 + ","
		// // + res.split(",")[2] + "," + res.split(",")[0] + ","
		// // + res.split(",")[1]);
		// }
		// }
		// for (int k = 0; k < snslen; k++) {
		// String sn1 = sns.get(k);
		// String res = getIndependentNodeAndSuperNodeRelationbyMinDis(in1,sn1,
		// leaf, dis, keyi);
		// if (res != null) {
		// mixg.put(in1 + "," + sn1, res);
		// // System.out.println("+++++"+res);
		// fraw.WriteToFile(mixedgefile,
		// in1 + "," + sn1 + "," + res.split(",")[2]);
		// // fraw.WriteToFile(absRealationfile, in1 + "," + sn1 + ","
		// // + res.split(",")[2] + "," + res.split(",")[0] + ","
		// // + res.split(",")[1]);
		// }
		// }
		// }
		return mixg;
	}

	public static void main(String[] args) {
		String basepath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";
//		String basepath = "E://ScaleFreeNetwork TestData/nbt/";
		AbstractMixedGraph amg = new AbstractMixedGraph(basepath);
		String disfr = basepath + "EdgeDisMatrix.csv";
		DisMAtrixAndIndex di = amg.ReadInDisMatrix(disfr);
		ArrayList<String> dis = di.getDis();
		HashMap<String, String> keyi = di.getKeyi();
		File file = new File(basepath + "Clusters/");
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File[] subfileList = fileList[i].listFiles();
			for (int j = 0; j < subfileList.length; j++) {
				String path = basepath + "Clusters/" + fileList[i].getName()
						+ "/" + subfileList[j].getName() + "/";		
					System.out.println("-----start mixed-----");
					amg.getMixedGraph(path + "SuperNodes.csv", path
							+ "IndependentNodes.csv", path + "MixGEdgeDis.csv",
							basepath + "Edges.csv", path
									+ "MixedGCorresponding.csv", dis, keyi,
							amg.ms1.ent, path + "abstract1.csv");
					System.out.println(fileList[i].getName() + "/"
							+ subfileList[j].getName());
			}

		}

		// for test;
		// String path = basepath + "Clusters/199/15/";
		// System.out.println("-----start mixed-----");
		// long startTime = System.currentTimeMillis();
		// amg.getMixedGraph(path + "SuperNodes.csv", path
		// + "IndependentNodes.csv", path + "MixGEdgeDis11.csv", basepath
		// + "Edges.csv", path + "MixedGCorresponding.csv", dis, keyi,
		// amg.ms1.ent, path + "abstract1.csv");
		// long endTime = System.currentTimeMillis();
		// System.out.println("running time:" + "=======" + (endTime -
		// startTime)
		// + "ms");

		// HashMap<String,String> result = amg.getMixedGraph(snsfile, insfile,
		// mixedgefile, edgedisfile, absRealationfile, prfr, amg.prmap);
		// Set<Entry<String, String>> set = result.entrySet();
		// Iterator<Entry<String, String>> it = set.iterator();
		// while (it.hasNext()) {
		// Entry<String, String> k = it.next();
		// System.out.println(k.getKey());
		// System.out.println(k.getValue());
		// }
	}

}
