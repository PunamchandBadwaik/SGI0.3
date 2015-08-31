package com.dexpert.feecollection.main.users.applicant;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.users.affiliated.AffBean;

public class GenerateEnrollmentNumber {
	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(GenerateEnrollmentNumber.class.getName());

	public String getLastEnrolNum() {
		Session session = factory.openSession();
		try {
			Criteria c = session.createCriteria(AppBean.class);
			c.addOrder(Order.desc("enrollmentNumber"));
			c.setMaxResults(1);
			AppBean temp = (AppBean) c.uniqueResult();

			String id = temp.getEnrollmentNumber();

			return id;
		} finally {
			session.close();
		}

	}

	public String getEnrollNum(String aa) {

		try {

			log.info("Existing Enrooll number is ::" + aa);
			String newValue = String.valueOf(Integer.parseInt(aa) + 1);
			String increment = newValue.length() == 1 ? "0000" + newValue : newValue.length() == 2 ? "0000" + newValue
					: newValue.length() == 3 ? "000" + newValue : newValue.length() == 4 ? "00" + newValue : newValue
							.length() == 5 ? "0" + newValue : newValue.length() == 6 ? newValue : newValue;
			log.info("incremented value of enrooll is ::" + increment);
			return increment;
		} catch (java.lang.NullPointerException e) {
			String newValue = "00001";
			log.info("initial Value is ::" + newValue);
			return newValue;
		}

	}

	public String generateEnrollmentNumber(String yr,String yc) {
		
		log.info("Year ::"+yr);
		log.info("Year Code  ::"+yc);
		String initialString = yr.substring(2).concat(yc);
		String enroll;
		String en = null;
		String finalEnroll;
		try {
			enroll = getLastEnrolNum();
			en = getEnrollNum(enroll.substring(4));
			finalEnroll = initialString.concat(en);
		} catch (java.lang.NullPointerException e) {
			finalEnroll = initialString.concat("00001");
		}

		log.info("Final Enrollment number is ::" + finalEnroll);
		return finalEnroll;
	}

	/*
	 * public String getStudentRowCount() { Session session =
	 * 
	 * factory.openSession(); try { Criteria c =
	 * session.createCriteria(AppBean.class);
	 * c.addOrder(Order.desc("enrollmentNumber")); c.setMaxResults(1); AppBean
	 * temp = (AppBean) c.uniqueResult(); String id =
	 * temp.getEnrollmentNumber(); Integer lngth = id.length();
	 * System.out.println("Id is ::" + id); System.out.println("Length ::" +
	 * lngth); if (lngth.equals(1)) {
	 * 
	 * id = "0000".concat(temp.getEnrollmentNumber()); return id; } else if
	 * (lngth.equals(2)) { id = "000".concat(temp.getEnrollmentNumber()); return
	 * id; } else if (lngth.equals(3)) { id =
	 * "00".concat(temp.getEnrollmentNumber()); return id; } else if
	 * (lngth.equals(4)) { id = "0".concat(temp.getEnrollmentNumber()); return
	 * id; } else { return id; } } finally { // close session session.close(); }
	 * } }
	 */

}
