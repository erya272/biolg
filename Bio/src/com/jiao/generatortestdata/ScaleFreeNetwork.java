package com.jiao.generatortestdata;

import java.util.ArrayList;
//import java.util.Random;

import org.apache.commons.math3.random.JDKRandomGenerator;

public class ScaleFreeNetwork {
	
	public static JDKRandomGenerator rand = new JDKRandomGenerator();
	
	/**
	 * Generate artificial graph used for testing purpose as explained in the paper, 
	 * the graph representation is an ArrayList of Edgeinfo	
	 *  
	 * @param n: number of nodes
	 */
	public ArrayList<Edgeinfo> SFNetworkGenerator(int n ){
		
		int [] degCumulative = new int [n];	                                        // array for cumulative degree sums
		int [] deg = new int [n];                                                   // array of node degrees
			
		int k;
		int len;
		int randNum;
		
		ArrayList<Edgeinfo> edgeList = new ArrayList<Edgeinfo>();
		edgeList.add(new Edgeinfo(0,1,WeightFunction()));						  // connect first three nodes in a triad
		edgeList.add(new Edgeinfo(1,2,WeightFunction()));
		edgeList.add(new Edgeinfo(2,0,WeightFunction()));
		deg[0] = 2;
		deg[1] = 2;
		deg[2] = 2;
		
		Edgeinfo ff;
		for (int i = 3; i < n; i++) {
			for (int j = 0; j < 3; j++) {                                           // generate three links
				if (rand.nextInt(11) < 8) {                                         // select destination node randomly
					randNum = rand.nextInt(i);
					ff = new Edgeinfo (i, randNum, WeightFunction());
					if (!JudgeContain(edgeList,ff))
						edgeList.add(ff);
					deg[i]++;
 					deg[randNum]++;
				}
				
				else {                                                              // select destination node proportionally to its degree
					degCumulative[0] = deg[0];
					for (k = 1; k < i; k++)
						degCumulative[k] = degCumulative[k-1] + deg[k];
					len = k-1;
				    randNum = rand.nextInt(degCumulative[len-1] + 1);	
					k = 0;
				    while (randNum > degCumulative[k])
				    	k++;
				    
				    ff = new Edgeinfo (i, k, WeightFunction());
				    if (!JudgeContain(edgeList,ff))
				    	edgeList.add(ff);
				    deg[i]++;
				    deg[k]++;					    
				}								
			}			
		}	
		return edgeList;
	}
	
	/** generate random value for Edge weight
	 * @param w : the value of edge weight (0,1)
	 * @return
	 */
	public double WeightFunction(){
//		Random regn =  new Random();
//		double w = 0;
//		while(w == 0 ){
//			w = regn.nextDouble();
//		}
		double w = 1;
		return w;
	}
	
	public boolean JudgeContain(ArrayList<Edgeinfo> E, Edgeinfo e){
		 int s = e.getSource();
		 int t = e.getTarget();
		 for(Edgeinfo ed :E){
			 if((ed.getSource() == s && ed.getTarget() == t )||(ed.getSource() == t && ed.getTarget() == s )){
				 return true;
			 }
		 }
		 return false;
	}
	
	public static void main(String[] args){
		ScaleFreeNetwork sf =  new ScaleFreeNetwork();
		int count = 0;
		for(Edgeinfo e:sf.SFNetworkGenerator(30)){
			System.out.println(e.getSource() +","+e.getTarget()+","+e.getWeight());
		}
		System.out.println(count);
	}
}
