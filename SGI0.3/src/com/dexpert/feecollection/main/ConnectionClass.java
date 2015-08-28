package com.dexpert.feecollection.main;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class ConnectionClass {
	public static SessionFactory sessionFactoryfactory = buildSessionFactory();

	public static SessionFactory buildSessionFactory() {
		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration
				.getProperties());
		SessionFactory factory = configuration.buildSessionFactory(builder.build());
		return factory;

	}

	public static SessionFactory getFactory() {
		
		return sessionFactoryfactory;
	}

	public static void shutDown() {

		getFactory().close();
	}

}
