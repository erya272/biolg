package com.jiao.file;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TreeParentChild {
	LinkedHashMap<String,ArrayList<String>> par_children = new LinkedHashMap<String,ArrayList<String>>();
	LinkedHashMap<String,String> child_parents = new LinkedHashMap<String,String>();
	LinkedHashMap<String, ArrayList<String>> root_leaf = new LinkedHashMap<String, ArrayList<String>>();
	public LinkedHashMap<String, ArrayList<String>> getRoot_leaf() {
		return root_leaf;
	}
	public void setRoot_leaf(LinkedHashMap<String, ArrayList<String>> root_leaf) {
		this.root_leaf = root_leaf;
	}
	public LinkedHashMap<String, ArrayList<String>> getPar_children() {
		return par_children;
	}
	public void setPar_children(LinkedHashMap<String, ArrayList<String>> par_children) {
		this.par_children = par_children;
	}
	public LinkedHashMap<String, String> getChild_parents() {
		return child_parents;
	}
	public void setChild_parents(LinkedHashMap<String, String> child_parents) {
		this.child_parents = child_parents;
	} 
	
}
