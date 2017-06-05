package com.jiao.steinerTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.jiao.file.FileReadAndWrite;

public class DreyfusWagner {
	// 实现混合图的SteinerTree算法，先粗略的解决外部的搜索，再细致到每一个cluster内部的搜索
	// Dreyfus-wagner algorithm
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static HashMap<Integer, String> jkey = new HashMap<Integer, String>();
	static HashMap<String, Integer> keyj = new HashMap<String, Integer>();
	// static String Disfile =
	// "E://ScaleFreeNetWork TestData/1000/FixedR/MixedGEdgesDisMatrix.csv";
	 static String Disfile ="/home/lee/biolg/ScaleFreeNetwork TestData/Compare/100/EdgeDisMatrix.csv";
// 	 static String Disfile = "D://For Cluster/test.csv";
//	 static String Disfile ="E://ScaleFreeNetwork TestData/HierarchyClustering/Clusters/199/43/disMatrix.csv";
	 static ShortestPathResult spr = new ShortestPathResult();
	 
	public double[][] getMixedGraphDistanceMatrixFromFile1(String fr) {
		// 从文件中读入最短路径矩阵
		ArrayList<String> disMatrix = new ArrayList<String>();
		disMatrix = fraw.ReadData(fr);
		int n = 0;
		for (String s : disMatrix.get(0).split("\t")) {
			jkey.put(n, s);
			keyj.put(s, n);
			n++;
		}
		double[][] Dis = new double[n][n];
		for (int j = 0; j < n; j++) {
			String dis = disMatrix.get(j + 1);
			System.out.println((j+1));
			for (int k = j; k < n; k++) {
				String []f =dis.split("\t");
				if(f[k+1] != "Inf" || !f[k+1].equals("Inf")){
					Dis[j][k] = Double.valueOf(f[k+1]);
					Dis[k][j] = Double.valueOf(f[k+1]);
				}				
				else{
					Dis[j][k] = Double.MAX_VALUE;
					Dis[k][j] = Double.MAX_VALUE;
				}
			}
		}
		return Dis;
	}
	
	public ResBackForDrefus getMixedGraphDistanceMatrixFromFile(String fr) {
		// 从文件中读入最短路径矩阵
		ResBackForDrefus res = new ResBackForDrefus();
		ArrayList<String> disMatrix = new ArrayList<String>();
		disMatrix = fraw.ReadData(fr);
		HashMap<Integer, String> jkey = new HashMap<Integer, String>();
		HashMap<String, Integer> keyj = new HashMap<String, Integer>();
		int n = 0;
		for (String s : disMatrix.get(0).split("\t")) {
			jkey.put(n, s);
			keyj.put(s, n);
			n++;
		}
		double[][] Dis = new double[n][n];
		for (int j = 0; j < n; j++) {
			String dis = disMatrix.get(j + 1);	
			String []f =dis.split("\t");
			for (int k = 0; k < n; k++) {
				if(f[k + 1]== "Inf" ||f[k + 1].equals("Inf") )
					Dis[j][k] = Double.MAX_VALUE;
				else
					Dis[j][k] = Double.valueOf(f[k + 1]);
			}
		}
		res.setDis(Dis);
		res.setJkey(jkey);
		res.setKeyj(keyj);
		return res;
	}

	public static String[] getBinaryValue(Set<Integer> set) {
		// 求出从0到集合子集数目之间的数的二进制形式
		int size = set.size();
		int m = (int) Math.pow(2, size) - 1;
		String[] results = new String[m + 1];
		for (int i = m; i > -1; i--) {
			StringBuffer sb = new StringBuffer(Integer.toBinaryString(i));
			int length = sb.length();
			if (length < size) {
				for (int j = 0; j < size - length; j++) {
					sb.insert(0, "0");
				}
			}
			results[i] = sb.toString();
		}
		return results;
	}

	public ArrayList<Set<Integer>> getSubset(Set<Integer> set) {
		// 根据二进制字符串生成子集
		ArrayList<Set<Integer>> result = new ArrayList<Set<Integer>>();
		int[] items = new int[set.size()];
		int i = 0;
		for (Integer item : set) {
			items[i] = item;
			i++;
		}
		String[] binaryValue = getBinaryValue(set);
		for (int j = 0; j < binaryValue.length; j++) {
			String value = binaryValue[j];
			Set<Integer> subset = new HashSet<Integer>();
			for (int k = 0; k < value.length(); k++) {
				if (value.charAt(k) == '1')
					subset.add(items[k]);
			}
			result.add(subset);
		}
		return result;
	}

	public Set<Integer> SetSubtract(Set<Integer> A, Set<Integer> B) {
		for (int it : B) {
			A.remove(it);
		}
		return A;
	}

	public ArrayList<String> RunDreyfusWagner1(Set<Integer> N, Set<Integer> Y,
			double Dis[][],HashMap<Integer, String> jkey,double adj[][]) {
		//////////  nodes set, keys set,  shortest matrix, nodes map,   adjesment matrix
		HashMap<HashMap<Set<Integer>, Integer>, Integer> arr = new HashMap<HashMap<Set<Integer>, Integer>, Integer>();
		HashMap<HashMap<Set<Integer>, Integer>, HashMap<Set<Integer>, Integer>> arr2 = new HashMap<HashMap<Set<Integer>, Integer>, HashMap<Set<Integer>, Integer>>();
		HashMap<HashMap<Set<Integer>, Integer>, HashMap<Set<Integer>, Integer>> arr3 = new HashMap<HashMap<Set<Integer>, Integer>, HashMap<Set<Integer>, Integer>>();
		long startTime = System.currentTimeMillis();
		int q = Y.iterator().next();
		Y.remove(q);     
		Set<Integer> C = Y;
//		System.out.println("q:"+q+"\tY:"+Y+"\tN:"+N);
//		System.out.println("dis:" + Dis.length +"\t adj: "+adj.length+" jkey:"+jkey.size() +"\n"+jkey );
//		System.out.println("drey fus wagner:\n"+ N.size()+"\t"+Y.size()+"\n"+ Dis.length+" "+jkey.size()+" "+adj.length);
		
		HashMap<HashMap<Set<Integer>, Integer>, Double> S = new HashMap<HashMap<Set<Integer>, Integer>, Double>();
		for (int t : C) {
			for (int J : N) {
				HashMap<Set<Integer>, Integer> hm = new HashMap<Set<Integer>, Integer>();
				Set<Integer> h = new HashSet<Integer>();
				h.add(t);
				hm.put(h, J);
				S.put(hm, Dis[t][J]);
			}
		}
//		System.out.println("S.size: "+S.size());
		ArrayList<Set<Integer>> res = new ArrayList<Set<Integer>>();
		res = getSubset(C); /////// all subsets
//		System.out.println("res: "+res);
		
		///////// dynamic algorithm /////////
		for (int m = 2; m <= C.size() - 1; m++) { ///////
			for (Set<Integer> D : res) {
				if (D.size() == m) {
//					System.out.println("set: "+D);
					for (int I : N) {  ////////  other subsets added into S
						HashMap<Set<Integer>, Integer> hm = new HashMap<Set<Integer>, Integer>();
						hm.put(D, I);
						S.put(hm, Double.MAX_VALUE);
					}
					
					int D1 = D.iterator().next();
//					System.out.println(D1+" "+D);
					ArrayList<Set<Integer>> re = new ArrayList<Set<Integer>>();
					re = getSubset(D);
					re.remove(D); //// D's true subset
					for (int J : N) {
						double u = Double.MAX_VALUE;
						for (Set<Integer> E : re) {  //////// is here some problem ???????????????????????????? 
							if (E.contains(D1)) {   ////////// make sure not repeated,  from head to tail  
								HashMap<Set<Integer>, Integer> hm = new HashMap<Set<Integer>, Integer>();
								hm.put(E, J);
								HashMap<Set<Integer>, Integer> hm1 = new HashMap<Set<Integer>, Integer>();
								Set<Integer> DC = new HashSet<Integer>();
								for (Integer d : D)
									DC.add(d);
								DC = SetSubtract(DC, E);  //////// from DC, delete all elements appeared in E 
								hm1.put(DC, J); //////// DC E 互为补集  , belong to D 
								// u = Math.min(u, S.get(hm) + S.get(hm1));
								if (u > S.get(hm) + S.get(hm1)) {
									u = S.get(hm) + S.get(hm1);
									HashMap<Set<Integer>, Integer> hm2 = new HashMap<Set<Integer>, Integer>();
									hm2.put(D, J);
									arr2.put(hm2, hm);  ///////// record the route
									arr3.put(hm2, hm1);  /////// record the route
								}
							}
						}
						for (int I : N) {
							HashMap<Set<Integer>, Integer> hm = new HashMap<Set<Integer>, Integer>();
							hm.put(D, I);
							// S.put(hm, Math.min(S.get(hm), Dis[I][J] + u));
							if (S.get(hm) > Dis[I][J] + u) {
								S.put(hm, Dis[I][J] + u);
								arr.put(hm, J);
								///////////////  record the partition in dynamic algorithm together
							}

						}
					}

				}

			}
		}
		///////// dynamic algorithm /////////
		
//		System.out.println("S.size: "+S.size());
//		System.out.println("Dynamic algorithm:"+(System.currentTimeMillis()-startTime)+"ms");
		
		double v = Double.MAX_VALUE;
		String s1 = null;
		// 集合S1
		String s2 = null;
		// 集合S2
		String s11 = null;
		// 最终的S1
		String s22 = null;
		// 最终的S2
		int p = 0;
		// 划分点
		HashMap<Set<Integer>, Integer> sh1 = new HashMap<Set<Integer>, Integer>();
		HashMap<Set<Integer>, Integer> sh2 = new HashMap<Set<Integer>, Integer>();
		HashMap<Set<Integer>, Integer> sh11 = new HashMap<Set<Integer>, Integer>();
		HashMap<Set<Integer>, Integer> sh22 = new HashMap<Set<Integer>, Integer>();

		int C1 = C.iterator().next();
		ArrayList<Set<Integer>> re = new ArrayList<Set<Integer>>();
		re = getSubset(C);
		re.remove(C);
		for (int J : N) {
			double u = Double.MAX_VALUE;
			for (Set<Integer> E : re) {
				if (E.contains(C1)) {
					HashMap<Set<Integer>, Integer> hm = new HashMap<Set<Integer>, Integer>();
					hm.put(E, J);
					HashMap<Set<Integer>, Integer> hm1 = new HashMap<Set<Integer>, Integer>();
					Set<Integer> DC = new HashSet<Integer>();
					for (Integer d : C)
						DC.add(d);
					DC = SetSubtract(DC, E);
					hm1.put(DC, J);
					if (u > S.get(hm) + S.get(hm1)) {
						u = S.get(hm) + S.get(hm1);
						s1 = hm.toString();
						s2 = hm1.toString();
						sh1 = hm;
						sh2 = hm1;
					}
				}
			}
			if (v > Dis[q][J] + u) {
				v = Dis[q][J] + u;
				p = J;
				s11 = s1;
				s22 = s2;
				sh11 = sh1;
				sh22 = sh2;

			}
		}
//		System.out.println(s11 + "," + s22);
//		System.out.println(q + "-" + p);
//		System.out.println("Cost-----:"+v);
		ArrayList<String> resu = new ArrayList<String>();
//		if (q != p)
//			resu.add(jkey.get(q) + "," + jkey.get(p));
		if (q != p)
			resu.add(q + "," + p);
		GetPath1(arr, arr2, arr3, sh11, resu, jkey);
		GetPath1(arr, arr2, arr3, sh22, resu, jkey);
		long endTime = System.currentTimeMillis();
//		System.out.println(resu);
		int len = N.size();
		ArrayList<String> fresu = new ArrayList<String>();
		for(String sss:resu){
			int sou = Integer.valueOf(sss.split(",")[0]);
			int tar = Integer.valueOf(sss.split(",")[1]);
//			System.out.println(jkey.get(sou)+"----"+jkey.get(tar));
			if(Dis[sou][tar]!=adj[sou][tar]){
				int[] r = spr.shortestPath(len, sou, tar, adj);
				for(int i = 0 ; i<r.length-1;i++){
//					System.out.println(jkey.get(r[i])+"-"+jkey.get(r[i+1]));
					fresu.add(jkey.get(r[i])+","+jkey.get(r[i+1]));
				}
			}
			else{
				fresu.add(jkey.get(sou)+","+jkey.get(tar));
			}
			
		}
//		System.out.println("running time:" + (endTime - startTime) + "ms");
		return fresu;
	}
	public ArrayList<String> GetPath1(
			HashMap<HashMap<Set<Integer>, Integer>, Integer> arr,
			HashMap<HashMap<Set<Integer>, Integer>, HashMap<Set<Integer>, Integer>> arr2,
			HashMap<HashMap<Set<Integer>, Integer>, HashMap<Set<Integer>, Integer>> arr3,
			HashMap<Set<Integer>, Integer> sh, ArrayList<String> resu,
			HashMap<Integer, String> jkey) {
		while (sh.keySet().iterator().next().size() >= 2) {
			int k = arr.get(sh);
			Set<Integer> set = sh.keySet().iterator().next();
			if (k != sh.get(set) ) {
				if(set.size() != 2){
					int v = sh.get(set);
					resu.add(k + "," + v);
					sh.put(set,k);
					GetPath1(arr, arr2, arr3, arr2.get(sh), resu, jkey);
					GetPath1(arr, arr2, arr3, arr3.get(sh), resu, jkey);
					break;
					}else{
						int v = sh.get(set);
						resu.add(k + "," + v);
						sh.put(set, k);
					}
			}
			else {
				if (set.size() == 2) {
					Object[] a = set.toArray();
					int k1 = (Integer) a[0];
					int k2 = (Integer) a[1];
					if (k1 != k)
						resu.add(k + "," + k1);
					if (k2 != k)
						resu.add(k + "," + k2);
					break;
				} else {
					GetPath1(arr, arr2, arr3, arr2.get(sh), resu, jkey);
					GetPath1(arr, arr2, arr3, arr3.get(sh), resu, jkey);
					break;
				}
			}
			if (sh.keySet().iterator().next().contains(k)) {
				sh.remove(set);
				set.remove(k);
				sh.put(set, k);
			}
		}

		int k = sh.keySet().iterator().next().iterator().next();
		int v = sh.get(sh.keySet().iterator().next());
		if (sh.keySet().iterator().next().size() == 1 && k != v) {
			resu.add(k + "," + v);
		}
		return resu;
	}
	public static void main(String[] args) {
		DreyfusWagner st = new DreyfusWagner();
		double[][] adj = spr.ReadAdjMatrix("/home/lee/biolg/ScaleFreeNetwork TestData/Compare/100/AdjMatrix.csv",jkey,keyj);
		double[][] Dis = st.getMixedGraphDistanceMatrixFromFile1(Disfile);
		// double[][] Dis =
		// st.getMixedGraphDistanceMatrixFromFile("D://For Cluster/Testdata6/AllEdgeDisMatrix.csv");
		Set<Integer> N = new HashSet<Integer>();
		Set<Integer> Y = new HashSet<Integer>();
		int len = Dis.length;
		for (int i = 0; i < len; i++) {
			N.add(i);
		}
		System.out.println(Dis[3][32]+",,,"+Dis[32][51]);
		 Y.add(keyj.get("4"));
		 System.out.println(keyj.get("4"));
		 Y.add(keyj.get("34"));
		 System.out.println(keyj.get("34"));
		 Y.add(keyj.get("11"));
		 System.out.println(keyj.get("11"));

//		Y.add(keyj.get("1"));
//		Y.add(keyj.get("2"));
//		Y.add(keyj.get("7"));
//		Y.add(keyj.get("4"));

//		 System.out.println(keyj.get("152"));
//		 Y.add(keyj.get("152"));
//		 System.out.println(keyj.get("69"));
//		 Y.add(keyj.get("69"));
//		 System.out.println(keyj.get("93"));
//		 Y.add(keyj.get("93"));
//		 System.out.println(keyj.get("56"));
//		 Y.add(keyj.get("56"));
//		 System.out.println(keyj.get("107"));
//		 Y.add(keyj.get("107"));
//		 System.out.println(keyj.get("25"));
//		 Y.add(keyj.get("25"));
//		 System.out.println(keyj.get("157"));
//		 Y.add(keyj.get("157"));
//		 System.out.println(keyj.get("73"));
//		 Y.add(keyj.get("73"));
//		 System.out.println(keyj.get("74"));
//		 Y.add(keyj.get("74"));
//		 System.out.println(keyj.get("156"));
//		 Y.add(keyj.get("156"));

		st.RunDreyfusWagner1(N, Y, Dis,jkey,adj);
	}
}
