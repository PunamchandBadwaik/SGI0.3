package com.dexpert.feecollection.main.fee;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.main.users.applicant.AppBean;
import com.dexpert.feecollection.main.users.applicant.AppDAO;
import com.opensymphony.xwork2.ActionSupport;

public class CartActions extends ActionSupport {

	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	HttpSession httpSession = request.getSession();
	CartDataBean cartdata;
	AppDAO appDAO = new AppDAO();
	ArrayList<CartDataBean> cartList = new ArrayList<CartDataBean>();
	Double cartTotal=(double)0;

	public String AddToCart() {
		AppBean studentDetails = new AppBean();
		Double fee = (double) 0;
		String dueString = "";
		try {
			String appId = request.getParameter("enrollmentId").trim();// Get
																		// Enrollment
																		// Id of
																		// Student
			studentDetails = appDAO.getUserDetail(appId);
			fee = Double.parseDouble(request.getParameter("totalPaidAmount"));
			dueString = request.getParameter("dueString").trim();
			Integer cartInit = (Integer) httpSession.getAttribute("cart_init");

			// Populate Cart Object
			cartdata=new CartDataBean();
			cartdata.setAmount(fee);
			cartdata.setAppId(appId);
			cartdata.setDueString(dueString);
			cartdata.setName(studentDetails.getAplFirstName() + " " + studentDetails.getAplLstName());
			if (cartInit == 0) {
				httpSession.setAttribute("cart_init", 1);
				cartdata.setId(1);
				cartList.add(cartdata);

			} else if (cartInit == 1) {
				cartList = (ArrayList<CartDataBean>) httpSession.getAttribute("cart_list");
				cartList.add(cartdata);
				for (int i = 0; i < cartList.size(); i++) {
					cartList.get(i).setId(i);
				}
			} else {
				return ERROR;
			}
			httpSession.setAttribute("cart_list", cartList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	public String ShowCart()
	{
		cartTotal=(double)0;
		try
		{
			Integer cartInit = (Integer) httpSession.getAttribute("cart_init");
			if(cartInit==1)
				
			{
				cartList=(ArrayList<CartDataBean>) httpSession.getAttribute("cart_list");
				for (int i = 0; i < cartList.size(); i++) {
					cartTotal=cartTotal+cartList.get(i).getAmount();
				}
				
			}
			return SUCCESS;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return SUCCESS;
		}
	}
	
	public String RemoveFromCart()
	{
		Integer cartid=0;
		try
		{
			cartid=Integer.parseInt(request.getParameter("cart_rem_id").trim());
			Integer cartInit = (Integer) httpSession.getAttribute("cart_init");
			if(cartInit==1)
				
			{
				cartList=(ArrayList<CartDataBean>) httpSession.getAttribute("cart_list");
				for (int i = 0; i < cartList.size(); i++) {
					if(cartList.get(i).getId()==cartid)
					{
						cartList.remove(i);
							break;
					}
				}
				for (int i = 0; i < cartList.size(); i++) {
					cartList.get(i).setId(i);
				}
				httpSession.setAttribute("cart_list", cartList);
			}
			return SUCCESS;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return SUCCESS;
		}
	}

	public CartDataBean getCartdata() {
		return cartdata;
	}

	public void setCartdata(CartDataBean cartdata) {
		this.cartdata = cartdata;
	}
	public ArrayList<CartDataBean> getCartList() {
		return cartList;
	}
	public Double getCartTotal() {
		return cartTotal;
	}
	public void setCartTotal(Double cartTotal) {
		this.cartTotal = cartTotal;
	}
	
	
}
