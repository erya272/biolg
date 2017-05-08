package main;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

public class testit {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        SparkConf conf=new SparkConf().setAppName("spark test").setMaster("local[4]");
		JavaSparkContext sc =new JavaSparkContext(conf);		
	
		JavaRDD<String> lines = sc.textFile("data.txt"); //hdfs://user/lee/
		JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer>() {
		  public Integer call(String s) { 
			  return s.length(); }
		});
		int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
		  public Integer call(Integer a, Integer b) { return a + b; }
		});				System.out.println("\ntotallength:\t"+totalLength);

	}

}
