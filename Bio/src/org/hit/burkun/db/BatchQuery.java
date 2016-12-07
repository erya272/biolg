package org.hit.burkun.db;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

public class BatchQuery {
	private boolean isDone = false;
	private DBHelper dbh;
	private int curCounter = 0;
	private int filedSize = 0;
	
	private String queryTemplate;
	
	public BatchQuery(DBHelper dbh, String tbName, String[] fileds){
		this.dbh = dbh;
		//create template
		StringBuilder sb = new StringBuilder();//StringBuilder ����ֱ��append
		sb.append("select ").append(StringUtils.join(fileds, ","));
		sb.append(" from " + tbName).append(" limit %d, %d");
		filedSize = fileds.length;
		queryTemplate = sb.toString();
		//System.out.println(queryTemplate);
	}
	public BatchQuery(DBHelper dbh, String tbName, String[] fileds,String condition){
		this.dbh = dbh;
		//create template
		StringBuilder sb = new StringBuilder();//StringBuilder 
		sb.append("select ").append(StringUtils.join(fileds, ","));
		sb.append(" from " + tbName);
		sb.append(" where ").append(condition).append(" limit %d, %d");;
		filedSize = fileds.length;
		queryTemplate = sb.toString();
//		System.out.println(queryTemplate);
	}
	public void begin(){
		dbh.connectionDB();
	}
	public void end(){
		dbh.disconnection();
	}
	
	
	public Collection<String[]> getNextBatchRealtions(int num) {
		Collection<String[]> returnres = null;
		if (!isDone) {
			String queryWithData = String.format(queryTemplate, curCounter, 
					num);
			//System.out.println(queryWithData);
			Collection<String[]> res = dbh.queryData(queryWithData, filedSize);
			returnres = res;
			if (res.size() < num) {
				// ȡ����
				isDone = true;
			}
			curCounter += num;
		}
		return returnres;
	}



	public boolean isDone() {
		return isDone;
	}



	


	
	
	
}
