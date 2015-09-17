package com.dexpert.feecollection.main.users.applicant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.users.LoginBean;

public class GenerateEnrollmentNumber {
	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(GenerateEnrollmentNumber.class.getName());

	public static void main(String[] args) {
		GenerateEnrollmentNumber gg = new GenerateEnrollmentNumber();
		// gg.generateEnrollmentNumber("2011", "1", "B.Ph.FY");
		Integer count = gg.getCountOfYear("2011", "1", "B.Ph.FY");

		String initialString = "111";
		String en = gg.getEnrollNumForPharma(count);
		String finalEnroll = initialString.concat(en);
		System.out.println("Enrollment number is ::: " + finalEnroll);

	}

	public Integer getCountOfYear(String AdmiYear, String yc, String course) {

		log.info("Admission Year :" + AdmiYear);
		log.info("Course :" + course);
		log.info("Year :" + yc);

		Session session = factory.openSession();
		List<AppBean> list = new ArrayList<AppBean>();
		try {
			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.add(Restrictions.eq("year", AdmiYear));
			criteria.add(Restrictions.eq("yearCode", yc));
			criteria.add(Restrictions.eq("course", course));
			list = criteria.list();
			log.info("List Size is :: " +list.size());
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

			log.info("Counter is ::: " + count);
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
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");

		String collegeId = lgBean.getAffBean().getInstId().toString();
		String adYear = yr.substring(2, 4);

		String initialString = adYear+collegeId+yc;
		String en = null;
		String finalEnroll = null;

		if (course.equals("FE") || course.equals("SE") || course.equals("SED") || course.equals("TE")
				|| course.equals("BE") || course.equals("BE") || course.equals("MBA") || course.equals("ME")) {
			try {
				Integer count = getCountOfYear(adYear, yc, course);
				en = getEnrollNum(count);
				finalEnroll = initialString.concat(en);
			} catch (java.lang.NullPointerException e) {
				finalEnroll = initialString.concat("00001");
			}

		} else if (course.equals("B.Ph.FY") || course.equals("B.Ph.SY") || course.equals("B.Ph.SY(Direct)")
				|| course.equals("B.Ph.TY") || course.equals("B.Ph.Final") || course.equals("M.Ph.FY")
				|| course.equals("M.Ph.Final")) {

			try {
				Integer count = getCountOfYear(adYear, yc, course);
				en = getEnrollNumForPharma(count);
				finalEnroll = initialString.concat(en);
			} catch (java.lang.NullPointerException e) {
				finalEnroll = initialString.concat("0001");
			}

		}
		return finalEnroll;
	}

}
