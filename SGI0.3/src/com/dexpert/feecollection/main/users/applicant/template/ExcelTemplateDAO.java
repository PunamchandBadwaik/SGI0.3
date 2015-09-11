package com.dexpert.feecollection.main.users.applicant.template;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.fee.lookup.LookupDAO;
import com.dexpert.feecollection.main.users.affiliated.AffAction;

public class ExcelTemplateDAO {
	static Logger log = Logger.getLogger(AffAction.class.getName());

	public static void generateTemplate(XSSFSheet xssfSheet) {

		LookupDAO lpdao = new LookupDAO();
		ArrayList<LookupBean> applicantParam = lpdao.getLookupData("Scope", "Applicant", null, null);
		ArrayList<String> paramStr = new ArrayList<String>();
		paramStr.add("Enrollment Number");
		paramStr.add("First Name");
		paramStr.add("Last Name");
		paramStr.add("Gender");
		paramStr.add("Cast");
		paramStr.add("Address");
		paramStr.add("Primary Contact Number");
		paramStr.add("Secondary Contact Number");
		paramStr.add("Admission Year");
		paramStr.add("Academic Year");
		paramStr.add("Course");
		paramStr.add("Branch");
		paramStr.add("Email Id");

		try {

			Row header = xssfSheet.createRow(0);

			Iterator<LookupBean> iterator = applicantParam.iterator();
			while (iterator.hasNext()) {
				LookupBean lookupBean = (LookupBean) iterator.next();
				paramStr.add(lookupBean.getLookupName());

			}
			for (int i = 0; i < paramStr.size(); i++) {
				Cell paramCell = header.createCell(i);
				paramCell.setCellValue(paramStr.get(i));
				xssfSheet.setColumnWidth(i, 6500);

			}

		} catch (Exception e) {

		}

	}

}
