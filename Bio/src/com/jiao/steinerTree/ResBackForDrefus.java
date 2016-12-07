package com.jiao.steinerTree;

import java.util.HashMap;

public class ResBackForDrefus {
	HashMap<Integer, String> jkey ;
	HashMap<String, Integer> keyj ;
	double[][] Dis ;
	public HashMap<Integer, String> getJkey() {
		return jkey;
	}
	public void setJkey(HashMap<Integer, String> jkey) {
		this.jkey = jkey;
	}
	public HashMap<String, Integer> getKeyj() {
		return keyj;
	}
	public void setKeyj(HashMap<String, Integer> keyj) {
		this.keyj = keyj;
	}
	public double[][] getDis() {
		return Dis;
	}
	public void setDis(double[][] dis) {
		Dis = dis;
	}
}
