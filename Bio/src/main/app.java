package main;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.ml.util.LocalStopwatch;
import org.apache.zookeeper.KeeperException.SystemErrorException;
import org.stringtemplate.v4.compiler.CodeGenerator.includeExpr_return;
import org.stringtemplate.v4.compiler.STParser.andConditional_return;
import org.apache.spark.api.java.AbstractJavaRDDLike;
import org.apache.spark.api.java.JavaRDD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.jdo.identity.IntIdentity;
import javax.validation.constraints.Min;

import org.apache.calcite.util.Static;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.hadoop.hive.metastore.api.ThriftHiveMetastore.Processor.get_partition_with_auth;
import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.intervalLiteral_return;
import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.stringLiteralSequence_return;
import org.apache.hadoop.yarn.client.api.impl.AHSClientImpl;
import org.apache.spark.SparkConf;

import com.jiao.file.FileReadAndWrite;
import com.jiao.hierarachy.HierarchyMapSerializable;
import com.jiao.nexoSim.*;
import com.mysql.fabric.xmlrpc.base.Array;

import antlr.collections.Stack;
import scala.Tuple2;
//import com.jiao.element.cluster_data;
import scala.annotation.implicitNotFound;
import scala.annotation.meta.field;
import scala.annotation.meta.param;
import main.cluster_data;

public class app    {
	
//	static String spath = "file://home/lee/biolg/ScaleFreeNetwork TestData/snbt/";   ////  "E://ScaleFreeNetwork TestData/snbt/";
	static String spath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";
	public static String path =  "/home/lee/biolg/ScaleFreeNetwork TestData/keys/" ;    
	public static HierarchyMapSerializable ms1; 
	public static HashMap<String, ArrayList<String>> entleaf;      ////////// every clusters' nodes(keywords set)
	static final int large = 10000;
	static FileReadAndWrite fraw = new FileReadAndWrite();
	
	public static cluster_data getDataAction(String path, ArrayList<String> nodes) throws IOException
	{		
		
		int dis[][] = new int[nodes.size()][nodes.size()];		
		//////// initlize
		for (int i=0;i<nodes.size();i++){
			for (int j=0;j<nodes.size();j++){
				dis[i][j] = 0;
			}
		}
//		System.out.println(nodes);		
		int i=0,j=0;
		BufferedReader br = new BufferedReader(new FileReader(path));
        // 读取直到最后一行 
        String line = "";         
        ArrayList<String> allnodes = new ArrayList<String>();        
        while ((line = br.readLine()) != null) { 
            // 把一行数据分割成多个字段
        	i += 1;
            StringTokenizer st = new StringTokenizer(line, "\t");      
            if (i==1){
//            	System.out.println(st.countTokens());
            	while (st.hasMoreTokens()){
//            		System.out.println(st.nextToken());
            		allnodes.add(st.nextToken());
            	}
            	continue;
            }
            String s =  st.nextToken();
        	if (nodes.contains(s)==false){
        		continue;
        	}
        	ArrayList<String> la = new ArrayList<String>();
            while (st.hasMoreTokens()) { 
                // 每一行的多个字段用TAB隔开表示 
//            	System.out.print(st.nextToken()+" ");
            	la.add(st.nextToken() );        	
            }
            int k = nodes.indexOf(s);
            for (j=0;j<nodes.size();j++){
            	dis[j][k] = dis[k][j] = Integer.parseInt( la.get(allnodes.indexOf( nodes.get(j) )) )  ;
            }           
        } 
        br.close();
        
        ////////////////   print dis  matrix   /////////
/*        for (i=0;i<nodes.size();i++){
        	for (j=0;j<nodes.size();j++){
        		System.out.print(dis[i][j]+" ");
        	}
        	System.out.println();
        }	*/
        cluster_data res = new cluster_data(dis,nodes);
		res.setDis(dis);
		res.setnodes(nodes);		
		return res;
//        return dis;
	}
	
	public static  Tuple2<int[][],int[][]>   flod(int[][] dis){
		int lg = dis.length;
//		System.out.println("res:");
		int[][] res = new int[lg][lg];
		int[][] path= new int[lg][lg];
		for (int i=0;i<lg;i++){
			for (int j=0;j<lg;j++){
				path[i][j] = -1;
				if (dis[i][j]==0 && i!=j){
					res[i][j] = large;
				}
				if (dis[i][j]!=0){
					res[i][j] = dis[i][j];
				}
//				System.out.print(res[i][j]+" ");
			}
//			System.out.println();
		}		
		for (int k=0;k<lg;k++) {
			for (int j=0;j<lg;j++){
				for (int i=0;i<lg;i++){
					if (res[i][j]>res[i][k]+res[k][j]){
						res[i][j] = res[i][k]+res[k][j];
						path[i][j] =  k;
					}					
				}
			}
		}
		Tuple2<int[][], int[][]> tp_res = new Tuple2<int[][], int[][]>(res, path);
		return tp_res;
	}
	
	public static void  getpath(int[][] path, int beg, int end,  ArrayList<Integer> ways){ 
		///////////   middle nodes in the path, not including start and end nodes    //////
//		System.out.println(beg+" "+end+" : "+path[beg][end]);
		if (path[beg][end]==-1 ){
			return;
		}
		
		getpath(path, beg, path[beg][end], ways);
		ways.add(Integer.valueOf(path[beg][end]));		
		getpath(path, path[beg][end], end, ways);
	}
	
	
	public static ArrayList<Integer> getpath_nonrecursive(int[][] path, int beg, int end){
		ArrayList<Integer> pnodes = new ArrayList<Integer>();
		if (path[beg][end]==-1){			
			return pnodes;
		}				
		java.util.Stack st = new java.util.Stack();	
		st.push(end);
//		st.push(path[beg][end]);
		
		int val= path[beg][end],kk=beg;
		
		while(st.isEmpty()==false){
//			System.out.println("beg: "+beg + " val:"+val);
			while (path[beg][val]!=-1){
				st.push(path[beg][val]);
				val = path[beg][val];				
			}	
//			val = beg;				
			if (!pnodes.contains(Integer.valueOf(val))){
				pnodes.add(Integer.valueOf(val));
			}		
			if (st.isEmpty()==false){
				kk = (int) st.pop();
		
			}else{
				continue;
			}
//			System.out.println("val:\t"+val+"\t kk: "+kk);
			if  (path[val][kk]!=-1){		
				st.push(kk);
				st.push(path[val][kk]);
				st.push(val);
				beg = val;
				val = path[val][kk];
//				val = (int) st.pop();					
			}
		

		}
		
		return pnodes;
	}
		
	
	public static HashSet<st_edge> get_st_f_ways(ArrayList<Integer> way_nodes,int beg,int end,int[][] floyd, ArrayList<String> nodes){
		HashSet<st_edge> rEdges = new HashSet<>();
		for (int i=0;i<way_nodes.size();i++){
			rEdges.add(new st_edge(nodes.get(beg),nodes.get((int)way_nodes.get(i)), floyd[beg][(int)way_nodes.get(i)]));
			beg = (int)way_nodes.get(i);
		}
		rEdges.add(new st_edge(nodes.get(beg),nodes.get(end), floyd[beg][end]));
		return rEdges;
	}
	
	
	public static steiner_tree getlocalst( cluster_data cst, ArrayList<String> keys)
	{
//		Tuple2<Integer, ArrayList<String>> res = null;
		ArrayList<Integer> keysint = new ArrayList<>() ;
		int[][] dis = cst.getDis();
		ArrayList<String> nodes = cst.getnodes();
		for (int i=0;i<keys.size();i++){
			keysint.add(nodes.indexOf(keys.get(i)));
		}
//		System.out.println("getlocalst:\n"+keys);
		HashMap<Tuple2<HashSet<String>, Integer> , Integer> data = new HashMap<>() ; ///// sub set of keys,   its shortest path
		HashMap<Tuple2<HashSet<String>, Integer> , HashSet<st_edge>> st_edges = new HashMap<>(); /////// including edges
		ArrayList<HashSet<String>> subsets = getallsubsets(new HashSet<String>(keys),1); ////// all non-empty sebsets of keys		
		Tuple2<int[][], int[][]> fld_res = flod(dis); //////// shortest path by floyd algorithm
		int[][] floyd =  fld_res._1;
		int[][] floyd_path = fld_res._2;
		
/*		System.out.println("getlocalst:\n");
		for (int i=0;i<floyd.length;i++){
			for (int j=0;j<floyd.length;j++){
				System.out.print(dis[i][j]+" ");
			}
			System.out.println();
		}*/
		////// initnial  1 key, with
		for (HashSet<String> a: subsets){
			if (a.size()==1){				
				for (String b:a){
					for (int i=0;i<nodes.size();i++){
						Tuple2<HashSet<String>, Integer> tuple2 = new Tuple2<HashSet<String>, Integer>(a, i); // = Tuple2<a, 1>;				
						data.put( tuple2,floyd[nodes.indexOf(b)][i]);    ////////  put the shortest path
						/////// without start and end nodes
						ArrayList<Integer> way_nodes = getpath_nonrecursive(floyd_path, i, nodes.indexOf(b));
//						System.out.println("way nodes:\n"+way_nodes);			
						//////////   this way to store the steiner tree ?? really the best way ??
						//////// 3 ways:  linkedlist,  class tree,  hashset.     for merge, hashset is applied.
						HashSet<st_edge> s_tree = get_st_f_ways(way_nodes, i, nodes.indexOf(b), floyd, nodes);
/*						System.out.println("waynodes:\t"+way_nodes+"\tbeg: "+i+"\tend: "+nodes.indexOf(b));
						for (st_edge elEdge: s_tree){
							elEdge.st_output();
						}	*/										
						st_edges.put(tuple2, s_tree);      /////////  put the tree edges 
					}
				}		
			}
		}
		for (int i =2;i<=keys.size();i++){
			for (HashSet<String> a:  subsets  ){
				if (a.size()!=i){
					continue;
				}
				for (Integer k=0;k< new Integer(nodes.size()) ;k++){					
					ArrayList<HashSet<String>> arrayList = getallsubsets(a, 2);
					Integer value = large;
					HashSet<st_edge> edges = new HashSet<>();
					for (HashSet<String> aHashSet:arrayList){
						HashSet<String> inters = getintersection(aHashSet, a);
						Tuple2<HashSet<String>, Integer>  t1 = new Tuple2<HashSet<String>, Integer>(aHashSet, k);
						Tuple2<HashSet<String>, Integer>  t2 = new Tuple2<HashSet<String>, Integer>(inters, k);
						if (data.containsKey(t1) && data.containsKey(t2)){
							Integer va = data.get(t1) + data.get(t2);
							///////// some bugs need to repair ; edges both include 
							if ( value > va ){
								value = va;
								edges.clear();
								edges.addAll(st_edges.get(t1));
								edges.addAll(st_edges.get(t2));
							}
						}
					}
					Tuple2<HashSet<String>, Integer> t3 = new Tuple2<HashSet<String>, Integer>(a, k);
					data.put(t3, value);  //////////////////
					st_edges.put(t3, edges  );
					
				}
				for (Integer k=0;k<nodes.size();k++){
					Tuple2<HashSet<String>, Integer> t4 = new Tuple2<HashSet<String>, Integer>(a, k);
					int value = data.get(t4);
					HashSet<st_edge> edges = new HashSet<>();
					for (int j = 0;j<nodes.size();j++){
						if (value > data.get(t4)+dis[k][j] ){
							value = data.get(t4)+dis[k][j] ;
							edges.clear();
							edges.addAll(st_edges.get(t4));
							edges.add(new st_edge(nodes.get(k), nodes.get(j), dis[k][j]));
						}						
					}
					data.put(t4, value);    //////////////
					st_edges.put(t4, edges);   //////////////////			
				}
	
			}	
		}	
		
		HashSet<String> allkeys = new HashSet<>();
		allkeys.addAll(keys);
		Tuple2<HashSet<String>, Integer> t5 = new Tuple2<HashSet<String>, Integer>(allkeys, 0);
		Integer va = data.get(t5);
		Integer n = 0;
		for (Integer k=1;k < (nodes.size());k++){
			Tuple2<HashSet<String>, Integer> t6 = new Tuple2<HashSet<String>, Integer>(allkeys, k);
			if (data.containsKey(t6)==false){
				continue;
			}
			if (data.get(t6)  < va){
				va = data.get(t6);
				n = k;
			}
		}
		Tuple2<HashSet<String>, Integer> t7 = new Tuple2<HashSet<String>, Integer>(allkeys, n);
//		res = new Tuple2<Integer, ArrayList<String>>(va , keys  );		
		
		int wgl =0;
		String string = "0";
		HashSet<st_edge>  ddd = st_edges.get(t7);
		for (st_edge ssa: ddd){
			wgl += ssa.weight;
		}			
		
		steiner_tree res = new steiner_tree(string, keys, nodes, wgl, ddd);	
		return res;
	}
	
	///////////// get the intersection
	public static HashSet<String> getintersection(HashSet<String> part, HashSet<String> whole ){
		HashSet<String> reSet = new HashSet<>();
		Iterator<String> iterator = whole.iterator();
		while(iterator.hasNext()){
			String string = iterator.next();
			if (part.contains(string)==false){
				reSet.add(string);
			}
		}
		return reSet;
	}
	
	///////// 
	public static ArrayList<HashSet<String>> getallsubsets(HashSet<String> hSet, int flg){
		//////// flg, parameter, 0, all subsets; 1, no empty subset; 2, no empty, no all subsets
		ArrayList<HashSet<String>> reSets = new ArrayList<>();
		ArrayList<String> list = new ArrayList<String>(hSet);
		for (int i=1;i< Math.pow(2,list.size()) ;i++){
			String sa = Integer.toBinaryString(i);
			HashSet<String> ha = new HashSet<>() ;
			for (int j=0;j< sa.length()  ;j++){
				if (sa.charAt(j)=='1'){
					ha.add(list.get(j));
				}				
			}
			reSets.add(ha);
		}
		if (flg==1){
			reSets.add(hSet );
		}
		if (flg == 0){
			reSets.add(new HashSet<String>());
			reSets.add(hSet);
		}
		
		return reSets;	
	}
	
	public static steiner_tree merge_localst( List<steiner_tree> local_Sts, int[][] com_father,int[][] com_level,
			HashMap<String, ArrayList<String>> subgra, ArrayList<String> allkeys) throws IOException{
		
		System.out.println("allkeys: "+allkeys.size()+"\t"+allkeys);		
		HashMap<Integer,HashSet<Integer>> level_clusters = new HashMap();
		HashSet<Integer> leves = new HashSet<>();
		HashMap<Integer, HashSet<Integer>> clu_stru = new HashMap<>();
		HashMap<Integer, Integer> clu_father = new HashMap<>();
		HashMap<Integer, HashSet<Integer>> clu_child = new HashMap<>();
		int root_cluster=-1;
		
		int all_level = large, m=0 ,n=0; ////// the lowest level for all keys father		
//		System.out.println("com_level:");
		for (int i=0;i<com_level.length;i++){
			for (int j=0;j<com_level.length;j++){
//				System.out.print(com_level[i][j]+" ");
				if (i!=j){
					if (all_level>com_level[i][j]){
						all_level = com_level[i][j];
						m = i; n = j;
					}					
					leves.add(com_level[i][j]);
					if (level_clusters.containsKey(com_level[i][j])){
						HashSet<Integer> reSet = level_clusters.get(com_level[i][j]);
						reSet.add(com_father[i][j]);
						level_clusters.put(com_level[i][j], reSet);
					}else{
						HashSet<Integer> reSet = new HashSet<>(com_father[i][j]);
						level_clusters.put(com_level[i][j], reSet);
					}					
				}
			}
//			System.out.println();
		}
//		System.out.println("all_level: "+ all_level +" "+ m +" "+n + " "+ com_father[m][n] );
		root_cluster = com_father[m][n];
//		System.out.println("level_clusters:\t"+level_clusters+" "+leves+"\n"+subgra);
		ArrayList<Integer> lvls_gw = new ArrayList<>(leves);
		lvls_gw.sort(null);
		

		for (String a: subgra.keySet()){
			clu_stru.put(Integer.valueOf(a), new HashSet<Integer>());	
		} /////////   add the original sets   //////
		for (int i=lvls_gw.size()-1;i>=0;i--){
			for (Integer  clus: level_clusters.get(lvls_gw.get(i))){
				HashSet<Integer> lsas = new HashSet<>();
				for (Integer a: clu_stru.keySet()){
					if (entleaf.get(clus+"").containsAll(entleaf.get(a+""))   ){ /////  &&
						if (a.equals(clus)==false){
							lsas.add(a);
						}						
					}
				}
				clu_stru.put(clus, lsas);					
			}			
		}		
		System.out.println("clu_stru:\t"+clu_stru);
		
		Set<String> skes =  subgra.keySet();
		System.out.println(skes.size());
		for (Integer a: clu_stru.keySet()){
			Integer bInteger = null;
			int num = large;
			for (Integer c: clu_stru.keySet()){
				if (clu_stru.get(c).contains(a) && clu_stru.get(c).size()<num  ){
					bInteger = c;
					num = clu_stru.get(c).size();
				}
			}
			if ( bInteger != null &&   skes.contains(bInteger.intValue()+"")){
				continue;
			}
			clu_father.put(a, bInteger);		
//			HashSet<Integer> childs = new HashSet<>();
			if (bInteger==null){
				continue;
			}
			if (clu_child.containsKey(bInteger)){
				HashSet<Integer> childs = clu_child.get(bInteger);
				childs.add(a);
				clu_child.put(bInteger, childs);
			}else{
				HashSet<Integer> childs = new HashSet<>();
				childs.add(a);
				clu_child.put(bInteger, childs);
			}
			
		}
		System.out.println(clu_father+"\n"+clu_child);
		
		HashMap<Integer, steiner_tree> clu_lst = new HashMap<>();  ///////// cluster , local st
		for (steiner_tree steiner_tree: local_Sts){
			clu_lst.put( Integer.valueOf(steiner_tree.cluster_number) , steiner_tree);
		}
//		System.out.println(clu_lst.size());
		
		Set<Integer> not_con = clu_child.keySet();
		HashSet<Integer> records = new HashSet<>();
		for (String string : skes){
			records.add(Integer.valueOf(string));
		}
		
		steiner_tree res=null;
		
		while (records.size()>0){
			for (Integer father: not_con){
				HashSet<Integer> childs = clu_child.get(father);
				if ( records.containsAll(childs)==true && records.contains(father)==false){
					System.out.println( "father: "+father+"\tchilds: "+childs  );
					String disfr = spath + "EdgeDisMatrix.csv";
					cluster_data cluster_allkeys = getDataAction( disfr, entleaf.get(father+"") );		
					Object[] clds = childs.toArray();
					
					steiner_tree  tree = merge_nodes(clu_lst.get(clds[0]) , clu_lst.get(clds[1]), cluster_allkeys  );
					tree.setclunum(father+"");
					
					clu_lst.put(father, tree);
					records.add(father);
					
					if (father.equals(root_cluster)){
						res = tree;
					}
				}
			}
			if (records.containsAll(not_con)){
				break;
			}
		}

				
		return res;		
	}

	public static steiner_tree  merge_nodes( steiner_tree ta, steiner_tree tb, cluster_data clu ){
		ArrayList<String> ta_keys = ta.keys;
		ArrayList<String> tb_keys = tb.keys;
		ArrayList<String> nodes = clu.getnodes();
		int[][] dis = clu.getDis();
		String takeys = "aaakeys";
		String tbkeys="bbbkeys";
		
		int num = nodes.size() - ta_keys.size() - tb_keys.size() +2;
		System.out.println("keys: "+ta_keys + "\t"+tb_keys+"\t nodes:"+nodes);
		System.out.println(" new matrix size:"+num);
		
		int pos = 0;
		int a=-1,b=-1;
		HashMap<String, Integer> nw_index = new HashMap<>();
		String[] nw_nods = new  String[num];
		for (int i=0;i<nodes.size();i++){
			if (ta_keys.contains(nodes.get(i) )==false && tb_keys.contains(nodes.get(i))==false  ){
				 nw_index.put(nodes.get(i), pos++);
				 nw_nods[pos-1] = nodes.get(i);
			}
			if (ta_keys.contains(nodes.get(i) ) ){
				if (a==-1){
					a = pos ++;
				}
				 nw_index.put(takeys, a);
				 nw_nods[a] = takeys;
			}
			if (tb_keys.contains(nodes.get(i) ) ){
				if (b==-1){
					b = pos ++;
				}
				 nw_index.put(tbkeys, b);
				 nw_nods[b] = tbkeys;
			}	
		}
		
		System.out.println("nw index:\t"+nw_index);
		
		int[][] nw_dis = new int[num][num];
		for (int i=0;i<num;i++){
			for (int j=0;j<num;j++){
				nw_dis[i][j] = large;
			}
		}
		
		int wi=-1,wj=-1;
		for (int i=0;i<nodes.size();i++){
			if (ta_keys.contains(nodes.get(i) )==false && tb_keys.contains(nodes.get(i))==false  ){
				wi = nw_index.get(nodes.get(i));
			}else if (ta_keys.contains(nodes.get(i) ) ){				
				wi = nw_index.get(takeys);
			}else {
				wi = nw_index.get(tbkeys);
			}	
			for (int j=0;j<nodes.size();j++){				
				if (ta_keys.contains(nodes.get(j) )==false && tb_keys.contains(nodes.get(j))==false  ){
					wj= nw_index.get(nodes.get(j));
				}else if (ta_keys.contains(nodes.get(j) ) ){				
					wj = nw_index.get(takeys);
				}else {
					wj = nw_index.get(tbkeys);
				}
				if (wi==wj){
					nw_dis[wi][wj] = 0;
					continue;
				}
				if (dis[i][j]!=0 ){
					if (nw_dis[wi][wj]!=0 && nw_dis[wi][wj]<dis[i][j]){
						continue;
					}nw_dis[wi][wj] = dis[i][j] ;
				}
			}
		}		
		
/*		for (int i=0;i<num;i++){
			for (int j=0;j<num;j++){
				System.out.print(nw_dis[i][j]+" "); 
			}System.out.println();
		}*/
		////////////////////       dijkstra algorithm           //////////////////// nw_index
		int[] dj = new int[num], visited = new int[num], pvs = new int[num];
		int  vsd =0, start = nw_index.get(takeys), end = nw_index.get(tbkeys);
		for (int i=0;i<num;i++){
			dj[i] = large;
			visited[i] = 0; 
			pvs[i] = -1;
		}
		dj[start] = 0;
		visited[start] = 1;
		vsd += 1;
		do {
			for (int i=0;i<num;i++){
				if (visited[i]==0){
					dj[i] = nw_dis[start][i];
					for (int j=0;j<num;j++){
						if (visited[j]==1){
							if ( dj[i]>dj[j]+nw_dis[j][i]  ){
								dj [i] = dj[j]+nw_dis[j][i];
								pvs[i] = j;
							}
						}
					}
					visited[i] = 1;
					vsd += 1;
				}
			}

		}while(vsd<num);
		System.out.println("start: "+start+"\tend: "+end+"\tlength: "  + dj[end]+" "+nw_dis[start][end]);
		///////////         way nodes, from end to start       //////////////
		ArrayList<Integer> way_nodes = new ArrayList<>();
		vsd = end;
		while(vsd!=-1){
//			System.out.println("vsd: "+vsd);
			way_nodes.add(vsd);
			vsd = pvs[vsd];
		}
//		System.out.println(start);
		way_nodes.add(start);		
		
		System.out.println("way nodes: "+way_nodes);
		int bg,tl,wg;
		HashSet<st_edge> new_edges = new HashSet<>();		
		for (int i=way_nodes.size()-1;i>0;i--){
			bg = way_nodes.get(i);
			tl = way_nodes.get(i-1);			
			wg =  large;
			
			if (bg==start && tl==end){
				for (String sa: ta_keys){
					for (String sb: tb_keys){
						if (dis[nodes.indexOf(sa)][nodes.indexOf(sb)]>0 && dis[nodes.indexOf(sa)][nodes.indexOf(sb)]<wg){
							wi = nodes.indexOf(sa);
							wj = nodes.indexOf(sb);
							wg = dis[wi][wj];
						}
					}
				}
			}else if (bg==start && tl!=end){
				for (String sa: ta_keys){
					if (dis[nodes.indexOf(sa)][tl]>0 && dis[nodes.indexOf(sa)][tl]<wg){
						wi = nodes.indexOf(sa);
						wj = tl;
						wg = dis[wi][tl];
					}
				}
			}else if (bg!=start && tl==end){
				for (String sb: tb_keys){
					if (dis[bg][nodes.indexOf(sb)]>0 && dis[bg][nodes.indexOf(sb)]<wg){
						wi = bg;
						wj = nodes.indexOf(sb);
						wg = dis[bg][wj];
					}
				}
			}else{
				wi = bg;
				wj = tl;
				wg = dis[wi][wj];
			}			
			new_edges.add(new st_edge(nodes.get(wi), nodes.get(wj), wg));
		}
		
		
		ArrayList<String> keys = ta_keys;
		keys.addAll(tb_keys);
		
		int wgal = 0;
		new_edges.addAll(ta.edges);
		new_edges.addAll(tb.edges);
		for (st_edge ssa:new_edges ){
			wgal += ssa.weight;
		}
		System.out.println("weight:"+wgal +"\tedges size: "+new_edges.size()+"\tkeys size:"+keys.size());
		
		////////////////////       dijkstra algorithm           //////////////////// 
		steiner_tree  res = new steiner_tree("-1", keys, nodes, wgal, new_edges);
		return res; 
	}
	
	
	
		
	public static void main( String[] args ) throws IOException
    {				
		/////// /////           change the deploy mode , just need to change the following line !              /////// //////
		long tm1 = System.currentTimeMillis();
		
//        SparkConf conf=new SparkConf().setAppName("bio spark app").setMaster("spark://master:7077");//local[4]
        SparkConf conf=new SparkConf().setAppName("bio spark app").setMaster("local[4]");
		JavaSparkContext sc =new JavaSparkContext(conf);		
	
/*		JavaRDD<String> lines = sc.textFile("data.txt"); //hdfs://user/lee/
		System.out.println("hello world");
		JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer>() {
		  public Integer call(String s) { 
			  return s.length(); }
		});
		int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
		  public Integer call(Integer a, Integer b) { return a + b; }
		});		
		System.out.println("\ntotallength:\t"+totalLength);*/
		
		final ComputeSim cs = new ComputeSim();
		/////// get entleaf;			 
		String fr1 = spath + "Leaf.dat";
		ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
		entleaf = ms1.ent;  		
		System.out.println(entleaf.size()+"  size entleaf");
		
		Tuple2<int[][], int[][]> ma2 = cs.getMatrix(spath+"nodes_sorted.csv");
		int[][] matrix = ma2._1;     ////////   level ,lowest common ansester's  level   //////// 
		int[][] mat_father = ma2._2;     ////////   cluster number ,lowest common ansester's  cluster number  ////////		
		long tm2 = System.currentTimeMillis();
		System.out.println("time:"+(tm2-tm1)+" ms!");
		
		////////////        get the 50 keys from text          ///////////
		ArrayList<String> read = new ArrayList<String>();
		read = fraw.ReadData(path+"set_one.csv");
		ArrayList<ArrayList<String>> allkeys = new ArrayList<ArrayList<String>>();		
		for(String s:read){
			ArrayList<String> subkeys = new ArrayList<String>();
			String[] ss = s.split(",");
			for(int j = 0; j<ss.length;j++){
				subkeys.add(ss[j]);		
			}
			allkeys.add(subkeys);
		}
		System.out.println(" all keys size: " + allkeys.size());
		
		int i=0;
		while (i++<allkeys.size()){			
/*			ArrayList<String> keys  = new ArrayList<String>();	
			 keys.add("YDR046C");
			 keys.add("YOL020W");
			 keys.add("YPL265W");
			 keys.add("YBR069C");
			 keys.add("YJL156C");
			 keys.add("YKR093W");
			 keys.add("YDR160W");
			 keys.add("YBR068C");
			 keys.add("YDR463W");
			 keys.add("YCL025C");
			 System.out.println("keys:\t"+keys);*/
			 
			 ArrayList<String> keys = allkeys.get(i);
			 System.out.println("\n\nnum:\t"+i+"\t\tkeys:\t"+keys);

	//		 System.out.println( "the least public root is: " + cs.getAllkeywordsRoot(keys)+"\n\n"); 			 
			 
			 System.out.println("clusters:");  ///////   cluster number,   cluster's including keys
			 final HashMap<String, ArrayList<String>> subgra = cs.getSubGraphList(keys, matrix , mat_father, cs.nls_bykey );
/*			 System.out.println("fathers:");  //////// 
			 for (java.util.Map.Entry<String, ArrayList<String>> entry : subgra.entrySet()){
				 cs.getCluFather(entry.getKey(),matrix);
			 }	*/
			 
			 /////  convert a hashmap to a javaRDD type
			 ArrayList<Tuple2<String, ArrayList<String>>> list = new ArrayList<Tuple2<String, ArrayList<String>>>();
			 Set<String> allKeys = subgra.keySet();
			 for (String key : allKeys) {
				 	list.add(new Tuple2<String, ArrayList<String>>(key, subgra.get(key)) );
			 };	
			 JavaRDD<Tuple2<String, ArrayList<String>>> rdd = sc.parallelize(list);	 							
			 
			 JavaRDD<steiner_tree> st_trees =  rdd.map(new Function<Tuple2<String, ArrayList<String>>, steiner_tree>() {
				 public steiner_tree call(Tuple2<String, ArrayList<String>> ls) throws IOException{			
					 /////////////  parameter:  cluster's number;   cluster's including keys   /////////
					 
					 /////////////   there need a steiner tree search algorithm
					 ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
					 entleaf = ms1.ent;  				 
					 					 
					 String disfr = spath + "EdgeDisMatrix.csv";
					 cluster_data dataAction = getDataAction( disfr, entleaf.get(ls._1) );		
					 
					 System.out.println("*******************\there is map process !!!!\t****************");
					 steiner_tree local_st = getlocalst(dataAction,ls._2); 
					 /////// dynamic algorithm,  need a smallest tree algorithm to compare   ////////
					 local_st.cluster_number = ls._1;	
					 return  local_st;
				 }
			 });
			 
			 ////// the following need to merge the part results	
			 System.out.println("st.count: "+st_trees.count());

			 List<steiner_tree> r1 = st_trees.collect();	
			 System.out.println("r1 size: "+r1.size());
			 for (steiner_tree ss: r1){/////////  print the local steiner tree
				 System.out.println(ss.cluster_number+" "+ss.edges.size());
				 for (st_edge ed: ss.edges){
					 ed.st_output();
				 }
			 }
			 
			 
			 int[][] part_matrix = cs.part_matrix;
			 int[][] part_mtx_father = cs.part_mtx_father;
			        
			 System.out.println(matrix.length+" "+part_matrix.length);		 
			 
			 steiner_tree fn_st = merge_localst( r1, part_mtx_father, part_matrix, subgra ,keys );	
			 
/*			 for (int k=0;k<part_matrix.length;k++){
				 for (int j =0;j<part_matrix.length;j++){
					 System.out.print(part_mtx_father[k][j]+" ");
				 }
				 System.out.println();
			 }*/
			 
			 System.out.println("final steiner tree:");
			 System.out.println("cluster: "+fn_st.cluster_number+"\tkeys: "+
					 fn_st.keys+"\tnodes size: "+fn_st.nodes.size()+"\tweight all: "+fn_st.weight_all+"\tedges size:"+fn_st.edges.size());
			 
		}
		long tm3 = System.currentTimeMillis();
		System.out.println("tm3-tm2:\t"+(tm3-tm2)+" ms!!\n\n");
		
		 System.out.println("end....");
    }
	
}