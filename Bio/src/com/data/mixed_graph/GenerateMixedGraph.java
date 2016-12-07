package com.data.mixed_graph;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.jiao.file.FileReadAndWrite;

// 通过比较节点的pagerank值和边的权重来获取最终的混合图。
public class GenerateMixedGraph {
	/*
	 * 其中要把IN和SN两类分开考虑处理整合成混合图 在接下来的混合图中要把两点之间的边的权值考虑成边的距离，即1-similarity
	 */
	static String filepath = "E://ScaleFreeNetWork TestData/1000/";
	static String SNINpath = filepath+"FixedR/SNInnerPageRank/PageRank/";
	static String edgedisfile = filepath+"1000EdgesDis.csv";
	static double weight = 1.0;
	
	//weight = 1.0在这里是只考虑PageRank值最大的点作为边的代表
	static FileReadAndWrite fraw = new FileReadAndWrite();

	
	public HashMap<String, ArrayList<String>> CenterNormalNodesMap(String path) {
		// 获取所有以center为SuperNode的每一个SuperNode点集合,还有每个SuperNode内部的PageRank值
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

	public String getTwoSuperNodeRelation(String sn1, String sn2,String edgedisfile,String SNINpath) {
		ArrayList<String> edgedis = new ArrayList<String>();
		HashMap<String, ArrayList<String>> cn = new HashMap<String, ArrayList<String>>();
		ArrayList<String> nn1 = new ArrayList<String>();
		ArrayList<String> nn2 = new ArrayList<String>();
		// // SN中的其他normal节点
		ArrayList<String> pr1 = new ArrayList<String>();
		ArrayList<String> pr2 = new ArrayList<String>();
		// SN内部的PageRank值
		double max = 0;
		// 代表边权和点权最高的值
		String resedge = null;
		// 最后的抽象边
		edgedis = fraw.ReadData(edgedisfile);
		cn = CenterNormalNodesMap(SNINpath);
		for (String s : cn.get(sn1)) {
			String[] ss = s.split(",");
			nn1.add(ss[0]);
			pr1.add(ss[1]);
		}
		for (String s : cn.get(sn2)) {
			String[] ss = s.split(",");
			nn2.add(ss[0]);
			pr2.add(ss[1]);
		}
		for (String s : edgedis) {
			String ss[] = s.split(",");
			String sou = ss[0];
			String tar = ss[1];
			double sim = 1 - Double.valueOf(ss[2]);
			// 1-两点边距，两点的相似度。如何来考虑边的权和点权的分配来剪枝，在这里设置了一个权值对边与点的重要性进行限制
			if (nn1.contains(sou) && nn2.contains(tar)) {
				int ind1 = nn1.indexOf(sou);
				int ind2 = nn2.indexOf(tar);
				double np1 = Double.valueOf(pr1.get(ind1));
				double np2 = Double.valueOf(pr2.get(ind2));
				double temp = sim * weight + np1 + np2;
				if (max < temp) {
					max = temp;
					resedge = s;
				}
				if (max == temp) {
					resedge = Double.valueOf(resedge.split(",")[2]) > Double
							.valueOf(ss[2]) ? resedge : s;
					// 如果边权和点权结合后一样大，暂且认为点权大的作为更重要的结果。
				}

			}
			if (nn2.contains(sou) && nn1.contains(tar)) {
				int ind2 = nn2.indexOf(sou);
				int ind1 = nn1.indexOf(tar);
				double np1 = Double.valueOf(pr1.get(ind1));
				double np2 = Double.valueOf(pr2.get(ind2));
				double temp = sim * weight + np1 + np2;
				if (max < temp) {
					max = temp;
					resedge = s;
				}
				if (max == temp) {
					resedge = Double.valueOf(resedge.split(",")[2]) > Double
							.valueOf(ss[2]) ? resedge : s;
				}
			}
		}
		return resedge;
		// 返回的是边距
	}

	public String getIndependentNodeAndSuperNodeRelation(String in1, String sn1,String edgedisfile,String SNINpath) {
		ArrayList<String> edgedis = new ArrayList<String>();
		HashMap<String, ArrayList<String>> cn = new HashMap<String, ArrayList<String>>();
		ArrayList<String> nn1 = new ArrayList<String>();
		ArrayList<String> pr1 = new ArrayList<String>();
		double max = 0;
		// 代表边权和点权最高的值
		String resedge = null;
		// 最后的抽象边
		edgedis = fraw.ReadData(edgedisfile);
		cn = CenterNormalNodesMap(SNINpath);
		for (String s : cn.get(sn1)) {
			String[] ss = s.split(",");
			nn1.add(ss[0]);
			pr1.add(ss[1]);
		}
		for (String s : edgedis) {
			String ss[] = s.split(",");
			String sou = ss[0];
			String tar = ss[1];
			double sim = 1 - Double.valueOf(ss[2]);
			// 1-两点边距，两点的相似度。如何来考虑边的权和点权的分配来剪枝，在这里设置了一个权值对边与点的重要性进行限制
			if (nn1.contains(sou) && in1.equals(tar)) {
				int ind1 = nn1.indexOf(sou);
				double np1 = Double.valueOf(pr1.get(ind1));
				double temp = sim * weight + np1;
				if (max < temp) {
					max = temp;
					resedge = s;
				}
				if (max == temp) {
					resedge = Double.valueOf(resedge.split(",")[2]) > Double
							.valueOf(ss[2]) ? resedge : s;
				}
			}
			if (nn1.contains(tar) && in1.equals(sou)) {
				int ind1 = nn1.indexOf(tar);
				double np1 = Double.valueOf(pr1.get(ind1));
				double temp = sim * weight + np1;
				if (max < temp) {
					max = temp;
					resedge = s;
				}
				if (max == temp) {
					resedge = Double.valueOf(resedge.split(",")[2]) > Double
							.valueOf(ss[2]) ? resedge : s;
				}
			}

		}
		return resedge;
	}

	public String getTwoindepedentNodeRelation(String in1, String in2,String edgedisfile) {
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

	public HashMap<String, String> getMixedGraph(String snsfile,
			String insfile, String mixedgefile, String absRealationfile,String edgedisfile,String SNINpath) {
		ArrayList<String> sns = new ArrayList<String>();
		ArrayList<String> ins = new ArrayList<String>();
		sns = fraw.ReadData(snsfile);
		ins = fraw.ReadData(insfile);
		int snslen = sns.size();
		int inslen = ins.size();
		HashMap<String, String> mixg = new HashMap<String, String>();
		for (int i = 0; i < snslen; i++) {
			String sn1 = sns.get(i);
			for (int j = i + 1; j < snslen; j++) {
				String sn2 = sns.get(j);
				String res = getTwoSuperNodeRelation(sn1, sn2,edgedisfile,SNINpath);
				if (res != null) {
					mixg.put(sn1 + "," + sn2, res);
					fraw.WriteToFile(mixedgefile,
							sn1 + "," + sn2 + "," + res.split(",")[2]);
					fraw.WriteToFile(absRealationfile, sn1 + "," + sn2 + ","
							+ res.split(",")[2] + "," + res.split(",")[0] + ","
							+ res.split(",")[1]);
				}
			}
		}
		for (int i = 0; i < inslen; i++) {
			String in1 = ins.get(i);
			for (int j = i + 1; j < inslen; j++) {
				String in2 = ins.get(j);
				String res = getTwoindepedentNodeRelation(in1, in2,edgedisfile);
				if (res != null) {
					mixg.put(in1 + "," + in2, res);
					fraw.WriteToFile(mixedgefile,
							in1 + "," + in2 + "," + res.split(",")[2]);
					fraw.WriteToFile(absRealationfile, in1 + "," + in2 + ","
							+ res.split(",")[2] + "," + res.split(",")[0] + ","
							+ res.split(",")[1]);
				}
			}
			for (int k = 0; k < snslen; k++) {
				String sn1 = sns.get(k);
				String res = getIndependentNodeAndSuperNodeRelation(in1, sn1,edgedisfile,SNINpath);
				if (res != null) {
					mixg.put(in1 + "," + sn1, res);
					fraw.WriteToFile(mixedgefile,
							in1 + "," + sn1 + "," + res.split(",")[2]);
					fraw.WriteToFile(absRealationfile, in1 + "," + sn1 + ","
							+ res.split(",")[2] + "," + res.split(",")[0] + ","
							+ res.split(",")[1]);
				}
			}
		}
		return mixg;
	}

	public static void main(String[] args) {
		GenerateMixedGraph gm = new GenerateMixedGraph();
		// gm.getTwoSuperNodeRelation("90021", "89949", edgedisfile);
		// gm.getIndependentNodeAndSuperNodeRelation("109771", "90021");
		HashMap<String, String> result = gm.getMixedGraph(
				filepath+"FixedR/SuperNodes.csv",
				filepath+"FixedR/IndependentNodes.csv",
				filepath+"FixedR/MixedGEdgeDis.csv",
				filepath+"FixedR/MixedGCorresponding.csv",edgedisfile,SNINpath);
		Set<Entry<String, String>> set = result.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, String> k = it.next();
			System.out.println(k.getKey());
			System.out.println(k.getValue());
		}

	}

}
