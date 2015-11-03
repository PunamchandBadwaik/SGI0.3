package com.dexpert.feecollection.main.payment.studentPayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.config.FcBean;
import com.dexpert.feecollection.main.fee.lookup.LookupDAO;
import com.dexpert.feecollection.main.users.applicant.AppBean;

public class ApplicantFeeCollectionDAO {
	// Global Variables
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(LookupDAO.class.getName());

	public String getDueString(String tranId) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(TransactionBean.class);
		criteria.add(Restrictions.eq("txnId", tranId));
		TransactionBean tran = (TransactionBean) criteria.list().iterator().next();
		String DueString = tran.getDueString();
		session.close();
		return DueString;

	}

	public void updateTransactionStatus(String transId, String transStatus, String paymentMode, String RPS) {
		Session session = factory.openSession();
		TransactionBean oldTransBean = (TransactionBean) session.get(TransactionBean.class, transId);
		// if (transStatus != null) {
		oldTransBean.setStatus(transStatus);
		// }
		oldTransBean.setPaymentMode(paymentMode);
		oldTransBean.setAllowPayCode(RPS);
		Transaction tran = session.beginTransaction();
		session.merge(oldTransBean);
		tran.commit();
		session.close();

	}

	public Integer getTransId(String txnId) {

		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(TransactionBean.class);
		criteria.add(Restrictions.eq("txnId", txnId));
		Integer id = (Integer) criteria.list().iterator().next();
		session.close();
		return id;

	}

	public TransactionBean getTransaction(String txnId) {
		Session session = factory.openSession();
		Criteria cr = session.createCriteria(TransactionBean.class);
		cr.add(Restrictions.eq("txnId", txnId));
		TransactionBean res = (TransactionBean) cr.list().iterator().next();
		return res;
	}

	public ArrayList<BulkPaymentBean> getBulkPayments(String txnId) {

		Session session = factory.openSession();
		Criteria cr = session.createCriteria(BulkPaymentBean.class);
		cr.add(Restrictions.eq("txnId", txnId));
		ArrayList<BulkPaymentBean> resList = new ArrayList<BulkPaymentBean>(cr.list());
		return resList;
	}

	public void updateTransTable(String txnId, String RspCode, String dueStr, String studentEnrollmentNo, String payMode) {

		Session session = factory.openSession();

		// log.info("DUe String is ::" + dueStr);
		try {
			// log.info("updateTransTable method :");
			TransactionBean oldBean = (TransactionBean) session.get(TransactionBean.class, txnId);

			if (RspCode.equals("Ok") || RspCode.equals("0")) {
				Transaction transaction = session.beginTransaction();
				oldBean.setPaymentMode(payMode);

				oldBean.setStatus("Paid");
				oldBean.setAllowPayCode(RspCode);
				session.merge(oldBean);
				transaction.commit();

				if (dueStr.contains("!")) {
					// log.info("If ::::::::::::::::");
					String collectionOfFeeIdAndAmount[] = dueStr.split("!");
					// log.info("collection Array 11111" +
					// collectionOfFeeIdAndAmount[0]);
					// log.info("Size of comple Array" +
					// collectionOfFeeIdAndAmount.length);
					for (String singleFeeIdAndAmount : collectionOfFeeIdAndAmount) {

						String feeIdAndAmount[] = singleFeeIdAndAmount.split("~");
						// log.info("Array size after spiting by ~" +
						// feeIdAndAmount[0]);
						// log.info("Fee Id AMount is ::" + feeIdAndAmount[0]);
						// log.info("Enrollment  is ::" + studentEnrollmentNo);

						SQLQuery sqlQuery = session
								.createSQLQuery("SELECT * FROM sgi.fee_dues_master where feeId=:feeId and enrollmentNumber_Fk=:enroll");
						sqlQuery.setParameter("feeId", feeIdAndAmount[0]);
						sqlQuery.setParameter("enroll", studentEnrollmentNo);
						sqlQuery.addEntity(PaymentDuesBean.class);
						PaymentDuesBean paymentDue = (PaymentDuesBean) sqlQuery.list().iterator().next();
						Transaction tran = session.beginTransaction();
						paymentDue.setNetDue((paymentDue.getNetDue() - Double.parseDouble(feeIdAndAmount[1])));
						Double paymentToDate = paymentDue.getPayments_to_date() == null ? 0.0d : paymentDue
								.getPayments_to_date();
						// log.info("payment to date" + paymentToDate);
						paymentDue.setPayments_to_date(paymentToDate + Double.parseDouble(feeIdAndAmount[1]));
						session.merge(paymentDue);
						tran.commit();
					}

				}

				else {

					String feeIdAndAmount[] = dueStr.split("~");
					SQLQuery sqlQuery = session
							.createSQLQuery("SELECT * FROM sgi.fee_dues_master where feeId=:feeId and enrollmentNumber_Fk=:enroll");
					sqlQuery.setParameter("feeId", feeIdAndAmount[0]);
					sqlQuery.setParameter("enroll", studentEnrollmentNo);
					sqlQuery.addEntity(PaymentDuesBean.class);
					PaymentDuesBean paymentDue = (PaymentDuesBean) sqlQuery.list().iterator().next();
					Transaction tran = session.beginTransaction();
					paymentDue.setNetDue((paymentDue.getNetDue() - Double.parseDouble(feeIdAndAmount[1])));
					paymentDue.setPayments_to_date(paymentDue.getPayments_to_date() == null ? 0.0d : paymentDue
							.getPayments_to_date() + Double.parseDouble(feeIdAndAmount[1]));
					session.merge(paymentDue);
					tran.commit();

				}

			} else {
				Transaction transaction = session.beginTransaction();
				oldBean.setStatus("Declined");
				session.merge(oldBean);
				transaction.commit();
			}

		} finally {

			session.close();
		}

	}

	public void updateFeeduesTableDetail(String dueString) {
		Session session = factory.openSession();
		String onePairOfDueIdAndString[] = null;
		try {

			if (dueString.contains("!")) {
				String[] collectionOfdueIdAndAmount = dueString.split("!");
				for (String string : collectionOfdueIdAndAmount) {
					onePairOfDueIdAndString = string.split("~");
					PaymentDuesBean dueBean = (PaymentDuesBean) session.get(PaymentDuesBean.class,
							onePairOfDueIdAndString[0]);
					dueBean.setNetDue(dueBean.getNetDue() == null ? 0.0d : dueBean.getNetDue()
							- Double.parseDouble(onePairOfDueIdAndString[1]));
					dueBean.setPayments_to_date(dueBean.getPayments_to_date() == null ? 0.0 : dueBean
							.getPayments_to_date() + Double.parseDouble(onePairOfDueIdAndString[1]));
					Transaction transaction = session.beginTransaction();
					session.merge(dueBean);
					transaction.commit();
				}
			} else {
				onePairOfDueIdAndString = dueString.split("~");
				PaymentDuesBean dueBean = (PaymentDuesBean) session.get(PaymentDuesBean.class,
						onePairOfDueIdAndString[0]);
				dueBean.setNetDue(dueBean.getNetDue() == null ? 0.0d : dueBean.getNetDue()
						- Double.parseDouble(onePairOfDueIdAndString[1]));
				dueBean.setPayments_to_date(dueBean.getPayments_to_date() == null ? 0.0 : dueBean.getPayments_to_date()
						+ Double.parseDouble(onePairOfDueIdAndString[1]));
				Transaction transaction = session.beginTransaction();
				session.merge(dueBean);
				transaction.commit();

			}

		} finally {
			session.close();

		}

	}

	public void insertPaymentDetails(TransactionBean transaction) {
		Session session = factory.openSession();
		Transaction hibernateTran = session.beginTransaction();
		session.save(transaction);
		hibernateTran.commit();
		session.close();
	}

	public void insertBulkPayDetails(ArrayList<BulkPaymentBean> savelist) {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			for (int i = 0; i < savelist.size(); i++) {
				BulkPaymentBean savebean = new BulkPaymentBean();
				savebean = savelist.get(i);
				session.save(savebean);
				if (i % 20 == 0) { // 20, same as the JDBC batch size
					// flush a batch of inserts and release memory:
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// close session
			session.close();
		}
	}
}
