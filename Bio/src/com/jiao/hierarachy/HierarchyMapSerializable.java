package com.jiao.hierarachy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.jiao.file.DirMaker;
import com.jiao.serializable.Logger;

public class HierarchyMapSerializable implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	//对 叶节点和cutoff的map 进行存储
    public HashMap<String, ArrayList<String>> ent = new HashMap<String, ArrayList<String>>();
//    static String fr = "/home/ajiao/nbt/finalHac2.csv";
//    static String fr1 = "/home/ajiao/nbt/Leaf.dat";//on is true
//    static String fr2path = "/home/ajiao/nbt/Cutoff/";//on is false
    static String fr = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/finalHac2.csv";
    static String fr1 = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/Leaf.dat";//on is true
    static String fr2path = "/home/lee/biolg/ScaleFreeNetwork TestData/snbt/Cutoff/";//on is false
    static DirMaker mdir = new DirMaker();
    
    static int dis = 50;
    static int n =4987;
//	  static int dis = 3;
//	  static int n =100;
    static ArrayList<String> Kroot = new ArrayList<String>();
    
	private static HashMap<String, ArrayList<String>> init1() {
		Logger.debug("load data from db...", HierarchyMapSerializable.class);
		IndexForHierarchyTree mfe = new IndexForHierarchyTree();
		Logger.debug("load data from db done!", HierarchyMapSerializable.class);
		return mfe.ConstructTreeAndNodeIndex(fr);//返回父节点对应的叶子节点
	}
	
	private static HashMap<String, ArrayList<String>> init2(int j) {
		Logger.debug("load data from db...", HierarchyMapSerializable.class);
		IndexForHierarchyTree mfe = new IndexForHierarchyTree();
		Logger.debug("load data from db done!", HierarchyMapSerializable.class);
//		mfe.ConstructTreeAndNodeIndex(fr);//先把树构造出来
//		return mfe.TreeCutoff(j,fr);
//		return mfe.TreeCutoffByShort(j, fr, layers, Kroot);
		return mfe.TreeCutoffByDistance(j, fr, dis, Kroot);
	}
	
	public static void saveMapData(File file, HierarchyMapSerializable map) {
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(map);
			os.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static HierarchyMapSerializable loadMapData(File file,boolean on, int j) {
		long startTime = new Date().getTime();
//		Logger.debug("load map data from file...", HierarchyMapSerializable.class);
		if(file != null){
			if(!file.exists()){
//				Logger.debug("can not find map data file...", HierarchyMapSerializable.class);				
				HierarchyMapSerializable map = new HierarchyMapSerializable(on,j);
//				Logger.debug("save map data to file...", HierarchyMapSerializable.class);
				saveMapData(file, map);
			}
			ObjectInputStream is = null;
			try {
				is = new ObjectInputStream(new FileInputStream(file));
				HierarchyMapSerializable ob = (HierarchyMapSerializable) is.readObject();
				long endTime = new Date().getTime();
//				System.out.println("载入所有点耗费时间" + (endTime - startTime) + " ms!");
				return ob;
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		return null;
	}
	
	public HierarchyMapSerializable(boolean on,int j) {
		if(on)
		 ent = init1();//leaf is flag true
		else 
	     ent = init2(j);// cutoff is flag false
	}
	
	public Collection<String> getPair(String key) {
		Collection<String> values = new ArrayList<String>();
		if (ent.containsKey(key)) {
			values = ent.get(key);
		}
		return values;
	}
	public static void main(String[] args){
		 mdir.makeDir(new File(fr2path));
		HierarchyMapSerializable ms1 = HierarchyMapSerializable.loadMapData(new File(
				fr1),true,1);
//		System.out.println(ms1.ent);
//		HierarchyMapSerializable.loadMapData(new File(
//				fr2path+(199)+".dat"),false,0);
//		for(int j = 1; j <n-1;j++){
//			HierarchyMapSerializable ms2 = HierarchyMapSerializable.loadMapData(new File(
//				fr2path+(100+n-j-1)+".dat"),false,j);
//			System.out.println(ms2.ent);
//			}
//		
		//通过高度cutoff来分层
		 HierarchyMapSerializable.loadMapData(new File(
				fr2path+(9973)+".dat"),false,0);
		 
		for(int j = 1; j < n-1;j++){
			if(Kroot.contains((4987+n-j-1)+"")){
				HierarchyMapSerializable.loadMapData(new File(
						fr2path+(4987+n-j-1)+".dat"),false,j);
				}
			}
		
		

		
	}
}
