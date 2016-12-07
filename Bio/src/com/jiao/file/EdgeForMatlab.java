package com.jiao.file;

import java.util.ArrayList;
// in order to get adjacent matrix in Matlab 
public class EdgeForMatlab {

	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String path ="/home/lee/biolg/ScaleFreeNetwork TestData/spectral clustering/";
	public void EdgesCovert(String fr, String  fw){
		ArrayList<String> edges = fraw.ReadData(fr);
		for(String s:edges){
			fraw.WriteToFile(fw, s);
			fraw.WriteToFile(fw, s.split(",")[1]+","+s.split(",")[0]+","+s.split(",")[2]);
		}	
	}
	public static void main(String[] args){
		EdgeForMatlab efl = new  EdgeForMatlab();
		efl.EdgesCovert(path+"10000Edges.csv", path+"10000EdgesMatlab.csv");
	}
}
