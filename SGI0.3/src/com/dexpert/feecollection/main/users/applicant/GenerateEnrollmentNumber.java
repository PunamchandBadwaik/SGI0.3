package com.dexpert.feecollection.main.users.applicant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvDAO;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.affiliated.AffBean;

public class GenerateEnrollmentNumber {

	public static void main(String[] args) {
		// GenerateEnrollmentNumber gn = new GenerateEnrollmentNumber();
		// Integer feeValueID = gn.getFeeValueId();
		// System.out.println("Value IS ::" + feeValueID);
	}

	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(GenerateEnrollmentNumber.class.getName());
	FvDAO fvDAO = new FvDAO();

	public Integer getFeeValueId(Set<FvBean> fvBeanSet) {
		/*
		 * FvBean bean = new FvBean(); bean.setValue("FE");
		 * bean.setFeeValueId(3); bean.setValue("SE"); bean.setFeeValueId(4);
		 */
		// Set<FvBean> fvBeanSet = new HashSet<FvBean>();
		// fvBeanSet.add(bean);

		Session session = factory.openSession();
		Integer feeValueId = null;
		try {
			
			Iterator<FvBean> iterator = fvBeanSet.iterator();

			while (iterator.hasNext()) {
				FvBean fvBean = (FvBean) iterator.next();
				log.info("Fee Value is :: " + fvBean.getValue());
				Criteria criteria = session.createCriteria(FvBean.class);
				criteria.add(Restrictions.eq("value", fvBean.getValue())).setProjection(Projections.id());
			
				feeValueId = (Integer) criteria.list().iterator().next();
				log.info("fee Value Id :: " + feeValueId);
			}

		} finally {
			session.close();
		}
		return feeValueId;

	}

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

	public String getNewEnrollmentNumber(String preEnrollmentNumber) {
		// log.info("inside generate enrollment number");
		String enrollmentNumber = "";
		Session session = factory.openSession();
		// feeValueId=:feeId and
		//String query = "select enrollmentNumber from fee_values_master inner join applicant_values on  fee_values_master.feeValueId=applicant_values.value_id  where enrollmentNumber like:enrollmentNumber ";
		String query = "select enrollmentNumber from applicant_details where college_id_fk="+ preEnrollmentNumber.substring(2,4) + "";
		log.info("hjj");
		SQLQuery sqlQuery = session.createSQLQuery(query);
		//sqlQuery.setParameter("feeId", feeValueId);
		//sqlQuery.setParameter("enrollmentNumber", preEnrollmentNumber + "%");
		sqlQuery.setCacheable(false);
		List<String> enrollmentNumberList = sqlQuery.list();
		session.close();

		if (enrollmentNumberList.size() < 1) {
			log.info("list size is less than");
			return preEnrollmentNumber + "00001";
		} else if (enrollmentNumberList.size() >= 1) {
			// log.info("list size is gretter than 1");
			// log.info("Number of enrollment Number" + enrollmentNumberList);
			Long maxEnrollmentNumber = sortSetOfEnrollmentNumberAndReturnMax(enrollmentNumberList);
			// log.info("Max enrollmentNumber" + maxEnrollmentNumber);
			Integer count = maxEnrollmentNumber.toString().length() == 10 ? Integer.parseInt(maxEnrollmentNumber
					.toString().substring(5)) : Integer.parseInt(maxEnrollmentNumber.toString().substring(6));
			// log.info("value before increment" + count);
			log.info("value after cutting" + count);
			String nextValue = getEnrollNum(count);
			// log.info("new count" + nextValue);
			return preEnrollmentNumber + nextValue;
		}

		return null;

	}

	public String getEnrollNum(Integer count) {
		if (count.equals(0)) {
			String newValue = "00001";
			return newValue;

		} else {
			try {
				Integer incVal = count + 1;

				String newValue = String.valueOf(incVal);
				String increment = newValue.length() == 1 ? "0000" + newValue : newValue.length() == 2 ? "000" + newValue
						: newValue.length() == 3 ?"00"+newValue : newValue.length()==4?"0"+newValue:newValue;
				return increment;
			} catch (java.lang.NullPointerException e) {
				String newValue = "00001";
				return newValue;
			}
		}

	}

	public String generateEnrollmentNum(AppBean appBean) {
		String startYear = appBean.getStartYear();
		log.info("start yar is:"+startYear);
		String course = appBean.getCourse();

		Set<FvBean> paramSet = appBean.getApplicantParamValues();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		// log.info("1");
		AffBean affBean = lgBean.getAffBean();
		String collegeId = affBean.getInstId().toString().length() == 1 ? "0" + affBean.getInstId().toString()
				: affBean.getInstId().toString();
		String universityId = affBean.getParBeanAff().getParInstId().toString().length() == 1 ? "0"
				+ affBean.getParBeanAff().getParInstId().toString() : affBean.getParBeanAff().getParInstId().toString();

		// Integer feevalueId = fvDAO.getCourseId(course);

		//Integer feevalueId = getFeeValueId(paramSet);

		//String feeValueIdStr = feevalueId.toString().length() == 1 ? "0" + feevalueId.toString() : feevalueId
				//.toString();
		String twoDigitStartYear = startYear.substring(2, 4);

		String enrollmentNumber = getNewEnrollmentNumber(universityId + collegeId + twoDigitStartYear); //+ feeValueIdStr);

		return enrollmentNumber;
	}

}
