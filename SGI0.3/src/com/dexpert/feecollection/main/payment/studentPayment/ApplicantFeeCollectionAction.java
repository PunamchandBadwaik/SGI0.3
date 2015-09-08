package com.dexpert.feecollection.main.payment.studentPayment;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.challan.ChallanDAO;
import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.fee.CartDataBean;
import com.dexpert.feecollection.main.fee.lookup.LookupAction;
import com.dexpert.feecollection.main.users.applicant.AppBean;
import com.dexpert.feecollection.main.users.applicant.AppDAO;
import com.dexpert.feecollection.main.users.operator.OperatorBean;
import com.opensymphony.xwork2.ActionSupport;

public class ApplicantFeeCollectionAction extends ActionSupport {

	/**
	 * 
	 */

	public String SabPaisaURL = "49.50.72.228:8080";
	public String returnUrl = "http://49.50.72.228:8080/SGI0.3/ReturnPage.jsp";
	String clientFailureUrl = "http://49.50.72.228:8080/SGI0.3/Login.jsp";

	private static final long serialVersionUID = 1L;
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	HttpSession httpSession = request.getSession();
	static Logger log = Logger.getLogger(LookupAction.class.getName());
	private String noValidate = "1";
	ApplicantFeeCollectionBean feeCollectionBean = new ApplicantFeeCollectionBean();
	ApplicantFeeCollectionDAO dao = new ApplicantFeeCollectionDAO();
	AppBean appBean1 = new AppBean();
	List<ApplicantFeeCollectionBean> collectionBeanList = new ArrayList<ApplicantFeeCollectionBean>();
	ApplicantFeeCollectionDAO afc = new ApplicantFeeCollectionDAO();
	AppDAO appDAO = new AppDAO();
	ChallanDAO challanDAO = new ChallanDAO();

	// //

	// get Student Service Detail

	public String responseHandelling() {

		String respCode = request.getParameter("RPS");
		/*
		 * String txnId = request.getParameter("txnID");
		 * log.info("Response Code is ::" + respCode);
		 */
		HttpSession session = (HttpSession) httpSession.getServletContext().getAttribute("txnId");
		HashMap<String, String> hmap = (HashMap<String, String>) session.getAttribute("hmap");
		String clientTranId = hmap.get("txnID");
		TransactionBean transBean = dao.getTransaction(clientTranId);
		if (transBean.getBulkPay() != null && transBean.getBulkPay() == 1) {
			ArrayList<BulkPaymentBean> bulkdetails = new ArrayList<BulkPaymentBean>();
			bulkdetails = dao.getBulkPayments(clientTranId);

			for (int i = 0; i < bulkdetails.size(); i++) {

				if (respCode.equals("0")) {
					dao.updateFeeduesTableDetail(bulkdetails.get(i).getDueString());

					request.setAttribute("msg", " Congratulations.., Your Transaction Successfully Done..");
					request.setAttribute("txnID", "Transaction ID is :: " + clientTranId);

				} else {
					request.setAttribute("msg", "Sorry, Your Transaction Declined..");
					request.setAttribute("txnID", "Transaction ID is :: " + clientTranId);
				}
			}
		} else {
			String dueString = dao.getDueString(clientTranId);
			if (respCode.equals("0")) {
				dao.updateFeeduesTableDetail(dueString);

				request.setAttribute("msg", " Congratulations.., Your Transaction Successfully Done..");
				request.setAttribute("txnID", "Transaction ID is :: " + clientTranId);

			} else {
				request.setAttribute("msg", "Sorry, Your Transaction Declined..");
				request.setAttribute("txnID", "Transaction ID is :: " + clientTranId);
			}
		}
		return SUCCESS;
	}

	public String studentServiceDetail() {

		return SUCCESS;
	}

	public String submitParameter() {

		return SUCCESS;
	}

	public String operatorStudentPayment() throws IOException {
		HttpSession httpSession = request.getSession();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date date = new Date(timestamp.getTime());
		AppBean studentDetails = new AppBean();
		try {

			String enrollmentNumber = request.getParameter("enrollmentId").trim();
			studentDetails = appDAO.getUserDetail(enrollmentNumber);

			String txnId = Idgenerator.transxId();
			/* String user = request.getParameter("feeName"); */
			Double fee = Double.parseDouble(request.getParameter("totalPaidAmount"));
			String dueString = request.getParameter("dueString").trim();

			// insert details into transaction bean
			TransactionBean tran = new TransactionBean();
			tran.setInsId(studentDetails.getAffBeanStu().getInstId());
			tran.setDueString(dueString);
			tran.setPayeeAdd(studentDetails.getAplAddress());
			tran.setPayeeEmail(studentDetails.getAplEmail());
			tran.setPayeeMob(studentDetails.getAplMobilePri());
			tran.setPayeeName(studentDetails.getAplFirstName());
			tran.setStudentEnrollmentNumber(studentDetails.getEnrollmentNumber());
			tran.setTransDate(date);
			tran.setTxnId(txnId);
			tran.setPayeeAmount(fee);
			tran.setStatus("Pending");
			tran.setBulkPay(0);
			//
			dao.insertPaymentDetails(tran);
			String name = studentDetails.getAplFirstName();

			HashMap<String, String> hashMap = new HashMap<String, String>();

			hashMap.put("enrollId", enrollmentNumber);
			hashMap.put("txnID", txnId);
			httpSession.setAttribute("dueStr", dueString);
			httpSession.setAttribute("hmap", hashMap);
			httpSession.getServletContext().setAttribute(txnId, httpSession);

			String url = "http://" + SabPaisaURL + "/SabPaisa?Name=" + name + "&amt=" + fee + "&txnId=" + txnId
					+ "&RollNo=" + enrollmentNumber + "&client=SGI" + "&ru=" + returnUrl + "&Contact="
					+ studentDetails.getAplMobilePri() + "&failureURL=" + clientFailureUrl + "&Email="
					+ studentDetails.getAplEmail() + "&Add=" + studentDetails.getAplAddress();

			response.sendRedirect(url);
			return SUCCESS;
		} catch (java.lang.NullPointerException e) {
			request.setAttribute("msg", "Session Time Out");
			return ERROR;
		}

	}

	public String operatorStudentPaymentBulk() throws IOException {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date date = new Date(timestamp.getTime());
		AppBean studentDetails = new AppBean();
		BulkPaymentBean bulkBean = new BulkPaymentBean();
		ArrayList<CartDataBean> cartList = new ArrayList<CartDataBean>();
		ArrayList<BulkPaymentBean> bulkPayList = new ArrayList<BulkPaymentBean>();
		try {
			Integer cartInit = (Integer) httpSession.getAttribute("cart_init");
			String enrollmentNumber = "0";

			String txnId = Idgenerator.transxId();
			/* String user = request.getParameter("feeName"); */
			// Double fee =
			// Double.parseDouble(request.getParameter("totalPaidAmount"));
			Double feeTot = (double) 0;
			String dueString = "NA";
			// populate the bulk payment table
			if (cartInit == 0) {
				return ERROR;

			} else if (cartInit == 1) {
				cartList = (ArrayList<CartDataBean>) httpSession.getAttribute("cart_list");
				for (int i = 0; i < cartList.size(); i++) {
					bulkBean=new BulkPaymentBean();
					CartDataBean cartdata = cartList.get(i);
					enrollmentNumber = cartdata.getAppId().trim();
					studentDetails = appDAO.getUserDetail(enrollmentNumber);
					bulkBean.setDueString(cartdata.getDueString());
					bulkBean.setStudentEnrollmentNumber(cartdata.getAppId());
					bulkBean.setTransId(txnId);
					bulkBean.setInsId(studentDetails.getAffBeanStu().getInstId());
					bulkBean.setPayeeAdd(studentDetails.getAplAddress());
					bulkBean.setPayeeEmail(studentDetails.getAplEmail());
					bulkBean.setPayeeMob(studentDetails.getAplMobilePri());
					bulkBean.setPayeeName(studentDetails.getAplFirstName() + " " + studentDetails.getAplLstName());
					bulkBean.setPayeeAmount(cartdata.getAmount());
					feeTot = feeTot + cartdata.getAmount();
					bulkPayList.add(bulkBean);
				}
				dao.insertBulkPayDetails(bulkPayList);
			} else {
				return ERROR;
			}
			// insert details into transaction bean
			TransactionBean tran = new TransactionBean();
			tran.setInsId(studentDetails.getAffBeanStu().getInstId());
			tran.setDueString(dueString);
			tran.setPayeeAdd("NA");
			tran.setPayeeEmail("NA");
			tran.setPayeeMob("NA");
			tran.setPayeeName("Bulk Transaction");
			tran.setStudentEnrollmentNumber("NA");
			tran.setTransDate(date);
			tran.setTxnId(txnId);
			tran.setPayeeAmount(feeTot);
			tran.setStatus("Pending");
			tran.setBulkPay(1);
			//
			dao.insertPaymentDetails(tran);
			OperatorBean oprBean = (OperatorBean) httpSession.getAttribute("oprBean");
			String name = oprBean.getOperatorName() + " " + oprBean.getOperatorLstName();
			HashMap<String, String> hashMap = new HashMap<String, String>();

			hashMap.put("enrollId", "Bulk Payment");
			hashMap.put("txnID", txnId);
			httpSession.setAttribute("dueStr", dueString);
			httpSession.setAttribute("hmap", hashMap);
			httpSession.getServletContext().setAttribute(txnId, httpSession);

			String url = "http://" + SabPaisaURL + "/SabPaisa?Name=" + name + "&amt=" + feeTot + "&txnId=" + txnId
					+ "&RollNo=" + "Bulk Payment" + "&client=SGI" + "&ru=" + returnUrl + "&Contact="
					+ oprBean.getOperatorContact() + "&failureURL=" + clientFailureUrl + "&Email="
					+ oprBean.getOperatorEmail() + "&Add=" + oprBean.getOperatorAddress();

			response.sendRedirect(url);
			return SUCCESS;
		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("msg", "Session Time Out");
			return ERROR;
		}

	}

	// jumping to payment Gateway

	public void studentToPaymentGateway() throws IOException {

		String enrolId = request.getParameter("enrollId");
		String fee = request.getParameter("feeValue");
		log.info("Enroll ment Id is ::" + enrolId);
		String amt = request.getParameter("amt");
		String txnId = Idgenerator.transxId();
		// appBean1 = appDAO.getUserDetail(enrolId);
		String user = appBean1.getAplFirstName().concat(" ").concat(appBean1.getAplLstName());

		log.info("enrollment Number ::" + enrolId);
		log.info("Total Fee CAlculated ::" + fee);
		String url = "http://" + SabPaisaURL + "/SabPaisa?name=" + user + "&amount=" + fee;
		String returnUrl = "http://49.50.72.228:8080/NITJ/ReturnPage.jsp";
		response.sendRedirect(url);

	}

	// jumping to payment Gateway

	public void instPaymentGateway() throws IOException {

		String user = request.getParameter("feeName");
		String fee = request.getParameter("amt");
		String txnId = Idgenerator.transxId();
		String url = "http://49.50.72.228:8080/SabPaisa?name=" + user + "&amount=" + fee;
		response.sendRedirect(url);

	}

	// SGI Payment method
	public String studentPayment() throws IOException {
		HttpSession httpSession = request.getSession();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Date date = new Date(timestamp.getTime());
		AppBean studentDetails = new AppBean();
		String enrollmentId = (String) httpSession.getAttribute("StudentEnrollId");
		if (httpSession.equals(null)) {

			studentDetails = appDAO.getUserDetail(enrollmentId);
		} else {
			studentDetails = appDAO.getUserDetail(enrollmentId);
		}

		String txnId = Idgenerator.transxId();
		/* String user = request.getParameter("feeName"); */
		Double fee = Double.parseDouble(request.getParameter("totalPaidAmount"));
		String dueString = request.getParameter("dueString").trim();

		try {
			// insert details into transaction bean
			TransactionBean tran = new TransactionBean();
			tran.setInsId(studentDetails.getAffBeanStu().getInstId());
			tran.setDueString(dueString);
			tran.setPayeeAdd(studentDetails.getAplAddress());
			tran.setPayeeEmail(studentDetails.getAplEmail());
			tran.setPayeeMob(studentDetails.getAplMobilePri());
			tran.setPayeeName(studentDetails.getAplFirstName());
			tran.setStudentEnrollmentNumber(studentDetails.getEnrollmentNumber());
			tran.setTransDate(date);
			tran.setTxnId(txnId);
			tran.setPayeeAmount(fee);
			tran.setStatus("Pending");
			//
			dao.insertPaymentDetails(tran);
			String name = studentDetails.getAplFirstName();

			HashMap<String, String> hashMap = new HashMap<String, String>();

			hashMap.put("enrollId", enrollmentId);
			hashMap.put("txnID", txnId);
			httpSession.setAttribute("dueStr", dueString);
			httpSession.setAttribute("hmap", hashMap);
			httpSession.getServletContext().setAttribute(txnId, httpSession);

			String url = "http://" + SabPaisaURL + "/SabPaisa?Name=" + name + "&amt=" + fee + "&txnId=" + txnId
					+ "&RollNo=" + enrollmentId + "&client=SGI" + "&ru=" + returnUrl + "&Contact="
					+ studentDetails.getAplMobilePri() + "&failureURL=" + clientFailureUrl + "&Email="
					+ studentDetails.getAplEmail();

			response.sendRedirect(url);
			return SUCCESS;
		} catch (java.lang.NullPointerException e) {
			request.setAttribute("msg", "Session Time Out");
			return ERROR;
		}

	}

	public void SGIPaymentGateway() throws IOException, ParseException {

		String user = request.getParameter("firstName");
		String fee = request.getParameter("feeValue");
		String lastname = request.getParameter("lastName");
		String enrollmentId = request.getParameter("enrollId");
		String mobile = request.getParameter("mob");
		String txnId = Idgenerator.transxId();

		String name = user.concat(" ").concat(lastname);
		Double studFee = Double.parseDouble(fee);

		challanDAO.saveToTransaction(enrollmentId, mobile, txnId, studFee);

		String url = "http://" + SabPaisaURL + "/SabPaisa?Name=" + name + "&amt=" + fee + "&Contact=" + mobile
				+ "&RollNo=" + enrollmentId + "&client=SGI" + "&ru=" + returnUrl + "&txnId=" + txnId;

		response.sendRedirect(url);

	}

	// /
	public ApplicantFeeCollectionBean getFeeCollectionBean() {
		return feeCollectionBean;
	}

	public void setFeeCollectionBean(ApplicantFeeCollectionBean feeCollectionBean) {
		this.feeCollectionBean = feeCollectionBean;
	}

	public List<ApplicantFeeCollectionBean> getCollectionBeanList() {
		return collectionBeanList;
	}

	public void setCollectionBeanList(List<ApplicantFeeCollectionBean> collectionBeanList) {
		this.collectionBeanList = collectionBeanList;
	}

	public AppBean getAppBean1() {
		return appBean1;
	}

	public void setAppBean1(AppBean appBean1) {
		this.appBean1 = appBean1;
	}

	public String getNoValidate() {
		return noValidate;
	}

	public void setNoValidate(String noValidate) {
		this.noValidate = noValidate;
	}

}
