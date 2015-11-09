package com.dexpert.feecollection.main.users.operator;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.RandomPasswordGenerator;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;

public class OperatorDao {

	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(OperatorDao.class.getName());

	public static void registerCollegeOperatorDao(OperatorBean operatorBean) {

		Session session = factory.openSession();

		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(operatorBean);
		transaction.commit();

		/*
		 * LoginBean loginBean=new LoginBean();
		 * loginBean.setUserName(operatorBean.getOperatorName());
		 * 
		 * // to Encrypt Password
		 * PasswordEncryption.encrypt(String.valueOf(appBean.)); String
		 * encryptedPwd = PasswordEncryption.encStr;
		 * 
		 * loginBean.setPassword(encryptedPwd); loginBean.setProfile("Student");
		 * appBean.setLoginBean(loginBean); loginBean.setAppBean(appBean);
		 */

	}

	public static List<OperatorBean> getAllRecordsOfCollegeOperator(Integer InstId) {
		Session session = factory.openSession();
		List<OperatorBean> listOfOptrRecords = new ArrayList<OperatorBean>();
		try {
			Criteria criteria = session.createCriteria(OperatorBean.class);
			criteria.add(Restrictions.eq("affBean.instId", InstId));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			listOfOptrRecords = criteria.list();
			return listOfOptrRecords;

		} finally {
			session.close();

		}

	}

	public static Integer getRowCount() {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			Criteria c = session.createCriteria(OperatorBean.class);
			c.addOrder(Order.desc("operatorId"));
			c.setMaxResults(1);
			OperatorBean temp = (OperatorBean) c.uniqueResult();
			return temp.getOperatorId() + 1;

		} finally {
			// close session
			session.close();
		}

	}

	public static OperatorBean validateTheForgetPwdDetails(String profile, String emailId) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		Session session = factory.openSession();
		try {

			OperatorBean bean = (OperatorBean) session.createCriteria(OperatorBean.class)
					.add(Restrictions.eq("operatorEmail", emailId)).list().iterator().next();
			if (bean != null) {

				LoginBean loginDetailsOfOperator = (LoginBean) session.get(LoginBean.class, bean.getLoginBean()
						.getLoginId());

				// to get Random generated Password
				String password = RandomPasswordGenerator.generatePswd(6, 8, 1, 2, 0);
				log.info("Password Generated is " + password);
				log.info("User Name is " + bean.loginBean.getUserName());

				// to Encrypt Password
				PasswordEncryption.encrypt(password);
				String encryptedPwd = PasswordEncryption.encStr;

				loginDetailsOfOperator.setPassword(encryptedPwd);

				Transaction tx = session.beginTransaction();
				session.saveOrUpdate(loginDetailsOfOperator);

				tx.commit();
				// session.close();

				// -----Code for sending email//--------------------
				String emailContent="Welcome to the FeeDesk portal of "+bean.getOperatorName()+ ". You can log in with the below credentials. ";

				EmailSessionBean email = new EmailSessionBean();
				email.sendEmail(bean.getOperatorEmail(), "Welcome To Fee Collection Portal!", bean.getLoginBean()
						.getUserName(), password, bean.getOperatorName(),emailContent);

				log.info("password :" + password);

			}

			return bean;

		} catch (NoSuchElementException ex) {
			return null;

		}

		finally {
			// close session
			session.close();
		}
	}

	public Integer getCollegeIdOfOperator(Integer operatorId) {
		Session session = factory.openSession();

		Criteria criteria = session.createCriteria(OperatorBean.class);
		criteria.add(Restrictions.eq("operatorId", operatorId));
		criteria.setProjection(Projections.property("affBean.instId"));
		Integer collegeId = (Integer) criteria.list().iterator().next();

		/*
		 * SQLQuery sqlQuery = session .createSQLQuery(
		 * "SELECT InsId_Fk FROM sgi.operator_table where operatorId=:operatorId"
		 * ); sqlQuery.setParameter("operatorId", operatorId); Integer collegeId
		 * = (Integer) sqlQuery.list().iterator().next();
		 */session.close();
		return collegeId;

	}

	
	public static void updatePersonalRecordOfCollegeOperatorDetail(OperatorBean operatorBean) {

		Session session=factory.openSession();
		OperatorBean operatorBean2=(OperatorBean)session.get(OperatorBean.class, operatorBean.getOperatorId());
		
		operatorBean2.setOperatorName(operatorBean.getOperatorName());
		operatorBean2.setOperatorAddress(operatorBean.getOperatorAddress());
		operatorBean2.setOperatorLstName(operatorBean.getOperatorLstName());
		operatorBean2.setOperatorEmail(operatorBean.getOperatorEmail());
		operatorBean2.setOperatorContact(operatorBean.getOperatorContact());
		operatorBean2.setOperatorContactSec(operatorBean.getOperatorContactSec());
		
		Transaction transaction=session.beginTransaction();
		session.merge(operatorBean2);
		transaction.commit();
		session.close();
		
		
	}
	public OperatorBean getOperatorBean(Integer operaId){
	Session session=factory.openSession();	
	OperatorBean operatorBean=(OperatorBean)session.get(OperatorBean.class,operaId);	
	session.close();
	return operatorBean;	
	}
	
}
