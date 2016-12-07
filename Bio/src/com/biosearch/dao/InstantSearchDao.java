package com.biosearch.dao;

import java.util.List;

public interface InstantSearchDao {

	public List<String[]> getInstantResult(String query);
}
