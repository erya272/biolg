package com.jiao.nexoSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class threads_data {
	
	public int begin;
	public int end;
//	public ArrayList<String> mkw;
//	public String root;
	public HashMap<String, ArrayList<String>> pr;
	public ArrayList<String> dis;
	public HashMap<String, String> keyi;
	public HashMap<String, ArrayList<String>> map ;
//	public LinkedHashMap<String, ArrayList<String>> rootAndpath;	
	
	public threads_data(int begin, int end,
//			ArrayList<String> mkw, String root,
			HashMap<String, ArrayList<String>> pr,
			ArrayList<String> dis,
			HashMap<String, String> keyi,
			HashMap<String, ArrayList<String>> map 
//			LinkedHashMap<String, ArrayList<String>> rootAndpath
			) {
		// TODO Auto-generated constructor stub
		this.begin = begin;
		this.end = end;
//		this.mkw = mkw;
//		this.root = root;
		this.pr = pr;
		this.dis = dis;
		this.keyi = keyi;
		this.map = map;
//		this.rootAndpath = rootAndpath;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
