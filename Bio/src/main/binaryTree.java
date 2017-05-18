package main;

public class binaryTree {
//	public int nodes;
	public int level;
	public int cluster;
	
	public binaryTree father;
	public binaryTree leftchild,rightchild;
	
	public binaryTree(int level, int cluster){
		this.level = level;
		this.cluster = cluster;
		this.leftchild = null;
		this.rightchild = null;		
	}


}
