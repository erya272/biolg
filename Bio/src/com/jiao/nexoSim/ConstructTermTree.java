package com.jiao.nexoSim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import com.jiao.file.FileReadAndWrite;
import com.jiao.file.TreeParentChild;

public class ConstructTermTree {

	static FileReadAndWrite fraw = new FileReadAndWrite();
	static String path = "/home/lee/biolg/ScaleFreeNetwork TestData/nnbt/Nexo/";
	static int vi = 0, ei = 0;

	public TreeParentChild DataPreparing(String treefr) {
		////////   get the content of the 3 hashmap 
		TreeParentChild tpc = new TreeParentChild();
		ArrayList<String> tst = fraw.ReadData(treefr);
		LinkedHashMap<String, ArrayList<String>> par_children = new LinkedHashMap<String, ArrayList<String>>();
		LinkedHashMap<String, String> child_parents = new LinkedHashMap<String, String>();
		LinkedHashMap<String, ArrayList<String>> root_leaf = new LinkedHashMap<String, ArrayList<String>>();
		HashSet<String> hs = new HashSet<String>();
		for (String s : tst) {
			String[] ss = s.split(",");
			String par = ss[0];
			String chi = ss[1];
			hs.add(par);
//			hs.add(chi);
			if (!par_children.containsKey(par)) {
				ArrayList<String> children = new ArrayList<String>();
				children.add(chi);
				par_children.put(par, children);
			} else {
				ArrayList<String> children = par_children.get(par);
				children.add(chi);
				par_children.put(par, children);
			}
			child_parents.put(chi, par);
		}
		System.out.println(hs.size());
		Set<String> set = par_children.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String par = it.next();
			ArrayList<String> leaf = new ArrayList<String>();
			leaf = getAllLeaf(par,par_children,leaf);
			root_leaf.put(par, leaf);
		}
		tpc.setChild_parents(child_parents);
		tpc.setPar_children(par_children);
		tpc.setRoot_leaf(root_leaf);
		return tpc;
	}

	public ArrayList<String> getAllLeaf(String par,
					LinkedHashMap<String, ArrayList<String>> pc, ArrayList<String> leaf) {
		for(String child: pc.get(par)){
			if(pc.get(child) != null){
				getAllLeaf(child,pc,leaf);
			}else{
				leaf.add(child);
			}
		}
		return leaf;
	}

	public static void main(String[] args) {
		ConstructTermTree ct = new ConstructTermTree();
		ct.DataPreparing(path + "tree.csv");
		System.out.println("over");
		// ct.CreatTree(path+"AdjcentMatrix.csv", path+"jkey.csv");
		// DelegateTree<String,String> g = new DelegateTree<String,String>();
		// g.setRoot("1");
		// g.addChild("1", "1", "2");
		// g.addChild("2", "1", "3");
		// g.addChild("3", "2", "4");

	}

}
