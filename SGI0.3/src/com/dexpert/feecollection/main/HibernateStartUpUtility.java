package com.dexpert.feecollection.main;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.hibernate.SessionFactory;

public class HibernateStartUpUtility extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) {

		SessionFactory factory = ConnectionClass.getFactory();

	}

}
