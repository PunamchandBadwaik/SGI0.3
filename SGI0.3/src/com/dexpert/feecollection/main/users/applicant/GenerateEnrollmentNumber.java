package com.dexpert.feecollection.main.users.applicant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvDAO;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.affiliated.AffBean;

public class GenerateEnrollmentNumber {
	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(GenerateEnrollmentNumber.class.getName());
	FvDAO fvDAO = new FvDAO();

	
	public Long sortSetOfEnrollmentNumberAndReturnMax(List<String> enrollMentNumber) {
		ArrayList<Long> tempArray = new ArrayList<Long>();
		Iterator<String> itr = enrollMentNumber.iterator();
		while (itr.hasNext()) {
			Long intEnrollmentNumber = Long.parseLong(itr.next());
			tempArray.add(intEnrollmentNumber);
		}
		Long maxEnrollmentNumber = Collections.max(tempArray);
		return maxEnrollmentNumber;

	}

	public String getNewEnrollmentNumber(String preEnrollmentNumber, Integer feeValueId) {
		log.info("inside generate enrollment number");
		String enrollmentNumber = "";
		Session session = factory.openSession();
		String query = "select enrollmentNumber from fee_values_master inner join applicant_values on  fee_values_master.feeValueId=applicant_values.value_id  where feeValueId=:feeId and enrollmentNumber like:enrollmentNumber ";
		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setParameter("feeId", feeValueId);
		sqlQuery.setParameter("enrollmentNumber", preEnrollmentNumber + "%");
		sqlQuery.setCacheable(false);
		List<String> enrollmentNumberList = sqlQuery.list();
		session.close();

		if (enrollmentNumberList.size() < 1) {
			log.info("list size is less than");
			return preEnrollmentNumber + "001";
		} else if (enrollmentNumberList.size() >= 1) {
			log.info("list size is gretter than 1");
			log.info("Number of enrollment Number" + enrollmentNumberList);
			Long maxEnrollmentNumber = sortSetOfEnrollmentNumberAndReturnMax(enrollmentNumberList);
			log.info("Max enrollmentNumber" + maxEnrollmentNumber);
			Integer count = maxEnrollmentNumber.toString().length() == 10 ? Integer.parseInt(maxEnrollmentNumber
					.toString().substring(7)) : Integer.parseInt(maxEnrollmentNumber.toString().substring(8));
			log.info("value before increment" + count);
			log.info("value after cutting"+count);
			String nextValue = getEnrollNum(count);
			log.info("new count" + nextValue);
			return preEnrollmentNumber + nextValue;
		}

		return null;

	}

	public String getEnrollNum(Integer count) {
		if (count.equals(0)) {
			String newValue = "001";
			return newValue;

		} else {
			try {
				Integer incVal = count + 1;

				String newValue = String.valueOf(incVal);
				String increment = newValue.length() == 1 ? "00" + newValue : newValue.length() == 2 ? "0" + newValue
						: newValue.length() == 3 ? newValue : newValue;
				return increment;
			} catch (java.lang.NullPointerException e) {
				String newValue = "001";
				return newValue;
			}
		}

	}

	public String generateEnrollmentNum(String admissionYear, String course) {
		log.info("inside generate enrollment Number method");
		log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		log.info("1");
		AffBean affBean = lgBean.getAffBean();
		String collegeId = affBean.getInstId().toString().length() == 1 ? "0" + affBean.getInstId().toString()
				: affBean.getInstId().toString();
		String universityId = affBean.getParBeanAff().getParInstId().toString().length() == 1 ? "0"
				+ affBean.getParBeanAff().getParInstId().toString() : affBean.getParBeanAff().getParInstId().toString();
		Integer feevalueId = fvDAO.getCourseId(course);
		String feeValueIdStr = feevalueId.toString().length() == 1 ? "0" + feevalueId.toString() : feevalueId
				.toString();
		String twoDigitAdmissionYear = admissionYear.substring(2, 4);

		String enrollmentNumber = getNewEnrollmentNumber(universityId + collegeId + twoDigitAdmissionYear
				+ feeValueIdStr, feevalueId);

		return enrollmentNumber;
	}

}
