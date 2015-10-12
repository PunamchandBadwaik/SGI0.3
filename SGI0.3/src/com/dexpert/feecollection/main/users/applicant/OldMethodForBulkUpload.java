package com.dexpert.feecollection.main.users.applicant;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.fee.config.FcDAO;
import com.dexpert.feecollection.main.fee.lookup.values.FvBean;
import com.dexpert.feecollection.main.fee.lookup.values.FvDAO;
import com.dexpert.feecollection.main.users.LoginBean;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;

public class OldMethodForBulkUpload {

	// Declare Global Variables Here
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(AppDAO.class.getName());
	AffDAO aff = new AffDAO();
	AffDAO affDAO = new AffDAO();
	FcDAO fcDAO = new FcDAO();
	FvDAO fvDAO = new FvDAO();
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpSession httpSession = request.getSession();
	LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");

	public ArrayList<AppBean> importExcelFileToDatabase1(String fileUploadFileName, File fileUpload, String path)
			throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		List<Integer> valueList = new ArrayList<Integer>();
		List<Integer> paramList = new ArrayList<Integer>();
		LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");

		List<Integer> str_ids = affDAO.getStrutureId(lgBean.getAffBean().getInstId(), null);
		valueList = fcDAO.getLookupValue(str_ids);

		paramList = fvDAO.getListOfValueBeans(valueList);

		FileInputStream fileInputStream = new FileInputStream(fileUpload);

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);

		XSSFSheet hssfSheet = xssfWorkbook.getSheetAt(0);

		LinkedHashMap<Integer, Map<ArrayList<Object>, List<FvBean>>> appBeanMap = new LinkedHashMap<Integer, Map<ArrayList<Object>, List<FvBean>>>(
				4000);
		try {

			Iterator<Row> rows = hssfSheet.rowIterator();

			int j = 0;
			String stringVal;
			String blankVal;

			Long numVal;
			XSSFCell cell;

			while (rows.hasNext()) {
				XSSFRow row = (XSSFRow) rows.next();

				if (row.getRowNum() == 0) {
					continue;
				}
				Iterator<Cell> cells = row.cellIterator();
				ArrayList<Object> tempArrayList = new ArrayList<Object>();

				List<FvBean> fvBeansList = new ArrayList<FvBean>();

				LinkedHashMap<ArrayList<Object>, List<FvBean>> rowMap = new LinkedHashMap<ArrayList<Object>, List<FvBean>>();

				// System.out.print(">>" + j + " ");

				int i = 0;
				while (cells.hasNext()) {

					cell = (XSSFCell) cells.next();

					switch (cell.getCellType()) {

					case XSSFCell.CELL_TYPE_STRING:
						stringVal = cell.getStringCellValue();
						// System.out.println("String :: " + stringVal);
						tempArrayList.add(stringVal.trim());
						break;

					case XSSFCell.CELL_TYPE_NUMERIC:
						numVal = (long) cell.getNumericCellValue();
						tempArrayList.add(numVal.toString().trim());

						// System.out.println("NUmber ::" + numVal);
						break;

					case XSSFCell.CELL_TYPE_BLANK:
						blankVal = cell.getStringCellValue();
						tempArrayList.add(blankVal.trim());
						// System.out.println("Blank ::" + blankVal);
						break;

					}
					i++;

					Iterator<Integer> paramIterator = paramList.iterator();
					// log.info("Look Parameter size is ::" + paramList.size());
					while (paramIterator.hasNext()) {
						FvBean bean = new FvBean();
						String tempString = "";

						Integer lookupId = (Integer) paramIterator.next();

						// log.info("Look Up Id is ::" + lookupId);

						switch (cell.getCellType()) {

						case XSSFCell.CELL_TYPE_STRING:

							tempString = row.getCell(i - 1).toString();
							// log.info(" >>" + tempString);
							break;

						case XSSFCell.CELL_TYPE_NUMERIC:
							tempString = row.getCell(i - 1).toString().trim();
							// log.info(" >>>>" + tempString);
							break;

						case XSSFCell.CELL_TYPE_BLANK:
							tempString = row.getCell(i - 1).toString().trim();
							// log.info(" >>>>" + tempString);
							break;

						}

						// log.info("temp String is ::" + tempString);
						String x = tempString.contains(".") ? tempString.substring(0, tempString.indexOf("."))
								: tempString;

						// bean = checkFeeValue(lookupId, x);

						if (bean != null) {
							fvBeansList.add(bean);

						}

						log.info("Fv Bean List size ::" + fvBeansList.size());
					}

				}
				rowMap.put(tempArrayList, fvBeansList);
				log.info("temp Arryalist Size ::" + tempArrayList.size());

				appBeanMap.put(j, rowMap);

				j++;

			}
			log.info("AppBEan Map size ::" + appBeanMap.size());
			ArrayList<Integer> arrayList = new ArrayList<Integer>(appBeanMap.keySet());
			ArrayList<AppBean> appBeansList = new ArrayList<AppBean>(4000);
			Iterator<Integer> iterator = arrayList.iterator();
			log.info("AppBean Array list  ::" + arrayList.size());

			while (iterator.hasNext()) {
				// log.info("AppBean Map");
				Integer integer = (Integer) iterator.next();
				Map<ArrayList<Object>, List<FvBean>> rMap = appBeanMap.get(integer);
				// log.info("R map ::" + rMap.size());
				Set<ArrayList<Object>> set = rMap.keySet();
				// log.info("Set  ::" + set.size());
				ArrayList<Object> aa = new ArrayList<Object>(set.iterator().next());
				// log.info("Arraylist ::" + aa.size());
				Collection<List<FvBean>> list = rMap.values();
				// log.info("Collection ::" + list.size());

				Set<FvBean> paramSet = new HashSet<FvBean>((List<FvBean>) list.iterator().next());

				AppBean appBean = new AppBean();
				appBean.setGrNumber(aa.get(0).toString());
				appBean.setAplFirstName(aa.get(1).toString());
				appBean.setAplLstName(aa.get(2).toString());
				appBean.setGender(aa.get(3).toString());
				appBean.setAplAddress(aa.get(4).toString());
				appBean.setAplMobilePri(aa.get(5).toString());
				appBean.setAplMobileSec(aa.get(6).toString());
				appBean.setAplEmail(aa.get(7).toString());
				appBean.setStartYear(aa.get(8).toString());
				appBean.setApplicantParamValues(paramSet);

				appBeansList.add(appBean);

			}
			log.info("appBean LIst Size c:: " + appBeansList.size());
			Iterator<AppBean> iterator2 = appBeansList.iterator();

			// log.info("appBean List Size " + appBeansList.size());

			while (iterator2.hasNext()) {
				AppBean appBean = (AppBean) iterator2.next();

				// addBulkData(appBean);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public ArrayList<AppBean> importExcelFileToDatabase(String fileUploadFileName, File fileUpload, String path)
			throws Exception {

		String name, lstName, gender, cast, address, acaYear, course, branch, emailAddress;

		Long enrollNo = null, mobileNumPri, MobileNumSec, admYear;
		String mobPri, mobSec, admssionYear;

		// ArrayList<AppBean> appBeansList = new ArrayList<AppBean>();
		AppBean appBean = new AppBean();
		// AffDAO affDAO = new AffDAO();
		FileInputStream fileInputStream = new FileInputStream(fileUpload);

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);

		XSSFSheet hssfSheet = xssfWorkbook.getSheetAt(0);

		Iterator<Row> rowIterator = hssfSheet.iterator();

		while (rowIterator.hasNext()) {
			Row row = (Row) rowIterator.next();

			if (row.getRowNum() == 0) {
				continue;
			}

			int noOfColumns = hssfSheet.getRow(row.getRowNum()).getPhysicalNumberOfCells();
			log.info("::::::row number::::: " + row.getRowNum() + " Column numbers ::" + noOfColumns);

			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = (Cell) cellIterator.next();

				switch (cell.getCellType()) {

				case Cell.CELL_TYPE_STRING:

					break;

				case Cell.CELL_TYPE_NUMERIC:
					break;

				}
			}

			Cell r = row.getCell(0);

			try {
				enrollNo = (long) r.getNumericCellValue();
			} catch (java.lang.NullPointerException e) {

			}
			r = row.getCell(1);
			name = r.getStringCellValue();

			r = row.getCell(2);
			lstName = r.getStringCellValue();

			r = row.getCell(3);
			gender = r.getStringCellValue();

			r = row.getCell(4);
			cast = r.getStringCellValue();

			r = row.getCell(5);
			address = r.getStringCellValue();

			r = row.getCell(6);
			mobileNumPri = (long) r.getNumericCellValue();

			r = row.getCell(7);
			MobileNumSec = (long) r.getNumericCellValue();

			r = row.getCell(8);
			admssionYear = r.getStringCellValue();

			r = row.getCell(9);
			course = r.getStringCellValue();

			r = row.getCell(10);

			try {
				branch = r.getStringCellValue();
			} catch (java.lang.NullPointerException e) {

			}

			r = row.getCell(11);
			emailAddress = r.getStringCellValue();

			appBean.setAplFirstName(name);
			appBean.setAplLstName(lstName);
			appBean.setGender(gender);
			appBean.setCategory(cast);
			appBean.setAplAddress(address);

			/*
			 * appBean.setAplMobilePri(mobPri.toString());
			 * appBean.setAplMobileSec(mobSec.toString());
			 */

			appBean.setAplMobilePri(mobileNumPri.toString());
			appBean.setAplMobileSec(MobileNumSec.toString());

			/* appBean.setYear(admYear.toString()); */

			// appBean.setYear(admssionYear.toString());

			/*
			 * if (cast.contentEquals("A")) {
			 * 
			 * appBean.setCategory("Open / E.B.C Category");
			 * 
			 * } else if (cast.contentEquals("B")) {
			 * 
			 * appBean.setCategory("OBC / ESBC (Maratha) Category");
			 * 
			 * } else if (cast.contentEquals("C")) {
			 * 
			 * appBean.setCategory("SC / ST / DT / VJ / NT / SBC Category");
			 * 
			 * }
			 */

			/*
			 * try { appBean.setEnrollmentNumber(enrollNo.toString()); if
			 * (appBean.getEnrollmentNumber().equals("null") ||
			 * appBean.getEnrollmentNumber().equals(null) ||
			 * appBean.getEnrollmentNumber().equals("")) {
			 * GenerateEnrollmentNumber en = new GenerateEnrollmentNumber();
			 * String EnrollNo = en.generateEnrollmentNumber(year, yearCode,
			 * course); appBean.setEnrollmentNumber(EnrollNo); } } catch
			 * (java.lang.NullPointerException e) { GenerateEnrollmentNumber en
			 * = new GenerateEnrollmentNumber(); String EnrollNo =
			 * en.generateEnrollmentNumber(year, yearCode, course);
			 * appBean.setEnrollmentNumber(EnrollNo); }
			 */

			appBean.setAplEmail(emailAddress);

			// appBeansList.add(appBean);

			// addBulkData(appBean);

		}

		return null;

	}

	public ArrayList<AppBean> importExcelFileToTempDataBase(String fileUploadFileName, File fileUpload, String path)
			throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession httpSession = request.getSession();
		List<Integer> valueList = new ArrayList<Integer>();
		List<Integer> paramList = new ArrayList<Integer>();
		LoginBean lgBean = (LoginBean) httpSession.getAttribute("loginUserBean");

		List<Integer> str_ids = affDAO.getStrutureId(lgBean.getAffBean().getInstId(), null);
		valueList = fcDAO.getLookupValue(str_ids);

		paramList = fvDAO.getListOfValueBeans(valueList);

		FileInputStream fileInputStream = new FileInputStream(fileUpload);

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);

		XSSFSheet hssfSheet = xssfWorkbook.getSheetAt(0);
		try {

			Iterator<Row> rows = hssfSheet.rowIterator();

			int j = 0;
			String stringVal;
			String blankVal;

			long numVal;
			XSSFCell cell;

			while (rows.hasNext()) {
				XSSFRow row = (XSSFRow) rows.next();

				if (row.getRowNum() == 0) {
					continue;
				}
				Iterator<Cell> cells = row.cellIterator();
				ArrayList<Object> tempArrayList = new ArrayList<Object>();

				int i = 0;
				while (cells.hasNext()) {

					cell = (XSSFCell) cells.next();

					switch (cell.getCellType()) {

					case XSSFCell.CELL_TYPE_STRING:
						stringVal = cell.getStringCellValue();
						System.out.println("String :: " + stringVal);
						tempArrayList.add(stringVal);
						break;

					case XSSFCell.CELL_TYPE_NUMERIC:
						numVal = (long) cell.getNumericCellValue();
						tempArrayList.add(numVal);
						System.out.println("NUmber ::" + numVal);
						break;

					case XSSFCell.CELL_TYPE_BLANK:
						blankVal = cell.getStringCellValue();
						tempArrayList.add(blankVal);
						System.out.println("Blank ::" + blankVal);
						break;

					}
					i++;

				}

				j++;

			}

		} catch (Exception e) {

		}

		return null;

	}

}
