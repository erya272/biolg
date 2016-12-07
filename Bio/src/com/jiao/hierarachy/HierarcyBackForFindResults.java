package com.jiao.hierarachy;

import java.util.ArrayList;
import java.util.HashMap;

public class HierarcyBackForFindResults {
	//设计用来针对二分查找层次，在找到层次以后，组合返回所有的相关项，1层次的index，2该层次的所有cluster，3包含所有关键字的3个cluster
	String index;
	String root;
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	
	ArrayList<String> indexContent;
	HashMap<String,ArrayList<String>> clusterContainKey = new HashMap<String,ArrayList<String>>();
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public ArrayList<String> getIndexContent() {
		return indexContent;
	}
	public void setIndexContent(ArrayList<String> indexContent) {
		this.indexContent = indexContent;
	}
	public HashMap<String, ArrayList<String>> getClusterContainKey() {
		return clusterContainKey;
	}
	public void setClusterContainKey(
			HashMap<String, ArrayList<String>> clusterContainKey) {
		this.clusterContainKey = clusterContainKey;
	}
	

}
