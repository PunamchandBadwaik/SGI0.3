package com.dexpert.feecollection.main.users.affiliated;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javassist.bytecode.stackmap.BasicBlock.Catch;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import COM.rsa.Intel.cr;

import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.config.FeeStructureData;
import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.RandomPasswordGenerator;
import com.dexpert.feecollection.main.users.applicant.AppBean;
import com.dexpert.feecollection.main.users.parent.ParBean;
import com.dexpert.feecollection.main.users.parent.ParDAO;
import com.mchange.v2.codegen.bean.Property;

public class AffDAO {

	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(AffDAO.class.getName());
	static Boolean isExist = false;
	ParDAO parDAO = new ParDAO();
	boolean isInserted = true;
	HttpSession httpSession = ServletActionContext.getRequest().getSession();

	// static ArrayList<AffBean> existingCollegeList = new ArrayList<AffBean>();

	// End of Global Variables

	// ---------------------------------------------------

	// DAO Methods Here

	// saveOrUpdate()
	@SuppressWarnings("resource")
	public AffBean saveOrUpdate(AffBean affInstBean, String path) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {

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
				File dstfile = new File(path, affInstBean.getFileUploadFileName());

				// to copy files at specified destination path
				FileUtils.copyFile(affInstBean.getFileUpload(), dstfile);

				// convert file into array of bytes
				bFile = new byte[(int) dstfile.length()];
				fileInputStream = new FileInputStream(dstfile);

				fileSize = fileInputStream.read(bFile);

				// fileinputStream must be close
				fileInputStream.close();
				affInstBean.setFilesByteSize(bFile);

				affInstBean.setFileSize(fileSize);

			} catch (java.lang.NullPointerException e) {
				isInserted = false;

			}
			session.beginTransaction();

			session.saveOrUpdate(affInstBean);

			session.getTransaction().commit();

			return affInstBean;

		} catch (Exception e) {

			e.printStackTrace();
			return affInstBean;
		} finally {

			// close session
			session.close();

		}

	}

	public ArrayList<AffBean> getInstitutes(String filterKey, String filterVal, ArrayList<Integer> idList,
			Date fromDate, Date toDate) {
		// Declarations
		ArrayList<AffBean> instList = new ArrayList<AffBean>();
		// Open session from session factory
		Session session = factory.openSession();
		try {
			if (filterKey.contentEquals("Relevant")) {

				if (httpSession.getAttribute("sesProfile").toString().contentEquals("Parent")) {
					ParBean parent = new ParBean();
					Integer parId = Integer.parseInt(httpSession.getAttribute("sesId").toString());
					ParDAO prdao = new ParDAO();
					parent = prdao.viewUniversity(parId);
					instList = new ArrayList<AffBean>(parent.getAffBeanOneToManySet());
					return instList;
				}
			}
			Criteria InstSearchCr = session.createCriteria(AffBean.class);
			if (filterKey.contentEquals("NONE")) {
				log.info("Showing All Affiliated Institutes");
			}
			Iterator<AffBean> instIter = InstSearchCr.list().iterator();
			while (instIter.hasNext()) {
				instList.add(instIter.next());
			}

			return instList;
		} finally {
			// close session
			session.close();
		}

	}

	// delete()
	// getList()

	public  Integer getRowCount() {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			List<Integer> instList = new ArrayList<Integer>();

			Criteria c = session.createCriteria(AffBean.class);
			c.setProjection(Projections.property("instId"));
			instList = c.list();

			//System.out.println(instList.size());
			//System.out.println(instList.size() + 1);

			return instList.size() + 1;

			/*
			 * Criteria c = session.createCriteria(AffBean.class);
			 * c.addOrder(Order.desc("instId")); c.setMaxResults(1); AffBean
			 * temp = (AffBean) c.uniqueResult(); return temp.getInstId() + 1;
			 */

		} finally {
			// close session
			session.close();
		}

	}
	public List<AffBean> getAllCollegeList() {

		Session session = factory.openSession();
		List<AffBean> affBeansList = new ArrayList<AffBean>();
		Criteria criteria = session.createCriteria(AffBean.class);

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		affBeansList = criteria.list();

		return affBeansList;
	}

	// get direct child i.e. college list
	public List<AffBean> getCollegesList(Integer parentInsId) {

		Session session = factory.openSession();
		List<AffBean> affBeansList = new ArrayList<AffBean>();

		// LoginBean loginBean = (LoginBean)
		// httpSession.getAttribute("loginUserBean");
		Criteria criteria = session.createCriteria(AffBean.class);
		criteria.add(Restrictions.eq("parBeanAff.parInstId", parentInsId));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		affBeansList = criteria.list();

		/*
		 * HttpSession httpSession = request.getSession(); String profile =
		 * (String) httpSession.getAttribute("sesProfile"); Criteria criteria =
		 * session.createCriteria(AffBean.class); Integer parInstId = null;
		 * List<AffBean> affBeansList=new ArrayList<AffBean>(); log.info("2");
		 * if (profile.equals("Parent")) { log.info("3");
		 * criteria.add(Restrictions.eq("parBeanAff.parInstId", parInstId)); }
		 * log.info("4"); //criteria.addOrder(Order.asc("instName"));
		 * log.info("5");
		 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		 * log.info("6"); affBeansList = criteria.list(); log.info("7");
		 * session.close();
		 */
		return affBeansList;
	}

	// to get child college list based on university
	public ParBean getUniversityCollegeList(Integer parentInsId) {

		Session session = factory.openSession();

		Criteria criteria = session.createCriteria(ParBean.class);
		criteria.add(Restrictions.eq("parInstId", parentInsId));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		ParBean parBean = (ParBean) criteria.list().iterator().next();
		session.close();
		return parBean;
	}

	public ArrayList<AffBean> getCollegesListByInstName(AffBean affBean) {

		Session session = factory.openSession();

		Criteria criteria = session.createCriteria(AffBean.class);
		criteria.add(Restrictions.eq("instName", affBean.getInstName()));

		ArrayList<AffBean> affBeansList = (ArrayList<AffBean>) criteria.list();
		session.close();
		return affBeansList;
	}

	// End of DAO Methods

	public AffBean viewInstDetail(Integer instId) {
		log.info("before creating session");
		Session session = factory.openSession();
		log.info("after creating session");
		AffBean affBean = (AffBean) session.get(AffBean.class, instId);
		log.info("after fetching bean");
		session.close();
		return affBean;
	}

	public AffBean getOneCollegeRecord(Integer instId) {

		Session session = factory.openSession();

		AffBean affBean = (AffBean) session.get(AffBean.class, instId);
		session.close();
		return affBean;
	}

	public void updateCollege(AffBean newAffInstBean) {

		Session session = factory.openSession();

		try {
			Transaction tx = session.beginTransaction();
			AffBean oldBean = (AffBean) session.get(AffBean.class, newAffInstBean.getInstId());

			oldBean.setPlace(newAffInstBean.getPlace());
			oldBean.setInstName(newAffInstBean.getInstName());
			oldBean.setContactPerson(newAffInstBean.getContactPerson());
			oldBean.setContactNumber(newAffInstBean.getContactNumber());
			oldBean.setMobileNum(newAffInstBean.getMobileNum());
			oldBean.setEmail(newAffInstBean.getEmail());
			oldBean.setInstAddress(newAffInstBean.getInstAddress());

			session.merge(oldBean);

			tx.commit();
		} finally {
			session.close();
		}

	}

	public void updateCollegeDoc(AffBean newAffInstBean, String path) {

		Session session = factory.openSession();
		try {
			AffBean oldBean = (AffBean) session.get(AffBean.class, newAffInstBean.getInstId());
			// file input Stream is use to save file in to DataBase

			FileInputStream fileInputStream = null;

			// to create new file with actual name with extension
			File dstfile = new File(path, newAffInstBean.getFileUploadFileName());

			// to copy files at specified destination path
			FileUtils.copyFile(newAffInstBean.getFileUpload(), dstfile);

			// convert file into array of bytes
			byte[] bFile = new byte[(int) dstfile.length()];
			fileInputStream = new FileInputStream(dstfile);

			int fileSize = fileInputStream.read(bFile);

			// fileinputStream must be close
			fileInputStream.close();

			newAffInstBean.setFilesByteSize(bFile);
			newAffInstBean.setFileSize(fileSize);
			session.beginTransaction();

			oldBean.setFilesByteSize(newAffInstBean.getFilesByteSize());
			oldBean.setFileSize(newAffInstBean.getFileSize());
			oldBean.setFileUploadFileName(newAffInstBean.getFileUploadFileName());

			session.saveOrUpdate(oldBean);
			session.getTransaction().commit();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			// close session
			session.close();

		}
	}

	// to check whether record is already exist or New
	public List<String> getCollegeNameList(String instName) {

		Session session = factory.openSession();

		Criteria criteria = session.createCriteria(AffBean.class);
		criteria.add(Restrictions.eq("instName", instName));
		criteria.setProjection(Projections.property("instName"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<String> affList = criteria.list();
		session.close();
		return affList;

	}

	// to check whether record is already exist or New
	public List<AffBean> getCollegeNameFromDB(String instName) {

		Session session = factory.openSession();

		Criteria criteria = session.createCriteria(AffBean.class);
		criteria.add(Restrictions.eq("instName", instName));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<AffBean> affList = criteria.list();
		session.close();
		return affList;
	}

	// to read excel file

	@SuppressWarnings("resource")
	public ArrayList<AffBean> importExcelFileToDatabase(String fileUploadFileName, File fileUpload, String path)
			throws Exception {
		// ArrayList<AffBean> notAddedCollegeList = new ArrayList<AffBean>();
		String instName, email, ContactPerson, instAddress, place;
		Integer contactNum, mobileNum;
		ArrayList<AffBean> affBeansList = new ArrayList<AffBean>();
		AffBean affBean = new AffBean();
		ArrayList<AffBean> totalCollegeList = new ArrayList<AffBean>();
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

				}
			}

			Cell r = row.getCell(0);
			instName = r.getStringCellValue();

			r = row.getCell(1);
			instAddress = r.getStringCellValue();

			r = row.getCell(2);
			contactNum = (int) r.getNumericCellValue();

			r = row.getCell(3);
			ContactPerson = r.getStringCellValue();

			r = row.getCell(4);
			mobileNum = (int) r.getNumericCellValue();

			r = row.getCell(5);
			email = r.getStringCellValue();

			r = row.getCell(6);
			place = r.getStringCellValue();

			affBean.setInstName(instName);
			affBean.setContactNumber(contactNum.toString());
			affBean.setMobileNum(mobileNum.toString());
			affBean.setEmail(email);
			affBean.setContactPerson(ContactPerson);
			affBean.setInstAddress(instAddress);
			affBean.setPlace(place);

			affBeansList.add(affBean);
			log.info("Parent " + affBean.getInstName());
			addBulkData(affBean);

		}

		return totalCollegeList;

	}

	// ---------------------------------------------------
	// to save record into Database
	public ArrayList<AffBean> addBulkData(AffBean affBean) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		HttpServletRequest request = ServletActionContext.getRequest();
		ArrayList<AffBean> collegeListFromDB = new ArrayList<AffBean>();
		ArrayList<AffBean> notAddedCollegeList = new ArrayList<AffBean>();
		collegeListFromDB = getCollegesListByInstName(affBean);
		HttpSession httpSession = request.getSession();
		Integer parentInsId = (Integer) httpSession.getAttribute("parentInstId");
		// LoginBean loginBean = (LoginBean)
		// httpSession.getAttribute("loginUserBean");

		String userprofile = httpSession.getAttribute("sesProfile").toString();
		log.info("CHild " + affBean.getInstName());
		if (collegeListFromDB.isEmpty()) {

			String username;

			// generate credentials for admin login
			try {
				username = "Inst".concat(affBean.getInstName().replaceAll("\\s+", "").substring(0, 4)
						.concat(getRowCount().toString()));

			} catch (java.lang.NullPointerException e) {
				username = "Inst".concat(affBean.getInstName().replaceAll("\\s+", "").substring(0, 4).concat("1"));

			}

			String password = RandomPasswordGenerator.generatePswd(6, 8, 1, 2, 0);
			PasswordEncryption.encrypt(password);
			String encryptedPwd = PasswordEncryption.encStr;

			// ------------------------------------------
			LoginBean creds = new LoginBean();
			creds.setPassword(encryptedPwd);
			creds.setUserName(username);

			String collegeProfile = "Admin";
			creds.setProfile(collegeProfile);
			affBean.setLoginBean(creds);
			creds.setProfile(collegeProfile);

			// -----------------------------------------

			// one to one Bidirectional relationship with login
			// for bidirectional relationship ,set parent record to child
			// record
			creds.setAffBean(affBean);

			// one to one relationship

			if (creds.getProfile().equals("Admin")) {

				// for bidirectional relationship ,set child record to
				// Parent
				// record
				affBean.setLoginBean(creds);

			}

			// ------------------------------
			ParBean parBean1 = new ParBean();
			// one to many Bidirectional relationship
			Set<AffBean> affBeansSet = new HashSet<AffBean>();

			parBean1 = parDAO.viewUniversity(parentInsId);
			log.info("Parent Login is :: " + parBean1.getParInstId() + " ::: " + parBean1.getParInstName());
			affBeansSet = parBean1.getAffBeanOneToManySet();

			affBeansSet.add(affBean);

			parBean1.setAffBeanOneToManySet(affBeansSet);

			parDAO.saveOrUpdate(parBean1, null);
			// ----------------------------
			String emailContent = "Welcome to the FeeDesk portal of " + affBean.getInstName()
					+ ". You can log in with the below credentials. ";
			EmailSessionBean email = new EmailSessionBean();
			email.sendEmail(affBean.getEmail(), "Welcome To Fee Collection Portal!", username, password,
					affBean.getInstName(), emailContent);
			Session session = factory.openSession();
			Transaction tx = session.beginTransaction();
			session.save(affBean);
			tx.commit();
			session.close();

		}
		return notAddedCollegeList;
	}

	public List<PaymentDuesBean> collegeDueReport() {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(PaymentDuesBean.class);
		List<PaymentDuesBean> collegeDueList = criteria.list();
		log.info("List size in DAO claSS is   " + collegeDueList.size());
		session.close();
		return collegeDueList;

	}

	public static AffBean validateTheForgetPwdDetails(String profile, String emailId) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		Session session = factory.openSession();
		try {
			AffBean bean = (AffBean) session.createCriteria(AffBean.class).add(Restrictions.eq("email", emailId))
					.list().iterator().next();
			if (bean != null) {

				LoginBean loginDetailsOfInst = (LoginBean) session.get(LoginBean.class, bean.getLoginBean()
						.getLoginId());

				// to get Random generated Password
				String password = RandomPasswordGenerator.generatePswd(6, 8, 1, 2, 0);
				log.info("Password Generated is " + password);
				log.info("User Name is " + bean.loginBean.getUserName());

				// to Encrypt Password
				PasswordEncryption.encrypt(password);
				String encryptedPwd = PasswordEncryption.encStr;

				loginDetailsOfInst.setPassword(encryptedPwd);

				Transaction tx = session.beginTransaction();
				session.saveOrUpdate(loginDetailsOfInst);

				tx.commit();
				// session.close();

				// -----Code for sending email//--------------------
				String emailContent = "Welcome to the FeeDesk portal of " + bean.getInstName()
						+ ". You can log in with the below credentials. ";
				EmailSessionBean email = new EmailSessionBean();
				email.sendEmail(bean.getEmail(), "Welcome To Fee Collection Portal!",
						bean.getLoginBean().getUserName(), password, bean.getInstName(), emailContent);

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

	public List<TransactionBean> getAllTransactionDetail(Integer insId) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(TransactionBean.class);
		criteria.add(Restrictions.eq("status", "Paid"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("transDate"));
		List<TransactionBean> transactionDetails = criteria.add(Restrictions.eq("insId", insId)).list();
		session.close();
		return transactionDetails;
	}

	public List<TransactionBean> getAllTransactionDetail() {
		Session session = factory.openSession();
		List<TransactionBean> transactionDetails = session.createCriteria(TransactionBean.class)
				.add(Restrictions.eq("status", "Paid")).list();
		session.close();
		return transactionDetails;
	}

	public List<TransactionBean> getStudentTransactionDetails(String enrollmentNumber) {

		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(TransactionBean.class);
		criteria.add(Restrictions.eq("studentEnrollmentNumber", enrollmentNumber));
		List<TransactionBean> transactionDetails = criteria.list();
		session.close();
		return transactionDetails;

	}

	public ParBean getTrasactionReportForUniversity(Integer universityId) {
		Session session = factory.openSession();
		ParBean parBean = (ParBean) session.get(ParBean.class, universityId);
		session.close();
		return parBean;

	}

	public List<TransactionBean> getTransactionOfColleges(List<Integer> ides) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(TransactionBean.class);
		criteria.add(Restrictions.eq("status", "Paid"));
		List<TransactionBean> collegesTransactionList = criteria.add(Restrictions.in("insId", ides)).list();
		session.close();
		return collegesTransactionList;
	}

	public Integer getCollegeId(String collegeName) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(AffBean.class);
		criteria.add(Restrictions.eq("instName", collegeName));
		AffBean affBean = (AffBean) criteria.list().iterator().next();
		Integer id = affBean.getInstId();
		log.info("College id in dao class" + id);
		return id;
	}

	public Integer getCollegeId(String collegeName, Integer universityId) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(AffBean.class);
		criteria.add(Restrictions.eq("parBeanAff.parInstId", universityId)).add(
				Restrictions.eq("instName", collegeName));
		AffBean affBean = (AffBean) criteria.list().iterator().next();
		Integer id = affBean.getInstId();
		log.info("College id in dao class" + id);
		session.close();
		return id;

	}

	public List<String> findAllStudentOfInstituteByCourse(Integer collegeId, String courseName, Integer courseId) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(AppBean.class);
		criteria.add(Restrictions.eq("affBeanStu.instId", collegeId));
		if (courseName != null && !courseName.isEmpty()) {
			criteria.add(Restrictions.eq("course", courseName));
		} else if (courseId != null) {
			criteria.createAlias("applicantParamValues", "av");
			criteria.add(Restrictions.eq("av.feeValueId", courseId));
		}
		criteria.setProjection(Projections.property("enrollmentNumber"));
		List<String> appBeans = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		return appBeans;
	}

	public List<Object[]> findTotalDuesOFFee(String feeName, List<String> enrollmentNumber) {
		Session session = factory.openSession();
		// String query =
		// "SELECT enrollmentNumber_Fk,sum(netDue),sum(total_fee_amount),sum(payments_to_date) FROM sgi.fee_dues_master where  enrollmentNumber_Fk in (:enrollmentNumber)  group by enrollmentNumber_Fk";
		String query = "SELECT enrollmentNumber_Fk,sum(netDue),sum(total_fee_amount),sum(payments_to_date),ad.grNumber FROM sgi.fee_dues_master fdm left join sgi.applicant_details ad on fdm.enrollmentNumber_Fk=ad.enrollmentNumber where  enrollmentNumber_Fk in (:enrollmentNumber)  group by enrollmentNumber_Fk";
		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setParameterList("enrollmentNumber", enrollmentNumber);
		List<Object[]> totalDueOfStudent = sqlQuery.list();
		session.close();
		return totalDueOfStudent;
	}

	public static void updatePersonalRecordOfInstitute(AffBean affBean) {

		Session session = factory.openSession();

		AffBean affBean2 = (AffBean) session.get(AffBean.class, affBean.getInstId());

		affBean2.setContactPerson(affBean.getContactPerson());
		affBean2.setEmail(affBean.getEmail());
		affBean2.setPlace(affBean.getPlace());
		affBean2.setInstAddress(affBean.getInstAddress());
		affBean2.setContactNumber(affBean.getContactNumber());
		affBean2.setMobileNum(affBean.getMobileNum());

		Transaction tx = session.beginTransaction();
		session.merge(affBean2);
		tx.commit();
		session.close();

	}

	public List<AffBean> getCollegList(Integer universityId) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(AffBean.class);
		if (universityId != null) {
			criteria.add(Restrictions.eq("parBeanAff.parInstId", universityId));
		}
		List<AffBean> affBeans = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		session.close();
		return affBeans;

	}

	// lagacy method
	public List<String> getListOfCourses(Integer collegeId, List<Integer> listOfCollegeId) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(AppBean.class);
		if (collegeId != null) {
			criteria.add(Restrictions.eq("affBeanStu.instId", collegeId));
		} else if (listOfCollegeId != null && listOfCollegeId.size() > 0) {
			criteria.add(Restrictions.in("affBeanStu.instId", listOfCollegeId));
		}
		criteria.setProjection(Projections.groupProperty("course")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<String> ListOfCourse = criteria.list();
		session.clear();
		return ListOfCourse;
	}

	// new method for getting cources of the
	public List<String> getCoursesOFCollege(Integer instId, List<Integer> instIdes) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(CollegeCourses.class);
		if (instId != null) {
			criteria.add(Restrictions.eq("affBean.instId", instId));
		} else if (instIdes != null && instIdes.size() > 0) {
			criteria.add(Restrictions.in("affBean.instId", instIdes));
		}
		criteria.setProjection(Projections.property("courseName"));
		List<String> listOfCourses = criteria.list();
		session.close();
		return listOfCourses;
	}

	public List<Object[]> findDueOfFees(String feeName, List<String> enrollmentNumber) {
		List<Object[]> feeDues = new ArrayList<Object[]>();
		Session session = factory.openSession();
		try {

			Criteria criteria = session.createCriteria(PaymentDuesBean.class);
			criteria.add(Restrictions.in("appBean.enrollmentNumber", enrollmentNumber));
			if (feeName != null && !feeName.isEmpty()) {
				criteria.add(Restrictions.eq("feeName", feeName));
				criteria.setProjection(Projections.projectionList().add(Projections.property("feeName"))
						.add(Projections.sum("total_fee_amount")).add(Projections.sum("payments_to_date"))
						.add(Projections.sum("netDue")));

			} else {
				criteria.setProjection(Projections.projectionList().add(Projections.property("feeName"))
						.add(Projections.sum("total_fee_amount")).add(Projections.sum("payments_to_date"))
						.add(Projections.sum("netDue")).add(Projections.groupProperty("feeName")));
			}
			feeDues = criteria.list();
			return feeDues;
		} catch (Exception ex) {
			// ex.printStackTrace();
			return feeDues;
		}

		finally {
			session.close();

		}
	}

	public List<String> findEnrollmentNumberOfMoreTheOneCollege(List<Integer> collegeIdes, String courseName) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(AppBean.class);
		criteria.add(Restrictions.in("affBeanStu.instId", collegeIdes));
		if (courseName != null && !courseName.isEmpty()) {
			criteria.add(Restrictions.eq("course", courseName));
		}
		criteria.setProjection(Projections.property("enrollmentNumber"));
		List<String> appBeans = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		return appBeans;

	}

	public AffBean getStudentList(Integer collegeId) {
		Session session = factory.openSession();
		try {

			Criteria criteria = session.createCriteria(AffBean.class);

			criteria.add(Restrictions.eq("instId", collegeId));

			AffBean affBean = (AffBean) criteria.list().iterator().next();

			return affBean;

		} finally {
			session.close();
			// TODO: handle exception
		}
	}

	public List<Integer> getStrutureId(Integer instId, Integer feeId) {
		List<Integer> structureIdes = new ArrayList<Integer>();
		Session session = factory.openSession();
		try {
			Criteria criteria = session.createCriteria(FeeStructureData.class);
			if (feeId != null) {
				criteria.add(Restrictions.eq("inst_id", instId)).add(Restrictions.eq("fee_id", feeId));
			} else {
				criteria.add(Restrictions.eq("inst_id", instId));
			}
			criteria.setProjection(Projections.property("structure_id"));
			structureIdes = criteria.list();
			log.info("struture id is" + structureIdes);
			return structureIdes;
		} catch (NoSuchElementException nse) {
			nse.printStackTrace();
			return structureIdes;
		} finally {
			session.close();
		}
	}

	public ArrayList<FeeStructureData> getInstStructures(Integer instId) {
		// Declarations
		ArrayList<FeeStructureData> resList = new ArrayList<FeeStructureData>();
		// Open session from session factory
		Session session = factory.openSession();
		try {
			Criteria cr = session.createCriteria(FeeStructureData.class);
			cr.add(Restrictions.eq("inst_id", instId));
			resList = new ArrayList<FeeStructureData>(cr.list());
			return resList;
		} catch (Exception e) {
			e.printStackTrace();
			return resList;

		} finally {
			// close session
			session.close();
		}
	}

	public List<Integer> getFeeIdesOfInst(Integer instId) {
		List<Integer> feeList = new ArrayList<Integer>();
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(FeeStructureData.class);
		if (instId != null) {
			criteria.add(Restrictions.eq("inst_id", instId));
		}
		criteria.setProjection(Projections.property("fee_id"));
		feeList = criteria.list();
		session.close();
		return feeList;
	}

	public List<Object[]> getTotalDueOfStudents(Integer collegeId) {
		List<Object[]> duesArray = null;
		DetachedCriteria dc = DetachedCriteria.forClass(AppBean.class);
		dc.add(Restrictions.eq("affBeanStu.instId", collegeId)).setProjection(Projections.property("enrollmentNumber"));
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(AffBean.class, "inst");
		try {
			criteria.createAlias("inst.aplBeanSet", "student");
			criteria.createAlias("student.paymentDues", "payDueBean");
			criteria.add(Subqueries.propertyIn("student.enrollmentNumber", dc));
			criteria.add(Subqueries.propertyIn("payDueBean.appBean.enrollmentNumber", dc));
			criteria.add(Restrictions.eq("instId", collegeId));
			criteria.setProjection(Projections.projectionList().add(Projections.property("inst.instName"))
					.add(Projections.sum("payDueBean.total_fee_amount"))
					.add(Projections.sum("payDueBean.payments_to_date")).add(Projections.sum("payDueBean.netDue")));
			duesArray = criteria.list();
			log.info("inside try");
		} catch (Exception ex) {
			log.info("inside catch");
			criteria.add(Restrictions.eq("instId", collegeId));
			criteria.setProjection(Projections.property("inst.instName"));
			String instName = (String) criteria.list().iterator().next();
			Object obj[] = new Object[4];
			obj[0] = instName;
			obj[1] = 0;
			obj[2] = 0;
			obj[3] = 0;
			duesArray = new ArrayList<Object[]>();
			duesArray.add(obj);

		} finally {
			session.close();

		}
		log.info("Total amount is" + duesArray.iterator().next()[1]);
		return duesArray;
	}

	public void saveCollegeCourses(CollegeCourses courses, Integer instId) {
		AffBean affBean = new AffBean();
		affBean.setInstId(instId);
		courses.setAffBean(affBean);
		Session session = factory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(courses);
		tx.commit();
		session.close();
	}

	public boolean courseNameAlreadySaved(String courseName, Integer instId) {
		boolean courseNameAlreadySaved = false;
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(CollegeCourses.class);
		criteria.add(Restrictions.eq("courseName", courseName)).add(Restrictions.eq("affBean.instId", instId));
		List<CollegeCourses> collegeCourses = criteria.list();
		session.close();
		courseNameAlreadySaved = collegeCourses.size() > 0 ? true : courseNameAlreadySaved;
		return courseNameAlreadySaved;
	}

	public List<CollegeCourses> getAllCourseOfInst(Integer instId) throws NullPointerException {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(CollegeCourses.class);
		criteria.add(Restrictions.eq("affBean.instId", instId)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<CollegeCourses> collegeCourses = criteria.list();
		if (collegeCourses.size() < 1)
			throw new NullPointerException();
		session.close();
		return collegeCourses;
	}

}
