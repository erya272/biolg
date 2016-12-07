package com.data.mixed_graph;

import java.util.ArrayList;

import com.jiao.file.FileReadAndWrite;

//在做完固定半径之后，做混合图之前，我们先处理一下所有的独立点()，获取由独立点组合成的网络，再在其中做Clustering等等
public class GetNetworkCosistOfIN {
	
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String filepath = "E://ScaleFreeNetWork TestData/100/";
	
	public void NetworkOfIndpedentNodes(String INfile, String AllEdgefile,String INEdgefile){
		
		ArrayList<String> nodes = fraw.ReadData(INfile);
		ArrayList<String> edges	= fraw.ReadData(AllEdgefile);
		for(String s :edges){
			String source = s.split(",")[0];
			String target = s.split(",")[1];
			if(nodes.contains(source)&&nodes.contains(target))
				fraw.WriteToFile(INEdgefile, s);
		}
	}
	
	public static void main(String[] args){
		GetNetworkCosistOfIN ge = new GetNetworkCosistOfIN();
		ge.NetworkOfIndpedentNodes(filepath+"FixedR/IndependentNodes.csv", filepath+"100Edges.csv", filepath+"FixedR/IN/INEdge.csv");
	}
}
