package com.dexpert.calendaryear;

import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import COM.rsa.Intel.cr;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.fee.config.FcDAO;

public class CalendarDAO {
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(FcDAO.class.getName());

	// method to persist calendar Bean
	public void saveCalendarYear(CalendarBean calendarBean) {
		Session session = factory.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			session.save(calendarBean);
			transaction.commit();

		} finally {
			session.close();
		}

	}

	public List<CalendarBean> getCalendarInformation(Integer instId, String key, String value) {
		Session session = factory.openSession();
		try {
			Criteria criteria = session.createCriteria(CalendarBean.class);
			if (instId != null) {
				criteria.add(Restrictions.eq("instId", instId));
			}
			if (key != null && key.contentEquals("ALL")) {

			}
			if (key != null && key.contentEquals("PaymentCycle")) {
				criteria.add(Restrictions.eq("numberOfPaymentCycle", value));
			}
			List<CalendarBean> beans = criteria.list();
			return beans;
		} finally {
			session.close();
		}

	}
    //method to save payment cycle bean
	public void savePaymentCycleBeans(ArrayList<PaymentCycleBean> paymentCycleBeans) {
		Session session = factory.openSession();
		try {
			Iterator<PaymentCycleBean> itr = paymentCycleBeans.iterator();
			while (itr.hasNext()) {
				PaymentCycleBean paymentCycleBean = itr.next();
				if (paymentCycleBean != null) {
					Transaction tx = session.beginTransaction();
					session.save(paymentCycleBean);
					tx.commit();
				}

			}
		} finally {
			session.close();

		}

	}

}
