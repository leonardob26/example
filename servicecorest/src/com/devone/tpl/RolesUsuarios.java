package com.devone.tpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.devone.tpl.logger.Errors;
import com.devone.tpl.logger.Logg;

public class RolesUsuarios {
	public Db db;
	public enum Action {loadData, loadEdit, updData};
	private String ppassword;
	private short rol=0;

	/**
	 * Devuelve las propiedades password y roles 
	 * @param puser
	 */
/*	public void getPasswordAndRoles(String puser){		
		String condition = "puser = '" + puser + "'";				
		try {
			setPassword(db.getDataValue("sec_users", "ppassword", condition));	
			rol = ((short)db.getDataValueInt("sec_users_roles", "rol_id", condition));		
			rol = ((short)db.getDataValueInt("sec_users_roles", "rol_id", condition));				
		} catch (Exception e) {
			Errors.getError(e);
		} 
	}*/
	public String getAllElementsWithRol(){
		return getAllElementsWithRol(String.valueOf(rol));
	}
	/**
	 * Devuelve todos los centros y en la columna usuario el usuario, 
	 * si es null la columna usuario es que no está seleccionado 
	 * @param usuario
	 * @return
	 */
	public String getAllElementsWithRol(String rol_id){
		String sql = "SELECT u.id, u.username, tur.rol_id FROM sec_user u left join " + 
					"(SELECT id, username, rol_id FROM sec_user where rol_id = " + rol_id + ") tur on u.id= tur.id" ;
		StringBuilder cad = new StringBuilder("<table class='table table-striped table-bordered'>");
		cad.append("<tr><td class='subtitulo' colspan='3'>Rol's users</td></tr><tr>");
		db = new Db();
			
		int iter = 0;
		try {
			ResultSet rs = db.getDataSql(sql);
			while (rs.next()){
				if (iter==3){
					cad.append("</tr><tr>");
					iter=0;
				}
				cad.append("<td><input type='checkbox' id='chk" + rs.getString("id")  + "'") ;
				if (!(rs.getString("rol_id")==null))
					cad.append(" checked='checked'");
/*				cad += "<td><input type='radio' id='chkUser' name='chkUser' value='" + rs.getString("puser")  + "'" ;
					if (!(rs.getString("rol")==null))
						cad += " checked='checked'";*/
				cad.append(">" + rs.getString("username") + "</td>");
				iter++;
			}
			rs.close();
		} catch (SQLException e) {
			Errors.getError(e);
		} finally {
			db.Close();
		}
		cad.append("</tr></table>"); 		
		return cad.toString(); 		
	}	
	@SuppressWarnings("static-access")
	public String updPermisosUser(String elementChecked, String elementNotChecked, String rolId, String puser) throws SQLException {
		String rst = "";
		int upd = 0;		
		elementNotChecked = elementNotChecked.replace("chk", "");
		String pusers="";
		String condition = "";
		if (elementChecked.length()!=0){
			elementChecked = elementChecked.replace("chk", "");
			pusers= elementChecked.substring(0, elementChecked.length()-1);
			pusers = "'" + elementChecked.replace(";", "','") + "'";
			condition = "rol_id!=" + rolId +  " and puser in (" + pusers + ")";
		} else 
			condition = "1>2";
						 
		ResultSet rs = db.getData("sec_users_roles", "rol_id, puser", condition);
		if (rs.next())
			rst = "Error: El usuario " + rs.getString("puser") + " ya está en el rol " + db.getDataValue("sec_rol_t", "rol", "id=" + rolId);
		else {
			if (!elementNotChecked.isEmpty())
				upd = delElementNotInActual(elementNotChecked, rolId);
			if (!elementChecked.isEmpty())
				upd += insElementNew(elementChecked, rolId);
			rst = Integer.toString(upd);
			if (upd>0){
				Logg log = new Logg(Logg.fileSource.App);
				log.loggerApp.info("DELETE -- User=" + puser + " -- Tabla=" + "sec_users_roles" +  " -- users=" + elementNotChecked + "; rol=" + rolId);
				log.loggerApp.info("INSERT -- User=" + puser + " -- Tabla=" + "sec_users_roles" +  " -- users=" + elementChecked + "; rol=" + rolId);
				log.fh.close();
			}
		}
		rs.close();
		return rst;
	}
	private int insElementNew(String elementChecked, String rolId) {
		String condition = "rol_id = " + rolId ;		
		ResultSet rs = db.getData("sec_users_roles", "puser", condition);
		ArrayList<String> itemsAnteriores = new ArrayList<String>();
		int result = 0;
		try {
			while (rs.next())
				itemsAnteriores.add(rs.getString("puser"));
			rs.close();	

		String[] users = elementChecked.split(";");
		for (String puser : users) {
			if (!itemsAnteriores.contains(puser))
				result += Integer.parseInt(db.insData1("sec_users_roles", new String[]{"rol_id", "puser"}, 
						new String[]{rolId, puser}, new String[]{"INTEGER", "TEXT"}));
		}
		} catch (SQLException e) {
			Errors.getError(e);
		} catch (Exception e){
			Errors.getError(e);
		}
		return result;
	}
	private int delElementNotInActual(String elementNotChecked, String rolId){
		/* Elimina todos los que no vengan en items actuales para el rol*/
		elementNotChecked = elementNotChecked.substring(0,elementNotChecked.length()-1);
		elementNotChecked = "'" + elementNotChecked.replace(";", "','") + "'";				
		String condition = "rol_id = " + rolId + " and puser in (" + elementNotChecked + ")";		
		return Integer.parseInt(db.delData("sec_users_roles", condition));	
	}
	public void setPassword(String ppassword) {
		this.ppassword = ppassword;
	}
	public String getPassword() {
		return ppassword;
	}

	public short getRol() {
		return rol;
	}

	public void setRol(short rol) {
		this.rol = rol;
	}
}
