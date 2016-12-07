package com.biosearch.bean;

import java.util.List;

public class InstantSearch {

	private String queryClass;
	private String follow;
	private List<String> query;
	private int tag;
	
	public String getQueryClass() {
		return queryClass;
	}
	public List<String> getQuery() {
		return query;
	}
	public void setQuery(List<String> query) {
		this.query = query;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	public void setQueryClass(String queryClass) {
		this.queryClass = queryClass;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	
}
