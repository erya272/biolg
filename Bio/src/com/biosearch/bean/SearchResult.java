package com.biosearch.bean;

public class SearchResult {

	private String title;
	private String url;
	private String description;
	private String source;
	private String more;
	private String classtag;
	public String getClasstag() {
		return classtag;
	}
	public void setClasstag(String classtag) {
		this.classtag = classtag;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getMore() {
		return more;
	}
	public void setMore(String more) {
		this.more = more;
	}
}
