package com.devone.modeljpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sec_rol database table.
 * 
 */
@Entity
@Table(name="sec_rol")
@NamedQuery(name="SecRol.findAll", query="SELECT s FROM SecRol s")
public class SecRol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private byte id;

	private String nombre;

	public SecRol() {
	}

	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}