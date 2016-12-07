package com.jiao.nexoSim;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.jiao.file.FileReadAndWrite;

public class GenerartorTestSets {
	// 产生系统性的测试数据集

	static int N = 4987;
	static String basepath = "/home/lee/biolg/ScaleFreeNetwork TestData/nnbt/Nexo/";  
	TreeSerializable prs = TreeSerializable.loadMapData(new File(basepath
			+ "Tree.dat"));
	HashMap<String, ArrayList<String>> par_children = prs.ent1;
	HashMap<String, ArrayList<String>> root_leaf = prs.ent2;
	HashMap<String, String> child_parent = prs.ent3;
	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String set_one ="/home/lee/biolg/ScaleFreeNetwork TestData/keys/set_one.csv"; 
	static String set_two ="/home/lee/biolg/ScaleFreeNetwork TestData/keys/set_two.csv"; 
	static String set_three ="/home/lee/biolg/ScaleFreeNetwork TestData/keys/set_three.csv"; 
	static String set_four ="/home/lee/biolg/ScaleFreeNetwork TestData/keys/set_four/"; 
	static String set_five ="/home/lee/biolg/ScaleFreeNetwork TestData/keys/set_one_5.csv"; 
	static int repeattime = 100;
	public void CreatSet_One(String keysfr) {
		ArrayList<String> parents = new ArrayList<String>();
		Set<String> set = root_leaf.keySet();
		Iterator<String> it = set.iterator();
		Random rand = new Random();
		while (it.hasNext()) {
			String par = it.next();
			parents.add(par);
		}
		int j = 0;
		while(j <50) {
			String key = parents.get(rand.nextInt(parents.size()));
			if (root_leaf.get(key).size() >= 10
					&& root_leaf.get(key).size() <= 10) {
				parents.remove(key);
				Iterator<String> its = set.iterator();
				while (its.hasNext()) {
					String par = its.next();
					if (root_leaf.get(par).containsAll(root_leaf.get(key))) {
						parents.remove(par);
					}
				}
				j++;
				String ss =root_leaf.get(key).get(0);
				for(String s :root_leaf.get(key)){
					if(ss != s && !ss.equals(s))
						ss +=","+s;
				}
				fraw.WriteToFile(keysfr, ss);
				System.out.println(key+","+root_leaf.get(key));
			}

		}
	}
	public void CreatSet_Two(String fr){
		ArrayList<String> read = fraw.ReadData(fr);
		int len = read.size();
		Random rand = new Random();
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		int j = 0;
		while(j <repeattime){
			ArrayList<String> subres =new ArrayList<String>();
			int index1 = rand.nextInt(len);
			int index2 = rand.nextInt(len);
			while(index2 == index1)
				index2 =rand.nextInt(len);
			String[] s1 =read.get(index1).split(",");
			String[] s2 =read.get(index2).split(",");
			ArrayList<String> ss1 = CreatEachGroupKeys(s1,5);
			ArrayList<String> ss2 = CreatEachGroupKeys(s2,5);
			subres.addAll(ss1);
			subres.addAll(ss2);
			if(!res.contains(subres)){
				res.add(subres);
				j++;
				String s= subres.get(0);
				for(String ss: subres){
					if(ss !=s&& !ss.equals(s)){
						s +=","+ss;
					}
				}
				fraw.WriteToFile(set_two, s);
			}		
		}
		
	}
	public void CreatSet_Three(String fr){   //////   here    ///// 
		ArrayList<String> read = fraw.ReadData(fr);
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		int j = 0;
		while(j <repeattime){
			ArrayList<String> subres =new ArrayList<String>();
			ArrayList<String> indexs= getGroups(read,1);
//			System.out.println(j+" "+indexs);
			for(String si:indexs){
//				System.out.println(j+" "+si);
				String []ssi = si.split(",");
				ArrayList<String> sssi =CreatEachGroupKeys(ssi,5);
//				System.out.println(sssi);
				subres.addAll(sssi);
			}
			if(!res.contains(subres)){
				res.add(subres);
				j++;
				String s= subres.get(0);
//				System.out.println(s);
//				System.out.println(subres);
				for(String ss: subres){
					if(ss !=s&& !ss.equals(s)){
						s +=","+ss;
					}
				}
//				System.out.println(s);
				fraw.WriteToFile(set_five, s);	
			}		
		}
		
	}
	public void CreatSet_four(String fr,int n){
		ArrayList<String> read = fraw.ReadData(fr);
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		int j = 0;
		while(j <repeattime){
			ArrayList<String> subres =new ArrayList<String>();
			ArrayList<String> indexs= getGroups(read,5);
			for(String si:indexs){				
				String []ssi = si.split(",");
				ArrayList<String> sssi =CreatEachGroupKeys(ssi,n);
				subres.addAll(sssi);
			}
			if(!res.contains(subres)){
				res.add(subres);
				j++;
				String s= subres.get(0);
				for(String ss: subres){
					if(ss !=s&& !ss.equals(s)){
						s +=","+ss;
					}
				}
				fraw.WriteToFile(set_four+(n*5)+".csv", s);
			}		
		}
		
		
	}
	public ArrayList<String> getGroups(ArrayList<String> read, int n){
//		System.out.println(read);
		ArrayList<String> res =new ArrayList<String>();
		int len =read.size();
		Random rand = new Random();
		int j = 0;
		while(j < n){
			int index = rand.nextInt(len);
//			System.out.println(len+" "+index);
			String s = read.get(index);
//			System.out.println(s);
			if(!res.contains(s)){
				res.add(s);
				j++;
			}		
		}
		return res;
		
	}
	public ArrayList<String> CreatEachGroupKeys(String[] keys,int n){
		ArrayList<String> res = new ArrayList<String>();
		Random rand = new Random();
		int len = keys.length;
		int j = 0;
		while(j <n){
			int index = rand.nextInt(len);
			String s = keys[index];
			if(!res.contains(s)){
				res.add(s);
				j++;
			}		
		}
		return res;
	}

	public static void main(String[] args) {
		GenerartorTestSets dp = new GenerartorTestSets();
//		dp.CreatSet_One(set_one);
//		dp.CreatSet_Two(set_one);
		dp.CreatSet_Three(set_one);
		System.out.println("over!");
//		dp.CreatSet_four(set_one, 5);
	}

}
