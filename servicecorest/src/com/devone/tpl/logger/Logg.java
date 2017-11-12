package com.devone.tpl.logger;

import java.io.IOException;
//import java.util.Date;
//import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//import javax.mail.Message;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;

public class Logg {	
	public static Logger loggerApp = Logger.getLogger("Action");
	public enum fileSource {App, Sec, Error};
	public FileHandler fh; 
	private String sourceClass, sourceMethod;
	//private String to="boet@gtm.etecsa.cu", from="pe@gtm.etecsa.cu";
	public Logg(fileSource fileDestination){			
		try {
			///var/log/pe/pe
			fh = new FileHandler("c:\\logs\\" + fileDestination + ".log" , 10485760, 10, true);
			//FileHandler fh = new FileHandler("./log/peApp" + MyUtils.dfEN.format(new Date()) + ".log" , 10485760, 10, true);
		    fh.setFormatter(new SimpleFormatter());
		    loggerApp.addHandler(fh);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	    
	}
	public void setLogApp(String sourceClass, String sourceMethod, String msg) {
	    loggerApp.logp(Level.INFO, sourceClass, sourceMethod, msg);
	    fh.close();
	}	
	/**
	 * Antes se debe haber seteado los campos sourceClass y sourceMethod
	 * @param msg
	 */
	public void setLogApp(String msg){
		setLogApp(sourceClass, sourceMethod, msg);
	}
	public void setLogError(String sourceClass, String sourceMethod, String msg) {
	    loggerApp.logp(Level.INFO, sourceClass, sourceMethod, msg);
	    fh.close();	
/*		try{
			String body=msg;
			send(ParamAdmin.smtpHost, to, from,body);
		}
		catch (Exception ex){
			System.out.println("Usage :\njava SimpleSender server to from body");
		}	*/	
	 
	}
	/**
	 * Antes se debe haber seteado los campos sourceClass y sourceMethod
	 * @param msg
	 */
	public void setLogError(String msg) {
		setLogError(sourceClass, sourceMethod, msg);	
	}
		
	public void setSourceClass(String sourceClass) {
		this.sourceClass = sourceClass;
	}
	public void setSourceMethod(String sourceMethod) {
		this.sourceMethod = sourceMethod;
	}	
	/*public static void send(String smtpServer, String to, String from, String body)
	{
		try{
			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpServer);
			Session session = Session.getDefaultInstance(props, null);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to, false));
			msg.setSubject("Test Mail thru java");
			msg.setContent(body,"text/plain");
			msg.setSentDate(new Date());
			Transport.send(msg);
			System.out.println("Message sent OK.");
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}*/
}