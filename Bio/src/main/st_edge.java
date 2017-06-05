package main;

import java.io.Serializable;

public class st_edge implements Serializable {
	public String beg;
	public String end;
	public int weight;
	public  st_edge(String beg,String end,int weight){
		this.beg = beg;
		this.end = end;
		this.weight = weight;
	}	
	public void st_output(){
		System.out.println("beg: "+beg+"\tend: "+end+"\tweight: "+weight);
	}
	
}
