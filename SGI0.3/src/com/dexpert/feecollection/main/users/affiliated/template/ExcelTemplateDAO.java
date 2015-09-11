package com.dexpert.feecollection.main.users.affiliated.template;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.fee.lookup.LookupDAO;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.affiliated.AffAction;

public class ExcelTemplateDAO {
	static Logger log = Logger.getLogger(AffAction.class.getName());

	public static void generateTemplate(XSSFSheet xssfSheet) {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpSession httpSession = request.getSession();
		String profile = httpSession.getAttribute("sesProfile").toString();
		LookupDAO lpdao = new LookupDAO();
		// ArrayList<LookupBean> applicantParam = lpdao.getLookupData("Scope",
		// "Applicant", null, null);
		ArrayList<String> paramStr = new ArrayList<String>();

		if (profile.equals("SU")) {
			log.info("Profile of user Is ::" + profile);
			
			paramStr.add("Institute Name");
			paramStr.add("Institute Address");
			paramStr.add("Institute Tele. No.");
			paramStr.add("Institute Contact Peroson");
			paramStr.add("Institute Contact Person Mob Number");
			paramStr.add("Institute Email Address");
			paramStr.add("Institute District");
			paramStr.add("University ID");

		} else {
			log.info("Profile of user Is ::" + profile);
			paramStr.add("Institute Name");
			paramStr.add("Institute Address");
			paramStr.add("Institute Tele. No.");
			paramStr.add("Institute Contact Peroson");
			paramStr.add("Institute Contact Person Mob Number");
			paramStr.add("Institute Email Address");
			paramStr.add("Institute District");
		}

		try {

			Row header = xssfSheet.createRow(0);

			for (int i = 0; i < paramStr.size(); i++) {
				Cell paramCell = header.createCell(i);
				paramCell.setCellValue(paramStr.get(i));
				xssfSheet.setColumnWidth(i, 6000);

			}

		} catch (Exception e) {

		}

	}

}
