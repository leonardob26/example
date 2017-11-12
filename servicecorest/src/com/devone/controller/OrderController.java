package com.devone.controller;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.devone.model.Order;
import com.devone.model.struc.DataWithOffSet;
import com.devone.model.struc.OrderDataForm;
import com.devone.modeljpa.Orders;
import com.devone.tpl.Emf;
import com.devone.tpl.logger.Errors;

@Path("/Order")
public class OrderController {
	@GET
	@Path("/orders/{onlyPaid}/{page}")
	@Produces(MediaType.APPLICATION_JSON)
	public DataWithOffSet getUsers(@Context HttpServletRequest request, 
			@PathParam("onlyPaid") boolean onlyPaid, @PathParam("page") String page){
		HttpSession se = request.getSession();
		Order or = new Order();
		try {
			int userId = 2;//(Integer) se.getAttribute("userId");
			or.setCurrentOffSet((se.getAttribute("offSetorder")== null)?0:(Integer)se.getAttribute("offSetorder"));
			or.openDb();
			DataWithOffSet dataWithOffSet = new DataWithOffSet();
			dataWithOffSet.setList(or.getOrderList(userId, onlyPaid, page));
			dataWithOffSet.setOffset(or.getCurrentOffSet());
			return dataWithOffSet;
		} catch (Exception e) {
			Errors.getError(e);
			return null;
		} finally {
			or.closeDb();
		}
	}	
	@GET
	@Path("/order/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public OrderDataForm showForm(@Context HttpServletRequest request, @PathParam("id") int id) { //@PathVariable int id,
		Order order = new Order();
		order.setId(id);
		OrderDataForm dataForm = new OrderDataForm();
		if (id!=0)
			order.getOrder();
		else
			return null;
        order.openDb();
        try {
        	dataForm.setCompanyList(order.getCmb("company",""));
        	dataForm.setProductList(order.getCmb("products",""));
        	dataForm.setStatusList(order.getCmb("status", "isorder=1"));
        	dataForm.setId(order.getId());
        	dataForm.setCompany(order.getCompany());
        	dataForm.setDateDelivery(order.getDateDelivery());
        	dataForm.setDateGetPaid(order.getDateGetPaid());
        	dataForm.setDateOrder(order.getDateOrder());
        	dataForm.setDeep(order.getDeep());
        	dataForm.setHeight(order.getHeight());
        	dataForm.setId(id);
        	dataForm.setPrice(order.getPrice());
        	dataForm.setProducts(order.getProducts());
        	dataForm.setQuantity(order.getQuantity());
        	dataForm.setStatus(order.getStatus());
        	dataForm.setUser(order.getUser());
        	dataForm.setWidth(order.getWidth());
		} catch (Exception e) {
			Errors.getError(e);
		} finally {
			order.closeDb();
		}
        return dataForm;
	}
}
