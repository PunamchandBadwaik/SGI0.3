package com.dexpert.feecollection.main.users.superadmin;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.RandomPasswordGenerator;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;

import com.opensymphony.xwork2.ActionSupport;

public class SaAction extends ActionSupport {

	// Declare Global Variables Here
	SaBean superAdmin;
	static Logger log = Logger.getLogger(AffDAO.class.getName());
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	public SaDAO saDAO = new SaDAO();

	// End of Global Variables

	// ---------------------------------------------------

	// Action Methods Here
	public String registerSuperAdmn() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {

		String username;

		try {
			username = "SA".concat(superAdmin.getFirstName().replaceAll("\\s+", "").substring(0, 4)
					.concat(saDAO.getRowCount().toString()));

		} catch (java.lang.NullPointerException e) {

			username = "SA".concat(superAdmin.getFirstName().replaceAll("\\s+", "").substring(0, 4).concat("1"));

		}

		// to get Random generated Password
		String password = RandomPasswordGenerator.generatePswd(6, 8, 1, 2, 0);
		log.info("Password Generated is " + password);
		log.info("User Name is " + username);

		// to Encrypt Password
		PasswordEncryption.encrypt(password);
		String encryptedPwd = PasswordEncryption.encStr;

		LoginBean creds = new LoginBean();
		creds.setPassword(encryptedPwd);
		creds.setUserName(username);

		log.info("User Profile is  ::" + superAdmin.getLoginBean().getProfile());
		creds.setProfile(superAdmin.getLoginBean().getProfile());

		// for bidirectional relationship ,set parent record to child
		// record
		creds.setSaBean(superAdmin);
		if (creds.getProfile().equals("Super Admin")) {

			// for bidirectional relationship ,set child record to Parent
			// record
			superAdmin.setLoginBean(creds);

		}

		superAdmin = saDAO.saveOrUpdate(superAdmin);
		// -----Code for sending email//--------------------
		EmailSessionBean email = new EmailSessionBean();
		email.sendEmail(superAdmin.getEmailId(), "Welcome To FeeDesk!", username, password,
				superAdmin.getFirstName());
		request.setAttribute("redirectLink", "SuperAdminForm.jsp");
		return SUCCESS;

	}

	// view Super Admin Profile
	public String viewSaProfile() {
		String saId = request.getParameter("saId");
		Integer id=Integer.parseInt(saId);
		log.info("Super Admin ID ::" + saId);
		
		superAdmin=saDAO.getSaDetail(id);
		return SUCCESS;
	}
	

	// End of Action Methods

	// ---------------------------------------------------

	// Keep Getter Setter Methods Here
	public SaBean getSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(SaBean superAdmin) {
		this.superAdmin = superAdmin;
	}

	// End of Getter Setter Methods
}
