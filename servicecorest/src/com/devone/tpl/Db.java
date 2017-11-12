package com.devone.tpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.devone.tpl.logger.Errors;

public class Db {
	public Connection conn=null;
	public DataSource ds;
	private int resultSetType = ResultSet.TYPE_FORWARD_ONLY;
	private String table, condition, fieldText, fieldValue;
	public Db() {
		try {
			if (ds==null)
				CreateDatasource();
			conn = ds.getConnection();
		} catch (NamingException e) {
			Errors.getError(e);
		}
		catch (SQLException e) {
			Errors.getError(e);
		}
	}
	public Db(DataSource ds) {
		try {
			conn = ds.getConnection();
		} 
		catch (SQLException e) {
			Errors.getError(e);
		}
	}
	private void CreateDatasource() throws NamingException {
		Context context = new InitialContext();
		ds = (DataSource) context.lookup("java:comp/env/serviceco");
	}

	public void Close() {
		try {
			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			Errors.getError(e);
		}
	}
	
	public boolean getConn() {
		try {
			if (conn==null || conn.isClosed()) {
				conn = ds.getConnection();
			}
			return true;
		} catch (SQLException e){
			Errors.getError(e);
			return false;
		}
	}

	public String getClose(){
		Close();
		return "";
	}
	/**
	 * 
	 * @param table Table or tables ex: cencos join filial using(id_filial) Or cencos
	 * @param select Select ex: ccosto, descripcion Or "" or "*" for all
	 * @param condition Condition of select ex: ccosto='101012'
	 * @param order Order of select ex: ccosto, descripcion
	 * @param limit Limit of result query "" is all
	 * @param offset Offset of result < count(*)
	 * @return
	 */
	public ResultSet getData(String table, String select, String condition,
			String order, String limit, String offset) {
		return getRs(getSentenceSelect(table, select, condition, order, limit, offset));
	}
	/**
	 * 
	 * @param table Table or tables ex: cencos join filial using(id_filial) Or cencos
	 * @param select  Select ex: ccosto, descripcion Or "" or "*" for all
	 * @param condition Condition of select ex: ccosto='101012'
	 * @param order Order of select ex: ccosto, descripcion
	 * @return
	 */
	public ResultSet getData(String table, String select, String condition,
			String order) {
		return getRs(getSentenceSelect(table, select, condition, order));
	}

	public ResultSet getData(String table, String select, String condition) {
		return getRs(getSentenceSelect(table, select, condition, ""));
	}
	/**
	 * @param table
	 * @param select
	 * @return
	 */
	public ResultSet getData(String table, String select) {
		return getRs(getSentenceSelect(table, select, "", ""));
	}

	public ResultSet getData(String table) {
		return getRs(getSentenceSelect(table, "", "", ""));
	}

	public ResultSet getDataSql(String sql) {
		return getRs(sql);
	}
	
	/**
	 * @param cad Es la sentencia Sql a ejecutar
	 */
	private ResultSet getRs(String cad) {
		try {
			Statement stmt = conn.createStatement(resultSetType,
					ResultSet.CONCUR_READ_ONLY);
			return stmt.executeQuery(cad);
		} catch (SQLException e) {
			Errors.getError(e);
		} catch (Exception e) {
			Errors.getError(e);
		}
		return null;
	}

	private String getSentenceSelect(String table, String select, String condition, String order, String limit, String offset) {
		StringBuilder cad = new StringBuilder("select ") ;
		cad.append(select.isEmpty() ? "*" : select);
		cad.append(" from " + table);
		cad.append(condition.isEmpty() ? "" : " where " + condition);
		cad.append(order.isEmpty() ? "" : " order by " + order);
		cad.append(limit.equals("0")?"": " limit " + limit);
		cad.append(offset.equals("0")?"": " offset " + offset);
		return cad.toString();
	}
	
	private String getSentenceSelect(String table, String select, String condition, String order) {
		StringBuilder cad = new StringBuilder("select ");
		cad.append(select.isEmpty() ? "*" : select);
		cad.append(" from " + table);
		cad.append(condition.isEmpty() ? "" : " where " + condition);
		cad.append(order.isEmpty() ? "" : " order by " + order);
		return cad.toString();
	}
	
	public int getDataValueInt(String table, String field, String condition) {
		String tmp = getDataValue(table, field, condition);
		if (tmp==null || tmp.isEmpty())
			return 0;
		else
			return Integer.parseInt(tmp);
	}
	
	public int getDataValueInt(String sql) {
		String tmp = getDataValue(sql);
		if (tmp==null || tmp.isEmpty())
			return 0;
		else
			return Integer.parseInt(tmp);
	}
	public boolean getDataValueBoolean(String table, String field, String condition) {
		boolean value = false;
		try {
			if (getConn()){
				ResultSet rs = getData(table, field, condition);
				if (rs.next())			
					value = rs.getBoolean(1);
				rs.close();
			}
		} catch (SQLException e) {
			Errors.getError(e);			
		}
		return value;
	}	
	public String  getDataValue(String table, String field, String condition) {
		String value = "";
		try {
			if (getConn()){
				ResultSet rs = getData(table, field, condition);
				if (rs.next())			
					value = rs.getString(1);
				rs.close();
			}
		} catch (SQLException e) {
			value = Errors.getError(e);
		}
		return value;
	}
	public String getDataValue(String sql) {
		String value = "";
		try {
			if (getConn()){
				ResultSet rs = getDataSql(sql);			
				if (rs.next())		
					value = rs.getString(1);
				rs.close();
			}
		} catch (SQLException e) {
			value = Errors.getError(e);
		}
		return value;
	}

	public String delData(String table, String condition) {
		String deleteString = "DELETE FROM " + table + (!condition.isEmpty()?" WHERE " + condition : "");
		try {
			Statement stmt = conn.createStatement();
			return Integer.toString(stmt.executeUpdate(deleteString));
		} catch (SQLException e) {
			return Errors.getError(e);
		}
	}

	public String insData1(String table, String[] parameters, String[] values, String[] types) {
		String insertString = "INSERT INTO " + table + "(";
		String paramString = " VALUES (";
		for (String parameter : parameters) {
			insertString += parameter + ",";
			paramString += "?,";
		}
		insertString = insertString.substring(0, insertString.length() - 1)
				+ ")";
		insertString += paramString.substring(0, paramString.length() - 1)
				+ ")";
		int value=0;
		short valueShort = 0;
		try {
			PreparedStatement ins = conn.prepareStatement(insertString);
			for (int i = 0; i < values.length; i++){
				
				switch (MyUtils.TypeData.valueOf(types[i])){
				case TEXT:
					ins.setString(i + 1, values[i].trim());
					break;
				case INTEGER:
					if ((values[i]!=null) && !values[i].isEmpty()){
						try{
							value = Integer.parseInt(values[i]);
							ins.setInt(i + 1, value);
						} catch (NumberFormatException e) {
							Errors.getError(e);
							return "Error: The field " + parameters[i] + " must be a number";
						}
					} else
						ins.setNull(i + 1, java.sql.Types.INTEGER);
					break;
				case SHORT:
					if ((values[i]!=null) && !values[i].isEmpty()){
						try{
							valueShort = Short.parseShort(values[i]);
							ins.setShort(i + 1, valueShort);
						} catch (NumberFormatException e) {
							Errors.getError(e);
							return "Error: The field " + parameters[i] + " must be a number";
						}
					} else
						ins.setNull(i + 1, java.sql.Types.SMALLINT);
					break;
				case NUMERIC:
					float valueNum = 0;
					if ((values[i]!=null) && !values[i].isEmpty()){
						try{
							valueNum = Float.parseFloat(values[i]);
							ins.setFloat(i + 1, valueNum);
						} catch (NumberFormatException e) {
							Errors.getError(e);
							return "Error: The field " + parameters[i] + "  must be a number";
						}
					}						
					else
						ins.setNull(i + 1, java.sql.Types.FLOAT);
					break;					
				case DATE:
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",new Locale("es","ES","Traditional"));
					try {
						ins.setDate(i + 1, new java.sql.Date(sdf.parse(values[i]).getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					break;
				case BOOLEAN:
					ins.setBoolean(i + 1, ((values[i].equals("1") || values[i].equals("true"))?true:false));
					break;
				default:
					ins.setString(i + 1, values[i]);
					break;					
				}
			}
			return Integer.toString(ins.executeUpdate());
		} catch (SQLException e) {
			return Errors.getError(e);
		}
	}
	public String updData(String table, String[] parameters, String[] values,  String[] types, String condition){
		if (condition.equals("-1"))
			return insData1(table, parameters, values, types);
		else
			return updData1(table, parameters, values, types, condition);
	}
	public String updData(String sql){
		try {
			PreparedStatement upd = conn.prepareStatement(sql);
			return Integer.toString(upd.executeUpdate());
		} catch (SQLException e) {
			return Errors.getError(e);
		}
	}
	public String updData1(String table, String[] parameters, String[] values, String[] types,	String condition) {
		String updateString = "UPDATE " + table + " SET ";
		for (String parameter : parameters)
			updateString += parameter + "=?,";
		updateString = updateString.substring(0, updateString.length() - 1);
		if (!condition.isEmpty())
			updateString += " WHERE " + condition;
		try {
			PreparedStatement upd = conn.prepareStatement(updateString);
			for (int i = 0; i < values.length; i++){
				
				switch (MyUtils.TypeData.valueOf(types[i])){
				case TEXT:
					upd.setString(i + 1, values[i].trim().length()!=0?values[i].trim():null);
					break;
				case INTEGER:
					if ((values[i]!=null) && !values[i].isEmpty())
						upd.setInt(i + 1, Integer.parseInt(values[i]));
					else
						upd.setNull(i + 1, java.sql.Types.INTEGER);
					break;
				case SHORT:
					if ((values[i]!=null) && !values[i].isEmpty())
						upd.setShort(i + 1, Short.parseShort(values[i]));
					else 
						upd.setNull(i + 1, java.sql.Types.INTEGER);
					break;
				case DATE:
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",new Locale("es","ES","Traditional"));
					try {
							upd.setDate(i + 1, new java.sql.Date(sdf.parse(values[i]).getTime()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					break;
				case BOOLEAN:
					upd.setBoolean(i + 1, ((values[i].equals("1") || values[i].equals("true"))?true:false));
					break;
				case NUMERIC:
					if ((values[i]!=null) && !values[i].isEmpty())
						upd.setFloat(i + 1, Float.parseFloat(values[i]));
					else 
						upd.setNull(i + 1, java.sql.Types.FLOAT);
					break;
				default:
					upd.setString(i + 1, values[i]);
					break;					
				}	
			}

			return Integer.toString(upd.executeUpdate());
		} catch (SQLException e) {
			return Errors.getError(e);
		} 
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getFieldText() {
		return fieldText;
	}

	public void setFieldText(String fieldText) {
		this.fieldText = fieldText;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public void setOrder(String order) {
	}

	public void setSqlSel(String sqlSel) {
	}

	public int getResultSetType() {
		return resultSetType;
	}

	public void setResultSetType(int resultSetType) {
		this.resultSetType = resultSetType;
	}
}
