package com.dexpert.feecollection.main.users.operator;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.RandomPasswordGenerator;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;
import com.opensymphony.xwork2.ActionSupport;

public class OperatorAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	static Logger log = Logger.getLogger(OperatorAction.class.getName());
	private String opInstId;
	private OperatorBean operatorBean = new OperatorBean();
	AffBean affBean = new AffBean();
	private List<OperatorBean> listOprtBean = new ArrayList<OperatorBean>();
	private List<AffBean> listAffBean = new ArrayList<AffBean>();
	AffDAO affDAO = new AffDAO();
	LoginBean loginBean;

	public OperatorBean getOperatorBean() {
		return operatorBean;
	}

	public void setOperatorBean(OperatorBean operatorBean) {
		this.operatorBean = operatorBean;
	}

	public String registerCollegeOperatorAction() throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		// AffDAO affDAO = new AffDAO();

		String username = new String();
		// generate credentials for admin login
		try {
			username = "Op".concat(operatorBean.getOperatorName().concat(operatorBean.getOperatorLstName())
					.replaceAll("\\s+", "").substring(0, 4).concat(OperatorDao.getRowCount().toString()));

		} catch (java.lang.NullPointerException e) {
			username = "Op".concat(operatorBean.getOperatorName().concat(operatorBean.getOperatorLstName())
					.replaceAll("\\s+", "").substring(0, 4).concat("1"));

		}

		String password = RandomPasswordGenerator.generatePswd(6, 8, 1, 2, 0);
		// log.info("Password Generated is " + password);

		PasswordEncryption.encrypt(password);

		String encryptedPwd = PasswordEncryption.encStr;

		LoginBean creds = new LoginBean();

		creds.setPassword(encryptedPwd);
		creds.setUserName(username);

		creds.setProfile("CollegeOperator");

		creds.setOperatorBean(operatorBean);

		operatorBean.setLoginBean(creds);

		Integer instId = Integer.parseInt(opInstId);

		
		
		
		affBean = affDAO.viewInstDetail(instId);

		/*log.info("Institutre Name is ::" + affBean.getInstName());

		operatorBean.setCollegeName(affBean.getInstName());*/
	
		
		affBean.getOptrBeanSet().add(operatorBean);
		operatorBean.setAffBean(affBean);
		
		affDAO.saveOrUpdate(affBean, null);

		
		
		/*
		 * if (creds.getProfile().equals("CollegeOperator")) {
		 * 
		 * // for bidirectional relationship ,set child record to // Parent //
		 * record operatorBean.setLoginBean(creds);
		 * 
		 * }
		 */

		OperatorDao.registerCollegeOperatorDao(operatorBean);
		// -----Code for sending email//--------------------
		EmailSessionBean email = new EmailSessionBean();
		email.sendEmail(operatorBean.getOperatorEmail(), "Welcome To Fee Collection Portal!", username, password,
				operatorBean.getOperatorName());

		request.setAttribute("msg", "Operator Added Successfully");
		request.setAttribute("redirectLink", "Success.jsp");

		/* request.setAttribute("msg", "Operator Added Successfully"); */

		return SUCCESS;
	}

	// Getting All Records of College Operators
	public String getListOfCollegeOperators() {

		listAffBean = affDAO.getCollegesList();

		for (AffBean affBean : listAffBean) {

			for (OperatorBean opBean : affBean.getOptrBeanSet()) {

			}

		}

		listOprtBean = OperatorDao.getAllRecordsOfCollegeOperator();

		for (OperatorBean obean : listOprtBean) {

		}

		return SUCCESS;
	}

	public String getOperatorLogin() {
		HttpSession httpSession = request.getSession();
		loginBean = (LoginBean) httpSession.getAttribute("loginUserBean");

		return SUCCESS;
	}

	public List<OperatorBean> getListOprtBean() {
		return listOprtBean;
	}

	public void setListOprtBean(List<OperatorBean> listOprtBean) {
		this.listOprtBean = listOprtBean;
	}

	public String getOpInstId() {
		return opInstId;
	}

	public void setOpInstId(String opInstId) {
		this.opInstId = opInstId;
	}

	public AffBean getAffBean() {
		return affBean;
	}

	public void setAffBean(AffBean affBean) {
		this.affBean = affBean;
	}

	public List<AffBean> getListAffBean() {
		return listAffBean;
	}

	public void setListAffBean(List<AffBean> listAffBean) {
		this.listAffBean = listAffBean;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

}
