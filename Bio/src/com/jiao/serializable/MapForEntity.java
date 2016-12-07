package com.jiao.serializable;

import java.util.ArrayList;
import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
//import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.jiao.db.DBHelper;

public class MapForEntity {
	//为map做数据准备
	HashMap<String, Set<String>> ent_edge = new HashMap<String, Set<String>>();
	HashMap<String, ArrayList<String>> ent_edge2 = new HashMap<String, ArrayList<String>>();
	HashMap<String, ArrayList<String[]>> ent_edge3 = new HashMap<String, ArrayList<String[]>>();
	public HashMap<String, ArrayList<String>> getData() {
		DBHelper dbh = DBHelper.getAbiodataDBDbHelper();
		dbh.connectionDB();
//		Collection<String[]> res = dbh.queryData(
//				"select ent1_pid, ent2_pid, score from link_relations", 3);
		Collection<String[]> res = dbh.queryData("link_relations",
				new String[]{"ent1_pid", "ent2_pid", "score "}, " limit 0,2200000");
//		for(String[] s :res){
//			System.out.println(s[0]+","+s[1]+","+s[2]);
//		}
		System.out.println(res.size());
		dbh.disconnection();
		System.out.println("****************************************");
//		HashMap<String, Set<String>> gsd1 = MapEntity(res);
		HashMap<String, ArrayList<String>> gsd2 = MapEntity2(res);
//		HashMap<String, ArrayList<String[]>> gsd3 = MapEntity3(res);
//		Iterator<String> it3 = gsd2.keySet().iterator();
//		while (it3.hasNext()) {
//			String key = (String) it3.next();	
//			for (String s : gsd2.get(key)) {
//				System.out.println(key + "->" + s);
//			}}
		
//		System.out.println("****************************************");
//		Iterator<String> it2 = gsd3.keySet().iterator();
//		while (it2.hasNext()) {
//			String key1 = (String) it2.next();	
//			for (String[] s : gsd3.get(key1)) {
//				System.out.println(key1 + "->" + s[0]+","+s[1]);
//			}}
//			Collections.sort(gsd3.get(key), new Comparator<String[]>() {
//				public int compare(String[] arg0,String[] arg1) {
//					return arg0[1].compareTo(arg1[1]);
//				}
//				});	
		return gsd2;
	}
//set 存储 pid2+score
	public HashMap<String, Set<String>> MapEntity(Collection<String[]> res) {
		Set<String> al = null;
		for (String[] s : res) {
			if (ent_edge.containsKey(s[0])) {
				String ss = s[1] + "," + s[2];
				al.add(ss);
				ent_edge.put(s[0], al);
			} else {
				al = new HashSet<String>();
				String ss = s[1] + "," + s[2];
				al.add(ss);
				ent_edge.put(s[0], al);
			}
		}
		return ent_edge;
	}

	public HashMap<String, ArrayList<String>> MapEntity2(
			Collection<String[]> res) {
		ArrayList<String> al = new ArrayList<String>();
		for (String[] s : res) {
			if (ent_edge2.containsKey(s[0])) {
				String ss = s[1] + "," + s[2];
				al = ent_edge2.get(s[0]);
				if (!al.contains(ss))
					al.add(ss);
				ent_edge2.put(s[0], al);
			}
			else {
				al =new ArrayList<String>();
				String ss = s[1] + "," + s[2];
				al.add(ss);
				ent_edge2.put(s[0], al);
			}
		}
		return ent_edge2;
	}
//arraylist 存储pid2 和 score组成的字符串数组
	public HashMap<String, ArrayList<String[]>> MapEntity3(
			Collection<String[]> res) {
		ArrayList<String[]> al = new ArrayList<String[]>();
		for (String[] s : res) {
			if (ent_edge3.containsKey(s[0])) {
				String []  ss =  new String[2];
				ss[0] =s[1];
				ss[1] =s[2];
				al = ent_edge3.get(s[0]);
				al = only(ss,al);
				ent_edge3.put(s[0], al);
			} else {
				al = new ArrayList<String[]>();
				String []  ss =  new String[2];
				ss[0] =s[1];
				ss[1] =s[2];
				al = only(ss,al);
				ent_edge3.put(s[0], al);
			}
		}
		return ent_edge3;
	}
//针对字符串数组去重
	public ArrayList<String[]> only(String[] instr, ArrayList<String[]> inlist) {
		String temp = instr[0] + instr[1];
		List<String> to = new ArrayList<String>();
		for (int i = 0; i < inlist.size(); i++) {
			to.add(inlist.get(i)[0] + inlist.get(i)[1]);
		}
		if (!to.contains(temp)) {
			inlist.add(instr);
		}

		return inlist;
	}

	public static void main(String[] args) {
		MapForEntity hhh = new MapForEntity();
		hhh.getData();
	}
}
