package com.jiao.steinerTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biosearch.bean.Edge;
import com.biosearch.bean.Node;
import com.jiao.element.Graph;
import com.jiao.file.FileReadAndWrite;
import com.jiao.hierarachy.InnerProduct;

public class DijkstraST {
	
	static FileReadAndWrite fraw = new FileReadAndWrite();
	
	public Graph ReadNodes(String fn ,String fed){
		Graph G = new Graph();
		ArrayList<Node> nodes = new ArrayList<Node>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<String> ns = fraw.ReadData(fn);
		ArrayList<String> es = fraw.ReadData(fed);
		for(String s: ns){
			Node node = new Node();
			node.setTitle(s);
			nodes.add(node);
		}
		for(String e :es){
			String  sou = e.split(",")[0];
			String  tar = e.split(",")[1];
			double weight = Double.valueOf(e.split(",")[2]);
			Edge edge = new Edge();
			edge.setSource(sou);
			edge.setTarget(tar);
			edge.setWeight(weight);
			edges.add(edge);
		}
		G.setNodes(nodes);
		G.setEdges(edges);
		return G;
	}
	public static String[] getBinaryValue(Set<Node> set) {
		// 求出从0到集合子集数目之间的数的二进制形式
		int size = set.size();
		int m = (int) Math.pow(2, size) - 1;
		String[] results = new String[m + 1];
		for (int i = m; i > -1; i--) {
			StringBuffer sb = new StringBuffer(Integer.toBinaryString(i));
			int length = sb.length();
			if (length < size) {
				for (int j = 0; j < size - length; j++) {
					sb.insert(0, "0");
				}
			}
			results[i] = sb.toString();
		}
		return results;
	}

	public ArrayList<Set<Node>> getSubset(Set<Node> set) {
		// 根据二进制字符串生成子集
		ArrayList<Set<Node>> result = new ArrayList<Set<Node>>();
		Node[] items = new Node[set.size()];
		int i = 0;
		for (Node item : set) {
			items[i] = item;
			i++;
		}
		String[] binaryValue = getBinaryValue(set);
		for (int j = 0; j < binaryValue.length; j++) {
			String value = binaryValue[j];
			Set<Node> subset = new HashSet<Node>();
			for (int k = 0; k < value.length(); k++) {
				if (value.charAt(k) == '1')
					subset.add(items[k]);
			}
			result.add(subset);
		}
		return result;
	}
	
	public Node RootGet(Set<Node> R){
		Node[] rr = (Node[]) R.toArray();
		int index = (int) (Math.random()*rr.length);
		Node r0 = rr[index];
		return r0;
		
	}
	
	public Set<Node> SetSubtract(Set<Node> A, Set<Node> B) {
		for (Node it : B) {
			A.remove(it);
		}
		return A;
	}
	
	public void DijST(Graph G, Set<Node> R){
		Node r0 = RootGet(R);
		Set<Node> RR = new HashSet<Node>();
		RR = R;
		R.remove(r0);
		HashMap<InnerProduct,Double> L = new HashMap<InnerProduct,Double>();
		HashMap<InnerProduct,Set<InnerProduct>> b = new HashMap<InnerProduct,Set<InnerProduct>>();
		Set<InnerProduct> N = new HashSet<InnerProduct>();
		Set<InnerProduct> P = new HashSet<InnerProduct>();
		ArrayList<Node> V = new ArrayList<Node>();
		V = (ArrayList<Node>) G.getNodes();
		ArrayList<Set<Node>> set =  getSubset(R);
	    for(Node v :V){
	    	for(Set<Node> I :set){
	    		InnerProduct vI = new InnerProduct();
	    		vI.setV(v);
	    		vI.setI(I);
	    		if(I.isEmpty()){
	    			L.put(vI, (double) 0);
	    		}
	    		else if(R.contains(v)&&I.size()==1&&I.iterator().next().equals(v)&&I.iterator().next() == v){
	    			L.put(vI, (double) 0);	
	    		}else{
	    			L.put(vI,Double.MAX_VALUE);
	    		}
	    		Set<InnerProduct> value = new HashSet<InnerProduct>(); 
	    		b.put(vI, value);
	    	}	
	    	Set<Node> kong = new HashSet<Node>();
	    	InnerProduct in = new InnerProduct();
	    	in.setV(v);
	    	in.setI(kong);
	    	P.add(in);	
	    }
	    while(R.iterator().hasNext()){
    		Node s = R.iterator().next();
    		Set<Node> S = new HashSet<Node>();
    		S.add(s);
    		InnerProduct n = new InnerProduct();
    		n.setI(S);
    		n.setV(s);
    		N.add(n);
    	}
	    
	    InnerProduct Rr0 = new InnerProduct();
	    Rr0.setV(r0);
	    Rr0.setI(R);
	    
	    while(!P.contains(Rr0)){
	    	Iterator<InnerProduct> it = N.iterator();
	    	while(it.hasNext()){
	    		InnerProduct ip = it.next();
	    		InnerProduct RI = new InnerProduct();
	    		Set<Node> node = SetSubtract(RR,ip.getI());
	    		System.out.println(RI+","+node);
	    	}
	    	
	    	
	    	
	    	
	    	
	    }
	    
		
		
	}
	public static void main(String[] args){
		
		DijkstraST dst = new DijkstraST();
		Node n1 = new Node();
		n1.setTitle("1122");
		Node n2 = new Node();
		n2.setTitle("3344");
		Node n3 = new Node();
		n3.setTitle("5566");
		Set<Node> R = new HashSet<Node>();
		R.add(n1);
		R.add(n2);
		R.add(n3);
		System.out.println(dst.getSubset(R));
		
		
		
	}
	

}
