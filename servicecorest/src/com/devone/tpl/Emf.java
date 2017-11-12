package com.devone.tpl;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Emf {
	public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("serviceco");
	public static void closeEmf() {
		/*emf.close();
		emf=null;*/
	}
}
