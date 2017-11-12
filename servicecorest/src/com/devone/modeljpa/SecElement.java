package com.devone.modeljpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sec_element database table.
 * 
 */
@Entity
@Table(name="sec_element")
@NamedQuery(name="SecElement.findAll", query="SELECT s FROM SecElement s")
public class SecElement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String element;

	private String description;

	private String father;

	@Column(name="is_menu")
	private byte isMenu;

	@Column(name="no_order")
	private byte noOrder;

	private String page;

	private String target;

	public SecElement() {
	}

	public String getElement() {
		return this.element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFather() {
		return this.father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public byte getIsMenu() {
		return this.isMenu;
	}

	public void setIsMenu(byte isMenu) {
		this.isMenu = isMenu;
	}

	public byte getNoOrder() {
		return this.noOrder;
	}

	public void setNoOrder(byte noOrder) {
		this.noOrder = noOrder;
	}

	public String getPage() {
		return this.page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}