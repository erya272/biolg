package com.jiao.file;

import java.util.ArrayList;
import java.util.HashMap;

import com.jiao.element.KeyAndIndex;

public class CompleteHACRes {
    //层次聚类结果转换，是离开MATLAB之后的第一步数据处理，Matlab中Z的结果转换成层次的
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String fpath = "E://ScaleFreeNetwork TestData/snbt/";
	static StringToNumberForHac stn =new StringToNumberForHac();
	public void Convert(String f1, String f2){
		ArrayList<String> odata =  new ArrayList<String>();
		odata = fraw.ReadData(f1);
		int len = odata.size()+1;
		int i =  len+1;//cluster 命名的起始类名
		for(String s : odata){
			fraw.WriteToFile(f2, i+","+s);
			i++;
		}
		
	}
	public void ConvertfinalHacToOriginal(String fr,String fw){
		KeyAndIndex ki = stn.KeyIIKey(fpath+"EdgeDisMatrix.csv");
		HashMap<String, String> ikey = new HashMap<String, String>();
		ArrayList<String> odata =  new ArrayList<String>();
		odata = fraw.ReadData(fr);
		ikey =ki.getikey();
		for(String s:odata){
			String  sn = s.split(",")[0];
			String  in1 = s.split(",")[1];
			String  in2 = s.split(",")[2];
			String  w = s.split(",")[3];
			if(ikey.get(in1) != null)
				in1 = ikey.get(in1);
			if(ikey.get(in2) != null)
				in2 = ikey.get(in2);
			String ss = sn+","+in1+","+in2+","+w;
			fraw.WriteToFile(fw,ss);
			System.out.println(ss);
		}
		
	}
	public static void main(String[] args){
		
		CompleteHACRes ch  = new CompleteHACRes();
//		ch.Convert(fpath+"Hac.csv", fpath+"finalHac.csv");
		ch.ConvertfinalHacToOriginal(fpath+"finalHac.csv", fpath+"finalHac2.csv");
		
		
	}
}
