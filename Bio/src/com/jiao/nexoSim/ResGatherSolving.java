package com.jiao.nexoSim;

import java.util.ArrayList;

import com.jiao.file.FileReadAndWrite;

public class ResGatherSolving {
	//把所有单独生成的结果汇总到一个Excel表
	
	static String path = "/home/lee/biolg/ScaleFreeNetwork TestData/keys/result/";
	static FileReadAndWrite fraw = new FileReadAndWrite();
	public void ResGather(String frp,String fw){
		ArrayList<String> Cost = fraw.ReadData(frp+"cost.csv");
		ArrayList<String> Runningtime = fraw.ReadData(frp+"running time.csv");
		ArrayList<String> R1 =fraw.ReadData(frp+"R1.csv");
		ArrayList<String> R2 =fraw.ReadData(frp+"R2.csv");
		ArrayList<String> St = fraw.ReadData(frp+"St.csv");
		for(int i = 0; i <Cost.size();i++){
			String cost = Cost.get(i).split(":")[1];
			String rt = Runningtime.get(i).split(":=======")[1];
			String r1 = R1.get(i).split(":")[1];
			String r2 = R2.get(i).split(":")[1];
			String st = St.get(i).split(":")[1];
			String s = cost+"\t"+rt+"\t"+r1+"\t"+r2+"\t"+st;
			fraw.WriteToFile(fw, s);		
		}
		
	}
	public static  void  main(String[] args){
		ResGatherSolving rg = new ResGatherSolving();
		rg.ResGather(path+"ST/set_three/", path+"ST/set_three/res.csv");	
	}
	
	

}
