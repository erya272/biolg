package com.jiao.steinerTree;



import java.util.HashMap;

import com.jiao.file.FileReadAndWrite;

public class RandomWalkRestart {
	public static double MAX = 0.0000001;// 阈值
	static double a = 0.1;// 阻尼系数
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static ShortestPathResult spr = new ShortestPathResult();
	static String adjfr = "/home/lee/biolg/ScaleFreeNetwork TestData/experiment/test/11AdjcentMatrix.csv";
	static HashMap<Integer, String> jkey = new HashMap<Integer, String>();
	static HashMap<String, Integer> keyj = new HashMap<String, Integer>();
	static double[][] adj = spr.ReadAdjMatrix(adjfr, jkey, keyj);

	// 计算一个矩阵乘以一个向量
	public double[] vectorMulMatrix(double[][] m, double[] v) {
		if (m == null || v == null || m.length <= 0 || m[0].length != v.length) {
			return null;
		}
		double[] mm = new double[m[0].length];
		double su = 0;
		for (int i = 0; i < m.length; i++) {
			double sum = 0;
			for (int j = 0; j < m[0].length; j++) {
				double temp = m[i][j] * v[j];
				sum += temp;
			}
			mm[i] = sum;
			su += sum;
		}
		for (int i = 0; i < m.length; i++) {
			mm[i] = (double) mm[i] / (double) su;
		}
		return mm;
	}

	// 计算一个数乘以矩阵
	public double[][] numberMulMatrix(double[][] s, double a) {
		double[][] m = new double[s.length][s.length];
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s.length; j++) {
				m[i][j] = a * s[i][j];
			}
		}
		return m;
	}

	// 计算两个矩阵之和
	public double[][] addMatrix(double[][] m1, double[][] m2) {
		if (m1.length != m2.length || m1.length <= 0 || m2.length <= 0)
			return null;
		double[][] m = new double[m1.length][m1.length];
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m1.length; j++) {
				double temp = m1[i][j] + m2[i][j];
				m[i][j] = temp;
			}
		}
		return m;
	}

	// 计算初始矩阵,邻接矩阵按行归一化
	public double[][] ComputeA(double[][] adj) {
		int n = adj.length;
		for (int i = 0; i < n; i++) {
			double sum = 0;
			for (int j = 0; j < n; j++) {
				if (adj[i][j] == Double.MAX_VALUE)
					adj[i][j] = 0;
				sum += adj[i][j];
			}
			for (int t = 0; t < n; t++) {
				if (adj[i][t] == 1) {
					adj[i][t] = (double) 1 / (double) sum;
				}
			}
		}
		return MatrixTranspose(adj);
	}

	// 数字乘以一个向量
	public double[] NumberMultiVector(double[] e, double a) {
		for (int i = 0; i < e.length; i++) {
			e[i] = a * e[i];
		}
		return e;
	}

	// 矩阵转置
	public double[][] MatrixTranspose(double[][] A) {
		int len = A.length;
		double[][] b = new double[len][len];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				b[j][i] = A[i][j];
			}
		}
		return b;
	}

	// 计算两个向量之间的距离
	public double calDistance(double[] q1, double[] q2) {
		double sum = 0;
		if (q1.length != q2.length) {
			return -1;
		}

		for (int i = 0; i < q1.length; i++) {
			sum += Math.pow(q1[i] - q2[i], 2);
		}
		return Math.sqrt(sum);
	}

	// 计算两个向量之和
	public double[] addVector(double[] a, double[] b) {
		int len = a.length;
		double[] c = new double[len];
		for (int i = 0; i < len; i++) {
			c[i] = a[i] + b[i];
		}
		return c;
	}

	// 计算稳态向量
	public double[] calRWR(double[][] adj, int s, double a) {
		//输入的adj 是初始邻接矩阵，没有经过归一化
		double[][] e = new double[adj.length][adj.length];
		for (int i = 0; i < adj.length; i++) {
			e[s][i] = 1;
		}
		double[] q1 = new double[adj.length];
		for (int i = 0; i < adj.length; i++) {
			q1[i] = 1;
		}
		adj = ComputeA(adj);	//规范化邻接矩阵
		double[][] A = addMatrix(numberMulMatrix(adj, 1 - a),
				numberMulMatrix(e, a));
		double[] q = new double[q1.length];
//		int iterator = 1;
		while (true) {
			q = vectorMulMatrix(A, q1);
			double dis = calDistance(q, q1);
			if (dis <= MAX) {
				break;
			}
			q1 = q;
//			iterator++;
		}
//		System.out.println(iterator);
		return q;
	}

	public int VectorGetMaxIndex(double[] q, int j){
		int index;
		double max;
		if(j !=0){
			index =0;
			max =q[0];
		}else{
			index =1;
			max =q[1];
		}
		for(int i = 0; i <q.length;i++){
			if(max <q[i] && i!=j){
				max =q[i];
				index = i;
			}
		}
		return index;
		
	}
	public static void main(String[] args)  {
		RandomWalkRestart rwr = new RandomWalkRestart();
		 
//		double[][] test = {
//				{ 0, (double) 1 / 3, (double) 1 / 3, (double) 1 / 3, 0, 0, 0,
//						0, 0, 0, 0, 0 },
//				{ (double) 1 / 3, 0, (double) 1 / 3, 0, 0, 0, 0,
//						(double) 1 / 4, 0, 0, 0, 0 },
//				{ (double) 1 / 3, (double) 1 / 3, 0, (double) 1 / 3, 0, 0, 0,
//						0, 0, 0, 0, 0 },
//				{ (double) 1 / 3, 0, (double) 1 / 3, 0, (double) 1 / 4, 0, 0,
//						0, 0, 0, 0, 0 },
//				{ 0, 0, 0, (double) 1 / 3, 0, (double) 1 / 2, (double) 1 / 2,
//						(double) 1 / 4, 0, 0, 0, 0 },
//				{ 0, 0, 0, 0, (double) 1 / 4, 0, (double) 1 / 2, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 0, (double) 1 / 4, (double) 1 / 2, 0, 0, 0, 0, 0, 0 },
//				{ 0, (double) 1 / 3, 0, 0, (double) 1 / 4, 0, 0, 0,
//						(double) 1 / 2, 0, (double) 1 / 3, 0 },
//				{ 0, 0, 0, 0, 0, 0, 0, (double) 1 / 4, 0, (double) 1 / 3, 0, 0 },
//				{ 0, 0, 0, 0, 0, 0, 0, 0, (double) 1 / 2, 0, (double) 1 / 3,
//						(double) 1 / 2 },
//				{ 0, 0, 0, 0, 0, 0, 0, (double) 1 / 4, 0, (double) 1 / 3, 0,
//						(double) 1 / 2 },
//				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, (double) 1 / 3, (double) 1 / 3, 0 } };		
//		double[][] test1 = {{0,1,1,1,1},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0},{1,0,0,0,0}};
//		double[][] qq = new double[5][5];
//		for(int  j = 0 ; j<qq.length ;j++){
//			double[] q = rwr.calRWR(test1,j, 0.1);
//			for(int i = 0; i <q.length;i++)
//				qq[j][i] = q[i];
//		}
//		System.out.println(qq[0][1]);
//		System.out.println(qq[1][0]);
//		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("0.00");  
//		for(int i = 0; i <qq.length;i++){
//			String s = "";
//			for(int j = 0; j < qq.length;j++){
//				s = s+" "+df.format(qq[i][j]);
//			}	
//			System.out.println(s);
//		}	
//	
		
		
		String[] keys = { "瞿颖", "窦唯", "李亚鹏", "窦颖", "朴树" };
		int len = keys.length;
		double[][] qq = new double[len][len];		
		int[] ii = new int[len];
		for (int i = 0; i < len; i++) {
			int k = keyj.get(keys[i]);
			ii[i] = k;
		}
		for(int i = 0; i<keys.length;i++){
			 int k = keyj.get(keys[i]);
			 double[] q = rwr.calRWR(adj,k, 0.1);	 
			 System.out.println(keys[i]+","+k);
			 for(int j = 0; j <len;j++){
				 qq[j][i] = q[ii[j]];
				 System.out.println(qq[j][i]);
			 }
		}
		
		
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("0.00");  
		for(int i = 0; i <qq.length;i++){
			String s = "";
			for(int j = 0; j < qq.length;j++){
				s = s+" "+df.format(qq[i][j]);
			}	
			System.out.println(s);
		}	
		 	
		
	}
}
