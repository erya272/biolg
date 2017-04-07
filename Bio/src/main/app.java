package main;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;



class app{
	
		
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
		
    }
	
}




