package com.dexpert.feecollection.main.users.affiliated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.config.FcBean;
import com.dexpert.feecollection.main.fee.config.FcDAO;
import com.dexpert.feecollection.main.fee.config.FeeDetailsBean;
import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.fee.lookup.LookupDAO;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.RandomPasswordGenerator;
import com.dexpert.feecollection.main.users.applicant.AppDAO;
import com.dexpert.feecollection.main.users.operator.OperatorDao;
import com.dexpert.feecollection.main.users.parent.ParBean;
import com.dexpert.feecollection.main.users.parent.ParDAO;
import com.opensymphony.xwork2.ActionSupport;

public class AffAction extends ActionSupport {

	// Declare Global Variables Here
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	HttpSession ses = request.getSession();
	List<AffBean> affBeansList = new ArrayList<AffBean>();
	List<ParBean> parBeans = new ArrayList<ParBean>();
	OperatorDao opratorDAO = new OperatorDao();
	ArrayList<PaymentDuesBean> collegeDueReport = new ArrayList<PaymentDuesBean>();
	Boolean saved = true;
	List<String> list = new ArrayList<String>();
	private Integer parInstId;
	private LookupDAO lookupdao = new LookupDAO();
	List<LookupBean> lookupBeanList = new ArrayList<LookupBean>();
	public AffBean affInstBean;
	private ArrayList<AffFeeCalcDetail> calcList = new ArrayList<AffFeeCalcDetail>();
	private AffFeePropBean propbean;
	ArrayList<AffFeePropBean> dueList = new ArrayList<AffFeePropBean>();
	AffDAO affDao = new AffDAO();
	ParBean parBean = new ParBean();
	List<ParBean> parBeansList = new ArrayList<ParBean>();
	ParDAO parDAO = new ParDAO();
	FcDAO feeDAO = new FcDAO();
	static Logger log = Logger.getLogger(AffAction.class.getName());
	public List<AffBean> affInstList = new ArrayList<AffBean>();
	private ArrayList<Integer> paramIds = new ArrayList<Integer>();;
	ArrayList<AffBean> failureAffBeanList = new ArrayList<AffBean>();
	ArrayList<AffBean> existingInstitureRecordList = new ArrayList<AffBean>();
	private ArrayList<LookupBean> paramList2 = new ArrayList<LookupBean>();
	String fileFileName;
	FileInputStream inputStream;
	private File fileUpload;
	private ArrayList<FeeDetailsBean> feeList = new ArrayList<FeeDetailsBean>();
	private String fileUploadFileName;
	private Integer fileSize;
	private String contentType;
	private List<TransactionBean> transactionDetailsForReport;
	private List<Object[]> totalDuesOfStudent;
	Double totalNetDuesOFCollegeStudent = 0.0;
	Double totalPaymentToDate = 0.0;
	Double totalOriginalDues = 0.0;
	List<String> listOfCourse;
	List<String> feeNameList;
	AppDAO appDAO = new AppDAO();
	List<Object[]> totalDuesOFCollege;

	// End of Global Variables

	// ---------------------------------------------------

	// Action Methods Here

	// registerInstitute()
	public String registerInstitute() throws Exception {

		// log.info("paramset is "+affInstBean.getParamvalues().toString());
		List<String> instNameList = affDao.getCollegeNameList(affInstBean.getInstName());
		/* log.info("List Size is ::" + instNameList.size()); */

		ParDAO parDao = new ParDAO();
		HttpSession httpSession = request.getSession();
		LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		if (instNameList.isEmpty()) {

			if (parInstId == null) {
				parBeansList = parDao.getUniversityList();
				request.setAttribute("msg", "Please Select University");
				return "failure";

			} else {
				String username;
				// generate credentials for admin login
				try {
					username = "Inst".concat(affInstBean.getInstName().concat(affInstBean.getInstAddress())
							.replaceAll("\\s+", "").substring(0, 4).concat(affDao.getRowCount().toString()));

				} catch (java.lang.NullPointerException e) {
					username = "Inst".concat(affInstBean.getInstName().concat(affInstBean.getInstAddress())
							.replaceAll("\\s+", "").substring(0, 4).concat("1"));

				}

				String password = RandomPasswordGenerator.generatePswd(6, 8, 1, 2, 0);
				// log.info("Password Generated is " + password);

				PasswordEncryption.encrypt(password);
				String encryptedPwd = PasswordEncryption.encStr;

				LoginBean creds = new LoginBean();
				creds.setPassword(encryptedPwd);
				creds.setUserName(username);

				creds.setProfile(affInstBean.getLoginBean().getProfile());
				ParBean parBean1 = new ParBean();
				parBean1 = parDAO.viewUniversity(parInstId);

				// one to many and many to one relationship
				parBean1.getAffBeanOneToManySet().add(affInstBean);
				affInstBean.setParBeanAff(parBean1);

				parDAO.saveOrUpdate(parBean1, null);

				// for bidirectional relationship ,set parent record to child
				// record
				creds.setAffBean(affInstBean);

				// one to one relationship
				// affInstBean.setParBeanOneToOne(parBean1);
				if (creds.getProfile().equals("Institute")) {

					// for bidirectional relationship ,set child record to
					// Parent
					// record
					affInstBean.setLoginBean(creds);

				}

				String path = request.getServletContext().getRealPath("/");
				path = path + File.separator;
				File f = new File(path + "/RGUHS/");
				f.mkdir();

				// add parent institute details to affInstBean

				affInstBean.setFileUpload(fileUpload);
				affInstBean.setFileUploadFileName(fileUploadFileName);

				log.info("The ID after saving is is " + affInstBean.getInstId());
				// if saved successfully generate credentials and forward
				// success

				affInstBean = affDao.saveOrUpdate(affInstBean, f + File.separator);

				// -----Code for sending email//--------------------
				EmailSessionBean email = new EmailSessionBean();
				email.sendEmail(affInstBean.getEmail(), "Welcome To FeeDesk!", username, password,
						affInstBean.getInstName());

				request.setAttribute("msg", "Institute Added Successfully");
				request.setAttribute("redirectLink", "Success.jsp");
				return SUCCESS;

			}
		}

		// else forward error message on input page

		else {
			log.info("College NAME ALREADY AVAILABLE");
			parBeansList = parDao.getUniversityList();
			request.setAttribute("msg", "Institute Name is Already Registered");
			return "failure";
		}

	}

	// getInstituteDetails()
	public String getInstituteDetails() {
		String filterKey = null, filterVal = null;
		Date fromDate = new Date(), toDate = new Date();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		try {

			try {
				filterKey = request.getParameter("fKey").toString();
				filterVal = request.getParameter("fVal").toString();
			} catch (java.lang.NullPointerException e) {
				log.info("filter key is Null");
				log.info("filter value is Null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			filterKey = "NONE";
		}
		affInstList = affDao.getInstitutes(filterKey, filterVal, idList, fromDate, toDate);
		return SUCCESS;
	}

	// get institute Details list

	public String getCollegeList() {

		affInstList = affDao.getCollegesList();
		lookupBeanList = lookupdao.getListOfLookUpValues("Applicant");
		log.info("look up List Size is ::" + lookupBeanList.size());

		return SUCCESS;
	}

	// get collgege list based on university ID
	public String getUniversityCollegeList() {
		HttpSession httpSession = request.getSession();
		LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		parBean = affDao.getUniversityCollegeList(loginBean);
		log.info("University Names ::" + parBean.getParInstName());
		return SUCCESS;
	}

	// get one college Detail to edit

	public String viewCollegeDetail() {

		String instituteId = request.getParameter("instId");
		Integer instId = Integer.parseInt(instituteId);
		affInstBean = affDao.viewInstDetail(instId);

		ses.setAttribute("sesAffBean", affInstBean);

		return SUCCESS;
	}

	// edit college Details

	public String editCollegeDetail() {

		String id = request.getParameter("instId");

		Integer instId = Integer.parseInt(id);

		affInstBean = affDao.getOneCollegeRecord(instId);

		return SUCCESS;

	}

	// update COllege Record

	public String updateCollegeDetail() {
		// log.info("paramlist is " + affInstBean.getParamvalues().toString());
		List<String> instNameList = affDao.getCollegeNameList(affInstBean.getInstName());
		log.info("list Size is ::" + instNameList.size());
		if (instNameList.isEmpty()) {

			affDao.updateCollege(affInstBean);

			request.setAttribute("msg", "Institute Updated Successfully");

			return SUCCESS;
		} else {

			log.info("College NaME ALREADY AVAILABLE");

			request.setAttribute("msg", "Institute Name is Already Registered");
			return "failure";

		}

	}

	public String configureCollegeParam() throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {

		log.info("parameter ids are " + paramIds.toString());
		HashMap<Integer, FvBean> valueMap = (HashMap<Integer, FvBean>) ses.getAttribute("sesParamMap");
		AffBean savedata = (AffBean) ses.getAttribute("sesAffBean");
		log.info("keys are " + valueMap.keySet().toString());
		for (int i = 0; i < paramIds.size(); i++) {
			savedata.getParamvalues().add(valueMap.get(paramIds.get(i)));
		}

		affDao.saveOrUpdate(savedata, null);
		ses.removeAttribute("sesAffBean");
		ses.removeAttribute("sesParamMap");
		request.setAttribute("msg", "Institute Updated Successfully");

		return SUCCESS;

	}

	// edit college doc

	public String editCollegeDoc() {
		String id = request.getParameter("instId");

		Integer instId = Integer.parseInt(id);

		affInstBean = affDao.getOneCollegeRecord(instId);
		return SUCCESS;

	}

	// update College Doc

	public String updateCollegeDoc() {

		String path = request.getServletContext().getRealPath("/");
		path = path + File.separator;
		File f = new File(path + "/RGUHS/");
		f.mkdir();

		affInstBean.setFileUpload(fileUpload);
		affInstBean.setFileUploadFileName(fileUploadFileName);
		affDao.updateCollegeDoc(affInstBean, f + File.separator);

		String msg = "File Successfully Updated";
		request.setAttribute("msg", msg);

		return SUCCESS;
	}

	// download File

	public String downloadFile() throws IOException {
		String docuId = request.getParameter("documentId");
		log.info("Document ID ::" + docuId);
		Integer id = Integer.parseInt(docuId);
		affInstBean = affDao.getOneCollegeRecord(id);
		FileOutputStream fileOuputStream = new FileOutputStream(affInstBean.getFileUploadFileName());
		fileOuputStream.write(affInstBean.getFilesByteSize());
		fileOuputStream.close();

		fileFileName = affInstBean.getFileUploadFileName();
		inputStream = new FileInputStream(affInstBean.getFileUploadFileName());

		return SUCCESS;
	}

	// add bulk colleges

	public String bulkCollegesAdd() throws Exception {
		log.info("file Name ::" + fileUploadFileName);
		log.info("file IS  ::" + fileUpload);

		// if loop to check format of file
		if (fileUploadFileName.endsWith(".xlsx")) {

			String path = request.getServletContext().getRealPath("/");
			path = path + File.separator;
			File f = new File(path + "/RGUHS/");
			f.mkdir();

			// try {
			affBeansList = affDao.importExcelFileToDatabase(fileUploadFileName, fileUpload, f + File.separator);
			// } catch (java.lang.NullPointerException e) {

			// request.setAttribute("msg",
			// "Please Create University Credential First");
			// return ERROR;
			// }
			Iterator<AffBean> iterator = affBeansList.iterator();
			while (iterator.hasNext()) {
				AffBean affBean = (AffBean) iterator.next();

			}

			return SUCCESS;

		}

		else {

			log.info("file Format not Match");
			String msg = "Please Select Proper File Format";
			request.setAttribute("msg", msg);
			return "failure";
		}

	}

	public String GetFees() {
		Integer instId = Integer.parseInt(request.getParameter("collId").trim());
		AffBean instbean = affDao.getOneCollegeRecord(instId);
		ArrayList<FeeDetailsBean> instfeeList = new ArrayList<FeeDetailsBean>(instbean.getFeeSet());
		feeList = feeDAO.GetFees("payee", "institute", null, null);
		for (int j = 0; j < instfeeList.size(); j++) {

			for (int i = 0; i < feeList.size(); i++) {
				if (instfeeList.get(j).getFeeId() == feeList.get(i).getFeeId()) {
					feeList.get(i).setGenericFlag(1);
				} else {
					feeList.get(i).setGenericFlag(0);
				}
			}

		}

		return SUCCESS;
	}

	public String AddFees() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		ArrayList<FeeDetailsBean> feelist = new ArrayList<FeeDetailsBean>();

		AffBean collegedata = new AffBean();
		try {
			// Get College id from request
			Integer id = Integer.parseInt(request.getParameter("collId").trim());
			// Get Fee ids from request
			String feeidstr = request.getParameter("reqFeeIds").trim();
			log.info(feeidstr);
			List<String> FeeIds = Arrays.asList(feeidstr.split(","));
			ArrayList<Integer> FeeIdsInt = new ArrayList<Integer>();
			Iterator<String> idIt = FeeIds.iterator();
			log.info(FeeIds.toString());
			while (idIt.hasNext()) {
				try {
					FeeIdsInt.add(Integer.parseInt(idIt.next()));
				} catch (java.lang.NumberFormatException e) {
					// TODO Auto-generated catch block
					FeeIdsInt.add(-1);
				}
			}
			// Get College Data
			collegedata = affDao.getOneCollegeRecord(id);
			// Get Fees in Set
			feelist = feeDAO.GetFees("ids", null, null, FeeIdsInt);
			Set<FeeDetailsBean> feeset = collegedata.getFeeSet();
			Set<AffFeePropBean> propSet = collegedata.getFeeProps();
			for (int i = 0; i < feelist.size(); i++) {
				AffFeePropBean tempbean = new AffFeePropBean();
				tempbean.setFeeId(feelist.get(i).getFeeId());
				tempbean.setFeeName(feelist.get(i).getFeeName());
				// Set Due Bean
				PaymentDuesBean duebean = new PaymentDuesBean();
				duebean.setDateCalculated(new Date());
				duebean.setFeeId(tempbean.getFeeId());
				duebean.setPayments_to_date((double) 0);
				duebean.setPayee("Institute");
				duebean.setTotal_fee_amount((double) 0);
				duebean.setNetDue((double) 0);

				tempbean.setDueBean(duebean);
				tempbean.setCalcFlag(0);
				propSet.add(tempbean);

			}

			feeset.addAll(feelist);
			feelist.clear();
			// Add Fees to College Beans' FeeSet
			collegedata.setFeeSet(feeset);
			collegedata.setFeeProps(propSet);
			// ////

			// ///
			affDao.saveOrUpdate(collegedata, null);
			request.setAttribute("msg", "Fees Updated Successfully");
			// Save College Bean
			return SUCCESS;
		} catch (java.lang.NumberFormatException e) {
			e.printStackTrace();

			return ERROR;
		}
	}

	public String RemoveFee() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		Integer feeId = Integer.parseInt(request.getParameter("reqFeeId").trim());
		Integer instId = Integer.parseInt(request.getParameter("collId").trim());
		AffBean collegedata = affDao.getOneCollegeRecord(instId);
		ArrayList<FeeDetailsBean> feeList = new ArrayList<FeeDetailsBean>(collegedata.getFeeSet());
		for (int i = 0; i < feeList.size(); i++) {
			if (feeId == feeList.get(i).getFeeId()) {
				feeList.remove(i);
			}
		}
		collegedata.setFeeSet(new HashSet<FeeDetailsBean>(feeList));
		collegedata = affDao.saveOrUpdate(collegedata, null);
		request.setAttribute("msg", "Fees Updated Successfully");
		request.setAttribute("redirectlink", "ViewCollegeFees?instId=" + instId);
		return SUCCESS;
	}

	public String GetParameterListInstitute() {
		try {

			paramList2 = lookupdao.getLookupData("Scope", "Institute", null, null);
			HashMap<Integer, FvBean> paramMap = new HashMap<Integer, FvBean>();
			Iterator<LookupBean> lIt = paramList2.iterator();
			while (lIt.hasNext()) {
				LookupBean temp = lIt.next();
				List<FvBean> valueList = new ArrayList<FvBean>();
				valueList = temp.getFvBeansList();
				Iterator<FvBean> pIt = valueList.iterator();
				while (pIt.hasNext()) {
					FvBean temp2 = pIt.next();
					paramMap.put(temp2.getFeeValueId(), temp2);
				}
			}

			ses.setAttribute("sesParamMap", paramMap);
			String instituteId = request.getParameter("instId");
			Integer instId = Integer.parseInt(instituteId);
			affInstBean = affDao.viewInstDetail(instId);
			ses.setAttribute("sesAffBean", affInstBean);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	// Method to get Fee Properties' Details
	public String viewFeeProps() {
		// get institute id from request
		Integer reqinstId = Integer.parseInt(request.getParameter("instId").trim());
		// get fee id from request
		Integer reqfeeId = Integer.parseInt(request.getParameter("reqfeeId").trim());
		// get inst bean from database
		AffBean tempbean = affDao.getOneCollegeRecord(reqinstId);
		// populate feeprop bean with correct object from set
		Set<AffFeePropBean> feePropsSet = tempbean.getFeeProps();
		log.info("set size is " + feePropsSet.size());
		Iterator<AffFeePropBean> setIt = feePropsSet.iterator();
		while (setIt.hasNext()) {
			AffFeePropBean tempbean2 = new AffFeePropBean();
			tempbean2 = setIt.next();
			if (tempbean2.getFeeId() == reqfeeId) {
				propbean = tempbean2;
				return SUCCESS;
			}

		}
		return ERROR;
	}

	public String getDues() {
		// Get id from session
		Integer id = (Integer) ses.getAttribute("sesId");
		// get bean from db
		AffBean collbean = affDao.getOneCollegeRecord(id);
		// get feeprops set in list
		dueList = new ArrayList<AffFeePropBean>(collbean.getFeeProps());
		return SUCCESS;
	}

	// to get College Due Report

	public String collegeDueReport() {
		boolean popUp = false;
		Integer collegeId = null;
		// collegeDueReport = (ArrayList<PaymentDuesBean>)
		// affDao.collegeDueReport();
		// log.info("list size of college Due" + collegeDueReport.size());

		if (ses.getAttribute("sesProfile").toString().contentEquals("Affiliated")) {
			collegeId = (Integer) ses.getAttribute("sesId");
			String courseName = request.getParameter("courseName");
			// String feeName=request.getParameter("feeName");
			List<String> enrollmentNumber = affDao.findAllStudentOfInstituteByCourse(collegeId, courseName);
			if (enrollmentNumber.size() < 1) {
				totalDuesOfStudent = new ArrayList<Object[]>();
				return "nodues";
			}
			totalDuesOfStudent = affDao.findTotalDuesOFFee(null, enrollmentNumber);
			log.info("List size is");
			Iterator<Object[]> itr = totalDuesOfStudent.iterator();
			while (itr.hasNext()) {
				Object[] dues = itr.next();
				Double netDue = dues[1] == null ? 0.0 : (Double) dues[1];
				Double originalDue = dues[2] == null ? 0.0 : (Double) dues[2];
				Double paymentToDate = dues[3] == null ? 0.0 : (Double) dues[3];
				totalNetDuesOFCollegeStudent = totalNetDuesOFCollegeStudent + netDue;
				totalOriginalDues = totalOriginalDues + originalDue;
				totalPaymentToDate = totalPaymentToDate + paymentToDate;
				log.info("total net due" + totalNetDuesOFCollegeStudent);
				log.info("total original due " + totalOriginalDues);
				log.info("total paid " + totalPaymentToDate);
			}
			return SUCCESS;

		} else if (ses.getAttribute("sesProfile").toString().contentEquals("Parent")||ses.getAttribute("sesProfile").toString().contentEquals("SU")) {
			collegeId = Integer.parseInt(request.getParameter("instId"));
			String courseName = request.getParameter("courseName");
			popUp = true;
			List<String> enrollmentNumber = affDao.findAllStudentOfInstituteByCourse(collegeId, courseName);
			if (enrollmentNumber.size() < 1) {
				totalDuesOfStudent = new ArrayList<Object[]>();
				return "nodues";
			}
			totalDuesOfStudent = affDao.findTotalDuesOFFee(null, enrollmentNumber);
			log.info("List size is");
			Iterator<Object[]> itr = totalDuesOfStudent.iterator();
			while (itr.hasNext()) {
				Object[] dues = itr.next();
				Double netDue = dues[1] == null ? 0.0 : (Double) dues[1];
				Double originalDue = dues[2] == null ? 0.0 : (Double) dues[2];
				Double paymentToDate = dues[3] == null ? 0.0 : (Double) dues[3];
				totalNetDuesOFCollegeStudent = totalNetDuesOFCollegeStudent + netDue;
				totalOriginalDues = totalOriginalDues + originalDue;
				totalPaymentToDate = totalPaymentToDate + paymentToDate;
				log.info("total net due" + totalNetDuesOFCollegeStudent);
				log.info("total original due " + totalOriginalDues);
				log.info("total paid " + totalPaymentToDate);
			}
			log.info("ppppppppppppppppppppppppppppppp");
			return "popUp";

		}
		return "popUp";

	}

	public String UpdateCalcParameters() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {
		AffFeePropBean feePropbean = new AffFeePropBean();

		// Get Fee Id
		Integer feeId = (Integer) ses.getAttribute("sesFeeId");
		// Get Institute Id
		Integer InsId = (Integer) ses.getAttribute("sesInstId");
		// Get Institute Bean from dB
		AffBean instBean = affDao.getOneCollegeRecord(InsId);
		// Get Institute Fee Property Bean from Institute Bean
		Set<AffFeePropBean> feeProp = instBean.getFeeProps();
		Iterator<AffFeePropBean> feeIt = feeProp.iterator();
		while (feeIt.hasNext()) {
			AffFeePropBean temp = feeIt.next();
			if (temp.getFeeId() == feeId) {
				feePropbean = temp;
			}

		}
		// Add Fee Calc Detail Beans to Institute Fee Property Bean and update
		// the Institute Bean
		Set<AffFeeCalcDetail> multipliers = new HashSet<AffFeeCalcDetail>(calcList);
		feePropbean.setMultipliers(multipliers);
		feePropbean.setCalcFlag(1);
		instBean.getFeeProps().add(feePropbean);
		ArrayList<AffFeePropBean> calculatedFees = calculateFee(instBean);
		Set<AffFeePropBean> calculateSet = new HashSet<AffFeePropBean>(calculatedFees);
		instBean.setFeeProps(calculateSet);

		affDao.saveOrUpdate(instBean, null);

		// Remove session Attributes
		ses.removeAttribute("sesInstId");
		ses.removeAttribute("sesFeeId");
		return SUCCESS;
	}

	private ArrayList<AffFeePropBean> calculateFee(AffBean institute) {
		// Get Associated Fees
		ArrayList<AffFeePropBean> tempList = new ArrayList<AffFeePropBean>(institute.getFeeProps());
		ArrayList<AffFeePropBean> resList = new ArrayList<AffFeePropBean>(institute.getFeeProps());
		// Get First Fee and see if it is fixed or per applicant
		Iterator<AffFeePropBean> tempIt = tempList.iterator();
		while (tempIt.hasNext()) {

			AffFeePropBean propBean = tempIt.next();
			PaymentDuesBean due = new PaymentDuesBean();
			FeeDetailsBean feedetail = feeDAO.GetFees("id", null, propBean.getFeeId(), null).get(0);
			List<FcBean> configs = new ArrayList<FcBean>();
			configs = feedetail.getConfigs();
			HashMap<Integer, Double> configMap = new HashMap<Integer, Double>();
			Iterator<FcBean> configIt = configs.iterator();
			while (configIt.hasNext()) {
				FcBean tempconfig = configIt.next();
				configMap.put(tempconfig.getComboId(), tempconfig.getAmount());
			}
			// if Fixed update amount
			if (feedetail.getCal_mode() == 0) {
				log.info("Calculation Mode is fixed,... development pending on fixed mode");
			}

			// if per Applicant see if calc multipliers have been set
			if (feedetail.getCal_mode() == 1) {
				Set<AffFeeCalcDetail> mults = propBean.getMultipliers();
				Double feeDue = (double) 0;
				// if set then calculate
				if (mults.size() > 0) {
					Iterator<AffFeeCalcDetail> mulIt = mults.iterator();
					while (mulIt.hasNext()) {
						AffFeeCalcDetail mul = mulIt.next();
						Double amount = configMap.get(mul.getComboId());
						feeDue = feeDue + (amount * mul.getMultiplier());

					}
					PaymentDuesBean dueBean = propBean.getDueBean();
					dueBean.setTotal_fee_amount(feeDue);
					dueBean.setDateCalculated(new Date());
					dueBean.setNetDue(dueBean.getTotal_fee_amount() - dueBean.getPayments_to_date());
					propBean.setDueBean(dueBean);
					resList.add(propBean);
				}
				// if no then set zero

			}

		}
		return resList;

	}

	public String CustomCollegeDueReport() {
		String universityName = request.getParameter("universityName");
		String collegeName = request.getParameter("collegeName");
		String courseName = request.getParameter("courseName");
		String feeName = request.getParameter("feeName");
		log.info("university name" + universityName);
		log.info("collegeName" + collegeName);
		log.info("courseName" + courseName);
		log.info("feeName" + feeName);
		// ////////////////////////////////////////////////////////////////////////////////////
		// start of su admin custom due report
		if (ses.getAttribute("sesProfile").toString().contentEquals("SU")) {

			// //user selected 4 value
			if ((universityName != null && !universityName.isEmpty())
					&& (collegeName != null && !collegeName.isEmpty()) && (courseName != null && !courseName.isEmpty())
					&& (feeName != null && !feeName.isEmpty())) {

				// user select all college and all course with specific fee
				if (collegeName.contentEquals("All") && courseName.contentEquals("All")) {
					Integer universityId = parDAO.getUniversityId(universityName);
					List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
					log.info("College ides is" + collegeIdes);
					List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
							null);
					log.info("enrollment Numbers of the student" + enrollmentNumberList);
					totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
					return SUCCESS;
				}
				// /end of both all

				// user select all college with specific course and specific fee
				else if (collegeName.contentEquals("All")) {
					Integer universityId = parDAO.getUniversityId(universityName);
					List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
					List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
							courseName);
					totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
					return SUCCESS;
				}
				// user select specific college and all course and feeName
				else if (courseName.contentEquals("All")) {
					Integer collegeId = affDao.getCollegeId(collegeName);
					List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null);
					log.info("Number of student" + enrollmentNumberList);
					totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
					return SUCCESS;

				}
				Integer collegeId = affDao.getCollegeId(collegeName);
				List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, courseName);
				log.info("Number of student" + enrollmentNumberList);
				totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
				return SUCCESS;

			}
			// //end of user selected all 4 values

			// start of user selected 3 value

			else if ((universityName != null && !universityName.isEmpty())
					&& (collegeName != null && !collegeName.isEmpty()) && (courseName != null && !courseName.isEmpty())
					|| (universityName != null && !universityName.isEmpty())
					&& (collegeName != null && !collegeName.isEmpty()) && (feeName != null && !feeName.isEmpty())
					|| (universityName != null && !universityName.isEmpty())
					&& (courseName != null && !courseName.isEmpty()) && (feeName != null && !feeName.isEmpty())
					|| (collegeName != null && !collegeName.isEmpty()) && (courseName != null && !courseName.isEmpty())
					&& (feeName != null && !feeName.isEmpty())) {
				if (collegeName.contentEquals("All") && courseName.contentEquals("All")) {
					Integer universityId = parDAO.getUniversityId(universityName);
					List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
					log.info("College ides is" + collegeIdes);
					List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
							null);
					log.info("enrollment Numbers of the student" + enrollmentNumberList);
					totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
					return SUCCESS;

				}

				// user select specific college and all course and feeName
				else if (courseName.contentEquals("All")) {
					Integer collegeId = affDao.getCollegeId(collegeName);
					List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null);
					log.info("Number of student" + enrollmentNumberList);
					totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
					return SUCCESS;

				}
				// user select all college with specific course and specific fee
				else if (collegeName.contentEquals("All")) {
					Integer universityId = parDAO.getUniversityId(universityName);
					List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
					List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
							courseName);
					totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
					return SUCCESS;
				}
				Integer collegeId = affDao.getCollegeId(collegeName);
				List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, courseName);
				log.info("Number of Student" + enrollmentNumberList);
				totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
				return SUCCESS;

			}
			// end of user selected 3 value
			// start of user selected 2 value
			else if ((universityName != null && !universityName.isEmpty())
					&& (collegeName != null && !collegeName.isEmpty())
					|| (universityName != null && !universityName.isEmpty())
					&& (courseName != null && !courseName.isEmpty())
					|| (universityName != null && !universityName.isEmpty()) && (feeName != null && !feeName.isEmpty())
					|| (collegeName != null && !collegeName.isEmpty()) && (courseName != null && !courseName.isEmpty())
					|| (collegeName != null && !collegeName.isEmpty()) && (feeName != null && !feeName.isEmpty())
					|| (courseName != null && !courseName.isEmpty()) && (feeName != null && !feeName.isEmpty())) {

				if (collegeName.contentEquals("All")) {
					Integer universityId = parDAO.getUniversityId(universityName);
					List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
					List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
							null);
					totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
					return SUCCESS;

				}
				Integer collegeId = affDao.getCollegeId(collegeName);
				List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null);
				log.info("Number of Student" + enrollmentNumberList);
				totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
				return SUCCESS;

			}
			// end of user selected 2 values

			// user selected only 1 value
			else if ((universityName != null && !universityName.isEmpty())
					|| (collegeName != null && !collegeName.isEmpty()) || (feeName != null && !feeName.isEmpty())
					|| (courseName != null && !courseName.isEmpty())) {

				Integer universityId = parDAO.getUniversityId(universityName);
				List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
				log.info("College ides is" + collegeIdes);
				List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes, null);
				log.info("enrollment Numbers of the student" + enrollmentNumberList);
				totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
				return SUCCESS;

			}

			// end of user selected 1 value
		}// end of super admin  report
			// /////////////////////////////////////////////////////////////////////////////////////////////////////////
			// start of university report
		else if (ses.getAttribute("sesProfile").toString().contentEquals("Parent")) {
			// start
			// user selected all three values
			if ((collegeName != null && !collegeName.isEmpty()) && (courseName != null && !courseName.isEmpty())
					&& (feeName != null && !feeName.isEmpty())) {
				// user select all college and all course and one fee
				if (collegeName.contentEquals("All") && courseName.contentEquals("All")) {
					Integer universityId = (Integer) ses.getAttribute("sesId");
					List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
					log.info("College ides is" + collegeIdes);
					List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
							null);
					log.info("enrollment Numbers of the student" + enrollmentNumberList);
					totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
					return SUCCESS;

				}

				// user select specific college and all course and feeName
				else if (courseName.contentEquals("All")) {
					Integer collegeId = affDao.getCollegeId(collegeName);
					List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null);
					log.info("Number of student" + enrollmentNumberList);
					totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
					return SUCCESS;

				}
				// user select all college with specific course and specific fee
				else if (collegeName.contentEquals("All")) {
					Integer universityId = (Integer) ses.getAttribute("sesId");
					List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
					List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
							courseName);
					totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
					return SUCCESS;
				}
				// user select specific college ,specific course and feeName
				Integer collegeId = affDao.getCollegeId(collegeName);
				List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, courseName);
				log.info("Number of student" + enrollmentNumberList);
				totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
				return SUCCESS;

			}
			// end of 3 value entered
			// for 2 value
			// user selected only two value
			else if ((collegeName != null && !collegeName.isEmpty()) && (courseName != null && !courseName.isEmpty())
					|| (collegeName != null && !collegeName.isEmpty()) && (feeName != null && !feeName.isEmpty())
					|| (courseName != null && !courseName.isEmpty()) && (feeName != null && !feeName.isEmpty())) {

				// user selected college and course
				if ((collegeName != null && !collegeName.isEmpty()) && (courseName != null && !courseName.isEmpty())) {

					//
					if (collegeName.contentEquals("All") && courseName.contentEquals("All")) {
						Integer universityId = (Integer) ses.getAttribute("sesId");
						List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
						log.info("College ides is" + collegeIdes);
						List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
								null);
						totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
						return SUCCESS;

					} else if (courseName.contentEquals("All")) {
						Integer collegeId = affDao.getCollegeId(collegeName);
						List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null);
						log.info("Number of student" + enrollmentNumberList);
						totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
						return SUCCESS;
					}

					else if (collegeName.contentEquals("All")) {
						Integer universityId = (Integer) ses.getAttribute("sesId");
						List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
						List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
								courseName);
						totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
						return SUCCESS;

					}

					Integer collegeId = affDao.getCollegeId(collegeName);
					List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, courseName);
					log.info("Number of Student" + enrollmentNumberList);
					totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
					return SUCCESS;
				}
				// end of user selected college and course

			}
			// end of 2 value entered

			// for 1 value
			// user entered only one value
			else if ((collegeName != null && !collegeName.isEmpty()) || (feeName != null && !feeName.isEmpty())
					|| (courseName != null && !courseName.isEmpty())) {

				// user entered on only college name
				if (collegeName != null && !collegeName.isEmpty()) {

					// user select all college
					if (collegeName.contains("All")) {
						Integer universityId = (Integer) ses.getAttribute("sesId");
						List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
						List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
								null);
						totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
						return SUCCESS;

					}
					// user selected specific college
					Integer collegeId = affDao.getCollegeId(collegeName);
					log.info("college Id is" + collegeId);
					List<String> enrollmentList = affDao.findAllStudentOfInstituteByCourse(collegeId, null);
					log.info("list of student" + enrollmentList);
					totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentList);
					return SUCCESS;
				}

			}

			// end of 1 value entered

		}// end of university
			// ////////////////////////////////////////////////////////////////////////////////
			// start of college reporting
			// reporting for institute
		else if (ses.getAttribute("sesProfile").toString().contentEquals("Affiliated")) {
			Integer collegeId = null;

			// user select all 2 value
			if ((feeName != null && !feeName.isEmpty()) && (courseName != null && !courseName.isEmpty())) {
				collegeId = (Integer) ses.getAttribute("sesId");
				List<String> enrollmentNumberList = null;
				enrollmentNumberList = courseName.contentEquals("All") ? affDao.findAllStudentOfInstituteByCourse(
						collegeId, null) : affDao.findAllStudentOfInstituteByCourse(collegeId, courseName);
				log.info("Number of Student" + enrollmentNumberList);
				totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
				log.info("Due list size is" + totalDuesOFCollege.size());
				return SUCCESS;
			}
			// /end of

			// user selected only one value
			else if ((feeName != null && !feeName.isEmpty()) || (courseName != null && !courseName.isEmpty())) {
				collegeId = (Integer) ses.getAttribute("sesId");
				List<String> enrollmentNumberList = null;
				enrollmentNumberList = courseName.contentEquals("All") ? affDao.findAllStudentOfInstituteByCourse(
						collegeId, null) : affDao.findAllStudentOfInstituteByCourse(collegeId, courseName);
				log.info("Number of Student" + enrollmentNumberList);
				totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
				log.info("Due list size is" + totalDuesOFCollege.size());
				return SUCCESS;

			}

			// end of selected only one value

		}
		// end of college reporting
		// /////////////////////////////////////////////////////////////////////////////////////////////

		return SUCCESS;
	}

	public String getValForDropDown() {
		String collegeName = request.getParameter("collegeName");
		String universityName = request.getParameter("universityName");
		String courseName = request.getParameter("courseName");
		String feeName = request.getParameter("");
		String ajax = request.getParameter("");
		if (ses.getAttribute("sesProfile").toString().contentEquals("SU")) {

			if (universityName != null && !universityName.isEmpty()) {
				Integer universityId = parDAO.getUniversityId(universityName);
				ses.setAttribute("UniversityId", universityId);
				affBeansList = affDao.getCollegList(universityId);
				log.info("List of college" + affBeansList.size());
				return "collegeList";
			}
			if (collegeName != null && !collegeName.isEmpty()) {

				if (collegeName.contentEquals("All")) {

					listOfCourse = affDao.getListOfCourses(null,
							parDAO.getIdesOfAllCollege((Integer) ses.getAttribute("UniversityId")));
					log.info("list of course" + listOfCourse.size());
					return "listOfCourse";
				}

				Integer id = affDao.getCollegeId(collegeName);
				log.info("id of the college is" + id);
				listOfCourse = affDao.getListOfCourses(id, null);
				log.info("list of course" + listOfCourse.size());
				return "listOfCourse";
			}
			if (courseName != null && !courseName.isEmpty()) {
				if (courseName.contentEquals("All")) {
					feeNameList = feeDAO.getFeeNames(null);
					log.info("fee name list size" + feeNameList.size());
					return "feeName";

				}
				String applicableFeeString = appDAO.getApplicableFeesString(courseName);
				String[] FeeIdArray = applicableFeeString.split("~");
				List<Integer> feeIdes = new ArrayList<Integer>();
				for (String feeId : FeeIdArray) {
					feeIdes.add(Integer.parseInt(feeId));
				}
				feeNameList = feeDAO.getFeeNames(feeIdes);
				log.info("fee name list size" + feeNameList.size());
				return "feeName";

			}

			parBeans = parDAO.getUniversityList();
			return SUCCESS;

		}

		else if (ses.getAttribute("sesProfile").toString().contentEquals("CollegeOperator")) {

		}

		// dynamic drop down generation for university
		// start
		else if (ses.getAttribute("sesProfile").toString().contentEquals("Parent")) {
			Integer universityId = (Integer) ses.getAttribute("sesId");
			// user selected college name
			if (collegeName != null && !collegeName.isEmpty()) {

				if (collegeName.contentEquals("All")) {

					listOfCourse = affDao.getListOfCourses(null, parDAO.getIdesOfAllCollege(universityId));
					log.info("list of course" + listOfCourse.size());
					return "listOfCourse";
				}

				Integer id = affDao.getCollegeId(collegeName);
				log.info("id of the college is" + id);
				listOfCourse = affDao.getListOfCourses(id, null);
				log.info("list of course" + listOfCourse.size());
				return "listOfCourse";
			}
			if (courseName != null && !courseName.isEmpty()) {
				if (courseName.contentEquals("All")) {
					feeNameList = feeDAO.getFeeNames(null);
					log.info("fee name list size" + feeNameList.size());
					return "feeName";

				}
				String applicableFeeString = appDAO.getApplicableFeesString(courseName);
				String[] FeeIdArray = applicableFeeString.split("~");
				List<Integer> feeIdes = new ArrayList<Integer>();
				for (String feeId : FeeIdArray) {
					feeIdes.add(Integer.parseInt(feeId));
				}
				feeNameList = feeDAO.getFeeNames(feeIdes);
				log.info("fee name list size" + feeNameList.size());
				return "feeName";

			}
			Integer id = (Integer) ses.getAttribute("sesId");
			affBeansList = affDao.getCollegList(id);
			log.info("List of college" + affBeansList.size());
			return SUCCESS;

		}

		// end of dynamic drop down generation for university

		// start of drop down generation for college
		else if (ses.getAttribute("sesProfile").toString().contentEquals("Affiliated")) {

			// user selects course name
			if (courseName != null && !courseName.isEmpty()) {
				// if user selects all course then generate correspondent
				// feeName
				if (courseName.contentEquals("All")) {
					feeNameList = feeDAO.getFeeNames(null);
					log.info("fee name list size" + feeNameList.size());
					return "feeName";

				}
				// if user select specific course
				String applicableFeeString = appDAO.getApplicableFeesString(courseName);
				String[] FeeIdArray = applicableFeeString.split("~");
				List<Integer> feeIdes = new ArrayList<Integer>();
				for (String feeId : FeeIdArray) {
					feeIdes.add(Integer.parseInt(feeId));
				}
				feeNameList = feeDAO.getFeeNames(feeIdes);
				log.info("fee name list size" + feeNameList.size());
				return "feeName";
			}
			Integer id = (Integer) ses.getAttribute("sesId") == null ? affDao.getCollegeId(collegeName) : (Integer) ses
					.getAttribute("sesId");
			log.info("id is" + id);
			listOfCourse = affDao.getListOfCourses(id, null);
			log.info("list of course" + listOfCourse.size());
			return SUCCESS;

		}

		// end of drop down generation for college

		return SUCCESS;
	}

	public String getInsTransactionDetails() {
		if (ses.getAttribute("sesProfile").toString().contentEquals("SU")) {
			transactionDetailsForReport = affDao.getAllTransactionDetail();
			return SUCCESS;
		} else if (ses.getAttribute("sesProfile").toString().contentEquals("CollegeOperator")) {
			Integer operatorId = (Integer) ses.getAttribute("opratorId");
			Integer instituteId = opratorDAO.getCollegeIdOfOperator(operatorId);
			log.info("insitute id is" + instituteId);
			transactionDetailsForReport = affDao.getAllTransactionDetail(instituteId);

			return SUCCESS;
		} else if (ses.getAttribute("sesProfile").toString().contentEquals("Student")) {
			String enrollmentNumber = (String) ses.getAttribute("StudentEnrollId");
			transactionDetailsForReport = affDao.getStudentTransactionDetails(enrollmentNumber);
			return SUCCESS;
		}

		else if (ses.getAttribute("sesProfile").toString().contentEquals("Parent")) {

			ParBean parBean = affDao.getTrasactionReportForUniversity((Integer) ses.getAttribute("sesId"));
			List<Integer> allCollegeId = parDAO.getIdesOfAllCollege((Integer) ses.getAttribute("sesId"));
			transactionDetailsForReport = affDao.getTransactionOfColleges(allCollegeId);
			log.info("Number of Transaction Done by Colleges Belongs to That University"
					+ transactionDetailsForReport.size());
			return SUCCESS;
		}

		else {
			Integer insId = (Integer) ses.getAttribute("sesId");
			log.info("ins id is=" + insId);
			transactionDetailsForReport = affDao.getAllTransactionDetail(insId);
			return SUCCESS;
		}
	}

	// End of Action Methods
	// ---------------------------------------------------

	// Keep Getter Setter Methods Here
	public AffBean getAffInstBean() {
		return affInstBean;
	}

	public void setAffInstBean(AffBean affInstBean) {
		this.affInstBean = affInstBean;
	}

	public void setAffInstList(ArrayList<AffBean> affInstList) {
		this.affInstList = affInstList;
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

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public FileInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(FileInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public ArrayList<FeeDetailsBean> getFeeList() {
		return feeList;
	}

	public void setFeeList(ArrayList<FeeDetailsBean> feeList) {
		this.feeList = feeList;
	}

	public Boolean getSaved() {
		return saved;
	}

	public void setSaved(Boolean saved) {
		this.saved = saved;
	}

	public ArrayList<AffBean> getFailureAffBeanList() {
		return failureAffBeanList;
	}

	public void setFailureAffBeanList(ArrayList<AffBean> failureAffBeanList) {
		this.failureAffBeanList = failureAffBeanList;
	}

	public ArrayList<AffBean> getExistingInstitureRecordList() {
		return existingInstitureRecordList;
	}

	public void setExistingInstitureRecordList(ArrayList<AffBean> existingInstitureRecordList) {
		this.existingInstitureRecordList = existingInstitureRecordList;
	}

	public ArrayList<Integer> getParamIds() {
		return paramIds;
	}

	public void setParamIds(ArrayList<Integer> paramIds) {
		this.paramIds = paramIds;
	}

	public ArrayList<LookupBean> getParamList2() {
		return paramList2;
	}

	public void setParamList2(ArrayList<LookupBean> paramList2) {
		this.paramList2 = paramList2;
	}

	public AffFeePropBean getPropbean() {
		return propbean;
	}

	public void setPropbean(AffFeePropBean propbean) {
		this.propbean = propbean;
	}

	public ArrayList<AffFeePropBean> getDueList() {
		return dueList;
	}

	public void setDueList(ArrayList<AffFeePropBean> dueList) {
		this.dueList = dueList;
	}

	public List<AffBean> getAffBeansList() {
		return affBeansList;
	}

	public void setAffBeansList(List<AffBean> affBeansList) {
		this.affBeansList = affBeansList;
	}

	public void setAffBeansList(ArrayList<AffBean> affBeansList) {
		this.affBeansList = affBeansList;
	}

	public ArrayList<AffFeeCalcDetail> getCalcList() {
		return calcList;
	}

	public void setCalcList(ArrayList<AffFeeCalcDetail> calcList) {
		this.calcList = calcList;
	}

	public Integer getParInstId() {
		return parInstId;
	}

	public void setParInstId(Integer parInstId) {
		this.parInstId = parInstId;
	}

	public ParBean getParBean() {
		return parBean;
	}

	public void setParBean(ParBean parBean) {
		this.parBean = parBean;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public ArrayList<PaymentDuesBean> getCollegeDueReport() {
		return collegeDueReport;
	}

	public void setCollegeDueReport(ArrayList<PaymentDuesBean> collegeDueReport) {
		this.collegeDueReport = collegeDueReport;
	}

	public List<TransactionBean> getTransactionDetailsForReport() {
		return transactionDetailsForReport;
	}

	public void setTransactionDetailsForReport(List<TransactionBean> transactionDetailsForReport) {
		this.transactionDetailsForReport = transactionDetailsForReport;
	}

	public List<ParBean> getParBeansList() {
		return parBeansList;
	}

	public void setParBeansList(List<ParBean> parBeansList) {
		this.parBeansList = parBeansList;
	}

	public List<Object[]> getTotalDuesOfStudent() {
		return totalDuesOfStudent;
	}

	public void setTotalDuesOfStudent(List<Object[]> totalDuesOfStudent) {
		this.totalDuesOfStudent = totalDuesOfStudent;
	}

	public Double getTotalNetDuesOFCollegeStudent() {
		return totalNetDuesOFCollegeStudent;
	}

	public void setTotalNetDuesOFCollegeStudent(Double totalNetDuesOFCollegeStudent) {
		this.totalNetDuesOFCollegeStudent = totalNetDuesOFCollegeStudent;
	}

	public Double getTotalPaymentToDate() {
		return totalPaymentToDate;
	}

	public void setTotalPaymentToDate(Double totalPaymentToDate) {
		this.totalPaymentToDate = totalPaymentToDate;
	}

	public Double getTotalOriginalDues() {
		return totalOriginalDues;
	}

	public void setTotalOriginalDues(Double totalOriginalDues) {
		this.totalOriginalDues = totalOriginalDues;
	}

	public List<ParBean> getParBeans() {
		return parBeans;
	}

	public void setParBeans(List<ParBean> parBeans) {
		this.parBeans = parBeans;
	}

	public List<String> getListOfCourse() {
		return listOfCourse;
	}

	public void setListOfCourse(List<String> listOfCourse) {
		this.listOfCourse = listOfCourse;
	}

	public List<String> getFeeNameList() {
		return feeNameList;
	}

	public void setFeeNameList(List<String> feeNameList) {
		this.feeNameList = feeNameList;
	}

	public List<Object[]> getTotalDuesOFCollege() {
		return totalDuesOFCollege;
	}

	public void setTotalDuesOFCollege(List<Object[]> totalDuesOFCollege) {
		this.totalDuesOFCollege = totalDuesOFCollege;
	}

	public List<LookupBean> getLookupBeanList() {
		return lookupBeanList;
	}

	public void setLookupBeanList(List<LookupBean> lookupBeanList) {
		this.lookupBeanList = lookupBeanList;
	}

	// End of Getter Setter Methods
}
