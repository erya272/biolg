package main;

import java.util.ArrayList;

import org.apache.hadoop.mapreduce.v2.api.protocolrecords.GetDiagnosticsRequest;

public class cluster_data {
	
	int dis[][];
	ArrayList<String> nodes = new ArrayList<String>();
	
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
