package com.dexpert.feecollection.main.users.applicant;

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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import COM.rsa.Intel.cr;

import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.communication.sms.SendSMS;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.config.FcDAO;
import com.dexpert.feecollection.main.fee.config.FeeDetailsBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvDAO;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.RandomPasswordGenerator;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;

public class AppDAO {

	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(AppDAO.class.getName());
	AffDAO aff = new AffDAO();

	// End of Global Variables

	// ---------------------------------------------------

	// DAO Methods Here

	public AppBean saveOrUpdate(AppBean appBean, Integer aplInstId) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		// Declarations
		// Open session from session factory

		Session session = factory.openSession();
		AffBean affBean = new AffBean();
		if (appBean.getCourse().contentEquals("FE")) {

			appBean.setYearCode("10");

		} else if (appBean.getCourse().contentEquals("SED") || appBean.getCourse().contentEquals("SE")) {
			appBean.setYearCode("20");

		} else if (appBean.getCourse().contentEquals("ME")) {
			appBean.setYearCode("50");

		} else if (appBean.getCourse().contentEquals("MBA")) {
			appBean.setYearCode("60");

		}

		else if (appBean.getCourse().contentEquals("BPhFY")) {
			appBean.setYearCode("10");

		} else if (appBean.getCourse().contentEquals("BPhSYD") || appBean.getCourse().contentEquals("BPhSY")) {
			appBean.setYearCode("20");

		} else if (appBean.getCourse().contentEquals("BPhTY")) {
			appBean.setYearCode("30");

		}

		else if (appBean.getCourse().contentEquals("BPhFnY")) {
			appBean.setYearCode("40");

		} else if (appBean.getCourse().contentEquals("MPhFY")) {
			appBean.setYearCode("30");

		} else if (appBean.getCourse().contentEquals("MPhFnY")) {
			appBean.setYearCode("30");

		}

		try {
			if (appBean.getEnrollmentNumber().equals("null") || appBean.getEnrollmentNumber().equals(null)
					|| appBean.getEnrollmentNumber().equals("")) {
				GenerateEnrollmentNumber en = new GenerateEnrollmentNumber();
				String EnrollNo = en.generateEnrollmentNumber(appBean.getYear(), appBean.getYearCode(),
						appBean.getCourse());
				appBean.setEnrollmentNumber(EnrollNo);
				log.info("enrollment number generated" + EnrollNo);
			}
		} catch (java.lang.NullPointerException e) {
			GenerateEnrollmentNumber en = new GenerateEnrollmentNumber();
			String EnrollNo = en
					.generateEnrollmentNumber(appBean.getYear(), appBean.getYearCode(), appBean.getCourse());
			appBean.setEnrollmentNumber(EnrollNo);
			log.info("enrollment number generated" + EnrollNo);
		}

		// to get college record based on id to create relationship
		affBean = aff.viewInstDetail(aplInstId);

		LoginBean loginBean = new LoginBean();
		loginBean.setUserName(appBean.getEnrollmentNumber());

		// to Encrypt Password
		PasswordEncryption.encrypt(String.valueOf(appBean.getYear().substring(0, 4)));
		String encryptedPwd = PasswordEncryption.encStr;

		loginBean.setPassword(encryptedPwd);
		loginBean.setProfile("Student");
		appBean.setLoginBean(loginBean);
		loginBean.setAppBean(appBean);

		affBean.setAppBean(appBean);
		appBean.setAffBeanStu(affBean);

		// one to many Relationship
		affBean.getAplBeanSet().add(appBean);

		try {
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(appBean);
			tx.commit();

			try {

				if (appBean.getAplMobilePri().equals("") || appBean.getAplMobilePri().equals("null")
						|| appBean.getAplMobilePri().equals(null)) {

				} else {
					String user = appBean.getEnrollmentNumber();
					String pass = appBean.getYear().substring(0, 4);;
					String msg = "UserId :" + user + "" + " Passsword : " + pass;
					SendSMS sms = new SendSMS();
					sms.sendSMS(appBean.getAplMobilePri(), msg);

				}
			} catch (java.lang.NullPointerException e) {

			}

			try {

				if (appBean.getAplEmail().equals("") || appBean.getAplEmail().equals("null")
						|| appBean.getAplEmail().equals(null)) {

				} else {
					EmailSessionBean email = new EmailSessionBean();
					email.sendEmail(appBean.getAplEmail(), "Welcome To FeeDesk!", appBean.getEnrollmentNumber(),
							appBean.getYear(), appBean.getAplFirstName().concat(" ").concat(appBean.getAplLstName()));

				}
			} catch (java.lang.NullPointerException e) {

			}
		} finally {

			session.close();
		}
		return appBean;

	}

	public List<AppBean> getAllStudentList() {
		Session session = factory.openSession();
		try {
			Criteria criteria = session.createCriteria(AppBean.class);
			List<AppBean> list = criteria.list();
			return list;
		} finally {
			session.close();
		}

	}

	public AppBean viewApplicantDetail(String appId) {
		Session session = factory.openSession();
		try {

			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.add(Restrictions.eq("enrollmentNumber", appId));
			AppBean appBean = (AppBean) criteria.list().iterator().next();
			return appBean;

		} finally {
			session.close();
			// TODO: handle exception
		}

	}

	public AffBean getStudentDetail(LoginBean bean) {
		Session session = factory.openSession();
		try {

			Integer id = bean.getAffBean().getInstId();
			Criteria criteria = session.createCriteria(AffBean.class);

			criteria.add(Restrictions.eq("instId", id));

			AffBean affBean = (AffBean) criteria.list().iterator().next();

			return affBean;

		} finally {
			session.close();
			// TODO: handle exception
		}
	}

	public List<String> existingEnrollNum(AppBean appBean) {
		Session session = factory.openSession();
		try {
			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.setProjection(Projections.property("enrollmentNumber"));
			criteria.add(Restrictions.eq("enrollmentNumber", appBean.getEnrollmentNumber()));
			List<String> list = criteria.list();
			return list;

		} finally {
			session.close();
			// TODO: handle exception
		}

	}

	public List<AppBean> getStudentDetailByEnrollMentNumber(String enrollmentNumber) {
		Session session = factory.openSession();

		try {

			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.add(Restrictions.eq("enrollmentNumber", enrollmentNumber));

			List<AppBean> appBeanList = criteria.list();
			return appBeanList;

		} finally {
			session.close();
			// TODO: handle exception
		}

	}

	public AppBean getUserDetail(String EnrId) {

		Session session = factory.openSession();

		try {

			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.add(Restrictions.eq("enrollmentNumber", EnrId));

			AppBean appBean = new AppBean();
			try {
				appBean = (AppBean) criteria.list().iterator().next();
			} catch (java.util.NoSuchElementException e) {

			}
			return appBean;

		} finally {
			session.close();
			// TODO: handle exception
		}
	}

	public ArrayList<AppBean> importExcelFileToDatabase(String fileUploadFileName, File fileUpload, String path)
			throws Exception {

		String name, lstName, gender, cast, address, acaYear, course, branch, emailAddress;

		Integer enrollNo = null, mobileNumPri, MobileNumSec, admYear;
		String mobPri, mobSec, admssionYear;

		// ArrayList<AppBean> appBeansList = new ArrayList<AppBean>();
		AppBean appBean = new AppBean();
		// AffDAO affDAO = new AffDAO();
		FileInputStream fileInputStream = new FileInputStream(fileUpload);

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);

		XSSFSheet hssfSheet = xssfWorkbook.getSheetAt(0);

		Iterator<Row> rowIterator = hssfSheet.iterator();

		while (rowIterator.hasNext()) {
			Row row = (Row) rowIterator.next();

			if (row.getRowNum() == 0) {
				continue;
			}

			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = (Cell) cellIterator.next();

				switch (cell.getCellType()) {

				case Cell.CELL_TYPE_STRING:

					break;

				case Cell.CELL_TYPE_NUMERIC:
					break;

				}
			}

			Cell r = row.getCell(0);

			try {
				enrollNo = (int) r.getNumericCellValue();
			} catch (java.lang.NullPointerException e) {

			}
			r = row.getCell(1);
			name = r.getStringCellValue();

			r = row.getCell(2);
			lstName = r.getStringCellValue();

			r = row.getCell(3);
			gender = r.getStringCellValue();

			r = row.getCell(4);
			cast = r.getStringCellValue();

			r = row.getCell(5);
			address = r.getStringCellValue();

			r = row.getCell(6);
			mobileNumPri = (int) r.getNumericCellValue();
			
			log.info("Mobile number is ::"+mobileNumPri);

			/*
			 * r = row.getCell(6); mobPri = r.getStringCellValue();
			 */

			r = row.getCell(7);
			MobileNumSec = (int) r.getNumericCellValue();

			/*
			 * r = row.getCell(7); mobSec = r.getStringCellValue();
			 */

			/*
			 * r = row.getCell(8); admYear = (int) r.getNumericCellValue();
			 */

			r = row.getCell(8);
			admssionYear = r.getStringCellValue();

			r = row.getCell(9);
			try {
				acaYear = r.getStringCellValue();
			} catch (java.lang.NullPointerException e) {
				// TODO: handle exception
			}

			r = row.getCell(10);
			course = r.getStringCellValue();

			r = row.getCell(11);

			try {
				branch = r.getStringCellValue();
			} catch (java.lang.NullPointerException e) {
				// TODO: handle exception
			}

			r = row.getCell(12);
			emailAddress = r.getStringCellValue();

			appBean.setAplFirstName(name);
			appBean.setAplLstName(lstName);
			appBean.setGender(gender);
			appBean.setCategory(cast);
			appBean.setAplAddress(address);

			/*
			 * appBean.setAplMobilePri(mobPri.toString());
			 * appBean.setAplMobileSec(mobSec.toString());
			 */
			log.info("Mobile number is1 ::"+mobileNumPri);
			appBean.setAplMobilePri(mobileNumPri.toString());
			appBean.setAplMobileSec(MobileNumSec.toString());
			log.info("Mobile number is2 ::"+appBean.getAplMobilePri());
			/* appBean.setYear(admYear.toString()); */

			appBean.setYear(admssionYear.toString());

			if (cast.contentEquals("A")) {

				appBean.setCategory("Open / E.B.C Category");

			} else if (cast.contentEquals("B")) {

				appBean.setCategory("OBC / ESBC (Maratha) Category");

			} else if (cast.contentEquals("C")) {

				appBean.setCategory("SC / ST / DT / VJ / NT / SBC Category");

			}

			if (course.contentEquals("SE (Direct)")) {
				String c = "SED";
				appBean.setCourse(c);
			} else if (course.contentEquals("S Y B.Ph.")) {
				String c = "BPhSY";
				appBean.setCourse(c);

			}

			else if (course.contentEquals("S Y B.Ph.(D)")) {
				String c = "BPhSYD";
				appBean.setCourse(c);

			} else if (course.contentEquals("T Y B.Ph.")) {
				String c = "BPhSYD";
				appBean.setCourse(c);

			} else if (course.contentEquals("M.Pharm First Year")) {
				String c = "MPhFY";
				appBean.setCourse(c);

			}

			else if (course.contentEquals("S.Y.M.Ph")) {
				String c = "MPhFnY";
				appBean.setCourse(c);

			}

			else {
				appBean.setCourse(course);
			}

			if (appBean.getCourse().contentEquals("FE")) {

				appBean.setYearCode("10");

			} else if (appBean.getCourse().contentEquals("SED") || appBean.getCourse().contentEquals("SE")) {
				appBean.setYearCode("20");

			} else if (appBean.getCourse().contentEquals("ME")) {
				appBean.setYearCode("50");

			} else if (appBean.getCourse().contentEquals("MBA")) {
				appBean.setYearCode("60");

			}

			else if (appBean.getCourse().contentEquals("BPhFY")) {
				appBean.setYearCode("10");

			} else if (appBean.getCourse().contentEquals("BPhSYD") || appBean.getCourse().contentEquals("BPhSY")) {
				appBean.setYearCode("20");

			} else if (appBean.getCourse().contentEquals("BPhTY")) {
				appBean.setYearCode("30");

			}

			else if (appBean.getCourse().contentEquals("BPhFnY")) {
				appBean.setYearCode("40");

			} else if (appBean.getCourse().contentEquals("MPhFY")) {
				appBean.setYearCode("30");

			} else if (appBean.getCourse().contentEquals("MPhFnY")) {
				appBean.setYearCode("30");

			}

			try {
				appBean.setEnrollmentNumber(enrollNo.toString());
				if (appBean.getEnrollmentNumber().equals("null") || appBean.getEnrollmentNumber().equals(null)
						|| appBean.getEnrollmentNumber().equals("")) {
					GenerateEnrollmentNumber en = new GenerateEnrollmentNumber();
					String EnrollNo = en.generateEnrollmentNumber(appBean.getYear(), appBean.getYearCode(),
							appBean.getCourse());
					appBean.setEnrollmentNumber(EnrollNo);
				}
			} catch (java.lang.NullPointerException e) {
				GenerateEnrollmentNumber en = new GenerateEnrollmentNumber();
				String EnrollNo = en.generateEnrollmentNumber(appBean.getYear(), appBean.getYearCode(),
						appBean.getCourse());
				appBean.setEnrollmentNumber(EnrollNo);
			}

			appBean.setAplEmail(emailAddress);

			// appBeansList.add(appBean);

			addBulkData(appBean);

		}
		return null;

	}

	// ---------------------------------------------------

	// to save record into Database
	public ArrayList<AppBean> addBulkData(AppBean appBean) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		HttpServletRequest request = ServletActionContext.getRequest();

		HttpSession httpSession = request.getSession();
		LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");

		List<AppBean> studentFromDBList = getStudentDetailByEnrollMentNumber(appBean.getEnrollmentNumber());

		if (studentFromDBList.isEmpty()) {
			Session session = factory.openSession();

			AffBean affBean = new AffBean();
			AffBean clgBean = new AffBean();
			if (lgBean.getProfile().contentEquals("CollegeOperator")) {

				clgBean = aff.viewInstDetail(lgBean.getOperatorBean().getAffBean().getInstId());

			} else if (lgBean.getProfile().contentEquals("Institute")) {
				clgBean = aff.viewInstDetail(lgBean.getAffBean().getInstId());
			}

			// to get college record based on id to create relationship
			affBean = aff.viewInstDetail(clgBean.getInstId());

			LoginBean loginBean = new LoginBean();
			loginBean.setUserName(appBean.getEnrollmentNumber());

			// to Encrypt Password
			PasswordEncryption.encrypt(String.valueOf(appBean.getYear().substring(0, 4)));
			String encryptedPwd = PasswordEncryption.encStr;

			loginBean.setPassword(encryptedPwd);
			loginBean.setProfile("Student");
			appBean.setLoginBean(loginBean);
			loginBean.setAppBean(appBean);

			affBean.setAppBean(appBean);
			appBean.setAffBeanStu(affBean);

			// one to many Relationship
			affBean.getAplBeanSet().add(appBean);
			Transaction tx = session.beginTransaction();
			session.save(appBean);
			tx.commit();
			//updateStudentDue(appBean);
			try {

				if (appBean.getAplMobilePri().equals("") || appBean.getAplMobilePri().equals("null")
						|| appBean.getAplMobilePri().equals(null)) {

				} else {
					String user = appBean.getEnrollmentNumber();
					String pass = appBean.getYear().substring(0, 4);
					String msg = "UserId :" + user + "" + " Passsword : " + pass;
					SendSMS sms = new SendSMS();
					sms.sendSMS(appBean.getAplMobilePri(), msg);

				}
			} catch (java.lang.NullPointerException e) {

			}

			try {

				if (appBean.getAplEmail().equals("") || appBean.getAplEmail().equals("null")
						|| appBean.getAplEmail().equals(null)) {

				} else {
					EmailSessionBean email = new EmailSessionBean();
					email.sendEmail(appBean.getAplEmail(), "Welcome To FeeDesk!", appBean.getEnrollmentNumber(),
							appBean.getYear(), appBean.getAplFirstName().concat(" ").concat(appBean.getAplLstName()));

				}
			} catch (java.lang.NullPointerException e) {

			}

			session.close();

		}

		return null;
	}

	public void updateStudentDue(AppBean appBean) {

		List<Integer> feeIdes = new ArrayList<Integer>();

		log.info("Course Name is ::" + appBean.getCourse());
		String applicableFeeString = getApplicableFeesString(appBean.getCourse().trim());
		FcDAO fcDAO = new FcDAO();
		FvDAO fvDAO = new FvDAO();
		String applicableFeeIdArray[] = applicableFeeString.split("~");
		for (String string : applicableFeeIdArray) {
			feeIdes.add(Integer.parseInt(string));
		}
		Iterator<Integer> itr = feeIdes.iterator();
		while (itr.hasNext()) {
			Integer feeId = itr.next();
			String feeName = fcDAO.getFeeName(feeId);
			Integer categoryId = fvDAO.findFeeValueId(appBean.getCategory(), feeId);
			Integer courseId = fvDAO.findFeeValueId(appBean.getCourse(), feeId);
			Double fee = fcDAO.calculateFeeStudent(categoryId, courseId, null, feeId);
			updateDueAmountOfStudent(appBean, feeId, fee, feeName);
		}

	}

	public void updateDueAmountOfStudent(AppBean appBean, Integer feeId, Double feeAmount, String feeName) {

		PaymentDuesBean paymentDue = new PaymentDuesBean();
		paymentDue.setFeeName(feeName);
		paymentDue.setFeeId(feeId);
		paymentDue.setNetDue(feeAmount);
		paymentDue.setTotal_fee_amount(feeAmount);
		// log.info("enrollment number in action class" +
		// appBean1.getEnrollmentNumber());
		saveDueFeeOFStudent(appBean.getEnrollmentNumber(), paymentDue);

	}

	public void saveDueFeeOFStudent(String enrollmentNumber, PaymentDuesBean dueBean) {

		log.info("enrollment Id of student is=========" + enrollmentNumber);
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(AppBean.class);
		criteria.add(Restrictions.eq("enrollmentNumber", enrollmentNumber));
		AppBean applicantDetails = (AppBean) criteria.list().iterator().next();
		applicantDetails.getPaymentDues().add(dueBean);
		Transaction transaction = session.beginTransaction();
		session.save(applicantDetails);
		transaction.commit();
		session.close();
	}

	public AppBean getStudentDues(String enrollmentNumber) {
		Session session = factory.openSession();
		AppBean completeDeatilsOfStudent = (AppBean) session.get(AppBean.class, enrollmentNumber);
		session.close();
		return completeDeatilsOfStudent;
	}

	public Double totalDueFeeOfStudent(String enrollMentNumber) {
		Session session = factory.openSession();
		String query = "SELECT  Sum(netDue) FROM sgi.fee_dues_master where enrollmentNumber_Fk=:enrollmentNumber";
		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setParameter("enrollmentNumber", enrollMentNumber);
		Double totalAmount = (Double) sqlQuery.list().iterator().next();
		log.info("Total fees ::" + totalAmount);
		return totalAmount;
	}

	public List<FeeDetailsBean> getAllFeeDeatils() {
		Session session = factory.openSession();
		List<FeeDetailsBean> feeList = session.createCriteria(FeeDetailsBean.class).list();
		session.close();
		return feeList;
	}

	public List<TransactionBean> getTransactionDetailsOfStudent(String enrollmentNumber) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(TransactionBean.class);
		criteria.add(Restrictions.eq("studentEnrollmentNumber", enrollmentNumber));
		List<TransactionBean> tranDetOfStu = criteria.list();
		session.close();
		return tranDetOfStu;
	}

	public String getApplicableFeesString(String courseName) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(ApplicableFeesBean.class);
		criteria.add(Restrictions.eq("courseName", courseName)).setProjection(Projections.property("applicableFeeId"));
		String applicableFee = (String) criteria.list().iterator().next();
		session.close();
		return applicableFee;

	}

	public Double totalfeesOfStudent(String enrollmentNumber) {
		Session session = factory.openSession();
		String query = "SELECT  Sum(total_fee_amount) FROM sgi.fee_dues_master where enrollmentNumber_Fk=:enrollmentNumber";
		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setParameter("enrollmentNumber", enrollmentNumber);
		Double totalAmount = (Double) sqlQuery.list().iterator().next();
		log.info("Total fees ::" + totalAmount);
		session.close();
		return totalAmount;
	}

	public Double totalPaymentDone(String enrollmentNumber) {
		Session session = factory.openSession();
		String query = "SELECT  Sum(payments_to_date) FROM sgi.fee_dues_master where enrollmentNumber_Fk=:enrollmentNumber";
		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setParameter("enrollmentNumber", enrollmentNumber);
		Double totalAmount = (Double) sqlQuery.list().iterator().next();
		log.info("Total fees ::" + totalAmount);
		session.close();
		return totalAmount;
	}

	public AppBean getStudentOpDues(String enrollmentNumber, Integer InstId) {
		log.info("Enroll  ::" + enrollmentNumber + " :::::: " + InstId);
		Session session = factory.openSession();
		try {

			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.add(Restrictions.eq("enrollmentNumber", enrollmentNumber)).add(
					Restrictions.eq("affBeanStu.instId", InstId));
			AppBean appBean = (AppBean) criteria.list().iterator().next();

			return appBean;
		} finally {
			session.close();
		}

	}

}
