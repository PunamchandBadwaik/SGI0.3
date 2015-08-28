package com.dexpert.feecollection.main;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.challan.ChallanAction;
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
		String payMode = request.getParameter("payMode");

		try {
			HttpSession httpSession = (HttpSession) request.getServletContext().getAttribute(txnId);
			LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");
			
			String dueStr = (String) httpSession.getAttribute("dueStr");
			HashMap hm = (HashMap) httpSession.getAttribute("hmap");
			String studentEnrollmentNo = (String) hm.get("enrollId");
			String sgiTxnId = hm.get("txnID").toString();

			if (RPS.equals("0"))

			{

				if (payMode.equals("Cash") || payMode.equals("Cheque")) {

					dao.updateTransactionStatus(sgiTxnId, "Pending", payMode);

				}

				// dao.updateTransTable(sgiTxnId, RPS, dueStr,
				// studentEnrollmentNo,
				// payMode);

				if (loginBean.getProfile().contentEquals("CollegeOperator")) {

					return "opHome";
				} else {
					return "home";
				}
			}

			else {

				// dao.updateTransTable(sgiTxnId, RPS, "", studentEnrollmentNo,
				// payMode);

				if (payMode.equals("null") || payMode.equals("") || payMode.equals(null)) {

					dao.updateTransactionStatus(sgiTxnId, "Cancelled", "");
				} else {
					dao.updateTransactionStatus(sgiTxnId, "Cancelled", payMode);
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
