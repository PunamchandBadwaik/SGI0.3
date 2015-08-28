package com.dexpert.feecollection.main.users.affiliated.template;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.main.users.affiliated.AffAction;
import com.opensymphony.xwork2.ActionSupport;

public class ExcelTemplateAction extends ActionSupport {
	// Declare Global Variables Here

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	static Logger log = Logger.getLogger(AffAction.class.getName());
	private InputStream inputStream;
	private String downloadFile;

	// End of Global Variables

	// ---------------------------------------------------

	// Action Methods Here

	public String downloadExcelFileTemplateForCollege() {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet xssfSheet = workbook.createSheet();
		downloadFile = "College Template File".concat(".xlsx");
		ExcelTemplateDAO.generateTemplate(xssfSheet);

		try {
			ByteArrayOutputStream boas = new ByteArrayOutputStream();
			workbook.write(boas);
			setInputStream(new ByteArrayInputStream(boas.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SUCCESS;

	}

	// End of Action Methods

	// ---------------------------------------------------

	// Keep Getter Setter Methods Here
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(String downloadFile) {
		this.downloadFile = downloadFile;
	}

}
