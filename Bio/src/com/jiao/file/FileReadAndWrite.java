package com.jiao.file;

import java.io.BufferedReader;
//import java.io.File;
import java.io.FileInputStream;
//import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileReadAndWrite {
	// 文件读写
	public ArrayList<String> ReadData(String filename) {
		// 文件读，按行读，存入List
		ArrayList<String> arr = new ArrayList<String>();
//		File fr = new File(filename);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"UTF-8"));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				arr.add(tempString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return arr;
	}
	public void WriteToFile(String filename, String s) {
		// 文件写，按行写，以追加的形式写
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename, true);
			fw.write(s);
			fw.write("\r\n");
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
