package com.devone.jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sec_authorized database table.
 * 
 */
@Entity
@Table(name="sec_authorized")
@NamedQuery(name="SecAuthorized.findAll", query="SELECT s FROM SecAuthorized s")
public class SecAuthorized implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="is_write")
	private byte isWrite;

	//uni-directional many-to-one association to SecElement
	@ManyToOne
	@JoinColumn(name="element")
	private SecElement secElement;

	//uni-directional many-to-one association to SecRol
	@ManyToOne
	@JoinColumn(name="rol_id")
	private SecRol secRol;

	public SecAuthorized() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getIsWrite() {
		return this.isWrite;
	}

	public void setIsWrite(byte isWrite) {
		this.isWrite = isWrite;
	}

	public SecElement getSecElement() {
		return this.secElement;
	}

	public void setSecElement(SecElement secElement) {
		this.secElement = secElement;
	}

	public SecRol getSecRol() {
		return this.secRol;
	}

	public void setSecRol(SecRol secRol) {
		this.secRol = secRol;
	}

}