package com.devone.jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the type_service database table.
 * 
 */
@Entity
@Table(name="type_service")
@NamedQuery(name="TypeService.findAll", query="SELECT t FROM TypeService t")
public class TypeService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String name;

	public TypeService() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}