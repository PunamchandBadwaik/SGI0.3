package com.dexpert.feecollection.challan;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class ChallanAction extends ActionSupport {
	HttpServletRequest request = ServletActionContext.getRequest();
	ChallanBean challanDetails;
	List<ChallanBean> allChallanInfo;
	static Logger log = Logger.getLogger(ChallanAction.class.getName());

	public List<ChallanBean> getAllChallanInfo() {
		return allChallanInfo;
	}

	public void setAllChallanInfo(List<ChallanBean> allChallanInfo) {
		this.allChallanInfo = allChallanInfo;
	}

	public ChallanBean getChallanDetails() {
		return challanDetails;
	}

	public void setChallanDetails(ChallanBean challanDetails) {
		this.challanDetails = challanDetails;
	}

	ChallanDAO challanDao = new ChallanDAO();

	public String serachByChallanNumber() {
		String challanNumberStr = request.getParameter("challanNumber");
		if (challanNumberStr != null) {

			challanDetails = challanDao.serachByChallanNumber(challanNumberStr);
		//	log.info("Challan List is ::" + challanDetails.getChallanNumber());

			return SUCCESS;
		} else {
			String challanNum = request.getParameter("challanNum");
			challanDetails = challanDao.serachByChallanNumber(challanNum);
			return "showView";
		}
	}

	public String allChallanInfo() {
		allChallanInfo = challanDao.allChallanInfo();
		log.info("Challan List is ::" + allChallanInfo.size());
		return SUCCESS;
	}
}
