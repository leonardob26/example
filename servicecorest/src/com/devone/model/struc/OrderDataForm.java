package com.devone.model.struc;

import java.util.Map;

public class OrderDataForm {
	private int id;
	private String dateOrder;
	private float width ;
	private float height ;
	private float deep ;
	private float price ;
	private short quantity=0;
	private String dateDelivery;
	private String dateGetPaid;
	private int user;
	private int products;
	private int status = 1;
	private int company = 0;
	private Map<Integer, String> productList;
	private Map<Integer, String> companyList;
	private Map<Integer, String> statusList;
	
	public Map<Integer, String> getProductList() {
		return productList;
	}
	public void setProductList(Map<Integer, String> productList) {
		this.productList = productList;
	}
	public Map<Integer, String> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(Map<Integer, String> companyList) {
		this.companyList = companyList;
	}
	public Map<Integer, String> getStatusList() {
		return statusList;
	}
	public void setStatusList(Map<Integer, String> statusList) {
		this.statusList = statusList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDateOrder() {
		return dateOrder;
	}
	public void setDateOrder(String dateOrder) {
		this.dateOrder = dateOrder;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getDeep() {
		return deep;
	}
	public void setDeep(float deep) {
		this.deep = deep;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public short getQuantity() {
		return quantity;
	}
	public void setQuantity(short quantity) {
		this.quantity = quantity;
	}
	public String getDateDelivery() {
		return dateDelivery;
	}
	public void setDateDelivery(String dateDelivery) {
		this.dateDelivery = dateDelivery;
	}
	public String getDateGetPaid() {
		return dateGetPaid;
	}
	public void setDateGetPaid(String dateGetPaid) {
		this.dateGetPaid = dateGetPaid;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public int getProducts() {
		return products;
	}
	public void setProducts(int products) {
		this.products = products;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCompany() {
		return company;
	}
	public void setCompany(int company) {
		this.company = company;
	}
}
