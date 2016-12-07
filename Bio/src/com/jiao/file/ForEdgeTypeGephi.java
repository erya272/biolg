package com.jiao.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ForEdgeTypeGephi {
	//为了适应gephi画图的数据准备
 public void TypeGephi(String file1,String file2) throws IOException{
	 	File f1 = new File(file1);
	 	FileWriter f2 = new FileWriter(file2);
		BufferedReader reader1= null;
		try {
			reader1 = new BufferedReader(new FileReader(f1));
			String tempString = null;
			String ws = null;
			String header = "Source"+","+"Target"+","+"Weight"+","+"Type";
			f2.write(header);
			f2.write("\r\n");
			f2.flush();
			while ((tempString = reader1.readLine()) != null) {
                  ws = tempString+","+"Undirected";
              	f2.write(ws);
    			f2.write("\r\n");
    			f2.flush();
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
		f2.close();
 }
 public static void main(String []args) throws IOException{
	 ForEdgeTypeGephi fe = new ForEdgeTypeGephi();
	 fe.TypeGephi("E://ScaleFreeNetwork TestData/experiment/test/11Edges.csv", "E://ScaleFreeNetwork TestData/experiment/test/11EdgesGeiph.csv");
 }
}
