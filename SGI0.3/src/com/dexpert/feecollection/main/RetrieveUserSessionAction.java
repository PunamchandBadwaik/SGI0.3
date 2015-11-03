package com.dexpert.feecollection.main;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import com.dexpert.feecollection.main.payment.studentPayment.ApplicantFeeCollectionDAO;
import com.dexpert.feecollection.main.users.LoginBean;
import com.opensymphony.xwork2.ActionSupport;

public class RetrieveUserSessionAction extends ActionSupport {

	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	static Logger log = Logger.getLogger(RetrieveUserSessionAction.class.getName());
	ApplicantFeeCollectionDAO dao = new ApplicantFeeCollectionDAO();

	public String RetrieveSessionUpdateTrans() {

		String RPS = request.getParameter("RPS");
		String txnId = request.getParameter("txnID");
		String paymentMode = request.getParameter("payMode");
		log.info("PayMode is ::" + paymentMode);
		log.info("TXN ID is ::" + txnId);
		log.info("Response COde is ::" + RPS);
		// return null;

		try {
			HttpSession httpSession = (HttpSession) request.getServletContext().getAttribute(txnId);
			LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");

			String dueStr = (String) httpSession.getAttribute("dueStr");
			HashMap hm = (HashMap) httpSession.getAttribute("hmap");
			String studentEnrollmentNo = (String) hm.get("enrollId");
			String sgiTxnId = hm.get("txnID").toString();

			if (RPS.equals("Ok") || RPS.equals("0"))

			{
				log.info("PayMode is ::" + paymentMode);
				if (paymentMode != null && !paymentMode.equals("null") && !paymentMode.equals("")) {
					log.info("paymentMode is ::" + paymentMode);
					if (paymentMode.equals("Cash") || paymentMode.equals("Cheque")) {

						dao.updateTransactionStatus(sgiTxnId, "Pending", paymentMode, RPS);

					} else {
						log.info("paymentMode is d ::" + paymentMode);
						dao.updateTransTable(sgiTxnId, RPS, dueStr, studentEnrollmentNo, paymentMode);
					}

				}

				if (loginBean.getProfile().contentEquals("CollegeOperator")) {

					return "opHome";
				} else {
					return "home";
				}
			}

			else {

				if (paymentMode.equals("null") || paymentMode.equals("") || paymentMode.equals(null)) {

					dao.updateTransactionStatus(sgiTxnId, "Cancelled", "", RPS);
				} else {
					dao.updateTransactionStatus(sgiTxnId, "Cancelled", paymentMode, RPS);
				}

				if (loginBean.getProfile().contentEquals("CollegeOperator")) {

					return "opHome";
				} else {
					return "home";
				}
			}

		} catch (java.lang.NullPointerException e) {
			request.setAttribute("msg", "Session Time Out");
			return ERROR;
		}

	}

}
