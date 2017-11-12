package com.devone.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.devone.modeljpa.Company;
import com.devone.modeljpa.Service;
import com.devone.modeljpa.Status;
import com.devone.modeljpa.TypeService;
import com.devone.tpl.Db;
import com.devone.tpl.Emf;
import com.devone.tpl.logger.Errors;

public class Services {
	private int id;
    private String description;
    
    /*@Min(value = 18, message = "Age should not be less than 18")
    @Max(value = 150, message = "Age should not be greater than 150")*/
    private float price;
    
    private float quantity;   
    private short miles;
    
    private Date dateService;
    private Date date;
    private int status = 1;
    private int typeService = 0;
    
    private int company = 0;
    private int user;
    private int currentOffSet;
    private int maxOffSet;
    private String[] clsStrings = {"", "", "", "", "", "", ""}; // 0 Previous, 6 Next
    private Db db;
    public java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm");
    /**
     * 
     * @param UserId
     * @param onlyPaid
     * @param page Page click(Previous, 1,2,3,4,5, Next)
     * @param currentOffSet
     * @return
     */
    public String getServiceList(int UserId, boolean onlyPaid, String page) {
    	final byte elementPerPage=10;
		StringBuilder sb = new StringBuilder();
		
		int companyId = db.getDataValueInt("sec_user", "company_id", "id=" + UserId);
		String whereSql = (onlyPaid?"se.status_id=5":"se.status_id<>5") + (companyId==-1?"":" and company_id = " + companyId);
		
		currentOffSet = handOffSet(page, currentOffSet, elementPerPage, whereSql);
		
		String sql = "SELECT se.id, description, price, date, date_service, miles, ts.name typeServiceName, "+
		"st.name statusName, co.name companyName " +
		"FROM service se join status st on status_id=st.id " +
		"join type_service ts on se.type_service_id = ts.id " +
		"join company co on se.company_id=co.id where " + whereSql + 
		" limit " + currentOffSet*elementPerPage + "," + elementPerPage;
		
		try {
			ResultSet rs = db.getDataSql(sql);
			while (rs.next()){
				sb.append("<tr><td>" + "<a href=selService.do?id=" + 
						rs.getInt("id") + ">" + rs.getString("description") + "</a> </td>");
				sb.append("<td>" + df.format(rs.getTimestamp("date_service")) + "</td>");
				sb.append("<td>" + rs.getString("companyName") + "</td>");
				sb.append("<td>" + rs.getString("price") + "</td>");
				sb.append("<td>" + rs.getString("typeServiceName")  + "</td>");
				sb.append("<td>" + rs.getString("StatusName") + "</td>");
				sb.append("</tr>");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return sb.toString();
	}

	private int handOffSet(String page, int currentOffSet, final byte elementPerPage, String whereSql) {
		maxOffSet = db.getDataValueInt("SELECT count(*) FROM service se where " + whereSql)/elementPerPage;		
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
		if (currentOffSet==maxOffSet)
			clsStrings[6] = "class='disabled'";
		if (currentOffSet==0){
			clsStrings[1] = "class='disabled'";
			clsStrings[0] = "class='disabled'";
		}
		if (currentOffSet<5)
			clsStrings[currentOffSet+1]="class='active ' ";
		
		
		return currentOffSet;
		
	}

    public String saveService() {
    	EntityManager em = Emf.emf.createEntityManager();
		EntityTransaction entr = em.getTransaction();
		entr.begin();
		try {
			Service serv;
			if (id==0){
				serv = new Service();
				serv.setDate(new java.sql.Timestamp(new Date().getTime()));
			}
			else 
				serv = em.find(Service.class, id);
			
			serv.setCompanyId(company);
			serv.setDateService(new java.sql.Timestamp(dateService.getTime()));
			serv.setDescription(description);
			serv.setMiles(miles);
			serv.setPrice(price);
			serv.setPrice(price);
			serv.setQuantity(quantity);
			
			Status st = new Status();
			st.setId((byte) status);
			serv.setStatus(st);
			
			TypeService ts = new TypeService();
			ts.setId(typeService);
			serv.setTypeService(ts);	
			
			serv.setUserId(user);
			
			em.persist(serv);
			entr.commit();
			return String.valueOf(serv.getId()); 
		} catch (Exception e) {
			entr.rollback();
			return Errors.getError(e);
		} finally {
			em.close();  
		}
	}
    public String delService() {
    	EntityManager em = Emf.emf.createEntityManager();
		try {
			Service serv = em.find(Service.class, id);
		    em.getTransaction().begin();
			em.remove(serv);
			em.getTransaction().commit();
			return "OK"; 
		} catch (Exception e) {
			return Errors.getError(e);
		} finally {
			em.close();  
		}
	}
    public void getService(){
    	EntityManager em = Emf.emf.createEntityManager();
		try {
			Service serv = em.find(Service.class, id);
			this.company = serv.getCompanyId();
			this.date = serv.getDate();
			
			this.dateService = serv.getDateService();
			this.description = serv.getDescription();
			this.miles = serv.getMiles();
			this.price = serv.getPrice();
			this.quantity = serv.getQuantity();
			Status sta = serv.getStatus();
			this.status = sta.getId();
			TypeService tps = serv.getTypeService();
			this.typeService = tps.getId();
			this.user = serv.getUserId();
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public int getTypeService() {
		return typeService;
	}
	public void setTypeService(int typeService) {
		this.typeService = typeService;
	}
	public short getMiles() {
		return miles;
	}
	public void setMiles(short miles) {
		this.miles = miles;
	}
	public String getDateService() {
		return df.format(dateService);
	}
	public void setDateService(Date dateService) {
		this.dateService = dateService;
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
}
