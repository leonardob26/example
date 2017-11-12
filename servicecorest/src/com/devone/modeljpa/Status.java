package com.devone.modeljpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the status database table.
 * 
 */
@Entity
@Table(name="status")
@NamedQuery(name="Status.findAll", query="SELECT s FROM Status s")
public class Status implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private byte id;

	private byte isOrder;

	private byte isService;

	private String name;

	public Status() {
	}

	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public byte getIsOrder() {
		return this.isOrder;
	}

	public void setIsOrder(byte isOrder) {
		this.isOrder = isOrder;
	}

	public byte getIsService() {
		return this.isService;
	}

	public void setIsService(byte isService) {
		this.isService = isService;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}