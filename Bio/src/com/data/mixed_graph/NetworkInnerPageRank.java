package com.data.mixed_graph;

import java.util.ArrayList;

import com.jiao.file.FileReadAndWrite;
import com.jiao.mixedGraph.BasePageRank;

public class NetworkInnerPageRank {
	// 在每个抽象的SN中运行PageRank，对SN的PageRank值进行分配
	static FileReadAndWrite fraw = new FileReadAndWrite();
	BasePageRank dpfs = new BasePageRank();
	static double d = 0.85;// 阻尼系数
	static String filepath = "E://ScaleFreeNetWork TestData/1000/";

	public void GetNodePageRank(String snodesfile, String sedgesfile,
			String sikeyfile, String fsprfile, double initvalue) {
		ArrayList<String> snodes = new ArrayList<String>();
		ArrayList<String> sedges = new ArrayList<String>();
		snodes = fraw.ReadData(snodesfile);
		sedges = fraw.ReadData(sedgesfile);
		int n = snodes.size();
		double[] q1 = new double[n];
		for (int i = 0; i < n; i++) {
			q1[i] = initvalue;
		}
		double[][] m = dpfs.adjacencymatrix(snodes, sedges, sikeyfile);
		double[][] u = dpfs.getU(n);
		double[] q = dpfs.calPageRank(m, u, q1, d);
		ArrayList<String> ikey = fraw.ReadData(sikeyfile);
		for (int j = 0; j < n; j++) {
			String key = ikey.get(j).split(":")[1];
			double v1 = initvalue * q[j];
			fraw.WriteToFile(fsprfile, key + "," + v1);
		}
	}

	public void GetClusterInnerNodePageRank(String center, double initvalue) {
		//计算出每一个社团内部PageRank
		String sikeyfile = filepath+"Cluster sum/ikey.csv";
		ArrayList<String> ikey = fraw.ReadData(sikeyfile);
		String i = null;
		for (String s : ikey) {
			if (s.split(":")[1].equals(center)) {
				i = s.split(":")[0];
			}
		}		
		String cikeyfile = filepath+"Cluster Sum/SNInnerPageRank/IK/"
				+ center + "ikey.csv";
		String snodesfile = filepath+"Cluster Sum/SN/" + center
				+ ".csv";
		String sedgesfile =filepath+ "Cluster sum/EdgeDis/"
				+ i + "EdgesDis.csv";
		String fsprfile = filepath+"Cluster Sum/SNInnerPageRank/PageRank/"
				+ center + ".csv";
		
		
		GetNodePageRank(snodesfile, sedgesfile, cikeyfile, fsprfile, initvalue);
	}

	public static void main(String[] args) {
		NetworkInnerPageRank npr = new NetworkInnerPageRank();
		ArrayList<String> ccens = new ArrayList<String>();
		ccens = fraw
				.ReadData(filepath+"Cluster sum/SuperNode.csv");
		for (String center : ccens){
			//计算每一个聚类内部的PageRank值；
			npr.GetClusterInnerNodePageRank(center, 1);
		}
	}

}
