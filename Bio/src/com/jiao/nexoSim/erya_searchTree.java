package com.jiao.nexoSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.jiao.hierarachy.HierarcyBackForFindResults;

public class erya_searchTree extends Thread{

	/**
	 * @param args
	 */
	   private Thread t;
	   private String threadName;
	   
	  erya_threads est = new erya_threads();
	
	  public  erya_searchTree(String name)
	  {
		  this.threadName=name;
	  }
	  
	  
	public int run(	 String root,
				HashMap<String, ArrayList<String>> pr, ArrayList<String> dis,
				HashMap<String, String> keyi,HierarcyBackForFindResults res,
			LinkedHashMap<String, ArrayList<String>> rootAndpath){
		
		HashMap<String, ArrayList<String>> map = res.getClusterContainKey();	
//		System.out.println("Running " +  threadName );     
		ArrayList<String> STres = est.ApplayDreyfus(res);
		System.out.println("STres: -------" + STres + "-------");
		
		ArrayList<String> path = est.getHeirarchyPath(pr, map, STres, root, dis,	keyi);
		rootAndpath.put(root, path);			
		
   return 0;	     
	}
	
	
	public void start(){
//	      System.out.println("Starting " +  threadName );
	      if (t == null) {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	}

}
