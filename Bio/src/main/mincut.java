package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.hadoop.hive.ql.parse.HiveParser_FromClauseParser.searchCondition_return;
import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.stringLiteralSequence_return;
import org.stringtemplate.v4.compiler.STParser.andConditional_return;

import scala.Tuple2;

public class mincut {
	
	public class edge{
		public int beg;
		public int end;
		public int weight;
		
		public edge(int beg, int end, int weight){
			this.beg = beg;
			this.end = end;
			this.weight = weight;
		}
		
	}
		
	
	final static int large = 10000;
	
	public HashSet<edge> get_path(int[][] dis, int source, int sink){
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
			if (nexts.contains(sink)){
				break;
			}
		}
		if (vs[sink]==0){
			return null;
		}
		
		HashSet<edge> rEdges = new HashSet<>();
		int las = father.get(sink);
		rEdges.add(new edge(las, sink, dis[las][sink]));
		while (las!=source){
			rEdges.add(new edge(father.get(las), las, dis[father.get(las)][las]));
			las = father.get(las);
		}
		
		return rEdges;
	}
				
	
	public HashSet<ArrayList<String>> get_mincut(ArrayList<String> nodes, int[][] dis,int source, int sink ){
		

		HashSet<edge> edges = get_path(dis, source, sink);		
		int min_wg = large;
		while (edges!=null){			
			for ( edge edge : edges){
				if (edge.weight < min_wg){
					min_wg = edge.weight;
				}
			}
//			System.out.println("min_wg: "+min_wg);			
			
			for ( edge edge : edges){
				dis[edge.beg][edge.end] -= min_wg;
				if (dis[edge.beg][edge.end]<0){
					dis[edge.beg][edge.end] = 0;
				}
				dis[edge.end][edge.beg] += min_wg;
			}	
	
			edges = get_path(dis, source, sink);	
			min_wg = large;
		}
		
/*		System.out.println("dis:");
		for (int i=0;i<nodes.size();i++){
			for (int j=0;j<nodes.size();j++){
				System.out.print(dis[i][j]+" ");
			}
			System.out.println();
		}		*/		
		
		
		int vm = 0;
		int vis[] = new int[nodes.size()];
		for (int i=0;i<nodes.size();i++){
			vis[i] = 0;
		}
		
		HashSet<ArrayList<String>> parts = new HashSet<>();
		Queue<Integer> qq = new LinkedList<>();
		qq.add(source);
		vis[source] = 1;
		vm += 1;
		ArrayList<String> sc = new ArrayList<>();
		sc.add(nodes.get(source ));
		
		while(vm<nodes.size()){			
			while (!qq.isEmpty()){
				int sa = qq.poll();
//				sc.add(sa);
				for (int i=0;i<nodes.size();i++){
					if (dis[sa][i]>0 &&  vis[i]==0 ){
						qq.add(i);
						vis[i] = 1;
						vm += 1;
//						sc.add(i);
						sc.add(nodes.get(i));
					}
				}				
			}
			parts.add((ArrayList<String>) sc.clone());
			sc.clear();
			for (int i=0;i<nodes.size();i++){
				if (vis[i] ==0 ){
					qq.add(i) ;
					vis[i] = 1;
					vm += 1;
//					sc.add(i);
					sc.add(nodes.get(i));
				}
			}
		}
//		System.out.println("sc size: " +  sc.size());
		if (sc.size()>0){
			parts.add(sc);			
		}		
		
		
		return parts;	
	}

}
