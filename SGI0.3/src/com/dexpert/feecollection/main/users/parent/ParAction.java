package com.dexpert.feecollection.main.users.parent;

import java.io.File;
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

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import com.dexpert.feecollection.main.communication.email.EmailSessionBean;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.PasswordEncryption;
import com.dexpert.feecollection.main.users.RandomPasswordGenerator;
import com.dexpert.feecollection.main.users.affiliated.AffAction;
import com.dexpert.feecollection.main.users.affiliated.AffBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;
import com.opensymphony.xwork2.ActionSupport;

public class ParAction extends ActionSupport {
	// Declare Global Variables Here
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	static Logger log = Logger.getLogger(AffAction.class.getName());
	List<ParBean> parBeansList = new ArrayList<ParBean>();
	public ParBean parBean;

	ParDAO parDAO = new ParDAO();
	private File fileUpload;
    AffDAO affDAO=new AffDAO();
	private String fileUploadFileName;
	private Integer fileSize;
	private String contentType;
    private List<List<Object[]>> AllChildInstDues=new ArrayList<List<Object[]>>();
	// End of Global Variables

	// ---------------------------------------------------

	// Action Methods Here
	public String registerUniversity() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException {

		String path = request.getServletContext().getRealPath("/");
		path = path + File.separator;
		File f = new File(path + "/RGUHS/");
		f.mkdir();
		String username;
		// to generate unique user Name
		try {

			/*if (parDAO.getRowCount() >= 1) {
				return "duplicate";
			}
*/
			username = "Uni".concat(parBean.getParInstName().replaceAll("\\s+", "").substring(0, 4)
					.concat(parDAO.getRowCount().toString()));
		} catch (java.lang.NullPointerException e) {
			username = "Uni".concat(parBean.getParInstName().replaceAll("\\s+", "").substring(0, 4).concat("1"));
			// TODO: handle exception
		}

		parBean.setFileUploadFileName(fileUploadFileName);
		parBean.setFileUpload(fileUpload);

		// to get Random generated Password
		String password = RandomPasswordGenerator.generatePswd(6, 8, 1, 2, 0);
		log.info("Password Generated is " + password);
		log.info("User Name  " + username);

		// to Encrypt Password
		PasswordEncryption.encrypt(password);
		String encryptedPwd = PasswordEncryption.encStr;

		LoginBean creds = new LoginBean();
		creds.setPassword(encryptedPwd);
		creds.setUserName(username);

		log.info("User Profile is  ::" + parBean.getLoginBean().getProfile());
		creds.setProfile(parBean.getLoginBean().getProfile());

		// for bidirectional relationship ,set parent record to child
		// record
		creds.setParBean(parBean);
		if (creds.getProfile().equals("Admin")) {

			// for bidirectional relationship ,set child record to Parent
			// record
			parBean.setLoginBean(creds);

		}

		parBean = parDAO.saveOrUpdate(parBean, f + File.separator);
		// -----Code for sending email//--------------------
		EmailSessionBean email = new EmailSessionBean();
		email.sendEmail(parBean.getParInstEmail(), "Welcome To FeeDesk!", username, password,
				parBean.getParInstName());
		request.setAttribute("msg", "University Added Successfully");
		return SUCCESS;

	}

	// to get Universiry Detail
	public String getUniversityDetail() {

		parBeansList = parDAO.getUniversityList();
		log.info("Parent List is ::" + parBeansList.size());
		return SUCCESS;

	}

	// view University Detail
	public String viewUniDetail() {

		String uniId = request.getParameter("parInstId");
		Integer id = Integer.parseInt(uniId);
		parBean = parDAO.viewUniversity(id);
		return SUCCESS;
	}
   public String getDueOFStAtInstLevel()
   {
	   Integer parentInsId=(Integer)request.getSession().getAttribute("sesId");
	   List<Integer> childInstIdes= parDAO.getIdesOfAllCollege(parentInsId);  
	   Iterator<Integer> itr=childInstIdes.iterator();
	   while (itr.hasNext()) {
		Integer instId =  itr.next();
		List<Object[]> oneInstDue=affDAO.getTotalDueOfStudents(instId);
		AllChildInstDues.add(oneInstDue);
	   }
	   log.info("number of inst"+AllChildInstDues.size());
	   
	   return SUCCESS;
   }
	// End of Action Methods

	// ---------------------------------------------------

	// Keep Getter Setter Methods Here

	public ParBean getParBean() {
		return parBean;
	}

	public void setParBean(ParBean parBean) {
		this.parBean = parBean;
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

	public List<ParBean> getParBeansList() {
		return parBeansList;
	}

	public void setParBeansList(List<ParBean> parBeansList) {
		this.parBeansList = parBeansList;
	}

	public List<List<Object[]>> getAllChildInstDues() {
		return AllChildInstDues;
	}

	public void setAllChildInstDues(List<List<Object[]>> allChildInstDues) {
		AllChildInstDues = allChildInstDues;
	}
	
	

}
