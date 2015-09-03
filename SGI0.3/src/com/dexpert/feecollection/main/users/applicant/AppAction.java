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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.engine.spi.SessionDelegatorBaseImpl;

import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.communication.sms.SendSMS;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.config.FcDAO;
import com.dexpert.feecollection.main.fee.config.FeeDetailsBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvDAO;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.LoginDAO;
import com.dexpert.feecollection.main.users.affiliated.AffAction;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;
import com.dexpert.feecollection.main.users.operator.OperatorBean;
import com.dexpert.feecollection.main.users.operator.OperatorDao;
import com.opensymphony.xwork2.ActionSupport;

public class AppAction extends ActionSupport {

	// Declare Global Variables Here
	AppBean appBean1, appBean;
	List<AppBean> appBeansList = new ArrayList<AppBean>();
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	static Logger log = Logger.getLogger(AffAction.class.getName());
	AffBean affBean = new AffBean();
	String collegeName;
	OperatorDao opratorDAO = new OperatorDao();

	Integer aplInstId;
	List<AffBean> affInstList = new ArrayList<AffBean>();
	String fileFileName;
	FileInputStream inputStream;
	private File fileUpload;
	private String fileUploadFileName;
	AffDAO affDAO = new AffDAO();
	FvDAO fvDAO = new FvDAO();
	private String contentType;
	public AppDAO aplDAO = new AppDAO();
	List<FeeDetailsBean> feeDetailsBeanList = new ArrayList<FeeDetailsBean>();
	FcDAO fcDAO = new FcDAO();
	private AppBean app1;
	private Double totalDueOFStudent, totalNetFees, paymentDone;
	private List<FeeDetailsBean> feeList;
	private List<TransactionBean> transactionDetailsForReport;
	private OperatorBean operatorBean;
	private AffDAO affDao = new AffDAO();

	// End of Global Variables

	// ---------------------------------------------------

	// Action Methods Here

	public OperatorBean getOperatorBean() {
		return operatorBean;
	}

	public void setOperatorBean(OperatorBean operatorBean) {
		this.operatorBean = operatorBean;
	}

	public String getDueFees() {

		String enrollmentId = request.getParameter("EnrollmentId");
		enrollmentId = "1234";

		appBean1 = aplDAO.getUserDetail(enrollmentId);

		feeDetailsBeanList = fcDAO.getAllFeeDetail();

		Iterator<FeeDetailsBean> itr = feeDetailsBeanList.iterator();
		while (itr.hasNext()) {

			Integer feeId = itr.next().getFeeId();
			fcDAO.calculateFeeStudent(1, 2, 3, feeId);
		}
		return SUCCESS;
	}

	public String registerStudent() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		log.info("Course  ::" + appBean1.getCourse());
		log.info("Category  ::" + appBean1.getCategory());
		log.info("Admission Year  ::" + appBean1.getYear());
		if (appBean1.getCourse().equals("") || appBean1.getCourse().equals(null) || appBean1.getCourse().equals("null")) {
			request.setAttribute("msg", "Please Select Course");
			affInstList = affDAO.getCollegesList();
			return "failure";

		}
		if (appBean1.getYear().equals(null) || appBean1.getYear().equals("") || appBean1.getYear().equals("null")) {
			request.setAttribute("msg", "Please Select Admission Year");
			affInstList = affDAO.getCollegesList();
			return "failure";

		}
		if (appBean1.getCategory().equals("") || appBean1.getCategory().equals(null)
				|| appBean1.getCategory().equals("null")) {
			request.setAttribute("msg", "Please Select Category");
			affInstList = affDAO.getCollegesList();
			return "failure";

		}

		HttpSession httpSession = request.getSession();
		LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		log.info("Applicant Name  ::" + appBean1.getAplFirstName());

		List<String> existEnrollmentList = aplDAO.existingEnrollNum(appBean1);
		if (existEnrollmentList.isEmpty()) {

			if (aplInstId == null) {
				request.setAttribute("msg", "Please Select College Name");
				affInstList = affDAO.getCollegesList();
				return "failure";
			}

			try {
				appBean1 = aplDAO.saveOrUpdate(appBean1, aplInstId);
			} catch (java.lang.NullPointerException e) {
				request.setAttribute("msg", "Please Enter Enrollment Number");
				affInstList = affDAO.getCollegesList();
				return "failure";
			}

			try {
				updateStudentDue();

			} catch (java.util.NoSuchElementException e) {

				request.setAttribute("msg", "Please Set Fees for Student");
				affInstList = affDAO.getCollegesList();
				return "feeNotSet";
			}

			request.setAttribute("msg", "Student Addedd Successfully");

			// String smsUrl="http://bhashsms.com/api/sendmsg.php?";

			return SUCCESS;

		} else {
			log.info("4");
			request.setAttribute("msg", "Enrollment Number Already Registered");
			affInstList = affDAO.getCollegesList();
			return "failure";
		}

	}

	// to get list of All Students

	public String getStudentList() {
		HttpSession httpSession = request.getSession();
		LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");

		try {
			affBean = aplDAO.getStudentDetail(loginBean);

		} catch (java.lang.NullPointerException e) {
			request.setAttribute("msg", "Session Time Out");
			return ERROR;
		}

		return SUCCESS;

	}

	// to view Applicant Detail
	public String viewApplicant() {

		String apId = request.getParameter("applicantId");
		appBean1 = aplDAO.viewApplicantDetail(apId);
		return SUCCESS;
	}

	/*
	 * public String authenticateStudent() { //
	 * log.info("User Entered Enrollment Number is ::" + //
	 * appBean1.getEnrollmentNumber());
	 * 
	 * appBeansList =
	 * aplDAO.getStudentDetailByEnrollMentNumber(appBean1.getEnrollmentNumber
	 * ());
	 * 
	 * if (appBeansList.isEmpty()) { // log.info("Invalid Enrollment ID");
	 * request.setAttribute("msg", "Please Enter valid Enrollment Number");
	 * return "failure";
	 * 
	 * }
	 * 
	 * return SUCCESS;
	 * 
	 * }
	 */

	// add Bulk Students
	public String addBulkStudents() throws Exception {

		if (fileUploadFileName.endsWith(".xlsx")) {

			String path = request.getServletContext().getRealPath("/");
			path = path + File.separator;
			File f = new File(path + "/SGI/");
			f.mkdir();

			log.info("File Name is ::" + fileUploadFileName);

			appBeansList = aplDAO.importExcelFileToDatabase(fileUploadFileName, fileUpload, f + File.separator);

			request.setAttribute("msg", "Student Record Uploaded Successfully");

			return SUCCESS;

		}

		else {

			// log.info("file Format not Match");
			String msg = "Please Select Proper File Format";
			request.setAttribute("msg", msg);
			return "failure";
		}

	}

	public void updateStudentDue() {

		List<Integer> feeIdes = new ArrayList<Integer>();
		String applicableFeeString = aplDAO.getApplicableFeesString(appBean1.getCourse());

		String applicableFeeIdArray[] = applicableFeeString.split("~");
		for (String string : applicableFeeIdArray) {
			feeIdes.add(Integer.parseInt(string));
		}
		Iterator<Integer> itr = feeIdes.iterator();
		while (itr.hasNext()) {
			Integer feeId = itr.next();
			String feeName = fcDAO.getFeeName(feeId);
			Integer categoryId = fvDAO.findFeeValueId(appBean1.getCategory(), feeId);
			Integer courseId = fvDAO.findFeeValueId(appBean1.getCourse(), feeId);
			Double fee = fcDAO.calculateFeeStudent(categoryId, courseId, null, feeId);
			updateDueAmountOfStudent(feeId, fee, feeName);
		}

	}

	public void updateDueAmountOfStudent(Integer feeId, Double feeAmount, String feeName) {

		PaymentDuesBean paymentDue = new PaymentDuesBean();
		paymentDue.setFeeName(feeName);
		paymentDue.setFeeId(feeId);
		paymentDue.setNetDue(feeAmount);
		paymentDue.setTotal_fee_amount(feeAmount);
		// log.info("enrollment number in action class" +
		// appBean1.getEnrollmentNumber());
		aplDAO.saveDueFeeOFStudent(appBean1.getEnrollmentNumber(), paymentDue);

	}

	// it's from student login page

	public String getParticularFeeDetailsOfStudentFromloginPage() {
		HttpSession httpSession = request.getSession();
		LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");

		try {
			appBean1 = aplDAO.getUserDetail(loginBean.getAppBean().getEnrollmentNumber());
			getDuesOfStudent();
			return SUCCESS;
		} catch (java.lang.NullPointerException e) {
			request.setAttribute("msg", "Session Time Out");
			return ERROR;
		}

	}

	public String StudentDuesDetail() {

		HttpSession httpSession = request.getSession();
		LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		String enroll = new String();
		try {
			enroll = appBean1.getEnrollmentNumber();

		} catch (java.lang.NullPointerException e) {
			request.setAttribute("msg", "Enrollment is not exist");
			return "failure";
		}
		try {
			appBean1 = aplDAO.getUserDetail(enroll);
			app1 = aplDAO.getStudentOpDues(appBean1.getEnrollmentNumber(), loginBean.getOperatorBean().getAffBean()
					.getInstId());
			// app1 = aplDAO.getStudentDues(appBean1.getEnrollmentNumber());
			totalDueOFStudent = aplDAO.totalDueFeeOfStudent(appBean1.getEnrollmentNumber());
			totalNetFees = aplDAO.totalfeesOfStudent(appBean1.getEnrollmentNumber());
			paymentDone = aplDAO.totalPaymentDone(appBean1.getEnrollmentNumber());

			// log.info("total net dues is ::" + totalNetFees);

		} catch (Exception e) {
			request.setAttribute("msg", "Enrollment Number is Not Registered");

			return "failure";
		}

		//
		return SUCCESS;
	}

	public void getDuesOfStudent() {
		app1 = aplDAO.getStudentDues(appBean1.getEnrollmentNumber());
		feeList = aplDAO.getAllFeeDeatils();
		totalDueOFStudent = aplDAO.totalDueFeeOfStudent(appBean1.getEnrollmentNumber());
		totalNetFees = aplDAO.totalfeesOfStudent(appBean1.getEnrollmentNumber());
		paymentDone = aplDAO.totalPaymentDone(appBean1.getEnrollmentNumber());
	}

	public String getTransactionDetailsOfStudent() {
		try {
			HttpSession session = request.getSession();

			LoginBean bean = (LoginBean) session.getAttribute("loginUserBean");

			if (session.getAttribute("sesProfile").toString().contentEquals("CollegeOperator")) {
				Integer operatorId = (Integer) session.getAttribute("opratorId");

				Integer instituteId = opratorDAO.getCollegeIdOfOperator(bean.getOperatorBean().getOperatorId());

				transactionDetailsForReport = affDao.getAllTransactionDetail(instituteId);
				return SUCCESS;
			}

			String enrollment = (String) session.getAttribute("StudentEnrollId");
			transactionDetailsForReport = aplDAO.getTransactionDetailsOfStudent(enrollment);
		} catch (java.lang.NullPointerException e) {
			request.setAttribute("msg", "Session Time Out");
			return ERROR;
		}
		return SUCCESS;
	}

	// End of Action Methods

	// ---------------------------------------------------

	// Keep Getter Setter Methods Here
	public AppBean getAppBean1() {
		return appBean1;
	}

	public void setAppBean1(AppBean appBean1) {
		this.appBean1 = appBean1;
	}

	public List<AppBean> getAppBeansList() {
		return appBeansList;
	}

	public void setAppBeansList(List<AppBean> appBeansList) {
		this.appBeansList = appBeansList;
	}

	public AppBean getAppBean() {
		return appBean;
	}

	public void setAppBean(AppBean appBean) {
		this.appBean = appBean;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public File getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getFileUploadFileName() {
		return fileUploadFileName;
	}

	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public FileInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(FileInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public AffBean getAffBean() {
		return affBean;
	}

	public void setAffBean(AffBean affBean) {
		this.affBean = affBean;
	}

	public Integer getAplInstId() {
		return aplInstId;
	}

	public void setAplInstId(Integer aplInstId) {
		this.aplInstId = aplInstId;
	}

	public List<AffBean> getAffInstList() {
		return affInstList;
	}

	public void setAffInstList(List<AffBean> affInstList) {
		this.affInstList = affInstList;
	}

	public List<FeeDetailsBean> getFeeDetailsBeanList() {
		return feeDetailsBeanList;
	}

	public void setFeeDetailsBeanList(List<FeeDetailsBean> feeDetailsBeanList) {
		this.feeDetailsBeanList = feeDetailsBeanList;
	}

	public AppBean getApp1() {
		return app1;
	}

	public void setApp1(AppBean app1) {
		this.app1 = app1;
	}

	public Double getTotalDueOFStudent() {
		return totalDueOFStudent;
	}

	public void setTotalDueOFStudent(Double totalDueOFStudent) {
		this.totalDueOFStudent = totalDueOFStudent;
	}

	public List<FeeDetailsBean> getFeeList() {
		return feeList;
	}

	public void setFeeList(List<FeeDetailsBean> feeList) {
		this.feeList = feeList;
	}

	public List<TransactionBean> getTransactionDetailsForReport() {
		return transactionDetailsForReport;
	}

	public void setTransactionDetailsForReport(List<TransactionBean> transactionDetailsForReport) {
		this.transactionDetailsForReport = transactionDetailsForReport;
	}

	public Double getTotalNetFees() {
		return totalNetFees;
	}

	public void setTotalNetFees(Double totalNetFees) {
		this.totalNetFees = totalNetFees;
	}

	public Double getPaymentDone() {
		return paymentDone;
	}

	public void setPaymentDone(Double paymentDone) {
		this.paymentDone = paymentDone;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

}
