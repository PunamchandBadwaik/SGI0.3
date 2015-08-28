package com.dexpert.feecollection.main.users.superadmin;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.RandomPasswordGenerator;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;
import com.dexpert.feecollection.main.users.parent.ParBean;

public class SaDAO {
	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(AffDAO.class.getName());

	// End of Global Variables
	public Object getRowCount() {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			Criteria c = session.createCriteria(ParBean.class);
			c.addOrder(Order.desc("parInstId"));
			c.setMaxResults(1);
			ParBean temp = (ParBean) c.uniqueResult();

			return temp.getParInstId() + 1;

		} finally {
			// close session
			session.close();
		}
	}

	public SaBean saveOrUpdate(SaBean superAdmin) {
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(superAdmin);
			session.getTransaction().commit();
			return superAdmin;

		} catch (Exception e) {

			e.printStackTrace();
			return superAdmin;
		} finally {

			// close session
			session.close();

		}
	}

	public SaBean getSaDetail(Integer id) {
		Session session = factory.openSession();
		try {
			Criteria criteria = session.createCriteria(SaBean.class);
			criteria.add(Restrictions.eq("saId", id));

			SaBean bean = (SaBean) criteria.list().iterator().next();
			return bean;

		} finally {

			// close session
			session.close();

		}

	}

	public SaBean validateTheForgetPwdDetails(String profile, String emailId) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		Session session=factory.openSession();
		try{
		SaBean bean=(SaBean)session.createCriteria(SaBean.class).add(Restrictions.eq("emailId", emailId)).list().iterator().next();
		if(bean!=null){
			
			LoginBean loginDetailsOfSuAdmin=(LoginBean)session.get(LoginBean.class,bean.getLoginBean().getLoginId());
			
			// to get Random generated Password
			String password = RandomPasswordGenerator.generatePswd(6, 8, 1, 2, 0);
			log.info("Password Generated is " + password);
			log.info("User Name is " + bean.loginBean.getUserName());

			// to Encrypt Password
			PasswordEncryption.encrypt(password);
			String encryptedPwd = PasswordEncryption.encStr;
			
			
		loginDetailsOfSuAdmin.setPassword(encryptedPwd);
		
		
		Transaction tx=session.beginTransaction();
		session.saveOrUpdate(loginDetailsOfSuAdmin);
			
				tx.commit();
			//session.close();
			
			// -----Code for sending email//--------------------
			EmailSessionBean email = new EmailSessionBean();
			email.sendEmail(bean.getEmailId(), "Welcome To Fee Collection Portal!", bean.getLoginBean().getUserName(), password,
					bean.getFirstName());
			
			log.info("password :"+password);
			
		}
		
		
		return bean;
	
		} catch(NoSuchElementException ex)
		{
		return null;
		
		}finally {

		// close session
		session.close();

	}
	}
	
}
