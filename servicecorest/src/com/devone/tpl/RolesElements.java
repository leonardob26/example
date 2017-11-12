package com.devone.tpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.devone.tpl.logger.Errors;
import com.devone.tpl.logger.Logg;

public class RolesElements {
	public Db db;
	public enum Action {loadData, loadEdit, updData};
	/**
	 * Devuelve todos los centros y en la columna usuario el usuario, 
	 * si es null la columna usuario es que no está seleccionado 
	 * @param usuario
	 * @return
	 */
	public String getAllUserWithCentros(String rolId) throws SQLException, Exception {
		String sql = "SELECT e.element, is_write, ta.element as taelement, description FROM sec_element e left join " + 
					"(SELECT element, is_write FROM sec_authorized where rol_id = " + rolId + ") ta on e.element= ta.element";
		ResultSet rs = db.getDataSql(sql);
		StringBuilder rst = new StringBuilder("<table class='table table-striped table-bordered'>");
		
		rst.append("<tr><td colspan='6'>Rol's elements</td></tr>");
		rst.append("<tr><td width='25%'>Items</td><td width='7%'>Writeable</td><td width='25%'>Items</td><td width='7%'>Writeable</td><td width='25%'>Items</td><td width='7%'>Writeable</td></tr>") ;
		rst.append("<tr>");
		int iter = 0;
		boolean isWrite; 
		while (rs.next()){
			isWrite = false;
			if (iter==3){
				rst.append("</tr><tr>");
				iter=0;
			}
			rst.append("<td><input type='checkbox' id='chkEl_" + rs.getString("element")  + "'") ;
			if (!(rs.getString("taelement")==null)){
				rst.append(" checked='checked'");
				if (rs.getBoolean("is_write"))
					isWrite = true;
			}
			rst.append(">" + rs.getString("description") + "</td>");
			// Parte de isWrite
			rst.append("<td><input type='checkbox' id='chkWr_" + rs.getString("element")  + "'") ;
			if (isWrite) //Existe el elemento
				rst.append(" checked='checked'");
			rst.append("></td>");
			iter++;
		}
		rst.append("</tr></table>"); 
		return rst.toString(); 
	}	
	@SuppressWarnings("static-access")
	public int updPermisosUser(String elementChecked, String elementNotChecked, String isWrite, String rol, String puser) throws SQLException, Exception {
		int result = 0;
		elementChecked = elementChecked.replace("chkEl_", "");
		elementNotChecked = elementNotChecked.replace("chkEl_", "");
		
		if (!elementNotChecked.isEmpty())
			result = delElementNotInActual(elementNotChecked, rol);
		if (!elementChecked.isEmpty())
			result += insElementNew(elementChecked, rol, isWrite);
		if (result>0){
			Logg log = new Logg(Logg.fileSource.App);
			log.loggerApp.info("DELETE -- User=" + puser + " -- Tabla=" + "sec_authorized_t" +  " -- Elements=" + elementNotChecked + "; rol=" + rol);
			log.loggerApp.info("INSERT -- User=" + puser + " -- Tabla=" + "sec_authorized_t" +  " -- Elements=" + elementChecked + "; rol=" + rol);
			log.fh.close();
		}
		return result;
	}

	private int insElementNew(String elementChecked, String rolId, String isWrite) {
		String condition = "rol_id = " + rolId ;		
		ResultSet rs = db.getData("sec_authorized", "element, is_write", condition);
		
		ArrayList<String> itemsAnteriores = new ArrayList<String>();
		ArrayList<String> isWriteAnteriores = new ArrayList<String>();
		
		int result = 0;
		try {
			while (rs.next()){
				itemsAnteriores.add(rs.getString("element"));
				isWriteAnteriores.add(rs.getString("is_write"));
			}
			rs.close();	
		} catch (SQLException e) {
			Errors.getError(e);
		}
		String[] elements = elementChecked.split(";");
		String[] isWrites = isWrite.split(";");
		int iter = 0; 
		int posElement = 0;
		for (String element : elements) {
			posElement = itemsAnteriores.indexOf(element);
			if (posElement==-1){				
				String rst = db.insData1("sec_authorized", new String[]{"rol_id", "element", "is_write"}, 
						new String[]{rolId, element, isWrites[iter]}, new String[]{"SHORT", "TEXT", "BOOLEAN"}); 
				try{
					result += Integer.parseInt(rst);
				} catch (NumberFormatException e){
					return 0;
				}
			}
			else { 				
				if (!isWrites[iter].equals(isWriteAnteriores.get(posElement))){
					condition = "rol_id = " + rolId + " and element ='" + element + "'"; 
					result += Integer.parseInt(db.updData1("sec_authorized", new String[]{"is_write"}, new String[]{isWrites[iter]}
					, new String[]{"BOOLEAN"}, condition));
				}
			}	
			iter++;
		}
		return result;
	}
	private int delElementNotInActual(String elementNotChecked, String rolId){
		/* Elimina todos los que no vengan en items actuales para el rol*/
		elementNotChecked = elementNotChecked.substring(0,elementNotChecked.length()-1);
		elementNotChecked = "'" + elementNotChecked.replace(";", "','") + "'";				
		String condition = "rol_id = " + rolId + " and element in (" + elementNotChecked + ")";		
		return Integer.parseInt(db.delData("sec_authorized", condition));	
	}
}
