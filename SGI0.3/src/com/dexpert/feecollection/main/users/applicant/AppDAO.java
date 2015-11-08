package com.dexpert.feecollection.main.users.applicant;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.communication.sms.SendSMS;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.config.FcBean;
import com.dexpert.feecollection.main.fee.config.FcDAO;
import com.dexpert.feecollection.main.fee.config.FeeDetailsBean;
import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.fee.lookup.LookupDAO;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvDAO;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class AppDAO {

	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(AppDAO.class.getName());
	AffDAO aff = new AffDAO();
	AffDAO affDAO = new AffDAO();
	FcDAO fcDAO = new FcDAO();
	FvDAO fvDAO = new FvDAO();
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpSession httpSession = request.getSession();
	LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");
	// String instName = lgBean.getAffBean().getInstName();
	// Integer instId = lgBean.getAffBean().getInstId();

	String tempTableName = "";

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

	/* To insert or update applicant detail from UI form */
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

		// BreakString bs = new BreakString();
		// String k = bs.breakString(pp);

		// log.info("String after Break Class : " + k);
		// String year = bs.getYear(k);
		// String course = bs.getCourse(k);

		// appBean.setYear(year);
		// appBean.setCourse(course);
		// appBean.setYearCode(yearCode);

		// generating enrollment Number
		GenerateEnrollmentNumber en = new GenerateEnrollmentNumber();
		String EnrollNo = en.generateEnrollmentNum(appBean);
		appBean.setEnrollmentNumber(EnrollNo);

		// log.info("Enrollment Number is ::" + appBean.getEnrollmentNumber());
		LoginBean loginBean = new LoginBean();
		loginBean.setUserName(appBean.getEnrollmentNumber());

		// to Encrypt Password
		PasswordEncryption.encrypt(String.valueOf(appBean.getStartYear()));
		String encryptedPwd = PasswordEncryption.encStr;

		loginBean.setPassword(encryptedPwd);
		loginBean.setProfile("Student");
		appBean.setLoginBean(loginBean);
		loginBean.setAppBean(appBean);
		appBean.setAffBeanStu(affBean);
		// LoginBean lgBean = (LoginBean)
		// httpSession.getAttribute("loginUserBean");
		// one to many Relationship
		// affBean.getAplBeanSet().add(appBean);
		try {
			Transaction tx = session.beginTransaction();
			session.save(appBean);
			tx.commit();
			String instName = lgBean.getAffBean().getInstName();
			// for text msg

			try {

				if (appBean.getAplMobilePri().equals("") || appBean.getAplMobilePri().equals("null")
						|| appBean.getAplMobilePri().equals(null)) {

				} else {

					String user = appBean.getEnrollmentNumber();
					String pass = appBean.getStartYear();
					String msg = "Welcome to the FeeDesk portal of " + instName
							+ ". You can log in to view and pay your fees with the these credentials. UserId :" + user
							+ "" + " Passsword : " + pass;
					SendSMS sms = new SendSMS();
					sms.sendSMS(appBean.getAplMobilePri(), msg);

				}
			} catch (java.lang.NullPointerException e) {
				e.printStackTrace();
			}

			// for email
			try {

				if (appBean.getAplEmail().equals("") || appBean.getAplEmail().equals("null")
						|| appBean.getAplEmail().equals(null)) {

				} else {

					EmailSessionBean email = new EmailSessionBean();

					String emailContent = "Welcome to the FeeDesk portal of " + instName
							+ ". You can log in to view and pay your fees with the below credentials. ";
					email.sendEmail(appBean.getAplEmail(), "Welcome To FeeDesk!", appBean.getEnrollmentNumber(),
							appBean.getStartYear(),
							appBean.getAplFirstName().concat(" ").concat(appBean.getAplLstName()), emailContent);

				}
			} catch (java.lang.NullPointerException e) {
				e.printStackTrace();
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
		AffBean instbean = affDao.getOneCollegeRecord(instId);
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
			// log.info("fee name " + feeDetailsBean.getFeeName());
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
			session.flush();
			session.clear();
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

	public List<AppBean> getStudentDetail(Integer instId) {
		Session session = factory.openSession();
		try {
			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.add(Restrictions.eq("affBeanStu.instId", instId));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<AppBean> appList = criteria.list();
			return appList;
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
		AppBean appBean = new AppBean();
		try {

			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.add(Restrictions.eq("enrollmentNumber", EnrId));

			try {
				appBean = (AppBean) criteria.list().iterator().next();

			} catch (java.util.NoSuchElementException e) {

			}

		} finally {
			session.close();
			// TODO: handle exception
		}
		return appBean;

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

	public FvBean checkFeeValue(Integer lookupId, String element) {

		Session session = factory.openSession();
		// FvBean bean = new FvBean();
		List<FvBean> list = new ArrayList<FvBean>();

		element = element.replaceAll("\\u00A0", "");
		// element = element.replaceAll("(^\\h*)|(\\h*$)", "");
		element = element.trim();
		try {
			Criteria criteria = session.createCriteria(FvBean.class);
			criteria.add(Restrictions.eq("value", element));
			criteria.add(Restrictions.eq("lookupname.lookupId", lookupId));
			list = criteria.list();
			log.info("Cell Value is ::" + element + " <<>> " + lookupId);
			log.info("found matches in the list..=" +list.size());
			if (list.size() > 0) {
				// LookupBean lookupBean = new LookupBean();
				log.info("Matched");
				Iterator<FvBean> iterator = list.iterator();
				FvBean fvBean = iterator.next();
				/*
				 * bean.setFeeValueId(fvBean.getFeeValueId());
				 * bean.setValue(fvBean.getValue());
				 * lookupBean.setLookupId(lookupId);
				 * bean.setLookupname(lookupBean);
				 */
				return fvBean;
			}
		} finally {
			session.close();
		}
		return null;

	}

	public void generateTempTable(File fileUpload) throws Exception {

		File excelFile = fileUpload;

		FileInputStream fileInputStream = new FileInputStream(excelFile);

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);

		XSSFSheet hssfSheet = xssfWorkbook.getSheetAt(0);
		Session session = factory.openSession();

		try {
			Iterator<Row> rows = hssfSheet.rowIterator();
			XSSFCell cell;
			ArrayList<String> columnList = new ArrayList<String>();
			while (rows.hasNext()) {
				XSSFRow row = (XSSFRow) rows.next();
				Iterator<Cell> cells = row.cellIterator();

				while (cells.hasNext()) {
					cell = (XSSFCell) cells.next();
					if (row.getRowNum() == 0) {

						// to get header name of each column of excel file to
						// create column name in table
						columnList.add(cell.getStringCellValue());
						continue;

					}

				}

			}

			List<String> useList = new ArrayList<String>();

			columnList.add("IsProcessed");
			columnList.add("Status");
			String dynSQL = new String();
			String t;
			Iterator<String> iterator = columnList.iterator();

			while (iterator.hasNext()) {
				String string2 = (String) iterator.next();

				string2 = string2.replaceAll("\\s", "_");
				// creating parameter with its data types
				dynSQL = dynSQL + "," + string2 + " varchar(255)";

			}

			String[] splitsqlString = dynSQL.split(",");
			for (int i = 0; i < splitsqlString.length; i++) {
				String string2 = splitsqlString[i];
				useList.add(string2);

			}

			tempTableName = "temp_imported_data" + generateReturnRandomNumber();
			log.info("Temp Table Name is "+tempTableName);
			dynSQL = dynSQL.substring(1, dynSQL.length());
			session.createSQLQuery(
					"CREATE TABLE " + tempTableName + " (id int(5) NOT NULL PRIMARY KEY AUTO_INCREMENT," + dynSQL
							+ " )").executeUpdate();

			importExcelFileToDatabase2(excelFile, useList);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<AppBean> importExcelFileToDatabase2(File fileUpload, List<String> colList) throws Exception {
		Session session = factory.openSession();

		FileInputStream fileInputStream = new FileInputStream(fileUpload);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet hssfSheet = xssfWorkbook.getSheetAt(0);

		try {

			Iterator<Row> rows = hssfSheet.rowIterator();
			String stringVal;
			String blankVal;

			Long numVal;
			XSSFCell cell;

			while (rows.hasNext()) {
				XSSFRow row = (XSSFRow) rows.next();

				if (row.getRowNum() == 0) {
					continue;
				}
				Iterator<Cell> cells = row.cellIterator();
				ArrayList<Object> tempArrayList = new ArrayList<Object>();
				while (cells.hasNext()) {

					cell = (XSSFCell) cells.next();

					switch (cell.getCellType()) {

					case XSSFCell.CELL_TYPE_STRING:
						stringVal = cell.getStringCellValue();

						stringVal = stringVal.replaceAll("\\u00A0", "");

						tempArrayList.add(stringVal.trim());
						break;

					case XSSFCell.CELL_TYPE_NUMERIC:
						numVal = (long) cell.getNumericCellValue();
						String tempSt = numVal.toString().replaceAll("\\u00A0", "");

						tempArrayList.add(tempSt.trim());
						break;

					case XSSFCell.CELL_TYPE_BLANK:
						blankVal = cell.getStringCellValue();
						blankVal.toString().replaceAll("\\u00A0", "");
						tempArrayList.add(blankVal.trim());
						break;

					}

				}
				String insertParam = new String();

				Iterator<Object> iterator = tempArrayList.iterator();

				while (iterator.hasNext()) {
					String ss = (String) iterator.next();

					insertParam = insertParam + "," + "'" + ss + "'";
				}
				Transaction tx = session.beginTransaction();
				insertParam = insertParam.substring(1, insertParam.length());
				String sql = "INSERT INTO " + tempTableName + " VALUES(null," + insertParam + ",'N',null )";
				SQLQuery sqlQuery = session.createSQLQuery(sql);

				sqlQuery.executeUpdate();
				tx.commit();
				// log.info("insert string ::" + insertParam);

			}
			Statement stmt = null;
			ResultSet rs = null;
			Connection conn;
			// SessionImpl sessionImpl = (SessionImpl) session;

			Class.forName("com.mysql.jdbc.Driver");
			// String JDBC_DRIVER = "com.mysql.jdbc.Driver";
			String DB_URL = "jdbc:mysql://localhost/sgi";

			// Database credentials
			String USER = "root";
			String PASS = "Dspl_2014";
			conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);

			boolean areMoreRecords = true;
			int g = 0;

			try {

				do {

					try {

						stmt = (Statement) conn.createStatement();

						String mysql1 = "SELECT * FROM " + tempTableName + " where IsProcessed = 'N' Limit 1";

						rs = stmt.executeQuery(mysql1);

						if (rs.next()) {
							// log.info("got another row with id equal to.." +
							// rs.getInt("id"));
							g = rs.getInt("id");

							// log.info("reading row");
							int x = colList.size();
							ArrayList<String> dbParameterList = new ArrayList<String>();
							Map<String, String> metaDataAndDataMap = new LinkedHashMap<String, String>();
							for (int i = 0; i < x - 2; i++) {

								String object = rs.getString(i + 1);
								// log.info(">>>>" + object);
								ResultSetMetaData rsmd = rs.getMetaData();
								String columnName = rsmd.getColumnName(i + 1);
								metaDataAndDataMap.put(columnName, object);
								
								dbParameterList.add(object);

							}

							// log.info("excecuting method validateLookupValues");
							validateLookupValues(dbParameterList,metaDataAndDataMap);

							stmt.close();
							rs.close();
							// log.info("current id of the record being processed is.."
							// + g);
							Statement stmt1 = (Statement) conn.createStatement();

							String mysql2 = "UPDATE " + tempTableName
									+ " set  IsProcessed = 'Y', status='PROCESSED' where id=" + g + "";
							int rs1 = 0;
							rs1 = stmt1.executeUpdate(mysql2);
							stmt1.close();

						}

						else {
							// log.info("exhausted, exiting...");
							areMoreRecords = false;
						}

					} catch (Exception e) {

						e.printStackTrace();
					}
				} while (areMoreRecords);
			} catch (Exception e) {
				e.printStackTrace();
			}

			finally {
				Statement stmt2 = (Statement) conn.createStatement();

				String dropTempTableSQL = "DROP table " + tempTableName;
				int rs2 = 0;
				rs2 = stmt2.executeUpdate(dropTempTableSQL);
				// log.info("dropped temp table" + tempTableName);
				stmt2.close();
				conn.close();
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

		}

		return null;

	}

	
	public void validateLookupValues(List<String> studentAllParamList) throws Exception {
		LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		log.info("<<<<<<<<<<<<<<<<<<<<<<<  validateLookupValues method >>>>>>>>>>>>>>>>>>>>>>>");
		Integer instId = lgBean.getAffBean().getInstId();
		log.info("INFO:inside the validateLookupValues method() ");
		List<Integer> str_ids = affDAO.getStrutureId(instId, null);
		log.info("DATA::All Structure Ides Of Institute=" + str_ids);
		List<Integer> valueList = fcDAO.getLookupValue(str_ids);
		log.info("DATA::All Look Up Value Ides=" + valueList);
		List<Integer> paramList = fvDAO.getListOfValueBeans(valueList);
		log.info("DATA::All Look Up Ides=" + paramList);
		try {

			List<FvBean> fvBeansList = new ArrayList<FvBean>();

			// log.info("Look up Dynamic Parameter size is ::" +
			// paramList.size());
			// log.info("Student parameter  size is ::" +
			// studentAllParamList.size());

			Iterator<String> studentIterator = studentAllParamList.iterator();
			// log.info(studentAllParamList);
			// log.info(paramList);
			AppBean appBean = new AppBean();
			while (studentIterator.hasNext()) {
				String object = studentIterator.next();

				appBean.setGrNumber(studentAllParamList.get(1));
				appBean.setAplFirstName(studentAllParamList.get(2));
				appBean.setAplLstName(studentAllParamList.get(3));
				appBean.setGender(studentAllParamList.get(4));
				appBean.setAplAddress(studentAllParamList.get(5));
				appBean.setAplMobilePri(studentAllParamList.get(6));
				appBean.setAplMobileSec(studentAllParamList.get(7));
				appBean.setAplEmail(studentAllParamList.get(8));
				appBean.setStartYear(studentAllParamList.get(9));

				Iterator<Integer> paramIterator = paramList.iterator();

				while (paramIterator.hasNext()) {

					FvBean bean = new FvBean();
					String tempString = object.toString().trim();
					object = object.toString().replaceAll("\\u00A0", "").trim().replaceAll("\\s", "");

					Integer lookupId = (Integer) paramIterator.next();
					log.info("DATA::Look Up Id Inside Iterator="+lookupId);

					String x = tempString.contains(".") ? tempString.substring(0, tempString.indexOf(".")) : tempString; //
					// log.info("look up id is  :: " + lookupId + " <<>> " +
					// object);
					log.info("lookup value id ="+lookupId);
					log.info("DATA: x = "+x);
					bean = checkFeeValue(lookupId, x);
                    log.info("DATA::FvBean Object Return By checkFeeValue="+bean);
					if (bean != null) {
						fvBeansList.add(bean);

					}

				}

			}
			log.info("*****************  adding to bulk data ***************");

			Set<FvBean> paramSet = new HashSet<FvBean>(fvBeansList);
			appBean.setApplicantParamValues(paramSet);
			addBulkData(appBean);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	//--------VAlidate Values With Map-----------
	public void validateLookupValues(List<String> studentAllParamList,Map<String, String> metaDataAndDataMap) throws Exception {
		LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		log.info("<<<<<<<<<<<<<<<<<<<<<<<  validateLookupValues method >>>>>>>>>>>>>>>>>>>>>>>");
		Integer instId = lgBean.getAffBean().getInstId();
		log.info("INFO:inside the validateLookupValues method() ");
		List<Integer> str_ids = affDAO.getStrutureId(instId, null);
		log.info("DATA::All Structure Ides Of Institute=" + str_ids);
		List<Integer> valueList = fcDAO.getLookupValue(str_ids);
		log.info("DATA::All Look Up Value Ides=" + valueList);
		List<Integer> paramList = fvDAO.getListOfValueBeans(valueList);
		log.info("DATA::All Look Up Ides=" + paramList);
		try {
			List<String>mapKeys=new ArrayList<String>(metaDataAndDataMap.keySet());
			log.info("Keys of MEta Map Are "+mapKeys.toString());
			List<FvBean> fvBeansList = new ArrayList<FvBean>();

			// log.info("Look up Dynamic Parameter size is ::" +
			// paramList.size());
			// log.info("Student parameter  size is ::" +
			// studentAllParamList.size());

			Iterator<String> studentIterator = studentAllParamList.iterator();
			// log.info(studentAllParamList);
			// log.info(paramList);
			AppBean appBean = new AppBean();
			while (studentIterator.hasNext()) {
				String object = studentIterator.next();

				appBean.setGrNumber(metaDataAndDataMap.get("GR_Number"));
				appBean.setAplFirstName(metaDataAndDataMap.get("First_Name"));
				appBean.setAplLstName(metaDataAndDataMap.get("Last_Name"));
				appBean.setGender(metaDataAndDataMap.get("Gender"));
				appBean.setAplAddress(metaDataAndDataMap.get("Address"));
				appBean.setAplMobilePri(metaDataAndDataMap.get("Primary_Mobile"));
				appBean.setAplMobileSec(metaDataAndDataMap.get("Secondary_Mobile"));
				appBean.setAplEmail(metaDataAndDataMap.get("Email_Id"));
				appBean.setStartYear(metaDataAndDataMap.get("Start_Year"));
				
				for (int i = 10; i < mapKeys.size(); i++) {
					String columnName=mapKeys.get(i);
					
					log.info("Got Column "+columnName);
					
					String columnValue=metaDataAndDataMap.get(columnName);
					
					log.info("Got Column Value "+columnValue);
					LookupDAO lkDao=new LookupDAO();
					columnName=columnName.replace("_", " ");
					LookupBean parameterBean=lkDao.getLookupData("Name", columnName, null, null).get(0);
					ArrayList<FvBean>paramValueList=new ArrayList<FvBean>(parameterBean.getFvBeansList());
					Iterator<FvBean>fvIt=paramValueList.iterator();
					while(fvIt.hasNext())
					{
						FvBean temp=fvIt.next();
						if(parameterBean.getLookupType().contentEquals("Boolean"))
						{
							if(columnValue.equalsIgnoreCase("YES")||columnValue.equalsIgnoreCase("TRUE"))
							{
								columnValue="1";
							}
							else if(columnValue.equalsIgnoreCase("NO")||columnValue.equalsIgnoreCase("FALSE"))
							{
								columnValue="0";
							}
							
						}
						if(temp.getValue().contentEquals(columnValue))
						{
							log.info("Found Match!! Value "+columnValue+" belongs to "+columnName);
							fvBeansList.add(temp);
						}
					}
					
				}
				
				
				/*appBean.setGrNumber(studentAllParamList.get(1));
				appBean.setAplFirstName(studentAllParamList.get(2));
				appBean.setAplLstName(studentAllParamList.get(3));
				appBean.setGender(studentAllParamList.get(4));
				appBean.setAplAddress(studentAllParamList.get(5));
				appBean.setAplMobilePri(studentAllParamList.get(6));
				appBean.setAplMobileSec(studentAllParamList.get(7));
				appBean.setAplEmail(studentAllParamList.get(8));
				appBean.setStartYear(studentAllParamList.get(9));*/

				//Iterator<Integer> paramIterator = paramList.iterator();

				/*while (paramIterator.hasNext()) {

					FvBean bean = new FvBean();
					String tempString = object.toString().trim();
					object = object.toString().replaceAll("\\u00A0", "").trim().replaceAll("\\s", "");

					Integer lookupId = (Integer) paramIterator.next();
					log.info("DATA::Look Up Id Inside Iterator="+lookupId);

					String x = tempString.contains(".") ? tempString.substring(0, tempString.indexOf(".")) : tempString; //
					// log.info("look up id is  :: " + lookupId + " <<>> " +
					// object);
					log.info("lookup value id ="+lookupId);
					log.info("DATA: x = "+x);
					bean = checkFeeValue(lookupId, x);
                    log.info("DATA::FvBean Object Return By checkFeeValue="+bean);
					if (bean != null) {
						fvBeansList.add(bean);

					}

				}*/

			}
			log.info("*****************  adding to bulk data ***************");

			Set<FvBean> paramSet = new HashSet<FvBean>(fvBeansList);
			appBean.setApplicantParamValues(paramSet);
			addBulkData(appBean);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ---------------------------------------------------

	// to save record into Database
	public void addBulkData(AppBean appBean) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {

		Integer instId = lgBean.getAffBean().getInstId();
		// generating enrollment Number
		GenerateEnrollmentNumber en = new GenerateEnrollmentNumber();
		String EnrollNo = en.generateEnrollmentNum(appBean);
		appBean.setEnrollmentNumber(EnrollNo);

		List<AppBean> studentFromDBList = getStudentDetailByEnrollMentNumber(appBean.getEnrollmentNumber());

		// if (studentFromDBList.isEmpty()) {

		Session session = factory.openSession();
		// log.info("44");
		AffBean affBean = new AffBean();
		AffBean clgBean = new AffBean();
		if (lgBean.getProfile().contentEquals("CollegeOperator")) {
			// log.info("5");
			clgBean = aff.viewInstDetail(instId);

		} else if (lgBean.getProfile().contentEquals("Institute")) {
			// log.info("66");
			clgBean = aff.viewInstDetail(instId);
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
		AffBean affBean2 = new AffBean();
		affBean2.setInstId(affBean.getInstId());
		// affBean.setAppBean(appBean);
		appBean.setAffBeanStu(affBean2);

		// one to many Relationship
		// affBean.getAplBeanSet().add(appBean);

		Transaction tx = session.beginTransaction();
		session.merge(appBean);
		tx.commit();

		// to get Dues of Applicant
		getDuesDetail(appBean);

		try {

			if (appBean.getAplMobilePri().equals("") || appBean.getAplMobilePri().equals("null")
					|| appBean.getAplMobilePri().equals(null)) {

			} else {
				String user = appBean.getEnrollmentNumber();
				String pass = appBean.getStartYear().substring(0, 4);
				String msg = "Hello " + appBean.getAplFirstName() + "Welcome to the FeeDesk portal of "
						+ lgBean.getAffBean().getInstName()
						+ ". You can log in to view and pay your fees with the these credentials. UserId :" + user + ""
						+ " Passsword : " + pass;
				SendSMS sms = new SendSMS();
				sms.sendSMS(appBean.getAplMobilePri(), msg);

			}
		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();

		}

		try {

			if (appBean.getAplEmail().equals("") || appBean.getAplEmail().equals("null")
					|| appBean.getAplEmail().equals(null)) {

			} else {

				String emailContent = "Welcome to the FeeDesk portal of " + lgBean.getAffBean().getInstName()
						+ ". You can log in to view and pay your fees with the below credentials. ";
				EmailSessionBean email = new EmailSessionBean();
				email.sendEmail(appBean.getAplEmail(), "Welcome To FeeDesk!", appBean.getEnrollmentNumber(), appBean
						.getStartYear().substring(0, 4),
						appBean.getAplFirstName().concat(" ").concat(appBean.getAplLstName()), emailContent);

			}
		} catch (java.lang.NullPointerException e) {
			e.printStackTrace();
		}

		session.close();

		// }

	}

	/*
	 * public void updateStudentDue(AppBean appBean) {
	 * 
	 * List<Integer> feeIdes = new ArrayList<Integer>();
	 * 
	 * log.info("Course Name is ::" + appBean.getCourse()); String
	 * applicableFeeString =
	 * getApplicableFeesString(appBean.getCourse().trim()); FcDAO fcDAO = new
	 * FcDAO(); FvDAO fvDAO = new FvDAO(); String applicableFeeIdArray[] =
	 * applicableFeeString.split("~"); for (String string :
	 * applicableFeeIdArray) { feeIdes.add(Integer.parseInt(string)); }
	 * Iterator<Integer> itr = feeIdes.iterator(); while (itr.hasNext()) {
	 * Integer feeId = itr.next(); String feeName = fcDAO.getFeeName(feeId);
	 * Integer categoryId = fvDAO.findFeeValueId(appBean.getCategory(), feeId);
	 * Integer courseId = fvDAO.findFeeValueId(appBean.getCourse(), feeId);
	 * Double fee = fcDAO.calculateFeeStudent(categoryId, courseId, null,
	 * feeId); updateDueAmountOfStudent(appBean, feeId, fee, feeName); }
	 * 
	 * }
	 */

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
		try {
			Criteria criteria = session.createCriteria(AppBean.class);
			criteria.add(Restrictions.eq("enrollmentNumber", enrollmentNumber));
			AppBean applicantDetails = (AppBean) criteria.list().iterator().next();
			applicantDetails.getPaymentDues().add(dueBean);
			Transaction transaction = session.beginTransaction();
			session.save(applicantDetails);
			transaction.commit();
		} finally {
			session.close();
		}

	}

	public AppBean getStudentDues(String enrollmentNumber) {
		Session session = factory.openSession();
		try {
			AppBean completeDeatilsOfStudent = (AppBean) session.get(AppBean.class, enrollmentNumber);
			return completeDeatilsOfStudent;
		} finally {
			session.close();
		}

	}

	public Double totalDueFeeOfStudent(String enrollMentNumber) {
		Session session = factory.openSession();
		try {
			String query = "SELECT  Sum(netDue) FROM sgi.fee_dues_master where enrollmentNumber_Fk=:enrollmentNumber";
			SQLQuery sqlQuery = session.createSQLQuery(query);
			sqlQuery.setParameter("enrollmentNumber", enrollMentNumber);
			Double totalAmount = (Double) sqlQuery.list().iterator().next();
			log.info("Total fees ::" + totalAmount);
			return totalAmount;
		} finally {
			session.close();
		}
	}

	public List<FeeDetailsBean> getAllFeeDeatils() {
		Session session = factory.openSession();
		try {
			List<FeeDetailsBean> feeList = session.createCriteria(FeeDetailsBean.class).list();
			return feeList;
		} finally {
			session.close();
		}

	}

	public List<TransactionBean> getTransactionDetailsOfStudent(String enrollmentNumber) {
		Session session = factory.openSession();
		try {
			Criteria criteria = session.createCriteria(TransactionBean.class);
			criteria.add(Restrictions.eq("studentEnrollmentNumber", enrollmentNumber));
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.addOrder(Order.desc("transDate"));
			List<TransactionBean> tranDetOfStu = criteria.list();
			return tranDetOfStu;
		} finally {
			session.close();
		}

	}

	/*
	 * public String getApplicableFeesString(String courseName) { Session
	 * session = factory.openSession(); Criteria criteria =
	 * session.createCriteria(ApplicableFeesBean.class);
	 * criteria.add(Restrictions.eq("courseName",
	 * courseName)).setProjection(Projections.property("applicableFeeId"));
	 * String applicableFee = (String) criteria.list().iterator().next();
	 * session.close(); return applicableFee;
	 * 
	 * }
	 */
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

	public int generateReturnRandomNumber() {
		Random r = new Random(System.currentTimeMillis());
		return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
	}

}
