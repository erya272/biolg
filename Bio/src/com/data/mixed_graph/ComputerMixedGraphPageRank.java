package com.data.mixed_graph;

import java.util.ArrayList;

import com.jiao.file.FileReadAndWrite;
import com.jiao.mixedGraph.BasePageRank;

public class ComputerMixedGraphPageRank {
	// 在混合图内计算PageRank值
	static FileReadAndWrite fraw = new FileReadAndWrite();
	BasePageRank dpfs = new BasePageRank();
	static double d = 0.85;// 阻尼系数
	static String filepath = "E://ScaleFreeNetWork TestData/1000/";
	
	public void GetNodePageRank(String snodesfile,String inodesfile, String sedgesfile,
			String sikeyfile, String fsprfile, double initvalue) {
		ArrayList<String> snodes = new ArrayList<String>();
		ArrayList<String> sedges = new ArrayList<String>();
		snodes = fraw.ReadData(snodesfile);
		snodes.addAll(fraw.ReadData(inodesfile));
		sedges = fraw.ReadData(sedgesfile);
		int n = snodes.size();
		System.out.println(n);
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
	public void GetMixedGraphPagerank(double initvalue) {
		//计算混合图的PageRank
		String snodesfile = filepath+"FixedR/SuperNodes.csv" ;
		String inodesfile =filepath+"FixedR/IndependentNodes.csv" ;
		String sedgesfile =filepath+"FixedR/MixedGEdgeDis.csv";
		String fsprfile = filepath+"FixedR/MixedGraphPageRank.csv";
		String cikeyfile =filepath+"FixedR/MixedGraphikey.csv";
		GetNodePageRank(snodesfile,inodesfile, sedgesfile, cikeyfile, fsprfile, initvalue);
	}
	public static void main(String[] args){
		ComputerMixedGraphPageRank cmgp = new ComputerMixedGraphPageRank();
		cmgp.GetMixedGraphPagerank(1);
		
	}
}
