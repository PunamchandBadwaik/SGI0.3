package com.dexpert.feecollection.challan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;

public class ChallanDAO {

	Configuration configuration = new Configuration().configure("/hibernateSab.cfg.xml");
	StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration
			.getProperties());
	SessionFactory factorySb = configuration.buildSessionFactory(builder.build());
	static Logger log = Logger.getLogger(ChallanDAO.class.getName());

	SessionFactory factory = ConnectionClass.getFactory();

	// static Logger log = Logger.getLogger(LookupDAO.class.getName());
	public ChallanBean serachByChallanNumber(String challanId) {
		ChallanBean challanBean = null;
		Session session = factorySb.openSession();
		Criteria criteria = session.createCriteria(ChallanBean.class);
		criteria.add(Restrictions.eq("challanNumber", challanId));
		List<ChallanBean> challanList = criteria.list();
		log.info("Challan List is ::" + challanList.size());
		if (challanList.size() > 0) {
			challanBean = challanList.iterator().next();
		}
		session.close();
		return challanBean;
	}

	public List<ChallanBean> allChallanInfo() {
		Session session = factorySb.openSession();
		Criteria criteria = session.createCriteria(ChallanBean.class);
		List<ChallanBean> allChallanList = criteria.list();
		session.close();
		return allChallanList;
	}

	public void saveToTransaction(ChallanBean challanBean) throws ParseException {
		Session session = factory.openSession();
		try {

			Date transDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String dt = sdf.format(transDate);

			transDate = sdf.parse(sdf.format(transDate));
			TransactionBean transactionBean = new TransactionBean();

			transactionBean.setTransDate(challanBean.getDate());
			transactionBean.setPayeeMob(challanBean.getPhone());
			transactionBean.setPayeeFirstName(challanBean.getFirstName());
			transactionBean.setPayeeLstName(challanBean.getLstName());
			transactionBean.setPayeeAmount(challanBean.getAmount());
			transactionBean.setTxnId(challanBean.getTxnId());
			transDate = sdf.parse(sdf.format(transDate));
			transactionBean.setTransDate(transDate);

			Transaction tx = session.beginTransaction();
			session.save(transactionBean);
			tx.commit();

		} finally {
			session.close();
		}

	}

	public void saveToTransaction(String enrollmentId, String mobile, String txnId, Double fee) throws ParseException {
		Session session = factorySb.openSession();
		try {
			Date transDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String dt = sdf.format(transDate);

			transDate = sdf.parse(sdf.format(transDate));
			TransactionBean transactionBean = new TransactionBean();
			transactionBean.setPayeeMob(mobile);
			transactionBean.setTxnId(txnId);
			transactionBean.setTransDate(transDate);
			transactionBean.setPayeeAmount(fee);

			Transaction tx = session.beginTransaction();
			session.save(transactionBean);

		} finally {

			session.close();
			// TODO: handle exception
		}

	}

}
