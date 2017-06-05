package com.jiao.nexoSim;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.jiao.element.DisMAtrixAndIndex;
import com.jiao.element.EvaluationElement;
import com.jiao.file.FileReadAndWrite;
import com.jiao.hierarachy.AbstractMixedGraph;
import com.jiao.hierarachy.FindCorrespondingHierarchy;
import com.jiao.hierarachy.HierarcyBackForFindResults;

import java.util.Set;
import java.util.function.LongToDoubleFunction;

import javax.print.attribute.HashPrintServiceAttributeSet;

import org.apache.zookeeper.KeeperException.SystemErrorException;

import com.jiao.steinerTree.STResult;
//import java.util.LinkedHashMap;
//import com.jiao.element.DisMAtrixAndIndex;
//import com.jiao.hierarachy.AbstractMixedGraph;
//import com.jiao.hierarachy.FindCorrespondingHierarchy;
//import com.jiao.steinerTree.ShortestPathResult;
import com.jiao.steinerTree.ShortestPathResult;

import main.st_edge;
import main.mincut.edge;
import scala.Tuple2;

public class AllResultsCompare {
	//SP,ST,CST三个算法的所有结果的对比实验
	public static String path =  "/home/lee/biolg/ScaleFreeNetwork TestData/keys/" ;        //  "E://ScaleFreeNetwork TestData/keys/";
	static FileReadAndWrite fraw = new FileReadAndWrite();
	String Disfile = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/EdgeDisMatrix.csv"; // "E://ScaleFreeNetwork TestData/snbt/EdgeDisMatrix.csv";
	String Adjfile = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/AdjcentMatrix.csv";  // "E://ScaleFreeNetwork TestData/snbt/AdjcentMatrix.csv"; 
	String spath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";   ////  "E://ScaleFreeNetwork TestData/snbt/";
	ComputeSim cs =new ComputeSim();
	//System.out.println("helllooooo");
	public void Result(String fr,String resfr) {
//		System.out.println("here");
		FindCorrespondingHierarchy fch = new FindCorrespondingHierarchy(spath);
//		erya_threads erya_fch = new erya_threads(spath, "erya_threads....."); 
//		erya_searchTree  erya_st = new erya_searchTree("eyra_searchTree....");
		
		AbstractMixedGraph amg = new AbstractMixedGraph();
		String disfr = spath + "EdgeDisMatrix.csv";
		DisMAtrixAndIndex di = amg.ReadInDisMatrix(disfr);
//		LinkedHashMap<String, ArrayList<String>> rootAndpath  = new  LinkedHashMap<String, ArrayList<String>>();	
//		
		STResult st = new STResult();
		ShortestPathResult spr = new ShortestPathResult();
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
		
		int i=-1;///////
		ArrayList<Integer> cls_num = new ArrayList<Integer>();
		ArrayList<Integer> ran_num = new ArrayList<Integer>();
		
		long t1 = System.currentTimeMillis();
		
		ArrayList<String> key100 = new ArrayList<String>();
		ArrayList<Integer> maptime = new ArrayList<Integer>();
		ArrayList<Integer> rdctime = new ArrayList<Integer>();
		ArrayList<Integer> add_node_num = new ArrayList<Integer>();
		ArrayList<Integer> tree_wgt = new ArrayList<Integer>();
		
		for(ArrayList<String> key :allkeys){
//				System.out.println("key: "+key.size()+" "+key.getClass().toString()+" "+i+" "+key);
			fraw.WriteToFile(resfr+"keys.csv","keys-----:"+ key.toString());
//			//CST
//			fraw.WriteToFile(resfr, "CST------:");
			
			/////
			i+=1;
			if (i>49){
				break;
			}
		
			System.out.println("\n"+i + "\t"+key.size()+"\t"+key);
			
			long beg = System.currentTimeMillis(); /// // by lee			
/*			LinkedHashMap<String, ArrayList<String>> rootAndpath  = new  LinkedHashMap<String, ArrayList<String>>();
			long startTime1 = System.currentTimeMillis();
			
			       					old way, single thread			         
			HierarcyBackForFindResults rrrr = fch.CircleFindCluster(1, 100, key, "9973", fch.prmap, di.getDis(),di.getKeyi(),rootAndpath);		
//			System.out.println("now:\t\t"+rrrr.getClusterContainKey());
			cls_num.add(rrrr.getClusterContainKey().size());
			ran_num.add(rootAndpath.size());		
					
			Iterator iterator = rootAndpath.entrySet().iterator();
			System.out.println("rootandpath:");
			while (iterator.hasNext()){
				System.out.println(iterator.next());
			}
			System.out.println(" end!  " );
			long mid =  System.currentTimeMillis();
			System.out.println("time for map:" +(mid-beg));
			maptime.add((int) (mid-beg));
//			System.out.println("rootAndpath: "+rootAndpath.size()+" "+rootAndpath);
			ArrayList<String> all1 = fch.ConstructFinalTree(rootAndpath, di.getDis(), di.getKeyi(),fch.prmap);
			
//			System.out.println("res:\t"+all1.size()+"\t"+all1.get(0)+"\t"+all1 );
			HashSet<String> nnnn = new HashSet<String>();
			HashSet<st_edge> edges = new HashSet<st_edge>();
			for (String sa: all1){
				String[] sp = sa.split(",");
				edges.add( new st_edge(sp[0], sp[1], 1) );
				nnnn.add(sp[0]);
				nnnn.add(sp[1]);
			}
			ArrayList<Tuple2<Integer, Integer> > tjrs = ana_res(edges, key);
			System.out.println(tjrs);
//			edges_num.add( edges.size() );
			add_node_num.add( nnnn.size() - key.size() ); 
			tree_wgt.add( edges.size());
			
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
			System.out.println("time for merge :" +(System.currentTimeMillis()-mid));
			rdctime.add( (int) (System.currentTimeMillis() - mid) );*/
			
			//ST
//			fraw.WriteToFile(resfr, "ST-------:");			
			long startTime2 = System.currentTimeMillis();
			ArrayList<String> all2 =new ArrayList<String>();
			all2 = st.RunST(Disfile, Adjfile, key);
			long endTime2 = System.currentTimeMillis();
//			fraw.WriteToFile(resfr+"path.csv", "Edges:"+all2.toString());
			HashMap<String,Double> sim2 = cs.allpathSim(all2);
			EvaluationElement ee2 =  Evaluation(key, all2, sim2);
			String res = all2.size()+"\t"+(endTime2 - startTime2) + "ms"+"\t"+ee2.getR1()+"\t"+ee2.getR2()+"\t"+ee2.getS_t();
//			fraw.WriteToFile(resfr+"res.csv", res);
			Set<String> set2 =sim2.keySet();
			Iterator<String> it2 = set2.iterator();
//			fraw.WriteToFile(resfr+"AllSimlarity.csv", "Two Gene Simlarity:");
			while(it2.hasNext()){
				String s  = it2.next();
				double sim =sim2.get(s);
//				fraw.WriteToFile(resfr+"AllSimlarity.csv", s+":"+sim);
			}			
			rdctime.add((int) (endTime2-startTime2));
			HashSet<String> nnnn = new HashSet<String>();
			HashSet<st_edge> edges = new HashSet<st_edge>();
			for (String sa: all2){
				String[] sp = sa.split(",");
				edges.add( new st_edge(sp[0], sp[1], 1) );
				nnnn.add(sp[0]);
				nnnn.add(sp[1]);
			}
			add_node_num.add( nnnn.size() - key.size() ); 
			tree_wgt.add( edges.size());
			
			//ShortestPath
/*//			fraw.WriteToFile(resfr, "Shortest Path------:");
			HashMap<Integer, String> jkey = new HashMap<Integer, String>();
			HashMap<String, Integer> keyj = new HashMap<String, Integer>();
			long startTime3 = System.currentTimeMillis();
			ArrayList<String> all3 = new ArrayList<String>();
			all3 = spr.GetAllPathByKeysPair(Adjfile, jkey, keyj, key);
			long endTime3 = System.currentTimeMillis();
//			fraw.WriteToFile(resfr+"path.csv","Edges:"+ all3.toString());
			HashMap<String,Double> sim3 = cs.allpathSim(all3);
			EvaluationElement ee3 =  Evaluation(key, all3, sim3);
			String res = all3.size()+"\t"+(endTime3 - startTime3) + "ms"+"\t"+ee3.getR1()+"\t"+ee3.getR2()+"\t"+ee3.getS_t();
			
			rdctime.add((int) (endTime3 - startTime3) );
			HashSet<String> nnnn = new HashSet<String>();
			HashSet<st_edge> edges = new HashSet<st_edge>();
			for (String sa: all3){
				String[] sp = sa.split(",");
				edges.add( new st_edge(sp[0], sp[1], 1) );
				nnnn.add(sp[0]);
				nnnn.add(sp[1]);
			}
			add_node_num.add( nnnn.size() - key.size() ); 
			tree_wgt.add( edges.size());
			
			fraw.WriteToFile(resfr+"res.csv", res);
			Set<String> set3 =sim3.keySet();
			Iterator<String> it3 = set3.iterator();
			fraw.WriteToFile(resfr+"AllSimlarity.csv", "Two Gene Simlarity:");
			while(it3.hasNext()){
				String s  = it3.next();
				double sim =sim3.get(s);
				fraw.WriteToFile(resfr+"AllSimlarity.csv", s+":"+sim);
			}*/
			
			
		}
//		System.out.println(cls_num+" "+ran_num + "\nfinished!!!\t\t"+(System.currentTimeMillis()-t1));
		System.out.println( "map time: "+maptime);
		System.out.println("reduce time: "+rdctime );
		System.out.println( "add node num: "+add_node_num );
		System.out.println("tree weight: "+tree_wgt );

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
	
	public static  ArrayList<Tuple2<Integer, Integer> >  ana_res(HashSet<st_edge> edges, ArrayList<String> keys){
		ArrayList<st_edge> list_edges = new ArrayList<st_edge>(edges);
//		System.out.println("Analysis results: \nkeys: "+keys);
		ArrayList<Tuple2<Integer, Integer>> rtt = new ArrayList<>()  ;
		
		ArrayList<String> nodes = new ArrayList<String>();
		for (st_edge dg: list_edges){
			if (!nodes.contains(dg.beg)){
				nodes.add(dg.beg);
			}
			if (!nodes.contains(dg.end)){
				nodes.add(dg.end);
			}
		}
		int len = nodes.size();
//		System.out.printf("edges :  %d\tnodes : %d\n",list_edges.size(),len);
		int dis[][] = new int[len][len];
		int flg[] = new int[len];
		for (int i=0;i<len;i++){
			flg[i] = 0;
			for (int j=0;j<len;j++){
				dis[i][j] = 0;
			}
		}
		
		for (st_edge dg: list_edges){
			dis[nodes.indexOf(dg.beg)][nodes.indexOf(dg.end)] = dis[nodes.indexOf(dg.end)][nodes.indexOf(dg.beg)] = dg.weight;
			if (dg.weight==0){
				System.out.println("weight == 0 !!!");
			}
		}
		int num =0;
		
		while (num <len){
			String now = null;
			int pos = -1;
			for (int i=0;i<len;i++){
				if (flg[i]==0){
					now = nodes.get(i);
					flg[i] = 1;
					break;
				}			
			}
			
			Queue<String> qq = new LinkedList<String>();
			ArrayList<String> part = new ArrayList<String>();
			qq.add(now);
			while (!qq.isEmpty()){
				now = qq.poll();
				part.add(now);
				num += 1;
				pos = nodes.indexOf(now);
				for (int i=0;i<len;i++){
					if (flg[i] ==0 && dis[pos][i]>0){
						qq.add(nodes.get(i));
						flg[i] = 1;
					}
				}		
			}
			int count = 0;
			for (String sa: part){
				if (keys.contains(sa)){
					count++;
				}
			}
			if (count>0){
				rtt.add(new Tuple2<Integer, Integer>(part.size(), count));
			}			
//			System.out.println("part : "+part.size()+"\tkeys num: "+count);
//			System.out.println(part);
		}
		
		
		
		return rtt;
		
	}
	public static void main(String[] args){
		long tm1 = System.currentTimeMillis();
		AllResultsCompare arc = new AllResultsCompare();
		String input_file = path+"set_one.csv";
		input_file = path + "/set_four/10.csv";
		arc.Result(input_file,path+"result/ST/set_one/");		
		System.out.println( "\n\nRunning for "+(System.currentTimeMillis()-tm1) +" ms!!!"   );
		
	}
}
