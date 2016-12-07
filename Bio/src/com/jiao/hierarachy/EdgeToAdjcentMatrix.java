package com.jiao.hierarachy;

//import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.Random;
import java.util.Set;

import com.jiao.file.FileReadAndWrite;

public class EdgeToAdjcentMatrix {
	//把边之间的关系直接转换成邻接矩阵
	static FileReadAndWrite fraw = new FileReadAndWrite();

	public void GenerateMatrixDirected(String fre, String frm,String jkeyfr,String keyjfr)
			throws IOException {
		FileWriter fw = new FileWriter(frm);
		ArrayList<String> nodes = new ArrayList<String>();
		ArrayList<String> edges = new ArrayList<String>();
		edges = fraw.ReadData(fre);
		for(String s:edges){
			String [] ss = s.split(",");
			String s1 = ss[0];
			String s2 = ss[1];
			if(!nodes.contains(s1))
				nodes.add(s1);
			if(!nodes.contains(s2))
				nodes.add(s2);	
		}
		HashMap<String, Integer> keyj = new HashMap<String, Integer>();
		HashMap<Integer, String> jkey = new HashMap<Integer, String>();
		int count = 0;
		int n = nodes.size();
		String node1 = nodes.get(0);
		String index = node1;
		keyj.put(node1, 0);
		jkey.put(0, node1);
		fraw.WriteToFile(keyjfr,node1+","+0);
		fraw.WriteToFile(jkeyfr,0+","+node1);
		for (String node : nodes) {
			if (count != 0) {
				index = index + " " + node;
				keyj.put(node, count);
				fraw.WriteToFile(keyjfr,node+","+count);
				jkey.put(count, node);
				fraw.WriteToFile(jkeyfr,count+","+node);
			}
			count++;
		}
		System.out.println(keyj.size());
		double[][] adj = new double[n][n];
		for (String s : edges) {
			String s1 = s.split(",")[0];
			String s2 = s.split(",")[1];
			String s3 = s.split(",")[2];
			int i = keyj.get(s1);
			int j = keyj.get(s2);
			adj[i][j] = Double.valueOf(s3);
		}
		for (int i = 0; i < n; i++) {
			String s = null;
			s = adj[i][0]+"";
			for (int j = 1; j < n; j++) {
				s = s+"\t" + adj[i][j];
			}
			fw.write(s);
			fw.write("\r\n");
			fw.flush();
		}
		fw.close();
	}
	public void GenerateMatrixByDisMatrix(String frdm, String fre, String frm)
			throws IOException {
		//为了方便后面的Dreyfus中的不直接连接点之间的最短路径。
		FileWriter fw = new FileWriter(frm,true);
		ArrayList<String> edges = new ArrayList<String>();
		String[] nodes = fraw.ReadData(frdm).get(0).split("\t");
		edges = fraw.ReadData(fre);
		HashMap<String, Integer> keyj = new HashMap<String, Integer>();
		HashMap<Integer, String> jkey = new HashMap<Integer, String>();
		int count = 0;
		int n = nodes.length;
		String node1 = nodes[0];
		String index = node1;
		keyj.put(node1, 0);
		jkey.put(0, node1);
		for (count = 1;  count < n;count++) {
				index = index + " " + nodes[count];
				keyj.put(nodes[count], count);
				jkey.put(count, nodes[count]);
//				System.out.println(nodes[count]);
		}
		Set<Integer> set = jkey.keySet();
		Iterator<Integer> it = set.iterator();
		while (it.hasNext()) {
			int par = it.next();
			System.out.println(par+jkey.get(par));
		}
	
		fw.write(index);
		fw.write("\r\n");
		fw.flush();
		double[][] adj = new double[n][n];
		for (String s : edges) {
			System.out.println(s);
			String ss[] = s.split(",");
			String s1 = ss[0];
			String s2 = ss[1];
			String s3 = ss[2];
			int i =0;
			if(s1!="王菲"&&!s1.equals("王菲"))
				i = keyj.get(s1);
			int j = keyj.get(s2);
			adj[i][j] = Double.valueOf(s3);
			adj[j][i] = Double.valueOf(s3);
		}
		for (int i = 0; i < n; i++) {
			String s = null;
			s = jkey.get(i) + "";
			for (int j = 0; j < n; j++) {
				s = s + " " + adj[i][j];
			}
			fw.write(s);
			fw.write("\r\n");
			fw.flush();
		}
		fw.close();
	}
	public static void main(String[] args) throws IOException{
		EdgeToAdjcentMatrix etam =new EdgeToAdjcentMatrix();
		
		String frn = "E://ScaleFreeNetwork TestData/experiment/test/13EdgeDisMatrix.csv";
		String fre = "E://ScaleFreeNetwork TestData/experiment/test/12Edges.csv";
		String frm = "E://ScaleFreeNetwork TestData/experiment/test/12AdjcentMatrix.csv";
		etam.GenerateMatrixByDisMatrix(frn, fre, frm);
		
//		directedmatrix
//		String path = "E://ScaleFreeNetwork TestData/nnbt/Nexo/";
//		etam.GenerateMatrixDirected(path+"tree.csv", path+"AdjcentMatrix.csv",path+"jkey.csv",path+"keyj.csv");
		
//		String basepath ="E://ScaleFreeNetwork TestData/snbt/Clusters/";
//		File file = new File(basepath );
//		File[] fileList = file.listFiles();
//		for (int i = 0; i < fileList.length; i++) {
//				File[] subfileList = fileList[i].listFiles();
//				for (int j = 0; j < subfileList.length; j++) {
//					String path = basepath +fileList[i].getName() + "/"
//							+ subfileList[j].getName() + "/";
//					if(new File(path+"disMatrix.csv").exists()){
//						etam.GenerateMatrixByDisMatrix(path+"disMatrix.csv", path+"MixGEdgeDis.csv", path+"AdjcentMatrix.csv");	
//						System.out.println(fileList[i].getName()+"/"+subfileList[j].getName());
//					}
//				}		
//		}
//		
	}

}
