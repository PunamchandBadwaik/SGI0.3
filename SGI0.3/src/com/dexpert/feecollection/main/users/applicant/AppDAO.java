package com.dexpert.feecollection.main.users.applicant;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellRange;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import COM.rsa.jsafe.v;

import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.communication.sms.SendSMS;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.config.FcBean;
import com.dexpert.feecollection.main.fee.config.FcDAO;
import com.dexpert.feecollection.main.fee.config.FeeDetailsBean;
import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvDAO;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;
import com.mysql.fabric.xmlrpc.base.Array;

public class AppDAO {

	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(AppDAO.class.getName());
	AffDAO aff = new AffDAO();
	AffDAO affDAO = new AffDAO();
	FcDAO fcDAO = new FcDAO();
	FvDAO fvDAO = new FvDAO();

	// End of Global Variables

	// ---------------------------------------------------

	// DAO Methods Here
	
	
	public FvBean getfeeValue(Integer feeValueId) {
		Session session = factory.openSession();
		try {

			FvBean bean = (FvBean) session.get(FvBean.class, feeValueId);

			return bean;
		} finally {

			session.close();
		}
	}

	public AppBean saveOrUpdate(AppBean appBean, Integer aplInstId, LinkedHashSet<FvBean> fvBeans)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		// Declarations
		// Open session from session factory

		Session session = factory.openSession();
		AffBean affBean = new AffBean();
		Iterator<FvBean> iterator = fvBeans.iterator();
		LinkedHashSet<FvBean> fvBeansFromDB = new LinkedHashSet<FvBean>();
		String pp = "";

		while (iterator.hasNext()) {
			FvBean fvBeanValue = (FvBean) iterator.next();
			fvBeanValue = getfeeValue(fvBeanValue.getFeeValueId());
			fvBeansFromDB.add(fvBeanValue);
			pp = pp + (pp.concat(",").concat(fvBeanValue.getValue()));

		}

		appBean.setApplicantParamValues(fvBeansFromDB);
		// to get college record based on id to create relationship
		affBean = aff.viewInstDetail(aplInstId);

		BreakString bs = new BreakString();
		String k = bs.breakString(pp);

		log.info("String after Break Class : " + k);
		String year = bs.getYear(k);
		String course = bs.getCourse(k);

		// appBean.setYear(year);
		appBean.setCourse(course);
		// appBean.setYearCode(yearCode);

		// generating enrollment Number
		GenerateEnrollmentNumber en = new GenerateEnrollmentNumber();
		String EnrollNo = en.generateEnrollmentNum(appBean);
		appBean.setEnrollmentNumber(EnrollNo);

		// log.info("Enrollment Number is ::" + appBean.getEnrollmentNumber());
		LoginBean loginBean = new LoginBean();
		loginBean.setUserName(appBean.getEnrollmentNumber());

		// to Encrypt Password
		PasswordEncryption.encrypt(String.valueOf(year));
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
			session.save(appBean);
			tx.commit();

			// for text msg

			try {

				if (appBean.getAplMobilePri().equals("") || appBean.getAplMobilePri().equals("null")
						|| appBean.getAplMobilePri().equals(null)) {

				} else {

					String user = appBean.getEnrollmentNumber();
					String pass = year;
					String msg = "UserId :" + user + "" + " Passsword : " + pass;
					SendSMS sms = new SendSMS();
					sms.sendSMS(appBean.getAplMobilePri(), msg);

				}
			} catch (java.lang.NullPointerException e) {

			}

			// for email
			try {

				if (appBean.getAplEmail().equals("") || appBean.getAplEmail().equals("null")
						|| appBean.getAplEmail().equals(null)) {

				} else {

					EmailSessionBean email = new EmailSessionBean();
					email.sendEmail(appBean.getAplEmail(), "Welcome To FeeDesk!", appBean.getEnrollmentNumber(), year,
							appBean.getAplFirstName().concat(" ").concat(appBean.getAplLstName()));

				}
			} catch (java.lang.NullPointerException e) {

			}

			// to get detail about Dues
			getDuesDetail(appBean);

		} finally {

			session.close();
		}
		return appBean;

	}

	public void getDuesDetail(AppBean appBean) {
		AffDAO affDao = new AffDAO();
		// FvBean fvBean = new FvBean();
		Integer instId = appBean.getAffBeanStu().getInstId();
		Set<FvBean> appParamSet = appBean.getApplicantParamValues();
		AffBean instbean = affDao.getOneCollegeRecord(appBean.getAffBeanStu().getInstId());
		LinkedHashSet<FeeDetailsBean> instfeeSet = new LinkedHashSet<FeeDetailsBean>(instbean.getFeeSet());
		Iterator<FeeDetailsBean> feeDetailIterator = instfeeSet.iterator();
		FeeDetailsBean feeDetailsBean = new FeeDetailsBean();
		FcBean fcBean = new FcBean();
		// com.google.common.collect.ListMultimap<Integer, FvBean> comboMap =
		// ArrayListMultimap.create();
		Iterator<FvBean> iterator = appParamSet.iterator();
		ArrayList<Integer> list = new ArrayList<Integer>();
		while (iterator.hasNext()) {
			FvBean tempFvBean = (FvBean) iterator.next();
			list.add(tempFvBean.getFeeValueId());

		}
		Collections.sort(list);
		CalculateDues calDue = new CalculateDues();
		while (feeDetailIterator.hasNext()) {
            feeDetailsBean = (FeeDetailsBean) feeDetailIterator.next();
			List<Integer> structureIdes = affDao.getStrutureId(instId, feeDetailsBean.getFeeId());
			log.info("fee name " + feeDetailsBean.getFeeName());
			Double amt = calDue.calculateFeeStudent(list, feeDetailsBean.getFeeId(), structureIdes.get(0));
			fcBean.setAmount(amt);
			addToDuesTable(appBean, fcBean, feeDetailsBean);
		}

	}

	public void addToDuesTable(AppBean appBean, FcBean fcBean, FeeDetailsBean detailsBean) {
		Session session = factory.openSession();
		try {
			Date dd = new Date();

			PaymentDuesBean duesBean = new PaymentDuesBean();
			Transaction tx = session.beginTransaction();
			duesBean.setAppBean(appBean);
			duesBean.setDateCalculated(dd);
			duesBean.setFeeId(detailsBean.getFeeId());
			duesBean.setFeeName(detailsBean.getFeeName());
			duesBean.setNetDue(fcBean.getAmount());
			// duesBean.setPayee(fcBean.get);
			duesBean.setPayments_to_date(0.0);
			duesBean.setTotal_fee_amount(fcBean.getAmount());
			session.save(duesBean);
			tx.commit();

		} finally {
			session.close();

		}

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

		log.info("Student Enrollment Number ::" + EnrId);
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

	public AppBean AddListRecordToAppBean(ArrayList<String> list) {
		AppBean appBean = new AppBean();
		Iterator<String> iterator = list.iterator();
		log.info("getting record one by one");
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			log.info(string);

		}

		return null;

	}

	@SuppressWarnings("finally")
	public FvBean checkFeeValue(Integer lookupId, String element) {

		Session session = factory.openSession();
		FvBean bean = new FvBean();
		List<FvBean> list = new ArrayList<FvBean>();
		try {
			log.info("cell Value ::" + element + " " + lookupId);
			Criteria criteria = session.createCriteria(FvBean.class);
			criteria.add(Restrictions.eq("value", element));
			criteria.add(Restrictions.eq("lookupname.lookupId", lookupId));
			list = criteria.list();
			
			
			if (list.size() > 0) {
				LookupBean lookupBean = new LookupBean();
				log.info("Matched");
				Iterator<FvBean> iterator = list.iterator();
				FvBean fvBean = iterator.next();
				bean.setFeeValueId(fvBean.getFeeValueId());
				bean.setValue(fvBean.getValue());
				lookupBean.setLookupId(lookupId);
				bean.setLookupname(lookupBean);

				return bean;
			}
		} finally {
			session.close();
		}
		return null;

	}

	public ArrayList<AppBean> importExcelFileToDatabase1(String fileUploadFileName, File fileUpload, String path)
			throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		List<Integer> valueList = new ArrayList<Integer>();
		List<Integer> paramList = new ArrayList<Integer>();
		LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");

		List<Integer> str_ids = affDAO.getStrutureId(lgBean.getAffBean().getInstId(), null);
		valueList = fcDAO.getLookupValue(str_ids);

		paramList = fvDAO.getListOfValueBeans(valueList);

		FileInputStream fileInputStream = new FileInputStream(fileUpload);

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);

		XSSFSheet hssfSheet = xssfWorkbook.getSheetAt(0);

		// LinkedHashMap<Integer, ArrayList<Object>> map = new
		// LinkedHashMap<Integer, ArrayList<Object>>();
		LinkedHashMap<Integer, Map<ArrayList<Object>, List<FvBean>>> appBeanMap = new LinkedHashMap<Integer, Map<ArrayList<Object>, List<FvBean>>>();
		try {

			Iterator<Row> rows = hssfSheet.rowIterator();

			int j = 0;
			String stringVal;
			String blankVal;

			long numVal;
			XSSFCell cell;

			while (rows.hasNext()) {
				XSSFRow row = (XSSFRow) rows.next();

				if (row.getRowNum() == 0) {
					continue;
				}
				Iterator<Cell> cells = row.cellIterator();
				ArrayList<Object> tempArrayList = new ArrayList<Object>();

				List<FvBean> fvBeansList = new ArrayList<FvBean>();

				LinkedHashMap<ArrayList<Object>, List<FvBean>> rowMap = new LinkedHashMap<ArrayList<Object>, List<FvBean>>();

				// System.out.print(">>" + j + " ");

				int i = 0;
				while (cells.hasNext()) {

					cell = (XSSFCell) cells.next();

					switch (cell.getCellType()) {

					case XSSFCell.CELL_TYPE_STRING:
						stringVal = cell.getStringCellValue();
						// System.out.println(stringVal);
						tempArrayList.add(stringVal);
						break;

					case XSSFCell.CELL_TYPE_NUMERIC:
						numVal = (long) cell.getNumericCellValue();
						tempArrayList.add(numVal);
						// System.out.println(numVal);
						break;

					case XSSFCell.CELL_TYPE_BLANK:
						blankVal = cell.getStringCellValue();
						tempArrayList.add(blankVal);
						// System.out.println(blankVal);
						break;

					}
					i++;

					Iterator<Integer> paramIterator = paramList.iterator();
					while (paramIterator.hasNext()) {
						FvBean bean = new FvBean();
						String tempString = "";
						Long tempNum = null;
						Integer lookupId = (Integer) paramIterator.next();

						switch (cell.getCellType()) {

						case XSSFCell.CELL_TYPE_STRING:

							tempString = row.getCell(i - 1).toString();
							break;

						case XSSFCell.CELL_TYPE_NUMERIC:
							tempString = row.getCell(i - 1).toString();

							break;

						}

						String x = tempString.contains(".") ? tempString.substring(0, tempString.indexOf("."))
								: tempString;

						bean = checkFeeValue(lookupId, x);

						if (bean != null) {
							fvBeansList.add(bean);

						}

						log.info("Fv Bean List size ::" + fvBeansList.size());
					}

				}
				rowMap.put(tempArrayList, fvBeansList);
				log.info("temp Arryalist Size ::" + tempArrayList.size());

				appBeanMap.put(j, rowMap);

				j++;

			}
			log.info("AppBEan Map size ::" + appBeanMap.size());
			ArrayList<Integer> arrayList = new ArrayList<Integer>(appBeanMap.keySet());
			ArrayList<AppBean> appBeansList = new ArrayList<AppBean>();
			Iterator<Integer> iterator = arrayList.iterator();
			log.info("AppBean Array list  ::" + arrayList.size());

			while (iterator.hasNext()) {
				// log.info("AppBean Map");
				Integer integer = (Integer) iterator.next();
				Map<ArrayList<Object>, List<FvBean>> rMap = appBeanMap.get(integer);
				// log.info("R map ::" + rMap.size());
				Set<ArrayList<Object>> set = rMap.keySet();
				// log.info("Set  ::" + set.size());
				ArrayList<Object> aa = new ArrayList<Object>(set.iterator().next());
				// log.info("Arraylist ::" + aa.size());
				Collection<List<FvBean>> list = rMap.values();
				// log.info("Collection ::" + list.size());

				Set<FvBean> paramSet = new HashSet<FvBean>((List<FvBean>) list.iterator().next());

				AppBean appBean = new AppBean();
				appBean.setGrNumber(aa.get(0).toString());
				appBean.setAplFirstName(aa.get(1).toString());
				appBean.setAplLstName(aa.get(2).toString());
				appBean.setGender(aa.get(3).toString());
				appBean.setAplAddress(aa.get(4).toString());
				appBean.setAplMobilePri(aa.get(5).toString());
				appBean.setAplMobileSec(aa.get(6).toString());
				appBean.setAplEmail(aa.get(7).toString());
				appBean.setStartYear(aa.get(8).toString());
				appBean.setApplicantParamValues(paramSet);

				appBeansList.add(appBean);

			}

			Iterator<AppBean> iterator2 = appBeansList.iterator();
			// log.info("appBean List Size " + appBeansList.size());
			while (iterator2.hasNext()) {
				AppBean appBean = (AppBean) iterator2.next();

				// System.out.println(appBean.getGrNumber());

				addBulkData(appBean);

			}

		} catch (Exception e) {

		}

		return null;

	}

	public ArrayList<AppBean> importExcelFileToDatabase(String fileUploadFileName, File fileUpload, String path)
			throws Exception {

		String name, lstName, gender, cast, address, acaYear, course, branch, emailAddress;

		Long enrollNo = null, mobileNumPri, MobileNumSec, admYear;
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

			int noOfColumns = hssfSheet.getRow(row.getRowNum()).getPhysicalNumberOfCells();
			log.info("::::::row number::::: " + row.getRowNum() + " Column numbers ::" + noOfColumns);

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
				enrollNo = (long) r.getNumericCellValue();
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
			mobileNumPri = (long) r.getNumericCellValue();

			r = row.getCell(7);
			MobileNumSec = (long) r.getNumericCellValue();

			r = row.getCell(8);
			admssionYear = r.getStringCellValue();

			r = row.getCell(9);
			course = r.getStringCellValue();

			r = row.getCell(10);

			try {
				branch = r.getStringCellValue();
			} catch (java.lang.NullPointerException e) {

			}

			r = row.getCell(11);
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

			appBean.setAplMobilePri(mobileNumPri.toString());
			appBean.setAplMobileSec(MobileNumSec.toString());

			/* appBean.setYear(admYear.toString()); */

			// appBean.setYear(admssionYear.toString());

			/*
			 * if (cast.contentEquals("A")) {
			 * 
			 * appBean.setCategory("Open / E.B.C Category");
			 * 
			 * } else if (cast.contentEquals("B")) {
			 * 
			 * appBean.setCategory("OBC / ESBC (Maratha) Category");
			 * 
			 * } else if (cast.contentEquals("C")) {
			 * 
			 * appBean.setCategory("SC / ST / DT / VJ / NT / SBC Category");
			 * 
			 * }
			 */

			/*
			 * try { appBean.setEnrollmentNumber(enrollNo.toString()); if
			 * (appBean.getEnrollmentNumber().equals("null") ||
			 * appBean.getEnrollmentNumber().equals(null) ||
			 * appBean.getEnrollmentNumber().equals("")) {
			 * GenerateEnrollmentNumber en = new GenerateEnrollmentNumber();
			 * String EnrollNo = en.generateEnrollmentNumber(year, yearCode,
			 * course); appBean.setEnrollmentNumber(EnrollNo); } } catch
			 * (java.lang.NullPointerException e) { GenerateEnrollmentNumber en
			 * = new GenerateEnrollmentNumber(); String EnrollNo =
			 * en.generateEnrollmentNumber(year, yearCode, course);
			 * appBean.setEnrollmentNumber(EnrollNo); }
			 */

			appBean.setAplEmail(emailAddress);

			// appBeansList.add(appBean);

			// addBulkData(appBean);

		}

		return null;

	}

	// ---------------------------------------------------

	// to save record into Database
	public ArrayList<AppBean> addBulkData(AppBean appBean) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {

		HttpServletRequest request = ServletActionContext.getRequest();

		// generating enrollment Number
		GenerateEnrollmentNumber en = new GenerateEnrollmentNumber();
		String EnrollNo = en.generateEnrollmentNum(appBean);
		appBean.setEnrollmentNumber(EnrollNo);

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
			PasswordEncryption.encrypt(String.valueOf(appBean.getStartYear().substring(0, 4)));
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
			getDuesDetail(appBean);
			try {

				if (appBean.getAplMobilePri().equals("") || appBean.getAplMobilePri().equals("null")
						|| appBean.getAplMobilePri().equals(null)) {

				} else {
					String user = appBean.getEnrollmentNumber();
					String pass = appBean.getStartYear().substring(0, 4);
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
							appBean.getStartYear().substring(0, 4),
							appBean.getAplFirstName().concat(" ").concat(appBean.getAplLstName()));

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

	public AppBean getQuickPayStudentOpDues(String enrollmentNumber) {
		log.info("Enroll  ::" + enrollmentNumber);
		Session session = factory.openSession();
		try {

			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.add(Restrictions.eq("enrollmentNumber", enrollmentNumber));
			AppBean appBean = (AppBean) criteria.list().iterator().next();

			return appBean;
		} finally {
			session.close();
		}
	}

}
