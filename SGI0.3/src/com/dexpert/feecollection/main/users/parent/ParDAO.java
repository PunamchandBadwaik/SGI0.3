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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import COM.rsa.Intel.cr;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.RandomPasswordGenerator;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;
import com.dexpert.feecollection.main.users.applicant.AppBean;
import com.dexpert.feecollection.main.users.superadmin.SaBean;

public class ParDAO {

	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(ParDAO.class.getName());

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
				String emailContent = "Welcome to the FeeDesk portal of " + bean.getParInstName()
						+ ". You can log in with the below credentials. ";
				EmailSessionBean email = new EmailSessionBean();
				email.sendEmail(bean.getParInstEmail(), "Welcome To Fee Collection Portal!", bean.getLoginBean()
						.getUserName(), password, bean.getParInstName(), emailContent);

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
		try {
			Criteria criteria = session.createCriteria(ParBean.class);
			ParBean parBean = (ParBean) criteria.add(Restrictions.eq("parInstId", id)).list().iterator().next();
			Set<AffBean> affBean = parBean.getAffBeanOneToManySet();
			Iterator<AffBean> itr = affBean.iterator();
			while (itr.hasNext()) {
				listOfIdes.add(itr.next().getInstId());
			}
			log.info("list size is" + listOfIdes.size());
			return listOfIdes;
		} finally {
			session.close();
		}
	}

	public Integer getUniversityId(String universityName) {
		Integer universityId = null;
		Session session = factory.openSession();
		try {
			if (universityName != null && !universityName.isEmpty() && !universityName.contentEquals("")) {

				universityId = (Integer) session.createCriteria(ParBean.class)
						.add(Restrictions.eq("parInstName", universityName)).setProjection(Projections.id()).list()
						.iterator().next();
			}
			return universityId;
		} finally {
			session.close();
		}

	}

	public List<Object[]> getTotDuesOFStudOFAllColl(Integer parentInstId) {
		List<Object[]> duesArray = null;
		DetachedCriteria dc = DetachedCriteria.forClass(AffBean.class);
		dc.add(Restrictions.eq("parBeanAff.parInstId", parentInstId));
		dc.setProjection(Projections.id());
		DetachedCriteria dc2 = DetachedCriteria.forClass(AppBean.class);
		dc2.add(Subqueries.propertyIn("affBeanStu.instId", dc)).setProjection(Projections.property("enrollmentNumber"));
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(ParBean.class, "parent");
		try {
			criteria.createAlias("parent.affBeanOneToManySet", "inst");
			criteria.createAlias("inst.aplBeanSet", "student");
			criteria.createAlias("student.paymentDues", "payDueBean");
			criteria.add(Subqueries.propertyIn("student.enrollmentNumber", dc2));
			criteria.add(Subqueries.propertyIn("payDueBean.appBean.enrollmentNumber", dc2));
			criteria.add(Restrictions.eq("parInstId", parentInstId));
			criteria.setProjection(Projections.projectionList().add(Projections.property("parent.parInstName"))
					.add(Projections.sum("payDueBean.total_fee_amount"))
					.add(Projections.sum("payDueBean.payments_to_date")).add(Projections.sum("payDueBean.netDue")));
			duesArray = criteria.list();
			return duesArray;
		} catch (Exception ex) {
			criteria.add(Restrictions.eq("parInstId", parentInstId));
			criteria.setProjection(Projections.property("parent.parInstName"));
			String parentInstName = (String) criteria.list().iterator().next();
			Object obj[] = new Object[4];
			obj[0] = parentInstName;
			obj[1] = 0;
			obj[2] = 0;
			obj[3] = 0;
			duesArray = new ArrayList<Object[]>();
			duesArray.add(obj);
			return duesArray;

		} finally {

			session.close();
		}

	}
}
