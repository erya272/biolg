package com.data.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.jiao.file.FileReadAndWrite;

//为斯坦纳树的搜索做数据准备
public class DataPreparingForSearch {
	/*
	 * 1.每个搜索项的精确图2.如有必要获取混合图，就获取混合图
	 * 3.进行斯坦纳树搜索（如果在一个cluster里就比较好办，若是在多个不同的cluster，就需要到混合图中进行）
	 */
	/*
	 * 对于边的读取是要首先把所有的边都读入，待用？ 还是对应到相应SN，然后一个一个读取边集？
	 */
	static Index index = new Index();
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String filepath = "E://ScaleFreeNetWork TestData/100/";
	static String snedgepath = filepath+"FixedR/Edgedis/";
	static String inedgepath = filepath+"FixedR/IN/";

	public HashMap<String, ArrayList<String>> getAllEdges(String allEdgepath) {
		File file = new File(allEdgepath);
		File[] tempList = file.listFiles();
		HashMap<String, ArrayList<String>> snedge = new HashMap<String, ArrayList<String>>();
		int len = tempList.length;
		for (int i = 0; i < len; i++) {
			ArrayList<String> edges = new ArrayList<String>();
			edges = fraw.ReadData(tempList[i].toString());
			String sn = tempList[i].getName();
			sn = sn.substring(0, sn.indexOf("."));
			snedge.put(sn, edges);
		}
		return snedge;
	}

	public void getclusterRelation(String key1, String key2,
			HashMap<String, ArrayList<String>> snedges,HashMap<String, ArrayList<String>> inedges) throws IOException {
		/*
		 * 要考虑的情况有： 两个节点在一个cluster（这个应该是最简单的情况） 两个节点在不同的cluster 两个节点其中一个为IN
		 * 两个节点都是IN
		 */
		//需不需要把两个边集合返回还需要后续考虑。
		String ns1 = index.Searcher(key1);
		String ns2 = index.Searcher(key2);
		String sn1 = ns1.split(",")[0];
		String tag1 = ns1.split(",")[1];
		String sn2 = ns2.split(",")[0];
		String tag2 = ns2.split(",")[1];
		ArrayList<String> edges1 = new ArrayList<String>();
		ArrayList<String> edges2 = new ArrayList<String>();
		if (tag1.equals("S")&&tag2.equals("S")) {
			// 如果两个Key在一个cluster，或是在两个不同的cluster
			edges1 = snedges.get(sn1);
			edges2 = snedges.get(sn2);
		}else if(tag1.equals("I")&&tag2.equals("S")){
			edges1 = inedges.get(sn1);
			edges2 = snedges.get(sn2);
		}else if(tag1.equals("S")&&tag2.equals("I")){
			edges1 = snedges.get(sn1);
			edges2 = inedges.get(sn2);
		}else if(tag1.equals("I")&&tag2.equals("I")){
			edges1 = inedges.get(sn1);
			edges2 = inedges.get(sn2);
		}
		for(String s :edges1)
			System.out.println(s);
		for(String s :edges2)
			System.out.println(s);
	}
	public static void main(String[] args) throws IOException {

		DataPreparingForSearch da = new DataPreparingForSearch();
		HashMap<String, ArrayList<String>> snedges = da.getAllEdges(snedgepath);
		HashMap<String, ArrayList<String>> inedges = da.getAllEdges(inedgepath);
		da.getclusterRelation("26", "17", snedges, inedges);
	}

}
