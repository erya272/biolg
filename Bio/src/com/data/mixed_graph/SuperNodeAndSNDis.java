package com.data.mixed_graph;
//暂时换另一个思路，要考虑SN和IN的独立图。（现在这些想法要放在FixedRadius之后呢，所以这个类只做SN 抽象和SN之间距离计算12——25）
import java.util.ArrayList;
import java.util.HashMap;
//对于形成的聚类中心，针对该社团抽象成SuperNode
//对于形成的SN ，我们要如何建立这些SN之间的联系。如何存储这些联系呢？
//即如何还原社团之间的关联关系？
//针对两个superNode里的点之间的关系利用加权平均数抽象成两个集合之间的联系。

import java.util.Map;

import com.jiao.file.FileReadAndWrite;


public class SuperNodeAndSNDis {
	//Super Node 的节点的抽象和边的抽象(边的抽象是基于所有边的加权平均)
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String filepath = "E://ScaleFreeNetWork TestData/1000/";
//	public HashMap<String, ArrayList<String>> CenterNormalNodesMap(String path,
//			String fr) {
//		//获取所有以center为SuperNode的每一个SuperNode点集合。
//		File file = new File(path);
//		File[] tempList = file.listFiles();
//		int n = tempList.length;
//		HashMap<String, ArrayList<String>> cn = new HashMap<String, ArrayList<String>>();
//		ArrayList<String> center = new ArrayList<String>();
//		center = fraw.ReadData(fr);
//		for (int i = 0; i < n; i++) {
//			ArrayList<String> nodes = new ArrayList<String>();
//			nodes = fraw.ReadData(tempList[i].toString());
//			String c = center.get(i).split(",")[0];
//			cn.put(c, nodes);
//		}
//		return cn;
//	}

//	public void TwoNodeEdgeAbstract(String fr1, String key1, String key2) {
//		//获取任意两个SuperNode 之间的关系.这个是通过算社团之间的所有边进行加权平均的。这是最初的想法，现在我们想通过权值，保留边距的最小的两个点。
//		ArrayList<String> edges = new ArrayList<String>();
//		edges = fraw.ReadData(fr1);
//		HashMap<String, ArrayList<String>> cn = new HashMap<String, ArrayList<String>>();
//		cn = CenterNormalNodesMap(
//				"D://For Cluster/Testdata6/Cluster sum/Nodes/",
//				"D://For Cluster/Testdata6/Cluster sum/ClusterSummary.csv");
//		int count = 0;
//		double sum = 0;
//		for (String s : edges) {
//			String sou = s.split(",")[0];
//			String tar = s.split(",")[1];
//			double dis = 1.0 - Double.valueOf(s.split(",")[2]);
//			if (cn.get(key1).contains(sou) && cn.get(key2).contains(tar)
//					|| cn.get(key2).contains(sou) && cn.get(key1).contains(tar)) {
//				count += 1;
//				sum += dis;
//			}
//		}
//		if (count != 0) {
//			double edis = sum / count;
//			fraw.WriteToFile(
//					"D://For Cluster/Testdata6/Cluster sum/SuperNodeRelation.csv",
//					key1 + "," + key2 + "," + edis);
//		}
//	}
//
//	public void GetAllSuperNodeRealation() {
//		//获取所有SuperNode之间的关系
//		HashMap<String, ArrayList<String>> cn = new HashMap<String, ArrayList<String>>();
//		cn = CenterNormalNodesMap(
//				"D://For Cluster/Testdata6/Cluster sum/Nodes/",
//				"D://For Cluster/Testdata6/Cluster sum/ClusterSummary.csv");
//		Iterator<String> it = cn.keySet().iterator();
//		ArrayList<String> keys = new ArrayList<String>();
//		while (it.hasNext()) {
//			String s = it.next();
//			keys.add(s);
//		}
//		int size = keys.size();
//		for (int i = 0; i < size; i++) {
//			for (int j = i + 1; j < size; j++) {
//				TwoNodeEdgeAbstract(
//						"D://For Cluster/Testdata6/Testdata6Edge.csv",
//						keys.get(i), keys.get(j));
//			}
//		}
//	}
     
	/*	第一步，我们先获取所有SN之间的两两之间的距离
	 *	接下来关于边的抽象我们需要放在社团固定半径之后进行。
	 */
	public void getClusterCenterdis(String adisfile,String ccsumfile,String rcfile){
		//在求取固定半径的时候，那么 0<rc<=SNdis(min);
		//然后在社团内部进行划分；
		ArrayList<String> arr =new ArrayList<String>();
		arr = fraw.ReadData(adisfile);
		ArrayList<String> sn =new ArrayList<String>();
		sn = fraw.ReadData(ccsumfile);
		ArrayList<String> cn =new ArrayList<String>();
		for(String s: sn){
			cn.add(s.split(",")[0]);
		}
		Map<String,Integer> keyi = new HashMap<String,Integer>();
		String s = arr.get(0);
		String[] ss = s.split("\t");
		for(int i =0 ;i <ss.length;i++){
			keyi.put(ss[i], i);
		}
		for(int i =0 ; i < cn.size();i++){
			String key1 = cn.get(i);
			int ikey1 = keyi.get(key1);
			System.out.println(key1+":"+ikey1);
			for(int j =i+1; j <cn.size();j++){
				String key2 = cn.get(j);
				int ikey2 = keyi.get(key2);
				String[] temp = arr.get(ikey1+1).split("\t");
				String dis = temp[ikey2+1];
				fraw.WriteToFile(rcfile, key1 +","+key2+","+dis);
				System.out.println(key1 +","+key2+":"+dis);
			}
		}
		
	}
	
	public static void main(String[] args) {
		 SuperNodeAndSNDis fsn = new SuperNodeAndSNDis();
//		 HashMap<String, ArrayList<String>> cn = new HashMap<String,
//		 ArrayList<String>>();
//		 cn = fsn.CenterNormalNodesMap(
//		 "D://For Cluster/Testdata6/Cluster sum/Nodes/",
//		 "D://For Cluster/Testdata6/Cluster sum/ClusterSummary.csv");
//		 Iterator<String> it = cn.keySet().iterator();
//		 while (it.hasNext()) {
//		 Object c = it.next();
//		 System.out.println(c + ":");
//		 System.out.println("---------------");
//		 for (String s : cn.get(c))
//		 System.out.println(s);
//		 }
//		 fsn.TwoNodeEdgeAbstract("D://For Cluster/Testdata6/Testdata6Edge.csv",
//		 "20874", "11546");
//		 fsn.GetAllSuperNodeRealation();
		 fsn.getClusterCenterdis(filepath+"1000EdgesDisMatrix.csv", filepath+"Cluster sum/ClusterSummary.csv",filepath+"Cluster sum/SNDis.csv");
	}

}
