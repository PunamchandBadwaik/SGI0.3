package com.dexpert.feecollection.main.users.parent;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
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
import com.dexpert.feecollection.main.users.superadmin.SaBean;

public class ParDAO {

	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(AffDAO.class.getName());

	// End of Global Variables

	// ---------------------------------------------------

	// DAO Methods Here

	ParBean parBean = new ParBean();

	// method to get max row count of table
	public Integer getRowCount() {

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

	// method to register record of university
	public ParBean saveOrUpdate(ParBean parBean, String path) {
		// Declarations
		// Open session from session factory
		Session session = factory.openSession();
		try {
			byte[] bFile = null;
			Integer fileSize = null;
			// file input Stream is use to save file in to DataBase
			try {
				FileInputStream fileInputStream = null;

				// to create new file with actual name with extension
				File dstFile = new File(path, parBean.getFileUploadFileName());

				// to copy files at specified destination path
				FileUtils.copyFile(parBean.getFileUpload(), dstFile);

				// convert file into array of bytes

				bFile = new byte[(int) dstFile.length()];
				fileInputStream = new FileInputStream(dstFile);

				fileSize = fileInputStream.read(bFile);
				// fileinputStream must be close
				fileInputStream.close();
			} catch (java.lang.NullPointerException e) {
				// TODO: handle exception
			}

			parBean.setFilesByteSize(bFile);

			parBean.setFileSize(fileSize);
			session.beginTransaction();
			session.saveOrUpdate(parBean);
			session.getTransaction().commit();
			return parBean;

		} catch (Exception e) {

			e.printStackTrace();
			return parBean;
		} finally {

			// close session
			session.close();

		}
	}

	public List<ParBean> getUniversityList() {
		Session session = factory.openSession();
		try {

			Criteria criteria = session.createCriteria(ParBean.class);
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<ParBean> list = criteria.list();
			return list;

		} finally {
			session.close();
			// TODO: handle exception
		}
	}

	public ParBean viewUniversity(Integer id) {
		Session session = factory.openSession();
		try {
			Criteria criteria = session.createCriteria(ParBean.class);
			criteria.add(Restrictions.eq("parInstId", id));
			ParBean bean = (ParBean) criteria.list().iterator().next();
			return bean;
		} finally {
			session.close();
			// TODO: handle exception
		}
		// TODO Auto-generated method stub

	}

	public static ParBean validateTheForgetPwdDetails(String profile, String emailId) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		Session session = factory.openSession();
		try {
			ParBean bean = (ParBean) session.createCriteria(ParBean.class)
					.add(Restrictions.eq("parInstEmail", emailId)).list().iterator().next();
			if (bean != null) {

				LoginBean loginDetailsOfSuAdmin = (LoginBean) session.get(LoginBean.class, bean.getLoginBean()
						.getLoginId());

				// to get Random generated Password
				String password = RandomPasswordGenerator.generatePswd(6, 8, 1, 2, 0);
				log.info("Password Generated is " + password);
				log.info("User Name is " + bean.loginBean.getUserName());

				// to Encrypt Password
				PasswordEncryption.encrypt(password);
				String encryptedPwd = PasswordEncryption.encStr;

				loginDetailsOfSuAdmin.setPassword(encryptedPwd);

				Transaction tx = session.beginTransaction();
				session.saveOrUpdate(loginDetailsOfSuAdmin);

				tx.commit();
				// session.close();

				// -----Code for sending email//--------------------
				EmailSessionBean email = new EmailSessionBean();
				email.sendEmail(bean.getParInstEmail(), "Welcome To Fee Collection Portal!", bean.getLoginBean()
						.getUserName(), password, bean.getParInstName());

				log.info("password :" + password);

			}

			return bean;
		} catch (NoSuchElementException ex) {
			return null;

		} finally {

			// close session
			session.close();

		}
	}

	public List<Integer> getIdesOfAllCollege(Integer id) {
		List<Integer> listOfIdes = new ArrayList<Integer>();
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(ParBean.class);
		ParBean parBean = (ParBean) criteria.add(Restrictions.eq("parInstId", id)).list().iterator().next();
		Set<AffBean> affBean = parBean.getAffBeanOneToManySet();
		Iterator<AffBean> itr = affBean.iterator();
		while (itr.hasNext()) {
			listOfIdes.add(itr.next().getInstId());
		}
		log.info("list size is" + listOfIdes.size());
		return listOfIdes;

	}

	public Integer getUniversityId(String universityName) {
		Integer universityId=null;
		if(universityName!=null &&!universityName.isEmpty()&&!universityName.contentEquals("")){
		Session session = factory.openSession();
		universityId=(Integer)session.createCriteria(ParBean.class).add(Restrictions.eq("parInstName", universityName))
				.setProjection(Projections.id()).list().iterator().next();
		}
	        return universityId;
	
	}

}
