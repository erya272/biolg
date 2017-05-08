package main;

import java.util.ArrayList;

public class cluster_data {
	
	int dis[][];
	ArrayList<String> nodes = new ArrayList<String>();
	
	public cluster_data(int dis[][],ArrayList<String> nodes){
		this.dis=dis;
		this.nodes=nodes;
	}
	
	public void setDis(int dis[][]){
		this.dis = dis;
	}
	public void setnodes(ArrayList<String> nodes){
		this.nodes = nodes;
	}
	
	public  int[][] getDis() {
		return dis;
	}
	public ArrayList<String> getnodes(){
		return nodes;
	}

}
