package main;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.Function3;
import org.apache.spark.ml.util.LocalStopwatch;
import org.apache.spark.scheduler.BeginEvent;
import org.apache.spark.serializer.SerializationStream;
import org.apache.zookeeper.KeeperException.SystemErrorException;
import org.bouncycastle.asn1.icao.CscaMasterList;
import org.codehaus.janino.Java.CompilationUnit.StaticImportOnDemandDeclaration;
import org.datanucleus.store.rdbms.sql.method.StringEqualsIgnoreCaseMethod;
import org.jboss.netty.util.HashedWheelTimer;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.compiler.CodeGenerator.includeExpr_return;
import org.stringtemplate.v4.compiler.STParser.andConditional_return;
import org.apache.spark.api.java.AbstractJavaRDDLike;
import org.apache.spark.api.java.JavaRDD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.jdo.identity.IntIdentity;
import javax.validation.constraints.Min;

import org.apache.calcite.util.Static;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.transform.FastHadamardTransformer;
import org.apache.derby.impl.sql.execute.HashScanResultSet;
import org.apache.hadoop.hive.metastore.api.ThriftHiveMetastore.Processor.get_partition_with_auth;
import org.apache.hadoop.hive.metastore.api.ThriftHiveMetastore.set_aggr_stats_for_args;
import org.apache.hadoop.hive.ql.parse.HiveParser.replicationClause_return;
import org.apache.hadoop.hive.ql.parse.HiveParser.statement_return;
import org.apache.hadoop.hive.ql.parse.HiveParser_FromClauseParser.fromClause_return;
import org.apache.hadoop.hive.ql.parse.HiveParser_FromClauseParser.lateralView_return;
import org.apache.hadoop.hive.ql.parse.HiveParser_FromClauseParser.valueRowConstructor_return;
import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.intervalLiteral_return;
import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.orderByClause_return;
import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.stringLiteralSequence_return;
import org.apache.hadoop.util.hash.Hash;
import org.apache.hadoop.yarn.client.api.impl.AHSClientImpl;
import org.apache.kerby.kerberos.kerb.server.KdcConfigKey;
import org.apache.spark.SparkConf;

import com.biosearch.bean.Nodes;
import com.esotericsoftware.kryo.util.IdentityMap.Keys;
import com.google.common.collect.ContiguousSet;
import com.jiao.file.FileReadAndWrite;
import com.jiao.hierarachy.HierarchyMapSerializable;
import com.jiao.nexoSim.*;
import com.jiao.steinerTree.DreyfusWagner;
import com.jiao.steinerTree.ResBackForDrefus;
import com.jiao.steinerTree.ShortestPathResult;
import com.mysql.fabric.xmlrpc.base.Array;
import com.twitter.chill.Tuple1LongSerializer;

import antlr.collections.Stack;
import breeze.linalg.all;
import breeze.linalg.reshape;
import breeze.optimize.FisherDiffFunction;
import io.netty.channel.epoll.EpollDomainSocketChannel;
import scala.Tuple2;
import scala.Tuple3;
//import com.jiao.element.cluster_data;
import scala.annotation.implicitNotFound;
import scala.annotation.meta.field;
import scala.annotation.meta.param;
import scala.collection.concurrent.LNode;
import scala.reflect.api.Internals.ReificationSupportApi.SyntacticSelectTermExtractor;
import scala.reflect.internal.Trees.New;
import scala.reflect.internal.Trees.This;
import scala.tools.ant.sabbus.Break;
import main.cluster_data;
import main.mincut.edge;

public class app    {
	
//	static String spath = "file://home/lee/biolg/ScaleFreeNetwork TestData/snbt/";   ////  "E://ScaleFreeNetwork TestData/snbt/";
	static String spath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";
	public static String path =  "/home/lee/biolg/ScaleFreeNetwork TestData/keys/" ;    
	public static HierarchyMapSerializable ms1; 
	public static HashMap<String, ArrayList<String>> entleaf;      ////////// every clusters' nodes(keywords set)
	public static HashMap<String, Integer> cluster_size = new HashMap<>() ;    //////every clusters' nodes size (original nodes)
	static HashMap<Integer, Integer> cluster2level = new HashMap<Integer, Integer>();
	static HashMap<String, String> child_parent = new HashMap<>(); /////// clusters' father cluster
	static HashMap<String, HashSet<String > > parent_childs_set = new HashMap<>(); 
	static HashMap<String, ArrayList<String > > cluster_son_set = new HashMap<>(); ////// clusters' son clusters //// super_cluster unflod results
	static HashMap<String, HashSet<st_edge>> cluster_links = new HashMap<>();  ////// clusters' links 
	static HashMap<Tuple2<Integer, Integer >, Integer> snbt_edges = new HashMap<Tuple2<Integer, Integer >, Integer>(); 
	static HashMap<String, Integer> nodes2itg =  new HashMap<String, Integer>() ;
	
	static HashMap<String, String> cluster_center_node = new HashMap<>();////  each cluster's subnetwork's center node /////
	static cluster_data  clust_whole = null;
	
	public static final int large = 10000;
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static ShortestPathResult spr = new ShortestPathResult();
	static DreyfusWagner st = new DreyfusWagner();
	static int  not_all=0;
	static HashSet<Integer> not_list = new HashSet<>();
	static HashSet<Integer> nu = new HashSet<>();
	static int chen = 0;
	static int no_chen = 0;
	static int max_size = 100;
	static int root_cluster=-1;
	static ComputeSim cs = new ComputeSim();
	static int[][] matrix ;     ////////   level ,lowest common ansester's  level   //////// 
	static int[][] mat_father ;     ////////   cluster number ,lowest common ansester's  cluster number  ////////	
	
		
	
	public static cluster_data getall(String path) throws NumberFormatException, IOException{
		int dis[][] = null;		
		//////// initlize

//		System.out.println(nodes);		
		int i=0,j=0;
		BufferedReader br = new BufferedReader(new FileReader(path));
        // 读取直到最后一行 
        String line = "";         
        ArrayList<String> allnodes = new ArrayList<String>();   
        HashMap<String, Integer> ind = new HashMap<String, Integer>();
        while ((line = br.readLine()) != null) { 
            // 把一行数据分割成多个字段
        	i += 1;
            StringTokenizer st = new StringTokenizer(line, "\t| ");      
            if (i==1){
//            	System.out.println(st.countTokens());
            	int kk=0;
            	while (st.hasMoreTokens()){
//            		System.out.println(st.nextToken());
            		String sa = st.nextToken();
            		allnodes.add(sa);
            		ind.put(sa, kk++);
            	}
            	dis = new int[allnodes.size()][allnodes.size()];
            	for (int n=0;n<allnodes.size();n++){
            		for (int k=0;k<allnodes.size();k++){
            			dis[n][k] = 0;
            		}
            	}
            	
            	continue;
            }
            String s =  st.nextToken();

        	ArrayList<String> la = new ArrayList<String>();
            while (st.hasMoreTokens()) { 
                // 每一行的多个字段用TAB隔开表示 
//            	System.out.print(st.nextToken()+" ");
            	la.add(st.nextToken() );        	
            }
            int k = ind.get(s);
            for (j=0;j<la.size();j++){
            	dis[j][k] = dis[k][j] = Integer.parseInt( la.get(j) )  ;
            }           
        } 
        br.close();
        

        cluster_data res = new cluster_data(dis,allnodes);
		return res;
	}
	
	 public static  boolean isInteger(String str) {
		 Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		 return pattern.matcher(str).matches();    
	}  
	 
	public static cluster_data get_partfromall(cluster_data all,ArrayList<String> nodes ){
		int[][] dis = all.getDis();
		ArrayList<String> allnodes =  all.getnodes();
		
		HashMap<String, Integer> inx = new HashMap<>();
		for (int i=0;i<allnodes.size();i++){
			inx.put(allnodes.get(i), i);
		}
		
		int[][] part = new int[nodes.size()][nodes.size()];
		for (int i=0;i<nodes.size();i++){
			for (int j=0;j<nodes.size();j++){
				part[i][j] = dis[inx.get(nodes.get(i)) ][inx.get(nodes.get(j))];
			}
		}
		
		return new cluster_data(dis, nodes);	
		
	}
	
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
            StringTokenizer st = new StringTokenizer(line, "\t| ");      
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
            	dis[j][k] = dis[k][j] =  Math.round(new Float(la.get(allnodes.indexOf( nodes.get(j) ))))  ;
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
	
	
//	public static steiner_tree get_localst( cluster_data cst, ArrayList<String> keys)
	public static steiner_tree get_localst( int[][] dis,ArrayList<String> nodes, ArrayList<String> keys)
	{	
		HashSet<String> allkeys = new HashSet<>(keys);
//		Tuple2<Integer, ArrayList<String>> res = null;
		ArrayList<Integer> keysint = new ArrayList<>() ;
		HashMap<String, Integer> nodesint = new HashMap<>();
		
//		int[][] dis = cst.getDis();
//		ArrayList<String> nodes = cst.getnodes();
		
		ArrayList<String> non_keys = new ArrayList<>();		
//		System.out.println(nodes.containsAll(keys));
		int n=0;
		for (String a:nodes){
			nodesint.put(a, n++);
			if (keys.contains(a)==false){
				non_keys.add(a);
			}
		}
		for (int i=0;i<keys.size();i++){
			keysint.add(nodes.indexOf(keys.get(i)));
		}
//		System.out.println("getlocalst:\n"+keys);
		HashMap<Tuple2<HashSet<String>, String> , Integer> data = new HashMap<>() ; ///// sub set of keys,   its shortest path
		HashMap<Tuple2<HashSet<String>, String> , HashSet<st_edge>> st_edges = new HashMap<>(); /////// including edges
		
		long tm1= System.currentTimeMillis();
		HashSet<HashSet<String>> subsets = getallsubsets(keys,1); ////// all non-empty sebsets of keys		
		Tuple2<int[][], int[][]> fld_res = flod(dis); //////// shortest path by floyd algorithm
		int[][] floyd =  fld_res._1;
		int[][] floyd_path = fld_res._2;
//		System.out.println("keys: "+keys+"\tnodes: "+nodes+" floyd: "+floyd.length+" \tdis: "+dis.length);
//		System.out.println("subs: "+cst.getClusters());
		
//		System.out.println("time for floyd:" +(System.currentTimeMillis() - tm1) );
		
/*		System.out.println("getlocalst:\nkeys: "+keys);
		for (int i=0;i<floyd.length;i++){
			for (int j=0;j<floyd.length;j++){
				System.out.print(dis[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("subsets: " + subsets+" size: "+subsets.size());*/
		////// initnial  1 key, with
		for (HashSet<String> a: subsets){
			if (a.size()==1){				
				for (String b:a){
					for (String c: nodes){
						Tuple2<HashSet<String>, String> tuple2 = new Tuple2<HashSet<String>, String>(a, c); // = Tuple2<a, 1>;				
						/////// without start and end nodes
						ArrayList<Integer> way_nodes = getpath_nonrecursive(floyd_path,  nodesint.get(c) , nodesint.get(b));
//						System.out.println("way nodes:\n"+way_nodes);			
						//////////   this way to store the steiner tree ?? really the best way ??
						//////// 3 ways:  linkedlist,  class tree,  hashset.     for merge, hashset is applied.
						HashSet<st_edge> s_tree = get_st_f_ways(way_nodes, nodesint.get(c)  , nodesint.get(b) , floyd, nodes);
/*						System.out.println("keys set: "+a+" "+i);
						for (st_edge sa: s_tree){
							sa.st_output();
						}*/
						data.put( tuple2,floyd[nodesint.get(b)][nodesint.get(c)]);    ////////  put the shortest path
						st_edges.put(tuple2, s_tree);      /////////  put the tree edges 
//						System.out.println("a:" +a +" \ti:"+ i);
					}
				}		
			}
		}
//		System.out.println("data size: "+data.size());
		for (int i =2;i<=keys.size();i++){
			for (HashSet<String> a:  subsets  ){
				if (a.size()!=i){
					continue;
				}
//				System.out.println("keys num: "+i+"\t keys subsets: "+a);
				HashSet<HashSet<String>> arrayList = getallsubsets(new ArrayList<String>(a) , 2);		
				int value = large;
				HashSet<st_edge> edges = new HashSet<>();
				for (String k: nodes){
					for (String mm :nodes){
						for (HashSet<String> aHashSet:arrayList){					
							HashSet<String> inters = getintersection(aHashSet, a);
							Tuple2<HashSet<String>, String>  t1 = new Tuple2<HashSet<String>, String>(aHashSet, k);
							Tuple2<HashSet<String>, String>  t2 = new Tuple2<HashSet<String>, String>(inters, k);
	//						System.out.println("a" +a+"\tah:"+aHashSet+"\tint: "+inters+"\t k:"+k+" "+data.containsKey(t1)+" "+data.containsKey(t2));
							if (data.containsKey(t1) && data.containsKey(t2)){
								int va = data.get(t1) + data.get(t2) + floyd[ nodesint.get(k) ][ nodesint.get(mm) ];
								///////// some bugs need to repair ; edges both include 
								if ( value > va ){
	//								System.out.println("world ................");
									value = va;
									edges.clear();
									edges.addAll(st_edges.get(t1));
									edges.addAll(st_edges.get(t2));
									
									ArrayList<Integer> way_nodes = getpath_nonrecursive(floyd_path,  nodesint.get(k) , nodesint.get(mm));
									edges.addAll( get_st_f_ways(way_nodes, nodesint.get(k) , nodesint.get(mm) , floyd, nodes) );
								}
							}
						}
					}						
					Tuple2<HashSet<String>, String> t3 = new Tuple2<HashSet<String>, String>(a, k);
					data.put(t3, value);  //////////////////
					st_edges.put(t3, edges  );
				}
				
				
/*				for (Integer k=0;k<nodes.size();k++){
					Tuple2<HashSet<String>, String> t4 = new Tuple2<HashSet<String>, String>(a, String.valueOf(k ) );
					value = data.get(t4);
					edges = st_edges.get(t4);
					for (int j = 0;j<nodes.size();j++){
						if (value > data.get(t4)+dis[k][j] ){
							value = data.get(t4)+dis[k][j] ;
							edges.clear();
							edges.addAll(st_edges.get(t4));
							edges.add(new st_edge(nodes.get(k), nodes.get(j), dis[k][j]));
//							System.out.println("hello world......................." );
						}						
					}
					data.put(t4, value);    //////////////
					st_edges.put(t4, edges);   //////////////////			
				}*/
	
			}	
		}	
		

		int va = large;
		String nString = "";
		
		
		
		for (String k: nodes ){
			Tuple2<HashSet<String>, String> t6 = new Tuple2<HashSet<String>, String>( allkeys , k);
			if (data.containsKey(t6)==false){
				continue;
			}
			if (data.get(t6)  < va){
//				System.out.println("hahahhahhah.............");
				va = data.get(t6);
				nString = k;
			}
//			System.out.println("tuples: "+t6+" \tedges num:"+st_edges.get(t6).size());			
		}
		Tuple2<HashSet<String>, String> t7 = new Tuple2<HashSet<String>, String>(allkeys, nString);

		int wgl =0;
		String string = "0";
		if (!st_edges.containsKey(t7)){
			System.out.println("can not connectted!!!.....  keys: "+keys);
			return new steiner_tree(string, keys, nodes, wgl, new HashSet<st_edge>());	
		}
		
		HashSet<st_edge>  ddd = st_edges.get(t7);
		for (st_edge ssa: ddd){
			wgl += ssa.weight;
		}			
		
		steiner_tree res = new steiner_tree(string, keys, nodes, wgl, ddd);	
		return res;
	}
	
	/////// dijkstra steiner tree search algorithm 
	/////// by liyuan 
	//////  written in 2018.10.12
	public static steiner_tree get_localst_2( int[][] dis,ArrayList<String> nodes, ArrayList<String> keys)
	{	
		
		HashSet<HashSet<String>> subsets = getallsubsets(keys,1); ////// all non-empty sebsets of keys		
		Tuple2<int[][], int[][]> fld_res = flod(dis); //////// shortest path by floyd algorithm
		int[][] floyd =  fld_res._1;
		int[][] floyd_path = fld_res._2;
		HashMap<String, Integer> nodesint = new HashMap<>();
		for (int i =0;i<nodes.size();i++){
			nodesint.put(nodes.get(i), i);
		}
		
		//////// only record linked nodes set //////
		HashMap<Tuple2<String, HashSet<String>>, Integer> part_wgt = new HashMap<>();
		HashMap<Tuple2<String, HashSet<String>>, HashSet<st_edge>> part_edges = new HashMap<>();

		HashMap<Tuple2<String, String>, HashSet<st_edge>> pairs = new HashMap<>();

		////// initnial  1 key, with
		for (String aa : keys){
			for (String bb:nodes){
				HashSet<String> sub_keys = new HashSet<String>();
				sub_keys.add(aa);
				if (aa.equals(bb)){
					part_wgt.put(new Tuple2<String, HashSet<String>>(bb,sub_keys ), 0);
					part_edges.put( new Tuple2<String, HashSet<String>>(bb,sub_keys )  , new HashSet<st_edge>());
				}else{
					ArrayList<Integer> way_nodes = getpath_nonrecursive(floyd_path, nodesint.get(aa) , nodesint.get(bb));
//					System.out.println("way nodes:\n"+way_nodes);			
					//////////   this way to store the steiner tree ?? really the best way ??
					//////// 3 ways:  linkedlist,  class tree,  hashset.     for merge, hashset is applied.
					HashSet<st_edge> s_tree = get_st_f_ways(way_nodes, nodesint.get(aa)  , nodesint.get(bb) , floyd, nodes);
					int val = 0;
					for (st_edge edge: s_tree){
						val += edge.weight;
					}
					if (way_nodes.size()==0){
						s_tree.clear();
						val = dis[nodesint.get(aa)][nodesint.get(bb)];
						if (val>0){							
							s_tree.add( new st_edge(aa, bb, val)  );					
						}			
//						System.out.println( "hehehhe:  "+ s_tree.size()+" "+way_nodes+" "+val+" " +aa+" "+ bb +" "+ dis[nodesint.get(aa)][nodesint.get(bb)]  );
					}
					if (val>0){ ////// only record linked nodes /////
						part_wgt.put(new Tuple2<String, HashSet<String>>(bb,sub_keys ), val);
						part_edges.put( new Tuple2<String, HashSet<String>>(bb,sub_keys ), s_tree);
						pairs.put(new Tuple2<String, String>(aa, bb), s_tree);
					}

				}	
			}		
		}		
		
		for (int sz = 2;sz<=keys.size();sz++){
			for (HashSet<String> sub_keys: subsets ){
				if (sub_keys.size()!=sz){
					continue;
				}
				ArrayList<String> sskeys = new ArrayList<>();
				for (String eString: sub_keys){
					sskeys.add(eString);
				}
				
				int wgtall = Integer.MAX_VALUE;
				
				///////////  all subsets
				HashSet<HashSet<String>> subsubs = getallsubsets(sskeys,2);
				for (HashSet<String> ppa: subsubs){
					HashSet<String> ppb = get_restset(ppa, sub_keys);
					for (String cc: nodes){
						HashSet< HashSet<String> > edges_set = new HashSet<>();//////// nodes pair
						int val = 0;
//						System.out.printf("sz: %d  cc: %s \n", sz,cc );
//						System.out.println(sub_keys);
						if (!part_wgt.containsKey( new Tuple2<String, HashSet<String>>(cc, ppa)) ||
								!part_wgt.containsKey( new Tuple2<String, HashSet<String>>(cc, ppb))    ){
							continue;
						}					
												
						HashSet<st_edge> aEdges = part_edges.get( new Tuple2<String, HashSet<String>>(cc, ppa) );
						if ( part_edges.get( new Tuple2<String, HashSet<String>>(cc, ppb) ) != null ){
							aEdges.addAll( part_edges.get( new Tuple2<String, HashSet<String>>(cc, ppb) ) );
						}
						if ( aEdges != null && aEdges.size()>0){
							for (st_edge ae: aEdges ){							
								HashSet<String> tmpset = new HashSet<>();
								tmpset.add(ae.beg);
								tmpset.add(ae.end);
								if (!edges_set.contains(tmpset)){
									edges_set.add(tmpset); 
									val += ae.weight;
								}							
							}	
						}					
						
						if (val<wgtall ){
							wgtall = val;
							part_wgt.put(new Tuple2<String, HashSet<String>>(cc,sub_keys ), val);
							part_edges.put( new Tuple2<String, HashSet<String>>(cc,sub_keys ), aEdges);
						}
//						wgtall = wgtall>val ? val: wgtall;						
					}
				}
				
				/////////  all nodes linked
				for (String aa: nodes){
					int la = nodesint.get(aa);
					for (String bb: nodes){
						int lb = nodesint.get(bb);
						if (!part_wgt.containsKey( new Tuple2<String, HashSet<String>>(aa, sub_keys)) ||
								!part_wgt.containsKey( new Tuple2<String, HashSet<String>>(bb, sub_keys))    ){
							continue;
						}	
						
						int va = part_wgt.get( new Tuple2<String, HashSet<String>>(aa, sub_keys) );
						int vb = part_wgt.get( new Tuple2<String, HashSet<String>>(bb, sub_keys) );
						if ( dis[la][lb]>0 &&  va>vb + dis[la][lb] ){
							part_wgt.put(  new Tuple2<String, HashSet<String>>(aa, sub_keys)  , vb + dis[la][lb] );
							
							HashSet<st_edge> aEdges = part_edges.get( new Tuple2<String, HashSet<String>>(bb, sub_keys) );
							aEdges.add( new st_edge(aa, bb, dis[la][lb]) );
							part_edges.put(  new Tuple2<String, HashSet<String>>(aa, sub_keys)  , aEdges );						
						}							
					}
				}		
				
			}
		}				
		
		
		int weight_all=Integer.MAX_VALUE;
		HashSet<st_edge> edges = new HashSet<>();
		for (String ndd: nodes){
			if ( part_wgt.containsKey( new Tuple2<String, HashSet<String>>(ndd, new HashSet<String>(keys)  )) &&
					part_wgt.get( new Tuple2<String, HashSet<String>>(ndd,new HashSet<String>(keys) ))< weight_all   ){
				weight_all = part_wgt.get( new Tuple2<String, HashSet<String>>(ndd,new HashSet<String>(keys) ));
				edges = part_edges.get( new Tuple2<String, HashSet<String>>(ndd,new HashSet<String>(keys) ));
			}
		}	
		
/*		System.out.printf( "hahhah: %d  %d \n",weight_all,edges.size() );
		int[][] mata =new int[keys.size()][keys.size()];
		for (int i=0;i<keys.size();i++){
			for (int j=0;j<keys.size();j++){
				mata[i][j] = 0;
			}
		}
		
		for (int i=0;i<nodes.size();i++){
			String aa = nodes.get(i);
			for (int j=0;j<nodes.size();j++){
				String bb = nodes.get(j);
				if (keys.contains(aa) && keys.contains(bb)){
					mata[keys.indexOf(aa)][keys.indexOf(bb)] = mata[keys.indexOf(bb)][keys.indexOf(aa)] = 
							dis[nodesint.get(aa)][nodesint.get(bb)];
				}				
			}
		}
		
		System.out.println("keys dis: ");
		for (int i=0;i<keys.size();i++){
			for (int j=0;j<keys.size();j++){
				System.out.print(mata[i][j]+" ");
			}System.out.println();
		}*/			
//		System.out.println("steiner tree search:\n"+weight_all+"\t"+edges.size()+"\tkeys: "+keys+"\tnodes size: "+nodes.size());
		steiner_tree res = new steiner_tree("0", keys, nodes, weight_all, edges);	
		return res;
	}
	
	///////  get the rest 
	public static HashSet<String> get_restset(HashSet<String> part , HashSet<String> all){
		HashSet<String> restSet = new HashSet<>();
		for (String aa: all){
			if (!part.contains(aa)){
				restSet.add(aa);
			}
		}	
		return restSet;
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
	public static HashSet<HashSet<String>> getallsubsets(ArrayList<String> hSet, int flg){
		//////// flg, parameter, 0, all subsets; 1, no empty subset; 2, no empty, no all subsets
		HashSet<HashSet<String>> reSets = new HashSet();
		ArrayList<String> list = hSet;
		int len = list.size();
		for (int i=1;i< Math.pow(2,list.size()) ;i++){
			String sa = Integer.toBinaryString(i);
			HashSet<String> ha = new HashSet<>() ;
			int lg = sa.length();
			for (int j=sa.length()-1;j>=0  ;j--){
				if (sa.charAt(j)=='1'){
					ha.add(list.get(len-(lg-j)));
				}				
			}
			reSets.add(ha);
		}
		if (flg==1){
			reSets.add(new HashSet<String>(hSet) );
		}
		if (flg == 0){
			reSets.add(new HashSet<String>());
			reSets.add(new HashSet<String>(hSet) );
		}
		
		return reSets;	
	}
	
	
	////////                 get the local optimal steiner tree by spaning tree                    ///////
	public static steiner_tree getlocalst_spanningTree( cluster_data cst, ArrayList<String> keys){
		int[][] dis = cst.getDis();
		ArrayList<String> nodes = cst.getnodes();
			
		if (keys.size()==1){ /////// only 1 key, just return ///////
			return new steiner_tree(-1+"", keys, nodes, 0, new HashSet<st_edge>() );
		}
		System.out.println("nodes:" +nodes+"\nkeys:"+keys);
		HashSet<Integer> incl = new HashSet<>(); /////// visited nodes 
		HashSet<Integer> not = new HashSet<>();  ///// not visited nodes
		int[][] tree = new int[nodes.size()][nodes.size()];
		
		ArrayList<Integer> keys_num = new ArrayList<>();
		for (int i=0;i<keys.size();i++){
			keys_num.add(nodes.indexOf(keys.get(i)));
		}
		
		HashSet<Integer> values = new HashSet<>();
		System.out.println("dis:");
		for (int i=0;i<nodes.size();i++){
			not.add(i);
			for (int j=0;j<nodes.size();j++){
				tree[i][j] = 0;
				values.add(dis[i][j]);
				System.out.print(dis[i][j]+" ");
			}
			System.out.println();
		}
		if (values.contains(0)){
			values.remove(0);
		}
		ArrayList<Integer> val_sorted = new ArrayList<>(values);
		val_sorted.sort(null);
		System.out.println("weights sorted: "+val_sorted);
		
		ArrayList<wg_edge> edges_sorted = new ArrayList<>();
		for (Integer va : val_sorted){
			for (int i=0;i<nodes.size();i++){
				for (int j=0;j<nodes.size();j++){
					if (dis[i][j]==va){
						edges_sorted.add(new wg_edge(i, j, dis[i][j]));
					}
				}
			}	
		}
		
		ArrayList<wg_edge> sptree =  new ArrayList<>();
		HashMap<Integer, HashSet<Tuple2<Integer, Integer>>> eds_byStart = new HashMap<>();		

		while (true){
			for (wg_edge th:edges_sorted){
				if (incl.contains(th.beg)==false || incl.contains(th.end)==false  ){
					sptree.add(th);
					incl.add(th.beg);
					incl.add(th.end);
					tree[th.beg][th.end] = th.weight;
					HashSet<Tuple2<Integer, Integer>> sb = new HashSet<>();
					HashSet<Tuple2<Integer, Integer>> sa = new HashSet<>();
					sb.add(new Tuple2<>(Integer.valueOf(th.end),Integer.valueOf(th.weight)) );
					sa.add(new Tuple2<>(Integer.valueOf(th.beg),Integer.valueOf(th.weight)) );
					if (eds_byStart.containsKey(th.beg)){
						 sb.addAll(eds_byStart.get(th.beg));
					}
					if (eds_byStart.containsKey(th.end)){
						sa.addAll(eds_byStart.get(th.end));
					}
					eds_byStart.put(th.beg, sb);
					eds_byStart.put(th.end, sa);
				}
			}
			if (incl.size()==nodes.size()){
				break;
			}
		}
		System.out.println("sp tree: "+sptree.size() +"\n"+eds_byStart);
		
		int changed = 0;
		while(true){
			for (int i=0;i<nodes.size();i++){
				if (eds_byStart.containsKey(i)==false){
					continue;
				}
				HashSet<Tuple2<Integer, Integer>> sb = eds_byStart.get(i);
				if (sb.size() ==1 && keys_num.contains(i)==false){
					for (Tuple2<Integer, Integer> bb : sb){
						if (keys_num.contains(bb._1)==false){
							changed += 1;
							eds_byStart.remove(i);
							HashSet<Tuple2<Integer, Integer>> sa = eds_byStart.get(bb._1);
							sa.remove(new Tuple2<>(i,bb._2));
							eds_byStart.put(bb._1, sa);
							sptree.remove(new wg_edge(i, bb._1, bb._2));
							System.out.println("cutting:"+i+" "+bb);
						}
					}
				}
			}
			if (changed==0){
				break;
			}
			changed = 0;
		}
		System.out.println("cuted..  sp tree: "+sptree.size() );
		
		steiner_tree res =  new steiner_tree(-1+"", keys, nodes, 0, new HashSet<st_edge>() );
		return res;
	}
	
	public static HashMap<Integer, HashSet<Integer>> get_stru(int[][] com_father,int[][] com_level,
			HashMap<String, ArrayList<String>> subgra, ArrayList<String> allkeys) throws FileNotFoundException, IOException, ClassNotFoundException{	
		
//		System.out.println("allkeys: "+allkeys.size()+"\t"+allkeys);		
		HashMap<Integer,HashSet<Integer>> level_clusters = new HashMap();
		HashMap<Integer, Integer> cluster_level = new HashMap<>();
		HashSet<Integer> leves = new HashSet<>();
		HashMap<Integer, HashSet<Integer>> clu_stru = new HashMap<>();
		HashMap<Integer, HashSet<Integer>> clu_child = new HashMap<>();
		
		
		int all_level = large, m=0 ,n=0; ////// the lowest level for all keys father		
//		System.out.println("com_level:");
		for (int i=0;i<com_level.length;i++){
			for (int j=0;j<com_level.length;j++){
//				System.out.print(com_level[i][j]+" ");
				
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
				cluster_level.put(com_father[i][j], com_level[i][j]) ;
			}
//			System.out.println();
		}
//		System.out.println("all_level: "+ all_level +" "+ m +" "+n + " "+ com_father[m][n] );
		root_cluster = com_father[m][n];
//		System.out.println("level_clusters:\t"+level_clusters);//+" "+leves+"\n"+subgra);
		ArrayList<Integer> lvls_gw = new ArrayList<>(leves);
		lvls_gw.sort(null);
//		System.out.println("levels: "+lvls_gw);

		for (String a: subgra.keySet()){
			clu_stru.put(Integer.valueOf(a), new HashSet<Integer>());	
		} /////////   add the original sets   //////
//		long tm = System.currentTimeMillis(); 
		
		for (int i=lvls_gw.size()-1;i>=0;i--){
			for (Integer  clus: level_clusters.get(lvls_gw.get(i))){
				HashSet<Integer> lsas = new HashSet<>();
				for (Integer a: clu_stru.keySet()){
					if (entleaf.get(clus+"").containsAll(entleaf.get(a+""))   ){ /////  &&
//					System.out.println("a: "+a +"\tclus: "+clus);
//					if (  parent_childs_set.get(clus+"").contains(a+"")   ){ /////  &&
						if (a.equals(clus)==false){
							lsas.add(a);
						}						
					}
				}
				clu_stru.put(clus, lsas);					
			}			
		}		
//		System.out.println("clu_stru:\t"+clu_stru);
//		System.out.println("time here: "+(System.currentTimeMillis()-tm) );
//		System.out.println("cluster levels: "+cluster_level+ " "+cluster_level.size());
//		System.out.println("level clusters: " +level_clusters);
		
		HashMap<Integer, HashSet<Integer>> fathers = new HashMap<>();
		HashMap<Integer, HashSet<Integer>> fa_sons = new HashMap<>();
		for (Integer aa: clu_stru.keySet()){			
			HashSet<Integer> anSet = clu_stru.get(aa);
//			System.out.println("aa: "+aa+"\t anset: "+anSet);
			for (Integer bb: anSet){
				HashSet<Integer> saHashSet = new HashSet<>();
				saHashSet.add(aa);
				if (fathers.containsKey(bb)){
					saHashSet.addAll(fathers.get(bb));
				}
				fathers.put(bb, saHashSet);
			}
		}
//		System.out.println("fathers:\n"+fathers); /////  all anceseters
		
		
		for (Integer aa: fathers.keySet()){
			HashSet<Integer> anSet = fathers.get(aa);
			int fa = 0,lv=0;
			for (Integer bb: anSet){
				if ( cluster_level.get(bb) >lv ){
					lv = cluster_level.get(bb);
					fa = bb;
				}
			}
			HashSet<Integer> sons = new HashSet<>();
			sons.add(aa);
			if (subgra.keySet().contains(String.valueOf(fa)) && !subgra.keySet().contains(String.valueOf(aa)) ){
				sons.remove(aa);
			}
			if (fa_sons.containsKey(fa)){
				sons.addAll(fa_sons.get(fa));
			}
			if (!sons.isEmpty()){
				fa_sons.put(fa, sons);
			}
		}
		clu_child = fa_sons;
	
		for (Integer a: clu_child.keySet()){
			System.out.println( a+"\t "+cluster_level.get(a) );
		}
		return clu_child;
	}
	
	
	
	public static steiner_tree merge_localst( List<steiner_tree> local_Sts, HashMap<Integer, HashSet<Integer>> clu_child,
			HashMap<String, ArrayList<String>> subgra, ArrayList<String> allkeys ) throws IOException, ClassNotFoundException{
		
		HashMap<Integer, steiner_tree> clu_lst = new HashMap<>();  ///////// cluster , local st
		for (steiner_tree steiner_tree: local_Sts){
			if (steiner_tree==null){
				continue;
			}
			clu_lst.put( Integer.valueOf(steiner_tree.cluster_number) , steiner_tree);
		}	
//		System.out.println(" root: "+root_cluster);
		Set<Integer> not_con = clu_child.keySet();
		HashSet<Integer> records = new HashSet<>();
		for (String string : subgra.keySet()){
			if (clu_child.keySet().contains( Integer.valueOf(string) )){
				continue;
			}
			records.add(Integer.valueOf(string));
		}
//		System.out.println("not con: "+not_con+"\trecords: "+records);
		
		steiner_tree res=null;
		int changed =0;
		while (records.size()>0){
			for (Integer father: not_con){
				HashSet<Integer> childs = clu_child.get(father);
				if ( records.containsAll(childs)==true && records.contains(father)==false){

				   ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/cache/"+(father+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
			       cluster_data  cluster_allkeys = (cluster_data) oin.readObject(); // 强制转换到Person类型  
			       oin.close();  
					
					Object[] clds = childs.toArray();
					System.out.println( "father: "+father+"\tchilds: "+childs +"  father cluster size:"+cluster_allkeys.getnodes().size() );
					steiner_tree tree=null;
					
//					System.out.println(clu_lst.containsKey(clds[0]));					
					if (childs.size()==1){
						int flg = 0;
						if (!clu_lst.containsKey(father)){
							ArrayList<String> hh = new ArrayList<>();
							for (String key: allkeys){
								if ( entleaf.get(father+"").contains(key) && !entleaf.get(clds[0]+"").contains(key) ){
									hh.add(key);
								}
							}
							System.out.println("hh: "+hh);
							System.out.println(cluster_allkeys.getnodes().size());
							if (hh.isEmpty()){
								flg = 1;
							}else{
								steiner_tree local_st = get_localst(cluster_allkeys.getDis(), cluster_allkeys.getnodes() ,hh); 
								local_st.cluster_number = father+"";	
							}
						}
						if (flg==1){
							tree = clu_lst.get(clds[0]) ;
							tree.setclunum(father+"");	
						}else{
							tree = merge_nodes(clu_lst.get(clds[0]) , clu_lst.get(father), cluster_allkeys  );
							tree.setclunum(father+"");	
						}
					}else if (childs.size()==2){
						tree = merge_nodes(clu_lst.get(clds[0]) , clu_lst.get(clds[1]), cluster_allkeys  );
						tree.setclunum(father+"");		
					}else {
						steiner_tree part_tr = merge_nodes(clu_lst.get(clds[0]) , clu_lst.get(clds[1]), cluster_allkeys  );
						for (int k=2;k<childs.size();k++){
							part_tr = merge_nodes(part_tr, clu_lst.get(clds[k]), cluster_allkeys  );
//							System.out.println("world......");
						}
						tree = part_tr;
						tree.setclunum(father+"");	
					}
					clu_lst.put(father, tree);
					System.out.println("keys merged:\t "+tree.keys);
					records.add(father);
					
					if (father.equals(root_cluster)){
						res = tree;
					}
					changed = 1;
				}
			}
/*			if (records.containsAll(not_con)){
				break;
			}*/
			if (changed==1){
				changed = 0;
				continue;
			}else{
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
/*		System.out.println("keys: "+ta_keys + "\t"+tb_keys+"\t nodes:"+nodes);
		System.out.println(" new matrix size:"+num);*/
		
		long tm1 = System.currentTimeMillis();
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
		
//		System.out.println("nw index:\t"+nw_index);
		
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
		
		long tm2 = System.currentTimeMillis();
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
//		System.out.println("start: "+start+"\tend: "+end+"\tlength: "  + dj[end]+" "+nw_dis[start][end]);
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
//		System.out.println(  "  time for  disksatr: "  +(tm2-tm1)+"\t"+(System.currentTimeMillis()-tm1)      );
		
//		System.out.println("way nodes: "+way_nodes);
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
//		System.out.println("weight:"+wgal +"\tedges size: "+new_edges.size()+"\tkeys size:"+keys.size());
		
		////////////////////       dijkstra algorithm           //////////////////// 
		steiner_tree  res = new steiner_tree("-1", keys, nodes, wgal, new_edges);
		return res; 
	}
	
	
	
	public static steiner_tree merge_chen(steiner_tree ta, steiner_tree tb, super_cluster clu){
		ArrayList<String> ta_keys = new ArrayList<>();
		ArrayList<String> tb_keys = new ArrayList<>() ;
		ArrayList<String> nodes = clu.getnodes();
		int[][] dis = clu.getDis();
		HashSet<st_edge> ea = ta.edges;
		HashSet<st_edge> eb = tb.edges;
		for (st_edge sa: ea){
			ta_keys.add(sa.beg);
			ta_keys.add(sa.end);
		}		
		for (st_edge sa: eb){
			tb_keys.add(sa.beg);
			tb_keys.add(sa.end);
		}		
				
		int va = large;
		int m = 0,n = 0;
		for (String ka : ta_keys){
			for (String kb : tb_keys){
				int a = nodes.indexOf(ka);
				int b = nodes.indexOf(kb);
				if (dis[a][b] < va){
					va = dis[a][b];
					m =a;
					n = b;
				}
			}
		}
		
		if (va<large ){
			ea.addAll(ta.edges);
			ea.add( new st_edge(nodes.get(m), nodes.get(n), dis[m][n])  );
			ArrayList<String> keys =  ta.keys;
			keys.addAll(tb.keys);	
			steiner_tree  res = new steiner_tree("-1", keys, nodes, ta.weight_all+tb.weight_all+dis[m][n], ea);
			return res; 	
		}else{
			steiner_tree res = null;
			return res;
		}
	}
	
	public static HashMap<String, HashSet< String> >  get_par_sons_entleaf() throws IOException{
		HashMap<String, HashSet< String> >  res = new HashMap<>();
		String fr1 = spath + "Leaf.dat";
		ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
		entleaf = ms1.ent;  		
		System.out.println(entleaf.size()+"  size entleaf");
		HashSet<String> sons = new HashSet<>();
		for (String key: entleaf.keySet()){
			sons.clear();
			for (String kk : entleaf.keySet()){
				if ( entleaf.get(key).containsAll(entleaf.get(kk)) && !key.equals(kk) ){
					sons.add(kk);
				}
			}
			res.put(key, (HashSet<String>)sons.clone());
		}
		String fh = "";
		int sz = large ;
		for (String key: entleaf.keySet()){
			fh = "";
			sz = large;
			for (String kk : entleaf.keySet()){
				if ( entleaf.get(kk).containsAll(entleaf.get(key)) && !key.equals(kk) && entleaf.get(kk).size()<sz ){
					sz =  entleaf.get(kk).size();
					fh = kk;
				}
			}
			child_parent.put(key,fh);
		}		
		
		//////  write into local   ////
	    FileOutputStream outStream = new FileOutputStream("/home/lee/biolg/parent_childs_set.dat");
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
	    objectOutputStream.writeObject(res);
	    outStream = new FileOutputStream("/home/lee/biolg/child_parent.dat");
	    objectOutputStream = new ObjectOutputStream(outStream);
	    objectOutputStream.writeObject(child_parent);
	    outStream.close();	
	    System.out.println("parent_childs_set size: "+res.size());
	    System.out.println("child_parent size: "+child_parent.size());
		return res;
	}
	
	
	
	public static int write_clusters_local(ComputeSim cs) throws FileNotFoundException, IOException, ClassNotFoundException{
		
		String fr1 = spath + "Leaf.dat";
		ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
		entleaf = ms1.ent;  		
//		System.out.println(entleaf.size()+"  size entleaf");
		Tuple2<int[][], int[][]> ma2 = cs.getMatrix(spath+"nodes_sorted.csv");		
	
		int[][] matrix = ma2._1;     ////////   level ,lowest common ansester's  level   //////// 
		int[][] mat_father = ma2._2;     ////////   cluster number ,lowest common ansester's  cluster number  ////////	
		String disfr = spath + "EdgeDisMatrix.csv";
		///// cache the cluster data
//		cluster_data all = getall(disfr);
		/// "AdjcentMatrix.csv"
		int num = 0;
		ArrayList<Integer> others = new ArrayList<>();
		
		FileInputStream in=new FileInputStream("/home/lee/biolg/par_childs.dat"); 
		ObjectInputStream objread=new ObjectInputStream(in);
		Object obj=objread.readObject();		
		objread.close();
		HashMap<String, HashSet<String>> par_children = (HashMap<String, HashSet<String>>) obj;
		
		HashSet<Integer> written = new HashSet<>();
		System.out.println("parent_childs_set size: "+parent_childs_set.size()+"\tchild_parent size: "+ child_parent.size());
		
		for (int i=0;i<matrix.length;i++){
			for (int j=0;j<matrix.length;j++){				
/*				if (cluster2level.containsKey( mat_father[i][j] )){
					continue;
				}*/
				cluster2level.put(mat_father[i][j], matrix[i][j]);		
				num +=1 ;
				
				if (written.contains(mat_father[i][j])){
					continue;
				}
				String fp = "/home/lee/biolg/clusters/"+( mat_father[i][j] +"")+".dat" ;
				written.add(mat_father[i][j]);
				File file = new File(fp);
		        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(file));  
				///////////////                   ////////////////
			    ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+(mat_father[i][j] +"") +".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
		        cluster_data dataAction = (cluster_data) oin.readObject(); // 强制转换到Person类型  
		        oin.close();  
		        
		        int[][] dis = dataAction.getDis();
		        ArrayList<String> nodes = dataAction.getnodes();
		        ArrayList<String> nw_nodes = (ArrayList<String>) dataAction.getnodes().clone();

		        if (par_children.containsKey( mat_father[i][j] +"" )==false){
		        	others.add(mat_father[i][j]); 
		        	super_cluster spCluster = 
		        			new super_cluster(nodes.size(), 0, dataAction.getDis(), nw_nodes,nodes, new ArrayList<String>(), new HashSet<st_edge>(),new HashMap<Tuple2<String, String>, Integer>() );
					oout.writeObject(spCluster);		        
			        oout.close(); 
		        	continue;
		        }
		        
		        
		        ArrayList<String> subs =  new ArrayList<>(par_children.get(mat_father[i][j] +"" ) );   /////////  subs clusters   ////////
		        System.out.println("fat:"+ mat_father[i][j]+"\t" +nodes.size() +"\tsubs:"+subs.size()+" "+ subs  );	     
		        
		        ///////// transfrom //////
		        ArrayList<String> clus = subs;   ///////      clusters      /////// 
//		        System.out.println("\tclus: "+clus + " "+nodes.size() );	
		        
		        for (String son:subs){   ////// transform nodes to new nodes(including clusters)
		        	oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+son +".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
				    cluster_data dason = (cluster_data) oin.readObject(); // 强制转换到Person类型  
				    oin.close();  
				    ArrayList<String> son_nodes = dason.getnodes();
				    nw_nodes.removeAll(son_nodes);
				    nw_nodes.add(son);		        	
		        }
		        
		        System.out.println("nodes: "+nodes.size()+"\t nw :"+nw_nodes.size()  );
		        
		        int[][] dis_nw = new int[nw_nodes.size()][nw_nodes.size()];
		        for (int i1=0;i1<nw_nodes.size();i1++){
		        	for (int j1=0;j1<nw_nodes.size();j1++){
		        		dis_nw[i1][j1] = 0;
		        	}
		        }
		        
		        HashSet<st_edge> links = new HashSet<>();	  
		        HashMap<Tuple2<String, String>, Integer> tp_links = new HashMap<>();
		        int count = 0;

		        for (int k=0;k<subs.size();k++ ){
		        	String a = subs.get(k);
				    ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+a+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
			        cluster_data da = (cluster_data) oina.readObject(); // 强制转换到Person类型  
			        ArrayList<String> da_nodes = da.getnodes();
			        oina.close();
		        	for (int mm =k+1;mm<subs.size();mm++){
		        		String b = subs.get(mm);
					    ObjectInputStream oinb = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+b+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
				        cluster_data db = (cluster_data) oinb.readObject(); // 强制转换到Person类型  
				        ArrayList<String> db_nodes = db.getnodes();
		        		oinb.close();
//		        		System.out.println("da: "+da_nodes.size()+"\tdb: "+db_nodes.size());
		        		HashSet<String> bj = new HashSet<>();
		        		bj.addAll(da_nodes);
		        		bj.addAll(db_nodes);
		        		if ( bj.size()<da_nodes.size()+db_nodes.size()  ){
		        			System.out.println("hehhehhheh");
		        			System.out.println("\tda: "+da_nodes.size()+"\tdb: "+db_nodes.size()+" bj: "+bj.size());
		        			continue;
		        		}
		        		
		        		count +=1 ;
//		        		System.out.println (nw_nodes.size() +" " + da_nodes.size()+" "+db_nodes.size()+" "+nodes.size()+" "+nodes.containsAll(da_nodes)+" "+nodes.containsAll(db_nodes));
		        		
		        		int wg = large;
		        		for (String na: da_nodes){
		        			for (String nb: db_nodes){
//		        				System.out.println("\t"+na+" "+nb+ "\t"+nodes.indexOf(na)+" "+nodes.indexOf(nb)+"\t"+dis.length +" "+nodes.size()   );
		        				if (dis[nodes.indexOf(na)][nodes.indexOf(nb)]!=0){
		        					links.add( new st_edge(na, nb,dis[nodes.indexOf(na)][nodes.indexOf(nb)])  );
		        					tp_links.put(new Tuple2<String, String>(na, nb),dis[nodes.indexOf(na)][nodes.indexOf(nb)] );
		        					if (wg>  dis[nodes.indexOf(na)][nodes.indexOf(nb)]  ){
		        						wg =  dis[nodes.indexOf(na)][nodes.indexOf(nb)] ;
		        					}
		        				}
		        			}
		        		}
		        		if (wg<large){
		        			dis_nw[nw_nodes.indexOf(a)][nw_nodes.indexOf(b)] = dis_nw[nw_nodes.indexOf(b)][nw_nodes.indexOf(a)] = wg;
		        		}
//		        		System.out.println( "a: "+a+"\tb: "+b+"\t size: "+links.size()+" "+count++);
		        	}
//		        	System.out.println( "a: "+a+"\t size: "+links.size()+" "+count);
		        }
		        
		        super_cluster spCluster = new super_cluster((nodes.size() - subs.size()), subs.size(), dis_nw, nw_nodes,nodes, subs, links,tp_links);		       
				oout.writeObject(spCluster);		        
		        oout.close(); 
//		        System.out.println( "\t size: "+  dis_nw.length +"\tlinks: "  +links.size()+" "+count);
/*		      	if (num>10){
			       break;
			    }*/
			}		        
/*	      	if (num>10){
			       break;
			    }*/
		}
		
		System.out.println("ok....."+cluster2level.size());
		System.out.println("others:\t"+others);	
		
		
		return cluster2level.size();
	}
	
	
	public static steiner_tree merge_lst_sp ( List<steiner_tree> local_Sts, HashMap<Integer, HashSet<Integer>> clu_child,
			HashMap<String, ArrayList<String>> subgra, ArrayList<String> allkeys ) throws IOException, ClassNotFoundException{
		
		steiner_tree res = null;
		
		HashMap<Integer, steiner_tree> clu_lst = new HashMap<>();  ///////// cluster , local st
		for (steiner_tree steiner_tree: local_Sts){
			if (steiner_tree==null){
				continue;
			}
			clu_lst.put( Integer.valueOf(steiner_tree.cluster_number) , steiner_tree);
		}	
//		System.out.println(" root: "+root_cluster);
		Set<Integer> not_con = clu_child.keySet();
		HashSet<Integer> records = new HashSet<>();
		for (String string : subgra.keySet()){
			if (clu_child.keySet().contains( Integer.valueOf(string) )){
				continue;
			}
			records.add(Integer.valueOf(string));
		}
//		System.out.println("not con: "+not_con+"\trecords: "+records);
		
		int changed =0;
		while (records.size()>0){
			for (Integer father: not_con){
				HashSet<Integer> childs = clu_child.get(father);
				if ( records.containsAll(childs)==true && records.contains(father)==false){
					ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(father+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
			        super_cluster  cluster_allkeys = (super_cluster) oin.readObject(); // 强制转换到Person类型  
			        oin.close();  
					ArrayList<Integer> clds = new ArrayList<>(childs);  //// transfrom hashset to arraylist
					ArrayList<String> cl_str = new ArrayList<>();
					for (Integer a: childs){
						cl_str.add(String.valueOf(a));
					}						
					
					ArrayList<String> f_nodes = cluster_allkeys.getnodes();
//					System.out.println( "clds: "+clds.size());
//					System.out.println( "father: "+father+"\tchilds: "+childs +"  father cluster size:"+cluster_allkeys.getnodes().size() );

					steiner_tree tree=null;
					HashSet<st_edge> new_edges = new HashSet<>(); ////////  in this father cluster, edges to connect 2 son trees
					
					ArrayList<String> new_keys = new ArrayList<>();
					int new_weight_all = 0;
					
//					System.out.println(clu_lst.containsKey(clds[0]));					
					if (childs.size()==1){
						Integer chd = clds.get(0);			
//						ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(chd+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
//						super_cluster  cluster_chl_keys = (super_cluster) oina.readObject(); // 强制转换到Person类型  
//						oina.close();  
						steiner_tree st_chld = clu_lst.get(chd);						
						//////////////////////////////////////
						tree = new steiner_tree(String.valueOf(father),st_chld.keys ,cluster_allkeys.nodes, st_chld.weight_all,st_chld.edges);
					}else {
//						System.out.println("fat nodes: "+f_nodes+"\t childs: "+childs+"\t "+ f_nodes.containsAll(cl_str) );
						ArrayList<String> nw_clds = new ArrayList<>();
						HashMap<String, String> sma2par = new HashMap<>();
						for (String a: cl_str){
//							System.out.println(a+"\t"+ clu_lst.containsKey(Integer.valueOf(a)) );
							if (f_nodes.contains(a)){
								nw_clds.add(a);
							}else{
								String bString =a;
								while ( !f_nodes.contains(a) ){
									a = cs.getCluFather(a, matrix);
//									System.out.println("a: "+a);
								}
								nw_clds.add(a);
								sma2par.put(bString, a);
								steiner_tree nn = clu_lst.get(Integer.valueOf(bString));
								///////////////////////////////
								ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+a+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
								super_cluster  haha = (super_cluster) oina.readObject(); // 强制转换到Person类型  
								oina.close();  
//								System.out.println("b: "+bString+"\ta: "+a);
								clu_lst.put(Integer.valueOf(a ) , new steiner_tree(a, nn.keys, haha.ori_nodes, nn.weight_all, nn.edges) );
							}
						}
//						System.out.println("sma2par: "+sma2par.size() );				
						HashSet<st_edge> links = cluster_allkeys.get_links();
//						System.out.println("links: " + links.size() +"\nnw_clds: ");
						for (int k=1;k<nw_clds.size();k++){
//							System.out.println("k-1:"+ nw_clds.get(k-1)+"\tk: "+nw_clds.get(k)  );
							steiner_tree tr1 = clu_lst.get( Integer.valueOf( nw_clds.get(k-1) ) ); //////// local steiner tree 
							steiner_tree tr2 = clu_lst.get(Integer.valueOf(nw_clds.get(k) ) );
							if (tr2==null){
								System.out.println("tr2 null!!!");
							}
//							System.out.println( "tr1 cluster number: "+ tr1.cluster_number +"\t tr2: "+tr2.cluster_number  );
							ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+tr1.cluster_number+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst1 = (cluster_data) oina.readObject(); // 强制转换到Person类型  
							oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+tr2.cluster_number+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst2 = (cluster_data) oina.readObject(); // 强制转换到Person类型  							
							oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+(father+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst_all = (cluster_data) oina.readObject(); // 强制转换到Person类型  			
							oina.close();  
							
							new_edges.addAll( get_links( links, tr1 , tr2 ,clst_all , clst1, clst2 ) );
							if (k==1){
								new_keys.addAll( tr1.keys );
								new_keys.addAll( tr2.keys );
								new_weight_all += tr1.weight_all + tr2.weight_all;
								new_edges.addAll(tr1.edges);
								new_edges.addAll(tr2.edges);
							}{
								new_keys.addAll( tr2.keys );
								new_weight_all +=  tr2.weight_all;
								new_edges.addAll(tr2.edges);
							}
							
							
						}
						
						new_keys = new ArrayList<>(new HashSet<>(new_keys )) ;
						tree = new steiner_tree(String.valueOf(father),new_keys ,cluster_allkeys.nodes, new_weight_all, new_edges);
					}
					clu_lst.put(father, tree);
//					System.out.println("keys merged:\t "+tree.keys.size()+" " +tree.keys+"\n");//+ (new HashSet<>(tree.keys )).size()+" "
					records.add(father);
					
					if (father.equals(root_cluster)){
						res = tree;
					}
					changed = 1;
				}
			}
/*			if (records.containsAll(not_con)){
				break;
			}*/
			if (changed==1){
				changed = 0;
				continue;
			}else{
				break;
			}
		}
		return res;
	}

	public static steiner_tree merge_stru_list ( List<steiner_tree> local_Sts, HashMap<Integer, HashSet<Integer>> clu_child,
			HashMap<String, ArrayList<String>> subgra, ArrayList<String> allkeys, HashMap<String, graph_stru> stru_list  ) throws IOException, ClassNotFoundException{
		
		steiner_tree res = null;
		
		HashMap<Integer, steiner_tree> clu_lst = new HashMap<>();  ///////// cluster , local st
		for (steiner_tree steiner_tree: local_Sts){
			if (steiner_tree==null){
				continue;
			}
			clu_lst.put( Integer.valueOf(steiner_tree.cluster_number) , steiner_tree);
		}	
//		System.out.println(" root: "+root_cluster);
		Set<Integer> not_con = clu_child.keySet();
		HashSet<Integer> records = new HashSet<>();
		for (String string : subgra.keySet()){
			if (clu_child.keySet().contains( Integer.valueOf(string) )){
				continue;
			}
			records.add(Integer.valueOf(string));
		}
//		System.out.println("not con: "+not_con+"\trecords: "+records);
		
		int changed =0;
		while (records.size()>0){
			for (Integer father: not_con){
				HashSet<Integer> childs = clu_child.get(father);
				if ( records.containsAll(childs)==true && records.contains(father)==false){
					ArrayList<Integer> clds = new ArrayList<>(childs);  //// transfrom hashset to arraylist		
//					ArrayList<String> f_nodes = cluster_allkeys.getnodes();
//					System.out.println( "clds: "+clds.size());			
					
					ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(father+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
			        super_cluster  cluster_allkeys = (super_cluster) oin.readObject(); // 强制转换到Person类型  
			        oin.close();  
					HashSet<st_edge> links_bt = cluster_allkeys.links;
					System.out.println( "father: "+father+"\tchilds: "+childs +"  father cluster size:"+cluster_allkeys.getnodes());
			        			        
					steiner_tree tree=null;
					HashSet<st_edge> new_edges = new HashSet<>(); ////////  in this father cluster, edges to connect 2 son trees
					ArrayList<String> new_keys = new ArrayList<>();
					int new_weight_all = 0;
					
			        graph_stru now = stru_list.get(father+"");
//			        System.out.println("child par size:" +child_parent.size()+"\t clusters 2 level: "+cluster2level.size());
//			        System.out.println("now nodes size: "+now.nodes.size());
/*			        for (String a:now.nodes){
			        	if ( isInteger(a) ){
			        		System.out.print(a+":"+cluster2level.get(Integer.valueOf(a))+"\t"  );
			        	}else{
			        		System.out.print(a+":"+"null"+"\t");
			        	}			        	
			        }*/
//			        System.out.println("father: "+father+"\tnodes: "+now.nodes+"\tlow_nodes: "+now.low_nodes  );
			        System.out.println("\tkeys: "+now.tree.keys+"\tlink size: "+now.tree.edges.size()  );					
					HashMap<String, String> low2high = now.low2high;
					HashSet<st_edge> links = now.tree.edges;
					for (String aa: low2high.keySet()){
						steiner_tree loo = clu_lst.get(Integer.valueOf(aa ) );
						ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+low2high.get(aa) +".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
						super_cluster  haha = (super_cluster) oina.readObject(); // 强制转换到Person类型  
						oina.close();  
//							System.out.println("aa: "+aa+"\thaha: "+haha.cluster_num+"\tlow2high: "+low2high.get(aa)+"\tloo: "+loo.cluster_number);
						clu_lst.put( Integer.valueOf(low2high.get(aa) )  ,
								new steiner_tree( low2high.get(aa)  , loo.keys, haha.nodes, loo.weight_all, loo.edges) );
					}	
					
				//					System.out.println(clu_lst.containsKey(clds[0]));					
					if (childs.size()==1){
						Integer chd = clds.get(0);			
						steiner_tree st_chld = clu_lst.get(chd);						
						//////////////////////////////////////
						tree = new steiner_tree(String.valueOf(father),st_chld.keys ,cluster_allkeys.nodes, st_chld.weight_all,st_chld.edges);
					}else {					
						for ( st_edge eg: links  ){

							String start = eg.beg;
							String tail = eg.end;
							int wg = eg.weight;
							if (start.equals(tail) || !isInteger(start) || !isInteger(tail)){
								continue;
							}
							
//							System.out.println("start: "+start+"\ttail: "+tail);
							steiner_tree tr1 = clu_lst.get( Integer.valueOf( start ) ); //////// local steiner tree 
							steiner_tree tr2 = clu_lst.get(Integer.valueOf( tail ) );
							ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+start+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst1 = (cluster_data) oina.readObject(); // 强制转换到Person类型  
							oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+tail+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst2 = (cluster_data) oina.readObject(); // 强制转换到Person类型  							
							oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+(father+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst_all = (cluster_data) oina.readObject(); // 强制转换到Person类型  			
							oina.close();  
							
							
							if (tr1==null){
//								System.out.println("tr1 null");
								if (tr2!=null){
									new_edges.addAll(tr2.edges);
									new_weight_all += tr2.weight_all;
									new_keys.addAll(tr2.keys);
								}
							}
							else if(tr2==null){
//								System.out.println("tr2 null");
								if (tr1!=null){
									new_edges.addAll(tr1.edges);
									new_weight_all += tr1.weight_all;
									new_keys.addAll(tr1.keys);
								}
							}else{
//								System.out.printf("tr1: %s  tr2: %s\n",tr1.cluster_number,tr2.cluster_number);
								new_edges.addAll( get_links( links_bt, tr1 , tr2 ,clst_all , clst1, clst2 ) );
								new_keys.addAll( tr1.keys );
								new_keys.addAll( tr2.keys );
								new_weight_all += tr1.weight_all + tr2.weight_all+wg;
								new_edges.addAll(tr1.edges);
								new_edges.addAll(tr2.edges);
							}							
//							
//							new_edges.addAll( get_links( links_bt, tr1 , tr2 ,clst_all , clst1, clst2 ) );
//							new_keys.addAll( tr1.keys );
//							new_keys.addAll( tr2.keys );
//							new_weight_all += tr1.weight_all + tr2.weight_all+wg;
//							new_edges.addAll(tr1.edges);
//							new_edges.addAll(tr2.edges);
						}						
						new_keys = new ArrayList<>(new HashSet<>(new_keys )) ;
						tree = new steiner_tree(String.valueOf(father),new_keys ,cluster_allkeys.nodes, new_weight_all, new_edges);
					}
					

					clu_lst.put(father, tree);
//					System.out.println("keys merged:\t "+tree.keys.size()+" " +tree.keys+"\n");//+ (new HashSet<>(tree.keys )).size()+" "
					records.add(father);
					System.out.println( "keys:  "+new_keys.size()+"\tedges: "+new_edges.size() +"\twg: "+new_weight_all);
					if (father.equals(root_cluster)){
						res = tree;
					}
					changed = 1;
				}
			}
/*			if (records.containsAll(not_con)){
				break;
			}*/
			if (changed==1){
				changed = 0;
				continue;
			}else{
				break;
			}
		}
		return res;
	}
	public static steiner_tree merge_stru_list_unfolded ( List<steiner_tree> local_Sts, HashMap<Integer, HashSet<Integer>> clu_child,
			HashMap<String, ArrayList<String>> subgra, ArrayList<String> allkeys, HashMap<String, graph_stru> stru_list  ) throws IOException, ClassNotFoundException{
		
		steiner_tree res = null;
		
		HashMap<Integer, steiner_tree> clu_lst = new HashMap<>();  ///////// cluster , local st
		for (steiner_tree steiner_tree: local_Sts){
			if (steiner_tree==null){
				continue;
			}
			clu_lst.put( Integer.valueOf(steiner_tree.cluster_number) , steiner_tree);
		}	
//		System.out.println(" root: "+root_cluster);
		Set<Integer> not_con = clu_child.keySet();
		HashSet<Integer> records = new HashSet<>();
		for (String string : subgra.keySet()){
			if (clu_child.keySet().contains( Integer.valueOf(string) )){
				continue;
			}
			records.add(Integer.valueOf(string));
		}
//		System.out.println("not con: "+not_con+"\trecords: "+records);
		
		int changed =0;
		while (records.size()>0){
			for (Integer father: not_con){
				HashSet<Integer> childs = clu_child.get(father);
				if ( records.containsAll(childs)==true && records.contains(father)==false){
					ArrayList<Integer> clds = new ArrayList<>(childs);  //// transfrom hashset to arraylist		
//					ArrayList<String> f_nodes = cluster_allkeys.getnodes();
//					System.out.println( "clds: "+clds.size());			
					
					ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(father+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
			        super_cluster  cluster_allkeys = (super_cluster) oin.readObject(); // 强制转换到Person类型  
			        oin.close();  
					HashSet<st_edge> links_bt = cluster_allkeys.links;
					System.out.println( "father: "+father+"\tchilds: "+childs +"  father cluster size:"+cluster_allkeys.getnodes());
			        			        
					steiner_tree tree=null;
					HashSet<st_edge> new_edges = new HashSet<>(); ////////  in this father cluster, edges to connect 2 son trees
					ArrayList<String> new_keys = new ArrayList<>();
					int new_weight_all = 0;
					
			        graph_stru now = stru_list.get(father+"");

//			        System.out.println("father: "+father+"\tnodes: "+now.nodes+"\tlow_nodes: "+now.low_nodes  );
			        System.out.println("\tkeys: "+now.tree.keys+"\tlink size: "+now.tree.edges.size()  );					
					HashMap<String, String> low2high = now.low2high;
					HashSet<st_edge> links = now.tree.edges;
					for (String aa: low2high.keySet()){
						steiner_tree loo = clu_lst.get(Integer.valueOf(aa ) );
						ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+low2high.get(aa) +".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
						super_cluster  haha = (super_cluster) oina.readObject(); // 强制转换到Person类型  
						oina.close();  
//							System.out.println("aa: "+aa+"\thaha: "+haha.cluster_num+"\tlow2high: "+low2high.get(aa)+"\tloo: "+loo.cluster_number);
						clu_lst.put( Integer.valueOf(low2high.get(aa) )  ,
								new steiner_tree( low2high.get(aa)  , loo.keys, haha.nodes, loo.weight_all, loo.edges) );
					}	

//					System.out.println(clu_lst.containsKey(clds[0]));					
					if (childs.size()==1){
						Integer chd = clds.get(0);			
						steiner_tree st_chld = clu_lst.get(chd);						
						//////////////////////////////////////
						tree = new steiner_tree(String.valueOf(father),st_chld.keys ,cluster_allkeys.nodes, st_chld.weight_all,st_chld.edges);
					}else {					
						for ( st_edge eg: links  ){

							String start = eg.beg;
							String tail = eg.end;
							int wg = eg.weight;
							if (start.equals(tail)  ||   !isInteger(start)  || !isInteger(tail)  ){
								System.out.println("start: "+start+"\ttail: "+tail);
								continue;
							}
							
//							System.out.println("start: "+start+"\ttail: "+tail);
							steiner_tree tr1 = clu_lst.get( Integer.valueOf( start ) ); //////// local steiner tree 
							steiner_tree tr2 = clu_lst.get(Integer.valueOf( tail ) );
							ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+start+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst1 = (cluster_data) oina.readObject(); // 强制转换到Person类型  
							oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+tail+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst2 = (cluster_data) oina.readObject(); // 强制转换到Person类型  							
							oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+(father+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst_all = (cluster_data) oina.readObject(); // 强制转换到Person类型  			
							oina.close(); 
							if (tr1==null){
//								System.out.println("tr1 null");
								if (tr2!=null){
									new_edges.addAll(tr2.edges);
									new_weight_all += tr2.weight_all;
									new_keys.addAll(tr2.keys);
								}
							}
							else if(tr2==null){
//								System.out.println("tr2 null");
								if (tr1!=null){
									new_edges.addAll(tr1.edges);
									new_weight_all += tr1.weight_all;
									new_keys.addAll(tr1.keys);
								}
							}else{
//								System.out.printf("tr1: %s  tr2: %s\n",tr1.cluster_number,tr2.cluster_number);
								new_edges.addAll( get_links( links_bt, tr1 , tr2 ,clst_all , clst1, clst2 ) );
								new_keys.addAll( tr1.keys );
								new_keys.addAll( tr2.keys );
								new_weight_all += tr1.weight_all + tr2.weight_all+wg;
								new_edges.addAll(tr1.edges);
								new_edges.addAll(tr2.edges);
							}
						}						
						new_keys = new ArrayList<>(new HashSet<>(new_keys )) ;
						tree = new steiner_tree(String.valueOf(father),new_keys ,cluster_allkeys.nodes, new_weight_all, new_edges);
					}
					

					clu_lst.put(father, tree);
//					System.out.println("keys merged:\t "+tree.keys.size()+" " +tree.keys+"\n");//+ (new HashSet<>(tree.keys )).size()+" "
					records.add(father);
					System.out.println( "keys:  "+new_keys.size()+"\tedges: "+new_edges.size() +"\twg: "+new_weight_all);
					if (father.equals(root_cluster)){
						res = tree;
					}
					changed = 1;
				}
			}
/*			if (records.containsAll(not_con)){
				break;
			}*/
			if (changed==1){
				changed = 0;
				continue;
			}else{
				break;
			}
		}
		return res;
	}
	
	public static steiner_tree merge_stru_list_fold( List<steiner_tree> local_Sts, HashMap<Integer, HashSet<Integer>> clu_child,
			HashMap<String, ArrayList<String>> subgra, ArrayList<String> allkeys, HashMap<String, graph_stru> stru_list, 
			HashMap<String, String>   gradson_son) throws IOException, ClassNotFoundException{
		
		HashMap<String, String> son_gradson = new HashMap<>();
		for (String key: gradson_son.keySet()){
			son_gradson.put(gradson_son.get(key), key);
		}
		if (son_gradson.size()!= gradson_son.size()){
//			System.out.println(" son!= gradson! ************\n**************\n***************  ");
		}
		
		
		steiner_tree res = null;
		
		HashMap<Integer, steiner_tree> clu_lst = new HashMap<>();  ///////// cluster , local st
		for (steiner_tree steiner_tree: local_Sts){
			if (steiner_tree==null){
				continue;
			}
			clu_lst.put( Integer.valueOf(steiner_tree.cluster_number) , steiner_tree);
//			System.out.println("cluster num: "+steiner_tree.cluster_number+" \tkeys: "+steiner_tree.keys );
		}	
//		System.out.println(" root: "+root_cluster);
		Set<Integer> not_con = clu_child.keySet();
		HashSet<Integer> records = new HashSet<>();
		for (String string : subgra.keySet()){
			if (clu_child.keySet().contains( Integer.valueOf(string) )){
				continue;
			}
			records.add(Integer.valueOf(string));
		}
//		System.out.println("not con: "+not_con+"\trecords: "+records);
		HashMap<Integer, ArrayList<String>>  test = new HashMap<>();
		HashMap<Integer, HashSet<String>> ddnn = new HashMap<>();
		
		int changed =0;
		while (records.size()>0){
			for (Integer father: not_con){
				HashSet<Integer> childs = clu_child.get(father);
				if ( records.containsAll(childs)==true && records.contains(father)==false){
					ArrayList<Integer> clds = new ArrayList<>(childs);  //// transfrom hashset to arraylist		

					
/*					ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(father+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
			        super_cluster  cluster_allkeys = (super_cluster) oin.readObject(); // 强制转换到Person类型  
			        oin.close();  			        
					HashSet<st_edge> links_bt = cluster_allkeys.links;
					ArrayList<String> fa_nodes = cluster_allkeys.nodes;*/
					
					HashSet<st_edge> links_bt = cluster_links.get(father+"");
					ArrayList<String> fa_nodes = cluster_son_set.get(father+"");
					
					steiner_tree tree=null;
					HashSet<st_edge> new_edges = new HashSet<>(); ////////  in this father cluster, edges to connect 2 son trees
					ArrayList<String> new_keys = new ArrayList<>();
					int new_weight_all = 0;
					
			        graph_stru now = stru_list.get(father+"");
					HashSet<st_edge> links = now.tree.edges;
					
					System.out.println( "father: "+father+"\tchilds: "+childs +"  father cluster size:"+fa_nodes);
//					System.out.println( "links size: "+links.size()+"\tlinks: "+ links);
//					for (st_edge aEdge: links){
//						System.out.print(aEdge.beg+"->"+aEdge.end+"\t");
//					}System.out.println();
					
					if (childs.size()==1){
						Integer chd = clds.get(0);			
						steiner_tree st_chld = clu_lst.get(chd);						
						//////////////////////////////////////
//						System.out.println("*************\n*********************\n**********************" );
						tree = new steiner_tree(String.valueOf(father),st_chld.keys , fa_nodes, st_chld.weight_all,st_chld.edges);
					}else {					
						for ( st_edge eg: links  ){
								
							String start = eg.beg;
							String tail = eg.end;
							int wg = eg.weight;
							if (start.equals(tail)  ||   !isInteger(start)  || !isInteger(tail)  ){
								System.out.println("hehhehhehehh**************\n*************************\n***********888");
								continue;
							}
							
							steiner_tree tr1 = clu_lst.get( Integer.valueOf( start ) ); //////// local steiner tree 
							steiner_tree tr2 = clu_lst.get(Integer.valueOf( tail ) );
//							System.out.println((tr1==null) +"\t"+ (tr2==null));
							String st_son = null, tl_son = null;
							if ( son_gradson.keySet().contains(start)){
								st_son = son_gradson.get(start);
								tr1 = clu_lst.get(Integer.valueOf(st_son));
							}
							if ( son_gradson.keySet().contains(tail)){
								tl_son = son_gradson.get(tail);
								tr2 = clu_lst.get(Integer.valueOf(tl_son));
							}		
							System.out.println("start: "+start+"\ttail: "+tail);
//							System.out.println( " st son: "+st_son+"\ttl son: "+tl_son);//+"\tson_grand: "+son_gradson);
							
							ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+start+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst1 = (cluster_data) oina.readObject(); // 强制转换到Person类型  
							oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+tail+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst2 = (cluster_data) oina.readObject(); // 强制转换到Person类型  		
							oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+(father+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
							cluster_data  clst_all = (cluster_data) oina.readObject(); // 强制转换到Person类型  			
							oina.close(); 
							
//							System.out.println( "tr1 null? "+(tr1==null)+"\ttr2 null? "+(tr2==null) );
							
							if (tr1==null && tr2==null){
								ArrayList<String> ppkeys_1 = new ArrayList<String>();
								ppkeys_1.add(clst1.nodes.get(0));
								ArrayList<String> ppkeys_2 = new ArrayList<String>();
								ppkeys_2.add(clst2.nodes.get(0));
								tr1 = new steiner_tree(tail, ppkeys_1, clst1.nodes , 0, new HashSet<st_edge>());
								tr2 = new steiner_tree(tail, ppkeys_2, clst2.nodes , 0, new HashSet<st_edge>());
								new_edges.addAll( get_links( links_bt, tr1, tr2, clst_all, clst1, clst2 ) );
								tr1.keys = new ArrayList<String>();
								tr2.keys = new ArrayList<String>();
//								System.out.println( "1......."+tr1.keys+"\t"+tr2.keys   );
							}else if (tr1==null ){
								ArrayList<String> ppkeys = new ArrayList<String>();
								ppkeys.add(clst1.nodes.get(0));
								tr1 = new steiner_tree(tail, ppkeys, clst1.nodes , 0, new HashSet<st_edge>());
								new_edges.addAll( get_links( links_bt,  tr1, tr2 ,clst_all , clst1, clst2 ) );
								tr1.keys = new ArrayList<String>();
//								System.out.println("2................"+tr1.keys	+"\t"+tr2.keys);
							}else if (tr2==null){
								ArrayList<String> ppkeys = new ArrayList<String>();
								ppkeys.add(clst2.nodes.get(0));
								tr2 = new steiner_tree(tail, ppkeys, clst2.nodes , 0, new HashSet<st_edge>());
								new_edges.addAll( get_links( links_bt, tr1, tr2, clst_all , clst1, clst2 ) );
								tr2.keys = new ArrayList<String>();
//								System.out.println("3............."+tr1.keys+"\t"+tr2.keys);
							}else{
								new_edges.addAll( get_links( links_bt, tr1, tr2, clst_all , clst1, clst2 ) );
//								System.out.println("4......."+tr1.cluster_number+" "+tr1.keys+"\t"+tr2.cluster_number+" "+tr2.keys);
							}
							
//							new_edges.addAll( get_links( links_bt, tr1 , tr2 ,clst_all , clst1, clst2 ) );
//							new_keys.addAll(test.get(Integer.valueOf(tr1.cluster_number)));
//							new_keys.addAll(test.get(Integer.valueOf(tr2.cluster_number)));

							new_keys.addAll( tr1.keys );
							new_keys.addAll( tr2.keys );
							new_weight_all += tr1.weight_all + tr2.weight_all+wg;
							new_edges.addAll(tr1.edges);
							new_edges.addAll(tr2.edges);
							
						}						
						new_keys = new ArrayList<>(new HashSet<>(new_keys )) ;
						tree = new steiner_tree(String.valueOf(father),(ArrayList<String>) new_keys.clone() , fa_nodes, new_weight_all, new_edges);
//						System.out.println("father: "+father+" \tkeys: "+new_keys);						
					}
					
					if (clu_lst.containsKey(father)){
						System.out.println("here ........."+father);
					}
					clu_lst.put(father, tree);
//					System.out.println("keys merged:\t "+tree.keys.size()+" " +tree.keys+"\n");//+ (new HashSet<>(tree.keys )).size()+" "
					records.add(father);			
//					test.put(father, new_keys);					
					
//					System.out.println(ana_res(new_edges, new_keys)+"\n" );
					if (father.equals(root_cluster)){
						res = tree;
					}
					changed = 1;
				}
			}
			if (changed==1){
				changed = 0;
				continue;
			}else{
				break;
			}
		}
/*		for (Integer aa: test.keySet()){
			System.out.println(aa+"\n"+test.get(aa)+"\n"+clu_lst.get(aa).keys );
		}*/
		
		
		
		return res;
	}
	
	
	public static HashSet<st_edge> get_links (HashSet<st_edge> links, steiner_tree tr1 ,steiner_tree tr2,cluster_data  clst_all, cluster_data  clst1, cluster_data  clst2){
		
		int merged = 0;
		ArrayList<String> nodes1 = clst1.getnodes(); /////// cluster's nodes (all original nodes )
		ArrayList<String> nodes2 = clst2.getnodes();
		
		ArrayList<String> link_nodes1 = new ArrayList<>(); ////// links between 2 clusters
		ArrayList<String> link_nodes2 = new ArrayList<>();
		ArrayList<Integer> link_weights = new ArrayList<>();
		
		for (st_edge eg: links){
			if (  nodes1.contains(eg.beg) &&  nodes2.contains(eg.end)  ){
				link_nodes1.add(eg.beg);
				link_nodes2.add(eg.end);
				link_weights.add(eg.weight);
			}
			if ( nodes1.contains(eg.end) &&  nodes2.contains(eg.beg)){
				link_nodes1.add(eg.end);
				link_nodes2.add(eg.beg);
				link_weights.add(eg.weight);
			}
		}
		
		link_nodes1 = new ArrayList<String>(new HashSet<String>(link_nodes1));
		link_nodes2 = new ArrayList<String>(new HashSet<String>(link_nodes2));

//		System.out.println( "nodes1: "+ nodes1.size() +"\tlink nodes1: "+link_nodes1.size()+"\t"+
//				link_nodes1 +"\n nodes2: "+nodes2.size() +"\tlink nodes2: "+link_nodes2.size()+"\t"+link_nodes2 );
		ArrayList<String> st_nodes1 = tr1.keys; /// nodes in local steiner tree,if no tree, just keys 
		ArrayList<String> st_nodes2 = tr2.keys;
		for ( st_edge eg: tr1.edges ){
			if (!st_nodes1.contains(eg.beg)){
				st_nodes1.add(eg.beg);
			}
			if (!st_nodes1.contains(eg.end)){
				st_nodes1.add(eg.end);
			}
		}
		for ( st_edge eg: tr2.edges ){
			if (!st_nodes2.contains(eg.beg)){
				st_nodes2.add(eg.beg);
			}
			if (!st_nodes2.contains(eg.end)){
				st_nodes2.add(eg.end);
			}								
		}
		st_nodes1 = new ArrayList<String>(new HashSet<String>(st_nodes1));
		st_nodes2 = new ArrayList<String>(new HashSet<String>(st_nodes2));
		
//		System.out.println("st 1: "+tr1.cluster_number+"\t nodes: "+st_nodes1+"\n st2: "+
//				tr2.cluster_number+"\tnodes: "+st_nodes2);
		HashSet<st_edge> new_edges = new HashSet<>();
		int flag = 0;
		int num = 0;
		for (st_edge eg: links){
//			System.out.println(eg.beg+"\t"+eg.end );
			if ( (st_nodes1.contains(eg.beg) && st_nodes2.contains(eg.end)) || 
					(st_nodes1.contains(eg.end) && st_nodes2.contains(eg.beg))  ){
				flag = 1;
				merged = 1;
				new_edges.add(eg);				
				num += 1;
				break;
			}
		}
		if (flag==0){
			link_nodes1.retainAll(st_nodes1);
			if ( link_nodes1.size() > 0 ){				
				for (st_edge eg: links){
					String port = null;
					if (st_nodes1.contains(eg.beg)){
						port = eg.end;
					}
					if (st_nodes1.contains(eg.end)){
						port = eg.beg;
					}
					if (!nodes2.contains(port)){
						continue;
					}
					if (port!=null){
//						System.out.println(eg.beg+"\t"+eg.end );
						num += 1 ;
						HashSet<st_edge>  more_edges = get_path_bfs( clst2, port, st_nodes2 );
						if (more_edges!=null){
							flag = 1;
							merged = 1;
							new_edges.addAll(more_edges);
							new_edges.add(eg);
							break;
						}
					}
				}
			}
			link_nodes2.retainAll( st_nodes2 );
			if (flag==0 && link_nodes2.size() > 0 ){
				for (st_edge eg: links){
					String port = null;
					if (st_nodes2.contains(eg.beg) ){
						port = eg.end;
					}
					if (st_nodes2.contains(eg.end)){
						port = eg.beg;
					}
					if (!nodes1.contains(port)){
						continue;
					}
					
					if (port!=null){
//						System.out.println(eg.beg+"\t"+eg.end );
						num += 1;
						HashSet<st_edge>  more_edges = get_path_bfs( clst1, port, st_nodes1 );
						if (more_edges!=null){
							flag = 1;
							merged = 1;
							new_edges.addAll(more_edges);
							new_edges.add(eg);
							break;
						}
					}
				}
			}
			if (flag==0){
				for (st_edge eg: links){					
					String tp1=null ,tp2=null;
					if (nodes1.contains(eg.beg)){
						tp1 = eg.beg;
						tp2 = eg.end;
					}else{
						tp1 = eg.end;
						tp2 = eg.beg;
					}
					if (nodes1.contains(tp1) && nodes2.contains(tp2) ){
//						System.out.println(eg.beg+"\t"+eg.end );
						num += 1;
						ArrayList<String> sinks =  new ArrayList<String>();
						sinks.add(tp2);
						ArrayList<String> sinks_2 =  new ArrayList<String>();
						sinks_2.add(tp1);
						HashSet<st_edge>  more_edges = get_path_bfs( clst_all, tp1, sinks );
						HashSet<st_edge>  more_edges_1 = get_path_bfs( clst_all, tp2, sinks_2);
						if (more_edges!=null && more_edges_1!=null && more_edges.size()>0 && more_edges_1.size()>0   ){
							flag = 1;
							merged = 1;
							new_edges.addAll(more_edges);
							new_edges.addAll(more_edges_1);
							new_edges.add(eg);
							break;
						}
					}

				}
			}			
		}
//		System.out.println( "merged: "+merged+"\ttr1: "+tr1.cluster_number+"\ttr2: "+tr2.cluster_number   );
		System.out.println(" test edges num: "+num);
		return new_edges;
	}
	public static HashSet<st_edge> get_path_bfs(cluster_data clst, String sour, ArrayList<String> ends ){
		
		int[][] dis = clst.getDis();
		ArrayList<String> nodes = clst.getnodes();
		int source = nodes.indexOf( sour ) ;
		ArrayList<Integer> sinks = new ArrayList<>();
//		System.out.println("sinks:   source:"+source);
		for (String a: ends){
			sinks.add(nodes.indexOf(a));
//			System.out.println(nodes.indexOf(a)+" "+a);
		}
//		System.out.println("sinks ends: "+sinks.size()+" "+ends.size());
		int sink = -1;
		
		HashMap<Integer, HashSet<Integer>> sons = new HashMap<>();
		HashMap<Integer, Integer> father = new HashMap<>();
		int[] vs = new int[dis.length];
		for (int i=0;i<dis.length;i++){
			vs[i] = 0;
		}
		vs[source] = 1;
		
		Queue<Integer> qq = new LinkedList<>();
		qq.add(source);
		
		while (!qq.isEmpty()){
			int start = qq.poll();
			HashSet<Integer> nexts = new HashSet<>();
			for (int i=0;i<dis.length;i++ ){
				if (dis[start][i]>0 && vs[i]==0 ){
					vs[i] = 1;
					nexts.add(i);
					qq.add(i);
					father.put(i, start);
				}
			}
			sons.put(start, nexts);
			
			int fg = 0;
			for (Integer b: sinks){
				if (nexts.contains(b)){
					sink = b;
					fg=1;
					break;
				}
			}
			if (fg==1){
				break;
			}
		}
		if (sink==-1 ||  vs[sink]==0){
			System.out.println("get path bfs: ************  not linked!!!\n\t"+sour +"\t "+ ends);
			return null;
		}
//		System.out.println("sink: " +sink );

		
		HashSet<st_edge> rEdges = new HashSet<>();
		int las = father.get(sink);
		rEdges.add(new st_edge ( nodes.get(las) , nodes.get(sink ) , dis[las][sink]));
		while (las!=source){
			rEdges.add(new st_edge( nodes.get(father.get(las)) ,  nodes.get( las ) , dis[father.get(las)][las]));
			las = father.get(las);
		}
		
		return rEdges;
	}
	
	
	public static int[][] get_new_matrix_from_old( int[][] dis, ArrayList<String> nodes, ArrayList<String> nw_nodes  ) {
		int[][] nw_dis = new int[nw_nodes.size()][nw_nodes.size()] ;
		for (int i=0;i<nw_nodes.size();i++){
			for (int j=0;j<nw_nodes.size();j++){
				nw_dis[i][j] = dis[nodes.indexOf( nw_nodes.get(i) )][ nodes.indexOf( nw_nodes.get(j) ) ];			
			}
		}
		return nw_dis;
	}
	
	public static HashMap<String, ArrayList<String>> graph_min_cut(ArrayList<String> keys, int[][] part_matrix, int[][] part_mtx_father){
		
		 ///////// min-cut algorithm  /////////
		 mincut mc = new mincut();
		 	 			 
		 HashSet<ArrayList<String> > min_clus = new HashSet<>();
		 Queue<ArrayList<String> > need_to_split = new LinkedList();
		 final int sub_size = 4 ;
		 int mx_size = 0;
		 int source=0,sink=1;
		 Random random = new Random();
		 source = random.nextInt(keys.size());
		 sink = random.nextInt(keys.size());
		 while (source==sink){
			 sink = random.nextInt(keys.size());
		 }
		 
		 HashSet<ArrayList<String>> mins = mc.get_mincut(keys, part_matrix, source, sink);
		
		 for (ArrayList<String> ele : mins ){
			 if (ele.size()>mx_size){
				 mx_size = ele.size();
			 }
		 }
		 while (mx_size>sub_size){		
//			 System.out.println(" mx_size: "+mx_size  );
			 mx_size = 0;		 
			 for (ArrayList<String> ele : mins ){
				 if (ele.size()>mx_size){
					 mx_size = ele.size();
				 }
				 if (ele.size()>sub_size){
					 need_to_split.add(ele);
				 }else{
					 min_clus.add(ele);
				 }				 
			 }	
			 
			 if (need_to_split.isEmpty()){
				 break;
			 }
			ArrayList<String> pppa = need_to_split.poll();
			int[][] pp_dis = get_new_matrix_from_old(part_matrix, keys, pppa);				
			source = 0;
			sink = 1;
			 source = random.nextInt(keys.size());
			 sink = random.nextInt(keys.size());
			 while (source==sink){
				 sink = random.nextInt(keys.size());
			 }
			mins = mc.get_mincut(pppa, pp_dis, 0, 1);
		 }
//		  System.out.println("mc:\t"+min_clus.size());
		  HashMap<String, ArrayList<String>> res = new HashMap<>();
		  for (ArrayList<String> pp: min_clus){				  
			  int sjd = -1;
			  if (pp.size()>=2){
				  int lvv = 100;
				  for (int m=0;m<pp.size();m++){
					  for (int n=0;n<pp.size();n++){
						if (part_matrix[m][n] <lvv){
							sjd = part_mtx_father[m][n];
							lvv = part_matrix[m][n];
						}
					  }
				  }
			  }else{
				  sjd = part_mtx_father[keys.indexOf(pp.get(0))][keys.indexOf(pp.get(0))];
			  }
			  
//			  System.out.print("cluster: "+sjd + "\tkeys: ");  //////  "\tsize: "+pp.size()+
			  res.put(String.valueOf(sjd), pp);
/*			  for (String aa: pp){
				  System.out.print(aa+" ");
			  }
			  System.out.println();*/
		  }
		return res;		
	}
	
	
	public void unfold_a_level(super_cluster dataAction ) throws FileNotFoundException, IOException, ClassNotFoundException{

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> nodes = dataAction.getnodes();
        HashMap<String, String> keys2nodes = new HashMap<>();
        
        ArrayList<String> added_nodes = new ArrayList<>() ;
		for (String aa: keys){
			String bb = aa;
			int lv = 0;
			while (!nodes.contains( bb)){
				bb = child_parent.get(bb);
				lv ++;
			}
			HashMap<Integer , ArrayList<String>> som = new HashMap<>();
			ArrayList<String> tmp = new ArrayList<>();
			tmp.add(aa);
			som.put(0, tmp);
			for (int k=1;k<lv;k++){
				tmp.clear();
				for (String key: som.get(k-1)){
				    ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(key+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
			        super_cluster dd = (super_cluster) oin.readObject(); // 强制转换到Person类型  
			        oin.close();  
			        tmp.addAll(dd.getnodes());
				}
			}
			added_nodes.addAll(som.get(lv));

		}
        
        
        
	}
	
	public static  ArrayList<Tuple2<Integer, Integer> >  ana_res(HashSet<st_edge> edges, ArrayList<String> keys){
		ArrayList<st_edge> list_edges = new ArrayList<>(edges);
//		System.out.println("Analysis results: \nkeys: "+keys);
		ArrayList<Tuple2<Integer, Integer>> rtt = new ArrayList<>()  ;
		
		ArrayList<String> nodes = new ArrayList<>();
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
			
			Queue<String> qq = new LinkedList<>();
			ArrayList<String> part = new ArrayList<>();
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
				
		System.out.println("rtt: "+rtt);
		return rtt;		
	}
	
	
	public static   HashMap<String, String>  del_singleKey_cluster(HashMap<String, ArrayList<String>> subgra, 
			HashMap<Integer, HashSet<Integer>> stru) throws FileNotFoundException, IOException, ClassNotFoundException{
		
		HashMap<String, HashSet<String>> hmp = new HashMap<>();		
		for (Integer kk : stru.keySet()){
			HashSet<Integer> kk_sons = stru.get(kk);
			String mm = kk+"";
			HashSet<String> mm_sons = new HashSet<>();
			
			for (Integer a: kk_sons){
				mm_sons.add(a+"");
			}
			hmp.put(mm, mm_sons);
		}
		
		HashMap<String, String> ressss = new HashMap<>();
		
		Set<String> sub_clus = subgra.keySet();
		for (String key: hmp.keySet()){
			
//		    ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(key+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
//	        super_cluster dd = (super_cluster) oin.readObject(); // 强制转换到Person类型  
//	        oin.close();  
//			ArrayList<String> nodes = dd.nodes;
			
			ArrayList<String> nodes = cluster_son_set.get(key);
			
			HashSet<String> values = hmp.get(key);
			HashSet<String> gg_values = new HashSet<>();
//			System.out.println("key: "+key+"\tvalues: "+values+"\tnodes: "+nodes);
			int flg = 0;
			for (String aa: values){
				if (nodes.contains(aa)){
					gg_values.add(aa);
					flg = 1;
					continue;
				}
				
				String ff = child_parent.get(aa);
				while (!nodes.contains(ff)){
					ff = child_parent.get(ff);
				}
				gg_values.add(ff);
				ressss.put(aa, ff);
			}
//			System.out.println(flg+"\tgg_values: "+gg_values);
			if (values.size()>gg_values.size()){
				System.out.println("hehhhheh  2 gradson , 1 son    *************\n*****************\n*************");
			}
		}

		
		return  ressss;		
	}
	
	public static steiner_tree  merge_stru_center_node(List<steiner_tree> local_Sts, HashMap<Integer, HashSet<Integer>> clu_child,
			HashMap<String, ArrayList<String>> subgra,ArrayList<String> allkeys ) throws FileNotFoundException, IOException, ClassNotFoundException{
		steiner_tree fn_tree =  null;
		
		HashMap<Integer, steiner_tree> clu_lst = new HashMap<>();  ///////// cluster , local st
		for (steiner_tree steiner_tree: local_Sts){
			if (steiner_tree==null){
				continue;
			}
//			System.out.println( steiner_tree.cluster_number+"\t"+steiner_tree.keys  );
//			System.out.println(  entleaf.get(steiner_tree.cluster_number)   );
//			ana_res( steiner_tree.edges, steiner_tree.keys);
			clu_lst.put( Integer.valueOf(steiner_tree.cluster_number) , steiner_tree);
		}	
//		System.out.println(" root: "+root_cluster);
		Set<Integer> not_con = clu_child.keySet();
		HashSet<Integer> records = new HashSet<>();
		for (String string : subgra.keySet()){
			if (clu_child.keySet().contains( Integer.valueOf(string) )){
				continue;
			}
			records.add(Integer.valueOf(string));
		}
//		System.out.println("not con: "+not_con+"\trecords: "+records);
		
		cluster_data  clst_father = clust_whole;
		int changed =0;
		while (records.size()>0){
			for (Integer father: not_con){
				HashSet<Integer> childs = clu_child.get(father);
				if ( records.containsAll(childs)==true && records.contains(father)==false){
					long tmst = System.currentTimeMillis();
					String father_center_node = cluster_center_node.get(father+"");
//					ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+(father+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
//					cluster_data  clst_father = (cluster_data) oina.readObject(); // 强制转换到Person类型  			
//					oina.close();  
//					System.out.println("time for load graph: "+ (System.currentTimeMillis() - tmst) );
					ArrayList<String> fa_nodes = entleaf.get(father+"");
					
					System.out.println( "father: "+father+"\tchilds: "+childs);
					ArrayList<Integer> clds = new ArrayList<>(childs);  //// transfrom hashset to arraylist								

					steiner_tree tree=null;
					HashSet<st_edge> new_edges = new HashSet<>(); ////////  in this father cluster, edges to connect 2 son trees					
					ArrayList<String> new_keys = new ArrayList<>();
					int new_weight_all = 0;
					
					for (Integer ii: clds){
						steiner_tree st_chld = clu_lst.get(ii);	
						new_keys.addAll(st_chld.keys);
						new_edges.addAll(st_chld.edges);
//						ana_res(st_chld.edges, st_chld.keys);
						
						ArrayList<String> tree_nodes = new ArrayList<>();
						HashSet<String> tree_nodes_Set = new HashSet<>();
						for (st_edge dg: st_chld.edges){
							tree_nodes_Set.add(dg.beg);
							tree_nodes_Set.add(dg.end);
							new_weight_all += dg.weight;
						}
						tree_nodes_Set.addAll(st_chld.keys);
						tree_nodes = new ArrayList<String>(tree_nodes_Set);
//						System.out.println( father_center_node+"\t"+ii +"\t"+tree_nodes );
						
//						System.out.println(fa_nodes.containsAll(tree_nodes)+"\t center in tree:"+tree_nodes.contains(father_center_node)+"\t"+fa_nodes.contains(father_center_node)  );
						if (!tree_nodes.contains(father_center_node)){							
							HashSet<st_edge> alink = get_path_bfs(clst_father, father_center_node, tree_nodes);
//							System.out.println("\tcld: "+ ii+"\tsize: "+alink.size());
							new_edges.addAll(alink);						
							for (st_edge dg: alink){
//								System.out.print(dg.beg+"->"+dg.end+"\t" );
								new_weight_all += dg.weight;
							}
//							System.out.println("\nalink size: "+alink.size() );
						}
					}					
//					System.out.println("keys: "+new_keys );
					tree = new steiner_tree(String.valueOf(father),new_keys ,fa_nodes, new_weight_all, new_edges);							
					clu_lst.put(father, tree);
					records.add(father);
//					System.out.println("keys num: "+new_keys.size()+"\tedges num: "+new_edges.size());
/*					ana_res(new_edges, new_keys);
					int num = 0;
					for (String key: allkeys){
						if (fa_nodes.contains(key)){
							num += 1;
						}
					}
					System.out.println( num+"\t"+new_keys.size()  );*/
					
					
					if (father.equals(root_cluster)){
						fn_tree = tree;
					}
					changed = 1;
				}
			}
/*			if (records.containsAll(not_con)){
				break;
			}*/
			if (changed==1){
				changed = 0;
				continue;
			}else{
				break;
			}
		}
//		ana_res(fn_tree.edges, allkeys);
		return fn_tree;
	}
	
	
	
	
	
	
		
	public static void main( String[] args ) throws IOException, ClassNotFoundException
    {				
	/////// /////           change the deploy mode , just need to change the following line !              /////// //////
		long tm1 = System.currentTimeMillis();
		long tm5,tm4,tm3;
		
//        SparkConf conf=new SparkConf().setAppName("bio spark app").setMaster("spark://master:7077");//local[4]
        SparkConf conf=new SparkConf().setAppName("bio spark app").setMaster("local[4]");
		JavaSparkContext sc =new JavaSparkContext(conf);		    
		
		ObjectInputStream oincp = new ObjectInputStream(new FileInputStream("/home/lee/biolg/par_childs.dat")); //////  "/home/lee/biolg/cache/5363.dat" 
	    parent_childs_set = (HashMap<String, HashSet<String>>) oincp.readObject(); // 强制转换到Person类型  
	    
	    oincp = new ObjectInputStream(new FileInputStream("/home/lee/biolg/child_parent.dat"));
	    child_parent = (HashMap<String, String>) oincp.readObject(); 	    
	    
	    oincp = new ObjectInputStream(new FileInputStream("/home/lee/biolg/cluster2level.dat"));
	    cluster2level = (HashMap<Integer, Integer>) oincp.readObject(); 	 
	    
//	    oincp = new ObjectInputStream(new FileInputStream("/home/lee/biolg/snbt_orgiedges.dat"));
//	    snbt_edges = (HashMap<Tuple2<Integer, Integer >, Integer>) oincp.readObject(); 
	    
		oincp = new ObjectInputStream(new FileInputStream("/home/lee/biolg/nodes2integer.dat"));
		nodes2itg = (HashMap<String, Integer>) oincp.readObject(); 	
		  
	    oincp.close();  		
	    System.out.println("parent_childs_set size: "+parent_childs_set.size()+"\tchild_parent size: "+ child_parent.size());
		
	    
		String fr1 = spath + "Leaf.dat";
		ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
		entleaf = ms1.ent;  		
		System.out.println("  size entleaf: "+entleaf.size());
		
		/////////// load the whole graph from disk //////
		ObjectInputStream oina = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+(9924+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
		clust_whole = (cluster_data) oina.readObject(); // 强制转换到Person类型  			
		oina.close();  		
		System.out.println("the whole graph size: "+clust_whole.nodes.size()); 
		 
		//////// get centre node /////
		String dis_file = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/AdjcentMatrix.csv" ;
		cluster_center_node = cs.get_center_nodes( dis_file,entleaf);
		System.out.println("center node size: "+cluster_center_node.size());
		
   		ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/cluster_size.dat")); //////  "/home/lee/biolg/cache/5363.dat" 
		cluster_size = (HashMap<String, Integer>) oin.readObject(); // 强制转换到Person类型  
	    oin.close();  

	    cluster_son_set = cs.get_cluster_son_Set(parent_childs_set);
	    cluster_links = cs.get_cluster_links(parent_childs_set);
		System.out.println("cluster_son_set: "+cluster_son_set.size());
		Tuple2<int[][], int[][]> ma2 = cs.getMatrix(spath+"nodes_sorted.csv");			
		HashMap<String, Integer> nls_bykey = cs.nls_bykey;		///// this is for ma2, not similar to nodes2itg
		
/*		for (String key: nls_bykey.keySet()){         ///// test if nls_bykey == nodes2itg ?
			if (!nls_bykey.get(key).equals( nodes2itg.get(key) )){
				System.out.println(nls_bykey.get(key)+"\t"+ nodes2itg.get(key));
			}
		}*/
				
		/////        read matrix, index from local       //// 
/*		oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/ances_matrix.dat")); //////  "/home/lee/biolg/cache/5363.dat" 
		Tuple2<int[][], int[][]> ma2 =  (Tuple2<int[][], int[][]>) oin.readObject();
		oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/nlsbykey_mat.dat")); //////  "/home/lee/biolg/cache/5363.dat" 
		HashMap<String, Integer> nls_bykey =  (HashMap<String, Integer>) oin.readObject();
		oin.close();  */
		
		matrix = ma2._1;     ////////   level ,lowest common ansester's  level   //////// 
		mat_father = ma2._2;     ////////   cluster number ,lowest common ansester's  cluster number  ////////	
		String disfr = spath + "EdgeDisMatrix.csv";
	
/*		int wsa = write_clusters_local(cs);	 ///// write  clusters into local (nodes including nodes and clusters) 
		if (wsa>0){
			return ;
		}*/		
		
		
		long tm2 = System.currentTimeMillis();
		System.out.println("time:"+(tm2-tm1)+" ms!" );
		
		////////////        get the 50 keys from text          ///////////
		ArrayList<String> read = new ArrayList<String>();
		
		String input_file = path+"set_one.csv";  ///////  10
		input_file = path + "/set_four/15.csv"; //////// 25,20,15,5
		
		read = fraw.ReadData(input_file);
		ArrayList<ArrayList<String>> allkeys = new ArrayList<ArrayList<String>>();		
		for(String s:read){
			ArrayList<String> subkeys = new ArrayList<String>();
			String[] ss = s.split(",");
			for(int j = 0; j<ss.length;j++){
				subkeys.add(ss[j]);		
			}
			allkeys.add(subkeys);
		}
		System.out.println("all keys size: " + allkeys.size());
		
		int i=-1;
		HashSet<Integer> ign = new HashSet<>();
		ign.add(2);
		
		ArrayList<Integer> gpcut_tm = new ArrayList<>();
		ArrayList<Integer> mp_tm = new ArrayList<>();
		ArrayList<Integer> rdc_tm = new ArrayList<>();
		ArrayList<Integer> add_node_num = new ArrayList<Integer>();
		ArrayList<Integer> tree_wgt = new ArrayList<Integer>();		
		
		while (++i<allkeys.size()){		
/*			if (i>=25){  /////keys 20
				continue;
			}*/
/*			if (i>50 || i==20 || i==35 || i==48){  /////keys 10
				continue;
			}*/
			if (i>5 || i==5){ ////// keys 25
				continue;
			}			
			
			tm3 = System.currentTimeMillis();		
		
			ArrayList<String> keys = allkeys.get(i);			 
			System.out.println("\n\nnum: "+i+"\tkeys: "+keys.size()+"\t"+keys);
//			System.out.println("the tree contains all the keys?  "+ entleaf.get(cs.tree_root).containsAll(keys));
//		 System.out.println( "the least public root is: " + cs.getAllkeywordsRoot(keys)+"\n\n"); 			 
			 
			 System.out.println("clusters:");  ///////   cluster number,   cluster's including keys
			 HashMap<String, ArrayList<String>> subgra = cs.getSubGraphList(keys, matrix , mat_father, cs.nls_bykey );
			 int[][] part_matrix = cs.part_matrix;
			 int[][] part_mtx_father = cs.part_mtx_father;
			 System.out.println("time for stru list: "+(System.currentTimeMillis()-tm3));			 
			 HashMap<String, graph_stru> stru_list = new HashMap<>();
			 ////// structure   /////////
			 HashMap<Integer, HashSet<Integer>> stru =  get_stru(part_mtx_father, part_matrix, subgra ,keys);  

			 System.out.println("subgra: "+subgra);
			 System.out.println("stru: "+stru);		
//			 System.out.println("time for stru list: "+(System.currentTimeMillis()-tm3));	
			 HashMap<String, String> gradson2son = del_singleKey_cluster(subgra, stru);
//			 System.out.println(" stru: " + stru.size() +" root: "+root_cluster+"\t stru list size: "+ stru_list.size());	
			 System.out.println("time for stru list: "+(System.currentTimeMillis()-tm3));		
			 
			 //////// across levels, unfold all  //////
			 ArrayList< Tuple2<Integer, HashSet<Integer>> > lsList = new ArrayList<>();
			 for (Integer a: stru.keySet()){
				 lsList.add(new Tuple2<>(a,stru.get(a)));			 
			 }
			 JavaRDD<Tuple2<Integer, HashSet<Integer>>> rdd_ls = sc.parallelize( lsList );	 					
			 JavaRDD<Tuple2<String, graph_stru>> stru_ls =  rdd_ls.map(new Function<Tuple2<Integer, HashSet<Integer>>, Tuple2<String, graph_stru>>() {
				 public Tuple2<String, graph_stru> call(Tuple2<Integer, HashSet<Integer>>  ls) throws IOException, ClassNotFoundException{	
					 Integer key = ls._1;
					 HashSet<Integer> sons = ls._2;					

				    ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(key+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
			        super_cluster dataAction = (super_cluster) oin.readObject(); // 强制转换到Person类型  
			        oin.close();
			        
//			        System.out.println("father: "+key+"\t sons: "+ sons +"\tnodes: "+dataAction.nodes);
			        
			        ArrayList<String> keys = new ArrayList<>();
			        for (Integer ii: sons){
			        	keys.add(ii+"");
			        }
			        ArrayList<String> nodes = dataAction.getnodes();
			        int[][] dis = dataAction.getDis();
			        HashSet<st_edge> links = dataAction.links;
			        HashMap<Tuple2<String, String>, Integer> tp_links = dataAction.tp_links;
			        
//			        System.out.println("links size: "+tp_links.size() );
			        ArrayList<String> lownodes = new ArrayList<>();
			        HashMap<String, String> keys2nodes = new HashMap<>();
			        
			        /////////////    unfold across levels         //////////////
			        ArrayList<String> new_nodes = new ArrayList<>() ;
					for (String aa: keys){
						if (!nodes.contains(aa )){
							lownodes.add(aa);
						}
					
						String bb = aa;
						int lv = 0;
						while (!nodes.contains( bb)){
							bb = child_parent.get(bb);
							lv ++;
						}
//						System.out.println("aa: " +aa+"\tbb: "+bb+" "+lv);
						
						HashMap<Integer , ArrayList<String>> som = new HashMap<>();
						ArrayList<String> tmp = new ArrayList<>();
						tmp.add(bb);
						som.put(0, (ArrayList<String>) tmp.clone());
						for (int k=1;k<=lv;k++){
							tmp.clear();
							for (String kk: som.get(k-1)){
								if (!isInteger(kk)){
									tmp.add(kk);
									continue;
								}
							    oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+kk+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
						        super_cluster dd = (super_cluster) oin.readObject(); // 强制转换到Person类型  
						        oin.close();  
						        tmp.addAll(dd.nodes);
							}
							som.put(k, (ArrayList<String>) tmp.clone());
						}
//						System.out.println("aa: "+aa+" "+lv+"\t sons:"+som.get(lv) );
						new_nodes.addAll(som.get(lv));
//						System.out.println(added_nodes);
						keys2nodes.put(aa+"", bb);
					}
									
					int[][] new_dis = new int[new_nodes.size()][new_nodes.size()];
					for (int i=0;i<new_nodes.size();i++){
						for (int j=0;j<new_nodes.size();j++ ){
							new_dis[i][j] = 0;
						}
					}
					
					HashMap<Tuple2<String, String>, HashSet<st_edge> > bt_edges_clus = new HashMap<>();
					for (int i=0;i<new_nodes.size();i++){
						ArrayList<String> anodes = new ArrayList<>(); 
						if (isInteger(new_nodes.get(i) ) ){
							anodes = entleaf.get(new_nodes.get(i));
						}else{
							anodes.add(new_nodes.get(i));
						}
						for (int j=0;j<new_nodes.size();j++ ){
							if (i==j){
								continue;
							}
							if ( nodes.contains(new_nodes.get(i)) && nodes.contains(new_nodes.get(j))  ){
								new_dis[i][j] = new_dis[j][i] = dis[nodes.indexOf(new_nodes.get(i))][nodes.indexOf(new_nodes.get(j))];
								continue;
							}
							ArrayList<String> bnodes = new ArrayList<>(); 
							if (isInteger(new_nodes.get(j) ) ){
								bnodes = entleaf.get(new_nodes.get(j));
							}else{
								bnodes.add(new_nodes.get(j));
							}
							
							HashSet<st_edge> st_edges = new HashSet<>();
							
							////////////////////     record related edges , hashmap                ///////////////////////
							for (String aa : anodes){
								for (String bb: bnodes){									
									if (tp_links.containsKey(new Tuple2<String, String>(aa, bb))){
										st_edges.add(new st_edge(aa, bb,tp_links.get(new Tuple2<String, String>(aa, bb) ) ));
									}																	
								}
							}						
							bt_edges_clus.put(new Tuple2<String, String>(new_nodes.get(i), new_nodes.get(j)), st_edges);
							if (st_edges.size()>0){
								new_dis[i][j] = new_dis[j][i] = 1;
							}
						}
					}
//					System.out.println("data action: "+dataAction.getnodes()+"\tkeys: "+keys+"\n"+dataAction.getnodes().containsAll(keys));
					
		            steiner_tree tree = null;
//		            tree = get_localst_2(dataAction.getDis(), dataAction.getnodes(), keys); /// no unfold
//		            tree = get_localst(new_dis, new_nodes, keys);
		            tree = get_localst_2(new_dis, new_nodes, keys); //// new algorithm,  accross level unfold        
		            
					return new Tuple2<>(key+"", new graph_stru(tree, key+"", new_nodes, keys, keys2nodes));
				 }
			 });
			 //////// across levels, unfold all  //////

			 
			 ////////////////////   sim level, don't unflod all /////////
			 ArrayList< Tuple3<Integer, HashSet<Integer>, HashMap<String, String>> > list_la = new ArrayList<>();
			 for (Integer a: stru.keySet()){
				 list_la.add(new Tuple3<>(a,stru.get(a),gradson2son));			 
			 }
			 JavaRDD<Tuple3<Integer, HashSet<Integer>, HashMap<String, String>>> rdd_la = sc.parallelize(list_la);
			 JavaRDD<Tuple2<String, graph_stru>> stru_lala =  rdd_la.map(new Function<Tuple3<Integer, HashSet<Integer>, HashMap<String, String>>, Tuple2<String, graph_stru>>() {
				 public Tuple2<String, graph_stru> call(Tuple3<Integer, HashSet<Integer>, HashMap<String, String>>  ls) throws IOException, ClassNotFoundException{	
					 Integer key = ls._1();
					 HashSet<Integer> sons = ls._2();		
					 HashMap<String, String> gradson_son= ls._3();
					 
					 long tttt = System.currentTimeMillis();

				    ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(key+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
			        super_cluster dataAction = (super_cluster) oin.readObject(); // 强制转换到Person类型  
			        oin.close();		        
			        
			        ArrayList<String> keys = new ArrayList<>();
			        HashMap<String, String> keys2nodes = new HashMap<>();
			        for (Integer aa: sons){
			        	if (gradson_son.containsKey(aa+"")){
			        		keys2nodes.put(aa+"", gradson_son.get(aa+""));
			        		keys.add(gradson_son.get(aa+""));
			        	}else{
			        		keys.add(aa+"");
			        	}
			        }		        				
//			        System.out.println(System.currentTimeMillis() - tttt);
		            steiner_tree tree =	get_localst_2(dataAction.dis, dataAction.nodes, keys); //// new algorithm,  accross level unfold       
//		            System.out.println("tree num: "+  key+"\tnodes num: "+dataAction.nodes.size()+"\tsons: "+sons+"\tkeys2nodes: "+keys2nodes );
		            System.out.println("time for " + (System.currentTimeMillis() - tttt)+"\t"+keys.size()+" "+dataAction.nodes.size() );
					return new Tuple2<>(key+"", new graph_stru(tree, key+"", dataAction.nodes, keys, keys2nodes));
				 }
			 });
			 
			 
			 
//			 List<Tuple2<String, graph_stru>> rsrs = null;
			 
			 /////   unflod all, across levels  /////
/*			 List<Tuple2<String, graph_stru>> rsrs = stru_ls.collect();
			 stru_list.clear();
			 for ( Tuple2<String, graph_stru> aa:rsrs  ) {
				 stru_list.put(aa._1, aa._2);
			 }*/
			 
/*			 ///////  don't unflod all, sim level  /////
			 List<Tuple2<String, graph_stru>> rsrs = stru_lala.collect();
			 stru_list.clear();
			 for ( Tuple2<String, graph_stru> aa:rsrs  ) {
				 stru_list.put(aa._1, aa._2);
			 }		 */
			 
//			 System.out.println(" stru: " + stru.size() +" root: "+root_cluster+"\t stru list size: "+ stru_list.size());				
//			 System.out.println("time for stru: "+(System.currentTimeMillis()-tm3)+" ms");
			 System.out.println("time for graph cut: "+(System.currentTimeMillis()-tm3)+" ms");
			 gpcut_tm.add((int) (System.currentTimeMillis()-tm3) );
			 
			 tm4 = System.currentTimeMillis();
			 /////////   graph min-cut algorithm //////////
//			 HashMap<String,ArrayList<String>> min_clus = graph_min_cut(keys, part_matrix, part_mtx_father);
			 			 
//			 cluster2level = cs.cluster2level;
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
				 public steiner_tree call(Tuple2<String, ArrayList<String>> ls) throws IOException, ClassNotFoundException{			
					 /////////////  parameter:  cluster's number;   cluster's including keys   /////////
					 
					 /////////////   there need a steiner tree search algorithm
					 long tmt  = System.currentTimeMillis();						 
					 ArrayList<String> part_keys = ls._2;					 

					 /////////////    read data from clusters/, super_cluster type         /////////////
//					  ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+ls._1+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
//				      super_cluster dataAction = (super_cluster) oin.readObject(); // 强制转换到Person类型  
//				      oin.close();  				      
//				     ArrayList<String> part_nodes = dataAction.getnodes();
				     steiner_tree local_st = null;
				      if (part_keys.size()==1){ 
//						System.out.println("just 1 key, return............."+"\t"+ls._1);
						local_st = new steiner_tree(ls._1, part_keys, new ArrayList<String>() , 0, new HashSet<st_edge>());	
						local_st.isCluster = -1;
					 }else{
						 ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+ls._1+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
						  cluster_data dtt = (cluster_data) oin.readObject(); // 强制转换到Person类型  
					      oin.close();  
					      if (dtt.getnodes().size() > max_size ){
//					    	  return local_st;
//					    	  System.out.println( " maxsize: .............******************* "+ls._1);
					    	  HashSet<st_edge> edges = new HashSet<>();
					    	  int wgt = 0;
					    	  String ct_node = cluster_center_node.get(ls._1);
					    	  for (String node: part_keys){
					    		  ArrayList<String> ends = new ArrayList();
					    		  ends.add(node);
					    		  HashSet<st_edge> sm = get_path_bfs(dtt, ct_node,  ends);
					    		  for (st_edge dg: sm){					    			  
					    			  wgt += dg.weight;
					    		  }
					    		  edges.addAll(sm);
					    	  }				    	  
					    	  
					    	  local_st = new steiner_tree(ls._1, part_keys, dtt.getnodes(), wgt, edges);
					      }else{
//					    	  local_st = get_localst(dtt.getDis(), dtt.getnodes(), part_keys); 
					    	  local_st = get_localst_2(dtt.getDis(), dtt.getnodes(), part_keys); 
					    	  
					    	  ArrayList<String> ends = new ArrayList<>();
					    	  String ct_node = cluster_center_node.get(ls._1);
					    	  for (st_edge dg: local_st.edges){
					    		  ends.add(dg.end);
					    		  ends.add(dg.beg);
					    	  }
					    	  ends =new ArrayList<String>(new HashSet<String>(ends));
					    	  HashSet<st_edge> edges = local_st.edges;
					    	  int wgt = local_st.weight_all;
					    	  if (!ends.contains(ct_node)){
					    		  HashSet<st_edge> sm = get_path_bfs(dtt, ct_node,  ends);
					    		  for (st_edge dg: sm){					    			  
					    			  wgt += dg.weight;
					    		  }
					    		  edges.addAll(sm);
					    	  }
					    	  
					    	  local_st = new steiner_tree(ls._1, part_keys, dtt.getnodes(), wgt, edges);
					      }
					      
				    	  local_st.cluster_number = ls._1;
				    	  local_st.isCluster = 0;		
					 }
//				 	System.out.println("cluster: "+ ls._1+"\tkeys: "+ls._2+"\tnodes: "+local_st.nodes);		      
					 System.out.println(  "\t time for local   running : "+ (System.currentTimeMillis()-tmt));
					 /////// dynamic algorithm,  need a smallest tree algorithm to compare   ////////
					 
					 return  local_st;
				 }
			 });
			 
			 ////// the following need to merge the part results	

			 List<steiner_tree> r1 = st_trees.collect();	
			 tm5 = System.currentTimeMillis();
			 System.out.println("\ntime for map: "+ (tm5-tm4));
			 mp_tm.add((int) (tm5-tm4));
		/*	 while (r1.contains(null)){
				 r1.remove(null);
				 System.out.println("null removed!");
			 }*/
			 
			 ////////////////    father cluster, father center, son center           ////////////////////
			 ArrayList<Tuple3<Integer,String, String>> coup_cnts = new ArrayList<Tuple3<Integer,String, String>>();			 
			 for (Integer key : stru.keySet()) {
				 HashSet<Integer> values = stru.get(key);
				 for (Integer val : values){
					 coup_cnts.add(new Tuple3<Integer, String, String>(key,cluster_center_node.get(key+""), cluster_center_node.get(val+"")));
				 }
			 };						 
			 JavaRDD<Tuple3<Integer,String, String>> rdd_cnt = sc.parallelize(coup_cnts);	 	
			 JavaRDD<HashSet<st_edge>> coup_link =  rdd_cnt.map(new Function<Tuple3<Integer, String, String>,  HashSet<st_edge> >() {
				 public HashSet<st_edge> call(Tuple3<Integer,String,String> ls) throws IOException, ClassNotFoundException{		
					 long tmt  = System.currentTimeMillis();						 
					 Integer fa_num = ls._1();
					 String fa_ctn = ls._2();
					 String son_ctn = ls._3();
					 HashSet<st_edge> links = new HashSet<>();					 
					 if (fa_ctn.equals(son_ctn)){
						 System.out.println( " links size: "+links.size()+"\t time : "+(System.currentTimeMillis()-tmt)  );
						 return links;
					 }
					 cluster_data dtt = clust_whole;
//					 ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/adjs/"+(fa_num+"")+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
//					 cluster_data dtt = (cluster_data) oin.readObject(); // 强制转换到Person类型  
//				     oin.close();
				     System.out.println( "\t time for load graph : "+(System.currentTimeMillis()-tmt)  );
				     ArrayList<String> ends = new ArrayList<>();
				     ends.add(son_ctn);				     
				     links = get_path_bfs(dtt, fa_ctn, ends);
				     System.out.println( " links size: "+links.size()+"\t time : "+(System.currentTimeMillis()-tmt)  );

					 return  links;
				 }
			 });
			 
/*			 List<HashSet<st_edge>> mg_links = coup_link.collect();
			 HashSet<st_edge> edges = new HashSet<>();
			 int wgt = 0;
			 for (HashSet<st_edge> dg: mg_links){
				 edges.addAll(dg);
			 }
			 for (steiner_tree st: r1){
				 edges.addAll(st.edges);
			 }
			 for (st_edge dg: edges){
				 wgt += dg.weight;
			 }
			 steiner_tree fn_st = new steiner_tree(root_cluster+"", keys, entleaf.get(root_cluster+""), wgt, edges);*/
			 
			        
//			 System.out.println(matrix.length+" "+part_matrix.length);		 
	 
//			 steiner_tree fn_st = merge_localst( r1,stru, subgra ,keys );	
			 
//			 steiner_tree fn_st = merge_lst_sp(r1,stru, subgra ,keys); ///// no unfold, no stru_list 
			 
//			 steiner_tree fn_st = merge_stru_list(r1,stru, subgra ,keys,stru_list);			 
			 
//			 steiner_tree fn_st = merge_stru_list_unfolded(r1,stru, subgra ,keys,stru_list);  ///// unfold all
//			 steiner_tree fn_st = merge_stru_list_fold(r1,stru, subgra ,keys,stru_list,gradson2son);  ///// fold all, get stru_list
			 
			 steiner_tree fn_st = merge_stru_center_node(r1,stru, subgra,keys );  ///// fold all, get stru_list

			 
			 
			 System.out.println("\ntime for merge: "+ (System.currentTimeMillis() - tm5));
			 
			 tree_wgt.add(fn_st.edges.size());
			 HashSet<String> nnnn = new HashSet<>();
			 for (st_edge dg: fn_st.edges){
				 nnnn.add(dg.beg );
				 nnnn.add(dg.end);
			 }			 
			 add_node_num.add(nnnn.size() - keys.size() );
			 
			 
			 System.out.println("final steiner tree:");
			 if (fn_st!=null){
				System.out.println("cluster: "+fn_st.cluster_number+"\t"+"\tweight all: "+fn_st.weight_all+"\tedges size:"+fn_st.edges.size());
//			 keys " "+fn_st.keys.size()+"\tkeys: "+
//					 fn_st.keys+"\tnodes size: "
				HashSet<String> ff_nodes = new HashSet();
//				System.out.println("all edges in final tree: ");
				for ( st_edge eg: fn_st.edges ){
					if (eg.weight==0){
						continue;
					}
//					eg.st_output();
					ff_nodes.add(eg.beg);
					ff_nodes.add(eg.end);
				}
				
//				ArrayList<Tuple2<Integer, Integer>> tttj = ana_res(fn_st.edges,keys);
//				System.out.println(tttj);				
				if (fn_st.nodes.size()!=keys.size()){
				 not_all += 1;
				 not_list.add(i);
				 }
				
			 }else{
				 nu.add(i);
			 }
			 
			 System.out.println("\ntime for merge: "+(System.currentTimeMillis()-tm4));//+"\n"+chen+"\t"+no_chen
			 rdc_tm.add((int) (System.currentTimeMillis()-tm4) );

			 
			 System.out.println("time now :\t"+(System.currentTimeMillis()-tm2)+" ms!!\n\n");
		}
		long tm33 = System.currentTimeMillis();
		System.out.println("tm3-tm2:\t"+(tm33-tm2)+" ms!!\n\n");
		System.out.println( "graph cut time: "+gpcut_tm);
		System.out.println("map time: "+mp_tm);
		System.out.println("reduce time: "+rdc_tm);
		System.out.println( "add node num: "+add_node_num );
		System.out.println("tree weight: "+tree_wgt );
		 System.out.println("end...");
    }
	
}