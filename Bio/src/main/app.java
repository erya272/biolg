package main;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.AbstractJavaRDDLike;
import org.apache.spark.api.java.JavaRDD;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.spark.SparkConf;
import com.jiao.nexoSim.*;

import breeze.util.partition;
import scala.Tuple2;


public class app{
	
	
	static String spath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";   ////  "E://ScaleFreeNetwork TestData/snbt/";
//	static HashMap<String, Integer> nls_bykey =  new HashMap<String, Integer>() ; //////// key: keywords, values: 0-4986	

		
	public static void main( String[] args )
    {		
        SparkConf conf=new SparkConf().setAppName("bio spark app").setMaster("spark://master:7077");//local[4]
//        SparkConf conf=new SparkConf().setAppName("bio spark app").setMaster("local[4]");
		JavaSparkContext sc =new JavaSparkContext(conf);
		
// 		JavaRDD<String> lines = sc.textFile("data.txt");
//		JavaRDD<Integer> lineLengths = lines.map(s -> s.length());
//		int totalLength = lineLengths.reduce((a, b) -> a + b);
//		System.out.println(totalLength+"\thellllllllll");		
		
		JavaRDD<String> lines = sc.textFile("data.txt"); //hdfs://user/lee/
		JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer>() {
		  public Integer call(String s) { 
//			  System.out.println("hello world");
			  return s.length(); }
		});
		int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
		  public Integer call(Integer a, Integer b) { return a + b; }
		});
		
		System.out.println("totallength:\t"+totalLength);
		
		ComputeSim cs = new ComputeSim();
		int i=0;
		
		
		
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
			 
			 
		    ArrayList<Tuple2<String, ArrayList<String>>> list = new ArrayList<Tuple2<String, ArrayList<String>>>();
		    Set<String> allKeys = subgra.keySet();
		    for (String key : allKeys) {
		        list.add(new Tuple2<String, ArrayList<String>>(key, subgra.get(key)) );
		    };	
		    JavaRDD<Tuple2<String, ArrayList<String>>> rdd = sc.parallelize(list);	 
			 
			JavaRDD<ArrayList<String>> st =  rdd.map(new Function<Tuple2<String, ArrayList<String>>, ArrayList<String>>() {
				 public ArrayList<String> call(Tuple2<String, ArrayList<String>> ls){				 
					 ArrayList<String> reStrings =new ArrayList<String>();
					 for (String s: ls._2){
						 reStrings.add(s);
					 }					 
					 reStrings.add("hah");				 
					 
					 
					 
					 
					 return reStrings;					 
					 
//					 return ls._2;
				 }
			 });
			 System.out.println(st.take(3) +" aaaa ");
		 
			
		}		 
		 System.out.println("end....");
    }
	
}




