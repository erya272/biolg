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

import com.jiao.serializable.Logger;

public class PRSerializable implements Serializable{
	//每个cluster的内部PageRank值；
//	static String basepath = "/home/ajiao/nbt/";
	static String basepath = "E://ScaleFreeNetwork TestData/snbt/";
	private static final long serialVersionUID = 11L;
	HashMap<String, ArrayList<String>> ent = new HashMap<String, ArrayList<String>>();
    
	private static HashMap<String, ArrayList<String>> init() {
		Logger.debug("load data from db...", PRSerializable.class);
		ComputerEachClusterPageRank cep = new ComputerEachClusterPageRank(
				basepath);
		HashMap<String, ArrayList<String>> entleaf = cep.ms1.ent;
		Logger.debug("load data from db done!", PRSerializable.class);
		return cep.ComputerPR(cep.DataPrepare(entleaf, basepath+"Edges.csv"));
	}
	
	public static void saveMapData(File file, PRSerializable map) {
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
	
	public static PRSerializable loadMapData(File file) {
		long startTime = new Date().getTime();
		Logger.debug("load map data from file...", PRSerializable.class);
		if(file != null){
			if(!file.exists()){
				Logger.debug("can not find map data file...", PRSerializable.class);				
				PRSerializable map = new PRSerializable();
				Logger.debug("save map data to file...", PRSerializable.class);
				saveMapData(file, map);
			}
			ObjectInputStream is = null;
			try {
				is = new ObjectInputStream(new FileInputStream(file));
				PRSerializable ob = (PRSerializable) is.readObject();
				long endTime = new Date().getTime();
				System.out.println("载入所有点耗费时间" + (endTime - startTime) + " ms!");
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
	
	public PRSerializable() {
		 ent = init();

	}
	
	public Collection<String> getPair(String key) {
		Collection<String> values = new ArrayList<String>();
		if (ent.containsKey(key)) {
			values = ent.get(key);
		}
		return values;
	}
	public static void main(String[] args){
		PRSerializable prs = PRSerializable.loadMapData(new File(basepath+"PR.dat"));
		System.out.println(prs.ent);
		

		
	}
	
}
