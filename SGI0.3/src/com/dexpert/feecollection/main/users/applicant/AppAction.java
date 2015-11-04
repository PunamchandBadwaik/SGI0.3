package com.dexpert.feecollection.main.users.applicant;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.config.FcDAO;
import com.dexpert.feecollection.main.fee.config.FeeDetailsBean;
import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.fee.lookup.LookupDAO;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvDAO;
import com.dexpert.feecollection.main.users.LoginBean;
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
	List<LookupBean> lookupBeanList = new ArrayList<LookupBean>();
	String collegeName, applicantParamValue;
	OperatorDao opratorDAO = new OperatorDao();
	LinkedHashSet<FvBean> fvBeansSet = new LinkedHashSet<FvBean>();
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
	private Double totalDueOFStudent, totalNetFees, paymentDone, discountedAmount = 0.0, amountAfterDiscount = 0.0,
			finalAmountToBePaid = 0.0;
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

	

	public String registerStudent() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		LookupDAO lookupdao = new LookupDAO();
		log.info("Course  ::" + applicantParamValue);

		// log.info("Length iss ::" + applicantParamValue.replace(" ", ""));

		String[] x = applicantParamValue.replace(" ", "").split(",");
		// log.info("x ::" + x.length);

		for (int i = 0; i < x.length; i++) {
			FvBean bean = new FvBean();
			bean.setFeeValueId(Integer.parseInt(x[i]));
			fvBeansSet.add(bean);
		}

		// appBean1.setApplicantParamValues(fvBeansSet);
		// appBean1.getApplicantParamValues();

		HttpSession httpSession = request.getSession();
		LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		log.info("Applicant Name  ::" + appBean1.getAplFirstName());

		List<String> existEnrollmentList = aplDAO.existingEnrollNum(appBean1);
		if (existEnrollmentList.isEmpty()) {

			/*
			 * if (aplInstId == null) { request.setAttribute("msg",
			 * "Please Select College Name"); affInstList =
			 * affDAO.getCollegesList(); return "failure"; }
			 */

			// try {
			// log.info("Enrollment Number is" +
			// appBean1.getEnrollmentNumber());
			appBean1 = aplDAO.saveOrUpdate(appBean1, loginBean.getAffBean().getInstId(), fvBeansSet);

			// } catch (java.lang.NullPointerException e) {
			// request.setAttribute("msg", "Please Enter Enrollment Number");
			// affInstList = affDAO.getCollegesList();
			// return "failure";
			// }
			/*
			 * catch(ConstraintViolationException ex){
			 * request.setAttribute("msg", "Enrollment Number Already Exit");
			 * affInstList = affDAO.getCollegesList(); return "failure";
			 * 
			 * }
			 */

			try {
				// updateStudentDue();

			} catch (java.util.NoSuchElementException e) {

				request.setAttribute("msg", "Please Set Fees for Student");
				affInstList = affDAO.getCollegesList();
				return "feeNotSet";
			}

			request.setAttribute("msg", "Student Addedd Successfully");

			return SUCCESS;

		} else {
			log.info("4");
			request.setAttribute("msg", "Enrollment Number Already Registered");
			affInstList = affDAO.getCollegesList();
			List<Integer> structureIdes = affDao.getStrutureId(loginBean.getAffBean().getInstId(), null);
			log.info("Struture id" + structureIdes);
			List<Integer> valueIdes = fcDAO.getLookupValue(structureIdes);
			log.info("value ides got from fee config table::::::" + valueIdes);
			List<Integer> lookUpParamList = fvDAO.getListOfValueBeans(valueIdes);
			log.info("look up param list::::::" + lookUpParamList);
			lookupBeanList = lookupdao.getListOfLookUpValues("Applicant", lookUpParamList, valueIdes);
			affDao.getAllCourseOfInst(loginBean.getAffBean().getInstId());
			return "failure";
		}

	}

	// to get list of All Students

	public String getStudentList() {
		HttpSession httpSession = request.getSession();
		Integer instId=(Integer)httpSession.getAttribute("sesId");

		try {
			appBeansList= aplDAO.getStudentDetail(instId);

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
			try {
				String path = request.getServletContext().getRealPath("/");
				path = path + File.separator;
				File f = new File(path + "/SGI/");
				f.mkdir();

				log.info("File Name is ::" + fileUploadFileName);

				aplDAO.generateTempTable(fileUpload);

				request.setAttribute("msg", "Student Record Uploaded Successfully");
				return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("msg", "Error in file uploading, please try again.");
				return SUCCESS;
			}

		}

		else {

			// log.info("file Format not Match");
			String msg = "Please Select Proper File Format";
			request.setAttribute("msg", msg);
			return "failure";
		}

	}

	/*
	 * public void updateStudentDue() {
	 * 
	 * List<Integer> feeIdes = new ArrayList<Integer>(); String
	 * applicableFeeString =
	 * aplDAO.getApplicableFeesString(appBean1.getCourse());
	 * log.info("Applicable fee string" + applicableFeeString); String
	 * applicableFeeIdArray[] = applicableFeeString.split("~"); for (String
	 * string : applicableFeeIdArray) { feeIdes.add(Integer.parseInt(string)); }
	 * Iterator<Integer> itr = feeIdes.iterator(); while (itr.hasNext()) {
	 * Integer feeId = itr.next(); String feeName = fcDAO.getFeeName(feeId);
	 * Integer categoryId = fvDAO.findFeeValueId(appBean1.getCategory(), feeId);
	 * Integer courseId = fvDAO.findFeeValueId(appBean1.getCourse(), feeId);
	 * Double fee = fcDAO.calculateFeeStudent(categoryId, courseId, null,
	 * feeId); updateDueAmountOfStudent(feeId, fee, feeName); }
	 * 
	 * }
	 */

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
			//e.printStackTrace();
			return ERROR;
		}

	}

	/* College Operator getting the Student dues Detail */

	public String operatorGettingTheStudentDuesDetail() {

		HttpSession httpSession = request.getSession();
		LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		String enroll = new String();
		try {
			enroll = appBean1.getEnrollmentNumber();

			httpSession.setAttribute("enroll", enroll);
			appBean1 = aplDAO.getUserDetail(enroll);

			/*
			 * httpSession.setAttribute("sesProfile", "Student");
			 * httpSession.setAttribute("dashLink",
			 * "getTheStudentFeeDetailsFromLoginPage");
			 * 
			 * httpSession.setAttribute("loginUserBean",
			 * appBean1.getLoginBean());
			 */
			getDuesOfStudent();
			return SUCCESS;
		} catch (Exception e) {
			request.setAttribute("msg", "Please Enter Valid UIN");
			return "failure";
		}
	}

	public String StudentDuesDetail() {

		HttpSession httpSession = request.getSession();
		// LoginBean loginBean = (LoginBean)
		// httpSession.getAttribute("loginUserBean");
		String enroll = new String();
		try {
			enroll = appBean1.getEnrollmentNumber();

			httpSession.setAttribute("enroll", enroll);
			appBean1 = aplDAO.getUserDetail(enroll);

			httpSession.setAttribute("sesProfile", "Student");
			httpSession.setAttribute("dashLink", "getTheStudentFeeDetailsFromLoginPage");

			httpSession.setAttribute("loginUserBean", appBean1.getLoginBean());

			getDuesOfStudent();
			return SUCCESS;
		} catch (Exception e) {
			request.setAttribute("msg", "Please Enter Valid UIN");
			return "failure";
		}

		/*
		 * } catch (java.lang.NullPointerException e) {
		 * request.setAttribute("msg", "Enrollment is not exist"); return
		 * "failure"; } try { appBean1 = aplDAO.getUserDetail(enroll); app1 =
		 * aplDAO.getStudentOpDues(appBean1.getEnrollmentNumber(),
		 * loginBean.getOperatorBean().getAffBean() .getInstId()); // app1 =
		 * aplDAO.getStudentDues(appBean1.getEnrollmentNumber());
		 * totalDueOFStudent =
		 * aplDAO.totalDueFeeOfStudent(appBean1.getEnrollmentNumber());
		 * totalNetFees =
		 * aplDAO.totalfeesOfStudent(appBean1.getEnrollmentNumber());
		 * paymentDone =
		 * aplDAO.totalPaymentDone(appBean1.getEnrollmentNumber());
		 * 
		 * // log.info("total net dues is ::" + totalNetFees);
		 * 
		 * } catch (Exception e) { request.setAttribute("msg",
		 * "Enrollment Number is Not Registered");
		 * 
		 * return "failure"; }
		 * 
		 * // return SUCCESS;
		 */
	}

	public Set<PaymentDuesBean> addSeqOfFees(Set<PaymentDuesBean> paymentDues, Integer instId) {
		Set<PaymentDuesBean> payDueSetWithSeqId = new HashSet<PaymentDuesBean>();
		Iterator<PaymentDuesBean> itr = paymentDues.iterator();
		while (itr.hasNext()) {
			PaymentDuesBean payDues = itr.next();
			Integer sequenceId = fcDAO.getSequenceOfFee(instId, payDues.getFeeId());
			payDues.setSequenceId(sequenceId);
			payDueSetWithSeqId.add(payDues);
		}
		return payDueSetWithSeqId;
	}

	public void getDuesOfStudent() {
		log.info("inside getDuesOfStudent");
		app1 = aplDAO.getStudentDues(appBean1.getEnrollmentNumber());
		feeList = aplDAO.getAllFeeDeatils();
		totalDueOFStudent = aplDAO.totalDueFeeOfStudent(appBean1.getEnrollmentNumber());
		totalNetFees = aplDAO.totalfeesOfStudent(appBean1.getEnrollmentNumber());
		paymentDone = aplDAO.totalPaymentDone(appBean1.getEnrollmentNumber());
		String discountType = app1.getDiscountType() == null ? "" : app1.getDiscountType();
		Double discountValue = app1.getDiscountValue() == null ? 0.0 : app1.getDiscountValue();
		if (discountValue > 0) {
			if (discountType.contentEquals("Per")) {
				discountedAmount = totalDueOFStudent * discountValue / 100;
				amountAfterDiscount = totalDueOFStudent - discountedAmount;
				finalAmountToBePaid = amountAfterDiscount - paymentDone;
				totalDueOFStudent = totalDueOFStudent - discountedAmount;

				totalNetFees = totalNetFees - discountedAmount;
			} else if (discountType.contentEquals("Fix")) {
				amountAfterDiscount = totalDueOFStudent - discountValue;
				finalAmountToBePaid = amountAfterDiscount - paymentDone;
				totalDueOFStudent = totalDueOFStudent - discountValue;
				totalNetFees = totalNetFees - discountValue;
			}
		}
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

	public String quickPayStudentDuesDetail() {

		/* HttpSession httpSession = request.getSession(); */
		/*
		 * LoginBean loginBean = (LoginBean)
		 * httpSession.getAttribute("loginUserBean");
		 */
		String enroll = new String();
		try {
			enroll = appBean1.getEnrollmentNumber();

		} catch (java.lang.NullPointerException e) {
			request.setAttribute("msg", "Enrollment is not exist");
			return "failure";
		}
		try {
			appBean1 = aplDAO.getUserDetail(enroll);
			/*
			 * app1 = aplDAO.getStudentOpDues(appBean1.getEnrollmentNumber(),
			 * loginBean.getOperatorBean().getAffBean() .getInstId());
			 */
			app1 = aplDAO.getQuickPayStudentOpDues(appBean1.getEnrollmentNumber());

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

	// student due view for college
	public String viewStudentDues() {
		String enrollmentNumber = request.getParameter("applicantId").trim();
		app1 = aplDAO.getStudentDues(enrollmentNumber);
		feeList = aplDAO.getAllFeeDeatils();
		totalDueOFStudent = aplDAO.totalDueFeeOfStudent(enrollmentNumber);
		totalNetFees = aplDAO.totalfeesOfStudent(enrollmentNumber);
		paymentDone = aplDAO.totalPaymentDone(enrollmentNumber);
		return SUCCESS;

	}

	public String showBulkTransDetail() {

		String transactionId = request.getParameter("transId");

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

	public String getApplicantParamValue() {
		return applicantParamValue;
	}

	public void setApplicantParamValue(String applicantParamValue) {
		this.applicantParamValue = applicantParamValue;
	}

	public LinkedHashSet<FvBean> getFvBeansSet() {
		return fvBeansSet;
	}

	public void setFvBeansSet(LinkedHashSet<FvBean> fvBeansSet) {
		this.fvBeansSet = fvBeansSet;
	}

	public List<LookupBean> getLookupBeanList() {
		return lookupBeanList;
	}

	public void setLookupBeanList(List<LookupBean> lookupBeanList) {
		this.lookupBeanList = lookupBeanList;
	}

	public Double getDiscountedAmount() {
		return discountedAmount;
	}

	public void setDiscountedAmount(Double discountedAmount) {
		this.discountedAmount = discountedAmount;
	}

	public Double getAmountAfterDiscount() {
		return amountAfterDiscount;
	}

	public void setAmountAfterDiscount(Double amountAfterDiscount) {
		this.amountAfterDiscount = amountAfterDiscount;
	}

	public Double getFinalAmountToBePaid() {
		return finalAmountToBePaid;
	}

	public void setFinalAmountToBePaid(Double finalAmountToBePaid) {
		this.finalAmountToBePaid = finalAmountToBePaid;
	}

}
