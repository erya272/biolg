package com.jiao.nexoSim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.jiao.file.TreeParentChild;
import com.jiao.serializable.Logger;

public class TreeSerializable implements Serializable{
	static String basepath = "/home/lee/biolg/ScaleFreeNetwork TestData/nnbt/Nexo/";
	private static final long serialVersionUID = 11L;
	/*
	 * ent1:Par_children;
	 * ent2:Root_leaf;
	 * ent3:Child_parents
	 * 
	 */
	HashMap<String, ArrayList<String>> ent1 = new HashMap<String, ArrayList<String>>();
	HashMap<String, ArrayList<String>> ent2 = new HashMap<String, ArrayList<String>>();
	HashMap<String, String> ent3 = new HashMap<String,String>();
	
	private static HashMap<String, ArrayList<String>> init1() {
		Logger.debug("load data from db...", TreeSerializable.class);
		TreeParentChild tpc =new TreeParentChild();
		ConstructTermTree sct =new ConstructTermTree();
		tpc = sct.DataPreparing(basepath+"tree.csv");
		Logger.debug("load data from db done!", TreeSerializable.class);
		return tpc.getPar_children();
		
	}
	private static HashMap<String, ArrayList<String>> init2() {
		Logger.debug("load data from db...", TreeSerializable.class);
		TreeParentChild tpc =new TreeParentChild();
		ConstructTermTree sct =new ConstructTermTree();
		tpc = sct.DataPreparing(basepath+"tree.csv");
		Logger.debug("load data from db done!", TreeSerializable.class);
		return tpc.getRoot_leaf();	
	}
	private static HashMap<String, String> init3() {
		Logger.debug("load data from db...", TreeSerializable.class);
		TreeParentChild tpc =new TreeParentChild();
		ConstructTermTree sct =new ConstructTermTree();
		tpc = sct.DataPreparing(basepath+"tree.csv");
		Logger.debug("load data from db done!", TreeSerializable.class);
		System.out.println("here");
		return tpc.getChild_parents();
	}
	
	public static void saveMapData(File file, TreeSerializable map) {
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
	
	public static TreeSerializable loadMapData(File file) {
		long startTime = new Date().getTime();
		Logger.debug("load map data from file...", TreeSerializable.class);
//		System.out.println(TreeSerializable.class.getName()+":load map data from file...");
//		System.out.println(TreeSerializable.serialVersionUID);
		if(file != null){
			if(!file.exists()){
				Logger.debug("can not find map data file...", TreeSerializable.class);				
				TreeSerializable map = new TreeSerializable();
				Logger.debug("save map data to file...", TreeSerializable.class);
				saveMapData(file, map);
			}
			ObjectInputStream is = null;
			try {
				is = new ObjectInputStream(new FileInputStream(file));
				TreeSerializable ob = (TreeSerializable) is.readObject();
				long endTime = new Date().getTime();
//				System.out.println("载入所有点耗费时间" + (endTime - startTime) + " ms!");
				is.close();
//				System.out.println(is.);
				return ob;
				
				// here  
				
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
	
	public TreeSerializable() {
		 ent1 = init1();
		 ent2 = init2();
		 ent3 = init3(); 
	}
	public static void main(String[] args){
//		HashMap<String, ArrayList<String>> hs = new HashMap<String, ArrayList<String>>();
//		for (int i=0;i<10;i++){
//			ArrayList<String> val = new ArrayList<String>();
//			val.add("A"+i);
//			hs.put(String.valueOf(i), val);
//			System.out.println(hs);			
//		}
//		System.out.println();
		
		TreeSerializable prs = TreeSerializable.loadMapData(new File(basepath+"Tree.dat"));
		System.out.println(prs.ent1.size());
//		System.out.println(prs.ent2);
//		System.out.println(prs.ent3);
		System.out.println("over!");
		
	}
	private static String String(int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
