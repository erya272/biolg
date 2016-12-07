package org.hit.burkun.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class DBTypeConverter implements Cloneable{

	public enum DATATYPE{I,F,D,S}; //Integer, Float, Double, String(date)
	private DATATYPE[] datatypes;

	
	public DBTypeConverter(DATATYPE[] types){
		this.datatypes = new DATATYPE[types.length];
		for(int i=0; i< types.length; i++){
			if(types[i] == null){
				this.datatypes[i] = DATATYPE.S;
			}else{
				this.datatypes[i] = types[i];
			}
		}
	}
	

	@Override
	public Object clone(){
		return new DBTypeConverter(datatypes);
	}




	public DATATYPE[] getTypes(){
		return this.datatypes;
	}
	
	public static void applyConvert(PreparedStatement ps, String[] items, DBTypeConverter converter) throws SQLException{
		DATATYPE[] types = converter.getTypes();
		for(int i=0; i < items.length; i++){
			if(items[i] == null) items[i] = "";
			switch(types[i]){
				case I:
					try{
						int iitem = Integer.parseInt(items[i].trim());
						ps.setInt(i + 1, iitem);	
					}catch(NumberFormatException e){
						//logger.warn("Can not convert " + items[i] + " to int! use NULL instead!");
						ps.setNull(i+1, java.sql.Types.INTEGER);
						//ps.setInt(i + 1, Integer.MIN_VALUE);
					}
					break;
				case F:
					try{
						float fitem = Float.parseFloat(items[i].trim());
						ps.setFloat(i+1, fitem);	
					}catch(NumberFormatException e){
						//logger.warn("Can not convert " + items[i] + " to float! use NULL instead!");
						ps.setNull(i+1, java.sql.Types.FLOAT);
						//ps.setFloat(i+1, Float.MIN_VALUE);
					}
					break;
				case D:
					try{
						double ditem = Double.parseDouble(items[i].trim());
						ps.setDouble(i+1, ditem);
					}catch(NumberFormatException e){
						//logger.warn("Can not convert " + items[i] + " to Double! use NULL instead!");
						ps.setNull(i+1, java.sql.Types.DOUBLE);
						//ps.setDouble(i+1, Double.MIN_VALUE);
					}
					break;
				case S:
				default:
					String it = items[i].trim();
					if(it.isEmpty()){
						ps.setNull(i+1, java.sql.Types.VARCHAR);
					}else{
						ps.setString(i+1, items[i].trim());
					}
					break;
			}
		}
		
	}
	
	

}
