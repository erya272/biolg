package com.jiao.file;

import java.util.ArrayList;
import java.util.HashMap;

import com.jiao.element.KeyAndIndex;

public class StringToNumberForHac {
	// 用于后期keyi和ikey序列化，以及把最短路径矩阵中的字符串转换成数字，方便后期的层次聚类
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String fpath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";

	public KeyAndIndex KeyIIKey(String fr) {
		ArrayList<String> read = fraw.ReadData(fr);
		HashMap<String, String> ikey = new HashMap<String, String>();
		HashMap<String, String> keyi = new HashMap<String, String>();

		KeyAndIndex ki = new KeyAndIndex();
		String s = read.get(0);
		String[] arr = s.split("\t");
		for (int j = 1; j <= arr.length; j++) {
			ikey.put(j + "", arr[j - 1]);
			keyi.put(arr[j - 1], j + "");
		}
		ki.setikey(ikey);
		ki.setKeyi(keyi);
		return ki;
	}
	public KeyAndIndex MatrixForHac(String fr, String fw) {
		ArrayList<String> read = fraw.ReadData(fr);
		int i = 0;
		KeyAndIndex ki = new KeyAndIndex();
		ki = KeyIIKey(fr);
		for (String s : read) {
			if (i == 0) {
				StringBuffer ss = new StringBuffer("0");
				String[] arr = s.split("\t");
				for (int j = 1; j <= arr.length; j++) {
					ss.append("\t" + j);
				}
				 fraw.WriteToFile(fw, ss.toString());
			} else {
				String[] arr = s.split("\t");
				System.out.println(arr[0] + "," + ki.getKeyi().get(arr[0]));
				StringBuffer ss = new StringBuffer(ki.getKeyi().get(arr[0]));
				for (int j = 1; j < arr.length; j++) {
					if (arr[j].equals("Inf") || arr[j] == "Inf")
						ss.append("\t" + Integer.MAX_VALUE);
					else
						ss.append("\t" + arr[j]);
				}
				 fraw.WriteToFile(fw, ss.toString());
			}
			i++;
		}
		return ki;
	}

	public static void main(String[] args) {
		StringToNumberForHac mh = new StringToNumberForHac();
		mh.MatrixForHac(fpath + "EdgeDisMatrix.csv", fpath
				+ "MatlabEdgeDisMatrix.csv");

	}

}
