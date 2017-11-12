package com.devone.tpl;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu {
	private String element, description, page, father, pathMain;
	private short noOrder, isMenu;
	private Short rol;
	public Db db;
	public enum TypeAction {loadEdit, updData, delData, refreshTreeView, refreshMenu, loadParamAdmin};	
	
	/**
	 * Verifica si el rol del usuario tiene permiso al elemento 
	 * @param rolId+element
	 * @return true tiene acceso de escritura, false no tiene permiso a escritura
	 */
	public static boolean isAuthWrite(Short rolId, String element){
		element = element.replace("_S", "");
		element = element.replace("_jsp", "");
		return (com.devone.tpl.MyUtils.authTrans.containsKey(rolId + element)?com.devone.tpl.MyUtils.authTrans.get(rolId + element):false);
	}
	public static boolean isAuth(Short rolId, String element){
		element = element.replace("_S", "");
		element = element.replace("_jsp", "");
		return com.devone.tpl.MyUtils.authTrans.containsKey(rolId + element)	;
	}	
	public Short getRol() {
		return rol;
	}
	public void setRol(Short rol) {
		this.rol = rol;
		
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getFather() {
		return father;
	}
	public void setFather(String father) {
		this.father = father;
	}
	public short getNoOrder() {
		return noOrder;
	}
	public void setNoOrder(short noOrder) {
		this.noOrder = noOrder;
	}
	public short getIsMenu() {
		return isMenu;
	}
	public void setIsMenu(short isMenu) {
		this.isMenu = isMenu;
	}

	public String getPathMain() {
		return pathMain;
	}

	public void setPathMain(String pathMain) {
		this.pathMain = pathMain;
	}

	public void loadsAuthorized() {
		MyUtils.authorized.clear();		
		try {
			ResultSet rs = db.getData("sec_authorized", "*", "", "rol_id, element");
			while (rs.next())
				MyUtils.authorized.put(rs.getShort("rol_id") + rs.getString("element"), rs.getBoolean("is_write"));			
			rs.close();			
		} catch (SQLException e) {
			e.printStackTrace();
		} 		
	}

}
