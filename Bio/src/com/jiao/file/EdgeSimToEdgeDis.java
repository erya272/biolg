package com.jiao.file;

import java.util.ArrayList;

public class EdgeSimToEdgeDis {
	static FileReadAndWrite fraw = new FileReadAndWrite();
	public void EdgeSimilarityToEdgeDis(String edgefr,String edgedisfr){
		ArrayList<String> edges = new ArrayList<String>();
		edges = fraw.ReadData(edgefr);
		for(String s: edges){
			String sou = s.split(",")[0];
			String tar = s.split(",")[1];
			double dis = 1.0 - Double.valueOf(s.split(",")[2]);
			fraw.WriteToFile(
					edgedisfr, sou + "," + tar
							+ "," + dis);
		}
	}
	public static void main(String[] args) {
		
		EdgeSimToEdgeDis ete = new EdgeSimToEdgeDis();
		ete.EdgeSimilarityToEdgeDis("/home/lee/biolg/ScaleFreeNetwork TestData/experiment/20/Edges.csv", "/home/lee/biolg/ScaleFreeNetwork TestData/experiment/20/EdgeDis.csv");
	}

}
