package main;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.JavaRDD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.stringLiteralSequence_return;
import org.apache.spark.SparkConf;

import com.jiao.hierarachy.HierarchyMapSerializable;
import com.jiao.nexoSim.*;
import scala.Tuple2;
//import com.jiao.element.cluster_data;

import main.cluster_data;

public class app    {
	
//	static String spath = "file://home/lee/biolg/ScaleFreeNetwork TestData/snbt/";   ////  "E://ScaleFreeNetwork TestData/snbt/";
	static String spath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";
	public static HierarchyMapSerializable ms1; 
	public static HashMap<String, ArrayList<String>> entleaf;      ////////// every clusters' nodes(keywords set)
	static final int large = 10000;
	
	
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
        for (i=0;i<nodes.size();i++){
        	for (j=0;j<nodes.size();j++){
        		System.out.print(dis[i][j]+" ");
        	}
        	System.out.println();
        }	
        cluster_data res = new cluster_data(dis,nodes);
		res.setDis(dis);
		res.setnodes(nodes);		
		return res;
//        return dis;
	}
	
	public static int[][] flod(int[][] dis){
		int lg = dis.length;
//		System.out.println("res:");
		int[][] res = new int[lg][lg];
		for (int i=0;i<lg;i++){
			for (int j=0;j<lg;j++){
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
					if (res[i][j]<res[i][k]+res[k][j]){
						res[i][j] = res[i][j]+res[k][j];
					}					
				}
			}
		}
		return res;
	}
	
	public static Tuple2<Integer, ArrayList<String>> getlocalst( cluster_data cst, ArrayList<String> keys)
	{
		Tuple2<Integer, ArrayList<String>> res = null;
		ArrayList<Integer> keysint = new ArrayList<>() ;
		int[][] dis = cst.getDis();
		ArrayList<String> nodes = cst.getnodes();
		for (int i=0;i<keys.size();i++){
			keysint.add(nodes.indexOf(keys.get(i)));
		}
//		System.out.println("getlocalst:\n"+keys);
		HashMap<Tuple2<HashSet<String>, Integer> , Integer> data = new HashMap<Tuple2<HashSet<String>, Integer> , Integer>() ; ///// sub set of keys,   its shortest path
		HashMap<Tuple2<HashSet<String>, Integer> , HashSet<String>> includingnodes = new HashMap<>(); //// including nodes
		ArrayList<HashSet<String>> subsets = getallsubsets(new HashSet<String>(keys),1); ////// all non-empty sebsets of keys		
		int[][] floyd = flod(dis); //////// shortest path by floyd algorithm		
		System.out.println("getlocalst:\n");
		for (int i=0;i<floyd.length;i++){
			for (int j=0;j<floyd.length;j++){
				System.out.print(dis[i][j]+" ");
			}
			System.out.println();
		}
		////// initnial  1 key, with
		for (HashSet<String> a: subsets){
			if (a.size()==1){				
				for (String b:a){
					for (int i=0;i<nodes.size();i++){
						Tuple2<HashSet<String>, Integer> tuple2 = new Tuple2<HashSet<String>, Integer>(a, i); // = Tuple2<a, 1>;				
						data.put( tuple2,floyd[nodes.indexOf(b)][i]);
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
					for (HashSet<String> aHashSet:arrayList){
						HashSet<String> inters = getintersection(aHashSet, a);
						Tuple2<HashSet<String>, Integer>  t1 = new Tuple2<HashSet<String>, Integer>(aHashSet, k);
						Tuple2<HashSet<String>, Integer>  t2 = new Tuple2<HashSet<String>, Integer>(inters, k);
						if (data.containsKey(t1) && data.containsKey(t2)){
							Integer va = data.get(t1) + data.get(t2);
							if ( value > va ){
								value = va;
							}
						}
					}
					Tuple2<HashSet<String>, Integer> t3 = new Tuple2<HashSet<String>, Integer>(a, k);
					data.put(t3, value);
				}
				for (Integer k=0;k<nodes.size();k++){
					Tuple2<HashSet<String>, Integer> t4 = new Tuple2<HashSet<String>, Integer>(a, k);
					int value = data.get(t4);
					for (int j = 0;j<nodes.size();j++){
						if (value > data.get(t4)+dis[k][j] ){
							value = data.get(t4)+dis[k][j] ;
						}						
					}
					data.put(t4, value);
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
		res = new Tuple2<Integer, ArrayList<String>>(va , keys  );		
//		System.out.println("getlocalst:\n"+va+keys);
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
	
	
	
	
		
	public static void main( String[] args ) throws IOException
    {		
		/////// /////           change the deploy mode , just need to change the following line !              /////// //////
		
        SparkConf conf=new SparkConf().setAppName("bio spark app").setMaster("spark://master:7077");//local[4]
//        SparkConf conf=new SparkConf().setAppName("bio spark app").setMaster("local[4]");
		JavaSparkContext sc =new JavaSparkContext(conf);		
	
		JavaRDD<String> lines = sc.textFile("data.txt"); //hdfs://user/lee/
		System.out.println("hello world");
		JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer>() {
		  public Integer call(String s) { 
			  return s.length(); }
		});
		int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
		  public Integer call(Integer a, Integer b) { return a + b; }
		});		
		System.out.println("\ntotallength:\t"+totalLength);
		
		final ComputeSim cs = new ComputeSim();
		int i=0;			
		/////// get entleaf;			 
		String fr1 = spath + "Leaf.dat";
		
		
		while (i<1){			
			ArrayList<String> keys  = new ArrayList<String>();	
	//		ArrayList<String> path = new ArrayList<String>();
	//		path.add("YCR107W,YFL061W"); //// not used 
			
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
			 System.out.println("keys:\t"+keys);
	//		 System.out.println("path:\t"+path);
	//		 System.out.println("erya:\t"+path+"\thello 1");
	//		 System.out.println( "the least public root is: " + cs.getAllkeywordsRoot(keys)+"\n\n"); 			 
			 
	//		 cs.getMatrix(par_children, rl, cp);
			 int[][] matrix = cs.getMatrix(spath+"nodes_sorted.csv");
			 System.out.println("clusters:");
			 final HashMap<String, ArrayList<String>> subgra = cs.getSubGraphList(keys, matrix, cs.nls_bykey );
			 System.out.println("fathers:");
			 for (java.util.Map.Entry<String, ArrayList<String>> entry : subgra.entrySet()){
				 cs.getCluFather(entry.getKey(),matrix);
			 }	
			 i += 1;	
			 
			 ////////  get the cluster's nodes list,  and cluster's  matrix
//			 cluster_data dataAction = getDataAction( disfr, subgra.get("7015") );
			 /////  convert a hashmap to a javaRDD type
			 ArrayList<Tuple2<String, ArrayList<String>>> list = new ArrayList<Tuple2<String, ArrayList<String>>>();
			 Set<String> allKeys = subgra.keySet();
			 for (String key : allKeys) {
				 	list.add(new Tuple2<String, ArrayList<String>>(key, subgra.get(key)) );
			 };	
			 JavaRDD<Tuple2<String, ArrayList<String>>> rdd = sc.parallelize(list);	 							
			 
			 JavaRDD<Tuple2<Integer ,ArrayList<String>>> st =  rdd.map(new Function<Tuple2<String, ArrayList<String>>, Tuple2<Integer ,ArrayList<String>>>() {
				 public Tuple2<Integer ,ArrayList<String>> call(Tuple2<String, ArrayList<String>> ls) throws IOException{			
					 /////////////  parameter:  cluster's number;   cluster's including keys   /////////
					 Tuple2<Integer ,ArrayList<String>> reStrings = null ;
					 ArrayList<String> arrayList = new ArrayList<>();
					 for (String s: ls._2){
						 arrayList.add(s);
					 }					 
					 arrayList.add("hah");				 
					 /////////////   there need a steiner tree search algorithm
//					 System.out.println(" in map function:\n "+arrayList);
					 ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
					 entleaf = ms1.ent;  				 
					 					 
					 String disfr = spath + "EdgeDisMatrix.csv";
					 cluster_data dataAction = getDataAction( disfr, entleaf.get(ls._1) );					 
//					 reStrings.addAll(dataAction.nodes);
					 Tuple2<Integer, ArrayList<String>> local_st = getlocalst(dataAction,ls._2);
					 
//					 reStrings._1 = local_st._1;
//					 reStrings._1();
					 reStrings = new Tuple2<Integer ,ArrayList<String>>(local_st._1,arrayList);
					 
					 return reStrings;					 
				 }
			 });
			 System.out.println("\n"+st.take(3) +" aaaa ");			 
			 ////// the following need to merge the part results		 		 
		
		}		 
		 System.out.println("end....");
    }
	
}




