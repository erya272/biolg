package com.jiao.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;




public class DBHelper {
	
	public static DBHelper getServerDBHelper(){
		return new DBHelper("219.223.244.255", "biosearch", "root", "bureaucrat", false);
	}
	public static DBHelper getBaikunDBDbHelper(){
		return new DBHelper("219.223.244.255", "baikun", "baikun", "123456", false);
	}
	public static DBHelper getLocalDBHelper(){
		return new DBHelper("127.0.0.1", "biograph", "root", "root", false);
	}
	public static DBHelper getUMLSDBHelper(){
		return new DBHelper("219.223.244.255", "umls", "root", "bureaucrat", false);
	}
	
	public static DBHelper getAjiaoDBDbHelper(){
		return new DBHelper("219.223.244.255", "ajiao", "ajiao", "123456", false);
	}
	public static DBHelper getAbiodataDBDbHelper(){
		return new DBHelper("219.223.244.255", "biodata", "root", "bureaucrat", false);
	}
	public DBHelper(String host, String dbName, String uname, String pwd, boolean autoCommit) {
		this.uname = uname;
		this.pwd = pwd;
		this.dbName = dbName;
		this.host = host;
		this.autoCommit = autoCommit;
		this.connStr = "jdbc:mysql://"+this.host +"/" + this.dbName +"?user="+ this.uname+ "&password=" + this.pwd + "&useUnicode=true&autoReconnect=true";
		try {
			Class.forName("com.mysql.jdbc.Driver"); // ����mysql��
		} catch (final ClassNotFoundException e) {
			System.out.println("����ش���");
			e.printStackTrace();// ��ӡ������ϸ��Ϣ
		}
				
	}
	
	public void connectionDB(){
		if(!isConn){
			try {
				conn = DriverManager.getConnection(this.connStr);
				conn.setAutoCommit(autoCommit);
				this.isConn = true;
			} catch (final SQLException e) {
				System.out.println("��ݿ����Ӵ���");
				e.printStackTrace();
			}
		}
	}
	
	public void disconnection(){
		try {
			if (conn != null) {
				conn.close();
				conn = null;
				isConn = false;
			}
		} catch (final Exception e) {
			System.out.println("��ݿ�رմ���");
			e.printStackTrace();
		}
	}
	

	
	public boolean insert(String tableName,String[] values){
		boolean error = false;
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(tableName).append(" VALUES (?");
		for(int i=1; i<values.length; i++){
			sb.append(", ?");
		}
		sb.append(")");
		try {
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			for(int i=0; i<values.length; i++){
				ps.setString(i+1, values[i]);
			}
			ps.executeUpdate();
			if(!autoCommit){
				conn.commit();
			}
		} catch (SQLException e) {
			error = true;
			e.printStackTrace();
		}
		return error;
	}
	
	
	public int insertBatch(String tableName, Collection<String[]> values, DBTypeConverter converter){
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(tableName).append(" VALUES (?");
		int len = converter.getTypes().length;
		for(int i=1; i<len; i++){
			sb.append(", ?");
		}
		sb.append(")");
		try {
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			for(String[] items : values){
				DBTypeConverter.applyConvert(ps, items, converter);
				ps.addBatch();
			}
			ps.executeBatch();
			if(!autoCommit){
				conn.commit();
			}
			ps.clearBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return values.size();
	}
	
	public void exec(String sqlStr){
		try {
			Statement stm = conn.createStatement();
			stm.execute(sqlStr);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int insertBatch(String tableName,LinkedList<String[]> values){
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(tableName).append(" VALUES (?");
		int len = values.get(0).length;
		for(int i=1; i<len; i++){
			sb.append(", ?");
		}
		sb.append(")");
		try {
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			for(String[] items : values){
				for(int i=0; i<len; i++){
					ps.setString(i+1, items[i]);
				}
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			ps.clearBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return values.size();
	}
	
	public Collection<String[]> queryData(String queryStr, int properNum){
		PreparedStatement ps = null;
		LinkedList<String[]> data = new LinkedList<String[]>();
		try {
			ps = conn.prepareStatement(queryStr);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String[] line = new String[properNum];
				boolean isNullLine = true;
				for(int i=0; i<properNum; i++){
					line[i] = rs.getString(i+1);
					if(line[i] != null){
						isNullLine = false;
					}
				}
				if(!isNullLine){
					data.add(line);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ps != null){
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	
	public Collection<String> queryData(String queryStr){
		PreparedStatement ps = null;
		LinkedList<String> data = new LinkedList<String>();
		try {
			ps = conn.prepareStatement(queryStr);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String res = rs.getString(1);
				if(res != null){
					data.add(res);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ps != null){
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	public Collection<String[]> queryData(String tableName, String[] attr, String condition){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(StringUtils.join(attr, ","));
		sb.append(" FROM ");
		sb.append(tableName);
		sb.append(" "+ condition);
		PreparedStatement ps = null;
		LinkedList<String[]> data = new LinkedList<String[]>();
		try {
			ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			int properNum = attr.length;
			while(rs.next()){
				String[] line = new String[properNum];
				boolean isNullLine = true;
				for(int i=0; i<properNum; i++){
					line[i] = rs.getString(i+1);
					if(line[i] != null){
						isNullLine = false;
					}
				}
				if(!isNullLine){
					data.add(line);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ps != null){
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	
	public DBHelper clone(){
		return new DBHelper(host, dbName, uname, pwd, autoCommit);
	}
	
	private boolean isConn = false;
	private String connStr;
	private String host;
	private String dbName;
	private String uname;
	private String pwd;
	private Connection conn;
	private boolean autoCommit;
}
