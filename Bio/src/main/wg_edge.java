package main;

public class wg_edge {
	public int beg;
	public int end;
	public int weight;
	
	public wg_edge(	int beg,	int end,	int weight){
		this.beg = beg;
		this.end = end;
		this.weight = weight;
	}
	public void output(){
		System.out.println("beg: "+beg+"\tend:"+end+"\tweight:"+weight);
	}
	

}
