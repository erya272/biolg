package main;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.JavaRDD;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.spark.SparkConf;
import com.jiao.nexoSim.*;


class app{
	
	
	static String spath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";   ////  "E://ScaleFreeNetwork TestData/snbt/";
//	static HashMap<String, Integer> nls_bykey =  new HashMap<String, Integer>() ; //////// key: keywords, values: 0-4986	

		
	public static void main( String[] args )
    {		
        SparkConf conf=new SparkConf().setAppName("first spark app").setMaster("local[4]");
		JavaSparkContext sc =new JavaSparkContext(conf);
		
// 		JavaRDD<String> lines = sc.textFile("data.txt");
//		JavaRDD<Integer> lineLengths = lines.map(s -> s.length());
//		int totalLength = lineLengths.reduce((a, b) -> a + b);
//		System.out.println(totalLength+"\thellllllllll");		
		
		JavaRDD<String> lines = sc.textFile("data.txt");
		JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer>() {
		  public Integer call(String s) { return s.length(); }
		});
		int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
		  public Integer call(Integer a, Integer b) { return a + b; }
		});
		
		System.out.println(totalLength);
		
		ComputeSim cs = new ComputeSim();
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
		 HashMap<String, ArrayList<String>> subgra = cs.getSubGraphList(keys, matrix, cs.nls_bykey );
		 for (java.util.Map.Entry<String, ArrayList<String>> entry : subgra.entrySet()){
			 cs.getCluFather(entry.getKey(),matrix);
		 }
    }
	
}




