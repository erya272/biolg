package com.data.mixed_graph;

//这里应该就可以得到超点和独立点了，在各自文件中，即为该聚类中包括的点，文件名即为聚类中心。
//
import java.io.File;
//import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;

//import java.util.Set;


import com.jiao.file.FileReadAndWrite;

//想可不可以和  entory-scale 那篇文章一样，定半径长进行分类。
//通过社团发现首先找到聚类中心，然后首先从聚类中心出发。
//但是现在应该缺少聚类中心数组，所以无法实现很好聚类。
public class FindClusterWithFixedRadius {
	static double rc = 1.8;
	// 根据SupeNodeAndSNDis 计算获取SN之间的最小半径
	// 根据rc对社团进行裁剪。在rc内部的算作该社团的，不在rc内的划分出去成为单独的点IN。
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String filepath = "E://ScaleFreeNetWork TestData/1000/";

	public Map<String, ArrayList<String>> Solution(String filename1,
			String filename2) {
		ArrayList<String> ccens = new ArrayList<String>();
		// 聚类中心
		Map<Integer, String> ikey = new HashMap<Integer, String>();
		Map<String, Integer> keyi = new HashMap<String, Integer>();
		// 距离矩阵中的标号对应的数据库中节点的值。
		ArrayList<String> nodes = new ArrayList<String>();
		nodes = fraw.ReadData(filename1);
		ArrayList<String> disMatrix = new ArrayList<String>();
		disMatrix = fraw.ReadData(filename2);
		ArrayList<String> startt = fraw.ReadData(filepath
				+ "Cluster Sum/ClusterSummary.csv");
		ArrayList<String> starts = new ArrayList<String>();
		for (String s : startt) {
			String ss = s.split(",")[0];
			starts.add(ss);
		}
		double[][] dis = new double[nodes.size()][nodes.size()];
		// 距离矩阵
		int line = 0;
		int j = 0;
		for (String s : disMatrix) {
			// 读取距离矩阵进行存储
			String[] ss = s.split("\t");
			int len = ss.length;
			if (line == 0) {
				for (int i = 0; i < len; i++) {
					ikey.put(i, ss[i]);
					keyi.put(ss[i], i);
				}
			} else {
				for (int k = 0; k < len - 1; k++) {
					dis[j][k] = Double.valueOf(ss[k + 1]);
				}
				j++;
			}
			line++;
		}
		ArrayList<String> nodesdup = new ArrayList<String>();
		int startindex = (int) (Math.random() * starts.size());
		String start = starts.get(startindex);
		ccens.add(start);
		nodesdup.add(start);
		starts.remove(start);
		nodes.remove(start);
		while (nodes.size() > 0) {
			int index = 0;
			int m = 0;
			String node = null;
			if (starts.size() > 0) {
				index = (int) (Math.random() * starts.size());
				node = starts.get(index);
				starts.remove(node);
				m = keyi.get(node);
				nodes.remove(node);
			} else {
				index = (int) (Math.random() * nodes.size());
				node = nodes.get(index);
				m = keyi.get(node);
				nodes.remove(node);
			}
			nodesdup.add(node);
			boolean flag = true;
			for (String s : ccens) {
				int i = keyi.get(s);
				if (dis[i][m] <= rc)
					flag = false;
				// 有一个值小于rc就不是聚类中心
				// 如果一个新点与聚类中心其它点的距离都大于rc,则可以把该点也可以做聚类中心，聚类中心的稳定性怎么保证.
				// 聚类中心的选择很重要，这里没有聚类中心的选择和规划。
				// 还有如果两个大社团中心的距离小于rc,那么当把一个社团中心规划在另一个社团之后，但这个社团的其他点与另一个社团中心的距离大于rc,
				// 该如何很好的处理剩下的点。
			}
			if (flag == true) {
				ccens.add(node);
			}
		}
		Map<String, ArrayList<String>> clus = new HashMap<String, ArrayList<String>>();
		for (String s : nodesdup) {
			int t = keyi.get(s);
			double min = Double.MAX_VALUE;
			String cen = null;
			for (String c : ccens) {
				int i = keyi.get(c);
				if (min > dis[i][t]) {
					min = dis[i][t];
					cen = ikey.get(i);
				}
			}
			if (clus.containsKey(cen)) {
				clus.get(cen).add(s);
			} else {
				ArrayList<String> nor = new ArrayList<String>();
				nor.add(s);
				clus.put(cen, nor);
			}

		}
		return clus;

	}

	/* 新的思路方法，根据rc对cluster进行剪枝，把大于rc的剪掉 */

	public void PrunningCluster(String edgedismatrixpath, String edgedispath,String snfile) {
		ArrayList<String> in = new ArrayList<String>();
		// 用来存储从每个社团中剪下的节点，作为独立点的集合
		File file = new File(edgedismatrixpath);
		File[] tempList = file.listFiles();
		File file2 = new File(edgedispath);
		File[] tempList2 = file2.listFiles();
		int len = tempList.length;
		for (int i = 0; i < len; i++) {
			ArrayList<String> dp = new ArrayList<String>();
			dp = fraw.ReadData(tempList[i].toString());
			Map<Integer, String> ikey = new HashMap<Integer, String>();
			ArrayList<String> sns = fraw.ReadData(snfile);
			String s = dp.get(0);
			String[] ss = s.split("\t");
			int index = 0;
			for (int j = 0; j < ss.length; j++) {
				ikey.put(j, ss[j]);
				if(ss[j].equals(sns.get(i)))
						index = j; //找出SN的在矩阵的index
			}
			String cc = dp.get(index+1);
			String[] dco = cc.split("\t");
			fraw.WriteToFile(filepath + "FixedR/SuperNodes.csv", dco[0]);
			// 把中心到其他点距离都存下来
			ArrayList<String> egd = new ArrayList<String>();
			egd = fraw.ReadData(tempList2[i].toString());
			for (int k = 1; k < dco.length; k++) {
				if (Double.valueOf(dco[k]) <= rc) {
					fraw.WriteToFile(filepath + "FixedR/SN/" + dco[0] + ".csv",
							ikey.get(k - 1));
				} else {
					in.add(ikey.get(k - 1));
					fraw.WriteToFile(filepath + "FixedR/IndependentNodes.csv",
							ikey.get(k - 1));
					for (String f : egd) {
						// 为了找到不在rc范围内的点与社团的联系
						if (f.split(",")[0].equals(ikey.get(k - 1))
								|| f.split(",")[1].equals(ikey.get(k - 1))) {
							fraw.WriteToFile(
									filepath + "FixedR/IN/" + ikey.get(k - 1)
											+ ".csv", f);
						}
					}
				}
			}

		}
	}

	public void getEdgedisAfterFixedR(String OldEdgedisfrpath, String ikeyfr,
			String INnodesfr, String NewEdgedisfrpath) {
		// ikeyfr 即为那个所有字符串与数字的一一对应，在这里我们可以看做每一个cluster对应的编号与SN的对应；
		// 对处理后的SN和IN，再处理一下他们的边,把原来的边中有独立节点的边去除。
		File file = new File(OldEdgedisfrpath);
		File[] tempList = file.listFiles();
		int len = tempList.length;
		ArrayList<String> innodes = new ArrayList<String>();
		innodes = fraw.ReadData(INnodesfr);
		ArrayList<String> ikey = new ArrayList<String>();
		ikey = fraw.ReadData(ikeyfr);
		HashMap<String, String> ik = new HashMap<String, String>();
		for (String s : ikey) {
			String i = s.split(":")[0];
			String key = s.split(":")[1];
			ik.put(i, key);
		}
		for (int i = 0; i < len; i++) {
			ArrayList<String> edges = new ArrayList<String>();
			edges = fraw.ReadData(tempList[i].toString());
			for (String s : edges) {
				String sou = s.split(",")[0];
				String tar = s.split(",")[1];
				if (!innodes.contains(sou) && !innodes.contains(tar)) {
					fraw.WriteToFile(
							NewEdgedisfrpath + ik.get(String.valueOf(i))
									+ ".csv", s);
				}
			}
		}
	}

	public static void main(String[] args) {
		FindClusterWithFixedRadius fcw = new FindClusterWithFixedRadius();
//		Map<String, ArrayList<String>> clus = fcw.Solution(filepath
//				+ "100nodes.csv", filepath + "100EdgesDisMatrix.csv");
//		Set<java.util.Map.Entry<String, ArrayList<String>>> set = clus.entrySet();
//		Iterator<java.util.Map.Entry<String, ArrayList<String>>> it = set
//				.iterator();
//		while (it.hasNext()) {
//			java.util.Map.Entry<String, ArrayList<String>> ob = it.next();
//			String key = ob.getKey();
//			ArrayList<String> values = ob.getValue();
//			for (String s : values) {
//				fraw.WriteToFile(filepath+"FixedR/" + key
//						+ ".csv", s);
//			}
//
//		}

		 fcw.PrunningCluster(filepath+"Cluster sum/EdgeDisMatrix",
		 filepath+"Cluster sum/EdgeDis",filepath+"Cluster sum/SuperNode.csv" );

//		 fcw.getEdgedisAfterFixedR(
//		 filepath+"Cluster sum/EdgeDis/",
//		 filepath+"CLuster sum/ikey.csv",
//		 filepath+"FixedR/IndependentNodes.csv",
//		 filepath+"FixedR/Edgedis/");
	}

}
