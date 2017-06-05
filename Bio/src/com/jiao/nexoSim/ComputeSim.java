package com.jiao.nexoSim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.Queue;

import org.apache.hadoop.hive.metastore.api.ThriftHiveMetastore.renew_delegation_token_result;
import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.intervalLiteral_return;
import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.stringLiteralSequence_return;
import org.apache.spark.sql.execution.QueryExecutionException;
import org.apache.zookeeper.KeeperException.SystemErrorException;
import org.bouncycastle.asn1.icao.CscaMasterList;
import org.datanucleus.store.rdbms.sql.method.StringEqualsIgnoreCaseMethod;
import org.dmg.pmml.NearestNeighborModel;

import com.jiao.hierarachy.HierarchyMapSerializable;

import breeze.linalg.all;
import breeze.linalg.cond;
import breeze.macros.expand.valify;
import main.cluster_data;
import main.st_edge;
import main.super_cluster;
import scala.Tuple2;
import scala.language;
import scala.collection.parallel.ParIterableLike.Forall;
import scala.reflect.internal.Trees.New;



public class ComputeSim {
	/*
	 * N : Gene的个数
	 * basepath : Nexo的有关data所在的基本路径目录
	 * prs : Nexo Tree的序列化结果
	 * par_children：父子关系，一个父亲对应多个孩子
	 * root_leaf：每一个父亲的所有叶子节点
	 * child_parent ：每一个孩子对应一个父亲
	 */
	static int N = 4987;
	final int large = 10000;
	static String basepath = "/home/lee/biolg/ScaleFreeNetwork TestData/nnbt/Nexo/";
	static String spath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";   ////  "E://ScaleFreeNetwork TestData/snbt/";
	static String treepath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/Cutoff/9973.dat"; // + root.dat
	
	TreeSerializable prs = TreeSerializable.loadMapData(new File(basepath+"Tree.dat"));
	
	
	/////////  nnbt ///////
	HashMap<String, ArrayList<String>> par_children = prs.ent1;  //// 4123 9109(4123+4987-1)
	HashMap<String, ArrayList<String>> root_leaf = prs.ent2;  //// 4213  57161(4987)
//	public HashMap<String, String> c_P =prs.ent3;
	public HashMap<String, String> child_parent =  prs.ent3;   //////  9109(4123+4987-1) 4123
	
	
	//////        snbt         /////
	HierarchyMapSerializable ms1; 
	// static HashMap<String, ArrayList<String>> entleaf = ms1.ent;
	public HashMap<String, ArrayList<String>> entleaf;      ////////// every clusters' nodes(keywords set)
	HashMap<String, Integer> clu_level = new HashMap<String, Integer>();  /////// every clusters' level in entcutoff (1-100)
	public static HashMap<String, Integer> nls_bykey =  new HashMap<String, Integer>() ; //////// key: keywords, values: 0-4986	
	HashMap<String, ArrayList<String>> entcutoff = new HashMap<String, ArrayList<String>>();  //// tree structure, 100 levels
	public static HashMap<Integer, Integer> cluster2level = new HashMap<Integer, Integer>();
	
	String fr1 = spath + "Leaf.dat";
//	this.path = spath + "Clusters/";
//	this.fr2path = spath + "Cutoff/";

	public int[][] part_matrix ;
	public int[][] part_mtx_father;
	public static String tree_root;  ////////  the tree root cluster number /////
	
	public ComputeSim() {
		// TODO Auto-generated constructor stub
			
	}
	
	
	
	public HashMap<String, ArrayList<String>> get_cluster_son_Set(HashMap<String, HashSet<String>> par_childs) throws FileNotFoundException, IOException, ClassNotFoundException{
		HashMap<String, ArrayList<String>> res = new HashMap<>();		
		for (String key: par_childs.keySet()){
		    ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(key)+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
	        super_cluster dd = (super_cluster) oin.readObject(); // 强制转换到Person类型  
	        oin.close();  
			res.put(key, dd.getnodes() );
		}		
		return res;
	}
	
	public HashMap<String, HashSet<st_edge>> get_cluster_links(HashMap<String, HashSet<String>> par_childs) throws FileNotFoundException, IOException, ClassNotFoundException{
		HashMap<String, HashSet<st_edge>> res = new HashMap<>();		
		for (String key: par_childs.keySet()){
		    ObjectInputStream oin = new ObjectInputStream(new FileInputStream( "/home/lee/biolg/clusters/"+(key)+".dat")); //////  "/home/lee/biolg/cache/5363.dat" 
	        super_cluster dd = (super_cluster) oin.readObject(); // 强制转换到Person类型  
	        oin.close();  
			res.put(key, dd.get_links());
		}		
		return res;
	}
	
	/////// write edges into local in hashmap<> 		
	////   write snbt original edges, nodes into local       ////
	public void write_edges_local() throws IOException{
		HashMap<Tuple2<Integer, Integer >, Integer> edges = new HashMap<>();
		HashMap<String, Integer> nodes2itg =  new HashMap<String, Integer>() ;
		String sdfsdf = spath + "AdjcentMatrix.csv";
				
		int i=0,j=0;
		BufferedReader br = new BufferedReader(new FileReader(sdfsdf));
        // 读取直到最后一行 
        String line = "";         
        ArrayList<String> allnodes = new ArrayList<String>();        
        while ((line = br.readLine()) != null) { 
            // 把一行数据分割成多个字段
        	i += 1;
            StringTokenizer st = new StringTokenizer(line, "\t| ");      
            if (i==1){
            	while (st.hasMoreTokens()){
            		allnodes.add(st.nextToken());
            	}            	
            	for (int k=0;k<allnodes.size();k++){
            		nodes2itg.put(allnodes.get(k), k);
            	}
            	
            	System.out.println("size: "+allnodes.size());          	
            	continue;
            }

            String s =  st.nextToken();
        	ArrayList<String> la = new ArrayList<String>();
            while (st.hasMoreTokens()) { 
                // 每一行的多个字段用TAB隔开表示 
//            	System.out.print(st.nextToken()+" ");
            	la.add(st.nextToken() );        	
            }
            for (int k=0;k<la.size();k++){
            	if (Math.round(new Float(la.get(k))) ==0){
            		continue;
            	}
            	edges.put(new Tuple2<Integer, Integer>( nodes2itg.get(s) , nodes2itg.get( allnodes.get(k) ) ) , Math.round(new Float(la.get(k))) );
            	edges.put(new Tuple2<Integer, Integer>( nodes2itg.get( allnodes.get(k) ) ,  nodes2itg.get(s) ) , Math.round(new Float(la.get(k))) );
            }                   
        } 
        br.close();
		
        System.out.println( "edges size: "+edges.size()+"\tallnodes: "+allnodes.size()+"\tnodes2int: "+nodes2itg.size());
        
	    FileOutputStream outStream11 = new FileOutputStream("/home/lee/biolg/snbt_orgiedges.dat");
	    ObjectOutputStream objectOutputStream11 = new ObjectOutputStream(outStream11);
	    objectOutputStream11.writeObject(edges);
	    outStream11.close();
	    
	    outStream11 = new FileOutputStream("/home/lee/biolg/nodes2integer.dat");
	    objectOutputStream11 = new ObjectOutputStream(outStream11);
	    objectOutputStream11.writeObject(nodes2itg);
	    outStream11.close();
        
        
	}
	
	
	
	public void get_snbt_stru() throws IOException, ClassNotFoundException{
		HierarchyMapSerializable ms2 = HierarchyMapSerializable.loadMapData(
				new File(treepath), true, 1);		
		entcutoff = ms2.ent; /////// tree structure, 100 levels, step by 50 clusters.4987 clusters in all.		
		this.ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
		this.entleaf = this.ms1.ent;   	////////// every clusters' nodes(keywords set)		
		
		System.out.println("entcutofff: "+entcutoff.size()+"\tentleaf: "+entleaf.size() );
		
		HashMap<Integer,  ArrayList<String> > cluster_level = new HashMap<>();
		HashMap<String, HashSet<String>> descendants = new  HashMap<String, HashSet<String>>();
		
		HashMap<Integer, Integer> cluster2level = new HashMap<>();
		for (int i=1;i<101;i++){
			ArrayList<String> arrayList = entcutoff.get(i+"");
//				System.out.println(i+"\t"+arrayList.size()); 
			ArrayList<String> arrayList2 = new ArrayList<>();
			for (String s : arrayList){
				if (isInteger(s)==false){
					continue;				
				}
				arrayList2.add(s);
				if (!cluster2level.containsKey(s)){
//					System.out.println("hhehe "+s);
					cluster2level.put( Integer.valueOf(s) , i);
				}
				
			}
			cluster_level.put(i, arrayList2);
		}
		System.out.println("cluster 2 size: "+cluster2level.size());
		
		
/*		InputStream outStream1 = new FileInputStream("/home/lee/biolg/descendants_new.dat");
		ObjectInputStream oinp = new ObjectInputStream(outStream1); //////  "/home/lee/biolg/cache/5363.dat" 
		HashMap<String, HashSet<String>> ded = (HashMap<String, HashSet<String>>) oinp.readObject(); // 强制转换到Person类型  
	    oinp.close();  
	    
	    System.out.println(ded.size());
	    int mn=0;
	    for (String key: ded.keySet()){
	    	HashSet<Integer> lvSet = new HashSet<>();
	    	if (mn++>400){
	    		break;
	    	}
	    	if (ded.get(key).size()==1){
	    		continue;
	    	}
	    	System.out.println("key: "+ key+"\t"+ded.get(key)+"\t"+entleaf.get(key));
	    	for (String aa: ded.get(key)  ){
	    		lvSet.add(cluster2level.get(aa));
//	    		System.out.println("\t"+aa+"\t"+cluster2level.get(aa)+"\t"+entleaf.get(aa));
	    	}
	    	if (lvSet.size()>1){
	    		System.out.println(lvSet);
	    	}
	    }*/
	    FileOutputStream outStream11 = new FileOutputStream("/home/lee/biolg/cluster2level.dat");
	    ObjectOutputStream objectOutputStream11 = new ObjectOutputStream(outStream11);
	    objectOutputStream11.writeObject(cluster2level);
	    outStream11.close();
			
		
//		ComputeSim.cluster2level = this.cluster2level;
/*		if (cluster_level.size()>0){
			return;
		}*/
		
		for (int i=1;i<101;i++){
			ArrayList<String> arrayList = entcutoff.get(i+"");
//				System.out.println(i+"\t"+arrayList.size()); 
			for (String s : arrayList){
				if (isInteger(s)==false){
					continue;				
				}	

				//////// test all clusters on above levels //////
				for (int j=1;j<=i;j++){
					for (String sa: cluster_level.get(j)){
						if (sa.equals(s)){
							continue;
						}
						if (entleaf.get(sa).containsAll( entleaf.get(s) )){
							HashSet<String> tmp = new HashSet<>();
							if(descendants.containsKey(sa)){
								tmp = descendants.get(sa);
							}
							tmp.add(s);
							descendants.put(sa, tmp);
						}
					}
				}
			}
			System.out.println("level: "+i);
/*			if (i>10){
				break;
			}*/
		}
		
	    FileOutputStream outStream = new FileOutputStream("/home/lee/biolg/descendants.dat");
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
	    objectOutputStream.writeObject(descendants);
	    outStream.close();
	    
	    System.out.println(descendants.size());
	    int n=0;
		HashMap<String, HashSet<String>> par_childs_set = new  HashMap<String, HashSet<String>>();
	    HashMap<String, String> child_parent = new  HashMap<String, String>();
	    for (String key: descendants.keySet()){
//	    	System.out.println(x);
	    	HashSet<String> sons = descendants.get(key);

	    	HashSet<String> tmp = new HashSet<>();
	    	for (String sa: sons){
	    		int flg= 0;
	    		for (String sb: sons){
	    			if (entleaf.get(sb).containsAll(entleaf.get(sa))){
	    				flg +=1;
	    			}
	    		}
	    		if (flg==1){
	    			tmp.add(sa);
	    		}
	    	}
	    	if (n++<50){
	    		System.out.println("key: "+key+"\tsons: "+sons);
	    		System.out.println(tmp);
	    	}
	    	
	    	par_childs_set.put(key, tmp); ///// par 2 sons 
	    	
	    	for (String aa: tmp){
	    		child_parent.put(aa, key); //// son 2 par
	    	}
	    	
	    }
	    
	    for (String key:par_childs_set.keySet()){
	    	System.out.println("\tkey: "+key+"\ttmp: "+par_childs_set.get(key)   );
	    }
		
	    outStream = new FileOutputStream("/home/lee/biolg/par_childs.dat");
	    objectOutputStream = new ObjectOutputStream(outStream);
	    objectOutputStream.writeObject(par_childs_set);
	    outStream.close();
		
	    outStream = new FileOutputStream("/home/lee/biolg/child_parent.dat");
	    objectOutputStream = new ObjectOutputStream(outStream);
	    objectOutputStream.writeObject(child_parent);
	    outStream.close();
		 System.out.println("snbt stru over!");
	
	}
	
		
	public void get_snbt_stru_part() throws IOException, ClassNotFoundException{
		HierarchyMapSerializable ms2 = HierarchyMapSerializable.loadMapData(
				new File(treepath), true, 1);		
		entcutoff = ms2.ent; /////// tree structure, 100 levels, step by 50 clusters.4987 clusters in all.		
		this.ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
		this.entleaf = this.ms1.ent;   	////////// every clusters' nodes(keywords set)		
		
		System.out.println("entcutofff: "+entcutoff.size()+"\tentleaf: "+entleaf.size() );
		
		HashMap<Integer,  ArrayList<String> > cluster_level = new HashMap<>();
		HashMap<String, HashSet<String>> descendants = new  HashMap<String, HashSet<String>>();
		
		HashMap<Integer, Integer> cluster2level = new HashMap<>();
		for (int i=1;i<101;i++){
			ArrayList<String> arrayList = entcutoff.get(i+"");
//				System.out.println(i+"\t"+arrayList.size()); 
			ArrayList<String> arrayList2 = new ArrayList<>();
			for (String s : arrayList){
				if (isInteger(s)==false){
					continue;				
				}
				arrayList2.add(s);
				if (!cluster2level.containsKey(s)){
//					System.out.println("hhehe "+s);
					cluster2level.put( Integer.valueOf(s) , i);
				}
				
			}
			cluster_level.put(i, arrayList2);
		}
		System.out.println("cluster 2 size: "+cluster2level.size());
		
		ObjectInputStream oincp = new ObjectInputStream(new FileInputStream("/home/lee/biolg/descendants.dat")); 
		descendants = (HashMap<String, HashSet<String>> ) oincp.readObject(); 
		oincp.close();
		
	    System.out.println(descendants.size());
	    int n=0;
		HashMap<String, HashSet<String>> par_childs_set = new  HashMap<String, HashSet<String>>();
	    HashMap<String, String> child_parent = new  HashMap<String, String>();
	    for (String key: descendants.keySet()){
	    	if (++n%300==0){
	    		System.out.println("proccessing...."+n);
	    	}
//	    	System.out.println(x);
	    	HashSet<String> sons = descendants.get(key);

	    	HashSet<String> tmp = new HashSet<>();
	    	for (String sa: sons){
	    		int flg= 0;
	    		for (String sb: sons){
	    			if (entleaf.get(sb).containsAll(entleaf.get(sa))){
	    				flg +=1;
	    			}
	    		}
	    		if (flg==1){
	    			tmp.add(sa);
	    		}
	    	}
//	    	if (n++<50){
	    	if (key.equals("8094")){
	    		System.out.println("key: "+key+"\tsons: "+sons+"\tafter delete: "+tmp);
	    		System.out.println(" "+key+"\t"+entleaf.get(key));
	    		for (String aString : tmp){
	    			System.out.println("\t"+aString+"\t"+entleaf.get(aString));
	    		}
	    	}
	    	
	    	par_childs_set.put(key, (HashSet<String>) tmp.clone()); ///// par 2 sons 
	    	
	    	for (String aa: tmp){
	    		child_parent.put(aa, key); //// son 2 par
	    	}
	    	
	    }
	    
/*	    for (String key:par_childs_set.keySet()){
	    	System.out.println("\tkey: "+key+"\ttmp: "+par_childs_set.get(key)   );
	    }*/
/*	    FileOutputStream outStream = new FileOutputStream("/home/lee/biolg/par_childs.dat");
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
	    objectOutputStream.writeObject(par_childs_set);
	    outStream.close();
		
	    outStream = new FileOutputStream("/home/lee/biolg/child_parent.dat");
	    objectOutputStream = new ObjectOutputStream(outStream);
	    objectOutputStream.writeObject(child_parent);
	    outStream.close();*/
		System.out.println("snbt stru over!");
		 
		int kk=0;
		for (String key: par_childs_set.keySet()){
/*			if (kk++>100){
				break;
			}*/
			
			HashSet<String> sons = par_childs_set.get(key);
			if (sons.size()>5){
				System.out.println(key+"\t"+sons);
			}			
			for (String aa: sons){
				for (String bb: sons){
					if (aa.equals(bb)){
						continue;
					}
//					System.out.println( entleaf.get(key)+"\t"+entleaf.get(aa)+"\t"+entleaf.get(bb)  );
/*					if (entleaf.get(key).containsAll(entleaf.get(aa)) && entleaf.get(key).containsAll(entleaf.get(bb)) &&
							entleaf.get(aa).retainAll(entleaf.get(bb))){
						System.out.println(key+"\t"+aa+"\t"+bb);
						System.out.println( entleaf.get(key)+"\t"+entleaf.get(aa)+"\t"+entleaf.get(bb)  );
					}*/

				}
			}
		}
	}
	
	public  Tuple2<int[][], int[][]>  getMatrix(String datapath)      ////////  the lowest common ansester's  level
	{
		HierarchyMapSerializable ms2 = HierarchyMapSerializable.loadMapData(
				new File(treepath), true, 1);
		
		entcutoff = ms2.ent; /////// tree structure, 100 levels, step by 50 clusters.4987 clusters in all.		
//		System.out.println("encutoff:"+entcutoff.size());
		this.ms1 = HierarchyMapSerializable.loadMapData(new File(fr1), true, 1);
		this.entleaf = this.ms1.ent;   	////////// every clusters' nodes(keywords set)
		
		HashMap<Integer, String> nodesList = getNodesList(datapath); //////// key: 0-4986, values: keywords			
		for (int i=0;i<nodesList.size();i++){
			nls_bykey.put(nodesList.get(i),i );
		}
//		System.out.println(clu_level.size()+" "+entcutoff.size());
		for (int i=1;i<entcutoff.size()+1;i++){
			ArrayList<String> sss = entcutoff.get(i+"");
			for (String s: sss){
				if (isInteger(s)==true){
					if (!clu_level.containsKey(s)){
//						System.out.println(" clu_level: " +s);
						clu_level.put(s, i);
					}					
				}
			}
		}
		
//		System.out.println("nodesList:"+nodesList.size()); 
//		System.out.println(nodesList.get(1)+nodesList.get(2)+nodesList.get(3));
		
		/////////   get the tree root cluster   //////////
		 ArrayList<String> clStrings = entcutoff.get(1+"");
		 int num = 0;
		 String root = new String();
		 for (String aa: clStrings){
			 if (isInteger(aa)==true && entleaf.get(aa).size()>num){
				 num = entleaf.get(aa).size();
				 root = aa;
			 }
		 }
//		 System.out.println("root:"+root+" "+num);
		tree_root = root;
		
		int comfh[][]= new int[N][N];		////   level  ////
		int com_father[][]= new int[N][N];    /////   clusters's number   ////	
		
		for(int i=0;i<N;i++){
			for (int j=0;j<N;j++){
				comfh[i][j]=1;
				com_father[i][j] = Integer.valueOf(root); 
			}
		}
		
		int numb =0;
		int seen=0;
		for (int i=1;i<101;i++){
			ArrayList<String> arrayList = entcutoff.get(i+"");
//			System.out.println(i+"\t"+arrayList.size()); 
			for (String s : arrayList){
				if (isInteger(s)==false){
					comfh[nls_bykey.get(s)][nls_bykey.get(s)] = i-1;
				}else{
					if (!cluster2level.containsKey(Integer.valueOf(s))){
//						seen += 1 ;
//						System.out.println(" seen! ");
						cluster2level.put(Integer.valueOf(s), i);
					}					
				}				
				try{
					ArrayList<String> sss = entleaf.get(s);
					for (String a:sss){
						com_father[nls_bykey.get(a)][nls_bykey.get(a)] = (int) Integer.valueOf(s) ;
						for (String b:sss){
							if (!a.equals(b)){
								comfh[nls_bykey.get(a)][nls_bykey.get(b)] = comfh[nls_bykey.get(b)][nls_bykey.get(a)] = i;
								com_father[nls_bykey.get(a)][nls_bykey.get(b)] = com_father[nls_bykey.get(b)][nls_bykey.get(a)] = (int) Integer.valueOf(s) ;
							}
							if ( sss.size()>1 && a.equals(b)  ){
								comfh[nls_bykey.get(a)][nls_bykey.get(b)] = comfh[nls_bykey.get(b)][nls_bykey.get(a)] = i;
								com_father[nls_bykey.get(a)][nls_bykey.get(b)] = com_father[nls_bykey.get(b)][nls_bykey.get(a)] = (int) Integer.valueOf(s) ;
							}
						}
					}
				}catch (Exception e) {
					// TODO: handle exception
					continue;
				}		
			}			
		}
//		System.out.println("seen: "+seen);
//		return comfh;
//		System.out.println(numb+" not in 100 level! "+nls_bykey.size());

		
		return new  Tuple2<int[][], int[][]>(comfh, com_father);
	}
	
	public HashMap<String, ArrayList<String>> getSubGraphList(ArrayList<String> keys,int[][] matrix, int[][] mtx_co_father,HashMap<String, Integer> numByKey){
		///////// parameters:    keys list,   common ansester's lowest level matrix,   nodes to index transform list.
		int len = keys.size(); 	
		///////   keys to index , transform   //////////
		HashMap<String, Integer> key2num = new HashMap<String, Integer>(); //// keys, 1 - len
		HashMap<Integer, String> num2key = new HashMap<Integer, String>(); /// 0 - len-1 , keys
		part_matrix =  new int[len][len];    ///////     the lowest common ancestor's  level    ////////  
		part_mtx_father = new int[len][len];  ///////     the lowest common ancestor's  cluster number    ///////
		
		
		
		int i=0;
		for (String ss:keys){
			num2key.put(i, ss);
			key2num.put(ss, i++);			
		}
		for (String ss:keys){
			for (String bb:keys){
				part_matrix[key2num.get(ss)][key2num.get(bb)] = matrix[numByKey.get(ss)][numByKey.get(bb)];
				part_mtx_father[key2num.get(ss)][key2num.get(bb)] = mtx_co_father[numByKey.get(ss)][numByKey.get(bb)];
			}
		}

		HashMap<Integer, HashSet<Integer>> infos = new HashMap<Integer, HashSet<Integer>>();  /////////  levels 2 clusters
//		HashMap<Integer, Integer> clust2lev = new HashMap<Integer, Integer>();
		for ( i=0;i<len;i++){
			for (int j=0;j<len;j++){
//				cluster2level.put(part_mtx_father[i][j], part_matrix[i][j]);
//				clust2lev.put(  part_mtx_father[i][j], part_matrix[i][j]  );
				if (infos.containsKey(part_matrix[i][j])){
					HashSet<Integer> hahh = infos.get(part_matrix[i][j]);
					hahh.add(part_mtx_father[i][j]);
					infos.put(part_matrix[i][j], hahh);
				}else{
					HashSet<Integer> hahh = new HashSet<>(part_mtx_father[i][j]);
					infos.put(part_matrix[i][j], hahh);
				}
			}
		}		
		ArrayList<Integer> vlList = new ArrayList<>(infos.keySet())  ;   ///////    all levels which include common ansester   ///////
		vlList.sort(null); /////   sort increasing  /////
//		System.out.println("vlist: "+vlList);
		
		
		HashSet<ArrayList<Integer>> clust = new HashSet<>();		
		int[] cled = new int[len];
		for (i=0;i<len;i++){
			cled[i]=0;
		}
		i = 1;
		int fgall=0;
		while(i<vlList.size()){
			int yz = vlList.get(i++);
			HashSet<Integer> clus = infos.get(vlList.get(i-1));
//			System.out.println("yz:"+yz);
			int[] fg = new int[len];			
			for (int j=0;j<len;j++){				
				fg[j]=cled[j];
			}
			for (int j=0;j<len;j++){
				if (fg[j]!=0){
					continue;
				}
				HashSet<Integer> paa = new HashSet();
				paa.add(j);
				Queue<Integer> cc = new LinkedList<>();
				cc.add(j);
				fg[j] = 1;
				int now;
				while (cc.isEmpty()==false){
					now = cc.poll();
//					System.out.println("now "+now+" j "+j);
					for (int k=0;k<len;k++){
						if (part_matrix[now][k]>yz && fg[k]==0){
							fg[k] = 1;
							paa.add(k);
							cc.add(k);
						}
					}
				}

				if (paa.size()<=3){
					clust.add(new ArrayList<Integer>(paa) );
					for (Integer aa:paa){
						cled[aa] = 1;
					}
					fgall += paa.size();
				}			
			}			
			if (fgall==len){
				break;
			}			
		}		
//		System.out.println("clust: "+clust);
		
		HashMap<String, ArrayList<String>> clu2keys = new HashMap<>();
		for (ArrayList<Integer> aaa:clust){
			int level = 101,cl = 0;
			ArrayList<String> kk = new ArrayList<>();
			for (Integer a:aaa){
				kk.add(num2key.get(a));
				for (Integer b:aaa){
					if (part_matrix[a][b]<level){
						level = part_matrix[a][b];
						cl = part_mtx_father[a][b];
					}
				}
			}
			if (clu2keys.containsKey(cl+"")){
				kk.addAll(clu2keys.get(cl+""));
			}
			clu2keys.put(cl+"", kk);
		}	
		
/*		for ( String a: clu2keys.keySet()){
			System.out.println( a+"\t"+clust2lev.get(Integer.valueOf(a))  );
		}*/
		return clu2keys;
	}
	
	
	
	public String getCluFather(String string,int[][] matrix){
		String res="";
		ArrayList<String> arrayList = entleaf.get(string);
		String onekey = arrayList.get(0);
		int level = clu_level.get(string);
		
		int now = 1;
		int k = nls_bykey.get(onekey);
		for (int i=0;i<nls_bykey.size();i++){
			if (matrix[k][i]>now && matrix[k][i] < level){
				now = matrix[k][i];
			}
		}		
		
		ArrayList<String> clusters = entcutoff.get(now+"");
//		System.out.println(level+" "+clusters);
		for (String s:  clusters){
			if (isInteger(s)==true){
				if (entleaf.get(s).contains(onekey)){
					// cluster number, cluster's nodes;    father cluster number, father cluster's nodes
//					System.out.println(string+" "+entleaf.get(string)+"\t"+s+" "+entleaf.get(s));
//					System.out.println(s+" "+entleaf.get(s));
					return s;
				}
			}
		}			
		return res;
	}
	
	
	 public  boolean isInteger(String str) {
		 Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		 return pattern.matcher(str).matches();    
	}  
	
	 
		public static HashMap<String, String> get_center_nodes(String path, HashMap<String, ArrayList<String>> entleaf) throws NumberFormatException, IOException{
			HashMap<String, String> res = new HashMap<String, String>();
			int dis[][] = null;		
			//////// initlize
			HashMap<String, Integer> node_degree = new HashMap<String, Integer>();
//			System.out.println(nodes);		
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
//	            	System.out.println(st.countTokens());
	            	int kk=0;
	            	while (st.hasMoreTokens()){
//	            		System.out.println(st.nextToken());
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
//	            	System.out.print(st.nextToken()+" ");
	            	la.add(st.nextToken() );        	
	            }
	            int k = ind.get(s);
	            for (j=0;j<la.size();j++){
	            	dis[j][k] = dis[k][j] = (int) Float.parseFloat( la.get(j) )  ;
	            	if (dis[j][k]!=0 ){
	            		String aString = allnodes.get(j);
	            		String bString = allnodes.get(k);
	            		if (node_degree.containsKey( aString)){
	            			node_degree.put(aString, node_degree.get(aString)+1);
	            		}else{
	            			node_degree.put(aString, 1);
	            		}
	            		if (node_degree.containsKey( bString)){
	            			node_degree.put(bString, node_degree.get(bString)+1);
	            		}else{
	            			node_degree.put(bString, 1);
	            		}
	            	}
	            }           
	        } 
	        br.close();
	        
	        for (String key: entleaf.keySet()){
	        	ArrayList<String> nodes = entleaf.get(key);
	        	int num =0;
	        	String ctnode = "";
	        	for (String node: nodes){
	        		if (node_degree.get(node)>=num){
	        			ctnode = node;
	        		}
	        	}
	        	res.put(key, ctnode) ;    	
	        }

			return res;
		}
	 
	 
	 
		
	 
	 /////////////////////////////     above by me     /////////////////////////////////////////
	 
	 
	public HashMap<Integer, String> getNodesList(String ph){
		HashMap<Integer, String> res = new  HashMap<Integer, String>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(ph));
			String line = null;
			int i =0;
//			Pattern pattern = Pattern.compile("\\s*|\r|\n"); 
			while((line = reader.readLine()) != null){
				res.put(i++, line.replace("\r", "").replace("\n", ""));				
			}			
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("file not exist!!");
		}			
		return res;
	}

	
	public String getAllkeywordsRoot(ArrayList<String> keys){
		//获取所有关键字的最低公共祖先
		Set<String> set = root_leaf.keySet(); 
		Iterator<String> it = set.iterator();
		String index = "";
		int len = Integer.MAX_VALUE;
//		System.out.println("root_leaf: "+root_leaf.size());
		int i = 0;
		while (it.hasNext()) {
			String par  = it.next();		
			ArrayList<String> leaf = root_leaf.get(par);
//			System.out.println(i++ + " "+par+" "+leaf.size());
			if(leaf.containsAll(keys) && leaf.size() < len){
				index = par;
				len = leaf.size();
			}
		}
		return index;
		
	}
	
//	public String getTwoTermCommomRoot(String term1, String notkey){
//		String index = "";
//		while(term1 != notkey &&!term1.equals(notkey)){
//			term1 =child_parent.get(term1);
//			notkey = child_parent.get(notkey);	
//		}
//		index = term1;
//		System.out.println(index);
//		return index;	
//	}
	
	public  String getTermParent(String term){
		//找到指定term的parent
		String parent = child_parent.get(term);
		return parent;	
	}
	public ArrayList<String> getTermChild(String term){
		//找到指定term的孩子
		ArrayList<String> children = par_children.get(term);
		return children;	
	}
	public ArrayList<String> getTermLeaf(String term){
		//找到指定term的叶子节点
		ArrayList<String> leaf = root_leaf.get(term);
		return leaf;	
	}
	
	public double TwoTermSim(String index,int N){	
		//计算两个Term的相似度
		
		ArrayList<String> ttleaf =root_leaf.get(index);
		int len = ttleaf.size();
		double sim = -Math.log((double)len/(double)N);
//		System.out.println(len+"\t"+N);
		return sim;	
		
	}
	public HashMap<String, Double> allpathSim(ArrayList<String> path){
		//计算path上的所有点对的相似度
		double sum = 0;
//		System.out.println(x);
		HashMap<String,Double> pathSim = new HashMap<String,Double>();
		for(String s:path){
			ArrayList<String> keys = new ArrayList<String>();
			String[] ss = s.split(",");
			keys.add(ss[0]);
			keys.add(ss[1]);
			double value = 0;
			value =	TwoTermSim(getAllkeywordsRoot(keys), N);
			sum += value;
			pathSim.put(s, value);
		}
		System.out.println("similarity-----:"+sum);
		return pathSim;
		
	}
	
	public void outuput (){
		System.out.println("size: "+par_children.size()+" "+root_leaf.size()+" "+child_parent.size());
		int i=0;
		for ( String key: par_children.keySet() ){
			System.out.println("key: "+key+"\tvalues: "+par_children.get(key));
			if (i++>30){
				break;
			}
		}
		
		
	}
	
	
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		ComputeSim cs =new ComputeSim();
		ArrayList<String> keys  = new ArrayList<String>();	
		ArrayList<String> path = new ArrayList<String>();
		path.add("YCR107W,YFL061W"); //// not used 
		
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
		 System.out.println( "the least public root is: " + cs.getAllkeywordsRoot(keys)); 
		 
//		cs.get_snbt_stru();
		cs.get_snbt_stru_part();
		 ////   write snbt original edges, nodes into local       ////
//		cs.write_edges_local();

				 
			 
		 
/*		 Tuple2<int[][], int[][]> ma2 = cs.getMatrix(spath+"nodes_sorted.csv");
		 int[][] matrix = ma2._1;     ////////   level ,lowest common ansester's  level   //////// 
		 int[][] mat_father = ma2._2;     ////////   cluster number ,lowest common ansester's  cluster number  ////////
		 
		 cs.outuput();*/
		 
/*			 
		 		 HashMap<String, ArrayList<String>> subgra = cs.getSubGraphList(keys, matrix, mat_father.clone(), nls_bykey );
		 for (java.util.Map.Entry<String, ArrayList<String>> entry : subgra.entrySet()){
			 cs.getCluFather(entry.getKey(),matrix);
		 }
		*/
		 System.out.println("cluster 2 size: "+cluster2level.size());
		 
		 System.out.println("over!");
	}
	

}
