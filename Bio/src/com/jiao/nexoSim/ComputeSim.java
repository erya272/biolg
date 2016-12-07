package com.jiao.nexoSim;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


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
	TreeSerializable prs = TreeSerializable.loadMapData(new File(basepath+"Tree.dat"));
	HashMap<String, ArrayList<String>> par_children = prs.ent1;  //// 9109
	HashMap<String, ArrayList<String>> root_leaf = prs.ent2;  //// 57161
	HashMap<String, String> child_parent = prs.ent3;   //////
//	public ComputeSim(){
//		System.out.println("sdjalfjalfajl");
//	}	
	
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
		 System.out.println(cs.getAllkeywordsRoot(keys)); 
		
	}
	

}
