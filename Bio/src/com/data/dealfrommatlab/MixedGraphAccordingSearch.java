package com.data.dealfrommatlab;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.data.mixed_graph.GenerateMixedGraph;
import com.data.mixed_graph.NetworkInnerPageRank;
import com.jiao.file.DirMaker;
import com.jiao.file.FileReadAndWrite;
import com.jiao.hierarachy.EdgeToAdjcentMatrix;

public class MixedGraphAccordingSearch {
	// 在根据输入的关键字找到相对应的层次，对应层次的cluster和边划分存储好之后，进行混合图构建
	static String filepath1 = "E://ScaleFreeNetwork TestData/Compare/100/";
//	static String SNINpath = filepath +"Cluster Sum/SNInnerPageRank/PageRank/";
	static String filepath = "E://ScaleFreeNetwork TestData/Compare/100/Clusters/";
	static String edgedisfile = "E://ScaleFreeNetwork TestData/Compare/100/EdgesDis.csv";
	static double weight = 0.8;
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static GenerateMixedGraph gm = new GenerateMixedGraph();
	static NetworkInnerPageRank npr = new NetworkInnerPageRank();
	static DirMaker mdir = new DirMaker();
	static EdgeToAdjcentMatrix etam =new EdgeToAdjcentMatrix();
	public void ComputeInnerPagerank(String index,String root) {
		// 首先计算每个社团内部的PageRank值
		File file = new File(filepath + root+"/"+index + "/SuperNodes.csv");
		if(file.exists()){
		ArrayList<String> ccens = new ArrayList<String>();
		ccens = fraw.ReadData(filepath + root+"/"+index + "/SuperNodes.csv");
		String cip = filepath + root+"/"+index + "/SNInnerPageRank/IK/";
		mdir.makeDir(new File(cip));
		String fsp = filepath + root+"/"+index + "/SNInnerPageRank/PageRank/";
		mdir.makeDir(new File(fsp));
		for (String center : ccens) {
			String cikeyfile = cip + center + "ikey.csv";
			String snodesfile = filepath + root+"/"+ index + "/SN/" + center + ".csv";
			String sedgesfile = filepath + root+"/"+ index + "/EdgesDis/" + center
					+ ".csv";
			String fsprfile = filepath + root+"/"+ index + "/SNInnerPageRank/PageRank/"
					+ center + ".csv";
			npr.GetNodePageRank(snodesfile, sedgesfile, cikeyfile, fsprfile, 1);
			}
		}
	}

	public static void main(String[] args) throws IOException {
//		 MixedGraphAccordingSearch mga = new MixedGraphAccordingSearch();
		for(int root = 199; root >100; root--){
		 for(int i =1; i<=100;i++){
			 String index = i+"";
			 
//		     mga.ComputeInnerPagerank(index,root+"");//用来计算社团内部的PageRank值
			 
//			 String SuperNodefile = filepath + root+"/"+ index+"/SuperNodes.csv";
//			 String IndependentNodefile =filepath+ root+"/"+index+ "/IndependentNodes.csv";
//			 if(new File(SuperNodefile).exists() && new File(IndependentNodefile).exists()){
//			 
//			 String SNINpath = filepath + root+"/"+index+"/SNInnerPageRank/PageRank/";
//				gm.getMixedGraph(filepath + root+"/"+ index+"/SuperNodes.csv", filepath+ root+"/"+index
//						+ "/IndependentNodes.csv", filepath
//						+ root+"/"+index+ "/MixedGEdgeDis.csv", filepath
//						+ root+"/"+ index+"/MixedGCorresponding.csv", edgedisfile, SNINpath);// 用来获取混合图
//			 }	
		
			 String frdm = filepath + root+"/"+ index+"/disMatrix.csv";
			 String fre = filepath + root+"/"+ index+"/MixedGEdgeDis.csv";
			 String frm = filepath + root+"/"+ index+"/AdjMatrix.csv";
			 if(new File(frdm).exists() )
				 etam.GenerateMatrixByDisMatrix(frdm, fre, frm);
		 }

		}
		etam.GenerateMatrixByDisMatrix(filepath1+"EdgeDisMatrix.csv", filepath1+"EdgesDis.csv", filepath1+"AdjMatrix.csv");
	}

}
