package com.jiao.generatortestdata;

import java.util.ArrayList;

import com.jiao.file.FileReadAndWrite;

public class GeneratorTestData {
	
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String filepath = "E://ScaleFreeNetwork TestData/no weight/";
	public  void TestDataWriten( int n ){
		
		ScaleFreeNetwork sf =  new ScaleFreeNetwork();
		ArrayList<Edgeinfo> edgeList = new ArrayList<Edgeinfo>();
		edgeList = sf.SFNetworkGenerator(n);
		for(int i = 0; i < n; i++){
			String s  = "" +(i+1);
			fraw.WriteToFile(filepath+n+"/nodes.csv",s);	
		}
		for(Edgeinfo e : edgeList){
			String s = (e.getSource()+1)+","+(e.getTarget()+1)+","+e.getWeight();
			fraw.WriteToFile(filepath+n+"/Edges.csv",s);	
		}
	
		
	}
	public static void main(String[] args){
		GeneratorTestData gtd = new GeneratorTestData();
		gtd.TestDataWriten(100);
		
	}
}
