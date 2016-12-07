package com.biosearch.action;

import com.opensymphony.xwork2.Action;

public class Jump implements Action{

	public String query2;
	public String getQuery() {
		return query2;
	}

	public void setQuery(String query2) {
		this.query2 = query2;
	}

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String jump(){
		System.out.println(query2);
		return SUCCESS;
	}
	
}
