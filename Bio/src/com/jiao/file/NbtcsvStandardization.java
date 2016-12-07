package com.jiao.file;

import java.util.ArrayList;

public class NbtcsvStandardization {
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String fpath = "E://ScaleFreeNetwork TestData/nnbt/";

	public void Processing(String fr,String sfr) {
		ArrayList<String> read = new ArrayList<String>();
		read = fraw.ReadData(fr);
		int count = 0;	
		for (String s : read) {
			if (count > 0) {
				String[] ss = s.split(",");
				if (ss[2].contains("\"") && ss[3].contains("\"")) {
					ss[2] = ss[2] + ss[3];
					ss[3] = "";
					String sss = ss[0];
					for (int i = 1; i < ss.length; i++) {
						if (ss[i] != "")
							sss += "," + ss[i];
					}
					s =sss;
					System.out.println(s);
				}
				else if (ss[5].contains("\"") && ss[6].contains("\"")) {
					ss[5] = ss[5] + ss[6];
					ss[6] = "";
					String sss = ss[0];
					for (int i = 1; i < ss.length; i++) {
						if (ss[i] != "")
							sss += "," + ss[i];
					}
					s =sss;
					System.out.println(s);
				}
				fraw.WriteToFile(sfr, s);
			}
			count++;
		}
	}

	public void Convert(String fr, String fw) {
		ArrayList<String> read = new ArrayList<String>();
		read = fraw.ReadData(fr);
		for (String s : read) {
				String s1 = s.split(",")[1];
				String s2 = s.split(",")[4];
				String ss = s1 + "," + s2 + "," + "1";
				fraw.WriteToFile(fw, ss);
			}
	}

	public static void main(String[] args) {
		NbtcsvStandardization nbt = new NbtcsvStandardization();

//		nbt.Processing(fpath + "nbt.csv",fpath + "snbt.csv");
		nbt.Convert(fpath+"snbt.csv", fpath+"Edges.csv");
	}

}
