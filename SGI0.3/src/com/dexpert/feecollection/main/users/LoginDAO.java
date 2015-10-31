package com.dexpert.feecollection.main.users;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;
import com.dexpert.feecollection.main.users.operator.OperatorBean;
import com.dexpert.feecollection.main.users.parent.ParBean;
import com.dexpert.feecollection.main.users.superadmin.SaBean;

public class LoginDAO {

	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(AffDAO.class.getName());

	// End of Global Variables

	// ---------------------------------------------------

	// DAO Methods Here
	// method to getDetails About user
	public List<LoginBean> getLoginDetails(LoginBean loginBean) {
		Session session = factory.openSession();
		try {

			String sql = "select * from sgi.login_master where userName=:user";
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			sqlQuery.setParameter("user", loginBean.getUserName());
			sqlQuery.addEntity(LoginBean.class);
			List<LoginBean> bean= sqlQuery.list();
			/*
			 * Criteria criteria = session.createCriteria(LoginBean.class);
			 * criteria.add(Restrictions.eq("userName",
			 * loginBean.getUserName()));
			 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			 * List<LoginBean> bean = criteria.list();
			 */

			return bean;
		} finally {
			session.close();
		}
	}

	public static OperatorBean getDetailsOfCollegeOperator(String operatorUserName) {

		Session session = factory.openSession();
		LoginBean bean = new LoginBean();
		try {
			bean = (LoginBean) session.createCriteria(LoginBean.class).add(
					Restrictions.eq("userName", operatorUserName));
			return bean.getOperatorBean();
		} catch (java.lang.NullPointerException e) {
			return bean.getOperatorBean();
		} finally {
			session.close();
		}

	}

	public LoginBean getLoginUserDetail(String id, String profile) {

		Integer loginId = Integer.parseInt(id);
		Session session = factory.openSession();
		try {

			LoginBean loginBean = (LoginBean) session.get(LoginBean.class, loginId);
			return loginBean;
		} finally {
			session.close();

		}

	}

	public ParBean getParentDetail(Integer parInstId) {

		Session session = factory.openSession();
		try {
			ParBean loginBean = (ParBean) session.get(ParBean.class, parInstId);
			return loginBean;
		} finally {
			session.close();

		}
	}

	public AffBean getInstDetail(Integer instId) {
		Session session = factory.openSession();
		try {
			AffBean affBean = (AffBean) session.get(AffBean.class, instId);
			return affBean;
		} finally {
			session.close();

		}

	}

	public SaBean getSuperAdminDetail(Integer saId) {
		Session session = factory.openSession();
		try {
			SaBean saBean = (SaBean) session.get(SaBean.class, saId);
			return saBean;
		} finally {
			session.close();

		}
	}

	public OperatorBean getOperatorDetail(Integer operatorId) {
		Session session = factory.openSession();
		try {
			OperatorBean opBean = (OperatorBean) session.get(OperatorBean.class, operatorId);
			return opBean;
		} finally {
			session.close();

		}
	}

	public static void updateChangePwdDetails(LoginBean creds, String newPwd) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {

		Session session = factory.openSession();

		String password = newPwd;
		log.info("Password  is changed:" + password);

		PasswordEncryption.encrypt(password);

		String userEncryptedPwd = PasswordEncryption.encStr;

		LoginBean bean = (LoginBean) session.get(LoginBean.class, creds.getLoginId());

		bean.setPassword(userEncryptedPwd);

		Transaction tx = session.beginTransaction();
		session.merge(bean);
		tx.commit();
		session.close();

	}

}
