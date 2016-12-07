package com.jiao.mixedGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jiao.element.AdjmatrixforPR;
import com.jiao.file.FileReadAndWrite;

//实现网络中各点权重
public class BasePageRank {
	public static double MAX = 0.0000001;// 阈值
	static double d = 0.85;// 阻尼系数
	static FileReadAndWrite fraw = new FileReadAndWrite();

	// 获取初始邻接矩阵
	public double[][] adjacencymatrix(ArrayList<String> nodes,
			ArrayList<String> edges, String fileik) {
		Map<Integer, String> ikey = new HashMap<Integer, String>();
		Map<String, Integer> keyi = new HashMap<String, Integer>();
		int len = nodes.size();
		double[][] m = new double[len][len];
		int i = 0;
		for (String s : nodes) {
			ikey.put(i, s);
			fraw.WriteToFile(fileik, i + ":" + s);
			keyi.put(s, i);
			i++;
		}
		for (String s : edges) {
			String sou = s.split(",")[0];
			String tar = s.split(",")[1];
			// 判断边的节点在不在这个节点集合里，为了后面的社团剪枝准备
			if (nodes.contains(sou) && nodes.contains(tar)) {
				int k = keyi.get(sou);
				int l = keyi.get(tar);
				m[k][l] = 1;
				m[l][k] = 1;
			}
		}
		for (int j = 0; j < len; j++) {
			int sum = 0;
			for (int t = 0; t < len; t++) {
				if (m[t][j] == 1) {
					sum++;
				}
			}
			for (int t = 0; t < len; t++) {
				if (m[t][j] == 1) {
					m[t][j] = (double) 1 / (double) sum;
				}
			}
		}
		return m;
	}
	public AdjmatrixforPR adjacencymatrix(ArrayList<String> nodes,
			ArrayList<String> edges) {
		AdjmatrixforPR apr = new AdjmatrixforPR();
		Map<Integer, String> ikey = new HashMap<Integer, String>();
		Map<String, Integer> keyi = new HashMap<String, Integer>();
		int len = nodes.size();
		double[][] m = new double[len][len];
		int i = 0;
		for (String s : nodes) {
			ikey.put(i, s);
			keyi.put(s, i);
			i++;
		}
		for (String s : edges) {
			String [] ss = s.split(",");
			String sou = ss[0];
			String tar = ss[1];
			// 判断边的节点在不在这个节点集合里，为了后面的社团剪枝准备
			if (nodes.contains(sou) && nodes.contains(tar)) {
				int k = keyi.get(sou);
				int l = keyi.get(tar);
				m[k][l] = 1;
				m[l][k] = 1;
			}
		}
		for (int j = 0; j < len; j++) {
			int sum = 0;
			for (int t = 0; t < len; t++) {
				if (m[t][j] == 1) {
					sum++;
				}
			}
			for (int t = 0; t < len; t++) {
				if (m[t][j] == 1) {
					m[t][j] = (double) 1 / (double) sum;
				}
			}
		}
		apr.setIkey(ikey);
		apr.setM(m);
		return apr;
	}
	// 获取单位矩阵
	public double[][] getU(int len) {
		double[][] u = new double[len][len];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				u[i][j] = 1;
			}
		}
		return u;
	}

	// 计算两个向量之间的距离
	public double calDistance(double[] q1, double[] q2) {
		double sum = 0;
		if (q1.length != q2.length) {
			return -1;
		}

		for (int i = 0; i < q1.length; i++) {
			sum += Math.pow(q1[i] - q2[i], 2);
		}
		return Math.sqrt(sum);
	}

	// 计算一个矩阵乘以一个向量
	public double[] vectorMulMatrix(double[][] m, double[] v) {
		if (m == null || v == null || m.length <= 0 || m[0].length != v.length) {
			return null;
		}
		double[] mm = new double[m[0].length];
		double su = 0;
		for (int i = 0; i < m.length; i++) {
			double sum = 0;
			for (int j = 0; j < m[0].length; j++) {
				double temp = m[i][j] * v[j];
				sum += temp;
			}
			mm[i] = sum;
			su += sum;
		}
		for (int i = 0; i < m.length; i++) {
			mm[i] = (double) mm[i] / (double) su;
		}
		return mm;
	}

	// 计算一个数乘以矩阵
	public double[][] numberMulMatrix(double[][] s, double a) {
		double[][] m = new double[s.length][s.length];
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s.length; j++) {
				m[i][j] = a * s[i][j];
			}
		}
		return m;
	}

	// 计算两个矩阵之和
	public double[][] addMatrix(double[][] m1, double[][] m2) {
		if (m1.length != m2.length || m1.length <= 0 || m2.length <= 0)
			return null;
		double[][] m = new double[m1.length][m1.length];
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m1.length; j++) {
				double temp = m1[i][j] + m2[i][j];
				m[i][j] = temp;
			}
		}
		return m;
	}

	// 计算获取初始的矩阵
	public double[][] getG(double[][] m, double[][] u, double a) {
		int n = m.length;
		double[][] s = numberMulMatrix(m, a);
		double[][] uu = numberMulMatrix(u, (1 - a) / n);
		double[][] g = addMatrix(s, uu);
		return g;
	}

	// 计算PageRank
	public double[] calPageRank(double[][] m, double[][] u, double[] q1,
			double a) {
		double[][] g = new double[m.length][];
		g = getG(m, u, a);
		double[] q = new double[q1.length];
		int iterator = 1;
		while (true) {
			q = vectorMulMatrix(g, q1);
			double dis = calDistance(q, q1);
			if (dis <= MAX) {
				break;
			}
			q1 = q;
			iterator++;
		}
		System.out.println(iterator);
		return q;
	}

	public static void main(String[] args) {
		BasePageRank dpfs = new BasePageRank();
		ArrayList<String> edges = fraw
				.ReadData("D://For Cluster/Testdata6/Testdata6Edge.csv");
		ArrayList<String> nodes = fraw
				.ReadData("D://For Cluster/Testdata6/Testdata6Node.csv");
		// ArrayList<String> nodes =new ArrayList<String>();
		// ArrayList<String> edges =new ArrayList<String>();
		// nodes.add("1");
		// nodes.add("2");
		// nodes.add("3");
		// nodes.add("4");
		// edges.add("1,2");
		// edges.add("1,3");
		// edges.add("1,4");
		// edges.add("2,3");
		// edges.add("2,4");
		// edges.add("3,4");
		// edges.add("4,2");
		int n = nodes.size();
		double[] q1 = new double[n];
		for (int i = 0; i < n; i++) {
			q1[i] = 1;
		}
		double[][] m = dpfs.adjacencymatrix(nodes, edges,
				"D://For Cluster/Testdata6/Testdata6ikey.csv");
		double[][] u = dpfs.getU(n);
		double[] q = dpfs.calPageRank(m, u, q1, d);
		ArrayList<String> ikey = fraw
				.ReadData("D://For Cluster/Testdata6/Testdata6ikey.csv");
		for (int j = 0; j < n; j++) {
			String key = ikey.get(j).split(":")[1];
			fraw.WriteToFile(
					"D://For Cluster/Testdata6/Testdata6NodePageRank.csv", key
							+ "," + q[j]);
		}
	}
}
