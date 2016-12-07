package com.jiao.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

//把点对存储的csv格式稀疏矩阵转换一般矩阵
public class CsvGraphToMatrix {
	public GraphStringOrder readcsv(String filenameedges,String filenamenodes) {
		GraphStringOrder gs = new GraphStringOrder();
		ArrayList<String> edges = new ArrayList<String>();
		ArrayList<String> nodes = new ArrayList<String>();
		File fr1 = new File(filenameedges);
		File fr2 = new File(filenamenodes);
		BufferedReader reader1= null;
		BufferedReader reader2= null;
		try {
			reader1 = new BufferedReader(new FileReader(fr1));
			String tempString = null;
			while ((tempString = reader1.readLine()) != null) {
				edges.add(tempString);
			}
			reader1.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader1 != null) {
				try {
					reader1.close();
				} catch (IOException e1) {
				}
			}
		}
		try {
			reader2 = new BufferedReader(new FileReader(fr2));
			String tempString = null;
			while ((tempString = reader2.readLine()) != null) {
				nodes.add(tempString);
			}
			reader2.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader2 != null) {
				try {
					reader2.close();
				} catch (IOException e1) {
				}
			}
		}
		gs.setNodes(nodes);
		gs.setEdges(edges);
		return gs;

	}

	public double[][] GetMatrix(GraphStringOrder gs) {
		ArrayList<String> nodes = new ArrayList<String>();
		nodes = gs.getNodes();
		ArrayList<String> edges = new ArrayList<String>();
		edges = gs.getEdges();
		int len = nodes.size();
		double[][] gmatrix = new double[len + 1][len + 1];
		for (int i = 0; i <= len; i++) {
			for (int j = 0; j <= len; j++) {
				gmatrix[i][j] = 0;
			}
		}
		for (int i = 1; i <= len; i++) {
			gmatrix[0][i] = Integer.valueOf(nodes.get(i - 1));
		}
		for (int i = 1; i <= len; i++) {
			gmatrix[i][0] = Integer.valueOf(nodes.get(i - 1));
		}
		for (String s : edges) {
			String s1 = s.split(",")[0];
			String s2 = s.split(",")[1];
			String s3 = s.split(",")[2];
			int sind = nodes.indexOf(s1) + 1;
			int tind = nodes.indexOf(s2) + 1;
			gmatrix[sind][tind] = Double.valueOf(s3);
			gmatrix[tind][sind] = Double.valueOf(s3);
		}
		return gmatrix;
	}

	public void Tostring(double a) {

	}

	public void MatrixTotxt(double[][] gmatrix, String filename)
			throws IOException {
		int size = gmatrix.length;
		FileWriter fw = new FileWriter(filename);
		for (int i = 0; i < size; i++) {
			String s = null;
			s = gmatrix[i][0] + "";
			for (int j = 1; j < size; j++) {
				s = s + " " + gmatrix[i][j];
			}
			fw.write(s);
			fw.write("\r\n");
			fw.flush();
		}
		fw.close();

	}

	public static void main(String[] args) throws IOException {
		CsvGraphToMatrix cs = new CsvGraphToMatrix();
		GraphStringOrder gs = cs.readcsv("/home/lee/biolg/ScaleFreeNetwork TestData/experiment/20/EdgeDis.csv",
				"/home/lee/biolg/ScaleFreeNetwork TestData/experiment/20/nodes.csv");
		double[][] gmatrix = new double[958][958];
		gmatrix = cs.GetMatrix(gs);
		cs.MatrixTotxt(gmatrix, "/home/lee/biolg/ScaleFreeNetwork TestData/experiment/20/AdjMatrix.csv");

	}
}
