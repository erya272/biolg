package com.jiao.nexoSim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jiao.file.FileReadAndWrite;

public class ComputeInformationContent {
	// Compute IC
	String root = "joining_root";
	static String frpath = "/home/lee/biolg/ScaleFreeNetwork TestData/keys/";
	static int N = 4987;//网络中Gene个数
	static FileReadAndWrite fraw = new FileReadAndWrite();
	ComputeSim cs = new ComputeSim();

	public ArrayList<ArrayList<String>> Readres(String fr,String reg) {
		// 读入easy,middle,hard的keys,path来返回keys,path
		ArrayList<String> read = fraw.ReadData(fr);
		ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>>();
		for (String s : read) {
			String[] ss = (s.split(":")[1]).split(reg);
			ArrayList<String> subres = new ArrayList<String>();
			for (int i = 0; i < ss.length; i++) {
				if(ss[i].contains("["))
					ss[i] =ss[i].substring(1);
				if(ss[i].contains("]"))
					ss[i] =ss[i].substring(0, ss[i].length()-1);
				subres.add(ss[i]);
			}
			res.add(subres);
		}
		return res;
	}

	public ArrayList<ArrayList<String>> ReturnIntermediateNodes(ArrayList<ArrayList<String>> keys,
			ArrayList<ArrayList<String>> path) {
		//return the set of intermediate nodes
		ArrayList<ArrayList<String>> InterNodes = new ArrayList<ArrayList<String>>();
		for(int i =0 ; i <path.size();i++){
//			System.out.println(keys.get(i).size());
			ArrayList<String> subInterNodes = new ArrayList<String>();
			ArrayList<String> pathi = path.get(i);
//			System.out.println(i+"\t"+pathi);
			HashSet<String> hs = new HashSet<String>();
			for(String s:pathi){
				String[] ss= s.split(",");
				String sou = ss[0];
				String tar = ss[1];
				hs.add(sou);
				hs.add(tar);
//				System.out.println(s);
//				System.out.println("sou: "+sou+"\ttar:"+tar);
//				System.out.println(keys.get(i));
				if(!keys.get(i).contains(sou)&&!subInterNodes.contains(sou))
					subInterNodes.add(sou);
				if(!keys.get(i).contains(tar)&&!subInterNodes.contains(tar))
					subInterNodes.add(tar);
			}
			InterNodes.add(subInterNodes);
//			System.out.println(hs.size()+"\t"+InterNodes);
		}
		return InterNodes;
	}

	public ArrayList<String> GetPathTerms(ArrayList<String> keys) {
		/*
		 * 1.根据所有关键字找到他们的对应Term的最低公共祖先parent
		 * 2.找到parent到root的path上的Terms(这里包含parent不包含root)
		 * 3.找出其他term直接注释的基因与intermediate nodes的交集
		 */
		ArrayList<String> PathTerms = new ArrayList<String>();
		String parent = cs.getAllkeywordsRoot(keys);
		System.out.println(keys);
		while (parent != root && !parent.equals(root)) {
			PathTerms.add(parent);
			parent = cs.getTermParent(parent);
		}
		System.out.println(PathTerms.toString());
		return PathTerms;
	}
	public double AllNodesSimilarity(ArrayList<String> keys, ArrayList<String> IntermediateNodes){
		//计算所有关键字和中间节点的similarity
		keys.addAll(IntermediateNodes);
		String index = cs.getAllkeywordsRoot(keys); /// find their common ansister
		double value  = cs.TwoTermSim(index, N);
		System.out.println(value);
		return value;
	}
	
	
	public boolean isNumberic(String str) {
		//判断一个字符串是不是一个数字
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	public ArrayList<String> GetSubSetofIntermediateNodes(ArrayList<String> path,ArrayList<String> IMNodes){
		//找到path上的terms(不包括根节点和包含所有的关键字的term)的直接注释基因和中间节点的交集
		ArrayList<String> subset =  new ArrayList<String>();
		for(int i = 1;i <path.size();i++){
			ArrayList<String> children = cs.getTermChild(path.get(i));
			for(String child :children){
				if(!isNumberic(child)){
					if(IMNodes.contains(child)){
						subset.add(child);
					}
				}
			}
		}
		return subset;
	}

	public static void main(String[] args) {
		ComputeInformationContent cic = new ComputeInformationContent();
		ArrayList<ArrayList<String>> keys = cic.Readres(frpath+"result/ST/set_two/keys.csv", ", ");
		ArrayList<ArrayList<String>> path = cic.Readres(frpath+"result/ST/set_two/path.csv", ", ");
		ArrayList<ArrayList<String>> InterNodes =cic.ReturnIntermediateNodes(keys, path);
		for(int i = 0 ; i <InterNodes.size() ; i++){	
			double value =cic.AllNodesSimilarity(keys.get(i), InterNodes.get(i));
			fraw.WriteToFile(frpath+"result/ST/set_two/globalSimilarity.csv", value+"");
		}
		System.out.println("over!");
	
	}

}
