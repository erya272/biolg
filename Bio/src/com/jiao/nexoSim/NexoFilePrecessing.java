package com.jiao.nexoSim;

import java.util.ArrayList;
import java.util.HashMap;

import com.jiao.file.FileReadAndWrite;
import com.jiao.hierarachy.IndexForHierarchyTree;

public class NexoFilePrecessing {
	//Nexo 数据的预处理
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String fpath = "/home/lee/biolg/ScaleFreeNetwork TestData/nnbt/";
	static IndexForHierarchyTree inf = new IndexForHierarchyTree();

	public void IndexSGDToORF(String soufr,String treefr,String treefw) {
		HashMap<String, String> index = new HashMap<String, String>();
		ArrayList<String> read = new ArrayList<String>();
		ArrayList<String> read2= new ArrayList<String>();
		read = fraw.ReadData(soufr);
		read2 = fraw.ReadData(treefr);
//		int  j=0;
		for (String s : read) {
				String[] ss = s.split(",");
				String s1 = ss[0];
				String s2 = ss[1];
				String s3 = ss[3];
				String s4 = ss[4];
//				if (j<10){
//					System.out.println(j+" "+s);
//					System.out.println(s1+" "+s2+" "+s3+" "+s4);				
//				}
//				j+=1;
				if (!index.containsKey(s1)) {
					index.put(s1, s2);
				}
				if (!index.containsKey(s3)) {
					index.put(s3, s4);
				}
		}
		for(String s:read2){
			String[] ss = s.split("\t");
//			System.out.println(ss[0]+" "+ss[2]);
			if(index.containsKey(ss[0])){
//				System.out.println(ss[0]);
				ss[0] = index.get(ss[0]);				
			}				
			if(index.containsKey(ss[2]))
				ss[2] = index.get(ss[2]);
			String sss = ss[0]+","+ss[2]+","+1;
			fraw.WriteToFile(treefw, sss);	
		}
	}
	
	public ArrayList<String> test(String soufr) {
		ArrayList<String> read = new ArrayList<String>();
		read = fraw.ReadData(soufr);
		ArrayList<String> res = new ArrayList<String>();
		for (String s : read) {
			String[] ss = s.split("\t");
			if (!inf.isNumberic(ss[2])) {
				res.add(ss[2]);
			}
		}
		System.out.println(res.size());
		return res;
	}
	
	public static void main(String[] args) {
		NexoFilePrecessing nfp = new NexoFilePrecessing();
//		nfp.test(fpath + "/Nexo/Nexo.sif");
		nfp.IndexSGDToORF(fpath + "snbt.csv",fpath + "/Nexo/Nexo.sif",fpath + "/Nexo/tree.csv");
		System.out.println("over!");

	}

}
