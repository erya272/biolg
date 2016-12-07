package com.data.mixed_graph;
//找到每一个聚类的中心和聚类半径，其实前一步的社团发现，只是为了获得之后的聚类中心作为berger算法的输入而已。
import java.io.File;
//import java.math.BigDecimal;
import java.util.ArrayList;

import com.jiao.file.FileReadAndWrite;
//先处理社团结构，获取单独每个社团的节点集合。DataFromModularity
//然后再处理边的集合，如果有一条边包括社团中的两个节点，那么该边就在该社团中。 ReadFromEdgeCsvPath（批量处理）
//然后到R中获取最短路径的矩阵
//通过最短路径矩阵获取聚类中心和聚类半径。ReadFromDisMatrixPath（批量处理）
public class FindClusterCenter {
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String filepath = "E://ScaleFreeNetWork TestData/1000/";
	public void DataFromModularity(String filename) {
		ArrayList<String> arr = new ArrayList<String>();
		arr = fraw.ReadData(filename);
		int i = 0;
		for (String s : arr) {
			if (i != 0) {
				fraw.WriteToFile(
						filepath+"Cluster sum/Nodes/"
								+ s.split(",")[2] + ".csv", s.split(",")[0]);
			}
			i++;
		}
	}

	public void ReadFromEdgeCsv(String fr1, String fr2, String fr3) {
		ArrayList<String> edges = new ArrayList<String>();
		edges = fraw.ReadData(fr1);
		ArrayList<String> cnodes = new ArrayList<String>();
		cnodes = fraw.ReadData(fr2);
		for (String s : edges) {
			String sou = s.split(",")[0];
			String tar = s.split(",")[1];
			double dis = 1.0 - Double.valueOf(s.split(",")[2]);
			// BigDecimal dis1 = new BigDecimal(dis);
			// double dis2 = dis1.setScale(3,
			// BigDecimal.ROUND_HALF_UP).doubleValue();
			if (cnodes.contains(sou) && cnodes.contains(tar)) {
				System.out.println(s);
				fraw.WriteToFile(fr3, sou + "," + tar + "," + dis);
				// edges.remove(s);
			}
		}
	}

	public void ReadFromEdgeCsvPath(String path, String fr1) {
		// 从文件夹批量读入处理
		File file = new File(path);
		File[] tempList = file.listFiles();
		ArrayList<String> edges = new ArrayList<String>();
		edges = fraw.ReadData(fr1);
		for (int i = 0; i < tempList.length; i++) {
			ArrayList<String> cnodes = new ArrayList<String>();
			cnodes = fraw.ReadData(tempList[i].toString());
			for (String s : edges) {
				String sou = s.split(",")[0];
				String tar = s.split(",")[1];
				double dis = 1.0 - Double.valueOf(s.split(",")[2]);
				// BigDecimal dis1 = new BigDecimal(dis);
				// double dis2 = dis1.setScale(3,
				// BigDecimal.ROUND_HALF_UP).doubleValue();
				if (cnodes.contains(sou) && cnodes.contains(tar)) {
					System.out.println(s);
					fraw.WriteToFile(
							filepath+"Cluster sum/EdgeDis/"
									+ i + "EdgesDis" + ".csv", sou + "," + tar
									+ "," + dis);
					// edges.remove(s);
				}
			}
		}
	}

	public void AllEdgeSimilarityToEdgeDis(String edgefr,String edgedisfr){
		ArrayList<String> edges = new ArrayList<String>();
		edges = fraw.ReadData(edgefr);
		for(String s: edges){
			String sou = s.split(",")[0];
			String tar = s.split(",")[1];
			double dis = 1.0 - Double.valueOf(s.split(",")[2]);
			fraw.WriteToFile(
					edgedisfr, sou + "," + tar
							+ "," + dis);
		}
		
	}
	public void ReadFromDisMatrix(String filename) {
		ArrayList<String> arr = new ArrayList<String>();
		arr = fraw.ReadData(filename);
		int line = 0;
		double Min = Double.MAX_VALUE;
		double Rmax = 0;
		String index = null;
		for (String s : arr) {
			if (line != 0) {
				double sum = 0;
				double rm = 0;
				String[] ss = s.split("\t");
				int len = ss.length;
				for (int i = 1; i < len; i++) {
					if (rm < Double.valueOf(ss[i])) {
						rm = Double.valueOf(ss[i]);
					}
					sum += Double.valueOf(ss[i]);
				}
				System.out.println(sum);
				if (Min > sum) {
					Min = sum;
					index = ss[0];
					Rmax = rm;
				}
			}
			line++;
		}
		fraw.WriteToFile(filepath+"Cluster sum/ClusterSummary.csv",
				index + "," + Rmax);
		System.out.println(index);
		System.out.println(Min);
		System.out.println(Rmax);
	}

	public void ReadFromDisMatrixPath(String path) {
		//批量处理距离矩阵获得聚类中心和聚类最大半径。
		File file = new File(path);
		File[] tempList = file.listFiles();
		for (int j = 0; j < tempList.length; j++) {
			ArrayList<String> arr = new ArrayList<String>();
			arr = fraw.ReadData(tempList[j].toString());
			int line = 0;
			double Min = Double.MAX_VALUE;
			double Rmax = 0;
			String index = null;
			for (String s : arr) {
				if (line != 0) {
					double sum = 0;
					double rm = 0;
					String[] ss = s.split("\t");
					int len = ss.length;
					for (int i = 1; i < len; i++) {
						if (rm < Double.valueOf(ss[i])) {
							rm = Double.valueOf(ss[i]);
						}
						sum += Double.valueOf(ss[i]);
					}
					if (Min > sum) {
						Min = sum;
						index = ss[0];
						Rmax = rm;
					}
				}
				line++;
			}
			fraw.WriteToFile(
					filepath+"Cluster sum/ClusterSummary.csv",
					index + "," + Rmax);
			fraw.WriteToFile(filepath+"Cluster sum/SuperNode.csv",index);
			fraw.WriteToFile(filepath+"Cluster sum/ikey.csv",j+":"+index);
			System.out.println(index);
			System.out.println(Min);
			System.out.println(Rmax);
		}
	}

	public static void main(String[] args) {
		FindClusterCenter fcc = new FindClusterCenter();
//		fcc.DataFromModularity(filepath+"1000nodes [Nodes]Modularity.csv");
//		fcc.ReadFromEdgeCsv(filepath+"1000Edges.csv",filepath+"1000nodes.csv",filepath+"1000EdgesDis.csv");
//		fcc.ReadFromEdgeCsvPath(filepath+"Cluster sum/Nodes",filepath+"1000Edges.csv");
//		fcc.ReadFromDisMatrixPath(filepath+"Cluster sum/EdgeDisMatrix/");
		fcc.AllEdgeSimilarityToEdgeDis(filepath+"1000Edges.csv",filepath+"1000EdgesDis.csv");

	}
}
