package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
public class cluster_data implements Serializable {
	
	int dis[][];
	ArrayList<String> nodes = new ArrayList<String>();
	
	public cluster_data(double[][] dis2,ArrayList<String> set){
//		this.dis=dis2;
		for (int i=0;i<dis2.length;i++){
			for (int j=0;j<dis2.length;j++){
				dis[i][j] = (int)dis2[i][j];
			}
		}
		
		
		this.nodes=set;
	}
	public cluster_data(int[][] dis,ArrayList<String> set){
		this.dis=dis;		
		this.nodes=set;
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
