package com.devone.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.devone.modeljpa.Company;
import com.devone.modeljpa.Orders;
import com.devone.modeljpa.Product;
import com.devone.modeljpa.SecUser;
import com.devone.modeljpa.Status;
import com.devone.model.struc.OrderData;
import com.devone.tpl.Db;
import com.devone.tpl.Emf;
import com.devone.tpl.logger.Errors;

public class Order {
	private int id;
	private Date dateOrder;
	private float width ;
	private float height ;
	private float deep ;
	private float price ;
	private short quantity=0;
	private Date dateDelivery;
	private Date dateGetPaid;
	private int user;
	private int products;
	private int status = 1;
	private int company = 0;
    
    private int currentOffSet;
    private int maxOffSet;
    private String[] clsStrings = {"", "", "", "", "", "", ""}; // 0 Previous, 6 Next
    private Db db;
    private java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm");
    
    /**
     * 
     * @param UserId
     * @param onlyPaid
     * @param page Page click(Previous, 1,2,3,4,5, Next)
     * @param currentOffSet
     * @return
     */
    public List<OrderData> getOrderList(int UserId, boolean onlyPaid, String page) {
    	final byte elementPerPage=10;
		new StringBuilder();
		int companyId = db.getDataValueInt("sec_user", "company_id", "id=" + UserId);
		String whereSql = (onlyPaid?"o.status_id=5":"o.status_id<>5") + (companyId==-1?"":" and company_id = " + companyId);
		
		currentOffSet = handOffSet(page, currentOffSet, elementPerPage, whereSql);
		
		String sql = "SELECT o.id, c.name nameCompany, p.name nameProduct, date_order, width, height, deep, "
				+ "price, quantity, date_delivery, s.name statusName " +
				"FROM orders o join products p on o.products_id = p.id join company c on o.company_id=c.id " +
				"join status s on o.status_id=s.id where " + whereSql + 
		" limit " + currentOffSet*elementPerPage + "," + elementPerPage;
		List<OrderData> list = new ArrayList<OrderData>();
		try {
			ResultSet rs = db.getDataSql(sql);
			
			while (rs.next()){
				
				OrderData or = new OrderData();
				or.setId(rs.getInt("id"));
				or.setOrderDate(df.format(rs.getTimestamp("date_order")));
				or.setNameCompany(rs.getString("nameCompany"));
				or.setNameProduct(rs.getString("nameProduct"));
				or.setPriceAndQuantity(rs.getString("price") + " * " +  rs.getString("quantity"));
				or.setSize(rs.getString("width") + " x " + rs.getString("height") + " x " + rs.getString("deep"));
				or.setStatusName(rs.getString("statusName"));
				list.add(or);
				/*sb.append("<tr><td>" + "<a href=selOrder.do?id=" + 
						rs.getInt("id") + ">" + rs.getString("nameProduct") + "</a> </td>");
				sb.append("<td>" + df.format(rs.getTimestamp("date_order")) + "</td>");
				sb.append("<td>" + rs.getString("nameCompany") + "</td>");
				sb.append("<td>" + rs.getString("price") + " * " +  rs.getString("quantity") + "</td>");
				sb.append("<td>" + rs.getString("width") + " x " + rs.getString("height") + " x " + rs.getString("deep") + "</td>");
				sb.append("<td>" + rs.getString("statusName") + "</td>");
				sb.append("</tr>");*/
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return list;
	}

    
    public String saveOrder() {
    	EntityManager em = Emf.emf.createEntityManager();
		EntityTransaction entr = em.getTransaction();
		entr.begin();
		try {
			Orders or;
			if (id==0)
				or = new Orders();
			else 
				or = em.find(Orders.class, id);
			or.setDateOrder(new java.sql.Timestamp(dateOrder.getTime()));
			if (dateDelivery!=null)			//serv.setDateService(new java.sql.Timestamp(dateService.getTime()));
				or.setDateDelivery(new java.sql.Timestamp(dateDelivery.getTime()));
			if (dateGetPaid!=null)
				or.setDateGetPaid(new java.sql.Timestamp(dateGetPaid.getTime()));
			
			or.setPrice(price);
			or.setQuantity(quantity);
			Company compy = new Company();
			compy.setId(company);
			
			or.setCompany(compy);
			
			Status st = new Status();
			st.setId((byte) status);
			or.setStatus(st);
			
			SecUser su = new SecUser();
			su.setId(user);		
			or.setSecUser(su);
			
			Product prod = new Product();
			prod.setId(products);
			or.setProduct(prod);
			
			or.setWidth(width);
			or.setHeight(height);
			or.setDeep(deep);
			
			em.persist(or);
			entr.commit();
			return String.valueOf(or.getId()); 
		} catch (Exception e) {
			entr.rollback();
			return Errors.getError(e);
		} finally {
			em.close();  
		}
	}
    public String delOrder() {
    	EntityManager em = Emf.emf.createEntityManager();
		try {
			Orders ord = em.find(Orders.class, id);
		    em.getTransaction().begin();
			em.remove(ord);
			em.getTransaction().commit();
			return "OK"; 
		} catch (Exception e) {
			return Errors.getError(e);
		} finally {
			em.close();  
		}
	}
    public void getOrder(){
    	EntityManager em = Emf.emf.createEntityManager();
		try {
			Orders ord = em.find(Orders.class, id);
			Company comp = ord.getCompany();
			this.company = comp.getId();
			
			dateDelivery = ord.getDateDelivery();
			dateGetPaid = ord.getDateGetPaid();
			dateOrder = ord.getDateOrder();
			deep = ord.getDeep();
			height = ord.getHeight();
			price = ord.getPrice();
			
			Product prod = ord.getProduct();
			products = prod.getId();
			quantity = ord.getQuantity();
			
			Status sta = ord.getStatus();
			status = sta.getId();
			width = ord.getWidth();
			
		} catch (Exception e) {
			Errors.getError(e);
		} finally {
			em.close();  
		}
    }
    public Map<Integer, String> getCmb(String table, String condition) throws Exception{
    	ResultSet rs = db.getData(table, "*", condition);
    	Map<Integer, String> list = new LinkedHashMap<Integer, String>();
    	list.put(0, "");
		while (rs.next())
			list.put(rs.getInt("id"), rs.getString("name"));
		rs.close();
		return list;
    }
    
    private int handOffSet(String page, int currentOffSet, final byte elementPerPage, String whereSql) {
		maxOffSet = db.getDataValueInt("SELECT count(*) FROM orders o where " + whereSql)/elementPerPage;		
		switch(page){
		case "previous":
			if (currentOffSet!=0)
				currentOffSet--;
			break;
		case "next":
			if (currentOffSet<maxOffSet)
				currentOffSet++;
			break;
		default:
			currentOffSet = Integer.parseInt(page)-1;
			if (currentOffSet>maxOffSet)
				currentOffSet=maxOffSet;
			
			break;
		}
		this.currentOffSet = currentOffSet;
		return currentOffSet;
	}
    public void openDb(){
    	db = new Db();
    }
    public void closeDb(){
    	db.Close();
    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public int getCompany() {
		return company;
	}
	public void setCompany(int company) {
		this.company = company;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public Db getDb() {
		return db;
	}
	public void setDb(Db db) {
		this.db = db;
	}

	public int getCurrentOffSet() {
		return currentOffSet;
	}

	public void setCurrentOffSet(int currentOffSet) {
		this.currentOffSet = currentOffSet;
	}

	public String[] getClsStrings() {
		return clsStrings;
	}

	public void setClsStrings(String[] clsStrings) {
		this.clsStrings = clsStrings;
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

	public String getDateDelivery() {
		if (dateDelivery!=null)
			return df.format(dateDelivery);
		else
			return "";
	}

	public void setDateDelivery(Date dateDelivery) {
		if (dateDelivery!=null)
			this.dateDelivery = dateDelivery;
	}

	public String getDateGetPaid() {
		if (dateGetPaid!=null)
			return df.format(dateGetPaid);
		else
			return "";
	}

	public void setDateGetPaid(Date dateGetPaid) {
		if (dateGetPaid!=null)
			this.dateGetPaid = dateGetPaid;
	}

	public int getProducts() {
		return products;
	}

	public void setProducts(int products) {
		this.products = products;
	}

	public void setQuantity(short quantity) {
		this.quantity = quantity;
	}

	public String getDateOrder() {
		if (dateOrder!=null)
			return df.format(dateOrder);
		else
			return "";
	}

	public void setDateOrder(Date dateOrder) {
		this.dateOrder = dateOrder;
	}
 
}
