package com.dexpert.feecollection.main.users.affiliated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.views.jsp.TagUtils;

import com.dexpert.feecollection.challan.TransactionBean;
import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.communication.sms.SendSMS;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.config.FcBean;
import com.dexpert.feecollection.main.fee.config.FcDAO;
import com.dexpert.feecollection.main.fee.config.FeeDetailsBean;
import com.dexpert.feecollection.main.fee.config.FeeStructureData;
import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.fee.lookup.LookupDAO;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvDAO;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.RandomPasswordGenerator;
import com.dexpert.feecollection.main.users.applicant.AppDAO;
import com.dexpert.feecollection.main.users.operator.OperatorDao;
import com.dexpert.feecollection.main.users.parent.ParBean;
import com.dexpert.feecollection.main.users.parent.ParDAO;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

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
	private String totalNetDuesOFCollegeStudent = "";
	private String totalPaymentToDate = "";
	private String totalOriginalDues = "";

	List<String> listOfCourse;
	List<String> feeNameList;
	AppDAO appDAO = new AppDAO();
	List<Object[]> totalDuesOFCollege = new ArrayList<Object[]>();
	AffBean affBean = new AffBean();
	FvDAO fvDAO = new FvDAO();
	List<FvBean> lookUpvalueList;
	String courses;
	List<CollegeCourses> allCourseOfInst;

	// End of Global Variables

	// ---------------------------------------------------

	// Action Methods Here

	// registerInstitute
	public String registerInstitute() throws Exception {

		// log.info("paramset is "+affInstBean.getParamvalues().toString());
		List<String> instNameList = affDao.getCollegeNameList(affInstBean.getInstName());
		/* log.info("List Size is ::" + instNameList.size()); */

		ParDAO parDao = new ParDAO();
		HttpSession httpSession = request.getSession();
		LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");
		System.out.println(parInstId);
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
				//affInstBean.getLoginBean().setProfile("Institute");
				LoginBean creds = new LoginBean();

				creds.setPassword(encryptedPwd);
				creds.setUserName(username);
				
				creds.setProfile(affInstBean.getLoginBean().getProfile());
				ParBean parBean1 = new ParBean();
				parBean1 = parDAO.viewUniversity(parInstId);

				// one to many and many to one relationship
				parBean1.getAffBeanOneToManySet().add(affInstBean);
				affInstBean.setParBeanAff(parBean1);

				//parDAO.saveOrUpdate(parBean1, null);

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

				// code to send msg
				String user = username;
				String pass = password;
				String msg = "UserId :" + user + "" + " Passsword : " + pass;
				SendSMS sms = new SendSMS();
				sms.sendSMS(affInstBean.getMobileNum(), msg);

				// -----Code for sending email//--------------------
				String emailContent = "Welcome to the FeeDesk portal of " + affInstBean.getInstName()
						+ ". You can log in with the below credentials. ";
				EmailSessionBean email = new EmailSessionBean();
				email.sendEmail(affInstBean.getEmail(), "Welcome To FeeDesk!", username, password,
						affInstBean.getInstName(), emailContent);

				request.setAttribute("msg", "Institute Added Successfully");
				request.setAttribute("redirectLink", "Success.jsp");
				return SUCCESS;

			}
		}

		// else forward error message on input page

		else {
			log.info("courses" + courses);
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

	public String getAllClgList() {
		Integer parentInstId = (Integer) ses.getAttribute("parentInstId");
		affInstList = affDao.getCollegesList(parentInstId);
		return SUCCESS;
	}

	public String getClgListForSA() {
		affInstList = affDao.getAllCollegeList();
		return SUCCESS;

	}

	public String getCollegeList() {
		HttpSession httpSession = request.getSession();
		// LoginBean loginBean = (LoginBean)
		// httpSession.getAttribute("loginUserBean");
		Integer instId = (Integer) httpSession.getAttribute("instId");
		List<Integer> structureIdes = affDao.getStrutureId(instId, null);
		log.info("Struture id" + structureIdes);
		try {
			List<Integer> valueIdes = feeDAO.getLookupValue(structureIdes);
			log.info("value ides got from fee config table::::::" + valueIdes);
			List<Integer> lookUpParamList = fvDAO.getListOfValueBeans(valueIdes);
			log.info("look up param list::::::" + lookUpParamList);
			lookupBeanList = lookupdao.getListOfLookUpValues("Applicant", lookUpParamList, valueIdes);
			log.info("look up List Size is ::" + lookupBeanList.size());
			allCourseOfInst = affDao.getAllCourseOfInst(instId);
			return SUCCESS;
		} catch (NullPointerException npe) {
			String message = "Please Add the Courses Before Adding the Student ";
			request.setAttribute("addCourse", "true");
			request.setAttribute("msg", message);
			return ERROR;
		} catch (Exception ex) {
			ex.printStackTrace();
			String message = "Please configure the Fee Before Adding the Student";
			request.setAttribute("msg", message);
			return ERROR;
		}
	}

	// get collgege list based on university ID
	public String getUniversityCollegeList() {
		try {
			HttpSession httpSession = request.getSession();
			// LoginBean loginBean = (LoginBean)
			// httpSession.getAttribute("loginUserBean");
			Integer parentInstId = (Integer) httpSession.getAttribute("parentInstId");
			parBean = affDao.getUniversityCollegeList(parentInstId);
			log.info("University Names ::" + parBean.getParInstName());
			return SUCCESS;
		} catch (java.lang.NullPointerException e) {
			request.setAttribute("msg", "Session Time Out");
			return ERROR;
		}

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
			savedata.getCollegeParamvalues().add(valueMap.get(paramIds.get(i)));
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
		feeList = feeDAO.GetFees("payee", "institute", null, null, null);
		for (int j = 0; j < instfeeList.size(); j++) {

			for (int i = 0; i < feeList.size(); i++) {
				if (instfeeList.get(j).getFeeId() == feeList.get(i).getFeeId()) {
					feeList.get(i).setGenericFlag(1);
				} else {
					feeList.get(i).setGenericFlag(1);
				}
			}

		}

		return SUCCESS;
	}

	public String CloneFeesValidate() {
		Integer instId = Integer.parseInt(request.getParameter("instId").trim());

		AffBean instbean = affDao.getOneCollegeRecord(instId);
		ses.setAttribute("sesCloneBean", instbean);

		if (instbean.getFeeSet().size() > 0) {
			request.setAttribute("msg", "Cloning only allowed for institutes which do not have fees already associated");
			request.setAttribute("redirectlink", "window.close()");
			return ERROR;
		}
		affInstList = affDao.getInstitutes("NONE", null, null, null, null);
		Iterator<AffBean> instIt = affInstList.iterator();
		while (instIt.hasNext()) {
			AffBean temp = instIt.next();
			if (temp.getInstId() == instId) {
				instIt.remove();
			}
		}

		return SUCCESS;
	}

	public String CloneFees() throws Exception {
		AffBean destInst = (AffBean) ses.getAttribute("sesCloneBean");
		ses.removeAttribute("sesCloneBean");
		AffBean instbean = affDao.getOneCollegeRecord(affInstBean.getInstId());
		ArrayList<FeeDetailsBean> feeList = new ArrayList<FeeDetailsBean>(instbean.getFeeSet());
		Set<FeeDetailsBean> feeSet = new HashSet<FeeDetailsBean>(feeList);
		ArrayList<FeeStructureData> structures = new ArrayList<FeeStructureData>(affDao.getInstStructures(instbean
				.getInstId()));
		ArrayList<FeeStructureData> newStructures = new ArrayList<FeeStructureData>();
		Iterator<FeeStructureData> structIt = structures.iterator();
		while (structIt.hasNext()) {
			FeeStructureData temp = structIt.next();
			FeeStructureData newstruct = new FeeStructureData();
			newstruct.setFee_id(temp.getFee_id());
			newstruct.setStructure_id(temp.getStructure_id());
			newstruct.setInst_id(destInst.getInstId());
			newStructures.add(newstruct);
		}
		feeDAO.insertFeeStructureBulk(newStructures);
		destInst.setFeeSet(feeSet);
		affDao.saveOrUpdate(destInst, null);

		request.setAttribute("msg", "Fees successfully Cloned");
		request.setAttribute("redirectlink", "window.close()");
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
			feelist = feeDAO.GetFees("ids", null, null, FeeIdsInt, null);
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

	// this method is for converting large amount into original
	public String convertIntoOriginalAmount(Double amount) {
		String originalAmount = BigDecimal.valueOf(Double.valueOf(amount.toString())).toPlainString();
		return originalAmount;
	}

	// to get College Due Report

	public String collegeDueReport() {
		boolean popUp = false;
		Integer collegeId = null;
		// collegeDueReport = (ArrayList<PaymentDuesBean>)
		// affDao.collegeDueReport();
		// log.info("list size of college Due" + collegeDueReport.size());

		String forDownload = request.getParameter("forDownload");
		popUp = request.getParameter("popUp") == null ? false : true;
		Double totalNetDuesOFCollegeStudent = 0.0;
		Double totalOriginalDues = 0.0;
		Double totalPaymentToDate = 0.0;
		if (ses.getAttribute("sesProfile").toString().contentEquals("Affiliated")) {
			collegeId = (Integer) ses.getAttribute("sesId");
			String courseName = request.getParameter("courseName");
			// String feeName=request.getParameter("feeName");
			List<String> enrollmentNumber = affDao.findAllStudentOfInstituteByCourse(collegeId, courseName, null);
			if (enrollmentNumber.size() < 1) {
				totalDuesOfStudent = new ArrayList<Object[]>();
				String result = popUp == true ? "popUp" : "success";
				return result;
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
			this.totalNetDuesOFCollegeStudent = convertIntoOriginalAmount(totalNetDuesOFCollegeStudent);
			this.totalOriginalDues = convertIntoOriginalAmount(totalOriginalDues);
			this.totalPaymentToDate = convertIntoOriginalAmount(totalPaymentToDate);
			String result = popUp == true ? "popUp" : "success";
			result = forDownload != null && forDownload.contentEquals("true") ? "forDownload" : result;

			return result;

		} else if (ses.getAttribute("sesProfile").toString().contentEquals("Parent")
				|| ses.getAttribute("sesProfile").toString().contentEquals("SU")) {
			collegeId = Integer.parseInt(request.getParameter("instId"));
			String courseName = request.getParameter("courseName");
			popUp = true;
			List<String> enrollmentNumber = affDao.findAllStudentOfInstituteByCourse(collegeId, courseName, null);
			if (enrollmentNumber.size() < 1) {
				totalDuesOfStudent = new ArrayList<Object[]>();
				return "popUp";
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
			this.totalNetDuesOFCollegeStudent = convertIntoOriginalAmount(totalNetDuesOFCollegeStudent);
			this.totalOriginalDues = convertIntoOriginalAmount(totalOriginalDues);
			this.totalPaymentToDate = convertIntoOriginalAmount(totalPaymentToDate);

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
			FeeDetailsBean feedetail = feeDAO.GetFees("id", null, propBean.getFeeId(), null, null).get(0);
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
		// log.info("university name" + universityName);
		// log.info("collegeName" + collegeName);
		// log.info("courseName" + courseName);
		// log.info("feeName" + feeName);
		// ////////////////////////////////////////////////////////////////////////////////////
		// start of su admin custom due report
		if (ses.getAttribute("sesProfile").toString().contentEquals("SU")) {

			/*
			 * // //user selected 4 value if ((universityName != null &&
			 * !universityName.isEmpty()) && (collegeName != null &&
			 * !collegeName.isEmpty()) && (courseName != null &&
			 * !courseName.isEmpty()) && (feeName != null &&
			 * !feeName.isEmpty())) {
			 * 
			 * // user select all college and all course with specific fee if
			 * (collegeName.contentEquals("All") &&
			 * courseName.contentEquals("All")) { Integer universityId =
			 * parDAO.getUniversityId(universityName); List<Integer> collegeIdes
			 * = parDAO.getIdesOfAllCollege(universityId);
			 * log.info("College ides is" + collegeIdes); List<String>
			 * enrollmentNumberList =
			 * affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
			 * null); log.info("enrollment Numbers of the student" +
			 * enrollmentNumberList); totalDuesOFCollege =
			 * affDao.findDueOfFees(feeName, enrollmentNumberList); return
			 * SUCCESS; } // /end of both all
			 * 
			 * // user select all college with specific course and specific fee
			 * else if (collegeName.contentEquals("All")) { Integer universityId
			 * = parDAO.getUniversityId(universityName); List<Integer>
			 * collegeIdes = parDAO.getIdesOfAllCollege(universityId);
			 * List<String> enrollmentNumberList =
			 * affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
			 * courseName); totalDuesOFCollege = affDao.findDueOfFees(feeName,
			 * enrollmentNumberList); return SUCCESS; } // user select specific
			 * college and all course and feeName else if
			 * (courseName.contentEquals("All")) { Integer collegeId =
			 * affDao.getCollegeId(collegeName); List<String>
			 * enrollmentNumberList =
			 * affDao.findAllStudentOfInstituteByCourse(collegeId, null,null);
			 * log.info("Number of student" + enrollmentNumberList);
			 * totalDuesOFCollege = affDao.findDueOfFees(feeName,
			 * enrollmentNumberList); return SUCCESS;
			 * 
			 * } Integer collegeId = affDao.getCollegeId(collegeName);
			 * List<String> enrollmentNumberList =
			 * affDao.findAllStudentOfInstituteByCourse(collegeId,
			 * courseName,null); log.info("Number of student" +
			 * enrollmentNumberList); totalDuesOFCollege =
			 * affDao.findDueOfFees(feeName, enrollmentNumberList); return
			 * SUCCESS;
			 * 
			 * } // //end of user selected all 4 values
			 * 
			 * // start of user selected 3 value
			 * 
			 * else if ((universityName != null && !universityName.isEmpty()) &&
			 * (collegeName != null && !collegeName.isEmpty()) && (courseName !=
			 * null && !courseName.isEmpty()) || (universityName != null &&
			 * !universityName.isEmpty()) && (collegeName != null &&
			 * !collegeName.isEmpty()) && (feeName != null &&
			 * !feeName.isEmpty()) || (universityName != null &&
			 * !universityName.isEmpty()) && (courseName != null &&
			 * !courseName.isEmpty()) && (feeName != null && !feeName.isEmpty())
			 * || (collegeName != null && !collegeName.isEmpty()) && (courseName
			 * != null && !courseName.isEmpty()) && (feeName != null &&
			 * !feeName.isEmpty())) {
			 * 
			 * 
			 * if (collegeName.contentEquals("All") &&
			 * courseName.contentEquals("All")) { Integer universityId =
			 * parDAO.getUniversityId(universityName); List<Integer> collegeIdes
			 * = parDAO.getIdesOfAllCollege(universityId);
			 * log.info("College ides is" + collegeIdes); List<String>
			 * enrollmentNumberList =
			 * affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
			 * null); log.info("enrollment Numbers of the student" +
			 * enrollmentNumberList); totalDuesOFCollege =
			 * affDao.findDueOfFees(feeName, enrollmentNumberList); return
			 * SUCCESS;
			 * 
			 * }
			 * 
			 * // user select specific college and all course and feeName else
			 * if (courseName.contentEquals("All")) { Integer collegeId =
			 * affDao.getCollegeId(collegeName); List<String>
			 * enrollmentNumberList =
			 * affDao.findAllStudentOfInstituteByCourse(collegeId, null,null);
			 * log.info("Number of student" + enrollmentNumberList);
			 * totalDuesOFCollege = affDao.findDueOfFees(null,
			 * enrollmentNumberList); return SUCCESS;
			 * 
			 * } // user select all college with specific course and specific
			 * fee else if (collegeName.contentEquals("All")) { Integer
			 * universityId = parDAO.getUniversityId(universityName);
			 * List<Integer> collegeIdes =
			 * parDAO.getIdesOfAllCollege(universityId); List<String>
			 * enrollmentNumberList =
			 * affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
			 * courseName); totalDuesOFCollege = affDao.findDueOfFees(null,
			 * enrollmentNumberList); return SUCCESS; } //user selected specific
			 * college ,course, Integer collegeId =
			 * affDao.getCollegeId(collegeName); List<String>
			 * enrollmentNumberList =
			 * affDao.findAllStudentOfInstituteByCourse(collegeId, courseName);
			 * log.info("Number of Student" + enrollmentNumberList);
			 * totalDuesOFCollege = affDao.findDueOfFees(null,
			 * enrollmentNumberList); return SUCCESS;
			 * 
			 * } // end of user selected 3 value // start of user selected 2
			 * value else if ((universityName != null &&
			 * !universityName.isEmpty()) && (collegeName != null &&
			 * !collegeName.isEmpty()) || (universityName != null &&
			 * !universityName.isEmpty()) && (courseName != null &&
			 * !courseName.isEmpty()) || (universityName != null &&
			 * !universityName.isEmpty()) && (feeName != null &&
			 * !feeName.isEmpty()) || (collegeName != null &&
			 * !collegeName.isEmpty()) && (courseName != null &&
			 * !courseName.isEmpty()) || (collegeName != null &&
			 * !collegeName.isEmpty()) && (feeName != null &&
			 * !feeName.isEmpty()) || (courseName != null &&
			 * !courseName.isEmpty()) && (feeName != null &&
			 * !feeName.isEmpty())) {
			 * 
			 * if (collegeName.contentEquals("All")) { Integer universityId =
			 * parDAO.getUniversityId(universityName); List<Integer> collegeIdes
			 * = parDAO.getIdesOfAllCollege(universityId); List<String>
			 * enrollmentNumberList =
			 * affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
			 * null); totalDuesOFCollege = affDao.findDueOfFees(null,
			 * enrollmentNumberList); return SUCCESS;
			 * 
			 * } Integer collegeId = affDao.getCollegeId(collegeName);
			 * List<String> enrollmentNumberList =
			 * affDao.findAllStudentOfInstituteByCourse(collegeId, null);
			 * log.info("Number of Student" + enrollmentNumberList);
			 * totalDuesOFCollege = affDao.findDueOfFees(null,
			 * enrollmentNumberList); return SUCCESS;
			 * 
			 * } // end of user selected 2 values
			 * 
			 * // user selected only 1 value else if ((universityName != null &&
			 * !universityName.isEmpty()) || (collegeName != null &&
			 * !collegeName.isEmpty()) || (feeName != null &&
			 * !feeName.isEmpty()) || (courseName != null &&
			 * !courseName.isEmpty())) {
			 * 
			 * Integer universityId = parDAO.getUniversityId(universityName);
			 * List<Integer> collegeIdes =
			 * parDAO.getIdesOfAllCollege(universityId);
			 * log.info("College ides is" + collegeIdes); List<String>
			 * enrollmentNumberList =
			 * affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
			 * null); log.info("enrollment Numbers of the student" +
			 * enrollmentNumberList); totalDuesOFCollege =
			 * affDao.findDueOfFees(null, enrollmentNumberList); return SUCCESS;
			 * 
			 * }
			 */
			request.setAttribute("msg", "Not Available for Super Admin Please Check Other Reports");
			return SUCCESS;
			// end of user selected 1 value
		}// end of super admin report
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
					List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null, null);
					log.info("Number of student" + enrollmentNumberList);
					totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
					return SUCCESS;

				}
				// user select all college with specific course and specific fee
				// Problem have to find out the solution
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
				List<Integer> structureIdes = affDao.getStrutureId(collegeId, null);
				List<Integer> valuesId = feeDAO.getValueIdByStructureIdes(structureIdes);
				Integer idOfCourse = fvDAO.valueIdOfCourse(valuesId, courseName).get(0);
				List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null,
						idOfCourse);
				log.info("Number of student" + enrollmentNumberList);
				totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
				return SUCCESS;

			}
			// end of 3 value entered
			// for 2 value
			// user selected only two value (parentlogin)
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
						List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null,
								null);
						log.info("Number of student" + enrollmentNumberList);
						totalDuesOFCollege = affDao.findDueOfFees(feeName, enrollmentNumberList);
						return SUCCESS;
					}
					// Problem have to address (Parent login)
					else if (collegeName.contentEquals("All")) {
						Integer universityId = (Integer) ses.getAttribute("sesId");
						List<Integer> collegeIdes = parDAO.getIdesOfAllCollege(universityId);
						List<String> enrollmentNumberList = affDao.findEnrollmentNumberOfMoreTheOneCollege(collegeIdes,
								courseName);
						totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
						return SUCCESS;

					}
					// user selected only specific college and specific course
					// like(college Name="LPS" and Course=x) (Parent Login)
					Integer collegeId = affDao.getCollegeId(collegeName);
					List<Integer> structureIdes = affDao.getStrutureId(collegeId, null);
					List<Integer> valuesId = feeDAO.getValueIdByStructureIdes(structureIdes);
					Integer idOfCourse = fvDAO.valueIdOfCourse(valuesId, courseName).get(0);
					List<String> enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, courseName,
							idOfCourse);
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
					List<String> enrollmentList = affDao.findAllStudentOfInstituteByCourse(collegeId, null, null);
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
				/*
				 * enrollmentNumberList = courseName.contentEquals("All") ?
				 * affDao.findAllStudentOfInstituteByCourse( collegeId, null) :
				 * affDao.findAllStudentOfInstituteByCourse(collegeId,
				 * courseName);
				 */if (courseName.contentEquals("All")) {
					enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null, null);
				} else {
					// user selected only one course find enrollment number of
					// student belongs to that course
					// find structure ides of college
					List<Integer> structureIdes = affDao.getStrutureId(collegeId, null);
					List<Integer> valuesId = feeDAO.getValueIdByStructureIdes(structureIdes);
					Integer idOfCourse = fvDAO.valueIdOfCourse(valuesId, courseName).get(0);
					enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null, idOfCourse);
				}

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
				if (courseName.contentEquals("All")) {
					enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null, null);
				} else {
					// user selected only one course find enrollment number of
					// student belongs to that course
					// find structure ids of college
					List<Integer> structureIdes = affDao.getStrutureId(collegeId, null);
					List<Integer> valuesId = feeDAO.getValueIdByStructureIdes(structureIdes);
					Integer idOfCourse = fvDAO.valueIdOfCourse(valuesId, courseName).get(0);
					enrollmentNumberList = affDao.findAllStudentOfInstituteByCourse(collegeId, null, idOfCourse);
				}
				log.info("Number of Student" + enrollmentNumberList);
				totalDuesOFCollege = affDao.findDueOfFees(null, enrollmentNumberList);
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
		String forStudentReport = request.getParameter("forStudentReport");

		// this code for generate dynamic drop down for student report
		if (ses.getAttribute("sesProfile").toString().contentEquals("SU") && forStudentReport != null
				&& !forStudentReport.isEmpty()) {
			if (universityName != null && !universityName.isEmpty()) {
				log.info(" for student Report ");
				Integer universityId = parDAO.getUniversityId(universityName);
				// ses.setAttribute("UniversityId", universityId);
				affBeansList = affDao.getCollegList(universityId);
				log.info("List of college" + affBeansList.size());
				return "collegeListForStudentReport";
			}
			parBeans = parDAO.getUniversityList();
		}
		if (ses.getAttribute("sesProfile").toString().contentEquals("Parent") && forStudentReport != null
				&& !forStudentReport.isEmpty()) {
			Integer id = (Integer) ses.getAttribute("sesId");
			affBeansList = affDao.getCollegList(id);
			log.info("List of college" + affBeansList.size());
			return SUCCESS;

		}

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
					ses.setAttribute("collegeIdFRep", null);
					log.info("list of course" + listOfCourse.size());
					return "listOfCourse";
				}

				Integer id = affDao.getCollegeId(collegeName);
				ses.setAttribute("collegeIdFRep", id);
				log.info("id of the college is" + id);
				listOfCourse = affDao.getListOfCourses(id, null);
				log.info("list of course" + listOfCourse.size());
				return "listOfCourse";
			}
			if (courseName != null && !courseName.isEmpty()) {
				if (courseName.contentEquals("All")) {
					Integer collegeIdFR = (Integer) ses.getAttribute("collegeIdFRep");
					feeNameList = feeDAO.getFeeNames(affDao.getFeeIdesOfInst(collegeIdFR));
					log.info("fee name list size" + feeNameList.size());
					ses.removeAttribute("collegeIdFRep");
					return "feeName";

				}
				Integer collegeIdFR = (Integer) ses.getAttribute("collegeIdFRep");
				feeNameList = feeDAO.getFeeNames(affDao.getFeeIdesOfInst(collegeIdFR));
				log.info("fee name list size" + feeNameList.size());
				ses.removeAttribute("collegeIdFRep");
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
					listOfCourse = affDao.getCoursesOFCollege(null, parDAO.getIdesOfAllCollege(universityId));
					feeNameList = feeDAO.getFeeNames(affDao.getFeeIdesOfInst(null));
					log.info("list of course" + listOfCourse.size());
					return "listOfCourse";
				}

				Integer id = affDao.getCollegeId(collegeName);
				ses.setAttribute("collegeIdFRep", id);
				log.info("id of the college is" + id);
				listOfCourse = affDao.getCoursesOFCollege(id, null);
				feeNameList = feeDAO.getFeeNames(affDao.getFeeIdesOfInst(id));
				return "listOfCourse";
			}
			if (courseName != null && !courseName.isEmpty()) {
				if (courseName.contentEquals("All")) {
					Integer collegeIdFR = (Integer) ses.getAttribute("collegeIdFRep");
					feeNameList = feeDAO.getFeeNames(affDao.getFeeIdesOfInst(collegeIdFR));
					log.info("fee name list size" + feeNameList.size());
					ses.removeAttribute("collegeIdFRep");
					return "feeName";
				}
				Integer collegeIdFR = (Integer) ses.getAttribute("collegeIdFRep");
				feeNameList = feeDAO.getFeeNames(affDao.getFeeIdesOfInst(collegeIdFR));
				log.info("fee name list size" + feeNameList.size());
				ses.removeAttribute("collegeIdFRep");
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
			Integer id = (Integer) ses.getAttribute("sesId") == null ? affDao.getCollegeId(collegeName) : (Integer) ses
					.getAttribute("sesId");
			listOfCourse = affDao.getCoursesOFCollege(id, null);
			feeNameList = feeDAO.getFeeNames(affDao.getFeeIdesOfInst(id));
			log.info("list of course" + listOfCourse.size());
			return SUCCESS;

		}

		// end of drop down generation for college

		return SUCCESS;
	}

	public String getInsTransactionDetails() {

		String forDownload = request.getParameter("forDownload");
		if (ses.getAttribute("sesProfile").toString().contentEquals("SU")) {
			transactionDetailsForReport = affDao.getAllTransactionDetail();
			String result = forDownload != null && forDownload.contentEquals("true") ? "forDowload" : "success";
			return result;
		} else if (ses.getAttribute("sesProfile").toString().contentEquals("CollegeOperator")) {
			Integer operatorId = (Integer) ses.getAttribute("opratorId");
			
			/*if(operatorId==null){
				ses.getAttribute("loginUserBean");
				operatorId=6;
			}*/
			log.info("testing college operator is :"+operatorId);
			Integer instituteId = opratorDAO.getCollegeIdOfOperator(operatorId);
			log.info("insitute id is" + instituteId);
			transactionDetailsForReport = affDao.getAllTransactionDetail(instituteId);
			String result = forDownload != null && forDownload.contentEquals("true") ? "forDowload" : "success";
			return result;

		} else if (ses.getAttribute("sesProfile").toString().contentEquals("Student")) {
			String enrollmentNumber = (String) ses.getAttribute("StudentEnrollId");
			transactionDetailsForReport = affDao.getStudentTransactionDetails(enrollmentNumber);
			String result = forDownload != null && forDownload.contentEquals("true") ? "forDowload" : "success";
			return result;
		}

		else if (ses.getAttribute("sesProfile").toString().contentEquals("Parent")) {

			ParBean parBean = affDao.getTrasactionReportForUniversity((Integer) ses.getAttribute("sesId"));
			List<Integer> allCollegeId = parDAO.getIdesOfAllCollege((Integer) ses.getAttribute("sesId"));
			transactionDetailsForReport = affDao.getTransactionOfColleges(allCollegeId);
			log.info("Number of Transaction Done by Colleges Belongs to That University"
					+ transactionDetailsForReport.size());
			String result = forDownload != null && forDownload.contentEquals("true") ? "forDowload" : "success";
			return result;
		}

		else {
			Integer insId = (Integer) ses.getAttribute("sesId");
			log.info("ins id is=" + insId);
			transactionDetailsForReport = affDao.getAllTransactionDetail(insId);
			String result = forDownload != null && forDownload.contentEquals("true") ? "forDowload" : "success";
			return result;
		}
	}

	public String getStudentList() {
		String universityName = request.getParameter("universityName");
		String collegeName = request.getParameter("collegeName");
		if ((universityName != null && !universityName.isEmpty()) && (collegeName != null && !collegeName.isEmpty())) {
			log.info("inside su");
			Integer universityId = parDAO.getUniversityId(universityName);
			Integer collegeId = affDao.getCollegeId(collegeName, universityId);
			affBean = affDao.getStudentList(collegeId);
			return SUCCESS;

		} else if (universityName != null && !universityName.isEmpty()) {

		} else if (collegeName != null && !collegeName.isEmpty()) {
			Integer universityId = (Integer) ses.getAttribute("sesId");
			Integer collegeId = affDao.getCollegeId(collegeName, universityId);
			affBean = affDao.getStudentList(collegeId);
			return SUCCESS;

		}

		return SUCCESS;
	}

	public void getTotalDuesOfStudents() {
		// read the college id from session
		Integer instId = (Integer) ses.getAttribute("sesId");
		affDao.getTotalDueOfStudents(instId);

	}

	public String saveCourses() {
		String courses = request.getParameter("values");
		String[] courseArray = courses.split(",");
		Integer instId = (Integer) ses.getAttribute("sesId");
		ArrayList<String> courseArrayList = new ArrayList<String>(Arrays.asList(courseArray));
		Iterator<String> itr = courseArrayList.iterator();
		while (itr.hasNext()) {
			String courseName = itr.next();
			CollegeCourses collCourse = new CollegeCourses();
			collCourse.setCourseName(courseName);
			// validate course name
			boolean isCourseNameAlreadySaved = affDao.courseNameAlreadySaved(courseName, instId);
			log.info("couses already saved" + isCourseNameAlreadySaved);
			if (isCourseNameAlreadySaved == false) {
				affDao.saveCollegeCourses(collCourse, instId);
			}
		}
		String message = "Courses Saved SuccessFully";
		request.setAttribute("msg", message);
		return SUCCESS;
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

	public AffBean getAffBean() {
		return affBean;
	}

	public void setAffBean(AffBean affBean) {
		this.affBean = affBean;
	}

	public List<FvBean> getLookUpvalueList() {
		return lookUpvalueList;
	}

	public void setLookUpvalueList(List<FvBean> lookUpvalueList) {
		this.lookUpvalueList = lookUpvalueList;
	}

	public String getCourses() {
		return courses;
	}

	public void setCourses(String courses) {
		this.courses = courses;
	}

	public List<CollegeCourses> getAllCourseOfInst() {
		return allCourseOfInst;
	}

	public void setAllCourseOfInst(List<CollegeCourses> allCourseOfInst) {
		this.allCourseOfInst = allCourseOfInst;
	}

	public String getTotalNetDuesOFCollegeStudent() {
		return totalNetDuesOFCollegeStudent;
	}

	public void setTotalNetDuesOFCollegeStudent(String totalNetDuesOFCollegeStudent) {
		this.totalNetDuesOFCollegeStudent = totalNetDuesOFCollegeStudent;
	}

	public String getTotalPaymentToDate() {
		return totalPaymentToDate;
	}

	public void setTotalPaymentToDate(String totalPaymentToDate) {
		this.totalPaymentToDate = totalPaymentToDate;
	}

	public String getTotalOriginalDues() {
		return totalOriginalDues;
	}

	public void setTotalOriginalDues(String totalOriginalDues) {
		this.totalOriginalDues = totalOriginalDues;
	}

	// End of Getter Setter Methods
}
