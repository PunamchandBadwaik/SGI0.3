package com.dexpert.feecollection.main.communication.email;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;


public class EmailSender extends ActionSupport{
	
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpSession ses = request.getSession();
	EmailSessionBean emailbean=new EmailSessionBean();

	public String processrequest() throws Exception {
		String to = request.getParameter("to");
		String subject = request.getParameter("subject");
		String body = request.getParameter("body");
		Logger log=Logger.getLogger("email service log");
		log.info("to "+to+" sub: "+subject+" body: "+body );
		javax.mail.internet.InternetAddress ia = new javax.mail.internet.InternetAddress(to);
		try {
			ia.validate();
			
		} catch (javax.mail.internet.AddressException ae) {

		}
		return "success";
	}

}
