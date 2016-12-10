package com.jiao.nexoSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.jiao.element.DisMAtrixAndIndex;
import com.jiao.element.EvaluationElement;
import com.jiao.file.FileReadAndWrite;
import com.jiao.hierarachy.AbstractMixedGraph;
import com.jiao.hierarachy.FindCorrespondingHierarchy;
import com.jiao.hierarachy.HierarcyBackForFindResults;

import java.util.Set;
import com.jiao.steinerTree.STResult;
//import java.util.LinkedHashMap;
//import com.jiao.element.DisMAtrixAndIndex;
//import com.jiao.hierarachy.AbstractMixedGraph;
//import com.jiao.hierarachy.FindCorrespondingHierarchy;
//import com.jiao.steinerTree.ShortestPathResult;

public class AllResultsCompare {
	//SP,ST,CST三个算法的所有结果的对比实验
	public static String path =  "/home/lee/biolg/ScaleFreeNetwork TestData/keys/" ;        //  "E://ScaleFreeNetwork TestData/keys/";
	static FileReadAndWrite fraw = new FileReadAndWrite();
	String Disfile =         "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/EdgeDisMatrix.csv"; // "E://ScaleFreeNetwork TestData/snbt/EdgeDisMatrix.csv";
	String Adjfile = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/AdjcentMatrix.csv";  // "E://ScaleFreeNetwork TestData/snbt/AdjcentMatrix.csv"; 
	String spath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";   ////  "E://ScaleFreeNetwork TestData/snbt/";
	ComputeSim cs =new ComputeSim();
	//System.out.println("helllooooo");
	public void Result(String fr,String resfr) {
//		System.out.println("here");
		FindCorrespondingHierarchy fch = new FindCorrespondingHierarchy(spath);
		erya_threads erya_fch = new erya_threads(spath, "erya_threads....."); 
		
		AbstractMixedGraph amg = new AbstractMixedGraph();
		String disfr = spath + "EdgeDisMatrix.csv";
		DisMAtrixAndIndex di = amg.ReadInDisMatrix(disfr);
//		LinkedHashMap<String, ArrayList<String>> rootAndpath  = new  LinkedHashMap<String, ArrayList<String>>();	
//		
		STResult st = new STResult();
//		ShortestPathResult spr = new ShortestPathResult();
		ArrayList<String> read = new ArrayList<String>();
		read = fraw.ReadData(fr);
		ArrayList<ArrayList<String>> allkeys = new ArrayList<ArrayList<String>>();
		
		for(String s:read){
			ArrayList<String> subkeys = new ArrayList<String>();
			String[] ss = s.split(",");
			for(int i = 0; i<ss.length;i++){
				subkeys.add(ss[i]);		
			}
			allkeys.add(subkeys);
		}
//		System.out.println("allkeys: "+allkeys.size());   ///////   50
		
		int i=0;///////
		ArrayList<Integer> cls_num = new ArrayList<Integer>();
		ArrayList<Integer> ran_num = new ArrayList<Integer>();
		
		for(ArrayList<String> key :allkeys){
			if(allkeys.indexOf(key) >=27){
//				System.out.println("key: "+key.size()+" "+key.getClass().toString()+" "+i+" "+key);
			fraw.WriteToFile(resfr+"keys.csv","keys-----:"+ key.toString());
//			//CST
//			fraw.WriteToFile(resfr, "CST------:");
			
			/////
			i+=1;
			if (i>5){
				break;
			}
			System.out.println(i + "\t"+key);
			
			long beg = System.currentTimeMillis(); /// // by lee
			
			//////
			
			LinkedHashMap<String, ArrayList<String>> rootAndpath  = new  LinkedHashMap<String, ArrayList<String>>();
			long startTime1 = System.currentTimeMillis();
			////
//			System.out.println(fch.prmap.size());
//			System.out.println(di.getDis().size());
			
/*			HierarcyBackForFindResults rrrr = fch.CircleFindCluster(1, 100, key, "9973", fch.prmap, di.getDis(),di.getKeyi(),rootAndpath);		
//			System.out.println("now:\t\t"+rrrr.getClusterContainKey());
			cls_num.add(rrrr.getClusterContainKey().size());
			ran_num.add(rootAndpath.size());*/
			
			
			
			threads_data rs = erya_fch.run(1, 100, key, "9973", fch.prmap, di.getDis(),di.getKeyi(),rootAndpath);
			
			HashMap<String, ArrayList<String>> map = rs.map;
			Set<String> set = map.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				Object key2 = it.next();
				ArrayList<String> values = (ArrayList<String>) map.get(key2);
				if (values.size() >= 3) {
					erya_fch.run(rs.begin,rs. end, values, (String)key2, rs.pr,di.getDis()
							, rs.keyi, rootAndpath);
				}
			}		

			
			
//			System.out.println("rootAndpath: "+rootAndpath.size()+" "+rootAndpath);
			ArrayList<String> all1 = fch.ConstructFinalTree(rootAndpath, di.getDis(), di.getKeyi(),fch.prmap);
			
			long endTime1 = System.currentTimeMillis();
			fraw.WriteToFile(resfr+"path.csv", "Edges:"+all1.toString());
			double cost = fch.Cost(all1, di.getDis(), di.getKeyi());
//			System.out.println("hahha "+cost);
			
			HashMap<String,Double> sim1 = cs.allpathSim(all1);
//			System.out.println(all1.size()+" "+sim1.size());
			EvaluationElement ee1 =  Evaluation(key, all1, sim1);
			String res = cost+"\t"+(endTime1 - startTime1) + "ms"+"\t"+ee1.getR1()+"\t"+ee1.getR2()+"\t"+ee1.getS_t();
			fraw.WriteToFile(resfr+"res.csv", res);
			Set<String> set1 =sim1.keySet();
			Iterator<String> it1 = set1.iterator();
			fraw.WriteToFile(resfr+"AllSimlarity.csv", "Two Gene Simlarity:");
			while(it1.hasNext()){
				String s  = it1.next();
				double sim =sim1.get(s);
				fraw.WriteToFile(resfr+"AllSimlarity.csv", s+":"+sim);
			}
			System.out.println(System.currentTimeMillis()-beg + " ms");
			
			
//			//ST
//			fraw.WriteToFile(resfr, "ST-------:");
			
//			long startTime2 = System.currentTimeMillis();
//			ArrayList<String> all2 =new ArrayList<String>();
//			all2 = st.RunST(Disfile, Adjfile, key);
//			long endTime2 = System.currentTimeMillis();
//			fraw.WriteToFile(resfr+"path.csv", "Edges:"+all2.toString());
//			HashMap<String,Double> sim2 = cs.allpathSim(all2);
//			EvaluationElement ee2 =  Evaluation(key, all2, sim2);
//			String res = all2.size()+"\t"+(endTime2 - startTime2) + "ms"+"\t"+ee2.getR1()+"\t"+ee2.getR2()+"\t"+ee2.getS_t();
//			fraw.WriteToFile(resfr+"res.csv", res);
//			Set<String> set2 =sim2.keySet();
//			Iterator<String> it2 = set2.iterator();
//			fraw.WriteToFile(resfr+"AllSimlarity.csv", "Two Gene Simlarity:");
//			while(it2.hasNext()){
//				String s  = it2.next();
//				double sim =sim2.get(s);
//				fraw.WriteToFile(resfr+"AllSimlarity.csv", s+":"+sim);
//			}			

			
			//ShortestPath
//			fraw.WriteToFile(resfr, "Shortest Path------:");
//			HashMap<Integer, String> jkey = new HashMap<Integer, String>();
//			HashMap<String, Integer> keyj = new HashMap<String, Integer>();
//			long startTime3 = System.currentTimeMillis();
//			ArrayList<String> all3 = new ArrayList<String>();
//			all3 = spr.GetAllPathByKeysPair(Adjfile, jkey, keyj, key);
//			long endTime3 = System.currentTimeMillis();
//			fraw.WriteToFile(resfr+"path.csv","Edges:"+ all3.toString());
//			HashMap<String,Double> sim3 = cs.allpathSim(all3);
//			EvaluationElement ee3 =  Evaluation(key, all3, sim3);
//			String res = all3.size()+"\t"+(endTime3 - startTime3) + "ms"+"\t"+ee3.getR1()+"\t"+ee3.getR2()+"\t"+ee3.getS_t();
//			fraw.WriteToFile(resfr+"res.csv", res);
//			Set<String> set3 =sim3.keySet();
//			Iterator<String> it3 = set3.iterator();
//			fraw.WriteToFile(resfr+"AllSimlarity.csv", "Two Gene Simlarity:");
//			while(it3.hasNext()){
//				String s  = it3.next();
//				double sim =sim3.get(s);
//				fraw.WriteToFile(resfr+"AllSimlarity.csv", s+":"+sim);
//			}
			
			
			}
		}
		System.out.println(cls_num+" "+ran_num + "\nfinished!!!");

	}
	
	public EvaluationElement Evaluation(ArrayList<String> keys,ArrayList<String> path,HashMap<String,Double> sim){
		
		EvaluationElement ee =new EvaluationElement();
		ArrayList<String> internodes = new ArrayList<String>();
		ArrayList<String> N_inter_inter = new ArrayList<String>();
		ArrayList<String> N_inter_key = new ArrayList<String>();
		double sumsim = 0;
		for(String pa:path){
			String[] ss = pa.split(",");
			String sou = ss[0];
			String tar = ss[1];
			if(!keys.contains(sou)&&!keys.contains(tar)){
				N_inter_inter.add(pa);
				internodes.add(sou);
				internodes.add(tar);
			}
			if(!keys.contains(sou)&&keys.contains(tar)){
				N_inter_key.add(pa);
				internodes.add(sou);
				sumsim += sim.get(pa);
			}
			if(keys.contains(sou)&&!keys.contains(tar)){
				N_inter_key.add(pa);
				internodes.add(tar);
				sumsim += sim.get(pa);
			}
		}
		
		double R1 = 0;
		double R2 = 0;
		double s_t = 0;
		//IC还没有计算
//		System.out.println("N_inter_inter.size()"+N_inter_inter.size());
//		System.out.println("N_inter_key.size()"+N_inter_key.size());
//		System.out.println("Sumsim:"+(double)sumsim);
		
		R1 = (double) internodes.size()/(double) keys.size();
		R2 = (double) N_inter_inter.size()/(double) N_inter_key.size();
		s_t = (double) 1/(double)N_inter_key.size()*sumsim;
		ee.setR1(R1);
		ee.setR2(R2);
		ee.setS_t(s_t);
		return ee;

	}
	public static void main(String[] args){
		AllResultsCompare arc = new AllResultsCompare();
		arc.Result(path+"set_one.csv",path+"result/ST/set_one/");		
	}
}
