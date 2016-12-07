package com.data.dealfrommatlab;

import java.io.File;
import java.util.ArrayList;

import com.jiao.file.FileReadAndWrite;
//用来第一步处理MATLAB数据，谱聚类的时候用到的。
public class PartionCluster {
	//根据节点的聚类情况把节点进行划分到各自的cluster中
	//根据节点的划分情况来划分cluster的内部边
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String path ="E://ScaleFreeNetwork TestData/spectral clustering/";
	public void NodePartion(String f1, String f2){
		ArrayList<String> clusters = fraw.ReadData(path+f1);
		int len  = clusters.size();
		for(int i = 0 ; i < len ; i++){
			String cname = clusters.get(i);
			fraw.WriteToFile(path+f2+cname+".csv", i+1+"");	
		}
	}
	public void ReadFromEdgeCsvPath(String nodesPath, String fr1) {
		// 从文件夹批量读入处理
		File file = new File(path+nodesPath);
		File[] tempList = file.listFiles();
		ArrayList<String> edges = new ArrayList<String>();
		edges = fraw.ReadData(fr1);
		for (int i = 0; i < tempList.length; i++) { 
			ArrayList<String> cnodes = new ArrayList<String>();
			cnodes = fraw.ReadData(tempList[i].toString());
			System.out.println(cnodes.size());
			for (String s : edges) {
				String sou = s.split(",")[0];
				String tar = s.split(",")[1];
				String dis = s.split(",")[2];
				if (cnodes.contains(sou) && cnodes.contains(tar)) {
					fraw.WriteToFile(
							path+"100/EdgeDis/"
									+ (i+1)+ "EdgesDis" + ".csv", sou + "," + tar
									+ "," + dis);
				}
			}
		}
	}
	public static void main(String[] args){
		PartionCluster pc = new PartionCluster();
//		pc.NodePartion("partionCluster.csv", "100/Nodes/");
		pc.ReadFromEdgeCsvPath("100/Nodes/", path+"100EdgesDis.csv");
	}
}
