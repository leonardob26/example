package com.devone.tpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.devone.tpl.logger.Errors;

public class ParamAdmin {
/*smtp_host character varying(254), ldap_host character varying(254), security_authentication character varying(25),
  base_dn character varying(255),  origen_authentication character varying(4),  home_birt*/
	public static String smtpHost="", ldapHost="", securityAuthentication="", 
	baseDn="", origenAuthentication="", homeBirt="";
	public static short anoHistorico=2000;
	public Db db;
	public void getParamAdmin() {
    	try {
    		ResultSet rs = db.getData("param_admin");
			if (rs.next()){
				smtpHost = rs.getString("smtp_host");
				ldapHost = rs.getString("ldap_host"); 
				securityAuthentication = rs.getString("security_authentication");
				baseDn = rs.getString("base_dn"); 
				origenAuthentication = rs.getString("origen_authentication");
				homeBirt = rs.getString("home_birt");				
			}
		} catch (SQLException e) {
			Errors.getError(e);
		} 
	}
	
}
