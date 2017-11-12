package com.devone.tpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.devone.tpl.logger.Errors;
public class MyUtils {
	public static enum TypeData {INTEGER, TEXT, DATE, BOOLEAN, SHORT, NUMERIC, OTHER};
	public static NumberFormat nf  = new java.text.DecimalFormat("######0.##");
	public static java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
	public static java.text.DateFormat dfWithTime = new java.text.SimpleDateFormat("dd/MM/yyyy KK:mm");	
	public static java.text.DateFormat dfTime = new java.text.SimpleDateFormat("KK:mm");
	public static java.text.DateFormat dfEN = new java.text.SimpleDateFormat("yyyyMMdd");
	public static java.text.DateFormat dfENAnsi = new java.text.SimpleDateFormat("yyyy-MM-dd");
	public static java.text.DateFormat dfWithTimeENAnsi = new java.text.SimpleDateFormat("yyyy-MM-dd KK:mm");
	public static HashMap<String, Boolean> authorized = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> authTrans = new HashMap<String, Boolean>();
	public static HashMap<Short, Boolean[]> authTab = new HashMap<Short, Boolean[]>();
	public Db db;

	public static String getEncoded(String texto, String algoritmo) {
		try {			
			MessageDigest md = MessageDigest.getInstance(algoritmo);
			md.update(texto.getBytes());
			byte[] digest = md.digest();  
			StringBuffer sb = new StringBuffer();
			for (byte b : digest)
				sb.append(String.format("%02x", b & 0xff));
			return sb.toString();
			//output = new String(codigo, "UTF-8");
			/*MessageDigest md = MessageDigest.getInstance(algoritmo);
			output = md.digest(texto.getBytes()).toString();*/
		} catch (NoSuchAlgorithmException e) {
			Errors.getError(e);
			return "";
		}
	}

	/**
	 * Verifica si el rol del usuario tiene permiso al elemento, en ENERGIA
	 * @param rolId+element
	 * @return true tiene acceso de escritura, false no tiene permiso a escritura
	 */
	public static boolean isAuthWrite(Short rolId, String element){
		element = element.replace("_S", "");
		element = element.replace("_jsp", "");
		return (authorized.containsKey(rolId + element)?authorized.get(rolId + element):false);
	}
	public static boolean isAuth(Short rolId, String element){
		element = element.replace("_S", "");
		element = element.replace("_jsp", "");
		return authorized.containsKey(rolId + element)	;
	}	
	
	public String getMonth(){
		return Integer.toString((Calendar.getInstance().get(Calendar.MONTH))+1);
	}
	public String getYear(){
		return Integer.toString((Calendar.getInstance().get(Calendar.YEAR)));
	}
	public String getDate(){
		return df.format(Calendar.getInstance().getTime());
	}
}
