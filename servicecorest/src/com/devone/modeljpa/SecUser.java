package com.devone.modeljpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sec_user database table.
 * 
 */
@Entity
@Table(name="sec_user")
@NamedQuery(name="SecUser.findAll", query="SELECT s FROM SecUser s")
public class SecUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String address;

	private String email;

	private String email1;

	private String nombres;

	private String password;

	private String phone;

	private String phone1;

	private String username;

	//uni-directional many-to-one association to SecRol
	@ManyToOne
	@JoinColumn(name="rol_id")
	private SecRol secRol;

	//uni-directional many-to-one association to Company
	@ManyToOne
	private Company company;

	public SecUser() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail1() {
		return this.email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone1() {
		return this.phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public SecRol getSecRol() {
		return this.secRol;
	}

	public void setSecRol(SecRol secRol) {
		this.secRol = secRol;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}