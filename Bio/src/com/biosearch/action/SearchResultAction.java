package com.biosearch.action;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.Action;
import com.biosearch.bean.SearchResult;

public class SearchResultAction implements Action {

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	List<SearchResult> re = new ArrayList<SearchResult>();
	
	public String searchResult(){
		re = makeResult();
		return SUCCESS;
	}
	
	public List<SearchResult> getRe() {
		return re;
	}

	public void setRe(List<SearchResult> re) {
		this.re = re;
	}

	public List<SearchResult> makeResult(){
		
		SearchResult t1 = new SearchResult();
		t1.setTitle("Mesh-Home-NCBI");
		t1.setDescription("laaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		t1.setSource("http://www.baidu.com");
		t1.setClasstag("gene");
		SearchResult t2 = new SearchResult();
		t2.setTitle("Mesh-Home-NCBI");
		t2.setDescription("laaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		t2.setSource("http://www.baidu.com");
		t2.setClasstag("disease");
		re.add(t1);
		re.add(t2);
		return re;
	}
}
