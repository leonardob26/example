package com.devone.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the orders database table.
 * 
 */
@Entity
@Table(name="orders")
@NamedQuery(name="Order.findAll", query="SELECT o FROM Order o")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private Timestamp date;

	@Temporal(TemporalType.DATE)
	@Column(name="date_delivery")
	private Date dateDelivery;

	@Temporal(TemporalType.DATE)
	@Column(name="date_get_paid")
	private Date dateGetPaid;

	@Column(name="date_order")
	private Timestamp dateOrder;

	private float deep;

	private float height;

	private float price;

	private short quantity;

	private float width;

	//uni-directional many-to-one association to Company
	@ManyToOne
	private Company company;

	//uni-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name="products_id")
	private Product product;

	//uni-directional many-to-one association to Status
	@ManyToOne
	private Status status;

	//uni-directional many-to-one association to SecUser
	@ManyToOne
	@JoinColumn(name="user_id")
	private SecUser secUser;

	public Order() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Date getDateDelivery() {
		return this.dateDelivery;
	}

	public void setDateDelivery(Date dateDelivery) {
		this.dateDelivery = dateDelivery;
	}

	public Date getDateGetPaid() {
		return this.dateGetPaid;
	}

	public void setDateGetPaid(Date dateGetPaid) {
		this.dateGetPaid = dateGetPaid;
	}

	public Timestamp getDateOrder() {
		return this.dateOrder;
	}

	public void setDateOrder(Timestamp dateOrder) {
		this.dateOrder = dateOrder;
	}

	public float getDeep() {
		return this.deep;
	}

	public void setDeep(float deep) {
		this.deep = deep;
	}

	public float getHeight() {
		return this.height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public short getQuantity() {
		return this.quantity;
	}

	public void setQuantity(short quantity) {
		this.quantity = quantity;
	}

	public float getWidth() {
		return this.width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public SecUser getSecUser() {
		return this.secUser;
	}

	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}

}