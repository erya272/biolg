package main;

import java.awt.List;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.ivy.util.CollectionUtils;
import main.app;
import scala.Tuple2;
public class justfortest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*	    String[] arrayA = new String[] { "1", "2", "3", "3", "4", "5" };        
	    String[] arrayB = new String[] { "3", "4", "4", "5", "6", "7" };                
	    java.util.List<String> a = Arrays.asList(arrayA);        
	    java.util.List<String> b = Arrays.asList(arrayB);     
	    HashSet<String> aHashSet = new HashSet<>(), bHashSet = new HashSet<>(), cHashSet= new HashSet<>() ;
	    aHashSet.addAll(a);
	    bHashSet.addAll(b);
	    System.out.println(aHashSet+" \t "+bHashSet + " "+ cHashSet);
	    
	    HashMap<String, Integer> hashMap = new HashMap<>();
	    hashMap.put("a", 1);
	    hashMap.put("a", 2);
	    System.out.println(hashMap);
	    String string = "01234";
	    for (int i=0;i<string.length();i++){
	    	System.out.println(i+"\t"+string.indexOf(i));
	    }*/
//	    aHashSet.
//	    //并集        
//	    java.util.Collection<String> union = CollectionUtils.union(aHashSet, bHashSet);        
//	    //交集        
//	    Collection<String> intersection = CollectionUtils.intersection(a, b);        
//	    //交集的补集        
//	    Collection<String> disjunction = CollectionUtils.disjunction(a, b);        
//	    //集合相减        
//	    java.util.Collection<String> subtract = CollectionUtils.subtract(a, b);                
	    
		////// to justfy floyd algorithm
		final int large = 10000;
/*		int[][] dis = {
				{large,30,large,10,50},
				{large,large,60,large,large},
				{large,large,large,large,large},
				{large,large,large,large,30},
				{50,large,40,large,large},		
		};*/
		
		int[][] dis = {
				{0,1,5, large,large,large,  large,large,large},
				{1,0,3, 7,5,large,  large,large,large},
				{5,3,0,  large,1,7,   large,large,large},
				
				{large,7,large,  0,2,large,  3,large,large},				
				{large,5,1,    2,0,3,    6,9,large},
				{large,large,7,    large,3,0,    large,5,large},
				
				{large,large,large,    3,6,large,    0,2,7},
				{large,large,large,    large,9,5,    2,0,4},
				{large,large,large,    large,large,large,    7,4,0}
		};
		
		
		
		
		app tsApp = new app();
		Tuple2<int[][],int[][]> ress = tsApp.flod(dis);
		ArrayList<Integer> ways = new ArrayList<Integer>(); 
		tsApp.getpath(ress._2, 0, 5, ways);
		System.out.println(ways);
		
		ArrayList<Integer> w_nocu = tsApp.getpath_nonrecursive(ress._2, 0, 5);
		System.out.println("without cu:\n"+w_nocu);
		
		
		
		

	}

}
