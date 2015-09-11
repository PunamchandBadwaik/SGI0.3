package com.dexpert.feecollection.main.users.applicant;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import com.dexpert.feecollection.main.ConnectionClass;

public class GenerateEnrollmentNumber {
	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(GenerateEnrollmentNumber.class.getName());

	public Integer getCountOfYear(String AdmiYear, String yc, String course) {
		Session session = factory.openSession();
		List<AppBean> list = new ArrayList<AppBean>();
		try {
			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.add(Restrictions.eq("year", AdmiYear));
			criteria.add(Restrictions.eq("yearCode", yc));
			criteria.add(Restrictions.eq("course", course));
			list = criteria.list();
			return list.size();

		} finally {
			session.close();
		}

	}

	public String getEnrollNum(Integer count) {
		if (count.equals(0)) {
			String newValue = "00001";
			return newValue;

		} else {
			try {
				Integer incVal = count + 1;

				String newValue = String.valueOf(incVal);
				String increment = newValue.length() == 1 ? "0000" + newValue : newValue.length() == 2 ? "000"
						+ newValue : newValue.length() == 3 ? "00" + newValue : newValue.length() == 4 ? "0" + newValue
						: newValue.length() == 5 ? newValue : newValue;
				return increment;
			} catch (java.lang.NullPointerException e) {
				String newValue = "00001";
				return newValue;
			}
		}

	}

	public String getEnrollNumForPharma(Integer count) {
		if (count.equals(0)) {
			String newValue = "0001";
			return newValue;

		} else {
			try {
				Integer incVal = count + 1;

				String newValue = String.valueOf(incVal);
				String increment = newValue.length() == 1 ? "000" + newValue : newValue.length() == 2 ? "00" + newValue
						: newValue.length() == 3 ? "0" + newValue : newValue.length() == 4 ? newValue : newValue;
				return increment;
			} catch (java.lang.NullPointerException e) {
				String newValue = "0001";
				return newValue;
			}
		}

	}

	public String generateEnrollmentNumber(String yr, String yc, String course) {
		String initialString = yr.substring(2, 4).concat(yc);
		String en = null;
		String finalEnroll = null;

		if (course.equals("FE") || course.equals("SE") || course.equals("SED") || course.equals("TE")
				|| course.equals("BE") || course.equals("BE") || course.equals("MBA") || course.equals("ME")) {
			try {
				Integer count = getCountOfYear(yr, yc, course);
				en = getEnrollNum(count);
				finalEnroll = initialString.concat(en);
			} catch (java.lang.NullPointerException e) {
				finalEnroll = initialString.concat("00001");
			}

		} else if (course.equals("BPhFY") || course.equals("BPhSY") || course.equals("BPhSYD")
				|| course.equals("BPhTY") || course.equals("BPhFnY") || course.equals("MPhFY")
				|| course.equals("MPhFnY")) {

			try {
				Integer count = getCountOfYear(yr, yc, course);
				en = getEnrollNumForPharma(count);
				finalEnroll = initialString.concat(en);
			} catch (java.lang.NullPointerException e) {
				finalEnroll = initialString.concat("0001");
			}

		}
		return finalEnroll;
	}

}
