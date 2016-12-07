package com.jiao.hierarachy;

import java.util.ArrayList;

import com.jiao.file.FileReadAndWrite;

public class DeleteNodesFormNbt {
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static IndexForHierarchyTree inf = new IndexForHierarchyTree();
	static String fpath = "/home/lee/biolg/ScaleFreeNetwork TestData/";
	public void GenerateGeneEdgesAccordingNexoTree(String soufr, String tarfr,String nw,String er){
		ArrayList<String> read  = fraw.ReadData(soufr);
		ArrayList<String> oread = fraw.ReadData(tarfr);
		ArrayList<String> nodes = new ArrayList<String>();
		for (String s : read) {
			String[] ss = s.split(",");
			if (!inf.isNumberic(ss[1])) {
				nodes.add(ss[1]);
				fraw.WriteToFile(nw,ss[1]);
			}
		}
		for(String s: oread){
			String[] ss = s.split(",");
			String sou = ss[0];
			String tar = ss[1];
			if(nodes.contains(sou)&&nodes.contains(tar)){
				fraw.WriteToFile(er, s);
			}
		}
	}
	public static void main(String[] args){
		DeleteNodesFormNbt dnf = new DeleteNodesFormNbt();
		dnf.GenerateGeneEdgesAccordingNexoTree(fpath+"nnbt/Nexo/tree.csv", fpath+"nnbt/Edges.csv",
				fpath+"snbt/Nodes.csv",fpath+"snbt/Edges.csv"	);
		
	}

}
