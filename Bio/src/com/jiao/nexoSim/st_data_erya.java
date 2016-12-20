package com.jiao.nexoSim;

import java.util.ArrayList;
import java.util.HashMap;

import com.jiao.hierarachy.HierarcyBackForFindResults;

public class st_data_erya {
	
	public String root;
	public HashMap<String, ArrayList<String>> pr; 
	public  ArrayList<String> dis;
	public HashMap<String, String> keyi;
	public HierarcyBackForFindResults res;	
	
	public st_data_erya( String root,
			HashMap<String, ArrayList<String>> pr, ArrayList<String> dis,
			HashMap<String, String> keyi,HierarcyBackForFindResults res ){
		this.root =root;
		this.pr= pr;
		this.dis=dis;
		this.keyi=keyi;
		this.res=res;				
	}	
	
}
