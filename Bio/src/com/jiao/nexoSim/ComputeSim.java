package com.jiao.nexoSim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.intervalLiteral_return;

import com.jiao.hierarachy.HierarchyMapSerializable;

import scala.Tuple2;



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
	static String basepath = "/home/lee/biolg/ScaleFreeNetwork TestData/nnbt/Nexo/";
	static String spath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/";   ////  "E://ScaleFreeNetwork TestData/snbt/";
	static String treepath = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/Cutoff/9973.dat"; // + root.dat
	TreeSerializable prs = TreeSerializable.loadMapData(new File(basepath+"Tree.dat"));
	
	
	HashMap<String, ArrayList<String>> par_children = prs.ent1;  //// 4123 9109(4123+4987-1)
	HashMap<String, ArrayList<String>> root_leaf = prs.ent2;  //// 4213  57161(4987)
	HashMap<String, String> child_parent = prs.ent3;   //////  9109(4123+4987-1) 4123
	
	HierarchyMapSerializable ms1; 
	// static HashMap<String, ArrayList<String>> entleaf = ms1.ent;
	public HashMap<String, ArrayList<String>> entleaf;      ////////// every clusters' nodes(keywords set)
	HashMap<String, Integer> clu_level = new HashMap<String, Integer>();  /////// every clusters' level in entcutoff (1-100)
	public static HashMap<String, Integer> nls_bykey =  new HashMap<String, Integer>() ; //////// key: keywords, values: 0-4986	
	HashMap<String, ArrayList<String>> entcutoff = new HashMap<String, ArrayList<String>>();  //// tree structure, 100 levels
	
	
	String fr1 = spath + "Leaf.dat";
//	this.path = spath + "Clusters/";
//	this.fr2path = spath + "Cutoff/";

	public int[][] part_matrix ;
	public int[][] part_mtx_father;
	
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
		for (int i=1;i<entcutoff.size()+1;i++){
			ArrayList<String> sss = entcutoff.get(i+"");
			for (String s: sss){
				if (isInteger(s)==true){
					clu_level.put(s, i);
				}
			}
		}
		
//		System.out.println("nodesList:"+nodesList.size()); 
//		System.out.println(nodesList.get(1)+nodesList.get(2)+nodesList.get(3));
		
		int comfh[][]= new int[N][N];		////   level  ////
		int com_father[][]= new int[N][N];    /////   clusters's number   ////	
		
		for(int i=0;i<N;i++){
			for (int j=0;j<N;j++){
				comfh[i][j]=1;
			}
		}
		
		int numb =0;
		for (int i=1;i<101;i++){
			ArrayList<String> arrayList = entcutoff.get(i+"");
//			System.out.println(i+"\t"+arrayList.size()); 
			for (String s : arrayList){
/*				if (isInteger(s)==false){
					comfh[nls_bykey.get(s)][nls_bykey.get(s)] = i;
				}	*/
				try{
					ArrayList<String> sss = entleaf.get(s);
					for (String a:sss){
						for (String b:sss){
							if (!a.equals(b)){
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
		
//		System.out.println("keys:"+keys);
		HashSet<Integer> set = new HashSet<>();
//		System.out.println("Local matrix:");
		for ( i=0;i<keys.size();i++){
			for (int j=0;j<keys.size();j++){
//				System.out.print(part_matrix[i][j]+" ");
				set.add(part_matrix[i][j]);
			}
//			System.out.println();
		}		
//		Object vList = (Object)set.toArray();
//		System.out.println(vList);
		ArrayList<Integer> vlList = new ArrayList<>();   ///////    all levels which include common ansester   ///////
		vlList.addAll(set);
		vlList.sort(null); /////   sort increasing  /////
//		System.out.println(vlList);
		
		HashSet<ArrayList<Integer>> clust = new HashSet<>();
		i = 0;
		while(true){
			int yz = vlList.get(i++);
			int[] fg = new int[len];
			int fgall=0;
			for (int j=0;j<len;j++){
				fg[j]=0;
			}
			for (int j=0;j<len;j++){
				if (fg[j]!=0){
					continue;
				}
				ArrayList<Integer> paa = new ArrayList<>();
				paa.add(j);
				for (int k=0;k<len;k++){
					if (part_matrix[j][k]>yz && fg[k]==0){
						int kk= k +1;
						paa.add(k);
					}
				}
				if (paa.size()<4){
					clust.add(paa);
					for (Integer aa:paa){
						fg[aa] = 1;
						fgall+=1;
					}
				}			
			}			
			if (fgall==len){
				break;
			}			
		}		
//		int[] res = new int[clust.size()];
		HashMap<String, ArrayList<String>> clu2keys = new HashMap<>();
		
//		System.out.println(clust);
		for (ArrayList<Integer> aaa:clust){
			ArrayList<String> part_keys = new ArrayList<>();
			int level = 100;
			for (Integer a: aaa){
				part_keys.add(num2key.get(a));
				for (Integer b:aaa){
					if (a!=b && part_matrix[a][b]<level){
						level = part_matrix[a][b];
					}
				}
			}
//			System.out.println("part_keys: "+part_keys);
//			clu2keys.put(getAllkeywordsRoot(part_keys), part_keys);
			String onekey = part_keys.get(0);

			level-=1;
			int lev0 = level+1;
			if (part_keys.size()==1){
//				level-=1;
				level = 0;
				int line = numByKey.get(onekey);
				for (int j=0;j<numByKey.size();j++){
					if (matrix[line][j]>level && matrix[line][j]<lev0){
						level = matrix[line][j];
					}					
				}				
			}
			ArrayList<String> clusters = entcutoff.get(level+"");  //////////   get all clusters on this level   ///////
//			System.out.println(clusters.size()+" "+clusters.contains(onekey));
			for (String string :clusters){
				if (isInteger(string)==true){
					if (entleaf.get(string).contains(onekey)){
						clu2keys.put(string, part_keys);
					}
				}
			}			
		}
		System.out.println(clu2keys);	
		//cluster number,  cluster's including keys,  cluster's nodes
		for (java.util.Map.Entry<String, ArrayList<String>> entry : clu2keys.entrySet()){
			System.out.println(entry.getKey()+"\t"+ entry.getValue()+"\t" +  entleaf.get(entry.getKey()) );			
		}	
		//////////////////////////       the cluster is so small, could it get the steiner tree ?       //////////////////////////
		
//		System.out.println("over!!!");
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
					System.out.println(string+" "+entleaf.get(string)+"\t"+s+" "+entleaf.get(s));
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
	public static void main(String[] args){
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
		 
		 
//		 cs.getMatrix(par_children, rl, cp);
//		 int[][] matrix = cs.getMatrix(spath+"nodes_sorted.csv");
		 Tuple2<int[][], int[][]> ma2 = cs.getMatrix(spath+"nodes_sorted.csv");
		 int[][] matrix = ma2._1;     ////////   level ,lowest common ansester's  level   //////// 
		 int[][] mat_father = ma2._2;     ////////   cluster number ,lowest common ansester's  cluster number  ////////
		 
		 
		 HashMap<String, ArrayList<String>> subgra = cs.getSubGraphList(keys, matrix, mat_father.clone(), nls_bykey );
		 for (java.util.Map.Entry<String, ArrayList<String>> entry : subgra.entrySet()){
			 cs.getCluFather(entry.getKey(),matrix);
		 }
//		 
		
	}
	

}
