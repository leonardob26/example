package com.devone.tpl.logger;

import java.sql.SQLException;
public class Errors {
	//public enum TypeError {a123, loadEdit, updData, delData, loadDataOfCombo}; 
	public static String getError(SQLException e){		
		String result = "";
		int code = 0;
		try {code = Integer.parseInt(e.getSQLState());}
		catch(NumberFormatException e1){}
		switch (code){
			case 22007:
				result = "Invalid date";
				break;
			case 23000:
				result = "You are trying to duplicate this item";
				break;
			case 23502:
				result = "You are trying to insert a empty value";	
				break;
			case 23503:
				result = "The element's identificator can't has delete or update, it has a relation with another item";	
				break;				
			case 23001:
				result = "Restriction violation";			
				break;
			case 23505:
				result = "Duplicate value";			
				break;		
			case 22001:
				result = "Insert a value too long";
				break;
			default:
				result = e.getMessage();
				Logg log = new Logg(Logg.fileSource.Error);		
				log.setSourceClass(e.getClass().getName());
				log.setSourceMethod(e.getStackTrace()[0].getMethodName());
				log.setLogError(e.getMessage());
				break;	
		}		
		return "Error: " + result;
	}
	public static String getError(Exception e) {
		try {
			String result = "Error: " + e.getMessage();
			Logg log = new Logg(Logg.fileSource.Error);		
			log.setLogError(e.getClass().getName(), e.getStackTrace()[0].getMethodName(), result);
			log.fh.close();
			return result;
		} catch (SecurityException e1) {			
			e1.printStackTrace();
			return "";
		}
	}
}
