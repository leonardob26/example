package com.devone.model;

import org.hibernate.Session;

import com.devone.jpa.Company;

public class CompanyModel {
	void save(){
		Company cp = new Company();
		cp.setName("Borrar");
		
		Session session = HbFactory.sessionFactory.openSession();
		session.save(cp);
		session.close();
	}
	void update(){
		Company cp = new Company();
		cp.setId(3);
		cp.setName("Borrar1");
		
		Session session = HbFactory.sessionFactory.openSession();
		session.beginTransaction();
		session.update(cp);
		session.getTransaction().commit();
		session.close();
	}
	void get(){
		Session session = HbFactory.sessionFactory.openSession();
		Company cp = session.get(Company.class, 3);
		System.out.println(cp.getName());
		session.close();
	}
	void delete(){
		Company cp = new Company();
		cp.setId(3);
		
		Session session = HbFactory.sessionFactory.openSession();
		session.beginTransaction();
		session.delete(cp);
		session.getTransaction().commit();
		session.close();
	}

}
