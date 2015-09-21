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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.affiliated.AffBean;

public class GenerateEnrollmentNumber {
	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(GenerateEnrollmentNumber.class.getName());

	/*
	 * public static void main(String[] args) { GenerateEnrollmentNumber gg =
	 * new GenerateEnrollmentNumber(); // gg.generateEnrollmentNumber("2011",
	 * "1", "B.Ph.FY"); Integer count = gg.getCountOfYear("2013", "1",
	 * "B.Ph.FY");
	 * 
	 * String initialString = "131"; String en =
	 * gg.getEnrollNumForPharma(count); String finalEnroll =
	 * initialString.concat(en); System.out.println("Enrollment number is ::: "
	 * + finalEnroll);
	 * 
	 * }
	 */
	public Long sortSetOfEnrollmentNumberAndReturnMax(Set<AppBean> appBeans)
	{
		ArrayList<Long> tempArray=new ArrayList<Long>();
		Iterator<AppBean> itr=appBeans.iterator();
		while(itr.hasNext())
		{
		Long intEnrollmentNumber=Long.parseLong(itr.next().getEnrollmentNumber());	
		tempArray.add(intEnrollmentNumber);	
		}
		Long maxEnrollmentNumber=Collections.max(tempArray);
		return maxEnrollmentNumber;
		
	}

	public String getNewEnrollmentNumber(String userEnteredValue,Integer couseId) {
		log.info("inside generate enrollment number");
		String enrollmentNumber="";
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(FvBean.class);
		criteria.add(Restrictions.eq("feeValueId", couseId));
		criteria.createAlias("appBeanParamSet", "app");
		criteria.add(Restrictions.like("app.enrollmentNumber", userEnteredValue+"%"));
		List<FvBean> fvBean=criteria.list();
		log.info("List size is"+fvBean.size());
		if(fvBean.size()<1){
		log.info("list size is less than");	
		return userEnteredValue+"001";	
		}
		else if(fvBean.size()>=1){
		log.info("list size is gretter than 1");	
		Set<AppBean> appBeans=fvBean.iterator().next().getAppBeanParamSet();	
		Long maxEnrollmentNumber=sortSetOfEnrollmentNumberAndReturnMax(appBeans);
		log.info("Max enrollmentNumber"+maxEnrollmentNumber);
		Integer count=Integer.parseInt(maxEnrollmentNumber.toString().substring(8,maxEnrollmentNumber.toString().length()));	
		String nextValue=getEnrollNum(count);
		return userEnteredValue+nextValue;
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

	public Integer getCourseId(String courseName) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(FvBean.class);
		criteria.add(Restrictions.eq("value", courseName)).setProjection(Projections.id());
		Integer courseId = (Integer) criteria.list().iterator().next();
		session.close();
		return courseId;
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
		Integer courseId = getCourseId(course);
		String twoDigitAdmissionYear=admissionYear.substring(2,4);
		

	String enrollmentNumber= getNewEnrollmentNumber(universityId+collegeId+twoDigitAdmissionYear+courseId,courseId);

	 return enrollmentNumber;
	}

}
