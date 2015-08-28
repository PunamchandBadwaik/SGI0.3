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

	public String getStudentRowCount() {
		Session session = factory.openSession();
		try {
			Criteria c = session.createCriteria(AppBean.class);
			c.addOrder(Order.desc("enrollmentNumber"));
			c.setMaxResults(1);
			AppBean temp = (AppBean) c.uniqueResult();

			String id = temp.getEnrollmentNumber();
			Integer lngth = id.length();
			System.out.println("Id is ::"+id);
			System.out.println("Length ::" + lngth);
			if (lngth.equals(1)) {

				id = "0000".concat(temp.getEnrollmentNumber());
				return id;
			} else if (lngth.equals(2)) {
				id = "000".concat(temp.getEnrollmentNumber());
				return id;
			} else if (lngth.equals(3)) {
				id = "00".concat(temp.getEnrollmentNumber());
				return id;
			} else if (lngth.equals(4)) {
				id = "0".concat(temp.getEnrollmentNumber());
				return id;
			} else {
				return id;
			}
		} finally {
			// close session
			session.close();
		}
	}

	public String createEnrollmentNumber() {

		String admissionyear = "2014";
		String instId = "201";
		String enrollmentNum = new String();

		enrollmentNum = instId.concat(admissionyear.substring(2)).concat(getStudentRowCount());

		return enrollmentNum;
	}

	public static void main(String[] args) {
		GenerateEnrollmentNumber gn = new GenerateEnrollmentNumber();
		String en = gn.createEnrollmentNumber();
		System.out.println("Enrollment Number is ::" + en);
	}

}
