package com.dexpert.calendaryear;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.opensymphony.xwork2.ActionSupport;

public class CalendarAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpSession httpSession = request.getSession();
	private CalendarBean calendarBean = new CalendarBean();
	Logger log = Logger.getLogger(CalendarAction.class.getName());
	CalendarDAO calendarDAO = new CalendarDAO();
	private ArrayList<PaymentCycleBean> paymentCycleBeans = new ArrayList<PaymentCycleBean>();

	// method to validate year
	public Boolean validateYears(String startYear, String endYear) {
		log.info("INFO:inside the valiadate Year method()");
		Boolean isYearValid = true;

		if (Integer.parseInt(startYear) > Integer.parseInt(endYear)) {
			isYearValid = false;

		}
		log.info("INFO:End valiadate Year method()");
		return isYearValid;
	}

	// method to validate date
	public Boolean valiadateDates(Date startDate, Date endDate) throws ParseException {
		log.info("INFO:inside the valiadate date method()");
		Boolean isDatesValid = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sdf.parse(startDate.toString());
		Date date2 = sdf.parse(endDate.toString());
		log.info("start date is after parsing" + date1);
		log.info("end  date is after parsing" + date2);
		if (date2.before(date1) || date2.equals(date1)) {
			isDatesValid = false;
		}
		log.info("INFO:End Of Validate Date method()");
		return isDatesValid;

	}

	public String generateSessionId(Integer instId) {
		Calendar calendar = Calendar.getInstance();
		Integer currentyear = calendar.get(Calendar.YEAR);
		Integer nextYear = currentyear + 1;
		// session id is combination of inst id +current year and next year
		String sessionId = instId.toString() + currentyear.toString() + nextYear.toString();

		return sessionId;
	}

	private String switchCasesForMonth(String numericValue) {
		String month = null;
		switch (numericValue) {
		case "01":
			month = "January";
			break;
		case "02":
			month = "February";
			break;
		case "03":
			month = "March";
			break;
		case "04":
			month = "April";
			break;
		case "05":
			month = "May";
			break;
		case "06":
			month = "June";
			break;
		case "07":
			month = "July";
			break;
		case "08":
			month = "August";
			break;
		case "09":
			month = "September";
			break;
		case "10":
			month = "October";
			break;
		case "11":
			month = "November";
			break;
		case "12":
			month = "December";
			break;

		default:
			break;
		}

		return month;
	}

	// method to save calendar year
	public String saveCalendarYear() {
		AffBean instBean = (AffBean) httpSession.getAttribute("instBean");
		Integer instId = instBean.getInstId();
		calendarBean.setCalendar_session_id(generateSessionId(instId));
		calendarBean.setInstId(instId);
		calendarBean.setIsCalendarActive(true);
		calendarDAO.saveCalendarYear(calendarBean);
		String message = "Calendar Saved SuccessFully";
		request.setAttribute("msg", message);
		return SUCCESS;
	}

	public String getCalInfMaForPayMentCycle() {
		AffBean instBean = (AffBean) httpSession.getAttribute("instBean");
		Integer instId = instBean.getInstId();
		calendarBean = calendarDAO.getCalendarInformation(instId, null, null).get(0);
		return SUCCESS;
	}
	public Calendar getPreviousDate (String date,String month,String year) throws ParseException{
		String completeDate=year.toString().concat("-").concat(month).concat("-").concat(date);
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date myDate = dateFormat.parse(completeDate);
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(myDate);
		cal1.add(Calendar.DAY_OF_YEAR, -1);
		return cal1;
	
		
		
	}

	public String savePaymentCycles() throws ParseException  {
		ArrayList<PaymentCycleBean> temp=new ArrayList<PaymentCycleBean>();
		
		for(int i=0;i<paymentCycleBeans.size();i++)
		{
		Calendar calendar=Calendar.getInstance();
		Integer year=calendar.get(Calendar.YEAR);	
		if(i==paymentCycleBeans.size()-1){
		PaymentCycleBean firstPaymentCycle=paymentCycleBeans.get(0);
		Calendar cal=getPreviousDate(firstPaymentCycle.getCycleStratDay(),firstPaymentCycle.getCycleStartMonth(),year.toString());
		PaymentCycleBean lastCycle=paymentCycleBeans.get(paymentCycleBeans.size()-1);
		lastCycle.setCycleEndDay(""+cal.get(Calendar.DATE));
		lastCycle.setCycleEndMonth(""+(cal.get(Calendar.MONTH)+1));
		temp.add(lastCycle);
		}else{	
		PaymentCycleBean paymentCycleBean=paymentCycleBeans.get(i);
		PaymentCycleBean nextCycle=paymentCycleBeans.get(i+1);
		Calendar calendar2=getPreviousDate(nextCycle.getCycleStratDay(),nextCycle.getCycleStartMonth(), year.toString());
		paymentCycleBean.setCycleEndDay(""+calendar2.get(Calendar.DATE));
		paymentCycleBean.setCycleEndMonth(""+(calendar2.get(Calendar.MONTH)+1));
		temp.add(paymentCycleBean);
		}
		}
		calendarDAO.savePaymentCycleBeans(temp);
		String msg = "Payment cycles saved successfully";
		request.setAttribute("msg", msg);
		return SUCCESS;
	}

	// setter and getter
	public CalendarBean getCalendarBean() {
		return calendarBean;
	}

	public void setCalendarBean(CalendarBean calendarBean) {
		this.calendarBean = calendarBean;
	}

	public ArrayList<PaymentCycleBean> getPaymentCycleBeans() {
		return paymentCycleBeans;
	}

	public void setPaymentCycleBeans(ArrayList<PaymentCycleBean> paymentCycleBeans) {
		this.paymentCycleBeans = paymentCycleBeans;
	}

}
