package com.devone.jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the param_admin database table.
 * 
 */
@Entity
@Table(name="param_admin")
@NamedQuery(name="ParamAdmin.findAll", query="SELECT p FROM ParamAdmin p")
public class ParamAdmin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private byte id;

	@Column(name="base_dn")
	private String baseDn;

	@Column(name="home_birt")
	private String homeBirt;

	@Column(name="ldap_host")
	private String ldapHost;

	@Column(name="origen_authentication")
	private String origenAuthentication;

	@Column(name="security_authentication")
	private String securityAuthentication;

	@Column(name="smtp_host")
	private String smtpHost;

	public ParamAdmin() {
	}

	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getBaseDn() {
		return this.baseDn;
	}

	public void setBaseDn(String baseDn) {
		this.baseDn = baseDn;
	}

	public String getHomeBirt() {
		return this.homeBirt;
	}

	public void setHomeBirt(String homeBirt) {
		this.homeBirt = homeBirt;
	}

	public String getLdapHost() {
		return this.ldapHost;
	}

	public void setLdapHost(String ldapHost) {
		this.ldapHost = ldapHost;
	}

	public String getOrigenAuthentication() {
		return this.origenAuthentication;
	}

	public void setOrigenAuthentication(String origenAuthentication) {
		this.origenAuthentication = origenAuthentication;
	}

	public String getSecurityAuthentication() {
		return this.securityAuthentication;
	}

	public void setSecurityAuthentication(String securityAuthentication) {
		this.securityAuthentication = securityAuthentication;
	}

	public String getSmtpHost() {
		return this.smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

}