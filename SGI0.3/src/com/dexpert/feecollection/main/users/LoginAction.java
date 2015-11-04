package com.dexpert.feecollection.main.users;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.main.users.affiliated.AffAction;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;
import com.dexpert.feecollection.main.users.operator.OperatorBean;
import com.dexpert.feecollection.main.users.operator.OperatorDao;
import com.dexpert.feecollection.main.users.parent.ParBean;
import com.dexpert.feecollection.main.users.parent.ParDAO;
import com.dexpert.feecollection.main.users.superadmin.SaBean;
import com.dexpert.feecollection.main.users.superadmin.SaDAO;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {

	// Declare Global Variables Here
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	static Logger log = Logger.getLogger(LoginAction.class.getName());

	HttpSession httpSession = ServletActionContext.getRequest().getSession();
	LoginBean loginBean = new LoginBean();
	LoginDAO loginDAO = new LoginDAO();

	private String firstName, newPwd;
	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	private String profile;
	private String emailId;
	private ParBean parBean = new ParBean();
	private AffBean affBean = new AffBean();
	private OperatorBean operatorBean = new OperatorBean();

	public OperatorBean getOperatorBean() {
		return operatorBean;
	}

	public void setOperatorBean(OperatorBean operatorBean) {
		this.operatorBean = operatorBean;
	}

	public AffBean getAffBean() {
		return affBean;
	}

	public void setAffBean(AffBean affBean) {
		this.affBean = affBean;
	}

	public ParBean getParBean() {
		return parBean;
	}

	public void setParBean(ParBean parBean) {
		this.parBean = parBean;
	}

	private SaBean saBean = new SaBean();
	private SaDAO saDAO = new SaDAO();
	private AffDAO affDAO = new AffDAO();
	private ParDAO parDAO = new ParDAO();

	// End of Global Variables

	// ---------------------------------------------------
	// Action Methods Here

	public SaBean getSaBean() {
		return saBean;
	}

	public void setSaBean(SaBean saBean) {
		this.saBean = saBean;
	}

	public String editUserDetail() {

		String id = request.getParameter("id");
		String profile = request.getParameter("userProfile");

		log.info("Id is ::" + id);
		log.info("Profile is ::" + profile);
		LoginBean bean = loginDAO.getLoginUserDetail(id, profile);

		if (bean.getProfile().contentEquals("Institute")) {

			affBean = loginDAO.getInstDetail(bean.getAffBean().getInstId());
			return "editInst";
		} else if (bean.getProfile().contentEquals("Admin")) {

			// university
			parBean = loginDAO.getParentDetail(bean.getParBean().getParInstId());
			return "editUni";

		} else if (bean.getProfile().contentEquals("Super Admin")) {
			log.info("SU");

			saBean = loginDAO.getSuperAdminDetail(bean.getSaBean().getSaId());

			return "editSu";
		} else if (bean.getProfile().contentEquals("CollegeOperator")) {
			log.info("College operator");
			operatorBean = loginDAO.getOperatorDetail(bean.getOperatorBean().getOperatorId());

			return "editOp";
		} else if (bean.getProfile().contentEquals("Student")) {
			log.info("Student");
		}

		return SUCCESS;
	}

	public String userLogin() throws InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException,
			InvalidKeySpecException

	{
		LoginBean loginClone = new LoginBean();
		log.info("user Name is ::" + loginBean.getUserName());
		log.info("Password is ::" + loginBean.getPassword());
		String encryptedPwd = null;
		LoginBean lgbean = new LoginBean();
		synchronized (this) {
			// to Encrypt Password
			PasswordEncryption.encrypt(String.valueOf(loginBean.getPassword()));
			encryptedPwd = PasswordEncryption.encStr;
			loginBean.setPassword(encryptedPwd);

		}
		loginClone.setUserName(loginBean.getUserName());
		loginClone.setPassword(loginBean.getPassword());
		log.info("After Encryption ::" + loginClone.getPassword());

		LoginBean loginUser = loginDAO.getLoginDetails(loginClone);
		// log.info("List Size ::" + loginUserList.size());
		// log.info("List  ::" + loginUserList);

		try {
			if ((loginUser.getUserName().equals(loginClone.getUserName()) && loginUser.getPassword().equals(
					loginClone.getPassword()))) {
				// log.info("valid User name and Password");
				Cookie usercookie = new Cookie("userName", loginBean.getUserName());
				usercookie.setMaxAge(60 * 60);
				response.addCookie(usercookie);
				httpSession.setAttribute("cart_init", 0);
				httpSession.setAttribute("loginUserBean", lgbean);

				if (lgbean.getAffBean() != null) {
					log.info("Valid College");
					httpSession.setAttribute("sesProfile", "Affiliated");
					httpSession.setAttribute("dashLink", "index-College.jsp");
					httpSession.setAttribute("sesId", lgbean.getAffBean().getInstId());

					httpSession.setAttribute("instId", lgbean.getAffBean().getInstId());
					httpSession.setAttribute("parInstId", lgbean.getAffBean().getParBeanAff().getParInstId());

					/*
					 * List<Object[]> studentsDues =
					 * affDAO.getTotalDueOfStudents
					 * (lgbean.getAffBean().getInstId());
					 * httpSession.setAttribute("duesArray", (Object[])
					 * studentsDues.iterator().next());
					 */return "college";
				} else if (lgbean.getParBean() != null) {
					log.info("Valid University");
					httpSession.setAttribute("sesId", lgbean.getParBean().getParInstId());
					httpSession.setAttribute("sesProfile", "Parent");
					httpSession.setAttribute("dashLink", "index-University.jsp");
					/*
					 * List<Object[]> viewstudentDuesForPar =
					 * parDAO.getTotDuesOFStudOFAllColl(lgbean.getParBean()
					 * .getParInstId());
					 * httpSession.setAttribute("duesArrayForParent", (Object[])
					 * viewstudentDuesForPar.iterator().next());
					 */return "university";
				} else if (lgbean.getSaBean() != null) {
					log.info("Valid Super Admin");
					httpSession.setAttribute("sesProfile", "SU");
					httpSession.setAttribute("dashLink", "index-Admin.jsp");
					return "superAdmin";
				}

				else if (lgbean.getOperatorBean() != null) {
					log.info("Valid College Operator");

					httpSession.setAttribute("sesProfile", "CollegeOperator");
					httpSession.setAttribute("dashLink", "index-College-Operator.jsp");
					httpSession.setAttribute("oprBean", lgbean.getOperatorBean());
					return "collegeOperator";
				}

				else if (lgbean.getAppBean() != null) {

					log.info("Enrollment Number is ::" + lgbean.getAppBean().getEnrollmentNumber());

					log.info("Valid Student EnrollmentNumber");

					httpSession.setAttribute("sesProfile", "Student");
					httpSession.setAttribute("StudentEnrollId", lgbean.getAppBean().getEnrollmentNumber());
					httpSession.setAttribute("dashLink", "getTheStudentFeeDetailsFromLoginPage");
					return "student";
				}

				else {
					request.setAttribute("msg", "Invalid Username or Password");

					return INPUT;
				}

			} else {
				request.setAttribute("msg", "Invalid Username or Password");
				return INPUT;

			}
		} catch (java.lang.NullPointerException e) {

			request.setAttribute("msg", "Session Time Out");

			return ERROR;
		}

	}

	public String logoutUser() throws IOException {

		loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");

		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Vary", "*");

		httpSession.removeAttribute("loginBean");
		httpSession.removeAttribute("sesProfile");
		httpSession.removeAttribute("dashLink");
		httpSession.removeAttribute("cart_init");
		request.setAttribute("redirectLink", "Login.jsp");

		return SUCCESS;
	}

	public String getTheForgetPasswordDetails() throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {

		if (profile.equals("SU")) {

			saBean = saDAO.validateTheForgetPwdDetails(profile, emailId);

			if (saBean != null) {
				log.info("" + saBean.getLoginBean().getUserName() + " " + saBean.getLoginBean().getLoginId() + " "
						+ saBean.getLoginBean().getPassword());
				request.setAttribute("success", "Password is Changed Successfully");
				return SUCCESS;
			} else {
				request.setAttribute("msg", "Please Use Registered Details");
				return "failure";
			}

		} else if (profile.equals("Parent")) {

			parBean = ParDAO.validateTheForgetPwdDetails(profile, emailId);
			if (parBean != null) {
				log.info("" + parBean.getLoginBean().getUserName() + " " + parBean.getLoginBean().getLoginId() + " "
						+ parBean.getLoginBean().getPassword());
				request.setAttribute("success", "Password is Changed Successfully");

				return SUCCESS;
			} else {
				request.setAttribute("msg", "Please Use Registered Details");
				return "failure";
			}

		} else if (profile.equals("Affiliated")) {

			affBean = AffDAO.validateTheForgetPwdDetails(profile, emailId);

			if (affBean != null) {
				log.info("" + affBean.getLoginBean().getUserName() + " " + affBean.getLoginBean().getLoginId() + " "
						+ affBean.getLoginBean().getPassword());
				request.setAttribute("success", "Password is Changed Successfully");
				return SUCCESS;
			} else {
				request.setAttribute("msg", "Please Use Registered Details");
				return "failure";
			}

		}

		else if (profile.equals("CollegeOperator")) {

			operatorBean = OperatorDao.validateTheForgetPwdDetails(profile, emailId);
			if (operatorBean != null) {
				log.info("" + operatorBean.getLoginBean().getUserName() + " "
						+ operatorBean.getLoginBean().getLoginId() + " " + operatorBean.getLoginBean().getPassword());
				request.setAttribute("success", "Password is Changed Successfully");
				return SUCCESS;
			} else {
				request.setAttribute("msg", "Please Use Registered Details");
				return "failure";

			}
		}

		request.setAttribute("msg", "Please Use Registered Details");
		return "failure";
	}

	// change the user password.
	public String changeUserPwd() {
		System.out.println(request.getParameter("id"));

		return SUCCESS;
	}

	/*
	 * public String validateChangePwdDetails() throws InvalidKeyException,
	 * NoSuchPaddingException, InvalidAlgorithmParameterException,
	 * IllegalBlockSizeException, BadPaddingException, IOException,
	 * NoSuchAlgorithmException, InvalidKeySpecException {
	 * 
	 * log.info("login details:" + loginBean.getUserName() + " " +
	 * loginBean.getPassword() + " " + newPwd); log.info("user Name is ::" +
	 * loginBean.getUserName()); log.info("Password is ::" +
	 * loginBean.getPassword()); String encryptedPwd, decrypedText = null;
	 * String userProfile = ""; LoginBean lgbean = new LoginBean();
	 * log.info("");
	 * 
	 * List<LoginBean> loginUserList = loginDAO.getLoginDetails(loginBean);
	 * 
	 * Iterator<LoginBean> loginIterator = loginUserList.iterator(); while
	 * (loginIterator.hasNext()) { log.info("1"); lgbean = (LoginBean)
	 * loginIterator.next(); userProfile = lgbean.getProfile();
	 * setUserId(lgbean.getLoginId()); encryptedPwd = lgbean.getPassword();
	 * log.info("2"); PasswordEncryption.decrypt(encryptedPwd); decrypedText =
	 * PasswordEncryption.plainStr; log.info("3");
	 * log.info("password frm Database is ::" + decrypedText); log.info("4"); }
	 * 
	 * if (loginBean.getPassword().equals(decrypedText)) {
	 * 
	 * log.info("Password is matching:" + loginBean.getPassword());
	 * 
	 * if (userProfile.contentEquals("Institute")) {
	 * 
	 * LoginDAO.updateChangePwdDetails(lgbean, newPwd);
	 * request.setAttribute("msg", "Your Password is Successfully changed..");
	 * return SUCCESS;
	 * 
	 * } else if (userProfile.contentEquals("Admin")) {
	 * 
	 * LoginDAO.updateChangePwdDetails(lgbean, newPwd);
	 * request.setAttribute("msg", "Your Password is Successfully changed..");
	 * return SUCCESS;
	 * 
	 * } else if (userProfile.contentEquals("Super Admin")) {
	 * 
	 * LoginDAO.updateChangePwdDetails(lgbean, newPwd);
	 * request.setAttribute("msg", "Your Password is Successfully changed..");
	 * return SUCCESS;
	 * 
	 * } else if (userProfile.contentEquals("CollegeOperator")) {
	 * 
	 * LoginDAO.updateChangePwdDetails(lgbean, newPwd);
	 * request.setAttribute("msg", "Your Password is Successfully changed..");
	 * return SUCCESS;
	 * 
	 * } } request.setAttribute("msg", "Please Enter The Valid Password");
	 * return "failure";
	 * 
	 * }
	 */

	// updatePersonalInfoDetail for Super Admin

	public String updatePersonalInfoDetail() {

		SaDAO.updatePersonalRecordOfSuperAdmin(saBean);
		request.setAttribute("msg", "Super Admin Updated Successfully");

		return SUCCESS;
	}

	// updatePersonalInfoAdminDetail for Admin

	public String updatePersonalInfoAdminDetail() {

		/*
		 * ParDAO.updatePersonalRecordOfAdmin(parBean);
		 * request.setAttribute("msg", "Admin Updated Successfully");
		 */

		return SUCCESS;
	}

	// updatePersonalInfoInstituteDetail for Institute

	public String updatePersonalInfoInstituteDetail() {

		AffDAO.updatePersonalRecordOfInstitute(affBean);
		request.setAttribute("msg", "Admin Updated Successfully");

		return SUCCESS;
	}

	/* updatePersonalInfoCollegeOperatorDetail for College Operator */
	public String updatePersonalInfoCollegeOperatorDetail() {

		OperatorDao.updatePersonalRecordOfCollegeOperatorDetail(operatorBean);
		request.setAttribute("msg", "Admin Updated Successfully");

		return SUCCESS;
	}

	// End of Action Methods

	// ---------------------------------------------------

	// Keep Getter Setter Methods Here

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

}
